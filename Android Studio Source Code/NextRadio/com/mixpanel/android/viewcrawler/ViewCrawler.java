package com.mixpanel.android.viewcrawler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import com.mixpanel.android.mpmetrics.MPConfig;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.mixpanel.android.mpmetrics.ResourceIds;
import com.mixpanel.android.mpmetrics.ResourceReader.Ids;
import com.mixpanel.android.mpmetrics.SuperPropertyUpdate;
import com.mixpanel.android.mpmetrics.Tweaks;
import com.mixpanel.android.mpmetrics.Tweaks.OnTweakDeclaredListener;
import com.mixpanel.android.util.ImageStore;
import com.mixpanel.android.util.JSONUtils;
import com.mixpanel.android.viewcrawler.EditProtocol.BadInstructionsException;
import com.mixpanel.android.viewcrawler.EditProtocol.CantGetEditAssetsException;
import com.mixpanel.android.viewcrawler.EditProtocol.Edit;
import com.mixpanel.android.viewcrawler.EditProtocol.InapplicableInstructionsException;
import com.mixpanel.android.viewcrawler.EditorConnection.EditorConnectionException;
import com.mixpanel.android.viewcrawler.FlipGesture.OnFlipGestureListener;
import com.mixpanel.android.viewcrawler.ViewVisitor.LayoutErrorMessage;
import com.mixpanel.android.viewcrawler.ViewVisitor.OnLayoutErrorListener;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@TargetApi(16)
public class ViewCrawler implements UpdatesFromMixpanel, TrackingDebug, OnLayoutErrorListener {
    private static final int EMULATOR_CONNECT_ATTEMPT_INTERVAL_MILLIS = 30000;
    private static final String LOGTAG = "MixpanelAPI.ViewCrawler";
    private static final int MESSAGE_CONNECT_TO_EDITOR = 1;
    private static final int MESSAGE_EVENT_BINDINGS_RECEIVED = 5;
    private static final int MESSAGE_HANDLE_EDITOR_BINDINGS_RECEIVED = 6;
    private static final int MESSAGE_HANDLE_EDITOR_CHANGES_CLEARED = 10;
    private static final int MESSAGE_HANDLE_EDITOR_CHANGES_RECEIVED = 3;
    private static final int MESSAGE_HANDLE_EDITOR_CLOSED = 8;
    private static final int MESSAGE_HANDLE_EDITOR_TWEAKS_RECEIVED = 11;
    private static final int MESSAGE_INITIALIZE_CHANGES = 0;
    private static final int MESSAGE_SEND_DEVICE_INFO = 4;
    private static final int MESSAGE_SEND_EVENT_TRACKED = 7;
    private static final int MESSAGE_SEND_LAYOUT_ERROR = 12;
    private static final int MESSAGE_SEND_STATE_FOR_EDITING = 2;
    private static final int MESSAGE_VARIANTS_RECEIVED = 9;
    private static final String SHARED_PREF_BINDINGS_KEY = "mixpanel.viewcrawler.bindings";
    private static final String SHARED_PREF_CHANGES_KEY = "mixpanel.viewcrawler.changes";
    private static final String SHARED_PREF_EDITS_FILE = "mixpanel.viewcrawler.changes";
    private final MPConfig mConfig;
    private final Context mContext;
    private final Map<String, String> mDeviceInfo;
    private final DynamicEventTracker mDynamicEventTracker;
    private final EditState mEditState;
    private final ViewCrawlerHandler mMessageThreadHandler;
    private final MixpanelAPI mMixpanel;
    private final float mScaledDensity;
    private final Tweaks mTweaks;

    /* renamed from: com.mixpanel.android.viewcrawler.ViewCrawler.1 */
    class C11321 implements OnTweakDeclaredListener {
        C11321() {
        }

        public void onTweakDeclared() {
            ViewCrawler.this.mMessageThreadHandler.sendMessage(ViewCrawler.this.mMessageThreadHandler.obtainMessage(ViewCrawler.MESSAGE_SEND_DEVICE_INFO));
        }
    }

    private class Editor implements com.mixpanel.android.viewcrawler.EditorConnection.Editor {
        private Editor() {
        }

        public void sendSnapshot(JSONObject message) {
            Message msg = ViewCrawler.this.mMessageThreadHandler.obtainMessage(ViewCrawler.MESSAGE_SEND_STATE_FOR_EDITING);
            msg.obj = message;
            ViewCrawler.this.mMessageThreadHandler.sendMessage(msg);
        }

        public void performEdit(JSONObject message) {
            Message msg = ViewCrawler.this.mMessageThreadHandler.obtainMessage(ViewCrawler.MESSAGE_HANDLE_EDITOR_CHANGES_RECEIVED);
            msg.obj = message;
            ViewCrawler.this.mMessageThreadHandler.sendMessage(msg);
        }

        public void clearEdits(JSONObject message) {
            Message msg = ViewCrawler.this.mMessageThreadHandler.obtainMessage(ViewCrawler.MESSAGE_HANDLE_EDITOR_CHANGES_CLEARED);
            msg.obj = message;
            ViewCrawler.this.mMessageThreadHandler.sendMessage(msg);
        }

        public void setTweaks(JSONObject message) {
            Message msg = ViewCrawler.this.mMessageThreadHandler.obtainMessage(ViewCrawler.MESSAGE_HANDLE_EDITOR_TWEAKS_RECEIVED);
            msg.obj = message;
            ViewCrawler.this.mMessageThreadHandler.sendMessage(msg);
        }

        public void bindEvents(JSONObject message) {
            Message msg = ViewCrawler.this.mMessageThreadHandler.obtainMessage(ViewCrawler.MESSAGE_HANDLE_EDITOR_BINDINGS_RECEIVED);
            msg.obj = message;
            ViewCrawler.this.mMessageThreadHandler.sendMessage(msg);
        }

        public void sendDeviceInfo() {
            ViewCrawler.this.mMessageThreadHandler.sendMessage(ViewCrawler.this.mMessageThreadHandler.obtainMessage(ViewCrawler.MESSAGE_SEND_DEVICE_INFO));
        }

        public void cleanup() {
            ViewCrawler.this.mMessageThreadHandler.sendMessage(ViewCrawler.this.mMessageThreadHandler.obtainMessage(ViewCrawler.MESSAGE_HANDLE_EDITOR_CLOSED));
        }
    }

    private class EmulatorConnector implements Runnable {
        private volatile boolean mStopped;

        public EmulatorConnector() {
            this.mStopped = true;
        }

        public void run() {
            if (!this.mStopped) {
                ViewCrawler.this.mMessageThreadHandler.sendMessage(ViewCrawler.this.mMessageThreadHandler.obtainMessage(ViewCrawler.MESSAGE_CONNECT_TO_EDITOR));
            }
            ViewCrawler.this.mMessageThreadHandler.postDelayed(this, 30000);
        }

        public void start() {
            this.mStopped = false;
            ViewCrawler.this.mMessageThreadHandler.post(this);
        }

