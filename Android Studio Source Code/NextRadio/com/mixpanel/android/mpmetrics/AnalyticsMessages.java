package com.mixpanel.android.mpmetrics;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.tagmanager.DataLayer;
import com.mixpanel.android.mpmetrics.MPDbAdapter.Table;
import com.mixpanel.android.util.HttpService;
import com.mixpanel.android.util.RemoteService;
import com.mixpanel.android.util.RemoteService.ServiceUnavailableException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.json.JSONException;
import org.json.JSONObject;

class AnalyticsMessages {
    private static final int ENQUEUE_EVENTS = 1;
    private static final int ENQUEUE_PEOPLE = 0;
    private static final int FLUSH_QUEUE = 2;
    private static final int INSTALL_DECIDE_CHECK = 12;
    private static final int KILL_WORKER = 5;
    private static final String LOGTAG = "MixpanelAPI.Messages";
    private static final int REGISTER_FOR_GCM = 13;
    private static final Map<Context, AnalyticsMessages> sInstances;
    protected final MPConfig mConfig;
    protected final Context mContext;
    private final Worker mWorker;

    static class EventDescription {
        private final String eventName;
        private final JSONObject properties;
        private final String token;

        public EventDescription(String eventName, JSONObject properties, String token) {
            this.eventName = eventName;
            this.properties = properties;
            this.token = token;
        }

        public String getEventName() {
            return this.eventName;
        }

        public JSONObject getProperties() {
            return this.properties;
        }

        public String getToken() {
            return this.token;
        }
    }

    class Worker {
        private long mAveFlushFrequency;
        private long mFlushCount;
        private Handler mHandler;
        private final Object mHandlerLock;
        private long mLastFlushTime;
        private SystemInformation mSystemInformation;

        class AnalyticsMessageHandler extends Handler {
            private MPDbAdapter mDbAdapter;
            private final DecideChecker mDecideChecker;
            private long mDecideRetryAfter;
            private final boolean mDisableFallback;
            private int mFailedRetries;
            private final long mFlushInterval;
            private long mTrackEngageRetryAfter;

            /* renamed from: com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.AnalyticsMessageHandler.1 */
            class C10781 implements InstanceProcessor {
                final /* synthetic */ String val$registrationId;

                C10781(String str) {
                    this.val$registrationId = str;
                }

                public void process(MixpanelAPI api) {
                    if (MPConfig.DEBUG) {
                        Log.v(AnalyticsMessages.LOGTAG, "Using existing pushId " + this.val$registrationId);
                    }
                    api.getPeople().setPushRegistrationId(this.val$registrationId);
                }
            }

            public AnalyticsMessageHandler(Looper looper) {
                super(looper);
                this.mDbAdapter = null;
                this.mDecideChecker = createDecideChecker();
                this.mDisableFallback = AnalyticsMessages.this.mConfig.getDisableFallback();
                this.mFlushInterval = (long) AnalyticsMessages.this.mConfig.getFlushInterval();
                Worker.this.mSystemInformation = new SystemInformation(AnalyticsMessages.this.mContext);
            }

            protected DecideChecker createDecideChecker() {
                return new DecideChecker(AnalyticsMessages.this.mContext, AnalyticsMessages.this.mConfig);
            }

