package com.amazon.device.ads;

import android.graphics.Rect;
import com.amazon.device.ads.ThreadUtils.ThreadRunner;

class ExtendedAdListenerExecutor extends AdListenerExecutor {
    private final ExtendedAdListener extendedAdListener;

    /* renamed from: com.amazon.device.ads.ExtendedAdListenerExecutor.1 */
    class C03001 implements Runnable {
        final /* synthetic */ Ad val$ad;
        final /* synthetic */ Rect val$positionOnScreen;

        C03001(Ad ad, Rect rect) {
            this.val$ad = ad;
            this.val$positionOnScreen = rect;
        }

        public void run() {
            ExtendedAdListenerExecutor.this.getAdListener().onAdResized(this.val$ad, this.val$positionOnScreen);
        }
    }

    /* renamed from: com.amazon.device.ads.ExtendedAdListenerExecutor.2 */
    class C03012 implements Runnable {
        final /* synthetic */ Ad val$ad;

        C03012(Ad ad) {
            this.val$ad = ad;
        }

        public void run() {
            ExtendedAdListenerExecutor.this.getAdListener().onAdExpired(this.val$ad);
        }
    }

    public ExtendedAdListenerExecutor(ExtendedAdListener adListener, MobileAdsLoggerFactory loggerFactory) {
        super(adListener, loggerFactory);
        this.extendedAdListener = adListener;
    }

    ExtendedAdListenerExecutor(ExtendedAdListener adListener, ThreadRunner threadRunner, MobileAdsLoggerFactory loggerFactory) {
        super(adListener, threadRunner, loggerFactory);
        this.extendedAdListener = adListener;
    }

    protected ExtendedAdListener getAdListener() {
        return this.extendedAdListener;
    }

    public void onAdResized(Ad ad, Rect positionOnScreen) {
        execute(new C03001(ad, positionOnScreen));
    }

    public void onAdExpired(Ad ad) {
        execute(new C03012(ad));
    }
}
