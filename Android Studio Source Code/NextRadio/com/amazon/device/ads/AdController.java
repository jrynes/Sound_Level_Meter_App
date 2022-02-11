package com.amazon.device.ads;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import com.amazon.device.ads.AdError.ErrorCode;
import com.amazon.device.ads.SDKEvent.SDKEventType;
import com.amazon.device.ads.ThreadUtils.ThreadRunner;
import com.amazon.device.ads.WebRequest.WebRequestFactory;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

class AdController implements MetricsSubmitter {
    private static final String LOGTAG;
    protected static final String MSG_PREPARE_AD_LOADING = "An ad is currently loading. Please wait for the ad to finish loading and showing before loading another ad.";
    protected static final String MSG_PREPARE_AD_READY_TO_SHOW = "An ad is ready to show. Please call showAd() to show the ad before loading another ad.";
    protected static final String MSG_PREPARE_AD_SHOWING = "An ad is currently showing. Please wait for the user to dismiss the ad before loading an ad.";
    protected static final String MSG_SHOW_AD_ANOTHER_SHOWING = "Another ad is currently showing. Please wait for the AdListener.onAdDismissed callback of the other ad.";
    protected static final String MSG_SHOW_AD_DESTROYED = "The ad cannot be shown because it has been destroyed. Create a new Ad object to load a new ad.";
    protected static final String MSG_SHOW_AD_DISMISSED = "The ad cannot be shown because it has already been displayed to the user. Please call loadAd(AdTargetingOptions) to load a new ad.";
    protected static final String MSG_SHOW_AD_EXPIRED = "This ad has expired. Please load another ad.";
    protected static final String MSG_SHOW_AD_LOADING = "The ad cannot be shown because it is still loading. Please wait for the AdListener.onAdLoaded() callback before showing the ad.";
    protected static final String MSG_SHOW_AD_READY_TO_LOAD = "The ad cannot be shown because it has not loaded successfully. Please call loadAd(AdTargetingOptions) to load an ad first.";
    protected static final String MSG_SHOW_AD_SHOWING = "The ad cannot be shown because it is already displayed on the screen. Please wait for the AdListener.onAdDismissed() callback and then load a new ad.";
    private Activity adActivity;
    private final AdCloser adCloser;
    private AdContainer adContainer;
    private final AdContainerFactory adContainerFactory;
    private AdControlAccessor adControlAccessor;
    private AdControlCallback adControlCallback;
    private AdData adData;
    private final AdHtmlPreprocessor adHtmlPreprocessor;
    private final AdSDKBridgeList adSdkBridgeList;
    private final AdSize adSize;
    private AdState adState;
    private final AdTimer adTimer;
    private final AdUrlLoader adUrlLoader;
    private final AdUtils2 adUtils;
    private int adWindowHeight;
    private int adWindowWidth;
    private final AndroidBuildInfo androidBuildInfo;
    private boolean backButtonOverridden;
    private final BridgeSelector bridgeSelector;
    private ConnectionInfo connectionInfo;
    private final Context context;
    private final DebugProperties debugProperties;
    private ViewGroup defaultParent;
    private boolean disableHardwareAccelerationRequest;
    private boolean forceDisableHardwareAcceleration;
    private final AtomicBoolean hasFinishedLoading;
    private final MobileAdsInfoStore infoStore;
    private boolean isModallyExpanded;
    private boolean isPrepared;
    private final AtomicBoolean isRendering;
    private final MobileAdsLogger logger;
    private MetricsCollector metricsCollector;
    private boolean orientationFailureMetricRecorded;
    private int originalOrientation;
    private final PermissionChecker permissionChecker;
    private double scalingMultiplier;
    private final ArrayList<SDKEventListener> sdkEventListeners;
    private int timeout;
    protected final WebUtils2 webUtils;
    private boolean windowDimensionsSet;