        public void stop() {
            this.mStopped = true;
            ViewCrawler.this.mMessageThreadHandler.removeCallbacks(this);
        }
    }

    private class LifecycleCallbacks implements ActivityLifecycleCallbacks, OnFlipGestureListener {
        private final EmulatorConnector mEmulatorConnector;
        private final FlipGesture mFlipGesture;

        public LifecycleCallbacks() {
            this.mFlipGesture = new FlipGesture(this);
            this.mEmulatorConnector = new EmulatorConnector();
        }

        public void onFlipGesture() {
            ViewCrawler.this.mMessageThreadHandler.sendMessage(ViewCrawler.this.mMessageThreadHandler.obtainMessage(ViewCrawler.MESSAGE_CONNECT_TO_EDITOR));
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
            installConnectionSensor();
            ViewCrawler.this.mEditState.add(activity);
        }

        public void onActivityPaused(Activity activity) {
            ViewCrawler.this.mEditState.remove(activity);
            if (ViewCrawler.this.mEditState.isEmpty()) {
                uninstallConnectionSensor(activity);
            }
        }

        public void onActivityStopped(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
        }

        private void installConnectionSensor() {
            if (isInEmulator() && !ViewCrawler.this.mConfig.getDisableEmulatorBindingUI()) {
                this.mEmulatorConnector.start();
            } else if (!ViewCrawler.this.mConfig.getDisableGestureBindingUI()) {
                SensorManager sensorManager = (SensorManager) ViewCrawler.this.mContext.getSystemService("sensor");
                sensorManager.registerListener(this.mFlipGesture, sensorManager.getDefaultSensor(ViewCrawler.MESSAGE_CONNECT_TO_EDITOR), ViewCrawler.MESSAGE_HANDLE_EDITOR_CHANGES_RECEIVED);
            }
        }

        private void uninstallConnectionSensor(Activity activity) {
            if (isInEmulator() && !ViewCrawler.this.mConfig.getDisableEmulatorBindingUI()) {
                this.mEmulatorConnector.stop();
            } else if (!ViewCrawler.this.mConfig.getDisableGestureBindingUI()) {
                ((SensorManager) activity.getSystemService("sensor")).unregisterListener(this.mFlipGesture);
            }
        }

        private boolean isInEmulator() {
            if (Build.HARDWARE.equals("goldfish") && Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") && Build.PRODUCT.contains(CommonUtils.SDK) && Build.MODEL.toLowerCase(Locale.US).contains(CommonUtils.SDK)) {
                return true;
            }
            return false;
        }
    }

    private static class VariantChange {
        public final String activityName;
        public final JSONObject change;
        public final Pair<Integer, Integer> variantId;

        public VariantChange(String anActivityName, JSONObject someChange, Pair<Integer, Integer> aVariantId) {
            this.activityName = anActivityName;
            this.change = someChange;
            this.variantId = aVariantId;
        }
    }

    private static class VariantTweak {
        public final JSONObject tweak;
        public final Pair<Integer, Integer> variantId;

        public VariantTweak(JSONObject aTweak, Pair<Integer, Integer> aVariantId) {
            this.tweak = aTweak;
            this.variantId = aVariantId;
        }
    }

    private class ViewCrawlerHandler extends Handler {
        private final Context mContext;
        private final List<String> mEditorAssetUrls;
        private final Map<String, Pair<String, JSONObject>> mEditorChanges;
        private EditorConnection mEditorConnection;
        private final List<Pair<String, JSONObject>> mEditorEventBindings;
        private final List<JSONObject> mEditorTweaks;
        private final ImageStore mImageStore;
        private final List<VariantChange> mPersistentChanges;
        private final List<Pair<String, JSONObject>> mPersistentEventBindings;
        private final List<VariantTweak> mPersistentTweaks;
        private final EditProtocol mProtocol;
        private final Set<Pair<Integer, Integer>> mSeenExperiments;
        private ViewSnapshot mSnapshot;
        private final Lock mStartLock;
        private final String mToken;

        /* renamed from: com.mixpanel.android.viewcrawler.ViewCrawler.ViewCrawlerHandler.1 */
        class C11331 implements SuperPropertyUpdate {
            final /* synthetic */ JSONObject val$variantObject;

            C11331(JSONObject jSONObject) {
                this.val$variantObject = jSONObject;
            }

            public JSONObject update(JSONObject in) {
                try {
                    in.put("$experiments", this.val$variantObject);
                } catch (JSONException e) {
                    Log.wtf(ViewCrawler.LOGTAG, "Can't write $experiments super property", e);
                }
                return in;
            }
        }

        public ViewCrawlerHandler(Context context, String token, Looper looper, OnLayoutErrorListener layoutErrorListener) {
            super(looper);
            this.mContext = context;
            this.mToken = token;
            this.mSnapshot = null;
            String resourcePackage = ViewCrawler.this.mConfig.getResourcePackageName();
            if (resourcePackage == null) {
                resourcePackage = context.getPackageName();
            }
            ResourceIds resourceIds = new Ids(resourcePackage, context);
            this.mImageStore = new ImageStore(context, "ViewCrawler");
            this.mProtocol = new EditProtocol(resourceIds, this.mImageStore, layoutErrorListener);
            this.mEditorChanges = new HashMap();
            this.mEditorTweaks = new ArrayList();
            this.mEditorAssetUrls = new ArrayList();
            this.mEditorEventBindings = new ArrayList();
            this.mPersistentChanges = new ArrayList();
            this.mPersistentTweaks = new ArrayList();
            this.mPersistentEventBindings = new ArrayList();
            this.mSeenExperiments = new HashSet();
            this.mStartLock = new ReentrantLock();
            this.mStartLock.lock();
        }

        public void start() {
            this.mStartLock.unlock();
        }

        public void handleMessage(Message msg) {
            this.mStartLock.lock();
            try {
                switch (msg.what) {
                    case ViewCrawler.MESSAGE_INITIALIZE_CHANGES /*0*/:
                        loadKnownChanges();
                        initializeChanges();
                        break;
                    case ViewCrawler.MESSAGE_CONNECT_TO_EDITOR /*1*/:
                        connectToEditor();
                        break;
                    case ViewCrawler.MESSAGE_SEND_STATE_FOR_EDITING /*2*/:
                        sendSnapshot((JSONObject) msg.obj);
                        break;
                    case ViewCrawler.MESSAGE_HANDLE_EDITOR_CHANGES_RECEIVED /*3*/:
                        handleEditorChangeReceived((JSONObject) msg.obj);
                        break;
                    case ViewCrawler.MESSAGE_SEND_DEVICE_INFO /*4*/:
                        sendDeviceInfo();
                        break;
                    case ViewCrawler.MESSAGE_EVENT_BINDINGS_RECEIVED /*5*/:
                        handleEventBindingsReceived((JSONArray) msg.obj);
                        break;
                    case ViewCrawler.MESSAGE_HANDLE_EDITOR_BINDINGS_RECEIVED /*6*/:
                        handleEditorBindingsReceived((JSONObject) msg.obj);
                        break;
                    case ViewCrawler.MESSAGE_SEND_EVENT_TRACKED /*7*/:
                        sendReportTrackToEditor((String) msg.obj);
                        break;
                    case ViewCrawler.MESSAGE_HANDLE_EDITOR_CLOSED /*8*/:
                        handleEditorClosed();
                        break;
                    case ViewCrawler.MESSAGE_VARIANTS_RECEIVED /*9*/:
                        handleVariantsReceived((JSONArray) msg.obj);
                        break;
                    case ViewCrawler.MESSAGE_HANDLE_EDITOR_CHANGES_CLEARED /*10*/:
                        handleEditorBindingsCleared((JSONObject) msg.obj);
                        break;
                    case ViewCrawler.MESSAGE_HANDLE_EDITOR_TWEAKS_RECEIVED /*11*/:
                        handleEditorTweaksReceived((JSONObject) msg.obj);
                        break;
                    case ViewCrawler.MESSAGE_SEND_LAYOUT_ERROR /*12*/:
                        sendLayoutError((LayoutErrorMessage) msg.obj);
                        break;
                }
                this.mStartLock.unlock();
            } catch (Throwable th) {
                this.mStartLock.unlock();
            }
        }

