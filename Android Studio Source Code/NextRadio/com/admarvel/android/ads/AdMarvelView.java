package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.admarvel.android.ads.AdMarvelActivityLifecycleCallbacksListener.AdMarvelActivityLifecycleCallbacksListener;
import com.admarvel.android.ads.AdMarvelAd.AdType;
import com.admarvel.android.ads.AdMarvelUtils.AdMarvelVideoEvents;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd.AdMarvelNativeAdListener;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd.AdMarvelNativeVideoAdListener;
import com.admarvel.android.util.AdHistoryDumpUtils;
import com.admarvel.android.util.AdMarvelConnectivityChangeReceiver;
import com.admarvel.android.util.AdMarvelLocationManager;
import com.admarvel.android.util.AdMarvelSensorManager;
import com.admarvel.android.util.AdMarvelThreadExecutorService;
import com.admarvel.android.util.Logging;
import com.admarvel.android.util.Rotate3dAnimation;
import com.admarvel.android.util.p000a.OfflineReflectionUtils;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;

@SuppressLint({"NewApi"})
public class AdMarvelView extends LinearLayout implements AdMarvelActivityLifecycleCallbacksListener {
    private static WeakReference<Activity> activityReference;
    private static boolean enableHardwareAcceleration;
    protected static boolean enableLogDump;
    public static boolean enableOfflineSDK;
    final String ADMARVEL_VIEW_GUID;
    private WeakReference<Activity> activityReferenceLocal;
    private int adContainerWidth;
    private AdMarvelAd admarvelAd;
    private int backgroundColor;
    private boolean disableAnimation;
    private boolean disableSDKImpressionTracking;
    private boolean enableAutoScaling;
    private boolean enableClickRedirect;
    private boolean enableFitToScreenForTablets;
    private final C0214h internalAdMarvelAdapterListener;
    private final C0215i internalAdmarvelListener;
    private boolean isAdFetchedModel;
    private boolean isAdNetworkAdExpanded;
    boolean isBannerNativeListenersSet;
    boolean isBannerNativeRequest;
    private boolean isPostitialView;
    private final AdMarvelViewListenerImpl listenerImpl;
    private final AtomicLong lockTimestamp;
    AdMarvelNativeAdListener nativeAdListener;
    AdMarvelNativeVideoAdListener nativeVideoAdListener;
    private boolean setSoftwareLayer;
    private int textBackgroundColor;
    private int textBorderColor;
    private int textFontColor;
    AdMarvelVideoEventListener videoEventListener;

    /* renamed from: com.admarvel.android.ads.AdMarvelView.1 */
    class C02061 implements AnimationListener {
        final /* synthetic */ View f344a;
        final /* synthetic */ AdMarvelAd f345b;
        final /* synthetic */ AdMarvelView f346c;

        C02061(AdMarvelView adMarvelView, View view, AdMarvelAd adMarvelAd) {
            this.f346c = adMarvelView;
            this.f344a = view;
            this.f345b = adMarvelAd;
        }

