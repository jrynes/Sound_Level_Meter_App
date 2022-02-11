package com.admarvel.android.ads.nativeads;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import com.admarvel.android.ads.AdMarvelAdapter;
import com.admarvel.android.ads.AdMarvelAdapterInstances;
import com.admarvel.android.ads.AdMarvelAdapterListener;
import com.admarvel.android.ads.AdMarvelNetworkHandler;
import com.admarvel.android.ads.AdMarvelRedirectRunnable;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.AdMarvelUtils.AdMArvelErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.AdMarvelVideoEvents;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.ads.AdMarvelVideoEventListener;
import com.admarvel.android.ads.AdMarvelView;
import com.admarvel.android.ads.AdMarvelView.AdMarvelViewListener;
import com.admarvel.android.ads.AdMarvelXMLElement;
import com.admarvel.android.ads.Constants;
import com.admarvel.android.ads.Utils;
import com.admarvel.android.ads.nativeads.AdMarvelClickDetector.AdMarvelClickDetector;
import com.admarvel.android.ads.nativeads.AdMarvelVisibilityDetector.AdMarvelVisibilityDetector;
import com.admarvel.android.util.Logging;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;
import org.json.JSONObject;

public class AdMarvelNativeAd {
    public static final String ADMARVEL_HANDLE_CLICK_EVENT = "AdMarvelHandleClickEvent";
    public static final String ADMARVEL_HANDLE_NOTICE_CLICK_EVENT = "AdMarvelHandleNoticeClickEvent";
    public static final String FIELD_CAMPAIGNIMAGE = "campaignImage";
    public static final String FIELD_CTA = "cta";
    public static final String FIELD_DISPLAYNAME = "displayName";
    public static final String FIELD_FULLMESSAGE = "fullMessage";
    public static final String FIELD_ICON = "icon";
    public static final String FIELD_NATIVE_VIDEO_AD_VIEW = "nativeVideoView";
    public static final String FIELD_NOTICE = "notice";
    public static final String FIELD_RATING = "rating";
    public static final String FIELD_SHORTMESSAGE = "shortMessage";
    final String ADMARVEL_NATIVE_AD_GUID;
    private long adColonyDelayAfterInitInMs;
    private String adColonyMuted;
    private String adColonyVolume;
    private String adId;
    private AdMarvelNativeVideoView adMarvelNativeVideoView;
    private AdMarvelNetworkHandler adMarvelNetworkHandler;
    String adMarvelViewNativeAdXML;
    private String adSponsoredMarker;
    private C0263a adType;
    private String adcolonyAppId;
    private String adcolonyAppVersion;
    private String adcolonyTargetZoneid;
    private String adcolonyZoneId;
    AdMarvelNativeAdType admarvelNativeAdType;
    private String appId;
    private String cachedAdOfflineTracker;
    private AdMarvelNativeImage[] campaignImage;
    private String chartboostAppID;
    private String chartboostAppSignature;
    private String chartboostLocation;
    private WeakReference<Context> contextReference;
    boolean createdUsingCachedAd;
    private AdMarvelNativeCta cta;
    private String deviceConnectivity;
    private String disableAdDuration;
    private boolean disableAdRequest;
    private String displayName;
    private int errorCode;
    private String errorReason;
    private String excluded;
    private String facebookChildDirectedFlag;
    private String[] facebookTestDeviceId;
    private String fullMessage;
    private AdMarvelNativeImage icon;
    private int id;
    private String inmobiAppId;
    private final InternalAdMarvelAdapterListener internalAdMarvelAdapterListener;
    private AdMarvelClickDetector internalAdMarvelClickDetectorListener;
    private AdMarvelVisibilityDetector internalAdMarvelVisibilityDetectorListener;
    private String ipAddress;
    protected boolean isAdMarvelViewCreated;
    boolean isAdMarvelViewNativeAdXML;
    private boolean isFiringClickTrackerFirstTime;
    private boolean isFiringClickTrackerMultipleTimeAllowed;
    private boolean isFiringImpressionTrackerFirstTime;
    private boolean isFiringImpresstionTrackerMultipleTimeAllowed;
    private boolean isPrepareForListView;
    private boolean isRegisteringContainerViewFirstTime;
    String lastRequestPostString;
    private final AdMarvelNativeAdListenerImpl listenerImpl;
    private final AtomicLong lockTimestamp;
    private AdMarvelClickDetector mAdMarvelClickDetector;
    private AdMarvelVisibilityDetector mAdMarvelVisibilityDetector;
    private Context mContext;
    private int maxretries;
    private Map<String, AdMarvelNativeMetadata> metadatas;
    private String nativeAdXml;
    private float nativeVideoWidth;
    private AdMarvelNativeNotice notice;
    private int orientation;
    private String partnerId;
    private List<String> pixels;
    private String pubId;
    private AdMarvelNativeRating rating;
    private String requestJson;
    private String responseJson;
    private Boolean retry;
    private int retrynum;
    private SDKAdNetwork sdkAdNetwork;
    private String sdkNetwork;
    private String shortMessage;
    private String siteId;
    private String source;
    private Map<String, Object> targetParams;
    private AdMarvelNativeTracker[] trackers;
    private String xml;

    /* renamed from: com.admarvel.android.ads.nativeads.AdMarvelNativeAd.1 */
    class C02581 implements Runnable {
        final /* synthetic */ AdMarvelView f683a;
        final /* synthetic */ Activity f684b;
        final /* synthetic */ AdMarvelNativeAd f685c;

        C02581(AdMarvelNativeAd adMarvelNativeAd, AdMarvelView adMarvelView, Activity activity) {
            this.f685c = adMarvelNativeAd;
            this.f683a = adMarvelView;
            this.f684b = activity;
        }

        public void run() {
            this.f683a.updateCurrentActivity(this.f684b);
            this.f683a.internalResume(this.f684b);
        }
    }

    /* renamed from: com.admarvel.android.ads.nativeads.AdMarvelNativeAd.2 */
    class C02592 implements AdMarvelViewListener {
        final /* synthetic */ AdMarvelNativeAd f686a;

        C02592(AdMarvelNativeAd adMarvelNativeAd) {
            this.f686a = adMarvelNativeAd;
        }

        public void onClickAd(AdMarvelView adMarvelView, String clickUrl) {
        }

        public void onClose(AdMarvelView adMarvelView) {
        }

        public void onExpand(AdMarvelView adMarvelView) {
        }

        public void onFailedToReceiveAd(AdMarvelView adMarvelView, int errorCode, ErrorReason errorReason) {
            if (this.f686a.listenerImpl != null) {
                this.f686a.listenerImpl.m405a(this.f686a, errorCode, AdMarvelUtils.getAdMArvelErrorReason(errorCode));
            }
        }

        public void onReceiveAd(AdMarvelView adMarvelView) {
            if (this.f686a.listenerImpl != null) {
                this.f686a.listenerImpl.m407b(this.f686a);
            }
        }

        public void onRequestAd(AdMarvelView adMarvelView) {
        }
    }

    /* renamed from: com.admarvel.android.ads.nativeads.AdMarvelNativeAd.3 */
    class C02603 implements AdMarvelVideoEventListener {
        final /* synthetic */ AdMarvelNativeAd f687a;

        C02603(AdMarvelNativeAd adMarvelNativeAd) {
            this.f687a = adMarvelNativeAd;
        }

        public void onAdMarvelVideoEvent(AdMarvelVideoEvents adMarvelVideoEvent, Map<String, String> customEventParams) {
            if (this.f687a.listenerImpl != null) {
                this.f687a.listenerImpl.m401a(adMarvelVideoEvent, customEventParams);
            }
        }

        public void onAudioStart() {
            this.f687a.listenerImpl.m400a();
        }

        public void onAudioStop() {
            this.f687a.listenerImpl.m406b();
        }
    }

    /* renamed from: com.admarvel.android.ads.nativeads.AdMarvelNativeAd.4 */
    class C02614 implements AdMarvelVisibilityDetector {
        final /* synthetic */ AdMarvelNativeAd f688a;

        C02614(AdMarvelNativeAd adMarvelNativeAd) {
            this.f688a = adMarvelNativeAd;
        }