        private void loadKnownChanges() {
            SharedPreferences preferences = getSharedPreferences();
            String storedChanges = preferences.getString(ViewCrawler.SHARED_PREF_EDITS_FILE, null);
            if (storedChanges != null) {
                try {
                    JSONArray variants = new JSONArray(storedChanges);
                    int variantsLength = variants.length();
                    for (int i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < variantsLength; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                        JSONObject variant = variants.getJSONObject(i);
                        this.mSeenExperiments.add(new Pair(Integer.valueOf(variant.getInt("experiment_id")), Integer.valueOf(variant.getInt(Name.MARK))));
                    }
                } catch (JSONException e) {
                    Log.e(ViewCrawler.LOGTAG, "Malformed variants found in persistent storage, clearing all variants", e);
                    android.content.SharedPreferences.Editor editor = preferences.edit();
                    editor.remove(ViewCrawler.SHARED_PREF_EDITS_FILE);
                    editor.remove(ViewCrawler.SHARED_PREF_BINDINGS_KEY);
                    editor.apply();
                }
            }
        }

        private void initializeChanges() {
            int i;
            SharedPreferences preferences = getSharedPreferences();
            String storedChanges = preferences.getString(ViewCrawler.SHARED_PREF_EDITS_FILE, null);
            String storedBindings = preferences.getString(ViewCrawler.SHARED_PREF_BINDINGS_KEY, null);
            List<Pair<Integer, Integer>> emptyVariantIds = new ArrayList();
            if (storedChanges != null) {
                try {
                    this.mPersistentChanges.clear();
                    this.mPersistentTweaks.clear();
                    JSONArray jSONArray = new JSONArray(storedChanges);
                    int variantsLength = jSONArray.length();
                    for (int variantIx = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; variantIx < variantsLength; variantIx += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                        JSONObject nextVariant = jSONArray.getJSONObject(variantIx);
                        int variantIdPart = nextVariant.getInt(Name.MARK);
                        int experimentIdPart = nextVariant.getInt("experiment_id");
                        Pair<Integer, Integer> pair = new Pair(Integer.valueOf(experimentIdPart), Integer.valueOf(variantIdPart));
                        JSONArray actions = nextVariant.getJSONArray("actions");
                        int actionsLength = actions.length();
                        for (i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < actionsLength; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                            JSONObject change = actions.getJSONObject(i);
                            VariantChange variantChange = new VariantChange(JSONUtils.optionalStringKey(change, "target_activity"), change, pair);
                            this.mPersistentChanges.add(variantChange);
                        }
                        JSONArray tweaks = nextVariant.getJSONArray("tweaks");
                        int tweaksLength = tweaks.length();
                        for (i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < tweaksLength; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                            VariantTweak variantTweak = new VariantTweak(tweaks.getJSONObject(i), pair);
                            this.mPersistentTweaks.add(variantTweak);
                        }
                        if (actionsLength == 0 && tweaksLength == 0) {
                            emptyVariantIds.add(new Pair(Integer.valueOf(experimentIdPart), Integer.valueOf(variantIdPart)));
                        }
                    }
                } catch (JSONException e) {
                    Log.i(ViewCrawler.LOGTAG, "JSON error when initializing saved changes, clearing persistent memory", e);
                    android.content.SharedPreferences.Editor editor = preferences.edit();
                    editor.remove(ViewCrawler.SHARED_PREF_EDITS_FILE);
                    editor.remove(ViewCrawler.SHARED_PREF_BINDINGS_KEY);
                    editor.apply();
                }
            }
            if (storedBindings != null) {
                JSONArray bindings = new JSONArray(storedBindings);
                this.mPersistentEventBindings.clear();
                for (i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < bindings.length(); i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                    JSONObject event = bindings.getJSONObject(i);
                    String targetActivity = JSONUtils.optionalStringKey(event, "target_activity");
                    this.mPersistentEventBindings.add(new Pair(targetActivity, event));
                }
            }
            applyVariantsAndEventBindings(emptyVariantIds);
        }