        public void onAnimationEnd(Animation animation) {
            this.f346c.post(new C0217k(this.f344a, this.f346c, this.f345b));
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    public interface AdMarvelViewExtendedListener {
        void onAdDisplayed(AdMarvelView adMarvelView);

        void onAdFetched(AdMarvelView adMarvelView, AdMarvelAd adMarvelAd);
    }

    public interface AdMarvelViewListener {
        void onClickAd(AdMarvelView adMarvelView, String str);

        void onClose(AdMarvelView adMarvelView);

        void onExpand(AdMarvelView adMarvelView);

        void onFailedToReceiveAd(AdMarvelView adMarvelView, int i, ErrorReason errorReason);

        void onReceiveAd(AdMarvelView adMarvelView);

        void onRequestAd(AdMarvelView adMarvelView);
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.a */
    private static class C0207a implements Runnable {
        private final WeakReference<Context> f347a;
        private final Map<String, Object> f348b;
        private final String f349c;
        private final String f350d;
        private final String f351e;
        private final int f352f;
        private final String f353g;
        private final WeakReference<AdMarvelView> f354h;
        private final int f355i;
        private final String f356j;

        public C0207a(Context context, Map<String, Object> map, String str, String str2, String str3, int i, String str4, AdMarvelView adMarvelView, int i2, String str5) {
            this.f347a = new WeakReference(context);
            this.f348b = map;
            this.f349c = str;
            this.f350d = str2;
            this.f351e = str3;
            this.f352f = i;
            this.f353g = str4;
            this.f354h = new WeakReference(adMarvelView);
            this.f355i = i2;
            this.f356j = str5;
        }

        public void run() {
            Context context = (Context) this.f347a.get();
            AdMarvelView adMarvelView = (AdMarvelView) this.f354h.get();
            if (context != null && adMarvelView != null) {
                if (Version.getAndroidSDKVersion() >= 11) {
                    AdMarvelThreadExecutorService.m597a().m598b().execute(new C0208b(context, this.f348b, this.f349c, this.f350d, this.f351e, this.f352f, this.f353g, adMarvelView, this.f355i, this.f356j, adMarvelView.isBannerNativeRequest, adMarvelView.isBannerNativeListenersSet));
                    return;
                }
                new AdMarvelViewAsyncTask(context).execute(new Object[]{this.f348b, this.f349c, this.f350d, this.f351e, Integer.valueOf(this.f352f), this.f353g, adMarvelView, Integer.valueOf(this.f355i), this.f356j, Boolean.valueOf(adMarvelView.isSoftwareLayer()), Boolean.valueOf(adMarvelView.enableAutoScaling), Boolean.valueOf(adMarvelView.isBannerNativeRequest), Boolean.valueOf(adMarvelView.isBannerNativeListenersSet)});
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.b */
    private static class C0208b implements Runnable {
        boolean f357a;
        boolean f358b;
        private final WeakReference<Context> f359c;
        private final Map<String, Object> f360d;
        private final String f361e;
        private final String f362f;
        private final String f363g;
        private final int f364h;
        private final String f365i;
        private final WeakReference<AdMarvelView> f366j;
        private final int f367k;
        private final String f368l;

        public C0208b(Context context, Map<String, Object> map, String str, String str2, String str3, int i, String str4, AdMarvelView adMarvelView, int i2, String str5, boolean z, boolean z2) {
            this.f359c = new WeakReference(context);
            this.f360d = map;
            this.f361e = str;
            this.f362f = str2;
            this.f363g = str3;
            this.f364h = i;
            this.f365i = str4;
            this.f366j = new WeakReference(adMarvelView);
            this.f367k = i2;
            this.f368l = str5;
            this.f358b = z2;
            this.f357a = z;
        }

        @SuppressLint({"NewApi"})
        public void run() {
            if (this.f359c.get() != null && this.f366j.get() != null) {
                new AdMarvelViewAsyncTask((Context) this.f359c.get()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[]{this.f360d, this.f361e, this.f362f, this.f363g, Integer.valueOf(this.f364h), this.f365i, this.f366j.get(), Integer.valueOf(this.f367k), this.f368l, Boolean.valueOf(((AdMarvelView) this.f366j.get()).isSoftwareLayer()), Boolean.valueOf(((AdMarvelView) this.f366j.get()).enableAutoScaling), Boolean.valueOf(this.f357a), Boolean.valueOf(this.f358b)});
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.c */
    private static class C0209c implements Runnable {
        private final WeakReference<AdMarvelView> f369a;

        public C0209c(AdMarvelView adMarvelView) {
            this.f369a = new WeakReference(adMarvelView);
        }

        public void run() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f369a.get();
            if (adMarvelView != null) {
                adMarvelView.cleanupWebView(adMarvelView.findViewWithTag("CURRENT"));
                if (!adMarvelView.isAdNetworkAdExpanded) {
                    adMarvelView.destroyAdapterView(adMarvelView);
                }
                adMarvelView.removeAllViews();
                if (AdMarvelView.activityReference != null) {
                    AdMarvelView.activityReference.clear();
                }
                try {
                    adMarvelView.getContext().getApplicationContext().unregisterReceiver(AdMarvelConnectivityChangeReceiver.m559a());
                    AdMarvelSensorManager a = AdMarvelSensorManager.m579a();
                    if (a != null && a.m593b()) {
                        a.m595c();
                    }
                    AdMarvelLocationManager a2 = AdMarvelLocationManager.m566a();
                    if (a2 != null) {
                        a2.m573a(adMarvelView.getContext());
                    }
                } catch (Exception e) {
                }
                AdMarvelAdapterInstances.destroyAdMarvelAdapterInstances(adMarvelView.ADMARVEL_VIEW_GUID);
                AdMarvelInternalWebView.m290d();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.d */
    private static class C0210d implements Runnable {
        private final AdMarvelAd f370a;
        private final WeakReference<AdMarvelView> f371b;

        public C0210d(AdMarvelView adMarvelView, AdMarvelAd adMarvelAd) {
            this.f370a = adMarvelAd;
            this.f371b = new WeakReference(adMarvelView);
        }

        public void run() {
            try {
                AdMarvelView adMarvelView = (AdMarvelView) this.f371b.get();
                if (adMarvelView != null) {
                    Context context = adMarvelView.getContext();
                    if (context != null) {
                        View findViewWithTag = adMarvelView.findViewWithTag("PENDING");
                        if (findViewWithTag != null) {
                            if (AdMarvelUtils.isLogDumpEnabled() && this.f370a != null) {
                                this.f370a.setResponseJson();
                            }
                            View findViewWithTag2 = adMarvelView.findViewWithTag("CURRENT");
                            if (findViewWithTag2 == null || adMarvelView.disableAnimation || !findViewWithTag2.isShown()) {
                                adMarvelView.cleanupWebView(findViewWithTag2);
                                adMarvelView.applyFadeIn();
                                findViewWithTag.setTag("CURRENT");
                                findViewWithTag.setVisibility(0);
                                adMarvelView.setHorizontalGravity(1);
                                adMarvelView.removeAllViews();
                                adMarvelView.addView(findViewWithTag);
                                if (!adMarvelView.isAdNetworkAdExpanded) {
                                    adMarvelView.cleanupAdapterView(findViewWithTag2);
                                }
                                if (this.f370a != null) {
                                    if (adMarvelView.isAdFetchedModel()) {
                                        adMarvelView.listenerImpl.m427b(context, adMarvelView, this.f370a.getSiteId(), this.f370a.getId(), this.f370a.getTargetParams(), this.f370a.getIpAddress());
                                    } else {
                                        adMarvelView.listenerImpl.m421a(context, adMarvelView, this.f370a.getSiteId(), this.f370a.getId(), this.f370a.getTargetParams(), this.f370a.getIpAddress());
                                    }
                                }
                                if (AdMarvelUtils.isLogDumpEnabled()) {
                                    new Handler().postDelayed(new C0211e(this.f370a, context), 1000);
                                }
                            } else {
                                adMarvelView.applyRotation(findViewWithTag, this.f370a);
                            }
                            Utils utils = new Utils(context);
                            if (AdMarvelView.enableOfflineSDK && !adMarvelView.disableSDKImpressionTracking) {
                                new OfflineReflectionUtils().m536a(this.f370a, context, new Handler());
                            } else if (!AdMarvelView.enableOfflineSDK) {
                                utils.m244a(this.f370a);
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.e */
    private static class C0211e implements Runnable {
        private final AdMarvelAd f372a;
        private final Context f373b;

        public C0211e(AdMarvelAd adMarvelAd, Context context) {
            this.f372a = adMarvelAd;
            if (!(context == null || (context instanceof Activity) || AdMarvelView.activityReference == null || AdMarvelView.activityReference.get() == null)) {
                context = (Context) AdMarvelView.activityReference.get();
            }
            this.f373b = context;
        }

        public void run() {
            if (this.f373b != null && this.f372a != null) {
                AdHistoryDumpUtils b = AdHistoryDumpUtils.m550b(this.f373b);
                if (b != null) {
                    int a = b.m555a(this.f373b);
                    this.f372a.setAdHistoryCounter(a);
                    b.m558a(this.f372a.getAdHistoryDumpString(), a);
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.f */
    private static class C0212f implements Runnable {
        private final WeakReference<AdMarvelView> f374a;

        public C0212f(AdMarvelView adMarvelView) {
            this.f374a = new WeakReference(adMarvelView);
        }

        public void run() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f374a.get();
            if (adMarvelView != null) {
                Handler handler = new Handler();
                Context context = adMarvelView.getContext();
                View findViewWithTag = adMarvelView.findViewWithTag("CURRENT");
                if (findViewWithTag != null && (findViewWithTag instanceof AdMarvelWebView)) {
                    AdMarvelAd adMarvelAd = ((AdMarvelWebView) findViewWithTag).getAdMarvelAd();
                    if (adMarvelAd != null) {
                        new OfflineReflectionUtils().m536a(adMarvelAd, context, handler);
                    }
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.g */
    private static class C0213g implements Runnable {
        private final WeakReference<AdMarvelView> f375a;

        public C0213g(AdMarvelView adMarvelView) {
            this.f375a = new WeakReference(adMarvelView);
        }

        public void run() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f375a.get();
            if (adMarvelView != null) {
                View findViewWithTag = adMarvelView.findViewWithTag("CURRENT");
                if (findViewWithTag != null && (findViewWithTag instanceof AdMarvelWebView)) {
                    ((AdMarvelWebView) findViewWithTag).m484f();
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.h */
    private static class C0214h implements AdMarvelAdapterListener {
        private final WeakReference<AdMarvelView> f376a;

        public C0214h(AdMarvelView adMarvelView) {
            this.f376a = new WeakReference(adMarvelView);
        }

        public void onAdMarvelVideoEvent(AdMarvelVideoEvents adMarvelVideoEvent, Map<String, String> customEventParams) {
            AdMarvelView adMarvelView = (AdMarvelView) this.f376a.get();
            if (adMarvelView != null && adMarvelView.getVideoEventListener() != null) {
                Logging.log("onAdMarvelVideoEvent");
                adMarvelView.getVideoEventListener().onAdMarvelVideoEvent(adMarvelVideoEvent, customEventParams);
            }
        }

        public void onAudioStart() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f376a.get();
            if (adMarvelView != null && adMarvelView.getVideoEventListener() != null) {
                adMarvelView.getVideoEventListener().onAudioStart();
            }
        }

        public void onAudioStop() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f376a.get();
            if (adMarvelView != null && adMarvelView.getVideoEventListener() != null) {
                adMarvelView.getVideoEventListener().onAudioStop();
            }
        }

        public void onClickAd(String clickUrl) {
            AdMarvelView adMarvelView = (AdMarvelView) this.f376a.get();
            if (adMarvelView != null) {
                AdMarvelAd access$1000 = adMarvelView.admarvelAd;
                if (access$1000 != null) {
                    adMarvelView.listenerImpl.m422a(adMarvelView.getContext(), adMarvelView, clickUrl, access$1000.getSiteId(), access$1000.getId(), access$1000.getTargetParams(), access$1000.getIpAddress());
                }
            }
        }

        public void onClose() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f376a.get();
            if (adMarvelView != null) {
                adMarvelView.isAdNetworkAdExpanded = false;
                adMarvelView.listenerImpl.m429c(adMarvelView);
            }
        }

        public void onExpand() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f376a.get();
            if (adMarvelView != null) {
                adMarvelView.isAdNetworkAdExpanded = true;
                adMarvelView.listenerImpl.m428b(adMarvelView);
            }
        }

        public void onFailedToReceiveAd(int errorCode, ErrorReason errorReason) {
            AdMarvelView adMarvelView = (AdMarvelView) this.f376a.get();
            if (adMarvelView != null) {
                AdMarvelAd access$1000 = adMarvelView.admarvelAd;
                new Handler(Looper.getMainLooper()).post(new C0216j(adMarvelView));
                Object obj = null;
                if (access$1000 != null && access$1000.getAdType() == AdType.SDKCALL) {
                    if (access$1000.getRetry() != null && access$1000.getRetry().equals(Boolean.valueOf(true)) && access$1000.getRetrynum() <= access$1000.getMaxretries()) {
                        int retrynum = access$1000.getRetrynum() + 1;
                        String bannerid = access$1000.getExcluded() == null ? access$1000.getBannerid() : access$1000.getExcluded().length() > 0 ? access$1000.getExcluded() + Stomp.COMMA + access$1000.getBannerid() : access$1000.getBannerid();
                        if (adMarvelView.getContext() != null) {
                            Logging.log("Retry : onRequestAd");
                            new Handler(Looper.getMainLooper()).post(new C0207a(adMarvelView.getContext(), access$1000.getTargetParams(), access$1000.getPartnerId(), access$1000.getSiteId(), access$1000.getAndroidId(), access$1000.getOrientation(), access$1000.getDeviceConnectivity(), adMarvelView, retrynum, bannerid));
                        }
                        obj = 1;
                    }
                    if (obj == null) {
                        adMarvelView.listenerImpl.m420a(adMarvelView.getContext(), adMarvelView, errorCode, errorReason, access$1000.getSiteId(), access$1000.getId(), access$1000.getTargetParams(), access$1000.getIpAddress());
                    }
                }
            }
        }

        public void onReceiveAd() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f376a.get();
            if (adMarvelView != null) {
                new Handler(Looper.getMainLooper()).post(new C0210d(adMarvelView, adMarvelView.admarvelAd));
            }
        }

        public void onReceiveNativeAd(Object nativeAd) {
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.i */
    private static class C0215i implements AdMarvelWebViewListener {
        private final WeakReference<AdMarvelView> f377a;

        public C0215i(AdMarvelView adMarvelView) {
            this.f377a = new WeakReference(adMarvelView);
        }

        public void m156a() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f377a.get();
            if (adMarvelView != null) {
                adMarvelView.listenerImpl.m428b(adMarvelView);
            }
        }

        public void m157a(AdMarvelAd adMarvelAd, String str) {
            AdMarvelView adMarvelView = (AdMarvelView) this.f377a.get();
            if (adMarvelView != null) {
                adMarvelView.listenerImpl.m422a(adMarvelView.getContext(), adMarvelView, str, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress());
            }
        }

        public void m158a(AdMarvelWebView adMarvelWebView, AdMarvelAd adMarvelAd) {
            AdMarvelView adMarvelView = (AdMarvelView) this.f377a.get();
            if (adMarvelView != null) {
                new Handler(Looper.getMainLooper()).post(new C0210d(adMarvelView, adMarvelAd));
            }
        }

        public void m159a(AdMarvelWebView adMarvelWebView, AdMarvelAd adMarvelAd, int i, ErrorReason errorReason) {
            AdMarvelView adMarvelView = (AdMarvelView) this.f377a.get();
            if (adMarvelView != null) {
                View findViewWithTag = adMarvelView.findViewWithTag("PENDING");
                if (findViewWithTag != null) {
                    adMarvelView.removeView(findViewWithTag);
                }
                if (adMarvelAd != null) {
                    adMarvelView.listenerImpl.m420a(adMarvelView.getContext(), adMarvelView, i, errorReason, adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress());
                }
            }
        }

        public void m160b() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f377a.get();
            if (adMarvelView != null) {
                adMarvelView.listenerImpl.m429c(adMarvelView);
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.j */
    private static class C0216j implements Runnable {
        final WeakReference<AdMarvelView> f378a;

        public C0216j(AdMarvelView adMarvelView) {
            this.f378a = new WeakReference(adMarvelView);
        }

        public void run() {
            AdMarvelView adMarvelView = (AdMarvelView) this.f378a.get();
            if (adMarvelView != null) {
                View findViewWithTag = adMarvelView.findViewWithTag("PENDING");
                if (findViewWithTag != null) {
                    adMarvelView.removeView(findViewWithTag);
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelView.k */
    private static class C0217k implements Runnable {
        private final WeakReference<View> f379a;
        private final WeakReference<AdMarvelView> f380b;
        private final AdMarvelAd f381c;

        public C0217k(View view, AdMarvelView adMarvelView, AdMarvelAd adMarvelAd) {
            this.f379a = new WeakReference(view);
            this.f380b = new WeakReference(adMarvelView);
            this.f381c = adMarvelAd;
        }

        public void run() {
            if (this.f379a != null && this.f379a.get() != null && this.f380b != null) {
                View view = (View) this.f379a.get();
                AdMarvelView adMarvelView = (AdMarvelView) this.f380b.get();
                if (adMarvelView != null) {
                    View findViewWithTag = adMarvelView.findViewWithTag("CURRENT");
                    if (findViewWithTag != null) {
                        adMarvelView.cleanupWebView(findViewWithTag);
                        adMarvelView.applyFadeIn();
                        view.setVisibility(0);
                        view.setTag("CURRENT");
                        adMarvelView.removeAllViews();
                        adMarvelView.addView(view);
                        if (!adMarvelView.isAdNetworkAdExpanded) {
                            adMarvelView.cleanupAdapterView(findViewWithTag);
                        }
                        Animation rotate3dAnimation = new Rotate3dAnimation(90.0f, 0.0f, ((float) adMarvelView.getWidth()) / 2.0f, ((float) adMarvelView.getHeight()) / 2.0f, Constants.ANIMATION_Z_DEPTH_PERCENTAGE * ((float) adMarvelView.getWidth()), false);
                        rotate3dAnimation.setDuration(700);
                        rotate3dAnimation.setFillAfter(true);
                        rotate3dAnimation.setInterpolator(new DecelerateInterpolator());
                        adMarvelView.startAnimation(rotate3dAnimation);
                        if (this.f381c != null) {
                            if (adMarvelView.isAdFetchedModel()) {
                                adMarvelView.listenerImpl.m427b(adMarvelView.getContext(), adMarvelView, this.f381c.getSiteId(), this.f381c.getId(), this.f381c.getTargetParams(), this.f381c.getIpAddress());
                            } else {
                                adMarvelView.listenerImpl.m421a(adMarvelView.getContext(), adMarvelView, this.f381c.getSiteId(), this.f381c.getId(), this.f381c.getTargetParams(), this.f381c.getIpAddress());
                            }
                            if (AdMarvelUtils.isLogDumpEnabled()) {
                                new Handler().postDelayed(new C0211e(this.f381c, adMarvelView.getContext()), 1000);
                            }
                        }
                    }
                }
            }
        }
    }

    static {
        enableHardwareAcceleration = true;
        enableLogDump = true;
    }

    public AdMarvelView(Context context) {
        this(context, null);
    }

    public AdMarvelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isAdNetworkAdExpanded = false;
        this.enableClickRedirect = true;
        this.adContainerWidth = -1;
        this.nativeAdListener = null;
        this.isBannerNativeRequest = false;
        this.isBannerNativeListenersSet = false;
        this.setSoftwareLayer = false;
        this.enableAutoScaling = true;
        this.enableFitToScreenForTablets = false;
        this.disableSDKImpressionTracking = false;
        this.isAdFetchedModel = false;
        this.isPostitialView = false;
        if (context != null && (context instanceof Activity)) {
            this.activityReferenceLocal = new WeakReference((Activity) context);
        }
        this.listenerImpl = new AdMarvelViewListenerImpl();
        this.enableAutoScaling = true;
        this.ADMARVEL_VIEW_GUID = UUID.randomUUID().toString();
        AdMarvelAdapterInstances.buildAdMarvelAdapterInstances(this.ADMARVEL_VIEW_GUID);
        setFocusable(true);
        setDescendantFocusability(AccessibilityNodeInfoCompat.ACTION_EXPAND);
        setClickable(true);
        String packageName = new StringBuilder().append("http://schemas.android.com/apk/res/").append(context).toString() != null ? context.getPackageName() : Stomp.EMPTY;
        if (attrs != null) {
            if (attrs.getAttributeValue(packageName, "backgroundColor") != null) {
                if ("0".equals(attrs.getAttributeValue(packageName, "backgroundColor"))) {
                    this.backgroundColor = 0;
                } else {
                    this.backgroundColor = Integer.parseInt(attrs.getAttributeValue(packageName, "backgroundColor").replace("#", Stomp.EMPTY), 16);
                }
            }
            if (attrs.getAttributeValue(packageName, "textBackgroundColor") != null) {
                this.textBackgroundColor = Integer.parseInt(attrs.getAttributeValue(packageName, "textBackgroundColor").replace("#", Stomp.EMPTY), 16);
            }
            if (attrs.getAttributeValue(packageName, "textFontColor") != null) {
                this.textFontColor = Integer.parseInt(attrs.getAttributeValue(packageName, "textFontColor").replace("#", Stomp.EMPTY), 16);
            }
            if (attrs.getAttributeValue(packageName, "textBorderColor") != null) {
                this.textBorderColor = Integer.parseInt(attrs.getAttributeValue(packageName, "textBorderColor").replace("#", Stomp.EMPTY), 16);
            }
            if (attrs.getAttributeValue(packageName, "disableAnimation") != null) {
                this.disableAnimation = Boolean.parseBoolean(attrs.getAttributeValue(packageName, "disableAnimation"));
            }
            if (attrs.getAttributeValue(packageName, "enableClickRedirect") != null) {
                this.enableClickRedirect = Boolean.parseBoolean(attrs.getAttributeValue(packageName, "enableClickRedirect"));
            }
            setAdMarvelBackgroundColor(this.backgroundColor);
        }
        this.lockTimestamp = new AtomicLong(0);
        this.internalAdMarvelAdapterListener = new C0214h(this);
        this.internalAdmarvelListener = new C0215i(this);
        if (Version.getAndroidSDKVersion() >= 14) {
            AdMarvelActivityLifecycleCallbacksListener.m248a().m250a(this.ADMARVEL_VIEW_GUID, this);
        }
    }

    private void applyFadeIn() {
        if (!this.disableAnimation) {
            Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(233);
            alphaAnimation.startNow();
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setInterpolator(new AccelerateInterpolator());
            startAnimation(alphaAnimation);
        }
    }

    private void applyRotation(View view, AdMarvelAd adMarvelAd) {
        if (!this.disableAnimation) {
            setVisibility(8);
            setVisibility(0);
            Animation rotate3dAnimation = new Rotate3dAnimation(0.0f, -90.0f, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, Constants.ANIMATION_Z_DEPTH_PERCENTAGE * ((float) getWidth()), true);
            rotate3dAnimation.setDuration(700);
            rotate3dAnimation.setFillAfter(true);
            rotate3dAnimation.setInterpolator(new AccelerateInterpolator());
            rotate3dAnimation.setAnimationListener(new C02061(this, view, adMarvelAd));
            startAnimation(rotate3dAnimation);
        }
    }

    private void cleanupAdapterView(View currentView) {
        View view = null;
        if (currentView instanceof FrameLayout) {
            view = ((FrameLayout) currentView).getChildAt(0);
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.GOOGLEPLAY_SDK_ADAPTER_FULL_CLASSNAME).cleanupView(view);
        } catch (Exception e) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.RHYTHM_SDK_ADAPTER_FULL_CLASSNAME).cleanupView(view);
        } catch (Exception e2) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.MILLENNIAL_SDK_APAPTER_FULL_CLASSNAME).cleanupView(view);
        } catch (Exception e3) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.AMAZON_SDK_APAPTER_FULL_CLASSNAME).cleanupView(view);
        } catch (Exception e4) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME).cleanupView(view);
        } catch (Exception e5) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.FACEBOOK_SDK_APAPTER_FULL_CLASSNAME).cleanupView(view);
        } catch (Exception e6) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.INMOBI_SDK_APAPTER_FULL_CLASSNAME).cleanupView(view);
        } catch (Exception e7) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.HEYZAP_SDK_APAPTER_FULL_CLASSNAME).cleanupView(view);
        } catch (Exception e8) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME).cleanupView(view);
        } catch (Exception e9) {
        }
    }

    private void cleanupWebView(View view) {
        if (view != null && (view instanceof AdMarvelWebView)) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) view;
            adMarvelWebView.m482e();
            adMarvelWebView.m481d();
        }
    }

    private void destroyAdapterView(AdMarvelView adMarvelView) {
        View findViewWithTag = adMarvelView.findViewWithTag("CURRENT");
        if (findViewWithTag != null) {
            findViewWithTag = findViewWithTag instanceof FrameLayout ? ((FrameLayout) findViewWithTag).getChildAt(0) : null;
            Logging.log("destroyAdapterView");
            try {
                AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.GOOGLEPLAY_SDK_ADAPTER_FULL_CLASSNAME).destroy(findViewWithTag);
            } catch (Exception e) {
            }
            try {
                AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.RHYTHM_SDK_ADAPTER_FULL_CLASSNAME).destroy(findViewWithTag);
            } catch (Exception e2) {
            }
            try {
                AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.MILLENNIAL_SDK_APAPTER_FULL_CLASSNAME).destroy(findViewWithTag);
            } catch (Exception e3) {
            }
            try {
                AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.AMAZON_SDK_APAPTER_FULL_CLASSNAME).destroy(findViewWithTag);
            } catch (Exception e4) {
            }
            try {
                AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME).destroy(findViewWithTag);
            } catch (Exception e5) {
            }
            try {
                AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.FACEBOOK_SDK_APAPTER_FULL_CLASSNAME).destroy(findViewWithTag);
            } catch (Exception e6) {
            }
            try {
                AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.INMOBI_SDK_APAPTER_FULL_CLASSNAME).destroy(findViewWithTag);
            } catch (Exception e7) {
            }
            try {
                AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.HEYZAP_SDK_APAPTER_FULL_CLASSNAME).destroy(findViewWithTag);
            } catch (Exception e8) {
            }
            try {
                AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME).destroy(findViewWithTag);
            } catch (Exception e9) {
            }
        }
    }