            public void handleMessage(Message msg) {
                if (this.mDbAdapter == null) {
                    this.mDbAdapter = AnalyticsMessages.this.makeDbAdapter(AnalyticsMessages.this.mContext);
                    this.mDbAdapter.cleanupEvents(System.currentTimeMillis() - ((long) AnalyticsMessages.this.mConfig.getDataExpiration()), Table.EVENTS);
                    this.mDbAdapter.cleanupEvents(System.currentTimeMillis() - ((long) AnalyticsMessages.this.mConfig.getDataExpiration()), Table.PEOPLE);
                }
                int returnCode = -3;
                try {
                    JSONObject message;
                    if (msg.what == 0) {
                        message = msg.obj;
                        AnalyticsMessages.this.logAboutMessageToMixpanel("Queuing people record for sending later");
                        AnalyticsMessages.this.logAboutMessageToMixpanel("    " + message.toString());
                        returnCode = this.mDbAdapter.addJSON(message, Table.PEOPLE);
                    } else if (msg.what == AnalyticsMessages.ENQUEUE_EVENTS) {
                        EventDescription eventDescription = msg.obj;
                        try {
                            message = prepareEventObject(eventDescription);
                            AnalyticsMessages.this.logAboutMessageToMixpanel("Queuing event for sending later");
                            AnalyticsMessages.this.logAboutMessageToMixpanel("    " + message.toString());
                            returnCode = this.mDbAdapter.addJSON(message, Table.EVENTS);
                        } catch (JSONException e) {
                            Log.e(AnalyticsMessages.LOGTAG, "Exception tracking event " + eventDescription.getEventName(), e);
                        }
                    } else if (msg.what == AnalyticsMessages.FLUSH_QUEUE) {
                        AnalyticsMessages.this.logAboutMessageToMixpanel("Flushing queue due to scheduled or forced flush");
                        Worker.this.updateFlushFrequency();
                        sendAllData(this.mDbAdapter);
                        if (SystemClock.elapsedRealtime() >= this.mDecideRetryAfter) {
                            try {
                                this.mDecideChecker.runDecideChecks(AnalyticsMessages.this.getPoster());
                            } catch (ServiceUnavailableException e2) {
                                this.mDecideRetryAfter = SystemClock.elapsedRealtime() + ((long) (e2.getRetryAfter() * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH));
                            }
                        }
                    } else if (msg.what == AnalyticsMessages.INSTALL_DECIDE_CHECK) {
                        AnalyticsMessages.this.logAboutMessageToMixpanel("Installing a check for surveys and in-app notifications");
                        this.mDecideChecker.addDecideCheck(msg.obj);
                        if (SystemClock.elapsedRealtime() >= this.mDecideRetryAfter) {
                            try {
                                this.mDecideChecker.runDecideChecks(AnalyticsMessages.this.getPoster());
                            } catch (ServiceUnavailableException e22) {
                                this.mDecideRetryAfter = SystemClock.elapsedRealtime() + ((long) (e22.getRetryAfter() * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH));
                            }
                        }
                    } else if (msg.what == AnalyticsMessages.REGISTER_FOR_GCM) {
                        runGCMRegistration(msg.obj);
                    } else if (msg.what == AnalyticsMessages.KILL_WORKER) {
                        Log.w(AnalyticsMessages.LOGTAG, "Worker received a hard kill. Dumping all events and force-killing. Thread id " + Thread.currentThread().getId());
                        synchronized (Worker.this.mHandlerLock) {
                            this.mDbAdapter.deleteDB();
                            Worker.this.mHandler = null;
                            Looper.myLooper().quit();
                        }
                    } else {
                        Log.e(AnalyticsMessages.LOGTAG, "Unexpected message received by Mixpanel worker: " + msg);
                    }
                    if ((returnCode >= AnalyticsMessages.this.mConfig.getBulkUploadLimit() || returnCode == -2) && this.mFailedRetries <= 0) {
                        AnalyticsMessages.this.logAboutMessageToMixpanel("Flushing queue due to bulk upload limit");
                        Worker.this.updateFlushFrequency();
                        sendAllData(this.mDbAdapter);
                        if (SystemClock.elapsedRealtime() >= this.mDecideRetryAfter) {
                            try {
                                this.mDecideChecker.runDecideChecks(AnalyticsMessages.this.getPoster());
                            } catch (RuntimeException e3) {
                                this.mDecideRetryAfter = SystemClock.elapsedRealtime() + ((long) (e3.getRetryAfter() * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH));
                            }
                        }
                    } else if (returnCode > 0 && !hasMessages(AnalyticsMessages.FLUSH_QUEUE)) {
                        AnalyticsMessages.this.logAboutMessageToMixpanel("Queue depth " + returnCode + " - Adding flush in " + this.mFlushInterval);
                        if (this.mFlushInterval >= 0) {
                            sendEmptyMessageDelayed(AnalyticsMessages.FLUSH_QUEUE, this.mFlushInterval);
                        }
                    }
                } catch (RuntimeException e32) {
                    Log.e(AnalyticsMessages.LOGTAG, "Worker threw an unhandled exception", e32);
                    synchronized (Worker.this.mHandlerLock) {
                    }
                    Worker.this.mHandler = null;
                    try {
                        Looper.myLooper().quit();
                        Log.e(AnalyticsMessages.LOGTAG, "Mixpanel will not process any more analytics messages", e32);
                    } catch (Exception tooLate) {
                        Log.e(AnalyticsMessages.LOGTAG, "Could not halt looper", tooLate);
                    }
                }
            }

