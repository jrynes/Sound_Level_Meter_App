package com.admarvel.android.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.admarvel.android.ads.AdFetcher.Adtype;
import com.admarvel.android.ads.AdMarvelAd.AdType;
import com.admarvel.android.ads.AdMarvelInterstitialAds.InterstitialAdsState;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.util.Logging;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.jndi.ReadOnlyContext;

/* renamed from: com.admarvel.android.ads.h */
class AdMarvelInterstitialAsyncTask extends AsyncTask<Object, Object, AdMarvelAd> {
    private Map<String, Object> f619a;
    private AdMarvelInterstitialAds f620b;
    private final WeakReference<Context> f621c;

    public AdMarvelInterstitialAsyncTask(Context context) {
        this.f619a = new HashMap();
        this.f621c = new WeakReference(context);
    }

    protected AdMarvelAd m334a(Object... objArr) {
        Map map = (Map) objArr[0];
        String str = (String) objArr[1];
        String str2 = (String) objArr[2];
        String str3 = (String) objArr[3];
        int intValue = ((Integer) objArr[4]).intValue();
        String str4 = (String) objArr[5];
        this.f620b = (AdMarvelInterstitialAds) objArr[6];
        int intValue2 = ((Integer) objArr[7]).intValue();
        String str5 = (String) objArr[8];
        Context context = (Context) this.f621c.get();
        Map map2 = (Map) objArr[9];
        String str6 = (String) objArr[10];
        boolean booleanValue = ((Boolean) objArr[11]).booleanValue();
        AdFetcher adFetcher = new AdFetcher();
        if (context == null) {
            return null;
        }
        int i;
        int i2;
        int i3;
        int i4;
        if (map != null) {
            try {
                synchronized (map) {
                    this.f619a.putAll(map);
                }
            } catch (Exception e) {
                this.f619a = null;
            }
        }
        try {
            map = AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context).getEnhancedTargetParams(str2, this.f619a);
        } catch (Exception e2) {
            map = null;
        }
        if (map != null) {
            try {
                if (this.f619a != null) {
                    map.putAll(this.f619a);
                    this.f619a.putAll(map);
                } else {
                    this.f619a = map;
                }
            } catch (Throwable e3) {
                Logging.log(Log.getStackTraceString(e3));
            }
        }
        int i5 = 0;
        try {
            i5 = AdMarvelAdapterInstances.getInstance(this.f620b.WEBVIEW_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME).getAdAvailablityStatus(str2, context);
            if (i5 == 0 || i5 == 1) {
                Utils.m187a(SDKAdNetwork.ADCOLONY, context, i5);
            } else if (i5 == 2) {
                if (Utils.m194a(SDKAdNetwork.ADCOLONY, context)) {
                    i5 = 1;
                }
            }
            i = i5;
        } catch (Exception e4) {
            i = i5;
        }
        i5 = 0;
        try {
            i5 = AdMarvelAdapterInstances.getInstance(this.f620b.WEBVIEW_GUID, Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME).getAdAvailablityStatus();
            if (i5 == 0 || i5 == 1) {
                Utils.m187a(SDKAdNetwork.UNITYADS, context, i5);
            } else if (i5 == 2) {
                if (Utils.m194a(SDKAdNetwork.UNITYADS, context)) {
                    i5 = 1;
                }
            }
            i2 = i5;
        } catch (Exception e5) {
            i2 = i5;
        }
        i5 = 0;
        try {
            i5 = AdMarvelAdapterInstances.getInstance(this.f620b.WEBVIEW_GUID, Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME).getAdAvailablityStatus();
            if (i5 == 0 || i5 == 1) {
                Utils.m187a(SDKAdNetwork.VUNGLE, context, i5);
            } else if (i5 == 2) {
                if (Utils.m194a(SDKAdNetwork.VUNGLE, context)) {
                    i5 = 1;
                }
            }
            i3 = i5;
        } catch (Exception e6) {
            i3 = i5;
        }
        i5 = 0;
        try {
            i5 = AdMarvelAdapterInstances.getInstance(this.f620b.WEBVIEW_GUID, Constants.YUME_SDK_ADAPTER_FULL_CLASSNAME).getAdAvailablityStatus();
            if (i5 == 0 || i5 == 1) {
                Utils.m187a(SDKAdNetwork.YUME, context, i5);
            } else if (i5 == 2) {
                if (Utils.m194a(SDKAdNetwork.YUME, context)) {
                    i5 = 1;
                }
            }
            i4 = i5;
        } catch (Exception e7) {
            i4 = i5;
        }
        String fetchOfflineAd = AdMarvelInterstitialAds.enableOfflineSDK ? adFetcher.fetchOfflineAd(Adtype.INTERSTITIAL, context, str3, intValue, str4, this.f619a, str, str2, intValue2, str5, false, this.f620b.isAutoScalingEnabled()) : adFetcher.fetchAd(Adtype.INTERSTITIAL, context, str3, intValue, str4, this.f619a, str, str2, intValue2, str5, false, this.f620b.isAutoScalingEnabled(), false, null, map2, str6, booleanValue, i, i2, i3, 0, i4, null, false, false);
        AdMarvelAd adMarvelAd = new AdMarvelAd(fetchOfflineAd, this.f619a, str, str2, str3, intValue, str4, context.getPackageName());
        adMarvelAd.setRewardParams(map2);
        adMarvelAd.setUserId(str6);
        adMarvelAd.setRewardInterstitial(booleanValue);
        adMarvelAd.setRequestJson(adFetcher.getRequestJson());
        if (AdMarvelInterstitialAds.enableOfflineSDK) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("admarvel_preferences", 0);
            String string = sharedPreferences.getString("banner_folder", "NULL");
            adMarvelAd.setOfflineBaseUrl("file://" + sharedPreferences.getString("childDirectory", "NULL") + ReadOnlyContext.SEPARATOR + string);
            adMarvelAd.setOfflinekeyUrl(sharedPreferences.getString("childDirectory", "NULL") + ReadOnlyContext.SEPARATOR + string);
        }
        if (fetchOfflineAd != null) {
            try {
                AdMarvelXMLReader loadAd = adMarvelAd.loadAd(context);
                if (loadAd == null) {
                    adMarvelAd.setAdType(AdType.ERROR);
                    adMarvelAd.setErrorCode(303);
                    return adMarvelAd;
                } else if (adMarvelAd.getSdkNetwork() == null || adMarvelAd.getSdkNetwork().length() <= 0) {
                    return adMarvelAd;
                } else {
                    try {
                        return AdMarvelAdapterInstances.getInstance(this.f620b.WEBVIEW_GUID, adMarvelAd.getSdkNetwork()).loadAd(adMarvelAd, loadAd);
                    } catch (Throwable e8) {
                        Logging.log(Log.getStackTraceString(e8));
                        adMarvelAd.setAdType(AdType.ERROR);
                        adMarvelAd.setErrorCode(303);
                        return adMarvelAd;
                    }
                }
            } catch (Throwable e82) {
                try {
                    Logging.log(Log.getStackTraceString(e82));
                    adMarvelAd.setAdType(AdType.ERROR);
                    adMarvelAd.setErrorCode(303);
                    return adMarvelAd;
                } catch (Exception e9) {
                    adMarvelAd.setAdType(AdType.ERROR);
                    adMarvelAd.setErrorCode(303);
                    return adMarvelAd;
                }
            }
        }
        adMarvelAd.setAdType(AdType.ERROR);
        adMarvelAd.setErrorCode(303);
        return adMarvelAd;
    }

    protected void m335a(AdMarvelAd adMarvelAd) {
        ErrorReason a;
        int a2;
        Context context;
        try {
            Context context2;
            super.onPostExecute(adMarvelAd);
            if (adMarvelAd.getAdType() == AdType.ERROR) {
                try {
                    a = Utils.m178a(adMarvelAd.getErrorCode());
                    a2 = Utils.m177a(a);
                    context = (Context) this.f621c.get();
                    if (context != null) {
                        this.f620b.interstitialAdsState = InterstitialAdsState.DEFAULT;
                        Logging.log("AdMarvelInterstitialAsyncTask - onPostExecute : InterstitialAdsState-" + this.f620b.interstitialAdsState);
                        this.f620b.unregisterReceiver(this.f620b.WEBVIEW_GUID);
                        this.f620b.unregisterCallbackReceiver(this.f620b.WEBVIEW_GUID);
                        this.f620b.getListener().m325a(context, adMarvelAd.getSdkAdNetwork(), a2, a, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), this.f620b);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (adMarvelAd.getAdType() == AdType.SDKCALL) {
                if (adMarvelAd.getSdkNetwork() != null) {
                    this.f620b.requestPendingAdapterAd(this.f619a, adMarvelAd, adMarvelAd.getSdkNetwork(), (Context) this.f621c.get());
                    return;
                } else if (adMarvelAd.isDisableAdrequest()) {
                    String disableAdDuration = adMarvelAd.getDisableAdDuration();
                    if (disableAdDuration != null) {
                        context2 = (Context) this.f621c.get();
                        if (this.f620b != null && context2 != null) {
                            this.f620b.disableAdRequest(disableAdDuration, adMarvelAd, context2);
                            return;
                        }
                        return;
                    }
                }
            }
            context2 = null;
            if (this.f621c != null) {
                context2 = (Context) this.f621c.get();
            }
            this.f620b.requestPendingAdMarvelAd(adMarvelAd, context2);
        } catch (Throwable e2) {
            Logging.log(Log.getStackTraceString(e2));
            a = Utils.m178a(303);
            a2 = Utils.m177a(a);
            context = (Context) this.f621c.get();
            if (context != null && this.f620b != null) {
                this.f620b.interstitialAdsState = InterstitialAdsState.DEFAULT;
                Logging.log("AdMarvelInterstitialAsyncTask - onPostExecute : InterstitialAdsState-" + this.f620b.interstitialAdsState);
                this.f620b.unregisterReceiver(this.f620b.WEBVIEW_GUID);
                this.f620b.unregisterCallbackReceiver(this.f620b.WEBVIEW_GUID);
                this.f620b.getListener().m325a(context, adMarvelAd.getSdkAdNetwork(), a2, a, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), this.f620b);
            }
        }
    }

    protected /* synthetic */ Object doInBackground(Object[] x0) {
        return m334a(x0);
    }

    protected /* synthetic */ void onPostExecute(Object x0) {
        m335a((AdMarvelAd) x0);
    }
}
