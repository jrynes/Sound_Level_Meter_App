package com.onelouder.adlib;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import com.rabbitmq.client.AMQP;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;

class SendAdUsage extends ServerBase {
    private static final String ENDPOINT_DEV = "https://ads-au-dev.onelouder.com/mss/adUsage";
    private static final String ENDPOINT_PROD = "https://ads-au.onelouder.com/mss/adUsage";
    private static final String ENDPOINT_QA = "https://ads-au-qa.onelouder.com/mss/adUsage";
    private static final String ENDPOINT_STAGE = "https://ads-au-stage.onelouder.com/mss/adUsage";
    private static final int MAX_CACHE_SIZE = 20;
    private static final int MAX_SEND_SIZE = 40;
    private static final String TAG = "SendAdUsage";
    private static boolean send_inprocess;
    private Context mContext;
    private ArrayList<String> outbox;

    /* renamed from: com.onelouder.adlib.SendAdUsage.1 */
    static class C13101 implements Runnable {
        final /* synthetic */ Context val$context;

        C13101(Context context) {
            this.val$context = context;
        }

        public void run() {
            Diagnostics.m1951d(SendAdUsage.TAG, "sendEvents.run()");
            if (SendAdUsage.send_inprocess) {
                Diagnostics.m1957w(SendAdUsage.TAG, "sendEvents already in process");
                return;
            }
            ArrayList<String> outbox = new ArrayList();
            if (!Utils.isNetworkConnected(this.val$context)) {
                Diagnostics.m1957w(SendAdUsage.TAG, "no connectivity, cache ad events");
                AdEventsCache.getInstance(this.val$context).SaveCache(this.val$context);
            } else if (AdEventsCache.getInstance(this.val$context).size() > 0) {
                SendAdUsage.send_inprocess = true;
                for (int i = 0; i < SendAdUsage.MAX_SEND_SIZE; i++) {
                    String event = AdEventsCache.getInstance(this.val$context).getNextEvent();
                    if (event != null) {
                        outbox.add(event);
                    }
                }
            }
            Diagnostics.m1951d(SendAdUsage.TAG, "pending.size() == " + AdEventsCache.getInstance(this.val$context).size());
            Diagnostics.m1951d(SendAdUsage.TAG, "outbox.size() == " + outbox.size());
            if (outbox.size() > 0) {
                new SendAdUsage(this.val$context, outbox).run();
            }
        }
    }

    static {
        send_inprocess = false;
    }

