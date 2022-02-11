package com.admarvel.android.ads.nativeads;

import android.content.Context;
import com.admarvel.android.ads.AdMarvelAnalyticsAdapterInstances;
import com.admarvel.android.ads.AdMarvelUtils.AdMArvelErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.AdMarvelVideoEvents;
import com.admarvel.android.ads.Constants;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd.AdMarvelNativeAdListener;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd.AdMarvelNativeVideoAdListener;
import com.admarvel.android.util.Logging;
import java.util.Map;

/* renamed from: com.admarvel.android.ads.nativeads.b */
public class AdMarvelNativeAdListenerImpl {
    private AdMarvelNativeAdListener f702a;
    private AdMarvelNativeVideoAdListener f703b;

    public void m400a() {
        if (this.f703b != null) {
            Logging.log("onNativeVideoViewAudioStart");
            this.f703b.onNativeVideoViewAudioStart();
        }
    }

    public void m401a(AdMarvelVideoEvents adMarvelVideoEvents, Map<String, String> map) {
        if (this.f703b != null) {
            Logging.log("onNativeVideoEvent");
            this.f703b.onNativeVideoEvent(adMarvelVideoEvents, map);
        }
    }

    public void m402a(AdMarvelNativeAdListener adMarvelNativeAdListener) {
        this.f702a = adMarvelNativeAdListener;
    }

    void m403a(AdMarvelNativeVideoAdListener adMarvelNativeVideoAdListener) {
        this.f703b = adMarvelNativeVideoAdListener;
    }

    public void m404a(AdMarvelNativeAd adMarvelNativeAd) {
        if (this.f702a != null) {
            Logging.log("onRequestNativeAd");
            this.f702a.onRequestNativeAd(adMarvelNativeAd);
        }
    }

    public void m405a(AdMarvelNativeAd adMarvelNativeAd, int i, AdMArvelErrorReason adMArvelErrorReason) {
        Context context = adMarvelNativeAd.getmContext();
        String siteId = adMarvelNativeAd.getSiteId();
        int id = adMarvelNativeAd.getId();
        String ipAddress = adMarvelNativeAd.getIpAddress();
        Map targetParams = adMarvelNativeAd.getTargetParams();
        if (context != null) {
            try {
                AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context).onFailedToReceiveAd(siteId, id, targetParams, ipAddress);
            } catch (Exception e) {
            }
        }
        if (this.f702a != null) {
            Logging.log("onFailedToReceiveNativeAd : Error Code " + i);
            this.f702a.onFailedToReceiveNativeAd(i, adMArvelErrorReason, adMarvelNativeAd);
        }
    }

    public void m406b() {
        if (this.f703b != null) {
            Logging.log("onNativeVideoViewAudioStop");
            this.f703b.onNativeVideoViewAudioStop();
        }
    }

    public void m407b(AdMarvelNativeAd adMarvelNativeAd) {
        Context context = adMarvelNativeAd.getmContext();
        String siteId = adMarvelNativeAd.getSiteId();
        int id = adMarvelNativeAd.getId();
        String ipAddress = adMarvelNativeAd.getIpAddress();
        try {
            AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context).onReceiveAd(siteId, id, adMarvelNativeAd.getTargetParams(), ipAddress);
        } catch (Exception e) {
        }
        if (this.f702a != null) {
            Logging.log("onReceiveNativeAd");
            this.f702a.onReceiveNativeAd(adMarvelNativeAd);
        }
    }
}
