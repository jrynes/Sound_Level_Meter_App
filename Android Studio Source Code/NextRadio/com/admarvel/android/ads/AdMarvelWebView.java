package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.admarvel.android.ads.AdMarvelAd.AdType;
import com.admarvel.android.ads.AdMarvelInternalWebView.AdMarvelInternalWebView;
import com.admarvel.android.ads.AdMarvelMediationActivity.C0175c;
import com.admarvel.android.ads.AdMarvelUniversalVideoView.AdMarvelUniversalVideoView;
import com.admarvel.android.ads.Utils.C0250s;
import com.admarvel.android.util.AdHistoryDumpUtils;
import com.admarvel.android.util.AdMarvelBitmapDrawableUtils;
import com.admarvel.android.util.AdMarvelThreadExecutorService;
import com.admarvel.android.util.Logging;
import com.admarvel.android.util.p000a.OfflineReflectionUtils;
import com.facebook.ads.AdError;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQConnection;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;

@SuppressLint({"ViewConstructor", "SimpleDateFormat", "DefaultLocale"})
/* renamed from: com.admarvel.android.ads.p */
public class AdMarvelWebView extends RelativeLayout {
    private static String ac;
    private static String ad;
    private static String ae;
    private static String af;
    private static String ag;
    private static String ah;
    private static String ai;
    private static final Map<String, AdMarvelWebViewListener> al;
    private static final Map<String, AdMarvelInternalWebView> am;
    boolean f846A;
    boolean f847B;
    String f848C;
    int f849D;
    int f850E;
    int f851F;
    int f852G;
    int f853H;
    String f854I;
    String f855J;
    String f856K;
    String f857L;
    String f858M;
    String f859N;
    String f860O;
    String f861P;
    String f862Q;
    ai f863R;
    boolean f864S;
    boolean f865T;
    final boolean f866U;
    final boolean f867V;
    AdMarvelWebView f868W;
    public final AtomicBoolean f869a;
    boolean aa;
    boolean ab;
    private String aj;
    private String ak;
    private boolean an;
    private boolean ao;
    private final String ap;
    private WeakReference<AdMarvelWebView> aq;
    private AdMarvelWebView ar;
    private AdMarvelInternalWebView as;
    private WeakReference<AdMarvelInternalWebView> at;
    private AdMarvelAd au;
    private boolean av;
    private boolean aw;
    final AtomicBoolean f870b;
    int f871c;
    final boolean f872d;
    final boolean f873e;
    boolean f874f;
    boolean f875g;
    boolean f876h;
    boolean f877i;
    String f878j;
    aa f879k;
    String f880l;
    String f881m;
    boolean f882n;
    String f883o;
    String f884p;
    final AtomicBoolean f885q;
    final AtomicBoolean f886r;
    final AtomicBoolean f887s;
    final String f888t;
    int f889u;
    int f890v;
    public int f891w;
    boolean f892x;
    boolean f893y;
    boolean f894z;

    /* renamed from: com.admarvel.android.ads.p.a */
    interface AdMarvelWebView {
        void m343a(boolean z);
    }

    /* renamed from: com.admarvel.android.ads.p.1 */
    class AdMarvelWebView implements Runnable {
        final /* synthetic */ AdMarvelView f728a;
        final /* synthetic */ int f729b;
        final /* synthetic */ AdMarvelWebView f730c;

        AdMarvelWebView(AdMarvelWebView adMarvelWebView, AdMarvelView adMarvelView, int i) {
            this.f730c = adMarvelWebView;
            this.f728a = adMarvelView;
            this.f729b = i;
        }

