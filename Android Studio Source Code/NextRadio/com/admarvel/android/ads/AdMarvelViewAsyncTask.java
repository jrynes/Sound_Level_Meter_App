package com.admarvel.android.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.admarvel.android.ads.AdFetcher.Adtype;
import com.admarvel.android.ads.AdMarvelAd.AdType;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd.RequestParameters.Builder;
import com.admarvel.android.util.Logging;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;

/* renamed from: com.admarvel.android.ads.n */
class AdMarvelViewAsyncTask extends AsyncTask<Object, Object, Object> {
    private Map<String, Object> f680a;
    private WeakReference<AdMarvelView> f681b;
    private final WeakReference<Context> f682c;

    public AdMarvelViewAsyncTask(Context context) {
        this.f680a = new HashMap();
        this.f682c = new WeakReference(context);
    }

    private boolean m390a(String str) {
        if (str == null) {
            return false;
        }
        try {
            AdMarvelXMLReader adMarvelXMLReader = new AdMarvelXMLReader();
            adMarvelXMLReader.parseXMLString(str);
            AdMarvelXMLElement parsedXMLData = adMarvelXMLReader.getParsedXMLData();
            if (parsedXMLData == null) {
                return false;
            }
            if (parsedXMLData.getName().equals(Constants.NATIVE_AD_ELEMENT)) {
                String str2 = (String) parsedXMLData.getAttributes().get(Send.TYPE);
                if ("native".equals(str2)) {
                    return true;
                }
                if ("sdkcall".equals(str2) && parsedXMLData.getAttributes().containsKey("native")) {
                    if ("1".equals((String) parsedXMLData.getAttributes().get("native"))) {
                        return true;
                    }
                }
            }
            return false;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    protected Object doInBackground(Object... params) {
        Map map = (Map) params[0];
        String str = (String) params[1];
        String str2 = (String) params[2];
        String str3 = (String) params[3];
        int intValue = ((Integer) params[4]).intValue();
        String str4 = (String) params[5];
        this.f681b = new WeakReference((AdMarvelView) params[6]);
        int intValue2 = ((Integer) params[7]).intValue();
        String str5 = (String) params[8];
        Boolean bool = (Boolean) params[9];
        Boolean bool2 = (Boolean) params[10];
        Boolean bool3 = (Boolean) params[11];
        Boolean bool4 = (Boolean) params[12];
        AdFetcher adFetcher = new AdFetcher();
        Context context = (Context) this.f682c.get();
        if (context == null) {
            return null;
        }
        if (map != null) {
            try {
                synchronized (map) {
                    this.f680a.putAll(map);
                }
            } catch (Exception e) {
                this.f680a = null;
            }
        }
        try {
            map = AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context).getEnhancedTargetParams(str2, this.f680a);
        } catch (Exception e2) {
            map = null;
        }
        if (map != null) {
            try {
                if (this.f680a != null) {
                    map.putAll(this.f680a);
                    this.f680a.putAll(map);
                } else {
                    this.f680a = map;
                }
            } catch (Throwable e3) {
                Logging.log(Log.getStackTraceString(e3));
            }
        }
        boolean z = false;
        int i = 0;
        AdMarvelView adMarvelView = this.f681b != null ? (AdMarvelView) this.f681b.get() : null;
        if (adMarvelView != null) {
            z = adMarvelView.isAdFetchedModel();
            try {
                int adAvailablityStatus = AdMarvelAdapterInstances.getInstance(adMarvelView.ADMARVEL_VIEW_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME).getAdAvailablityStatus(str2, context);
                if (adAvailablityStatus == 0) {
                    try {
                        Utils.m187a(SDKAdNetwork.ADCOLONY, context, adAvailablityStatus);
                    } catch (Exception e4) {
                        i = adAvailablityStatus;
                    }
                } else if (adAvailablityStatus == 2) {
                    if (Utils.m194a(SDKAdNetwork.ADCOLONY, context)) {
                        adAvailablityStatus = 1;
                    }
                }
                i = adAvailablityStatus;
            } catch (Exception e5) {
            }
        }
        str5 = AdMarvelView.enableOfflineSDK ? adFetcher.fetchOfflineAd(Adtype.BANNER, context, str3, intValue, str4, this.f680a, str, str2, intValue2, str5, bool.booleanValue(), bool2.booleanValue()) : adFetcher.fetchAd(Adtype.BANNER, context, str3, intValue, str4, this.f680a, str, str2, intValue2, str5, bool.booleanValue(), bool2.booleanValue(), z, null, null, null, false, i, 0, 0, 0, 0, null, bool3.booleanValue(), bool4.booleanValue());
        if (m390a(str5)) {
            try {
                AdMarvelNativeAd adMarvelNativeAd = new AdMarvelNativeAd();
                Builder builder = new Builder();
                builder.context(context);
                builder.partnerId(str);
                builder.siteId(str2);
                builder.targetParams(this.f680a);
                if (adMarvelView != null) {
                    adMarvelNativeAd.setListener(adMarvelView.nativeAdListener);
                    adMarvelNativeAd.setAdMarvelNativeVideoAdListener(adMarvelView.nativeVideoAdListener);
                }
                adMarvelNativeAd.loadNativeAdThroghAdMarvelView(builder.build(), str5);
                return adMarvelNativeAd;
            } catch (Exception e6) {
                e6.printStackTrace();
                return null;
            }
        }
        Object adMarvelAd = new AdMarvelAd(str5, this.f680a, str, str2, str3, intValue, str4, context.getPackageName());
        if (AdMarvelUtils.isLogDumpEnabled()) {
            adMarvelAd.setRequestJson(adFetcher.getRequestJson());
        }
        if (AdMarvelView.enableOfflineSDK) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("admarvel_preferences", 0);
            String string = sharedPreferences.getString("banner_folder", "NULL");
            adMarvelAd.setOfflineBaseUrl("file://" + sharedPreferences.getString("childDirectory", "NULL") + ReadOnlyContext.SEPARATOR + string);
            adMarvelAd.setOfflinekeyUrl(sharedPreferences.getString("childDirectory", "NULL") + ReadOnlyContext.SEPARATOR + string);
        }
        if (str5 != null) {
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
                        AdMarvelView adMarvelView2 = (AdMarvelView) this.f681b.get();
                        return adMarvelView2 != null ? AdMarvelAdapterInstances.getInstance(adMarvelView2.ADMARVEL_VIEW_GUID, adMarvelAd.getSdkNetwork()).loadAd(adMarvelAd, loadAd) : adMarvelAd;
                    } catch (Throwable e7) {
                        Logging.log(Log.getStackTraceString(e7));
                        adMarvelAd.setAdType(AdType.ERROR);
                        adMarvelAd.setErrorCode(303);
                        return adMarvelAd;
                    }
                }
            } catch (Throwable e72) {
                try {
                    Logging.log(Log.getStackTraceString(e72));
                    adMarvelAd.setAdType(AdType.ERROR);
                    adMarvelAd.setErrorCode(303);
                    return adMarvelAd;
                } catch (Exception e8) {
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

    protected void onPostExecute(Object object) {
        super.onPostExecute(object);
        if (!(object instanceof AdMarvelNativeAd)) {
            AdMarvelView adMarvelView;
            ErrorReason a;
            if (object instanceof AdMarvelAd) {
                try {
                    AdMarvelAd adMarvelAd = (AdMarvelAd) object;
                    if (adMarvelAd.getAdType() == AdType.ERROR) {
                        adMarvelView = (AdMarvelView) this.f681b.get();
                        if (adMarvelView != null) {
                            a = Utils.m178a(adMarvelAd.getErrorCode());
                            adMarvelView.getListenerImpl().m420a(adMarvelView.getContext(), adMarvelView, Utils.m177a(a), a, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress());
                            return;
                        }
                        return;
                    }
                    AdMarvelView adMarvelView2;
                    if (this.f681b.get() != null) {
                        adMarvelView2 = (AdMarvelView) this.f681b.get();
                        if (adMarvelView2 != null && adMarvelView2.isAdFetchedModel()) {
                            if (adMarvelView2.getListenerImpl() != null) {
                                adMarvelView2.getListenerImpl().m426a(adMarvelView2, adMarvelAd);
                                return;
                            }
                            return;
                        }
                    }
                    if (adMarvelAd.getAdType() == AdType.SDKCALL) {
                        Context context;
                        if (adMarvelAd.getSdkNetwork() != null) {
                            adMarvelView2 = (AdMarvelView) this.f681b.get();
                            context = (Context) this.f682c.get();
                            if (adMarvelView2 != null && context != null) {
                                adMarvelView2.requestPendingAd(this.f680a, adMarvelAd, adMarvelAd.getSdkNetwork(), context);
                                return;
                            }
                            return;
                        } else if (adMarvelAd.isDisableAdrequest()) {
                            String disableAdDuration = adMarvelAd.getDisableAdDuration();
                            if (disableAdDuration != null) {
                                adMarvelView2 = (AdMarvelView) this.f681b.get();
                                context = (Context) this.f682c.get();
                                if (adMarvelView2 != null && context != null) {
                                    adMarvelView2.disableAdRequest(disableAdDuration, adMarvelAd, context);
                                    return;
                                }
                                return;
                            }
                        }
                    }
                    adMarvelView2 = (AdMarvelView) this.f681b.get();
                    if (adMarvelView2 != null) {
                        adMarvelView2.requestInternalPendingAd(adMarvelAd);
                    }
                } catch (Throwable e) {
                    Logging.log(Log.getStackTraceString(e));
                    a = Utils.m178a(303);
                    int a2 = Utils.m177a(a);
                    adMarvelView = (AdMarvelView) this.f681b.get();
                    AdMarvelAd adMarvelAd2 = (AdMarvelAd) object;
                    if (adMarvelView != null && adMarvelAd2 != null) {
                        adMarvelView.getListenerImpl().m420a(adMarvelView.getContext(), adMarvelView, a2, a, adMarvelAd2.getSiteId(), adMarvelAd2.getId(), adMarvelAd2.getTargetParams(), adMarvelAd2.getIpAddress());
                    }
                }
            } else if (object == null) {
                try {
                    adMarvelView = (AdMarvelView) this.f681b.get();
                    if (adMarvelView != null) {
                        a = Utils.m178a(303);
                        adMarvelView.getListenerImpl().m420a(adMarvelView.getContext(), adMarvelView, Utils.m177a(a), a, null, 0, null, Stomp.EMPTY);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