    public static synchronized void disableNetworkActivity() {
        synchronized (AdMarvelView.class) {
            OfflineReflectionUtils.m532a();
        }
    }

    public static synchronized void enableNetworkActivity(Activity activity, String partnerId) {
        synchronized (AdMarvelView.class) {
            OfflineReflectionUtils.m534b(activity, partnerId);
        }
    }

    public static void initializeOfflineSDK(Activity activity, String partnerId) {
        enableOfflineSDK = true;
        OfflineReflectionUtils.m533a(activity, partnerId);
    }

    static boolean isEnableHardwareAcceleration() {
        return enableHardwareAcceleration;
    }

    public static void setEnableHardwareAcceleration(boolean enableHardwareAccelerationFlag) {
        Logging.log("AdMarvelView - setEnableHardwareAcceleration :" + enableHardwareAccelerationFlag);
        enableHardwareAcceleration = enableHardwareAccelerationFlag;
    }

    public void adMarvelViewDisplayed() {
        AdMarvelThreadExecutorService.m597a().m598b().execute(new C0212f(this));
    }

    public void cleanup() {
        Logging.log("AdMarvelView - cleanup");
        AdMarvelActivityLifecycleCallbacksListener.m248a().m251b(this.ADMARVEL_VIEW_GUID);
        internalDestroy();
    }

