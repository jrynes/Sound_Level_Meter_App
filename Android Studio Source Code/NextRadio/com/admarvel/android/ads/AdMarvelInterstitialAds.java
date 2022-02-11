package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import com.admarvel.android.ads.AdMarvelAd.AdType;
import com.admarvel.android.ads.AdMarvelInternalWebView.AdMarvelInternalWebView;
import com.admarvel.android.ads.AdMarvelUtils.AdMarvelVideoEvents;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.util.AdHistoryDumpUtils;
import com.admarvel.android.util.AdMarvelThreadExecutorService;
import com.admarvel.android.util.Logging;
import com.admarvel.android.util.p000a.OfflineReflectionUtils;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;

@SuppressLint({"NewApi"})
public class AdMarvelInterstitialAds {
    public static final String CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT = "com.admarvel.adreceiver.LISTENER";
    public static final String CUSTOM_INTERSTITIAL_AD_STATE_INTENT = "com.admarvel.adreceiver.STATE";
    private static String DEFAULT_IMAGE_WEB_VIEW_CSS = null;
    private static String DEFAULT_IMAGE_WEB_VIEW_HTML_FORMAT = null;
    private static String DEFAULT_WEB_VIEW_CSS = null;
    private static String DEFAULT_WEB_VIEW_HTML_FORMAT = null;
    private static String DEFAULT_WEB_VIEW_VIEWPORT = null;
    protected static final String GUID;
    private static String IMAGE_AD_AUTOSCALED_WEB_VIEW_VIEWPORT = null;
    static final String INTERSTITIAL_ACTIVITY_LAUNCH = "activitylaunch";
    static final String INTERSTITIAL_AUDIO_START = "audiostart";
    static final String INTERSTITIAL_AUDIO_STOP = "audiostop";
    static final String INTERSTITIAL_CALLBACK_KEY = "callback";
    static final String INTERSTITIAL_CLICK = "click";
    static final String INTERSTITIAL_CLOSE = "close";
    static final String INTERSTITIAL_DISPLAYED = "displayed";
    static final String INTERSTITIAL_FAIL = "fail";
    static final String INTERSTITIAL_RECEIVE = "receive";
    static final String INTERSTITIAL_RECEIVER_UNREGISTER = "unregisterreceiver";
    static final String INTERSTITIAL_VIDEO_ACTIVITY_LAUNCH = "videoactivitylaunch";
    static final String INTERSTITIAL_VIDEO_EVENT = "videoevent";
    private static String ORMMA_WEB_VIEW_CSS;
    private static final Map<String, AdMarvelInternalWebView> adMarvelInterstitialAdWebViewMap;
    private static boolean enableClickRedirect;
    public static boolean enableOfflineSDK;
    private static boolean isCallbackReceiverRegistred;
    private static boolean isReceiverRegistred;
    private static AdMarvelRewardListener rewardListener;
    final String WEBVIEW_GUID;
    private WeakReference<AdMarvelAd> adMarvelAdWeakRef;
    private String adNetworkPubId;
    private final int backgroundColor;
    WeakReference<Context> contextReference;
    private boolean enableAutoScaling;
    private final C0169d internalAdMarvelInterstitialAdapterListener;
    String interstitialAdContent;
    private BroadcastReceiver interstitialAdsLisenterReceiver;
    InterstitialAdsState interstitialAdsState;
    private BroadcastReceiver interstitialAdsStateReceiver;
    boolean interstitialCustomAdContentFlag;
    String interstitialPublisherID;
    boolean isRewardFired;
    private AdMarvelInterstitialAdListenerImpl listener;
    private final AtomicLong loadTimestamp;
    private final AtomicLong lockTimestamp;
    private Map<String, String> optionalFlags;
    private String partnerId;
    private String siteId;
    private final int textBackgroundColor;
    private final int textBorderColor;
    private final int textFontColor;
    private AdMarvelVideoEventListener videoEventListener;

    /* renamed from: com.admarvel.android.ads.AdMarvelInterstitialAds.1 */
    class C01621 extends BroadcastReceiver {
        final /* synthetic */ AdMarvelInterstitialAds f171a;

        C01621(AdMarvelInterstitialAds adMarvelInterstitialAds) {
            this.f171a = adMarvelInterstitialAds;
        }

