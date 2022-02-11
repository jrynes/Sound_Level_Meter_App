package com.admarvel.android.ads.nativeads;

import android.content.Context;
import android.os.AsyncTask;
import com.admarvel.android.ads.AdFetcher;
import com.admarvel.android.ads.AdFetcher.Adtype;
import com.admarvel.android.ads.AdMarvelAdapter;
import com.admarvel.android.ads.AdMarvelAdapterInstances;
import com.admarvel.android.ads.AdMarvelAnalyticsAdapterInstances;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelUtils.AdMArvelErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.ads.AdMarvelXMLReader;
import com.admarvel.android.ads.Constants;
import com.admarvel.android.ads.Utils;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd.C0263a;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd.RequestParameters;
import com.admarvel.android.util.Logging;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Map;

/* renamed from: com.admarvel.android.ads.nativeads.c */
class AdMarvelNativeAsyncTask extends AsyncTask<Object, Object, AdMarvelNativeAd> {
    String f704a;
    private AdMarvelNativeAd f705b;

    AdMarvelNativeAsyncTask() {
        this.f704a = null;
    }

    private Map<String, Object> m408a(Context context, String str, Map<String, Object> map) {
        try {
            Map<String, Object> enhancedTargetParams = AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context).getEnhancedTargetParams(str, map);
            if (enhancedTargetParams == null) {
                return map;
            }
            if (map == null) {
                return enhancedTargetParams;
            }
            map.putAll(enhancedTargetParams);
            return map;
        } catch (Exception e) {
            return map;
        }
    }

    protected AdMarvelNativeAd m409a(Object... objArr) {
        int i;
        Exception e;
        AdMarvelXMLReader loadAd;
        AdMarvelNativeAdListenerImpl listener;
        RequestParameters requestParameters = (RequestParameters) objArr[0];
        this.f705b = (AdMarvelNativeAd) objArr[1];
        if (this.f705b == null) {
            return null;
        }
        int intValue = ((Integer) objArr[2]).intValue();
        String str = (String) objArr[3];
        String str2 = (String) objArr[4];
        Boolean bool = (Boolean) objArr[5];
        Map targetParams = requestParameters.getTargetParams();
        String partnerId = requestParameters.getPartnerId();
        String siteId = requestParameters.getSiteId();
        Context context = this.f705b.getmContext();
        if (context == null) {
            return null;
        }
        Map a = m408a(context, siteId, targetParams);
        AdFetcher adFetcher = new AdFetcher();
        this.f705b.createdUsingCachedAd = false;
        Object obj = null;
        int i2 = 0;
        try {
            AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(this.f705b.ADMARVEL_NATIVE_AD_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME);
            if (instance != null) {
                i2 = instance.getAdAvailablityStatus(siteId, context);
                if (i2 == 0) {
                    Utils.m187a(SDKAdNetwork.ADCOLONY, context, i2);
                } else if (i2 == 2) {
                    if (Utils.m194a(SDKAdNetwork.ADCOLONY, context)) {
                        i2 = 1;
                    }
                }
            }
            i = i2;
        } catch (Exception e2) {
            i = i2;
        }
        FileInputStream fileInputStream;
        try {
            if (!bool.booleanValue()) {
                File dir = context.getDir("adm_assets", 0);
                if (dir != null && dir.isDirectory()) {
                    File file = new File(dir.getAbsolutePath() + "/cachedad.txt");
                    if (file == null || !file.exists() || file.length() <= 0) {
                        this.f705b.createdUsingCachedAd = false;
                        file.delete();
                    } else {
                        Object obj2;
                        if ((Calendar.getInstance().getTimeInMillis() - AdMarvelUtils.getPreferenceValueLong(context, "adm_viewport", "adm_cachedad_timestamp")) / 1000 < AdMarvelUtils.getPreferenceValueLong(context, "adm_viewport", "adm_cachedad_ttl")) {
                            byte[] bArr = new byte[((int) file.length())];
                            fileInputStream = new FileInputStream(file);
                            fileInputStream.read(bArr);
                            fileInputStream.close();
                            this.f704a = new String(bArr);
                            try {
                                this.f705b.createdUsingCachedAd = true;
                                this.f705b.lastRequestPostString = adFetcher.generateRequestParams(Adtype.NATIVE, context, null, this.f705b.getOrientation(), this.f705b.getDeviceConnectivity(), a, partnerId, siteId, intValue, str, false, false, false, this.f705b.getAdMarvelNetworkHandler(), null, null, false, i, 0, 0, 0, 0, str2, false, false);
                                Logging.log("Loading Ad from client side cached ad");
                                obj2 = 1;
                            } catch (Exception e3) {
                                e = e3;
                                int i3 = 1;
                                try {
                                    e.printStackTrace();
                                    this.f705b.createdUsingCachedAd = false;
                                    if (this.f705b.isAdMarvelViewNativeAdXML) {
                                        this.f704a = this.f705b.adMarvelViewNativeAdXML;
                                    }
                                    this.f705b.createdUsingCachedAd = false;
                                    if (i == 2) {
                                    }
                                    this.f704a = adFetcher.fetchAd(Adtype.NATIVE, context, null, this.f705b.getOrientation(), this.f705b.getDeviceConnectivity(), a, partnerId, siteId, intValue, str, false, false, false, this.f705b.getAdMarvelNetworkHandler(), null, null, false, i, 0, 0, 0, 0, str2, false, false);
                                    if (this.f704a == null) {
                                        loadAd = this.f705b.loadAd(this.f704a);
                                        AdMarvelAdapterInstances.getInstance(this.f705b.ADMARVEL_NATIVE_AD_GUID, this.f705b.getSdkNetwork()).loadNativeAd(this.f705b, loadAd);
                                        return this.f705b;
                                    }
                                    listener = this.f705b.getListener();
                                    if (listener != null) {
                                        listener.m405a(this.f705b, AdMArvelErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION.getErrorCode(), AdMArvelErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION);
                                    }
                                    return null;
                                } catch (Exception e4) {
                                    this.f705b.setAdType(C0263a.ERROR);
                                    this.f705b.setErrorCode(AdMArvelErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION.getErrorCode());
                                }
                            }
                        } else {
                            this.f705b.createdUsingCachedAd = false;
                            file.delete();
                            obj2 = null;
                        }
                        obj = obj2;
                    }
                }
            }
        } catch (Exception e5) {
            e = e5;
            e.printStackTrace();
            this.f705b.createdUsingCachedAd = false;
            if (this.f705b.isAdMarvelViewNativeAdXML) {
                this.f704a = this.f705b.adMarvelViewNativeAdXML;
            }
            this.f705b.createdUsingCachedAd = false;
            if (i == 2) {
            }
            this.f704a = adFetcher.fetchAd(Adtype.NATIVE, context, null, this.f705b.getOrientation(), this.f705b.getDeviceConnectivity(), a, partnerId, siteId, intValue, str, false, false, false, this.f705b.getAdMarvelNetworkHandler(), null, null, false, i, 0, 0, 0, 0, str2, false, false);
            if (this.f704a == null) {
                listener = this.f705b.getListener();
                if (listener != null) {
                    listener.m405a(this.f705b, AdMArvelErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION.getErrorCode(), AdMArvelErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION);
                }
                return null;
            }
            loadAd = this.f705b.loadAd(this.f704a);
            AdMarvelAdapterInstances.getInstance(this.f705b.ADMARVEL_NATIVE_AD_GUID, this.f705b.getSdkNetwork()).loadNativeAd(this.f705b, loadAd);
            return this.f705b;
        } catch (Throwable th) {
            fileInputStream.close();
        }
        if (this.f705b.isAdMarvelViewNativeAdXML) {
            this.f704a = this.f705b.adMarvelViewNativeAdXML;
        }
        if (obj == null && !this.f705b.isAdMarvelViewNativeAdXML) {
            this.f705b.createdUsingCachedAd = false;
            if (i == 2 || a == null || !a.containsKey("ADC_SAAS_REQ") || !a.get("ADC_SAAS_REQ").equals("1")) {
                this.f704a = adFetcher.fetchAd(Adtype.NATIVE, context, null, this.f705b.getOrientation(), this.f705b.getDeviceConnectivity(), a, partnerId, siteId, intValue, str, false, false, false, this.f705b.getAdMarvelNetworkHandler(), null, null, false, i, 0, 0, 0, 0, str2, false, false);
            } else {
                this.f704a = null;
            }
        }
        if (this.f704a == null) {
            listener = this.f705b.getListener();
            if (listener != null) {
                listener.m405a(this.f705b, AdMArvelErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION.getErrorCode(), AdMArvelErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION);
            }
            return null;
        }
        loadAd = this.f705b.loadAd(this.f704a);
        if (loadAd != null && this.f705b.getAdType() == C0263a.SDKCALL && this.f705b.getSdkNetwork() != null && this.f705b.getSdkNetwork().length() > 0) {
            AdMarvelAdapterInstances.getInstance(this.f705b.ADMARVEL_NATIVE_AD_GUID, this.f705b.getSdkNetwork()).loadNativeAd(this.f705b, loadAd);
        }
        return this.f705b;
    }

    protected void m410a(AdMarvelNativeAd adMarvelNativeAd) {
        if (adMarvelNativeAd != null) {
            if (adMarvelNativeAd.getAdType() == C0263a.ERROR) {
                Logging.log("ADType Error ");
                AdMarvelNativeAdListenerImpl listener = adMarvelNativeAd.getListener();
                if (listener != null) {
                    AdMArvelErrorReason adMArvelErrorReason = AdMarvelUtils.getAdMArvelErrorReason(adMarvelNativeAd.getErrorCode());
                    listener.m405a(adMarvelNativeAd, adMArvelErrorReason != null ? adMArvelErrorReason.getErrorCode() : -1, adMArvelErrorReason);
                }
            } else if (adMarvelNativeAd.getAdType() == C0263a.SDKCALL) {
                if (adMarvelNativeAd.getSdkNetwork() != null) {
                    adMarvelNativeAd.requestPendingAd();
                }
            } else if (adMarvelNativeAd.isDisableAdrequest()) {
                String disableAdDuration = adMarvelNativeAd.getDisableAdDuration();
                if (disableAdDuration != null) {
                    adMarvelNativeAd.disableAdRequest(disableAdDuration);
                }
            } else {
                AdMarvelNativeAdListenerImpl listener2 = adMarvelNativeAd.getListener();
                if (listener2 != null && !adMarvelNativeAd.isAdMarvelViewCreated) {
                    listener2.m407b(adMarvelNativeAd);
                }
            }
        }
    }

    protected /* synthetic */ Object doInBackground(Object[] x0) {
        return m409a(x0);
    }

    protected /* synthetic */ void onPostExecute(Object x0) {
        m410a((AdMarvelNativeAd) x0);
    }
}
