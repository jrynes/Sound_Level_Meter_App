package com.admarvel.android.ads;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.ExploreByTouchHelper;
import android.webkit.JavascriptInterface;
import com.admarvel.android.ads.AdMarvelActivity.C0140c;
import com.admarvel.android.ads.AdMarvelActivity.C0145h;
import com.admarvel.android.ads.AdMarvelActivity.C0148k;
import com.admarvel.android.ads.AdMarvelActivity.C0152l;
import com.admarvel.android.ads.AdMarvelActivity.C0153m;
import com.admarvel.android.ads.AdMarvelActivity.C0155n;
import com.admarvel.android.ads.AdMarvelActivity.C0157p;
import com.admarvel.android.ads.AdMarvelActivity.C0158q;
import com.admarvel.android.ads.AdMarvelActivity.C0159r;
import com.admarvel.android.ads.AdMarvelActivity.C0161t;
import com.admarvel.android.ads.AdMarvelMediationActivity.C0174b;
import com.admarvel.android.ads.AdMarvelUtils.AdMarvelVideoEvents;
import com.admarvel.android.ads.AdMarvelWebView.AdMarvelWebView;
import com.admarvel.android.ads.AdMarvelWebView.ab;
import com.admarvel.android.ads.AdMarvelWebView.ac;
import com.admarvel.android.ads.AdMarvelWebView.ad;
import com.admarvel.android.ads.AdMarvelWebView.ae;
import com.admarvel.android.ads.AdMarvelWebView.af;
import com.admarvel.android.ads.AdMarvelWebView.ag;
import com.admarvel.android.ads.AdMarvelWebView.ah;
import com.admarvel.android.ads.AdMarvelWebView.aj;
import com.admarvel.android.ads.AdMarvelWebView.ak;
import com.admarvel.android.ads.AdMarvelWebView.al;
import com.admarvel.android.ads.Utils.C0234c;
import com.admarvel.android.ads.Utils.C0235d;
import com.admarvel.android.ads.Utils.C0236e;
import com.admarvel.android.ads.Utils.C0249r;
import com.admarvel.android.ads.Utils.C0253t;
import com.admarvel.android.ads.Utils.C0256u;
import com.admarvel.android.util.AdMarvelConnectivityChangeReceiver;
import com.admarvel.android.util.AdMarvelLocationManager;
import com.admarvel.android.util.AdMarvelSensorManager;
import com.admarvel.android.util.AdMarvelThreadExecutorService;
import com.admarvel.android.util.Logging;
import com.rabbitmq.client.impl.AMQConnection;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;

public class AdMarvelWebViewJSInterface {
    private static WeakReference<AdMarvelActivity> adMarvelActivityReference;
    private static WeakReference<Context> contextReference;
    private static int lastOrientation;
    final AdMarvelAd adMarvelAd;
    final WeakReference<AdMarvelInternalWebView> adMarvelInternalWebViewReference;
    private final WeakReference<AdMarvelWebView> adMarvelWebViewReference;
    private boolean isInterstitial;
    private String lockedOrientation;

    /* renamed from: com.admarvel.android.ads.AdMarvelWebViewJSInterface.1 */
    class C02181 implements OnClickListener {
        final /* synthetic */ AdMarvelWebViewJSInterface f382a;