        public void onReceive(Context context, Intent intent) {
            if (context != null && intent != null && intent.getExtras() != null && this.f171a.adMarvelAdWeakRef != null && this.f171a.adMarvelAdWeakRef.get() != null && this.f171a.getListener() != null) {
                try {
                    AdMarvelAd adMarvelAd = (AdMarvelAd) this.f171a.adMarvelAdWeakRef.get();
                    Bundle extras = intent.getExtras();
                    String string = extras.getString(AdMarvelInterstitialAds.INTERSTITIAL_CALLBACK_KEY);
                    String string2 = extras.getString("WEBVIEW_GUID");
                    if (!this.f171a.WEBVIEW_GUID.equals(string2)) {
                        return;
                    }
                    if (AdMarvelInterstitialAds.INTERSTITIAL_RECEIVE.equalsIgnoreCase(string)) {
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-receive");
                        this.f171a.getListener().m326a(context, SDKAdNetwork.ADMARVEL, this.f171a.interstitialPublisherID, adMarvelAd, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), this.f171a);
                    } else if (AdMarvelInterstitialAds.INTERSTITIAL_CLICK.equalsIgnoreCase(string)) {
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-click");
                        String string3 = extras.getString(SettingsJsonConstants.APP_URL_KEY);
                        Context context2;
                        int id;
                        if (string3 == null || !(adMarvelAd.getWebViewRedirectUrlProtocol() == null || "admarvelCustomVideo".equalsIgnoreCase(adMarvelAd.getWebViewRedirectUrlProtocol()) || "admarvelVideo".equalsIgnoreCase(adMarvelAd.getWebViewRedirectUrlProtocol()))) {
                            context2 = context;
                            id = adMarvelAd.getId();
                            this.f171a.getListener().m327a(context2, Utils.m184a(adMarvelAd.getWebViewRedirectUrl(), adMarvelAd.getWebViewRedirectUrlProtocol(), Stomp.EMPTY, Utils.m179a(adMarvelAd.getWebViewRedirectUrl(), adMarvelAd.getWebViewRedirectUrlProtocol()), context), adMarvelAd.getSiteId(), r8, adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), this.f171a);
                            return;
                        }
                        context2 = context;
                        id = adMarvelAd.getId();
                        this.f171a.getListener().m327a(context2, string3, adMarvelAd.getSiteId(), r8, adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), this.f171a);
                    } else if (AdMarvelInterstitialAds.INTERSTITIAL_CLOSE.equalsIgnoreCase(string)) {
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-close");
                        this.f171a.getListener().m332a(adMarvelAd, this.f171a);
                        this.f171a.unregisterCallbackReceiver(string2);
                        AdMarvelAdapterInstances.destroyAdMarvelAdapterInstances(string2);
                    } else if (AdMarvelInterstitialAds.INTERSTITIAL_DISPLAYED.equalsIgnoreCase(string)) {
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-displayed");
                        this.f171a.getListener().m333b(this.f171a);
                    } else if (AdMarvelInterstitialAds.INTERSTITIAL_FAIL.equalsIgnoreCase(string)) {
                        ErrorReason errorReason;
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-fail");
                        string = extras.getString("errorCode");
                        int i = -1;
                        ErrorReason a = Utils.m178a(adMarvelAd.getErrorCode());
                        if (a != null) {
                            i = Utils.m177a(a);
                        }
                        if (string == null || string.length() <= 0) {
                            errorReason = a;
                        } else {
                            try {
                                a = Utils.m178a(Integer.parseInt(string));
                                i = Utils.m177a(a);
                                errorReason = a;
                            } catch (Exception e) {
                                e.printStackTrace();
                                a = Utils.m178a(adMarvelAd.getErrorCode());
                                i = Utils.m177a(a);
                                errorReason = a;
                            }
                        }
                        this.f171a.getListener().m325a(context, adMarvelAd.getSdkAdNetwork(), i, errorReason, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), this.f171a);
                        this.f171a.unregisterCallbackReceiver(string2);
                        AdMarvelAdapterInstances.destroyAdMarvelAdapterInstances(string2);
                    } else if (AdMarvelInterstitialAds.INTERSTITIAL_AUDIO_START.equalsIgnoreCase(string)) {
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-audiostart");
                        if (this.f171a.getVideoEventListener() != null) {
                            this.f171a.getVideoEventListener().onAudioStart();
                        }
                    } else if (AdMarvelInterstitialAds.INTERSTITIAL_AUDIO_STOP.equalsIgnoreCase(string)) {
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-audiostop");
                        if (this.f171a.getVideoEventListener() != null) {
                            this.f171a.getVideoEventListener().onAudioStop();
                        }
                    } else if (AdMarvelInterstitialAds.INTERSTITIAL_VIDEO_EVENT.equalsIgnoreCase(string)) {
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-videoevent");
                        if (this.f171a.getVideoEventListener() == null) {
                        }
                    } else if (AdMarvelInterstitialAds.INTERSTITIAL_ACTIVITY_LAUNCH.equalsIgnoreCase(string)) {
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-activitylaunch");
                        r0 = AdMarvelInterstitialAds.getWebView(this.f171a.WEBVIEW_GUID);
                        if (r0 == null || r0.f612u == null || r0.f612u.get() == null || !(r0.f612u.get() instanceof Activity)) {
                            this.f171a.getListener().m328a(null, this.f171a);
                            return;
                        }
                        r0 = (Activity) r0.f612u.get();
                        if (r0 != null && (r0 instanceof AdMarvelActivity)) {
                            this.f171a.getListener().m328a((AdMarvelActivity) r0, this.f171a);
                        }
                    } else if (AdMarvelInterstitialAds.INTERSTITIAL_VIDEO_ACTIVITY_LAUNCH.equalsIgnoreCase(string)) {
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-videoactivitylaunch");
                        r0 = AdMarvelInterstitialAds.getWebView(this.f171a.WEBVIEW_GUID);
                        if (r0 == null || r0.f612u == null || r0.f612u.get() == null || !(r0.f612u.get() instanceof Activity)) {
                            this.f171a.getListener().m331a(null, this.f171a);
                            return;
                        }
                        r0 = (Activity) r0.f612u.get();
                        if (r0 != null && (r0 instanceof AdMarvelVideoActivity)) {
                            this.f171a.getListener().m331a((AdMarvelVideoActivity) r0, this.f171a);
                        }
                    } else if (AdMarvelInterstitialAds.INTERSTITIAL_RECEIVER_UNREGISTER.equalsIgnoreCase(string)) {
                        Logging.log("AdMarvelInterstitialAds - interstitialAdsLisenterReceiver : CallBack Name-unregisterreceiver");
                        this.f171a.unregisterCallbackReceiver(string2);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelInterstitialAds.2 */
    class C01632 extends BroadcastReceiver {
        final /* synthetic */ AdMarvelInterstitialAds f172a;

        C01632(AdMarvelInterstitialAds adMarvelInterstitialAds) {
            this.f172a = adMarvelInterstitialAds;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getExtras() != null) {
                String string = intent.getExtras().getString("WEBVIEW_GUID");
                if (string != null && this.f172a.WEBVIEW_GUID.equals(string)) {
                    this.f172a.interstitialAdsState = InterstitialAdsState.DEFAULT;
                    Logging.log("AdMarvelInterstitialAds - interstitialAdsStateReceiver : InterstitialAdsState-" + this.f172a.interstitialAdsState);
                    this.f172a.unregisterReceiver(string);
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelInterstitialAds.3 */
    class C01643 extends BroadcastReceiver {
        final /* synthetic */ AdMarvelInterstitialAds f173a;

        C01643(AdMarvelInterstitialAds adMarvelInterstitialAds) {
            this.f173a = adMarvelInterstitialAds;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getExtras() != null) {
                String string = intent.getExtras().getString("WEBVIEW_GUID");
                if (string != null && this.f173a.WEBVIEW_GUID.equals(string)) {
                    this.f173a.interstitialAdsState = InterstitialAdsState.DEFAULT;
                    Logging.log("AdMarvelInterstitialAds - interstitialAdsStateReceiver : InterstitialAdsState-" + this.f173a.interstitialAdsState);
                    this.f173a.unregisterReceiver(string);
                }
            }
        }
    }

    public interface AdMarvelInterstitialAdListener {
        void onAdMarvelVideoActivityLaunched(AdMarvelVideoActivity adMarvelVideoActivity, AdMarvelInterstitialAds adMarvelInterstitialAds);

        void onAdmarvelActivityLaunched(AdMarvelActivity adMarvelActivity, AdMarvelInterstitialAds adMarvelInterstitialAds);

        void onClickInterstitialAd(String str, AdMarvelInterstitialAds adMarvelInterstitialAds);

        void onCloseInterstitialAd(AdMarvelInterstitialAds adMarvelInterstitialAds);

        void onFailedToReceiveInterstitialAd(SDKAdNetwork sDKAdNetwork, AdMarvelInterstitialAds adMarvelInterstitialAds, int i, ErrorReason errorReason);

        void onInterstitialDisplayed(AdMarvelInterstitialAds adMarvelInterstitialAds);

        void onReceiveInterstitialAd(SDKAdNetwork sDKAdNetwork, AdMarvelInterstitialAds adMarvelInterstitialAds, AdMarvelAd adMarvelAd);

        void onRequestInterstitialAd(AdMarvelInterstitialAds adMarvelInterstitialAds);
    }

    public enum InterstitialAdsState {
        DEFAULT,
        LOADING,
        AVAILABLE,
        DISPLAYING
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelInterstitialAds.a */
    private static class C0165a implements Runnable {
        private final Map<String, Object> f174a;
        private final String f175b;
        private final String f176c;
        private final String f177d;
        private final int f178e;
        private final String f179f;
        private final WeakReference<AdMarvelInterstitialAds> f180g;
        private final int f181h;
        private final String f182i;
        private final WeakReference<Context> f183j;
        private final Map<String, String> f184k;
        private final String f185l;
        private final boolean f186m;

        public C0165a(Map<String, Object> map, String str, String str2, String str3, int i, String str4, AdMarvelInterstitialAds adMarvelInterstitialAds, int i2, String str5, Context context, Map<String, String> map2, String str6, boolean z) {
            this.f174a = map;
            this.f175b = str;
            this.f176c = str2;
            this.f177d = str3;
            this.f178e = i;
            this.f179f = str4;
            this.f180g = new WeakReference(adMarvelInterstitialAds);
            this.f181h = i2;
            this.f182i = str5;
            this.f183j = new WeakReference(context);
            this.f184k = map2;
            this.f185l = str6;
            this.f186m = z;
        }

        public void run() {
            Context context = (Context) this.f183j.get();
            if (((AdMarvelInterstitialAds) this.f180g.get()) != null && context != null) {
                new AdMarvelInterstitialAsyncTask(context).execute(new Object[]{this.f174a, this.f175b, this.f176c, this.f177d, Integer.valueOf(this.f178e), this.f179f, r0, Integer.valueOf(this.f181h), this.f182i, this.f184k, this.f185l, Boolean.valueOf(this.f186m)});
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelInterstitialAds.b */
    private static class C0166b implements Runnable {
        private final Map<String, Object> f187a;
        private final String f188b;
        private final String f189c;
        private final String f190d;
        private final int f191e;
        private final String f192f;
        private final WeakReference<AdMarvelInterstitialAds> f193g;
        private final int f194h;
        private final String f195i;
        private final WeakReference<Context> f196j;
        private final Map<String, String> f197k;
        private final String f198l;
        private final boolean f199m;

        public C0166b(Map<String, Object> map, String str, String str2, String str3, int i, String str4, AdMarvelInterstitialAds adMarvelInterstitialAds, int i2, String str5, Context context, Map<String, String> map2, String str6, boolean z) {
            this.f187a = map;
            this.f188b = str;
            this.f189c = str2;
            this.f190d = str3;
            this.f191e = i;
            this.f192f = str4;
            this.f193g = new WeakReference(adMarvelInterstitialAds);
            this.f194h = i2;
            this.f195i = str5;
            this.f196j = new WeakReference(context);
            this.f197k = map2;
            this.f198l = str6;
            this.f199m = z;
        }

        @SuppressLint({"NewApi"})
        public void run() {
            Context context = (Context) this.f196j.get();
            if (((AdMarvelInterstitialAds) this.f193g.get()) != null && context != null) {
                new AdMarvelInterstitialAsyncTask(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[]{this.f187a, this.f188b, this.f189c, this.f190d, Integer.valueOf(this.f191e), this.f192f, r0, Integer.valueOf(this.f194h), this.f195i, this.f197k, this.f198l, Boolean.valueOf(this.f199m)});
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelInterstitialAds.c */
    private static class C0167c implements Runnable {
        private final AdMarvelAd f200a;
        private final Context f201b;

        public C0167c(AdMarvelAd adMarvelAd, Context context) {
            this.f200a = adMarvelAd;
            this.f201b = context;
        }

        public void run() {
            if (this.f200a != null) {
                this.f200a.setResponseJson();
            }
            AdHistoryDumpUtils b = AdHistoryDumpUtils.m550b(this.f201b);
            if (b != null && this.f200a != null) {
                int a = b.m555a(this.f201b);
                this.f200a.setAdHistoryCounter(a);
                b.m558a(this.f200a.getAdHistoryDumpString(), a);
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelInterstitialAds.d */
    private static class C0169d implements AdMarvelInterstitialAdapterListener {
        private final WeakReference<AdMarvelInterstitialAds> f206a;
        private WeakReference<AdMarvelAd> f207b;
        private AdMarvelAd f208c;

        /* renamed from: com.admarvel.android.ads.AdMarvelInterstitialAds.d.1 */
        class C01681 implements Runnable {
            final /* synthetic */ String f202a;
            final /* synthetic */ AdMarvelEvent f203b;
            final /* synthetic */ AdMarvelInterstitialAds f204c;
            final /* synthetic */ C0169d f205d;

            C01681(C0169d c0169d, String str, AdMarvelEvent adMarvelEvent, AdMarvelInterstitialAds adMarvelInterstitialAds) {
                this.f205d = c0169d;
                this.f202a = str;
                this.f203b = adMarvelEvent;
                this.f204c = adMarvelInterstitialAds;
            }

            public void run() {
                AdMarvelEventHandler.m269a(this.f202a, this.f203b, null, this.f204c);
            }
        }

        public C0169d(AdMarvelInterstitialAds adMarvelInterstitialAds) {
            this.f206a = new WeakReference(adMarvelInterstitialAds);
        }

        public void m44a(AdMarvelAd adMarvelAd) {
            this.f208c = adMarvelAd;
            this.f207b = new WeakReference(adMarvelAd);
        }

        public void onAdMarvelVideoEvent(AdMarvelVideoEvents adMarvelVideoEvent, Map<String, String> customEventParams) {
            AdMarvelInterstitialAds adMarvelInterstitialAds = (AdMarvelInterstitialAds) this.f206a.get();
            if (adMarvelInterstitialAds != null && adMarvelInterstitialAds.getVideoEventListener() != null) {
                Logging.log("onAdMarvelVideoEvent");
                adMarvelInterstitialAds.getVideoEventListener().onAdMarvelVideoEvent(adMarvelVideoEvent, customEventParams);
            }
        }

        public void onAudioStart() {
            AdMarvelInterstitialAds adMarvelInterstitialAds = (AdMarvelInterstitialAds) this.f206a.get();
            if (adMarvelInterstitialAds != null && adMarvelInterstitialAds.getVideoEventListener() != null) {
                adMarvelInterstitialAds.getVideoEventListener().onAudioStart();
            }
        }

        public void onAudioStop() {
            AdMarvelInterstitialAds adMarvelInterstitialAds = (AdMarvelInterstitialAds) this.f206a.get();
            if (adMarvelInterstitialAds != null && adMarvelInterstitialAds.getVideoEventListener() != null) {
                adMarvelInterstitialAds.getVideoEventListener().onAudioStop();
            }
        }

        public void onClickInterstitialAd(String clickUrl) {
            AdMarvelAd adMarvelAd = (AdMarvelAd) this.f207b.get();
            AdMarvelInterstitialAds adMarvelInterstitialAds = (AdMarvelInterstitialAds) this.f206a.get();
            if (adMarvelInterstitialAds != null && adMarvelInterstitialAds.contextReference != null && adMarvelInterstitialAds.contextReference.get() != null && adMarvelAd != null) {
                Context context = (Context) adMarvelInterstitialAds.contextReference.get();
                if (context != null && adMarvelInterstitialAds.listener != null) {
                    adMarvelInterstitialAds.listener.m327a(context, clickUrl, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), adMarvelInterstitialAds);
                }
            }
        }

        public void onCloseInterstitialAd() {
            AdMarvelInterstitialAds adMarvelInterstitialAds = (AdMarvelInterstitialAds) this.f206a.get();
            if (adMarvelInterstitialAds != null) {
                adMarvelInterstitialAds.interstitialAdsState = InterstitialAdsState.DEFAULT;
                Logging.log("AdMarvelInterstitialAds - onCloseInterstitialAd : InterstitialAdsState-" + adMarvelInterstitialAds.interstitialAdsState);
                adMarvelInterstitialAds.unregisterReceiver(adMarvelInterstitialAds.WEBVIEW_GUID);
                adMarvelInterstitialAds.unregisterCallbackReceiver(adMarvelInterstitialAds.WEBVIEW_GUID);
                AdMarvelAdapterInstances.destroyAdMarvelAdapterInstances(adMarvelInterstitialAds.WEBVIEW_GUID);
            }
            if (adMarvelInterstitialAds != null && adMarvelInterstitialAds.listener != null) {
                adMarvelInterstitialAds.listener.m332a(null, adMarvelInterstitialAds);
            }
        }

        public void onFailedToReceiveInterstitialAd(SDKAdNetwork sdkAdNetwork, String publisherid, int errorCode, ErrorReason errorReason, AdMarvelAd adMarvelAd) {
            AdMarvelInterstitialAds adMarvelInterstitialAds = (AdMarvelInterstitialAds) this.f206a.get();
            if (adMarvelInterstitialAds != null) {
                adMarvelInterstitialAds.interstitialAdsState = InterstitialAdsState.DEFAULT;
                Logging.log("AdMarvelInterstitialAds - onFailedToReceiveInterstitialAd : InterstitialAdsState-" + adMarvelInterstitialAds.interstitialAdsState);
                adMarvelInterstitialAds.setAdNetworkPubId(publisherid);
                Object obj = null;
                if (adMarvelAd.getRetry().equals(Boolean.valueOf(true)) && adMarvelAd.getRetrynum() <= adMarvelAd.getMaxretries()) {
                    int retrynum = adMarvelAd.getRetrynum() + 1;
                    String bannerid = adMarvelAd.getExcluded() == null ? adMarvelAd.getBannerid() : adMarvelAd.getExcluded().length() > 0 ? adMarvelAd.getExcluded() + Stomp.COMMA + adMarvelAd.getBannerid() : adMarvelAd.getBannerid();
                    if (!(adMarvelInterstitialAds.contextReference == null || ((Context) adMarvelInterstitialAds.contextReference.get()) == null)) {
                        adMarvelInterstitialAds.interstitialAdsState = InterstitialAdsState.LOADING;
                        if (Version.getAndroidSDKVersion() >= 11) {
                            AdMarvelThreadExecutorService.m597a().m598b().execute(new C0166b(adMarvelAd.getTargetParams(), adMarvelAd.getPartnerId(), adMarvelAd.getSiteId(), adMarvelAd.getAndroidId(), adMarvelAd.getOrientation(), adMarvelAd.getDeviceConnectivity(), adMarvelInterstitialAds, retrynum, bannerid, (Context) adMarvelInterstitialAds.contextReference.get(), adMarvelAd.getRewardParams(), adMarvelAd.getUserId(), this.f208c.isRewardInterstitial()));
                            obj = 1;
                        } else {
                            new Handler(Looper.getMainLooper()).post(new C0165a(adMarvelAd.getTargetParams(), adMarvelAd.getPartnerId(), adMarvelAd.getSiteId(), adMarvelAd.getAndroidId(), adMarvelAd.getOrientation(), adMarvelAd.getDeviceConnectivity(), adMarvelInterstitialAds, retrynum, bannerid, (Context) adMarvelInterstitialAds.contextReference.get(), adMarvelAd.getRewardParams(), adMarvelAd.getUserId(), this.f208c.isRewardInterstitial()));
                        }
                    }
                    int i = 1;
                }
                if (obj == null && adMarvelInterstitialAds.contextReference != null) {
                    Context context = (Context) adMarvelInterstitialAds.contextReference.get();
                    if (context != null && adMarvelInterstitialAds.listener != null) {
                        adMarvelInterstitialAds.interstitialAdsState = InterstitialAdsState.DEFAULT;
                        Logging.log("AdMarvelInterstitialAds - onFailedToReceiveInterstitialAd : InterstitialAdsState-" + adMarvelInterstitialAds.interstitialAdsState);
                        adMarvelInterstitialAds.unregisterReceiver(adMarvelInterstitialAds.WEBVIEW_GUID);
                        adMarvelInterstitialAds.unregisterCallbackReceiver(adMarvelInterstitialAds.WEBVIEW_GUID);
                        AdMarvelAdapterInstances.destroyAdMarvelAdapterInstances(adMarvelInterstitialAds.WEBVIEW_GUID);
                        adMarvelInterstitialAds.listener.m325a(context, sdkAdNetwork, errorCode, errorReason, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), (AdMarvelInterstitialAds) this.f206a.get());
                    }
                }
            }
        }

        public void onInterstitialDisplayed() {
            AdMarvelInterstitialAds adMarvelInterstitialAds = (AdMarvelInterstitialAds) this.f206a.get();
            if (adMarvelInterstitialAds != null && adMarvelInterstitialAds.contextReference != null && adMarvelInterstitialAds.contextReference.get() != null && adMarvelInterstitialAds.listener != null) {
                adMarvelInterstitialAds.listener.m333b(adMarvelInterstitialAds);
            }
        }

        public void onReceiveInterstitialAd(SDKAdNetwork sdkAdNetwork, String publisherid, AdMarvelAd adMarvelAd) {
            AdMarvelInterstitialAds adMarvelInterstitialAds = (AdMarvelInterstitialAds) this.f206a.get();
            if (adMarvelInterstitialAds != null && adMarvelInterstitialAds.contextReference != null && adMarvelInterstitialAds.contextReference.get() != null) {
                adMarvelInterstitialAds.interstitialAdsState = InterstitialAdsState.AVAILABLE;
                Logging.log("AdMarvelInterstitialAds - onReceiveInterstitialAd : InterstitialAdsState-" + adMarvelInterstitialAds.interstitialAdsState);
                adMarvelInterstitialAds.setAdNetworkPubId(publisherid);
                Context context = (Context) adMarvelInterstitialAds.contextReference.get();
                if (context != null && adMarvelInterstitialAds.listener != null) {
                    adMarvelInterstitialAds.interstitialPublisherID = publisherid;
                    adMarvelInterstitialAds.listener.m326a(context, sdkAdNetwork, publisherid, adMarvelAd, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), adMarvelInterstitialAds);
                }
            }
        }

        public void onRequestInterstitialAd() {
            AdMarvelInterstitialAds adMarvelInterstitialAds = (AdMarvelInterstitialAds) this.f206a.get();
            if (adMarvelInterstitialAds != null && adMarvelInterstitialAds.contextReference != null && adMarvelInterstitialAds.contextReference.get() != null && adMarvelInterstitialAds.listener != null) {
                adMarvelInterstitialAds.listener.m330a(adMarvelInterstitialAds);
            }
        }

        public void onReward(boolean success, SDKAdNetwork sdkAdNetwork, String eventname) {
            AdMarvelInterstitialAds adMarvelInterstitialAds = (AdMarvelInterstitialAds) this.f206a.get();
            AdMarvelEvent a = AdMarvelRewardQueueHandler.m336a().m337a(sdkAdNetwork);
            if (!success || a == null) {
                AdMarvelReward adMarvelReward = new AdMarvelReward();
                adMarvelReward.setSuccess(false);
                if (AdMarvelInterstitialAds.getRewardListener() != null) {
                    AdMarvelInterstitialAds.getRewardListener().onReward(adMarvelReward);
                    return;
                }
                return;
            }
            AdMarvelThreadExecutorService.m597a().m598b().execute(new C01681(this, eventname, a, adMarvelInterstitialAds));
        }
    }

    static {
        DEFAULT_WEB_VIEW_CSS = "<style>* {-webkit-tap-highlight-color: rgba(0,0,0,0.0);} body {background-color:transparent;margin:0px;padding:0px;}</style>";
        DEFAULT_IMAGE_WEB_VIEW_CSS = "<style>#u2nfwuKbaKzVwGmUNmk7wFVXHwzy7S{display:table;height:100%;width:100%;margin:0;padding:0;background-color:transparent;}#u2nfwuKbaKzVwGmUNmk7wFVXHwzy7S>div{display:table-cell;vertical-align:middle;text-align:center;}</style>";
        ORMMA_WEB_VIEW_CSS = "<style>* {-webkit-tap-highlight-color: rgba(0,0,0,0.0);} body {background-color:transparent;margin:0px;padding:0px;}</style><script type='text/javascript' src='http://admarvel.s3.amazonaws.com/js/admarvel_compete_v1.1.js'></script>";
        DEFAULT_WEB_VIEW_HTML_FORMAT = "<html><head>%s</head><body><div align=\"center\">%s</div></body></html>";
        DEFAULT_IMAGE_WEB_VIEW_HTML_FORMAT = "<html><head>%s</head><body id=\"u2nfwuKbaKzVwGmUNmk7wFVXHwzy7S\"><div>%s</div></body></html>";
        DEFAULT_WEB_VIEW_VIEWPORT = "<meta name=\"viewport\" content=\"initial-scale=1.0,maximum-scale=1.0,target-densitydpi=device-dpi, width=device-width\" />";
        IMAGE_AD_AUTOSCALED_WEB_VIEW_VIEWPORT = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0\" />";
        enableClickRedirect = true;
        isReceiverRegistred = false;
        isCallbackReceiverRegistred = false;
        adMarvelInterstitialAdWebViewMap = new ConcurrentHashMap();
        GUID = UUID.randomUUID().toString();
    }

    public AdMarvelInterstitialAds(Context context) {
        this(context, 0, 7499117, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 0);
    }

    public AdMarvelInterstitialAds(Context context, int backgroundColor, int textBackgroundColor, int textFontColor, int textBorderColor) {
        this.listener = null;
        this.enableAutoScaling = true;
        this.isRewardFired = false;
        this.lockTimestamp = new AtomicLong(0);
        this.loadTimestamp = new AtomicLong(0);
        this.contextReference = new WeakReference(context);
        this.internalAdMarvelInterstitialAdapterListener = new C0169d(this);
        this.interstitialAdsState = InterstitialAdsState.DEFAULT;
        Logging.log("AdMarvelInterstitialAds - AdMarvelInterstitialAds-Constructor : InterstitialAdsState-" + this.interstitialAdsState);
        this.interstitialCustomAdContentFlag = false;
        this.WEBVIEW_GUID = UUID.randomUUID().toString();
        if (backgroundColor == 0) {
            this.backgroundColor = 0;
        } else {
            this.backgroundColor = ViewCompat.MEASURED_STATE_MASK | backgroundColor;
        }
        if (textBackgroundColor == 0) {
            this.textBackgroundColor = 0;
        } else {
            this.textBackgroundColor = ViewCompat.MEASURED_STATE_MASK | textBackgroundColor;
        }
        this.textFontColor = textFontColor;
        this.textBorderColor = textBorderColor;
    }

    private boolean checkInterstitialAdStateOnAdRequest(Context context, String siteId, Map<String, Object> targetParams) {
        if (System.currentTimeMillis() - this.lockTimestamp.getAndSet(System.currentTimeMillis()) < 2000) {
            Logging.log("AdMarvelInterstitialAds - checkInterstitialAdStateOnAdRequest :" + this.interstitialAdsState);
            Logging.log("requestNewAd: AD REQUEST PENDING, IGNORING REQUEST");
            this.listener.m325a(context, null, 304, Utils.m178a(304), siteId, 0, (Map) targetParams, Stomp.EMPTY, this);
            return true;
        } else if (this.interstitialAdsState == InterstitialAdsState.LOADING) {
            Logging.log("AdMarvelInterstitialAds - checkInterstitialAdStateOnAdRequest : InterstitialAdsState-" + this.interstitialAdsState);
            if (this.loadTimestamp.get() > 30000) {
                this.interstitialAdsState = InterstitialAdsState.DEFAULT;
                Logging.log("AdMarvelInterstitialAds - checkInterstitialAdStateOnAdRequest : InterstitialAdsState-" + this.interstitialAdsState);
            } else {
                Logging.log("requestNewAd: AD REQUEST PENDING, IGNORING REQUEST");
                this.listener.m325a(context, null, 304, Utils.m178a(304), siteId, 0, (Map) targetParams, Stomp.EMPTY, this);
            }
            return true;
        } else if (this.interstitialAdsState == InterstitialAdsState.DISPLAYING) {
            Logging.log("AdMarvelInterstitialAds - checkInterstitialAdStateOnAdRequest :" + this.interstitialAdsState);
            Logging.log("requestNewAd: AD REQUEST PENDING, IGNORING REQUEST");
            this.listener.m325a(context, null, 309, Utils.m178a(309), siteId, 0, (Map) targetParams, Stomp.EMPTY, this);
            return true;
        } else if (this.interstitialAdsState != InterstitialAdsState.AVAILABLE) {
            return false;
        } else {
            Logging.log("AdMarvelInterstitialAds - checkInterstitialAdStateOnAdRequest : InterstitialAdsState-" + this.interstitialAdsState);
            AdMarvelAd adMarvelAd = this.adMarvelAdWeakRef != null ? (AdMarvelAd) this.adMarvelAdWeakRef.get() : null;
            if (adMarvelAd != null) {
                this.listener.m326a(context, adMarvelAd.getSdkAdNetwork() != null ? adMarvelAd.getSdkAdNetwork() : SDKAdNetwork.ADMARVEL, this.interstitialPublisherID, adMarvelAd, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), this);
            }
            return true;
        }
    }

    static synchronized void clearWebViewMap() {
        synchronized (AdMarvelInterstitialAds.class) {
            try {
                adMarvelInterstitialAdWebViewMap.clear();
            } catch (Exception e) {
            }
        }
    }

    private void createCallbackBroadCastReceiver() {
        Logging.log("AdMarvelInterstitialAds - createBroadCastReceiver : Creating Receiver");
        this.interstitialAdsLisenterReceiver = new C01621(this);
    }

    public static synchronized void disableNetworkActivity() {
        synchronized (AdMarvelInterstitialAds.class) {
            OfflineReflectionUtils.m532a();
        }
    }

    private boolean displayMediationAd(Activity activity, SDKAdNetwork adnetwork, AdMarvelAd adMarvelAd) {
        Context context = (Context) this.contextReference.get();
        if (context != null) {
            AdMarvelAdapter adMarvelAdapter = null;
            try {
                Intent intent = new Intent(context, AdMarvelMediationActivity.class);
                intent.addFlags(268435456);
                intent.putExtra("SDKAdNetwork", adnetwork.toString());
                intent.putExtra("adapterWebviewGUID", this.WEBVIEW_GUID);
                adMarvelAd.removeNonStringEntriesTargetParam();
                if (adnetwork == SDKAdNetwork.YUME) {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.YUME_SDK_ADAPTER_FULL_CLASSNAME);
                } else if (adnetwork == SDKAdNetwork.CHARTBOOST) {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.CHARTBOOST_SDK_ADAPTER_FULL_CLASSNAME);
                }
                OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(adMarvelAd);
                objectOutputStream.close();
                intent.putExtra("serialized_admarvelad", byteArrayOutputStream.toByteArray());
                if (adnetwork == SDKAdNetwork.CHARTBOOST) {
                    if (adMarvelAd.isRewardInterstitial()) {
                        if (adMarvelAdapter != null && adMarvelAdapter.getAdAvailablityStatus(Stomp.TRUE, context) == 0) {
                            context.startActivity(intent);
                            return true;
                        }
                    } else if (adMarvelAdapter != null && adMarvelAdapter.getAdAvailablityStatus(Stomp.FALSE, context) == 0) {
                        context.startActivity(intent);
                        return true;
                    }
                } else if (adMarvelAdapter != null && adMarvelAdapter.getAdAvailablityStatus() == 0) {
                    context.startActivity(intent);
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean displayPendingAdMarvelAd(AdMarvelAd adMarvelAd) {
        if (this.interstitialAdContent != null && this.interstitialAdContent.length() > 0) {
            Context context = (Context) this.contextReference.get();
            if (context != null) {
                if (enableOfflineSDK) {
                    new OfflineReflectionUtils().m536a(adMarvelAd, context, new Handler());
                } else if (!enableOfflineSDK) {
                    new Utils(context).m244a(adMarvelAd);
                }
                if (!this.interstitialCustomAdContentFlag) {
                    Intent intent = new Intent(context, AdMarvelActivity.class);
                    intent.addFlags(268435456);
                    intent.putExtra(locationTracking.source, "campaign");
                    intent.putExtra(Constants.NATIVE_VIDEO_AD_HTML_ELEMENT, this.interstitialAdContent);
                    intent.putExtra("xml", adMarvelAd.getXml());
                    intent.putExtra("backgroundcolor", this.backgroundColor);
                    intent.putExtra("isInterstitial", true);
                    intent.putExtra("isInterstitialClick", false);
                    intent.putExtra("GUID", GUID);
                    if (getWebView(this.WEBVIEW_GUID) != null) {
                        intent.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                        adMarvelAd.removeNonStringEntriesTargetParam();
                        try {
                            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                            objectOutputStream.writeObject(adMarvelAd);
                            objectOutputStream.close();
                            intent.putExtra("serialized_admarvelad", byteArrayOutputStream.toByteArray());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        context.startActivity(intent);
                        return true;
                    }
                    Logging.log("Error in feaching webview");
                    return false;
                } else if (Version.getAndroidSDKVersion() < 14) {
                    return false;
                } else {
                    Intent intent2 = new Intent(context, AdMarvelVideoActivity.class);
                    intent2.addFlags(268435456);
                    intent2.putExtra(Constants.NATIVE_VIDEO_AD_HTML_ELEMENT, this.interstitialAdContent);
                    intent2.putExtra("GUID", GUID);
                    if (getWebView(this.WEBVIEW_GUID) != null) {
                        intent2.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                    }
                    context.startActivity(intent2);
                    return true;
                }
            }
        }
        return false;
    }

    public static synchronized void enableNetworkActivity(Activity activity, String partnerId) {
        synchronized (AdMarvelInterstitialAds.class) {
            OfflineReflectionUtils.m534b(activity, partnerId);
        }
    }

    public static boolean getEnableClickRedirect() {
        return enableClickRedirect;
    }

    public static AdMarvelRewardListener getRewardListener() {
        return rewardListener;
    }

    public static AdMarvelInternalWebView getWebView(String key) {
        return (AdMarvelInternalWebView) adMarvelInterstitialAdWebViewMap.get(key);
    }

    public static void initializeOfflineSDK(Activity activity, String partnerId) {
        enableOfflineSDK = true;
        OfflineReflectionUtils.m533a(activity, partnerId);
    }

    private boolean isAdRequestBlocked(Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.m205d("admarvel"), 0);
        try {
            String str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            str = str != null ? "duration" + str + i + AdMarvelUtils.getSDKVersion() : "duration" + i + AdMarvelUtils.getSDKVersion();
            if (str == null) {
                return false;
            }
            str = sharedPreferences.getString(Utils.m205d(str), null);
            if (str == null || str.length() <= 0) {
                return false;
            }
            return DateFormat.getDateTimeInstance().parse(DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()))).before(DateFormat.getDateTimeInstance().parse(str));
        } catch (Exception e) {
            return false;
        }
    }

    static synchronized void purgeWebViewMap(String key) {
        synchronized (AdMarvelInterstitialAds.class) {
            try {
                adMarvelInterstitialAdWebViewMap.remove(key);
            } catch (Exception e) {
            }
        }
    }

    private void registerCallbackReceiver() {
        if (this.contextReference != null && this.contextReference.get() != null) {
            Logging.log("AdMarvelInterstitialAds - registerCallbackReceiver : registering...");
            ((Context) this.contextReference.get()).getApplicationContext().registerReceiver(this.interstitialAdsLisenterReceiver, new IntentFilter(CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT));
            isCallbackReceiverRegistred = true;
        }
    }

    private void registerReceiver() {
        if (this.contextReference != null && this.contextReference.get() != null) {
            ((Context) this.contextReference.get()).getApplicationContext().registerReceiver(this.interstitialAdsStateReceiver, new IntentFilter(CUSTOM_INTERSTITIAL_AD_STATE_INTENT));
            isReceiverRegistred = true;
        }
    }

    public static void setEnableClickRedirect(boolean enableClickRedirect) {
        Logging.log("AdMarvelInterstitialAds - setEnableClickRedirect :" + enableClickRedirect);
        enableClickRedirect = enableClickRedirect;
    }

    public static void setRewardListener(AdMarvelRewardListener rewardListener) {
        rewardListener = rewardListener;
    }

    static synchronized void setWebViewMap(String key, AdMarvelInternalWebView webview) {
        synchronized (AdMarvelInterstitialAds.class) {
            adMarvelInterstitialAdWebViewMap.put(key, webview);
        }
    }

    protected void disableAdRequest(String duration, AdMarvelAd adMarvelAd, Context context) {
        if (context != null) {
            String str;
            try {
                str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
                str = str != null ? "duration" + str + i + AdMarvelUtils.getSDKVersion() : "duration" + i + AdMarvelUtils.getSDKVersion();
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                str = null;
            }
            if (str != null) {
                Editor edit = context.getSharedPreferences(Utils.m205d("admarvel"), 0).edit();
                edit.putString(Utils.m205d(str), DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis() + ((long) (Integer.parseInt(duration) * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH)))));
                edit.commit();
                Logging.log("requestNewAd: AD REQUEST BLOCKED, IGNORING REQUEST");
                this.interstitialAdsState = InterstitialAdsState.DEFAULT;
                Logging.log("AdMarvelInterstitialAds - disableAdRequest : InterstitialAdsState-" + this.interstitialAdsState);
                unregisterReceiver(this.WEBVIEW_GUID);
                unregisterCallbackReceiver(this.WEBVIEW_GUID);
                this.listener.m325a(context, adMarvelAd.getSdkAdNetwork(), 304, Utils.m178a(304), adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), this);
            }
        }
    }

    public boolean displayInterstitial(Activity activity, SDKAdNetwork adnetwork, AdMarvelAd adMarvelAd) {
        boolean z = false;
        Logging.log("AdMarvelInterstitialAds - displayInterstitial");
        if (this.interstitialAdsState == InterstitialAdsState.DEFAULT) {
            Logging.log("AdMarvelInterstitialAds - displayInterstitial : InterstitialAdsState-" + this.interstitialAdsState);
            Logging.log("AdMarvelInterstitialAds - Interstitial Ad Not Available");
        } else if (this.interstitialAdsState == InterstitialAdsState.LOADING) {
            Logging.log("AdMarvelInterstitialAds - displayInterstitial : InterstitialAdsState-" + this.interstitialAdsState);
            Logging.log("AdMarvelInterstitialAds - Interstitial Ad Not Available");
        } else if (this.interstitialAdsState == InterstitialAdsState.DISPLAYING) {
            Logging.log("AdMarvelInterstitialAds - displayInterstitial : InterstitialAdsState-" + this.interstitialAdsState);
            Logging.log("AdMarvelInterstitialAds - Interstitial Ad already in visible state");
        } else {
            this.interstitialAdsState = InterstitialAdsState.DISPLAYING;
            Logging.log("AdMarvelInterstitialAds - displayInterstitial : InterstitialAdsState-" + this.interstitialAdsState);
            AdMarvelAdapter adMarvelAdapter = null;
            if (adnetwork == SDKAdNetwork.GOOGLEPLAY) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.GOOGLEPLAY_SDK_ADAPTER_FULL_CLASSNAME);
                } catch (Exception e) {
                }
            } else if (adnetwork == SDKAdNetwork.RHYTHM) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.RHYTHM_SDK_ADAPTER_FULL_CLASSNAME);
                } catch (Exception e2) {
                }
            } else if (adnetwork == SDKAdNetwork.MILLENNIAL) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.MILLENNIAL_SDK_APAPTER_FULL_CLASSNAME);
                } catch (Exception e3) {
                }
            } else if (adnetwork == SDKAdNetwork.AMAZON) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.AMAZON_SDK_APAPTER_FULL_CLASSNAME);
                } catch (Exception e4) {
                }
            } else if (adnetwork == SDKAdNetwork.ADCOLONY) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME);
                } catch (Exception e5) {
                }
            } else if (adnetwork == SDKAdNetwork.FACEBOOK) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.FACEBOOK_SDK_APAPTER_FULL_CLASSNAME);
                } catch (Exception e6) {
                }
            } else if (adnetwork == SDKAdNetwork.INMOBI) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.INMOBI_SDK_APAPTER_FULL_CLASSNAME);
                } catch (Exception e7) {
                }
            } else if (adnetwork == SDKAdNetwork.HEYZAP) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.HEYZAP_SDK_APAPTER_FULL_CLASSNAME);
                } catch (Exception e8) {
                }
            } else if (adnetwork == SDKAdNetwork.UNITYADS) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME);
                } catch (Exception e9) {
                }
            } else if (adnetwork == SDKAdNetwork.VUNGLE) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME);
                } catch (Exception e10) {
                }
            } else if (adnetwork == SDKAdNetwork.VERVE) {
                try {
                    adMarvelAdapter = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.VERVE_SDK_ADAPTER_FULL_CLASSNAME);
                } catch (Exception e11) {
                }
            }
            if (adMarvelAdapter != null) {
                registerReceiver();
                if (adMarvelAd != null) {
                    z = adMarvelAd.isRewardInterstitial();
                }
                boolean displayInterstitial = adMarvelAdapter.displayInterstitial(activity, z);
                if (displayInterstitial) {
                    new Utils(activity).m244a(adMarvelAd);
                }
                if (this.contextReference == null || this.contextReference.get() == null || !AdMarvelUtils.isLogDumpEnabled()) {
                    z = displayInterstitial;
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(new C0167c(adMarvelAd, (Context) this.contextReference.get()), 3000);
                    z = displayInterstitial;
                }
            } else if (adnetwork == SDKAdNetwork.YUME || adnetwork == SDKAdNetwork.CHARTBOOST) {
                registerReceiver();
                z = displayMediationAd(activity, adnetwork, adMarvelAd);
                if (z) {
                    new Utils(activity).m244a(adMarvelAd);
                }
            } else if (adnetwork == SDKAdNetwork.ADMARVEL) {
                registerReceiver();
                z = displayPendingAdMarvelAd(adMarvelAd);
            }
            if (!z) {
                this.interstitialAdsState = InterstitialAdsState.DEFAULT;
                unregisterReceiver(this.WEBVIEW_GUID);
                unregisterCallbackReceiver(this.WEBVIEW_GUID);
            }
        }
        return z;
    }

    public boolean displayInterstitial(Activity activity, SDKAdNetwork adnetwork, String key, AdMarvelAd adMarvelAd) {
        return displayInterstitial(activity, adnetwork, adMarvelAd);
    }

    public int getAdMarvelBackgroundColor() {
        return this.backgroundColor;
    }

    public String getAdNetworkPubId() {
        return this.adNetworkPubId;
    }

    public AdMarvelInterstitialAdListenerImpl getListener() {
        return this.listener;
    }

    public String getPartnerId() {
        return this.partnerId;
    }

    public String getSiteId() {
        return this.siteId;
    }

    public int getTextBackgroundColor() {
        return this.textBackgroundColor;
    }

    public int getTextBorderColor() {
        return this.textBorderColor;
    }

    public int getTextFontColor() {
        return this.textFontColor;
    }

    public AdMarvelVideoEventListener getVideoEventListener() {
        return this.videoEventListener;
    }

    public boolean isAutoScalingEnabled() {
        return this.enableAutoScaling;
    }

    public boolean isInterstitialAdAvailable() {
        return this.interstitialAdsState == InterstitialAdsState.AVAILABLE;
    }

    public boolean isRewardFired() {
        return this.isRewardFired;
    }

    public void requestNewInterstitialAd(Context context, Map<String, Object> targetparams, String partnerId, String siteId) {
        try {
            Logging.log("AdMarvelInterstitialAds - requestNewInterstitialAd");
            this.partnerId = partnerId;
            this.siteId = siteId;
            Map map = null;
            if (targetparams != null) {
                map = new HashMap(targetparams);
            }
            this.contextReference = new WeakReference(context);
            if (isAdRequestBlocked(context)) {
                Logging.log("requestNewAd: AD REQUEST PENDING, IGNORING REQUEST");
                this.listener.m325a(context, null, 304, Utils.m178a(304), siteId, 0, map, Stomp.EMPTY, this);
                return;
            }
            partnerId = partnerId.trim();
            siteId = siteId.trim();
            if (!checkInterstitialAdStateOnAdRequest(context, siteId, map)) {
                this.interstitialAdsStateReceiver = new C01643(this);
                createCallbackBroadCastReceiver();
                registerCallbackReceiver();
                AdMarvelAdapterInstances.buildAdMarvelAdapterInstances(this.WEBVIEW_GUID);
                this.interstitialAdsState = InterstitialAdsState.LOADING;
                Logging.log("AdMarvelInterstitialAds - requestNewInterstitialAd : InterstitialAdsState-" + this.interstitialAdsState);
                this.loadTimestamp.set(System.currentTimeMillis());
                this.listener.m330a(this);
                String str = null;
                if (map != null) {
                    str = (String) map.get("UNIQUE_ID");
                }
                if (Version.getAndroidSDKVersion() >= 11) {
                    AdMarvelThreadExecutorService.m597a().m598b().execute(new C0166b(map, partnerId, siteId, str, Utils.m216j(context), Utils.m181a(context), this, 0, Stomp.EMPTY, context, null, null, false));
                    return;
                }
                new Handler(Looper.getMainLooper()).post(new C0165a(map, partnerId, siteId, str, Utils.m216j(context), Utils.m181a(context), this, 0, Stomp.EMPTY, context, null, null, false));
            }
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
        }
    }

    public void requestNewInterstitialAd(Context context, Map<String, Object> targetParams, String partnerId, String siteId, Activity activity) {
        if (!(Version.getAndroidSDKVersion() < 14 || AdMarvelUtils.isRegisteredForActivityLifecylceCallbacks || activity == null)) {
            activity.getApplication().registerActivityLifecycleCallbacks(AdMarvelActivityLifecycleCallbacksListener.m248a());
            AdMarvelUtils.isRegisteredForActivityLifecylceCallbacks = true;
        }
        requestNewInterstitialAd(context, targetParams, partnerId, siteId);
    }

    protected void requestPendingAdMarvelAd(AdMarvelAd adMarvelAd, Context context) {
        String str = Stomp.EMPTY;
        this.adMarvelAdWeakRef = new WeakReference(adMarvelAd);
        if (adMarvelAd != null) {
            String xhtml;
            if (adMarvelAd.getAdType() == AdType.CUSTOM) {
                xhtml = adMarvelAd.getXhtml();
            } else if (!adMarvelAd.getAdType().equals(AdType.IMAGE) || !adMarvelAd.hasImage() || adMarvelAd.getImageWidth() <= 0 || adMarvelAd.getImageHeight() <= 0) {
                xhtml = adMarvelAd.getXhtml().contains("ORMMA_API") ? String.format(DEFAULT_WEB_VIEW_HTML_FORMAT, new Object[]{ORMMA_WEB_VIEW_CSS, adMarvelAd.getXHTML()}) : String.format(DEFAULT_WEB_VIEW_HTML_FORMAT, new Object[]{DEFAULT_WEB_VIEW_CSS, adMarvelAd.getXHTML() + Utils.f493a});
            } else if (Version.getAndroidSDKVersion() >= 7) {
                float m = ((float) (Utils.m222m(context) < Utils.m224n(context) ? Utils.m222m(context) : Utils.m224n(context))) / Utils.m226o(context);
                Logging.log("Device Relative Screen Width :" + m);
                str = "<a href=\"" + adMarvelAd.getClickURL() + "\"><img src=\"" + adMarvelAd.getImageURL() + "\" width=\"" + m + "\"\" /></a>";
                xhtml = String.format(DEFAULT_IMAGE_WEB_VIEW_HTML_FORMAT, new Object[]{DEFAULT_IMAGE_WEB_VIEW_CSS + IMAGE_AD_AUTOSCALED_WEB_VIEW_VIEWPORT, str});
            } else if (context != null) {
                int imageWidth;
                int n = Utils.m216j(context) == 2 ? Utils.m224n(context) : Utils.m222m(context);
                int n2 = Utils.m216j(context) == 1 ? Utils.m224n(context) : Utils.m222m(context);
                if (this.enableAutoScaling) {
                    imageWidth = (int) (((float) adMarvelAd.getImageWidth()) * Utils.m175a(context, n, adMarvelAd.getImageWidth()));
                    n = (int) (Utils.m175a(context, n, adMarvelAd.getImageWidth()) * ((float) adMarvelAd.getImageHeight()));
                } else {
                    imageWidth = adMarvelAd.getImageWidth();
                    n = adMarvelAd.getImageHeight();
                }
                str = "<a href=\"" + adMarvelAd.getClickURL() + "\"><img src=\"" + adMarvelAd.getImageURL() + "\" width=\"" + imageWidth + "\" height=\"" + Math.min(n, n2) + "\" /></a>";
                xhtml = String.format(DEFAULT_WEB_VIEW_HTML_FORMAT, new Object[]{DEFAULT_WEB_VIEW_CSS + DEFAULT_WEB_VIEW_VIEWPORT, str});
            } else {
                xhtml = String.format(DEFAULT_WEB_VIEW_HTML_FORMAT, new Object[]{DEFAULT_WEB_VIEW_CSS + DEFAULT_WEB_VIEW_VIEWPORT, adMarvelAd.getXHTML()});
            }
            this.interstitialAdContent = xhtml;
            if (adMarvelAd.getAdType() == AdType.CUSTOM) {
                this.interstitialCustomAdContentFlag = true;
            } else {
                this.interstitialCustomAdContentFlag = false;
            }
            if (context != null) {
                if (Version.getAndroidSDKVersion() == 19 || Version.getAndroidSDKVersion() == 20) {
                    Utils.m240v(context.getApplicationContext());
                }
                AdMarvelInternalWebView adMarvelInternalWebView = new AdMarvelInternalWebView(context, adMarvelAd.getXml(), GUID, null, null, adMarvelAd, AdMarvelInternalWebView.INTERSTITIAL, this.WEBVIEW_GUID);
                adMarvelInternalWebView.setAdMarvelInterstitialAdsInstance(this);
                adMarvelInternalWebView.m321k();
                adMarvelInternalWebView.m322l();
                adMarvelInternalWebView.addJavascriptInterface(new AdMarvelWebViewJSInterface(adMarvelInternalWebView, adMarvelAd, context), "ADMARVEL");
                adMarvelInternalWebView.addJavascriptInterface(new AdMarvelBrightrollJSInterface(adMarvelInternalWebView), "AndroidBridge");
                adMarvelInternalWebView.setBackgroundColor(this.backgroundColor);
                String str2 = "content://" + context.getPackageName() + ".AdMarvelLocalFileContentProvider";
                if (enableOfflineSDK) {
                    adMarvelInternalWebView.loadDataWithBaseURL(adMarvelAd.getOfflineBaseUrl() + ReadOnlyContext.SEPARATOR, xhtml, WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
                } else if (Version.getAndroidSDKVersion() < 11) {
                    adMarvelInternalWebView.loadDataWithBaseURL(str2, xhtml, WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
                } else {
                    adMarvelInternalWebView.loadDataWithBaseURL("http://baseurl.admarvel.com", xhtml, WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
                }
                setWebViewMap(this.WEBVIEW_GUID, adMarvelInternalWebView);
            }
        } else if (this.listener != null) {
            this.interstitialAdsState = InterstitialAdsState.DEFAULT;
            Logging.log("AdMarvelInterstitialAds - requestPendingAdMarvelAd : InterstitialAdsState-" + this.interstitialAdsState);
            unregisterReceiver(this.WEBVIEW_GUID);
            unregisterCallbackReceiver(this.WEBVIEW_GUID);
            this.listener.m325a(context, SDKAdNetwork.ADMARVEL, 304, Utils.m178a(304), Stomp.EMPTY, 0, null, Stomp.EMPTY, this);
        }
    }

    void requestPendingAdapterAd(Map<String, Object> targettingParams, AdMarvelAd adMarvelAd, String className, Context context) {
        try {
            this.adMarvelAdWeakRef = new WeakReference(adMarvelAd);
            AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, className);
            if (this.internalAdMarvelInterstitialAdapterListener != null) {
                this.internalAdMarvelInterstitialAdapterListener.m44a(adMarvelAd);
            }
            instance.requestIntersitialNewAd(this.internalAdMarvelInterstitialAdapterListener, context, adMarvelAd, targettingParams, this.backgroundColor, this.textFontColor, this.WEBVIEW_GUID);
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            this.interstitialAdsState = InterstitialAdsState.DEFAULT;
            Logging.log("AdMarvelInterstitialAds - requestPendingAdapterAd : InterstitialAdsState-" + this.interstitialAdsState);
            unregisterReceiver(this.WEBVIEW_GUID);
            unregisterCallbackReceiver(this.WEBVIEW_GUID);
            this.listener.m325a(context, adMarvelAd.getSdkAdNetwork(), 304, Utils.m178a(304), adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress(), this);
        }
    }

    public void requestRewardInterstitial(Context context, Map<String, Object> targetParams, String partnerId, String siteId, Map<String, String> rewardParams) {
        try {
            Logging.log("AdMarvelInterstitialAds - requestRewardInterstitial");
            this.partnerId = partnerId;
            this.siteId = siteId;
            if (Version.getAndroidSDKVersion() >= 14 && !AdMarvelUtils.isRegisteredForActivityLifecylceCallbacks && context != null && (context instanceof Activity)) {
                ((Activity) context).getApplication().registerActivityLifecycleCallbacks(AdMarvelActivityLifecycleCallbacksListener.m248a());
                AdMarvelUtils.isRegisteredForActivityLifecylceCallbacks = true;
            }
            Map map = null;
            if (targetParams != null) {
                map = new HashMap(targetParams);
            }
            this.contextReference = new WeakReference(context);
            if (isAdRequestBlocked(context)) {
                Logging.log("requestNewAd: AD REQUEST PENDING, IGNORING REQUEST");
                this.listener.m325a(context, null, 304, Utils.m178a(304), siteId, 0, (Map) targetParams, Stomp.EMPTY, this);
                return;
            }
            partnerId = partnerId.trim();
            siteId = siteId.trim();
            if (!checkInterstitialAdStateOnAdRequest(context, siteId, map)) {
                this.interstitialAdsStateReceiver = new C01632(this);
                createCallbackBroadCastReceiver();
                registerCallbackReceiver();
                AdMarvelAdapterInstances.buildAdMarvelAdapterInstances(this.WEBVIEW_GUID);
                this.interstitialAdsState = InterstitialAdsState.LOADING;
                Logging.log("AdMarvelInterstitialAds - requestNewInterstitialAd : InterstitialAdsState-" + this.interstitialAdsState);
                this.loadTimestamp.set(System.currentTimeMillis());
                String userId = AdMarvelUtils.getUserId();
                setUserId(userId);
                this.listener.m330a(this);
                String str = null;
                if (map != null) {
                    str = (String) map.get("UNIQUE_ID");
                }
                if (Version.getAndroidSDKVersion() >= 11) {
                    ExecutorService b = AdMarvelThreadExecutorService.m597a().m598b();
                    b.execute(new C0166b(map, partnerId, siteId, str, Utils.m216j(context), Utils.m181a(context), this, 0, Stomp.EMPTY, context, rewardParams, userId, true));
                    return;
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new C0165a(map, partnerId, siteId, str, Utils.m216j(context), Utils.m181a(context), this, 0, Stomp.EMPTY, context, rewardParams, userId, true));
            }
        } catch (Exception e) {
        }
    }

    public void setAdMarvelBackgroundColor(int backgroundColor) {
    }

    void setAdNetworkPubId(String _publisherId) {
        this.adNetworkPubId = _publisherId;
    }

    public void setEnableAutoScaling(boolean enableAutoScaling) {
        Logging.log("AdMarvelInterstitialAds - setEnableAutoScaling :" + enableAutoScaling);
        this.enableAutoScaling = enableAutoScaling;
    }

    void setInterstitialAdsState(InterstitialAdsState adState) {
        Logging.log("AdMarvelInterstitialAds - setInterstitialAdsState : InterstitialAdsState-" + adState);
        this.interstitialAdsState = adState;
    }

    public void setListener(AdMarvelInterstitialAdListener listener) {
        this.listener = new AdMarvelInterstitialAdListenerImpl();
        this.listener.m329a(listener);
    }

    public void setOptionalFlags(Map<String, String> optionalFlags) {
        this.optionalFlags = optionalFlags;
    }

    public void setRewardFired(boolean isRewardFired) {
        this.isRewardFired = isRewardFired;
    }

    public void setTextBackgroundColor(int textBackgroundColor) {
    }

    void setUserId(String userId) {
        Logging.log("AdMarvelInterstitialAds : setUserId-" + userId);
        try {
            AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME).setUserId(userId);
        } catch (Exception e) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME).setUserId(userId);
        } catch (Exception e2) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.CHARTBOOST_SDK_ADAPTER_FULL_CLASSNAME).setUserId(userId);
        } catch (Exception e3) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.WEBVIEW_GUID, Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME).setUserId(userId);
        } catch (Exception e4) {
        }
    }

    public void setVideoEventListener(AdMarvelVideoEventListener listener) {
        this.videoEventListener = listener;
    }

    void unregisterCallbackReceiver(String WEBVIEW_GUID_TEMP) {
        if (WEBVIEW_GUID_TEMP != null && this.WEBVIEW_GUID.equals(WEBVIEW_GUID_TEMP) && this.contextReference != null && this.contextReference.get() != null && isCallbackReceiverRegistred && this.interstitialAdsLisenterReceiver != null) {
            try {
                Logging.log("AdMarvelInterstitialAds - unregisterCallbackReceiver : interstitialAdsLisenterReceiver");
                ((Context) this.contextReference.get()).getApplicationContext().unregisterReceiver(this.interstitialAdsLisenterReceiver);
            } catch (Exception e) {
            }
            isCallbackReceiverRegistred = false;
            this.interstitialAdsLisenterReceiver = null;
        }
    }

    void unregisterReceiver(String WEBVIEW_GUID_TEMP) {
        if (WEBVIEW_GUID_TEMP != null && this.WEBVIEW_GUID.equals(WEBVIEW_GUID_TEMP) && this.contextReference != null && this.contextReference.get() != null && isReceiverRegistred && this.interstitialAdsStateReceiver != null) {
            try {
                Logging.log("AdMarvelInterstitialAds - unregisterReceiver : interstitialAdsStateReceiver");
                ((Context) this.contextReference.get()).getApplicationContext().unregisterReceiver(this.interstitialAdsStateReceiver);
            } catch (Exception e) {
            }
            isReceiverRegistred = false;
            this.interstitialAdsStateReceiver = null;
        }
    }
}
