package com.admarvel.android.admarvelgoogleplayadapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
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
import com.google.ads.mediation.AbstractAdViewAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;

public class AdMarvelGooglePlayAdapter extends AdMarvelAdapter {
    boolean disableImageLoading;
    private InternalGooglePlayNativeListener internalGooglePlayNativeListener;
    private InterstitialAd interstitialAd;
    private int preferredImageOrientation;
    private boolean shouldRequestMultipleImages;
    private Location userLocation;

    public AdMarvelGooglePlayAdapter() {
        this.userLocation = null;
        this.shouldRequestMultipleImages = false;
        this.disableImageLoading = false;
    }

    private String getAdNetworkSDKDate() {
        return Stomp.EMPTY;
    }

    protected void cleanupView(View view) {
        if (view instanceof AdView) {
            AdView adView = (AdView) view;
            adView.destroy();
            adView.setAdListener(null);
            Logging.log("GooglePlay Ads Adapter: cleanup current view");
        }
    }

    public void create(Activity activity) {
    }

    public void destroy(View view) {
        if (view instanceof AdView) {
            AdView adView = (AdView) view;
            adView.destroy();
            adView.setAdListener(null);
            Logging.log("GooglePlay Ads Adapter: destroy current view");
        }
        this.interstitialAd = null;
    }