    public void collapse() {
        Logging.log("AdMarvelView - Collapse");
        View findViewWithTag = findViewWithTag("CURRENT");
        if (findViewWithTag != null && (findViewWithTag instanceof AdMarvelWebView)) {
            ((AdMarvelWebView) findViewWithTag).m482e();
        }
    }

    public void destroy() {
        if (Version.getAndroidSDKVersion() < 14) {
            internalDestroy();
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
                this.listenerImpl.m420a(getContext(), this, 304, Utils.m178a(304), adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress());
            }
        }
    }

    public void displayAd(Context activityContext, AdMarvelAd admarvelAd) {
        this.admarvelAd = admarvelAd;
        if (admarvelAd != null && activityContext != null) {
            try {
                if (admarvelAd.getAdType() == AdType.SDKCALL) {
                    if (admarvelAd.getSdkNetwork() != null) {
                        requestPendingAd(admarvelAd.getTargetParams(), admarvelAd, admarvelAd.getSdkNetwork(), activityContext);
                        return;
                    } else if (admarvelAd.isDisableAdrequest()) {
                        String disableAdDuration = admarvelAd.getDisableAdDuration();
                        if (disableAdDuration != null) {
                            disableAdRequest(disableAdDuration, admarvelAd, activityContext);
                            return;
                        }
                        return;
                    }
                }
                requestInternalPendingAd(admarvelAd);
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                ErrorReason a = Utils.m178a(303);
                getListenerImpl().m420a(activityContext, this, Utils.m177a(a), a, admarvelAd.getSiteId(), admarvelAd.getId(), admarvelAd.getTargetParams(), admarvelAd.getIpAddress());
            }
        }
    }

