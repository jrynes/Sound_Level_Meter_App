package com.amazon.device.ads;

import android.graphics.Rect;
import com.amazon.device.ads.ThreadUtils.ExecutionStyle;
import com.amazon.device.ads.ThreadUtils.ExecutionThread;
import com.amazon.device.ads.ThreadUtils.ThreadRunner;

class AdListenerExecutor {
    private static final String LOGTAG;
    private final AdListener adListener;
    private final MobileAdsLogger logger;
    private final ThreadRunner threadRunner;

    /* renamed from: com.amazon.device.ads.AdListenerExecutor.1 */
    class C02811 implements Runnable {
        final /* synthetic */ Ad val$ad;
        final /* synthetic */ AdProperties val$adProperties;

        C02811(Ad ad, AdProperties adProperties) {
            this.val$ad = ad;
            this.val$adProperties = adProperties;
        }

        public void run() {
            AdListenerExecutor.this.getAdListener().onAdLoaded(this.val$ad, this.val$adProperties);
        }
    }

    /* renamed from: com.amazon.device.ads.AdListenerExecutor.2 */
    class C02822 implements Runnable {
        final /* synthetic */ Ad val$ad;
        final /* synthetic */ AdError val$adError;

        C02822(Ad ad, AdError adError) {
            this.val$ad = ad;
            this.val$adError = adError;
        }

        public void run() {
            AdListenerExecutor.this.getAdListener().onAdFailedToLoad(this.val$ad, this.val$adError);
        }
    }

    /* renamed from: com.amazon.device.ads.AdListenerExecutor.3 */
    class C02833 implements Runnable {
        final /* synthetic */ Ad val$ad;

        C02833(Ad ad) {
            this.val$ad = ad;
        }

        public void run() {
            AdListenerExecutor.this.getAdListener().onAdExpanded(this.val$ad);
        }
    }

    /* renamed from: com.amazon.device.ads.AdListenerExecutor.4 */
    class C02844 implements Runnable {
        final /* synthetic */ Ad val$ad;

        C02844(Ad ad) {
            this.val$ad = ad;
        }

        public void run() {
            AdListenerExecutor.this.getAdListener().onAdCollapsed(this.val$ad);
        }
    }

    /* renamed from: com.amazon.device.ads.AdListenerExecutor.5 */
    class C02855 implements Runnable {
        final /* synthetic */ Ad val$ad;

        C02855(Ad ad) {
            this.val$ad = ad;
        }

        public void run() {
            AdListenerExecutor.this.getAdListener().onAdDismissed(this.val$ad);
        }
    }

    static {
        LOGTAG = AdListenerExecutor.class.getSimpleName();
    }

    public AdListenerExecutor(AdListener adListener, MobileAdsLoggerFactory loggerFactory) {
        this(adListener, ThreadUtils.getThreadRunner(), loggerFactory);
    }

    AdListenerExecutor(AdListener adListener, ThreadRunner threadRunner, MobileAdsLoggerFactory loggerFactory) {
        this.adListener = adListener;
        this.threadRunner = threadRunner;
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
    }

    protected AdListener getAdListener() {
        return this.adListener;
    }

    public void onAdLoaded(Ad ad, AdProperties adProperties) {
        execute(new C02811(ad, adProperties));
    }

    public void onAdFailedToLoad(Ad ad, AdError adError) {
        execute(new C02822(ad, adError));
    }

    public void onAdExpanded(Ad ad) {
        execute(new C02833(ad));
    }

    public void onAdCollapsed(Ad ad) {
        execute(new C02844(ad));
    }

    public void onAdDismissed(Ad ad) {
        execute(new C02855(ad));
    }

    public void onAdResized(Ad ad, Rect positionOnScreen) {
        this.logger.m637d("Ad listener called - Ad Resized.");
    }

    public void onAdExpired(Ad ad) {
        this.logger.m637d("Ad listener called - Ad Expired.");
    }

    public void onSpecialUrlClicked(Ad ad, String url) {
        this.logger.m637d("Ad listener called - Special Url Clicked.");
    }

    public ActionCode onAdReceived(Ad ad, AdData adData) {
        this.logger.m637d("Ad listener called - Ad Received.");
        return null;
    }

    public void onAdEvent(AdEvent adEvent) {
        this.logger.m637d("Ad listener called - Ad Event: " + adEvent);
    }

    protected void execute(Runnable runnable) {
        this.threadRunner.execute(runnable, ExecutionStyle.SCHEDULE, ExecutionThread.MAIN_THREAD);
    }
}