    protected boolean displayInterstitial(Activity activity, boolean z) {
        Logging.log("GooglePlay Ads Adapter: displayInterstitial");
        if (!z) {
            try {
                if (this.interstitialAd != null && this.interstitialAd.isLoaded()) {
                    this.interstitialAd.show();
                    return true;
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
        return false;
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
        return Stomp.EMPTY;
    }

    public String getAdapterVersion() {
        return C0129g.f15a;
    }

    public String getAdnetworkSDKVersionInfo() {
        return "admarvel_version: " + C0129g.f15a;
    }

    public void handleBackKeyPressed(Activity activity) {
    }

    public void handleClick() {
    }

    public void handleConfigChanges(Activity activity, Configuration configuration) {
    }

    public void handleImpression() {
    }

    public void handleNotice() {
    }

    public void initialize(Activity activity, Map map) {
    }

    protected AdMarvelAd loadAd(AdMarvelAd adMarvelAd, AdMarvelXMLReader adMarvelXMLReader) {
        Logging.log("GooglePlay Ads Adapter: loadAd");
        AdMarvelXMLElement parsedXMLData = adMarvelXMLReader.getParsedXMLData();
        String str = (String) parsedXMLData.getAttributes().get(AbstractAdViewAdapter.AD_UNIT_ID_PARAMETER);
        if (str == null || str.length() <= 0) {
            adMarvelAd.setAdType(AdType.ERROR);
            adMarvelAd.setErrorCode(306);
            adMarvelAd.setErrorReason("Missing SDK publisher id");
        } else {
            adMarvelAd.setPubId(str);
        }
        str = (String) parsedXMLData.getAttributes().get("testdeviceids");
        if (str != null && str.length() > 0) {
            String[] split = str.split(Stomp.COMMA);
            if (split != null && split.length > 0) {
                adMarvelAd.setAdmobTestDeviceId(split);
            }
        }
        str = (String) parsedXMLData.getAttributes().get("creativetype");
        if (str != null && str.length() > 0) {
            adMarvelAd.setCreativeType(str);
        }
        str = (String) parsedXMLData.getAttributes().get("admobextras");
        if (str != null && str.length() > 0) {
            adMarvelAd.setAdMobExtras(str);
        }
        str = (String) parsedXMLData.getAttributes().get("location");
        if (str != null && str.length() > 0) {
            adMarvelAd.setGooglePlayLocation(str);
        }
        return adMarvelAd;
    }

    public Object loadNativeAd(AdMarvelNativeAd adMarvelNativeAd, AdMarvelXMLReader adMarvelXMLReader) {
        Logging.log("GooglePlay SDK Ads Adapter - loadNativeAd");
        try {
            AdMarvelXMLElement parsedXMLData = adMarvelXMLReader.getParsedXMLData();
            String str = (String) parsedXMLData.getAttributes().get(AbstractAdViewAdapter.AD_UNIT_ID_PARAMETER);
            if (str == null || str.length() <= 0) {
                adMarvelNativeAd.setNativeAdErrorTypeFromAdapter("Missing SDK app id");
            } else {
                adMarvelNativeAd.setPubId(str);
            }
            str = (String) parsedXMLData.getAttributes().get("disableImageLoading");
            if (str == null || !"YES".equalsIgnoreCase(str)) {
                this.disableImageLoading = false;
            } else {
                this.disableImageLoading = true;
            }
            str = (String) parsedXMLData.getAttributes().get("shouldRequestMultipleImages");
            if (str == null || !"YES".equalsIgnoreCase(str)) {
                this.shouldRequestMultipleImages = false;
            } else {
                this.shouldRequestMultipleImages = true;
            }
            str = (String) parsedXMLData.getAttributes().get("preferredImageOrientation");
            if (str == null || !"YES".equalsIgnoreCase(str)) {
                this.preferredImageOrientation = 0;
                return adMarvelNativeAd;
            }
            if (DeviceInfo.ORIENTATION_PORTRAIT.equalsIgnoreCase(str)) {
                this.preferredImageOrientation = 1;
            } else if (DeviceInfo.ORIENTATION_LANDSCAPE.equalsIgnoreCase(str)) {
                this.preferredImageOrientation = 2;
            } else {
                this.preferredImageOrientation = 0;
            }
            return adMarvelNativeAd;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void notifyAddedToListView(View view) {
    }

    public void pause(Activity activity, View view) {
        if (view instanceof AdView) {
            ((AdView) view).pause();
            Logging.log("GooglePlay Ads Adapter: pause");
        }
    }

    public void registerViewForInteraction(View view) {
        if (view != null && this.internalGooglePlayNativeListener != null) {
            if (this.internalGooglePlayNativeListener.f11c != null || this.internalGooglePlayNativeListener.f12d != null) {
                try {
                    if (view instanceof NativeContentAdView) {
                        Logging.log("GooglePlay Ads Adapter: registerViewForInteraction");
                        ((NativeContentAdView) view).setNativeAd(this.internalGooglePlayNativeListener.f11c);
                    } else if (view instanceof NativeAppInstallAdView) {
                        Logging.log("GooglePlay Ads Adapter: registerViewForInteraction");
                        ((NativeAppInstallAdView) view).setNativeAd(this.internalGooglePlayNativeListener.f12d);
                    }
                } catch (Throwable e) {
                    Logging.log(Log.getStackTraceString(e));
                }
            }
        }
    }

    public void registerViewForInteraction(View[] viewArr) {
    }

    protected void requestIntersitialNewAd(AdMarvelInterstitialAdapterListener adMarvelInterstitialAdapterListener, Context context, AdMarvelAd adMarvelAd, Map map, int i, int i2, String str) {
        Logging.log("GooglePlay Ads Adapter: requestIntersitialNewAd");
        AdListener internalGooglePlayInterstitialListener = new InternalGooglePlayInterstitialListener(adMarvelInterstitialAdapterListener, adMarvelAd, context, str);
        String str2 = null;
        String str3 = null;
        String str4 = null;
        if (!adMarvelAd.isRewardInterstitial()) {
            if (map != null) {
                try {
                    str2 = (String) map.get("GENDER");
                } catch (Exception e) {
                    Logging.log("Exception in Seting Target option" + e.getMessage());
                }
                try {
                    str3 = (String) map.get("KEYWORDS");
                } catch (Exception e2) {
                    Logging.log("Exception in Seting Target option" + e2.getMessage());
                }
                try {
                    str4 = (String) map.get("DOB");
                } catch (Exception e22) {
                    Logging.log("Exception in Seting Target option" + e22.getMessage());
                }
                if (map.get("LOCATION_OBJECT") != null) {
                    try {
                        this.userLocation = (Location) map.get("LOCATION_OBJECT");
                    } catch (Exception e222) {
                        Logging.log("Exception in Seting userLocation Target option" + e222.getMessage());
                    }
                }
            }
            if (this.userLocation == null && adMarvelAd.getGooglePlayLocation() != null) {
                try {
                    String[] split = adMarvelAd.getGooglePlayLocation().split(Stomp.COMMA);
                    if (split.length == 2) {
                        this.userLocation = new Location(((LocationManager) context.getSystemService("location")).getBestProvider(new Criteria(), true));
                        this.userLocation.setLatitude(Double.valueOf(split[0]).doubleValue());
                        this.userLocation.setLongitude(Double.valueOf(split[1]).doubleValue());
                    }
                } catch (Exception e2222) {
                    Logging.log("Exception in Seting userLocation Target option" + e2222.getMessage());
                }
            }
            Builder builder = new Builder();
            if (str2 != null && str2.length() > 0) {
                if (str2.toLowerCase().startsWith("m")) {
                    builder.setGender(0);
                } else if (str2.toLowerCase().startsWith("f")) {
                    builder.setGender(1);
                }
            }
            if (str4 != null && str4.length() > 0) {
                try {
                    builder.setBirthday(Date.valueOf(str4));
                } catch (Throwable e3) {
                    Logging.log(Log.getStackTraceString(e3));
                }
            }
            if (str3 != null && str3.length() > 0) {
                for (String str22 : str3.split(" ")) {
                    builder.addTestDevice(str22);
                }
            }
            if (adMarvelAd.getAdmobTestDeviceId() != null && adMarvelAd.getAdmobTestDeviceId().length > 0) {
                for (String str222 : adMarvelAd.getAdmobTestDeviceId()) {
                    builder.addTestDevice(str222);
                }
            }
            if (this.userLocation != null) {
                builder.setLocation(this.userLocation);
            }
            if (context instanceof Activity) {
                if (this.interstitialAd != null) {
                    this.interstitialAd.setAdListener(null);
                    this.interstitialAd = null;
                }
                this.interstitialAd = new InterstitialAd((Activity) context);
                this.interstitialAd.setAdUnitId(adMarvelAd.getPubId());
                this.interstitialAd.setAdListener(internalGooglePlayInterstitialListener);
                this.interstitialAd.loadAd(builder.build());
            } else if (adMarvelInterstitialAdapterListener != null) {
                adMarvelInterstitialAdapterListener.onFailedToReceiveInterstitialAd(SDKAdNetwork.GOOGLEPLAY, adMarvelAd.getPubId(), 205, AdMarvelUtils.getErrorReason(205), adMarvelAd);
            }
        }
    }

    public Object requestNativeAd(AdMarvelAdapterListener adMarvelAdapterListener, AdMarvelNativeAd adMarvelNativeAd) {
        Logging.log("GooglePlay Ads Adapter: requestNativeAd");
        if (adMarvelNativeAd != null) {
            try {
                Context context = adMarvelNativeAd.getmContext();
                String pubId = adMarvelNativeAd.getPubId();
                this.internalGooglePlayNativeListener = new InternalGooglePlayNativeListener(adMarvelAdapterListener, adMarvelNativeAd, this);
                new AdLoader.Builder(context, pubId).forAppInstallAd(this.internalGooglePlayNativeListener).forContentAd(this.internalGooglePlayNativeListener).withNativeAdOptions(new NativeAdOptions.Builder().setImageOrientation(this.preferredImageOrientation).setRequestMultipleImages(this.shouldRequestMultipleImages).setReturnUrlsForImageAssets(this.disableImageLoading).build()).withAdListener(this.internalGooglePlayNativeListener).build().loadAd(new PublisherAdRequest.Builder().build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public View requestNewAd(AdMarvelAdapterListener adMarvelAdapterListener, Context context, AdMarvelAd adMarvelAd, Map map, int i, int i2) {
        Logging.log("GooglePlay Ads Adapter: requestNewAd");
        if (adMarvelAd == null) {
            return null;
        }
        AdListener internalGooglePlayListener = new InternalGooglePlayListener(adMarvelAdapterListener, adMarvelAd);
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        if (map != null) {
            try {
                str = (String) map.get("GENDER");
            } catch (Exception e) {
                Logging.log("Exception in setting gender target param " + e.getMessage());
            }
            try {
                str2 = (String) map.get("KEYWORDS");
            } catch (Exception e2) {
                Logging.log("Exception in setting keywords target param " + e2.getMessage());
            }
            try {
                str3 = (String) map.get("DOB");
            } catch (Exception e22) {
                Logging.log("Exception in setting dob target param " + e22.getMessage());
            }
            try {
                str4 = (String) map.get("ADMOBEXTRAS");
            } catch (Exception e222) {
                Logging.log("Exception in setting admobextrasParams target param " + e222.getMessage());
            }
            if (map.get("LOCATION_OBJECT") != null) {
                try {
                    this.userLocation = (Location) map.get("LOCATION_OBJECT");
                } catch (Exception e2222) {
                    Logging.log("Exception in Seting userLocation Target option" + e2222.getMessage());
                }
            }
        }
        if (this.userLocation == null && adMarvelAd.getGooglePlayLocation() != null) {
            try {
                String[] split = adMarvelAd.getGooglePlayLocation().split(Stomp.COMMA);
                if (split.length == 2) {
                    this.userLocation = new Location(((LocationManager) context.getSystemService("location")).getBestProvider(new Criteria(), true));
                    this.userLocation.setLatitude(Double.valueOf(split[0]).doubleValue());
                    this.userLocation.setLongitude(Double.valueOf(split[1]).doubleValue());
                }
            } catch (Exception e22222) {
                Logging.log("Exception in Seting userLocation Target option" + e22222.getMessage());
            }
        }
        Builder builder = new Builder();
        if (str != null && str.length() > 0) {
            if (str.toLowerCase().startsWith("m")) {
                builder.setGender(0);
            } else if (str.toLowerCase().startsWith("f")) {
                builder.setGender(1);
            }
        }
        if (str3 != null && str3.length() > 0) {
            try {
                builder.setBirthday(Date.valueOf(str3));
            } catch (Throwable e3) {
                Logging.log(Log.getStackTraceString(e3));
            }
        }
        if (str2 != null && str2.length() > 0) {
            for (String str5 : str2.split(" ")) {
                builder.addKeyword(str5);
            }
        }
        if (adMarvelAd.getAdmobTestDeviceId() != null) {
            for (String str52 : adMarvelAd.getAdmobTestDeviceId()) {
                builder.addTestDevice(str52);
            }
        }
        if (this.userLocation != null) {
            builder.setLocation(this.userLocation);
        }
        AdSize adSize = AdSize.BANNER;
        if (adMarvelAd.getCreativeType() != null) {
            if (adMarvelAd.getCreativeType().equals("IAB_BANNER")) {
                adSize = AdSize.BANNER;
            } else if (adMarvelAd.getCreativeType().equals("IAB_LEADERBOARD")) {
                adSize = AdSize.LEADERBOARD;
            } else if (adMarvelAd.getCreativeType().equals("IAB_MRECT")) {
                adSize = AdSize.MEDIUM_RECTANGLE;
            } else if (adMarvelAd.getCreativeType().equals("SMART_BANNER")) {
                adSize = AdSize.SMART_BANNER;
            }
        }
        if (adMarvelAd.getAdMobExtras() != null && adMarvelAd.getAdMobExtras().length() > 0) {
            str4 = adMarvelAd.getAdMobExtras();
        }
        if (str4 != null && str4.length() > 0) {
            String[] split2 = str4.split(Stomp.COMMA);
            if (split2 != null && split2.length > 0) {
                Bundle bundle = new Bundle();
                for (String str32 : split2) {
                    String[] split3 = str32.split(Headers.SEPERATOR);
                    if (split3 != null && split3.length == 2 && split3[0].length() > 0 && split3[1].length() > 0) {
                        try {
                            bundle.putString(URLDecoder.decode(split3[0], HttpRequest.CHARSET_UTF8), URLDecoder.decode(split3[1], HttpRequest.CHARSET_UTF8));
                        } catch (UnsupportedEncodingException e4) {
                            e4.printStackTrace();
                        }
                    }
                }
                builder.addNetworkExtras(new AdMobExtras(bundle));
            }
        }
        if (context == null || !(context instanceof Activity)) {
            if (adMarvelAdapterListener != null) {
                adMarvelAdapterListener.onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
            }
            return null;
        }
        View adView = new AdView((Activity) context);
        adView.setAdUnitId(adMarvelAd.getPubId());
        adView.setAdSize(adSize);
        adView.setLayoutParams(new LayoutParams(-1, -2));
        adView.setBackgroundColor(i);
        adView.setAdListener(internalGooglePlayListener);
        adView.loadAd(builder.build());
        return adView;
    }

    public void resume(Activity activity, View view) {
        if (view instanceof AdView) {
            ((AdView) view).resume();
            Logging.log("GooglePlay Ads Adapter: resume");
        }
    }

    public void setUserId(String str) {
    }

    public void start(Activity activity, View view) {
    }

    public void stop(Activity activity, View view) {
    }

    public void unregisterView() {
    }
}
