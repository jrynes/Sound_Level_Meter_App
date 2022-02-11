package com.admarvel.android.admarvelamazonadapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
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
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdSize;
import com.amazon.device.ads.AdTargetingOptions;
import com.amazon.device.ads.InterstitialAd;
import com.google.ads.mediation.AbstractAdViewAdapter;
import com.rabbitmq.client.AMQP;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.transport.stomp.StompSubscription;
import org.xbill.DNS.Type;

public class AdMarvelAmazonAdapter extends AdMarvelAdapter {
    String WEBVIEW_GUID;
    private InterstitialAd interstitialAd;

    private String getAdNetworkSDKDate() {
        return "2015-06-08";
    }

    protected void cleanupView(View view) {
        if (view != null && (view instanceof AdLayout)) {
            AdLayout adLayout = (AdLayout) view;
            adLayout.destroy();
            adLayout.setListener(null);
            Logging.log("Amazon SDK Adapter - cleanupView");
        }
    }

    public void create(Activity activity) {
    }

    public void destroy(View view) {
        if (view != null && (view instanceof AdLayout)) {
            AdLayout adLayout = (AdLayout) view;
            adLayout.destroy();
            adLayout.setListener(null);
            Logging.log("Amazon SDK Adapter - destroy view");
        }
    }

    protected boolean displayInterstitial(Activity activity, boolean z) {
        if (this.interstitialAd == null || !this.interstitialAd.isReady() || this.interstitialAd.isShowing()) {
            return false;
        }
        if (this.interstitialAd.showAd()) {
            Logging.log("admarvelAmazonAdapter: interstitial displayed sucessfully");
            return true;
        }
        Logging.log("admarvelAmazonAdapter: interstitial failed to display");
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
        return AdRegistration.getVersion();
    }

    public String getAdapterVersion() {
        return C0120b.f1a;
    }

    public String getAdnetworkSDKVersionInfo() {
        return "admarvel_version: " + C0120b.f1a + "; amazon_sdk_version: " + getAdNetworkSDKVersion() + "; date: " + getAdNetworkSDKDate();
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
        Logging.log("Amazon SDK Adapter - loadAd");
        AdMarvelXMLElement parsedXMLData = adMarvelXMLReader.getParsedXMLData();
        String str = (String) parsedXMLData.getAttributes().get(AbstractAdViewAdapter.AD_UNIT_ID_PARAMETER);
        if (str == null || str.length() <= 0) {
            adMarvelAd.setAdType(AdType.ERROR);
            adMarvelAd.setErrorCode(306);
            adMarvelAd.setErrorReason("Missing SDK publisher id");
        } else {
            adMarvelAd.setPubId(str);
        }
        str = (String) parsedXMLData.getAttributes().get("test");
        if (str == null || !str.equalsIgnoreCase(Stomp.TRUE)) {
            adMarvelAd.setTest(false);
        } else {
            adMarvelAd.setTest(true);
        }
        str = (String) parsedXMLData.getAttributes().get("adformat");
        if (str == null || str.length() <= 0) {
            adMarvelAd.setAdType(AdType.ERROR);
            adMarvelAd.setErrorCode(306);
            adMarvelAd.setErrorReason("Missing SDK adformat, possible values(300x50,320x50,300x250,600x90,728x90,auto-size,interstitial)");
        } else {
            adMarvelAd.setAdFormat(str);
        }
        str = (String) parsedXMLData.getAttributes().get("advancedoptions");
        if (str != null && str.length() > 0) {
            adMarvelAd.setAmazonAdvancedOptions(str);
        }
        str = (String) parsedXMLData.getAttributes().get("adrequestadvancedoptions");
        if (str != null && str.length() > 0) {
            adMarvelAd.setAmazonAdRequestAdvancedOptions(str);
        }
        str = (String) parsedXMLData.getAttributes().get("timeout");
        if (str != null && str.length() > 0) {
            try {
                adMarvelAd.setAmazonAdTimeOut(Integer.parseInt(str));
            } catch (NumberFormatException e) {
                Logging.log("adMarvel Amazon SDK: InterstitialTimeOut number format exception");
            }
        }
        str = (String) parsedXMLData.getAttributes().get("enableGeoLocation");
        if (str != null && str.length() > 0 && str.equalsIgnoreCase(Stomp.TRUE)) {
            adMarvelAd.setAmazonEnableGeoLocation(true);
        }
        return adMarvelAd;
    }

