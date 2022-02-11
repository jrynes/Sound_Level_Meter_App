package com.admarvel.android.ads;

import android.content.Context;
import com.admarvel.android.ads.AdMarvelInterstitialAds.AdMarvelInterstitialAdListener;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.util.Logging;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.admarvel.android.ads.g */
public class AdMarvelInterstitialAdListenerImpl {
    private AdMarvelInterstitialAdListener f618a;

    public void m325a(Context context, SDKAdNetwork sDKAdNetwork, int i, ErrorReason errorReason, String str, int i2, Map<String, Object> map, String str2, AdMarvelInterstitialAds adMarvelInterstitialAds) {
        try {
            Map hashMap;
            AdMarvelAnalyticsAdapter instance = AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context);
            if (map == null) {
                hashMap = new HashMap();
            }
            instance.onFailedToReceiveAd(str, i2, hashMap, str2);
        } catch (Exception e) {
        }
        if (this.f618a != null) {
            if (adMarvelInterstitialAds != null) {
                adMarvelInterstitialAds.unregisterCallbackReceiver(adMarvelInterstitialAds.WEBVIEW_GUID);
                AdMarvelAdapterInstances.destroyAdMarvelAdapterInstances(adMarvelInterstitialAds.WEBVIEW_GUID);
            }
            Logging.log("onFailedToReceiveInterstitialAd : Error Code " + i);
            this.f618a.onFailedToReceiveInterstitialAd(sDKAdNetwork, adMarvelInterstitialAds, i, errorReason);
        }
    }

    public void m326a(Context context, SDKAdNetwork sDKAdNetwork, String str, AdMarvelAd adMarvelAd, String str2, int i, Map<String, Object> map, String str3, AdMarvelInterstitialAds adMarvelInterstitialAds) {
        try {
            Map hashMap;
            AdMarvelAnalyticsAdapter instance = AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context);
            if (map == null) {
                hashMap = new HashMap();
            }
            instance.onReceiveAd(str2, i, hashMap, str3);
        } catch (Exception e) {
        }
        if (this.f618a != null) {
            Logging.log("onReceiveInterstitialAd");
            this.f618a.onReceiveInterstitialAd(sDKAdNetwork, adMarvelInterstitialAds, adMarvelAd);
        }
    }

    public void m327a(Context context, String str, String str2, int i, Map<String, Object> map, String str3, AdMarvelInterstitialAds adMarvelInterstitialAds) {
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
        if (this.f618a != null) {
            Logging.log("onClickInterstitialAd");
            this.f618a.onClickInterstitialAd(str, adMarvelInterstitialAds);
        }
    }

    public void m328a(AdMarvelActivity adMarvelActivity, AdMarvelInterstitialAds adMarvelInterstitialAds) {
        if (this.f618a != null) {
            Logging.log("onAdmarvelActivityLaunched");
            this.f618a.onAdmarvelActivityLaunched(adMarvelActivity, adMarvelInterstitialAds);
        }
    }

    public void m329a(AdMarvelInterstitialAdListener adMarvelInterstitialAdListener) {
        this.f618a = adMarvelInterstitialAdListener;
    }

    public void m330a(AdMarvelInterstitialAds adMarvelInterstitialAds) {
        if (this.f618a != null) {
            Logging.log("onRequestInterstitialAd");
            this.f618a.onRequestInterstitialAd(adMarvelInterstitialAds);
        }
    }

    public void m331a(AdMarvelVideoActivity adMarvelVideoActivity, AdMarvelInterstitialAds adMarvelInterstitialAds) {
        if (this.f618a != null) {
            Logging.log("onAdMarvelVideoActivityLaunched");
            this.f618a.onAdMarvelVideoActivityLaunched(adMarvelVideoActivity, adMarvelInterstitialAds);
        }
    }

    public boolean m332a(AdMarvelAd adMarvelAd, AdMarvelInterstitialAds adMarvelInterstitialAds) {
        if (adMarvelAd != null) {
            try {
                if (adMarvelAd.isRewardInterstitial()) {
                    AdMarvelEvent adMarvelEvent = adMarvelAd.getAdMarvelEvent();
                    if (!(adMarvelEvent == null || !adMarvelEvent.m266b() || adMarvelInterstitialAds == null || adMarvelInterstitialAds.isRewardFired())) {
                        adMarvelEvent.m265a(false);
                        adMarvelInterstitialAds.setRewardFired(false);
                        AdMarvelReward adMarvelReward = new AdMarvelReward();
                        adMarvelReward.setSuccess(false);
                        if (AdMarvelInterstitialAds.getRewardListener() != null) {
                            AdMarvelInterstitialAds.getRewardListener().onReward(adMarvelReward);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        if (this.f618a == null) {
            return false;
        }
        if (adMarvelInterstitialAds != null) {
            adMarvelInterstitialAds.unregisterCallbackReceiver(adMarvelInterstitialAds.WEBVIEW_GUID);
            AdMarvelAdapterInstances.destroyAdMarvelAdapterInstances(adMarvelInterstitialAds.WEBVIEW_GUID);
        }
        Logging.log("onCloseInterstitialAd");
        this.f618a.onCloseInterstitialAd(adMarvelInterstitialAds);
        return true;
    }

    public void m333b(AdMarvelInterstitialAds adMarvelInterstitialAds) {
        if (this.f618a != null) {
            Logging.log("onInterstitialDisplayed");
            this.f618a.onInterstitialDisplayed(adMarvelInterstitialAds);
        }
    }
}