    class 10 implements OnKeyListener {
        10() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode != 4 || event.getRepeatCount() != 0) {
                return false;
            }
            AdController.this.onBackButtonPress();
            return true;
        }
    }

    /* renamed from: com.amazon.device.ads.AdController.1 */
    class C02671 extends TimerTask {
        C02671() {
        }

        public void run() {
            AdController.this.onAdTimedOut();
        }
    }

    /* renamed from: com.amazon.device.ads.AdController.2 */
    class C02682 implements Runnable {
        final /* synthetic */ String val$javascript;
        final /* synthetic */ boolean val$preload;

        C02682(String str, boolean z) {
            this.val$javascript = str;
            this.val$preload = z;
        }

        public void run() {
            AdController.this.getAdContainer().injectJavascript(this.val$javascript, this.val$preload);
        }
    }

    /* renamed from: com.amazon.device.ads.AdController.3 */
    class C02693 implements Runnable {
        C02693() {
        }

        public void run() {
            AdController.this.getAdControlCallback().onAdExpired();
            AdController.this.submitAndResetMetricsIfNecessary(true);
        }
    }

    /* renamed from: com.amazon.device.ads.AdController.4 */
    class C02704 extends TimerTask {
        C02704() {
        }

        public void run() {
            AdController.this.onAdExpired();
        }
    }

    /* renamed from: com.amazon.device.ads.AdController.5 */
    class C02715 implements Runnable {
        final /* synthetic */ AdError val$error;
        final /* synthetic */ boolean val$shouldSubmitMetrics;

        C02715(AdError adError, boolean z) {
            this.val$error = adError;
            this.val$shouldSubmitMetrics = z;
        }

        public void run() {
            AdController.this.getAdControlCallback().onAdFailed(this.val$error);
            AdController.this.submitAndResetMetricsIfNecessary(this.val$shouldSubmitMetrics);
        }
    }

    /* renamed from: com.amazon.device.ads.AdController.6 */
    class C02726 implements Runnable {
        final /* synthetic */ AdProperties val$adProperties;

        C02726(AdProperties adProperties) {
            this.val$adProperties = adProperties;
        }

        public void run() {
            if (AdController.this.canBeUsed()) {
                AdController.this.getAdControlCallback().onAdLoaded(this.val$adProperties);
            }
        }
    }

    /* renamed from: com.amazon.device.ads.AdController.7 */
    class C02737 implements Runnable {
        C02737() {
        }

        public void run() {
            if (AdController.this.canBeUsed()) {
                AdController.this.getAdControlCallback().onAdRendered();
            }
        }
    }

    /* renamed from: com.amazon.device.ads.AdController.8 */
    class C02748 implements Runnable {
        C02748() {
        }

        public void run() {
            if (AdController.this.canBeUsed()) {
                AdController.this.getAdControlCallback().postAdRendered();
            }
        }
    }

    /* renamed from: com.amazon.device.ads.AdController.9 */
    class C02759 implements Runnable {
        final /* synthetic */ AdEvent val$adEvent;

        C02759(AdEvent adEvent) {
            this.val$adEvent = adEvent;
        }

        public void run() {
            if (AdController.this.canBeUsed()) {
                AdController.this.getAdControlCallback().onAdEvent(this.val$adEvent);
            }
        }
    }

    private class AdControllerAdWebViewClientListener implements AdWebViewClientListener {
        private AdControllerAdWebViewClientListener() {
        }

        public void onPageFinished(WebView webView, String url) {
            if (AdController.this.getAdContainer().isCurrentView(webView)) {
                AdController.this.adRendered(url);
            }
        }

        public void onPageStarted(WebView view, String url) {
        }

        public void onLoadResource(WebView view, String url) {
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }
    }

    class DefaultAdControlCallback implements AdControlCallback {
        DefaultAdControlCallback() {
        }

        public boolean isAdReady(boolean deferredLoad) {
            AdController.this.logger.m637d("DefaultAdControlCallback isAdReady called");
            return AdController.this.getAdState().equals(AdState.READY_TO_LOAD) || AdController.this.getAdState().equals(AdState.SHOWING);
        }

        public void onAdLoaded(AdProperties adProperties) {
            AdController.this.logger.m637d("DefaultAdControlCallback onAdLoaded called");
        }

        public void onAdRendered() {
            AdController.this.logger.m637d("DefaultAdControlCallback onAdRendered called");
        }

        public void postAdRendered() {
            AdController.this.logger.m637d("DefaultAdControlCallback postAdRendered called");
        }

        public void onAdFailed(AdError adError) {
            AdController.this.logger.m637d("DefaultAdControlCallback onAdFailed called");
        }

        public void onAdEvent(AdEvent event) {
            AdController.this.logger.m637d("DefaultAdControlCallback onAdEvent called");
        }

        public int adClosing() {
            AdController.this.logger.m637d("DefaultAdControlCallback adClosing called");
            return 1;
        }

        public void onAdExpired() {
            AdController.this.logger.m637d("DefaultAdControlCallback onAdExpired called");
        }
    }

    static {
        LOGTAG = AdController.class.getSimpleName();
    }

    AdController(Context context, AdSize adSize) {
        this(context, adSize, new WebUtils2(), new MetricsCollector(), new MobileAdsLoggerFactory(), new AdUtils2(), new AdContainerFactory(), MobileAdsInfoStore.getInstance(), new PermissionChecker(), new AndroidBuildInfo(), BridgeSelector.getInstance(), new AdSDKBridgeList(), ThreadUtils.getThreadRunner(), new WebRequestFactory(), null, null, null, new AdTimer(), DebugProperties.getInstance());
    }

    AdController(Context context, AdSize adSize, WebUtils2 webUtils, MetricsCollector metricsCollector, MobileAdsLoggerFactory loggerFactory, AdUtils2 adUtils, AdContainerFactory adContainerFactory, MobileAdsInfoStore infoStore, PermissionChecker permissionChecker, AndroidBuildInfo androidBuildInfo, BridgeSelector bridgeSelector, AdSDKBridgeList adSdkBridgeList, ThreadRunner threadRunner, WebRequestFactory webRequestFactory, AdHtmlPreprocessor adHtmlPreprocessor, AdUrlLoader adUrlLoader, AdCloser adCloser, AdTimer adTimer, DebugProperties debugProperties) {
        this(context, adSize, webUtils, metricsCollector, loggerFactory, adUtils, adContainerFactory, infoStore, permissionChecker, androidBuildInfo, bridgeSelector, adSdkBridgeList, threadRunner, new AdWebViewClientFactory(webUtils, loggerFactory, androidBuildInfo), webRequestFactory, adHtmlPreprocessor, adUrlLoader, adCloser, adTimer, debugProperties);
    }

    AdController(Context context, AdSize adSize, WebUtils2 webUtils, MetricsCollector metricsCollector, MobileAdsLoggerFactory loggerFactory, AdUtils2 adUtils, AdContainerFactory adContainerFactory, MobileAdsInfoStore infoStore, PermissionChecker permissionChecker, AndroidBuildInfo androidBuildInfo, BridgeSelector bridgeSelector, AdSDKBridgeList adSdkBridgeList, ThreadRunner threadRunner, AdWebViewClientFactory adWebViewClientFactory, WebRequestFactory webRequestFactory, AdHtmlPreprocessor adHtmlPreprocessor, AdUrlLoader adUrlLoader, AdCloser adCloser, AdTimer adTimer, DebugProperties debugProperties) {
        this.timeout = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
        this.sdkEventListeners = new ArrayList();
        this.adWindowHeight = 0;
        this.adWindowWidth = 0;
        this.windowDimensionsSet = false;
        this.adState = AdState.READY_TO_LOAD;
        this.scalingMultiplier = 1.0d;
        this.isPrepared = false;
        this.defaultParent = null;
        this.isRendering = new AtomicBoolean(false);
        this.hasFinishedLoading = new AtomicBoolean(false);
        this.disableHardwareAccelerationRequest = false;
        this.forceDisableHardwareAcceleration = false;
        this.backButtonOverridden = false;
        this.isModallyExpanded = false;
        this.orientationFailureMetricRecorded = false;
        this.context = context;
        this.adSize = adSize;
        this.webUtils = webUtils;
        this.metricsCollector = metricsCollector;
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        this.adUtils = adUtils;
        this.adContainerFactory = adContainerFactory;
        this.infoStore = infoStore;
        this.permissionChecker = permissionChecker;
        this.androidBuildInfo = androidBuildInfo;
        this.bridgeSelector = bridgeSelector;
        this.adTimer = adTimer;
        this.debugProperties = debugProperties;
        this.adSdkBridgeList = adSdkBridgeList;
        if (adHtmlPreprocessor != null) {
            this.adHtmlPreprocessor = adHtmlPreprocessor;
        } else {
            this.adHtmlPreprocessor = new AdHtmlPreprocessor(bridgeSelector, this.adSdkBridgeList, getAdControlAccessor(), loggerFactory, adUtils);
        }
        if (adUrlLoader != null) {
            this.adUrlLoader = adUrlLoader;
        } else {
            ThreadRunner threadRunner2 = threadRunner;
            WebRequestFactory webRequestFactory2 = webRequestFactory;
            WebUtils2 webUtils2 = webUtils;
            MobileAdsLoggerFactory mobileAdsLoggerFactory = loggerFactory;
            this.adUrlLoader = new AdUrlLoader(threadRunner2, adWebViewClientFactory.createAdWebViewClient(context, this.adSdkBridgeList, getAdControlAccessor()), webRequestFactory2, getAdControlAccessor(), webUtils2, mobileAdsLoggerFactory, infoStore.getDeviceInfo());
        }
        this.adUrlLoader.setAdWebViewClientListener(new AdControllerAdWebViewClientListener());
        if (adCloser != null) {
            this.adCloser = adCloser;
        } else {
            this.adCloser = new AdCloser(this);
        }
    }

    AdContainer getAdContainer() {
        if (this.adContainer == null) {
            this.adContainer = createAdContainer();
            this.adContainer.disableHardwareAcceleration(shouldDisableHardwareAcceleration());
            this.adContainer.setAdWebViewClient(this.adUrlLoader.getAdWebViewClient());
        }
        return this.adContainer;
    }

    AdContainer createAdContainer() {
        return this.adContainerFactory.createAdContainer(this.context, this.adCloser);
    }

    AdControlCallback getAdControlCallback() {
        if (this.adControlCallback == null) {
            this.adControlCallback = new DefaultAdControlCallback();
        }
        return this.adControlCallback;
    }

    void setAdActivity(Activity activity) {
        this.adActivity = activity;
    }

    public void requestDisableHardwareAcceleration(boolean shouldDisable) {
        this.disableHardwareAccelerationRequest = shouldDisable;
        if (this.adContainer != null) {
            this.adContainer.disableHardwareAcceleration(shouldDisableHardwareAcceleration());
        }
    }

    private boolean shouldDisableHardwareAcceleration() {
        return this.forceDisableHardwareAcceleration || this.disableHardwareAccelerationRequest;
    }

    public AdControlAccessor getAdControlAccessor() {
        if (this.adControlAccessor == null) {
            this.adControlAccessor = new AdControlAccessor(this);
        }
        return this.adControlAccessor;
    }

    public MetricsCollector getMetricsCollector() {
        return this.metricsCollector;
    }

    public void resetMetricsCollector() {
        this.metricsCollector = new MetricsCollector();
    }

    public String getInstrumentationPixelUrl() {
        if (this.adData != null) {
            return this.adData.getInstrumentationPixelUrl();
        }
        return null;
    }

    public void setAdState(AdState adState) {
        this.logger.m638d("Changing AdState from %s to %s", this.adState, adState);
        this.adState = adState;
    }

    public AdState getAdState() {
        return this.adState;
    }

    public boolean isVisible() {
        return AdState.SHOWING.equals(getAdState()) || AdState.EXPANDED.equals(getAdState());
    }

    public boolean isModal() {
        return getAdSize().isModal() || (AdState.EXPANDED.equals(getAdState()) && this.isModallyExpanded);
    }

    public void orientationChangeAttemptedWhenNotAllowed() {
        if (!this.orientationFailureMetricRecorded) {
            this.orientationFailureMetricRecorded = true;
            getMetricsCollector().incrementMetric(MetricType.SET_ORIENTATION_FAILURE);
        }
    }

    protected Context getContext() {
        if (this.adActivity == null) {
            return this.context;
        }
        return this.adActivity;
    }

    public void setOriginalOrientation(Activity activity) {
        this.originalOrientation = activity.getRequestedOrientation();
    }

    public int getOriginalOrientation() {
        return this.originalOrientation;
    }

    public boolean getAndResetIsPrepared() {
        boolean isPrepared = this.isPrepared;
        this.isPrepared = false;
        return isPrepared;
    }

    public boolean isValid() {
        return !getAdState().equals(AdState.INVALID);
    }

    public AdData getAdData() {
        return this.adData;
    }

    public void setAdData(AdData adData) {
        this.adData = adData;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public AdSize getAdSize() {
        return this.adSize;
    }

    public int getWindowHeight() {
        return this.adWindowHeight;
    }

    public int getWindowWidth() {
        return this.adWindowWidth;
    }

    public void setWindowDimensions(int width, int height) {
        this.adWindowWidth = width;
        this.adWindowHeight = height;
        this.windowDimensionsSet = true;
    }

    void setViewDimensionsToAdDimensions() {
        if (this.adData != null) {
            int height = (int) ((((double) this.adData.getHeight()) * getScalingMultiplier()) * ((double) this.adUtils.getScalingFactorAsFloat()));
            if (height <= 0) {
                height = -1;
            }
            if (getAdSize().canUpscale()) {
                getAdContainer().setViewHeight(height);
                return;
            }
            getAdContainer().setViewLayoutParams((int) ((((double) this.adData.getWidth()) * getScalingMultiplier()) * ((double) this.adUtils.getScalingFactorAsFloat())), height, getAdSize().getGravity());
        }
    }

    public void setViewDimensionsToMatchParent() {
        getAdContainer().setViewLayoutParams(-1, -1, 17);
    }

    public boolean areWindowDimensionsSet() {
        return this.windowDimensionsSet;
    }

    public double getScalingMultiplier() {
        return this.scalingMultiplier;
    }

    public ConnectionInfo getConnectionInfo() {
        return this.connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public View getView() {
        return getAdContainer();
    }

    public Destroyable getDestroyable() {
        return getAdContainer();
    }

    public void stashView() {
        getAdContainer().stashView();
    }

    public boolean popView() {
        return getAdContainer().popView();
    }

    public int getViewWidth() {
        return getAdContainer().getWidth();
    }

    public int getViewHeight() {
        return getAdContainer().getHeight();
    }

    public String getMaxSize() {
        if (getAdSize().isAuto()) {
            return AdSize.getAsSizeString(getWindowWidth(), getWindowHeight());
        }
        return null;
    }

    public String getScalingMultiplierDescription() {
        if (getScalingMultiplier() > 1.0d) {
            return "u";
        }
        if (getScalingMultiplier() >= 1.0d || getScalingMultiplier() <= 0.0d) {
            return "n";
        }
        return "d";
    }

    public void setCallback(AdControlCallback adControlCallback) {
        this.adControlCallback = adControlCallback;
    }

    public void addSDKEventListener(SDKEventListener listener) {
        this.logger.m638d("Add SDKEventListener %s", listener);
        this.sdkEventListeners.add(listener);
    }

    public void clearSDKEventListeners() {
        this.sdkEventListeners.clear();
    }

    public void resetToReady() {
        if (canBeUsed()) {
            this.adActivity = null;
            this.isPrepared = false;
            this.adTimer.cancelTimer();
            resetMetricsCollector();
            this.orientationFailureMetricRecorded = false;
            getAdContainer().destroy();
            this.adSdkBridgeList.clear();
            this.adData = null;
            setAdState(AdState.READY_TO_LOAD);
        }
    }

    private void reset() {
        if (canBeUsed()) {
            this.isPrepared = false;
            this.adTimer.cancelTimer();
            resetMetricsCollector();
            this.orientationFailureMetricRecorded = false;
            if (this.adContainer != null) {
                this.adContainer.destroy();
                this.adSdkBridgeList.clear();
                this.adContainer = null;
            }
            this.adData = null;
        }
    }

    public boolean canShowViews() {
        return getAdContainer().canShowViews();
    }

    public boolean prepareForAdLoad(long loadAdStartTime, boolean deferredLoad) {
        if (!canBeUsed()) {
            onRequestError("An ad could not be loaded because the view has been destroyed or was not created properly.");
            return false;
        } else if (!checkDefinedActivities()) {
            String msg = "Ads cannot load unless \"com.amazon.device.ads.AdActivity\" is correctly declared as an activity in AndroidManifest.xml. Consult the online documentation for more info.";
            onRequestError("Ads cannot load unless \"com.amazon.device.ads.AdActivity\" is correctly declared as an activity in AndroidManifest.xml. Consult the online documentation for more info.");
            return false;
        } else if (!passesInternetPermissionCheck(this.context)) {
            onRequestError("Ads cannot load because the INTERNET permission is missing from the app's manifest.");
            return false;
        } else if (!isValidAppKey()) {
            onRequestError("Can't load an ad because Application Key has not been set. Did you forget to call AdRegistration.setAppKey( ... )?");
            return false;
        } else if (getAdContainer().canShowViews()) {
            if (!isReadyToLoad(deferredLoad)) {
                boolean failLoad = true;
                if (getAdState().equals(AdState.RENDERED)) {
                    if (isExpired()) {
                        failLoad = false;
                    } else {
                        this.logger.m639e(MSG_PREPARE_AD_READY_TO_SHOW);
                    }
                } else if (getAdState().equals(AdState.EXPANDED)) {
                    this.logger.m639e("An ad could not be loaded because another ad is currently expanded.");
                } else {
                    this.logger.m639e(MSG_PREPARE_AD_LOADING);
                }
                if (failLoad) {
                    return false;
                }
            }
            reset();
            getMetricsCollector().startMetricInMillisecondsFromNanoseconds(MetricType.AD_LATENCY_TOTAL, loadAdStartTime);
            getMetricsCollector().startMetricInMillisecondsFromNanoseconds(MetricType.AD_LATENCY_TOTAL_FAILURE, loadAdStartTime);
            getMetricsCollector().startMetricInMillisecondsFromNanoseconds(MetricType.AD_LATENCY_TOTAL_SUCCESS, loadAdStartTime);
            getMetricsCollector().startMetricInMillisecondsFromNanoseconds(MetricType.AD_LOAD_LATENCY_LOADAD_TO_FETCH_THREAD_REQUEST_START, loadAdStartTime);
            setAdState(AdState.LOADING);
            this.isRendering.set(false);
            setHasFinishedLoading(false);
            this.adTimer.restartTimer();
            this.adTimer.scheduleTask(new C02671(), (long) getTimeout());
            this.infoStore.getDeviceInfo().populateUserAgentString(this.context);
            this.isPrepared = true;
            return true;
        } else {
            Metrics.getInstance().getMetricsCollector().incrementMetric(MetricType.AD_FAILED_UNKNOWN_WEBVIEW_ISSUE);
            onRequestError("We will be unable to create a WebView for rendering an ad due to an unknown issue with the WebView.");
            return false;
        }
    }

    public void initialize() {
        if (canBeUsed()) {
            determineShouldForceDisableHardwareAcceleration();
            if (initializeAdContainer()) {
                calculateScalingMultiplier();
                Iterator it = this.adData.iterator();
                while (it.hasNext()) {
                    Set<AdSDKBridgeFactory> bridgeFactories = this.bridgeSelector.getBridgeFactories((AAXCreative) it.next());
                    if (bridgeFactories != null) {
                        for (AdSDKBridgeFactory bridgeFactory : bridgeFactories) {
                            addAdSDKBridge(bridgeFactory.createAdSDKBridge(getAdControlAccessor()));
                        }
                    }
                }
                adLoaded();
            }
        }
    }

    private void addAdSDKBridge(AdSDKBridge adSdkbridge) {
        this.adSdkBridgeList.addBridge(adSdkbridge);
    }

    private void calculateScalingMultiplier() {
        if (isInterstitial()) {
            this.scalingMultiplier = -1.0d;
            return;
        }
        float scalingDensity = this.infoStore.getDeviceInfo().getScalingFactorAsFloat();
        this.scalingMultiplier = this.adUtils.calculateScalingMultiplier((int) (((float) this.adData.getWidth()) * scalingDensity), (int) (((float) this.adData.getHeight()) * scalingDensity), getWindowWidth(), getWindowHeight());
        if (!getAdSize().canUpscale() && this.scalingMultiplier > 1.0d) {
            this.scalingMultiplier = 1.0d;
        }
        setViewDimensionsToAdDimensions();
    }

    private void determineShouldForceDisableHardwareAcceleration() {
        if ((AndroidTargetUtils.isAndroidAPI(this.androidBuildInfo, 14) || AndroidTargetUtils.isAndroidAPI(this.androidBuildInfo, 15)) && this.adData.getCreativeTypes().contains(AAXCreative.REQUIRES_TRANSPARENCY)) {
            this.forceDisableHardwareAcceleration = true;
        } else {
            this.forceDisableHardwareAcceleration = false;
        }
    }

    boolean initializeAdContainer() {
        try {
            getAdContainer().initialize();
            return true;
        } catch (IllegalStateException e) {
            String message = "An unknown error occurred when attempting to create the web view.";
            adFailed(new AdError(ErrorCode.INTERNAL_ERROR, "An unknown error occurred when attempting to create the web view."));
            setAdState(AdState.INVALID);
            this.logger.m639e("An unknown error occurred when attempting to create the web view.");
            return false;
        }
    }

    public void render() {
        if (canBeUsed()) {
            setAdState(AdState.RENDERING);
            long renderStartTime = System.nanoTime();
            getMetricsCollector().stopMetricInMillisecondsFromNanoseconds(MetricType.AD_LOAD_LATENCY_FINALIZE_FETCH_START_TO_RENDER_START, renderStartTime);
            getMetricsCollector().startMetricInMillisecondsFromNanoseconds(MetricType.AD_LATENCY_RENDER, renderStartTime);
            this.isRendering.set(true);
            loadHtml("http://amazon-adsystem.amazon.com/", this.adData.getCreative());
        }
    }

    public void preloadHtml(String baseUrl, String html, PreloadCallback callback) {
        loadHtml(baseUrl, html, true, callback);
    }

    public void loadHtml(String baseUrl, String html) {
        loadHtml(baseUrl, html, false, null);
    }

    public void loadHtml(String baseUrl, String html, boolean shouldPreload, PreloadCallback callback) {
        getAdContainer().removePreviousInterfaces();
        clearSDKEventListeners();
        getAdContainer().loadHtml(baseUrl, this.adHtmlPreprocessor.preprocessHtml(html, shouldPreload), shouldPreload, callback);
    }

    public void preloadUrl(String url, PreloadCallback callback) {
        this.adUrlLoader.loadUrl(url, true, callback);
    }

    public void loadUrl(String url) {
        this.adUrlLoader.loadUrl(url, false, null);
    }

    public void openUrl(String url) {
        this.adUrlLoader.openUrl(url);
    }

    public void setExpanded(boolean isExpanded) {
        if (isExpanded) {
            setAdState(AdState.EXPANDED);
        } else {
            setAdState(AdState.SHOWING);
        }
    }

    public void injectJavascript(String javascript, boolean preload) {
        ThreadUtils.executeOnMainThread(new C02682(javascript, preload));
    }

    public void destroy() {
        if (canBeUsed()) {
            closeAd();
            this.adState = AdState.DESTROYED;
            if (this.adContainer != null) {
                getAdContainer().destroy();
                this.adSdkBridgeList.clear();
                this.adContainer = null;
            }
            this.isPrepared = false;
            this.metricsCollector = null;
            this.adData = null;
            return;
        }
        this.logger.m639e("The ad cannot be destroyed because it has already been destroyed.");
    }

    protected boolean passesInternetPermissionCheck(Context context) {
        return this.permissionChecker.hasInternetPermission(context);
    }

    public void onRequestError(String message) {
        this.logger.m639e(message);
        adFailed(new AdError(ErrorCode.REQUEST_ERROR, message));
    }

    public boolean isExpired() {
        return this.adData != null && this.adData.isExpired();
    }

    public boolean canBeUsed() {
        return (AdState.DESTROYED.equals(getAdState()) || AdState.INVALID.equals(getAdState())) ? false : true;
    }

    private boolean isReadyToLoad(boolean deferredLoad) {
        return getAdControlCallback().isAdReady(deferredLoad);
    }

    public boolean startAdDrawing() {
        this.adTimer.cancelTimer();
        if (AdState.RENDERED.equals(getAdState()) && canExpireOrDraw(AdState.DRAWING)) {
            return true;
        }
        return false;
    }

    private synchronized boolean canExpireOrDraw(AdState newState) {
        boolean z;
        if (AdState.RENDERED.compareTo(getAdState()) >= 0) {
            setAdState(newState);
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public void adShown() {
        if (canBeUsed()) {
            getMetricsCollector().stopMetric(MetricType.AD_SHOW_LATENCY);
            this.adTimer.cancelTimer();
            if (canFireImpressionPixel()) {
                this.webUtils.executeWebRequestInThread(getAdData().getImpressionPixelUrl(), false);
            }
            setAdState(AdState.SHOWING);
            if (!areWindowDimensionsSet()) {
                setWindowDimensions(getView().getWidth(), getView().getHeight());
            }
            fireSDKEvent(new SDKEvent(SDKEventType.VISIBLE));
        }
    }

    private boolean canFireImpressionPixel() {
        return !getAdState().equals(AdState.HIDDEN);
    }

    public void adHidden() {
        setAdState(AdState.HIDDEN);
        fireSDKEvent(new SDKEvent(SDKEventType.HIDDEN));
    }

    void onAdTimedOut() {
        if (this.debugProperties.getDebugPropertyAsBoolean(DebugProperties.DEBUG_CAN_TIMEOUT, Boolean.valueOf(true)).booleanValue() && !getAndSetHasFinishedLoading(true)) {
            adFailedAfterTimerCheck(new AdError(ErrorCode.NETWORK_TIMEOUT, "Ad Load Timed Out"));
            setAdState(AdState.INVALID);
        }
    }

    private void onAdExpired() {
        if (AdState.RENDERED.compareTo(getAdState()) >= 0 && canExpireOrDraw(AdState.INVALID)) {
            this.logger.m637d("Ad Has Expired");
            callOnAdExpired();
        }
    }

    private void callOnAdExpired() {
        ThreadUtils.scheduleOnMainThread(new C02693());
    }

    void adFailed(AdError error) {
        if (canBeUsed() && !getAndSetHasFinishedLoading(true)) {
            this.adTimer.cancelTimer();
            adFailedAfterTimerCheck(error);
            setAdState(AdState.READY_TO_LOAD);
        }
    }

    private void adFailedAfterTimerCheck(AdError error) {
        if (getMetricsCollector() == null || getMetricsCollector().isMetricsCollectorEmpty()) {
            adFailedBeforeAdMetricsStart(error);
        } else {
            adFailedAfterAdMetricsStart(error);
        }
    }

    private void adLoaded() {
        if (canBeUsed()) {
            setAdState(AdState.LOADED);
            callOnAdLoaded(this.adData.getProperties());
        }
    }

    void adFailedBeforeAdMetricsStart(AdError error) {
        callOnAdFailedToLoad(error, false);
    }

    void adFailedAfterAdMetricsStart(AdError error) {
        accumulateAdFailureMetrics(error);
        callOnAdFailedToLoad(error, true);
    }

    void accumulateAdFailureMetrics(AdError error) {
        long renderStopTime = System.nanoTime();
        getMetricsCollector().stopMetricInMillisecondsFromNanoseconds(MetricType.AD_LATENCY_TOTAL, renderStopTime);
        getMetricsCollector().stopMetricInMillisecondsFromNanoseconds(MetricType.AD_LOAD_LATENCY_FINALIZE_FETCH_START_TO_FAILURE, renderStopTime);
        getMetricsCollector().stopMetricInMillisecondsFromNanoseconds(MetricType.AD_LATENCY_TOTAL_FAILURE, renderStopTime);
        if (error != null && (ErrorCode.NO_FILL.equals(error.getCode()) || ErrorCode.NETWORK_ERROR.equals(error.getCode()) || ErrorCode.NETWORK_TIMEOUT.equals(error.getCode()) || ErrorCode.INTERNAL_ERROR.equals(error.getCode()))) {
            getMetricsCollector().incrementMetric(MetricType.AD_LOAD_FAILED);
            if (error.getCode() == ErrorCode.NETWORK_TIMEOUT) {
                if (this.isRendering.get()) {
                    getMetricsCollector().incrementMetric(MetricType.AD_LOAD_FAILED_ON_PRERENDERING_TIMEOUT);
                } else {
                    getMetricsCollector().incrementMetric(MetricType.AD_LOAD_FAILED_ON_AAX_CALL_TIMEOUT);
                }
            }
        }
        getMetricsCollector().stopMetricInMillisecondsFromNanoseconds(MetricType.AD_LATENCY_RENDER_FAILED, renderStopTime);
        if (getAdState().equals(AdState.RENDERING)) {
            getMetricsCollector().incrementMetric(MetricType.AD_COUNTER_RENDERING_FATAL);
        }
        setAdditionalMetrics();
    }

    public void adRendered(String url) {
        if (canBeUsed()) {
            this.logger.m637d("Ad Rendered");
            if (!getAdState().equals(AdState.RENDERING)) {
                this.logger.m637d("Ad State was not Rendering. It was " + getAdState());
            } else if (!getAndSetHasFinishedLoading(true)) {
                this.isRendering.set(false);
                this.adTimer.cancelTimer();
                startExpirationTimer();
                setAdState(AdState.RENDERED);
                callOnAdRendered();
                long renderStopTime = System.nanoTime();
                if (getMetricsCollector() != null) {
                    getMetricsCollector().stopMetricInMillisecondsFromNanoseconds(MetricType.AD_LATENCY_RENDER, renderStopTime);
                    getMetricsCollector().stopMetricInMillisecondsFromNanoseconds(MetricType.AD_LATENCY_TOTAL, renderStopTime);
                    getMetricsCollector().stopMetricInMillisecondsFromNanoseconds(MetricType.AD_LATENCY_TOTAL_SUCCESS, renderStopTime);
                    setAdditionalMetrics();
                    submitAndResetMetricsIfNecessary(true);
                }
                callPostAdRendered();
            }
            fireSDKEvent(new SDKEvent(SDKEventType.RENDERED).setParameter(SettingsJsonConstants.APP_URL_KEY, url));
        }
    }

    private void startExpirationTimer() {
        long timeToExpire = getAdData().getTimeToExpire();
        if (timeToExpire > 0) {
            this.adTimer.restartTimer();
            this.adTimer.scheduleTask(new C02704(), timeToExpire);
        }
    }

    void callOnAdFailedToLoad(AdError error, boolean shouldSubmitMetrics) {
        ThreadUtils.scheduleOnMainThread(new C02715(error, shouldSubmitMetrics));
    }

    void callOnAdLoaded(AdProperties adProperties) {
        ThreadUtils.scheduleOnMainThread(new C02726(adProperties));
    }

    void callOnAdRendered() {
        ThreadUtils.scheduleOnMainThread(new C02737());
    }

    void callPostAdRendered() {
        ThreadUtils.scheduleOnMainThread(new C02748());
    }

    void callOnAdEvent(AdEvent adEvent) {
        ThreadUtils.scheduleOnMainThread(new C02759(adEvent));
    }

    void setHasFinishedLoading(boolean hasFinishedLoading) {
        this.hasFinishedLoading.set(hasFinishedLoading);
    }

    boolean getAndSetHasFinishedLoading(boolean hasFinishedLoading) {
        return this.hasFinishedLoading.getAndSet(hasFinishedLoading);
    }

    public void fireAdEvent(AdEvent event) {
        this.logger.m638d("Firing AdEvent of type %s", event.getAdEventType());
        callOnAdEvent(event);
    }

    public void fireSDKEvent(SDKEvent event) {
        this.logger.m638d("Firing SDK Event of type %s", event.getEventType());
        Iterator i$ = this.sdkEventListeners.iterator();
        while (i$.hasNext()) {
            ((SDKEventListener) i$.next()).onSDKEvent(event, getAdControlAccessor());
        }
    }

    public boolean closeAd() {
        return this.adCloser.closeAd();
    }

    public void enableNativeCloseButton(boolean showImage, RelativePosition position) {
        getAdContainer().enableNativeCloseButton(showImage, position);
    }

    public void removeNativeCloseButton() {
        getAdContainer().removeNativeCloseButton();
    }

    public void showNativeCloseButtonImage(boolean showNativeCloseButtonImage) {
        getAdContainer().showNativeCloseButtonImage(showNativeCloseButtonImage);
    }

    protected void setAdditionalMetrics() {
        this.adUtils.setConnectionMetrics(getConnectionInfo(), getMetricsCollector());
        if (getWindowHeight() == 0) {
            getMetricsCollector().incrementMetric(MetricType.ADLAYOUT_HEIGHT_ZERO);
        }
        getMetricsCollector().setMetricString(MetricType.VIEWPORT_SCALE, getScalingMultiplierDescription());
    }

    public void submitAndResetMetrics() {
        Metrics.getInstance().submitAndResetMetrics(this);
    }

    public void submitAndResetMetricsIfNecessary(boolean shouldSubmitMetrics) {
        if (shouldSubmitMetrics) {
            submitAndResetMetrics();
        }
    }

    public void moveViewToViewGroup(ViewGroup newViewGroup, LayoutParams layoutParams, boolean isModal) {
        ViewGroup currentParent = getViewParent();
        if (this.defaultParent == null) {
            this.defaultParent = currentParent;
        }
        if (currentParent != null) {
            currentParent.removeView(getView());
        }
        setViewDimensionsToMatchParent();
        newViewGroup.addView(getView(), layoutParams);
        this.isModallyExpanded = isModal;
        setExpanded(true);
        if (this.isModallyExpanded) {
            captureBackButton();
        }
    }

    public void captureBackButton() {
        getAdContainer().listenForKey(new 10());
    }

    boolean onBackButtonPress() {
        if (this.backButtonOverridden) {
            fireSDKEvent(new SDKEvent(SDKEventType.BACK_BUTTON_PRESSED));
            return true;
        }
        closeAd();
        return false;
    }

    ViewGroup getViewParent() {
        return (ViewGroup) getView().getParent();
    }

    ViewGroup getViewParentIfExpanded() {
        if (this.defaultParent == null || this.defaultParent == getView().getParent()) {
            return null;
        }
        return getViewParent();
    }

    public void moveViewBackToParent(LayoutParams params) {
        ViewGroup parent = (ViewGroup) getView().getParent();
        if (parent != null) {
            parent.removeView(getView());
        }
        setViewDimensionsToAdDimensions();
        if (this.defaultParent != null) {
            this.defaultParent.addView(getView(), params);
        }
        getAdContainer().listenForKey(null);
        setExpanded(false);
    }

    boolean checkDefinedActivities() {
        return this.adUtils.checkDefinedActivities(getContext().getApplicationContext());
    }

    boolean isValidAppKey() {
        return this.infoStore.getRegistrationInfo().getAppKey() != null;
    }

    Position getAdPosition() {
        int adWidth = getViewWidth();
        int adHeight = getViewHeight();
        if (adWidth == 0 && adHeight == 0) {
            adWidth = getWindowWidth();
            adHeight = getWindowHeight();
        }
        int width = this.adUtils.pixelToDeviceIndependentPixel(adWidth);
        int height = this.adUtils.pixelToDeviceIndependentPixel(adHeight);
        int[] onScreen = new int[2];
        getAdContainer().getLocationOnScreen(onScreen);
        View rootView = ((Activity) getContext()).findViewById(16908290);
        if (rootView == null) {
            this.logger.m645w("Could not find the activity's root view while determining ad position.");
            return null;
        }
        int[] rootViewPos = new int[2];
        rootView.getLocationOnScreen(rootViewPos);
        return new Position(new Size(width, height), this.adUtils.pixelToDeviceIndependentPixel(onScreen[0]), this.adUtils.pixelToDeviceIndependentPixel(onScreen[1] - rootViewPos[1]));
    }

    boolean isInterstitial() {
        if (SizeType.INTERSTITIAL.equals(this.adSize.getSizeType())) {
            return true;
        }
        return false;
    }

    Size getMaxExpandableSize() {
        FrameLayout rootView = (FrameLayout) ((Activity) getContext()).findViewById(16908290);
        if (rootView == null) {
            this.logger.m645w("Could not find the activity's root view while determining max expandable size.");
            return null;
        }
        return new Size(this.adUtils.pixelToDeviceIndependentPixel(rootView.getWidth()), this.adUtils.pixelToDeviceIndependentPixel(rootView.getHeight()));
    }

    Size getScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getMetrics(metrics);
        return new Size(this.adUtils.pixelToDeviceIndependentPixel(metrics.widthPixels), this.adUtils.pixelToDeviceIndependentPixel(metrics.heightPixels));
    }

    void getMetrics(DisplayMetrics metrics) {
        ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay().getMetrics(metrics);
    }

    void addJavascriptInterface(Object jsif, boolean shouldPreload, String interfaceName) {
        getAdContainer().addJavascriptInterface(jsif, shouldPreload, interfaceName);
    }

    void reload() {
        getAdContainer().reload();
    }

    void putUrlExecutorInAdWebViewClient(String scheme, UrlExecutor executor) {
        this.adUrlLoader.putUrlExecutorInAdWebViewClient(scheme, executor);
    }

    public void overrideBackButton(boolean override) {
        this.backButtonOverridden = override;
    }

    public void setAllowClicks(boolean allowClicks) {
        getAdContainer().setAllowClicks(allowClicks);
    }
}
