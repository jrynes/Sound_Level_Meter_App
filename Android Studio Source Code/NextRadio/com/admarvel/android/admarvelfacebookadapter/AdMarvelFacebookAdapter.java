package com.admarvel.android.admarvelfacebookadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import com.admarvel.android.ads.AdMarvelAd;
import com.admarvel.android.ads.AdMarvelAd.AdType;
import com.admarvel.android.ads.AdMarvelAdapter;
import com.admarvel.android.ads.AdMarvelAdapterListener;
import com.admarvel.android.ads.AdMarvelInterstitialAdapterListener;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.ads.AdMarvelXMLElement;
import com.admarvel.android.ads.AdMarvelXMLReader;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd;
import com.admarvel.android.util.Logging;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.NativeAd;
import com.facebook.ads.internal.AdSdkVersion;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;

public class AdMarvelFacebookAdapter extends AdMarvelAdapter {
    String WEBVIEW_GUID;
    private WeakReference contextWeakReference;
    public NativeAd fbNativeAd;
    private InterstitialAd interstitialAd;
    private View nativeAdContainerView;

    private String getAdNetworkSDKDate() {
        return "2015-10-07";
    }

    private String[] getFacebookTestDeviceIdStr(AdMarvelXMLElement adMarvelXMLElement) {
        String str = (String) adMarvelXMLElement.getAttributes().get("testdevices");
        if (str != null && str.length() > 0) {
            String[] split = str.split(Stomp.COMMA);
            if (split != null && split.length > 0) {
                return split;
            }
        }
        return null;
    }

    public void cleanupView(View view) {
        if (view instanceof AdView) {
            AdView adView = (AdView) view;
            adView.setAdListener(null);
            adView.destroy();
            Logging.log("Facebook Adapter: cleanup current view");
        }
    }

    public void create(Activity activity) {
    }

    public void destroy(View view) {
        if (view instanceof AdView) {
            AdView adView = (AdView) view;
            adView.setAdListener(null);
            adView.destroy();
            Logging.log("Facebook Adapter: destroy current view");
        }
    }

    protected boolean displayInterstitial(Activity activity, boolean z) {
        if (this.interstitialAd == null || !this.interstitialAd.isAdLoaded()) {
            return false;
        }
        Logging.log("Facebook Adapter: displayInterstitial");
        return this.interstitialAd.show();
    }

    protected void forceCloseFullScreenAd(Activity activity) {
    }

    public int getAdAvailablityStatus() {
        return 0;
    }

    public int getAdAvailablityStatus(String str, Context context) {
        return 0;
    }

    public String getAdNetworkSDKVersion() {
        return AdSdkVersion.BUILD;
    }

    public String getAdapterVersion() {
        return C0122b.f3a;
    }

    public String getAdnetworkSDKVersionInfo() {
        return "admarvel_version: " + C0122b.f3a + "; facebook_sdk_version: " + getAdNetworkSDKVersion() + "; date: " + getAdNetworkSDKDate();
    }

    public void handleBackKeyPressed(Activity activity) {
    }

