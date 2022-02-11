package com.admarvel.android.admarvelamazonadapter;

import android.content.Context;
import android.content.Intent;
import com.admarvel.android.ads.AdMarvelAd;
import com.admarvel.android.ads.AdMarvelInterstitialAdapterListener;
import com.admarvel.android.ads.AdMarvelInterstitialAds;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.util.Logging;
import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.DefaultAdListener;
import java.lang.ref.WeakReference;

public class InternalAmazonInterstitialListener extends DefaultAdListener {
    String WEBVIEW_GUID;
    private final WeakReference adMarvelAdReference;
    private final WeakReference adMarvelInterstitialAdapterListenerReference;
    private WeakReference contextWeakRef;

    public InternalAmazonInterstitialListener(AdMarvelInterstitialAdapterListener adMarvelInterstitialAdapterListener, AdMarvelAd adMarvelAd, Context context, String str) {
        this.adMarvelInterstitialAdapterListenerReference = new WeakReference(adMarvelInterstitialAdapterListener);
        this.adMarvelAdReference = new WeakReference(adMarvelAd);
        this.contextWeakRef = new WeakReference(context);
        this.WEBVIEW_GUID = str;
    }

    public void onAdDismissed(Ad ad) {
        if (this.adMarvelInterstitialAdapterListenerReference == null || this.adMarvelInterstitialAdapterListenerReference.get() == null) {
            if (!(this.contextWeakRef == null || this.contextWeakRef.get() == null)) {
                Intent intent = new Intent();
                intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_STATE_INTENT);
                intent.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                ((Context) this.contextWeakRef.get()).getApplicationContext().sendBroadcast(intent);
            }
            Logging.log("Amazon : onCloseAd No listener found");
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.adMarvelInterstitialAdapterListenerReference.get()).onCloseInterstitialAd();
        Logging.log("Amazon : onCloseAd");
    }

    public void onAdFailedToLoad(Ad ad, AdError adError) {
        AdMarvelAd adMarvelAd = (AdMarvelAd) this.adMarvelAdReference.get();
        if (this.adMarvelInterstitialAdapterListenerReference == null || this.adMarvelInterstitialAdapterListenerReference.get() == null || adMarvelAd == null) {
            Logging.log("Amazon Ads : onAdFailedToLoad No listener found");
            if (this.contextWeakRef != null && this.contextWeakRef.get() != null) {
                Intent intent = new Intent();
                intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_STATE_INTENT);
                intent.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                ((Context) this.contextWeakRef.get()).getApplicationContext().sendBroadcast(intent);
                return;
            }
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.adMarvelInterstitialAdapterListenerReference.get()).onFailedToReceiveInterstitialAd(SDKAdNetwork.AMAZON, adMarvelAd.getPubId(), 205, AdMarvelUtils.getErrorReason(205), adMarvelAd);
        Logging.log("Amazon Ads : onAdFailedToLoad");
    }

    public void onAdLoaded(Ad ad, AdProperties adProperties) {
        AdMarvelAd adMarvelAd = (AdMarvelAd) this.adMarvelAdReference.get();
        if (this.adMarvelInterstitialAdapterListenerReference == null || this.adMarvelInterstitialAdapterListenerReference.get() == null) {
            Logging.log("Amazon : onReceiveAd No listener found");
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.adMarvelInterstitialAdapterListenerReference.get()).onReceiveInterstitialAd(SDKAdNetwork.AMAZON, adMarvelAd.getPubId(), adMarvelAd);
        Logging.log("Amazon : onReceiveAd");
    }
}
