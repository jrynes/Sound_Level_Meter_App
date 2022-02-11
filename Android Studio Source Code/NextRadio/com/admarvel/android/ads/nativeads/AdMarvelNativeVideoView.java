package com.admarvel.android.ads.nativeads;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.admarvel.android.ads.AdMarvelAdapter;
import com.admarvel.android.ads.AdMarvelAdapterInstances;
import com.admarvel.android.ads.Constants;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd.C0263a;
import com.rabbitmq.client.AMQP;
import java.lang.ref.WeakReference;

public class AdMarvelNativeVideoView extends LinearLayout {
    private WeakReference<AdMarvelNativeAd> adMarvelNativeAdReference;
    private int nativeVideoHeight;
    private int nativeVideoWidth;

    public AdMarvelNativeVideoView(Context context, Object adMarvelNativeAd) {
        super(context);
        this.nativeVideoWidth = AMQP.CONNECTION_FORCED;
        this.nativeVideoHeight = 180;
        if (adMarvelNativeAd instanceof AdMarvelNativeAd) {
            this.adMarvelNativeAdReference = new WeakReference((AdMarvelNativeAd) adMarvelNativeAd);
        }
    }

    protected AdMarvelNativeVideoView getAdMarvelNativeVideoView() {
        return this;
    }

    int getNativeVideoHeight() {
        return this.nativeVideoHeight;
    }

    float getNativeVideoWidth() {
        return (float) this.nativeVideoWidth;
    }

    public void notifyAddedToListView() {
        if (this.adMarvelNativeAdReference.get() != null && ((AdMarvelNativeAd) this.adMarvelNativeAdReference.get()).getAdType() == C0263a.SDKCALL) {
            AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(((AdMarvelNativeAd) this.adMarvelNativeAdReference.get()).ADMARVEL_NATIVE_AD_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME);
            if (instance != null) {
                instance.notifyAddedToListView(getChildAt(0));
            }
        }
    }

    void setNativeVideoHeight(int nativeVideoHeight) {
        this.nativeVideoHeight = nativeVideoHeight;
    }

    public void setNativeVideoView(View NativeVideoAdView) {
        addView(NativeVideoAdView);
    }

    void setNativeVideoWidth(int nativeVideoWidth) {
        this.nativeVideoWidth = nativeVideoWidth;
    }
}