    public void enableAdFetchedModel(boolean isAdFetchedModel) {
        this.isAdFetchedModel = isAdFetchedModel;
    }

    public void fetchNewAd(Map<String, Object> targetParams, String partnerId, String siteId) {
        enableAdFetchedModel(true);
        requestNewAd(targetParams, partnerId, siteId);
    }

    public void fetchNewAd(Map<String, Object> targetParams, String partnerId, String siteId, Activity context) {
        enableAdFetchedModel(true);
        requestNewAd(targetParams, partnerId, siteId, context);
    }

    public void focus() {
        Logging.log("AdMarvelView - focus");
        new Handler(Looper.getMainLooper()).post(new C0213g(this));
    }

    int getAdContainerWidth() {
        return this.adContainerWidth;
    }

    public int getAdMarvelBackgroundColor() {
        return this.backgroundColor;
    }

    AdMarvelViewListenerImpl getListenerImpl() {
        return this.listenerImpl;
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

    public void internalDestroy() {
        Logging.log("AdMarvelView - internalDestroy");
        new Handler(Looper.getMainLooper()).post(new C0209c(this));
    }

    public void internalPause(Activity activity) {
        if (isCurrentlyVisible()) {
            Logging.log("AdMarvelView - Pause");
            View findViewWithTag = findViewWithTag("CURRENT");
            if (findViewWithTag instanceof AdMarvelWebView) {
                ((AdMarvelWebView) findViewWithTag).m474a();
            }
            if (findViewWithTag instanceof FrameLayout) {
                View childAt = ((FrameLayout) findViewWithTag).getChildAt(0);
                try {
                    AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.GOOGLEPLAY_SDK_ADAPTER_FULL_CLASSNAME).pause(activity, childAt);
                } catch (Exception e) {
                }
                try {
                    AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.VERVE_SDK_ADAPTER_FULL_CLASSNAME).pause(activity, childAt);
                } catch (Exception e2) {
                }
            }
            try {
                AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, activity).pause();
                return;
            } catch (Exception e3) {
                return;
            }
        }
        Logging.log("AdMarvelView - not visible or no parent ");
    }

    public void internalResume(Activity activity) {
        if (isCurrentlyVisible()) {
            Logging.log("AdMarvelView - Resume");
            View findViewWithTag = findViewWithTag("CURRENT");
            if (findViewWithTag != null && (findViewWithTag instanceof AdMarvelWebView)) {
                ((AdMarvelWebView) findViewWithTag).m479b();
            }
            if (findViewWithTag != null && (findViewWithTag instanceof FrameLayout)) {
                View childAt = ((FrameLayout) findViewWithTag).getChildAt(0);
                try {
                    AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.GOOGLEPLAY_SDK_ADAPTER_FULL_CLASSNAME).resume(activity, childAt);
                } catch (Exception e) {
                }
                try {
                    AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.VERVE_SDK_ADAPTER_FULL_CLASSNAME).resume(activity, childAt);
                } catch (Exception e2) {
                }
            }
            try {
                AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, activity).resume();
                return;
            } catch (Exception e3) {
                return;
            }
        }
        Logging.log("AdMarvelView - not visible or no parent ");
    }

    public boolean isAdExpanded() {
        Logging.log("AdMarvelView - isAdExpanded");
        View findViewWithTag = findViewWithTag("CURRENT");
        return (findViewWithTag == null || !(findViewWithTag instanceof AdMarvelWebView)) ? false : ((AdMarvelWebView) findViewWithTag).f893y;
    }

    public boolean isAdFetchedModel() {
        return this.isAdFetchedModel;
    }

    public boolean isAutoScalingEnabled() {
        return this.enableAutoScaling;
    }

    boolean isCurrentlyVisible() {
        int[] iArr = new int[]{-1, -1};
        getLocationOnScreen(iArr);
        return iArr[1] > 0 && iArr[1] >= 0 && iArr[1] < Utils.m224n(getContext());
    }

    public boolean isDisableAnimation() {
        return this.disableAnimation;
    }

    public boolean isEnableClickRedirect() {
        return this.enableClickRedirect;
    }

    public boolean isEnableFitToScreenForTablets() {
        return this.enableFitToScreenForTablets;
    }

    public boolean isPostitialView() {
        return this.isPostitialView;
    }

    public boolean isSoftwareLayer() {
        return this.setSoftwareLayer;
    }

    public void loadNativeVideoContent(String xhtml, Map<String, Object> targetParams, String partnerId, String siteId, int orientation, String deviceConnectivity) {
        AdMarvelAd adMarvelAd = new AdMarvelAd(xhtml, targetParams, partnerId, siteId, null, orientation, deviceConnectivity, null);
        adMarvelAd.setXhtml(xhtml);
        adMarvelAd.setAdType(AdType.JAVASCRIPT);
        adMarvelAd.setSource("campaign");
        requestInternalPendingAd(adMarvelAd);
    }

    public void notifyAddedToListView() {
        Logging.log("AdMarvelView - notifyAddedToListView ");
        View findViewWithTag = findViewWithTag("CURRENT");
        if (findViewWithTag instanceof AdMarvelWebView) {
            ((AdMarvelWebView) findViewWithTag).m480c();
        }
        if (findViewWithTag instanceof FrameLayout) {
            try {
                AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME).notifyAddedToListView(((FrameLayout) findViewWithTag).getChildAt(0));
            } catch (Exception e) {
            }
        }
    }

    public void onInternalDestroy(Activity activity) {
        if (activity != null && this.activityReferenceLocal != null && this.activityReferenceLocal.get() != null && ((Activity) this.activityReferenceLocal.get()).getClass().getName().equals(activity.getClass().getName())) {
            try {
                AdMarvelActivityLifecycleCallbacksListener.m248a().m249a(this.ADMARVEL_VIEW_GUID);
                internalDestroy();
                this.activityReferenceLocal.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onInternalPause(Activity activity) {
        if (activity != null && this.activityReferenceLocal != null && this.activityReferenceLocal.get() != null && ((Activity) this.activityReferenceLocal.get()).getClass().getName().equals(activity.getClass().getName())) {
            internalPause(activity);
        }
    }

    public void onInternalResume(Activity activity) {
        if (activity != null && this.activityReferenceLocal != null && this.activityReferenceLocal.get() != null && ((Activity) this.activityReferenceLocal.get()).getClass().getName().equals(activity.getClass().getName())) {
            internalResume(activity);
        }
    }

    public void pause(Activity activity) {
        if (Version.getAndroidSDKVersion() < 14) {
            internalPause(activity);
        }
    }

    public void requestBannerOrNativeAd(Map<String, Object> targetParams, String partnerId, String siteId) {
        if (!(this.listenerImpl == null || this.nativeAdListener == null)) {
            this.isBannerNativeListenersSet = true;
            this.isBannerNativeRequest = true;
        }
        requestNewAd(targetParams, partnerId, siteId);
    }

    protected void requestInternalPendingAd(AdMarvelAd adMarvelAd) {
        if (adMarvelAd != null) {
            float adContainerWidth;
            this.admarvelAd = adMarvelAd;
            if (getAdContainerWidth() > 0) {
                adContainerWidth = ((float) getAdContainerWidth()) / Utils.m226o(getContext());
            } else if (getWidth() > 0) {
                adContainerWidth = ((float) getWidth()) / Utils.m226o(getContext());
            } else {
                adContainerWidth = ((float) (Utils.m222m(getContext()) < Utils.m224n(getContext()) ? Utils.m222m(getContext()) : Utils.m224n(getContext()))) / Utils.m226o(getContext());
            }
            adMarvelAd.setAdMarvelViewWidth(adContainerWidth);
        }
        View findViewWithTag = findViewWithTag("CURRENT");
        if (findViewWithTag != null && (findViewWithTag instanceof AdMarvelWebView)) {
            ((AdMarvelWebView) findViewWithTag).m482e();
        }
        if (adMarvelAd != null) {
            findViewWithTag = (activityReference == null || activityReference.get() == null) ? new AdMarvelWebView(getContext(), this.enableAutoScaling, this.enableFitToScreenForTablets, adMarvelAd.getXml(), adMarvelAd, this.isAdFetchedModel, this.isPostitialView) : new AdMarvelWebView((Context) activityReference.get(), this.enableAutoScaling, this.enableFitToScreenForTablets, adMarvelAd.getXml(), adMarvelAd, this.isAdFetchedModel, this.isPostitialView);
            findViewWithTag.setLayoutParams(new LayoutParams(-2, -2));
            findViewWithTag.setBackgroundColor(this.backgroundColor);
            findViewWithTag.setEnableClickRedirect(this.enableClickRedirect);
            AdMarvelWebView.m457a(findViewWithTag.f888t, this.internalAdmarvelListener);
            findViewWithTag.setTag("PENDING");
            findViewWithTag.setVisibility(8);
            findViewWithTag.m476a(getTextFontColor(), getTextBorderColor(), getTextBackgroundColor(), getAdMarvelBackgroundColor(), this);
            while (true) {
                View findViewWithTag2 = findViewWithTag("PENDING");
                if (findViewWithTag2 == null) {
                    addView(findViewWithTag);
                    return;
                }
                removeView(findViewWithTag2);
            }
        }
    }

    public void requestNewAd(Map<String, Object> targetparams, String partnerId, String siteId) {
        try {
            if (!(Version.getAndroidSDKVersion() < 14 || AdMarvelUtils.isRegisteredForActivityLifecylceCallbacks || this.activityReferenceLocal == null || this.activityReferenceLocal.get() == null)) {
                ((Activity) this.activityReferenceLocal.get()).getApplication().registerActivityLifecycleCallbacks(AdMarvelActivityLifecycleCallbacksListener.m248a());
                AdMarvelUtils.isRegisteredForActivityLifecylceCallbacks = true;
            }
            Map map = null;
            if (targetparams != null) {
                map = new HashMap(targetparams);
            }
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Utils.m205d("admarvel"), 0);
            String str = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
            int i = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
            str = str != null ? "duration" + str + i + AdMarvelUtils.getSDKVersion() : "duration" + i + AdMarvelUtils.getSDKVersion();
            if (str != null) {
                str = sharedPreferences.getString(Utils.m205d(str), null);
                if (str != null && str.length() > 0) {
                    if (DateFormat.getDateTimeInstance().parse(DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()))).before(DateFormat.getDateTimeInstance().parse(str))) {
                        Logging.log("requestNewAd: AD REQUEST BLOCKED, IGNORING REQUEST");
                        this.listenerImpl.m420a(getContext(), this, 304, Utils.m178a(304), siteId, 0, map, Stomp.EMPTY);
                        return;
                    }
                }
            }
            partnerId = partnerId.trim();
            siteId = siteId.trim();
            AdMarvelSensorManager a = AdMarvelSensorManager.m579a();
            if (a != null && a.m593b()) {
                a.m595c();
            }
            AdMarvelLocationManager a2 = AdMarvelLocationManager.m566a();
            if (a2 != null) {
                a2.m573a(getContext());
            }
            try {
                getContext().getApplicationContext().unregisterReceiver(AdMarvelConnectivityChangeReceiver.m559a());
            } catch (IllegalArgumentException e) {
            }
            if (System.currentTimeMillis() - this.lockTimestamp.getAndSet(System.currentTimeMillis()) > 2000) {
                this.listenerImpl.m425a(this);
                String str2 = null;
                if (map != null) {
                    str2 = (String) map.get("UNIQUE_ID");
                }
                new Handler(Looper.getMainLooper()).post(new C0207a(getContext(), map, partnerId, siteId, str2, Utils.m216j(getContext()), Utils.m181a(getContext()), this, 0, Stomp.EMPTY));
                return;
            }
            Logging.log("requestNewAd: AD REQUEST PENDING, IGNORING REQUEST");
            this.listenerImpl.m420a(getContext(), this, 304, Utils.m178a(304), siteId, 0, map, Stomp.EMPTY);
        } catch (Throwable e2) {
            Logging.log(Log.getStackTraceString(e2));
        }
    }

    public void requestNewAd(Map<String, Object> targetParams, String partnerId, String siteId, Activity activity) {
        activityReference = new WeakReference(activity);
        this.activityReferenceLocal = new WeakReference(activity);
        requestNewAd(targetParams, partnerId, siteId);
    }

    protected void requestPendingAd(Map<String, Object> targettingParams, AdMarvelAd adMarvelAd, String className, Context context) {
        if (adMarvelAd != null) {
            try {
                float adContainerWidth;
                this.admarvelAd = adMarvelAd;
                if (getAdContainerWidth() > 0) {
                    adContainerWidth = ((float) getAdContainerWidth()) / Utils.m226o(getContext());
                } else if (getWidth() > 0) {
                    adContainerWidth = ((float) getWidth()) / Utils.m226o(getContext());
                } else {
                    adContainerWidth = ((float) (Utils.m222m(getContext()) < Utils.m224n(getContext()) ? Utils.m222m(getContext()) : Utils.m224n(getContext()))) / Utils.m226o(getContext());
                }
                adMarvelAd.setAdMarvelViewWidth(adContainerWidth);
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                this.listenerImpl.m420a(getContext(), this, 304, Utils.m178a(304), adMarvelAd.getSiteId(), adMarvelAd.getId(), adMarvelAd.getTargetParams(), adMarvelAd.getIpAddress());
                return;
            }
        }
        AdMarvelAdapter instance = AdMarvelAdapterInstances.getInstance(this.ADMARVEL_VIEW_GUID, className);
        View requestNewAd = (activityReference == null || activityReference.get() == null) ? instance.requestNewAd(this.internalAdMarvelAdapterListener, context, adMarvelAd, targettingParams, this.backgroundColor, this.textFontColor) : instance.requestNewAd(this.internalAdMarvelAdapterListener, (Context) activityReference.get(), adMarvelAd, targettingParams, this.backgroundColor, this.textFontColor);
        if (requestNewAd != null) {
            while (true) {
                View findViewWithTag = findViewWithTag("PENDING");
                if (findViewWithTag == null) {
                    break;
                }
                removeView(findViewWithTag);
            }
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams != null && layoutParams.width < 0) {
                layoutParams.width = -1;
                setLayoutParams(layoutParams);
            }
            setGravity(1);
            View frameLayout = new FrameLayout(context);
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 1));
            if (requestNewAd.getLayoutParams() instanceof LayoutParams) {
                LayoutParams layoutParams2 = (LayoutParams) requestNewAd.getLayoutParams();
                layoutParams2.gravity = 1;
                frameLayout.addView(requestNewAd, layoutParams2);
            } else if (requestNewAd.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) requestNewAd.getLayoutParams();
                layoutParams3.gravity = 1;
                frameLayout.addView(requestNewAd, layoutParams3);
            } else if (requestNewAd.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) requestNewAd.getLayoutParams();
                layoutParams4.addRule(13);
                frameLayout.addView(requestNewAd, layoutParams4);
            } else {
                frameLayout.addView(requestNewAd, new FrameLayout.LayoutParams(-2, -2, 1));
            }
            frameLayout.setTag("PENDING");
            if (adMarvelAd.isMustBeVisible()) {
                removeAllViews();
                frameLayout.setVisibility(0);
            } else {
                frameLayout.setVisibility(8);
            }
            addView(frameLayout);
        }
    }

    public void resume(Activity activity) {
        if (Version.getAndroidSDKVersion() < 14) {
            internalResume(activity);
        }
    }

    public void setAdContainerWidth(int adContainerWidth) {
        Logging.log("AdMarvelView - setAdContainerWidth :" + adContainerWidth);
        this.adContainerWidth = adContainerWidth;
    }

    public void setAdMarvelBackgroundColor(int backgroundColor) {
        Logging.log("AdMarvelView - setAdMarvelBackgroundColor :" + backgroundColor);
        if (backgroundColor == 0) {
            this.backgroundColor = 0;
        } else {
            this.backgroundColor = ViewCompat.MEASURED_STATE_MASK | backgroundColor;
        }
        setBackgroundColor(this.backgroundColor);
    }

    public void setAdMarvelNativeAdListener(AdMarvelNativeAdListener nativeAdListener) {
        this.nativeAdListener = nativeAdListener;
    }

    public void setAdMarvelNativeVideoAdListener(AdMarvelNativeVideoAdListener nativeVideoAdListener) {
        this.nativeVideoAdListener = nativeVideoAdListener;
    }

    public void setAdmarvelWebViewAsSoftwareLayer(boolean setSoftwareLayerFlag) {
        Logging.log("AdMarvelView - setAdmarvelWebViewAsSoftwareLayer :" + setSoftwareLayerFlag);
        this.setSoftwareLayer = setSoftwareLayerFlag;
    }

    public void setDisableAnimation(boolean disableAnimation) {
        Logging.log("AdMarvelView - setDisableAnimation :" + disableAnimation);
        this.disableAnimation = disableAnimation;
    }

    public void setDisableSDKImpressionTracking(boolean disableSDKImpressionTracking) {
        Logging.log("AdMarvelView - setDisableSDKImpressionTracking :" + disableSDKImpressionTracking);
        this.disableSDKImpressionTracking = disableSDKImpressionTracking;
    }

    public void setEnableAutoScaling(boolean enableAutoScaling) {
        Logging.log("AdMarvelView - setEnableAutoScaling :" + enableAutoScaling);
        this.enableAutoScaling = enableAutoScaling;
    }

    public void setEnableClickRedirect(boolean enableClickRedirect) {
        Logging.log("AdMarvelView - setEnableClickRedirect :" + enableClickRedirect);
        this.enableClickRedirect = enableClickRedirect;
    }

    public void setEnableFitToScreenForTablets(boolean enableFitToScreenForTablets) {
        Logging.log("AdMarvelView - setEnableFitToScreenForTablets :" + enableFitToScreenForTablets);
        this.enableFitToScreenForTablets = enableFitToScreenForTablets;
    }

    public void setExtendedListener(AdMarvelViewExtendedListener extendedListener) {
        this.listenerImpl.m423a(extendedListener);
    }

    public void setListener(AdMarvelViewListener listener) {
        this.listenerImpl.m424a(listener);
    }

    public void setPostitialView(boolean isPostitialView) {
        Logging.log("AdMarvelView - setPostitialView :" + isPostitialView);
        this.isPostitialView = isPostitialView;
    }

    public void setTextBackgroundColor(int textBackgroundColor) {
        Logging.log("AdMarvelView - setTextBackgroundColor :" + textBackgroundColor);
        this.textBackgroundColor = ViewCompat.MEASURED_STATE_MASK | textBackgroundColor;
    }

    public void setTextBorderColor(int textBorderColor) {
        Logging.log("AdMarvelView - setTextBorderColor :" + textBorderColor);
        this.textBorderColor = textBorderColor;
    }

    public void setTextFontColor(int textFontColor) {
        Logging.log("AdMarvelView - setTextFontColor :" + textFontColor);
        this.textFontColor = ViewCompat.MEASURED_STATE_MASK | textFontColor;
    }

    public void setVideoEventListener(AdMarvelVideoEventListener listener) {
        this.videoEventListener = listener;
    }

    @Deprecated
    public void start(Activity activity) {
    }

    @Deprecated
    public void stop(Activity activity) {
    }

    public void updateCurrentActivity(Activity activity) {
        Logging.log("AdMarvelView - updateCurrentActivity");
        if (activity != null) {
            activityReference = new WeakReference(activity);
            this.activityReferenceLocal = new WeakReference(activity);
        }
    }
}
