package com.onelouder.adlib;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.PowerManager;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.admarvel.android.ads.AdMarvelUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;

public class AdView extends RelativeLayout {
    public static final String PREF_KEY_ANDROID_ID_CHEKED = "androidIdChecked";
    public static final String SDK_VERSION = "2.7.1.827";
    private static int instanceId;
    private static Handler mHandler;
    private String _tag;
    private boolean isAdRequested;
    private Boolean isBannerAd;
    private boolean isClosed;
    private Boolean isFixedHeight;
    private boolean isPaused;
    private long lAdRequestedAt;
    private RelativeLayout mAdContainer;
    private AdPlacement mAdPlacement;
    private I1LouderAdProxy mAdProxy;
    private int mAdsReceived;
    private boolean mAllowLockScreen;
    private int mBackgroundColor;
    private ImageView mCloseButton;
    private RelativeLayout mCloseLayout;
    private String mKeywords;
    private String mLastProvider;
    private int mLineSeparatorColor;
    private AdViewListener mListener;
    private NativeAdUiImpl mNativeAdUiImpl;
    private AdOverlay mOverlay;
    private String mPlacementId;
    private final HashMap<String, I1LouderAdProxy> mProxyMap;
    private BroadcastReceiver mReceiver;
    private boolean mReceiverRegistered;
    private boolean mRestoredFromInstance;
    private Runnable mRotateAdTask;
    private ImageView mSeparator;
    private String placementInuse;

    /* renamed from: com.onelouder.adlib.AdView.1 */
    static class C12861 implements Runnable {
        final /* synthetic */ Context val$context;

        C12861(Context context) {
            this.val$context = context;
        }

        public void run() {
            Preferences.setSimplePref(this.val$context, AdView.PREF_KEY_ANDROID_ID_CHEKED, true);
            PlacementManager.checkAdTargetUpdate(this.val$context);
        }
    }

