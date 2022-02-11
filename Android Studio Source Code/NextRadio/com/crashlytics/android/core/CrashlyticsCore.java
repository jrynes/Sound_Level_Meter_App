package com.crashlytics.android.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.ScrollView;
import android.widget.TextView;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.internal.CrashEventDataProvider;
import com.crashlytics.android.core.internal.models.SessionEventData;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.Crash.FatalException;
import io.fabric.sdk.android.services.common.Crash.LoggedException;
import io.fabric.sdk.android.services.common.ExecutorUtils;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import io.fabric.sdk.android.services.concurrency.Priority;
import io.fabric.sdk.android.services.concurrency.PriorityCallable;
import io.fabric.sdk.android.services.concurrency.Task;
import io.fabric.sdk.android.services.concurrency.UnmetDependencyException;
import io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.persistence.FileStore;
import io.fabric.sdk.android.services.persistence.FileStoreImpl;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import io.fabric.sdk.android.services.settings.PromptSettingsData;
import io.fabric.sdk.android.services.settings.SessionSettingsData;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.settings.Settings.SettingsAccess;
import io.fabric.sdk.android.services.settings.SettingsData;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.HttpsURLConnection;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;

@DependsOn({CrashEventDataProvider.class})
public class CrashlyticsCore extends Kit<Void> {
    static final float CLS_DEFAULT_PROCESS_DELAY = 1.0f;
    static final String COLLECT_CUSTOM_KEYS = "com.crashlytics.CollectCustomKeys";
    static final String COLLECT_CUSTOM_LOGS = "com.crashlytics.CollectCustomLogs";
    static final String CRASHLYTICS_API_ENDPOINT = "com.crashlytics.ApiEndpoint";
    static final String CRASHLYTICS_REQUIRE_BUILD_ID = "com.crashlytics.RequireBuildId";
    static final boolean CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT = true;
    static final String CRASH_MARKER_FILE_NAME = "crash_marker";
    static final int DEFAULT_MAIN_HANDLER_TIMEOUT_SEC = 4;
    private static final String INITIALIZATION_MARKER_FILE_NAME = "initialization_marker";
    static final int MAX_ATTRIBUTES = 64;
    static final int MAX_ATTRIBUTE_SIZE = 1024;
    private static final String PREF_ALWAYS_SEND_REPORTS_KEY = "always_send_reports_opt_in";
    private static final boolean SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT = false;
    public static final String TAG = "CrashlyticsCore";
    private String apiKey;
    private final ConcurrentHashMap<String, String> attributes;
    private String buildId;
    private CrashlyticsFileMarker crashMarker;
    private float delay;
    private boolean disabled;
    private CrashlyticsExecutorServiceWrapper executorServiceWrapper;
    private CrashEventDataProvider externalCrashEventDataProvider;
    private FileStore fileStore;
    private CrashlyticsUncaughtExceptionHandler handler;
    private HttpRequestFactory httpRequestFactory;
    private CrashlyticsFileMarker initializationMarker;
    private String installerPackageName;
    private CrashlyticsListener listener;
    private String packageName;
    private final PinningInfoProvider pinningInfo;
    private File sdkDir;
    private final long startTime;
    private String userEmail;
    private String userId;
    private String userName;
    private String versionCode;
    private String versionName;

    /* renamed from: com.crashlytics.android.core.CrashlyticsCore.1 */
    class C03651 extends PriorityCallable<Void> {
        C03651() {
        }

        public Void call() throws Exception {
            return CrashlyticsCore.this.doInBackground();
        }

        public Priority getPriority() {
            return Priority.IMMEDIATE;
        }
    }

    /* renamed from: com.crashlytics.android.core.CrashlyticsCore.2 */
    class C03662 implements Callable<Void> {
        C03662() {
        }

        public Void call() throws Exception {
            CrashlyticsCore.this.initializationMarker.create();
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Initialization marker file created.");
            return null;
        }
    }

    /* renamed from: com.crashlytics.android.core.CrashlyticsCore.3 */
    class C03673 implements Callable<Boolean> {
        C03673() {
        }

