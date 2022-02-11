package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.RelativeLayout;
import com.admarvel.android.ads.AdMarvelInternalWebView.AdMarvelInternalWebView;
import com.admarvel.android.util.AdHistoryDumpUtils;
import com.admarvel.android.util.AdMarvelLocationManager;
import com.admarvel.android.util.AdMarvelSensorManager;
import com.admarvel.android.util.Logging;
import com.admarvel.android.util.p000a.OfflineReflectionUtils;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;

@SuppressLint({"NewApi", "HandlerLeak", "DefaultLocale", "SimpleDateFormat"})
public class AdMarvelActivity extends Activity {
    private int f79A;
    private boolean f80B;
    private boolean f81C;
    private String f82D;
    private String f83E;
    private boolean f84F;
    private boolean f85G;
    private boolean f86H;
    private String f87I;
    private Handler f88J;
    private Handler f89K;
    int f90a;
    boolean f91b;
    Runnable f92c;
    final Handler f93d;
    String f94e;
    String f95f;
    public AdMarvelAd f96g;
    public C0160s f97h;
    public boolean f98i;
    public boolean f99j;
    String f100k;
    String f101l;
    String f102m;
    String f103n;
    String f104o;
    String f105p;
    String f106q;
    public String f107r;
    public String f108s;
    boolean f109t;
    public boolean f110u;
    private C0139b f111v;
    private int f112w;
    private String f113x;
    private String f114y;
    private String f115z;

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.1 */
    class C01351 implements Runnable {
        final /* synthetic */ AdMarvelActivity f18a;

        C01351(AdMarvelActivity adMarvelActivity) {
            this.f18a = adMarvelActivity;
        }