    public Object loadNativeAd(AdMarvelNativeAd adMarvelNativeAd, AdMarvelXMLReader adMarvelXMLReader) {
        return null;
    }

    public void notifyAddedToListView(View view) {
    }

    public void pause(Activity activity, View view) {
    }

    public void registerViewForInteraction(View view) {
    }

    public void registerViewForInteraction(View[] viewArr) {
    }

    protected void requestIntersitialNewAd(AdMarvelInterstitialAdapterListener adMarvelInterstitialAdapterListener, Context context, AdMarvelAd adMarvelAd, Map map, int i, int i2, String str) {
        String amazonAdvancedOptions;
        Logging.log("Amazon SDK Adapter - requestIntersitialNewAd");
        AdRegistration.enableLogging(AdMarvelUtils.isLoggingEnabled());
        if (adMarvelAd != null) {
            AdRegistration.enableTesting(adMarvelAd.isTest());
        }
        AdTargetingOptions adTargetingOptions = new AdTargetingOptions();
        if (adMarvelAd != null) {
            if (str != null) {
                this.WEBVIEW_GUID = str;
            }
            amazonAdvancedOptions = adMarvelAd.getAmazonAdvancedOptions();
            if (amazonAdvancedOptions != null && amazonAdvancedOptions.contains(Headers.SEPERATOR)) {
                for (String amazonAdvancedOptions2 : amazonAdvancedOptions2.split("\\|")) {
                    String[] split = amazonAdvancedOptions2.split(Headers.SEPERATOR, 2);
                    if (split.length >= 2 && split[0].length() > 0 && split[1].length() > 0) {
                        try {
                            adTargetingOptions.setAdvancedOption(split[0], split[1]);
                        } catch (Throwable e) {
                            Logging.log(Log.getStackTraceString(e));
                        }
                    }
                }
            }
        }
        if (map != null) {
            try {
                amazonAdvancedOptions2 = (String) map.get("AGE");
                if (amazonAdvancedOptions2 != null && amazonAdvancedOptions2.length() > 0) {
                    adTargetingOptions.setAge(Integer.parseInt(amazonAdvancedOptions2));
                }
            } catch (Throwable e2) {
                Logging.log(Log.getStackTraceString(e2));
            }
            if (adMarvelAd != null) {
                try {
                    AdRegistration.setAppKey(adMarvelAd.getPubId());
                } catch (Throwable e22) {
                    Logging.log(Log.getStackTraceString(e22));
                }
            }
        }
        if (adMarvelAd != null) {
            adTargetingOptions.enableGeoLocation(adMarvelAd.isAmazonEnableGeoLocation());
            if (this.interstitialAd != null) {
                Logging.log("Amazon Adapter : cleanup interstitial ad");
                this.interstitialAd.setListener(null);
                this.interstitialAd = null;
            }
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                AdListener internalAmazonInterstitialListener = new InternalAmazonInterstitialListener(adMarvelInterstitialAdapterListener, adMarvelAd, context, str);
                this.interstitialAd = new InterstitialAd(activity);
                this.interstitialAd.setListener(internalAmazonInterstitialListener);
                if (adMarvelAd.getAmazonAdTimeOut() > 0) {
                    this.interstitialAd.setTimeout(adMarvelAd.getAmazonAdTimeOut());
                }
                this.interstitialAd.loadAd(adTargetingOptions);
            } else if (adMarvelInterstitialAdapterListener != null) {
                adMarvelInterstitialAdapterListener.onFailedToReceiveInterstitialAd(SDKAdNetwork.AMAZON, adMarvelAd.getPubId(), 205, AdMarvelUtils.getErrorReason(205), adMarvelAd);
                Logging.log("Amazon Adapter : onFailedToReceiveInterstitialAd");
            }
        }
    }

    public Object requestNativeAd(AdMarvelAdapterListener adMarvelAdapterListener, AdMarvelNativeAd adMarvelNativeAd) {
        return null;
    }

    public View requestNewAd(AdMarvelAdapterListener adMarvelAdapterListener, Context context, AdMarvelAd adMarvelAd, Map map, int i, int i2) {
        String amazonAdvancedOptions;
        int i3;
        Logging.log("Amazon SDK Adapter - requestNewAd");
        AdRegistration.enableLogging(AdMarvelUtils.isLoggingEnabled());
        if (adMarvelAd != null) {
            AdRegistration.enableTesting(adMarvelAd.isTest());
        }
        AdTargetingOptions adTargetingOptions = new AdTargetingOptions();
        if (adMarvelAd != null) {
            amazonAdvancedOptions = adMarvelAd.getAmazonAdvancedOptions();
            if (amazonAdvancedOptions != null && amazonAdvancedOptions.contains(Headers.SEPERATOR)) {
                for (String amazonAdvancedOptions2 : amazonAdvancedOptions2.split("\\|")) {
                    String[] split = amazonAdvancedOptions2.split(Headers.SEPERATOR, 2);
                    if (split.length >= 2 && split[0].length() > 0 && split[1].length() > 0) {
                        try {
                            adTargetingOptions.setAdvancedOption(split[0], split[1]);
                        } catch (Throwable e) {
                            Logging.log(Log.getStackTraceString(e));
                        }
                    }
                }
            }
        }
        if (map != null) {
            try {
                amazonAdvancedOptions2 = (String) map.get("AGE");
                if (amazonAdvancedOptions2 != null && amazonAdvancedOptions2.length() > 0) {
                    adTargetingOptions.setAge(Integer.parseInt(amazonAdvancedOptions2));
                }
            } catch (Throwable e2) {
                Logging.log(Log.getStackTraceString(e2));
            }
            if (adMarvelAd != null) {
                try {
                    AdRegistration.setAppKey(adMarvelAd.getPubId());
                } catch (Throwable e22) {
                    Logging.log(Log.getStackTraceString(e22));
                }
            }
        }
        if (adMarvelAd == null) {
            return null;
        }
        String adFormat = adMarvelAd.getAdFormat();
        View view = null;
        if (context instanceof Activity) {
            LayoutParams layoutParams;
            Activity activity = (Activity) context;
            i3 = AMQP.CONNECTION_FORCED;
            int i4 = 50;
            if (adFormat.equalsIgnoreCase("300x50")) {
                view = new AdLayout(activity, AdSize.SIZE_300x50);
                i3 = 300;
                i4 = 50;
            } else if (adFormat.equalsIgnoreCase("320x50")) {
                view = new AdLayout(activity, AdSize.SIZE_320x50);
                i3 = AMQP.CONNECTION_FORCED;
                i4 = 50;
            } else if (adFormat.equalsIgnoreCase("300x250")) {
                view = new AdLayout(activity, AdSize.SIZE_300x250);
                i3 = 300;
                i4 = Type.TSIG;
            } else if (adFormat.equalsIgnoreCase("600x90")) {
                view = new AdLayout(activity, AdSize.SIZE_600x90);
                i3 = SettingsJsonConstants.ANALYTICS_FLUSH_INTERVAL_SECS_DEFAULT;
                i4 = 90;
            } else if (adFormat.equalsIgnoreCase("728x90")) {
                view = new AdLayout(activity, AdSize.SIZE_728x90);
                i3 = 728;
                i4 = 90;
            } else if (adFormat.equalsIgnoreCase("320x50")) {
                view = new AdLayout(activity, AdSize.SIZE_320x50);
                i3 = AMQP.CONNECTION_FORCED;
                i4 = 50;
            } else if (adFormat.equalsIgnoreCase(StompSubscription.AUTO_ACK)) {
                view = new AdLayout(activity, AdSize.SIZE_AUTO);
                i3 = 0;
                i4 = 0;
            }
            adTargetingOptions.enableGeoLocation(adMarvelAd.isAmazonEnableGeoLocation());
            view.setListener(new InternalAmazonListener(adMarvelAdapterListener));
            if (i3 > 0) {
                float f = activity.getResources().getDisplayMetrics().density;
                layoutParams = new FrameLayout.LayoutParams((int) (((float) i3) * f), (int) (((float) i4) * f), 81);
            } else {
                layoutParams = new FrameLayout.LayoutParams(-1, -2, 81);
            }
            view.setLayoutParams(layoutParams);
            i4 = adMarvelAd.getAmazonAdTimeOut();
            if (i4 > 0) {
                view.setTimeout(i4);
            }
            view.loadAd(adTargetingOptions);
            return view;
        }
        if (adMarvelAdapterListener != null) {
            adMarvelAdapterListener.onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
        }
        return null;
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
    }
}
