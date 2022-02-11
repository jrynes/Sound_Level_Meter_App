package com.admarvel.android.ads;

import android.content.Context;
import android.location.Location;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.util.Decoder;
import com.admarvel.android.util.p000a.MethodBuilderFactory;
import com.admarvel.android.util.p000a.Reflection.Reflection;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;
import org.json.JSONException;
import org.json.JSONObject;

public class AdMarvelAd implements Serializable {
    private String f116A;
    private String f117B;
    private long f118C;
    private String f119D;
    private String f120E;
    private String f121F;
    private String f122G;
    private String f123H;
    private String f124I;
    private String f125J;
    private Boolean f126K;
    private String f127L;
    private int f128M;
    private String f129N;
    private int f130O;
    private boolean f131P;
    private String f132Q;
    private String f133R;
    private String f134S;
    private final String f135T;
    private final String f136U;
    private boolean f137V;
    private String f138W;
    private String f139X;
    private String f140Y;
    private String f141Z;
    private int f142a;
    private String aA;
    private String aB;
    private boolean aC;
    private Integer aD;
    private String aE;
    private final Map<String, Object> aF;
    private final String aG;
    private final String aH;
    private final String aI;
    private final int aJ;
    private final String aK;
    private Map<String, String> aL;
    private String aM;
    private boolean aN;
    private String aO;
    private String aa;
    private String ab;
    private String ac;
    private String ad;
    private String ae;
    private String af;
    private String ag;
    private String ah;
    private boolean ai;
    private int aj;
    private float ak;
    private boolean al;
    private boolean am;
    private AdMarvelEvent an;
    private String ao;
    private String ap;
    private String aq;
    private String ar;
    private RhythmVideoAdType as;
    private HeyzapAdType at;
    private boolean au;
    private boolean av;
    private String aw;
    private String ax;
    private String ay;
    private String az;
    private String f143b;
    private String f144c;
    private String f145d;
    private String f146e;
    private int f147f;
    private int f148g;
    private String f149h;
    private String f150i;
    private AdType f151j;
    private String f152k;
    private List<String> f153l;
    private int f154m;
    private String f155n;
    private String f156o;
    private SDKAdNetwork f157p;
    private String f158q;
    private String f159r;
    private String f160s;
    private boolean f161t;
    private String f162u;
    private String[] f163v;
    private String[] f164w;
    private String f165x;
    private String f166y;
    private String f167z;

    public enum AdType {
        TEXT,
        IMAGE,
        JAVASCRIPT,
        SDKCALL,
        ERROR,
        CUSTOM
    }

    public enum HeyzapAdType {
        VIDEO_AD,
        INTERSTITIAL_AD,
        INCENTIVIZED_AD
    }

    public enum RhythmVideoAdType {
        VIDEO_AD_ONLY,
        VIDEO_AD
    }

    public AdMarvelAd(String xml, Map<String, Object> targetParams, String partnerId, String siteId, String androidId, int orientation, String deviceConnectivity, String packageName) {
        this.f147f = -1;
        this.f148g = -1;
        this.f165x = null;
        this.f166y = null;
        this.f167z = null;
        this.f116A = null;
        this.f117B = null;
        this.f118C = 0;
        this.f119D = null;
        this.f120E = null;
        this.f137V = false;
        this.ad = null;
        this.ae = null;
        this.ak = GroundOverlayOptions.NO_DIMENSION;
        this.al = false;
        this.am = false;
        this.as = null;
        this.at = null;
        this.aD = Integer.valueOf(0);
        this.aN = false;
        this.f131P = false;
        this.f136U = xml;
        this.aF = targetParams;
        this.aG = partnerId;
        this.aH = siteId;
        this.aI = androidId;
        this.aJ = orientation;
        this.aK = deviceConnectivity;
        this.f135T = packageName;
        if (androidId != null && targetParams.get("UNIQUE_ID") == null) {
            targetParams.put("UNIQUE_ID", androidId);
        }
    }

    public void allowInteractionInExpandableAds() {
        this.am = true;
    }

    public String getAdColonyAppVersion() {
        return this.f117B;
    }

    public long getAdColonyDelayAfterInitInMs() {
        return this.f118C;
    }

    public String getAdColonyMuted() {
        return this.f141Z;
    }

    public String getAdColonyShowConfirmationDialog() {
        return this.ab;
    }

    public String getAdColonyShowResultDialog() {
        return this.af;
    }

    public String getAdColonyVolume() {
        return this.aa;
    }

    public String getAdFormat() {
        return this.f120E;
    }

    public int getAdHistoryCounter() {
        return this.aD.intValue();
    }

