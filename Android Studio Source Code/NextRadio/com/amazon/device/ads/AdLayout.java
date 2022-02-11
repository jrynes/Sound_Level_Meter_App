package com.amazon.device.ads;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.amazon.device.ads.AdError.ErrorCode;
import com.amazon.device.ads.AdEvent.AdEventType;
import com.amazon.device.ads.MobileAdsLogger.Level;
import java.util.Locale;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.transport.stomp.StompSubscription;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

@SuppressLint({"InlinedApi"})
public class AdLayout extends FrameLayout implements Ad {
    private static final String CONTENT_DESCRIPTION_AD_LAYOUT = "adLayoutObject";
    public static final int DEFAULT_TIMEOUT = 20000;
    static final String LAYOUT_NOT_RUN_ERR_MSG = "Can't load an ad because the view size cannot be determined.";
    static final String LAYOUT_PARAMS_NULL_ERR_MSG = "Can't load an ad because layout parameters are blank. Use setLayoutParams() to specify dimensions for this AdLayout.";
    static final String LOADING_IN_PROGRESS_LOG_MSG = "Can't load an ad because ad loading is already in progress";
    private static final String LOGTAG;
    private static ScheduledThreadPoolExecutor threadPool;
    private View activityRootView;
    private AdController adController;
    private final AdControllerFactory adControllerFactory;
    private AdListenerExecutor adListenerExecutor;
    private final AdListenerExecutorFactory adListenerExecutorFactory;
    private final AdLoadStarter adLoadStarter;
    private final AdRegistrationExecutor adRegistration;
    private final AdSize adSize;
    private AdTargetingOptions adTargetingOptions;
    private boolean attached;
    private final Context context;
    private Destroyable currentDestroyable;
    private View currentView;
    private boolean hasRegisterBroadcastReciever;
    private boolean isDestroyed;
    private boolean isInForeground;
    private boolean isInitialized;
    private boolean isParentViewMissingAtLoadTime;
    private int lastVisibility;
    private final MobileAdsLogger logger;
    private final MobileAdsLoggerFactory loggerFactory;
    private AtomicBoolean needsToLoadAdOnLayout;
    private BroadcastReceiver screenStateReceiver;
    private boolean shouldDisableWebViewHardwareAcceleration;