    public static void trackEvent(Context context, AdPlacement placement, String action, HashMap<String, Object> targetParams, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"action\":");
        appendJsonValue(sb, action);
        sb.append(", \"sessionId\":");
        appendJsonValue(sb, Utils.getSESSIONID(context));
        sb.append(", \"adPlacement\":");
        appendJsonValue(sb, placement.getID());
        sb.append(", \"adProvider\":");
        appendJsonValue(sb, placement.getNetwork());
        sb.append(", \"adPublisher\":");
        appendJsonValue(sb, placement.getPubid());
        sb.append(", \"adSite\":");
        appendJsonValue(sb, placement.getSiteid());
        sb.append(", \"time\":\"");
        sb.append(new Date().getTime() / 1000);
        sb.append("\"");
        if (url != null && url.length() > 0) {
            sb.append(", \"adUrl\":");
            appendJsonValue(sb, url);
        }
        if (!action.equals("close")) {
            GeoLocation location = LocationUtils.getCachedGeoLocation(context);
            if (location != null) {
                String value = location.getLatitude();
                if (value != null && value.length() > 0) {
                    sb.append(", \"latitude\":");
                    appendJsonValue(sb, value);
                }
                value = location.getLongitude();
                if (value != null && value.length() > 0) {
                    sb.append(", \"longitude\":");
                    appendJsonValue(sb, value);
                }
            }
        }
        if (targetParams != null && targetParams.size() > 0) {
            try {
                sb.append(", \"targetParms\":{");
                StringBuilder tp = new StringBuilder();
                for (String key : targetParams.keySet()) {
                    Object o = targetParams.get(key);
                    if (o != null) {
                        if (tp.length() != 0) {
                            tp.append(Stomp.COMMA);
                        }
                        appendJsonValue(tp, key);
                        tp.append(Headers.SEPERATOR);
                        appendJsonValue(tp, o.toString());
                    }
                }
                tp.append("}");
                sb.append(tp);
            } catch (Exception e) {
            }
        }
        sb.append("}");
        AdEventsCache.getInstance(context).addEvent(sb.toString());
        boolean bSendEvents = false;
        int size = AdEventsCache.getInstance(context).size();
        Diagnostics.m1951d(TAG, "pending.size()=" + size);
        if (size >= MAX_CACHE_SIZE) {
            bSendEvents = true;
        }
        if (bSendEvents) {
            sendEvents(context);
        } else if (action != null) {
            if (action.equals("impression")) {
                AdEventsCache.getInstance(context).SaveCache(context);
            }
        }
    }

    public static void sendEvents(Context context) {
        Diagnostics.m1951d(TAG, "sendEvents");
        RunnableManager.getInstance().pushRequest(new C13101(context));
    }

    SendAdUsage(Context context, ArrayList<String> sendevents) {
        this.outbox = new ArrayList();
        try {
            this.outbox.addAll(sendevents);
            this.mContext = context;
            StringBuilder sb = new StringBuilder();
            sb.append("{\"guId\":");
            appendJsonValue(sb, Utils.getGUID(context));
            String olaId = Preferences.getSimplePref(context, "ola_id", Stomp.EMPTY);
            if (olaId.length() == 0) {
                olaId = Preferences.getSimplePref(context, "ads-product-name", Stomp.EMPTY);
            }
            sb.append(", \"olaId\":");
            appendJsonValue(sb, olaId);
            if (Preferences.getMobileConsumerId(context).length() > 0) {
                sb.append(", \"mssId\":");
                appendJsonValue(sb, Preferences.getMobileConsumerId(context));
            }
            sb.append(", \"platform\":\"Android\"");
            sb.append(", \"adSdkVersion\":");
            appendJsonValue(sb, AdView.SDK_VERSION);
            if (Preferences.getSimplePref(context, "ads-product-version", Stomp.EMPTY).length() > 0) {
                sb.append(", \"appVersion\":");
                appendJsonValue(sb, Preferences.getSimplePref(context, "ads-product-version", Stomp.EMPTY));
            }
            String value = Utils.getCarrier(context);
            if (value != null && value.length() > 0) {
                sb.append(", \"carrier\":");
                appendJsonValue(sb, value);
            }
            value = Utils.getAdvertisingId(context);
            if (value != null) {
                sb.append(", \"advertisingId\":");
                appendJsonValue(sb, value);
                sb.append(", \"limitAdTrackingEnabled\":");
                appendJsonValue(sb, Utils.getLimitAdTrackingEnabled(context));
            }
            sb.append(", \"version\":");
            appendJsonValue(sb, VERSION.SDK);
            sb.append(", \"manufacturer\":");
            appendJsonValue(sb, Build.MANUFACTURER);
            sb.append(", \"devname\":");
            appendJsonValue(sb, Build.MODEL);
            sb.append(", \"events\":[ ");
            boolean bDiagnostics = Diagnostics.getInstance().isEnabled(4);
            int requests = 0;
            int impressions = 0;
            int clicks = 0;
            int closes = 0;
            for (int i = 0; i < this.outbox.size(); i++) {
                String event = (String) this.outbox.get(i);
                if (event != null && event.length() > 0) {
                    if (i != 0) {
                        sb.append(Stomp.COMMA);
                    }
                    if (bDiagnostics) {
                        Diagnostics.m1956v(TAG, event);
                        if (event.startsWith("{\"action\":\"request\"")) {
                            requests++;
                        } else if (event.startsWith("{\"action\":\"impression\"")) {
                            impressions++;
                        } else if (event.startsWith("{\"action\":\"click\"")) {
                            clicks++;
                        } else if (event.startsWith("{\"action\":\"close\"")) {
                            closes++;
                        }
                    }
                    sb.append(event);
                }
            }
            sb.append("]}");
            if (bDiagnostics) {
                StringBuilder log = new StringBuilder();
                log.append("requests=");
                log.append(requests);
                log.append("  impressions=");
                log.append(impressions);
                log.append("  clicks=");
                log.append(clicks);
                log.append("  closes=");
                log.append(closes);
                Diagnostics.m1952e(TAG, log.toString());
            }
            String url = null;
            if (Preferences.isProdEnv(context)) {
                url = ENDPOINT_PROD;
            } else if (Preferences.isQaEnv(context)) {
                url = ENDPOINT_QA;
            } else if (Preferences.isDevEnv(context)) {
                url = ENDPOINT_DEV;
            } else if (Preferences.isStageEnv(context)) {
                url = ENDPOINT_STAGE;
            }
            if (url != null) {
                this.mUrl = url;
                this.mPostParams = sb.toString();
                this.bLogPostParams = false;
            }
        } catch (VirtualMachineError e) {
            Diagnostics.m1954e(TAG, e);
        } catch (Throwable e2) {
            Diagnostics.m1953e(TAG, e2);
        }
    }

    public String TAG() {
        return TAG;
    }

    protected String ConstructPOST() {
        return this.mPostParams;
    }

    protected void onFinished(int completionCode) {
        Diagnostics.m1951d(TAG, "onFinished");
        if (completionCode != AMQP.REPLY_SUCCESS) {
            AdEventsCache.getInstance(this.mContext).addEvent(this.outbox);
        }
        AdEventsCache.getInstance(this.mContext).SaveCache(this.mContext);
        send_inprocess = false;
    }

    private static void appendJsonValue(StringBuilder sb, String value) {
        sb.append("\"");
        sb.append(value.replace("\"", "\\\""));
        sb.append("\"");
    }
}
