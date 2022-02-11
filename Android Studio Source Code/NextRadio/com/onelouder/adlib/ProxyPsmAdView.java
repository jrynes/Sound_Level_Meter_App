package com.onelouder.adlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import com.admarvel.android.ads.Constants;
import com.pinsightmediaplus.advertising.IPsmAd;
import com.pinsightmediaplus.advertising.PsmAdManager;
import com.pinsightmediaplus.advertising.PsmInterstitialAdView;
import com.pinsightmediaplus.advertising.PsmSimpleAdView;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.HashMap;

public class ProxyPsmAdView extends BaseAdProxy {
    private static int instanceId;
    private String _tag;
    private long interstitial_displayed;
    private PsmSimpleAdView mAdView;
    private boolean mInterstitialReady;
    private SimplePsmNotification mPsmAdListener;
    private SimplePsmNotification mPsmInterstitialListener;

    /* renamed from: com.onelouder.adlib.ProxyPsmAdView.1 */
    class C13071 extends SimplePsmNotification {

        /* renamed from: com.onelouder.adlib.ProxyPsmAdView.1.1 */
        class C13061 implements Runnable {
            C13061() {
            }

            public void run() {
                ProxyPsmAdView.this.mAdView.invalidate();
            }
        }

        C13071() {
        }

        public void onRequest(IPsmAd ad) {
            Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "onRequestAd - psm");
        }