        C02181(AdMarvelWebViewJSInterface adMarvelWebViewJSInterface) {
            this.f382a = adMarvelWebViewJSInterface;
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelWebViewJSInterface.2 */
    class C02192 implements OnClickListener {
        final /* synthetic */ AdMarvelInternalWebView f383a;
        final /* synthetic */ Activity f384b;
        final /* synthetic */ String f385c;
        final /* synthetic */ String f386d;
        final /* synthetic */ String f387e;
        final /* synthetic */ AdMarvelWebViewJSInterface f388f;

        C02192(AdMarvelWebViewJSInterface adMarvelWebViewJSInterface, AdMarvelInternalWebView adMarvelInternalWebView, Activity activity, String str, String str2, String str3) {
            this.f388f = adMarvelWebViewJSInterface;
            this.f383a = adMarvelInternalWebView;
            this.f384b = activity;
            this.f385c = str;
            this.f386d = str2;
            this.f387e = str3;
        }

        public void onClick(DialogInterface dialog, int id) {
            if (Version.getAndroidSDKVersion() >= 14) {
                AdMarvelThreadExecutorService.m597a().m598b().execute(new C0236e(this.f383a, this.f384b, this.f385c, this.f386d, this.f387e));
            } else {
                AdMarvelThreadExecutorService.m597a().m598b().execute(new C0235d(this.f383a, this.f384b, this.f385c, this.f386d, this.f387e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelWebViewJSInterface.3 */
    class C02203 implements OnClickListener {
        final /* synthetic */ AdMarvelWebViewJSInterface f389a;

        C02203(AdMarvelWebViewJSInterface adMarvelWebViewJSInterface) {
            this.f389a = adMarvelWebViewJSInterface;
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelWebViewJSInterface.4 */
    class C02214 implements OnClickListener {
        final /* synthetic */ AdMarvelInternalWebView f390a;
        final /* synthetic */ Activity f391b;
        final /* synthetic */ String f392c;
        final /* synthetic */ String f393d;
        final /* synthetic */ String f394e;
        final /* synthetic */ String f395f;
        final /* synthetic */ String f396g;
        final /* synthetic */ String f397h;
        final /* synthetic */ int f398i;
        final /* synthetic */ AdMarvelWebViewJSInterface f399j;

        C02214(AdMarvelWebViewJSInterface adMarvelWebViewJSInterface, AdMarvelInternalWebView adMarvelInternalWebView, Activity activity, String str, String str2, String str3, String str4, String str5, String str6, int i) {
            this.f399j = adMarvelWebViewJSInterface;
            this.f390a = adMarvelInternalWebView;
            this.f391b = activity;
            this.f392c = str;
            this.f393d = str2;
            this.f394e = str3;
            this.f395f = str4;
            this.f396g = str5;
            this.f397h = str6;
            this.f398i = i;
        }

        public void onClick(DialogInterface dialog, int id) {
            if (Version.getAndroidSDKVersion() >= 14) {
                AdMarvelThreadExecutorService.m597a().m598b().execute(new C0236e(this.f390a, this.f391b, this.f392c, this.f393d, this.f394e, this.f395f, this.f396g, this.f397h, this.f398i));
            } else {
                AdMarvelThreadExecutorService.m597a().m598b().execute(new C0235d(this.f390a, this.f391b, this.f392c, this.f393d, this.f394e, this.f395f, this.f396g, this.f397h, this.f398i));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelWebViewJSInterface.5 */
    class C02225 implements OnClickListener {
        final /* synthetic */ String f400a;
        final /* synthetic */ AdMarvelInternalWebView f401b;
        final /* synthetic */ AdMarvelWebViewJSInterface f402c;

        C02225(AdMarvelWebViewJSInterface adMarvelWebViewJSInterface, String str, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f402c = adMarvelWebViewJSInterface;
            this.f400a = str;
            this.f401b = adMarvelInternalWebView;
        }

        public void onClick(DialogInterface dialog, int id) {
            if (this.f400a != null) {
                this.f401b.m315e(this.f400a + "(\"NO\")");
            }
            dialog.cancel();
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelWebViewJSInterface.6 */
    class C02236 implements OnClickListener {
        final /* synthetic */ AdMarvelInternalWebView f403a;
        final /* synthetic */ Activity f404b;
        final /* synthetic */ String f405c;
        final /* synthetic */ String f406d;
        final /* synthetic */ String f407e;
        final /* synthetic */ String f408f;
        final /* synthetic */ String f409g;
        final /* synthetic */ String f410h;
        final /* synthetic */ int f411i;
        final /* synthetic */ String f412j;
        final /* synthetic */ String f413k;
        final /* synthetic */ String f414l;
        final /* synthetic */ String f415m;
        final /* synthetic */ int f416n;
        final /* synthetic */ int f417o;
        final /* synthetic */ String f418p;
        final /* synthetic */ AdMarvelWebViewJSInterface f419q;

        C02236(AdMarvelWebViewJSInterface adMarvelWebViewJSInterface, AdMarvelInternalWebView adMarvelInternalWebView, Activity activity, String str, String str2, String str3, String str4, String str5, String str6, int i, String str7, String str8, String str9, String str10, int i2, int i3, String str11) {
            this.f419q = adMarvelWebViewJSInterface;
            this.f403a = adMarvelInternalWebView;
            this.f404b = activity;
            this.f405c = str;
            this.f406d = str2;
            this.f407e = str3;
            this.f408f = str4;
            this.f409g = str5;
            this.f410h = str6;
            this.f411i = i;
            this.f412j = str7;
            this.f413k = str8;
            this.f414l = str9;
            this.f415m = str10;
            this.f416n = i2;
            this.f417o = i3;
            this.f418p = str11;
        }

        public void onClick(DialogInterface dialog, int id) {
            if (Version.getAndroidSDKVersion() >= 14) {
                ExecutorService b = AdMarvelThreadExecutorService.m597a().m598b();
                ExecutorService executorService = b;
                executorService.execute(new C0236e(this.f403a, this.f404b, this.f405c, this.f406d, this.f407e, this.f408f, this.f409g, this.f410h, this.f411i, this.f412j, this.f413k, this.f414l, this.f415m, this.f416n, this.f417o, this.f418p));
                return;
            }
            b = AdMarvelThreadExecutorService.m597a().m598b();
            executorService = b;
            executorService.execute(new C0235d(this.f403a, this.f404b, this.f405c, this.f406d, this.f407e, this.f408f, this.f409g, this.f410h, this.f411i, this.f412j, this.f413k, this.f414l, this.f415m, this.f416n, this.f417o, this.f418p));
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelWebViewJSInterface.7 */
    class C02247 implements OnClickListener {
        final /* synthetic */ String f420a;
        final /* synthetic */ AdMarvelInternalWebView f421b;
        final /* synthetic */ AdMarvelWebViewJSInterface f422c;

        C02247(AdMarvelWebViewJSInterface adMarvelWebViewJSInterface, String str, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f422c = adMarvelWebViewJSInterface;
            this.f420a = str;
            this.f421b = adMarvelInternalWebView;
        }

        public void onClick(DialogInterface dialog, int id) {
            if (this.f420a != null) {
                this.f421b.m315e(this.f420a + "(\"NO\")");
            }
            dialog.cancel();
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelWebViewJSInterface.8 */
    class C02258 implements OnClickListener {
        final /* synthetic */ AdMarvelInternalWebView f423a;
        final /* synthetic */ String f424b;
        final /* synthetic */ String f425c;
        final /* synthetic */ AdMarvelWebViewJSInterface f426d;

        C02258(AdMarvelWebViewJSInterface adMarvelWebViewJSInterface, AdMarvelInternalWebView adMarvelInternalWebView, String str, String str2) {
            this.f426d = adMarvelWebViewJSInterface;
            this.f423a = adMarvelInternalWebView;
            this.f424b = str;
            this.f425c = str2;
        }

        public void onClick(DialogInterface dialog, int id) {
            if (Version.getAndroidSDKVersion() < 8) {
                AdMarvelThreadExecutorService.m597a().m598b().execute(new C0256u(this.f423a, this.f424b, this.f425c));
            } else {
                AdMarvelThreadExecutorService.m597a().m598b().execute(new C0253t(this.f423a, this.f424b, this.f425c));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelWebViewJSInterface.9 */
    class C02269 implements Runnable {
        final /* synthetic */ String f427a;
        final /* synthetic */ AdMarvelActivity f428b;
        final /* synthetic */ AdMarvelInternalWebView f429c;
        final /* synthetic */ AdMarvelWebViewJSInterface f430d;

        C02269(AdMarvelWebViewJSInterface adMarvelWebViewJSInterface, String str, AdMarvelActivity adMarvelActivity, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f430d = adMarvelWebViewJSInterface;
            this.f427a = str;
            this.f428b = adMarvelActivity;
            this.f429c = adMarvelInternalWebView;
        }

        public void run() {
            AdMarvelEventHandler.m269a(this.f427a, this.f428b.f96g.getAdMarvelEvent(), this.f428b, this.f429c.getAdMarvelInterstitialAdsInstance());
        }
    }

    public AdMarvelWebViewJSInterface(AdMarvelInternalWebView vw, AdMarvelAd ad, Context context) {
        this.lockedOrientation = null;
        this.isInterstitial = false;
        this.adMarvelInternalWebViewReference = new WeakReference(vw);
        this.adMarvelWebViewReference = null;
        contextReference = new WeakReference(context);
        this.adMarvelAd = ad;
        adMarvelActivityReference = null;
        this.isInterstitial = true;
    }

    public AdMarvelWebViewJSInterface(AdMarvelInternalWebView vw, AdMarvelAd ad, AdMarvelActivity adMarvelActivity) {
        this.lockedOrientation = null;
        this.isInterstitial = false;
        this.adMarvelInternalWebViewReference = new WeakReference(vw);
        this.adMarvelWebViewReference = null;
        contextReference = new WeakReference(adMarvelActivity);
        this.adMarvelAd = ad;
        adMarvelActivityReference = new WeakReference(adMarvelActivity);
        this.isInterstitial = true;
        lastOrientation = adMarvelActivity.getRequestedOrientation();
    }

    public AdMarvelWebViewJSInterface(AdMarvelInternalWebView vw, AdMarvelAd ad, AdMarvelWebView adMarvelWebView, Context context) {
        this.lockedOrientation = null;
        this.isInterstitial = false;
        this.adMarvelInternalWebViewReference = new WeakReference(vw);
        this.adMarvelWebViewReference = new WeakReference(adMarvelWebView);
        contextReference = new WeakReference(context);
        this.adMarvelAd = ad;
        adMarvelActivityReference = null;
        this.isInterstitial = false;
    }

    public static void updateActivity(AdMarvelActivity adMarvelActivity) {
        adMarvelActivityReference = new WeakReference(adMarvelActivity);
        contextReference = new WeakReference(adMarvelActivity);
        lastOrientation = adMarvelActivity.getRequestedOrientation();
    }

    @JavascriptInterface
    public void admarvelCheckFrameValues(String callback) {
    }

    @JavascriptInterface
    public void allowCenteringOfExpandedAd() {
        if (!this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                adMarvelWebView.f846A = true;
            }
        }
    }

    @JavascriptInterface
    public void allowInteractionInExpandState() {
        if (!this.isInterstitial && this.adMarvelAd != null) {
            this.adMarvelAd.allowInteractionInExpandableAds();
        }
    }

    @JavascriptInterface
    public void checkForApplicationSupportedOrientations(String callback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null) {
            if (adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) {
                Context context = adMarvelInternalWebView.getContext();
                if (context != null && (context instanceof Activity)) {
                    String[] split = Utils.m180a((Activity) context).split(Stomp.COMMA);
                    HashMap hashMap = new HashMap();
                    hashMap.put(DeviceInfo.ORIENTATION_PORTRAIT, "NO");
                    hashMap.put("landscapeLeft", "NO");
                    hashMap.put("landscapeRight", "NO");
                    hashMap.put("portraitUpsideDown", "NO");
                    for (String str : split) {
                        if (str.equals("0")) {
                            hashMap.put(DeviceInfo.ORIENTATION_PORTRAIT, "YES");
                        } else if (str.equals("90")) {
                            hashMap.put("landscapeLeft", "YES");
                        } else if (str.equals("-90")) {
                            hashMap.put("landscapeRight", "YES");
                        } else if (str.equals("180")) {
                            hashMap.put("portraitUpsideDown", "YES");
                        }
                    }
                    String str2 = "\"{portrait:" + ((String) hashMap.get(DeviceInfo.ORIENTATION_PORTRAIT)) + Stomp.COMMA + "landscapeLeft:" + ((String) hashMap.get("landscapeLeft")) + Stomp.COMMA + "landscapeRight:" + ((String) hashMap.get("landscapeRight")) + Stomp.COMMA + "portraitUpsideDown:" + ((String) hashMap.get("portraitUpsideDown")) + "}\"";
                    if (adMarvelInternalWebView != null) {
                        adMarvelInternalWebView.m315e(callback + "(" + str2 + ")");
                    }
                }
            }
        }
    }

    @JavascriptInterface
    public void checkFrameValues(String callback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                AdMarvelThreadExecutorService.m597a().m598b().execute(new AdMarvelWebView(callback, adMarvelInternalWebView, adMarvelWebView));
            }
        }
    }

    @JavascriptInterface
    public void checkNetworkAvailable(String callback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            AdMarvelThreadExecutorService.m597a().m598b().execute(new C0234c(adMarvelInternalWebView, callback));
        }
    }

    @JavascriptInterface
    public void cleanup() {
        if (this.isInterstitial) {
            Logging.log("window.ADMARVEL.cleanup()");
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null && ((AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get()) != null) {
                adMarvelActivity.f93d.post(new C0140c(adMarvelActivity));
            }
        } else if (Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.cleanup()");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null && ((AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get()) != null) {
                new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView));
            }
        }
    }

    @JavascriptInterface
    public void close() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView == null) {
                return;
            }
            if (adMarvelWebView.f847B) {
                Activity activity = (Activity) (adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null);
                if (activity instanceof AdMarvelMediationActivity) {
                    new Handler(Looper.getMainLooper()).post(new C0174b((AdMarvelMediationActivity) activity));
                    return;
                }
                return;
            }
            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView));
        }
    }

    @JavascriptInterface
    public void closeinterstitialad() {
        if (this.isInterstitial) {
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) {
                    adMarvelActivity.m41g();
                }
            }
        }
    }

    @JavascriptInterface
    public void createcalendarevent(String date, String title, String description) {
        Context context = (Context) contextReference.get();
        if (context != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && Utils.m202c(context, "android.permission.READ_CALENDAR") && Utils.m202c(context, "android.permission.WRITE_CALENDAR")) {
                Context context2;
                Activity activity;
                Builder builder;
                if (!this.isInterstitial) {
                    AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
                    if (adMarvelWebView == null) {
                        return;
                    }
                    if (adMarvelWebView.f847B) {
                        context2 = adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null;
                        if (context2 instanceof Activity) {
                            activity = (Activity) context2;
                            builder = new Builder(activity);
                            builder.setMessage("Allow access to Calendar?").setCancelable(false).setPositiveButton("Yes", new C02192(this, adMarvelInternalWebView, activity, date, title, description)).setNegativeButton("No", new C02181(this));
                            builder.create().show();
                        }
                    }
                }
                context2 = context;
                if (context2 instanceof Activity) {
                    activity = (Activity) context2;
                    builder = new Builder(activity);
                    builder.setMessage("Allow access to Calendar?").setCancelable(false).setPositiveButton("Yes", new C02192(this, adMarvelInternalWebView, activity, date, title, description)).setNegativeButton("No", new C02181(this));
                    builder.create().show();
                }
            }
        }
    }

    @JavascriptInterface
    public void createcalendarevent(String date, String title, String description, String location, String allday, String endDate, int reminderoffset) {
        Context context = (Context) contextReference.get();
        if (context != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && Utils.m202c(context, "android.permission.READ_CALENDAR") && Utils.m202c(context, "android.permission.WRITE_CALENDAR")) {
                Context context2;
                Activity activity;
                Builder builder;
                if (!this.isInterstitial) {
                    AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
                    if (adMarvelWebView == null) {
                        return;
                    }
                    if (adMarvelWebView.f847B) {
                        context2 = adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null;
                        if (context2 instanceof Activity) {
                            activity = (Activity) context2;
                            builder = new Builder(activity);
                            builder.setMessage("Allow access to Calendar?").setCancelable(false).setPositiveButton("Yes", new C02214(this, adMarvelInternalWebView, activity, date, title, description, location, allday, endDate, reminderoffset)).setNegativeButton("No", new C02203(this));
                            builder.create().show();
                        }
                    }
                }
                context2 = context;
                if (context2 instanceof Activity) {
                    activity = (Activity) context2;
                    builder = new Builder(activity);
                    builder.setMessage("Allow access to Calendar?").setCancelable(false).setPositiveButton("Yes", new C02214(this, adMarvelInternalWebView, activity, date, title, description, location, allday, endDate, reminderoffset)).setNegativeButton("No", new C02203(this));
                    builder.create().show();
                }
            }
        }
    }

    @JavascriptInterface
    public void createcalendarevent(String date, String title, String description, String location, String allday, String endDate, int reminderoffset, String timezone, String url, String recurrenceRules, String exceptionDateString, int status, int availability, String callback) {
        Context context = (Context) contextReference.get();
        if (context != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                if (Utils.m202c(context, "android.permission.READ_CALENDAR") && Utils.m202c(context, "android.permission.WRITE_CALENDAR")) {
                    Context context2;
                    Activity activity;
                    Builder builder;
                    if (!this.isInterstitial) {
                        AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
                        if (adMarvelWebView == null) {
                            return;
                        }
                        if (adMarvelWebView.f847B) {
                            context2 = adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null;
                            if (context2 instanceof Activity) {
                                adMarvelInternalWebView.m315e(callback + "(\"NO\")");
                                return;
                            }
                            activity = (Activity) context2;
                            builder = new Builder(activity);
                            builder.setMessage("Allow access to Calendar?").setCancelable(false).setPositiveButton("Yes", new C02236(this, adMarvelInternalWebView, activity, date, title, description, location, allday, endDate, reminderoffset, timezone, url, recurrenceRules, exceptionDateString, status, availability, callback)).setNegativeButton("No", new C02225(this, callback, adMarvelInternalWebView));
                            builder.create().show();
                        }
                    }
                    context2 = context;
                    if (context2 instanceof Activity) {
                        adMarvelInternalWebView.m315e(callback + "(\"NO\")");
                        return;
                    }
                    activity = (Activity) context2;
                    builder = new Builder(activity);
                    builder.setMessage("Allow access to Calendar?").setCancelable(false).setPositiveButton("Yes", new C02236(this, adMarvelInternalWebView, activity, date, title, description, location, allday, endDate, reminderoffset, timezone, url, recurrenceRules, exceptionDateString, status, availability, callback)).setNegativeButton("No", new C02225(this, callback, adMarvelInternalWebView));
                    builder.create().show();
                } else if (callback != null) {
                    adMarvelInternalWebView.m315e(callback + "(false)");
                }
            }
        }
    }

    @JavascriptInterface
    public void delaydisplay() {
        if (this.adMarvelInternalWebViewReference != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
                return;
            }
        }
        if (!this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                adMarvelWebView.f885q.set(true);
            }
        }
    }