        private void connectToEditor() {
            if (MPConfig.DEBUG) {
                Log.v(ViewCrawler.LOGTAG, "connecting to editor");
            }
            if (this.mEditorConnection == null || !this.mEditorConnection.isValid()) {
                SSLSocketFactory socketFactory = ViewCrawler.this.mConfig.getSSLSocketFactory();
                if (socketFactory != null) {
                    String url = MPConfig.getInstance(this.mContext).getEditorUrl() + this.mToken;
                    try {
                        this.mEditorConnection = new EditorConnection(new URI(url), new Editor(null), socketFactory.createSocket());
                    } catch (URISyntaxException e) {
                        Log.e(ViewCrawler.LOGTAG, "Error parsing URI " + url + " for editor websocket", e);
                    } catch (EditorConnectionException e2) {
                        Log.e(ViewCrawler.LOGTAG, "Error connecting to URI " + url, e2);
                    } catch (IOException e3) {
                        Log.i(ViewCrawler.LOGTAG, "Can't create SSL Socket to connect to editor service", e3);
                    }
                } else if (MPConfig.DEBUG) {
                    Log.v(ViewCrawler.LOGTAG, "SSL is not available on this device, no connection will be attempted to the events editor.");
                }
            } else if (MPConfig.DEBUG) {
                Log.v(ViewCrawler.LOGTAG, "There is already a valid connection to an events editor.");
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void sendError(java.lang.String r7) {
            /*
            r6 = this;
            r3 = r6.mEditorConnection;
            if (r3 != 0) goto L_0x0005;
        L_0x0004:
            return;
        L_0x0005:
            r1 = new org.json.JSONObject;
            r1.<init>();
            r3 = "error_message";
            r1.put(r3, r7);	 Catch:{ JSONException -> 0x003d }
        L_0x000f:
            r2 = new java.io.OutputStreamWriter;
            r3 = r6.mEditorConnection;
            r3 = r3.getBufferedOutputStream();
            r2.<init>(r3);
            r3 = "{\"type\": \"error\", ";
            r2.write(r3);	 Catch:{ IOException -> 0x0046 }
            r3 = "\"payload\": ";
            r2.write(r3);	 Catch:{ IOException -> 0x0046 }
            r3 = r1.toString();	 Catch:{ IOException -> 0x0046 }
            r2.write(r3);	 Catch:{ IOException -> 0x0046 }
            r3 = "}";
            r2.write(r3);	 Catch:{ IOException -> 0x0046 }
            r2.close();	 Catch:{ IOException -> 0x0034 }
            goto L_0x0004;
        L_0x0034:
            r0 = move-exception;
            r3 = "MixpanelAPI.ViewCrawler";
            r4 = "Could not close output writer to editor";
            android.util.Log.e(r3, r4, r0);
            goto L_0x0004;
        L_0x003d:
            r0 = move-exception;
            r3 = "MixpanelAPI.ViewCrawler";
            r4 = "Apparently impossible JSONException";
            android.util.Log.e(r3, r4, r0);
            goto L_0x000f;
        L_0x0046:
            r0 = move-exception;
            r3 = "MixpanelAPI.ViewCrawler";
            r4 = "Can't write error message to editor";
            android.util.Log.e(r3, r4, r0);	 Catch:{ all -> 0x005b }
            r2.close();	 Catch:{ IOException -> 0x0052 }
            goto L_0x0004;
        L_0x0052:
            r0 = move-exception;
            r3 = "MixpanelAPI.ViewCrawler";
            r4 = "Could not close output writer to editor";
            android.util.Log.e(r3, r4, r0);
            goto L_0x0004;
        L_0x005b:
            r3 = move-exception;
            r2.close();	 Catch:{ IOException -> 0x0060 }
        L_0x005f:
            throw r3;
        L_0x0060:
            r0 = move-exception;
            r4 = "MixpanelAPI.ViewCrawler";
            r5 = "Could not close output writer to editor";
            android.util.Log.e(r4, r5, r0);
            goto L_0x005f;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.viewcrawler.ViewCrawler.ViewCrawlerHandler.sendError(java.lang.String):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void sendDeviceInfo() {
            /*
            r12 = this;
            r9 = r12.mEditorConnection;
            if (r9 != 0) goto L_0x0005;
        L_0x0004:
            return;
        L_0x0005:
            r9 = r12.mEditorConnection;
            r5 = r9.getBufferedOutputStream();
            r4 = new android.util.JsonWriter;
            r9 = new java.io.OutputStreamWriter;
            r9.<init>(r5);
            r4.<init>(r9);
            r4.beginObject();	 Catch:{ IOException -> 0x0099 }
            r9 = "type";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = "device_info_response";
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            r9 = "payload";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r9.beginObject();	 Catch:{ IOException -> 0x0099 }
            r9 = "device_type";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = "Android";
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            r9 = "device_name";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0099 }
            r10.<init>();	 Catch:{ IOException -> 0x0099 }
            r11 = android.os.Build.BRAND;	 Catch:{ IOException -> 0x0099 }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0099 }
            r11 = "/";
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0099 }
            r11 = android.os.Build.MODEL;	 Catch:{ IOException -> 0x0099 }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0099 }
            r10 = r10.toString();	 Catch:{ IOException -> 0x0099 }
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            r9 = "scaled_density";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = com.mixpanel.android.viewcrawler.ViewCrawler.this;	 Catch:{ IOException -> 0x0099 }
            r10 = r10.mScaledDensity;	 Catch:{ IOException -> 0x0099 }
            r10 = (double) r10;	 Catch:{ IOException -> 0x0099 }
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            r9 = com.mixpanel.android.viewcrawler.ViewCrawler.this;	 Catch:{ IOException -> 0x0099 }
            r9 = r9.mDeviceInfo;	 Catch:{ IOException -> 0x0099 }
            r9 = r9.entrySet();	 Catch:{ IOException -> 0x0099 }
            r3 = r9.iterator();	 Catch:{ IOException -> 0x0099 }
        L_0x0079:
            r9 = r3.hasNext();	 Catch:{ IOException -> 0x0099 }
            if (r9 == 0) goto L_0x00b0;
        L_0x007f:
            r2 = r3.next();	 Catch:{ IOException -> 0x0099 }
            r2 = (java.util.Map.Entry) r2;	 Catch:{ IOException -> 0x0099 }
            r9 = r2.getKey();	 Catch:{ IOException -> 0x0099 }
            r9 = (java.lang.String) r9;	 Catch:{ IOException -> 0x0099 }
            r10 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r9 = r2.getValue();	 Catch:{ IOException -> 0x0099 }
            r9 = (java.lang.String) r9;	 Catch:{ IOException -> 0x0099 }
            r10.value(r9);	 Catch:{ IOException -> 0x0099 }
            goto L_0x0079;
        L_0x0099:
            r1 = move-exception;
            r9 = "MixpanelAPI.ViewCrawler";
            r10 = "Can't write device_info to server";
            android.util.Log.e(r9, r10, r1);	 Catch:{ all -> 0x0130 }
            r4.close();	 Catch:{ IOException -> 0x00a6 }
            goto L_0x0004;
        L_0x00a6:
            r1 = move-exception;
            r9 = "MixpanelAPI.ViewCrawler";
            r10 = "Can't close websocket writer";
            android.util.Log.e(r9, r10, r1);
            goto L_0x0004;
        L_0x00b0:
            r9 = com.mixpanel.android.viewcrawler.ViewCrawler.this;	 Catch:{ IOException -> 0x0099 }
            r9 = r9.mTweaks;	 Catch:{ IOException -> 0x0099 }
            r7 = r9.getAllValues();	 Catch:{ IOException -> 0x0099 }
            r9 = "tweaks";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r9.beginArray();	 Catch:{ IOException -> 0x0099 }
            r9 = r7.entrySet();	 Catch:{ IOException -> 0x0099 }
            r3 = r9.iterator();	 Catch:{ IOException -> 0x0099 }
        L_0x00cb:
            r9 = r3.hasNext();	 Catch:{ IOException -> 0x0099 }
            if (r9 == 0) goto L_0x01bc;
        L_0x00d1:
            r6 = r3.next();	 Catch:{ IOException -> 0x0099 }
            r6 = (java.util.Map.Entry) r6;	 Catch:{ IOException -> 0x0099 }
            r0 = r6.getValue();	 Catch:{ IOException -> 0x0099 }
            r0 = (com.mixpanel.android.mpmetrics.Tweaks.TweakValue) r0;	 Catch:{ IOException -> 0x0099 }
            r8 = r6.getKey();	 Catch:{ IOException -> 0x0099 }
            r8 = (java.lang.String) r8;	 Catch:{ IOException -> 0x0099 }
            r4.beginObject();	 Catch:{ IOException -> 0x0099 }
            r9 = "name";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r9.value(r8);	 Catch:{ IOException -> 0x0099 }
            r9 = "minimum";
            r10 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r9 = 0;
            r9 = (java.lang.Number) r9;	 Catch:{ IOException -> 0x0099 }
            r10.value(r9);	 Catch:{ IOException -> 0x0099 }
            r9 = "maximum";
            r10 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r9 = 0;
            r9 = (java.lang.Number) r9;	 Catch:{ IOException -> 0x0099 }
            r10.value(r9);	 Catch:{ IOException -> 0x0099 }
            r9 = r0.type;	 Catch:{ IOException -> 0x0099 }
            switch(r9) {
                case 1: goto L_0x0135;
                case 2: goto L_0x0152;
                case 3: goto L_0x017a;
                case 4: goto L_0x01a2;
                default: goto L_0x010c;
            };	 Catch:{ IOException -> 0x0099 }
        L_0x010c:
            r9 = "MixpanelAPI.ViewCrawler";
            r10 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0099 }
            r10.<init>();	 Catch:{ IOException -> 0x0099 }
            r11 = "Unrecognized Tweak Type ";
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0099 }
            r11 = r0.type;	 Catch:{ IOException -> 0x0099 }
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0099 }
            r11 = " encountered.";
            r10 = r10.append(r11);	 Catch:{ IOException -> 0x0099 }
            r10 = r10.toString();	 Catch:{ IOException -> 0x0099 }
            android.util.Log.wtf(r9, r10);	 Catch:{ IOException -> 0x0099 }
        L_0x012c:
            r4.endObject();	 Catch:{ IOException -> 0x0099 }
            goto L_0x00cb;
        L_0x0130:
            r9 = move-exception;
            r4.close();	 Catch:{ IOException -> 0x01d4 }
        L_0x0134:
            throw r9;
        L_0x0135:
            r9 = "type";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = "boolean";
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            r9 = "value";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = r0.getBooleanValue();	 Catch:{ IOException -> 0x0099 }
            r10 = r10.booleanValue();	 Catch:{ IOException -> 0x0099 }
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            goto L_0x012c;
        L_0x0152:
            r9 = "type";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = "number";
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            r9 = "encoding";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = "d";
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            r9 = "value";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = r0.getNumberValue();	 Catch:{ IOException -> 0x0099 }
            r10 = r10.doubleValue();	 Catch:{ IOException -> 0x0099 }
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            goto L_0x012c;
        L_0x017a:
            r9 = "type";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = "number";
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            r9 = "encoding";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = "l";
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            r9 = "value";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = r0.getNumberValue();	 Catch:{ IOException -> 0x0099 }
            r10 = r10.longValue();	 Catch:{ IOException -> 0x0099 }
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            goto L_0x012c;
        L_0x01a2:
            r9 = "type";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = "string";
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            r9 = "value";
            r9 = r4.name(r9);	 Catch:{ IOException -> 0x0099 }
            r10 = r0.getStringValue();	 Catch:{ IOException -> 0x0099 }
            r9.value(r10);	 Catch:{ IOException -> 0x0099 }
            goto L_0x012c;
        L_0x01bc:
            r4.endArray();	 Catch:{ IOException -> 0x0099 }
            r4.endObject();	 Catch:{ IOException -> 0x0099 }
            r4.endObject();	 Catch:{ IOException -> 0x0099 }
            r4.close();	 Catch:{ IOException -> 0x01ca }
            goto L_0x0004;
        L_0x01ca:
            r1 = move-exception;
            r9 = "MixpanelAPI.ViewCrawler";
            r10 = "Can't close websocket writer";
            android.util.Log.e(r9, r10, r1);
            goto L_0x0004;
        L_0x01d4:
            r1 = move-exception;
            r10 = "MixpanelAPI.ViewCrawler";
            r11 = "Can't close websocket writer";
            android.util.Log.e(r10, r11, r1);
            goto L_0x0134;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.viewcrawler.ViewCrawler.ViewCrawlerHandler.sendDeviceInfo():void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void sendSnapshot(org.json.JSONObject r12) {
            /*
            r11 = this;
            r6 = java.lang.System.currentTimeMillis();
            r8 = "payload";
            r2 = r12.getJSONObject(r8);	 Catch:{ JSONException -> 0x0036, BadInstructionsException -> 0x0044 }
            r8 = "config";
            r8 = r2.has(r8);	 Catch:{ JSONException -> 0x0036, BadInstructionsException -> 0x0044 }
            if (r8 == 0) goto L_0x0025;
        L_0x0012:
            r8 = r11.mProtocol;	 Catch:{ JSONException -> 0x0036, BadInstructionsException -> 0x0044 }
            r8 = r8.readSnapshotConfig(r2);	 Catch:{ JSONException -> 0x0036, BadInstructionsException -> 0x0044 }
            r11.mSnapshot = r8;	 Catch:{ JSONException -> 0x0036, BadInstructionsException -> 0x0044 }
            r8 = com.mixpanel.android.mpmetrics.MPConfig.DEBUG;	 Catch:{ JSONException -> 0x0036, BadInstructionsException -> 0x0044 }
            if (r8 == 0) goto L_0x0025;
        L_0x001e:
            r8 = "MixpanelAPI.ViewCrawler";
            r9 = "Initializing snapshot with configuration";
            android.util.Log.v(r8, r9);	 Catch:{ JSONException -> 0x0036, BadInstructionsException -> 0x0044 }
        L_0x0025:
            r8 = r11.mSnapshot;
            if (r8 != 0) goto L_0x0054;
        L_0x0029:
            r8 = "No snapshot configuration (or a malformed snapshot configuration) was sent.";
            r11.sendError(r8);
            r8 = "MixpanelAPI.ViewCrawler";
            r9 = "Mixpanel editor is misconfigured, sent a snapshot request without a valid configuration.";
            android.util.Log.w(r8, r9);
        L_0x0035:
            return;
        L_0x0036:
            r0 = move-exception;
            r8 = "MixpanelAPI.ViewCrawler";
            r9 = "Payload with snapshot config required with snapshot request";
            android.util.Log.e(r8, r9, r0);
            r8 = "Payload with snapshot config required with snapshot request";
            r11.sendError(r8);
            goto L_0x0035;
        L_0x0044:
            r0 = move-exception;
            r8 = "MixpanelAPI.ViewCrawler";
            r9 = "Editor sent malformed message with snapshot request";
            android.util.Log.e(r8, r9, r0);
            r8 = r0.getMessage();
            r11.sendError(r8);
            goto L_0x0035;
        L_0x0054:
            r8 = r11.mEditorConnection;
            r1 = r8.getBufferedOutputStream();
            r3 = new java.io.OutputStreamWriter;
            r3.<init>(r1);
            r8 = "{";
            r3.write(r8);	 Catch:{ IOException -> 0x00aa }
            r8 = "\"type\": \"snapshot_response\",";
            r3.write(r8);	 Catch:{ IOException -> 0x00aa }
            r8 = "\"payload\": {";
            r3.write(r8);	 Catch:{ IOException -> 0x00aa }
            r8 = "\"activities\":";
            r3.write(r8);	 Catch:{ IOException -> 0x00aa }
            r3.flush();	 Catch:{ IOException -> 0x00aa }
            r8 = r11.mSnapshot;	 Catch:{ IOException -> 0x00aa }
            r9 = com.mixpanel.android.viewcrawler.ViewCrawler.this;	 Catch:{ IOException -> 0x00aa }
            r9 = r9.mEditState;	 Catch:{ IOException -> 0x00aa }
            r8.snapshots(r9, r1);	 Catch:{ IOException -> 0x00aa }
            r8 = java.lang.System.currentTimeMillis();	 Catch:{ IOException -> 0x00aa }
            r4 = r8 - r6;
            r8 = ",\"snapshot_time_millis\": ";
            r3.write(r8);	 Catch:{ IOException -> 0x00aa }
            r8 = java.lang.Long.toString(r4);	 Catch:{ IOException -> 0x00aa }
            r3.write(r8);	 Catch:{ IOException -> 0x00aa }
            r8 = "}";
            r3.write(r8);	 Catch:{ IOException -> 0x00aa }
            r8 = "}";
            r3.write(r8);	 Catch:{ IOException -> 0x00aa }
            r3.close();	 Catch:{ IOException -> 0x00a1 }
            goto L_0x0035;
        L_0x00a1:
            r0 = move-exception;
            r8 = "MixpanelAPI.ViewCrawler";
            r9 = "Can't close writer.";
            android.util.Log.e(r8, r9, r0);
            goto L_0x0035;
        L_0x00aa:
            r0 = move-exception;
            r8 = "MixpanelAPI.ViewCrawler";
            r9 = "Can't write snapshot request to server";
            android.util.Log.e(r8, r9, r0);	 Catch:{ all -> 0x00c0 }
            r3.close();	 Catch:{ IOException -> 0x00b6 }
            goto L_0x0035;
        L_0x00b6:
            r0 = move-exception;
            r8 = "MixpanelAPI.ViewCrawler";
            r9 = "Can't close writer.";
            android.util.Log.e(r8, r9, r0);
            goto L_0x0035;
        L_0x00c0:
            r8 = move-exception;
            r3.close();	 Catch:{ IOException -> 0x00c5 }
        L_0x00c4:
            throw r8;
        L_0x00c5:
            r0 = move-exception;
            r9 = "MixpanelAPI.ViewCrawler";
            r10 = "Can't close writer.";
            android.util.Log.e(r9, r10, r0);
            goto L_0x00c4;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.viewcrawler.ViewCrawler.ViewCrawlerHandler.sendSnapshot(org.json.JSONObject):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void sendReportTrackToEditor(java.lang.String r8) {
            /*
            r7 = this;
            r4 = r7.mEditorConnection;
            if (r4 != 0) goto L_0x0005;
        L_0x0004:
            return;
        L_0x0005:
            r4 = r7.mEditorConnection;
            r2 = r4.getBufferedOutputStream();
            r3 = new java.io.OutputStreamWriter;
            r3.<init>(r2);
            r1 = new android.util.JsonWriter;
            r1.<init>(r3);
            r1.beginObject();	 Catch:{ IOException -> 0x004a }
            r4 = "type";
            r4 = r1.name(r4);	 Catch:{ IOException -> 0x004a }
            r5 = "track_message";
            r4.value(r5);	 Catch:{ IOException -> 0x004a }
            r4 = "payload";
            r1.name(r4);	 Catch:{ IOException -> 0x004a }
            r1.beginObject();	 Catch:{ IOException -> 0x004a }
            r4 = "event_name";
            r4 = r1.name(r4);	 Catch:{ IOException -> 0x004a }
            r4.value(r8);	 Catch:{ IOException -> 0x004a }
            r1.endObject();	 Catch:{ IOException -> 0x004a }
            r1.endObject();	 Catch:{ IOException -> 0x004a }
            r1.flush();	 Catch:{ IOException -> 0x004a }
            r1.close();	 Catch:{ IOException -> 0x0041 }
            goto L_0x0004;
        L_0x0041:
            r0 = move-exception;
            r4 = "MixpanelAPI.ViewCrawler";
            r5 = "Can't close writer.";
            android.util.Log.e(r4, r5, r0);
            goto L_0x0004;
        L_0x004a:
            r0 = move-exception;
            r4 = "MixpanelAPI.ViewCrawler";
            r5 = "Can't write track_message to server";
            android.util.Log.e(r4, r5, r0);	 Catch:{ all -> 0x005f }
            r1.close();	 Catch:{ IOException -> 0x0056 }
            goto L_0x0004;
        L_0x0056:
            r0 = move-exception;
            r4 = "MixpanelAPI.ViewCrawler";
            r5 = "Can't close writer.";
            android.util.Log.e(r4, r5, r0);
            goto L_0x0004;
        L_0x005f:
            r4 = move-exception;
            r1.close();	 Catch:{ IOException -> 0x0064 }
        L_0x0063:
            throw r4;
        L_0x0064:
            r0 = move-exception;
            r5 = "MixpanelAPI.ViewCrawler";
            r6 = "Can't close writer.";
            android.util.Log.e(r5, r6, r0);
            goto L_0x0063;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.viewcrawler.ViewCrawler.ViewCrawlerHandler.sendReportTrackToEditor(java.lang.String):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void sendLayoutError(com.mixpanel.android.viewcrawler.ViewVisitor.LayoutErrorMessage r8) {
            /*
            r7 = this;
            r4 = r7.mEditorConnection;
            if (r4 != 0) goto L_0x0005;
        L_0x0004:
            return;
        L_0x0005:
            r4 = r7.mEditorConnection;
            r2 = r4.getBufferedOutputStream();
            r3 = new java.io.OutputStreamWriter;
            r3.<init>(r2);
            r1 = new android.util.JsonWriter;
            r1.<init>(r3);
            r1.beginObject();	 Catch:{ IOException -> 0x004d }
            r4 = "type";
            r4 = r1.name(r4);	 Catch:{ IOException -> 0x004d }
            r5 = "layout_error";
            r4.value(r5);	 Catch:{ IOException -> 0x004d }
            r4 = "exception_type";
            r4 = r1.name(r4);	 Catch:{ IOException -> 0x004d }
            r5 = r8.getErrorType();	 Catch:{ IOException -> 0x004d }
            r4.value(r5);	 Catch:{ IOException -> 0x004d }
            r4 = "cid";
            r4 = r1.name(r4);	 Catch:{ IOException -> 0x004d }
            r5 = r8.getName();	 Catch:{ IOException -> 0x004d }
            r4.value(r5);	 Catch:{ IOException -> 0x004d }
            r1.endObject();	 Catch:{ IOException -> 0x004d }
            r1.close();	 Catch:{ IOException -> 0x0044 }
            goto L_0x0004;
        L_0x0044:
            r0 = move-exception;
            r4 = "MixpanelAPI.ViewCrawler";
            r5 = "Can't close writer.";
            android.util.Log.e(r4, r5, r0);
            goto L_0x0004;
        L_0x004d:
            r0 = move-exception;
            r4 = "MixpanelAPI.ViewCrawler";
            r5 = "Can't write track_message to server";
            android.util.Log.e(r4, r5, r0);	 Catch:{ all -> 0x0062 }
            r1.close();	 Catch:{ IOException -> 0x0059 }
            goto L_0x0004;
        L_0x0059:
            r0 = move-exception;
            r4 = "MixpanelAPI.ViewCrawler";
            r5 = "Can't close writer.";
            android.util.Log.e(r4, r5, r0);
            goto L_0x0004;
        L_0x0062:
            r4 = move-exception;
            r1.close();	 Catch:{ IOException -> 0x0067 }
        L_0x0066:
            throw r4;
        L_0x0067:
            r0 = move-exception;
            r5 = "MixpanelAPI.ViewCrawler";
            r6 = "Can't close writer.";
            android.util.Log.e(r5, r6, r0);
            goto L_0x0066;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.viewcrawler.ViewCrawler.ViewCrawlerHandler.sendLayoutError(com.mixpanel.android.viewcrawler.ViewVisitor$LayoutErrorMessage):void");
        }

        private void handleEditorChangeReceived(JSONObject changeMessage) {
            try {
                JSONArray actions = changeMessage.getJSONObject("payload").getJSONArray("actions");
                for (int i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < actions.length(); i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                    JSONObject change = actions.getJSONObject(i);
                    String targetActivity = JSONUtils.optionalStringKey(change, "target_activity");
                    this.mEditorChanges.put(change.getString("name"), new Pair(targetActivity, change));
                }
                applyVariantsAndEventBindings(Collections.emptyList());
            } catch (JSONException e) {
                Log.e(ViewCrawler.LOGTAG, "Bad change request received", e);
            }
        }

        private void handleEditorBindingsCleared(JSONObject clearMessage) {
            try {
                JSONArray actions = clearMessage.getJSONObject("payload").getJSONArray("actions");
                for (int i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < actions.length(); i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                    this.mEditorChanges.remove(actions.getString(i));
                }
            } catch (JSONException e) {
                Log.e(ViewCrawler.LOGTAG, "Bad clear request received", e);
            }
            applyVariantsAndEventBindings(Collections.emptyList());
        }

        private void handleEditorTweaksReceived(JSONObject tweaksMessage) {
            try {
                this.mEditorTweaks.clear();
                JSONArray tweaks = tweaksMessage.getJSONObject("payload").getJSONArray("tweaks");
                int length = tweaks.length();
                for (int i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < length; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                    this.mEditorTweaks.add(tweaks.getJSONObject(i));
                }
            } catch (JSONException e) {
                Log.e(ViewCrawler.LOGTAG, "Bad tweaks received", e);
            }
            applyVariantsAndEventBindings(Collections.emptyList());
        }

        private void handleVariantsReceived(JSONArray variants) {
            android.content.SharedPreferences.Editor editor = getSharedPreferences().edit();
            editor.putString(ViewCrawler.SHARED_PREF_EDITS_FILE, variants.toString());
            editor.apply();
            initializeChanges();
        }

        private void handleEventBindingsReceived(JSONArray eventBindings) {
            android.content.SharedPreferences.Editor editor = getSharedPreferences().edit();
            editor.putString(ViewCrawler.SHARED_PREF_BINDINGS_KEY, eventBindings.toString());
            editor.apply();
            initializeChanges();
        }

        private void handleEditorBindingsReceived(JSONObject message) {
            try {
                JSONArray eventBindings = message.getJSONObject("payload").getJSONArray("events");
                int eventCount = eventBindings.length();
                this.mEditorEventBindings.clear();
                for (int i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < eventCount; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                    try {
                        JSONObject event = eventBindings.getJSONObject(i);
                        this.mEditorEventBindings.add(new Pair(JSONUtils.optionalStringKey(event, "target_activity"), event));
                    } catch (JSONException e) {
                        Log.e(ViewCrawler.LOGTAG, "Bad event binding received from editor in " + eventBindings.toString(), e);
                    }
                }
                applyVariantsAndEventBindings(Collections.emptyList());
            } catch (JSONException e2) {
                Log.e(ViewCrawler.LOGTAG, "Bad event bindings received", e2);
            }
        }

        private void handleEditorClosed() {
            this.mEditorChanges.clear();
            this.mEditorEventBindings.clear();
            this.mSnapshot = null;
            if (MPConfig.DEBUG) {
                Log.v(ViewCrawler.LOGTAG, "Editor closed- freeing snapshot");
            }
            applyVariantsAndEventBindings(Collections.emptyList());
            for (String assetUrl : this.mEditorAssetUrls) {
                this.mImageStore.deleteStorage(assetUrl);
            }
        }

        private void applyVariantsAndEventBindings(List<Pair<Integer, Integer>> emptyVariantIds) {
            int i;
            Pair<String, JSONObject> changeInfo;
            List<Pair<String, ViewVisitor>> newVisitors = new ArrayList();
            Set<Pair<Integer, Integer>> toTrack = new HashSet();
            int size = this.mPersistentChanges.size();
            for (i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < size; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                VariantChange changeInfo2 = (VariantChange) this.mPersistentChanges.get(i);
                try {
                    Edit edit = this.mProtocol.readEdit(changeInfo2.change);
                    newVisitors.add(new Pair(changeInfo2.activityName, edit.visitor));
                    if (!this.mSeenExperiments.contains(changeInfo2.variantId)) {
                        toTrack.add(changeInfo2.variantId);
                    }
                } catch (CantGetEditAssetsException e) {
                    Log.v(ViewCrawler.LOGTAG, "Can't load assets for an edit, won't apply the change now", e);
                } catch (InapplicableInstructionsException e2) {
                    Log.i(ViewCrawler.LOGTAG, e2.getMessage());
                } catch (BadInstructionsException e3) {
                    Log.e(ViewCrawler.LOGTAG, "Bad persistent change request cannot be applied.", e3);
                }
            }
            size = this.mPersistentTweaks.size();
            for (i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < size; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                VariantTweak tweakInfo = (VariantTweak) this.mPersistentTweaks.get(i);
                try {
                    Pair<String, Object> tweakValue = this.mProtocol.readTweak(tweakInfo.tweak);
                    ViewCrawler.this.mTweaks.set((String) tweakValue.first, tweakValue.second);
                    if (!this.mSeenExperiments.contains(tweakInfo.variantId)) {
                        toTrack.add(tweakInfo.variantId);
                    }
                } catch (BadInstructionsException e32) {
                    Log.e(ViewCrawler.LOGTAG, "Bad editor tweak cannot be applied.", e32);
                }
            }
            for (Pair<String, JSONObject> changeInfo3 : this.mEditorChanges.values()) {
                try {
                    edit = this.mProtocol.readEdit((JSONObject) changeInfo3.second);
                    newVisitors.add(new Pair(changeInfo3.first, edit.visitor));
                    this.mEditorAssetUrls.addAll(edit.imageUrls);
                } catch (CantGetEditAssetsException e4) {
                    Log.v(ViewCrawler.LOGTAG, "Can't load assets for an edit, won't apply the change now", e4);
                } catch (InapplicableInstructionsException e22) {
                    Log.i(ViewCrawler.LOGTAG, e22.getMessage());
                } catch (BadInstructionsException e322) {
                    Log.e(ViewCrawler.LOGTAG, "Bad editor change request cannot be applied.", e322);
                }
            }
            size = this.mEditorTweaks.size();
            for (i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < size; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                JSONObject tweakDesc = (JSONObject) this.mEditorTweaks.get(i);
                try {
                    tweakValue = this.mProtocol.readTweak(tweakDesc);
                    ViewCrawler.this.mTweaks.set((String) tweakValue.first, tweakValue.second);
                } catch (BadInstructionsException e3222) {
                    Log.e(ViewCrawler.LOGTAG, "Strange tweaks received", e3222);
                }
            }
            size = this.mPersistentEventBindings.size();
            for (i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < size; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                changeInfo3 = (Pair) this.mPersistentEventBindings.get(i);
                try {
                    ViewVisitor visitor = this.mProtocol.readEventBinding((JSONObject) changeInfo3.second, ViewCrawler.this.mDynamicEventTracker);
                    newVisitors.add(new Pair(changeInfo3.first, visitor));
                } catch (InapplicableInstructionsException e222) {
                    Log.i(ViewCrawler.LOGTAG, e222.getMessage());
                } catch (BadInstructionsException e32222) {
                    Log.e(ViewCrawler.LOGTAG, "Bad persistent event binding cannot be applied.", e32222);
                }
            }
            size = this.mEditorEventBindings.size();
            for (i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < size; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                changeInfo3 = (Pair) this.mEditorEventBindings.get(i);
                try {
                    visitor = this.mProtocol.readEventBinding((JSONObject) changeInfo3.second, ViewCrawler.this.mDynamicEventTracker);
                    newVisitors.add(new Pair(changeInfo3.first, visitor));
                } catch (InapplicableInstructionsException e2222) {
                    Log.i(ViewCrawler.LOGTAG, e2222.getMessage());
                } catch (BadInstructionsException e322222) {
                    Log.e(ViewCrawler.LOGTAG, "Bad editor event binding cannot be applied.", e322222);
                }
            }
            Map<String, List<ViewVisitor>> editMap = new HashMap();
            int totalEdits = newVisitors.size();
            for (i = ViewCrawler.MESSAGE_INITIALIZE_CHANGES; i < totalEdits; i += ViewCrawler.MESSAGE_CONNECT_TO_EDITOR) {
                List<ViewVisitor> mapElement;
                Pair<String, ViewVisitor> next = (Pair) newVisitors.get(i);
                if (editMap.containsKey(next.first)) {
                    mapElement = (List) editMap.get(next.first);
                } else {
                    mapElement = new ArrayList();
                    editMap.put(next.first, mapElement);
                }
                mapElement.add(next.second);
            }
            ViewCrawler.this.mEditState.setEdits(editMap);
            for (Pair<Integer, Integer> id : emptyVariantIds) {
                if (!this.mSeenExperiments.contains(id)) {
                    toTrack.add(id);
                }
            }
            this.mSeenExperiments.addAll(toTrack);
            if (toTrack.size() > 0) {
                JSONObject variantObject = new JSONObject();
                try {
                    for (Pair<Integer, Integer> variant : toTrack) {
                        int experimentId = ((Integer) variant.first).intValue();
                        int variantId = ((Integer) variant.second).intValue();
                        JSONObject trackProps = new JSONObject();
                        trackProps.put("$experiment_id", experimentId);
                        trackProps.put("$variant_id", variantId);
                        variantObject.put(Integer.toString(experimentId), variantId);
                        ViewCrawler.this.mMixpanel.getPeople().merge("$experiments", variantObject);
                        ViewCrawler.this.mMixpanel.updateSuperProperties(new C11331(variantObject));
                        ViewCrawler.this.mMixpanel.track("$experiment_started", trackProps);
                    }
                } catch (JSONException e5) {
                    Log.wtf(ViewCrawler.LOGTAG, "Could not build JSON for reporting experiment start", e5);
                }
            }
        }

        private SharedPreferences getSharedPreferences() {
            return this.mContext.getSharedPreferences(ViewCrawler.SHARED_PREF_EDITS_FILE + this.mToken, ViewCrawler.MESSAGE_INITIALIZE_CHANGES);
        }
    }

    public ViewCrawler(Context context, String token, MixpanelAPI mixpanel, Tweaks tweaks) {
        this.mConfig = MPConfig.getInstance(context);
        this.mContext = context;
        this.mEditState = new EditState();
        this.mTweaks = tweaks;
        this.mDeviceInfo = mixpanel.getDeviceInfo();
        this.mScaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new LifecycleCallbacks());
        HandlerThread thread = new HandlerThread(ViewCrawler.class.getCanonicalName());
        thread.setPriority(MESSAGE_HANDLE_EDITOR_CHANGES_CLEARED);
        thread.start();
        this.mMessageThreadHandler = new ViewCrawlerHandler(context, token, thread.getLooper(), this);
        this.mDynamicEventTracker = new DynamicEventTracker(mixpanel, this.mMessageThreadHandler);
        this.mMixpanel = mixpanel;
        this.mTweaks.addOnTweakDeclaredListener(new C11321());
    }

    public void startUpdates() {
        this.mMessageThreadHandler.start();
        this.mMessageThreadHandler.sendMessage(this.mMessageThreadHandler.obtainMessage(MESSAGE_INITIALIZE_CHANGES));
    }

    public Tweaks getTweaks() {
        return this.mTweaks;
    }

    public void setEventBindings(JSONArray bindings) {
        Message msg = this.mMessageThreadHandler.obtainMessage(MESSAGE_EVENT_BINDINGS_RECEIVED);
        msg.obj = bindings;
        this.mMessageThreadHandler.sendMessage(msg);
    }

    public void setVariants(JSONArray variants) {
        Message msg = this.mMessageThreadHandler.obtainMessage(MESSAGE_VARIANTS_RECEIVED);
        msg.obj = variants;
        this.mMessageThreadHandler.sendMessage(msg);
    }

    public void reportTrack(String eventName) {
        Message m = this.mMessageThreadHandler.obtainMessage();
        m.what = MESSAGE_SEND_EVENT_TRACKED;
        m.obj = eventName;
        this.mMessageThreadHandler.sendMessage(m);
    }

    public void onLayoutError(LayoutErrorMessage e) {
        Message m = this.mMessageThreadHandler.obtainMessage();
        m.what = MESSAGE_SEND_LAYOUT_ERROR;
        m.obj = e;
        this.mMessageThreadHandler.sendMessage(m);
    }
}
