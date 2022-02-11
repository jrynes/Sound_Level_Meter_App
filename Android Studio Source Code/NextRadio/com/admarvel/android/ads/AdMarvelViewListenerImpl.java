package com.admarvel.android.ads;

import android.content.Context;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import com.admarvel.android.ads.AdMarvelView.AdMarvelViewExtendedListener;
import com.admarvel.android.ads.AdMarvelView.AdMarvelViewListener;
import com.admarvel.android.util.Logging;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.admarvel.android.ads.o */
public class AdMarvelViewListenerImpl {
    private AdMarvelViewListener f726a;
    private AdMarvelViewExtendedListener f727b;

    public void m420a(Context context, AdMarvelView adMarvelView, int i, ErrorReason errorReason, String str, int i2, Map<String, Object> map, String str2) {
        try {
            Map hashMap;
            AdMarvelAnalyticsAdapter instance = AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context);
            if (map == null) {
                hashMap = new HashMap();
            }
            instance.onFailedToReceiveAd(str, i2, hashMap, str2);
        } catch (Exception e) {
        }
        if (this.f726a != null) {
            Logging.log("onFailedToReceiveAd : Error Code " + i);
            this.f726a.onFailedToReceiveAd(adMarvelView, i, errorReason);
            return;
        }
        Logging.log("onFailedToReceiveAd - No listener found");
    }

    public void m421a(Context context, AdMarvelView adMarvelView, String str, int i, Map<String, Object> map, String str2) {
        try {
            Map hashMap;
            AdMarvelAnalyticsAdapter instance = AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context);
            if (map == null) {
                hashMap = new HashMap();
            }
            instance.onReceiveAd(str, i, hashMap, str2);
        } catch (Exception e) {
        }
        if (this.f726a != null) {
            Logging.log("onReceiveAd");
            this.f726a.onReceiveAd(adMarvelView);
            return;
        }
        Logging.log("onReceiveAd - No listener found");
    }

    public void m422a(Context context, AdMarvelView adMarvelView, String str, String str2, int i, Map<String, Object> map, String str3) {
        try {
            Map hashMap;
            AdMarvelAnalyticsAdapter instance = AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context);
            if (map == null) {
                hashMap = new HashMap();
            } else {
                Map<String, Object> map2 = map;
            }
            instance.onAdClick(str2, i, hashMap, str, str3);
        } catch (Exception e) {
        }
        if (this.f726a != null) {
            Logging.log("onClickAd");
            this.f726a.onClickAd(adMarvelView, str);
            return;
        }
        Logging.log("onClickAd - No listener found");
    }

    public void m423a(AdMarvelViewExtendedListener adMarvelViewExtendedListener) {
        this.f727b = adMarvelViewExtendedListener;
    }

    public void m424a(AdMarvelViewListener adMarvelViewListener) {
        this.f726a = adMarvelViewListener;
    }

    public void m425a(AdMarvelView adMarvelView) {
        if (this.f726a != null) {
            Logging.log("onRequestAd");
            this.f726a.onRequestAd(adMarvelView);
            return;
        }
        Logging.log("onRequestAd - No listener found");
    }

    public void m426a(AdMarvelView adMarvelView, AdMarvelAd adMarvelAd) {
        if (this.f727b != null) {
            Logging.log("onAdFetched");
            this.f727b.onAdFetched(adMarvelView, adMarvelAd);
            return;
        }
        Logging.log("onAdFetched - No listener found");
    }

    public void m427b(Context context, AdMarvelView adMarvelView, String str, int i, Map<String, Object> map, String str2) {
        try {
            Map hashMap;
            AdMarvelAnalyticsAdapter instance = AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context);
            if (map == null) {
                hashMap = new HashMap();
            }
            instance.onReceiveAd(str, i, hashMap, str2);
        } catch (Exception e) {
        }
        if (this.f727b != null) {
            Logging.log("onDisplayedAd");
            this.f727b.onAdDisplayed(adMarvelView);
            return;
        }
        Logging.log("onDisplayedAd - No listener found");
    }

    public void m428b(AdMarvelView adMarvelView) {
        if (this.f726a != null) {
            Logging.log("onExpand");
            this.f726a.onExpand(adMarvelView);
            return;
        }
        Logging.log("onExpand - No listener found");
    }

    public void m429c(AdMarvelView adMarvelView) {
        if (this.f726a != null) {
            Logging.log("onClose");
            this.f726a.onClose(adMarvelView);
            return;
        }
        Logging.log("onClose - No listener found");
    }
}