        public void onImpression(IPsmAd ad) {
            Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "onImpression - psm");
            try {
                if (ProxyPsmAdView.this.mAdView != null) {
                    ProxyPsmAdView.this.mAdPlacement.sendBroadcast(ProxyPsmAdView.this.getContext(), AdPlacement.ACTION_1L_ADVIEW_RECEIVED, null);
                    if (!ProxyPsmAdView.this.mAdPlacement.ispaused()) {
                        ProxyPsmAdView.this.mAdPlacement.setTimestamp(System.currentTimeMillis());
                        ProxyPsmAdView.this.mAdViewParent.resumeAdView(true);
                        ProxyPsmAdView.this.mAdView.invalidate();
                        ProxyPsmAdView.this.mAdViewParent.getHandler().postDelayed(new C13061(), 100);
                        return;
                    } else if (Diagnostics.getInstance().isEnabled(4)) {
                        Diagnostics.m1952e(ProxyPsmAdView.this.TAG(), "isPaused=true, ignoring ad");
                        return;
                    } else {
                        return;
                    }
                }
                Diagnostics.m1957w(ProxyPsmAdView.this.TAG(), "ad was null");
            } catch (Throwable e) {
                Diagnostics.m1953e(ProxyPsmAdView.this.TAG(), e);
            }
        }

        public void onResponse(IPsmAd ad, boolean valid) {
            Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "onResponse - psm, valid=" + valid);
            if (!valid) {
                ProxyPsmAdView.this.onAdRequestFailed(-1, null);
            }
        }

        public void onError(IPsmAd ad, String errorMsg) {
            Diagnostics.m1957w(ProxyPsmAdView.this.TAG(), "onFailedToReceiveAd - psm, errorMsg=" + errorMsg);
        }

        public boolean onClick(IPsmAd ad, String url) {
            Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "onClickAd - psm, url=" + url);
            if (ProxyPsmAdView.this.mAdViewParent == null) {
                Diagnostics.m1952e(ProxyPsmAdView.this.TAG(), "onClick - mAdViewParent == null");
                return false;
            } else if (url == null || url.length() == 0) {
                Diagnostics.m1957w(ProxyPsmAdView.this.TAG(), "onClickAd - url invalid (null or len=0)");
                return false;
            } else if (url.startsWith("clktoact")) {
                params = new HashMap();
                params.put(SettingsJsonConstants.APP_URL_KEY, url);
                SendAdUsage.trackEvent(ProxyPsmAdView.this.getContext(), ProxyPsmAdView.this.mAdPlacement, "clktoact", null, url);
                ProxyPsmAdView.this.mAdPlacement.sendBroadcast(ProxyPsmAdView.this.getContext(), AdPlacement.ACTION_1L_ADVIEW_CLKTOACT, params);
                return true;
            } else if (ProxyPsmAdView.this.mAdViewParent.wasTouched()) {
                ProxyPsmAdView.this.mAdViewParent.resetTouch();
                if (ProxyPsmAdView.this.mAdViewParent.onAdViewClicked(url)) {
                    Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "user handled click event.");
                    return true;
                }
                try {
                    Context context = ProxyPsmAdView.this.getContext();
                    if (url.startsWith("clktoact")) {
                        params = new HashMap();
                        params.put(SettingsJsonConstants.APP_URL_KEY, url);
                        SendAdUsage.trackEvent(context, ProxyPsmAdView.this.mAdPlacement, "clktoact", null, url);
                        ProxyPsmAdView.this.mAdPlacement.sendBroadcast(context, AdPlacement.ACTION_1L_ADVIEW_CLKTOACT, params);
                        return true;
                    }
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
                        }
                    } else {
                        intent = new Intent(context, AdActivity.class);
                        intent.setFlags(268435456);
                        intent.putExtra(SettingsJsonConstants.APP_URL_KEY, url);
                        context.startActivity(intent);
                    }
                    return true;
                } catch (Throwable e) {
                    Diagnostics.m1953e(ProxyPsmAdView.this.TAG(), e);
                }
            } else {
                Diagnostics.m1957w(ProxyPsmAdView.this.TAG(), "onClickAd - admarvel called without touch event");
                return true;
            }
        }
    }

    /* renamed from: com.onelouder.adlib.ProxyPsmAdView.2 */
    class C13082 extends SimplePsmNotification {
        C13082() {
        }

        public void onRequest(IPsmAd ad) {
            Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "onRequest - psm");
        }

        public void onImpression(IPsmAd ad) {
            Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "onImpression - psm");
            ProxyPsmAdView.this.mInterstitialReady = true;
        }

        public void onResponse(IPsmAd ad, boolean valid) {
            Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "onResponse - psm");
            if (!valid) {
                onError(ad, null);
            }
        }

        public void onError(IPsmAd ad, String errorMsg) {
            Diagnostics.m1952e(ProxyPsmAdView.this.TAG(), "onError - psm, errorMsg=" + errorMsg);
            try {
                HashMap<String, Object> params = new HashMap();
                if (errorMsg != null) {
                    params.put(AdPlacement.EXTRA_1L_ERROR_MESSAGE, errorMsg);
                }
                ProxyPsmAdView.this.mAdPlacement.sendBroadcast(ProxyPsmAdView.this.getContext(), AdPlacement.ACTION_1L_INTERSTITIAL_REQ_FAILED, params);
                ProxyPsmAdView.this.mInterstitialRequested = false;
            } catch (Throwable e) {
                Diagnostics.m1953e(ProxyPsmAdView.this.TAG(), e);
            }
        }

        public boolean onClick(IPsmAd ad, String url) {
            Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "onClick - psm");
            return false;
        }

        public void onClose(IPsmAd ad, String operation) {
            Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "onClose - psm");
            try {
                if (ProxyPsmAdView.this.mAdPlacement.areStringsDefined()) {
                    PlacementManager.getInstance().pauseFromClose(ProxyPsmAdView.this.getContext(), ProxyPsmAdView.this.mAdPlacement.getReset(), ProxyPsmAdView.this.mAdPlacement.getPauseDuration());
                }
                HashMap<String, Object> params = new HashMap();
                params.put(AdPlacement.EXTRA_1L_OPEN_DURATION, Integer.valueOf((int) (System.currentTimeMillis() - ProxyPsmAdView.this.interstitial_displayed)));
                ProxyPsmAdView.this.mAdPlacement.onInterstitialClosed(ProxyPsmAdView.this.getContext(), params);
            } catch (Throwable e) {
                Diagnostics.m1953e(ProxyPsmAdView.this.TAG(), e);
            }
        }

        public void onExpand(IPsmAd ad) {
            Diagnostics.m1951d(ProxyPsmAdView.this.TAG(), "onExpand - psm");
        }
    }

    static {
        instanceId = 0;
    }

    protected String TAG() {
        if (this._tag == null) {
            this._tag = "ProxyPsmAdView-" + instanceId;
        }
        return this._tag;
    }

    ProxyPsmAdView() {
        this.interstitial_displayed = 0;
        this.mInterstitialReady = false;
        this.mPsmAdListener = new C13071();
        this.mPsmInterstitialListener = new C13082();
    }

    ProxyPsmAdView(Context context, AdPlacement placement, AdView parent) {
        super(context, placement, parent);
        this.interstitial_displayed = 0;
        this.mInterstitialReady = false;
        this.mPsmAdListener = new C13071();
        this.mPsmInterstitialListener = new C13082();
        instanceId++;
        if (Diagnostics.getInstance().isEnabled(4)) {
            PsmAdManager.getInstance().setLogLevel("DEBUG");
        }
        Log.e(TAG(), PsmAdManager.getSDKVersionNumber());
        this.mAdView = new PsmSimpleAdView(context);
        this.mAdView.setNotification(this.mPsmAdListener);
    }

    ProxyPsmAdView(Activity activity, AdPlacement placement) {
        super(activity, placement);
        this.interstitial_displayed = 0;
        this.mInterstitialReady = false;
        this.mPsmAdListener = new C13071();
        this.mPsmInterstitialListener = new C13082();
    }

    public View getProxiedView() {
        return this.mAdView;
    }

    public void invalidate() {
        if (this.mAdView != null) {
            this.mAdView.invalidate();
        }
    }

    public void requestAd(HashMap<String, Object> targetParams) {
        Diagnostics.m1951d(TAG(), "requestAd");
        GeoLocation location = LocationUtils.getGeoLocation(getContext());
        if (location != null) {
            PsmAdManager.getInstance().setLocation(location.getLocation());
        }
        logTargetParams(targetParams);
        this.mAdPlacement.sendBroadcast(getContext(), AdPlacement.ACTION_1L_ADVIEW_REQUESTED, null);
        SendAdUsage.trackEvent(getContext(), this.mAdPlacement, Constants.AD_REQUEST, targetParams, null);
        for (String key : targetParams.keySet()) {
            String value = (String) targetParams.get(key);
            if (value != null && value.length() > 0) {
                if (key.equals("KEYWORDS")) {
                    this.mAdView.setKeywords(value.split(" "));
                } else if (!key.equals("GEOLOCATION")) {
                    this.mAdView.putCustomParameter(key, value);
                }
            }
        }
        if (Diagnostics.getInstance().isEnabled(4)) {
            Diagnostics.m1951d(TAG(), "siteid=" + this.mAdPlacement.getSiteid());
        }
        this.mAdView.setAdSpaceId(this.mAdPlacement.getSiteid());
        this.mAdView.refreshAd();
    }

    public void requestInterstitial(Activity activity, HashMap<String, Object> hashMap) {
        Diagnostics.m1951d(TAG(), "requestInterstitial");
        try {
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(TAG(), "siteid=" + this.mAdPlacement.getSiteid());
            }
            PsmInterstitialAdView.requestInterstitialAd(activity, this.mAdPlacement.getSiteid(), this.mPsmInterstitialListener);
            this.mInterstitialRequested = true;
            this.mInterstitialReady = false;
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public void displayInterstitial(Activity activity) {
        Diagnostics.m1951d(TAG(), "displayInterstitial");
        try {
            if (!this.mAdPlacement.ispaused_until(activity)) {
                PsmInterstitialAdView.showRequestedAd(activity);
                this.interstitial_displayed = System.currentTimeMillis();
                this.mInterstitialRequested = false;
                this.mInterstitialReady = false;
                String reset = this.mAdPlacement.getReset();
                if (reset != null) {
                    PlacementManager.getInstance().resetInterstitials(getContext(), reset);
                } else {
                    this.mAdPlacement.reset(getContext(), System.currentTimeMillis());
                }
            } else if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1952e(TAG(), "mAdPlacement.ispaused_until() is true");
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public boolean isInterstitialReady() {
        return this.mInterstitialReady;
    }
}