        public void run() {
            this.f730c.as = new AdMarvelInternalWebView(this.f730c.getContext(), this.f730c.ap, this.f730c.f888t, this.f728a, null, this.f730c.au, AdMarvelInternalWebView.BANNER, null);
            this.f730c.as.setTag(this.f730c.f888t + "INTERNAL");
            this.f730c.as.setFocusable(true);
            this.f730c.as.setClickable(true);
            this.f730c.as.setBackgroundColor(this.f729b);
            this.f730c.as.setScrollContainer(false);
            this.f730c.as.setVerticalScrollBarEnabled(false);
            this.f730c.as.setHorizontalScrollBarEnabled(false);
            this.f730c.as.m320j();
            if (Version.getAndroidSDKVersion() >= 11 && Version.getAndroidSDKVersion() < 21) {
                this.f730c.as.setWebViewClient((WebViewClient) new WeakReference(new AdMarvelWebView(this.f730c)).get());
            } else if (Version.getAndroidSDKVersion() < 11) {
                this.f730c.as.setWebViewClient((WebViewClient) new WeakReference(new AdMarvelWebView(this.f730c)).get());
            } else {
                this.f730c.as.setWebViewClient((WebViewClient) new WeakReference(new AdMarvelWebView(this.f730c)).get());
            }
            this.f730c.at = new WeakReference(this.f730c.as);
            this.f730c.addView((View) this.f730c.at.get());
            this.f730c.as.addJavascriptInterface(new WeakReference(new AdMarvelWebViewJSInterface((AdMarvelInternalWebView) this.f730c.at.get(), this.f730c.au, this.f730c, this.f730c.getContext())).get(), "ADMARVEL");
            if (AdMarvelView.enableOfflineSDK) {
                this.f730c.as.loadDataWithBaseURL(this.f730c.au.getOfflineBaseUrl() + ReadOnlyContext.SEPARATOR, this.f730c.aj, WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
            } else if (Version.getAndroidSDKVersion() < 11) {
                this.f730c.as.loadDataWithBaseURL("content://" + this.f730c.getContext().getPackageName() + ".AdMarvelLocalFileContentProvider", this.f730c.aj, WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
            } else {
                this.f730c.as.loadDataWithBaseURL("http://baseurl.admarvel.com", this.f730c.aj, WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.2 */
    class AdMarvelWebView implements OnKeyListener {
        final /* synthetic */ AdMarvelWebView f731a;

        AdMarvelWebView(AdMarvelWebView adMarvelWebView) {
            this.f731a = adMarvelWebView;
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() != 0 || keyCode != 4) {
                return false;
            }
            this.f731a.m482e();
            return true;
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.aa */
    static class aa extends OrientationEventListener {
        private final WeakReference<AdMarvelWebView> f735a;
        private final WeakReference<Activity> f736b;
        private int f737c;

        /* renamed from: com.admarvel.android.ads.p.aa.1 */
        class AdMarvelWebView implements Runnable {
            final /* synthetic */ AdMarvelWebView f732a;
            final /* synthetic */ int f733b;
            final /* synthetic */ aa f734c;

            AdMarvelWebView(aa aaVar, AdMarvelWebView adMarvelWebView, int i) {
                this.f734c = aaVar;
                this.f732a = adMarvelWebView;
                this.f733b = i;
            }

            public void run() {
                Activity activity = (Activity) this.f734c.f736b.get();
                if (activity != null && this.f732a.f893y) {
                    ViewGroup viewGroup = (ViewGroup) activity.getWindow().findViewById(16908290);
                    RelativeLayout relativeLayout = (RelativeLayout) viewGroup.findViewWithTag(this.f732a.f888t + "EXPAND_LAYOUT");
                    if (relativeLayout != null) {
                        LayoutParams layoutParams = (LayoutParams) relativeLayout.getLayoutParams();
                        LinearLayout linearLayout = (LinearLayout) viewGroup.findViewWithTag(this.f732a.f888t + "BTN_CLOSE");
                        if (linearLayout != null) {
                            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
                            int height = viewGroup.getHeight();
                            AdMarvelWebView.m460b(linearLayout, layoutParams2, this.f732a.f878j, layoutParams.leftMargin, layoutParams.topMargin, layoutParams.width, layoutParams.height, viewGroup.getWidth(), height, (int) TypedValue.applyDimension(1, BitmapDescriptorFactory.HUE_ORANGE, this.f732a.getContext().getResources().getDisplayMetrics()));
                        } else {
                            return;
                        }
                    }
                    return;
                }
                this.f734c.f737c = this.f733b;
            }
        }

        public aa(AdMarvelWebView adMarvelWebView, Activity activity, int i) {
            super(activity, i);
            this.f737c = -1;
            this.f735a = new WeakReference(adMarvelWebView);
            this.f736b = new WeakReference(activity);
        }

        public void onOrientationChanged(int orientation) {
            if (this.f737c == -1) {
                this.f737c = orientation;
            } else if (Math.abs(orientation - this.f737c) >= 90 && Math.abs(orientation - this.f737c) <= 270) {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f735a.get();
                if (adMarvelWebView != null) {
                    new Handler(Looper.getMainLooper()).postDelayed(new AdMarvelWebView(this, adMarvelWebView, orientation), 500);
                }
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.ab */
    static class ab implements Runnable {
        private final WeakReference<AdMarvelWebView> f738a;
        private final WeakReference<AdMarvelInternalWebView> f739b;

        public ab(AdMarvelWebView adMarvelWebView, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f738a = new WeakReference(adMarvelWebView);
            this.f739b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f738a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f739b.get();
                if (adMarvelWebView != null && adMarvelInternalWebView != null) {
                    if (adMarvelWebView.f863R != null) {
                        new Handler(Looper.getMainLooper()).removeCallbacks(adMarvelWebView.f863R);
                        adMarvelWebView.f863R = null;
                    }
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (adMarvelUniversalVideoView != null) {
                        if (adMarvelUniversalVideoView.isPlaying()) {
                            adMarvelUniversalVideoView.pause();
                        }
                        if (adMarvelWebView.f859N != null && adMarvelWebView.f859N.length() > 0) {
                            adMarvelInternalWebView.m315e(adMarvelWebView.f859N + "()");
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.ac */
    static class ac implements Runnable {
        private final WeakReference<AdMarvelWebView> f740a;
        private final WeakReference<AdMarvelInternalWebView> f741b;

        public ac(AdMarvelWebView adMarvelWebView, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f740a = new WeakReference(adMarvelWebView);
            this.f741b = new WeakReference(adMarvelInternalWebView);
        }

        @TargetApi(14)
        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f740a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f741b.get();
                if (adMarvelWebView != null && adMarvelInternalWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (adMarvelUniversalVideoView != null) {
                        adMarvelUniversalVideoView.start();
                        if (adMarvelWebView.f855J != null && adMarvelWebView.f855J.length() > 0) {
                            adMarvelInternalWebView.m315e(adMarvelWebView.f855J + "()");
                        }
                        if (adMarvelWebView.f863R == null) {
                            adMarvelWebView.f863R = new ai(adMarvelWebView, adMarvelInternalWebView);
                            new Handler(Looper.getMainLooper()).postDelayed(adMarvelWebView.f863R, 500);
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.ad */
    static class ad implements Runnable {
        private final WeakReference<AdMarvelWebView> f742a;
        private final WeakReference<AdMarvelInternalWebView> f743b;
        private int f744c;
        private int f745d;
        private int f746e;
        private int f747f;

        public ad(AdMarvelWebView adMarvelWebView, AdMarvelInternalWebView adMarvelInternalWebView, int i, int i2, int i3, int i4) {
            this.f742a = new WeakReference(adMarvelWebView);
            this.f743b = new WeakReference(adMarvelInternalWebView);
            this.f744c = i;
            this.f745d = i2;
            this.f746e = i3;
            this.f747f = i4;
        }

        @TargetApi(14)
        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f742a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f743b.get();
                if (adMarvelWebView != null && adMarvelInternalWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = adMarvelWebView != null ? (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO") : null;
                    if (adMarvelUniversalVideoView != null) {
                        adMarvelUniversalVideoView.m381a(this.f746e, this.f747f, this.f744c, this.f745d);
                        adMarvelUniversalVideoView.m384c();
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.ae */
    static class ae implements Runnable {
        private final WeakReference<AdMarvelWebView> f748a;
        private final WeakReference<AdMarvelInternalWebView> f749b;

        public ae(AdMarvelWebView adMarvelWebView, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f748a = new WeakReference(adMarvelWebView);
            this.f749b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f748a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f749b.get();
                if (adMarvelWebView != null && adMarvelInternalWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (adMarvelUniversalVideoView != null && !adMarvelWebView.f865T) {
                        if (!adMarvelUniversalVideoView.isPlaying()) {
                            adMarvelUniversalVideoView.start();
                            if (adMarvelWebView.f860O != null && adMarvelWebView.f860O.length() > 0) {
                                adMarvelInternalWebView.m315e(adMarvelWebView.f860O + "()");
                            }
                        }
                        if (adMarvelWebView.f863R == null) {
                            adMarvelWebView.f863R = new ai(adMarvelWebView, adMarvelInternalWebView);
                            new Handler(Looper.getMainLooper()).postDelayed(adMarvelWebView.f863R, 500);
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.af */
    static class af implements Runnable {
        private final WeakReference<AdMarvelWebView> f750a;
        private final WeakReference<AdMarvelInternalWebView> f751b;
        private float f752c;

        public af(AdMarvelWebView adMarvelWebView, AdMarvelInternalWebView adMarvelInternalWebView, float f) {
            this.f750a = new WeakReference(adMarvelWebView);
            this.f751b = new WeakReference(adMarvelInternalWebView);
            this.f752c = f;
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f750a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f751b.get();
                if (adMarvelWebView != null && adMarvelInternalWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (adMarvelUniversalVideoView != null) {
                        adMarvelUniversalVideoView.seekTo((int) (this.f752c * 1000.0f));
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.ag */
    static class ag implements Runnable {
        private final WeakReference<AdMarvelWebView> f753a;

        public ag(AdMarvelWebView adMarvelWebView) {
            this.f753a = new WeakReference(adMarvelWebView);
        }

        public void run() {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f753a.get();
            if (adMarvelWebView != null) {
                adMarvelWebView.setBackgroundColor(adMarvelWebView.f871c);
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.ah */
    static class ah implements Runnable {
        private final WeakReference<AdMarvelWebView> f754a;
        private final WeakReference<AdMarvelInternalWebView> f755b;

        public ah(AdMarvelWebView adMarvelWebView, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f754a = new WeakReference(adMarvelWebView);
            this.f755b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f754a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f755b.get();
                if (adMarvelWebView != null && adMarvelInternalWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (adMarvelUniversalVideoView != null && adMarvelUniversalVideoView.isPlaying()) {
                        adMarvelUniversalVideoView.m380a();
                        if (adMarvelWebView.f861P != null && adMarvelWebView.f861P.length() > 0) {
                            adMarvelInternalWebView.m315e(adMarvelWebView.f861P + "()");
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.ai */
    static class ai implements Runnable {
        private final WeakReference<AdMarvelWebView> f756a;
        private final WeakReference<AdMarvelInternalWebView> f757b;

        public ai(AdMarvelWebView adMarvelWebView, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f756a = new WeakReference(adMarvelWebView);
            this.f757b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f756a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f757b.get();
                if (adMarvelWebView != null && adMarvelInternalWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (!(adMarvelInternalWebView == null || adMarvelUniversalVideoView == null)) {
                        try {
                            if (adMarvelWebView.f857L != null && adMarvelWebView.f857L.length() > 0) {
                                adMarvelInternalWebView.m315e(adMarvelWebView.f857L + "(" + (((float) adMarvelUniversalVideoView.getCurrentPositionToDisplay()) / 1000.0f) + ")");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(adMarvelWebView.f863R, 500);
                }
            } catch (Throwable e2) {
                Logging.log(Log.getStackTraceString(e2));
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.aj */
    static class aj implements Runnable {
        private final WeakReference<AdMarvelWebView> f758a;
        private String f759b;

        public aj(AdMarvelWebView adMarvelWebView, String str) {
            this.f758a = new WeakReference(adMarvelWebView);
            this.f759b = str;
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f758a.get();
                if (adMarvelWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (adMarvelUniversalVideoView != null && this.f759b != null && this.f759b.trim().length() > 0) {
                        if (this.f759b.equalsIgnoreCase("mute")) {
                            adMarvelUniversalVideoView.m385d();
                        } else if (this.f759b.equalsIgnoreCase("unmute")) {
                            adMarvelUniversalVideoView.m386e();
                        }
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.ak */
    static class ak implements Runnable {
        private final WeakReference<AdMarvelWebView> f760a;
        private String f761b;

        public ak(String str, AdMarvelWebView adMarvelWebView) {
            this.f761b = null;
            this.f761b = str;
            this.f760a = new WeakReference(adMarvelWebView);
        }

        public void run() {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f760a.get();
            if (adMarvelWebView != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "INTERNAL");
                if (adMarvelInternalWebView != null) {
                    int visibility = adMarvelInternalWebView.getVisibility();
                    if (this.f761b.equals("show") && visibility != 0) {
                        adMarvelInternalWebView.setVisibility(0);
                    }
                    if (this.f761b.equals("hide") && visibility == 0) {
                        adMarvelInternalWebView.setVisibility(4);
                    }
                }
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.al */
    static class al implements Runnable {
        private final WeakReference<AdMarvelInternalWebView> f762a;

        public al(AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f762a = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f762a.get();
            if (adMarvelInternalWebView != null) {
                adMarvelInternalWebView.setLayerType(1, null);
            }
        }
    }

    /* compiled from: AdMarvelWebView */
    /* renamed from: com.admarvel.android.ads.p.am */
    static class am implements Runnable {
        private final WeakReference<AdMarvelWebView> f763a;

        public am(AdMarvelWebView adMarvelWebView) {
            this.f763a = new WeakReference(adMarvelWebView);
        }

        public void run() {
            try {
                if (Version.getAndroidSDKVersion() >= 14) {
                    AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f763a.get();
                    if (adMarvelWebView != null) {
                        AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                        if (adMarvelUniversalVideoView != null) {
                            adMarvelUniversalVideoView.m380a();
                            adMarvelWebView.removeView(adMarvelUniversalVideoView);
                        }
                        if (adMarvelWebView.f863R != null) {
                            new Handler(Looper.getMainLooper()).removeCallbacks(adMarvelWebView.f863R);
                            adMarvelWebView.f863R = null;
                        }
                        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "INTERNAL");
                        if (adMarvelInternalWebView != null) {
                            adMarvelWebView.removeView(adMarvelInternalWebView);
                            adMarvelInternalWebView.setLayerType(2, null);
                            adMarvelWebView.removeAllViews();
                            adMarvelWebView.addView(adMarvelInternalWebView);
                            adMarvelInternalWebView.refreshDrawableState();
                            adMarvelInternalWebView.requestLayout();
                            adMarvelInternalWebView.forceLayout();
                        }
                        adMarvelWebView.forceLayout();
                        adMarvelWebView.requestLayout();
                        adMarvelWebView.invalidate();
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.b */
    class AdMarvelWebView extends WebViewClient {
        final /* synthetic */ AdMarvelWebView f764a;

        AdMarvelWebView(AdMarvelWebView adMarvelWebView) {
            this.f764a = adMarvelWebView;
        }

        public void onLoadResource(WebView view, String url) {
            Logging.log("Load Ad : onLoadResource URL - " + url);
            super.onLoadResource(view, url);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            this.f764a.av = true;
            Logging.log("Load Ad: onPageFinished");
            if (AdMarvelUtils.isLogDumpEnabled()) {
                view.loadUrl("javascript:window.ADMARVEL.fetchWebViewHtmlContent(document.getElementsByTagName('html')[0].outerHTML);");
            }
            if (!this.f764a.f885q.get() && this.f764a.f870b.compareAndSet(true, false)) {
                if (AdMarvelWebView.m451a(this.f764a.f888t) != null) {
                    AdMarvelInternalWebView.m277a(this.f764a.f888t, (AdMarvelInAppBrowserPrivateListener) this.f764a.aq.get());
                    AdMarvelWebView.m451a(this.f764a.f888t).m153a(this.f764a, this.f764a.au);
                } else {
                    Logging.log("Load Ad: onPageFinished - No listener found");
                }
            }
            this.f764a.f886r.set(true);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Logging.log("Load Ad: onPageStarted");
            this.f764a.f886r.set(false);
            if (Version.getAndroidSDKVersion() > 18) {
                new Handler(Looper.getMainLooper()).postDelayed(new AdMarvelWebView(this.f764a, url), Constants.WAIT_FOR_ON_PAGE_FINISHED);
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            AdMarvelInternalWebView.m283b(this.f764a.f888t);
            Logging.log("Load Ad: onReceivedError - Failing Url " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (this.f764a.f870b.compareAndSet(true, false) && AdMarvelWebView.m451a(this.f764a.f888t) != null) {
                AdMarvelWebView.m451a(this.f764a.f888t).m154a(this.f764a, this.f764a.au, 305, Utils.m178a(305));
            }
        }

        @TargetApi(21)
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String uri;
            String str;
            File dir;
            HttpURLConnection httpURLConnection;
            int responseCode;
            int contentLength;
            InputStream inputStream;
            List arrayList;
            byte[] bArr;
            Object obj;
            File file = null;
            int i = 0;
            if (request != null) {
                Uri url = request.getUrl();
                if (url != null) {
                    uri = url.toString();
                    Logging.log("Load Ad : shouldInterceptRequest URL - " + uri);
                    if (uri == null) {
                        return null;
                    }
                    if (uri.equals("http://baseurl.admarvel.com/mraid.js")) {
                        return super.shouldInterceptRequest(view, request);
                    }
                    str = Stomp.EMPTY;
                    dir = ((AdMarvelInternalWebView) this.f764a.findViewWithTag(this.f764a.f888t + "INTERNAL")).getContext().getDir("adm_assets", 0);
                    if (dir != null && dir.isDirectory()) {
                        file = new File(dir.getAbsolutePath() + "/mraid.js");
                    }
                    if (file == null && file.exists()) {
                        try {
                            Logging.log("Mraid loading from client");
                            return new WebResourceResponse(WebRequest.CONTENT_TYPE_CSS, HttpRequest.CHARSET_UTF8, new FileInputStream(file));
                        } catch (Throwable e) {
                            Logging.log(Log.getStackTraceString(e));
                            return super.shouldInterceptRequest(view, request);
                        }
                    }
                    try {
                        httpURLConnection = (HttpURLConnection) new URL(Constants.MRAID_JS_URL).openConnection();
                        httpURLConnection.setRequestMethod(HttpRequest.METHOD_GET);
                        httpURLConnection.setDoOutput(false);
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setUseCaches(false);
                        httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_FORM);
                        httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, "0");
                        httpURLConnection.setConnectTimeout(AdError.SERVER_ERROR_CODE);
                        httpURLConnection.setReadTimeout(AMQConnection.HANDSHAKE_TIMEOUT);
                        responseCode = httpURLConnection.getResponseCode();
                        contentLength = httpURLConnection.getContentLength();
                        Logging.log("Mraid Connection Status Code: " + responseCode);
                        Logging.log("Mraid Content Length: " + contentLength);
                        if (responseCode == AMQP.REPLY_SUCCESS) {
                            return super.shouldInterceptRequest(view, request);
                        }
                        inputStream = (InputStream) httpURLConnection.getContent();
                        arrayList = new ArrayList();
                        responseCode = 0;
                        contentLength = Flags.FLAG2;
                        while (contentLength != -1) {
                            bArr = new byte[Flags.FLAG2];
                            contentLength = inputStream.read(bArr, 0, Flags.FLAG2);
                            if (contentLength > 0) {
                                AdMarvelWebView adMarvelWebView = new AdMarvelWebView();
                                adMarvelWebView.f768a = bArr;
                                adMarvelWebView.f769b = contentLength;
                                responseCode += contentLength;
                                arrayList.add(adMarvelWebView);
                            }
                        }
                        inputStream.close();
                        if (responseCode <= 0) {
                            obj = new byte[responseCode];
                            for (responseCode = 0; responseCode < arrayList.size(); responseCode++) {
                                AdMarvelWebView adMarvelWebView2 = (AdMarvelWebView) arrayList.get(responseCode);
                                System.arraycopy(adMarvelWebView2.f768a, 0, obj, i, adMarvelWebView2.f769b);
                                i += adMarvelWebView2.f769b;
                            }
                            uri = new String(obj);
                        } else {
                            uri = str;
                        }
                        return new WebResourceResponse(WebRequest.CONTENT_TYPE_CSS, HttpRequest.CHARSET_UTF8, new ByteArrayInputStream(uri.getBytes()));
                    } catch (Throwable e2) {
                        Logging.log(Log.getStackTraceString(e2));
                        return super.shouldInterceptRequest(view, request);
                    }
                }
            }
            uri = null;
            Logging.log("Load Ad : shouldInterceptRequest URL - " + uri);
            if (uri == null) {
                return null;
            }
            if (uri.equals("http://baseurl.admarvel.com/mraid.js")) {
                return super.shouldInterceptRequest(view, request);
            }
            str = Stomp.EMPTY;
            dir = ((AdMarvelInternalWebView) this.f764a.findViewWithTag(this.f764a.f888t + "INTERNAL")).getContext().getDir("adm_assets", 0);
            file = new File(dir.getAbsolutePath() + "/mraid.js");
            if (file == null) {
            }
            httpURLConnection = (HttpURLConnection) new URL(Constants.MRAID_JS_URL).openConnection();
            httpURLConnection.setRequestMethod(HttpRequest.METHOD_GET);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_FORM);
            httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, "0");
            httpURLConnection.setConnectTimeout(AdError.SERVER_ERROR_CODE);
            httpURLConnection.setReadTimeout(AMQConnection.HANDSHAKE_TIMEOUT);
            responseCode = httpURLConnection.getResponseCode();
            contentLength = httpURLConnection.getContentLength();
            Logging.log("Mraid Connection Status Code: " + responseCode);
            Logging.log("Mraid Content Length: " + contentLength);
            if (responseCode == AMQP.REPLY_SUCCESS) {
                return super.shouldInterceptRequest(view, request);
            }
            inputStream = (InputStream) httpURLConnection.getContent();
            arrayList = new ArrayList();
            responseCode = 0;
            contentLength = Flags.FLAG2;
            while (contentLength != -1) {
                bArr = new byte[Flags.FLAG2];
                contentLength = inputStream.read(bArr, 0, Flags.FLAG2);
                if (contentLength > 0) {
                    AdMarvelWebView adMarvelWebView3 = new AdMarvelWebView();
                    adMarvelWebView3.f768a = bArr;
                    adMarvelWebView3.f769b = contentLength;
                    responseCode += contentLength;
                    arrayList.add(adMarvelWebView3);
                }
            }
            inputStream.close();
            if (responseCode <= 0) {
                uri = str;
            } else {
                obj = new byte[responseCode];
                for (responseCode = 0; responseCode < arrayList.size(); responseCode++) {
                    AdMarvelWebView adMarvelWebView22 = (AdMarvelWebView) arrayList.get(responseCode);
                    System.arraycopy(adMarvelWebView22.f768a, 0, obj, i, adMarvelWebView22.f769b);
                    i += adMarvelWebView22.f769b;
                }
                uri = new String(obj);
            }
            return new WebResourceResponse(WebRequest.CONTENT_TYPE_CSS, HttpRequest.CHARSET_UTF8, new ByteArrayInputStream(uri.getBytes()));
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logging.log("Load Ad : shouldOverrideUrlLoading URL - " + url);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f764a.findViewWithTag(this.f764a.f888t + "INTERNAL");
            if (adMarvelInternalWebView == null && this.f764a.f893y) {
                Context context = this.f764a.getContext();
                if (context != null && (context instanceof Activity)) {
                    adMarvelInternalWebView = (AdMarvelInternalWebView) ((ViewGroup) ((Activity) context).getWindow().findViewById(16908290)).findViewWithTag(this.f764a.f888t + "INTERNAL");
                }
            }
            if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
                return false;
            }
            if (Utils.m209f(url)) {
                return true;
            }
            if (this.f764a.an) {
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m313c() && (url == null || url.length() <= 0 || (!url.startsWith("admarvelsdk") && !url.startsWith("admarvelinternal")))) {
                    return true;
                }
                if (Utils.m193a(this.f764a.getContext(), url, this.f764a.f867V)) {
                    new Utils(this.f764a.getContext()).m247c(this.f764a.ap);
                    return true;
                } else if (url != null && Utils.m179a(url, "admarvelsdk") != C0250s.NONE) {
                    if (AdMarvelWebView.m451a(this.f764a.f888t) != null) {
                        AdMarvelWebView.m451a(this.f764a.f888t).m152a(this.f764a.au, Utils.m184a(url, "admarvelsdk", Stomp.EMPTY, Utils.m179a(url, "admarvelsdk"), this.f764a.getContext()));
                    }
                    new Utils(this.f764a.getContext()).m247c(this.f764a.ap);
                    return true;
                } else if (url != null && Utils.m179a(url, "admarvelinternal") != C0250s.NONE) {
                    if (AdMarvelWebView.m451a(this.f764a.f888t) != null) {
                        AdMarvelWebView.m451a(this.f764a.f888t).m152a(this.f764a.au, Utils.m184a(url, "admarvelinternal", Stomp.EMPTY, Utils.m179a(url, "admarvelinternal"), this.f764a.getContext()));
                    }
                    new Utils(this.f764a.getContext()).m247c(this.f764a.ap);
                    return true;
                } else if (url != null && Utils.m179a(url, "admarvelvideo") != C0250s.NONE) {
                    String a = Utils.m184a(url, "admarvelvideo", "http://", Utils.m179a(url, "admarvelvideo"), this.f764a.getContext());
                    r1 = new Intent("android.intent.action.VIEW");
                    r1.addFlags(268435456);
                    r1.setDataAndType(Uri.parse(a), "video/*");
                    if (Utils.m191a(this.f764a.getContext(), r1)) {
                        this.f764a.getContext().startActivity(r1);
                    }
                    new Utils(this.f764a.getContext()).m247c(this.f764a.ap);
                    return true;
                } else if (url != null && Utils.m179a(url, "admarvelexternal") != C0250s.NONE) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(Utils.m184a(url, "admarvelexternal", Stomp.EMPTY, Utils.m179a(url, "admarvelexternal"), this.f764a.getContext())));
                    intent.addFlags(268435456);
                    if (Utils.m191a(this.f764a.getContext(), intent)) {
                        this.f764a.getContext().startActivity(intent);
                    }
                    new Utils(this.f764a.getContext()).m247c(this.f764a.ap);
                    return true;
                } else if (!(url == null || Utils.m179a(url, "admarvelcustomvideo") == C0250s.NONE)) {
                    r1 = new Intent(this.f764a.getContext(), AdMarvelVideoActivity.class);
                    r1.addFlags(268435456);
                    this.f764a.au.removeNonStringEntriesTargetParam();
                    try {
                        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(this.f764a.au);
                        objectOutputStream.close();
                        r1.putExtra("serialized_admarvelad", byteArrayOutputStream.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    r1.putExtra(SettingsJsonConstants.APP_URL_KEY, url);
                    r1.putExtra("isCustomUrl", true);
                    r1.putExtra("xml", this.f764a.ap);
                    r1.putExtra(locationTracking.source, this.f764a.ak);
                    r1.putExtra("GUID", this.f764a.f888t);
                    this.f764a.getContext().startActivity(r1);
                    new Utils(this.f764a.getContext()).m247c(this.f764a.ap);
                    return true;
                }
            }
            if (this.f764a.f887s.get() || (adMarvelInternalWebView != null && adMarvelInternalWebView.m313c())) {
                this.f764a.m483e(url);
            }
            return true;
        }
    }

    /* renamed from: com.admarvel.android.ads.p.c */
    class AdMarvelWebView extends WebViewClient {
        final /* synthetic */ AdMarvelWebView f765a;

        AdMarvelWebView(AdMarvelWebView adMarvelWebView) {
            this.f765a = adMarvelWebView;
        }

        public void onLoadResource(WebView view, String url) {
            Logging.log("Load Ad : onLoadResource URL - " + url);
            super.onLoadResource(view, url);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logging.log("Load Ad: onPageFinished");
            if (AdMarvelUtils.isLogDumpEnabled()) {
                view.loadUrl("javascript:window.ADMARVEL.fetchWebViewHtmlContent(document.getElementsByTagName('html')[0].outerHTML);");
            }
            if (!(this.f765a.f885q.get() || !this.f765a.f870b.compareAndSet(true, false) || AdMarvelWebView.m451a(this.f765a.f888t) == null)) {
                AdMarvelInternalWebView.m277a(this.f765a.f888t, (AdMarvelInAppBrowserPrivateListener) this.f765a.aq.get());
                AdMarvelWebView.m451a(this.f765a.f888t).m153a(this.f765a, this.f765a.au);
            }
            this.f765a.f886r.set(true);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Logging.log("Load Ad: onPageStarted");
            this.f765a.f886r.set(false);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Logging.log("Load Ad : onReceivedError Failing URL - " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (this.f765a.f870b.compareAndSet(true, false) && AdMarvelWebView.m451a(this.f765a.f888t) != null) {
                AdMarvelWebView.m451a(this.f765a.f888t).m154a(this.f765a, this.f765a.au, 305, Utils.m178a(305));
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logging.log("Load Ad : shouldOverrideUrlLoading URL - " + url);
            if (url == null) {
                return false;
            }
            try {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f765a.findViewWithTag(this.f765a.f888t + "INTERNAL");
                if (adMarvelInternalWebView == null && this.f765a.f893y) {
                    Context context = this.f765a.getContext();
                    if (context != null && (context instanceof Activity)) {
                        adMarvelInternalWebView = (AdMarvelInternalWebView) ((ViewGroup) ((Activity) context).getWindow().findViewById(16908290)).findViewWithTag(this.f765a.f888t + "INTERNAL");
                    }
                }
                if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
                    return false;
                }
                if (Utils.m209f(url)) {
                    return true;
                }
                if (this.f765a.an) {
                    if (Utils.m193a(this.f765a.getContext(), url, this.f765a.f867V)) {
                        new Utils(this.f765a.getContext()).m247c(this.f765a.ap);
                        return true;
                    } else if (url != null && Utils.m179a(url, "admarvelsdk") != C0250s.NONE) {
                        if (AdMarvelWebView.m451a(this.f765a.f888t) != null) {
                            AdMarvelWebView.m451a(this.f765a.f888t).m152a(this.f765a.au, Utils.m184a(url, "admarvelsdk", Stomp.EMPTY, Utils.m179a(url, "admarvelsdk"), this.f765a.getContext()));
                        }
                        new Utils(this.f765a.getContext()).m247c(this.f765a.ap);
                        return true;
                    } else if (url != null && Utils.m179a(url, "admarvelinternal") != C0250s.NONE) {
                        if (AdMarvelWebView.m451a(this.f765a.f888t) != null) {
                            AdMarvelWebView.m451a(this.f765a.f888t).m152a(this.f765a.au, Utils.m184a(url, "admarvelinternal", Stomp.EMPTY, Utils.m179a(url, "admarvelinternal"), this.f765a.getContext()));
                        }
                        new Utils(this.f765a.getContext()).m247c(this.f765a.ap);
                        return true;
                    } else if (url != null && Utils.m179a(url, "admarvelvideo") != C0250s.NONE) {
                        String a = Utils.m184a(url, "admarvelvideo", "http://", Utils.m179a(url, "admarvelvideo"), this.f765a.getContext());
                        r2 = new Intent("android.intent.action.VIEW");
                        r2.addFlags(268435456);
                        r2.setDataAndType(Uri.parse(a), "video/*");
                        if (Utils.m191a(this.f765a.getContext(), r2)) {
                            this.f765a.getContext().startActivity(r2);
                        }
                        new Utils(this.f765a.getContext()).m247c(this.f765a.ap);
                        return true;
                    } else if (url != null && Utils.m179a(url, "admarvelexternal") != C0250s.NONE) {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(Utils.m184a(url, "admarvelexternal", Stomp.EMPTY, Utils.m179a(url, "admarvelexternal"), this.f765a.getContext())));
                        intent.addFlags(268435456);
                        if (Utils.m191a(this.f765a.getContext(), intent)) {
                            this.f765a.getContext().startActivity(intent);
                        }
                        new Utils(this.f765a.getContext()).m247c(this.f765a.ap);
                        return true;
                    } else if (!(url == null || Utils.m179a(url, "admarvelcustomvideo") == C0250s.NONE)) {
                        r2 = new Intent(this.f765a.getContext(), AdMarvelVideoActivity.class);
                        r2.addFlags(268435456);
                        r2.putExtra(SettingsJsonConstants.APP_URL_KEY, url);
                        this.f765a.au.removeNonStringEntriesTargetParam();
                        try {
                            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                            objectOutputStream.writeObject(this.f765a.au);
                            objectOutputStream.close();
                            r2.putExtra("serialized_admarvelad", byteArrayOutputStream.toByteArray());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        r2.putExtra("isCustomUrl", true);
                        r2.putExtra("xml", this.f765a.ap);
                        r2.putExtra(locationTracking.source, this.f765a.ak);
                        r2.putExtra("GUID", this.f765a.f888t);
                        this.f765a.getContext().startActivity(r2);
                        new Utils(this.f765a.getContext()).m247c(this.f765a.ap);
                        return true;
                    }
                }
                if (this.f765a.f887s.get() || adMarvelInternalWebView.m313c()) {
                    this.f765a.m483e(url);
                }
                return true;
            } catch (Exception e2) {
                Logging.log(e2.getMessage() + "exception in shouldOverrideUrlLoading");
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.d */
    class AdMarvelWebView extends WebViewClient {
        final /* synthetic */ AdMarvelWebView f766a;

        AdMarvelWebView(AdMarvelWebView adMarvelWebView) {
            this.f766a = adMarvelWebView;
        }

        public void onLoadResource(WebView view, String url) {
            Logging.log("Load Ad : onLoadResource URL - " + url);
            super.onLoadResource(view, url);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            this.f766a.av = true;
            Logging.log("Load Ad: onPageFinished");
            if (AdMarvelUtils.isLogDumpEnabled()) {
                view.loadUrl("javascript:window.ADMARVEL.fetchWebViewHtmlContent(document.getElementsByTagName('html')[0].outerHTML);");
            }
            if (!this.f766a.f885q.get() && this.f766a.f870b.compareAndSet(true, false)) {
                if (AdMarvelWebView.m451a(this.f766a.f888t) != null) {
                    AdMarvelInternalWebView.m277a(this.f766a.f888t, (AdMarvelInAppBrowserPrivateListener) this.f766a.aq.get());
                    AdMarvelWebView.m451a(this.f766a.f888t).m153a(this.f766a, this.f766a.au);
                } else {
                    Logging.log("Load Ad: onPageFinished - No listener found");
                }
            }
            this.f766a.f886r.set(true);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Logging.log("Load Ad: onPageStarted");
            this.f766a.f886r.set(false);
            if (Version.getAndroidSDKVersion() > 18) {
                new Handler(Looper.getMainLooper()).postDelayed(new AdMarvelWebView(this.f766a, url), Constants.WAIT_FOR_ON_PAGE_FINISHED);
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            AdMarvelInternalWebView.m283b(this.f766a.f888t);
            Logging.log("Load Ad: onReceivedError - Failing Url " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (this.f766a.f870b.compareAndSet(true, false) && AdMarvelWebView.m451a(this.f766a.f888t) != null) {
                AdMarvelWebView.m451a(this.f766a.f888t).m154a(this.f766a, this.f766a.au, 305, Utils.m178a(305));
            }
        }

        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            File file = null;
            int i = 0;
            Logging.log("Load Ad : shouldInterceptRequest URL - " + url);
            if (url == null) {
                return null;
            }
            if (!url.equals("http://baseurl.admarvel.com/mraid.js")) {
                return super.shouldInterceptRequest(view, url);
            }
            String str = Stomp.EMPTY;
            File dir = ((AdMarvelInternalWebView) this.f766a.findViewWithTag(this.f766a.f888t + "INTERNAL")).getContext().getDir("adm_assets", 0);
            if (dir != null && dir.isDirectory()) {
                file = new File(dir.getAbsolutePath() + "/mraid.js");
            }
            if (file == null || !file.exists()) {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constants.MRAID_JS_URL).openConnection();
                    httpURLConnection.setRequestMethod(HttpRequest.METHOD_GET);
                    httpURLConnection.setDoOutput(false);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_FORM);
                    httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, "0");
                    httpURLConnection.setConnectTimeout(AdError.SERVER_ERROR_CODE);
                    httpURLConnection.setReadTimeout(AMQConnection.HANDSHAKE_TIMEOUT);
                    int responseCode = httpURLConnection.getResponseCode();
                    int contentLength = httpURLConnection.getContentLength();
                    Logging.log("Mraid Connection Status Code: " + responseCode);
                    Logging.log("Mraid Content Length: " + contentLength);
                    if (responseCode != AMQP.REPLY_SUCCESS) {
                        return super.shouldInterceptRequest(view, url);
                    }
                    String str2;
                    InputStream inputStream = (InputStream) httpURLConnection.getContent();
                    List arrayList = new ArrayList();
                    responseCode = 0;
                    contentLength = Flags.FLAG2;
                    while (contentLength != -1) {
                        byte[] bArr = new byte[Flags.FLAG2];
                        contentLength = inputStream.read(bArr, 0, Flags.FLAG2);
                        if (contentLength > 0) {
                            AdMarvelWebView adMarvelWebView = new AdMarvelWebView();
                            adMarvelWebView.f768a = bArr;
                            adMarvelWebView.f769b = contentLength;
                            responseCode += contentLength;
                            arrayList.add(adMarvelWebView);
                        }
                    }
                    inputStream.close();
                    if (responseCode > 0) {
                        Object obj = new byte[responseCode];
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            AdMarvelWebView adMarvelWebView2 = (AdMarvelWebView) arrayList.get(i2);
                            System.arraycopy(adMarvelWebView2.f768a, 0, obj, i, adMarvelWebView2.f769b);
                            i += adMarvelWebView2.f769b;
                        }
                        str2 = new String(obj);
                    } else {
                        str2 = str;
                    }
                    return new WebResourceResponse(WebRequest.CONTENT_TYPE_CSS, HttpRequest.CHARSET_UTF8, new ByteArrayInputStream(str2.getBytes()));
                } catch (Throwable e) {
                    Logging.log(Log.getStackTraceString(e));
                    return super.shouldInterceptRequest(view, url);
                }
            }
            try {
                Logging.log("Mraid loading from client");
                return new WebResourceResponse(WebRequest.CONTENT_TYPE_CSS, HttpRequest.CHARSET_UTF8, new FileInputStream(file));
            } catch (Throwable e2) {
                Logging.log(Log.getStackTraceString(e2));
                return super.shouldInterceptRequest(view, url);
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logging.log("Load Ad : shouldOverrideUrlLoading URL - " + url);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f766a.findViewWithTag(this.f766a.f888t + "INTERNAL");
            if (adMarvelInternalWebView == null && this.f766a.f893y) {
                Context context = this.f766a.getContext();
                if (context != null && (context instanceof Activity)) {
                    adMarvelInternalWebView = (AdMarvelInternalWebView) ((ViewGroup) ((Activity) context).getWindow().findViewById(16908290)).findViewWithTag(this.f766a.f888t + "INTERNAL");
                }
            }
            if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
                return false;
            }
            if (Utils.m209f(url)) {
                return true;
            }
            if (this.f766a.an) {
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m313c() && (url == null || url.length() <= 0 || (!url.startsWith("admarvelsdk") && !url.startsWith("admarvelinternal")))) {
                    return true;
                }
                if (Utils.m193a(this.f766a.getContext(), url, this.f766a.f867V)) {
                    new Utils(this.f766a.getContext()).m247c(this.f766a.ap);
                    return true;
                } else if (url != null && Utils.m179a(url, "admarvelsdk") != C0250s.NONE) {
                    if (AdMarvelWebView.m451a(this.f766a.f888t) != null) {
                        AdMarvelWebView.m451a(this.f766a.f888t).m152a(this.f766a.au, Utils.m184a(url, "admarvelsdk", Stomp.EMPTY, Utils.m179a(url, "admarvelsdk"), this.f766a.getContext()));
                    }
                    new Utils(this.f766a.getContext()).m247c(this.f766a.ap);
                    return true;
                } else if (url != null && Utils.m179a(url, "admarvelinternal") != C0250s.NONE) {
                    if (AdMarvelWebView.m451a(this.f766a.f888t) != null) {
                        AdMarvelWebView.m451a(this.f766a.f888t).m152a(this.f766a.au, Utils.m184a(url, "admarvelinternal", Stomp.EMPTY, Utils.m179a(url, "admarvelinternal"), this.f766a.getContext()));
                    }
                    new Utils(this.f766a.getContext()).m247c(this.f766a.ap);
                    return true;
                } else if (url != null && Utils.m179a(url, "admarvelvideo") != C0250s.NONE) {
                    String a = Utils.m184a(url, "admarvelvideo", "http://", Utils.m179a(url, "admarvelvideo"), this.f766a.getContext());
                    r1 = new Intent("android.intent.action.VIEW");
                    r1.addFlags(268435456);
                    r1.setDataAndType(Uri.parse(a), "video/*");
                    if (Utils.m191a(this.f766a.getContext(), r1)) {
                        this.f766a.getContext().startActivity(r1);
                    }
                    new Utils(this.f766a.getContext()).m247c(this.f766a.ap);
                    return true;
                } else if (url != null && Utils.m179a(url, "admarvelexternal") != C0250s.NONE) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(Utils.m184a(url, "admarvelexternal", Stomp.EMPTY, Utils.m179a(url, "admarvelexternal"), this.f766a.getContext())));
                    intent.addFlags(268435456);
                    if (Utils.m191a(this.f766a.getContext(), intent)) {
                        this.f766a.getContext().startActivity(intent);
                    }
                    new Utils(this.f766a.getContext()).m247c(this.f766a.ap);
                    return true;
                } else if (!(url == null || Utils.m179a(url, "admarvelcustomvideo") == C0250s.NONE)) {
                    r1 = new Intent(this.f766a.getContext(), AdMarvelVideoActivity.class);
                    r1.addFlags(268435456);
                    this.f766a.au.removeNonStringEntriesTargetParam();
                    try {
                        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(this.f766a.au);
                        objectOutputStream.close();
                        r1.putExtra("serialized_admarvelad", byteArrayOutputStream.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    r1.putExtra(SettingsJsonConstants.APP_URL_KEY, url);
                    r1.putExtra("isCustomUrl", true);
                    r1.putExtra("xml", this.f766a.ap);
                    r1.putExtra(locationTracking.source, this.f766a.ak);
                    r1.putExtra("GUID", this.f766a.f888t);
                    this.f766a.getContext().startActivity(r1);
                    new Utils(this.f766a.getContext()).m247c(this.f766a.ap);
                    return true;
                }
            }
            if (this.f766a.f887s.get() || (adMarvelInternalWebView != null && adMarvelInternalWebView.m313c())) {
                this.f766a.m483e(url);
            }
            return true;
        }
    }

    /* renamed from: com.admarvel.android.ads.p.e */
    private static class AdMarvelWebView implements AdMarvelInAppBrowserPrivateListener, AdMarvelTwoPartPrivateListener {
        private final WeakReference<AdMarvelWebView> f767a;

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView) {
            this.f767a = new WeakReference(adMarvelWebView);
        }

        public void m432a() {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f767a.get();
            if (adMarvelWebView != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "INTERNAL");
                if (adMarvelInternalWebView != null) {
                    adMarvelWebView.f894z = false;
                    adMarvelWebView.f882n = false;
                    if (AdMarvelWebView.m451a(adMarvelWebView.f888t) != null) {
                        AdMarvelWebView.m451a(adMarvelWebView.f888t).m155b();
                    }
                    if (adMarvelWebView.f880l != null) {
                        adMarvelInternalWebView.m315e(adMarvelWebView.f880l + "()");
                    }
                }
            }
        }

        public void m433a(String str) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f767a.get();
            if (adMarvelWebView != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) adMarvelWebView.findViewWithTag(str + "INTERNAL");
                if (adMarvelInternalWebView != null && adMarvelInternalWebView.f585O != null && adMarvelInternalWebView.f585O.length() > 0) {
                    adMarvelInternalWebView.m315e(adMarvelInternalWebView.f585O + "()");
                }
            }
        }

        public void m434b() {
            this.f767a.clear();
        }
    }

    /* renamed from: com.admarvel.android.ads.p.f */
    private static class AdMarvelWebView {
        public byte[] f768a;
        public int f769b;

        private AdMarvelWebView() {
            this.f768a = null;
            this.f769b = 0;
        }
    }

    /* renamed from: com.admarvel.android.ads.p.g */
    static class AdMarvelWebView implements Runnable {
        private final String f770a;
        private final WeakReference<AdMarvelInternalWebView> f771b;
        private final WeakReference<AdMarvelWebView> f772c;

        public AdMarvelWebView(String str, AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelWebView adMarvelWebView) {
            this.f770a = str;
            this.f771b = new WeakReference(adMarvelInternalWebView);
            this.f772c = new WeakReference(adMarvelWebView);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r26 = this;
            r0 = r26;
            r2 = r0.f771b;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r2 = r2.get();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r2 = (com.admarvel.android.ads.AdMarvelInternalWebView) r2;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r0 = r26;
            r3 = r0.f772c;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r3 = r3.get();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r3 = (com.admarvel.android.ads.AdMarvelWebView) r3;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r3 != 0) goto L_0x0017;
        L_0x0016:
            return;
        L_0x0017:
            if (r2 == 0) goto L_0x0016;
        L_0x0019:
            r5 = 0;
            r4 = "";
            r6 = -1;
            r4 = com.admarvel.android.ads.Version.getAndroidSDKVersion();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r7 = 9;
            if (r4 >= r7) goto L_0x03d3;
        L_0x0025:
            r4 = r3.getResources();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.getConfiguration();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.orientation;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r7 = 1;
            if (r4 != r7) goto L_0x03ca;
        L_0x0032:
            r4 = 0;
            r20 = r4;
        L_0x0035:
            r4 = "NO";
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r21 = com.admarvel.android.ads.Utils.m181a(r4);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = "wifi";
            r0 = r21;
            r4 = r0.equals(r4);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 != 0) goto L_0x0053;
        L_0x0049:
            r4 = "mobile";
            r0 = r21;
            r4 = r0.equals(r4);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 == 0) goto L_0x0409;
        L_0x0053:
            r4 = "YES";
            r19 = r4;
        L_0x0057:
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = "location";
            r4 = com.admarvel.android.ads.Utils.m199b(r4, r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 == 0) goto L_0x0483;
        L_0x0063:
            r4 = com.admarvel.android.util.AdMarvelLocationManager.m566a();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.m572a(r2);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
        L_0x006b:
            if (r4 == 0) goto L_0x040f;
        L_0x006d:
            r5 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5.<init>();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = "{lat:";
            r5 = r5.append(r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = r4.getLatitude();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r5.append(r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = ", lon:";
            r5 = r5.append(r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = r4.getLongitude();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r5.append(r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = ", acc:";
            r5 = r5.append(r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.getAccuracy();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r5.append(r4);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = "}";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.toString();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r18 = r4;
        L_0x00a8:
            r22 = "2.6.0";
            r4 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4.<init>();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = "{screen: true, orientation: true, heading: ";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = "compass";
            r5 = com.admarvel.android.ads.Utils.m199b(r5, r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = ", location : ";
            r5 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = "android.permission.ACCESS_COARSE_LOCATION";
            r4 = com.admarvel.android.ads.Utils.m202c(r4, r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 != 0) goto L_0x00e1;
        L_0x00d5:
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = "android.permission.ACCESS_FINE_LOCATION";
            r4 = com.admarvel.android.ads.Utils.m202c(r4, r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 == 0) goto L_0x0415;
        L_0x00e1:
            r4 = 1;
        L_0x00e2:
            r4 = r5.append(r4);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = ",shake: ";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = "accelerometer";
            r5 = com.admarvel.android.ads.Utils.m199b(r5, r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = ",tilt: ";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = "accelerometer";
            r5 = com.admarvel.android.ads.Utils.m199b(r5, r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = ", network: true, sms:";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = com.admarvel.android.ads.Utils.m230q(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = ", phone:";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = com.admarvel.android.ads.Utils.m230q(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = ", email:true,calendar:";
            r5 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = "android.permission.READ_CALENDAR";
            r4 = com.admarvel.android.ads.Utils.m202c(r4, r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 == 0) goto L_0x0418;
        L_0x0144:
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = "android.permission.WRITE_CALENDAR";
            r4 = com.admarvel.android.ads.Utils.m202c(r4, r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 == 0) goto L_0x0418;
        L_0x0150:
            r4 = 1;
        L_0x0151:
            r4 = r5.append(r4);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = ", camera: ";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = "camera";
            r5 = com.admarvel.android.ads.Utils.m199b(r5, r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = ",map:true, audio:true, video:true, 'level-1':true,'level-2': true, 'level-3':false}";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r23 = r4.toString();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r12 = 0;
            r11 = 0;
            r10 = 0;
            r9 = 0;
            r8 = 0;
            r7 = 0;
            r6 = 0;
            r5 = 0;
            r24 = "Banner";
            r13 = 0;
            r4 = r2.f595d;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.get();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = (com.admarvel.android.ads.AdMarvelView) r4;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 == 0) goto L_0x0477;
        L_0x0188:
            r5 = 2;
            r7 = new int[r5];	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r2.getLocationOnScreen(r7);	 Catch:{ Exception -> 0x041b, NullPointerException -> 0x03c0 }
        L_0x018e:
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r5 instanceof android.app.Activity;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r5 == 0) goto L_0x0474;
        L_0x0196:
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = (android.app.Activity) r5;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r5 == 0) goto L_0x0474;
        L_0x019e:
            r6 = r3.f891w;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r8 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
            if (r6 == r8) goto L_0x0430;
        L_0x01a4:
            r6 = r3.f891w;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r6 < 0) goto L_0x0430;
        L_0x01a8:
            r5 = r3.f891w;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
        L_0x01aa:
            r6 = 0;
            r11 = r7[r6];	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = 1;
            r6 = r7[r6];	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r10 = r6 - r5;
            r9 = r2.getWidth();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r8 = r2.getHeight();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = 2;
            r6 = new int[r6];	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4.getLocationOnScreen(r6);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r7 = 0;
            r7 = r6[r7];	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r12 = 1;
            r6 = r6[r12];	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = r6 - r5;
            r5 = r3.getAdMarvelAd();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r5 == 0) goto L_0x0467;
        L_0x01cd:
            r5 = r3.getAdMarvelAd();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r5.getAdMarvelViewWidth();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r12 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
            r5 = (r5 > r12 ? 1 : (r5 == r12 ? 0 : -1));
            if (r5 == 0) goto L_0x0460;
        L_0x01db:
            r5 = r3.getAdMarvelAd();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r5.getAdMarvelViewWidth();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r12 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r12 = com.admarvel.android.ads.Utils.m226o(r12);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r5 * r12;
        L_0x01ec:
            r5 = (int) r5;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
        L_0x01ed:
            r4 = r4.getHeight();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r12 = r6;
            r13 = r7;
            r14 = r8;
            r15 = r9;
            r16 = r10;
            r17 = r11;
            r10 = r4;
            r11 = r5;
        L_0x01fb:
            r9 = "0,90";
            r4 = r3.getRootView();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r8 = r4.getLeft();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r3.getRootView();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r7 = r4.getTop();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r3.getRootView();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = r4.getWidth();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r3.getRootView();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r4.getHeight();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4 instanceof android.app.Activity;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 == 0) goto L_0x046d;
        L_0x0225:
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = (android.app.Activity) r4;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 == 0) goto L_0x046d;
        L_0x022d:
            r8 = com.admarvel.android.ads.Utils.m180a(r4);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.getWindow();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = 16908290; // 0x1020002 float:2.3877235E-38 double:8.353805E-317;
            r4 = r4.findViewById(r5);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = (android.view.ViewGroup) r4;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r7 = r4.getLeft();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = r4.getTop();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r4.getWidth();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r4.getHeight();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
        L_0x024e:
            r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r9.<init>();	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r25 = "";
            r0 = r25;
            r9 = r9.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r0 = r26;
            r0 = r0.f770a;	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r25 = r0;
            r0 = r25;
            r9 = r9.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r25 = "({x:";
            r0 = r25;
            r9 = r9.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r0 = r17;
            r9 = r9.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r17 = ",y:";
            r0 = r17;
            r9 = r9.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r0 = r16;
            r9 = r9.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r16 = ",width:";
            r0 = r16;
            r9 = r9.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r9 = r9.append(r15);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r15 = ",height:";
            r9 = r9.append(r15);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r9 = r9.append(r14);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r14 = ",appX:";
            r9 = r9.append(r14);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r7 = r9.append(r7);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r9 = ",appY:";
            r7 = r7.append(r9);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r6 = r7.append(r6);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r7 = ",appWidth:";
            r6 = r6.append(r7);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = r6.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r6 = ",appHeight:";
            r5 = r5.append(r6);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = r5.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = ",orientation:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r0 = r20;
            r4 = r4.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = ",defaultX:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = r4.append(r13);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = ",defaultY:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = r4.append(r12);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = ",defaultWidth:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = r4.append(r11);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = ",defaultHeight:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = r4.append(r10);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = ",networkType:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = "'";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r0 = r21;
            r4 = r4.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = "'";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = ",network:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = "'";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r0 = r19;
            r4 = r4.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = "'";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = ",screenWidth:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = r3.getContext();	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = com.admarvel.android.ads.Utils.m222m(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r5 = ",screenHeight:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r3 = r3.getContext();	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r3 = com.admarvel.android.ads.Utils.m224n(r3);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r3 = r4.append(r3);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = ",adType:";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r0 = r24;
            r3 = r3.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = ",supportedFeatures:";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r0 = r23;
            r3 = r3.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = ",sdkVersion:";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r0 = r22;
            r3 = r3.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = ",location:";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r0 = r18;
            r3 = r3.append(r0);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = ",applicationSupportedOrientations:";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r3 = r3.append(r8);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r4 = "})";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r3 = r3.toString();	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            r2.m315e(r3);	 Catch:{ Exception -> 0x03b6, NullPointerException -> 0x03c0 }
            goto L_0x0016;
        L_0x03b6:
            r2 = move-exception;
            r2 = android.util.Log.getStackTraceString(r2);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            com.admarvel.android.util.Logging.log(r2);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            goto L_0x0016;
        L_0x03c0:
            r2 = move-exception;
            r2 = android.util.Log.getStackTraceString(r2);
            com.admarvel.android.util.Logging.log(r2);
            goto L_0x0016;
        L_0x03ca:
            r7 = 2;
            if (r4 != r7) goto L_0x0486;
        L_0x03cd:
            r4 = 90;
            r20 = r4;
            goto L_0x0035;
        L_0x03d3:
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r7 = "window";
            r4 = r4.getSystemService(r7);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = (android.view.WindowManager) r4;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = com.admarvel.android.ads.AdMarvelWebView.AdMarvelWebView.m441a(r4);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r3.f890v = r4;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r4 = r3.f890v;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            if (r4 != 0) goto L_0x03ee;
        L_0x03e9:
            r4 = 0;
            r20 = r4;
            goto L_0x0035;
        L_0x03ee:
            r7 = 1;
            if (r4 != r7) goto L_0x03f7;
        L_0x03f1:
            r4 = 90;
            r20 = r4;
            goto L_0x0035;
        L_0x03f7:
            r7 = 2;
            if (r4 != r7) goto L_0x0400;
        L_0x03fa:
            r4 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
            r20 = r4;
            goto L_0x0035;
        L_0x0400:
            r7 = 3;
            if (r4 != r7) goto L_0x0486;
        L_0x0403:
            r4 = -90;
            r20 = r4;
            goto L_0x0035;
        L_0x0409:
            r4 = "NO";
            r19 = r4;
            goto L_0x0057;
        L_0x040f:
            r4 = "null";
            r18 = r4;
            goto L_0x00a8;
        L_0x0415:
            r4 = 0;
            goto L_0x00e2;
        L_0x0418:
            r4 = 0;
            goto L_0x0151;
        L_0x041b:
            r5 = move-exception;
            r5 = 0;
            r6 = 0;
            r7[r5] = r6;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = 1;
            r6 = 0;
            r7[r5] = r6;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            goto L_0x018e;
        L_0x0426:
            r2 = move-exception;
            r2 = android.util.Log.getStackTraceString(r2);
            com.admarvel.android.util.Logging.log(r2);
            goto L_0x0016;
        L_0x0430:
            r6 = r5.getWindow();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r8 = 16908290; // 0x1020002 float:2.3877235E-38 double:8.353805E-317;
            r6 = r6.findViewById(r8);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = (android.view.ViewGroup) r6;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r8 = new android.util.DisplayMetrics;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r8.<init>();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r5.getWindowManager();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r5.getDefaultDisplay();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5.getMetrics(r8);	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r8.heightPixels;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r6 = r6.getMeasuredHeight();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = r5 - r6;
            if (r5 < 0) goto L_0x01aa;
        L_0x0456:
            r6 = r3.f891w;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r8 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
            if (r6 != r8) goto L_0x01aa;
        L_0x045c:
            r3.f891w = r5;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            goto L_0x01aa;
        L_0x0460:
            r5 = r4.getWidth();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            r5 = (float) r5;	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            goto L_0x01ec;
        L_0x0467:
            r5 = r4.getWidth();	 Catch:{ NullPointerException -> 0x03c0, Exception -> 0x0426 }
            goto L_0x01ed;
        L_0x046d:
            r4 = r5;
            r5 = r6;
            r6 = r7;
            r7 = r8;
            r8 = r9;
            goto L_0x024e;
        L_0x0474:
            r5 = r13;
            goto L_0x01aa;
        L_0x0477:
            r13 = r8;
            r14 = r9;
            r15 = r10;
            r16 = r11;
            r17 = r12;
            r12 = r7;
            r10 = r5;
            r11 = r6;
            goto L_0x01fb;
        L_0x0483:
            r4 = r5;
            goto L_0x006b;
        L_0x0486:
            r20 = r6;
            goto L_0x0035;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.admarvel.android.ads.p.g.run():void");
        }
    }

    /* renamed from: com.admarvel.android.ads.p.h */
    static class AdMarvelWebView implements Runnable {
        private final WeakReference<AdMarvelWebView> f773a;

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView) {
            this.f773a = new WeakReference(adMarvelWebView);
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f773a.get();
                if (adMarvelWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (adMarvelUniversalVideoView != null) {
                        try {
                            adMarvelUniversalVideoView.m380a();
                            adMarvelWebView.removeView(adMarvelUniversalVideoView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (adMarvelWebView.f863R != null) {
                        new Handler(Looper.getMainLooper()).removeCallbacks(adMarvelWebView.f863R);
                        adMarvelWebView.f863R = null;
                    }
                    AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "INTERNAL");
                    if (adMarvelInternalWebView != null) {
                        adMarvelInternalWebView.m318h();
                        adMarvelInternalWebView.f601j = null;
                        adMarvelInternalWebView.invalidate();
                        adMarvelInternalWebView.requestLayout();
                    }
                    adMarvelWebView.removeView(adMarvelInternalWebView);
                    adMarvelWebView.forceLayout();
                    adMarvelWebView.requestLayout();
                    adMarvelWebView.invalidate();
                    adMarvelWebView.addView(adMarvelInternalWebView);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) adMarvelWebView.getLayoutParams();
                    layoutParams.height = -2;
                    adMarvelWebView.setLayoutParams(layoutParams);
                    adMarvelWebView.forceLayout();
                    adMarvelWebView.requestLayout();
                    adMarvelWebView.invalidate();
                    adMarvelWebView.requestFocus();
                    this.f773a.clear();
                }
            } catch (Throwable e2) {
                Logging.log(Log.getStackTraceString(e2));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.i */
    static class AdMarvelWebView extends LinearLayout {
        private final WeakReference<AdMarvelWebView> f776a;

        /* renamed from: com.admarvel.android.ads.p.i.1 */
        class AdMarvelWebView implements OnClickListener {
            final /* synthetic */ AdMarvelWebView f774a;
            final /* synthetic */ AdMarvelWebView f775b;

            AdMarvelWebView(AdMarvelWebView adMarvelWebView, AdMarvelWebView adMarvelWebView2) {
                this.f775b = adMarvelWebView;
                this.f774a = adMarvelWebView2;
            }

            public void onClick(View v) {
                new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(this.f774a));
            }
        }

        public AdMarvelWebView(Context context, AdMarvelWebView adMarvelWebView) {
            super(context);
            this.f776a = new WeakReference(adMarvelWebView);
            m435a(context);
        }

        private void m435a(Context context) {
            setBackgroundColor(0);
            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.m176a(50.0f, context), Utils.m176a(50.0f, context));
            layoutParams.weight = 1.0f;
            layoutParams.width = 0;
            layoutParams.gravity = 17;
            setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams2.weight = 25.0f;
            layoutParams2.gravity = 17;
            float applyDimension = TypedValue.applyDimension(1, 36.0f, getResources().getDisplayMetrics());
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams((int) applyDimension, (int) applyDimension);
            layoutParams3.addRule(13);
            m436a(context, layoutParams3, layoutParams);
        }

        private void m436a(Context context, RelativeLayout.LayoutParams layoutParams, LinearLayout.LayoutParams layoutParams2) {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f776a.get();
            if (adMarvelWebView != null) {
                View imageView = new ImageView(context);
                imageView.setLayoutParams(layoutParams);
                imageView.setTag("BTN_CLOSE_IMAGE");
                if (adMarvelWebView.f876h) {
                    imageView.setBackgroundColor(0);
                } else {
                    AdMarvelBitmapDrawableUtils.getBitMapDrawable("close", context, imageView);
                }
                View relativeLayout = new RelativeLayout(context);
                relativeLayout.setLayoutParams(layoutParams2);
                relativeLayout.addView(imageView);
                relativeLayout.setOnClickListener(new AdMarvelWebView(this, adMarvelWebView));
                addView(relativeLayout);
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.j */
    static class AdMarvelWebView implements Runnable {
        private final WeakReference<AdMarvelWebView> f777a;

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView) {
            this.f777a = new WeakReference(adMarvelWebView);
        }

        public void run() {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f777a.get();
            if (adMarvelWebView != null) {
                adMarvelWebView.m482e();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.k */
    private static class AdMarvelWebView implements Runnable {
        private final AdMarvelAd f778a;
        private final Context f779b;

        public AdMarvelWebView(AdMarvelAd adMarvelAd, Context context) {
            this.f778a = adMarvelAd;
            this.f779b = context;
        }

        public void run() {
            AdHistoryDumpUtils b = AdHistoryDumpUtils.m550b(this.f779b);
            if (b != null) {
                int adHistoryCounter = this.f778a.getAdHistoryCounter();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("/sse_");
                stringBuilder.append(adHistoryCounter);
                stringBuilder.append(".jpg");
                b.m557a(stringBuilder.toString());
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.l */
    class AdMarvelWebView extends BroadcastReceiver {
        final /* synthetic */ AdMarvelWebView f780a;

        AdMarvelWebView(AdMarvelWebView adMarvelWebView) {
            this.f780a = adMarvelWebView;
        }

        public void onReceive(Context context, Intent intent) {
            String str = null;
            try {
                str = intent.getExtras().getString("GUID");
            } catch (Exception e) {
            }
            this.f780a.m485f(str);
            this.f780a.m478a(this, context);
        }
    }

    /* renamed from: com.admarvel.android.ads.p.m */
    static class AdMarvelWebView extends FrameLayout {
        Bitmap f781a;
        Canvas f782b;
        long f783c;
        boolean f784d;
        boolean f785e;

        public AdMarvelWebView(Context context, boolean z, boolean z2) {
            super(context);
            this.f783c = 0;
            this.f784d = false;
            this.f785e = false;
            this.f784d = z;
            this.f785e = z2;
        }

        private boolean m437a(MotionEvent motionEvent) {
            try {
                if (!this.f784d) {
                    return false;
                }
                m438b();
                return this.f781a != null && !this.f781a.isRecycled() && motionEvent != null && motionEvent.getX() >= 0.0f && motionEvent.getX() < ((float) this.f781a.getWidth()) && motionEvent.getY() >= 0.0f && motionEvent.getY() < ((float) this.f781a.getHeight()) && Color.alpha(this.f781a.getPixel((int) motionEvent.getX(), (int) motionEvent.getY())) <= 0;
            } catch (Throwable e) {
                Log.e("AdMarvel hover extenstion ", "Error while checking event target transparency ", e);
                return false;
            }
        }

        private void m438b() {
            long time = new Date().getTime();
            if (time - this.f783c > 500 && getWidth() > 0 && getHeight() > 0) {
                if (!(this.f781a != null && this.f781a.getWidth() == getWidth() && this.f781a.getHeight() == getHeight())) {
                    if (this.f781a != null) {
                        this.f781a.recycle();
                    }
                    this.f781a = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
                    this.f782b = new Canvas(this.f781a);
                }
                this.f782b.drawColor(0, Mode.CLEAR);
                draw(this.f782b);
                this.f783c = time;
            }
        }

        public void m439a() {
            if (this.f781a != null) {
                this.f781a.recycle();
            }
        }

        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return m437a(ev);
        }

        public boolean onTouchEvent(MotionEvent event) {
            return !this.f785e ? true : super.onTouchEvent(event);
        }
    }

    /* renamed from: com.admarvel.android.ads.p.n */
    static class AdMarvelWebView implements Runnable {
        private int f786a;
        private int f787b;
        private int f788c;
        private int f789d;
        private final WeakReference<AdMarvelWebView> f790e;
        private final WeakReference<Activity> f791f;

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView, Activity activity, int i, int i2) {
            this.f786a = 0;
            this.f787b = 0;
            this.f788c = 0;
            this.f789d = 0;
            this.f790e = new WeakReference(adMarvelWebView);
            this.f791f = new WeakReference(activity);
            this.f786a = i;
            this.f787b = i2;
        }

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView, Activity activity, int i, int i2, int i3, int i4) {
            this.f786a = 0;
            this.f787b = 0;
            this.f788c = 0;
            this.f789d = 0;
            this.f790e = new WeakReference(adMarvelWebView);
            this.f791f = new WeakReference(activity);
            this.f786a = i3;
            this.f787b = i4;
            this.f788c = i;
            this.f789d = i2;
        }

        public void run() {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f790e.get();
            if (adMarvelWebView != null) {
                Activity activity = (Activity) this.f791f.get();
                if (activity != null) {
                    ViewGroup viewGroup = (ViewGroup) activity.getWindow().findViewById(16908290);
                    AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) viewGroup.findViewWithTag(adMarvelWebView.f888t + "INTERNAL");
                    if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && adMarvelWebView.f893y) {
                        RelativeLayout relativeLayout = (RelativeLayout) viewGroup.findViewWithTag(adMarvelWebView.f888t + "EXPAND_LAYOUT");
                        if (relativeLayout != null) {
                            AdMarvelWebView adMarvelWebView2 = (AdMarvelWebView) viewGroup.findViewWithTag(adMarvelWebView.f888t + "EXPAND_BG");
                            if (adMarvelWebView2 != null) {
                                adMarvelWebView2.setFocusableInTouchMode(true);
                                adMarvelWebView2.requestFocus();
                                LayoutParams layoutParams = (LayoutParams) relativeLayout.getLayoutParams();
                                if (layoutParams != null) {
                                    layoutParams.width = this.f786a;
                                    layoutParams.height = this.f787b;
                                    layoutParams.leftMargin = this.f788c;
                                    layoutParams.topMargin = this.f789d;
                                    if (this.f788c != 0) {
                                        layoutParams.gravity = 0;
                                    }
                                }
                                adMarvelInternalWebView.m307a(this.f788c, this.f789d, this.f786a, this.f787b);
                                if (adMarvelWebView.f874f) {
                                    LinearLayout linearLayout = (LinearLayout) viewGroup.findViewWithTag(adMarvelWebView.f888t + "BTN_CLOSE");
                                    if (linearLayout != null) {
                                        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                                        int measuredHeight = viewGroup.getMeasuredHeight();
                                        AdMarvelWebView.m460b(linearLayout, layoutParams2, adMarvelWebView.f878j, this.f788c, this.f789d, this.f786a, this.f787b, viewGroup.getMeasuredWidth(), measuredHeight, (int) TypedValue.applyDimension(1, BitmapDescriptorFactory.HUE_ORANGE, adMarvelWebView.getContext().getResources().getDisplayMetrics()));
                                        linearLayout.removeAllViews();
                                        linearLayout.addView(new AdMarvelWebView(adMarvelWebView.getContext(), adMarvelWebView));
                                    } else {
                                        return;
                                    }
                                }
                                adMarvelWebView.ao = true;
                                viewGroup.invalidate();
                                viewGroup.requestLayout();
                                adMarvelWebView.f847B = false;
                                adMarvelWebView.f869a.set(true);
                                adMarvelWebView.invalidate();
                                adMarvelWebView.requestLayout();
                                if (AdMarvelWebView.m451a(adMarvelWebView.f888t) != null) {
                                    AdMarvelWebView.m451a(adMarvelWebView.f888t).m151a();
                                }
                                adMarvelWebView.invalidate();
                                adMarvelWebView.requestLayout();
                            }
                        }
                    }
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.o */
    static class AdMarvelWebView implements Runnable {
        private int f792a;
        private int f793b;
        private int f794c;
        private int f795d;
        private final WeakReference<AdMarvelWebView> f796e;
        private final WeakReference<Activity> f797f;

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView, Activity activity, int i, int i2) {
            this.f792a = 0;
            this.f793b = 0;
            this.f794c = 0;
            this.f795d = 0;
            this.f796e = new WeakReference(adMarvelWebView);
            this.f797f = new WeakReference(activity);
            this.f792a = i;
            this.f793b = i2;
        }

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView, Activity activity, int i, int i2, int i3, int i4) {
            this.f792a = 0;
            this.f793b = 0;
            this.f794c = 0;
            this.f795d = 0;
            this.f796e = new WeakReference(adMarvelWebView);
            this.f797f = new WeakReference(activity);
            this.f792a = i3;
            this.f793b = i4;
            this.f794c = i;
            this.f795d = i2;
        }

        public void run() {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f796e.get();
            if (adMarvelWebView != null) {
                Activity activity = (Activity) this.f797f.get();
                if (activity != null && adMarvelWebView.f893y && (activity instanceof AdMarvelMediationActivity)) {
                    new Handler(Looper.getMainLooper()).post(new C0175c(this.f794c, this.f795d, this.f792a, this.f793b, adMarvelWebView.f878j, adMarvelWebView.f874f, adMarvelWebView.f876h, adMarvelWebView.f884p, (AdMarvelMediationActivity) activity));
                    adMarvelWebView.ao = true;
                    adMarvelWebView.f869a.set(true);
                    adMarvelWebView.f847B = true;
                    adMarvelWebView.invalidate();
                    adMarvelWebView.requestLayout();
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.p */
    static class AdMarvelWebView implements Runnable {
        private int f800a;
        private int f801b;
        private int f802c;
        private int f803d;
        private final WeakReference<AdMarvelWebView> f804e;
        private final WeakReference<Activity> f805f;
        private final AdMarvelAd f806g;

        /* renamed from: com.admarvel.android.ads.p.p.1 */
        class AdMarvelWebView implements OnKeyListener {
            final /* synthetic */ AdMarvelWebView f798a;
            final /* synthetic */ AdMarvelWebView f799b;

            AdMarvelWebView(AdMarvelWebView adMarvelWebView, AdMarvelWebView adMarvelWebView2) {
                this.f799b = adMarvelWebView;
                this.f798a = adMarvelWebView2;
            }

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != 0 || keyCode != 4) {
                    return false;
                }
                this.f798a.m482e();
                return true;
            }
        }

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView, Activity activity, int i, int i2, int i3, int i4, AdMarvelAd adMarvelAd) {
            this.f800a = 0;
            this.f801b = 0;
            this.f802c = 0;
            this.f803d = 0;
            this.f804e = new WeakReference(adMarvelWebView);
            this.f805f = new WeakReference(activity);
            this.f800a = i3;
            this.f801b = i4;
            this.f802c = i;
            this.f803d = i2;
            this.f806g = adMarvelAd;
        }

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView, Activity activity, int i, int i2, AdMarvelAd adMarvelAd) {
            this.f800a = 0;
            this.f801b = 0;
            this.f802c = 0;
            this.f803d = 0;
            this.f804e = new WeakReference(adMarvelWebView);
            this.f805f = new WeakReference(activity);
            this.f800a = i;
            this.f801b = i2;
            this.f806g = adMarvelAd;
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f804e.get();
                if (adMarvelWebView != null) {
                    Context context = (Activity) this.f805f.get();
                    if (context != null) {
                        ViewGroup viewGroup = (ViewGroup) context.getWindow().findViewById(16908290);
                        View view = (AdMarvelInternalWebView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "INTERNAL");
                        if (view != null && !view.m312b()) {
                            view.f597f = true;
                            View adMarvelWebView2 = new AdMarvelWebView(context, this.f806g.isHoverAd(), this.f806g.isAppInteractionAllowedForExpandableAds());
                            adMarvelWebView2.setTag(adMarvelWebView.f888t + "EXPAND_BG");
                            adMarvelWebView2.setFocusableInTouchMode(true);
                            adMarvelWebView2.requestFocus();
                            adMarvelWebView2.setOnKeyListener(new AdMarvelWebView(this, adMarvelWebView));
                            ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -1);
                            View relativeLayout = new RelativeLayout(adMarvelWebView.getContext());
                            relativeLayout.setTag(adMarvelWebView.f888t + "EXPAND_LAYOUT");
                            ViewGroup.LayoutParams layoutParams2 = new LayoutParams(this.f800a, this.f801b);
                            if (this.f802c == 0 && adMarvelWebView.f846A) {
                                layoutParams2.gravity = 1;
                            } else if (Version.getAndroidSDKVersion() < 11) {
                                layoutParams2.gravity = 48;
                            }
                            relativeLayout.setGravity(1);
                            layoutParams2.leftMargin = this.f802c;
                            layoutParams2.topMargin = this.f803d;
                            int childCount = adMarvelWebView.getChildCount();
                            int i = 0;
                            while (i < childCount && adMarvelWebView.getChildAt(i) != view) {
                                i++;
                            }
                            View frameLayout = new FrameLayout(adMarvelWebView.getContext());
                            frameLayout.setTag(adMarvelWebView.f888t + "EXPAND_PLACE_HOLDER");
                            adMarvelWebView.addView(frameLayout, i, new RelativeLayout.LayoutParams(view.getWidth(), view.getHeight()));
                            adMarvelWebView.removeView(view);
                            if (view != null) {
                                view.m307a(this.f802c, this.f803d, this.f800a, this.f801b);
                            }
                            relativeLayout.addView(view);
                            adMarvelWebView.f893y = true;
                            adMarvelWebView2.addView(relativeLayout, layoutParams2);
                            viewGroup.addView(adMarvelWebView2, layoutParams);
                            relativeLayout.bringToFront();
                            if (adMarvelWebView.f875g && !adMarvelWebView.ao) {
                                View linearLayout = new LinearLayout(adMarvelWebView.getContext());
                                linearLayout.setBackgroundColor(0);
                                linearLayout.setTag(adMarvelWebView.f888t + "BTN_CLOSE");
                                RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(Utils.m176a(50.0f, adMarvelWebView.getContext()), Utils.m176a(50.0f, adMarvelWebView.getContext()));
                                int measuredHeight = viewGroup.getMeasuredHeight();
                                AdMarvelWebView.m460b(linearLayout, layoutParams3, adMarvelWebView.f878j, this.f802c, this.f803d, this.f800a, this.f801b, viewGroup.getMeasuredWidth(), measuredHeight, (int) TypedValue.applyDimension(1, BitmapDescriptorFactory.HUE_ORANGE, adMarvelWebView.getContext().getResources().getDisplayMetrics()));
                                linearLayout.addView(new AdMarvelWebView(adMarvelWebView.getContext(), adMarvelWebView));
                                relativeLayout.addView(linearLayout);
                                adMarvelWebView.f874f = true;
                                adMarvelWebView.f879k = new aa(adMarvelWebView, context, 3);
                                if (adMarvelWebView.f879k.canDetectOrientation()) {
                                    adMarvelWebView.f879k.enable();
                                }
                            }
                            viewGroup.invalidate();
                            viewGroup.requestLayout();
                            adMarvelWebView.f869a.set(true);
                            adMarvelWebView.f847B = false;
                            adMarvelWebView.invalidate();
                            adMarvelWebView.requestLayout();
                            if (AdMarvelWebView.m451a(adMarvelWebView.f888t) != null) {
                                AdMarvelWebView.m451a(adMarvelWebView.f888t).m151a();
                            }
                            adMarvelWebView.invalidate();
                            adMarvelWebView.requestLayout();
                            view.f597f = false;
                            if (AdMarvelUtils.isLogDumpEnabled()) {
                                new Handler().postDelayed(new AdMarvelWebView(this.f806g, context), 1000);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.q */
    static class AdMarvelWebView implements Runnable {
        private String f807a;
        private final WeakReference<AdMarvelWebView> f808b;
        private final AdMarvelAd f809c;

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView, String str, AdMarvelAd adMarvelAd) {
            this.f807a = null;
            this.f808b = new WeakReference(adMarvelWebView);
            this.f807a = str;
            this.f809c = adMarvelAd;
        }

        public void run() {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f808b.get();
            if (adMarvelWebView != null) {
                if (this.f807a == null || !URLUtil.isNetworkUrl(this.f807a) || Utils.m220l(adMarvelWebView.getContext())) {
                    AdMarvelInternalWebView.m278a(adMarvelWebView.f888t, (AdMarvelTwoPartPrivateListener) adMarvelWebView.aq.get());
                    Intent intent = adMarvelWebView.f867V ? new Intent(adMarvelWebView.getContext(), AdMarvelPostitialActivity.class) : new Intent(adMarvelWebView.getContext(), AdMarvelActivity.class);
                    intent.addFlags(268435456);
                    if (adMarvelWebView.f867V) {
                        intent.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                    }
                    intent.putExtra("expand_url", this.f807a);
                    intent.putExtra("closeBtnEnabled", adMarvelWebView.f877i);
                    intent.putExtra("closeAreaEnabled", adMarvelWebView.f876h);
                    if (adMarvelWebView.f892x) {
                        intent.putExtra("orientationState", adMarvelWebView.f883o);
                    }
                    intent.putExtra("isInterstitial", false);
                    intent.putExtra("isInterstitialClick", false);
                    intent.putExtra(locationTracking.source, adMarvelWebView.ak);
                    intent.putExtra("GUID", adMarvelWebView.f888t);
                    this.f809c.removeNonStringEntriesTargetParam();
                    try {
                        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(this.f809c);
                        objectOutputStream.close();
                        intent.putExtra("serialized_admarvelad", byteArrayOutputStream.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adMarvelWebView.getContext().startActivity(intent);
                    if (AdMarvelWebView.m451a(adMarvelWebView.f888t) != null) {
                        AdMarvelWebView.m451a(adMarvelWebView.f888t).m151a();
                    }
                    new Utils(adMarvelWebView.getContext()).m247c(adMarvelWebView.ap);
                    adMarvelWebView.f847B = false;
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.r */
    static class AdMarvelWebView implements Runnable {
        private int f810a;
        private int f811b;
        private int f812c;
        private int f813d;
        private final WeakReference<AdMarvelWebView> f814e;
        private final WeakReference<Activity> f815f;
        private final AdMarvelAd f816g;

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView, Activity activity, int i, int i2, int i3, int i4, AdMarvelAd adMarvelAd) {
            this.f810a = 0;
            this.f811b = 0;
            this.f812c = 0;
            this.f813d = 0;
            this.f814e = new WeakReference(adMarvelWebView);
            this.f815f = new WeakReference(activity);
            this.f810a = i3;
            this.f811b = i4;
            this.f812c = i;
            this.f813d = i2;
            this.f816g = adMarvelAd;
        }

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView, Activity activity, int i, int i2, AdMarvelAd adMarvelAd) {
            this.f810a = 0;
            this.f811b = 0;
            this.f812c = 0;
            this.f813d = 0;
            this.f814e = new WeakReference(adMarvelWebView);
            this.f815f = new WeakReference(activity);
            this.f810a = i;
            this.f811b = i2;
            this.f816g = adMarvelAd;
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f814e.get();
                if (adMarvelWebView != null) {
                    Activity activity = (Activity) this.f815f.get();
                    if (activity != null) {
                        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "INTERNAL");
                        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                            AdMarvelWebView.m456a(adMarvelWebView.f888t, adMarvelInternalWebView);
                            Intent intent = new Intent(activity, AdMarvelMediationActivity.class);
                            intent.putExtra(SettingsJsonConstants.ICON_WIDTH_KEY, this.f810a);
                            intent.putExtra(SettingsJsonConstants.ICON_HEIGHT_KEY, this.f811b);
                            intent.putExtra("x", this.f812c);
                            intent.putExtra("y", this.f813d);
                            intent.putExtra("GUID", adMarvelWebView.f888t);
                            intent.putExtra("expandAdType", "expand");
                            intent.putExtra("allowCenteringOfExpandedAd", adMarvelWebView.f846A);
                            intent.putExtra("enableCloseButton", adMarvelWebView.f875g);
                            intent.putExtra("isMultiLayerExpandedState", adMarvelWebView.ao);
                            intent.putExtra("closeButtonPosition", adMarvelWebView.f878j);
                            intent.putExtra("closeAreaEnabled", adMarvelWebView.f876h);
                            if (adMarvelWebView.f892x) {
                                intent.putExtra("orientationState", adMarvelWebView.f884p);
                            }
                            this.f816g.removeNonStringEntriesTargetParam();
                            try {
                                OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                                objectOutputStream.writeObject(this.f816g);
                                objectOutputStream.close();
                                intent.putExtra("serialized_admarvelad", byteArrayOutputStream.toByteArray());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            adMarvelWebView.getClass();
                            adMarvelWebView.m477a(new AdMarvelWebView(adMarvelWebView), activity);
                            adMarvelWebView.removeView(adMarvelInternalWebView);
                            adMarvelWebView.f879k = new aa(adMarvelWebView, activity, 3);
                            if (adMarvelWebView.f879k.canDetectOrientation()) {
                                adMarvelWebView.f879k.enable();
                            }
                            if (adMarvelWebView.f875g && !adMarvelWebView.ao) {
                                adMarvelWebView.f874f = true;
                            }
                            adMarvelWebView.f869a.set(true);
                            adMarvelWebView.f893y = true;
                            adMarvelWebView.f847B = true;
                            adMarvelWebView.invalidate();
                            adMarvelWebView.requestLayout();
                            intent.addFlags(268435456);
                            activity.startActivity(intent);
                        }
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.s */
    static class AdMarvelWebView implements Runnable {
        private final WeakReference<AdMarvelWebView> f817a;

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView) {
            this.f817a = new WeakReference(adMarvelWebView);
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f817a.get();
                if (adMarvelWebView != null) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (adMarvelUniversalVideoView != null) {
                        adMarvelUniversalVideoView.m380a();
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.t */
    static class AdMarvelWebView implements Runnable {
        private final WeakReference<AdMarvelInternalWebView> f818a;
        private final WeakReference<AdMarvelWebView> f819b;
        private final String f820c;

        public AdMarvelWebView(AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelWebView adMarvelWebView, String str) {
            this.f818a = new WeakReference(adMarvelInternalWebView);
            this.f819b = new WeakReference(adMarvelWebView);
            this.f820c = str;
        }

        public void run() {
            if (((AdMarvelInternalWebView) this.f818a.get()) != null) {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f819b.get();
                if (adMarvelWebView != null && this.f820c != null && this.f820c.length() != 0) {
                    if (AdMarvelView.enableOfflineSDK) {
                        try {
                            new OfflineReflectionUtils().m537a(this.f820c, adMarvelWebView.getContext(), new Handler());
                            return;
                        } catch (Exception e) {
                            return;
                        }
                    }
                    new Utils(adMarvelWebView.getContext()).m245a(this.f820c);
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.u */
    static class AdMarvelWebView implements Runnable {
        private static int f821a;
        private final WeakReference<AdMarvelWebView> f822b;

        static {
            f821a = ExploreByTouchHelper.INVALID_ID;
        }

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView) {
            this.f822b = new WeakReference(adMarvelWebView);
        }

        public int m440a() {
            return f821a;
        }

        public void run() {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f822b.get();
            if (adMarvelWebView != null) {
                Context context = adMarvelWebView.getContext();
                if (context != null) {
                    f821a = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
                    adMarvelWebView.f890v = f821a;
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.v */
    private static class AdMarvelWebView {
        @SuppressLint({"NewApi"})
        public static int m441a(WindowManager windowManager) {
            return windowManager.getDefaultDisplay().getRotation();
        }
    }

    /* renamed from: com.admarvel.android.ads.p.w */
    static class AdMarvelWebView implements Runnable {
        private final String f823a;
        private final WeakReference<AdMarvelInternalWebView> f824b;
        private final WeakReference<AdMarvelWebView> f825c;

        public AdMarvelWebView(String str, AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelWebView adMarvelWebView) {
            this.f823a = str;
            this.f824b = new WeakReference(adMarvelInternalWebView);
            this.f825c = new WeakReference(adMarvelWebView);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r22 = this;
            r0 = r22;
            r2 = r0.f824b;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r2 = r2.get();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r2 = (com.admarvel.android.ads.AdMarvelInternalWebView) r2;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r0 = r22;
            r3 = r0.f825c;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r3 = r3.get();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r3 = (com.admarvel.android.ads.AdMarvelWebView) r3;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r3 != 0) goto L_0x0017;
        L_0x0016:
            return;
        L_0x0017:
            if (r2 == 0) goto L_0x0016;
        L_0x0019:
            r5 = 0;
            r4 = "";
            r4 = "NO";
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r17 = com.admarvel.android.ads.Utils.m181a(r4);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = -1;
            r4 = com.admarvel.android.ads.Version.getAndroidSDKVersion();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r7 = 9;
            if (r4 >= r7) goto L_0x0381;
        L_0x002f:
            r4 = r3.getResources();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.getConfiguration();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.orientation;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r7 = 1;
            if (r4 != r7) goto L_0x0378;
        L_0x003c:
            r4 = 0;
            r16 = r4;
        L_0x003f:
            r4 = "wifi";
            r0 = r17;
            r4 = r0.equals(r4);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 != 0) goto L_0x0053;
        L_0x0049:
            r4 = "mobile";
            r0 = r17;
            r4 = r0.equals(r4);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 == 0) goto L_0x03b7;
        L_0x0053:
            r4 = "YES";
            r15 = r4;
        L_0x0056:
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = "location";
            r4 = com.admarvel.android.ads.Utils.m199b(r4, r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 == 0) goto L_0x0429;
        L_0x0062:
            r4 = com.admarvel.android.util.AdMarvelLocationManager.m566a();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.m572a(r2);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
        L_0x006a:
            if (r4 == 0) goto L_0x03bc;
        L_0x006c:
            r5 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5.<init>();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = "{lat:";
            r5 = r5.append(r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = r4.getLatitude();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r5.append(r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = ", lon:";
            r5 = r5.append(r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = r4.getLongitude();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r5.append(r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = ", acc:";
            r5 = r5.append(r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.getAccuracy();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r5.append(r4);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = "}";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.toString();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r14 = r4;
        L_0x00a6:
            r18 = "2.6.0";
            r4 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4.<init>();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = "{screen: true, orientation: true, heading: ";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = "compass";
            r5 = com.admarvel.android.ads.Utils.m199b(r5, r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = ", location : ";
            r5 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = "android.permission.ACCESS_COARSE_LOCATION";
            r4 = com.admarvel.android.ads.Utils.m202c(r4, r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 != 0) goto L_0x00df;
        L_0x00d3:
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = "android.permission.ACCESS_FINE_LOCATION";
            r4 = com.admarvel.android.ads.Utils.m202c(r4, r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 == 0) goto L_0x03c1;
        L_0x00df:
            r4 = 1;
        L_0x00e0:
            r4 = r5.append(r4);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = ",shake: ";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = "accelerometer";
            r5 = com.admarvel.android.ads.Utils.m199b(r5, r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = ",tilt: ";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = "accelerometer";
            r5 = com.admarvel.android.ads.Utils.m199b(r5, r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = ", network: true, sms:";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = com.admarvel.android.ads.Utils.m230q(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = ", phone:";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = com.admarvel.android.ads.Utils.m230q(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = ", email:true,calendar:";
            r5 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = "android.permission.READ_CALENDAR";
            r4 = com.admarvel.android.ads.Utils.m202c(r4, r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 == 0) goto L_0x03c4;
        L_0x0142:
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = "android.permission.WRITE_CALENDAR";
            r4 = com.admarvel.android.ads.Utils.m202c(r4, r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 == 0) goto L_0x03c4;
        L_0x014e:
            r4 = 1;
        L_0x014f:
            r4 = r5.append(r4);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = ", camera: ";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = "camera";
            r5 = com.admarvel.android.ads.Utils.m199b(r5, r6);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = ",map:true, audio:true, video:true, 'level-1':true,'level-2': true, 'level-3':false}";
            r4 = r4.append(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r19 = r4.toString();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r8 = 0;
            r7 = 0;
            r6 = 0;
            r5 = 0;
            r20 = "Banner";
            r9 = 0;
            r4 = r2.f595d;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.get();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = (com.admarvel.android.ads.AdMarvelView) r4;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 == 0) goto L_0x0423;
        L_0x0182:
            r5 = 2;
            r8 = new int[r5];	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r2.getLocationOnScreen(r8);	 Catch:{ Exception -> 0x03c7, NullPointerException -> 0x036e }
        L_0x0188:
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r5 instanceof android.app.Activity;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r5 == 0) goto L_0x0420;
        L_0x0190:
            r5 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = (android.app.Activity) r5;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r5 == 0) goto L_0x0420;
        L_0x0198:
            r6 = r3.f891w;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
            if (r6 == r7) goto L_0x03dc;
        L_0x019e:
            r6 = r3.f891w;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r6 < 0) goto L_0x03dc;
        L_0x01a2:
            r5 = r3.f891w;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
        L_0x01a4:
            r6 = 0;
            r7 = r8[r6];	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = 1;
            r6 = r8[r6];	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = r6 - r5;
            r5 = r3.getAdMarvelAd();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r5 == 0) goto L_0x0413;
        L_0x01b1:
            r5 = r3.getAdMarvelAd();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r5.getAdMarvelViewWidth();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r8 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
            r5 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1));
            if (r5 == 0) goto L_0x040c;
        L_0x01bf:
            r5 = r3.getAdMarvelAd();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r5.getAdMarvelViewWidth();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r8 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r8 = com.admarvel.android.ads.Utils.m226o(r8);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r5 * r8;
        L_0x01d0:
            r5 = (int) r5;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
        L_0x01d1:
            r4 = r4.getHeight();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r2.f599h = r7;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r2.f600i = r6;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r10 = r4;
            r11 = r5;
            r12 = r6;
            r13 = r7;
        L_0x01dd:
            r9 = "0,90";
            r4 = r3.getRootView();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r8 = r4.getLeft();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r3.getRootView();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r7 = r4.getTop();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r3.getRootView();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = r4.getWidth();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r3.getRootView();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r4.getHeight();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4 instanceof android.app.Activity;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 == 0) goto L_0x0419;
        L_0x0207:
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = (android.app.Activity) r4;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 == 0) goto L_0x0419;
        L_0x020f:
            r8 = com.admarvel.android.ads.Utils.m180a(r4);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.getWindow();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = 16908290; // 0x1020002 float:2.3877235E-38 double:8.353805E-317;
            r4 = r4.findViewById(r5);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = (android.view.ViewGroup) r4;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r7 = r4.getLeft();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = r4.getTop();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r4.getWidth();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r4.getHeight();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
        L_0x0230:
            r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r9.<init>();	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r21 = "";
            r0 = r21;
            r9 = r9.append(r0);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r0 = r22;
            r0 = r0.f823a;	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r21 = r0;
            r0 = r21;
            r9 = r9.append(r0);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r21 = "({x:";
            r0 = r21;
            r9 = r9.append(r0);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r9 = r9.append(r13);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r13 = ",y:";
            r9 = r9.append(r13);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r9 = r9.append(r12);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r12 = ",width:";
            r9 = r9.append(r12);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r9 = r9.append(r11);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r11 = ",height:";
            r9 = r9.append(r11);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r9 = r9.append(r10);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r10 = ",appX:";
            r9 = r9.append(r10);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r7 = r9.append(r7);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r9 = ",appY:";
            r7 = r7.append(r9);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r6 = r7.append(r6);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r7 = ",appWidth:";
            r6 = r6.append(r7);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = r6.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r6 = ",appHeight:";
            r5 = r5.append(r6);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = r5.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = ",orientation:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r0 = r16;
            r4 = r4.append(r0);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = ",networkType:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = "'";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r0 = r17;
            r4 = r4.append(r0);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = "'";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = ",network:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = "'";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = r4.append(r15);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = "'";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = ",screenWidth:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = r3.getContext();	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = com.admarvel.android.ads.Utils.m222m(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r5 = ",screenHeight:";
            r4 = r4.append(r5);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r3 = r3.getContext();	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r3 = com.admarvel.android.ads.Utils.m224n(r3);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r3 = r4.append(r3);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = ",adType:";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r0 = r20;
            r3 = r3.append(r0);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = ",supportedFeatures:";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r0 = r19;
            r3 = r3.append(r0);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = ",sdkVersion:";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r0 = r18;
            r3 = r3.append(r0);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = ",location:";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r3 = r3.append(r14);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = ",applicationSupportedOrientations:";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r3 = r3.append(r8);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = "'";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r4 = "})";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r3 = r3.toString();	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            r2.m315e(r3);	 Catch:{ Exception -> 0x0364, NullPointerException -> 0x036e }
            goto L_0x0016;
        L_0x0364:
            r2 = move-exception;
            r2 = android.util.Log.getStackTraceString(r2);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            com.admarvel.android.util.Logging.log(r2);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            goto L_0x0016;
        L_0x036e:
            r2 = move-exception;
            r2 = android.util.Log.getStackTraceString(r2);
            com.admarvel.android.util.Logging.log(r2);
            goto L_0x0016;
        L_0x0378:
            r7 = 2;
            if (r4 != r7) goto L_0x042c;
        L_0x037b:
            r4 = 90;
            r16 = r4;
            goto L_0x003f;
        L_0x0381:
            r4 = r3.getContext();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r7 = "window";
            r4 = r4.getSystemService(r7);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = (android.view.WindowManager) r4;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = com.admarvel.android.ads.AdMarvelWebView.AdMarvelWebView.m441a(r4);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r3.f890v = r4;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r4 = r3.f890v;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            if (r4 != 0) goto L_0x039c;
        L_0x0397:
            r4 = 0;
            r16 = r4;
            goto L_0x003f;
        L_0x039c:
            r7 = 1;
            if (r4 != r7) goto L_0x03a5;
        L_0x039f:
            r4 = 90;
            r16 = r4;
            goto L_0x003f;
        L_0x03a5:
            r7 = 2;
            if (r4 != r7) goto L_0x03ae;
        L_0x03a8:
            r4 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
            r16 = r4;
            goto L_0x003f;
        L_0x03ae:
            r7 = 3;
            if (r4 != r7) goto L_0x042c;
        L_0x03b1:
            r4 = -90;
            r16 = r4;
            goto L_0x003f;
        L_0x03b7:
            r4 = "NO";
            r15 = r4;
            goto L_0x0056;
        L_0x03bc:
            r4 = "null";
            r14 = r4;
            goto L_0x00a6;
        L_0x03c1:
            r4 = 0;
            goto L_0x00e0;
        L_0x03c4:
            r4 = 0;
            goto L_0x014f;
        L_0x03c7:
            r5 = move-exception;
            r5 = 0;
            r6 = 0;
            r8[r5] = r6;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = 1;
            r6 = 0;
            r8[r5] = r6;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            goto L_0x0188;
        L_0x03d2:
            r2 = move-exception;
            r2 = android.util.Log.getStackTraceString(r2);
            com.admarvel.android.util.Logging.log(r2);
            goto L_0x0016;
        L_0x03dc:
            r6 = r5.getWindow();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r7 = 16908290; // 0x1020002 float:2.3877235E-38 double:8.353805E-317;
            r6 = r6.findViewById(r7);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = (android.view.ViewGroup) r6;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r7 = new android.util.DisplayMetrics;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r7.<init>();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r5.getWindowManager();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r5.getDefaultDisplay();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5.getMetrics(r7);	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r7.heightPixels;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r6 = r6.getMeasuredHeight();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = r5 - r6;
            if (r5 < 0) goto L_0x01a4;
        L_0x0402:
            r6 = r3.f891w;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
            if (r6 != r7) goto L_0x01a4;
        L_0x0408:
            r3.f891w = r5;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            goto L_0x01a4;
        L_0x040c:
            r5 = r4.getWidth();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            r5 = (float) r5;	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            goto L_0x01d0;
        L_0x0413:
            r5 = r4.getWidth();	 Catch:{ NullPointerException -> 0x036e, Exception -> 0x03d2 }
            goto L_0x01d1;
        L_0x0419:
            r4 = r5;
            r5 = r6;
            r6 = r7;
            r7 = r8;
            r8 = r9;
            goto L_0x0230;
        L_0x0420:
            r5 = r9;
            goto L_0x01a4;
        L_0x0423:
            r10 = r5;
            r11 = r6;
            r12 = r7;
            r13 = r8;
            goto L_0x01dd;
        L_0x0429:
            r4 = r5;
            goto L_0x006a;
        L_0x042c:
            r16 = r6;
            goto L_0x003f;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.admarvel.android.ads.p.w.run():void");
        }
    }

    /* renamed from: com.admarvel.android.ads.p.x */
    static class AdMarvelWebView implements Runnable {
        private final WeakReference<AdMarvelWebView> f826a;
        private final WeakReference<AdMarvelInternalWebView> f827b;
        private String f828c;

        public AdMarvelWebView(String str, AdMarvelWebView adMarvelWebView, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f828c = str;
            this.f826a = new WeakReference(adMarvelWebView);
            this.f827b = new WeakReference(adMarvelInternalWebView);
        }

        @TargetApi(14)
        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f826a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f827b.get();
                if (adMarvelWebView != null && adMarvelInternalWebView != null && this.f828c != null && this.f828c.length() > 0) {
                    View view;
                    View view2 = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (view2 != null) {
                        view2.m380a();
                    }
                    if (view2 == null) {
                        AdMarvelWebView adMarvelUniversalVideoView = new AdMarvelUniversalVideoView(adMarvelWebView.getContext(), false, this.f828c, adMarvelWebView);
                        adMarvelUniversalVideoView.setTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                        adMarvelWebView.setVisibiltyListener(adMarvelUniversalVideoView);
                        adMarvelWebView.ab = true;
                        view = adMarvelUniversalVideoView;
                    } else {
                        view = view2;
                    }
                    view.m381a(adMarvelWebView.f849D, adMarvelWebView.f850E, adMarvelWebView.f851F, adMarvelWebView.f852G);
                    float f = adMarvelWebView.getContext().getResources().getDisplayMetrics().density;
                    if (adMarvelWebView.f853H > 0) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) adMarvelWebView.getLayoutParams();
                        layoutParams.height = (int) (f * ((float) adMarvelWebView.f853H));
                        adMarvelWebView.setLayoutParams(layoutParams);
                    }
                    adMarvelWebView.removeAllViews();
                    adMarvelWebView.addView(view);
                    adMarvelWebView.addView(adMarvelInternalWebView);
                    adMarvelInternalWebView.setBackgroundColor(0);
                    adMarvelInternalWebView.setBackgroundDrawable(null);
                    adMarvelInternalWebView.setLayerType(1, null);
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.y */
    static class AdMarvelWebView implements Runnable {
        private final WeakReference<AdMarvelWebView> f841a;
        private final WeakReference<AdMarvelInternalWebView> f842b;
        private String f843c;

        /* renamed from: com.admarvel.android.ads.p.y.1 */
        class AdMarvelWebView implements OnPreparedListener {
            final /* synthetic */ AdMarvelWebView f829a;
            final /* synthetic */ AdMarvelInternalWebView f830b;
            final /* synthetic */ AdMarvelWebView f831c;

            AdMarvelWebView(AdMarvelWebView adMarvelWebView, AdMarvelWebView adMarvelWebView2, AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f831c = adMarvelWebView;
                this.f829a = adMarvelWebView2;
                this.f830b = adMarvelInternalWebView;
            }

            public void onPrepared(MediaPlayer mp) {
                Logging.log("setOnPreparedListener : onPrepared");
                if (this.f829a.f864S) {
                    mp.setVolume(0.0f, 0.0f);
                    this.f829a.f864S = false;
                }
                if (this.f829a.f854I != null && this.f829a.f854I.length() > 0) {
                    this.f830b.m315e(this.f829a.f854I + "()");
                }
                if (this.f829a.f856K != null && this.f829a.f856K.length() > 0) {
                    this.f830b.m315e(this.f829a.f856K + "()");
                }
                if (this.f829a.f863R == null) {
                    this.f829a.f863R = new ai(this.f829a, this.f830b);
                    new Handler(Looper.getMainLooper()).postDelayed(this.f829a.f863R, 500);
                }
            }
        }

        /* renamed from: com.admarvel.android.ads.p.y.2 */
        class AdMarvelWebView implements OnCompletionListener {
            final /* synthetic */ AdMarvelWebView f832a;
            final /* synthetic */ AdMarvelInternalWebView f833b;
            final /* synthetic */ AdMarvelWebView f834c;

            AdMarvelWebView(AdMarvelWebView adMarvelWebView, AdMarvelWebView adMarvelWebView2, AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f834c = adMarvelWebView;
                this.f832a = adMarvelWebView2;
                this.f833b = adMarvelInternalWebView;
            }

            public void onCompletion(MediaPlayer mp) {
                if (this.f832a.f858M != null && this.f832a.f858M.length() > 0) {
                    this.f833b.m315e(this.f832a.f858M + "()");
                }
                if (this.f832a.f863R != null) {
                    new Handler(Looper.getMainLooper()).removeCallbacks(this.f832a.f863R);
                    this.f832a.f863R = null;
                }
            }
        }

        /* renamed from: com.admarvel.android.ads.p.y.3 */
        class AdMarvelWebView implements OnErrorListener {
            final /* synthetic */ AdMarvelWebView f835a;
            final /* synthetic */ AdMarvelInternalWebView f836b;
            final /* synthetic */ AdMarvelWebView f837c;

            AdMarvelWebView(AdMarvelWebView adMarvelWebView, AdMarvelWebView adMarvelWebView2, AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f837c = adMarvelWebView;
                this.f835a = adMarvelWebView2;
                this.f836b = adMarvelInternalWebView;
            }

            public boolean onError(MediaPlayer mp, int what, int extra) {
                this.f835a.f865T = true;
                this.f836b.setLayerType(2, null);
                new Handler(Looper.getMainLooper()).post(new am(this.f835a));
                if (this.f835a.f862Q != null && this.f835a.f862Q.length() > 0) {
                    this.f836b.m315e(this.f835a.f862Q + "()");
                }
                return false;
            }
        }

        /* renamed from: com.admarvel.android.ads.p.y.4 */
        class AdMarvelWebView implements AdMarvelUniversalVideoView {
            final /* synthetic */ AdMarvelWebView f838a;
            final /* synthetic */ AdMarvelInternalWebView f839b;
            final /* synthetic */ AdMarvelWebView f840c;

            AdMarvelWebView(AdMarvelWebView adMarvelWebView, AdMarvelWebView adMarvelWebView2, AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f840c = adMarvelWebView;
                this.f838a = adMarvelWebView2;
                this.f839b = adMarvelInternalWebView;
            }

            public void m442a() {
            }

            public void m443a(AdMarvelUniversalVideoView adMarvelUniversalVideoView) {
                try {
                    Logging.log("AdMarvelVideoViewListener : onVideoViewSurfaceTextureDestroyed");
                    this.f838a.f865T = true;
                    new Handler(Looper.getMainLooper()).post(new am(this.f838a));
                    if (this.f838a.f862Q != null && this.f838a.f862Q.length() > 0) {
                        this.f839b.m315e(this.f838a.f862Q + "()");
                    }
                    adMarvelUniversalVideoView.m380a();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void m444b() {
            }

            public void m445c() {
            }

            public void m446d() {
            }

            public void m447e() {
            }

            public void m448f() {
            }

            public void m449g() {
            }
        }

        public AdMarvelWebView(String str, AdMarvelWebView adMarvelWebView, AdMarvelInternalWebView adMarvelInternalWebView) {
            this.f843c = str;
            this.f841a = new WeakReference(adMarvelWebView);
            this.f842b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f841a.get();
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f842b.get();
                if (adMarvelWebView != null && adMarvelInternalWebView != null && this.f843c != null && this.f843c.length() > 0) {
                    AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) adMarvelWebView.findViewWithTag(adMarvelWebView.f888t + "EMBEDDED_VIDEO");
                    if (adMarvelUniversalVideoView != null) {
                        adMarvelUniversalVideoView.setVideoPath(this.f843c);
                        adMarvelUniversalVideoView.setVisibility(0);
                        adMarvelUniversalVideoView.setOnPreparedListener(new AdMarvelWebView(this, adMarvelWebView, adMarvelInternalWebView));
                        adMarvelUniversalVideoView.setOnCompletionListener(new AdMarvelWebView(this, adMarvelWebView, adMarvelInternalWebView));
                        adMarvelUniversalVideoView.setOnErrorListener(new AdMarvelWebView(this, adMarvelWebView, adMarvelInternalWebView));
                        adMarvelUniversalVideoView.setListener(new AdMarvelWebView(this, adMarvelWebView, adMarvelInternalWebView));
                    }
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.p.z */
    private class AdMarvelWebView implements Runnable {
        final /* synthetic */ AdMarvelWebView f844a;
        private final String f845b;

        public AdMarvelWebView(AdMarvelWebView adMarvelWebView, String str) {
            this.f844a = adMarvelWebView;
            this.f845b = str;
        }

        public void run() {
            if (!this.f844a.av) {
                Logging.log("onPagFinished is not  called after delay" + Constants.WAIT_FOR_ON_PAGE_FINISHED);
                this.f844a.m470g(this.f845b);
                this.f844a.m482e();
                this.f844a.m481d();
            }
        }
    }

    static {
        ac = "<style>* {-webkit-tap-highlight-color: rgba(0,0,0,0.0);} body {background-color:transparent;margin:0px;padding:0px;}</style>";
        ad = "<style>* {-webkit-tap-highlight-color: rgba(0,0,0,0.0);} body {background-color:transparent;margin:0px;padding:0px;}</style><script type='text/javascript' src='http://admarvel.s3.amazonaws.com/js/admarvel_compete_v1.1.js'></script>";
        ae = "<html><head>%s</head><body><div align=\"center\">%s</div></body></html>";
        af = "<style>* {-webkit-tap-highlight-color: rgba(0,0,0,0.0);} body {background-color:transparent;margin:0;padding:0;} .bl span{display:table-cell;vertical-align:middle;height:38px;text-align:center;width:500px;} .bl {margin:2px;padding: 2px 15px;display:block;vertical-align:middle;text-align:center;line-height: 15px;font-size:12px;font-family: Helvetica;font-weight: bold;text-decoration: none;color:rgb(%d,%d,%d);text-shadow: #222222 0px 1px 2px; background-color:rgb(%d,%d,%d);background-image: url('http://admarvel.s3.amazonaws.com/btn_bg_trns.png');border: 2px rgb(%d, %d, %d) solid;-webkit-border-radius: 10px;}</style>";
        ag = "<html><head>%s</head><body><div class=\"bl\"><a href=\"%s\" style=\"text-decoration: none; color: #000;\" ><span>%s</span></a></div>";
        ah = "<meta name=\"viewport\" content=\"initial-scale=1.0,maximum-scale=1.0,target-densitydpi=device-dpi, width=device-width\" />";
        ai = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0\" />";
        al = new ConcurrentHashMap();
        am = new ConcurrentHashMap();
    }

    AdMarvelWebView(Context context, boolean z, boolean z2, String str, AdMarvelAd adMarvelAd, boolean z3, boolean z4) {
        super(context);
        this.f874f = false;
        this.f875g = false;
        this.f876h = false;
        this.f877i = false;
        this.f878j = "top-right";
        this.f882n = false;
        this.f883o = null;
        this.f884p = null;
        this.ao = false;
        this.f889u = -1;
        this.f890v = ExploreByTouchHelper.INVALID_ID;
        this.f891w = ExploreByTouchHelper.INVALID_ID;
        this.f892x = false;
        this.f893y = false;
        this.f894z = false;
        this.f846A = false;
        this.f847B = false;
        this.au = null;
        this.av = false;
        this.f848C = null;
        this.f849D = -1;
        this.f850E = -1;
        this.f851F = -1;
        this.f852G = -1;
        this.f853H = -1;
        this.f854I = null;
        this.f855J = null;
        this.f856K = null;
        this.f857L = null;
        this.f858M = null;
        this.f859N = null;
        this.f860O = null;
        this.f861P = null;
        this.f862Q = null;
        this.f863R = null;
        this.f864S = true;
        this.f865T = false;
        this.aw = false;
        this.f868W = null;
        this.aa = true;
        this.ab = false;
        this.f866U = z3;
        this.f867V = z4;
        this.ap = str;
        this.f872d = z;
        this.f873e = z2;
        this.f888t = UUID.randomUUID().toString();
        this.f869a = new AtomicBoolean(false);
        this.f870b = new AtomicBoolean(false);
        this.f885q = new AtomicBoolean(false);
        this.f886r = new AtomicBoolean(false);
        this.f887s = new AtomicBoolean(false);
        this.ar = new AdMarvelWebView(this);
        if (context instanceof Activity) {
            this.f889u = ((Activity) context).getRequestedOrientation();
        }
        this.aq = new WeakReference(this.ar);
        this.au = adMarvelAd;
    }

    public static AdMarvelWebViewListener m451a(String str) {
        return (AdMarvelWebViewListener) al.get(str);
    }

    static synchronized void m456a(String str, AdMarvelInternalWebView adMarvelInternalWebView) {
        synchronized (AdMarvelWebView.class) {
            am.put(str, adMarvelInternalWebView);
        }
    }

    public static synchronized void m457a(String str, AdMarvelWebViewListener adMarvelWebViewListener) {
        synchronized (AdMarvelWebView.class) {
            al.put(str, adMarvelWebViewListener);
        }
    }

    private static void m460b(LinearLayout linearLayout, RelativeLayout.LayoutParams layoutParams, String str, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        layoutParams.rightMargin = 0;
        layoutParams.leftMargin = 0;
        layoutParams.topMargin = 0;
        layoutParams.bottomMargin = 0;
        if (i3 == -1) {
            i3 = i5;
        }
        if (i4 == -1) {
            i4 = i6;
        }
        int i8;
        if (str == null || str.length() <= 0) {
            layoutParams.addRule(11);
            layoutParams.addRule(10);
            i8 = (i + i3) - i5;
            if (i8 > 0) {
                layoutParams.rightMargin = i8;
            } else {
                layoutParams.rightMargin = 0;
            }
            if (i2 < 0) {
                layoutParams.topMargin = Math.abs(i2);
            } else {
                layoutParams.topMargin = 0;
            }
        } else if ("top-right".equals(str)) {
            layoutParams.addRule(10);
            layoutParams.addRule(11);
            i8 = (i + i3) - i5;
            if (i == 0 && i8 > 0) {
                layoutParams.rightMargin = i8 / 2;
            } else if (i8 > 0) {
                layoutParams.rightMargin = i8;
            } else {
                layoutParams.rightMargin = 0;
            }
            if (i2 < 0) {
                layoutParams.topMargin = Math.abs(i2);
            } else {
                layoutParams.topMargin = 0;
            }
        } else if ("top-left".equals(str)) {
            layoutParams.addRule(9);
            layoutParams.addRule(10);
            if (i < 0) {
                layoutParams.leftMargin = Math.abs(i);
            } else if (i == 0) {
                i8 = (i3 - i5) / 2;
                if (i8 > 0) {
                    layoutParams.leftMargin = i8;
                }
            } else {
                layoutParams.leftMargin = 0;
            }
            if (i2 < 0) {
                layoutParams.topMargin = Math.abs(i2);
            } else {
                layoutParams.topMargin = 0;
            }
        } else if ("bottom-right".equals(str)) {
            layoutParams.addRule(11);
            layoutParams.addRule(12);
            i8 = (i + i3) - i5;
            if (i == 0 && i8 > 0) {
                layoutParams.rightMargin = i8 / 2;
            } else if (i8 > 0) {
                layoutParams.rightMargin = i8;
            } else {
                layoutParams.rightMargin = 0;
            }
            i8 = i6 - (i2 + i4);
            if (i8 < 0) {
                layoutParams.bottomMargin = Math.abs(i8);
            } else {
                layoutParams.bottomMargin = 0;
            }
        } else if ("bottom-left".equals(str)) {
            layoutParams.addRule(9);
            layoutParams.addRule(12);
            if (i < 0) {
                layoutParams.leftMargin = Math.abs(i);
            } else if (i == 0) {
                i8 = (i3 - i5) / 2;
                if (i8 > 0) {
                    layoutParams.leftMargin = i8;
                }
            } else {
                layoutParams.leftMargin = 0;
            }
            i8 = i6 - (i2 + i4);
            if (i8 < 0) {
                layoutParams.bottomMargin = Math.abs(i8);
            } else {
                layoutParams.bottomMargin = 0;
            }
        } else if ("center".equals(str)) {
            if (i + i3 <= i5) {
                if (i <= 0) {
                    layoutParams.leftMargin = (((i + i3) / 2) + Math.abs(i)) - (i7 / 2);
                } else {
                    layoutParams.leftMargin = (i3 / 2) - (i7 / 2);
                }
            } else if (i <= 0) {
                layoutParams.leftMargin = ((i5 / 2) + Math.abs(i)) - (i7 / 2);
            } else {
                layoutParams.leftMargin = ((i5 - i) / 2) - (i7 / 2);
            }
            layoutParams.addRule(15);
            if (i2 < 0) {
                i8 = (i4 / 2) + i2;
                if (i8 < 0) {
                    layoutParams.topMargin = Math.abs(i8) + (i4 / 2);
                    layoutParams.addRule(10);
                } else {
                    layoutParams.topMargin = 0;
                    layoutParams.addRule(15);
                }
            } else {
                i8 = i6 - ((i4 / 2) + i2);
                if (i8 < 0) {
                    layoutParams.bottomMargin = Math.abs(i8) + (i4 / 2);
                    layoutParams.addRule(12);
                } else {
                    layoutParams.bottomMargin = 0;
                    layoutParams.addRule(15);
                }
            }
        } else if ("bottom-center".equals(str)) {
            i8 = i6 - (i2 + i4);
            if (i8 < 0) {
                layoutParams.bottomMargin = Math.abs(i8);
            } else {
                layoutParams.bottomMargin = 0;
            }
            layoutParams.addRule(9);
            layoutParams.addRule(12);
            if (i + i3 <= i5) {
                if (i <= 0) {
                    layoutParams.leftMargin = (((i + i3) / 2) + Math.abs(i)) - (i7 / 2);
                } else {
                    layoutParams.leftMargin = (i3 / 2) - (i7 / 2);
                }
            } else if (i <= 0) {
                layoutParams.leftMargin = ((i5 / 2) + Math.abs(i)) - (i7 / 2);
            } else {
                layoutParams.leftMargin = ((i5 - i) / 2) - (i7 / 2);
            }
        } else if ("top-center".equals(str)) {
            layoutParams.addRule(14);
            if (i2 < 0) {
                layoutParams.topMargin = Math.abs(i2);
            } else {
                layoutParams.topMargin = 0;
            }
            if (i + i3 <= i5) {
                if (i <= 0) {
                    layoutParams.leftMargin = (((i + i3) / 2) + Math.abs(i)) - (i7 / 2);
                } else {
                    layoutParams.leftMargin = (i3 / 2) - (i7 / 2);
                }
            } else if (i <= 0) {
                layoutParams.leftMargin = ((i5 / 2) + Math.abs(i)) - (i7 / 2);
            } else {
                layoutParams.leftMargin = ((i5 - i) / 2) - (i7 / 2);
            }
        } else {
            layoutParams.addRule(11);
            layoutParams.addRule(10);
            i8 = (i + i3) - i5;
            if (i8 > 0) {
                layoutParams.rightMargin = i8;
            } else {
                layoutParams.rightMargin = 0;
            }
            if (i2 < 0) {
                layoutParams.topMargin = Math.abs(i2);
            } else {
                layoutParams.topMargin = 0;
            }
        }
        linearLayout.setLayoutParams(layoutParams);
    }

    public static void m461b(String str) {
        al.remove(str);
    }

    static synchronized void m464c(String str) {
        synchronized (AdMarvelWebView.class) {
            try {
                am.remove(str);
            } catch (Exception e) {
            }
        }
    }

    public static AdMarvelInternalWebView m465d(String str) {
        return (AdMarvelInternalWebView) am.get(str);
    }

    private void m470g(String str) {
        if (this.f870b.compareAndSet(true, false) && AdMarvelWebView.m451a(this.f888t) != null) {
            AdMarvelWebView.m451a(this.f888t).m154a(this, this.au, 308, Utils.m178a(308));
        }
    }

    void m474a() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) findViewWithTag(this.f888t + "INTERNAL");
        if (adMarvelInternalWebView == null && this.f893y) {
            Context context = getContext();
            if (context != null && (context instanceof Activity)) {
                adMarvelInternalWebView = (AdMarvelInternalWebView) ((ViewGroup) ((Activity) context).getWindow().findViewById(16908290)).findViewWithTag(this.f888t + "INTERNAL");
            }
        }
        if (adMarvelInternalWebView != null) {
            adMarvelInternalWebView.m316f();
        }
    }

    void m475a(int i, int i2) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i2;
        requestLayout();
    }

    void m476a(int i, int i2, int i3, int i4, AdMarvelView adMarvelView) {
        if (this.au != null) {
            if (!(this.au == null || this.au.getSource() == null)) {
                this.ak = this.au.getSource();
            }
            try {
                int n;
                int n2;
                int imageWidth;
                int imageHeight;
                String str;
                if (this.au.getAdType().equals(AdType.IMAGE) && this.au.hasImage()) {
                    if (this.au.getImageWidth() <= 0 || this.au.getImageHeight() <= 0) {
                        this.aj = String.format(ae, new Object[]{ac + ah, this.au.getXhtml()});
                    } else if (Version.getAndroidSDKVersion() < 7) {
                        n = Utils.m216j(getContext()) == 2 ? Utils.m224n(getContext()) : Utils.m222m(getContext());
                        n2 = Utils.m216j(getContext()) == 1 ? Utils.m224n(getContext()) : Utils.m222m(getContext());
                        if (this.f872d) {
                            imageWidth = (int) (((float) this.au.getImageWidth()) * Utils.m175a(getContext(), n, this.au.getImageWidth()));
                            imageHeight = (int) (((float) this.au.getImageHeight()) * Utils.m175a(getContext(), n, this.au.getImageWidth()));
                        } else {
                            imageWidth = this.au.getImageWidth();
                            imageHeight = this.au.getImageHeight();
                        }
                        str = "<a href=\"" + this.au.getClickURL() + "\"><img src=\"" + this.au.getImageURL() + "\" width=\"" + imageWidth + "\" height=\"" + Math.min(imageHeight, n2) + "\" /></a>";
                        this.aj = String.format(ae, new Object[]{ac + ah, str});
                    } else if (!AdMarvelUtils.isTabletDevice(getContext()) || this.f873e) {
                        if (adMarvelView.getAdContainerWidth() > 0) {
                            r2 = ((float) adMarvelView.getAdContainerWidth()) / Utils.m226o(getContext());
                        } else if (adMarvelView.getWidth() > 0) {
                            r2 = ((float) adMarvelView.getWidth()) / Utils.m226o(getContext());
                        } else {
                            r2 = ((float) (Utils.m222m(getContext()) < Utils.m224n(getContext()) ? Utils.m222m(getContext()) : Utils.m224n(getContext()))) / Utils.m226o(getContext());
                        }
                        str = "<a href=\"" + this.au.getClickURL() + "\"><img src=\"" + this.au.getImageURL() + "\" width=\"" + r2 + "\"\" /></a>";
                        this.aj = String.format(ae, new Object[]{ac + ai, str});
                    } else {
                        float imageWidth2 = (float) this.au.getImageWidth();
                        r2 = (float) this.au.getImageHeight();
                        if (Version.getAndroidSDKVersion() >= 19) {
                            float m = ((float) (Utils.m222m(getContext()) < Utils.m224n(getContext()) ? Utils.m222m(getContext()) : Utils.m224n(getContext()))) / Utils.m226o(getContext());
                            Logging.log("Device Relative Screen Width :" + m);
                            if (imageWidth2 > m) {
                                m /= imageWidth2;
                                Logging.log("Device Image Ad Scaling factpr :" + m);
                                imageWidth2 *= m;
                                r2 *= m;
                            }
                        }
                        str = "<a href=\"" + this.au.getClickURL() + "\"><img src=\"" + this.au.getImageURL() + "\" width=\"" + imageWidth2 + "\" height=\"" + r2 + "\" /></a>";
                        this.aj = String.format(ae, new Object[]{ac + ah, str});
                    }
                } else if (this.au.getAdType().equals(AdType.TEXT) && this.au.getText() != null && this.au.getText().length() > 0) {
                    imageHeight = (i >> 16) & Type.ANY;
                    imageWidth = (i >> 8) & Type.ANY;
                    n2 = i & Type.ANY;
                    n = (i3 >> 16) & Type.ANY;
                    int i5 = (i3 >> 8) & Type.ANY;
                    int i6 = i3 & Type.ANY;
                    int i7 = (i2 >> 16) & Type.ANY;
                    int i8 = (i2 >> 8) & Type.ANY;
                    int i9 = i2 & Type.ANY;
                    str = String.format(af, new Object[]{Integer.valueOf(imageHeight), Integer.valueOf(imageWidth), Integer.valueOf(n2), Integer.valueOf(n), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8), Integer.valueOf(i9)});
                    this.aj = String.format(ag, new Object[]{str, this.au.getClickURL(), this.au.getText()});
                } else if (this.au.getXhtml().contains("ORMMA_API")) {
                    this.aj = String.format(ae, new Object[]{ad, this.au.getXHTML()});
                } else {
                    this.aj = String.format(ae, new Object[]{ac, this.au.getXHTML() + Utils.f493a});
                }
                if (Version.getAndroidSDKVersion() == 19 || Version.getAndroidSDKVersion() == 20) {
                    Utils.m240v(getContext().getApplicationContext());
                }
                new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(this, adMarvelView, i4));
                this.f870b.set(true);
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                if (AdMarvelWebView.m451a(this.f888t) != null) {
                    AdMarvelWebView.m451a(this.f888t).m154a(this, this.au, 305, Utils.m178a(305));
                }
            }
        } else if (AdMarvelWebView.m451a(this.f888t) != null) {
            AdMarvelWebView.m451a(this.f888t).m154a(this, this.au, 305, Utils.m178a(305));
        }
    }

    void m477a(AdMarvelWebView adMarvelWebView, Activity activity) {
        if (activity != null && adMarvelWebView != null) {
            activity.getApplicationContext().registerReceiver(adMarvelWebView, new IntentFilter("com.admarvel.expandadclose"));
            this.aw = true;
        }
    }

    void m478a(AdMarvelWebView adMarvelWebView, Context context) {
        if (context != null && adMarvelWebView != null && this.aw) {
            try {
                context.getApplicationContext().unregisterReceiver(adMarvelWebView);
            } catch (IllegalArgumentException e) {
            }
            this.aw = false;
        }
    }

    void m479b() {
        WebView webView = (AdMarvelInternalWebView) findViewWithTag(this.f888t + "INTERNAL");
        if (webView == null && this.f893y) {
            Context context = getContext();
            if (context != null && (context instanceof Activity)) {
                ViewGroup viewGroup = (ViewGroup) ((Activity) context).getWindow().findViewById(16908290);
                WebView webView2 = (AdMarvelInternalWebView) viewGroup.findViewWithTag(this.f888t + "INTERNAL");
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) viewGroup.findViewWithTag(this.f888t + "EXPAND_BG");
                if (adMarvelWebView != null) {
                    adMarvelWebView.setFocusableInTouchMode(true);
                    adMarvelWebView.requestFocus();
                    adMarvelWebView.setOnKeyListener(new AdMarvelWebView(this));
                }
                webView = webView2;
            }
        }
        if (webView != null) {
            webView.m317g();
            if (webView instanceof AdMarvelInternalWebView) {
                webView.m310a(this);
            }
            if (Version.getAndroidSDKVersion() >= 11) {
                ac.m254a(webView);
            } else {
                ad.m256a(webView);
            }
        }
    }

    public void m480c() {
    }

    void m481d() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) findViewWithTag(this.f888t + "INTERNAL");
        if (adMarvelInternalWebView == null && this.f893y) {
            Context context = getContext();
            if (context != null && (context instanceof Activity)) {
                adMarvelInternalWebView = (AdMarvelInternalWebView) ((ViewGroup) ((Activity) context).getWindow().findViewById(16908290)).findViewWithTag(this.f888t + "INTERNAL");
            }
        }
        AdMarvelInternalWebView.m283b(this.f888t);
        AdMarvelInternalWebView.m276a(this.f888t);
        AdMarvelWebView.m461b(this.f888t);
        if (adMarvelInternalWebView != null) {
            if (Version.getAndroidSDKVersion() >= 14) {
                new Handler(Looper.getMainLooper()).post(new AdMarvelWebView(this));
            }
            if (Version.getAndroidSDKVersion() >= 15) {
                adMarvelInternalWebView.setWebChromeClient(null);
            }
            adMarvelInternalWebView.setWebViewClient(null);
            adMarvelInternalWebView.m306a();
            adMarvelInternalWebView.loadUrl("about:blank");
            if (this.at != null) {
                this.at.clear();
            }
            this.au = null;
            if (this.ar != null) {
                this.ar.m434b();
                this.ar = null;
                this.aq = null;
            }
        }
    }

    void m482e() {
        int i = 0;
        try {
            if (this.f879k != null) {
                this.f879k.disable();
            }
            Context context = getContext();
            Activity activity = context instanceof Activity ? (Activity) context : null;
            if (this.f892x && this.f894z && !this.f882n && activity != null) {
                activity.setRequestedOrientation(this.f889u);
                this.f892x = false;
            }
            if (this.f869a.compareAndSet(true, false) && activity != null) {
                ViewGroup viewGroup = (ViewGroup) activity.getWindow().findViewById(16908290);
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) viewGroup.findViewWithTag(this.f888t + "INTERNAL");
                if (adMarvelInternalWebView != null) {
                    adMarvelInternalWebView.f598g = true;
                }
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) viewGroup.findViewWithTag(this.f888t + "EXPAND_BG");
                RelativeLayout relativeLayout = (RelativeLayout) viewGroup.findViewWithTag(this.f888t + "EXPAND_LAYOUT");
                if (this.f874f) {
                    LinearLayout linearLayout = (LinearLayout) viewGroup.findViewWithTag(this.f888t + "BTN_CLOSE");
                    if (linearLayout != null) {
                        relativeLayout.removeView(linearLayout);
                    }
                    this.f874f = false;
                }
                if (!(adMarvelWebView == null || adMarvelInternalWebView == null)) {
                    View view = (FrameLayout) findViewWithTag(this.f888t + "EXPAND_PLACE_HOLDER");
                    int childCount = getChildCount();
                    while (i < childCount && getChildAt(i) != view) {
                        i++;
                    }
                    relativeLayout.removeView(adMarvelInternalWebView);
                    adMarvelWebView.removeView(relativeLayout);
                    adMarvelWebView.m439a();
                    viewGroup.removeView(adMarvelWebView);
                    viewGroup.invalidate();
                    viewGroup.requestLayout();
                    removeAllViews();
                    addView(adMarvelInternalWebView, i);
                    removeView(view);
                    adMarvelInternalWebView.m319i();
                    this.f893y = false;
                    this.ao = false;
                }
                invalidate();
                requestLayout();
                if (AdMarvelWebView.m451a(this.f888t) != null) {
                    AdMarvelWebView.m451a(this.f888t).m155b();
                }
                if (adMarvelInternalWebView != null) {
                    if (this.f880l != null) {
                        adMarvelInternalWebView.loadUrl("javascript:" + this.f880l + "()");
                    }
                    adMarvelInternalWebView.f598g = false;
                }
            }
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
        }
    }

    void m483e(String str) {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    AdMarvelThreadExecutorService.m597a().m598b().execute(new AdMarvelRedirectRunnable(str, getContext(), this.au, "banner", this.f888t, this.an, AdMarvelView.enableOfflineSDK, this.f867V, Stomp.EMPTY));
                }
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    void m484f() {
        View findViewWithTag = findViewWithTag(this.f888t + "INTERNAL");
        if (findViewWithTag != null && (findViewWithTag instanceof AdMarvelInternalWebView) && !findViewWithTag.hasFocus()) {
            findViewWithTag.requestFocus();
        }
    }

    void m485f(String str) {
        try {
            this.f847B = false;
            if (this.f879k != null) {
                this.f879k.disable();
            }
            Context context = getContext();
            Activity activity = context instanceof Activity ? (Activity) context : null;
            if (this.f892x && this.f894z && !this.f882n && activity != null) {
                activity.setRequestedOrientation(this.f889u);
                this.f892x = false;
            }
            if (this.f869a.compareAndSet(true, false) && activity != null) {
                if (this.f874f) {
                    this.f874f = false;
                }
                View d = AdMarvelWebView.m465d(str);
                if (d != null) {
                    d.f598g = true;
                }
                if (d != null) {
                    removeAllViews();
                    addView(d);
                    d.m319i();
                    this.f893y = false;
                    this.ao = false;
                }
                invalidate();
                requestLayout();
                if (AdMarvelWebView.m451a(str) != null) {
                    AdMarvelWebView.m451a(str).m155b();
                }
                if (d != null) {
                    if (this.f880l != null) {
                        d.loadUrl("javascript:" + this.f880l + "()");
                    }
                    d.f598g = false;
                }
                AdMarvelWebView.m464c(str);
            }
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
        }
    }

    public AdMarvelAd getAdMarvelAd() {
        return this.au;
    }

    AdMarvelWebView getVisiblityListener() {
        return this.f868W;
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        this.aa = hasWindowFocus;
        super.onWindowFocusChanged(hasWindowFocus);
    }

    public void setEnableClickRedirect(boolean enableClickRedirect) {
        this.an = enableClickRedirect;
    }

    void setVisibiltyListener(AdMarvelWebView listener) {
        this.f868W = listener;
    }
}
