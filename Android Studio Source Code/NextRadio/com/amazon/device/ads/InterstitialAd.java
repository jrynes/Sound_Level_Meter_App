package com.amazon.device.ads;

import android.app.Activity;
import com.amazon.device.ads.AdError.ErrorCode;
import com.amazon.device.ads.AdProperties.AdType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.util.concurrent.atomic.AtomicBoolean;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class InterstitialAd implements Ad {
    protected static final String ACTION_INTERSTITIAL_DISMISSED = "dismissed";
    protected static final String ACTION_INTERSTITIAL_FINISHED_LOADING = "finished";
    protected static final String BROADCAST_ACTION = "action";
    protected static final String BROADCAST_CREATIVE = "creative";
    protected static final String BROADCAST_INTENT = "amazon.mobile.ads.interstitial";
    protected static final String BROADCAST_UNIQUE_IDENTIFIER_KEY = "uniqueIdentifier";
    private static final String LOGTAG;
    protected static final String MSG_PREPARE_AD_DESTROYED = "This interstitial ad has been destroyed and can no longer be used. Create a new InterstitialAd object to load a new ad.";
    protected static final String MSG_PREPARE_AD_LOADING = "An interstitial ad is currently loading. Please wait for the ad to finish loading and showing before loading another ad.";
    protected static final String MSG_PREPARE_AD_READY_TO_SHOW = "An interstitial ad is ready to show. Please call showAd() to show the ad before loading another ad.";
    protected static final String MSG_PREPARE_AD_SHOWING = "An interstitial ad is currently showing. Please wait for the user to dismiss the ad before loading an ad.";
    protected static final String MSG_SHOW_AD_ANOTHER_SHOWING = "Another interstitial ad is currently showing. Please wait for the InterstitialAdListener.onAdDismissed callback of the other ad.";
    protected static final String MSG_SHOW_AD_DESTROYED = "The interstitial ad cannot be shown because it has been destroyed. Create a new InterstitialAd object to load a new ad.";
    protected static final String MSG_SHOW_AD_DISMISSED = "The interstitial ad cannot be shown because it has already been displayed to the user. Please call loadAd(AdTargetingOptions) to load a new ad.";
    protected static final String MSG_SHOW_AD_EXPIRED = "This interstitial ad has expired. Please load another ad.";
    protected static final String MSG_SHOW_AD_LOADING = "The interstitial ad cannot be shown because it is still loading. Please wait for the AdListener.onAdLoaded() callback before showing the ad.";
    protected static final String MSG_SHOW_AD_READY_TO_LOAD = "The interstitial ad cannot be shown because it has not loaded successfully. Please call loadAd(AdTargetingOptions) to load an ad first.";
    protected static final String MSG_SHOW_AD_SHOWING = "The interstitial ad cannot be shown because it is already displayed on the screen. Please wait for the InterstitialAdListener.onAdDismissed() callback and then load a new ad.";
    private static final AtomicBoolean isAdShowing;
    private final Activity activity;
    private AdController adController;
    private final AdControllerFactory adControllerFactory;
    private AdListenerExecutor adListenerExecutor;
    private final AdListenerExecutorFactory adListenerExecutorFactory;
    private final AdLoadStarter adLoadStarter;
    private final AdRegistrationExecutor adRegistration;
    private final IntentBuilderFactory intentBuilderFactory;
    private boolean isInitialized;
    private boolean isThisAdShowing;
    private final MobileAdsLogger logger;
    private final MobileAdsLoggerFactory loggerFactory;
    private final AtomicBoolean previousAdExpired;
    private int timeout;

    /* renamed from: com.amazon.device.ads.InterstitialAd.1 */
    class C03111 implements Runnable {
        final /* synthetic */ AdProperties val$adProperties;

        C03111(AdProperties adProperties) {
            this.val$adProperties = adProperties;
        }

        public void run() {
            InterstitialAd.this.callOnAdLoaded(this.val$adProperties);
        }
    }

    /* renamed from: com.amazon.device.ads.InterstitialAd.2 */
    class C03122 implements Runnable {
        final /* synthetic */ AdError val$error;

        C03122(AdError adError) {
            this.val$error = adError;
        }

        public void run() {
            InterstitialAd.this.callOnAdFailedToLoad(this.val$error);
        }
    }

    /* renamed from: com.amazon.device.ads.InterstitialAd.3 */
    class C03133 implements Runnable {
        C03133() {
        }

        public void run() {
            InterstitialAd.this.callOnAdDismissed();
            InterstitialAd.this.submitAndResetMetrics();
        }
    }

    /* renamed from: com.amazon.device.ads.InterstitialAd.4 */
    class C03144 implements Runnable {
        C03144() {
        }

        public void run() {
            InterstitialAd.this.callOnAdExpired();
        }
    }

    /* renamed from: com.amazon.device.ads.InterstitialAd.5 */
    static /* synthetic */ class C03155 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$AdState;

        static {
            $SwitchMap$com$amazon$device$ads$AdState = new int[AdState.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.RENDERED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.SHOWING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.INVALID.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.DESTROYED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    class InterstitialAdControlCallback implements AdControlCallback {
        private AdProperties adProperties;

        InterstitialAdControlCallback() {
        }

        public void onAdLoaded(AdProperties adProperties) {
            this.adProperties = adProperties;
            InterstitialAd.this.setAdditionalMetrics();
            InterstitialAd.this.getAdController().enableNativeCloseButton(true, RelativePosition.TOP_RIGHT);
            InterstitialAd.this.getAdController().render();
        }

        public void onAdRendered() {
            InterstitialAd.this.callOnAdLoadedOnMainThread(this.adProperties);
        }

        public void onAdFailed(AdError adError) {
            if (ErrorCode.NETWORK_TIMEOUT.equals(adError.getCode())) {
                InterstitialAd.this.adController = null;
            }
            InterstitialAd.this.callOnAdFailedOnMainThread(adError);
        }

        public void onAdEvent(AdEvent event) {
        }

        public boolean isAdReady(boolean deferredLoad) {
            return InterstitialAd.this.isReadyToLoad();
        }

        public int adClosing() {
            InterstitialAd.this.handleDismissed();
            return 1;
        }

        public void postAdRendered() {
            InterstitialAd.this.getMetricsCollector().startMetric(MetricType.AD_LOADED_TO_AD_SHOW_TIME);
        }

        public void onAdExpired() {
            InterstitialAd.this.getMetricsCollector().incrementMetric(MetricType.AD_EXPIRED_BEFORE_SHOWING);
            InterstitialAd.this.previousAdExpired.set(true);
            InterstitialAd.this.adController = null;
            InterstitialAd.this.callOnAdExpiredOnMainThread();
        }
    }

    static {
        LOGTAG = InterstitialAd.class.getSimpleName();
        isAdShowing = new AtomicBoolean(false);
    }

    public InterstitialAd(Activity activity) {
        this(activity, new MobileAdsLoggerFactory(), new AdControllerFactory(), new IntentBuilderFactory(), AdRegistration.getAmazonAdRegistrationExecutor(), new AdLoadStarter());
    }

    InterstitialAd(Activity activity, MobileAdsLoggerFactory loggerFactory, AdControllerFactory adControllerFactory, IntentBuilderFactory intentBuilderFactory, AdRegistrationExecutor adRegistration, AdLoadStarter adLoadStarter) {
        this(activity, loggerFactory, new AdListenerExecutorFactory(loggerFactory), adControllerFactory, intentBuilderFactory, adRegistration, adLoadStarter);
    }

    InterstitialAd(Activity activity, MobileAdsLoggerFactory loggerFactory, AdListenerExecutorFactory adListenerExecutorFactory, AdControllerFactory adControllerFactory, IntentBuilderFactory intentBuilderFactory, AdRegistrationExecutor adRegistration, AdLoadStarter adLoadStarter) {
        this.isThisAdShowing = false;
        this.timeout = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
        this.isInitialized = false;
        this.previousAdExpired = new AtomicBoolean(false);
        if (activity == null) {
            throw new IllegalArgumentException("InterstitialAd requires a non-null Activity");
        }
        this.activity = activity;
        this.loggerFactory = loggerFactory;
        this.logger = this.loggerFactory.createMobileAdsLogger(LOGTAG);
        this.adListenerExecutorFactory = adListenerExecutorFactory;
        this.adControllerFactory = adControllerFactory;
        this.intentBuilderFactory = intentBuilderFactory;
        this.adRegistration = adRegistration;
        this.adLoadStarter = adLoadStarter;
    }

    private void initializeIfNecessary() {
        if (!isInitialized()) {
            this.isInitialized = true;
            this.adRegistration.initializeAds(this.activity.getApplicationContext());
            if (this.adListenerExecutor == null) {
                setListener(null);
            }
            initializeAdController();
            setAdditionalMetrics();
        }
    }

    private void initializeAdController() {
        setAdController(createAdController(this.activity));
    }

    private boolean isInitialized() {
        return this.isInitialized;
    }

    private AdController getAdController() {
        initializeIfNecessary();
        if (this.adController == null) {
            initializeAdController();
        }
        return this.adController;
    }

    static void resetIsAdShowing() {
        isAdShowing.set(false);
    }

    public void setListener(AdListener adListener) {
        if (adListener == null) {
            adListener = new DefaultAdListener(LOGTAG);
        }
        this.adListenerExecutor = this.adListenerExecutorFactory.createAdListenerExecutor(adListener);
    }

    public boolean loadAd() {
        return loadAd(null);
    }

    public boolean loadAd(AdTargetingOptions options) {
        didAdActivityFail();
        if (isReadyToLoad()) {
            this.previousAdExpired.set(false);
            this.adLoadStarter.loadAds(getTimeout(), options, new AdSlot(getAdController(), options));
            return getAdController().getAndResetIsPrepared();
        }
        switch (C03155.$SwitchMap$com$amazon$device$ads$AdState[getAdController().getAdState().ordinal()]) {
            case Zone.PRIMARY /*1*/:
                this.logger.m645w(MSG_PREPARE_AD_READY_TO_SHOW);
                return false;
            case Zone.SECONDARY /*2*/:
                this.logger.m645w(MSG_PREPARE_AD_SHOWING);
                return false;
            case Protocol.GGP /*3*/:
                if (getAdController().isExpired()) {
                    getAdController().resetToReady();
                    return loadAd(options);
                }
                this.logger.m639e("An interstitial ad could not be loaded because of an unknown issue with the web views.");
                return false;
            case Type.MF /*4*/:
                this.logger.m639e("An interstitial ad could not be loaded because the view has been destroyed.");
                return false;
            default:
                this.logger.m645w(MSG_PREPARE_AD_LOADING);
                return false;
        }
    }

    private MetricsCollector getMetricsCollector() {
        return getAdController().getMetricsCollector();
    }

    public static boolean isAdShowing() {
        return isAdShowing.get();
    }

    public boolean isLoading() {
        return getAdController().getAdState().equals(AdState.LOADING) || getAdController().getAdState().equals(AdState.LOADED) || getAdController().getAdState().equals(AdState.RENDERING);
    }

    public boolean isShowing() {
        return getAdController().getAdState().equals(AdState.SHOWING);
    }

    boolean isReadyToLoad() {
        return getAdController().getAdState().equals(AdState.READY_TO_LOAD);
    }

    boolean isReadyToShow() {
        return getAdController().getAdState().equals(AdState.RENDERED);
    }

    public boolean isReady() {
        return isReadyToShow() && !getAdController().isExpired();
    }

    boolean didAdActivityFail() {
        boolean isFailed = this.isThisAdShowing && !isAdShowing.get();
        if (isFailed) {
            getMetricsCollector().incrementMetric(MetricType.INTERSTITIAL_AD_ACTIVITY_FAILED);
            getAdController().closeAd();
        }
        return isFailed;
    }

    public boolean showAd() {
        if (didAdActivityFail()) {
            this.logger.m639e("The ad could not be shown because it previously failed to show. Please load a new ad.");
            return false;
        } else if (this.previousAdExpired.get()) {
            this.logger.m645w(MSG_SHOW_AD_EXPIRED);
            return false;
        } else {
            long renderLatencyStartTime = System.nanoTime();
            if (!isReadyToShow()) {
                if (isReadyToLoad()) {
                    this.logger.m645w(MSG_SHOW_AD_READY_TO_LOAD);
                } else if (isLoading()) {
                    this.logger.m645w(MSG_SHOW_AD_LOADING);
                } else if (isShowing()) {
                    this.logger.m645w(MSG_SHOW_AD_SHOWING);
                } else {
                    this.logger.m645w("An interstitial ad is not ready to show.");
                }
                return false;
            } else if (getAdController().isExpired()) {
                this.logger.m645w(MSG_SHOW_AD_EXPIRED);
                return false;
            } else if (isAdShowing.getAndSet(true)) {
                this.logger.m645w(MSG_SHOW_AD_ANOTHER_SHOWING);
                return false;
            } else if (getAdController().startAdDrawing()) {
                this.isThisAdShowing = true;
                getMetricsCollector().stopMetricInMillisecondsFromNanoseconds(MetricType.AD_LOADED_TO_AD_SHOW_TIME, renderLatencyStartTime);
                getMetricsCollector().startMetricInMillisecondsFromNanoseconds(MetricType.AD_SHOW_DURATION, renderLatencyStartTime);
                AdControllerFactory.cacheAdController(getAdController());
                getMetricsCollector().startMetric(MetricType.AD_SHOW_LATENCY);
                boolean activityShown = showAdInActivity();
                if (activityShown) {
                    return activityShown;
                }
                clearCachedAdController();
                getAdController().resetToReady();
                isAdShowing.set(false);
                this.isThisAdShowing = false;
                getMetricsCollector().stopMetric(MetricType.AD_LATENCY_RENDER_FAILED);
                return activityShown;
            } else {
                this.logger.m645w("Interstitial ad could not be shown.");
                return false;
            }
        }
    }

    private void clearCachedAdController() {
        AdControllerFactory.removeCachedAdController();
    }

    boolean showAdInActivity() {
        boolean isSuccess = this.intentBuilderFactory.createIntentBuilder().withClass(AdActivity.class).withContext(this.activity.getApplicationContext()).withActivity(this.activity).withExtra("adapter", InterstitialAdActivityAdapter.class.getName()).fireIntent();
        if (!isSuccess) {
            this.logger.m639e("Failed to show the interstitial ad because AdActivity could not be found.");
        }
        return isSuccess;
    }

    AdController createAdController(Activity activity) {
        return this.adControllerFactory.buildAdController(activity, AdSize.SIZE_INTERSTITIAL);
    }

    private void setAdController(AdController adController) {
        this.adController = adController;
        adController.setCallback(constructAdControlCallback());
    }

    AdControlCallback constructAdControlCallback() {
        return new InterstitialAdControlCallback();
    }

    void handleDismissed() {
        getMetricsCollector().stopMetric(MetricType.AD_SHOW_DURATION);
        AdControllerFactory.removeCachedAdController();
        isAdShowing.set(false);
        this.isThisAdShowing = false;
        callOnAdDismissedOnMainThread();
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private void callOnAdLoaded(AdProperties adProperties) {
        this.adListenerExecutor.onAdLoaded(this, adProperties);
    }

    void callOnAdLoadedOnMainThread(AdProperties adProperties) {
        ThreadUtils.executeOnMainThread(new C03111(adProperties));
    }

    void callOnAdFailedToLoad(AdError error) {
        this.adListenerExecutor.onAdFailedToLoad(this, error);
    }

    void callOnAdFailedOnMainThread(AdError error) {
        ThreadUtils.executeOnMainThread(new C03122(error));
    }

    void callOnAdDismissed() {
        this.adListenerExecutor.onAdDismissed(this);
    }

    void callOnAdDismissedOnMainThread() {
        ThreadUtils.executeOnMainThread(new C03133());
    }

    void callOnAdExpiredOnMainThread() {
        ThreadUtils.executeOnMainThread(new C03144());
    }

    void callOnAdExpired() {
        this.adListenerExecutor.onAdExpired(this);
    }

    void submitAndResetMetrics() {
        if (getMetricsCollector() != null && !getMetricsCollector().isMetricsCollectorEmpty()) {
            setAdditionalMetrics();
            getAdController().submitAndResetMetricsIfNecessary(true);
        }
    }

    private void setAdditionalMetrics() {
        getMetricsCollector().setAdType(AdType.INTERSTITIAL);
        getMetricsCollector().incrementMetric(MetricType.AD_IS_INTERSTITIAL);
    }
}