    /* renamed from: com.onelouder.adlib.AdView.2 */
    class C12872 extends BroadcastReceiver {
        C12872() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                try {
                    if (intent.getAction().equals(UpdateAdsConfig.ACTION_PLACMENTS_AVAILABLE)) {
                        Diagnostics.m1951d(AdView.this.TAG(), "received ACTION_PLACMENTS_AVAILABLE");
                        if (Preferences.getSimplePref(AdView.this.getContext(), "ads_enabled", true) && !AdView.this.isPaused && AdView.this.mAdPlacement == null) {
                            AdView.this.resume();
                        }
                    }
                } catch (Throwable e) {
                    Diagnostics.m1953e(AdView.this.TAG(), e);
                }
            }
        }
    }

    /* renamed from: com.onelouder.adlib.AdView.3 */
    class C12903 implements OnClickListener {

        /* renamed from: com.onelouder.adlib.AdView.3.1 */
        class C12881 extends SimpleAnimationListener {
            C12881() {
            }

            public void onAnimationEnd(Animation animation) {
                AdView.this.setVisibility(4);
                AdView.this.getLayoutParams().height = 0;
            }
        }

        /* renamed from: com.onelouder.adlib.AdView.3.2 */
        class C12892 extends SimpleAnimationListener {
            C12892() {
            }

            public void onAnimationEnd(Animation animation) {
                AdView.this.setVisibility(4);
                AdView.this.getLayoutParams().height = 0;
            }
        }

        C12903() {
        }

        public void onClick(View v) {
            try {
                if (AdView.this.isFixedHeight()) {
                    if (AdView.this.mAdProxy != null) {
                        AdView.this.mAdProxy.destroy();
                        AdView.this.mAdProxy = null;
                    }
                    Utils.fadeOut(AdView.this, null, 0, 100);
                } else if (AdView.this.isAdAtTop()) {
                    Utils.slideOutUp(AdView.this, new C12881(), 0);
                } else {
                    Utils.slideOutDown(AdView.this, new C12892(), 0);
                }
                AdView.this.sendAction(AdPlacement.ACTION_1L_ADVIEW_CLOSED, "close");
                if (AdView.this.mAdPlacement != null) {
                    AdView.this.mAdPlacement.onCloseAd(AdView.this.getContext());
                }
                AdView.this.isClosed = true;
            } catch (Throwable e) {
                Diagnostics.m1953e(AdView.this.TAG(), e);
            }
        }
    }

    /* renamed from: com.onelouder.adlib.AdView.4 */
    class C12924 implements Runnable {

        /* renamed from: com.onelouder.adlib.AdView.4.1 */
        class C12911 implements Runnable {
            C12911() {
            }

            public void run() {
                AdView.this.requestAd(true);
            }
        }

        C12924() {
        }

        public void run() {
            Diagnostics.m1951d(AdView.this.TAG(), "mRotateAdTask.run()");
            if (!Preferences.getSimplePref(AdView.this.getContext(), "ads_enabled", true)) {
                if (!(AdView.this.isFixedHeight() || AdView.this.getLayoutParams() == null)) {
                    AdView.this.getLayoutParams().height = 0;
                }
                AdView.this.removeAllViews();
            } else if (AdView.this.isPaused) {
                if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.m1952e(AdView.this.TAG(), "mRotateAdTask.run(), isPaused=true THIS SHOULD NOT BE HAPPENING");
                }
            } else if (AdView.this.isClosed) {
                Diagnostics.m1951d(AdView.this.TAG(), "mRotateAdTask.run(), isClosed=true");
                if (AdView.this.getHandler() != null) {
                    AdView.this.getHandler().postDelayed(new C12911(), (long) 25000);
                }
            } else {
                AdView.this.requestAd(true);
            }
        }
    }

    public interface AdViewListener {
        boolean onAdClicked(AdView adView, String str);

        void onAdReceived(AdView adView);

        void onAdRequestFailed(AdView adView, int i, String str);

        void onAdRequested(AdView adView);

        void onSetTargetParams(AdView adView, HashMap<String, String> hashMap);
    }

    class ImageCallback extends AbsLoadImageCallback {
        String url;

        public ImageCallback(View v) {
            super(null, (Activity) v.getContext(), v);
            this.url = "https://advrts.s3.amazonaws.com/sdk2/adlib_close_ad_v2.png";
        }

        public String getUrl() {
            return this.url;
        }

        protected void onExisting(Bitmap bitmap) {
            onReady(bitmap);
        }

        protected void onReady(Bitmap bitmap) {
            try {
                ImageView iv = this.view;
                bitmap.setDensity(240);
                iv.setImageDrawable(new BitmapDrawable(iv.getContext().getResources(), bitmap));
                iv.setVisibility(0);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    static {
        instanceId = 0;
    }

    private String TAG() {
        if (this._tag == null) {
            this._tag = "1LAdView-" + instanceId;
        }
        return this._tag;
    }

    public AdView(Context context) {
        this(context, null);
    }

    public AdView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mProxyMap = new HashMap();
        this.lAdRequestedAt = 0;
        this.isAdRequested = false;
        this.isPaused = true;
        this.isClosed = false;
        this.mReceiverRegistered = false;
        this.mRestoredFromInstance = false;
        this.mAllowLockScreen = false;
        this.mLastProvider = Stomp.EMPTY;
        this.mBackgroundColor = ViewCompat.MEASURED_STATE_MASK;
        this.mLineSeparatorColor = ViewCompat.MEASURED_STATE_MASK;
        this.mAdsReceived = 0;
        this.mReceiver = new C12872();
        this.isFixedHeight = null;
        this.isBannerAd = null;
        this.mRotateAdTask = new C12924();
        instanceId++;
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        if (Preferences.getMobileConsumerId(context).length() == 0) {
            long plus1year = System.currentTimeMillis() + (12 * 2678400000L);
            String str = "MssRegPlus1Month";
            Preferences.setSimplePref(context, r16, System.currentTimeMillis() + 2678400000L);
            Preferences.setSimplePref(context, "MssRegPlus1Year", plus1year);
        } else {
            RegisterMobileConsumer registerMobileConsumer;
            long plusonemonth = Preferences.getSimplePref(context, "MssRegPlus1Month", 0);
            if (plusonemonth > 0 && System.currentTimeMillis() > plusonemonth) {
                registerMobileConsumer = new RegisterMobileConsumer(context);
                Preferences.setSimplePref(context, "MssRegPlus1Month", 0);
            }
            long plusoneyear = Preferences.getSimplePref(context, "MssRegPlus1Year", 0);
            if (plusoneyear > 0 && System.currentTimeMillis() > plusoneyear) {
                registerMobileConsumer = new RegisterMobileConsumer(context);
                Preferences.setSimplePref(context, "MssRegPlus1Year", 0);
            }
        }
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(UpdateAdsConfig.ACTION_PLACMENTS_AVAILABLE);
            context.registerReceiver(this.mReceiver, filter);
            this.mReceiverRegistered = true;
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public static boolean diagnosticsEnabled() {
        return Diagnostics.getInstance().isEnabled(4);
    }

    public static void enableDiagnostics() {
        Diagnostics.getInstance().setLogLevel(6);
        try {
            if (VERSION.SDK_INT >= 19) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
            AdMarvelUtils.enableLogging(true);
        } catch (Throwable th) {
        }
    }

    public static void disableDiagnostics() {
        Diagnostics.getInstance().setLogLevel(0);
        try {
            AdMarvelUtils.enableLogging(false);
        } catch (Throwable th) {
        }
    }

    public static void setProductInfo(Context context, String productname) {
        Utils.disableMMSDKLogging();
        Diagnostics.m1951d("1LAdView", "setProductInfo - productname=" + productname);
        Log.e("1LAdView", "SDK_VERSION=2.7.1.827");
        if (diagnosticsEnabled()) {
            int resourceId = context.getResources().getIdentifier("google_play_services_version", "integer", context.getPackageName());
            if (resourceId != 0) {
                try {
                    Diagnostics.m1951d("1LAdView", "google play services library version: " + context.getResources().getInteger(resourceId));
                } catch (Exception e) {
                }
            } else {
                Diagnostics.m1951d("1LAdView", "Unable to find google play services library version");
            }
        }
        if (Looper.getMainLooper().getThread().equals(Thread.currentThread())) {
            boolean firstLaunch;
            if (mHandler == null) {
                firstLaunch = true;
            } else {
                firstLaunch = false;
            }
            mHandler = new Handler();
            if (productname != null) {
                if (!Preferences.getSimplePref(context, "ads-product-name", Stomp.EMPTY).equals(productname)) {
                    Preferences.setMobileConsumerId(context, Stomp.EMPTY);
                }
                Preferences.setSimplePref(context, "ads-product-name", productname);
            }
            String productversion = null;
            try {
                productversion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (Throwable e2) {
                e2.printStackTrace();
            }
            if (productversion != null) {
                if (!Preferences.getSimplePref(context, "ads-product-version", Stomp.EMPTY).equals(productversion)) {
                    Preferences.setSimplePref(context, "last-adconfig-update", 0);
                    Preferences.setMobileConsumerId(context, Stomp.EMPTY);
                }
                Preferences.setSimplePref(context, "ads-product-version", productversion);
            }
            Runnable afterAndroidId = null;
            if (Preferences.getSimplePref(context, Utils.PREF_KEY_ANDROID_AD_ID, null) == null) {
                afterAndroidId = new C12861(context);
            }
            Utils.checkAndroidId(context, afterAndroidId);
            if (afterAndroidId == null) {
                PlacementManager.checkAdConfigUpdate(context, firstLaunch);
            }
            Preferences.setSimplePref(context, "ads-cardinal", 0);
            AdActivity.updateGraphics(context);
            return;
        }
        Diagnostics.m1952e("1LAdView", "You must call setProductInfo from the main UI thread.");
        throw new IllegalStateException();
    }

    public static void startActivity(Activity activity) {
        try {
            new ProxyAdMarvelView(activity).start();
        } catch (Throwable th) {
        }
    }

    public static void stopActivity(Activity activity) {
        try {
            new ProxyAdMarvelView(activity).stop();
        } catch (Throwable th) {
        }
    }

    public static void setEnvProd(Context context) {
        if (!Preferences.isProdEnv(context)) {
            SendAdUsage.sendEvents(context);
            Preferences.setEnvPrefProd(context);
            resetEnv(context);
        }
    }

    public static void setEnvQa(Context context) {
        if (!Preferences.isQaEnv(context)) {
            SendAdUsage.sendEvents(context);
            Preferences.setEnvPrefQa(context);
            resetEnv(context);
        }
    }

    public static void setEnvDev(Context context) {
        if (!Preferences.isDevEnv(context)) {
            SendAdUsage.sendEvents(context);
            Preferences.setEnvPrefDev(context);
            resetEnv(context);
        }
    }

    public static void setEnvStage(Context context) {
        if (!Preferences.isStageEnv(context)) {
            SendAdUsage.sendEvents(context);
            Preferences.setEnvPrefStage(context);
            resetEnv(context);
        }
    }

    private static void resetEnv(Context context) {
        String appname = Preferences.getSimplePref(context, "ads-product-name", Stomp.EMPTY);
        if (appname.length() > 0) {
            Preferences.setSimplePref(context, "last-adconfig-update", System.currentTimeMillis());
            UpdateAdsConfig updateAdsConfig = new UpdateAdsConfig(context, appname);
        }
        Preferences.setSimplePref(context, "last-mobileconsumer-request", 0);
        Preferences.setMobileConsumerId(context, Stomp.EMPTY);
        Preferences.setMobileConsumerEtag(context, Stomp.EMPTY);
        Preferences.setSimplePref(context, Utils.PREF_KEY_ANDROID_AD_ID, null);
        setProductInfo(context, Preferences.getSimplePref(context, "ads-product-name", Stomp.EMPTY));
    }

    public static void setEmail(Context context, String email) {
        setCustomParam(context, "ads-email", email);
    }

    public static void setTwitterId(Context context, String twitterid) {
        setCustomParam(context, "ads-twitterid", twitterid);
    }

    public static void setCustomParam(Context context, String name, String value) {
        if (value != null) {
            try {
                if (value.length() > 0) {
                    if (name.equals("ads-twitterid") || name.equals("ads-email")) {
                        if (!Preferences.getSimplePref(context, name, Stomp.EMPTY).equals(value)) {
                            Preferences.setMobileConsumerId(context, Stomp.EMPTY);
                        }
                    } else if (name.equalsIgnoreCase("twitterSN")) {
                        name = "ads-tsn";
                        value = Base64.encode(value.getBytes());
                        if (!Preferences.getSimplePref(context, name, Stomp.EMPTY).equals(value)) {
                            Preferences.setMobileConsumerId(context, Stomp.EMPTY);
                        }
                    } else if (name.equalsIgnoreCase("twitterFriends")) {
                        name = "ads-tfnds";
                        value = Base64.encode(value.getBytes());
                        if (!Preferences.getSimplePref(context, name, Stomp.EMPTY).equals(value)) {
                            Preferences.setMobileConsumerId(context, Stomp.EMPTY);
                        }
                    }
                    Preferences.setSimplePref(context, name, value);
                }
            } catch (Throwable e) {
                Diagnostics.m1953e("1LAdView", e);
            }
        }
    }

    public static boolean isValidPlacementId(Context context, String placementid) {
        if (PlacementManager.getInstance().getAdPlacement(context, placementid, "banner") == null && PlacementManager.getInstance().getAdPlacement(context, placementid, "square") == null) {
            return false;
        }
        return true;
    }

    public Handler getHandler() {
        return getStaticHandler();
    }

    protected static Handler getStaticHandler() {
        if (mHandler == null && Looper.getMainLooper().getThread().equals(Thread.currentThread())) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    public void setListener(AdViewListener listener) {
        this.mListener = listener;
    }

    public void setNativeAdUiImpl(NativeAdUiImpl impl) {
        this.mNativeAdUiImpl = impl;
    }

    public NativeAdUiImpl getNativeAdUiImpl() {
        return this.mNativeAdUiImpl;
    }

    public String getPlacementId() {
        return this.mPlacementId;
    }

    public void setPlacementId(String placementid) {
        Diagnostics.m1951d(TAG(), "setPlacementId(), placementid=" + placementid);
        this.mPlacementId = placementid;
    }

    public void setKeywords(String keywords) {
        if (keywords != null && keywords.length() > 0) {
            this.mKeywords = keywords;
        }
    }

    public void allowLockScreenAds() {
        this.mAllowLockScreen = true;
    }

    public void setBackgroundColor(int color) {
        this.mBackgroundColor = color;
        super.setBackgroundColor(color);
    }

    public void setLineSeparatorColor(int color) {
        if (this.mSeparator != null) {
            this.mSeparator.setBackgroundColor(color);
        }
        this.mLineSeparatorColor = color;
    }

    public boolean isFullScreen() {
        if (this.mAdProxy != null) {
            return this.mAdProxy.isFullScreen();
        }
        return false;
    }

    public void removeAllViews() {
        this.mAdProxy = null;
        removeView(this.mOverlay);
        this.mOverlay = null;
        removeView(this.mCloseButton);
        this.mCloseButton = null;
        removeView(this.mCloseLayout);
        this.mCloseLayout = null;
        removeView(this.mSeparator);
        this.mSeparator = null;
    }

    public void requestNewAd() {
        Diagnostics.m1951d(TAG(), "requestNewAd()");
        if (this.isAdRequested && this.mAdPlacement != null) {
            if (((long) this.mAdPlacement.getRefreshRate(getContext())) - (System.currentTimeMillis() - this.lAdRequestedAt) > 0) {
                Diagnostics.m1957w(TAG(), "AD REQUEST PENDING, IGNORING REQUEST!");
                return;
            }
            this.isAdRequested = false;
        }
        if (this.mAdPlacement != null) {
            this.mAdPlacement.setTimestamp(0);
        }
        resume();
    }

    public void destroy() {
        Diagnostics.m1951d(TAG(), "destroy");
        destroyImpl(false);
    }

    private void destroyImpl(boolean detached) {
        Diagnostics.m1951d(TAG(), "destroyImpl " + detached);
        try {
            if (this.mAdPlacement != null) {
                try {
                    if (this.mAdPlacement.isRecycleable()) {
                        this.mAdPlacement = null;
                    } else if (!this.mAdPlacement.isCloneable()) {
                        if (this.mAdProxy != null) {
                            this.mAdProxy.destroy();
                        }
                        this.mAdPlacement.setAdProxy(null);
                        this.mAdPlacement = null;
                    } else if (!detached) {
                        if (this.mAdProxy != null) {
                            this.mAdProxy.destroy();
                        }
                        this.mAdPlacement.setAdProxy(null);
                        this.mAdPlacement = null;
                    }
                    this.mAdProxy = null;
                } catch (Exception e) {
                }
                SendAdUsage.sendEvents(getContext().getApplicationContext());
            }
            if (this.mReceiverRegistered) {
                getContext().unregisterReceiver(this.mReceiver);
                this.mReceiverRegistered = false;
            }
        } catch (Throwable e2) {
            Diagnostics.m1953e(TAG(), e2);
        }
    }

    public void pause() {
        if (!this.isPaused) {
            Diagnostics.m1951d(TAG(), "pause");
            try {
                for (String key : this.mProxyMap.keySet()) {
                    I1LouderAdProxy proxy = (I1LouderAdProxy) this.mProxyMap.get(key);
                    if (proxy != null) {
                        proxy.pause();
                    }
                }
                if (getHandler() != null) {
                    getHandler().removeCallbacks(this.mRotateAdTask);
                }
                if (this.mAdPlacement != null) {
                    this.mAdPlacement.pause(getContext());
                }
                this.isPaused = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resume() {
        Diagnostics.m1951d(TAG(), "resume");
        if (Preferences.getSimplePref(getContext(), "ads_enabled", true)) {
            if (!this.mRestoredFromInstance) {
                this.isPaused = false;
            }
            if (this.mPlacementId == null) {
                this.mPlacementId = "global";
            }
            if (this.mAdPlacement == null) {
                if (this.placementInuse == null) {
                    this.placementInuse = this.mPlacementId;
                }
                this.mAdPlacement = PlacementManager.getInstance().getAdPlacement(getContext(), this.placementInuse, isBannerAd() ? "banner" : "square");
            }
            if (checkAdPlacement("resume")) {
                try {
                    I1LouderAdProxy proxy;
                    ArrayList<String> proxyremovals = new ArrayList();
                    for (String key : this.mProxyMap.keySet()) {
                        proxy = (I1LouderAdProxy) this.mProxyMap.get(key);
                        if (proxy != null) {
                            if (proxy.getProxiedView() != null) {
                                proxy.resume();
                            } else {
                                proxyremovals.add(key);
                            }
                        }
                    }
                    Iterator i$ = proxyremovals.iterator();
                    while (i$.hasNext()) {
                        this.mProxyMap.remove((String) i$.next());
                    }
                    proxy = this.mAdPlacement.getAdProxy();
                    if (proxy == null && this.mAdPlacement.isRecycleable()) {
                        String rolloverplacement = this.mAdPlacement.getRolloverId();
                        if (rolloverplacement != null) {
                            AdPlacement placement = PlacementManager.getInstance().getAdPlacement(getContext(), rolloverplacement, isBannerAd() ? "banner" : "square");
                            if (placement != null) {
                                proxy = placement.getAdProxy();
                                if (proxy != null) {
                                    this.mAdPlacement = placement;
                                    this.placementInuse = rolloverplacement;
                                }
                            }
                        }
                    }
                    if (proxy != null) {
                        if (this.isClosed) {
                            proxy.setAdViewParent(this);
                        } else {
                            removeAllViews();
                            this.mLastProvider = this.mAdPlacement.getNetwork();
                            this.mAdProxy = proxy;
                            this.mAdProxy.setAdPlacement(this.mAdPlacement);
                            addProxiedAdView(this.mAdProxy);
                            this.mAdProxy.invalidate();
                            Diagnostics.m1951d(TAG(), "resuming recycled adview");
                            resumeAdView(this.mAdPlacement.getImpressionFlag());
                        }
                    } else if (!this.mProxyMap.containsKey(this.mAdPlacement.getNetwork())) {
                        this.lAdRequestedAt = 0;
                        this.mAdPlacement.setTimestamp(0);
                        this.mAdProxy = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.mRestoredFromInstance = false;
                if (this.mAdPlacement == null) {
                    return;
                }
                if (this.mAdProxy == null || !this.mAdProxy.isExpanded()) {
                    long remaining = this.mAdPlacement.getRemaining(getContext());
                    this.isPaused = false;
                    this.mAdPlacement.resume(getContext());
                    this.mAdPlacement.clearImpressionFlag();
                    scheduleUpdates(remaining);
                    return;
                }
                Diagnostics.m1956v(TAG(), "Ad is in expanded state, don't resume");
                return;
            }
            return;
        }
        if (isBannerAd() && getLayoutParams() != null) {
            getLayoutParams().height = 0;
        }
        removeAllViews();
    }

    public void start() {
        boolean startedAdMarvelProxy = false;
        for (String key : this.mProxyMap.keySet()) {
            try {
                I1LouderAdProxy proxy = (I1LouderAdProxy) this.mProxyMap.get(key);
                if (!(proxy == null || proxy.getProxiedView() == null)) {
                    proxy.start();
                    if (proxy instanceof ProxyAdMarvelView) {
                        startedAdMarvelProxy = true;
                    }
                }
            } catch (Throwable e) {
                Diagnostics.m1955i(TAG(), "Caught throwable starting proxies: " + e);
            }
        }
        if (!startedAdMarvelProxy) {
            try {
                new ProxyAdMarvelView((Activity) getContext()).start();
            } catch (Throwable th) {
            }
        }
    }

    public void stop() {
        try {
            for (String key : this.mProxyMap.keySet()) {
                I1LouderAdProxy proxy = (I1LouderAdProxy) this.mProxyMap.get(key);
                if (!(proxy == null || proxy.getProxiedView() == null)) {
                    proxy.stop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mPlacementId = bundle.getString("placementid");
            this.placementInuse = bundle.getString("placementinuse");
            this.isPaused = bundle.getBoolean("ispaused");
            this.isClosed = bundle.getBoolean("isclosed");
            this.mRestoredFromInstance = true;
            super.onRestoreInstanceState(bundle.getParcelable("ptr_super"));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("ispaused", this.isPaused);
        bundle.putBoolean("isclosed", this.isClosed);
        bundle.putString("placementid", this.mPlacementId);
        bundle.putString("placementinuse", this.placementInuse);
        bundle.putParcelable("ptr_super", super.onSaveInstanceState());
        return bundle;
    }

    protected boolean wasTouched() {
        if (this.mOverlay != null) {
            return this.mOverlay.wasTouched();
        }
        return false;
    }

    protected void resetTouch() {
        if (this.mOverlay != null) {
            this.mOverlay.resetTouch();
        }
    }

    private boolean checkAdPlacement(String method) {
        if (this.mAdPlacement == null) {
            Diagnostics.m1952e(TAG(), method + ", mAdPlacement == null");
            if (this.mListener == null) {
                return false;
            }
            this.mListener.onAdRequestFailed(this, AdPlacement.ERRORCODE_NO_PLACEMENT, null);
            return false;
        }
        if (this.mReceiverRegistered) {
            try {
                getContext().unregisterReceiver(this.mReceiver);
                this.mReceiverRegistered = false;
            } catch (Throwable e) {
                Diagnostics.m1953e(TAG(), e);
            }
        }
        if (!this.mAdPlacement.ispaused_until(getContext())) {
            return true;
        }
        if (Diagnostics.getInstance().isEnabled(4)) {
            Diagnostics.m1957w(TAG(), "mAdPlacement.ispaused_until() is true");
        }
        if (this.mListener == null) {
            return false;
        }
        this.mListener.onAdRequestFailed(this, AdPlacement.ERRORCODE_AD_PLACEMENT_PAUSED, null);
        return false;
    }

    private void sendAction(String action, String event) {
        sendAction(action, event, (String) null);
    }

    private void sendAction(String action, String event, String url) {
        if (this.mAdPlacement != null) {
            HashMap<String, Object> params = null;
            if (url != null && url.length() > 0) {
                params = new HashMap();
                params.put(AdPlacement.EXTRA_1L_URL_CLICKED, url);
            }
            this.mAdPlacement.sendBroadcast(getContext(), action, params);
            SendAdUsage.trackEvent(getContext(), this.mAdPlacement, event, null, url);
        }
    }

    protected boolean isFixedHeight() {
        boolean bReturn = false;
        try {
            if (this.isFixedHeight == null && getLayoutParams() != null) {
                this.isFixedHeight = Boolean.valueOf(getLayoutParams().height != -2);
            }
            bReturn = this.isFixedHeight.booleanValue();
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
        return bReturn;
    }

    protected boolean isBannerAd() {
        boolean bReturn = true;
        try {
            if (this.isBannerAd == null && getLayoutParams() != null) {
                this.isBannerAd = Boolean.valueOf(getLayoutParams().width == -1);
            }
            bReturn = this.isBannerAd.booleanValue();
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
        return bReturn;
    }

    private boolean isAdAtTop() {
        try {
            LayoutParams params = getLayoutParams();
            if (params instanceof LinearLayout.LayoutParams) {
                LinearLayout parent = (LinearLayout) getParent();
                int count = parent.getChildCount();
                int i = 0;
                while (i < count) {
                    if (parent.getChildAt(i).equals(this) && i == 0) {
                        return true;
                    }
                    i++;
                }
                return false;
            } else if (!(params instanceof RelativeLayout.LayoutParams)) {
                return false;
            } else {
                for (int r : ((RelativeLayout.LayoutParams) params).getRules()) {
                    if (r == 10) {
                        return true;
                    }
                    if (r == 12) {
                        return false;
                    }
                }
                return false;
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
            return false;
        }
    }

    private void addCloseButton() {
        if (checkAdPlacement("addCloseButton")) {
            RelativeLayout.LayoutParams params;
            if (this.mOverlay == null) {
                this.mOverlay = new AdOverlay(getContext());
                addView(this.mOverlay, new RelativeLayout.LayoutParams(-1, -1));
            }
            this.mOverlay.setAdPlacement(this.mAdPlacement);
            if (this.mAdPlacement.showCloseButton(getContext()) && this.mCloseButton == null) {
                try {
                    Context context = getContext();
                    this.mCloseButton = new ImageView(context);
                    int resid = Utils.getResourseIdByName(context.getPackageName(), "drawable", "adlib_close_ad");
                    if (resid != 0) {
                        this.mCloseButton.setImageResource(resid);
                    } else {
                        ImageLoader.displayCachedImage(context, new ImageCallback(this.mCloseButton), this.mCloseButton);
                    }
                    this.mCloseLayout = new RelativeLayout(getContext());
                    int height = Utils.getDIP(50.0d);
                    if (this.mAdProxy != null) {
                        height = this.mAdProxy.getHeight();
                    }
                    params = new RelativeLayout.LayoutParams(Utils.getDIP(30.0d), height);
                    params.addRule(9);
                    addView(this.mCloseLayout, params);
                    params = new RelativeLayout.LayoutParams(Utils.getDIP(20.0d), Utils.getDIP(20.0d));
                    if (isAdAtTop()) {
                        params.setMargins(Utils.getDIP(4.0d), 0, 0, Utils.getDIP(4.0d));
                        params.addRule(12);
                    } else {
                        params.setMargins(Utils.getDIP(4.0d), Utils.getDIP(4.0d), 0, 0);
                        params.addRule(10);
                    }
                    params.addRule(9);
                    this.mCloseLayout.addView(this.mCloseButton, params);
                    this.mCloseLayout.setOnClickListener(new C12903());
                } catch (Throwable e) {
                    Diagnostics.m1953e(TAG(), e);
                    this.mCloseButton = null;
                    this.mCloseLayout = null;
                } catch (Throwable e2) {
                    Diagnostics.m1953e(TAG(), e2);
                    this.mCloseButton = null;
                    this.mCloseLayout = null;
                }
            }
            if (this.mSeparator == null && isBannerAd()) {
                this.mSeparator = new ImageView(getContext());
                this.mSeparator.setBackgroundColor(this.mLineSeparatorColor);
                params = new RelativeLayout.LayoutParams(-1, Utils.getDIP(1.0d));
                if (isAdAtTop()) {
                    params.addRule(12);
                } else {
                    params.addRule(10);
                }
                addView(this.mSeparator, params);
            }
        }
    }

    private void scheduleUpdates(long interval) {
        Diagnostics.m1951d(TAG(), "scheduleUpdates, interval=" + interval);
        if (checkAdPlacement("scheduleUpdates")) {
            try {
                if (getHandler() != null) {
                    getHandler().removeCallbacks(this.mRotateAdTask);
                }
                if (this.mAdsReceived > 0 && this.mAdPlacement.isOnetime()) {
                    Diagnostics.m1955i(TAG(), "onetime==true");
                } else if (interval > 0) {
                    getHandler().postDelayed(this.mRotateAdTask, interval);
                } else {
                    this.mRotateAdTask.run();
                }
            } catch (Throwable e) {
                Diagnostics.m1953e(TAG(), e);
            }
        }
    }

    private boolean isActivityInBackground() {
        try {
            if (getContext().checkCallingOrSelfPermission("android.permission.GET_TASKS") != 0) {
                return false;
            }
            List<RunningTaskInfo> tasks = ((ActivityManager) getContext().getSystemService("activity")).getRunningTasks(1);
            if (tasks.isEmpty() || !(getContext() instanceof Activity)) {
                return false;
            }
            if (((RunningTaskInfo) tasks.get(0)).topActivity.getClassName().equals(((Activity) getContext()).getComponentName().getClassName())) {
                return false;
            }
            Diagnostics.m1951d(TAG(), "isActivityInBackground()==true");
            return true;
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
            return false;
        }
    }

    protected void requestAd(boolean firstTry) {
        if (firstTry) {
            if (this.isAdRequested && this.mAdPlacement != null) {
                long interval = (long) this.mAdPlacement.getRefreshRate(getContext());
                if (interval - (System.currentTimeMillis() - this.lAdRequestedAt) > 0) {
                    Diagnostics.m1957w(TAG(), "ad request pending");
                    scheduleUpdates(interval);
                    return;
                }
            }
            if (!this.mPlacementId.equals(this.placementInuse)) {
                this.placementInuse = this.mPlacementId;
                this.mAdPlacement = PlacementManager.getInstance().getAdPlacement(getContext(), this.placementInuse, isBannerAd() ? "banner" : "square");
                this.mAdProxy = null;
            }
        } else if (this.mAdPlacement != null) {
            this.placementInuse = this.mAdPlacement.getRolloverId();
            this.mAdPlacement = PlacementManager.getInstance().getAdPlacement(getContext(), this.placementInuse, isBannerAd() ? "banner" : "square");
            this.mAdProxy = null;
            if (this.mAdPlacement != null) {
                this.mAdPlacement.resume(getContext());
            }
        }
        if (checkAdPlacement("requestAd")) {
            KeyguardManager km = (KeyguardManager) getContext().getSystemService("keyguard");
            boolean isScreenOn = ((PowerManager) getContext().getSystemService("power")).isScreenOn();
            if (km.inKeyguardRestrictedInputMode() && !this.mAllowLockScreen) {
                Diagnostics.m1957w(TAG(), "isScreenLocked");
                scheduleUpdates((long) this.mAdPlacement.getRefreshRate(getContext()));
            } else if (!isScreenOn || isActivityInBackground()) {
                Diagnostics.m1957w(TAG(), "!isScreenOn || activityInBackground");
                scheduleUpdates((long) this.mAdPlacement.getRefreshRate(getContext()));
            } else {
                Diagnostics.m1951d(TAG(), "requestAd, firstTry=" + firstTry);
                if (this.isPaused) {
                    if (Diagnostics.getInstance().isEnabled(4)) {
                        Diagnostics.m1952e(TAG(), "requestAd, isPaused=true");
                    }
                    if (this.mListener != null) {
                        this.mListener.onAdRequestFailed(this, AdPlacement.ERRORCODE_AD_PLACEMENT_PAUSED, null);
                        return;
                    }
                    return;
                }
                if (this.mAdPlacement.ispaused()) {
                    this.mAdPlacement.resume(getContext());
                }
                if (this.mAdsReceived > 0) {
                    if (this.mAdPlacement.isOnetime()) {
                        Diagnostics.m1955i(TAG(), "onetime==true");
                        if (this.mListener != null) {
                            this.mListener.onAdRequestFailed(this, AdPlacement.ERRORCODE_AD_COUNT_EXCEEDED, null);
                            return;
                        }
                        return;
                    }
                }
                scheduleUpdates((long) this.mAdPlacement.getRefreshRate(getContext()));
                String provider = this.mAdPlacement.getNetwork();
                if (provider == null) {
                    Diagnostics.m1957w(TAG(), "requestAd, provider == null");
                    if (this.mListener != null) {
                        this.mListener.onAdRequestFailed(this, AdPlacement.ERRORCODE_AD_NETWORK_MISSING, null);
                    }
                } else if (Utils.isNetworkConnected(getContext())) {
                    if (!this.mLastProvider.equals(provider)) {
                        this.mAdProxy = null;
                    }
                    Diagnostics.m1951d(TAG(), "provider=" + provider);
                    this.mLastProvider = provider;
                    try {
                        String value;
                        HashMap<String, Object> targetParams = new HashMap();
                        Map<String, String> mediatorParams = Preferences.getMediatorArguments(getContext(), provider);
                        if (mediatorParams != null) {
                            for (String key : mediatorParams.keySet()) {
                                value = (String) mediatorParams.get(key);
                                if (value != null && value.length() > 0) {
                                    targetParams.put(key, value);
                                }
                            }
                        }
                        GeoLocation geolocation = LocationUtils.getGeoLocation(getContext());
                        if (geolocation != null) {
                            value = geolocation.getPostalcode();
                            if (value != null && value.length() > 0) {
                                targetParams.put("POSTAL_CODE", value);
                            }
                            targetParams.put("GEOLOCATION", geolocation.toString());
                        }
                        String param = Preferences.getSimplePref(getContext(), "ads-twitterid", Stomp.EMPTY);
                        if (param.length() > 0) {
                            targetParams.put("jz", param);
                        }
                        param = Preferences.getSimplePref(getContext(), "ads-product-version", Stomp.EMPTY);
                        if (param.length() > 0) {
                            targetParams.put("APP_VERSION", param);
                        }
                        param = Utils.getCountry(getContext());
                        if (param.length() > 0) {
                            targetParams.put("COUNTRY", param);
                        }
                        param = Utils.getLanguage(getContext());
                        if (param.length() > 0) {
                            targetParams.put("LANG", param);
                        }
                        if (this.mKeywords != null) {
                            targetParams.put("KEYWORDS", this.mKeywords);
                        }
                        if (this.mListener != null) {
                            HashMap<String, String> userParams = new HashMap();
                            this.mListener.onSetTargetParams(this, userParams);
                            for (String key2 : userParams.keySet()) {
                                value = (String) userParams.get(key2);
                                if (value != null && value.length() > 0) {
                                    targetParams.put(key2, value);
                                }
                            }
                        }
                        int cardinal = Preferences.getSimplePref(getContext(), "ads-cardinal", 0) + 1;
                        Preferences.setSimplePref(getContext(), "ads-cardinal", cardinal);
                        targetParams.put("CARDINAL", Integer.toString(cardinal));
                        int connType = Utils.getConnectionType(getContext());
                        if (connType != 0) {
                            targetParams.put("CXN", Integer.toString(connType));
                        }
                        long lastrequest = Preferences.getSimplePref(getContext(), "ads-lastrequest", 0);
                        long now = System.currentTimeMillis();
                        Preferences.setSimplePref(getContext(), "ads-lastrequest", now);
                        if (!Utils.isToday(lastrequest)) {
                            Utils.resetSESSIONID(getContext());
                            Preferences.setSimplePref(getContext(), "ads-scounter", 1);
                            Preferences.setSimplePref(getContext(), "ads-adcounter", 0);
                        } else if (3600000 < now - lastrequest) {
                            Utils.resetSESSIONID(getContext());
                            Preferences.setSimplePref(getContext(), "ads-scounter", Preferences.getSimplePref(getContext(), "ads-scounter", 0) + 1);
                            Preferences.setSimplePref(getContext(), "ads-adcounter", 0);
                        }
                        int adcounter = Preferences.getSimplePref(getContext(), "ads-adcounter", 0) + 1;
                        Preferences.setSimplePref(getContext(), "ads-adcounter", adcounter);
                        targetParams.put("ADCOUNTER", Integer.toString(adcounter));
                        targetParams.put("SCOUNTER", Integer.toString(Preferences.getSimplePref(getContext(), "ads-scounter", 1)));
                        if (this.mAdProxy == null) {
                            this.mAdProxy = createProxy();
                        }
                        if (this.mAdProxy != null) {
                            if (isBannerAd() && this.mAdsReceived == 0 && !isFixedHeight() && getLayoutParams() != null) {
                                getLayoutParams().height = 0;
                            }
                            boolean adviewisnull = this.mAdProxy.getProxiedView() == null;
                            this.mAdProxy.requestAd(targetParams);
                            if (adviewisnull) {
                                addProxiedAdView(this.mAdProxy);
                            }
                            if (this.mListener != null && firstTry) {
                                this.mListener.onAdRequested(this);
                            }
                            this.lAdRequestedAt = System.currentTimeMillis();
                            this.isAdRequested = true;
                        }
                    } catch (Throwable e) {
                        Diagnostics.m1953e(TAG(), e);
                    }
                } else {
                    Diagnostics.m1957w(TAG(), "no connectivity");
                    if (this.mListener != null) {
                        this.mListener.onAdRequestFailed(this, AdPlacement.ERRORCODE_NO_CONNECTIVITY, null);
                    }
                }
            }
        }
    }

    private I1LouderAdProxy createProxy() {
        Throwable e;
        Diagnostics.m1951d(TAG(), "createProxy");
        I1LouderAdProxy proxy = null;
        try {
            if (this.mProxyMap.containsKey(this.mAdPlacement.getNetwork())) {
                proxy = (I1LouderAdProxy) this.mProxyMap.get(this.mAdPlacement.getNetwork());
                proxy.destroy();
                this.mProxyMap.remove(proxy);
            }
            I1LouderAdProxy proxy2 = proxy;
            try {
                if (this.mAdPlacement.getNetwork().equals("admarvel")) {
                    proxy = new ProxyAdMarvelView(getContext(), this.mAdPlacement, this);
                } else if (this.mAdPlacement.getNetwork().equals("googleplay")) {
                    proxy = new ProxyGooglePlay(getContext(), this.mAdPlacement, this);
                } else if (this.mAdPlacement.getNetwork().equals("psm")) {
                    proxy = new ProxyPsmAdView(getContext(), this.mAdPlacement, this);
                } else if (this.mAdPlacement.getNetwork().equals("aol")) {
                    proxy = proxy2;
                } else {
                    proxy = this.mAdPlacement.getNetwork().equals("virool") ? proxy2 : proxy2;
                }
                if (proxy != null) {
                    addProxiedAdView(proxy);
                }
            } catch (Throwable th) {
                e = th;
                proxy = proxy2;
                Diagnostics.m1953e(TAG(), e);
                return proxy;
            }
        } catch (Throwable th2) {
            e = th2;
            Diagnostics.m1953e(TAG(), e);
            return proxy;
        }
        return proxy;
    }

    private void addProxiedAdView(I1LouderAdProxy proxy) {
        Diagnostics.m1951d(TAG(), "addProxiedAdView");
        boolean bExisting = this.mProxyMap.containsKey(this.mAdPlacement.getNetwork());
        this.mProxyMap.put(this.mAdPlacement.getNetwork(), proxy);
        View view = proxy.getProxiedView();
        if (view != null) {
            RelativeLayout.LayoutParams params;
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                if (this.mAdContainer == null || !parent.equals(this.mAdContainer)) {
                    Diagnostics.m1951d(TAG(), "addProxiedAdView, removing adview from previous parent");
                    parent.removeView(view);
                } else {
                    Diagnostics.m1951d(TAG(), "addProxiedAdView, adview already child view");
                    return;
                }
            }
            view.setBackgroundColor(this.mBackgroundColor);
            view.setPadding(0, 0, 0, 0);
            if (!isBannerAd() || isFixedHeight()) {
                if (this.mAdContainer == null) {
                    this.mAdContainer = new RelativeLayout(getContext());
                    addView(this.mAdContainer, new RelativeLayout.LayoutParams(-1, -1));
                }
                if (this.mAdPlacement.getNetwork().equals("virool")) {
                    params = new RelativeLayout.LayoutParams(-1, -1);
                } else {
                    params = new RelativeLayout.LayoutParams(-2, -2);
                    params.addRule(13);
                }
            } else {
                if (this.mAdContainer == null) {
                    this.mAdContainer = new RelativeLayout(getContext());
                    addView(this.mAdContainer, new RelativeLayout.LayoutParams(-1, -2));
                }
                params = new RelativeLayout.LayoutParams(-1, -2);
            }
            proxy.setAdViewParent(this);
            this.mAdContainer.addView(view, params);
        }
        if (!bExisting) {
            proxy.resume();
        }
    }

    protected void requestFailed(int errorcode, String errormessage) {
        if (this.mListener != null) {
            this.mListener.onAdRequestFailed(this, errorcode, errormessage);
        }
        this.isAdRequested = false;
    }

    protected boolean onAdViewClicked(String url) {
        if (this.mListener != null) {
            return this.mListener.onAdClicked(this, url);
        }
        return false;
    }

    protected void resumeAdView(boolean bSendImpression) {
        Diagnostics.m1951d(TAG(), "resumeAdView, bSendImpression=" + bSendImpression);
        if (this.mAdProxy == null) {
            Diagnostics.m1957w(TAG(), "resumeAdView, mAdProxy == null");
        } else if (this.mAdContainer == null) {
            Diagnostics.m1957w(TAG(), "resumeAdView, mAdContainer == null");
        } else {
            if (this.mAdContainer.getChildCount() > 0) {
                this.mAdContainer.removeAllViews();
            }
            addProxiedAdView(this.mAdProxy);
            View view = this.mAdProxy.getProxiedView();
            if (view != null) {
                view.setVisibility(0);
            }
            addCloseButton();
            if (isBannerAd() && !isFixedHeight()) {
                if (getLayoutParams() != null) {
                    getLayoutParams().height = this.mAdProxy.getHeight();
                    requestLayout();
                }
                if (this.mAdsReceived == 0 || this.isClosed) {
                    if (isAdAtTop()) {
                        Utils.slideInDown(this, null, (long) this.mAdProxy.getAnimationDelay());
                    } else {
                        Utils.slideInUp(this, null, (long) this.mAdProxy.getAnimationDelay());
                    }
                }
            }
            setVisibility(0);
            this.isClosed = false;
            if (bSendImpression) {
                this.isAdRequested = false;
                this.mAdsReceived++;
                if (this.mListener != null) {
                    this.mListener.onAdReceived(this);
                }
                sendAction(AdPlacement.ACTION_1L_ADVIEW_DISPLAYED, "impression");
            }
        }
    }

    public void onAddedToListView() {
        try {
            for (String key : this.mProxyMap.keySet()) {
                I1LouderAdProxy proxy = (I1LouderAdProxy) this.mProxyMap.get(key);
                if (!(proxy == null || proxy.getProxiedView() == null)) {
                    proxy.onAddedToListView();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        try {
            Diagnostics.m1951d(TAG(), "onDetachedFromWindow");
            pause();
            destroyImpl(true);
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
        super.onDetachedFromWindow();
    }
}