            private void runGCMRegistration(String senderID) {
                try {
                    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(AnalyticsMessages.this.mContext) != 0) {
                        Log.i(AnalyticsMessages.LOGTAG, "Can't register for push notifications, Google Play Services are not installed.");
                        return;
                    }
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(AnalyticsMessages.this.mContext);
                    String[] strArr = new String[AnalyticsMessages.ENQUEUE_EVENTS];
                    strArr[AnalyticsMessages.ENQUEUE_PEOPLE] = senderID;
                    MixpanelAPI.allInstances(new C10781(gcm.register(strArr)));
                } catch (RuntimeException e) {
                    try {
                        Log.i(AnalyticsMessages.LOGTAG, "Can't register for push notifications, Google Play services are not configured.");
                    } catch (IOException e2) {
                        Log.i(AnalyticsMessages.LOGTAG, "Exception when trying to register for GCM", e2);
                    } catch (NoClassDefFoundError e3) {
                        Log.w(AnalyticsMessages.LOGTAG, "Google play services were not part of this build, push notifications cannot be registered or delivered");
                    }
                }
            }

            private void sendAllData(MPDbAdapter dbAdapter) {
                if (!AnalyticsMessages.this.getPoster().isOnline(AnalyticsMessages.this.mContext)) {
                    AnalyticsMessages.this.logAboutMessageToMixpanel("Not flushing data to Mixpanel because the device is not connected to the internet.");
                } else if (this.mDisableFallback) {
                    r1 = Table.EVENTS;
                    r2 = new String[AnalyticsMessages.ENQUEUE_EVENTS];
                    r2[AnalyticsMessages.ENQUEUE_PEOPLE] = AnalyticsMessages.this.mConfig.getEventsEndpoint();
                    sendData(dbAdapter, r1, r2);
                    r1 = Table.PEOPLE;
                    r2 = new String[AnalyticsMessages.ENQUEUE_EVENTS];
                    r2[AnalyticsMessages.ENQUEUE_PEOPLE] = AnalyticsMessages.this.mConfig.getPeopleEndpoint();
                    sendData(dbAdapter, r1, r2);
                } else {
                    r1 = Table.EVENTS;
                    r2 = new String[AnalyticsMessages.FLUSH_QUEUE];
                    r2[AnalyticsMessages.ENQUEUE_PEOPLE] = AnalyticsMessages.this.mConfig.getEventsEndpoint();
                    r2[AnalyticsMessages.ENQUEUE_EVENTS] = AnalyticsMessages.this.mConfig.getEventsFallbackEndpoint();
                    sendData(dbAdapter, r1, r2);
                    r1 = Table.PEOPLE;
                    r2 = new String[AnalyticsMessages.FLUSH_QUEUE];
                    r2[AnalyticsMessages.ENQUEUE_PEOPLE] = AnalyticsMessages.this.mConfig.getPeopleEndpoint();
                    r2[AnalyticsMessages.ENQUEUE_EVENTS] = AnalyticsMessages.this.mConfig.getPeopleFallbackEndpoint();
                    sendData(dbAdapter, r1, r2);
                }
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            private void sendData(com.mixpanel.android.mpmetrics.MPDbAdapter r25, com.mixpanel.android.mpmetrics.MPDbAdapter.Table r26, java.lang.String[] r27) {
                /*
                r24 = this;
                r0 = r24;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r19 = r0;
                r0 = r19;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;
                r19 = r0;
                r14 = r19.getPoster();
                r8 = r25.generateDataString(r26);
                if (r8 == 0) goto L_0x00ad;
            L_0x0016:
                r19 = 0;
                r10 = r8[r19];
                r19 = 1;
                r15 = r8[r19];
                r7 = com.mixpanel.android.util.Base64Coder.encodeString(r15);
                r12 = new java.util.HashMap;
                r12.<init>();
                r19 = "data";
                r0 = r19;
                r12.put(r0, r7);
                r19 = com.mixpanel.android.mpmetrics.MPConfig.DEBUG;
                if (r19 == 0) goto L_0x003d;
            L_0x0032:
                r19 = "verbose";
                r20 = "1";
                r0 = r19;
                r1 = r20;
                r12.put(r0, r1);
            L_0x003d:
                r5 = 1;
                r4 = r27;
                r11 = r4.length;
                r9 = 0;
            L_0x0042:
                if (r9 >= r11) goto L_0x0093;
            L_0x0044:
                r18 = r4[r9];
                r0 = r24;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = r0;
                r0 = r19;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = r0;
                r0 = r19;
                r0 = r0.mConfig;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = r0;
                r17 = r19.getSSLSocketFactory();	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r0 = r18;
                r1 = r17;
                r16 = r14.performRequest(r0, r12, r1);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r5 = 1;
                if (r16 != 0) goto L_0x00ae;
            L_0x0067:
                r0 = r24;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = r0;
                r0 = r19;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = r0;
                r20 = new java.lang.StringBuilder;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r20.<init>();	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r21 = "Response was null, unexpected failure posting to ";
                r20 = r20.append(r21);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r0 = r20;
                r1 = r18;
                r20 = r0.append(r1);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r21 = ".";
                r20 = r20.append(r21);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r20 = r20.toString();	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19.logAboutMessageToMixpanel(r20);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
            L_0x0093:
                if (r5 == 0) goto L_0x023a;
            L_0x0095:
                r0 = r24;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r19 = r0;
                r0 = r19;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;
                r19 = r0;
                r20 = "Not retrying this batch of events, deleting them from DB.";
                r19.logAboutMessageToMixpanel(r20);
                r0 = r25;
                r1 = r26;
                r0.cleanupEvents(r10, r1);
            L_0x00ad:
                return;
            L_0x00ae:
                r13 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x0153 }
                r19 = "UTF-8";
                r0 = r16;
                r1 = r19;
                r13.<init>(r0, r1);	 Catch:{ UnsupportedEncodingException -> 0x0153 }
                r0 = r24;
                r0 = r0.mFailedRetries;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = r0;
                if (r19 <= 0) goto L_0x00d2;
            L_0x00c1:
                r19 = 0;
                r0 = r19;
                r1 = r24;
                r1.mFailedRetries = r0;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = 2;
                r0 = r24;
                r1 = r19;
                r0.removeMessages(r1);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
            L_0x00d2:
                r0 = r24;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = r0;
                r0 = r19;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = r0;
                r20 = new java.lang.StringBuilder;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r20.<init>();	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r21 = "Successfully posted to ";
                r20 = r20.append(r21);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r0 = r20;
                r1 = r18;
                r20 = r0.append(r1);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r21 = ": \n";
                r20 = r20.append(r21);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r0 = r20;
                r20 = r0.append(r15);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r20 = r20.toString();	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19.logAboutMessageToMixpanel(r20);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r0 = r24;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = r0;
                r0 = r19;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19 = r0;
                r20 = new java.lang.StringBuilder;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r20.<init>();	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r21 = "Response was ";
                r20 = r20.append(r21);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r0 = r20;
                r20 = r0.append(r13);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r20 = r20.toString();	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r19.logAboutMessageToMixpanel(r20);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                goto L_0x0093;
            L_0x012a:
                r6 = move-exception;
                r19 = "MixpanelAPI.Messages";
                r20 = new java.lang.StringBuilder;
                r20.<init>();
                r21 = "Out of memory when posting to ";
                r20 = r20.append(r21);
                r0 = r20;
                r1 = r18;
                r20 = r0.append(r1);
                r21 = ".";
                r20 = r20.append(r21);
                r20 = r20.toString();
                r0 = r19;
                r1 = r20;
                android.util.Log.e(r0, r1, r6);
                goto L_0x0093;
            L_0x0153:
                r6 = move-exception;
                r19 = new java.lang.RuntimeException;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                r20 = "UTF not supported on this platform?";
                r0 = r19;
                r1 = r20;
                r0.<init>(r1, r6);	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
                throw r19;	 Catch:{ OutOfMemoryError -> 0x012a, MalformedURLException -> 0x0160, ServiceUnavailableException -> 0x0189, SocketTimeoutException -> 0x01d4, IOException -> 0x0207 }
            L_0x0160:
                r6 = move-exception;
                r19 = "MixpanelAPI.Messages";
                r20 = new java.lang.StringBuilder;
                r20.<init>();
                r21 = "Cannot interpret ";
                r20 = r20.append(r21);
                r0 = r20;
                r1 = r18;
                r20 = r0.append(r1);
                r21 = " as a URL.";
                r20 = r20.append(r21);
                r20 = r20.toString();
                r0 = r19;
                r1 = r20;
                android.util.Log.e(r0, r1, r6);
                goto L_0x0093;
            L_0x0189:
                r6 = move-exception;
                r0 = r24;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r19 = r0;
                r0 = r19;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;
                r19 = r0;
                r20 = new java.lang.StringBuilder;
                r20.<init>();
                r21 = "Cannot post message to ";
                r20 = r20.append(r21);
                r0 = r20;
                r1 = r18;
                r20 = r0.append(r1);
                r21 = ".";
                r20 = r20.append(r21);
                r20 = r20.toString();
                r0 = r19;
                r1 = r20;
                r0.logAboutMessageToMixpanel(r1, r6);
                r5 = 0;
                r19 = r6.getRetryAfter();
                r0 = r19;
                r0 = r0 * 1000;
                r19 = r0;
                r0 = r19;
                r0 = (long) r0;
                r20 = r0;
                r0 = r20;
                r2 = r24;
                r2.mTrackEngageRetryAfter = r0;
            L_0x01d0:
                r9 = r9 + 1;
                goto L_0x0042;
            L_0x01d4:
                r6 = move-exception;
                r0 = r24;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r19 = r0;
                r0 = r19;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;
                r19 = r0;
                r20 = new java.lang.StringBuilder;
                r20.<init>();
                r21 = "Cannot post message to ";
                r20 = r20.append(r21);
                r0 = r20;
                r1 = r18;
                r20 = r0.append(r1);
                r21 = ".";
                r20 = r20.append(r21);
                r20 = r20.toString();
                r0 = r19;
                r1 = r20;
                r0.logAboutMessageToMixpanel(r1, r6);
                r5 = 0;
                goto L_0x01d0;
            L_0x0207:
                r6 = move-exception;
                r0 = r24;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r19 = r0;
                r0 = r19;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;
                r19 = r0;
                r20 = new java.lang.StringBuilder;
                r20.<init>();
                r21 = "Cannot post message to ";
                r20 = r20.append(r21);
                r0 = r20;
                r1 = r18;
                r20 = r0.append(r1);
                r21 = ".";
                r20 = r20.append(r21);
                r20 = r20.toString();
                r0 = r19;
                r1 = r20;
                r0.logAboutMessageToMixpanel(r1, r6);
                r5 = 0;
                goto L_0x01d0;
            L_0x023a:
                r19 = 2;
                r0 = r24;
                r1 = r19;
                r0.removeMessages(r1);
                r20 = 4611686018427387904; // 0x4000000000000000 float:0.0 double:2.0;
                r0 = r24;
                r0 = r0.mFailedRetries;
                r19 = r0;
                r0 = r19;
                r0 = (double) r0;
                r22 = r0;
                r20 = java.lang.Math.pow(r20, r22);
                r0 = r20;
                r0 = (long) r0;
                r20 = r0;
                r22 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
                r20 = r20 * r22;
                r0 = r24;
                r0 = r0.mTrackEngageRetryAfter;
                r22 = r0;
                r20 = java.lang.Math.max(r20, r22);
                r0 = r20;
                r2 = r24;
                r2.mTrackEngageRetryAfter = r0;
                r0 = r24;
                r0 = r0.mTrackEngageRetryAfter;
                r20 = r0;
                r22 = 600000; // 0x927c0 float:8.40779E-40 double:2.964394E-318;
                r20 = java.lang.Math.min(r20, r22);
                r0 = r20;
                r2 = r24;
                r2.mTrackEngageRetryAfter = r0;
                r19 = 2;
                r0 = r24;
                r0 = r0.mTrackEngageRetryAfter;
                r20 = r0;
                r0 = r24;
                r1 = r19;
                r2 = r20;
                r0.sendEmptyMessageDelayed(r1, r2);
                r0 = r24;
                r0 = r0.mFailedRetries;
                r19 = r0;
                r19 = r19 + 1;
                r0 = r19;
                r1 = r24;
                r1.mFailedRetries = r0;
                r0 = r24;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r19 = r0;
                r0 = r19;
                r0 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;
                r19 = r0;
                r20 = new java.lang.StringBuilder;
                r20.<init>();
                r21 = "Retrying this batch of events in ";
                r20 = r20.append(r21);
                r0 = r24;
                r0 = r0.mTrackEngageRetryAfter;
                r22 = r0;
                r0 = r20;
                r1 = r22;
                r20 = r0.append(r1);
                r21 = " ms";
                r20 = r20.append(r21);
                r20 = r20.toString();
                r19.logAboutMessageToMixpanel(r20);
                goto L_0x00ad;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.AnalyticsMessageHandler.sendData(com.mixpanel.android.mpmetrics.MPDbAdapter, com.mixpanel.android.mpmetrics.MPDbAdapter$Table, java.lang.String[]):void");
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            private org.json.JSONObject getDefaultEventProperties() throws org.json.JSONException {
                /*
                r14 = this;
                r10 = new org.json.JSONObject;
                r10.<init>();
                r12 = "mp_lib";
                r13 = "android";
                r10.put(r12, r13);
                r12 = "$lib_version";
                r13 = "4.8.0";
                r10.put(r12, r13);
                r12 = "$os";
                r13 = "Android";
                r10.put(r12, r13);
                r13 = "$os_version";
                r12 = android.os.Build.VERSION.RELEASE;
                if (r12 != 0) goto L_0x0119;
            L_0x0020:
                r12 = "UNKNOWN";
            L_0x0022:
                r10.put(r13, r12);
                r13 = "$manufacturer";
                r12 = android.os.Build.MANUFACTURER;
                if (r12 != 0) goto L_0x011d;
            L_0x002b:
                r12 = "UNKNOWN";
            L_0x002d:
                r10.put(r13, r12);
                r13 = "$brand";
                r12 = android.os.Build.BRAND;
                if (r12 != 0) goto L_0x0121;
            L_0x0036:
                r12 = "UNKNOWN";
            L_0x0038:
                r10.put(r13, r12);
                r13 = "$model";
                r12 = android.os.Build.MODEL;
                if (r12 != 0) goto L_0x0125;
            L_0x0041:
                r12 = "UNKNOWN";
            L_0x0043:
                r10.put(r13, r12);
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;	 Catch:{ RuntimeException -> 0x0132 }
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.this;	 Catch:{ RuntimeException -> 0x0132 }
                r12 = r12.mContext;	 Catch:{ RuntimeException -> 0x0132 }
                r11 = com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable(r12);	 Catch:{ RuntimeException -> 0x0132 }
                switch(r11) {
                    case 0: goto L_0x0129;
                    case 1: goto L_0x0146;
                    case 2: goto L_0x014f;
                    case 3: goto L_0x0158;
                    case 4: goto L_0x0053;
                    case 5: goto L_0x0053;
                    case 6: goto L_0x0053;
                    case 7: goto L_0x0053;
                    case 8: goto L_0x0053;
                    case 9: goto L_0x0161;
                    default: goto L_0x0053;
                };
            L_0x0053:
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r12 = r12.mSystemInformation;
                r4 = r12.getDisplayMetrics();
                r12 = "$screen_dpi";
                r13 = r4.densityDpi;
                r10.put(r12, r13);
                r12 = "$screen_height";
                r13 = r4.heightPixels;
                r10.put(r12, r13);
                r12 = "$screen_width";
                r13 = r4.widthPixels;
                r10.put(r12, r13);
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r12 = r12.mSystemInformation;
                r1 = r12.getAppVersionName();
                if (r1 == 0) goto L_0x0088;
            L_0x007e:
                r12 = "$app_version";
                r10.put(r12, r1);
                r12 = "$app_version_string";
                r10.put(r12, r1);
            L_0x0088:
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r12 = r12.mSystemInformation;
                r0 = r12.getAppVersionCode();
                if (r0 == 0) goto L_0x009e;
            L_0x0094:
                r12 = "$app_release";
                r10.put(r12, r0);
                r12 = "$app_build_number";
                r10.put(r12, r0);
            L_0x009e:
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r12 = r12.mSystemInformation;
                r12 = r12.hasNFC();
                r6 = java.lang.Boolean.valueOf(r12);
                if (r6 == 0) goto L_0x00b7;
            L_0x00ae:
                r12 = "$has_nfc";
                r13 = r6.booleanValue();
                r10.put(r12, r13);
            L_0x00b7:
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r12 = r12.mSystemInformation;
                r12 = r12.hasTelephony();
                r7 = java.lang.Boolean.valueOf(r12);
                if (r7 == 0) goto L_0x00d0;
            L_0x00c7:
                r12 = "$has_telephone";
                r13 = r7.booleanValue();
                r10.put(r12, r13);
            L_0x00d0:
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r12 = r12.mSystemInformation;
                r3 = r12.getCurrentNetworkOperator();
                if (r3 == 0) goto L_0x00e1;
            L_0x00dc:
                r12 = "$carrier";
                r10.put(r12, r3);
            L_0x00e1:
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r12 = r12.mSystemInformation;
                r9 = r12.isWifiConnected();
                if (r9 == 0) goto L_0x00f6;
            L_0x00ed:
                r12 = "$wifi";
                r13 = r9.booleanValue();
                r10.put(r12, r13);
            L_0x00f6:
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r12 = r12.mSystemInformation;
                r8 = r12.isBluetoothEnabled();
                if (r8 == 0) goto L_0x0107;
            L_0x0102:
                r12 = "$bluetooth_enabled";
                r10.put(r12, r8);
            L_0x0107:
                r12 = com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.this;
                r12 = r12.mSystemInformation;
                r2 = r12.getBluetoothVersion();
                if (r2 == 0) goto L_0x0118;
            L_0x0113:
                r12 = "$bluetooth_version";
                r10.put(r12, r2);
            L_0x0118:
                return r10;
            L_0x0119:
                r12 = android.os.Build.VERSION.RELEASE;
                goto L_0x0022;
            L_0x011d:
                r12 = android.os.Build.MANUFACTURER;
                goto L_0x002d;
            L_0x0121:
                r12 = android.os.Build.BRAND;
                goto L_0x0038;
            L_0x0125:
                r12 = android.os.Build.MODEL;
                goto L_0x0043;
            L_0x0129:
                r12 = "$google_play_services";
                r13 = "available";
                r10.put(r12, r13);	 Catch:{ RuntimeException -> 0x0132 }
                goto L_0x0053;
            L_0x0132:
                r5 = move-exception;
                r12 = "$google_play_services";
                r13 = "not configured";
                r10.put(r12, r13);	 Catch:{ NoClassDefFoundError -> 0x013c }
                goto L_0x0053;
            L_0x013c:
                r5 = move-exception;
                r12 = "$google_play_services";
                r13 = "not included";
                r10.put(r12, r13);
                goto L_0x0053;
            L_0x0146:
                r12 = "$google_play_services";
                r13 = "missing";
                r10.put(r12, r13);	 Catch:{ RuntimeException -> 0x0132 }
                goto L_0x0053;
            L_0x014f:
                r12 = "$google_play_services";
                r13 = "out of date";
                r10.put(r12, r13);	 Catch:{ RuntimeException -> 0x0132 }
                goto L_0x0053;
            L_0x0158:
                r12 = "$google_play_services";
                r13 = "disabled";
                r10.put(r12, r13);	 Catch:{ RuntimeException -> 0x0132 }
                goto L_0x0053;
            L_0x0161:
                r12 = "$google_play_services";
                r13 = "invalid";
                r10.put(r12, r13);	 Catch:{ RuntimeException -> 0x0132 }
                goto L_0x0053;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.mpmetrics.AnalyticsMessages.Worker.AnalyticsMessageHandler.getDefaultEventProperties():org.json.JSONObject");
            }

            private JSONObject prepareEventObject(EventDescription eventDescription) throws JSONException {
                JSONObject eventObj = new JSONObject();
                JSONObject eventProperties = eventDescription.getProperties();
                JSONObject sendProperties = getDefaultEventProperties();
                sendProperties.put("token", eventDescription.getToken());
                if (eventProperties != null) {
                    Iterator<?> iter = eventProperties.keys();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        sendProperties.put(key, eventProperties.get(key));
                    }
                }
                eventObj.put(DataLayer.EVENT_KEY, eventDescription.getEventName());
                eventObj.put("properties", sendProperties);
                return eventObj;
            }
        }

        public Worker() {
            this.mHandlerLock = new Object();
            this.mFlushCount = 0;
            this.mAveFlushFrequency = 0;
            this.mLastFlushTime = -1;
            this.mHandler = restartWorkerThread();
        }

        public boolean isDead() {
            boolean z;
            synchronized (this.mHandlerLock) {
                z = this.mHandler == null;
            }
            return z;
        }

        public void runMessage(Message msg) {
            synchronized (this.mHandlerLock) {
                if (this.mHandler == null) {
                    AnalyticsMessages.this.logAboutMessageToMixpanel("Dead mixpanel worker dropping a message: " + msg.what);
                } else {
                    this.mHandler.sendMessage(msg);
                }
            }
        }

        protected Handler restartWorkerThread() {
            HandlerThread thread = new HandlerThread("com.mixpanel.android.AnalyticsWorker", AnalyticsMessages.ENQUEUE_EVENTS);
            thread.start();
            return new AnalyticsMessageHandler(thread.getLooper());
        }

        private void updateFlushFrequency() {
            long now = System.currentTimeMillis();
            long newFlushCount = this.mFlushCount + 1;
            if (this.mLastFlushTime > 0) {
                this.mAveFlushFrequency = ((now - this.mLastFlushTime) + (this.mAveFlushFrequency * this.mFlushCount)) / newFlushCount;
                AnalyticsMessages.this.logAboutMessageToMixpanel("Average send frequency approximately " + (this.mAveFlushFrequency / 1000) + " seconds.");
            }
            this.mLastFlushTime = now;
            this.mFlushCount = newFlushCount;
        }
    }

    AnalyticsMessages(Context context) {
        this.mContext = context;
        this.mConfig = getConfig(context);
        this.mWorker = createWorker();
        getPoster().checkIsMixpanelBlocked();
    }

    protected Worker createWorker() {
        return new Worker();
    }

    public static AnalyticsMessages getInstance(Context messageContext) {
        AnalyticsMessages ret;
        synchronized (sInstances) {
            Context appContext = messageContext.getApplicationContext();
            if (sInstances.containsKey(appContext)) {
                ret = (AnalyticsMessages) sInstances.get(appContext);
            } else {
                ret = new AnalyticsMessages(appContext);
                sInstances.put(appContext, ret);
            }
        }
        return ret;
    }

    public void eventsMessage(EventDescription eventDescription) {
        Message m = Message.obtain();
        m.what = ENQUEUE_EVENTS;
        m.obj = eventDescription;
        this.mWorker.runMessage(m);
    }

    public void peopleMessage(JSONObject peopleJson) {
        Message m = Message.obtain();
        m.what = ENQUEUE_PEOPLE;
        m.obj = peopleJson;
        this.mWorker.runMessage(m);
    }

    public void postToServer() {
        Message m = Message.obtain();
        m.what = FLUSH_QUEUE;
        this.mWorker.runMessage(m);
    }

    public void installDecideCheck(DecideMessages check) {
        Message m = Message.obtain();
        m.what = INSTALL_DECIDE_CHECK;
        m.obj = check;
        this.mWorker.runMessage(m);
    }

    public void registerForGCM(String senderID) {
        Message m = Message.obtain();
        m.what = REGISTER_FOR_GCM;
        m.obj = senderID;
        this.mWorker.runMessage(m);
    }

    public void hardKill() {
        Message m = Message.obtain();
        m.what = KILL_WORKER;
        this.mWorker.runMessage(m);
    }

    boolean isDead() {
        return this.mWorker.isDead();
    }

    protected MPDbAdapter makeDbAdapter(Context context) {
        return new MPDbAdapter(context);
    }

    protected MPConfig getConfig(Context context) {
        return MPConfig.getInstance(context);
    }

    protected RemoteService getPoster() {
        return new HttpService();
    }

    private void logAboutMessageToMixpanel(String message) {
        if (MPConfig.DEBUG) {
            Log.v(LOGTAG, message + " (Thread " + Thread.currentThread().getId() + ")");
        }
    }

    private void logAboutMessageToMixpanel(String message, Throwable e) {
        if (MPConfig.DEBUG) {
            Log.v(LOGTAG, message + " (Thread " + Thread.currentThread().getId() + ")", e);
        }
    }

    static {
        sInstances = new HashMap();
    }
}