    /* renamed from: com.amazon.device.ads.AdLayout.1 */
    class C02761 extends BroadcastReceiver {
        C02761() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF") && AdLayout.this.isInForeground) {
                AdLayout.this.getAdController().closeAd();
            }
        }
    }

    /* renamed from: com.amazon.device.ads.AdLayout.2 */
    class C02772 implements Runnable {
        C02772() {
        }

        public void run() {
            if (AdLayout.this.getAdController().getAdState().equals(AdState.EXPANDED)) {
                AdLayout.this.getAdController().closeAd();
            }
        }
    }

    /* renamed from: com.amazon.device.ads.AdLayout.3 */
    class C02783 implements Runnable {
        C02783() {
        }

        public void run() {
            AdLayout.this.failLoadIfLayoutHasNotRun();
        }
    }

    /* renamed from: com.amazon.device.ads.AdLayout.4 */
    static /* synthetic */ class C02794 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$AdEvent$AdEventType;
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$AdState;

        static {
            $SwitchMap$com$amazon$device$ads$AdEvent$AdEventType = new int[AdEventType.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$AdEvent$AdEventType[AdEventType.EXPANDED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdEvent$AdEventType[AdEventType.CLOSED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdEvent$AdEventType[AdEventType.RESIZED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$com$amazon$device$ads$AdState = new int[AdState.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.INVALID.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.DESTROYED.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.EXPANDED.ordinal()] = 3;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    class AdLayoutAdControlCallback implements AdControlCallback {
        private AdProperties properties;

        AdLayoutAdControlCallback() {
        }

        public boolean isAdReady(boolean deferredLoad) {
            return AdLayout.this.prepareAd(deferredLoad);
        }

        public void onAdLoaded(AdProperties adProperties) {
            this.properties = adProperties;
            AdLayout.this.getAdController().render();
        }

        @SuppressLint({"InlinedApi"})
        public void onAdRendered() {
            AdLayout.this.getAdController().getMetricsCollector().startMetric(MetricType.AD_SHOW_LATENCY);
            if (AdLayout.this.currentView != null) {
                AdLayout.this.removeView(AdLayout.this.currentView);
            }
            if (AdLayout.this.currentDestroyable != null) {
                AdLayout.this.currentDestroyable.destroy();
            }
            AdLayout.this.currentView = AdLayout.this.getAdController().getView();
            AdLayout.this.currentDestroyable = AdLayout.this.getAdController().getDestroyable();
            AdLayout.this.addView(AdLayout.this.currentView, new LayoutParams(-1, -1, 17));
            notifyAdShowing(this.properties);
        }

        void notifyAdShowing(AdProperties adProperties) {
            AdLayout.this.adShown();
            AdLayout.this.getAdListenerExecutor().onAdLoaded(AdLayout.this, adProperties);
        }

        public void onAdFailed(AdError adError) {
            if (ErrorCode.NETWORK_TIMEOUT.equals(adError.getCode())) {
                AdLayout.this.adController = null;
            }
            AdLayout.this.getAdListenerExecutor().onAdFailedToLoad(AdLayout.this, adError);
        }

        public void onAdEvent(AdEvent event) {
            handleAdEvent(event);
        }

        boolean handleAdEvent(AdEvent event) {
            switch (C02794.$SwitchMap$com$amazon$device$ads$AdEvent$AdEventType[event.getAdEventType().ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    AdLayout.this.getAdListenerExecutor().onAdExpanded(AdLayout.this);
                    return true;
                case Zone.SECONDARY /*2*/:
                    AdLayout.this.getAdListenerExecutor().onAdCollapsed(AdLayout.this);
                    return true;
                case Protocol.GGP /*3*/:
                    AdLayout.this.getAdListenerExecutor().onAdResized(AdLayout.this, (Rect) event.getParameters().getParameter(AdEvent.POSITION_ON_SCREEN));
                    return true;
                default:
                    return false;
            }
        }

        public int adClosing() {
            if (AdLayout.this.getAdController().getAdState().equals(AdState.EXPANDED)) {
                return 0;
            }
            return 2;
        }

        public void postAdRendered() {
        }

        public void onAdExpired() {
        }
    }

    private static class OnLayoutChangeListenerUtil {

        /* renamed from: com.amazon.device.ads.AdLayout.OnLayoutChangeListenerUtil.1 */
        static class C02801 implements OnLayoutChangeListener {
            final /* synthetic */ AdLayout val$adLayout;

            C02801(AdLayout adLayout) {
                this.val$adLayout = adLayout;
            }

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (this.val$adLayout.getAndSetNeedsToLoadAdOnLayout(false)) {
                    this.val$adLayout.setFloatingWindowDimensions();
                    this.val$adLayout.startAdLoadUponLayout();
                    this.val$adLayout.activityRootView.removeOnLayoutChangeListener(this);
                }
            }
        }

        private OnLayoutChangeListenerUtil() {
        }

        @TargetApi(11)
        protected static void setOnLayoutChangeListenerForRoot(AdLayout adLayout) {
            adLayout.activityRootView.addOnLayoutChangeListener(new C02801(adLayout));
        }
    }

    static {
        LOGTAG = AdLayout.class.getSimpleName();
        threadPool = new ScheduledThreadPoolExecutor(1);
        threadPool.setKeepAliveTime(60, TimeUnit.SECONDS);
    }

    public AdLayout(Activity activity) {
        this(activity, AdSize.SIZE_AUTO);
    }

    public AdLayout(Activity activity, AdSize adSize) {
        this(activity, adSize, new MobileAdsLoggerFactory(), new AdControllerFactory(), AdRegistration.getAmazonAdRegistrationExecutor(), new AdLoadStarter());
    }

    AdLayout(Activity activity, AdSize adSize, MobileAdsLoggerFactory loggerFactory, AdControllerFactory adControllerFactory, AdRegistrationExecutor adRegistration, AdLoadStarter adLoadStarter) {
        this(activity, adSize, loggerFactory, new AdListenerExecutorFactory(loggerFactory), adControllerFactory, adRegistration, adLoadStarter);
    }

    AdLayout(Activity activity, AdSize adSize, MobileAdsLoggerFactory loggerFactory, AdListenerExecutorFactory adListenerExecutorFactory, AdControllerFactory adControllerFactory, AdRegistrationExecutor adRegistration, AdLoadStarter adLoadStarter) {
        super(activity);
        this.hasRegisterBroadcastReciever = false;
        this.attached = false;
        this.lastVisibility = 8;
        this.needsToLoadAdOnLayout = new AtomicBoolean(false);
        this.isParentViewMissingAtLoadTime = false;
        this.activityRootView = null;
        this.adTargetingOptions = null;
        this.isDestroyed = false;
        this.isInitialized = false;
        this.context = activity;
        this.adSize = adSize;
        this.loggerFactory = loggerFactory;
        this.logger = this.loggerFactory.createMobileAdsLogger(LOGTAG);
        this.adListenerExecutorFactory = adListenerExecutorFactory;
        this.adControllerFactory = adControllerFactory;
        this.adRegistration = adRegistration;
        this.adLoadStarter = adLoadStarter;
    }

    public AdLayout(Context context, AttributeSet attrs) {
        this(context, attrs, new MobileAdsLoggerFactory(), new AdControllerFactory(), AdRegistration.getAmazonAdRegistrationExecutor(), new AdLoadStarter());
    }

    AdLayout(Context context, AttributeSet attrs, MobileAdsLoggerFactory loggerFactory, AdControllerFactory adControllerFactory, AdRegistrationExecutor adRegistration, AdLoadStarter adLoadStarter) {
        this(context, attrs, loggerFactory, new AdListenerExecutorFactory(loggerFactory), adControllerFactory, adRegistration, adLoadStarter);
    }

    AdLayout(Context context, AttributeSet attrs, MobileAdsLoggerFactory loggerFactory, AdListenerExecutorFactory adListenerExecutorFactory, AdControllerFactory adControllerFactory, AdRegistrationExecutor adRegistration, AdLoadStarter adLoadStarter) {
        super(context, attrs);
        this.hasRegisterBroadcastReciever = false;
        this.attached = false;
        this.lastVisibility = 8;
        this.needsToLoadAdOnLayout = new AtomicBoolean(false);
        this.isParentViewMissingAtLoadTime = false;
        this.activityRootView = null;
        this.adTargetingOptions = null;
        this.isDestroyed = false;
        this.isInitialized = false;
        this.context = context;
        this.adSize = determineAdSize(attrs);
        this.loggerFactory = loggerFactory;
        this.logger = this.loggerFactory.createMobileAdsLogger(LOGTAG);
        this.adListenerExecutorFactory = adListenerExecutorFactory;
        this.adControllerFactory = adControllerFactory;
        this.adRegistration = adRegistration;
        this.adLoadStarter = adLoadStarter;
    }

    public AdLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, new MobileAdsLoggerFactory(), new AdControllerFactory(), AdRegistration.getAmazonAdRegistrationExecutor(), new AdLoadStarter());
    }

    AdLayout(Context context, AttributeSet attrs, int defStyle, MobileAdsLoggerFactory loggerFactory, AdControllerFactory adControllerFactory, AdRegistrationExecutor adRegistration, AdLoadStarter adLoadStarter) {
        this(context, attrs, defStyle, loggerFactory, new AdListenerExecutorFactory(loggerFactory), adControllerFactory, adRegistration, adLoadStarter);
    }

    AdLayout(Context context, AttributeSet attrs, int defStyle, MobileAdsLoggerFactory loggerFactory, AdListenerExecutorFactory adListenerExecutorFactory, AdControllerFactory adControllerFactory, AdRegistrationExecutor adRegistration, AdLoadStarter adLoadStarter) {
        super(context, attrs, defStyle);
        this.hasRegisterBroadcastReciever = false;
        this.attached = false;
        this.lastVisibility = 8;
        this.needsToLoadAdOnLayout = new AtomicBoolean(false);
        this.isParentViewMissingAtLoadTime = false;
        this.activityRootView = null;
        this.adTargetingOptions = null;
        this.isDestroyed = false;
        this.isInitialized = false;
        this.context = context;
        this.adSize = determineAdSize(attrs);
        this.loggerFactory = loggerFactory;
        this.logger = this.loggerFactory.createMobileAdsLogger(LOGTAG);
        this.adListenerExecutorFactory = adListenerExecutorFactory;
        this.adControllerFactory = adControllerFactory;
        this.adRegistration = adRegistration;
        this.adLoadStarter = adLoadStarter;
    }

    private AdSize determineAdSize(AttributeSet attrs) {
        String adSizeAttributeName = "adSize";
        String adsXmlNamespace = "http://schemas.android.com/apk/lib/com.amazon.device.ads";
        String adSizeAttributeValue = getAttributeValue(attrs, "http://schemas.android.com/apk/lib/com.amazon.device.ads", "adSize");
        if (adSizeAttributeValue == null) {
            adSizeAttributeValue = getAttributeValue(attrs, "http://schemas.android.com/apk/res/" + this.context.getPackageName(), "adSize");
            if (adSizeAttributeValue != null) {
                this.logger.forceLog(Level.WARN, "DEPRECATED - Please use the XML namespace \"http://schemas.android.com/apk/lib/com.amazon.device.ads\" for specifying AdLayout properties.", new Object[0]);
                if (adSizeAttributeValue.toLowerCase(Locale.US).equals("custom")) {
                    String msg = "Using \"custom\" or \"CUSTOM\" for the \"adSize\" property is no longer supported. Please specifiy a size or remove the property to use Auto Ad Size.";
                    this.logger.forceLog(Level.ERROR, msg, new Object[0]);
                    throw new IllegalArgumentException(msg);
                }
            }
        }
        return parseAdSize(adSizeAttributeValue);
    }

    MobileAdsLogger getLogger() {
        return this.logger;
    }

    boolean shouldDisableWebViewHardwareAcceleration() {
        return this.shouldDisableWebViewHardwareAcceleration;
    }

    void setShouldDisableWebViewHardwareAcceleration(boolean shouldDisableWebViewHardwareAcceleration) {
        this.shouldDisableWebViewHardwareAcceleration = shouldDisableWebViewHardwareAcceleration;
        if (this.adController != null) {
            this.adController.requestDisableHardwareAcceleration(this.shouldDisableWebViewHardwareAcceleration);
        }
    }

    void initializeIfNecessary() {
        if (!isInitialized()) {
            long initStart = System.nanoTime();
            this.logger.m637d("Initializing AdLayout.");
            this.adRegistration.initializeAds(this.context);
            setContentDescription(CONTENT_DESCRIPTION_AD_LAYOUT);
            if (isInEditMode()) {
                TextView textView = new TextView(this.context);
                textView.setText("AdLayout");
                textView.setLayoutParams(new LayoutParams(-1, -1));
                textView.setGravity(17);
                addView(textView);
                this.isInitialized = true;
                return;
            }
            this.isInForeground = getVisibility() == 0;
            setHorizontalScrollBarEnabled(false);
            setVerticalScrollBarEnabled(false);
            this.isInitialized = true;
            if (this.adListenerExecutor == null) {
                setListener(null);
            }
            initializeAdController();
            if (isWebViewDatabaseNull()) {
                this.logger.forceLog(Level.ERROR, "Disabling ads. Local cache file is inaccessible so ads will fail if we try to create a WebView. Details of this Android bug found at: http://code.google.com/p/android/issues/detail?id=10789", new Object[0]);
                this.isInitialized = false;
                return;
            }
            this.adController.getMetricsCollector().startMetricInMillisecondsFromNanoseconds(MetricType.AD_LAYOUT_INITIALIZATION, initStart);
            this.adController.getMetricsCollector().stopMetric(MetricType.AD_LAYOUT_INITIALIZATION);
        }
    }

    private void initializeAdController() {
        if (this.adController == null) {
            AdSize size;
            if (this.adSize == null) {
                size = AdSize.SIZE_AUTO;
            } else {
                size = this.adSize;
            }
            setAdController(createAdController(size, this.context));
            this.adController.requestDisableHardwareAcceleration(this.shouldDisableWebViewHardwareAcceleration);
        }
    }

    private void setAdController(AdController adController) {
        this.adController = adController;
        this.adController.setCallback(createAdControlCallback());
    }

    private AdController getAdController() {
        initializeIfNecessary();
        if (this.adController == null) {
            initializeAdController();
        }
        return this.adController;
    }

    private static String getAttributeValue(AttributeSet attrs, String namespace, String name) {
        return attrs.getAttributeValue(namespace, name);
    }

    private static AdSize parseAdSize(String string) {
        AdSize adSize = AdSize.SIZE_AUTO;
        if (string == null) {
            return adSize;
        }
        string = string.toLowerCase(Locale.US);
        if (string.equals("autonoscale")) {
            return AdSize.SIZE_AUTO_NO_SCALE;
        }
        if (string.equals(StompSubscription.AUTO_ACK)) {
            return adSize;
        }
        String[] sizes = string.split("x");
        int width = 0;
        int height = 0;
        if (sizes.length == 2) {
            width = NumberUtils.parseInt(sizes[0], 0);
            height = NumberUtils.parseInt(sizes[1], 0);
        }
        return new AdSize(width, height);
    }

    private AdController createAdController(AdSize size, Context context) {
        return this.adControllerFactory.buildAdController(context, size);
    }

    AdControlCallback createAdControlCallback() {
        return new AdLayoutAdControlCallback();
    }

    boolean isWebViewDatabaseNull() {
        return !getAdController().canShowViews();
    }

    AdData getAdData() {
        return getAdController().getAdData();
    }

    boolean isInitialized() {
        return this.isInitialized;
    }

    private void registerScreenStateBroadcastReceiver() {
        if (!this.hasRegisterBroadcastReciever) {
            this.hasRegisterBroadcastReciever = true;
            this.screenStateReceiver = new C02761();
            IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_OFF");
            filter.addAction("android.intent.action.USER_PRESENT");
            this.context.getApplicationContext().registerReceiver(this.screenStateReceiver, filter);
        }
    }

    private void unregisterScreenStateBroadcastReceiver() {
        if (this.hasRegisterBroadcastReciever) {
            this.hasRegisterBroadcastReciever = false;
            this.context.getApplicationContext().unregisterReceiver(this.screenStateReceiver);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.attached = true;
            registerScreenStateBroadcastReceiver();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attached = false;
        collapseAd();
        unregisterScreenStateBroadcastReceiver();
    }

    protected void onWindowVisibilityChanged(int visibility) {
        if (this.attached && this.lastVisibility != visibility) {
            if (visibility != 0) {
                this.isInForeground = false;
                collapseAd();
                unregisterScreenStateBroadcastReceiver();
            } else if (visibility == 0) {
                this.isInForeground = true;
            }
        }
    }

    private void collapseAd() {
        if (getAdController().getAdState().equals(AdState.EXPANDED)) {
            ThreadUtils.scheduleOnMainThread(new C02772());
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!this.isDestroyed) {
            int width = right - left;
            int height = bottom - top;
            super.onLayout(changed, left, top, right, bottom);
            if (!isInEditMode()) {
                getAdController().setWindowDimensions(width, height);
                if (getAndSetNeedsToLoadAdOnLayout(false)) {
                    startAdLoadUponLayout();
                }
            }
        }
    }

    private void startAdLoadUponLayout() {
        AdTargetingOptions options = this.adTargetingOptions;
        AdSlot adSlot = new AdSlot(getAdController(), options).setDeferredLoad(true);
        this.adLoadStarter.loadAds(getAdController().getTimeout(), options, adSlot);
        if (!getAndResetIsPrepared()) {
            onRequestError("Could not load ad on layout.");
        }
    }

    public int getTimeout() {
        if (getAdController() == null) {
            return -1;
        }
        return getAdController().getTimeout();
    }

    public void setTimeout(int timeout) {
        AdController adController = getAdController();
        if (adController != null) {
            adController.setTimeout(timeout);
        }
    }

    public AdSize getAdSize() {
        AdController adController = getAdController();
        if (adController == null) {
            return null;
        }
        return adController.getAdSize();
    }

    public boolean loadAd() {
        return loadAd(new AdTargetingOptions());
    }

    public boolean loadAd(AdTargetingOptions options) {
        if (options == null) {
            options = new AdTargetingOptions();
        }
        this.adTargetingOptions = options;
        if (getNeedsToLoadAdOnLayout()) {
            this.logger.m639e(LOADING_IN_PROGRESS_LOG_MSG);
            return false;
        }
        initializeIfNecessary();
        if (!isInitialized()) {
            this.logger.m639e("The ad could not be initialized properly.");
            return false;
        } else if (isReadyToLoad()) {
            this.adLoadStarter.loadAds(getAdController().getTimeout(), options, new AdSlot(getAdController(), options));
            if (getNeedsToLoadAdOnLayout()) {
                return true;
            }
            return getAndResetIsPrepared();
        } else {
            switch (C02794.$SwitchMap$com$amazon$device$ads$AdState[getAdController().getAdState().ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    this.logger.m639e("An ad could not be loaded because of an unknown issue with web views.");
                    return false;
                case Zone.SECONDARY /*2*/:
                    this.logger.m639e("An ad could not be loaded because the AdLayout has been destroyed.");
                    return false;
                case Protocol.GGP /*3*/:
                    this.logger.m639e("An ad could not be loaded because another ad is currently expanded.");
                    return false;
                default:
                    this.logger.m639e(LOADING_IN_PROGRESS_LOG_MSG);
                    return false;
            }
        }
    }

    private boolean isReadyToLoad() {
        return AdState.READY_TO_LOAD.equals(getAdController().getAdState()) || AdState.SHOWING.equals(getAdController().getAdState());
    }

    boolean prepareAd(boolean deferredLoad) {
        if (deferredLoad) {
            this.logger.m637d("Skipping ad layout preparation steps because the layout is already prepared.");
            return true;
        } else if (!isReadyToLoad()) {
            return false;
        } else {
            if (getNeedsToLoadAdOnLayout()) {
                this.logger.m639e(LOADING_IN_PROGRESS_LOG_MSG);
                return false;
            }
            if (getAdSize().isAuto()) {
                this.logger.m637d("Ad size to be determined automatically.");
            }
            setIsParentViewMissingAtLoadTime();
            if (getAdSize().isAuto() && getAdController().areWindowDimensionsSet()) {
                return true;
            }
            if (isLayoutRequested() && getAdSize().isAuto() && !isParentViewMissingAtLoadTime()) {
                deferAdLoadToLayoutEvent();
                return false;
            } else if (!isParentViewMissingAtLoadTime()) {
                return true;
            } else {
                this.logger.m637d("The ad's parent view is missing at load time.");
                return loadAdWhenParentViewMissing();
            }
        }
    }

    private boolean loadAdWhenParentViewMissing() {
        if (getLayoutParams() == null) {
            Metrics.getInstance().getMetricsCollector().incrementMetric(MetricType.AD_FAILED_NULL_LAYOUT_PARAMS);
            onRequestError(LAYOUT_PARAMS_NULL_ERR_MSG);
            return false;
        } else if (AndroidTargetUtils.isAtLeastAndroidAPI(11)) {
            setActivityRootView();
            if (isActivityRootViewNull()) {
                onRequestError("Ad load failed because root view could not be obtained from the activity.");
                return false;
            } else if (isActivityRootViewLayoutRequested()) {
                this.logger.m637d("Activity root view layout is requested.");
                deferAdLoadToLayoutEvent();
                setOnLayoutChangeListenerForRoot();
                return false;
            } else {
                setFloatingWindowDimensions();
                return true;
            }
        } else {
            setFloatingWindowDimensions();
            return true;
        }
    }

    void adFailed(AdError adError) {
        getAdController().adFailed(adError);
    }

    void bypassAdRenderingProcess() {
        getAdController().setAdState(AdState.RENDERING);
        getAdController().adRendered("custom-render");
    }

    void adShown() {
        getAdController().adShown();
    }

    void setOnLayoutChangeListenerForRoot() {
        OnLayoutChangeListenerUtil.setOnLayoutChangeListenerForRoot(this);
    }

    void setFloatingWindowDimensions() {
        if (getLayoutParams().width == -1 || getLayoutParams().height == -1) {
            this.logger.m637d("The requested ad will scale based on the screen's dimensions because at least one AdLayout dimension is set to MATCH_PARENT but the AdLayout is currently missing a fixed-size parent view.");
        }
        getAdController().setWindowDimensions(resolveLayoutParamForFloatingAd(true), resolveLayoutParamForFloatingAd(false));
    }

    int resolveLayoutParamForFloatingAd(boolean isWidth) {
        int value;
        if (isWidth) {
            value = getLayoutParams().width;
        } else {
            value = getLayoutParams().height;
        }
        if (value == -1) {
            if (isActivityRootViewNull()) {
                return getRawScreenDimension(isWidth);
            }
            return getActivityRootViewDimension(isWidth);
        } else if (value == -2) {
            return 0;
        } else {
            return value;
        }
    }

    int getRawScreenDimension(boolean isWidth) {
        WindowManager wm = (WindowManager) this.context.getSystemService("window");
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return isWidth ? metrics.widthPixels : metrics.heightPixels;
    }

    void deferAdLoadToLayoutEvent() {
        setNeedsToLoadAdOnLayout(true);
        scheduleTaskForCheckingIfLayoutHasRun();
    }

    void scheduleTaskForCheckingIfLayoutHasRun() {
        threadPool.schedule(new C02783(), (long) getTimeout(), TimeUnit.MILLISECONDS);
    }

    void failLoadIfLayoutHasNotRun() {
        if (getAndSetNeedsToLoadAdOnLayout(false)) {
            Metrics.getInstance().getMetricsCollector().incrementMetric(MetricType.AD_FAILED_LAYOUT_NOT_RUN);
            onRequestError(LAYOUT_NOT_RUN_ERR_MSG);
        }
    }

    boolean getNeedsToLoadAdOnLayout() {
        return this.needsToLoadAdOnLayout.get();
    }

    void setNeedsToLoadAdOnLayout(boolean value) {
        this.needsToLoadAdOnLayout.set(value);
    }

    boolean getAndSetNeedsToLoadAdOnLayout(boolean value) {
        return this.needsToLoadAdOnLayout.getAndSet(value);
    }

    boolean getAndResetIsPrepared() {
        return getAdController().getAndResetIsPrepared();
    }

    private void onRequestError(String message) {
        getAdController().onRequestError(message);
    }

    boolean isParentViewMissingAtLoadTime() {
        return this.isParentViewMissingAtLoadTime;
    }

    void setIsParentViewMissingAtLoadTime() {
        this.isParentViewMissingAtLoadTime = getParent() == null;
    }

    void setIsParentViewMissingAtLoadTime(boolean parentViewMissingAtLoadTime) {
        this.isParentViewMissingAtLoadTime = parentViewMissingAtLoadTime;
    }

    View getActivityRootView() {
        return this.activityRootView;
    }

    void setActivityRootView() {
        this.activityRootView = ((Activity) this.context).getWindow().getDecorView().findViewById(16908290).getRootView();
    }

    boolean isActivityRootViewLayoutRequested() {
        return this.activityRootView.isLayoutRequested();
    }

    boolean isActivityRootViewNull() {
        return this.activityRootView == null;
    }

    int getActivityRootViewDimension(boolean isWidth) {
        return isWidth ? this.activityRootView.getWidth() : this.activityRootView.getHeight();
    }

    public void setListener(AdListener adListener) {
        if (adListener == null) {
            adListener = new DefaultAdListener(LOGTAG);
        }
        this.adListenerExecutor = this.adListenerExecutorFactory.createAdListenerExecutor(adListener);
    }

    AdListenerExecutor getAdListenerExecutor() {
        return this.adListenerExecutor;
    }

    public boolean isLoading() {
        if (getAdController() == null) {
            return false;
        }
        return getAdController().getAdState().equals(AdState.LOADING);
    }

    public boolean isAdLoading() {
        return isLoading();
    }

    public void destroy() {
        if (isInitialized()) {
            this.logger.m637d("Destroying the AdLayout");
            this.isDestroyed = true;
            unregisterScreenStateBroadcastReceiver();
            getAdController().destroy();
        }
    }
}
