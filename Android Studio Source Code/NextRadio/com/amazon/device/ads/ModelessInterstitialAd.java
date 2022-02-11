package com.amazon.device.ads;

import android.content.Context;
import android.view.ViewGroup;
import com.amazon.device.ads.AdError.ErrorCode;
import com.amazon.device.ads.AdProperties.AdType;
import java.util.concurrent.atomic.AtomicBoolean;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class ModelessInterstitialAd implements Ad {
    private static final String LOGTAG;
    private static final int MIN_PIXELS = 380;
    private static final double MIN_SCREEN_COVERAGE_PERCENTAGE = 0.75d;
    private static final String PUBLISHER_KEYWORD = "modeless-interstitial";
    private AdController adController;
    private final AdControllerFactory adControllerFactory;
    private AdListenerExecutor adListenerExecutor;
    private final AdListenerExecutorFactory adListenerExecutorFactory;
    private final AdLoadStarter adLoadStarter;
    private AdProperties adProperties;
    private final AdRegistrationExecutor amazonAdRegistration;
    private final Context context;
    private final ViewGroup hostedViewGroup;
    private final MobileAdsLogger logger;
    private final MobileAdsLoggerFactory loggerFactory;
    private MetricsCollector metricsCollector;
    private final AtomicBoolean previousAdExpired;
    private int timeout;

    /* renamed from: com.amazon.device.ads.ModelessInterstitialAd.1 */
    static /* synthetic */ class C03301 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$AdState;

        static {
            $SwitchMap$com$amazon$device$ads$AdState = new int[AdState.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.LOADING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.LOADED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.RENDERING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.RENDERED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.INVALID.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.DESTROYED.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    private class ModelessInterstitialAdControlCallback implements AdControlCallback {
        private ModelessInterstitialAdControlCallback() {
        }

        public boolean isAdReady(boolean deferredLoad) {
            return ModelessInterstitialAd.this.isReadyToLoad();
        }

        public void onAdLoaded(AdProperties adProperties) {
            ModelessInterstitialAd.this.onAdFetched(adProperties);
        }

        public void onAdRendered() {
            ModelessInterstitialAd.this.onAdRendered();
        }

        public void postAdRendered() {
            ModelessInterstitialAd.this.onAdRenderMetricsRecorded();
        }

        public void onAdFailed(AdError adError) {
            ModelessInterstitialAd.this.onAdFailedToLoadOrRender(adError);
        }

        public void onAdEvent(AdEvent event) {
        }

        public int adClosing() {
            return 2;
        }

        public void onAdExpired() {
            ModelessInterstitialAd.this.onAdExpired();
        }
    }

    static {
        LOGTAG = ModelessInterstitialAd.class.getSimpleName();
    }

    public ModelessInterstitialAd(ViewGroup hostedViewGroup) {
        this(hostedViewGroup, AdRegistration.getAmazonAdRegistrationExecutor(), new AdControllerFactory(), new MobileAdsLoggerFactory(), new AdLoadStarter());
    }

    ModelessInterstitialAd(ViewGroup hostedViewGroup, AdRegistrationExecutor amazonAdRegistration, AdControllerFactory adControllerFactory, MobileAdsLoggerFactory loggerFactory, AdLoadStarter adLoadStarter) {
        this(hostedViewGroup, amazonAdRegistration, adControllerFactory, loggerFactory, new AdListenerExecutorFactory(loggerFactory), adLoadStarter);
    }

    ModelessInterstitialAd(ViewGroup hostedViewGroup, AdRegistrationExecutor amazonAdRegistration, AdControllerFactory adControllerFactory, MobileAdsLoggerFactory loggerFactory, AdListenerExecutorFactory adListenerExecutorFactory, AdLoadStarter adLoadStarter) {
        this.previousAdExpired = new AtomicBoolean(false);
        if (hostedViewGroup == null) {
            throw new IllegalArgumentException("The hostedViewGroup must not be null.");
        }
        this.hostedViewGroup = hostedViewGroup;
        this.context = this.hostedViewGroup.getContext();
        this.amazonAdRegistration = amazonAdRegistration;
        this.adControllerFactory = adControllerFactory;
        this.loggerFactory = loggerFactory;
        this.logger = this.loggerFactory.createMobileAdsLogger(LOGTAG);
        this.adListenerExecutorFactory = adListenerExecutorFactory;
        this.adLoadStarter = adLoadStarter;
        initialize();
    }

    public void setListener(AdListener adListener) {
        if (adListener == null) {
            adListener = new DefaultAdListener(LOGTAG);
        }
        this.adListenerExecutor = this.adListenerExecutorFactory.createAdListenerExecutor(adListener);
    }

    public boolean loadAd(AdTargetingOptions adTargetingOptions) {
        if (isReadyToLoad()) {
            this.previousAdExpired.set(false);
            AdTargetingOptions modifiedTargetingOptions = adTargetingOptions == null ? new AdTargetingOptions() : adTargetingOptions.copy();
            modifiedTargetingOptions.addInternalPublisherKeyword(PUBLISHER_KEYWORD);
            submitMetrics();
            this.adLoadStarter.loadAds(this.timeout, modifiedTargetingOptions, new AdSlot(this.adController, modifiedTargetingOptions));
            return this.adController.getAndResetIsPrepared();
        }
        switch (C03301.$SwitchMap$com$amazon$device$ads$AdState[this.adController.getAdState().ordinal()]) {
            case Zone.PRIMARY /*1*/:
            case Zone.SECONDARY /*2*/:
            case Protocol.GGP /*3*/:
                this.logger.m645w("The modeless interstitial ad is already loading. Please wait for the loading operation to complete.");
                break;
            case Type.MF /*4*/:
                this.logger.m645w("The modeless interstitial ad has already been loaded. Please call adShown once the ad is shown.");
                break;
            case Service.RJE /*5*/:
                if (!this.adController.isExpired()) {
                    this.logger.m639e("The modeless interstitial ad could not be loaded because of an unknown issue with the web views.");
                    break;
                }
                this.adController.resetToReady();
                return loadAd(adTargetingOptions);
            case Protocol.TCP /*6*/:
                this.logger.m639e("The modeless interstitial ad has been destroyed. Please create a new ModelessInterstitialAd.");
                break;
        }
        this.metricsCollector.incrementMetric(MetricType.AD_LOAD_FAILED);
        return false;
    }

    public boolean loadAd() {
        return loadAd(null);
    }

    public boolean isLoading() {
        AdState adState = this.adController.getAdState();
        return adState.equals(AdState.LOADING) || adState.equals(AdState.LOADED) || adState.equals(AdState.RENDERING);
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean adShown() {
        AdState adState = this.adController.getAdState();
        if (this.previousAdExpired.get() || (!adState.equals(AdState.HIDDEN) && this.adController.isExpired())) {
            this.logger.m639e("The ad is unable to be shown because it has expired.");
            this.metricsCollector.stopMetric(MetricType.AD_LOADED_TO_AD_SHOW_TIME);
            this.metricsCollector.incrementMetric(MetricType.EXPIRED_AD_CALL);
        } else if (adState.equals(AdState.LOADING)) {
            this.logger.m645w("The adShown call failed because the ad cannot be shown until it has completed loading.");
        } else if (adState.equals(AdState.SHOWING)) {
            this.logger.m645w("The adShown call failed because adShown was previously called on this ad.");
        } else if (adState.equals(AdState.RENDERED) || adState.equals(AdState.HIDDEN)) {
            if (adState.equals(AdState.RENDERED)) {
                this.metricsCollector.stopMetric(MetricType.AD_LOADED_TO_AD_SHOW_TIME);
            }
            Position adPos = this.adController.getAdPosition();
            Size adSize = adPos.getSize();
            Size screenSize = this.adController.getScreenSize();
            if (doesAdSizeHaveOneSideWithAtLeastMinPixels(adSize) && isAdOnScreen(adPos, screenSize) && doesAdSizeMeetRequiredScreenPercentage(adSize, screenSize)) {
                checkIfAdAspectRatioLessThanScreenAspectRatio(adSize, screenSize);
                if (this.adController.getAdState().equals(AdState.HIDDEN)) {
                    this.metricsCollector.incrementMetric(MetricType.AD_COUNTER_RESHOWN);
                }
                setRenderedViewClickable(true);
                this.adController.adShown();
                this.metricsCollector.startMetric(MetricType.AD_SHOW_DURATION);
                return true;
            }
            this.metricsCollector.incrementMetric(MetricType.RENDER_REQUIREMENT_CHECK_FAILURE);
        } else {
            this.logger.m646w("The adShown call failed because the ad is not in a state to be shown. The ad is currently in the %s state.", adState);
        }
        return false;
    }

    public void adHidden() {
        AdState adState = this.adController.getAdState();
        if (adState.equals(AdState.HIDDEN)) {
            this.logger.m637d("The ad is already hidden from view.");
        } else if (adState.equals(AdState.SHOWING)) {
            this.adController.getMetricsCollector().stopMetric(MetricType.AD_SHOW_DURATION);
            setRenderedViewClickable(false);
            this.adController.adHidden();
        } else {
            this.logger.m645w("The ad must be shown before it can be hidden.");
        }
    }

    public void destroy() {
        this.logger.m637d("Destroying the Modeless Interstitial Ad");
        if (this.adController.getAdState().equals(AdState.SHOWING)) {
            adHidden();
        }
        submitMetrics();
        this.adController.destroy();
    }

    public boolean isReady() {
        return this.adController.getAdState().equals(AdState.RENDERED) && !this.adController.isExpired();
    }

    private void initialize() {
        this.amazonAdRegistration.initializeAds(this.context.getApplicationContext());
        setListener(null);
        buildAdController();
    }

    private void buildAdController() {
        this.adController = this.adControllerFactory.buildAdController(this.context, AdSize.SIZE_MODELESS_INTERSTITIAL);
        this.adController.setCallback(new ModelessInterstitialAdControlCallback());
        this.metricsCollector = this.adController.getMetricsCollector();
        this.metricsCollector.setAdType(AdType.MODELESS_INTERSTITIAL);
        this.metricsCollector.incrementMetric(MetricType.AD_IS_INTERSTITIAL);
    }

    private void onAdFailedToLoadOrRender(AdError adError) {
        if (adError.getCode().equals(ErrorCode.NETWORK_TIMEOUT)) {
            submitMetrics();
            buildAdController();
        }
        this.adListenerExecutor.onAdFailedToLoad(this, adError);
    }

    private void onAdFetched(AdProperties adProperties) {
        this.adProperties = adProperties;
        this.adController.render();
    }

    private void onAdRendered() {
        this.hostedViewGroup.addView(this.adController.getView());
        setRenderedViewClickable(false);
        this.adListenerExecutor.onAdLoaded(this, this.adProperties);
    }

    private boolean isReadyToLoad() {
        AdState adState = this.adController.getAdState();
        return this.adController.isExpired() || adState.equals(AdState.READY_TO_LOAD) || adState.equals(AdState.HIDDEN);
    }

    private void onAdRenderMetricsRecorded() {
        this.metricsCollector.startMetric(MetricType.AD_LOADED_TO_AD_SHOW_TIME);
    }

    private void setRenderedViewClickable(boolean clickable) {
        this.adController.setAllowClicks(clickable);
    }

    private void onAdExpired() {
        this.metricsCollector.incrementMetric(MetricType.AD_EXPIRED_BEFORE_SHOWING);
        this.previousAdExpired.set(true);
        buildAdController();
        this.adListenerExecutor.onAdExpired(this);
    }

    private void submitMetrics() {
        if (!this.adController.getMetricsCollector().isMetricsCollectorEmpty()) {
            this.adController.submitAndResetMetrics();
        }
    }

    private boolean doesAdSizeHaveOneSideWithAtLeastMinPixels(Size adSize) {
        if (adSize.getHeight() >= MIN_PIXELS || adSize.getWidth() >= MIN_PIXELS) {
            return true;
        }
        this.logger.m640e("This ModelessInterstitialAd cannot fire impression pixels or receive clicks because the height %d and width %d does not meet the requirement of one side being at least %d device independent pixels.", Integer.valueOf(adSize.getHeight()), Integer.valueOf(adSize.getWidth()), Integer.valueOf(MIN_PIXELS));
        return false;
    }

    private boolean isAdOnScreen(Position adPos, Size screenSize) {
        if (adPos.getX() >= 0 && adPos.getX() + adPos.getSize().getWidth() <= screenSize.getWidth() && adPos.getY() >= 0 && adPos.getY() + adPos.getSize().getHeight() <= screenSize.getHeight()) {
            return true;
        }
        this.logger.m639e("This ModelessInterstitialAd cannot fire impression pixels or receive clicks because it does not meet the requirement of being fully on screen.");
        return false;
    }

    private boolean doesAdSizeMeetRequiredScreenPercentage(Size adSize, Size screenSize) {
        if ((((double) adSize.getHeight()) * ((double) adSize.getWidth())) / (((double) screenSize.getHeight()) * ((double) screenSize.getWidth())) >= MIN_SCREEN_COVERAGE_PERCENTAGE) {
            return true;
        }
        this.logger.m640e("This ModelessInterstitialAd cannot fire impression pixels or receive clicks because it has a screen coverage percentage of %f which does not meet the requirement of covering at least %d percent.", Double.valueOf(100.0d * ((((double) adSize.getHeight()) * ((double) adSize.getWidth())) / (((double) screenSize.getHeight()) * ((double) screenSize.getWidth())))), Integer.valueOf(75));
        return false;
    }

    private void checkIfAdAspectRatioLessThanScreenAspectRatio(Size adSize, Size screenSize) {
        boolean adAspectRatioLessThanScreenAspectRatio = true;
        float adSizeWidth = (float) adSize.getWidth();
        float adSizeHeight = (float) adSize.getHeight();
        float screenSizeWidth = (float) screenSize.getWidth();
        float screenSizeHeight = (float) screenSize.getHeight();
        if (adSizeWidth <= adSizeHeight) {
            if (adSizeWidth / adSizeHeight >= screenSizeWidth / screenSizeHeight) {
                adAspectRatioLessThanScreenAspectRatio = false;
            }
        } else if (adSizeHeight / adSizeWidth >= screenSizeHeight / screenSizeWidth) {
            adAspectRatioLessThanScreenAspectRatio = false;
        }
        if (adAspectRatioLessThanScreenAspectRatio) {
            this.metricsCollector.incrementMetric(MetricType.AD_ASPECT_RATIO_LESS_THAN_SCREEN_ASPECT_RATIO);
            this.logger.m645w("For an optimal ad experience, the aspect ratio of the ModelessInterstitialAd should be greater than or equal to the aspect ratio of the screen.");
        }
    }
}
