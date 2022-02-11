package com.onelouder.adlib;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import com.admarvel.android.ads.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class ProxyGooglePlay extends BaseAdProxy implements AppEventListener {
    private static int instanceId;
    private String _tag;
    private PublisherInterstitialAd interstitial;
    private long interstitial_displayed;
    private int mAdHeight;
    private AdListener mAdInterstitialListener;
    private AdListener mAdListener;
    private PublisherAdView mAdView;
    private FrameLayout mContainer;

    /* renamed from: com.onelouder.adlib.ProxyGooglePlay.1 */
    class C13041 extends AdListener {
        C13041() {
        }

        public void onAdClosed() {
            Diagnostics.m1951d(ProxyGooglePlay.this.TAG(), "AdListener.onAdClosed()");
        }

        public void onAdFailedToLoad(int errorCode) {
            String errorMessage;
            switch (errorCode) {
                case Tokenizer.EOF /*0*/:
                    errorMessage = "ERROR_CODE_INTERNAL_ERROR";
                    break;
                case Zone.PRIMARY /*1*/:
                    errorMessage = "ERROR_CODE_INVALID_REQUEST";
                    break;
                case Zone.SECONDARY /*2*/:
                    errorMessage = "ERROR_CODE_NETWORK_ERROR";
                    break;
                case Protocol.GGP /*3*/:
                    errorMessage = "ERROR_CODE_NO_FILL";
                    break;
                default:
                    try {
                        errorMessage = Integer.toString(errorCode);
                        break;
                    } catch (Throwable e) {
                        Diagnostics.m1953e(ProxyGooglePlay.this.TAG(), e);
                        return;
                    }
            }
            ProxyGooglePlay.this.onAdRequestFailed(errorCode, errorMessage);
        }

        public void onAdLeftApplication() {
            Diagnostics.m1951d(ProxyGooglePlay.this.TAG(), "AdListener.onAdLeftApplication()");
        }

        public void onAdLoaded() {
            Diagnostics.m1951d(ProxyGooglePlay.this.TAG(), "AdListener.onAdLoaded()");
            if (ProxyGooglePlay.this.mAdView != null) {
                ProxyGooglePlay.this.mContainer.removeAllViews();
                ProxyGooglePlay.this.mContainer.addView(ProxyGooglePlay.this.mAdView);
                ProxyGooglePlay.this.mAdPlacement.sendBroadcast(ProxyGooglePlay.this.getContext(), AdPlacement.ACTION_1L_ADVIEW_RECEIVED, null);
                if (!ProxyGooglePlay.this.mAdPlacement.ispaused()) {
                    ProxyGooglePlay.this.mAdPlacement.setTimestamp(System.currentTimeMillis());
                    ProxyGooglePlay.this.mAdViewParent.resumeAdView(true);
                    return;
                } else if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.m1952e(ProxyGooglePlay.this.TAG(), "isPaused=true, ignoring ad");
                    return;
                } else {
                    return;
                }
            }
            Diagnostics.m1957w(ProxyGooglePlay.this.TAG(), "ad was null");
        }

        public void onAdOpened() {
            Diagnostics.m1951d(ProxyGooglePlay.this.TAG(), "AdListener.onAdOpened()");
        }
    }

    /* renamed from: com.onelouder.adlib.ProxyGooglePlay.2 */
    class C13052 extends AdListener {
        C13052() {
        }

        public void onAdClosed() {
            Diagnostics.m1951d(ProxyGooglePlay.this.TAG(), "AdInterstitialListener.onAdClosed()");
            try {
                if (ProxyGooglePlay.this.mAdPlacement.areStringsDefined()) {
                    PlacementManager.getInstance().pauseFromClose(ProxyGooglePlay.this.getContext(), ProxyGooglePlay.this.mAdPlacement.getReset(), ProxyGooglePlay.this.mAdPlacement.getPauseDuration());
                }
                long interstitial_ended = System.currentTimeMillis();
                HashMap<String, Object> params = new HashMap();
                params.put(AdPlacement.EXTRA_1L_OPEN_DURATION, Integer.valueOf((int) (interstitial_ended - ProxyGooglePlay.this.interstitial_displayed)));
                ProxyGooglePlay.this.mAdPlacement.onInterstitialClosed(ProxyGooglePlay.this.getContext(), params);
            } catch (Throwable e) {
                Diagnostics.m1953e(ProxyGooglePlay.this.TAG(), e);
            }
        }

        public void onAdFailedToLoad(int errorCode) {
            String errorMessage;
            Diagnostics.m1951d(ProxyGooglePlay.this.TAG(), "AdInterstitialListener.onAdFailedToLoad()");
            switch (errorCode) {
                case Tokenizer.EOF /*0*/:
                    errorMessage = "ERROR_CODE_INTERNAL_ERROR";
                    break;
                case Zone.PRIMARY /*1*/:
                    errorMessage = "ERROR_CODE_INVALID_REQUEST";
                    break;
                case Zone.SECONDARY /*2*/:
                    errorMessage = "ERROR_CODE_NETWORK_ERROR";
                    break;
                case Protocol.GGP /*3*/:
                    errorMessage = "ERROR_CODE_NO_FILL";
                    break;
                default:
                    try {
                        errorMessage = Integer.toString(errorCode);
                        break;
                    } catch (Throwable e) {
                        Diagnostics.m1953e(ProxyGooglePlay.this.TAG(), e);
                        return;
                    }
            }
            Diagnostics.m1951d(ProxyGooglePlay.this.TAG(), "AdListener.onAdFailedToLoad(), errorCode=" + errorMessage);
            HashMap<String, Object> params = new HashMap();
            params.put(AdPlacement.EXTRA_1L_ERROR_MESSAGE, errorMessage);
            params.put(AdPlacement.EXTRA_1L_ERROR_CODE, Integer.toString(errorCode));
            ProxyGooglePlay.this.mAdPlacement.sendBroadcast(ProxyGooglePlay.this.getContext(), AdPlacement.ACTION_1L_INTERSTITIAL_REQ_FAILED, params);
        }

        public void onAdLeftApplication() {
            Diagnostics.m1951d(ProxyGooglePlay.this.TAG(), "AdInterstitialListener.onAdLeftApplication()");
        }

        public void onAdLoaded() {
            Diagnostics.m1951d(ProxyGooglePlay.this.TAG(), "AdInterstitialListener.onAdLoaded()");
        }

        public void onAdOpened() {
            Diagnostics.m1951d(ProxyGooglePlay.this.TAG(), "AdInterstitialListener.onAdOpened()");
        }
    }

    static {
        instanceId = 0;
    }

    protected String TAG() {
        if (this._tag == null) {
            this._tag = "ProxyGooglePlay-" + instanceId;
        }
        return this._tag;
    }

    ProxyGooglePlay() {
        this.mAdHeight = 0;
        this.interstitial_displayed = 0;
        this.mAdListener = new C13041();
        this.mAdInterstitialListener = new C13052();
    }

    ProxyGooglePlay(Context context, AdPlacement placement, AdView parent) {
        super(context, placement, parent);
        this.mAdHeight = 0;
        this.interstitial_displayed = 0;
        this.mAdListener = new C13041();
        this.mAdInterstitialListener = new C13052();
        instanceId++;
    }

    ProxyGooglePlay(Activity activity, AdPlacement placement) {
        super(activity, placement);
        this.mAdHeight = 0;
        this.interstitial_displayed = 0;
        this.mAdListener = new C13041();
        this.mAdInterstitialListener = new C13052();
    }

    public View getProxiedView() {
        if (this.mContainer == null) {
            this.mContainer = new FrameLayout(getContext());
        }
        return this.mContainer;
    }

    public void resume() {
        super.resume();
        if (this.mAdView != null) {
            this.mAdView.resume();
        }
    }

    public void pause() {
        super.pause();
        if (this.mAdView != null) {
            this.mAdView.pause();
        }
    }

    public void destroy() {
        Diagnostics.m1951d(TAG(), "destroy");
        if (this.mContainer != null) {
            this.mContainer.removeAllViews();
        }
        if (this.mAdView != null) {
            this.mAdView.destroy();
            this.mAdView = null;
        }
    }

    public void invalidate() {
        if (this.mAdView != null) {
            this.mAdView.invalidate();
        }
    }

    public int getHeight() {
        if (this.mAdHeight != 0) {
            return this.mAdHeight;
        }
        return super.getHeight();
    }

    public void requestAd(HashMap<String, Object> targetParams) {
        Diagnostics.m1951d(TAG(), "requestAd");
        try {
            String partnerid = this.mAdPlacement.getPubid();
            String siteid = this.mAdPlacement.getSiteid();
            this.mAdView = new PublisherAdView(getContext());
            this.mAdView.setAppEventListener(this);
            this.mAdView.setAdListener(this.mAdListener);
            if (targetParams.containsKey("ADUNITID")) {
                siteid = (String) targetParams.get("ADUNITID");
                targetParams.remove("ADUNITID");
            }
            this.mAdView.setAdUnitId(siteid);
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(TAG(), "pubid=" + partnerid);
                Diagnostics.m1951d(TAG(), "siteid=" + siteid);
            }
            logTargetParams(targetParams);
            this.mAdPlacement.sendBroadcast(getContext(), AdPlacement.ACTION_1L_ADVIEW_REQUESTED, null);
            SendAdUsage.trackEvent(getContext(), this.mAdPlacement, Constants.AD_REQUEST, targetParams, null);
            boolean bSizeSet = false;
            if (targetParams != null && targetParams.containsKey("ADSIZES")) {
                String size = (String) targetParams.get("ADSIZES");
                if (size.equals("300x250")) {
                    this.mAdView.setAdSizes(AdSize.MEDIUM_RECTANGLE);
                    bSizeSet = true;
                } else if (size.length() > 0) {
                    try {
                        if (size.split("x").length == 2) {
                            this.mAdView.setAdSizes(new AdSize(Integer.parseInt(sizeSpec[0]), Integer.parseInt(sizeSpec[1])));
                            bSizeSet = true;
                        }
                    } catch (Throwable e) {
                        Diagnostics.m1953e(TAG(), e);
                    }
                }
            }
            if (!bSizeSet) {
                if (!Utils.isXLargeLayout(getContext())) {
                    this.mAdView.setAdSizes(AdSize.BANNER);
                } else if (Utils.isPortrait()) {
                    this.mAdView.setAdSizes(new AdSize(728, 90), new AdSize(Flags.OWNER_MASK, 90));
                } else {
                    this.mAdView.setAdSizes(new AdSize(728, 90), new AdSize(Flags.OWNER_MASK, 90), new AdSize(Flags.FLAG5, 90));
                }
            }
            this.mAdView.loadAd(createAdRequest(targetParams));
        } catch (Throwable e2) {
            Diagnostics.m1953e(TAG(), e2);
        }
    }

    public void requestInterstitial(Activity activity, HashMap<String, Object> targetParams) {
        Diagnostics.m1951d(TAG(), "requestInterstitial");
        try {
            this.activityRef = new SoftReference(activity);
            this.interstitial = new PublisherInterstitialAd(activity);
            this.interstitial.setAdUnitId(this.mAdPlacement.getSiteid());
            this.interstitial.setAdListener(this.mAdInterstitialListener);
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(TAG(), "pubid=" + this.mAdPlacement.getPubid());
                Diagnostics.m1951d(TAG(), "siteid=" + this.mAdPlacement.getSiteid());
            }
            this.interstitial.loadAd(createAdRequest(targetParams));
            this.mInterstitialRequested = true;
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    private PublisherAdRequest createAdRequest(HashMap<String, Object> targetParams) {
        Builder builder = new Builder();
        GeoLocation location = LocationUtils.getGeoLocation(getContext());
        if (location != null) {
            builder = builder.setLocation(location.getLocation());
        }
        if (targetParams != null) {
            Bundle bundle = new Bundle();
            for (String key : targetParams.keySet()) {
                String value = (String) targetParams.get(key);
                if (!(value == null || value.length() <= 0 || key.equals("GEOLOCATION") || key.equals("GENDER") || key.equals("ADSIZES"))) {
                    bundle.putString(key, value);
                }
            }
            if (!bundle.isEmpty()) {
                builder = builder.addNetworkExtras(new AdMobExtras(bundle));
            }
            if (targetParams.containsKey("GENDER")) {
                String gender = ((String) targetParams.get("GENDER")).toLowerCase();
                if (gender.startsWith("m")) {
                    builder = builder.setGender(1);
                } else if (gender.startsWith("f")) {
                    builder = builder.setGender(2);
                }
            }
        }
        return builder.build();
    }

    public void displayInterstitial(Activity activity) {
        Diagnostics.m1951d(TAG(), "displayInterstitial");
        try {
            if (this.mAdPlacement.ispaused_until(activity)) {
                if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.m1952e(TAG(), "mAdPlacement.ispaused_until() is true");
                }
            } else if (this.interstitial != null && this.interstitial.isLoaded()) {
                this.activityRef = new SoftReference(activity);
                this.interstitial.show();
                this.interstitial_displayed = System.currentTimeMillis();
                this.mInterstitialRequested = false;
                this.interstitial = null;
                String reset = this.mAdPlacement.getReset();
                if (reset != null) {
                    PlacementManager.getInstance().resetInterstitials(getContext(), reset);
                } else {
                    this.mAdPlacement.reset(getContext(), System.currentTimeMillis());
                }
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public boolean isInterstitialReady() {
        if (this.interstitial != null) {
            boolean isready = this.interstitial.isLoaded();
            Diagnostics.m1951d(TAG(), "isInterstitialReady " + isready);
            if (isready) {
                return isready;
            }
            logPermissionCheck(getContext());
            return isready;
        }
        Diagnostics.m1957w(TAG(), "isInterstitialReady, interstitial==null");
        return false;
    }

    public void onAppEvent(String name, String info) {
        Diagnostics.m1951d(TAG(), "AppEventListener.onAppEvent() name=" + name + " info=" + info);
    }
}
