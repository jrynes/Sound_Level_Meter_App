package com.onelouder.adlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.admarvel.android.ads.AdMarvelActivity;
import com.admarvel.android.ads.AdMarvelAd;
import com.admarvel.android.ads.AdMarvelInterstitialAds;
import com.admarvel.android.ads.AdMarvelInterstitialAds.AdMarvelInterstitialAdListener;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.ads.AdMarvelVideoActivity;
import com.admarvel.android.ads.AdMarvelView;
import com.admarvel.android.ads.AdMarvelView.AdMarvelViewListener;
import com.admarvel.android.ads.Constants;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.rabbitmq.client.impl.AMQImpl.Basic.Nack;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.xml.sax.Attributes;

public class ProxyAdMarvelView extends BaseAdProxy {
    private static boolean initialized;
    private static int instanceId;
    private String _tag;
    private AdMarvelActivity adMarvelActivity;
    private AdMarvelInterstitialAds adMarvelInterstitialAds;
    private AdMarvelVideoActivity adMarvelVideoActivity;
    private long interstitial_displayed;
    private int mAdHeight;
    private AdMarvelViewListener mAdListener;
    private AdMarvelAd mAdMarvelAd;
    private AdMarvelView mAdView;
    AdMarvelInterstitialAdListener mInterstitialListener;
    private String mPublisherid;
    private SDKAdNetwork mSdkAdNetwork;

    /* renamed from: com.onelouder.adlib.ProxyAdMarvelView.1 */
    static class C13011 implements Runnable {
        final /* synthetic */ Context val$context;

        C13011(Context context) {
            this.val$context = context;
        }

        public void run() {
            ProxyAdMarvelView.initializeAdcolonyMediation(this.val$context);
        }
    }

    /* renamed from: com.onelouder.adlib.ProxyAdMarvelView.2 */
    class C13022 implements AdMarvelViewListener {
        C13022() {
        }