        public Boolean call() throws Exception {
            try {
                boolean removed = CrashlyticsCore.this.initializationMarker.remove();
                Fabric.getLogger().d(CrashlyticsCore.TAG, "Initialization marker file removed: " + removed);
                return Boolean.valueOf(removed);
            } catch (Exception e) {
                Fabric.getLogger().e(CrashlyticsCore.TAG, "Problem encountered deleting Crashlytics initialization marker.", e);
                return Boolean.valueOf(CrashlyticsCore.SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT);
            }
        }
    }

    /* renamed from: com.crashlytics.android.core.CrashlyticsCore.4 */
    class C03684 implements Callable<Boolean> {
        C03684() {
        }

        public Boolean call() throws Exception {
            return Boolean.valueOf(CrashlyticsCore.this.initializationMarker.isPresent());
        }
    }

    /* renamed from: com.crashlytics.android.core.CrashlyticsCore.5 */
    class C03695 implements SettingsAccess<Boolean> {
        C03695() {
        }

        public Boolean usingSettings(SettingsData settingsData) {
            boolean z = CrashlyticsCore.SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT;
            if (!settingsData.featuresData.promptEnabled) {
                return Boolean.valueOf(CrashlyticsCore.SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT);
            }
            if (!CrashlyticsCore.this.shouldSendReportsWithoutPrompting()) {
                z = CrashlyticsCore.CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT;
            }
            return Boolean.valueOf(z);
        }
    }

    /* renamed from: com.crashlytics.android.core.CrashlyticsCore.6 */
    class C03706 implements SettingsAccess<Boolean> {
        C03706() {
        }

        public Boolean usingSettings(SettingsData settingsData) {
            boolean send = CrashlyticsCore.CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT;
            Activity activity = CrashlyticsCore.this.getFabric().getCurrentActivity();
            if (!(activity == null || activity.isFinishing() || !CrashlyticsCore.this.shouldPromptUserBeforeSendingCrashReports())) {
                send = CrashlyticsCore.this.getSendDecisionFromUser(activity, settingsData.promptData);
            }
            return Boolean.valueOf(send);
        }
    }

    /* renamed from: com.crashlytics.android.core.CrashlyticsCore.7 */
    class C03747 implements Runnable {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ OptInLatch val$latch;
        final /* synthetic */ PromptSettingsData val$promptData;
        final /* synthetic */ DialogStringResolver val$stringResolver;

        /* renamed from: com.crashlytics.android.core.CrashlyticsCore.7.1 */
        class C03711 implements OnClickListener {
            C03711() {
            }

            public void onClick(DialogInterface dialog, int which) {
                C03747.this.val$latch.setOptIn(CrashlyticsCore.CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT);
                dialog.dismiss();
            }
        }

        /* renamed from: com.crashlytics.android.core.CrashlyticsCore.7.2 */
        class C03722 implements OnClickListener {
            C03722() {
            }

            public void onClick(DialogInterface dialog, int id) {
                C03747.this.val$latch.setOptIn(CrashlyticsCore.SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT);
                dialog.dismiss();
            }
        }

        /* renamed from: com.crashlytics.android.core.CrashlyticsCore.7.3 */
        class C03733 implements OnClickListener {
            C03733() {
            }

            public void onClick(DialogInterface dialog, int id) {
                CrashlyticsCore.this.setShouldSendUserReportsWithoutPrompting(CrashlyticsCore.CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT);
                C03747.this.val$latch.setOptIn(CrashlyticsCore.CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT);
                dialog.dismiss();
            }
        }

        C03747(Activity activity, OptInLatch optInLatch, DialogStringResolver dialogStringResolver, PromptSettingsData promptSettingsData) {
            this.val$activity = activity;
            this.val$latch = optInLatch;
            this.val$stringResolver = dialogStringResolver;
            this.val$promptData = promptSettingsData;
        }

