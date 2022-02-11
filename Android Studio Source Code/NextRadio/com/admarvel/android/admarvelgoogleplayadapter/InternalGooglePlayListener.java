package com.admarvel.android.admarvelgoogleplayadapter;

import com.admarvel.android.ads.AdMarvelAd;
import com.admarvel.android.ads.AdMarvelAdapterListener;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.util.Logging;
import com.google.android.gms.ads.AdListener;
import java.lang.ref.WeakReference;
import org.apache.activemq.transport.stomp.Stomp;

class InternalGooglePlayListener extends AdListener {
    private final WeakReference f8a;

    public InternalGooglePlayListener(AdMarvelAdapterListener adMarvelAdapterListener, AdMarvelAd adMarvelAd) {
        this.f8a = new WeakReference(adMarvelAdapterListener);
    }

    public void onAdClosed() {
        Logging.log("GooglePlay Ads : onAdClosed");
    }

    public void onAdFailedToLoad(int i) {
        if (this.f8a == null || this.f8a.get() == null) {
            Logging.log("GooglePlay Ads : onAdFailedToLoad No listener found");
            return;
        }
        ((AdMarvelAdapterListener) this.f8a.get()).onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
        Logging.log("GooglePlay Ads : onAdFailedToLoad");
    }

    public void onAdLeftApplication() {
        if (this.f8a == null || this.f8a.get() == null) {
            Logging.log("GooglePlay Ads : onAdLeftApplication No listener found");
            return;
        }
        ((AdMarvelAdapterListener) this.f8a.get()).onClickAd(Stomp.EMPTY);
        Logging.log("GooglePlay Ads : onAdLeftApplication");
    }

    public void onAdLoaded() {
        if (this.f8a == null || this.f8a.get() == null) {
            Logging.log("GooglePlay Ads : onAdLoaded No listener found");
            return;
        }
        ((AdMarvelAdapterListener) this.f8a.get()).onReceiveAd();
        Logging.log("GooglePlay Ads : onAdLoaded");
    }

    public void onAdOpened() {
        Logging.log("GooglePlay Ads : onAdOpened");
    }
}