    @JavascriptInterface
    public void detectlocation(String callback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            AdMarvelLocationManager a = AdMarvelLocationManager.m566a();
            if (a != null) {
                a.m574a(adMarvelInternalWebView, callback);
            }
        }
    }

    @JavascriptInterface
    public void detectsizechange(String callback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            adMarvelInternalWebView.f606o = callback;
        }
    }

    @JavascriptInterface
    public void detectvisibility(String callback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            adMarvelInternalWebView.f601j = callback;
            if (this.isInterstitial && !adMarvelInternalWebView.f602k && adMarvelInternalWebView.f604m) {
                adMarvelInternalWebView.m315e(callback + "(" + true + ")");
                adMarvelInternalWebView.f602k = true;
            }
        }
    }

    @JavascriptInterface
    public void disableClientSideCachingOfVideo() {
        if (!this.isInterstitial && Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.disableClientSideCachingOfVideo()");
        }
    }

    @JavascriptInterface
    public void disableRotationForExpand() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                adMarvelWebView.f892x = true;
                this.lockedOrientation = null;
                if (adMarvelWebView.f893y && adMarvelWebView.f894z) {
                    disablerotations(this.lockedOrientation);
                }
            }
        }
    }

    @JavascriptInterface
    public void disableRotationForExpand(String lockOrientation) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                adMarvelWebView.f892x = true;
                this.lockedOrientation = lockOrientation;
                if (adMarvelWebView.f893y && adMarvelWebView.f894z) {
                    disablerotations(lockOrientation);
                }
            }
        }
    }

    @JavascriptInterface
    public void disableautodetect() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
            return;
        }
        if (this.isInterstitial) {
            adMarvelInternalWebView.getEnableAutoDetect().set(false);
            return;
        }
        AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
        if (adMarvelWebView != null) {
            adMarvelWebView.f887s.set(false);
        }
    }

    @JavascriptInterface
    public void disablebackbutton() {
        if (this.isInterstitial) {
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                if (adMarvelActivity != null) {
                    if (adMarvelActivity.f92c != null) {
                        adMarvelActivity.f93d.removeCallbacks(adMarvelActivity.f92c);
                    }
                    adMarvelActivity.f91b = true;
                    return;
                }
                adMarvelInternalWebView.f616y = true;
            }
        }
    }

    @JavascriptInterface
    public void disablebackbutton(int timeInMilliSec) {
        if (this.isInterstitial) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                if (adMarvelActivity == null) {
                    adMarvelInternalWebView.f616y = true;
                    adMarvelInternalWebView.f617z = timeInMilliSec;
                } else if (timeInMilliSec > 0) {
                    adMarvelActivity.f91b = true;
                    if (adMarvelActivity.f92c != null) {
                        adMarvelActivity.f93d.removeCallbacks(adMarvelActivity.f92c);
                    }
                    adMarvelActivity.f93d.postDelayed(adMarvelActivity.f92c, (long) timeInMilliSec);
                }
            }
        }
    }

    @JavascriptInterface
    public void disableclosebutton(String state) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && state != null && state.equals(Stomp.TRUE)) {
            sdkclosebutton(Stomp.FALSE, Stomp.FALSE);
        }
    }

    @JavascriptInterface
    public void disablerotations() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
            return;
        }
        if (this.isInterstitial) {
            disablerotations(null);
            return;
        }
        AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
        if (adMarvelWebView != null) {
            Context context = adMarvelWebView.getContext();
            Activity activity = (context == null || !(context instanceof Activity)) ? null : (Activity) context;
            if (activity != null) {
                int i = adMarvelWebView.getResources().getConfiguration().orientation;
                if (i == 1) {
                    activity.setRequestedOrientation(1);
                } else if (i == 2) {
                    activity.setRequestedOrientation(0);
                }
            }
        }
    }

    @JavascriptInterface
    public void disablerotations(String lockorientation) {
        Activity activity = null;
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
            return;
        }
        if (this.isInterstitial) {
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null) {
                lastOrientation = adMarvelActivity.getRequestedOrientation();
                adMarvelActivity.m35a(lockorientation);
                return;
            } else if (adMarvelInternalWebView != null) {
                adMarvelInternalWebView.f614w = true;
                adMarvelInternalWebView.f615x = lockorientation;
                return;
            } else {
                return;
            }
        }
        AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
        if (adMarvelWebView != null) {
            Context context = adMarvelWebView.f847B ? adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null : adMarvelWebView.getContext();
            if (context != null && (context instanceof Activity)) {
                activity = (Activity) context;
            }
            if (activity != null) {
                int i;
                if (Version.getAndroidSDKVersion() < 9) {
                    i = adMarvelWebView.getResources().getConfiguration().orientation;
                } else {
                    Object adMarvelWebView2 = new AdMarvelWebView(adMarvelWebView);
                    AdMarvelThreadExecutorService.m597a().m598b().execute(adMarvelWebView2);
                    i = ExploreByTouchHelper.INVALID_ID;
                    while (i == ExploreByTouchHelper.INVALID_ID) {
                        i = adMarvelWebView2.m440a();
                    }
                }
                if (lockorientation != null) {
                    if (adMarvelWebView.f892x) {
                        if (Version.getAndroidSDKVersion() >= 9) {
                            AdMarvelThreadExecutorService.m597a().m598b().execute(new C0158q(activity, lockorientation));
                        } else if (lockorientation.equalsIgnoreCase("Portrait")) {
                            activity.setRequestedOrientation(1);
                        } else if (lockorientation.equalsIgnoreCase("LandscapeLeft")) {
                            activity.setRequestedOrientation(0);
                        }
                    } else if (Version.getAndroidSDKVersion() < 9) {
                        if (lockorientation.equalsIgnoreCase("Portrait") && i == 1) {
                            activity.setRequestedOrientation(1);
                        } else if (lockorientation.equalsIgnoreCase("LandscapeLeft") && i == 2) {
                            activity.setRequestedOrientation(0);
                        }
                    } else if (lockorientation.equalsIgnoreCase("Portrait") && i == 0) {
                        activity.setRequestedOrientation(1);
                    } else if (lockorientation.equalsIgnoreCase("LandscapeLeft") && i == 1) {
                        activity.setRequestedOrientation(0);
                    }
                } else if (Version.getAndroidSDKVersion() < 9) {
                    if (i == 1) {
                        activity.setRequestedOrientation(1);
                    } else if (i == 2) {
                        activity.setRequestedOrientation(0);
                    }
                } else if (i == 0) {
                    AdMarvelThreadExecutorService.m597a().m598b().execute(new C0158q(activity, "Portrait"));
                } else if (i == 1) {
                    AdMarvelThreadExecutorService.m597a().m598b().execute(new C0158q(activity, "LandscapeLeft"));
                } else {
                    AdMarvelThreadExecutorService.m597a().m598b().execute(new C0158q(activity, "none"));
                }
            }
        }
    }

    @JavascriptInterface
    public void enableCustomExpand(boolean isCustomExpandEnable) {
        AdMarvelUtils.enableCustomExpand(isCustomExpandEnable);
    }

    @JavascriptInterface
    public void enableVideoCloseInBackground() {
        AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
        if (adMarvelActivity != null) {
            adMarvelActivity.f109t = true;
            return;
        }
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null) {
            adMarvelInternalWebView.f573C = true;
        }
    }

    @JavascriptInterface
    public void enableautodetect() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
            return;
        }
        if (this.isInterstitial) {
            adMarvelInternalWebView.getEnableAutoDetect().set(true);
            return;
        }
        AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
        if (adMarvelWebView != null) {
            adMarvelWebView.f887s.set(true);
        }
    }

    @JavascriptInterface
    public void enablebackbutton() {
        if (this.isInterstitial) {
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                    adMarvelActivity.f91b = false;
                }
            }
        }
    }

    @JavascriptInterface
    public void enablerotations() {
        Activity activity = null;
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
            return;
        }
        if (this.isInterstitial) {
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null) {
                adMarvelActivity.setRequestedOrientation(lastOrientation);
                return;
            }
            return;
        }
        AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
        if (adMarvelWebView != null) {
            Context context = adMarvelWebView.getContext();
            if (context != null && (context instanceof Activity)) {
                activity = (Activity) context;
            }
            if (activity != null) {
                activity.setRequestedOrientation(adMarvelWebView.f889u);
                adMarvelWebView.f892x = false;
            }
        }
    }

    @JavascriptInterface
    public void expandto(int width, int height) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                Context context = adMarvelInternalWebView.getContext();
                if (context != null && (context instanceof Activity)) {
                    Activity activity = (Activity) context;
                    if (adMarvelWebView.f893y) {
                        if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, width, height));
                        } else {
                            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, (Activity) (adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null), width, height));
                        }
                    } else if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                        new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, width, height, this.adMarvelAd));
                    } else {
                        new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, width, height, this.adMarvelAd));
                    }
                }
            }
        }
    }

    @JavascriptInterface
    public void expandto(int x, int y, int width, int height, String closeCallback, String url) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                Context context = adMarvelInternalWebView.getContext();
                if (context != null && (context instanceof Activity)) {
                    Activity activity = (Activity) context;
                    if (url == null || url.length() <= 0) {
                        if (closeCallback != null) {
                            adMarvelWebView.f880l = closeCallback;
                        }
                        if (adMarvelWebView.f893y) {
                            if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                                new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, (Activity) context, x, y, width, height));
                                return;
                            } else {
                                new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, (Activity) (adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null), x, y, width, height));
                                return;
                            }
                        } else if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, x, y, width, height, this.adMarvelAd));
                            return;
                        } else {
                            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, x, y, width, height, this.adMarvelAd));
                            return;
                        }
                    }
                    expandtofullscreen(closeCallback, url);
                }
            }
        }
    }

    @JavascriptInterface
    public void expandto(int width, int height, String closeCallback, String url) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                Context context = adMarvelInternalWebView.getContext();
                if (context != null && (context instanceof Activity)) {
                    Activity activity = (Activity) context;
                    if (url == null || url.length() <= 0) {
                        if (closeCallback != null) {
                            adMarvelWebView.f880l = closeCallback;
                        }
                        if (adMarvelWebView.f893y) {
                            if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                                new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, width, height));
                                return;
                            } else {
                                new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, (Activity) (adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null), width, height));
                                return;
                            }
                        } else if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, width, height, this.adMarvelAd));
                            return;
                        } else {
                            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, width, height, this.adMarvelAd));
                            return;
                        }
                    }
                    expandtofullscreen(closeCallback, url);
                }
            }
        }
    }

    @JavascriptInterface
    public void expandtofullscreen() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                Context context = adMarvelInternalWebView.getContext();
                if (context != null && (context instanceof Activity)) {
                    Activity activity = (Activity) context;
                    adMarvelWebView.f894z = true;
                    if (adMarvelWebView.f892x) {
                        if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                            disablerotations(this.lockedOrientation);
                        } else if (this.lockedOrientation == null || this.lockedOrientation.length() <= 0) {
                            adMarvelWebView.f884p = "Current";
                        } else {
                            adMarvelWebView.f884p = this.lockedOrientation;
                        }
                    }
                    if (adMarvelWebView.f893y) {
                        if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, 0, 0, -1, -1));
                        } else {
                            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, (Activity) (adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null), 0, 0, -1, -1));
                        }
                    } else if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                        new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, 0, 0, -1, -1, this.adMarvelAd));
                    } else {
                        new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, 0, 0, -1, -1, this.adMarvelAd));
                    }
                }
            }
        }
    }

    @JavascriptInterface
    public void expandtofullscreen(String closeCallback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                Context context = adMarvelInternalWebView.getContext();
                if (context != null && (context instanceof Activity)) {
                    Activity activity = (Activity) context;
                    adMarvelWebView.f894z = true;
                    if (closeCallback != null) {
                        adMarvelWebView.f880l = closeCallback;
                    }
                    if (adMarvelWebView.f892x) {
                        if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                            disablerotations(this.lockedOrientation);
                        } else if (this.lockedOrientation == null || this.lockedOrientation.length() <= 0) {
                            adMarvelWebView.f884p = "Current";
                        } else {
                            adMarvelWebView.f884p = this.lockedOrientation;
                        }
                    }
                    if (adMarvelWebView.f893y) {
                        if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, 0, 0, -1, -1));
                        } else {
                            new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, (Activity) (adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null), 0, 0, -1, -1));
                        }
                    } else if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                        new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, 0, 0, -1, -1, this.adMarvelAd));
                    } else {
                        new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, 0, 0, -1, -1, this.adMarvelAd));
                    }
                }
            }
        }
    }

    @JavascriptInterface
    public void expandtofullscreen(String closeCallback, String url) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                Activity activity;
                Context context = adMarvelInternalWebView.getContext();
                if (context != null && (context instanceof Activity)) {
                    activity = (Activity) context;
                } else if (url != null && url.length() != 0) {
                    activity = null;
                } else {
                    return;
                }
                if (closeCallback != null) {
                    adMarvelWebView.f880l = closeCallback;
                }
                adMarvelWebView.f894z = true;
                if (url != null && url.length() > 0) {
                    adMarvelWebView.f881m = url;
                    adMarvelWebView.f882n = true;
                }
                if (adMarvelWebView.f892x) {
                    if (adMarvelWebView.f882n) {
                        if (this.lockedOrientation == null || this.lockedOrientation.length() <= 0) {
                            adMarvelWebView.f883o = "Current";
                        } else {
                            adMarvelWebView.f883o = this.lockedOrientation;
                        }
                    } else if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                        disablerotations(this.lockedOrientation);
                    } else if (this.lockedOrientation == null || this.lockedOrientation.length() <= 0) {
                        adMarvelWebView.f884p = "Current";
                    } else {
                        adMarvelWebView.f884p = this.lockedOrientation;
                    }
                }
                if (url != null && url.length() > 0) {
                    new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, url, this.adMarvelAd));
                } else if (adMarvelWebView.f893y) {
                    if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                        new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, 0, 0, -1, -1));
                    } else {
                        new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, (Activity) (adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null), 0, 0, -1, -1));
                    }
                } else if (!AdMarvelUtils.isCustomExpandEnable || this.adMarvelAd.isHoverAd() || this.adMarvelAd.isAppInteractionAllowedForExpandableAds()) {
                    new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, 0, 0, -1, -1, this.adMarvelAd));
                } else {
                    new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView, activity, 0, 0, -1, -1, this.adMarvelAd));
                }
            }
        }
    }

    @JavascriptInterface
    public void fetchWebViewHtmlContent(String html) {
        if (this.adMarvelAd != null) {
            this.adMarvelAd.setHtmlJson(html);
        }
    }

    @JavascriptInterface
    public void finishVideo() {
        if (!this.isInterstitial && Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.finishVideo()");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null && ((AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get()) != null) {
                new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView));
            }
        }
    }

    @JavascriptInterface
    public void firePixel(String pixelUrl) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            if (this.isInterstitial) {
                AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
                if (adMarvelActivity != null) {
                    adMarvelActivity.f93d.post(new C0145h(adMarvelInternalWebView, adMarvelActivity, pixelUrl));
                    return;
                }
                return;
            }
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                AdMarvelThreadExecutorService.m597a().m598b().execute(new AdMarvelWebView(adMarvelInternalWebView, adMarvelWebView, pixelUrl));
            }
        }
    }

    @JavascriptInterface
    public int getAndroidOSVersionAPI() {
        return Version.getAndroidSDKVersion();
    }

    @JavascriptInterface
    public void getLocation(String callback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            AdMarvelThreadExecutorService.m597a().m598b().execute(new C0249r(adMarvelInternalWebView, callback));
        }
    }

    @JavascriptInterface
    public void initAdMarvel(String callback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView == null) {
            return;
        }
        if (!this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                AdMarvelThreadExecutorService.m597a().m598b().execute(new AdMarvelWebView(callback, adMarvelInternalWebView, adMarvelWebView));
            }
        } else if (adMarvelActivityReference == null || adMarvelActivityReference.get() == null) {
            Context context = (Context) contextReference.get();
            if (context != null) {
                AdMarvelThreadExecutorService.m597a().m598b().execute(new C0148k(callback, adMarvelInternalWebView, context));
            }
        } else {
            AdMarvelActivity adMarvelActivity = (AdMarvelActivity) adMarvelActivityReference.get();
            if (adMarvelActivity != null) {
                adMarvelActivity.f93d.post(new C0148k(callback, adMarvelInternalWebView, adMarvelActivity));
            }
        }
    }

    public void initVideo(String videoUrl) {
        if (!this.isInterstitial && Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.setVideoUrl(\"" + videoUrl + "\")");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                adMarvelWebView.f848C = videoUrl;
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && adMarvelWebView.f848C != null && adMarvelWebView.f848C.length() > 0) {
                    new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(videoUrl, adMarvelWebView, adMarvelInternalWebView));
                }
            }
        }
    }

    @JavascriptInterface
    public int isInterstitial() {
        return this.isInterstitial ? 1 : 0;
    }

    @JavascriptInterface
    public int isinitialload() {
        return 1;
    }

    @JavascriptInterface
    public int isinstalled(String packageName) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView == null) {
            return 0;
        }
        if (adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) {
            return Utils.m192a(adMarvelInternalWebView.getContext(), packageName) ? 1 : 0;
        } else {
            return 0;
        }
    }

    @JavascriptInterface
    public int isvideocached() {
        if (!this.isInterstitial && Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.isvideocached()");
            if (((AdMarvelWebView) this.adMarvelWebViewReference.get()) != null && Version.getAndroidSDKVersion() >= 14) {
            }
        }
        return 0;
    }

    @JavascriptInterface
    public void loadVideo() {
        if (this.isInterstitial) {
            Logging.log("window.ADMARVEL.loadVideo()");
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView == null) {
                return;
            }
            if (adMarvelActivity != null) {
                adMarvelActivity.f99j = true;
                if (adMarvelInternalWebView.f574D != null && adMarvelInternalWebView.f574D.length() > 0) {
                    adMarvelActivity.f93d.post(new C0152l(adMarvelInternalWebView.f574D, adMarvelActivity, adMarvelInternalWebView));
                    return;
                }
                return;
            }
            adMarvelInternalWebView.f582L = true;
        } else if (Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.loadVideo()");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                AdMarvelInternalWebView adMarvelInternalWebView2 = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView2 != null && adMarvelWebView.f848C != null && adMarvelWebView.f848C.length() > 0) {
                    new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(adMarvelWebView.f848C, adMarvelWebView, adMarvelInternalWebView2));
                }
            }
        }
    }

    @JavascriptInterface
    public void notifyEvent(String eventName) {
        Map map;
        Logging.log("window.ADMARVEL.notifyEvent -" + eventName);
        AdMarvelVideoEvents adMarvelVideoEvents = AdMarvelVideoEvents.getEnum(eventName);
        if (adMarvelVideoEvents == AdMarvelVideoEvents.CUSTOM) {
            Map hashMap = new HashMap();
            hashMap.put("eventName", eventName);
            map = hashMap;
        } else {
            map = null;
        }
        AdMarvelInternalWebView adMarvelInternalWebView;
        if (this.isInterstitial) {
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            adMarvelInternalWebView = this.adMarvelInternalWebViewReference != null ? (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get() : null;
            if (adMarvelActivity != null && adMarvelInternalWebView != null && adMarvelInternalWebView.getAdMarvelInterstitialAdsInstance() != null) {
                if (adMarvelActivity.f96g != null && adMarvelActivity.f96g.isRewardInterstitial() && adMarvelVideoEvents == AdMarvelVideoEvents.COMPLETE) {
                    AdMarvelThreadExecutorService.m597a().m598b().execute(new C02269(this, eventName, adMarvelActivity, adMarvelInternalWebView));
                    return;
                } else if (adMarvelInternalWebView != null && adMarvelInternalWebView.getAdMarvelInterstitialAdsInstance() != null && adMarvelInternalWebView.getAdMarvelInterstitialAdsInstance().getVideoEventListener() != null) {
                    adMarvelInternalWebView.getAdMarvelInterstitialAdsInstance().getVideoEventListener().onAdMarvelVideoEvent(adMarvelVideoEvents, map);
                    return;
                } else {
                    return;
                }
            }
            return;
        }
        adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.f595d != null && adMarvelInternalWebView.f595d.get() != null) {
            AdMarvelView adMarvelView = (AdMarvelView) adMarvelInternalWebView.f595d.get();
            if (adMarvelView.getVideoEventListener() != null) {
                adMarvelView.getVideoEventListener().onAdMarvelVideoEvent(adMarvelVideoEvents, map);
            }
        }
    }

    @JavascriptInterface
    public void notifyInAppBrowserCloseAction(String callback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            adMarvelInternalWebView.f585O = callback;
        }
    }

    @JavascriptInterface
    public void notifyInterstitialBackgroundState(String callback) {
        if (callback != null && callback.length() > 0) {
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null) {
                adMarvelActivity.f108s = callback;
                return;
            }
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                adMarvelInternalWebView.f572B = callback;
            }
        }
    }

    @JavascriptInterface
    public void onAudioStart() {
        AdMarvelInternalWebView adMarvelInternalWebView;
        if (this.isInterstitial) {
            adMarvelInternalWebView = this.adMarvelInternalWebViewReference != null ? (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get() : null;
            if (adMarvelInternalWebView != null && adMarvelInternalWebView.getAdMarvelInterstitialAdsInstance() != null && adMarvelInternalWebView.getAdMarvelInterstitialAdsInstance().getVideoEventListener() != null) {
                adMarvelInternalWebView.getAdMarvelInterstitialAdsInstance().getVideoEventListener().onAudioStart();
                return;
            }
            return;
        }
        adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.f595d != null && adMarvelInternalWebView.f595d.get() != null) {
            AdMarvelView adMarvelView = (AdMarvelView) adMarvelInternalWebView.f595d.get();
            if (adMarvelView.getVideoEventListener() != null) {
                adMarvelView.getVideoEventListener().onAudioStart();
            }
        }
    }

    @JavascriptInterface
    public void onAudioStop() {
        AdMarvelInternalWebView adMarvelInternalWebView;
        if (this.isInterstitial) {
            adMarvelInternalWebView = this.adMarvelInternalWebViewReference != null ? (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get() : null;
            if (adMarvelInternalWebView != null && adMarvelInternalWebView.getAdMarvelInterstitialAdsInstance() != null && adMarvelInternalWebView.getAdMarvelInterstitialAdsInstance().getVideoEventListener() != null) {
                adMarvelInternalWebView.getAdMarvelInterstitialAdsInstance().getVideoEventListener().onAudioStop();
                return;
            }
            return;
        }
        adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.f595d != null && adMarvelInternalWebView.f595d.get() != null) {
            AdMarvelView adMarvelView = (AdMarvelView) adMarvelInternalWebView.f595d.get();
            if (adMarvelView.getVideoEventListener() != null) {
                adMarvelView.getVideoEventListener().onAudioStop();
            }
        }
    }

    @JavascriptInterface
    public void pauseVideo() {
        if (this.isInterstitial) {
            Logging.log("window.ADMARVEL.pauseVideo()");
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView != null) {
                    adMarvelActivity.f93d.post(new C0153m(adMarvelActivity, adMarvelInternalWebView));
                }
            }
        } else if (Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.pauseVideo()");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                AdMarvelInternalWebView adMarvelInternalWebView2 = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView2 != null) {
                    new Handler(Looper.getMainLooper()).post(new ab(adMarvelWebView, adMarvelInternalWebView2));
                }
            }
        }
    }

    @JavascriptInterface
    public void playVideo() {
        if (this.isInterstitial) {
            Logging.log("window.ADMARVEL.playVideo()");
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView != null && adMarvelInternalWebView.f574D != null && adMarvelInternalWebView.f574D.length() > 0) {
                    adMarvelActivity.f93d.post(new C0155n(adMarvelActivity, adMarvelInternalWebView));
                }
            }
        } else if (Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.playVideo()");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                AdMarvelInternalWebView adMarvelInternalWebView2 = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView2 != null && adMarvelWebView.f848C != null && adMarvelWebView.f848C.length() > 0) {
                    new Handler(Looper.getMainLooper()).post(new ac(adMarvelWebView, adMarvelInternalWebView2));
                }
            }
        }
    }

    @JavascriptInterface
    public void readyfordisplay() {
        if (this.adMarvelInternalWebViewReference != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
                return;
            }
        }
        if (!this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView == null) {
                return;
            }
            if (!adMarvelWebView.f886r.get()) {
                adMarvelWebView.f885q.set(false);
            } else if (adMarvelWebView.f870b.compareAndSet(true, false) && AdMarvelWebView.m451a(adMarvelWebView.f888t) != null) {
                AdMarvelWebView.m451a(adMarvelWebView.f888t).m153a(adMarvelWebView, this.adMarvelAd);
            }
        }
    }

    @JavascriptInterface
    public void redirect(String url) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
            return;
        }
        if (this.isInterstitial) {
            Context context = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (context != null && url != null && url.length() > 0) {
                context.f93d.post(new AdMarvelRedirectRunnable(url, context, this.adMarvelAd, "interstitial", context.f94e, AdMarvelInterstitialAds.getEnableClickRedirect(), AdMarvelInterstitialAds.enableOfflineSDK, false, context.f95f));
                return;
            }
            return;
        }
        AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
        if (adMarvelWebView != null && adMarvelInternalWebView != null) {
            if (!adMarvelInternalWebView.m313c()) {
                if (url != null && url.length() > 0) {
                    if (!(url.startsWith("admarvelsdk") || url.startsWith("admarvelinternal"))) {
                        return;
                    }
                }
                return;
            }
            adMarvelWebView.m483e(url);
        }
    }

    public void registerBroadcastReceiver(Context context, AdMarvelConnectivityChangeReceiver adMarvelReceiver) {
        context.getApplicationContext().registerReceiver(adMarvelReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @JavascriptInterface
    public void registerEventsForAdMarvelVideo(String event, String callback) {
        if (this.isInterstitial) {
            if (event != null && event.length() != 0 && callback != null && callback.length() != 0) {
                AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
                if (adMarvelActivity == null) {
                    AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                    if (adMarvelInternalWebView == null) {
                        return;
                    }
                    if (event.equals("loadeddata")) {
                        adMarvelInternalWebView.f575E = callback;
                    } else if (event.equals("play")) {
                        adMarvelInternalWebView.f576F = callback;
                    } else if (event.equals("canplay")) {
                        adMarvelInternalWebView.f577G = callback;
                    } else if (event.equals("timeupdate")) {
                        adMarvelInternalWebView.f578H = callback;
                    } else if (event.equals("ended")) {
                        adMarvelInternalWebView.f579I = callback;
                    } else if (event.equals("pause")) {
                        adMarvelInternalWebView.f580J = callback;
                    } else if (event.equals(Diagnostics.error)) {
                        adMarvelInternalWebView.f581K = callback;
                    }
                } else if (event.equals("loadeddata")) {
                    adMarvelActivity.f100k = callback;
                } else if (event.equals("play")) {
                    adMarvelActivity.f101l = callback;
                } else if (event.equals("canplay")) {
                    adMarvelActivity.f102m = callback;
                } else if (event.equals("timeupdate")) {
                    adMarvelActivity.f103n = callback;
                } else if (event.equals("ended")) {
                    adMarvelActivity.f104o = callback;
                } else if (event.equals("pause")) {
                    adMarvelActivity.f105p = callback;
                } else if (event.equals(Diagnostics.error)) {
                    adMarvelActivity.f106q = callback;
                }
            }
        } else if (Version.getAndroidSDKVersion() >= 14 && event != null && event.length() != 0 && callback != null && callback.length() != 0) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView == null) {
                return;
            }
            if (event.equals("loadeddata")) {
                adMarvelWebView.f854I = callback;
            } else if (event.equals("play")) {
                adMarvelWebView.f855J = callback;
            } else if (event.equals("canplay")) {
                adMarvelWebView.f856K = callback;
            } else if (event.equals("timeupdate")) {
                adMarvelWebView.f857L = callback;
            } else if (event.equals("ended")) {
                adMarvelWebView.f858M = callback;
            } else if (event.equals("pause")) {
                adMarvelWebView.f859N = callback;
            } else if (event.equals("resume")) {
                adMarvelWebView.f860O = callback;
            } else if (event.equals("stop")) {
                adMarvelWebView.f861P = callback;
            } else if (event.equals(Diagnostics.error)) {
                adMarvelWebView.f862Q = callback;
            }
        }
    }

    @JavascriptInterface
    public void registerInterstitialCloseCallback(String callback) {
        if (callback != null && callback.length() > 0) {
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null) {
                adMarvelActivity.f107r = callback;
                return;
            }
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                adMarvelInternalWebView.f571A = callback;
            }
        }
    }

    @JavascriptInterface
    public void registeraccelerationevent(String callback) {
        Context context = (Context) contextReference.get();
        if (context != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                AdMarvelSensorManager a = AdMarvelSensorManager.m579a();
                if (a != null && a.m591a(context)) {
                    a.m592b(callback);
                    a.m588a(context, adMarvelInternalWebView);
                }
            }
        }
    }

    @JavascriptInterface
    public void registerheadingevent(String callback) {
        Context context = (Context) contextReference.get();
        if (context != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                AdMarvelSensorManager a = AdMarvelSensorManager.m579a();
                if (a != null && a.m591a(context)) {
                    a.m596c(callback);
                    a.m588a(context, adMarvelInternalWebView);
                }
            }
        }
    }

    @JavascriptInterface
    public void registernetworkchangeevent(String callback) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            if (contextReference.get() != null) {
                registerBroadcastReceiver((Context) contextReference.get(), AdMarvelConnectivityChangeReceiver.m559a());
            }
            AdMarvelConnectivityChangeReceiver.m560a(adMarvelInternalWebView, callback);
        }
    }

    @JavascriptInterface
    public void registershakeevent(String callback, String threshold, String interval) {
        Context context = (Context) contextReference.get();
        if (context != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                AdMarvelSensorManager a = AdMarvelSensorManager.m579a();
                if (a != null && a.m591a(context)) {
                    a.m589a(callback);
                    a.m590a(threshold, interval);
                    a.m588a(context, adMarvelInternalWebView);
                }
            }
        }
    }

    @JavascriptInterface
    public void reportAdMarvelAdHistory() {
        Context context = (Context) contextReference.get();
        if (context != null) {
            AdMarvelUtils.reportAdMarvelAdHistory(context);
        }
    }

    @JavascriptInterface
    public void reportAdMarvelAdHistory(int count) {
        Context context = (Context) contextReference.get();
        if (context != null) {
            AdMarvelUtils.reportAdMarvelAdHistory(count, context);
        }
    }

    @JavascriptInterface
    public void resumeVideo() {
        if (!this.isInterstitial && Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.resumeVideo()");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView != null) {
                    new Handler(Looper.getMainLooper()).post(new ae(adMarvelWebView, adMarvelInternalWebView));
                }
            }
        }
    }

    @JavascriptInterface
    public void sdkclosebutton(String state, String showCloseArea) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
            return;
        }
        if (!this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                adMarvelWebView.f875g = false;
                adMarvelWebView.f876h = false;
                adMarvelWebView.f877i = false;
                if (state != null && state.equals(Stomp.TRUE)) {
                    adMarvelWebView.f875g = true;
                    adMarvelWebView.f877i = true;
                } else if (state != null && state.equals(Stomp.FALSE) && showCloseArea != null && showCloseArea.equals(Stomp.TRUE)) {
                    adMarvelWebView.f875g = true;
                    adMarvelWebView.f876h = true;
                    adMarvelWebView.f877i = false;
                }
            }
        } else if (state == null || !state.equals(Stomp.FALSE)) {
            if (state != null && state.equals(Stomp.TRUE)) {
                adMarvelInternalWebView.m323m();
            }
        } else if (showCloseArea == null || !showCloseArea.equals(Stomp.TRUE)) {
            if (adMarvelInternalWebView != null) {
                adMarvelInternalWebView.m311a(false);
            }
        } else if (adMarvelInternalWebView != null) {
            adMarvelInternalWebView.m311a(true);
        }
    }

    @JavascriptInterface
    public void sdkclosebutton(String state, String showCloseArea, String closePosition) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                adMarvelWebView.f875g = false;
                adMarvelWebView.f876h = false;
                adMarvelWebView.f877i = false;
                if (state != null && state.equals(Stomp.TRUE)) {
                    adMarvelWebView.f875g = true;
                    adMarvelWebView.f877i = true;
                } else if (state != null && state.equals(Stomp.FALSE) && showCloseArea != null && showCloseArea.equals(Stomp.TRUE)) {
                    adMarvelWebView.f875g = true;
                    adMarvelWebView.f876h = true;
                    adMarvelWebView.f877i = false;
                }
                if (closePosition != null && closePosition.length() > 0) {
                    adMarvelWebView.f878j = closePosition;
                }
            }
        }
    }

    @JavascriptInterface
    public void seekVideoTo(float currentTime) {
        if (this.isInterstitial) {
            Logging.log("window.ADMARVEL.seekToVideo()");
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView != null && adMarvelInternalWebView.f574D != null && adMarvelInternalWebView.f574D.length() > 0) {
                    adMarvelActivity.f93d.post(new C0157p(adMarvelActivity, adMarvelInternalWebView, currentTime));
                }
            }
        } else if (Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.seekToVideo()");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                AdMarvelInternalWebView adMarvelInternalWebView2 = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView2 != null && adMarvelWebView.f848C != null && adMarvelWebView.f848C.length() > 0) {
                    new Handler(Looper.getMainLooper()).post(new af(adMarvelWebView, adMarvelInternalWebView2, currentTime));
                }
            }
        }
    }

    @JavascriptInterface
    public void setAsHoverAd() {
        if (!this.isInterstitial && this.adMarvelAd != null) {
            this.adMarvelAd.setAsHoverAd();
        }
    }

    @JavascriptInterface
    public void setInitialAudioState(String audioState) {
        if (this.isInterstitial) {
            Logging.log("window.ADMARVEL.setInitialAudioState - " + audioState);
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity == null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && audioState != null && audioState.trim().length() > 0) {
                    if (audioState.equalsIgnoreCase("mute")) {
                        adMarvelInternalWebView.f584N = true;
                    } else if (audioState.equalsIgnoreCase("unmute")) {
                        adMarvelInternalWebView.f584N = false;
                    }
                }
            } else if (audioState != null && audioState.trim().length() > 0) {
                if (audioState.equalsIgnoreCase("mute")) {
                    adMarvelActivity.f110u = true;
                } else if (audioState.equalsIgnoreCase("unmute")) {
                    adMarvelActivity.f110u = false;
                }
            }
        } else if (Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.setInitialAudioState()");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null && audioState != null && audioState.trim().length() > 0) {
                if (audioState.equalsIgnoreCase("mute")) {
                    adMarvelWebView.f864S = true;
                } else if (audioState.equalsIgnoreCase("unmute")) {
                    adMarvelWebView.f864S = false;
                }
            }
        }
    }

    @JavascriptInterface
    public void setVideoBackgroundcolor(String backgroundColor) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            int i;
            if ("0".equals(backgroundColor)) {
                i = 0;
            } else {
                long parseLong = Long.parseLong(backgroundColor.replace("#", Stomp.EMPTY), 16);
                if (backgroundColor.length() == 7 || backgroundColor.length() == 6) {
                    parseLong |= -16777216;
                }
                i = (int) parseLong;
            }
            if (!this.isInterstitial) {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
                if (adMarvelWebView != null) {
                    adMarvelWebView.f871c = i;
                    new Handler(Looper.getMainLooper()).post(new ag(adMarvelWebView));
                }
            }
        }
    }

    @JavascriptInterface
    public void setVideoContainerHeight(int height) {
        if (!this.isInterstitial && Version.getAndroidSDKVersion() >= 14) {
            Logging.log("ADMARVEL.setVideoContainerHeight " + height);
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                adMarvelWebView.f853H = height;
            }
        }
    }

    @JavascriptInterface
    public void setVideoDimensions(int x, int y, int width, int height) {
        if (!this.isInterstitial) {
            Logging.log("ADMARVEL.setVideoDimensions " + x + " " + y + " " + width + " " + height);
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null && Version.getAndroidSDKVersion() >= 14) {
                adMarvelWebView.f849D = width;
                adMarvelWebView.f850E = height;
                adMarvelWebView.f851F = x;
                adMarvelWebView.f852G = y;
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                    new Handler(Looper.getMainLooper()).post(new ad(adMarvelWebView, adMarvelInternalWebView, x, y, width, height));
                }
            }
        }
    }

    @JavascriptInterface
    public void setVideoUrl(String videoUrl) {
        if (this.isInterstitial) {
            Logging.log("window.ADMARVEL.setVideoUrl(\"" + videoUrl + "\")");
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null) {
                adMarvelInternalWebView.f574D = videoUrl;
                if (adMarvelActivity != null) {
                    adMarvelActivity.f99j = true;
                    return;
                }
                return;
            }
            return;
        }
        initVideo(videoUrl);
    }

    @JavascriptInterface
    public void setbackgroundcolor(String backgroundColor) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            int i;
            if ("0".equals(backgroundColor)) {
                i = 0;
            } else {
                long parseLong = Long.parseLong(backgroundColor.replace("#", Stomp.EMPTY), 16);
                if (backgroundColor.length() == 7 || backgroundColor.length() == 6) {
                    parseLong |= -16777216;
                }
                i = (int) parseLong;
            }
            adMarvelInternalWebView.setBackgroundColor(i);
            if (!this.isInterstitial) {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
                if (adMarvelWebView != null) {
                    adMarvelWebView.f871c = i;
                    new Handler(Looper.getMainLooper()).post(new ag(adMarvelWebView));
                }
            }
        }
    }

    @JavascriptInterface
    public void setsoftwarelayer() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && Version.getAndroidSDKVersion() >= 11 && !this.isInterstitial && ((AdMarvelWebView) this.adMarvelWebViewReference.get()) != null) {
            new Handler(Looper.getMainLooper()).post(new al(adMarvelInternalWebView));
        }
    }

    @JavascriptInterface
    public void stopVideo() {
        if (this.isInterstitial) {
            Logging.log("window.ADMARVEL.stopVideo()");
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView != null) {
                    adMarvelActivity.f93d.post(new C0159r(adMarvelActivity, adMarvelInternalWebView));
                }
            }
        } else if (Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.stopVideo()");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                AdMarvelInternalWebView adMarvelInternalWebView2 = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
                if (adMarvelInternalWebView2 != null) {
                    new Handler(Looper.getMainLooper()).post(new ah(adMarvelWebView, adMarvelInternalWebView2));
                }
            }
        }
    }

    @JavascriptInterface
    public void storepicture(String Url, String callback) {
        Context context = (Context) contextReference.get();
        if (context != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                String a = Utils.m183a(Url, context);
                if (Utils.m202c(adMarvelInternalWebView.getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") && "mounted".equals(Environment.getExternalStorageState())) {
                    if (!this.isInterstitial) {
                        AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
                        if (adMarvelWebView == null) {
                            return;
                        }
                        if (adMarvelWebView.f847B) {
                            context = adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null;
                        }
                    }
                    if (context instanceof Activity) {
                        Builder builder = new Builder((Activity) context);
                        builder.setMessage("Allow storing image in your Gallery?").setCancelable(false).setPositiveButton("Yes", new C02258(this, adMarvelInternalWebView, a, callback)).setNegativeButton("No", new C02247(this, callback, adMarvelInternalWebView));
                        builder.create().show();
                        return;
                    }
                    adMarvelInternalWebView.m315e(callback + "(\"NO\")");
                } else if (callback != null) {
                    adMarvelInternalWebView.m315e(callback + "(false)");
                }
            }
        }
    }

    @JavascriptInterface
    public void triggerVibration(String timeInMillisec) {
        int parseInt;
        int i = AMQConnection.HANDSHAKE_TIMEOUT;
        if (timeInMillisec != null && timeInMillisec.trim().length() > 0) {
            try {
                parseInt = Integer.parseInt(timeInMillisec);
                if (parseInt > AMQConnection.HANDSHAKE_TIMEOUT) {
                    Logging.log("Time mentioned is greater than Max duration ");
                } else {
                    i = parseInt;
                }
                parseInt = i;
            } catch (NumberFormatException e) {
                Logging.log("NumberFormatException so setting vibrate time to 500 Milli Sec");
            }
            Utils.m190a((Context) contextReference.get(), parseInt);
        }
        parseInt = ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH;
        Utils.m190a((Context) contextReference.get(), parseInt);
    }

    @JavascriptInterface
    public void updateAudioState(String audioState) {
        if (this.isInterstitial) {
            Logging.log("window.ADMARVEL.updateAudioState - " + audioState);
            AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
            if (adMarvelActivity != null && ((AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get()) != null) {
                adMarvelActivity.f93d.post(new C0161t(adMarvelActivity, audioState));
            }
        } else if (Version.getAndroidSDKVersion() >= 14) {
            Logging.log("window.ADMARVEL.updateAudioState()");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                new Handler(Looper.getMainLooper()).post(new aj(adMarvelWebView, audioState));
            }
        }
    }

    @JavascriptInterface
    public void updatestate(String state) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if ((adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) && !this.isInterstitial) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.adMarvelWebViewReference.get();
            if (adMarvelWebView != null) {
                new Handler(Looper.getMainLooper()).post(new ak(state, adMarvelWebView));
            }
        }
    }
}