        public void run() {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this.val$activity);
            OnClickListener sendClickListener = new C03711();
            float density = this.val$activity.getResources().getDisplayMetrics().density;
            int textViewPadding = CrashlyticsCore.dipsToPixels(density, 5);
            TextView textView = new TextView(this.val$activity);
            textView.setAutoLinkMask(15);
            textView.setText(this.val$stringResolver.getMessage());
            textView.setTextAppearance(this.val$activity, 16973892);
            textView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
            textView.setFocusable(CrashlyticsCore.SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT);
            ScrollView scrollView = new ScrollView(this.val$activity);
            scrollView.setPadding(CrashlyticsCore.dipsToPixels(density, 14), CrashlyticsCore.dipsToPixels(density, 2), CrashlyticsCore.dipsToPixels(density, 10), CrashlyticsCore.dipsToPixels(density, 12));
            scrollView.addView(textView);
            builder.setView(scrollView).setTitle(this.val$stringResolver.getTitle()).setCancelable(CrashlyticsCore.SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT).setNeutralButton(this.val$stringResolver.getSendButtonTitle(), sendClickListener);
            if (this.val$promptData.showCancelButton) {
                builder.setNegativeButton(this.val$stringResolver.getCancelButtonTitle(), new C03722());
            }
            if (this.val$promptData.showAlwaysSendButton) {
                builder.setPositiveButton(this.val$stringResolver.getAlwaysSendButtonTitle(), new C03733());
            }
            builder.show();
        }
    }

    public static class Builder {
        private float delay;
        private boolean disabled;
        private CrashlyticsListener listener;
        private PinningInfoProvider pinningInfoProvider;

        public Builder() {
            this.delay = GroundOverlayOptions.NO_DIMENSION;
            this.disabled = CrashlyticsCore.SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT;
        }

        public Builder delay(float delay) {
            if (delay <= 0.0f) {
                throw new IllegalArgumentException("delay must be greater than 0");
            } else if (this.delay > 0.0f) {
                throw new IllegalStateException("delay already set.");
            } else {
                this.delay = delay;
                return this;
            }
        }

        public Builder listener(CrashlyticsListener listener) {
            if (listener == null) {
                throw new IllegalArgumentException("listener must not be null.");
            } else if (this.listener != null) {
                throw new IllegalStateException("listener already set.");
            } else {
                this.listener = listener;
                return this;
            }
        }

        @Deprecated
        public Builder pinningInfo(PinningInfoProvider pinningInfoProvider) {
            if (pinningInfoProvider == null) {
                throw new IllegalArgumentException("pinningInfoProvider must not be null.");
            } else if (this.pinningInfoProvider != null) {
                throw new IllegalStateException("pinningInfoProvider already set.");
            } else {
                this.pinningInfoProvider = pinningInfoProvider;
                return this;
            }
        }

        public Builder disabled(boolean isDisabled) {
            this.disabled = isDisabled;
            return this;
        }

        public CrashlyticsCore build() {
            if (this.delay < 0.0f) {
                this.delay = CrashlyticsCore.CLS_DEFAULT_PROCESS_DELAY;
            }
            return new CrashlyticsCore(this.delay, this.listener, this.pinningInfoProvider, this.disabled);
        }
    }

    private static final class CrashMarkerCheck implements Callable<Boolean> {
        private final CrashlyticsFileMarker crashMarker;

        public CrashMarkerCheck(CrashlyticsFileMarker crashMarker) {
            this.crashMarker = crashMarker;
        }

        public Boolean call() throws Exception {
            if (!this.crashMarker.isPresent()) {
                return Boolean.FALSE;
            }
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Found previous crash marker.");
            this.crashMarker.remove();
            return Boolean.TRUE;
        }
    }

    private static final class NoOpListener implements CrashlyticsListener {
        private NoOpListener() {
        }

        public void crashlyticsDidDetectCrashDuringPreviousExecution() {
        }
    }

    private static class OptInLatch {
        private final CountDownLatch latch;
        private boolean send;

        private OptInLatch() {
            this.send = CrashlyticsCore.SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT;
            this.latch = new CountDownLatch(1);
        }

        void setOptIn(boolean optIn) {
            this.send = optIn;
            this.latch.countDown();
        }

        boolean getOptIn() {
            return this.send;
        }

        void await() {
            try {
                this.latch.await();
            } catch (InterruptedException e) {
            }
        }
    }

    public CrashlyticsCore() {
        this(CLS_DEFAULT_PROCESS_DELAY, null, null, SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT);
    }

    CrashlyticsCore(float delay, CrashlyticsListener listener, PinningInfoProvider pinningInfo, boolean disabled) {
        this(delay, listener, pinningInfo, disabled, ExecutorUtils.buildSingleThreadExecutorService("Crashlytics Exception Handler"));
    }

    CrashlyticsCore(float delay, CrashlyticsListener listener, PinningInfoProvider pinningInfo, boolean disabled, ExecutorService crashHandlerExecutor) {
        this.userId = null;
        this.userEmail = null;
        this.userName = null;
        this.delay = delay;
        if (listener == null) {
            listener = new NoOpListener();
        }
        this.listener = listener;
        this.pinningInfo = pinningInfo;
        this.disabled = disabled;
        this.executorServiceWrapper = new CrashlyticsExecutorServiceWrapper(crashHandlerExecutor);
        this.attributes = new ConcurrentHashMap();
        this.startTime = System.currentTimeMillis();
    }

    protected boolean onPreExecute() {
        return onPreExecute(super.getContext());
    }

    boolean onPreExecute(Context context) {
        if (this.disabled) {
            return SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT;
        }
        this.apiKey = new ApiKey().getValue(context);
        if (this.apiKey == null) {
            return SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT;
        }
        Fabric.getLogger().i(TAG, "Initializing Crashlytics " + getVersion());
        this.fileStore = new FileStoreImpl(this);
        this.crashMarker = new CrashlyticsFileMarker(CRASH_MARKER_FILE_NAME, this.fileStore);
        this.initializationMarker = new CrashlyticsFileMarker(INITIALIZATION_MARKER_FILE_NAME, this.fileStore);
        try {
            setAndValidateKitProperties(context, this.apiKey);
            UnityVersionProvider unityVersionProvider = new ManifestUnityVersionProvider(context, getPackageName());
            boolean initializeSynchronously = didPreviousInitializationFail();
            checkForPreviousCrash();
            installExceptionHandler(unityVersionProvider);
            if (!initializeSynchronously || !CommonUtils.canTryConnection(context)) {
                return CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT;
            }
            finishInitSynchronously();
            return SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT;
        } catch (Throwable e) {
            throw new UnmetDependencyException(e);
        } catch (Exception e2) {
            Fabric.getLogger().e(TAG, "Crashlytics was not started due to an exception during initialization", e2);
            return SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT;
        }
    }

    private void setAndValidateKitProperties(Context context, String apiKey) throws NameNotFoundException {
        CrashlyticsPinningInfoProvider infoProvider = this.pinningInfo != null ? new CrashlyticsPinningInfoProvider(this.pinningInfo) : null;
        this.httpRequestFactory = new DefaultHttpRequestFactory(Fabric.getLogger());
        this.httpRequestFactory.setPinningInfoProvider(infoProvider);
        this.packageName = context.getPackageName();
        this.installerPackageName = getIdManager().getInstallerPackageName();
        Fabric.getLogger().d(TAG, "Installer package name is: " + this.installerPackageName);
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(this.packageName, 0);
        this.versionCode = Integer.toString(packageInfo.versionCode);
        this.versionName = packageInfo.versionName == null ? IdManager.DEFAULT_VERSION_NAME : packageInfo.versionName;
        this.buildId = CommonUtils.resolveBuildId(context);
        getBuildIdValidator(this.buildId, isRequiringBuildId(context)).validate(apiKey, this.packageName);
    }

    private void installExceptionHandler(UnityVersionProvider unityVersionProvider) {
        try {
            Fabric.getLogger().d(TAG, "Installing exception handler...");
            this.handler = new CrashlyticsUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), this.executorServiceWrapper, getIdManager(), unityVersionProvider, this.fileStore, this);
            this.handler.openSession();
            Thread.setDefaultUncaughtExceptionHandler(this.handler);
            Fabric.getLogger().d(TAG, "Successfully installed exception handler.");
        } catch (Exception e) {
            Fabric.getLogger().e(TAG, "There was a problem installing the exception handler.", e);
        }
    }

    protected Void doInBackground() {
        markInitializationStarted();
        this.handler.cleanInvalidTempFiles();
        try {
            SettingsData settingsData = Settings.getInstance().awaitSettingsData();
            if (settingsData == null) {
                Fabric.getLogger().w(TAG, "Received null settings, skipping initialization!");
            } else if (settingsData.featuresData.collectReports) {
                this.handler.finalizeSessions();
                CreateReportSpiCall call = getCreateReportSpiCall(settingsData);
                if (call == null) {
                    Fabric.getLogger().w(TAG, "Unable to create a call to upload reports.");
                    markInitializationComplete();
                } else {
                    new ReportUploader(call).uploadReports(this.delay);
                    markInitializationComplete();
                }
            } else {
                Fabric.getLogger().d(TAG, "Collection of crash reports disabled in Crashlytics settings.");
                markInitializationComplete();
            }
        } catch (Exception e) {
            Fabric.getLogger().e(TAG, "Crashlytics encountered a problem during asynchronous initialization.", e);
        } finally {
            markInitializationComplete();
        }
        return null;
    }

    public String getIdentifier() {
        return "com.crashlytics.sdk.android.crashlytics-core";
    }

    public String getVersion() {
        return "2.3.8.97";
    }

    public static CrashlyticsCore getInstance() {
        return (CrashlyticsCore) Fabric.getKit(CrashlyticsCore.class);
    }

    public PinningInfoProvider getPinningInfoProvider() {
        return !this.disabled ? this.pinningInfo : null;
    }

    public void logException(Throwable throwable) {
        if (this.disabled || !ensureFabricWithCalled("prior to logging exceptions.")) {
            return;
        }
        if (throwable == null) {
            Fabric.getLogger().log(5, TAG, "Crashlytics is ignoring a request to log a null exception.");
        } else {
            this.handler.writeNonFatalException(Thread.currentThread(), throwable);
        }
    }

    public void log(String msg) {
        doLog(3, TAG, msg);
    }

    private void doLog(int priority, String tag, String msg) {
        if (!this.disabled && ensureFabricWithCalled("prior to logging messages.")) {
            this.handler.writeToLog(System.currentTimeMillis() - this.startTime, formatLogMessage(priority, tag, msg));
        }
    }

    public void log(int priority, String tag, String msg) {
        doLog(priority, tag, msg);
        Fabric.getLogger().log(priority, Stomp.EMPTY + tag, Stomp.EMPTY + msg, CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT);
    }

    public void setUserIdentifier(String identifier) {
        if (!this.disabled) {
            this.userId = sanitizeAttribute(identifier);
            this.handler.cacheUserData(this.userId, this.userName, this.userEmail);
        }
    }

    public void setUserName(String name) {
        if (!this.disabled) {
            this.userName = sanitizeAttribute(name);
            this.handler.cacheUserData(this.userId, this.userName, this.userEmail);
        }
    }

    public void setUserEmail(String email) {
        if (!this.disabled) {
            this.userEmail = sanitizeAttribute(email);
            this.handler.cacheUserData(this.userId, this.userName, this.userEmail);
        }
    }

    public void setString(String key, String value) {
        if (!this.disabled) {
            if (key == null) {
                Context context = getContext();
                if (context == null || !CommonUtils.isAppDebuggable(context)) {
                    Fabric.getLogger().e(TAG, "Attempting to set custom attribute with null key, ignoring.", null);
                    return;
                }
                throw new IllegalArgumentException("Custom attribute key must not be null.");
            }
            key = sanitizeAttribute(key);
            if (this.attributes.size() < MAX_ATTRIBUTES || this.attributes.containsKey(key)) {
                this.attributes.put(key, value == null ? Stomp.EMPTY : sanitizeAttribute(value));
                this.handler.cacheKeyData(this.attributes);
                return;
            }
            Fabric.getLogger().d(TAG, "Exceeded maximum number of custom attributes (64)");
        }
    }

    public void setBool(String key, boolean value) {
        setString(key, Boolean.toString(value));
    }

    public void setDouble(String key, double value) {
        setString(key, Double.toString(value));
    }

    public void setFloat(String key, float value) {
        setString(key, Float.toString(value));
    }

    public void setInt(String key, int value) {
        setString(key, Integer.toString(value));
    }

    public void setLong(String key, long value) {
        setString(key, Long.toString(value));
    }

    public void crash() {
        new CrashTest().indexOutOfBounds();
    }

    public boolean verifyPinning(URL url) {
        try {
            return internalVerifyPinning(url);
        } catch (Exception e) {
            Fabric.getLogger().e(TAG, "Could not verify SSL pinning", e);
            return SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT;
        }
    }

    @Deprecated
    public synchronized void setListener(CrashlyticsListener listener) {
        Fabric.getLogger().w(TAG, "Use of setListener is deprecated.");
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null.");
        }
        this.listener = listener;
    }

    static void recordLoggedExceptionEvent(String sessionId) {
        Answers answers = (Answers) Fabric.getKit(Answers.class);
        if (answers != null) {
            answers.onException(new LoggedException(sessionId));
        }
    }

    static void recordFatalExceptionEvent(String sessionId) {
        Answers answers = (Answers) Fabric.getKit(Answers.class);
        if (answers != null) {
            answers.onException(new FatalException(sessionId));
        }
    }

    Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    BuildIdValidator getBuildIdValidator(String buildId, boolean requireBuildId) {
        return new BuildIdValidator(buildId, requireBuildId);
    }

    String getPackageName() {
        return this.packageName;
    }

    String getApiKey() {
        return this.apiKey;
    }

    String getInstallerPackageName() {
        return this.installerPackageName;
    }

    String getVersionName() {
        return this.versionName;
    }

    String getVersionCode() {
        return this.versionCode;
    }

    String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(getContext(), CRASHLYTICS_API_ENDPOINT);
    }

    String getBuildId() {
        return this.buildId;
    }

    CrashlyticsUncaughtExceptionHandler getHandler() {
        return this.handler;
    }

    String getUserIdentifier() {
        return getIdManager().canCollectUserIds() ? this.userId : null;
    }

    String getUserEmail() {
        return getIdManager().canCollectUserIds() ? this.userEmail : null;
    }

    String getUserName() {
        return getIdManager().canCollectUserIds() ? this.userName : null;
    }

    private void finishInitSynchronously() {
        PriorityCallable<Void> callable = new C03651();
        for (Task task : getDependencies()) {
            callable.addDependency(task);
        }
        Future<Void> future = getFabric().getExecutorService().submit(callable);
        Fabric.getLogger().d(TAG, "Crashlytics detected incomplete initialization on previous app launch. Will initialize synchronously.");
        try {
            future.get(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Fabric.getLogger().e(TAG, "Crashlytics was interrupted during initialization.", e);
        } catch (ExecutionException e2) {
            Fabric.getLogger().e(TAG, "Problem encountered during Crashlytics initialization.", e2);
        } catch (TimeoutException e3) {
            Fabric.getLogger().e(TAG, "Crashlytics timed out during initialization.", e3);
        }
    }

    void markInitializationStarted() {
        this.executorServiceWrapper.executeSyncLoggingException(new C03662());
    }

    void markInitializationComplete() {
        this.executorServiceWrapper.executeAsync(new C03673());
    }

    boolean didPreviousInitializationFail() {
        return ((Boolean) this.executorServiceWrapper.executeSyncLoggingException(new C03684())).booleanValue();
    }

    void setExternalCrashEventDataProvider(CrashEventDataProvider provider) {
        this.externalCrashEventDataProvider = provider;
    }

    SessionEventData getExternalCrashEventData() {
        if (this.externalCrashEventDataProvider != null) {
            return this.externalCrashEventDataProvider.getCrashEventData();
        }
        return null;
    }

    boolean internalVerifyPinning(URL url) {
        if (getPinningInfoProvider() == null) {
            return SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT;
        }
        HttpRequest httpRequest = this.httpRequestFactory.buildHttpRequest(HttpMethod.GET, url.toString());
        ((HttpsURLConnection) httpRequest.getConnection()).setInstanceFollowRedirects(SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT);
        httpRequest.code();
        return CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT;
    }

    File getSdkDirectory() {
        if (this.sdkDir == null) {
            this.sdkDir = new FileStoreImpl(this).getFilesDir();
        }
        return this.sdkDir;
    }

    boolean shouldPromptUserBeforeSendingCrashReports() {
        return ((Boolean) Settings.getInstance().withSettings(new C03695(), Boolean.valueOf(SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT))).booleanValue();
    }

    boolean shouldSendReportsWithoutPrompting() {
        return new PreferenceStoreImpl(this).get().getBoolean(PREF_ALWAYS_SEND_REPORTS_KEY, SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT);
    }

    @SuppressLint({"CommitPrefEdits"})
    void setShouldSendUserReportsWithoutPrompting(boolean send) {
        PreferenceStore prefStore = new PreferenceStoreImpl(this);
        prefStore.save(prefStore.edit().putBoolean(PREF_ALWAYS_SEND_REPORTS_KEY, send));
    }

    boolean canSendWithUserApproval() {
        return ((Boolean) Settings.getInstance().withSettings(new C03706(), Boolean.valueOf(CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT))).booleanValue();
    }

    CreateReportSpiCall getCreateReportSpiCall(SettingsData settingsData) {
        if (settingsData != null) {
            return new DefaultCreateReportSpiCall(this, getOverridenSpiEndpoint(), settingsData.appData.reportsUrl, this.httpRequestFactory);
        }
        return null;
    }

    private void checkForPreviousCrash() {
        if (Boolean.TRUE.equals((Boolean) this.executorServiceWrapper.executeSyncLoggingException(new CrashMarkerCheck(this.crashMarker)))) {
            try {
                this.listener.crashlyticsDidDetectCrashDuringPreviousExecution();
            } catch (Exception e) {
                Fabric.getLogger().e(TAG, "Exception thrown by CrashlyticsListener while notifying of previous crash.", e);
            }
        }
    }

    void createCrashMarker() {
        this.crashMarker.create();
    }

    private boolean getSendDecisionFromUser(Activity context, PromptSettingsData promptData) {
        DialogStringResolver stringResolver = new DialogStringResolver(context, promptData);
        OptInLatch latch = new OptInLatch();
        Activity activity = context;
        activity.runOnUiThread(new C03747(activity, latch, stringResolver, promptData));
        Fabric.getLogger().d(TAG, "Waiting for user opt-in.");
        latch.await();
        return latch.getOptIn();
    }

    static SessionSettingsData getSessionSettingsData() {
        SettingsData settingsData = Settings.getInstance().awaitSettingsData();
        return settingsData == null ? null : settingsData.sessionData;
    }

    private static boolean isRequiringBuildId(Context context) {
        return CommonUtils.getBooleanResourceValue(context, CRASHLYTICS_REQUIRE_BUILD_ID, CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT);
    }

    private static String formatLogMessage(int priority, String tag, String msg) {
        return CommonUtils.logPriorityToString(priority) + ReadOnlyContext.SEPARATOR + tag + " " + msg;
    }

    private static boolean ensureFabricWithCalled(String msg) {
        CrashlyticsCore instance = getInstance();
        if (instance != null && instance.handler != null) {
            return CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT;
        }
        Fabric.getLogger().e(TAG, "Crashlytics must be initialized by calling Fabric.with(Context) " + msg, null);
        return SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT;
    }

    private static String sanitizeAttribute(String input) {
        if (input == null) {
            return input;
        }
        input = input.trim();
        if (input.length() > MAX_ATTRIBUTE_SIZE) {
            return input.substring(0, MAX_ATTRIBUTE_SIZE);
        }
        return input;
    }

    private static int dipsToPixels(float density, int dips) {
        return (int) (((float) dips) * density);
    }
}