        public void m392a(boolean z) {
            if (z) {
                Logging.log("AdMarvelnativeAd - Ad visibility detected");
                this.f688a.handleImpression();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.nativeads.AdMarvelNativeAd.5 */
    class C02625 implements AdMarvelClickDetector {
        final /* synthetic */ AdMarvelNativeAd f689a;

        C02625(AdMarvelNativeAd adMarvelNativeAd) {
            this.f689a = adMarvelNativeAd;
        }

        public void m394a(View view, String str) {
            if (str != null && str.equalsIgnoreCase(AdMarvelNativeAd.ADMARVEL_HANDLE_CLICK_EVENT)) {
                this.f689a.handleClick();
            } else if (str != null && str.equalsIgnoreCase(AdMarvelNativeAd.ADMARVEL_HANDLE_NOTICE_CLICK_EVENT)) {
                this.f689a.handleNotice();
            }
        }
    }

    public interface AdMarvelNativeAdListener {
        void onClickNativeAd(AdMarvelNativeAd adMarvelNativeAd, String str);

        void onFailedToReceiveNativeAd(int i, AdMArvelErrorReason adMArvelErrorReason, AdMarvelNativeAd adMarvelNativeAd);

        void onReceiveNativeAd(AdMarvelNativeAd adMarvelNativeAd);

        void onRequestNativeAd(AdMarvelNativeAd adMarvelNativeAd);
    }

    public enum AdMarvelNativeAdType {
        ADMARVEL_NATIVEAD_TYPE_DEFAULT,
        ADMARVEL_NATIVEAD_TYPE_APPINSTALL,
        ADMARVEL_NATIVEAD_TYPE_CONTENT
    }

    public interface AdMarvelNativeVideoAdListener {
        void onNativeVideoEvent(AdMarvelVideoEvents adMarvelVideoEvents, Map<String, String> map);

        void onNativeVideoViewAudioStart();

        void onNativeVideoViewAudioStop();
    }

    private static class InternalAdMarvelAdapterListener implements AdMarvelAdapterListener {
        private AdMarvelNativeAd mNativeAd;

        public InternalAdMarvelAdapterListener(AdMarvelNativeAd mNativeAd) {
            this.mNativeAd = mNativeAd;
        }

        public void onAdMarvelVideoEvent(AdMarvelVideoEvents adMarvelVideoEvent, Map<String, String> customEventParams) {
            if (this.mNativeAd.listenerImpl != null) {
                this.mNativeAd.listenerImpl.m401a(adMarvelVideoEvent, customEventParams);
            }
        }

        public void onAudioStart() {
            if (this.mNativeAd.listenerImpl != null) {
                this.mNativeAd.listenerImpl.m400a();
            }
        }

        public void onAudioStop() {
            if (this.mNativeAd.listenerImpl != null) {
                this.mNativeAd.listenerImpl.m406b();
            }
        }

        public void onClickAd(String clickUrl) {
        }

        public void onClose() {
        }

        public void onExpand() {
        }

        public void onFailedToReceiveAd(int errorCode, ErrorReason errorReason) {
            boolean z;
            if (this.mNativeAd == null || !this.mNativeAd.getRetry().equals(Boolean.valueOf(true)) || this.mNativeAd.getRetrynum() > this.mNativeAd.getMaxretries()) {
                z = false;
            } else {
                this.mNativeAd.pixels = null;
                int retrynum = this.mNativeAd.getRetrynum() + 1;
                String adId = this.mNativeAd.getExcluded() == null ? this.mNativeAd.getAdId() : this.mNativeAd.getExcluded().length() > 0 ? this.mNativeAd.getExcluded() + Stomp.COMMA + this.mNativeAd.getAdId() : this.mNativeAd.getAdId();
                String adId2 = this.mNativeAd.createdUsingCachedAd ? this.mNativeAd.getAdId() : null;
                if (this.mNativeAd.getmContext() != null) {
                    Logging.log("Retry : onRequestAd");
                    Builder builder = new Builder();
                    builder.context(this.mNativeAd.mContext);
                    builder.partnerId(this.mNativeAd.partnerId);
                    builder.siteId(this.mNativeAd.siteId);
                    builder.targetParams(this.mNativeAd.targetParams);
                    AsyncTaskExecutor.m418a(new AdMarvelNativeAsyncTask(), builder.build(), this.mNativeAd, Integer.valueOf(retrynum), adId, adId2, Boolean.valueOf(true));
                }
                z = true;
            }
            if (!z && this.mNativeAd.listenerImpl != null) {
                this.mNativeAd.listenerImpl.m405a(this.mNativeAd, AdMArvelErrorReason.NO_AD_FOUND.getErrorCode(), AdMArvelErrorReason.NO_AD_FOUND);
                if (this.mNativeAd.isAdMarvelViewNativeAdXML) {
                    this.mNativeAd.isAdMarvelViewNativeAdXML = false;
                    this.mNativeAd.adMarvelViewNativeAdXML = null;
                }
            }
        }

        public void onReceiveAd() {
        }

        public void onReceiveNativeAd(Object nativeAd) {
            if (nativeAd != null && (nativeAd instanceof AdMarvelNativeAd)) {
                this.mNativeAd = (AdMarvelNativeAd) nativeAd;
            }
            if (this.mNativeAd.listenerImpl != null) {
                this.mNativeAd.listenerImpl.m407b(this.mNativeAd);
                if (this.mNativeAd.isAdMarvelViewNativeAdXML) {
                    this.mNativeAd.isAdMarvelViewNativeAdXML = false;
                    this.mNativeAd.adMarvelViewNativeAdXML = null;
                }
            }
        }
    }

    public static final class RequestParameters {
        private final Context mContext;
        private final String mPartnerId;
        private final String mSiteId;
        private final Map<String, Object> mTargetParams;

        public static final class Builder {
            private Context mContext;
            private String mPartnerId;
            private String mSiteId;
            private Map<String, Object> mTargetParams;

            public final RequestParameters build() {
                return new RequestParameters();
            }

            public final Builder context(Context context) {
                this.mContext = context;
                return this;
            }

            public final Builder partnerId(String partnerId) {
                this.mPartnerId = partnerId;
                return this;
            }

            public final Builder siteId(String siteId) {
                this.mSiteId = siteId;
                return this;
            }

            public final Builder targetParams(Map<String, Object> targetParams) {
                this.mTargetParams = targetParams;
                return this;
            }
        }

        private RequestParameters(Builder builder) {
            this.mSiteId = builder.mSiteId;
            this.mPartnerId = builder.mPartnerId;
            this.mContext = builder.mContext;
            this.mTargetParams = builder.mTargetParams;
        }

        public final Context getContext() {
            return this.mContext;
        }

        public final String getPartnerId() {
            return this.mPartnerId;
        }

        public final String getSiteId() {
            return this.mSiteId;
        }

        public final Map<String, Object> getTargetParams() {
            return this.mTargetParams;
        }
    }

    /* renamed from: com.admarvel.android.ads.nativeads.AdMarvelNativeAd.a */
    enum C0263a {
        SDKCALL,
        ERROR,
        NATIVE
    }

    public AdMarvelNativeAd() {
        this.facebookChildDirectedFlag = null;
        this.isPrepareForListView = false;
        this.isAdMarvelViewCreated = false;
        this.adMarvelViewNativeAdXML = null;
        this.isAdMarvelViewNativeAdXML = false;
        this.isRegisteringContainerViewFirstTime = true;
        this.admarvelNativeAdType = AdMarvelNativeAdType.ADMARVEL_NATIVEAD_TYPE_DEFAULT;
        this.disableAdRequest = false;
        this.isFiringImpressionTrackerFirstTime = true;
        this.isFiringClickTrackerFirstTime = true;
        this.isFiringImpresstionTrackerMultipleTimeAllowed = false;
        this.isFiringClickTrackerMultipleTimeAllowed = false;
        this.createdUsingCachedAd = false;
        this.nativeAdXml = null;
        this.lastRequestPostString = null;
        this.internalAdMarvelVisibilityDetectorListener = new C02614(this);
        this.internalAdMarvelClickDetectorListener = new C02625(this);
        this.ADMARVEL_NATIVE_AD_GUID = UUID.randomUUID().toString();
        AdMarvelAdapterInstances.buildAdMarvelAdapterInstances(this.ADMARVEL_NATIVE_AD_GUID);
        this.listenerImpl = new AdMarvelNativeAdListenerImpl();
        this.internalAdMarvelAdapterListener = new InternalAdMarvelAdapterListener(this);
        this.lockTimestamp = new AtomicLong(0);
    }

    private float calculateHeightFromWidth(float videoWidth, AdMarvelNativeVideoView adMarvelNativeVideo) {
        return adMarvelNativeVideo != null ? videoWidth / (adMarvelNativeVideo.getNativeVideoWidth() / ((float) adMarvelNativeVideo.getNativeVideoHeight())) : 0.0f;
    }

    private void firePixel() {
        if (this.mContext != null) {
            Utils utils = new Utils(this.mContext);
            if (!this.createdUsingCachedAd || this.cachedAdOfflineTracker == null || this.cachedAdOfflineTracker.length() <= 0) {
                List<String> pixels = getPixels();
                if (pixels != null) {
                    for (String str : pixels) {
                        if (str != null) {
                            utils.m245a(str);
                        }
                    }
                }
            } else {
                utils.m245a(this.cachedAdOfflineTracker);
            }
            this.isFiringImpressionTrackerFirstTime = false;
        }
    }

    private void fireTrackerPixel(String type) {
        if (this.mContext != null) {
            Utils utils = new Utils(this.mContext);
            AdMarvelNativeTracker[] trackers = getTrackers();
            if (trackers != null && trackers.length > 0) {
                for (AdMarvelNativeTracker adMarvelNativeTracker : trackers) {
                    if (adMarvelNativeTracker.getType().equals(type)) {
                        for (String str : adMarvelNativeTracker.getUrl()) {
                            if (str != null) {
                                utils.m245a(str);
                            }
                        }
                    }
                }
            }
            if (type.equals(Promotion.ACTION_CLICK)) {
                this.isFiringClickTrackerFirstTime = false;
            } else if (type.equals("impression")) {
                this.isFiringImpressionTrackerFirstTime = false;
            }
        }
    }

    private LayoutParams getNativeVideoLayoutParams(AdMarvelNativeVideoView adMarvelNativeVideo, Context context) {
        if (adMarvelNativeVideo == null || context == null) {
            return null;
        }
        float nativeVideoHeight = (float) adMarvelNativeVideo.getNativeVideoHeight();
        if (getNativeVideoViewWidth() == 0.0f) {
            return new LayoutParams(-2, -2);
        }
        float calculateHeightFromWidth = calculateHeightFromWidth(getNativeVideoViewWidth(), adMarvelNativeVideo);
        return new LayoutParams((int) TypedValue.applyDimension(1, adMarvelNativeVideo.getNativeVideoWidth(), ((Context) getContextReference().get()).getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(1, calculateHeightFromWidth, ((Context) getContextReference().get()).getResources().getDisplayMetrics()));
    }

    private void handleClick() {
        Logging.log("handleClick");
        if (getAdType() == C0263a.SDKCALL) {
            AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(this.ADMARVEL_NATIVE_AD_GUID, getSdkNetwork());
            if (instance != null) {
                instance.handleClick();
                return;
            }
            return;
        }
        String clickUrl = getCta() != null ? getCta().getClickUrl() : null;
        if (clickUrl != null) {
            redirectUrl(clickUrl);
        }
        if (this.isFiringClickTrackerFirstTime || this.isFiringClickTrackerMultipleTimeAllowed) {
            fireTrackerPixel(Promotion.ACTION_CLICK);
        }
    }

    private void handleImpression() {
        Logging.log("handleImpression");
        if (this.isFiringImpressionTrackerFirstTime || this.isFiringImpresstionTrackerMultipleTimeAllowed) {
            firePixel();
            fireTrackerPixel("impression");
            if (getAdType() == C0263a.SDKCALL) {
                AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(this.ADMARVEL_NATIVE_AD_GUID, getSdkNetwork());
                if (instance != null) {
                    instance.handleImpression();
                }
            }
        }
        try {
            if (this.isAdMarvelViewCreated && this.adMarvelNativeVideoView != null && this.adMarvelNativeVideoView.isShown()) {
                ViewGroup viewGroup = (ViewGroup) this.adMarvelNativeVideoView.getParent();
                if (viewGroup != null) {
                    for (Context context = viewGroup.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
                        if (context instanceof Activity) {
                            AdMarvelView adMarvelView = (AdMarvelView) this.adMarvelNativeVideoView.findViewWithTag(this.ADMARVEL_NATIVE_AD_GUID + "CURRENT_ADMVIEW_NATIVEAD");
                            new Handler(Looper.getMainLooper()).post(new C02581(this, adMarvelView, (Activity) context));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNotice() {
        Logging.log("handleNotice");
        if (getAdType() == C0263a.SDKCALL) {
            AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(this.ADMARVEL_NATIVE_AD_GUID, getSdkNetwork());
            if (instance != null) {
                instance.handleNotice();
                return;
            }
            return;
        }
        String clickUrl = getNotice() != null ? getNotice().getClickUrl() : null;
        if (clickUrl != null) {
            redirectUrl(clickUrl);
        }
    }

    private AdMarvelNativeCta parseAndGetNativeAdCTA(AdMarvelXMLElement xhtmlXMLElement) {
        try {
            AdMarvelNativeCta adMarvelNativeCta = new AdMarvelNativeCta();
            String str = (String) xhtmlXMLElement.getAttributes().get(locationTracking.action);
            if (str != null) {
                adMarvelNativeCta.setAction(str);
            }
            str = parseAndGetNativeAdStandardElement(xhtmlXMLElement, SettingsJsonConstants.PROMPT_TITLE_KEY, 0);
            if (str != null) {
                adMarvelNativeCta.setTitle(str);
            }
            str = parseAndGetNativeAdStandardElement(xhtmlXMLElement, Constants.NATIVE_AD_CLICK_URL_ELEMENT, 0);
            if (str != null) {
                adMarvelNativeCta.setClickUrl(str);
            }
            AdMarvelNativeImage parseAndGetNativeAdImageElement = parseAndGetNativeAdImageElement(xhtmlXMLElement, Constants.NATIVE_AD_IMAGE_ELEMENT);
            if (parseAndGetNativeAdImageElement != null) {
                adMarvelNativeCta.setImage(parseAndGetNativeAdImageElement);
            }
            return adMarvelNativeCta;
        } catch (Exception e) {
            return null;
        }
    }

    private AdMarvelNativeImage parseAndGetNativeAdImageElement(AdMarvelXMLElement xhtmlXMLElement) {
        try {
            AdMarvelNativeImage adMarvelNativeImage = new AdMarvelNativeImage();
            if (xhtmlXMLElement != null) {
                Object data = xhtmlXMLElement.getData();
                if (data != null && data.length() > 0 && Constants.WEB_URL.matcher(data).matches()) {
                    adMarvelNativeImage.setImageUrl(data);
                }
                String str = (String) xhtmlXMLElement.getAttributes().get(SettingsJsonConstants.ICON_WIDTH_KEY);
                String str2 = (String) xhtmlXMLElement.getAttributes().get(SettingsJsonConstants.ICON_HEIGHT_KEY);
                if (str != null) {
                    try {
                        if (str.length() > 0) {
                            adMarvelNativeImage.setWidth(Integer.parseInt(str));
                        }
                    } catch (Exception e) {
                        return null;
                    }
                }
                if (str2 != null && str2.length() > 0) {
                    adMarvelNativeImage.setHeight(Integer.parseInt(str2));
                }
            }
            return adMarvelNativeImage;
        } catch (Exception e2) {
            return null;
        }
    }

    private AdMarvelNativeImage parseAndGetNativeAdImageElement(AdMarvelXMLElement xhtmlXMLElement, String key) {
        try {
            return xhtmlXMLElement.getChildren().containsKey(key) ? parseAndGetNativeAdImageElement((AdMarvelXMLElement) ((ArrayList) xhtmlXMLElement.getChildren().get(key)).get(0)) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private AdMarvelNativeMetadata parseAndGetNativeAdMetadata(AdMarvelXMLElement adMarvelXMLElement) {
        try {
            AdMarvelNativeMetadata adMarvelNativeMetadata = new AdMarvelNativeMetadata();
            String str = (String) adMarvelXMLElement.getAttributes().get(Send.TYPE);
            String data = adMarvelXMLElement.getData();
            if (str != null) {
                adMarvelNativeMetadata.setType(str);
            }
            if (data != null) {
                adMarvelNativeMetadata.setValue(data);
            }
            return adMarvelNativeMetadata;
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, AdMarvelNativeMetadata> parseAndGetNativeAdMetadatas(AdMarvelXMLElement adMarvelXMLElement) {
        try {
            Map<String, AdMarvelNativeMetadata> hashMap = new HashMap();
            if (adMarvelXMLElement.getChildren().containsKey(Constants.NATIVE_AD_METADATA_ELEMENT)) {
                int size = ((ArrayList) adMarvelXMLElement.getChildren().get(Constants.NATIVE_AD_METADATA_ELEMENT)).size();
                for (int i = 0; i < size; i++) {
                    AdMarvelXMLElement adMarvelXMLElement2 = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement.getChildren().get(Constants.NATIVE_AD_METADATA_ELEMENT)).get(i);
                    String str = (String) adMarvelXMLElement2.getAttributes().get(Constants.NATIVE_AD_KEY_ELEMENT);
                    AdMarvelNativeMetadata parseAndGetNativeAdMetadata = parseAndGetNativeAdMetadata(adMarvelXMLElement2);
                    if (!(parseAndGetNativeAdMetadata == null || str == null)) {
                        hashMap.put(str, parseAndGetNativeAdMetadata);
                    }
                }
            }
            return hashMap;
        } catch (Exception e) {
            return null;
        }
    }

    private AdMarvelNativeNotice parseAndGetNativeAdNotice(AdMarvelXMLElement xhtmlXMLElement) {
        try {
            AdMarvelNativeNotice adMarvelNativeNotice = new AdMarvelNativeNotice();
            String parseAndGetNativeAdStandardElement = parseAndGetNativeAdStandardElement(xhtmlXMLElement, SettingsJsonConstants.PROMPT_TITLE_KEY, 0);
            if (parseAndGetNativeAdStandardElement != null) {
                adMarvelNativeNotice.setTitle(parseAndGetNativeAdStandardElement);
            }
            parseAndGetNativeAdStandardElement = parseAndGetNativeAdStandardElement(xhtmlXMLElement, Constants.NATIVE_AD_CLICK_URL_ELEMENT, 0);
            if (parseAndGetNativeAdStandardElement != null) {
                adMarvelNativeNotice.setClickUrl(parseAndGetNativeAdStandardElement);
            }
            AdMarvelNativeImage parseAndGetNativeAdImageElement = parseAndGetNativeAdImageElement(xhtmlXMLElement, Constants.NATIVE_AD_IMAGE_ELEMENT);
            if (parseAndGetNativeAdImageElement == null) {
                return adMarvelNativeNotice;
            }
            adMarvelNativeNotice.setImage(parseAndGetNativeAdImageElement);
            return adMarvelNativeNotice;
        } catch (Exception e) {
            return null;
        }
    }

    private AdMarvelNativeRating parseAndGetNativeAdRating(AdMarvelXMLElement xhtmlXMLElement) {
        try {
            AdMarvelNativeImage parseAndGetNativeAdImageElement;
            AdMarvelNativeRating adMarvelNativeRating = new AdMarvelNativeRating();
            String str = (String) xhtmlXMLElement.getAttributes().get(Constants.NATIVE_AD_VALUE_ELEMENT);
            String str2 = (String) xhtmlXMLElement.getAttributes().get(Constants.NATIVE_AD_BASE_ELEMENT);
            if (str != null) {
                adMarvelNativeRating.setValue(str);
            }
            if (str2 != null) {
                adMarvelNativeRating.setBase(str2);
            }
            if (xhtmlXMLElement.getChildren().containsKey(Constants.NATIVE_AD_COMPLETE_ELEMENT)) {
                parseAndGetNativeAdImageElement = parseAndGetNativeAdImageElement((AdMarvelXMLElement) ((ArrayList) xhtmlXMLElement.getChildren().get(Constants.NATIVE_AD_COMPLETE_ELEMENT)).get(0), Constants.NATIVE_AD_IMAGE_ELEMENT);
                if (parseAndGetNativeAdImageElement != null) {
                    adMarvelNativeRating.setComplete(parseAndGetNativeAdImageElement);
                }
            }
            if (xhtmlXMLElement.getChildren().containsKey(Constants.NATIVE_AD_HALF_ELEMENT)) {
                parseAndGetNativeAdImageElement = parseAndGetNativeAdImageElement((AdMarvelXMLElement) ((ArrayList) xhtmlXMLElement.getChildren().get(Constants.NATIVE_AD_HALF_ELEMENT)).get(0), Constants.NATIVE_AD_IMAGE_ELEMENT);
                if (parseAndGetNativeAdImageElement != null) {
                    adMarvelNativeRating.setHalf(parseAndGetNativeAdImageElement);
                }
            }
            if (xhtmlXMLElement.getChildren().containsKey(Constants.NATIVE_AD_FULL_ELEMENT)) {
                parseAndGetNativeAdImageElement = parseAndGetNativeAdImageElement((AdMarvelXMLElement) ((ArrayList) xhtmlXMLElement.getChildren().get(Constants.NATIVE_AD_FULL_ELEMENT)).get(0), Constants.NATIVE_AD_IMAGE_ELEMENT);
                if (parseAndGetNativeAdImageElement != null) {
                    adMarvelNativeRating.setFull(parseAndGetNativeAdImageElement);
                }
            }
            if (xhtmlXMLElement.getChildren().containsKey(Constants.NATIVE_AD_BLANK_ELEMENT)) {
                parseAndGetNativeAdImageElement = parseAndGetNativeAdImageElement((AdMarvelXMLElement) ((ArrayList) xhtmlXMLElement.getChildren().get(Constants.NATIVE_AD_BLANK_ELEMENT)).get(0), Constants.NATIVE_AD_IMAGE_ELEMENT);
                if (parseAndGetNativeAdImageElement != null) {
                    adMarvelNativeRating.setBlank(parseAndGetNativeAdImageElement);
                }
            }
            return adMarvelNativeRating;
        } catch (Exception e) {
            return null;
        }
    }

    private String parseAndGetNativeAdStandardElement(AdMarvelXMLElement xhtmlXMLElement, String key, int index) {
        try {
            if (xhtmlXMLElement.getChildren().containsKey(key)) {
                AdMarvelXMLElement adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) xhtmlXMLElement.getChildren().get(key)).get(index);
                if (adMarvelXMLElement != null) {
                    String data = adMarvelXMLElement.getData();
                    if (data != null && data.length() > 0) {
                        return data;
                    }
                }
            }
            return Stomp.EMPTY;
        } catch (Exception e) {
            return Stomp.EMPTY;
        }
    }

    private AdMarvelNativeTracker parseAndGetNativeAdTrackerElement(AdMarvelXMLElement adMarvelXMLElement) {
        try {
            AdMarvelNativeTracker adMarvelNativeTracker = new AdMarvelNativeTracker();
            String str = (String) adMarvelXMLElement.getAttributes().get(Send.TYPE);
            if (str != null) {
                adMarvelNativeTracker.setType(str);
            }
            adMarvelNativeTracker.setUrl(parseAndGetNativeAdTrackerUrlElement(adMarvelXMLElement));
            return adMarvelNativeTracker;
        } catch (Exception e) {
            return null;
        }
    }

    private String[] parseAndGetNativeAdTrackerUrlElement(AdMarvelXMLElement adMarvelXMLElement) {
        try {
            if (!adMarvelXMLElement.getChildren().containsKey(SettingsJsonConstants.APP_URL_KEY)) {
                return null;
            }
            int size = ((ArrayList) adMarvelXMLElement.getChildren().get(SettingsJsonConstants.APP_URL_KEY)).size();
            String[] strArr = new String[size];
            for (int i = 0; i < size; i++) {
                strArr[i] = parseAndGetNativeAdStandardElement(adMarvelXMLElement, SettingsJsonConstants.APP_URL_KEY, i);
            }
            return strArr;
        } catch (Exception e) {
            return null;
        }
    }

    private AdMarvelNativeTracker[] parseAndGetNativeAdTrackersElement(AdMarvelXMLElement adMarvelXMLElement) {
        try {
            if (!adMarvelXMLElement.getChildren().containsKey(Constants.NATIVE_AD_TRACKER_ELEMENT)) {
                return null;
            }
            int size = ((ArrayList) adMarvelXMLElement.getChildren().get(Constants.NATIVE_AD_TRACKER_ELEMENT)).size();
            AdMarvelNativeTracker[] adMarvelNativeTrackerArr = new AdMarvelNativeTracker[size];
            for (int i = 0; i < size; i++) {
                adMarvelNativeTrackerArr[i] = parseAndGetNativeAdTrackerElement((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement.getChildren().get(Constants.NATIVE_AD_TRACKER_ELEMENT)).get(i));
            }
            return adMarvelNativeTrackerArr;
        } catch (Exception e) {
            return null;
        }
    }

    private AdMarvelNativeImage[] parseAndGetNativeAdcampaignImageElement(AdMarvelXMLElement xhtmlXMLElement, String key) {
        try {
            if (!xhtmlXMLElement.getChildren().containsKey(key)) {
                return null;
            }
            int size = ((ArrayList) xhtmlXMLElement.getChildren().get(key)).size();
            AdMarvelNativeImage[] adMarvelNativeImageArr = new AdMarvelNativeImage[size];
            for (int i = 0; i < size; i++) {
                adMarvelNativeImageArr[i] = parseAndGetNativeAdImageElement((AdMarvelXMLElement) ((ArrayList) xhtmlXMLElement.getChildren().get(key)).get(i));
            }
            return adMarvelNativeImageArr;
        } catch (Exception e) {
            return null;
        }
    }

    private AdMarvelNativeVideoView parseAndGetNativeVideoAdContent(AdMarvelXMLElement admarvelXMlElement) {
        try {
            String data = ((AdMarvelXMLElement) ((ArrayList) admarvelXMlElement.getChildren().get(Constants.NATIVE_VIDEO_AD_HTML_ELEMENT)).get(0)).getData();
            if (data == null || data.length() <= 0) {
                return null;
            }
            View adMarvelView = new AdMarvelView((Context) this.contextReference.get());
            this.isAdMarvelViewCreated = true;
            adMarvelView.setTag(this.ADMARVEL_NATIVE_AD_GUID + "CURRENT_ADMVIEW_NATIVEAD");
            adMarvelView.setListener(new C02592(this));
            adMarvelView.setVideoEventListener(new C02603(this));
            adMarvelView.loadNativeVideoContent(data, this.targetParams, this.partnerId, this.siteId, this.orientation, this.deviceConnectivity);
            AdMarvelNativeVideoView adMarvelNativeVideoView = new AdMarvelNativeVideoView((Context) this.contextReference.get(), this);
            ((ArrayList) admarvelXMlElement.getChildren().get(Constants.NATIVE_VIDEO_AD_HTML_ELEMENT)).get(0);
            data = (String) admarvelXMlElement.getAttributes().get(SettingsJsonConstants.ICON_WIDTH_KEY);
            String str = (String) admarvelXMlElement.getAttributes().get(SettingsJsonConstants.ICON_HEIGHT_KEY);
            if (data != null) {
                try {
                    if (data.length() > 0) {
                        adMarvelNativeVideoView.setNativeVideoHeight(Integer.parseInt(str));
                    }
                } catch (Exception e) {
                    return null;
                }
            }
            if (str != null && str.length() > 0) {
                adMarvelNativeVideoView.setNativeVideoWidth(Integer.parseInt(data));
            }
            adMarvelView.setLayoutParams(getNativeVideoLayoutParams(adMarvelNativeVideoView, (Context) this.contextReference.get()));
            adMarvelView.requestLayout();
            adMarvelNativeVideoView.setNativeVideoView(adMarvelView);
            return adMarvelNativeVideoView;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private void redirectUrl(String url) {
        Context context = null;
        if (url != null) {
            try {
                if (url.length() > 0) {
                    if (getContextReference() != null) {
                        context = (Context) getContextReference().get();
                    }
                    new Thread(new AdMarvelRedirectRunnable(url, context, null, "native", this.ADMARVEL_NATIVE_AD_GUID, true, false, false, Stomp.EMPTY)).start();
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    private void setAdMarvelNativeAdView(AdMarvelNativeVideoView adMarvelNativeVideoView) {
        this.adMarvelNativeVideoView = adMarvelNativeVideoView;
    }

    private void setCampaignImage(AdMarvelNativeImage[] campaignImage) {
        this.campaignImage = campaignImage;
    }

    private void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    private void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    private void setIcon(AdMarvelNativeImage icon) {
        this.icon = icon;
    }

    private void setNativeAdFields(String key, Object value) {
        if (key != null && value != null) {
            if (key.equals(FIELD_DISPLAYNAME) && (value instanceof String)) {
                setDisplayName((String) value);
            } else if (key != null && key.equals(FIELD_FULLMESSAGE) && value != null && (value instanceof String)) {
                setFullMessage((String) value);
            } else if (key != null && key.equals(FIELD_SHORTMESSAGE) && value != null && (value instanceof String)) {
                setShortMessage((String) value);
            } else if (key != null && key.equals(FIELD_ICON) && value != null && (value instanceof AdMarvelNativeImage)) {
                setIcon((AdMarvelNativeImage) value);
            } else if (key != null && key.equals(FIELD_CAMPAIGNIMAGE) && ((value instanceof AdMarvelNativeImage[]) || (value instanceof Object[]))) {
                if (value instanceof Object[]) {
                    AdMarvelNativeImage[] adMarvelNativeImageArr = new AdMarvelNativeImage[((Object[]) value).length];
                    for (int i = 0; i < ((Object[]) value).length; i++) {
                        adMarvelNativeImageArr[i] = (AdMarvelNativeImage) ((Object[]) value)[i];
                    }
                    setCampaignImage(adMarvelNativeImageArr);
                    return;
                }
                setCampaignImage((AdMarvelNativeImage[]) value);
            } else if (key != null && key.equals(FIELD_RATING) && (value instanceof AdMarvelNativeRating)) {
                setRating((AdMarvelNativeRating) value);
            } else if (key != null && key.equals(FIELD_CTA) && (value instanceof AdMarvelNativeCta)) {
                setCta((AdMarvelNativeCta) value);
            } else if (key != null && key.equals(FIELD_NOTICE) && (value instanceof AdMarvelNativeNotice)) {
                setNotice((AdMarvelNativeNotice) value);
            } else if (key != null && key.equals(Constants.NATIVE_AD_METADATAS_ELEMENT) && (value instanceof Map)) {
                Map hashMap = new HashMap();
                for (String str : ((Map) value).keySet()) {
                    hashMap.put(str, (AdMarvelNativeMetadata) ((Map) value).get(str));
                }
                setMetadatas(hashMap);
            } else if (key != null && key.equals(FIELD_NATIVE_VIDEO_AD_VIEW) && (value instanceof AdMarvelNativeVideoView)) {
                setAdMarvelNativeAdView((AdMarvelNativeVideoView) value);
            }
        }
    }

    private void setNotice(AdMarvelNativeNotice notice) {
        this.notice = notice;
    }

    private void setRating(AdMarvelNativeRating rating) {
        this.rating = rating;
    }

    private void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public boolean checkForBlockedAd() {
        Context context = (Context) this.contextReference.get();
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(AdMarvelUtils.encodeString("admarvel"), 0);
            String str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            String str2 = str != null ? "duration" + str + i + AdMarvelUtils.getSDKVersion() : "duration" + i + AdMarvelUtils.getSDKVersion();
            if (str2 != null) {
                str2 = sharedPreferences.getString(AdMarvelUtils.encodeString(str2), null);
                if (str2 != null && str2.length() > 0) {
                    if (DateFormat.getDateTimeInstance().parse(DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()))).before(DateFormat.getDateTimeInstance().parse(str2))) {
                        Logging.log("requestNewAd: AD REQUEST BLOCKED, IGNORING REQUEST");
                        this.listenerImpl.m405a(this, AdMArvelErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION.getErrorCode(), AdMArvelErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION);
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Deprecated
    public void destroy() {
    }

    protected void disableAdRequest(String duration) {
        if (this.mContext != null) {
            String str;
            try {
                str = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionName;
                int i = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionCode;
                str = str != null ? "duration" + str + i + AdMarvelUtils.getSDKVersion() : "duration" + i + AdMarvelUtils.getSDKVersion();
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                str = null;
            }
            if (str != null) {
                Editor edit = this.mContext.getSharedPreferences(AdMarvelUtils.encodeString("admarvel"), 0).edit();
                edit.putString(AdMarvelUtils.encodeString(str), DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis() + ((long) (Integer.parseInt(duration) * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH)))));
                edit.commit();
                Logging.log("requestNewAd: AD REQUEST BLOCKED, IGNORING REQUEST");
                if (this.listenerImpl != null) {
                    this.listenerImpl.m405a(this, AdMArvelErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION.getErrorCode(), AdMArvelErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION);
                }
            }
        }
    }

    public long getAdColonyDelayAfterInitInMs() {
        return this.adColonyDelayAfterInitInMs;
    }

    public String getAdColonyMuted() {
        return this.adColonyMuted;
    }

    public String getAdColonyVolume() {
        return this.adColonyVolume;
    }

    public String getAdId() {
        return this.adId;
    }

    public AdMarvelNativeVideoView getAdMarvelNativeVideoView() {
        return this.adMarvelNativeVideoView != null ? this.adMarvelNativeVideoView.getAdMarvelNativeVideoView() : null;
    }

    public AdMarvelNetworkHandler getAdMarvelNetworkHandler() {
        return this.adMarvelNetworkHandler;
    }

    public String getAdSponsoredMarker() {
        return this.adSponsoredMarker;
    }

    C0263a getAdType() {
        return this.adType;
    }

    public String getAdcolonyAppId() {
        return this.adcolonyAppId;
    }

    public String getAdcolonyAppVersion() {
        return this.adcolonyAppVersion;
    }

    public String getAdcolonyTargetZoneid() {
        return this.adcolonyTargetZoneid;
    }

    public String getAdcolonyZoneId() {
        return this.adcolonyZoneId;
    }

    public AdMarvelNativeImage[] getCampaignImage() {
        return this.campaignImage;
    }

    public String getChartboostAppID() {
        return this.chartboostAppID;
    }

    public String getChartboostAppSignature() {
        return this.chartboostAppSignature;
    }

    public String getChartboostLocation() {
        return this.chartboostLocation;
    }

    public WeakReference<Context> getContextReference() {
        return this.contextReference;
    }

    public AdMarvelNativeCta getCta() {
        return this.cta;
    }

    public String getDeviceConnectivity() {
        return this.deviceConnectivity;
    }

    public String getDisableAdDuration() {
        return this.disableAdDuration;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorReason() {
        return this.errorReason;
    }

    public String getExcluded() {
        return this.excluded;
    }

    public String getFacebookChildDirectedFlag() {
        return this.facebookChildDirectedFlag;
    }

    public String[] getFacebookTestDeviceId() {
        return this.facebookTestDeviceId;
    }

    public String getFullMessage() {
        return this.fullMessage;
    }

    public AdMarvelNativeImage getIcon() {
        return this.icon;
    }

    public int getId() {
        return this.id;
    }

    public String getInmobiAppId() {
        return this.inmobiAppId;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public AdMarvelNativeAdListenerImpl getListener() {
        return this.listenerImpl;
    }

    public int getMaxretries() {
        return this.maxretries;
    }

    public Map<String, AdMarvelNativeMetadata> getMetadatas() {
        return this.metadatas;
    }

    public AdMarvelNativeAdType getNativeAdType() {
        return this.admarvelNativeAdType;
    }

    public float getNativeVideoViewHeight() {
        return calculateHeightFromWidth(this.nativeVideoWidth, this.adMarvelNativeVideoView);
    }

    public float getNativeVideoViewWidth() {
        return this.nativeVideoWidth;
    }

    public AdMarvelNativeNotice getNotice() {
        return this.notice;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public String getPartnerId() {
        return this.partnerId;
    }

    public List<String> getPixels() {
        return this.pixels;
    }

    public String getPubId() {
        return this.pubId;
    }

    public AdMarvelNativeRating getRating() {
        return this.rating;
    }

    public String getRequestJson() {
        return this.requestJson;
    }

    public String getResponseJson() {
        return this.responseJson;
    }

    public Boolean getRetry() {
        return this.retry;
    }

    public int getRetrynum() {
        return this.retrynum;
    }

    public SDKAdNetwork getSdkAdNetwork() {
        return this.sdkAdNetwork;
    }

    public String getSdkNetwork() {
        return this.sdkNetwork;
    }

    public String getSerializedString() {
        return this.nativeAdXml;
    }

    public String getShortMessage() {
        return this.shortMessage;
    }

    public String getSiteId() {
        return this.siteId;
    }

    public String getSource() {
        return this.source;
    }

    public Map<String, Object> getTargetParams() {
        return this.targetParams;
    }

    public AdMarvelNativeTracker[] getTrackers() {
        return this.trackers;
    }

    public String getXml() {
        return this.xml;
    }

    public Context getmContext() {
        return this.mContext;
    }

    public boolean isDisableAdrequest() {
        return this.disableAdRequest;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    com.admarvel.android.ads.AdMarvelXMLReader loadAd(java.lang.String r11) {
        /*
        r10 = this;
        r3 = 0;
        r9 = 1;
        r6 = 0;
        if (r11 != 0) goto L_0x0007;
    L_0x0005:
        r0 = r3;
    L_0x0006:
        return r0;
    L_0x0007:
        r4 = new com.admarvel.android.ads.AdMarvelXMLReader;
        r4.<init>();
        r4.parseXMLString(r11);
        r2 = r4.getParsedXMLData();
        if (r2 != 0) goto L_0x0017;
    L_0x0015:
        r0 = r3;
        goto L_0x0006;
    L_0x0017:
        r10.pixels = r3;
        r10.trackers = r3;
        r0 = r2.getName();
        r1 = "ad";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0150;
    L_0x0027:
        r0 = r2.getAttributes();
        r1 = "id";
        r0 = r0.get(r1);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x0043;
    L_0x0035:
        r1 = r0.length();
        if (r1 <= 0) goto L_0x0043;
    L_0x003b:
        r1 = java.lang.Integer.parseInt(r0);
        r10.id = r1;
        r10.adId = r0;
    L_0x0043:
        r0 = r2.getAttributes();
        r1 = "ip";
        r0 = r0.get(r1);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x0059;
    L_0x0051:
        r1 = r0.length();
        if (r1 <= 0) goto L_0x0059;
    L_0x0057:
        r10.ipAddress = r0;
    L_0x0059:
        r0 = r2.getAttributes();
        r1 = "type";
        r0 = r0.get(r1);
        r0 = (java.lang.String) r0;
        r1 = "error";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x0134;
    L_0x006d:
        r0 = com.admarvel.android.ads.nativeads.AdMarvelNativeAd.C0263a.ERROR;
        r10.adType = r0;
    L_0x0071:
        r0 = r2.getAttributes();
        r1 = "source";
        r0 = r0.get(r1);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x0087;
    L_0x007f:
        r1 = r0.length();
        if (r1 <= 0) goto L_0x0087;
    L_0x0085:
        r10.source = r0;
    L_0x0087:
        r0 = r2.getAttributes();
        r1 = "adi";
        r0 = r0.get(r1);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x00a5;
    L_0x0095:
        r1 = r0.length();
        if (r1 <= 0) goto L_0x00a5;
    L_0x009b:
        r1 = "1";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x00a5;
    L_0x00a3:
        r10.isFiringImpresstionTrackerMultipleTimeAllowed = r9;
    L_0x00a5:
        r0 = r2.getAttributes();
        r1 = "adc";
        r0 = r0.get(r1);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x00c3;
    L_0x00b3:
        r1 = r0.length();
        if (r1 <= 0) goto L_0x00c3;
    L_0x00b9:
        r1 = "1";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x00c3;
    L_0x00c1:
        r10.isFiringClickTrackerMultipleTimeAllowed = r9;
    L_0x00c3:
        r0 = r10.adType;
        r1 = com.admarvel.android.ads.nativeads.AdMarvelNativeAd.C0263a.NATIVE;
        if (r0 != r1) goto L_0x00cb;
    L_0x00c9:
        r10.nativeAdXml = r11;
    L_0x00cb:
        r0 = r2.getChildren();
        r1 = "pixels";
        r0 = r0.containsKey(r1);
        if (r0 == 0) goto L_0x0156;
    L_0x00d7:
        r0 = r2.getChildren();
        r1 = "pixels";
        r0 = r0.get(r1);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        r1 = r0.getChildren();
        r5 = "pixel";
        r1 = r1.containsKey(r5);
        if (r1 == 0) goto L_0x0156;
    L_0x00f5:
        r1 = r0.getChildren();
        r5 = "pixel";
        r1 = r1.get(r5);
        r1 = (java.util.ArrayList) r1;
        r7 = r1.size();
        r5 = r6;
    L_0x0106:
        if (r5 >= r7) goto L_0x0156;
    L_0x0108:
        r1 = r0.getChildren();
        r8 = "pixel";
        r1 = r1.get(r8);
        r1 = (java.util.ArrayList) r1;
        r1 = r1.get(r5);
        r1 = (com.admarvel.android.ads.AdMarvelXMLElement) r1;
        if (r1 == 0) goto L_0x0130;
    L_0x011c:
        r1 = r1.getData();
        r8 = r10.pixels;
        if (r8 != 0) goto L_0x012b;
    L_0x0124:
        r8 = new java.util.ArrayList;
        r8.<init>();
        r10.pixels = r8;
    L_0x012b:
        r8 = r10.pixels;
        r8.add(r1);
    L_0x0130:
        r1 = r5 + 1;
        r5 = r1;
        goto L_0x0106;
    L_0x0134:
        r1 = "sdkcall";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x0142;
    L_0x013c:
        r0 = com.admarvel.android.ads.nativeads.AdMarvelNativeAd.C0263a.SDKCALL;
        r10.adType = r0;
        goto L_0x0071;
    L_0x0142:
        r1 = "native";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0071;
    L_0x014a:
        r0 = com.admarvel.android.ads.nativeads.AdMarvelNativeAd.C0263a.NATIVE;
        r10.adType = r0;
        goto L_0x0071;
    L_0x0150:
        r0 = com.admarvel.android.ads.nativeads.AdMarvelNativeAd.C0263a.ERROR;
        r10.adType = r0;
        goto L_0x00cb;
    L_0x0156:
        r0 = r10.adType;
        r1 = com.admarvel.android.ads.nativeads.AdMarvelNativeAd.C0263a.SDKCALL;
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0586;
    L_0x0160:
        r0 = r2.getChildren();
        r1 = "xhtml";
        r0 = r0.get(r1);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        if (r0 == 0) goto L_0x0586;
    L_0x0174:
        r0 = r0.getData();
        r1 = new com.admarvel.android.util.j;
        r1.<init>();
        r0 = r1.m604a(r0);
        r4.parseXMLString(r0);
        r1 = r4.getParsedXMLData();
        r0 = r1.getAttributes();
        r2 = "network";
        r0 = r0.get(r2);
        r0 = (java.lang.String) r0;
        r2 = "facebook";
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x04cd;
    L_0x019c:
        r0 = "com.admarvel.android.admarvelfacebookadapter.AdMarvelFacebookAdapter";
        r10.sdkNetwork = r0;
        r0 = com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork.FACEBOOK;
        r10.sdkAdNetwork = r0;
    L_0x01a4:
        r0 = r1.getAttributes();
        r2 = "retry";
        r0 = r0.get(r2);
        r0 = (java.lang.String) r0;
        r2 = "YES";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0567;
    L_0x01b8:
        r0 = java.lang.Boolean.valueOf(r9);
        r10.retry = r0;
    L_0x01be:
        r0 = r1.getAttributes();
        r2 = "retrynum";
        r0 = r0.get(r2);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x01d8;
    L_0x01cc:
        r2 = r0.length();
        if (r2 <= 0) goto L_0x01d8;
    L_0x01d2:
        r0 = java.lang.Integer.parseInt(r0);
        r10.retrynum = r0;
    L_0x01d8:
        r0 = r1.getAttributes();
        r2 = "excluded";
        r0 = r0.get(r2);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x01ee;
    L_0x01e6:
        r2 = r0.length();
        if (r2 <= 0) goto L_0x01ee;
    L_0x01ec:
        r10.excluded = r0;
    L_0x01ee:
        r0 = r1.getAttributes();
        r2 = "maxretries";
        r0 = r0.get(r2);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x056f;
    L_0x01fc:
        r2 = r0.length();
        if (r2 <= 0) goto L_0x056f;
    L_0x0202:
        r0 = java.lang.Integer.parseInt(r0);
        r10.maxretries = r0;
    L_0x0208:
        r0 = r1.getAttributes();
        r2 = "cachedadtracker";
        r0 = r0.get(r2);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x02c4;
    L_0x0216:
        r2 = r0.length();
        if (r2 <= 0) goto L_0x02c4;
    L_0x021c:
        r2 = r10.lastRequestPostString;
        if (r2 == 0) goto L_0x0573;
    L_0x0220:
        r2 = r10.lastRequestPostString;
        r2 = r2.length();
        if (r2 <= 0) goto L_0x0573;
    L_0x0228:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r2.append(r0);
        r2 = "&rp=";
        r0 = r0.append(r2);
        r2 = r10.lastRequestPostString;
        r0 = r0.append(r2);
        r0 = r0.toString();
        r10.cachedAdOfflineTracker = r0;
    L_0x0243:
        r0 = r1.getAttributes();
        r2 = "ttl";
        r0 = r0.get(r2);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x02c4;
    L_0x0251:
        r2 = r0.length();
        if (r2 <= 0) goto L_0x02c4;
    L_0x0257:
        r2 = r10.getmContext();
        r5 = "adm_assets";
        r2 = r2.getDir(r5, r6);
        if (r2 == 0) goto L_0x02c4;
    L_0x0263:
        r5 = r2.isDirectory();
        if (r5 == 0) goto L_0x02c4;
    L_0x0269:
        r5 = new java.io.File;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r2 = r2.getAbsolutePath();
        r2 = r7.append(r2);
        r7 = "/cachedad.txt";
        r2 = r2.append(r7);
        r2 = r2.toString();
        r5.<init>(r2);
        if (r5 == 0) goto L_0x02c4;
    L_0x0287:
        r2 = r5.exists();
        if (r2 != 0) goto L_0x02c4;
    L_0x028d:
        r2 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0577, all -> 0x057c }
        r2.<init>(r5);	 Catch:{ Exception -> 0x0577, all -> 0x057c }
        r3 = r11.getBytes();	 Catch:{ Exception -> 0x0583, all -> 0x0581 }
        r2.write(r3);	 Catch:{ Exception -> 0x0583, all -> 0x0581 }
        r2.flush();	 Catch:{ Exception -> 0x0583, all -> 0x0581 }
        r2.close();	 Catch:{ Exception -> 0x0583, all -> 0x0581 }
        if (r2 == 0) goto L_0x02a1;
    L_0x02a1:
        r2 = r10.getmContext();
        r3 = "adm_viewport";
        r5 = "adm_cachedad_timestamp";
        r7 = java.util.Calendar.getInstance();
        r8 = r7.getTimeInMillis();
        com.admarvel.android.ads.AdMarvelUtils.setPreferenceValueLong(r2, r3, r5, r8);
        r2 = r10.getmContext();
        r3 = "adm_viewport";
        r5 = "adm_cachedad_ttl";
        r0 = java.lang.Integer.parseInt(r0);
        r8 = (long) r0;
        com.admarvel.android.ads.AdMarvelUtils.setPreferenceValueLong(r2, r3, r5, r8);
    L_0x02c4:
        r0 = r1.getChildren();
        r2 = "errorCode";
        r0 = r0.containsKey(r2);
        if (r0 == 0) goto L_0x02ee;
    L_0x02d0:
        r0 = r1.getChildren();
        r2 = "errorCode";
        r0 = r0.get(r2);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        if (r0 == 0) goto L_0x02ee;
    L_0x02e4:
        r0 = r0.getData();
        r0 = java.lang.Integer.parseInt(r0);
        r10.errorCode = r0;
    L_0x02ee:
        r0 = r1.getChildren();
        r2 = "errorReason";
        r0 = r0.containsKey(r2);
        if (r0 == 0) goto L_0x0314;
    L_0x02fa:
        r0 = r1.getChildren();
        r2 = "errorReason";
        r0 = r0.get(r2);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        if (r0 == 0) goto L_0x0314;
    L_0x030e:
        r0 = r0.getData();
        r10.errorReason = r0;
    L_0x0314:
        r0 = r10.adType;
        r2 = com.admarvel.android.ads.nativeads.AdMarvelNativeAd.C0263a.NATIVE;
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x04ca;
    L_0x031e:
        if (r1 == 0) goto L_0x04ca;
    L_0x0320:
        r0 = "displayName";
        r0 = r10.parseAndGetNativeAdStandardElement(r1, r0, r6);
        r10.displayName = r0;
        r0 = "shortMessage";
        r0 = r10.parseAndGetNativeAdStandardElement(r1, r0, r6);
        r10.shortMessage = r0;
        r0 = "fullMessage";
        r0 = r10.parseAndGetNativeAdStandardElement(r1, r0, r6);
        r10.fullMessage = r0;
        r0 = "adSponsoredMarker";
        r0 = r10.parseAndGetNativeAdStandardElement(r1, r0, r6);
        r10.adSponsoredMarker = r0;
        r0 = r1.getChildren();
        r2 = "icon";
        r0 = r0.get(r2);
        if (r0 == 0) goto L_0x0366;
    L_0x034c:
        r0 = r1.getChildren();
        r2 = "icon";
        r0 = r0.get(r2);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        r2 = "image";
        r0 = r10.parseAndGetNativeAdImageElement(r0, r2);
        r10.icon = r0;
    L_0x0366:
        r0 = r1.getChildren();
        r2 = "campaignImage";
        r0 = r0.get(r2);
        if (r0 == 0) goto L_0x038c;
    L_0x0372:
        r0 = r1.getChildren();
        r2 = "campaignImage";
        r0 = r0.get(r2);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        r2 = "image";
        r0 = r10.parseAndGetNativeAdcampaignImageElement(r0, r2);
        r10.campaignImage = r0;
    L_0x038c:
        r0 = r1.getChildren();
        r2 = "cta";
        r0 = r0.get(r2);
        if (r0 == 0) goto L_0x03b0;
    L_0x0398:
        r0 = r1.getChildren();
        r2 = "cta";
        r0 = r0.get(r2);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        r0 = r10.parseAndGetNativeAdCTA(r0);
        r10.cta = r0;
    L_0x03b0:
        r0 = r1.getChildren();
        r2 = "notice";
        r0 = r0.get(r2);
        if (r0 == 0) goto L_0x03d4;
    L_0x03bc:
        r0 = r1.getChildren();
        r2 = "notice";
        r0 = r0.get(r2);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        r0 = r10.parseAndGetNativeAdNotice(r0);
        r10.notice = r0;
    L_0x03d4:
        r0 = r1.getChildren();
        r2 = "metadatas";
        r0 = r0.get(r2);
        if (r0 == 0) goto L_0x045c;
    L_0x03e0:
        r0 = r1.getChildren();
        r2 = "metadatas";
        r0 = r0.get(r2);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        r0 = r10.parseAndGetNativeAdMetadatas(r0);
        r10.metadatas = r0;
        r0 = r10.metadatas;
        r2 = "rating";
        r0 = r0.containsKey(r2);
        if (r0 == 0) goto L_0x045c;
    L_0x0402:
        r0 = r10.metadatas;
        r2 = "rating";
        r0 = r0.get(r2);
        r0 = (com.admarvel.android.ads.nativeads.AdMarvelNativeMetadata) r0;
        r2 = r0.getType();
        if (r2 == 0) goto L_0x0438;
    L_0x0412:
        r2 = r0.getType();
        r3 = "xml";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0438;
    L_0x041e:
        r0 = r0.getValue();
        if (r0 == 0) goto L_0x0438;
    L_0x0424:
        r2 = new com.admarvel.android.ads.AdMarvelXMLReader;
        r2.<init>();
        r2.parseXMLString(r0);
        r0 = r2.getParsedXMLData();
        if (r0 == 0) goto L_0x0438;
    L_0x0432:
        r0 = r10.parseAndGetNativeAdRating(r0);
        r10.rating = r0;
    L_0x0438:
        r0 = r1.getChildren();
        r2 = "rating";
        r0 = r0.get(r2);
        if (r0 == 0) goto L_0x045c;
    L_0x0444:
        r0 = r1.getChildren();
        r2 = "rating";
        r0 = r0.get(r2);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        r0 = r10.parseAndGetNativeAdRating(r0);
        r10.rating = r0;
    L_0x045c:
        r0 = r1.getChildren();
        r2 = "trackers";
        r0 = r0.get(r2);
        if (r0 == 0) goto L_0x0480;
    L_0x0468:
        r0 = r1.getChildren();
        r2 = "trackers";
        r0 = r0.get(r2);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        r0 = r10.parseAndGetNativeAdTrackersElement(r0);
        r10.trackers = r0;
    L_0x0480:
        r0 = r1.getChildren();
        r2 = "videoContent";
        r0 = r0.get(r2);
        if (r0 == 0) goto L_0x04ca;
    L_0x048c:
        r0 = r1.getChildren();
        r2 = "videoContent";
        r0 = r0.containsKey(r2);
        if (r0 == 0) goto L_0x04ca;
    L_0x0498:
        r0 = r1.getChildren();
        r1 = "videoContent";
        r0 = r0.get(r1);
        r0 = (java.util.ArrayList) r0;
        r0 = r0.get(r6);
        r0 = (com.admarvel.android.ads.AdMarvelXMLElement) r0;
        if (r0 == 0) goto L_0x04ca;
    L_0x04ac:
        r1 = r0.getChildren();
        r2 = "html";
        r1 = r1.containsKey(r2);
        if (r1 == 0) goto L_0x04ca;
    L_0x04b8:
        r1 = r10.contextReference;
        if (r1 == 0) goto L_0x04ca;
    L_0x04bc:
        r1 = r10.contextReference;
        r1 = r1.get();
        if (r1 == 0) goto L_0x04ca;
    L_0x04c4:
        r0 = r10.parseAndGetNativeVideoAdContent(r0);
        r10.adMarvelNativeVideoView = r0;
    L_0x04ca:
        r0 = r4;
        goto L_0x0006;
    L_0x04cd:
        r2 = "heyzap";
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x04df;
    L_0x04d5:
        r0 = "com.admarvel.android.admarvelheyzapadapter.AdMarvelHeyzapAdapter";
        r10.sdkNetwork = r0;
        r0 = com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork.HEYZAP;
        r10.sdkAdNetwork = r0;
        goto L_0x01a4;
    L_0x04df:
        r2 = "inmobi";
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x04f1;
    L_0x04e7:
        r0 = "com.admarvel.android.admarvelinmobiadapter.AdMarvelInmobiAdapter";
        r10.sdkNetwork = r0;
        r0 = com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork.INMOBI;
        r10.sdkAdNetwork = r0;
        goto L_0x01a4;
    L_0x04f1:
        r2 = "admob";
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x0503;
    L_0x04f9:
        r0 = "com.admarvel.android.admarvelgoogleplayadapter.AdMarvelGooglePlayAdapter";
        r10.sdkNetwork = r0;
        r0 = com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork.GOOGLEPLAY;
        r10.sdkAdNetwork = r0;
        goto L_0x01a4;
    L_0x0503:
        r2 = "adcolony";
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x0515;
    L_0x050b:
        r0 = "com.admarvel.android.admarveladcolonyadapter.AdMarvelAdColonyAdapter";
        r10.sdkNetwork = r0;
        r0 = com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork.ADCOLONY;
        r10.sdkAdNetwork = r0;
        goto L_0x01a4;
    L_0x0515:
        r2 = "chartboost";
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x0527;
    L_0x051d:
        r0 = "com.admarvel.android.admarvelchartboostadapter.AdMarvelChartboostAdapter";
        r10.sdkNetwork = r0;
        r0 = com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork.CHARTBOOST;
        r10.sdkAdNetwork = r0;
        goto L_0x01a4;
    L_0x0527:
        r2 = "millennial";
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x0539;
    L_0x052f:
        r0 = "com.admarvel.android.admarvelmillennialadapter.AdMarvelMillennialAdapter";
        r10.sdkNetwork = r0;
        r0 = com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork.MILLENNIAL;
        r10.sdkAdNetwork = r0;
        goto L_0x01a4;
    L_0x0539:
        r2 = "disable_ad_request";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0555;
    L_0x0541:
        r0 = r1.getAttributes();
        r2 = "durationinseconds";
        r0 = r0.get(r2);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x01a4;
    L_0x054f:
        r10.disableAdRequest = r9;
        r10.disableAdDuration = r0;
        goto L_0x01a4;
    L_0x0555:
        r0 = com.admarvel.android.ads.nativeads.AdMarvelNativeAd.C0263a.ERROR;
        r10.adType = r0;
        r0 = com.admarvel.android.ads.AdMarvelUtils.AdMArvelErrorReason.AD_REQUEST_SDK_TYPE_UNSUPPORTED;
        r0 = r0.getErrorCode();
        r10.errorCode = r0;
        r0 = "Missing SDK ad network";
        r10.errorReason = r0;
        goto L_0x01a4;
    L_0x0567:
        r0 = java.lang.Boolean.valueOf(r6);
        r10.retry = r0;
        goto L_0x01be;
    L_0x056f:
        r10.maxretries = r9;
        goto L_0x0208;
    L_0x0573:
        r10.cachedAdOfflineTracker = r0;
        goto L_0x0243;
    L_0x0577:
        r2 = move-exception;
    L_0x0578:
        if (r3 == 0) goto L_0x02a1;
    L_0x057a:
        goto L_0x02a1;
    L_0x057c:
        r0 = move-exception;
        r2 = r3;
    L_0x057e:
        if (r2 == 0) goto L_0x0580;
    L_0x0580:
        throw r0;
    L_0x0581:
        r0 = move-exception;
        goto L_0x057e;
    L_0x0583:
        r3 = move-exception;
        r3 = r2;
        goto L_0x0578;
    L_0x0586:
        r1 = r2;
        goto L_0x02c4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.admarvel.android.ads.nativeads.AdMarvelNativeAd.loadAd(java.lang.String):com.admarvel.android.ads.AdMarvelXMLReader");
    }

    public void loadNativeAdFromSerializedString(Context context, String xml) {
        this.contextReference = new WeakReference(context);
        this.mContext = context;
        if (xml != null) {
            try {
                if (loadAd(xml) != null && this.adType != C0263a.ERROR) {
                    Logging.log("Successfully loaded native ad from serialized string");
                    return;
                }
                return;
            } catch (Exception e) {
                this.adType = C0263a.ERROR;
                Logging.log("Failed loading native ad from serialized string");
                return;
            }
        }
        this.adType = C0263a.ERROR;
        Logging.log("Failed loading native ad from serialized string");
    }

    public void loadNativeAdThroghAdMarvelView(RequestParameters requestParameters, String xml) {
        this.isAdMarvelViewNativeAdXML = true;
        this.adMarvelViewNativeAdXML = xml;
        requestNativeAd(requestParameters);
    }

    public void nativeAdCleanup() {
        if (this.contextReference != null) {
            this.contextReference.clear();
        }
        if (this.targetParams != null) {
            this.targetParams.clear();
        }
        this.partnerId = null;
        this.siteId = null;
        this.deviceConnectivity = null;
        this.pixels = null;
        this.id = 0;
        this.ipAddress = null;
        this.source = null;
        this.disableAdRequest = false;
        this.disableAdDuration = null;
        this.retrynum = -1;
        this.displayName = null;
        this.shortMessage = null;
        this.fullMessage = null;
        this.adSponsoredMarker = null;
        this.icon = null;
        this.campaignImage = null;
        this.cta = null;
        this.metadatas = null;
        this.rating = null;
        this.trackers = null;
        this.adMarvelNativeVideoView = null;
    }

    @Deprecated
    public void pause(Activity activity) {
    }

    public void registerClickableViews(View[] clickableViews, String event) {
        if (getAdType() != C0263a.SDKCALL || (getSdkAdNetwork() != SDKAdNetwork.FACEBOOK && getSdkAdNetwork() != SDKAdNetwork.GOOGLEPLAY)) {
            if (this.mAdMarvelClickDetector == null) {
                this.mAdMarvelClickDetector = new AdMarvelClickDetector(this.internalAdMarvelClickDetectorListener);
            }
            if (!(this.mAdMarvelVisibilityDetector == null || this.mAdMarvelVisibilityDetector.m416a() == null)) {
                this.mAdMarvelClickDetector.m398a(this.mAdMarvelVisibilityDetector.m416a());
            }
            if (event == null) {
                return;
            }
            if (event.trim().equalsIgnoreCase(ADMARVEL_HANDLE_CLICK_EVENT) || event.trim().equalsIgnoreCase(ADMARVEL_HANDLE_NOTICE_CLICK_EVENT)) {
                this.mAdMarvelClickDetector.m399a(clickableViews, event);
            }
        } else if (event.trim().equalsIgnoreCase(ADMARVEL_HANDLE_CLICK_EVENT)) {
            AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(this.ADMARVEL_NATIVE_AD_GUID, getSdkNetwork());
            if (instance != null) {
                instance.registerViewForInteraction(clickableViews);
            }
        }
    }

    public void registerContainerView(View view) {
        if (this.isRegisteringContainerViewFirstTime) {
            this.isRegisteringContainerViewFirstTime = false;
            if (getAdType() == C0263a.SDKCALL) {
                unregisterView();
                AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(this.ADMARVEL_NATIVE_AD_GUID, getSdkNetwork());
                if (getSdkAdNetwork() == SDKAdNetwork.GOOGLEPLAY) {
                    if (!(view == null || instance == null)) {
                        try {
                            if (view instanceof NativeContentAdView) {
                                instance.registerViewForInteraction(view);
                            } else if (view instanceof NativeAppInstallAdView) {
                                instance.registerViewForInteraction(view);
                            }
                        } catch (Throwable e) {
                            Logging.log(Log.getStackTraceString(e));
                        }
                    }
                } else if (instance != null) {
                    instance.registerViewForInteraction(view);
                }
            }
            if (this.mAdMarvelVisibilityDetector == null) {
                this.mAdMarvelVisibilityDetector = new AdMarvelVisibilityDetector(this.internalAdMarvelVisibilityDetectorListener, (Context) this.contextReference.get());
            }
            this.mAdMarvelVisibilityDetector.m417a(view);
            if (this.mAdMarvelClickDetector == null) {
                this.mAdMarvelClickDetector = new AdMarvelClickDetector(this.internalAdMarvelClickDetectorListener);
            }
            this.mAdMarvelClickDetector.m399a(new View[]{view}, ADMARVEL_HANDLE_CLICK_EVENT);
        }
    }

    public synchronized void removeNonStringEntriesTargetParam() {
        Map concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.putAll(this.targetParams);
        try {
            Map concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.putAll(this.targetParams);
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
            this.targetParams.clear();
            this.targetParams.putAll(concurrentHashMap2);
        } catch (Exception e) {
            this.targetParams.clear();
            this.targetParams.putAll(concurrentHashMap);
            e.printStackTrace();
        }
    }

    public void requestNativeAd(RequestParameters requestParameters) {
        Logging.log("requestNativeAd");
        this.isRegisteringContainerViewFirstTime = true;
        this.isFiringImpressionTrackerFirstTime = true;
        this.contextReference = new WeakReference(requestParameters.mContext);
        this.partnerId = requestParameters.mPartnerId.trim();
        this.siteId = requestParameters.mSiteId.trim();
        this.mContext = (Context) this.contextReference.get();
        this.targetParams = requestParameters.mTargetParams;
        if (this.mContext != null) {
            this.orientation = AdMarvelUtils.getScreenOrientation(this.mContext);
            this.deviceConnectivity = AdMarvelUtils.getDeviceConnectivitiy(this.mContext);
            try {
                if (checkForBlockedAd()) {
                    if (this.listenerImpl != null) {
                        this.listenerImpl.m405a(this, AdMArvelErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION.getErrorCode(), AdMArvelErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION);
                    }
                } else if (System.currentTimeMillis() - this.lockTimestamp.getAndSet(System.currentTimeMillis()) > 2000) {
                    this.listenerImpl.m404a(this);
                    AsyncTaskExecutor.m418a(new AdMarvelNativeAsyncTask(), requestParameters, this, Integer.valueOf(0), Stomp.EMPTY, null, Boolean.valueOf(false));
                } else {
                    Logging.log("requestNewAd: AD REQUEST PENDING, IGNORING REQUEST");
                    this.listenerImpl.m405a(this, AdMArvelErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION.getErrorCode(), AdMArvelErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION);
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                AdMarvelNativeAdListenerImpl listener = getListener();
                if (listener != null) {
                    listener.m405a(this, AdMArvelErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION.getErrorCode(), AdMArvelErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION);
                }
            }
        }
    }

    protected void requestPendingAd() {
        AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(this.ADMARVEL_NATIVE_AD_GUID, getSdkNetwork());
        if (instance != null) {
            instance.requestNativeAd(this.internalAdMarvelAdapterListener, this);
        }
    }

    @Deprecated
    public void resume(Activity activity) {
    }

    public void setAdColonyDelayAfterInitInMs(Long adColonyDelayAfterInitInMs) {
        this.adColonyDelayAfterInitInMs = adColonyDelayAfterInitInMs.longValue();
    }

    public void setAdColonyMuted(String muted) {
        this.adColonyMuted = muted;
    }

    public void setAdColonyVolume(String volume) {
        this.adColonyVolume = volume;
    }

    public void setAdMarvelNativeVideoAdListener(AdMarvelNativeVideoAdListener listener) {
        this.listenerImpl.m403a(listener);
    }

    public void setAdMarvelNetworkHandler(AdMarvelNetworkHandler adMarvelNetworkHandler) {
        this.adMarvelNetworkHandler = adMarvelNetworkHandler;
    }

    void setAdType(C0263a adType) {
        this.adType = adType;
    }

    public void setAdcolonyAppId(String adcolonyAppId) {
        this.adcolonyAppId = adcolonyAppId;
    }

    public void setAdcolonyAppVersion(String adcolonyAppVersion) {
        this.adcolonyAppVersion = adcolonyAppVersion;
    }

    public void setAdcolonyTargetZoneid(String adcolonyTargetZoneid) {
        this.adcolonyTargetZoneid = adcolonyTargetZoneid;
    }

    public void setAdcolonyZoneId(String adcolonyZoneId) {
        this.adcolonyZoneId = adcolonyZoneId;
    }

    public void setChartboostAppID(String chartboostAppID) {
        this.chartboostAppID = chartboostAppID;
    }

    public void setChartboostAppSignature(String chartboostAppSignature) {
        this.chartboostAppSignature = chartboostAppSignature;
    }

    public void setChartboostLocation(String chartboostLocation) {
        this.chartboostLocation = chartboostLocation;
    }

    public void setCta(AdMarvelNativeCta cta) {
        this.cta = cta;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public void setFacebookChildDirectedFlag(String facebookChildDirectedFlag) {
        this.facebookChildDirectedFlag = facebookChildDirectedFlag;
    }

    public void setFacebookTestDeviceId(String[] facebookTestDeviceId) {
        this.facebookTestDeviceId = facebookTestDeviceId;
    }

    public void setInmobiAppId(String inmobiAppId) {
        this.inmobiAppId = inmobiAppId;
    }

    public void setListener(AdMarvelNativeAdListener listener) {
        this.listenerImpl.m402a(listener);
    }

    public void setMetadatas(Map<String, AdMarvelNativeMetadata> metadatas) {
        this.metadatas = metadatas;
    }

    public void setNativeAdErrorTypeFromAdapter(String errorReason) {
        setAdType(C0263a.ERROR);
        setErrorCode(AdMArvelErrorReason.AD_REQUEST_MISSING_XML_ELEMENTS.getErrorCode());
        setErrorReason(errorReason);
    }

    public void setNativeAdType(AdMarvelNativeAdType nativeAdType) {
        this.admarvelNativeAdType = nativeAdType;
    }

    public void setNativeVideoViewWidth(float nativeVideoWidth) {
        this.nativeVideoWidth = nativeVideoWidth;
    }

    public void setPixels(List<String> pixels) {
        this.pixels = pixels;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    public void setRequestJson(JSONObject requestJson) {
        if (requestJson != null) {
            try {
                this.requestJson = requestJson.toString(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    public void start(Activity activity) {
    }

    @Deprecated
    public void stop(Activity activity) {
    }

    public void unregisterView() {
        Logging.log("unregisterView");
        if (getAdType() != C0263a.SDKCALL) {
            return;
        }
        if (getSdkAdNetwork() == SDKAdNetwork.FACEBOOK || getSdkAdNetwork() == SDKAdNetwork.INMOBI) {
            AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(this.ADMARVEL_NATIVE_AD_GUID, getSdkNetwork());
            if (instance != null) {
                instance.unregisterView();
            }
        }
    }

    public void updateNativeAdFromAdapter(Map<String, Object> adapterNativeAd) {
        for (Entry entry : adapterNativeAd.entrySet()) {
            if (!(entry.getKey() == null || entry.getValue() == null)) {
                setNativeAdFields((String) entry.getKey(), entry.getValue());
            }
        }
    }
}