        public void onRequestAd(AdMarvelView v) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "onRequestAd - admarvel");
        }

        public void onReceiveAd(AdMarvelView v) {
            if (ProxyAdMarvelView.this.mAdView != null) {
                Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "onReceiveAd - admarvel");
                ProxyAdMarvelView.this.mAdPlacement.sendBroadcast(ProxyAdMarvelView.this.getContext(), AdPlacement.ACTION_1L_ADVIEW_RECEIVED, null);
                if (!ProxyAdMarvelView.this.mAdPlacement.ispaused()) {
                    ProxyAdMarvelView.this.mAdPlacement.setTimestamp(System.currentTimeMillis());
                    ProxyAdMarvelView.this.mAdViewParent.resumeAdView(true);
                    return;
                } else if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.m1952e(ProxyAdMarvelView.this.TAG(), "isPaused=true, ignoring ad");
                    return;
                } else {
                    return;
                }
            }
            Diagnostics.m1957w(ProxyAdMarvelView.this.TAG(), "ad was null");
        }

        public void onFailedToReceiveAd(AdMarvelView v, int arg1, ErrorReason arg2) {
            Diagnostics.m1957w(ProxyAdMarvelView.this.TAG(), "onFailedToReceiveAd - admarvel");
            String errormessage = null;
            if (arg2 != null) {
                errormessage = arg2.toString();
            }
            ProxyAdMarvelView.this.onAdRequestFailed(arg1, errormessage);
        }

        public void onClickAd(AdMarvelView v, String url) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "onClickAd - admarvel, url=" + url);
            if (ProxyAdMarvelView.this.mAdViewParent == null) {
                Diagnostics.m1952e(ProxyAdMarvelView.this.TAG(), "onClickAd - mAdViewParent == null");
            } else if (url != null && url.startsWith("clktoact")) {
                context = v.getContext();
                HashMap<String, Object> params = new HashMap();
                params.put(SettingsJsonConstants.APP_URL_KEY, url);
                SendAdUsage.trackEvent(context, ProxyAdMarvelView.this.mAdPlacement, "clktoact", null, url);
                ProxyAdMarvelView.this.mAdPlacement.sendBroadcast(ProxyAdMarvelView.this.getContext(), AdPlacement.ACTION_1L_ADVIEW_CLKTOACT, params);
            } else if (ProxyAdMarvelView.this.mAdViewParent.wasTouched()) {
                ProxyAdMarvelView.this.mAdViewParent.resetTouch();
                if (ProxyAdMarvelView.this.mAdViewParent.onAdViewClicked(url)) {
                    Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "user handled click event.");
                } else if (url != null && !ProxyAdMarvelView.this.mAdPlacement.doClickRedirect()) {
                    try {
                        context = v.getContext();
                        boolean isVideo = false;
                        int idxQuestionmark = url.indexOf("?");
                        if (idxQuestionmark != -1) {
                            String temp = url.substring(0, idxQuestionmark);
                            if (temp.endsWith(".mp4") || temp.endsWith(".m3u8")) {
                                isVideo = true;
                            }
                        }
                        Intent intent;
                        if (isVideo) {
                            intent = new Intent("android.intent.action.VIEW");
                            intent.setFlags(268435456);
                            if (Build.MANUFACTURER.equals("HTC")) {
                                intent.setDataAndType(Uri.parse(url), WebRequest.CONTENT_TYPE_HTML);
                            } else {
                                intent.setDataAndType(Uri.parse(url), "video/mp4");
                            }
                            if (intent.resolveActivity(context.getPackageManager()) != null) {
                                context.startActivity(intent);
                                return;
                            }
                            return;
                        }
                        intent = new Intent(context, AdActivity.class);
                        intent.setFlags(268435456);
                        intent.putExtra(SettingsJsonConstants.APP_URL_KEY, url);
                        context.startActivity(intent);
                    } catch (Throwable e) {
                        Diagnostics.m1953e(ProxyAdMarvelView.this.TAG(), e);
                    }
                }
            } else {
                Diagnostics.m1957w(ProxyAdMarvelView.this.TAG(), "onClickAd - admarvel called without touch event");
            }
        }

        public void onExpand(AdMarvelView ad) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "admarvel.onExpand");
            ProxyAdMarvelView.this.expanded = true;
            ProxyAdMarvelView.this.mAdViewParent.pause();
        }

        public void onClose(AdMarvelView ad) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "admarvel.onClose");
            ProxyAdMarvelView.this.expanded = false;
            Context c = ProxyAdMarvelView.this.getContext();
            if ((c instanceof Activity) && !((Activity) c).isFinishing() && ProxyAdMarvelView.this.mAdViewParent.getWindowToken() != null) {
                ProxyAdMarvelView.this.mAdViewParent.resume();
            }
        }
    }

    /* renamed from: com.onelouder.adlib.ProxyAdMarvelView.3 */
    class C13033 implements AdMarvelInterstitialAdListener {
        C13033() {
        }

        public void onAdMarvelVideoActivityLaunched(AdMarvelVideoActivity a, AdMarvelInterstitialAds paramAdMarvelInterstitialAds) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "admarvel.onAdmarvelVideoActivityLaunched");
            ProxyAdMarvelView.this.adMarvelVideoActivity = a;
        }

        public void onAdmarvelActivityLaunched(AdMarvelActivity a, AdMarvelInterstitialAds paramAdMarvelInterstitialAds) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "admarvel.onAdmarvelActivityLaunched");
            ProxyAdMarvelView.this.adMarvelActivity = a;
        }

        public void onClickInterstitialAd(String clickUrl, AdMarvelInterstitialAds paramAdMarvelInterstitialAds) {
            HashMap<String, Object> params = null;
            if (clickUrl != null) {
                try {
                    if (clickUrl.length() > 0) {
                        HashMap<String, Object> params2 = new HashMap();
                        try {
                            params2.put(AdPlacement.EXTRA_1L_URL_CLICKED, clickUrl);
                            params = params2;
                        } catch (Exception e) {
                            Throwable e2 = e;
                            params = params2;
                            Diagnostics.m1953e(ProxyAdMarvelView.this.TAG(), e2);
                            return;
                        }
                    }
                } catch (Exception e3) {
                    e2 = e3;
                }
            }
            ProxyAdMarvelView.this.mAdPlacement.sendBroadcast(ProxyAdMarvelView.this.getContext(), AdPlacement.ACTION_1L_INTERSTITIAL_CLICKED, params);
            SendAdUsage.trackEvent(ProxyAdMarvelView.this.getContext(), ProxyAdMarvelView.this.mAdPlacement, Promotion.ACTION_CLICK, null, clickUrl);
        }

        public void onCloseInterstitialAd(AdMarvelInterstitialAds paramAdMarvelInterstitialAds) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "admarvel.onCloseInterstitialAd");
            try {
                if (ProxyAdMarvelView.this.mAdPlacement.areStringsDefined()) {
                    PlacementManager.getInstance().pauseFromClose(ProxyAdMarvelView.this.getContext(), ProxyAdMarvelView.this.mAdPlacement.getReset(), ProxyAdMarvelView.this.mAdPlacement.getPauseDuration());
                }
                long interstitial_ended = System.currentTimeMillis();
                HashMap<String, Object> params = new HashMap();
                params.put(AdPlacement.EXTRA_1L_OPEN_DURATION, Integer.valueOf((int) (interstitial_ended - ProxyAdMarvelView.this.interstitial_displayed)));
                ProxyAdMarvelView.this.mAdPlacement.onInterstitialClosed(ProxyAdMarvelView.this.getContext(), params);
                if (ProxyAdMarvelView.this.adMarvelActivity != null) {
                    Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "calling adMarvelActivity.finish()");
                    ProxyAdMarvelView.this.adMarvelActivity.finish();
                    ProxyAdMarvelView.this.adMarvelActivity = null;
                } else if (ProxyAdMarvelView.this.adMarvelVideoActivity != null) {
                    Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "calling adMarvelVideoActivity.finish()");
                    ProxyAdMarvelView.this.adMarvelVideoActivity.finish();
                    ProxyAdMarvelView.this.adMarvelVideoActivity = null;
                } else {
                    Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "adMarvelActivity==null && adMarvelVideoActivity==null");
                }
            } catch (Throwable e) {
                Diagnostics.m1953e(ProxyAdMarvelView.this.TAG(), e);
            }
        }

        public void onFailedToReceiveInterstitialAd(SDKAdNetwork sdkAdNetwork, AdMarvelInterstitialAds paramAdMarvelInterstitialAds, int errorCode, ErrorReason errorReason) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "admarvel.onFailedToReceiveInterstitialAd; errorCode: " + errorCode + " errorReason: " + errorReason.toString());
            try {
                HashMap<String, Object> params = new HashMap();
                if (errorReason != null) {
                    params.put(AdPlacement.EXTRA_1L_ERROR_MESSAGE, errorReason.toString());
                }
                params.put(AdPlacement.EXTRA_1L_ERROR_CODE, Integer.toString(errorCode));
                ProxyAdMarvelView.this.mAdPlacement.sendBroadcast(ProxyAdMarvelView.this.getContext(), AdPlacement.ACTION_1L_INTERSTITIAL_REQ_FAILED, params);
                ProxyAdMarvelView.this.adMarvelInterstitialAds = null;
                ProxyAdMarvelView.this.mInterstitialRequested = false;
            } catch (Throwable e) {
                Diagnostics.m1953e(ProxyAdMarvelView.this.TAG(), e);
            }
        }

        public void onReceiveInterstitialAd(SDKAdNetwork sdkAdNetwork, AdMarvelInterstitialAds paramAdMarvelInterstitialAds, AdMarvelAd adMarvelAd) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "admarvel.onReceiveInterstitialAd");
            try {
                if (ProxyAdMarvelView.this.activityRef != null && adMarvelAd.getSiteId().equals(ProxyAdMarvelView.this.mAdPlacement.getSiteid())) {
                    ProxyAdMarvelView.this.mSdkAdNetwork = sdkAdNetwork;
                    ProxyAdMarvelView.this.mPublisherid = paramAdMarvelInterstitialAds.getPartnerId();
                    ProxyAdMarvelView.this.mAdMarvelAd = adMarvelAd;
                }
            } catch (Throwable e) {
                Diagnostics.m1953e(ProxyAdMarvelView.this.TAG(), e);
            }
        }

        public void onRequestInterstitialAd(AdMarvelInterstitialAds paramAdMarvelInterstitialAds) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "admarvel.onRequestInterstitialAd");
        }

        public void onInterstitialDisplayed(AdMarvelInterstitialAds paramAdMarvelInterstitialAds) {
            Diagnostics.m1951d(ProxyAdMarvelView.this.TAG(), "admarvel.onInterstitialDisplayed");
        }
    }

    static {
        initialized = false;
        instanceId = 0;
    }

    public static void initAdColony(Context context, Attributes atts) {
        if (atts != null) {
            String appId = Stomp.EMPTY;
            String isInstafeed = Stomp.FALSE;
            StringBuilder zoneIds = new StringBuilder();
            for (int i = 0; i < atts.getLength(); i++) {
                Diagnostics.m1956v("ProxyAdMarvelView:initAdColony", "found adcolony init attribute: " + atts.getLocalName(i) + "=" + atts.getValue(i));
                String attName = atts.getLocalName(i);
                if (attName != null) {
                    if ("appId".equals(attName)) {
                        appId = atts.getValue(attName);
                    } else if (attName.startsWith("zoneId")) {
                        zoneIds.append(atts.getValue(attName)).append('|');
                    } else if ("isInstafeed".equals(attName)) {
                        isInstafeed = atts.getValue(attName);
                    }
                }
            }
            if (zoneIds.length() > 0) {
                zoneIds.setLength(zoneIds.length() - 1);
            }
            boolean needsInit = (appId.equals(Preferences.getSimplePref(context, "adcolonyAppId", null)) && zoneIds.toString().equals(Preferences.getSimplePref(context, "adcolonyZoneIds", null)) && isInstafeed.equals(Preferences.getSimplePref(context, "adcolonyIsInstafeed", null))) ? false : true;
            if (needsInit) {
                Preferences.setSimplePref(context, "adcolonyAppId", appId);
                Preferences.setSimplePref(context, "adcolonyZoneIds", zoneIds.toString());
                Preferences.setSimplePref(context, "adcolonyIsInstafeed", isInstafeed);
                Handler h = AdView.getStaticHandler();
                if (h != null) {
                    h.post(new C13011(context));
                }
            }
        }
    }

    private static void initializeAdcolonyMediation(Context context) {
        try {
            String appId = Preferences.getSimplePref(context, "adcolonyAppId", Stomp.EMPTY);
            String zoneIds = Preferences.getSimplePref(context, "adcolonyZoneIds", Stomp.EMPTY);
            String isInstafeed = Preferences.getSimplePref(context, "adcolonyIsInstafeed", Stomp.FALSE);
            if (appId.length() == 0 || zoneIds.length() == 0) {
                AdMarvelUtils.initialize((Activity) context, null);
                initialized = true;
                return;
            }
            Map<SDKAdNetwork, String> publisherIds = null;
            if (zoneIds != null) {
                publisherIds = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append(Preferences.getSimplePref(context, "ads-product-version", "0"));
                if (Stomp.TRUE.equals(isInstafeed)) {
                    sb.append(":YES");
                }
                sb.append('|').append(appId).append('|').append(zoneIds);
                Diagnostics.m1956v("ProxyAdMarvelView", "Initializing admarvel with adcolony setup: " + sb.toString());
                publisherIds.put(SDKAdNetwork.ADCOLONY, sb.toString());
            }
            AdMarvelUtils.initialize((Activity) context, publisherIds);
            initialized = true;
        } catch (Throwable e) {
            Diagnostics.m1953e("ProxyAdMarvelView", e);
        }
    }

    protected String TAG() {
        if (this._tag == null) {
            this._tag = "ProxyAdMarvelView-" + instanceId;
        }
        return this._tag;
    }

    ProxyAdMarvelView(Activity activity) {
        super(activity, null, null);
        this.mAdHeight = 0;
        this.interstitial_displayed = 0;
        this.mAdListener = new C13022();
        this.mInterstitialListener = new C13033();
    }

    ProxyAdMarvelView(Context context, AdPlacement placement, AdView parent) {
        super(context, placement, parent);
        this.mAdHeight = 0;
        this.interstitial_displayed = 0;
        this.mAdListener = new C13022();
        this.mInterstitialListener = new C13033();
        instanceId++;
        try {
            if (placement.isRecycleable()) {
                this.mAdView = new AdMarvelView(context.getApplicationContext());
            } else {
                this.mAdView = new AdMarvelView(context);
            }
            this.mAdView.setAdmarvelWebViewAsSoftwareLayer(true);
            this.mAdView.setListener(this.mAdListener);
            this.mAdView.setDisableAnimation(true);
            this.mAdView.setEnableAutoScaling(true);
            if (parent != null && !parent.isBannerAd()) {
                LayoutParams lp = parent.getLayoutParams();
                if (lp != null && lp.width != -2) {
                    this.mAdView.setAdContainerWidth(lp.width);
                }
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    ProxyAdMarvelView(Activity activity, AdPlacement placement) {
        super(activity, placement);
        this.mAdHeight = 0;
        this.interstitial_displayed = 0;
        this.mAdListener = new C13022();
        this.mInterstitialListener = new C13033();
    }

    public void resume() {
        super.resume();
        if (this.mAdView != null) {
            this.mAdView.resume((Activity) getContext());
        }
    }

    public void pause() {
        super.pause();
        if (this.mAdView != null) {
            this.mAdView.pause((Activity) getContext());
        }
    }

    public void onAddedToListView() {
        if (this.mAdView != null && this.mAdPlacement != null && this.mAdPlacement.isCloneable()) {
            Diagnostics.m1956v(TAG(), "mAdView.notifyAddedToListView()");
            this.mAdView.notifyAddedToListView();
        }
    }

    public void start() {
        super.start();
        try {
            if (!initialized) {
                initializeAdcolonyMediation(getContext());
            }
            if (VERSION.SDK_INT >= 19) {
                AdMarvelView.setEnableHardwareAcceleration(false);
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public void stop() {
        super.stop();
        if (this.mAdView != null) {
            Context context = getContext();
            if (context != null && (context instanceof Activity)) {
                this.mAdView.stop((Activity) getContext());
            }
        }
    }

    public void destroy() {
        Diagnostics.m1951d(TAG(), "destroy");
        try {
            if (this.mAdView != null) {
                this.mAdView.destroy();
                this.mAdView = null;
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public View getProxiedView() {
        return this.mAdView;
    }

    public int getHeight() {
        if (this.mAdHeight != 0) {
            return this.mAdHeight;
        }
        return super.getHeight();
    }

    public void invalidate() {
        if (this.mAdView != null) {
            this.mAdView.invalidate();
        }
    }

    public void requestAd(HashMap<String, Object> targetParams) {
        Diagnostics.m1951d(TAG(), "requestAd");
        try {
            if (this.mAdView != null) {
                String partnerid = this.mAdPlacement.getPubid();
                String siteid = this.mAdPlacement.getSiteid();
                String adSizes = null;
                if (targetParams == null) {
                    targetParams = new HashMap();
                }
                for (String key : targetParams.keySet()) {
                    String value = (String) targetParams.get(key);
                    if (value != null && value.length() > 0) {
                        if (key.equals("ADSIZES")) {
                            adSizes = value;
                            this.mAdHeight = 0;
                        } else {
                            if (key.equals("ADUNITID")) {
                                siteid = value;
                            }
                        }
                    }
                }
                int clickCount = Preferences.getSimplePref(getContext(), "admarvel-clickcnt", 0);
                if (clickCount == 0) {
                    targetParams.put("BNG", "0");
                } else if (clickCount < 10) {
                    targetParams.put("BNG", "1");
                } else if (clickCount < 20) {
                    targetParams.put("BNG", "2");
                } else {
                    targetParams.put("BNG", "3");
                }
                if (adSizes != null) {
                    for (String size : adSizes.split(Stomp.COMMA)) {
                        String size2 = size2.trim();
                        int idx = size2.indexOf(Nack.INDEX);
                        if (idx != -1) {
                            String wStr = size2.substring(0, idx);
                            String hStr = size2.substring(idx + 1);
                            int width = Integer.parseInt(wStr);
                            int height = Integer.parseInt(hStr);
                            if (!(width == 0 || height == 0)) {
                                int i = this.mAdHeight;
                                if (height > r0) {
                                    this.mAdHeight = height;
                                }
                            }
                        }
                    }
                }
                if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.m1951d(TAG(), "pubid=" + partnerid);
                    Diagnostics.m1951d(TAG(), "siteid=" + siteid);
                }
                logTargetParams(targetParams);
                this.mAdPlacement.sendBroadcast(getContext(), AdPlacement.ACTION_1L_ADVIEW_REQUESTED, null);
                SendAdUsage.trackEvent(getContext(), this.mAdPlacement, Constants.AD_REQUEST, targetParams, null);
                boolean autoScale = true;
                if ((this.mAdHeight != 0 && Utils.isTabletLayout(getContext())) || Utils.isLandscape()) {
                    autoScale = false;
                }
                this.mAdView.setEnableAutoScaling(autoScale);
                this.mAdView.setEnableClickRedirect(this.mAdPlacement.doClickRedirect());
                if (getContext() instanceof Activity) {
                    this.mAdView.requestNewAd(targetParams, partnerid, siteid, (Activity) getContext());
                    return;
                }
                this.mAdView.requestNewAd(targetParams, partnerid, siteid);
                return;
            }
            Diagnostics.m1952e(TAG(), "requestAd, mAdView == null");
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        } catch (Throwable e2) {
            Diagnostics.m1953e(TAG(), e2);
        }
    }

    public void requestInterstitial(Activity activity, HashMap<String, Object> targetParams) {
        Diagnostics.m1951d(TAG(), "requestInterstitial");
        try {
            this.activityRef = new SoftReference(activity);
            this.adMarvelInterstitialAds = new AdMarvelInterstitialAds(activity, 0, 7499117, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 0);
            GeoLocation geolocation = LocationUtils.getGeoLocation(activity);
            String value = Preferences.getSimplePref((Context) activity, "ads-twitterid", Stomp.EMPTY);
            if (value.length() > 0) {
                targetParams.put("jz", value);
            }
            value = Preferences.getSimplePref((Context) activity, "ads-product-version", Stomp.EMPTY);
            if (value.length() > 0) {
                targetParams.put("APP_VERSION", value);
            }
            if (geolocation != null) {
                value = geolocation.getPostalcode();
                if (value != null && value.length() > 0) {
                    targetParams.put("POSTAL_CODE", value);
                }
                targetParams.put("GEOLOCATION", geolocation.toString());
            }
            Map<String, String> mediatorParams = Preferences.getMediatorArguments(activity, "admarvel");
            for (String key : mediatorParams.keySet()) {
                value = (String) mediatorParams.get(key);
                if (value != null && value.length() > 0) {
                    Diagnostics.m1955i(TAG(), key + "=" + value);
                    targetParams.put(key, value);
                }
            }
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(TAG(), "pubid=" + this.mAdPlacement.getPubid());
                Diagnostics.m1951d(TAG(), "siteid=" + this.mAdPlacement.getSiteid());
            }
            this.adMarvelInterstitialAds.setListener(this.mInterstitialListener);
            AdMarvelInterstitialAds.setEnableClickRedirect(true);
            this.adMarvelInterstitialAds.requestNewInterstitialAd(activity, targetParams, this.mAdPlacement.getPubid(), this.mAdPlacement.getSiteid());
            this.mInterstitialRequested = true;
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public void displayInterstitial(Activity activity) {
        Diagnostics.m1951d(TAG(), "displayInterstitial");
        try {
            if (this.mAdPlacement.ispaused_until(activity)) {
                if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.m1952e(TAG(), "mAdPlacement.ispaused_until() is true");
                }
            } else if (this.adMarvelInterstitialAds != null) {
                this.activityRef = new SoftReference(activity);
                this.adMarvelInterstitialAds.displayInterstitial(activity, this.mSdkAdNetwork, this.mPublisherid, this.mAdMarvelAd);
                this.adMarvelInterstitialAds = null;
                this.interstitial_displayed = System.currentTimeMillis();
                this.mInterstitialRequested = false;
                this.mAdMarvelAd = null;
                this.mSdkAdNetwork = null;
                this.mPublisherid = null;
                String reset = this.mAdPlacement.getReset();
                if (reset != null) {
                    PlacementManager.getInstance().resetInterstitials(getContext(), reset);
                } else {
                    this.mAdPlacement.reset(getContext(), System.currentTimeMillis());
                }
            } else {
                Diagnostics.m1957w(TAG(), "adMarvelInterstitialAds == null");
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public boolean isInterstitialReady() {
        boolean isready = this.mAdMarvelAd != null;
        Diagnostics.m1951d(TAG(), "isInterstitialReady " + isready);
        return isready;
    }
}
