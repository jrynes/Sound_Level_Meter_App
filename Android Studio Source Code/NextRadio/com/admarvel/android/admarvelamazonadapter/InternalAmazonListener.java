package com.admarvel.android.admarvelamazonadapter;

import android.graphics.Rect;
import com.admarvel.android.ads.AdMarvelAdapterListener;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.util.Logging;
import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.DefaultAdListener;
import com.amazon.device.ads.ExtendedAdListener;
import java.lang.ref.WeakReference;

public class InternalAmazonListener extends DefaultAdListener implements ExtendedAdListener {
    private final WeakReference adMarvelAdapterListenerReference;

    public InternalAmazonListener(AdMarvelAdapterListener adMarvelAdapterListener) {
        this.adMarvelAdapterListenerReference = new WeakReference(adMarvelAdapterListener);
    }

    public void onAdCollapsed(Ad ad) {
        if (this.adMarvelAdapterListenerReference == null || this.adMarvelAdapterListenerReference.get() == null) {
            Logging.log("Amazon SDK - onAdCollapsed --- No listener found");
            return;
        }
        ((AdMarvelAdapterListener) this.adMarvelAdapterListenerReference.get()).onClose();
        Logging.log("Amazon SDK - onAdCollapsed");
    }

    public void onAdDismissed(Ad ad) {
        if (this.adMarvelAdapterListenerReference == null || this.adMarvelAdapterListenerReference.get() == null) {
            Logging.log("Amazon SDK - onAdDismissed --- No listener found");
            return;
        }
        ((AdMarvelAdapterListener) this.adMarvelAdapterListenerReference.get()).onClose();
        Logging.log("Amazon SDK - onAdDismissed");
    }

    public void onAdExpanded(Ad ad) {
        if (this.adMarvelAdapterListenerReference == null || this.adMarvelAdapterListenerReference.get() == null) {
            Logging.log("Amazon SDK - onAdExpanded --- No listener found");
            return;
        }
        ((AdMarvelAdapterListener) this.adMarvelAdapterListenerReference.get()).onExpand();
        Logging.log("Amazon SDK - onAdExpanded");
    }

    public void onAdFailedToLoad(Ad ad, AdError adError) {
        if (this.adMarvelAdapterListenerReference == null || this.adMarvelAdapterListenerReference.get() == null) {
            Logging.log("Amazon SDK - onAdFailedToLoad --- No listener found");
            return;
        }
        ((AdMarvelAdapterListener) this.adMarvelAdapterListenerReference.get()).onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
        Logging.log("Amazon SDK - onAdFailedToLoad");
    }

    public void onAdLoaded(Ad ad, AdProperties adProperties) {
        if (this.adMarvelAdapterListenerReference == null || this.adMarvelAdapterListenerReference.get() == null) {
            Logging.log("Amazon SDK - onAdLoaded --- No listener found");
            return;
        }
        ((AdMarvelAdapterListener) this.adMarvelAdapterListenerReference.get()).onReceiveAd();
        Logging.log("Amazon SDK - onAdLoaded");
    }

    public void onAdResized(Ad ad, Rect rect) {
        if (this.adMarvelAdapterListenerReference == null || this.adMarvelAdapterListenerReference.get() == null) {
            Logging.log("Amazon SDK - onAdResized --- No listener found");
        } else {
            ((AdMarvelAdapterListener) this.adMarvelAdapterListenerReference.get()).onExpand();
            Logging.log("Amazon SDK - onAdResized");
        }
        super.onAdResized(ad, rect);
    }
}
