package com.admarvel.android.admarvelfacebookadapter;

import android.content.Context;
import android.content.Intent;
import com.admarvel.android.ads.AdMarvelAd;
import com.admarvel.android.ads.AdMarvelInterstitialAdapterListener;
import com.admarvel.android.ads.AdMarvelInterstitialAds;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.util.Logging;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import java.lang.ref.WeakReference;
import org.apache.activemq.transport.stomp.Stomp;

public class InternalFacebookInterstitialListener implements InterstitialAdListener {
    private String WEBVIEW_GUID;
    private final WeakReference adMarvelAdReference;
    private WeakReference adMarvelInterstitialAdapterListenerReference;
    private WeakReference contextWeakRef;
    private final WeakReference interstitialAdReference;

    public InternalFacebookInterstitialListener(AdMarvelInterstitialAdapterListener adMarvelInterstitialAdapterListener, AdMarvelAd adMarvelAd, InterstitialAd interstitialAd, Context context, String str) {
        this.adMarvelInterstitialAdapterListenerReference = new WeakReference(adMarvelInterstitialAdapterListener);
        this.adMarvelAdReference = new WeakReference(adMarvelAd);
        this.interstitialAdReference = new WeakReference(interstitialAd);
        this.contextWeakRef = new WeakReference(context);
        this.WEBVIEW_GUID = str;
    }

    public void onAdClicked(Ad ad) {
        if (this.adMarvelInterstitialAdapterListenerReference == null || this.adMarvelInterstitialAdapterListenerReference.get() == null) {
            Logging.log("Facebook SDK : onAdClicked No listener found");
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.adMarvelInterstitialAdapterListenerReference.get()).onClickInterstitialAd(Stomp.EMPTY);
        Logging.log("Facebook SDK : onAdClicked");
    }

    public void onAdLoaded(Ad ad) {
        AdMarvelAd adMarvelAd = (AdMarvelAd) this.adMarvelAdReference.get();
        if (this.adMarvelInterstitialAdapterListenerReference == null || this.adMarvelInterstitialAdapterListenerReference.get() == null || adMarvelAd == null) {
            Logging.log("Facebook SDK : onAdLoaded No listenr found");
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.adMarvelInterstitialAdapterListenerReference.get()).onReceiveInterstitialAd(SDKAdNetwork.FACEBOOK, adMarvelAd.getPubId(), adMarvelAd);
        Logging.log("Facebook SDK : onAdLoaded");
    }

    public void onError(Ad ad, AdError adError) {
        AdMarvelAd adMarvelAd = (AdMarvelAd) this.adMarvelAdReference.get();
        if (this.adMarvelInterstitialAdapterListenerReference == null || this.adMarvelInterstitialAdapterListenerReference.get() == null || adMarvelAd == null) {
            Logging.log("Facebook SDK : onError No listenr found - " + adError.getErrorMessage());
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.adMarvelInterstitialAdapterListenerReference.get()).onFailedToReceiveInterstitialAd(SDKAdNetwork.FACEBOOK, adMarvelAd.getPubId(), 205, AdMarvelUtils.getErrorReason(205), adMarvelAd);
        Logging.log("Facebook SDK : onError - " + adError.getErrorMessage());
    }

    public void onInterstitialDismissed(Ad ad) {
        if (this.interstitialAdReference != null) {
            InterstitialAd interstitialAd = (InterstitialAd) this.interstitialAdReference.get();
            if (interstitialAd != null) {
                interstitialAd.destroy();
            }
        }
        if (this.adMarvelInterstitialAdapterListenerReference == null || this.adMarvelInterstitialAdapterListenerReference.get() == null) {
            Logging.log("Facebook SDK : onAdDismissed No listenr found");
            if (this.contextWeakRef != null && this.contextWeakRef.get() != null) {
                Intent intent = new Intent();
                intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_STATE_INTENT);
                intent.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                ((Context) this.contextWeakRef.get()).getApplicationContext().sendBroadcast(intent);
                return;
            }
            return;
        }
        ((AdMarvelInterstitialAdapterListener) this.adMarvelInterstitialAdapterListenerReference.get()).onCloseInterstitialAd();
        Logging.log("Facebook SDK : onAdDismissed");
    }

    public void onInterstitialDisplayed(Ad ad) {
        Logging.log("Facebook SDK : onInterstitialDisplayed");
        if (this.adMarvelInterstitialAdapterListenerReference != null && this.adMarvelInterstitialAdapterListenerReference.get() != null) {
            ((AdMarvelInterstitialAdapterListener) this.adMarvelInterstitialAdapterListenerReference.get()).onInterstitialDisplayed();
        }
    }
}