        public void run() {
            this.f18a.f91b = false;
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.2 */
    class C01362 extends Handler {
        final /* synthetic */ AdMarvelActivity f19a;

        C01362(AdMarvelActivity adMarvelActivity) {
            this.f19a = adMarvelActivity;
        }

        public void handleMessage(Message msg) {
            try {
                if (this.f19a.f80B) {
                    this.f19a.f112w = this.f19a.f112w + 1;
                    Intent intent = new Intent();
                    intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_STATE_INTENT);
                    intent.putExtra("WEBVIEW_GUID", this.f19a.f95f);
                    this.f19a.getApplicationContext().sendBroadcast(intent);
                    intent = new Intent();
                    intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                    intent.putExtra("WEBVIEW_GUID", this.f19a.f95f);
                    intent.putExtra("callback", "close");
                    this.f19a.sendBroadcast(intent);
                    this.f19a.finish();
                    if (this.f19a.f107r != null && this.f19a.f107r.length() > 0) {
                        RelativeLayout relativeLayout = (RelativeLayout) this.f19a.findViewById(this.f19a.f90a);
                        if (relativeLayout != null) {
                            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) relativeLayout.findViewWithTag(this.f19a.f94e + "WEBVIEW");
                            if (adMarvelInternalWebView != null) {
                                adMarvelInternalWebView.loadUrl("javascript:" + this.f19a.f107r + "()");
                            }
                        }
                    }
                    if (this.f19a.f112w > 2) {
                        this.f19a.finish();
                        return;
                    }
                    return;
                }
                this.f19a.finish();
            } catch (NullPointerException e) {
                Logging.log("Nullpointer exception occured in close" + e.getMessage());
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.3 */
    class C01373 extends Handler {
        final /* synthetic */ AdMarvelActivity f20a;

        C01373(AdMarvelActivity adMarvelActivity) {
            this.f20a = adMarvelActivity;
        }

        public void handleMessage(Message msg) {
            try {
                View webView;
                RelativeLayout relativeLayout = (RelativeLayout) this.f20a.findViewById(this.f20a.f90a);
                if (this.f20a.f80B) {
                    webView = AdMarvelInterstitialAds.getWebView(this.f20a.f95f);
                    if (webView != null) {
                        AdMarvelInterstitialAds.purgeWebViewMap(this.f20a.f95f);
                        webView.m308a(this.f20a);
                        webView.m309a(this.f20a, true);
                        webView.m321k();
                        if (webView.f616y) {
                            if (this.f20a.f92c != null) {
                                this.f20a.f93d.removeCallbacks(this.f20a.f92c);
                            }
                            this.f20a.f91b = true;
                            if (webView.f617z > 0) {
                                this.f20a.f93d.postDelayed(this.f20a.f92c, (long) webView.f617z);
                            }
                        }
                        if (webView.f571A != null && webView.f571A.length() > 0) {
                            this.f20a.f107r = webView.f571A;
                        }
                        if (webView.f572B != null && webView.f572B.length() > 0) {
                            this.f20a.f108s = webView.f572B;
                        }
                    } else {
                        Logging.log("Issue in preparing webview : closing activity");
                        this.f20a.m41g();
                        return;
                    }
                }
                if (this.f20a.m39e()) {
                    webView = new AdMarvelInternalWebView(this.f20a, this.f20a.f82D, this.f20a.f94e, null, null, this.f20a.f96g, AdMarvelInternalWebView.TWOPARTEXPAND, null);
                    webView.m321k();
                    webView.addJavascriptInterface(new AdMarvelWebViewJSInterface((AdMarvelInternalWebView) webView, this.f20a.f96g, this.f20a), "ADMARVEL");
                } else {
                    webView = new AdMarvelInternalWebView(this.f20a, this.f20a.f82D, this.f20a.f94e, null, null, this.f20a.f96g, AdMarvelInternalWebView.INAPPBROWSER, this.f20a.f95f);
                    webView.m321k();
                }
                webView.m322l();
                webView.setTag(this.f20a.f94e + "WEBVIEW");
                if (webView.getParent() == null) {
                    relativeLayout.addView(webView);
                } else {
                    Logging.log("Issue in loading ad : closing activity");
                    this.f20a.m41g();
                }
                View fullScreenControls;
                if (!this.f20a.f80B && !this.f20a.f84F) {
                    fullScreenControls = new FullScreenControls(this.f20a, this.f20a.getApplicationContext(), this.f20a.f94e, true);
                    fullScreenControls.setTag(this.f20a.f94e + "CONTROLS");
                    relativeLayout.addView(fullScreenControls);
                } else if (!AdMarvelUtils.isInterstitialProgressBarDisabled()) {
                    fullScreenControls = new FullScreenControls(this.f20a, this.f20a.getApplicationContext(), this.f20a.f94e, false);
                    fullScreenControls.setTag(this.f20a.f94e + "CONTROLS");
                    relativeLayout.addView(fullScreenControls);
                }
                if (this.f20a.f80B) {
                    AdMarvelInternalWebView.m277a(this.f20a.f94e, this.f20a.f111v);
                    if (webView.f614w) {
                        this.f20a.m35a(webView.f615x);
                    }
                    if (webView.f577G != null && webView.f577G.length() > 0) {
                        this.f20a.f102m = webView.f577G;
                    }
                    if (webView.f579I != null && webView.f579I.length() > 0) {
                        this.f20a.f104o = webView.f579I;
                    }
                    if (webView.f581K != null && webView.f581K.length() > 0) {
                        this.f20a.f106q = webView.f581K;
                    }
                    if (webView.f575E != null && webView.f575E.length() > 0) {
                        this.f20a.f100k = webView.f575E;
                    }
                    if (webView.f580J != null && webView.f580J.length() > 0) {
                        this.f20a.f105p = webView.f580J;
                    }
                    if (webView.f576F != null && webView.f576F.length() > 0) {
                        this.f20a.f101l = webView.f576F;
                    }
                    if (webView.f578H != null && webView.f578H.length() > 0) {
                        this.f20a.f103n = webView.f578H;
                    }
                    if (webView.f573C) {
                        this.f20a.f109t = webView.f573C;
                    }
                    if (webView.f574D != null && webView.f574D.length() > 0) {
                        if (webView.f583M) {
                            this.f20a.f99j = true;
                        }
                        if (webView.f584N) {
                            this.f20a.f110u = true;
                        }
                        if (webView.f582L) {
                            this.f20a.f93d.post(new C0152l(webView.f574D, this.f20a, webView));
                        }
                    }
                }
                if (this.f20a.f113x != null && this.f20a.f113x.length() > 0) {
                    webView.loadUrl(this.f20a.f113x);
                } else if (this.f20a.m39e() && this.f20a.f83E != null && this.f20a.f83E.length() > 0) {
                    if (AdMarvelInterstitialAds.enableOfflineSDK) {
                        if (URLUtil.isNetworkUrl(this.f20a.f83E) && Utils.m220l(this.f20a)) {
                            webView.loadUrl(this.f20a.f83E);
                        }
                        if (!URLUtil.isNetworkUrl(this.f20a.f83E)) {
                            SharedPreferences sharedPreferences = this.f20a.getSharedPreferences("admarvel_preferences", 0);
                            String str = sharedPreferences.getString("childDirectory", "NULL") + ReadOnlyContext.SEPARATOR + sharedPreferences.getString("banner_folder", "NULL");
                            if (str != null) {
                                this.f20a.f115z = OfflineReflectionUtils.m531a(str, this.f20a.f83E);
                                Logging.log("Offline SDK:Admarvel XML Response:" + this.f20a.f82D);
                            }
                            webView.loadDataWithBaseURL(this.f20a.f96g.getOfflineBaseUrl() + ReadOnlyContext.SEPARATOR, this.f20a.f115z, WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
                        }
                    } else {
                        webView.loadUrl(this.f20a.f83E);
                    }
                    if (!this.f20a.f85G) {
                        webView.m311a(this.f20a.f86H);
                    }
                    if (this.f20a.f87I != null && this.f20a.f87I.length() > 0) {
                        this.f20a.m35a(this.f20a.f87I);
                    }
                }
            } catch (Exception e) {
                Logging.log("Issue in loading ad : closing activity");
                this.f20a.m41g();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.a */
    private static class C0138a extends AsyncTask<Object, Object, Object> {
        private final WeakReference<AdMarvelActivity> f21a;
        private final AdMarvelAd f22b;

        public C0138a(AdMarvelActivity adMarvelActivity, AdMarvelAd adMarvelAd) {
            this.f21a = new WeakReference(adMarvelActivity);
            this.f22b = adMarvelAd;
        }

        protected Object doInBackground(Object... params) {
            AdMarvelActivity adMarvelActivity = this.f21a != null ? (AdMarvelActivity) this.f21a.get() : null;
            if (adMarvelActivity != null) {
                try {
                    adMarvelActivity.f89K.sendEmptyMessage(0);
                } catch (Throwable e) {
                    Logging.log(Log.getStackTraceString(e));
                    adMarvelActivity.finish();
                }
            }
            return null;
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.b */
    private static class C0139b implements AdMarvelInAppBrowserPrivateListener {
        private final WeakReference<AdMarvelActivity> f23a;

        public C0139b(AdMarvelActivity adMarvelActivity) {
            this.f23a = new WeakReference(adMarvelActivity);
        }

        public void m16a(String str) {
            AdMarvelActivity adMarvelActivity = (AdMarvelActivity) this.f23a.get();
            if (adMarvelActivity != null) {
                RelativeLayout relativeLayout = (RelativeLayout) adMarvelActivity.findViewById(adMarvelActivity.f90a);
                if (relativeLayout != null) {
                    AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) relativeLayout.findViewWithTag(str + "WEBVIEW");
                    if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && adMarvelInternalWebView.f585O != null && adMarvelInternalWebView.f585O.length() > 0) {
                        adMarvelInternalWebView.loadUrl("javascript:" + adMarvelInternalWebView.f585O + "()");
                    }
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.c */
    static class C0140c implements Runnable {
        private final WeakReference<AdMarvelActivity> f24a;

        public C0140c(AdMarvelActivity adMarvelActivity) {
            this.f24a = new WeakReference(adMarvelActivity);
        }

        public void run() {
            try {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) this.f24a.get();
                if (adMarvelActivity != null) {
                    adMarvelActivity.f98i = false;
                    RelativeLayout relativeLayout = (RelativeLayout) adMarvelActivity.findViewById(adMarvelActivity.f90a);
                    if (Version.getAndroidSDKVersion() >= 14) {
                        AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) relativeLayout.findViewWithTag(adMarvelActivity.f94e + "BR_VIDEO");
                        if (adMarvelUniversalVideoView != null) {
                            adMarvelUniversalVideoView.m380a();
                            relativeLayout.removeView(adMarvelUniversalVideoView);
                        }
                    }
                    if (adMarvelActivity.f97h != null) {
                        adMarvelActivity.f93d.removeCallbacks(adMarvelActivity.f97h);
                        adMarvelActivity.f97h = null;
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.d */
    public static class C0141d implements Runnable {
        private final AdMarvelAd f25a;
        private final Context f26b;

        public C0141d(AdMarvelAd adMarvelAd, Context context) {
            this.f25a = adMarvelAd;
            this.f26b = context;
        }

        public void run() {
            if (this.f25a != null) {
                this.f25a.setResponseJson();
            }
            AdHistoryDumpUtils b = AdHistoryDumpUtils.m550b(this.f26b);
            if (b != null && this.f25a != null) {
                int a = b.m555a(this.f26b);
                this.f25a.setAdHistoryCounter(a);
                b.m558a(this.f25a.getAdHistoryDumpString(), a);
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.e */
    private static class C0142e implements Runnable {
        private final AdMarvelAd f27a;
        private final Context f28b;

        public C0142e(AdMarvelAd adMarvelAd, Context context) {
            this.f27a = adMarvelAd;
            this.f28b = context;
        }

        public void run() {
            AdHistoryDumpUtils b = AdHistoryDumpUtils.m550b(this.f28b);
            if (b != null && this.f27a != null) {
                int adHistoryCounter = this.f27a.getAdHistoryCounter();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("/ssr_");
                stringBuilder.append(adHistoryCounter);
                stringBuilder.append(".jpg");
                b.m557a(stringBuilder.toString());
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.f */
    private class C0143f implements Runnable {
        final /* synthetic */ AdMarvelActivity f29a;
        private final AdMarvelAd f30b;

        public C0143f(AdMarvelActivity adMarvelActivity, AdMarvelAd adMarvelAd) {
            this.f29a = adMarvelActivity;
            this.f30b = adMarvelAd;
        }

        public void run() {
            try {
                new C0138a(this.f29a, this.f30b).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                this.f29a.f88J.sendEmptyMessage(0);
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.g */
    private class C0144g implements Runnable {
        final /* synthetic */ AdMarvelActivity f31a;

        private C0144g(AdMarvelActivity adMarvelActivity) {
            this.f31a = adMarvelActivity;
        }

        public void run() {
            try {
                PowerManager powerManager = (PowerManager) this.f31a.getSystemService("power");
                if (!this.f31a.isFinishing() && powerManager != null && powerManager.isScreenOn()) {
                    RelativeLayout relativeLayout = (RelativeLayout) this.f31a.findViewById(this.f31a.f90a);
                    if (relativeLayout != null) {
                        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) relativeLayout.findViewWithTag(this.f31a.f94e + "WEBVIEW");
                        if (!(adMarvelInternalWebView == null || adMarvelInternalWebView.m312b() || adMarvelInternalWebView.f601j == null || adMarvelInternalWebView.f601j.length() <= 0 || !adMarvelInternalWebView.f602k)) {
                            adMarvelInternalWebView.m315e(adMarvelInternalWebView.f601j + "(" + false + ")");
                            adMarvelInternalWebView.f602k = false;
                        }
                    }
                    if (Version.getAndroidSDKVersion() < 14) {
                        return;
                    }
                    if (this.f31a.f99j) {
                        if (((AdMarvelUniversalVideoView) ((RelativeLayout) this.f31a.findViewById(this.f31a.f90a)).findViewWithTag(this.f31a.f94e + "BR_VIDEO")) != null && this.f31a.f109t) {
                            if (!(this.f31a.f107r == null || this.f31a.f107r.length() <= 0 || relativeLayout == null)) {
                                AdMarvelInternalWebView adMarvelInternalWebView2 = (AdMarvelInternalWebView) relativeLayout.findViewWithTag(this.f31a.f94e + "WEBVIEW");
                                if (adMarvelInternalWebView2 != null) {
                                    adMarvelInternalWebView2.loadUrl("javascript:" + this.f31a.f107r + "()");
                                }
                            }
                            this.f31a.m41g();
                        }
                    } else if (this.f31a.f98i && this.f31a.f109t && ((AdMarvelUniversalVideoView) ((RelativeLayout) this.f31a.findViewById(this.f31a.f90a)).findViewWithTag(this.f31a.f94e + "BR_VIDEO")) != null) {
                        this.f31a.m41g();
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                this.f31a.f88J.sendEmptyMessage(0);
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.h */
    static class C0145h implements Runnable {
        private final WeakReference<AdMarvelInternalWebView> f32a;
        private final WeakReference<AdMarvelActivity> f33b;
        private final String f34c;

        public C0145h(AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelActivity adMarvelActivity, String str) {
            this.f32a = new WeakReference(adMarvelInternalWebView);
            this.f33b = new WeakReference(adMarvelActivity);
            this.f34c = str;
        }

        public void run() {
            try {
                if (((AdMarvelInternalWebView) this.f32a.get()) != null) {
                    Context context = (AdMarvelActivity) this.f33b.get();
                    if (context != null && this.f34c != null && this.f34c.length() > 0) {
                        if (AdMarvelInterstitialAds.enableOfflineSDK) {
                            new OfflineReflectionUtils().m537a(this.f34c, context, context.f93d);
                        } else {
                            new Utils(context).m245a(this.f34c);
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.i */
    static class C0146i implements Runnable {
        private static int f35a;
        private final WeakReference<Activity> f36b;

        static {
            f35a = ExploreByTouchHelper.INVALID_ID;
        }

        public C0146i(Activity activity) {
            this.f36b = new WeakReference(activity);
        }

        public int m17a() {
            return f35a;
        }

        public void run() {
            try {
                if (this.f36b.get() != null) {
                    f35a = ((WindowManager) ((Activity) this.f36b.get()).getSystemService("window")).getDefaultDisplay().getRotation();
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.j */
    private static class C0147j {
        static void m18a(Activity activity) {
            activity.getWindow().setFlags(ViewCompat.MEASURED_STATE_TOO_SMALL, ViewCompat.MEASURED_STATE_TOO_SMALL);
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.k */
    static class C0148k implements Runnable {
        private final String f37a;
        private final WeakReference<AdMarvelInternalWebView> f38b;
        private final WeakReference<AdMarvelActivity> f39c;
        private final WeakReference<Context> f40d;

        public C0148k(String str, AdMarvelInternalWebView adMarvelInternalWebView, Context context) {
            this.f37a = str;
            this.f38b = new WeakReference(adMarvelInternalWebView);
            this.f39c = null;
            this.f40d = new WeakReference(context);
        }

        public C0148k(String str, AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelActivity adMarvelActivity) {
            this.f37a = str;
            this.f38b = new WeakReference(adMarvelInternalWebView);
            this.f39c = new WeakReference(adMarvelActivity);
            this.f40d = new WeakReference(adMarvelActivity);
        }

        public void run() {
            try {
                AdMarvelInternalWebView adMarvelInternalWebView = this.f38b != null ? (AdMarvelInternalWebView) this.f38b.get() : null;
                Activity activity = this.f39c != null ? (AdMarvelActivity) this.f39c.get() : null;
                Context context = this.f40d != null ? (Context) this.f40d.get() : null;
                if (context != null && adMarvelInternalWebView != null) {
                    String str;
                    Location location;
                    String str2;
                    String str3;
                    String str4;
                    StringBuilder append;
                    boolean z;
                    String stringBuilder;
                    int i;
                    int i2;
                    int i3;
                    int i4;
                    ViewGroup viewGroup;
                    String a;
                    int width;
                    int height;
                    String str5;
                    int height2;
                    String str6 = "NO";
                    String a2 = Utils.m181a(context);
                    int j = Utils.m216j(context);
                    int i5 = j == 1 ? 0 : j == 2 ? 90 : -1;
                    if (!a2.equals("wifi")) {
                        if (!a2.equals("mobile")) {
                            str = "NO";
                            location = null;
                            if (Utils.m199b(context, "location")) {
                                location = AdMarvelLocationManager.m566a().m572a(adMarvelInternalWebView);
                            }
                            str2 = location == null ? "{lat:" + location.getLatitude() + ", lon:" + location.getLongitude() + ", acc:" + location.getAccuracy() + "}" : "null";
                            str3 = Version.SDK_VERSION;
                            str4 = (activity == null && activity.f84F) ? "Expanded" : "Interstitial";
                            append = new StringBuilder().append("{screen: true, orientation: true, heading: ").append(Utils.m199b(context, "compass")).append(", location : ");
                            z = Utils.m202c(context, "android.permission.ACCESS_COARSE_LOCATION") || Utils.m202c(context, "android.permission.ACCESS_FINE_LOCATION");
                            append = append.append(z).append(",shake: ").append(Utils.m199b(context, "accelerometer")).append(",tilt: ").append(Utils.m199b(context, "accelerometer")).append(", network: true, sms:").append(Utils.m230q(context)).append(", phone:").append(Utils.m230q(context)).append(", email:true,calendar:");
                            z = Utils.m202c(context, "android.permission.READ_CALENDAR") && Utils.m202c(context, "android.permission.WRITE_CALENDAR");
                            append = append.append(z).append(", camera: ").append(Utils.m199b(context, "camera")).append(", storage: ");
                            z = Utils.m202c(context, "android.permission.WRITE_EXTERNAL_STORAGE") && "mounted".equals(Environment.getExternalStorageState());
                            stringBuilder = append.append(z).append(",map:true, audio:true, video:true, 'level-1':true,'level-2': true, 'level-3':false}").toString();
                            i = 0;
                            i2 = 0;
                            i3 = 0;
                            i4 = 0;
                            str6 = "0,90";
                            if (activity == null) {
                                viewGroup = (ViewGroup) activity.getWindow().findViewById(16908290);
                                viewGroup.getWidth();
                                a = Utils.m180a(activity);
                                i = adMarvelInternalWebView.getLeft();
                                i2 = adMarvelInternalWebView.getTop();
                                width = adMarvelInternalWebView.getWidth();
                                height = adMarvelInternalWebView.getHeight();
                                i3 = viewGroup.getLeft();
                                i4 = viewGroup.getTop();
                                j = viewGroup.getWidth();
                                str5 = a;
                                height2 = viewGroup.getHeight();
                                str6 = str5;
                            } else {
                                width = Utils.m222m(context);
                                height = Utils.m224n(context);
                                j = Utils.m222m(context);
                                height2 = Utils.m224n(context);
                            }
                            adMarvelInternalWebView.m315e(this.f37a + "({x:" + i + ",y:" + i2 + ",width:" + width + ",height:" + height + ",appX:" + i3 + ",appY:" + i4 + ",appWidth:" + j + ",appHeight:" + height2 + ",orientation:" + i5 + ",networkType:" + "'" + a2 + "'" + ",network:" + "'" + str + "'" + ",screenWidth:" + Utils.m222m(context) + ",screenHeight:" + Utils.m224n(context) + ",adType:" + "'" + str4 + "'" + ",supportedFeatures:" + stringBuilder + ",sdkVersion:" + "'" + str3 + "'" + ",location:" + str2 + ",applicationSupportedOrientations:" + "'" + str6 + "'" + "})");
                        }
                    }
                    str = "YES";
                    location = null;
                    if (Utils.m199b(context, "location")) {
                        location = AdMarvelLocationManager.m566a().m572a(adMarvelInternalWebView);
                    }
                    if (location == null) {
                    }
                    str3 = Version.SDK_VERSION;
                    if (activity == null) {
                    }
                    append = new StringBuilder().append("{screen: true, orientation: true, heading: ").append(Utils.m199b(context, "compass")).append(", location : ");
                    if (!Utils.m202c(context, "android.permission.ACCESS_COARSE_LOCATION")) {
                    }
                    append = append.append(z).append(",shake: ").append(Utils.m199b(context, "accelerometer")).append(",tilt: ").append(Utils.m199b(context, "accelerometer")).append(", network: true, sms:").append(Utils.m230q(context)).append(", phone:").append(Utils.m230q(context)).append(", email:true,calendar:");
                    if (!Utils.m202c(context, "android.permission.READ_CALENDAR")) {
                    }
                    append = append.append(z).append(", camera: ").append(Utils.m199b(context, "camera")).append(", storage: ");
                    if (!Utils.m202c(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                    }
                    stringBuilder = append.append(z).append(",map:true, audio:true, video:true, 'level-1':true,'level-2': true, 'level-3':false}").toString();
                    i = 0;
                    i2 = 0;
                    i3 = 0;
                    i4 = 0;
                    str6 = "0,90";
                    if (activity == null) {
                        width = Utils.m222m(context);
                        height = Utils.m224n(context);
                        j = Utils.m222m(context);
                        height2 = Utils.m224n(context);
                    } else {
                        viewGroup = (ViewGroup) activity.getWindow().findViewById(16908290);
                        viewGroup.getWidth();
                        a = Utils.m180a(activity);
                        i = adMarvelInternalWebView.getLeft();
                        i2 = adMarvelInternalWebView.getTop();
                        width = adMarvelInternalWebView.getWidth();
                        height = adMarvelInternalWebView.getHeight();
                        i3 = viewGroup.getLeft();
                        i4 = viewGroup.getTop();
                        j = viewGroup.getWidth();
                        str5 = a;
                        height2 = viewGroup.getHeight();
                        str6 = str5;
                    }
                    try {
                        adMarvelInternalWebView.m315e(this.f37a + "({x:" + i + ",y:" + i2 + ",width:" + width + ",height:" + height + ",appX:" + i3 + ",appY:" + i4 + ",appWidth:" + j + ",appHeight:" + height2 + ",orientation:" + i5 + ",networkType:" + "'" + a2 + "'" + ",network:" + "'" + str + "'" + ",screenWidth:" + Utils.m222m(context) + ",screenHeight:" + Utils.m224n(context) + ",adType:" + "'" + str4 + "'" + ",supportedFeatures:" + stringBuilder + ",sdkVersion:" + "'" + str3 + "'" + ",location:" + str2 + ",applicationSupportedOrientations:" + "'" + str6 + "'" + "})");
                    } catch (Throwable e) {
                        Logging.log(Log.getStackTraceString(e));
                    }
                }
            } catch (Exception e2) {
                Logging.log(e2.getMessage() + " Exception in InitAdMarvel ");
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.l */
    static class C0152l implements Runnable {
        private final WeakReference<AdMarvelActivity> f50a;
        private final WeakReference<AdMarvelInternalWebView> f51b;
        private String f52c;

        /* renamed from: com.admarvel.android.ads.AdMarvelActivity.l.1 */
        class C01491 implements OnPreparedListener {
            final /* synthetic */ AdMarvelActivity f41a;
            final /* synthetic */ AdMarvelInternalWebView f42b;
            final /* synthetic */ C0152l f43c;

            C01491(C0152l c0152l, AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f43c = c0152l;
                this.f41a = adMarvelActivity;
                this.f42b = adMarvelInternalWebView;
            }

            public void onPrepared(MediaPlayer mp) {
                if (this.f41a.f99j) {
                    if (this.f41a.f110u) {
                        mp.setVolume(0.0f, 0.0f);
                    }
                    if (this.f41a.f100k != null && this.f41a.f100k.length() > 0) {
                        this.f42b.loadUrl("javascript:" + this.f41a.f100k + "()");
                    }
                    if (this.f41a.f102m != null && this.f41a.f102m.length() > 0) {
                        this.f42b.loadUrl("javascript:" + this.f41a.f102m + "()");
                        return;
                    }
                    return;
                }
                this.f42b.loadUrl("javascript:AdApp.videoView().setDuration(" + (mp.getDuration() / ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) + ")");
                this.f42b.loadUrl("javascript:AdApp.videoView().setCanPlay(true)");
                this.f42b.loadUrl("javascript:AdApp.adView().play()");
            }
        }

        /* renamed from: com.admarvel.android.ads.AdMarvelActivity.l.2 */
        class C01502 implements OnCompletionListener {
            final /* synthetic */ AdMarvelActivity f44a;
            final /* synthetic */ AdMarvelInternalWebView f45b;
            final /* synthetic */ C0152l f46c;

            C01502(C0152l c0152l, AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f46c = c0152l;
                this.f44a = adMarvelActivity;
                this.f45b = adMarvelInternalWebView;
            }

            public void onCompletion(MediaPlayer mp) {
                if (!this.f44a.f99j) {
                    this.f45b.loadUrl("javascript:AdApp.videoView().end();");
                } else if (this.f44a.f104o != null && this.f44a.f104o.length() > 0) {
                    this.f45b.loadUrl("javascript:" + this.f44a.f104o + "()");
                }
            }
        }

        /* renamed from: com.admarvel.android.ads.AdMarvelActivity.l.3 */
        class C01513 implements OnErrorListener {
            final /* synthetic */ AdMarvelActivity f47a;
            final /* synthetic */ AdMarvelInternalWebView f48b;
            final /* synthetic */ C0152l f49c;

            C01513(C0152l c0152l, AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f49c = c0152l;
                this.f47a = adMarvelActivity;
                this.f48b = adMarvelInternalWebView;
            }

            public boolean onError(MediaPlayer mp, int what, int extra) {
                this.f47a.f93d.post(new C0140c(this.f47a));
                if (this.f47a.f99j && this.f47a.f106q != null && this.f47a.f106q.length() > 0) {
                    this.f48b.loadUrl("javascript:" + this.f47a.f106q + "()");
                }
                return false;
            }
        }

        public C0152l(String str, AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f52c = str;
            this.f50a = new WeakReference(adMarvelActivity);
            this.f51b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                if (Version.getAndroidSDKVersion() >= 14) {
                    AdMarvelActivity adMarvelActivity = (AdMarvelActivity) this.f50a.get();
                    View view = (AdMarvelInternalWebView) this.f51b.get();
                    if (adMarvelActivity != null && view != null && this.f52c != null && this.f52c.length() > 0) {
                        adMarvelActivity.f98i = true;
                        RelativeLayout relativeLayout = (RelativeLayout) adMarvelActivity.findViewById(adMarvelActivity.f90a);
                        AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) relativeLayout.findViewWithTag(adMarvelActivity.f94e + "BR_VIDEO");
                        if (adMarvelUniversalVideoView == null) {
                            View adMarvelUniversalVideoView2 = new AdMarvelUniversalVideoView(adMarvelActivity);
                            adMarvelUniversalVideoView2.setTag(adMarvelActivity.f94e + "BR_VIDEO");
                            if (adMarvelActivity.f99j) {
                                LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                                layoutParams.addRule(13);
                                adMarvelUniversalVideoView2.setLayoutParams(layoutParams);
                                int childCount = relativeLayout.getChildCount();
                                int i = 0;
                                while (i < childCount && relativeLayout.getChildAt(i) != view) {
                                    i++;
                                }
                                relativeLayout.addView(adMarvelUniversalVideoView2, i);
                                relativeLayout.removeView(view);
                                view.setBackgroundColor(0);
                                view.setBackgroundDrawable(null);
                                if (Version.getAndroidSDKVersion() >= 11) {
                                    try {
                                        View.class.getMethod("setLayerType", new Class[]{Integer.TYPE, Paint.class}).invoke(view, new Object[]{Integer.valueOf(1), null});
                                    } catch (Exception e) {
                                    }
                                }
                                relativeLayout.addView(view);
                            } else {
                                adMarvelUniversalVideoView2.setLayoutParams(new RelativeLayout.LayoutParams(1, 1));
                                relativeLayout.addView(adMarvelUniversalVideoView2);
                            }
                            adMarvelUniversalVideoView2.setVideoURI(Uri.parse(this.f52c));
                            adMarvelUniversalVideoView2.setVisibility(0);
                            adMarvelUniversalVideoView2.setOnPreparedListener(new C01491(this, adMarvelActivity, view));
                            adMarvelUniversalVideoView2.setOnCompletionListener(new C01502(this, adMarvelActivity, view));
                            adMarvelUniversalVideoView2.setOnErrorListener(new C01513(this, adMarvelActivity, view));
                        } else if (this.f52c != null) {
                            adMarvelUniversalVideoView.setVideoURI(Uri.parse(this.f52c));
                        }
                    }
                }
            } catch (Throwable e2) {
                Logging.log(Log.getStackTraceString(e2));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.m */
    static class C0153m implements Runnable {
        private final WeakReference<AdMarvelActivity> f53a;
        private final WeakReference<AdMarvelInternalWebView> f54b;

        public C0153m(AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f53a = new WeakReference(adMarvelActivity);
            this.f54b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) this.f53a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f54b.get();
                if (adMarvelActivity != null && adMarvelInternalWebView != null) {
                    RelativeLayout relativeLayout = (RelativeLayout) adMarvelActivity.findViewById(adMarvelActivity.f90a);
                    if (Version.getAndroidSDKVersion() >= 14) {
                        AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) relativeLayout.findViewWithTag(adMarvelActivity.f94e + "BR_VIDEO");
                        if (adMarvelUniversalVideoView != null && adMarvelUniversalVideoView.isPlaying()) {
                            adMarvelUniversalVideoView.pause();
                            if (adMarvelActivity.f99j && adMarvelActivity.f105p != null && adMarvelActivity.f105p.length() > 0) {
                                adMarvelInternalWebView.loadUrl("javascript:" + adMarvelActivity.f105p + "()");
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.n */
    static class C0155n implements Runnable {
        private final WeakReference<AdMarvelActivity> f59a;
        private final WeakReference<AdMarvelInternalWebView> f60b;

        /* renamed from: com.admarvel.android.ads.AdMarvelActivity.n.1 */
        class C01541 implements Runnable {
            final /* synthetic */ AdMarvelUniversalVideoView f55a;
            final /* synthetic */ AdMarvelInternalWebView f56b;
            final /* synthetic */ AdMarvelActivity f57c;
            final /* synthetic */ C0155n f58d;

            C01541(C0155n c0155n, AdMarvelUniversalVideoView adMarvelUniversalVideoView, AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelActivity adMarvelActivity) {
                this.f58d = c0155n;
                this.f55a = adMarvelUniversalVideoView;
                this.f56b = adMarvelInternalWebView;
                this.f57c = adMarvelActivity;
            }

            public void run() {
                if (this.f55a.getHeight() == 0) {
                    this.f56b.loadUrl("javascript:" + this.f57c.f106q + "()");
                } else {
                    this.f55a.start();
                }
            }
        }

        public C0155n(AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f59a = new WeakReference(adMarvelActivity);
            this.f60b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) this.f59a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f60b.get();
                if (adMarvelActivity != null && adMarvelInternalWebView != null) {
                    RelativeLayout relativeLayout = (RelativeLayout) adMarvelActivity.findViewById(adMarvelActivity.f90a);
                    if (Version.getAndroidSDKVersion() >= 14) {
                        AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) relativeLayout.findViewWithTag(adMarvelActivity.f94e + "BR_VIDEO");
                        if (adMarvelUniversalVideoView != null) {
                            if (adMarvelUniversalVideoView.getHeight() == 0) {
                                adMarvelActivity.f93d.postDelayed(new C01541(this, adMarvelUniversalVideoView, adMarvelInternalWebView, adMarvelActivity), 500);
                            } else {
                                adMarvelUniversalVideoView.start();
                            }
                        }
                        if (adMarvelActivity.f97h == null) {
                            adMarvelActivity.f97h = new C0160s(adMarvelActivity, adMarvelInternalWebView);
                            adMarvelActivity.f93d.postDelayed(adMarvelActivity.f97h, 1000);
                        }
                        if (adMarvelActivity.f99j && adMarvelActivity.f101l != null && adMarvelActivity.f101l.length() > 0) {
                            adMarvelInternalWebView.loadUrl("javascript:" + adMarvelActivity.f101l + "()");
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.o */
    static class C0156o implements Runnable {
        private final WeakReference<AdMarvelActivity> f61a;
        private final WeakReference<AdMarvelInternalWebView> f62b;
        private float f63c;
        private float f64d;
        private float f65e;
        private float f66f;

        public C0156o(AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView, float f, float f2, float f3, float f4) {
            this.f61a = new WeakReference(adMarvelActivity);
            this.f62b = new WeakReference(adMarvelInternalWebView);
            this.f63c = f;
            this.f64d = f2;
            this.f65e = f3;
            this.f66f = f4;
        }

        public void run() {
            try {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) this.f61a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f62b.get();
                if (adMarvelActivity != null && adMarvelInternalWebView != null) {
                    adMarvelInternalWebView.loadUrl("javascript:console.log(\"Window Innerwidth \"+window.innerWidth);");
                    RelativeLayout relativeLayout = (RelativeLayout) adMarvelActivity.findViewById(adMarvelActivity.f90a);
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = null;
                    if (relativeLayout != null) {
                        adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) relativeLayout.findViewWithTag(adMarvelActivity.f94e + "BR_VIDEO");
                    }
                    if (adMarvelUniversalVideoView != null && adMarvelActivity.f97h != null) {
                        int width = relativeLayout.getWidth();
                        int height = relativeLayout.getHeight();
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) adMarvelUniversalVideoView.getLayoutParams();
                        layoutParams.width = (int) (((float) width) * this.f65e);
                        layoutParams.height = (int) (((float) height) * this.f66f);
                        layoutParams.leftMargin = (int) (((float) width) * this.f63c);
                        layoutParams.topMargin = (int) (((float) height) * this.f64d);
                        adMarvelUniversalVideoView.setLayoutParams(layoutParams);
                        adMarvelUniversalVideoView.forceLayout();
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.p */
    static class C0157p implements Runnable {
        private final WeakReference<AdMarvelActivity> f67a;
        private final WeakReference<AdMarvelInternalWebView> f68b;
        private float f69c;

        public C0157p(AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView, float f) {
            this.f67a = new WeakReference(adMarvelActivity);
            this.f68b = new WeakReference(adMarvelInternalWebView);
            this.f69c = f;
        }

        public void run() {
            try {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) this.f67a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f68b.get();
                if (adMarvelActivity != null && adMarvelInternalWebView != null) {
                    RelativeLayout relativeLayout = (RelativeLayout) adMarvelActivity.findViewById(adMarvelActivity.f90a);
                    if (relativeLayout != null) {
                        AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) relativeLayout.findViewWithTag(adMarvelActivity.f94e + "BR_VIDEO");
                        if (adMarvelUniversalVideoView != null) {
                            adMarvelUniversalVideoView.seekTo((int) (this.f69c * 1000.0f));
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.q */
    static class C0158q implements Runnable {
        private final WeakReference<Activity> f70a;
        private String f71b;
        private Activity f72c;

        public C0158q(Activity activity, String str) {
            this.f71b = null;
            this.f72c = null;
            this.f70a = new WeakReference(activity);
            this.f71b = str;
        }

        public void run() {
            try {
                this.f72c = (Activity) this.f70a.get();
                if (this.f72c != null && this.f71b != null) {
                    Display defaultDisplay = ((WindowManager) this.f72c.getSystemService("window")).getDefaultDisplay();
                    if (this.f71b.equalsIgnoreCase("Portrait")) {
                        this.f72c.setRequestedOrientation(1);
                        if (defaultDisplay.getRotation() != 0) {
                            this.f72c.setRequestedOrientation(9);
                        }
                    } else if (this.f71b.equalsIgnoreCase("LandscapeLeft")) {
                        this.f72c.setRequestedOrientation(0);
                        if (defaultDisplay.getRotation() != 1) {
                            this.f72c.setRequestedOrientation(8);
                        }
                    } else if (this.f71b.equalsIgnoreCase("PortraitUpSideDown")) {
                        this.f72c.setRequestedOrientation(9);
                        if (defaultDisplay.getRotation() != 2) {
                            this.f72c.setRequestedOrientation(1);
                        }
                    } else if (this.f71b.equalsIgnoreCase("LandscapeRight")) {
                        this.f72c.setRequestedOrientation(8);
                        if (defaultDisplay.getRotation() != 3) {
                            this.f72c.setRequestedOrientation(0);
                        }
                    } else if (!this.f71b.equalsIgnoreCase("none")) {
                    } else {
                        if (defaultDisplay.getRotation() == 2) {
                            this.f72c.setRequestedOrientation(9);
                            if (defaultDisplay.getRotation() != 2) {
                                this.f72c.setRequestedOrientation(1);
                            }
                        } else if (defaultDisplay.getRotation() == 3) {
                            this.f72c.setRequestedOrientation(8);
                            if (defaultDisplay.getRotation() != 3) {
                                this.f72c.setRequestedOrientation(0);
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.r */
    static class C0159r implements Runnable {
        private final WeakReference<AdMarvelActivity> f73a;
        private final WeakReference<AdMarvelInternalWebView> f74b;

        public C0159r(AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f73a = new WeakReference(adMarvelActivity);
            this.f74b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) this.f73a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f74b.get();
                if (adMarvelActivity != null && adMarvelInternalWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) ((RelativeLayout) adMarvelActivity.findViewById(adMarvelActivity.f90a)).findViewWithTag(adMarvelActivity.f94e + "BR_VIDEO");
                    if (adMarvelUniversalVideoView != null && adMarvelUniversalVideoView.isPlaying()) {
                        adMarvelUniversalVideoView.m380a();
                        adMarvelInternalWebView.loadUrl("javascript:stop()");
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.s */
    static class C0160s implements Runnable {
        private final WeakReference<AdMarvelActivity> f75a;
        private final WeakReference<AdMarvelInternalWebView> f76b;

        public C0160s(AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f75a = new WeakReference(adMarvelActivity);
            this.f76b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) this.f75a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f76b.get();
                if (adMarvelActivity != null && adMarvelInternalWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) ((RelativeLayout) adMarvelActivity.findViewById(adMarvelActivity.f90a)).findViewWithTag(adMarvelActivity.f94e + "BR_VIDEO");
                    if (!(adMarvelInternalWebView == null || adMarvelUniversalVideoView == null)) {
                        try {
                            if (!adMarvelActivity.f99j) {
                                adMarvelInternalWebView.loadUrl("javascript:AdApp.videoView().setCurrentTime(" + (adMarvelUniversalVideoView.getCurrentPosition() / ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) + ")");
                            } else if (adMarvelActivity.f103n != null && adMarvelActivity.f103n.length() > 0) {
                                adMarvelInternalWebView.loadUrl("javascript:" + adMarvelActivity.f103n + "(" + (((float) adMarvelUniversalVideoView.getCurrentPosition()) / 1000.0f) + ")");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adMarvelActivity.f93d.postDelayed(adMarvelActivity.f97h, 1000);
                }
            } catch (Throwable e2) {
                Logging.log(Log.getStackTraceString(e2));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelActivity.t */
    static class C0161t implements Runnable {
        private final WeakReference<AdMarvelActivity> f77a;
        private String f78b;

        public C0161t(AdMarvelActivity adMarvelActivity, String str) {
            this.f77a = new WeakReference(adMarvelActivity);
            this.f78b = str;
        }

        public void run() {
            try {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) this.f77a.get();
                if (adMarvelActivity != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) ((RelativeLayout) adMarvelActivity.findViewById(adMarvelActivity.f90a)).findViewWithTag(adMarvelActivity.f94e + "BR_VIDEO");
                    if (adMarvelUniversalVideoView != null && this.f78b != null && this.f78b.trim().length() > 0) {
                        if (this.f78b.equalsIgnoreCase("mute")) {
                            adMarvelUniversalVideoView.m385d();
                        } else if (this.f78b.equalsIgnoreCase("unmute")) {
                            adMarvelUniversalVideoView.m386e();
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    public AdMarvelActivity() {
        this.f90a = 103422;
        this.f91b = false;
        this.f112w = 0;
        this.f92c = new C01351(this);
        this.f80B = false;
        this.f81C = false;
        this.f93d = new Handler();
        this.f98i = false;
        this.f99j = false;
        this.f109t = false;
        this.f110u = false;
        this.f84F = false;
        this.f85G = false;
        this.f86H = false;
        this.f88J = new C01362(this);
        this.f89K = new C01373(this);
    }

    public int m34a() {
        return this.f112w;
    }

    public void m35a(String str) {
        int i;
        Logging.log("DisableActivityOrientation");
        if (Version.getAndroidSDKVersion() < 9) {
            i = getResources().getConfiguration().orientation;
        } else {
            C0146i c0146i = new C0146i(this);
            c0146i.run();
            int i2 = 0;
            i = ExploreByTouchHelper.INVALID_ID;
            while (i == ExploreByTouchHelper.INVALID_ID && i2 < 20) {
                i2++;
                i = c0146i.m17a();
            }
        }
        if (str != null) {
            if (Version.getAndroidSDKVersion() < 9) {
                if (str.equalsIgnoreCase("Portrait")) {
                    setRequestedOrientation(1);
                } else if (str.equalsIgnoreCase("LandscapeLeft")) {
                    setRequestedOrientation(0);
                } else if (!str.equalsIgnoreCase("Current")) {
                } else {
                    if (i == 1) {
                        setRequestedOrientation(1);
                    } else if (i == 2) {
                        setRequestedOrientation(0);
                    }
                }
            } else if (str.equalsIgnoreCase("Portrait")) {
                setRequestedOrientation(1);
            } else if (str.equalsIgnoreCase("LandscapeLeft")) {
                setRequestedOrientation(0);
            } else if (!str.equalsIgnoreCase("Current")) {
                this.f93d.post(new C0158q(this, str));
            } else if (i == 0) {
                this.f93d.post(new C0158q(this, "Portrait"));
            } else if (i == 1) {
                this.f93d.post(new C0158q(this, "LandscapeLeft"));
            } else {
                this.f93d.post(new C0158q(this, "none"));
            }
        } else if (Version.getAndroidSDKVersion() < 9) {
            if (i == 1) {
                setRequestedOrientation(1);
            } else if (i == 2) {
                setRequestedOrientation(0);
            }
        } else if (i == 0) {
            this.f93d.post(new C0158q(this, "Portrait"));
        } else if (i == 1) {
            this.f93d.post(new C0158q(this, "LandscapeLeft"));
        } else {
            this.f93d.post(new C0158q(this, "none"));
        }
    }

    public void m36b() {
        this.f112w++;
    }

    boolean m37c() {
        return this.f80B;
    }

    boolean m38d() {
        return this.f81C;
    }

    boolean m39e() {
        return this.f84F;
    }

    String m40f() {
        return this.f114y;
    }

    void m41g() {
        this.f88J.sendEmptyMessage(0);
    }

    public void m42h() {
        this.f93d.postDelayed(new C0141d(this.f96g, this), 1000);
    }

    public void m43i() {
        this.f93d.postDelayed(new C0142e(this.f96g, this), 3000);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AdMarvelUtils.getAdmarvelActivityOrientationInfo(this) != null) {
            setRequestedOrientation(AdMarvelUtils.getAdmarvelActivityOrientationInfo(this).intValue());
        }
        if (Version.getAndroidSDKVersion() >= 11) {
            C0147j.m18a(this);
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.f113x = extras.getString(SettingsJsonConstants.APP_URL_KEY);
            this.f114y = extras.getString(locationTracking.source);
            this.f115z = extras.getString(Constants.NATIVE_VIDEO_AD_HTML_ELEMENT);
            this.f82D = extras.getString("xml");
            this.f80B = extras.getBoolean("isInterstitial", false);
            this.f81C = extras.getBoolean("isInterstitialClick", false);
            this.f94e = extras.getString("GUID");
            this.f95f = extras.getString("WEBVIEW_GUID");
            if (m37c()) {
                AdMarvelWebViewJSInterface.updateActivity(this);
                AdMarvelBrightrollJSInterface.updateActivityContext(this);
            }
            byte[] byteArray = extras.getByteArray("serialized_admarvelad");
            if (byteArray != null) {
                try {
                    this.f96g = (AdMarvelAd) new ObjectInputStream(new ByteArrayInputStream(byteArray)).readObject();
                } catch (Throwable e) {
                    Logging.log(Log.getStackTraceString(e));
                }
            }
            this.f79A = extras.getInt("backgroundcolor");
            this.f83E = extras.getString("expand_url");
            if (this.f83E != null && this.f83E.length() > 0) {
                this.f84F = true;
                this.f85G = extras.getBoolean("closeBtnEnabled");
                this.f86H = extras.getBoolean("closeAreaEnabled");
                this.f87I = extras.getString("orientationState");
            }
        }
        if (AdMarvelUtils.isNotificationBarInFullScreenLaunchEnabled() || (!this.f80B && (Build.MODEL.contains("Kindle") || Build.PRODUCT.contains("Kindle")))) {
            getWindow().addFlags(KEYRecord.OWNER_ZONE);
            getWindow().clearFlags(Flags.FLAG5);
        } else {
            getWindow().setFlags(KEYRecord.OWNER_HOST, KEYRecord.OWNER_HOST);
        }
        this.f111v = new C0139b(this);
        View relativeLayout = new RelativeLayout(this);
        relativeLayout.setId(this.f90a);
        relativeLayout.setLayoutParams(new LayoutParams(-1, -1));
        relativeLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        setContentView(relativeLayout);
        if (this.f80B && (this instanceof AdMarvelActivity)) {
            AdMarvelInternalWebView webView = AdMarvelInterstitialAds.getWebView(this.f95f);
            if (webView != null) {
                webView.m308a((Context) this);
            }
            Intent intent = new Intent();
            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
            intent.putExtra("WEBVIEW_GUID", this.f95f);
            intent.putExtra("callback", "activitylaunch");
            sendBroadcast(intent);
            intent = new Intent();
            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
            intent.putExtra("WEBVIEW_GUID", this.f95f);
            intent.putExtra("callback", "displayed");
            sendBroadcast(intent);
        }
        if (Version.getAndroidSDKVersion() >= 11) {
            this.f93d.post(new C0143f(this, this.f96g));
        } else {
            new C0138a(this, this.f96g).execute(new Object[0]);
        }
    }

    protected void onDestroy() {
        AdMarvelInternalWebView adMarvelInternalWebView;
        AdMarvelSensorManager a = AdMarvelSensorManager.m579a();
        if (a.m593b()) {
            a.m595c();
        }
        this.f93d.post(new C0140c(this));
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(this.f90a);
        if (m39e() && AdMarvelInternalWebView.m286c(this.f94e) != null) {
            AdMarvelInternalWebView.m286c(this.f94e).m342a();
        }
        if (relativeLayout != null) {
            adMarvelInternalWebView = (AdMarvelInternalWebView) relativeLayout.findViewWithTag(this.f94e + "WEBVIEW");
            if (adMarvelInternalWebView != null && adMarvelInternalWebView.f601j != null && adMarvelInternalWebView.f601j.length() > 0 && adMarvelInternalWebView.f602k) {
                adMarvelInternalWebView.m315e(adMarvelInternalWebView.f601j + "(" + false + ")");
                adMarvelInternalWebView.f602k = false;
            }
        }
        if (this.f80B) {
            AdMarvelInternalWebView.m283b(this.f94e);
            AdMarvelInterstitialAds.purgeWebViewMap(this.f95f);
            Intent intent = new Intent();
            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_STATE_INTENT);
            intent.putExtra("WEBVIEW_GUID", this.f95f);
            getApplicationContext().sendBroadcast(intent);
            intent = new Intent();
            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
            intent.putExtra("WEBVIEW_GUID", this.f95f);
            intent.putExtra("callback", "unregisterreceiver");
            getApplicationContext().sendBroadcast(intent);
        }
        if (relativeLayout != null) {
            adMarvelInternalWebView = (AdMarvelInternalWebView) relativeLayout.findViewWithTag(this.f94e + "WEBVIEW");
            if (adMarvelInternalWebView != null) {
                relativeLayout.removeView(adMarvelInternalWebView);
                adMarvelInternalWebView.loadUrl(Stomp.EMPTY);
                adMarvelInternalWebView.m306a();
            }
        }
        if (!AdMarvelUtils.isLogDumpEnabled()) {
            Utils.m236t((Context) this);
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == 0) {
            switch (keyCode) {
                case Type.MF /*4*/:
                    if (this.f91b) {
                        return false;
                    }
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(this.f90a);
                    if (relativeLayout != null) {
                        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) relativeLayout.findViewWithTag(this.f94e + "WEBVIEW");
                        if (adMarvelInternalWebView != null) {
                            if (adMarvelInternalWebView.f586P) {
                                adMarvelInternalWebView.m324n();
                            } else {
                                m41g();
                            }
                        }
                    } else {
                        m41g();
                    }
                    if (!(m37c() || AdMarvelInternalWebView.m288d(this.f94e) == null)) {
                        AdMarvelInternalWebView.m288d(this.f94e).m15a(this.f94e);
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onPause() {
        AdMarvelInternalWebView adMarvelInternalWebView;
        try {
            AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, this).pause();
        } catch (Exception e) {
        }
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(this.f90a);
        if (relativeLayout != null) {
            adMarvelInternalWebView = (AdMarvelInternalWebView) relativeLayout.findViewWithTag(this.f94e + "WEBVIEW");
            if (adMarvelInternalWebView != null) {
                if (Version.getAndroidSDKVersion() >= 8 && Version.getAndroidSDKVersion() <= 18) {
                    adMarvelInternalWebView.m324n();
                }
                adMarvelInternalWebView.m316f();
            }
        }
        if (!(isFinishing() || this.f108s == null || this.f108s.length() <= 0)) {
            relativeLayout = (RelativeLayout) findViewById(this.f90a);
            if (relativeLayout != null) {
                adMarvelInternalWebView = (AdMarvelInternalWebView) relativeLayout.findViewWithTag(this.f94e + "WEBVIEW");
                if (adMarvelInternalWebView != null) {
                    adMarvelInternalWebView.loadUrl("javascript:" + this.f108s + "()");
                }
            }
        }
        if (m37c()) {
            try {
                AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, this).pause();
            } catch (Exception e2) {
            }
        }
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        if (m37c()) {
            AdMarvelWebViewJSInterface.updateActivity(this);
            AdMarvelBrightrollJSInterface.updateActivityContext(this);
        }
        try {
            AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, this).resume();
        } catch (Exception e) {
        }
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(this.f90a);
        if (relativeLayout != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) relativeLayout.findViewWithTag(this.f94e + "WEBVIEW");
            if (adMarvelInternalWebView != null) {
                if (Version.getAndroidSDKVersion() >= 11) {
                    ac.m254a(adMarvelInternalWebView);
                } else {
                    ad.m256a(adMarvelInternalWebView);
                }
                adMarvelInternalWebView.m317g();
            }
        }
    }

    protected void onStart() {
        if (m37c()) {
            try {
                AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, this).start();
            } catch (Exception e) {
            }
        }
        super.onStart();
    }

    protected void onStop() {
        try {
            AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, this).stop();
        } catch (Exception e) {
        }
        if (Version.getAndroidSDKVersion() >= 7) {
            this.f93d.post(new C0144g());
        }
        if (m37c()) {
            try {
                AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, this).stop();
            } catch (Exception e2) {
            }
        }
        super.onStop();
    }
}