    public void handleClick() {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() != null && AdMarvelUtils.getAdMarvelOptionalFlags().containsKey("FACEBOOK_NO_VIEW_REGISTERED") && ((String) AdMarvelUtils.getAdMarvelOptionalFlags().get("FACEBOOK_NO_VIEW_REGISTERED")).equals(Stomp.TRUE) && this.contextWeakReference != null && this.contextWeakReference.get() != null) {
            LocalBroadcastManager.getInstance((Context) this.contextWeakReference.get()).sendBroadcast(new Intent("com.facebook.ads.native.click:" + this.fbNativeAd.getId()));
        }
    }

    public void handleConfigChanges(Activity activity, Configuration configuration) {
    }

    public void handleImpression() {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() != null && AdMarvelUtils.getAdMarvelOptionalFlags().containsKey("FACEBOOK_NO_VIEW_REGISTERED") && ((String) AdMarvelUtils.getAdMarvelOptionalFlags().get("FACEBOOK_NO_VIEW_REGISTERED")).equals(Stomp.TRUE) && this.contextWeakReference != null && this.contextWeakReference.get() != null) {
            LocalBroadcastManager.getInstance((Context) this.contextWeakReference.get()).sendBroadcast(new Intent("com.facebook.ads.native.impression:" + this.fbNativeAd.getId()));
        }
    }

    public void handleNotice() {
    }

    public void initialize(Activity activity, Map map) {
    }

    protected AdMarvelAd loadAd(AdMarvelAd adMarvelAd, AdMarvelXMLReader adMarvelXMLReader) {
        Logging.log("Facebook SDK Adapter - loadAd");
        AdMarvelXMLElement parsedXMLData = adMarvelXMLReader.getParsedXMLData();
        String str = (String) parsedXMLData.getAttributes().get("placementid");
        if (str == null || str.length() <= 0) {
            adMarvelAd.setAdType(AdType.ERROR);
            adMarvelAd.setErrorCode(306);
            adMarvelAd.setErrorReason("Missing SDK publisher id");
        } else {
            adMarvelAd.setPubId(str);
        }
        String[] facebookTestDeviceIdStr = getFacebookTestDeviceIdStr(parsedXMLData);
        if (facebookTestDeviceIdStr != null && facebookTestDeviceIdStr.length > 0) {
            adMarvelAd.setFacebookTestDeviceId(facebookTestDeviceIdStr);
        }
        str = (String) parsedXMLData.getAttributes().get("childdirected");
        if (str != null && str.length() > 0) {
            adMarvelAd.setFacebookChildDirectedFlag(str);
        }
        str = (String) parsedXMLData.getAttributes().get("adsize");
        if (str != null && str.length() > 0) {
            adMarvelAd.setFacebookAdSize(str);
        }
        return adMarvelAd;
    }

    public Object loadNativeAd(AdMarvelNativeAd adMarvelNativeAd, AdMarvelXMLReader adMarvelXMLReader) {
        Logging.log("Facebook SDK Adapter - loadNativeAd");
        try {
            AdMarvelXMLElement parsedXMLData = adMarvelXMLReader.getParsedXMLData();
            String str = (String) parsedXMLData.getAttributes().get("placementid");
            if (str == null || str.length() <= 0) {
                adMarvelNativeAd.setNativeAdErrorTypeFromAdapter("Missing SDK publisher id");
            } else {
                adMarvelNativeAd.setPubId(str);
            }
            String[] facebookTestDeviceIdStr = getFacebookTestDeviceIdStr(parsedXMLData);
            if (facebookTestDeviceIdStr != null && facebookTestDeviceIdStr.length > 0) {
                adMarvelNativeAd.setFacebookTestDeviceId(facebookTestDeviceIdStr);
            }
            str = (String) parsedXMLData.getAttributes().get("childdirected");
            if (str != null && str.length() > 0) {
                adMarvelNativeAd.setFacebookChildDirectedFlag(str);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return adMarvelNativeAd;
    }

    public void notifyAddedToListView(View view) {
    }

    public void pause(Activity activity, View view) {
    }

    public void registerViewForInteraction(View view) {
        if (this.fbNativeAd != null) {
            Logging.log("Facebook Adapter: registerViewForInteraction");
            this.nativeAdContainerView = view;
            this.fbNativeAd.registerViewForInteraction(view);
        }
    }

    public void registerViewForInteraction(View[] viewArr) {
        if (this.fbNativeAd != null && this.nativeAdContainerView != null) {
            Logging.log("Facebook Adapter - registerViewForInteraction2");
            unregisterView();
            this.fbNativeAd.registerViewForInteraction(this.nativeAdContainerView, Arrays.asList(viewArr));
        }
    }

    protected void requestIntersitialNewAd(AdMarvelInterstitialAdapterListener adMarvelInterstitialAdapterListener, Context context, AdMarvelAd adMarvelAd, Map map, int i, int i2, String str) {
        Logging.log("Facebook SDK Adapter : requestIntersitialNewAd");
        String pubId = adMarvelAd.getPubId();
        String[] facebookTestDeviceId = adMarvelAd.getFacebookTestDeviceId();
        String facebookChildDirectedFlag = adMarvelAd.getFacebookChildDirectedFlag();
        if (facebookTestDeviceId != null && facebookTestDeviceId.length > 0) {
            Collection hashSet = new HashSet();
            Collections.addAll(hashSet, facebookTestDeviceId);
            AdSettings.addTestDevices(hashSet);
        }
        if (str != null) {
            this.WEBVIEW_GUID = str;
        }
        if (facebookChildDirectedFlag != null && facebookChildDirectedFlag.trim().length() > 0) {
            if (facebookChildDirectedFlag.equalsIgnoreCase("yes")) {
                AdSettings.setIsChildDirected(true);
            } else if (facebookChildDirectedFlag.equalsIgnoreCase("no")) {
                AdSettings.setIsChildDirected(false);
            }
        }
        if (pubId != null && pubId.trim().length() > 0) {
            if (this.interstitialAd != null) {
                this.interstitialAd.setAdListener(null);
                this.interstitialAd.destroy();
                this.interstitialAd = null;
            }
            this.interstitialAd = new InterstitialAd(context, pubId);
            this.interstitialAd.setAdListener(new InternalFacebookInterstitialListener(adMarvelInterstitialAdapterListener, adMarvelAd, this.interstitialAd, context, str));
            this.interstitialAd.loadAd();
        } else if (adMarvelInterstitialAdapterListener != null) {
            adMarvelInterstitialAdapterListener.onFailedToReceiveInterstitialAd(SDKAdNetwork.FACEBOOK, adMarvelAd.getPubId(), 205, AdMarvelUtils.getErrorReason(205), adMarvelAd);
            Logging.log("Facebook Adapter : onFailedToReceiveInterstitialAd");
        }
    }

    public Object requestNativeAd(AdMarvelAdapterListener adMarvelAdapterListener, AdMarvelNativeAd adMarvelNativeAd) {
        Logging.log("Facebook Ads Adapter: requestNativeAd");
        if (adMarvelNativeAd != null) {
            try {
                AdListener internalFacebookNativeListener = new InternalFacebookNativeListener(adMarvelAdapterListener, adMarvelNativeAd);
                String pubId = adMarvelNativeAd.getPubId();
                String[] facebookTestDeviceId = adMarvelNativeAd.getFacebookTestDeviceId();
                String facebookChildDirectedFlag = adMarvelNativeAd.getFacebookChildDirectedFlag();
                if (facebookTestDeviceId != null && facebookTestDeviceId.length > 0) {
                    Collection hashSet = new HashSet();
                    Collections.addAll(hashSet, facebookTestDeviceId);
                    AdSettings.addTestDevices(hashSet);
                }
                if (facebookChildDirectedFlag != null && facebookChildDirectedFlag.trim().length() > 0) {
                    if (facebookChildDirectedFlag.equalsIgnoreCase("yes")) {
                        AdSettings.setIsChildDirected(true);
                    } else if (facebookChildDirectedFlag.equalsIgnoreCase("no")) {
                        AdSettings.setIsChildDirected(false);
                    }
                }
                Context context = adMarvelNativeAd.getmContext();
                if (context != null) {
                    this.contextWeakReference = new WeakReference(context);
                    this.fbNativeAd = new NativeAd(context, pubId);
                    this.fbNativeAd.setAdListener(internalFacebookNativeListener);
                    this.fbNativeAd.loadAd();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return this.fbNativeAd;
    }

    public View requestNewAd(AdMarvelAdapterListener adMarvelAdapterListener, Context context, AdMarvelAd adMarvelAd, Map map, int i, int i2) {
        Logging.log("Facebook Ads Adapter: requestNewAd");
        if (adMarvelAd == null) {
            return null;
        }
        AdListener internalFacebookListener = new InternalFacebookListener(adMarvelAdapterListener);
        String pubId = adMarvelAd.getPubId();
        String[] facebookTestDeviceId = adMarvelAd.getFacebookTestDeviceId();
        String facebookChildDirectedFlag = adMarvelAd.getFacebookChildDirectedFlag();
        if (facebookTestDeviceId != null && facebookTestDeviceId.length > 0) {
            Collection hashSet = new HashSet();
            Collections.addAll(hashSet, facebookTestDeviceId);
            AdSettings.addTestDevices(hashSet);
        }
        if (facebookChildDirectedFlag != null && facebookChildDirectedFlag.trim().length() > 0) {
            if (facebookChildDirectedFlag.equalsIgnoreCase("yes")) {
                AdSettings.setIsChildDirected(true);
            } else if (facebookChildDirectedFlag.equalsIgnoreCase("no")) {
                AdSettings.setIsChildDirected(false);
            }
        }
        AdSize adSize = AdSize.BANNER_HEIGHT_50;
        facebookChildDirectedFlag = adMarvelAd.getFacebookAdSize();
        if (facebookChildDirectedFlag != null && facebookChildDirectedFlag.length() > 0) {
            if (facebookChildDirectedFlag.equalsIgnoreCase("BANNER_HEIGHT_50")) {
                adSize = AdSize.BANNER_HEIGHT_50;
            } else if (facebookChildDirectedFlag.equalsIgnoreCase("BANNER_320_50")) {
                adSize = AdSize.BANNER_320_50;
            } else if (facebookChildDirectedFlag.equalsIgnoreCase("BANNER_HEIGHT_90")) {
                adSize = AdSize.BANNER_HEIGHT_90;
            } else if (facebookChildDirectedFlag.equalsIgnoreCase("RECTANGLE_HEIGHT_250")) {
                adSize = AdSize.RECTANGLE_HEIGHT_250;
            }
        }
        if (pubId != null && pubId.trim().length() > 0) {
            View adView = new AdView(context, pubId, adSize);
            adView.setAdListener(internalFacebookListener);
            adView.disableAutoRefresh();
            adView.loadAd();
            return adView;
        } else if (adMarvelAdapterListener == null) {
            return null;
        } else {
            adMarvelAdapterListener.onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
            return null;
        }
    }

    public void resume(Activity activity, View view) {
    }

    public void setUserId(String str) {
    }

    public void start(Activity activity, View view) {
    }

    public void stop(Activity activity, View view) {
    }

    public void unregisterView() {
        if (this.fbNativeAd != null) {
            this.fbNativeAd.unregisterView();
        }
    }
}