    public String getAdHistoryDumpString() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(Constants.APPNAME, this.f135T);
            jSONObject.put(Constants.PARTNERID, this.aG);
            if (this.aw != null) {
                jSONObject.put(Constants.AD_REQUEST, new JSONObject(this.aw));
            }
            if (this.ax != null) {
                jSONObject.put(Constants.AD_RESPONSE, new JSONObject(this.ax));
            }
            if (this.ay != null) {
                jSONObject.put(Constants.NATIVE_VIDEO_AD_HTML_ELEMENT, new JSONObject(this.ay));
            }
            this.az = jSONObject.toString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return this.az;
    }

    public String getAdId() {
        return this.f132Q;
    }

    public AdMarvelEvent getAdMarvelEvent() {
        return this.an;
    }

    public float getAdMarvelViewWidth() {
        return this.ak;
    }

    public String getAdMobExtras() {
        return this.f167z;
    }

    public AdType getAdType() {
        return this.f151j;
    }

    public String getAdmobAdFormat() {
        return this.aO;
    }

    public String getAdmobTestAction() {
        return this.aO;
    }

    public String[] getAdmobTestDeviceId() {
        return this.f163v;
    }

    public String getAmazonAdRequestAdvancedOptions() {
        return this.ah;
    }

    public int getAmazonAdTimeOut() {
        return this.aj;
    }

    public String getAmazonAdvancedOptions() {
        return this.ag;
    }

    public String getAndroidId() {
        return this.aI;
    }

    public String getAppId() {
        return this.aE;
    }

    public String getAppName() {
        return this.f134S;
    }

    public String getBannerid() {
        return this.f127L;
    }

    public String getChannelId() {
        return this.f159r;
    }

    public String getChartboostAppSignature() {
        return this.ac;
    }

    public String getClickURL() {
        return this.f150i;
    }

    public String getCloseFunction() {
        return this.f162u;
    }

    public String getCompanyName() {
        return this.f160s;
    }

    public String getCountdowntext() {
        return this.aA;
    }

    public String getCreativeType() {
        return this.f152k;
    }

    public String getDeviceConnectivity() {
        return this.aK;
    }

    public String getDisableAdDuration() {
        return this.f138W;
    }

    public int getErrorCode() {
        return this.f154m;
    }

    public String getErrorReason() {
        return this.f155n;
    }

    public String getExcluded() {
        return this.f129N;
    }

    public String getExpandDirection() {
        return this.f119D;
    }

    public String getFacebookAdSize() {
        return this.f166y;
    }

    public String getFacebookChildDirectedFlag() {
        return this.f165x;
    }

    public String[] getFacebookTestDeviceId() {
        return this.f164w;
    }

    public String getGooglePlayLocation() {
        return this.f116A;
    }

    public String getHeight() {
        return this.f121F;
    }

    public HeyzapAdType getHeyzapAdType() {
        return this.at;
    }

    public int getId() {
        return this.f142a;
    }

    public String getImageAlt() {
        return this.f149h;
    }

    public int getImageHeight() {
        return this.f148g;
    }

    public String getImageURL() {
        return this.f146e;
    }

    public int getImageWidth() {
        return this.f147f;
    }

    public String getInterstitialAction() {
        return this.f124I;
    }

    public String getIpAddress() {
        return this.f143b;
    }

    public String getKeywordsContentUrl() {
        return this.f125J;
    }

    public int getMaxretries() {
        return this.f130O;
    }

    public String getOfflineBaseUrl() {
        return this.ao;
    }

    public String getOfflinekeyUrl() {
        return this.ap;
    }

    public int getOrientation() {
        return this.aJ;
    }

    public String getPartnerId() {
        return this.aG;
    }

    public List<String> getPixels() {
        return this.f153l;
    }

    public String getPubId() {
        return this.f158q;
    }

    public Boolean getRetry() {
        return this.f126K;
    }

    public int getRetrynum() {
        return this.f128M;
    }

    public Map<String, String> getRewardParams() {
        return this.aL;
    }

    public RhythmVideoAdType getRhythmVideoAdType() {
        return this.as;
    }

    public String getRhythmVideoUrl() {
        return this.ar;
    }

    public String getScene() {
        return this.aq;
    }

    public SDKAdNetwork getSdkAdNetwork() {
        return this.f157p;
    }

    public String getSdkNetwork() {
        return this.f156o;
    }

    public String getSiteId() {
        return this.aH;
    }

    public String getSlotName() {
        return this.f133R;
    }

    public String getSource() {
        return this.f123H;
    }

    public Map<String, Object> getTargetParams() {
        return this.aF;
    }

    public String getTargetZoneId() {
        return this.f140Y;
    }

    public String getText() {
        return this.f145d;
    }

    public String getUserId() {
        return this.aM;
    }

    public String getVideoplacement() {
        return this.aB;
    }

    public String getWebViewRedirectUrl() {
        return this.ad;
    }

    public String getWebViewRedirectUrlProtocol() {
        return this.ae;
    }

    public String getWidth() {
        return this.f122G;
    }

    public String getXHTML() {
        return this.f144c;
    }

    public String getXhtml() {
        return this.f144c;
    }

    public String getXml() {
        return this.f136U;
    }

    public String getZoneId() {
        return this.f139X;
    }

    public boolean hasImage() {
        return this.f146e != null;
    }

    public boolean isAmazonEnableGeoLocation() {
        return this.ai;
    }

    public boolean isAppInteractionAllowedForExpandableAds() {
        return this.am;
    }

    public boolean isCachingEnabled() {
        return this.au;
    }

    public boolean isDisableAdrequest() {
        return this.f137V;
    }

    public boolean isHoverAd() {
        return this.al;
    }

    public boolean isMustBeVisible() {
        return this.f131P;
    }

    public boolean isRewardInterstitial() {
        return this.aN;
    }

    public boolean isTest() {
        return this.f161t;
    }

    public boolean isTimercountdownEnabled() {
        return this.aC;
    }

    public boolean isTrackingIdSet() {
        return this.av;
    }

    public AdMarvelXMLReader loadAd(Context context) {
        if (this.f136U == null) {
            return null;
        }
        AdMarvelXMLReader adMarvelXMLReader = new AdMarvelXMLReader();
        adMarvelXMLReader.parseXMLString(this.f136U);
        AdMarvelXMLElement parsedXMLData = adMarvelXMLReader.getParsedXMLData();
        if (parsedXMLData == null) {
            return null;
        }
        String str;
        AdMarvelXMLElement adMarvelXMLElement;
        AdMarvelXMLElement adMarvelXMLElement2;
        if (parsedXMLData.getName().equals(Constants.NATIVE_AD_ELEMENT)) {
            str = (String) parsedXMLData.getAttributes().get(Name.MARK);
            if (str != null && str.length() > 0) {
                this.f142a = Integer.parseInt(str);
                this.f127L = str;
            }
            str = (String) parsedXMLData.getAttributes().get("ip");
            if (str != null && str.length() > 0) {
                this.f143b = str;
            }
            str = (String) parsedXMLData.getAttributes().get("dah");
            if (str != null && str.length() > 0 && str.equalsIgnoreCase(Stomp.TRUE)) {
                AdMarvelUtils.disableLogDump();
            }
            str = (String) parsedXMLData.getAttributes().get(Send.TYPE);
            if ("text".equals(str)) {
                this.f151j = AdType.TEXT;
            } else if (Constants.NATIVE_AD_IMAGE_ELEMENT.equals(str)) {
                this.f151j = AdType.IMAGE;
            } else if ("javascript".equals(str)) {
                this.f151j = AdType.JAVASCRIPT;
            } else if (Diagnostics.error.equals(str)) {
                this.f151j = AdType.ERROR;
            } else if ("sdkcall".equals(str)) {
                this.f151j = AdType.SDKCALL;
            } else if ("custom".equals(str)) {
                this.f151j = AdType.CUSTOM;
            }
            str = (String) parsedXMLData.getAttributes().get(locationTracking.source);
            if (str != null && str.length() > 0) {
                this.f123H = str;
            }
            str = (String) parsedXMLData.getAttributes().get("ave");
            if (str != null && str.length() > 0) {
                try {
                    AdMarvelAnalyticsAdapter instance = AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context);
                    if (str.equals("1")) {
                        instance.enableAppInstallCheck(true);
                    } else if (str.equals("0")) {
                        instance.enableAppInstallCheck(false);
                    }
                } catch (Exception e) {
                }
            }
            str = (String) parsedXMLData.getAttributes().get("iha");
            if (str != null && str.length() > 0 && str.equals("1")) {
                setAsHoverAd();
            }
            str = (String) parsedXMLData.getAttributes().get("aie");
            if (str != null && str.length() > 0 && str.equals("1")) {
                allowInteractionInExpandableAds();
            }
            str = (String) parsedXMLData.getAttributes().get("dip");
            if (str != null && str.length() > 0 && str.equals("1")) {
                AdMarvelUtils.setInterstitialProgressBarDisabled(true);
            }
            str = (String) parsedXMLData.getAttributes().get("ece");
            if (str != null && str.length() > 0) {
                if (str.equalsIgnoreCase("1")) {
                    AdMarvelUtils.enableCustomExpand(true);
                } else if (str.equalsIgnoreCase("0")) {
                    AdMarvelUtils.enableCustomExpand(false);
                }
            }
        } else {
            this.f151j = AdType.ERROR;
        }
        if (parsedXMLData.getChildren().containsKey("eventTrackers")) {
            this.an = new AdMarvelEvent((AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get("eventTrackers")).get(0));
        }
        if (this.aN && this.an == null) {
            this.f151j = AdType.ERROR;
            this.f154m = 303;
        }
        if (parsedXMLData.getChildren().containsKey("pixels")) {
            adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get("pixels")).get(0);
            if (adMarvelXMLElement.getChildren().containsKey("pixel")) {
                int size = ((ArrayList) adMarvelXMLElement.getChildren().get("pixel")).size();
                for (int i = 0; i < size; i++) {
                    adMarvelXMLElement2 = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement.getChildren().get("pixel")).get(i);
                    if (adMarvelXMLElement2 != null) {
                        Object data = adMarvelXMLElement2.getData();
                        if (AdMarvelView.enableOfflineSDK || AdMarvelInterstitialAds.enableOfflineSDK) {
                            data = data.replaceAll("\\{siteid\\}", getSiteId()).replaceAll("\\{random\\}", String.valueOf(System.currentTimeMillis())).replaceAll("\\{uniqueid\\}", getAndroidId());
                        }
                        if (this.f153l == null) {
                            this.f153l = new ArrayList();
                        }
                        this.f153l.add(data);
                    }
                }
            }
        }
        if ((AdMarvelView.enableOfflineSDK || AdMarvelInterstitialAds.enableOfflineSDK) && parsedXMLData.getChildren().containsKey("file")) {
            String data2 = ((AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get("file")).get(0)).getData();
            if (this.f151j.equals(AdType.JAVASCRIPT)) {
                try {
                    Reflection a = MethodBuilderFactory.m529a(Class.forName("com.admarvel.android.offlinesdk.AdmarvelOfflineUtils").newInstance(), "readData");
                    a.m539a(String.class, this.ap);
                    a.m539a(String.class, data2);
                    this.f144c = (String) a.m540a();
                } catch (InstantiationException e2) {
                    e2.printStackTrace();
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                } catch (ClassNotFoundException e4) {
                    e4.printStackTrace();
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
                if (this.f144c != null) {
                    this.f144c = this.f144c.replaceAll("\\{siteid\\}", getSiteId());
                }
            }
            this.ap += ReadOnlyContext.SEPARATOR + data2;
        }
        if (this.f151j.equals(AdType.SDKCALL) && parsedXMLData.getChildren().containsKey("xhtml")) {
            adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) parsedXMLData.getChildren().get("xhtml")).get(0);
            if (adMarvelXMLElement != null) {
                adMarvelXMLReader.parseXMLString(new Decoder().m604a(adMarvelXMLElement.getData()));
                adMarvelXMLElement2 = adMarvelXMLReader.getParsedXMLData();
                str = (String) adMarvelXMLElement2.getAttributes().get("network");
                if ("googleplay".equals(str) || "admob".equals(str)) {
                    this.f156o = Constants.GOOGLEPLAY_SDK_ADAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.GOOGLEPLAY;
                } else if ("rhythm".equals(str)) {
                    this.f156o = Constants.RHYTHM_SDK_ADAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.RHYTHM;
                } else if ("millennial".equals(str)) {
                    this.f156o = Constants.MILLENNIAL_SDK_APAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.MILLENNIAL;
                } else if ("amazon".equals(str)) {
                    this.f156o = Constants.AMAZON_SDK_APAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.AMAZON;
                } else if ("adcolony".equals(str)) {
                    this.f156o = Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.ADCOLONY;
                    if (this.aN && this.an != null) {
                        AdMarvelRewardQueueHandler.m336a().m338a(this.f157p, this.an);
                    }
                } else if ("facebook".equals(str)) {
                    this.f156o = Constants.FACEBOOK_SDK_APAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.FACEBOOK;
                } else if ("inmobi".equals(str)) {
                    this.f156o = Constants.INMOBI_SDK_APAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.INMOBI;
                    if (this.aN && this.an != null) {
                        AdMarvelRewardQueueHandler.m336a().m338a(this.f157p, this.an);
                    }
                } else if ("heyzap".equals(str)) {
                    this.f156o = Constants.HEYZAP_SDK_APAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.HEYZAP;
                } else if ("unityads".equals(str)) {
                    this.f156o = Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.UNITYADS;
                    if (this.aN && this.an != null) {
                        AdMarvelRewardQueueHandler.m336a().m338a(this.f157p, this.an);
                    }
                } else if ("chartboost".equals(str)) {
                    this.f156o = Constants.CHARTBOOST_SDK_ADAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.CHARTBOOST;
                    if (this.aN && this.an != null) {
                        AdMarvelRewardQueueHandler.m336a().m338a(this.f157p, this.an);
                    }
                } else if ("vungle".equals(str)) {
                    this.f156o = Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.VUNGLE;
                    if (this.aN && this.an != null) {
                        AdMarvelRewardQueueHandler.m336a().m338a(this.f157p, this.an);
                    }
                } else if ("yume".equals(str)) {
                    this.f156o = Constants.YUME_SDK_ADAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.YUME;
                } else if ("verve".equals(str)) {
                    this.f156o = Constants.VERVE_SDK_ADAPTER_FULL_CLASSNAME;
                    this.f157p = SDKAdNetwork.VERVE;
                } else if ("disable_ad_request".equals(str)) {
                    str = (String) adMarvelXMLElement2.getAttributes().get("durationinseconds");
                    if (str != null) {
                        this.f137V = true;
                        this.f138W = str;
                    }
                } else {
                    this.f151j = AdType.ERROR;
                    this.f154m = 307;
                    this.f155n = "Missing SDK ad network";
                }
                if ("YES".equals((String) adMarvelXMLElement2.getAttributes().get("retry"))) {
                    this.f126K = Boolean.valueOf(true);
                } else {
                    this.f126K = Boolean.valueOf(false);
                }
                str = (String) adMarvelXMLElement2.getAttributes().get("bannerid");
                if (str != null && str.length() > 0) {
                    this.f127L = str;
                }
                str = (String) adMarvelXMLElement2.getAttributes().get("retrynum");
                if (str != null && str.length() > 0) {
                    this.f128M = Integer.parseInt(str);
                }
                str = (String) adMarvelXMLElement2.getAttributes().get("excluded");
                if (str != null && str.length() > 0) {
                    this.f129N = str;
                }
                str = (String) adMarvelXMLElement2.getAttributes().get("maxretries");
                if (str == null || str.length() <= 0) {
                    this.f130O = 1;
                } else {
                    this.f130O = Integer.parseInt(str);
                }
                if (this.f151j.equals(AdType.JAVASCRIPT) && adMarvelXMLElement2.getChildren().containsKey("customdata")) {
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("customdata")).get(0);
                    if (adMarvelXMLElement.getChildren().containsKey("close_func")) {
                        this.f162u = ((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement.getChildren().get("close_func")).get(0)).getData();
                    }
                }
                if (adMarvelXMLElement2.getChildren().containsKey("errorCode")) {
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("errorCode")).get(0);
                    if (adMarvelXMLElement != null) {
                        this.f154m = Integer.parseInt(adMarvelXMLElement.getData());
                    }
                }
                if (adMarvelXMLElement2.getChildren().containsKey("errorReason")) {
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("errorReason")).get(0);
                    if (adMarvelXMLElement != null) {
                        this.f155n = adMarvelXMLElement.getData();
                    }
                }
                if (adMarvelXMLElement2.getChildren().containsKey("xhtml")) {
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("xhtml")).get(0);
                    if (adMarvelXMLElement != null) {
                        this.f144c = adMarvelXMLElement.getData();
                    }
                }
                if (adMarvelXMLElement2.getChildren().containsKey("clickurl")) {
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("clickurl")).get(0);
                    if (adMarvelXMLElement != null) {
                        this.f150i = adMarvelXMLElement.getData();
                        if ((AdMarvelView.enableOfflineSDK || AdMarvelInterstitialAds.enableOfflineSDK) && this.f150i != null) {
                            this.f150i = this.f150i.replaceAll("\\{siteid\\}", getSiteId());
                        }
                    }
                }
                if (adMarvelXMLElement2.getChildren().containsKey(Constants.NATIVE_AD_IMAGE_ELEMENT)) {
                    if (((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().containsKey(SettingsJsonConstants.APP_URL_KEY)) {
                        adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) ((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().get(SettingsJsonConstants.APP_URL_KEY)).get(0);
                        if (adMarvelXMLElement != null) {
                            this.f146e = adMarvelXMLElement.getData();
                        }
                    }
                    if (((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().containsKey("alt")) {
                        adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) ((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().get("alt")).get(0);
                        if (adMarvelXMLElement != null) {
                            this.f149h = adMarvelXMLElement.getData();
                        }
                    }
                    if (((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().containsKey(SettingsJsonConstants.ICON_WIDTH_KEY)) {
                        adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) ((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().get(SettingsJsonConstants.ICON_WIDTH_KEY)).get(0);
                        if (adMarvelXMLElement != null) {
                            this.f147f = Integer.parseInt(adMarvelXMLElement.getData());
                        }
                    }
                    if (((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().containsKey(SettingsJsonConstants.ICON_HEIGHT_KEY)) {
                        adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) ((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().get(SettingsJsonConstants.ICON_HEIGHT_KEY)).get(0);
                        if (adMarvelXMLElement != null) {
                            this.f148g = Integer.parseInt(adMarvelXMLElement.getData());
                        }
                    }
                }
                if (adMarvelXMLElement2.getChildren().containsKey("text")) {
                    adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("text")).get(0);
                    if (adMarvelXMLElement != null) {
                        this.f145d = adMarvelXMLElement.getData();
                    }
                }
                return adMarvelXMLReader;
            }
        }
        adMarvelXMLElement2 = parsedXMLData;
        adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("customdata")).get(0);
        if (adMarvelXMLElement.getChildren().containsKey("close_func")) {
            this.f162u = ((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement.getChildren().get("close_func")).get(0)).getData();
        }
        if (adMarvelXMLElement2.getChildren().containsKey("errorCode")) {
            adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("errorCode")).get(0);
            if (adMarvelXMLElement != null) {
                this.f154m = Integer.parseInt(adMarvelXMLElement.getData());
            }
        }
        if (adMarvelXMLElement2.getChildren().containsKey("errorReason")) {
            adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("errorReason")).get(0);
            if (adMarvelXMLElement != null) {
                this.f155n = adMarvelXMLElement.getData();
            }
        }
        if (adMarvelXMLElement2.getChildren().containsKey("xhtml")) {
            adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("xhtml")).get(0);
            if (adMarvelXMLElement != null) {
                this.f144c = adMarvelXMLElement.getData();
            }
        }
        if (adMarvelXMLElement2.getChildren().containsKey("clickurl")) {
            adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("clickurl")).get(0);
            if (adMarvelXMLElement != null) {
                this.f150i = adMarvelXMLElement.getData();
                this.f150i = this.f150i.replaceAll("\\{siteid\\}", getSiteId());
            }
        }
        if (adMarvelXMLElement2.getChildren().containsKey(Constants.NATIVE_AD_IMAGE_ELEMENT)) {
            if (((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().containsKey(SettingsJsonConstants.APP_URL_KEY)) {
                adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) ((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().get(SettingsJsonConstants.APP_URL_KEY)).get(0);
                if (adMarvelXMLElement != null) {
                    this.f146e = adMarvelXMLElement.getData();
                }
            }
            if (((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().containsKey("alt")) {
                adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) ((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().get("alt")).get(0);
                if (adMarvelXMLElement != null) {
                    this.f149h = adMarvelXMLElement.getData();
                }
            }
            if (((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().containsKey(SettingsJsonConstants.ICON_WIDTH_KEY)) {
                adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) ((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().get(SettingsJsonConstants.ICON_WIDTH_KEY)).get(0);
                if (adMarvelXMLElement != null) {
                    this.f147f = Integer.parseInt(adMarvelXMLElement.getData());
                }
            }
            if (((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().containsKey(SettingsJsonConstants.ICON_HEIGHT_KEY)) {
                adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) ((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(Constants.NATIVE_AD_IMAGE_ELEMENT)).get(0)).getChildren().get(SettingsJsonConstants.ICON_HEIGHT_KEY)).get(0);
                if (adMarvelXMLElement != null) {
                    this.f148g = Integer.parseInt(adMarvelXMLElement.getData());
                }
            }
        }
        if (adMarvelXMLElement2.getChildren().containsKey("text")) {
            adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get("text")).get(0);
            if (adMarvelXMLElement != null) {
                this.f145d = adMarvelXMLElement.getData();
            }
        }
        return adMarvelXMLReader;
    }

    public synchronized void removeNonStringEntriesTargetParam() {
        Map concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.putAll(this.aF);
        try {
            Map concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.putAll(this.aF);
            for (Entry entry : concurrentHashMap2.entrySet()) {
                if (!(entry.getValue() instanceof String)) {
                    if ((entry.getValue() instanceof Location) && ((String) entry.getKey()).equals("LOCATION_OBJECT")) {
                        Location location = (Location) entry.getValue();
                        concurrentHashMap2.put("GEOLOCATION", String.format("%f,%f", new Object[]{Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude())}));
                        concurrentHashMap2.remove(entry.getKey());
                    } else {
                        concurrentHashMap2.remove(entry.getKey());
                    }
                }
            }
            this.aF.clear();
            this.aF.putAll(concurrentHashMap2);
        } catch (Exception e) {
            this.aF.clear();
            this.aF.putAll(concurrentHashMap);
            e.printStackTrace();
        }
    }

    public void setAdColonyAppVersion(String appVersion) {
        this.f117B = appVersion;
    }

    public void setAdColonyDelayAfterInitInMs(long adColonyDelayAfterInitInMs) {
        this.f118C = adColonyDelayAfterInitInMs;
    }

    public void setAdColonyMuted() {
        this.f141Z = Stomp.TRUE;
    }

    public void setAdColonyShowConfirmationDialog(String adColonyShowConfirmationDialog) {
        this.ab = adColonyShowConfirmationDialog;
    }

    public void setAdColonyShowResultDialog(String adColonyShowResultDialog) {
        this.af = adColonyShowResultDialog;
    }

    public void setAdColonyVolume(String volume) {
        this.aa = volume;
    }

    public void setAdFormat(String adFormat) {
        this.f120E = adFormat;
    }

    public void setAdHistoryCounter(int counter) {
        this.aD = Integer.valueOf(counter);
    }

    public void setAdId(String adId) {
        this.f132Q = adId;
    }

    public void setAdMarvelEvent(AdMarvelEvent adMarvelEvent) {
        this.an = adMarvelEvent;
    }

    public void setAdMarvelViewWidth(float adMarvelViewWidth) {
        this.ak = adMarvelViewWidth;
    }

    public void setAdMobExtras(String extras) {
        this.f167z = extras;
    }

    public void setAdType(AdType adType) {
        this.f151j = adType;
    }

    public void setAdmobTestAction(String admobTestAction) {
        this.aO = admobTestAction;
    }

    public void setAdmobTestDeviceId(String[] admobTestDeviceId) {
        this.f163v = admobTestDeviceId;
    }

    public void setAmazonAdRequestAdvancedOptions(String amazonAdRequestAdvancedOptions) {
        this.ah = amazonAdRequestAdvancedOptions;
    }

    public void setAmazonAdTimeOut(int amazonAdTimeOut) {
        this.aj = amazonAdTimeOut;
    }

    public void setAmazonAdvancedOptions(String amazonAdvancedOptions) {
        this.ag = amazonAdvancedOptions;
    }

    public void setAmazonEnableGeoLocation(boolean amazonEnableGeoLocation) {
        this.ai = amazonEnableGeoLocation;
    }

    public void setAppId(String appId) {
        this.aE = appId;
    }

    public void setAppName(String appName) {
        this.f134S = appName;
    }

    public void setAsHoverAd() {
        this.al = true;
    }

    public void setBannerid(String bannerid) {
        this.f127L = bannerid;
    }

    public void setCachingEnabled(boolean cachingEnabled) {
        this.au = cachingEnabled;
    }

    public void setChannelId(String channelId) {
        this.f159r = channelId;
    }

    public void setChartboostAppSignature(String chartboostAppSignature) {
        this.ac = chartboostAppSignature;
    }

    public void setClickURL(String clickURL) {
        this.f150i = clickURL;
    }

    public void setCloseFunction(String closeFunction) {
        this.f162u = closeFunction;
    }

    public void setCompanyName(String companyName) {
        this.f160s = companyName;
    }

    public void setCountdowntext(String countdowntext) {
        this.aA = countdowntext;
    }

    public void setCreativeType(String creativeType) {
        this.f152k = creativeType;
    }

    public void setErrorCode(int errorCode) {
        this.f154m = errorCode;
    }

    public void setErrorReason(String errorReason) {
        this.f155n = errorReason;
    }

    public void setExcluded(String excluded) {
        this.f129N = excluded;
    }

    public void setExpandDirection(String expandDirection) {
        this.f119D = expandDirection;
    }

    public void setFacebookAdSize(String adSize) {
        this.f166y = adSize;
    }

    public void setFacebookChildDirectedFlag(String childDirected) {
        this.f165x = childDirected;
    }

    public void setFacebookTestDeviceId(String[] facebookTestDeviceId) {
        this.f164w = facebookTestDeviceId;
    }

    public void setGooglePlayLocation(String location) {
        this.f116A = location;
    }

    public void setHeight(String height) {
        this.f121F = height;
    }

    public void setHeyzapAdType(HeyzapAdType heyzapAdType) {
        this.at = heyzapAdType;
    }

    public void setHtmlJson(String html) {
        try {
            JSONObject jSONObject = new JSONObject();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = Calendar.getInstance().getTime();
            Long valueOf = Long.valueOf(System.currentTimeMillis());
            jSONObject.put(MPDbAdapter.KEY_DATA, html);
            jSONObject.put(Message.TIMESTAMP, String.valueOf(valueOf));
            jSONObject.put(Constants.UTC, simpleDateFormat.format(time));
            jSONObject.put(Constants.LOCAL, simpleDateFormat2.format(time));
            this.ay = jSONObject.toString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setId(int id) {
        this.f142a = id;
    }

    public void setImageAlt(String imageAlt) {
        this.f149h = imageAlt;
    }

    public void setImageHeight(int imageHeight) {
        this.f148g = imageHeight;
    }

    public void setImageURL(String imageURL) {
        this.f146e = imageURL;
    }

    public void setImageWidth(int imageWidth) {
        this.f147f = imageWidth;
    }

    public void setInterstitialAction(String interstitialAction) {
        this.f124I = interstitialAction;
    }

    public void setIpAddress(String ipAddress) {
        this.f143b = ipAddress;
    }

    public void setKeywordsContentUrl(String keywordsContentUrl) {
        this.f125J = keywordsContentUrl;
    }

    public void setMustBeVisible(boolean mustBeVisible) {
        this.f131P = mustBeVisible;
    }

    public void setOfflineBaseUrl(String offlineBaseUrl) {
        this.ao = offlineBaseUrl;
    }

    public void setOfflinekeyUrl(String offlinekeyUrl) {
        this.ap = offlinekeyUrl;
    }

    public void setPixels(List<String> pixels) {
        this.f153l = pixels;
    }

    public void setPubId(String pubId) {
        this.f158q = pubId;
    }

    public void setRequestJson(JSONObject requestJson) {
        if (requestJson != null) {
            try {
                this.aw = requestJson.toString(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setResponseJson() {
        try {
            JSONObject jSONObject = new JSONObject();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = Calendar.getInstance().getTime();
            Long valueOf = Long.valueOf(System.currentTimeMillis());
            jSONObject.put(MPDbAdapter.KEY_DATA, this.f136U);
            jSONObject.put(Message.TIMESTAMP, String.valueOf(valueOf));
            jSONObject.put(Constants.UTC, simpleDateFormat.format(time));
            jSONObject.put(Constants.LOCAL, simpleDateFormat2.format(time));
            this.ax = jSONObject.toString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRetry(Boolean retry) {
        this.f126K = retry;
    }

    public void setRetrynum(int retrynum) {
        this.f128M = retrynum;
    }

    void setRewardInterstitial(boolean isRewardInterstitial) {
        this.aN = isRewardInterstitial;
    }

    void setRewardParams(Map<String, String> rewardParams) {
        this.aL = rewardParams;
    }

    public void setRhythmVideoAdType(RhythmVideoAdType rhythmVideoAdType) {
        this.as = rhythmVideoAdType;
    }

    public void setRhythmVideoUrl(String rhythmVideoUrl) {
        this.ar = rhythmVideoUrl;
    }

    public void setScene(String scene) {
        this.aq = scene;
    }

    public void setSdkNetwork(String sdkNetwork) {
        this.f156o = sdkNetwork;
    }

    public void setSetTrackingId(boolean setTrackingId) {
        this.av = setTrackingId;
    }

    public void setSlotName(String slotName) {
        this.f133R = slotName;
    }

    public void setSource(String source) {
        this.f123H = source;
    }

    public void setTargetZoneId(String zoneId) {
        this.f140Y = zoneId;
    }

    public void setTest(boolean test) {
        this.f161t = test;
    }

    public void setText(String text) {
        this.f145d = text;
    }

    public void setTimercountdown(boolean timercountdown) {
        this.aC = timercountdown;
    }

    void setUserId(String userId) {
        this.aM = userId;
    }

    public void setVideoplacement(String videoplacement) {
        this.aB = videoplacement;
    }

    public void setWebViewRedirectUrl(String webViewRedirectUrl) {
        this.ad = webViewRedirectUrl;
    }

    public void setWebViewRedirectUrlProtocol(String webViewRedirectUrlProtocol) {
        this.ae = webViewRedirectUrlProtocol;
    }

    public void setWidth(String width) {
        this.f122G = width;
    }

    public void setXhtml(String xhtml) {
        this.f144c = xhtml;
    }

    public void setZoneId(String zoneId) {
        this.f139X = zoneId;
    }
}
