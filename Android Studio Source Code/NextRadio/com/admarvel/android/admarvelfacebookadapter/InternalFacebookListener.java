package com.admarvel.android.admarvelfacebookadapter;

import com.admarvel.android.ads.AdMarvelAdapterListener;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.util.Logging;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdView;
import java.lang.ref.WeakReference;
import org.apache.activemq.transport.stomp.Stomp;

public class InternalFacebookListener implements AdListener {
    private WeakReference adMarvelAdapterListenerReference;

    public InternalFacebookListener(AdMarvelAdapterListener adMarvelAdapterListener) {
        this.adMarvelAdapterListenerReference = new WeakReference(adMarvelAdapterListener);
    }

    public void onAdClicked(Ad ad) {
        if (this.adMarvelAdapterListenerReference == null || this.adMarvelAdapterListenerReference.get() == null) {
            Logging.log("Facebook SDK : onAdClicked .. No listener Found");
            return;
        }
        ((AdMarvelAdapterListener) this.adMarvelAdapterListenerReference.get()).onClickAd(Stomp.EMPTY);
        Logging.log("Facebook SDK : onAdClicked");
    }

    public void onAdLoaded(Ad ad) {
        if (ad instanceof AdView) {
            ((AdView) ad).setVisibility(0);
        }
        if (this.adMarvelAdapterListenerReference == null || this.adMarvelAdapterListenerReference.get() == null) {
            Logging.log("Facebook SDK : onAdLoaded .. No listener Found");
            return;
        }
        ((AdMarvelAdapterListener) this.adMarvelAdapterListenerReference.get()).onReceiveAd();
        Logging.log("Facebook SDK : onAdLoaded");
    }

    public void onError(Ad ad, AdError adError) {
        if (ad != null) {
            ad.destroy();
        }
        if (this.adMarvelAdapterListenerReference == null || this.adMarvelAdapterListenerReference.get() == null) {
            Logging.log("Facebook SDK : onError .. No listener Found - " + adError.getErrorMessage());
            return;
        }
        ((AdMarvelAdapterListener) this.adMarvelAdapterListenerReference.get()).onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
        Logging.log("Facebook SDK : onError - " + adError.getErrorMessage());
    }
}
