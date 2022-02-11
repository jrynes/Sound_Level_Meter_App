package com.admarvel.android.admarvelgoogleplayadapter;

import android.content.Context;
import android.content.Intent;
import com.admarvel.android.ads.AdMarvelAd;
import com.admarvel.android.ads.AdMarvelInterstitialAdapterListener;
import com.admarvel.android.ads.AdMarvelInterstitialAds;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.util.Logging;
import com.google.android.gms.ads.AdListener;
import java.lang.ref.WeakReference;
import org.apache.activemq.transport.stomp.Stomp;

class InternalGooglePlayInterstitialListener extends AdListener {
    String f4a;
    private final WeakReference f5b;
    private final WeakReference f6c;
    private WeakReference f7d;

    public InternalGooglePlayInterstitialListener(AdMarvelInterstitialAdapterListener adMarvelInterstitialAdapterListener, AdMarvelAd adMarvelAd, Context context, String str) {
        this.f5b = new WeakReference(adMarvelInterstitialAdapterListener);
        this.f6c = new WeakReference(adMarvelAd);
        this.f7d = new WeakReference(context);
    }

    public void onAdClosed() {
        if (this.f5b == null || this.f5b.get() == null) {
            Logging.log("GooglePlay Ads : onAdClosed No listener found");
            if (this.f7d != null && this.f7d.get() != null) {
                Intent intent = new Intent();
                intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_STATE_INTENT);
                intent.putExtra("WEBVIEW_GUID", this.f4a);
                ((Context) this.f7d.get()).getApplicationContext().sendBroadcast(intent);
                return;
            }
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.f5b.get()).onCloseInterstitialAd();
        Logging.log("GooglePlay Ads : onAdClosed");
    }

    public void onAdFailedToLoad(int i) {
        AdMarvelAd adMarvelAd = (AdMarvelAd) this.f6c.get();
        if (this.f5b == null || this.f5b.get() == null || adMarvelAd == null) {
            Logging.log("GooglePlay Ads : onAdFailedToLoad No listener found");
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.f5b.get()).onFailedToReceiveInterstitialAd(SDKAdNetwork.GOOGLEPLAY, adMarvelAd.getPubId(), 205, AdMarvelUtils.getErrorReason(205), adMarvelAd);
        Logging.log("GooglePlay Ads : onAdFailedToLoad");
    }

    public void onAdLeftApplication() {
        if (this.f5b == null || this.f5b.get() == null) {
            Logging.log("GooglePlay Ads : onAdLeftApplication No listener found");
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.f5b.get()).onClickInterstitialAd(Stomp.EMPTY);
        Logging.log("GooglePlay Ads : onAdLeftApplication");
    }

    public void onAdLoaded() {
        AdMarvelAd adMarvelAd = (AdMarvelAd) this.f6c.get();
        if (this.f5b == null || this.f5b.get() == null || adMarvelAd == null) {
            Logging.log("GooglePlay Ads : onAdLoaded No listener found");
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.f5b.get()).onReceiveInterstitialAd(SDKAdNetwork.GOOGLEPLAY, adMarvelAd.getPubId(), adMarvelAd);
        Logging.log("GooglePlay Ads : onAdLoaded");
    }

    public void onAdOpened() {
        Logging.log("GooglePlay Ads : onAdOpened");
        if (this.f5b != null && this.f5b.get() != null) {
            ((AdMarvelInterstitialAdapterListener) this.f5b.get()).onInterstitialDisplayed();
        }
    }
}
