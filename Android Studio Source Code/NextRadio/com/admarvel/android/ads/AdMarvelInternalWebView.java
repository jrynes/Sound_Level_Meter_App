package com.admarvel.android.ads;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import com.admarvel.android.ads.AdMarvelInterstitialAds.InterstitialAdsState;
import com.admarvel.android.ads.Utils.C0250s;
import com.admarvel.android.util.AdMarvelBitmapDrawableUtils;
import com.admarvel.android.util.AdMarvelThreadExecutorService;
import com.admarvel.android.util.Logging;
import com.facebook.ads.AdError;
import com.google.android.gms.analytics.ecommerce.Promotion;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Zone;

/* renamed from: com.admarvel.android.ads.f */
public class AdMarvelInternalWebView extends WebView implements OnTouchListener {
    static int f569a;
    private static final Map<String, AdMarvelTwoPartPrivateListener> au;
    private static final Map<String, AdMarvelInAppBrowserPrivateListener> av;
    static String f570b;
    public String f571A;
    public String f572B;
    public boolean f573C;
    public String f574D;
    public String f575E;
    public String f576F;
    public String f577G;
    public String f578H;
    public String f579I;
    public String f580J;
    public String f581K;
    public boolean f582L;
    public boolean f583M;
    public boolean f584N;
    public String f585O;
    boolean f586P;
    private final AtomicBoolean f587Q;
    private AdMarvelInternalWebView f588R;
    private ScheduledThreadPoolExecutor f589S;
    private boolean f590T;
    private boolean f591U;
    private boolean f592V;
    private boolean f593W;
    private int aa;
    private int ab;
    private int ac;
    private int ad;
    private final AtomicBoolean ae;
    private final String af;
    private volatile boolean ag;
    private volatile boolean ah;
    private WeakReference<AdMarvelInterstitialAds> ai;
    private final AdMarvelAd aj;
    private boolean ak;
    private boolean al;
    private boolean am;
    private boolean an;
    private boolean ao;
    private final AdMarvelInternalWebView ap;
    private AdMarvelInternalWebView aq;
    private boolean ar;
    private WeakReference<AdMarvelInternalWebView> as;
    private String at;
    public boolean f594c;
    final WeakReference<AdMarvelView> f595d;
    final WeakReference<RelativeLayout> f596e;
    public boolean f597f;
    public boolean f598g;
    public int f599h;
    public int f600i;
    public String f601j;
    public boolean f602k;
    public boolean f603l;
    public boolean f604m;
    public boolean f605n;
    public String f606o;
    public String f607p;
    public String f608q;
    public String f609r;
    public String f610s;
    final String f611t;
    WeakReference<Context> f612u;
    public GestureDetector f613v;
    public boolean f614w;
    public String f615x;
    public boolean f616y;
    public int f617z;

    /* renamed from: com.admarvel.android.ads.f.a */
    private static class AdMarvelInternalWebView extends WebChromeClient {
        private CustomViewCallback f516a;
        private View f517b;
        private FrameLayout f518c;
        private final WeakReference<AdMarvelInternalWebView> f519d;
        private final WeakReference<Context> f520e;

        /* renamed from: com.admarvel.android.ads.f.a.1 */
        class AdMarvelInternalWebView implements OnKeyListener {
            final /* synthetic */ AdMarvelInternalWebView f512a;

            AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f512a = adMarvelInternalWebView;
            }

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != 0 || keyCode != 4) {
                    return false;
                }
                this.f512a.m271a();
                return true;
            }
        }

        /* renamed from: com.admarvel.android.ads.f.a.2 */
        class AdMarvelInternalWebView implements OnErrorListener {
            final /* synthetic */ AdMarvelInternalWebView f513a;

            AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f513a = adMarvelInternalWebView;
            }

            public boolean onError(MediaPlayer mp, int what, int extra) {
                this.f513a.m271a();
                return true;
            }
        }

        /* renamed from: com.admarvel.android.ads.f.a.3 */
        class AdMarvelInternalWebView implements OnKeyListener {
            final /* synthetic */ AdMarvelInternalWebView f514a;

            AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f514a = adMarvelInternalWebView;
            }

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != 0 || keyCode != 4) {
                    return false;
                }
                if (this.f514a.f517b == null) {
                    return v.onKeyDown(keyCode, event);
                }
                this.f514a.m271a();
                return true;
            }
        }

        /* renamed from: com.admarvel.android.ads.f.a.4 */
        class AdMarvelInternalWebView implements OnKeyListener {
            final /* synthetic */ AdMarvelInternalWebView f515a;

            AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f515a = adMarvelInternalWebView;
            }

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != 0 || keyCode != 4) {
                    return false;
                }
                if (this.f515a.f517b == null) {
                    return v.onKeyDown(keyCode, event);
                }
                this.f515a.m271a();
                return true;
            }
        }

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, Context context) {
            this.f519d = new WeakReference(adMarvelInternalWebView);
            this.f520e = new WeakReference(context);
        }

        void m271a() {
            onHideCustomView();
        }

        public View getVideoLoadingProgressView() {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f519d.get();
            if (adMarvelInternalWebView == null) {
                return super.getVideoLoadingProgressView();
            }
            if (adMarvelInternalWebView.ag) {
                return super.getVideoLoadingProgressView();
            }
            Context context = (Context) this.f520e.get();
            if (context != null && (context instanceof Activity)) {
                Activity activity = (Activity) context;
                if (activity != null) {
                    View progressBar = new ProgressBar(activity, null, 16842871);
                    progressBar.setIndeterminate(true);
                    return progressBar;
                }
            }
            return super.getVideoLoadingProgressView();
        }

        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f519d.get();
            if (adMarvelInternalWebView == null) {
                return true;
            }
            return adMarvelInternalWebView.ag ? true : super.onConsoleMessage(consoleMessage);
        }

        public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f519d.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                callback.invoke(origin, true, false);
            }
        }

        public void onHideCustomView() {
            super.onHideCustomView();
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f519d.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                if (this.f518c != null) {
                    Context context = (Context) this.f520e.get();
                    if (context != null && (context instanceof Activity)) {
                        Activity activity = (Activity) context;
                        if (activity != null) {
                            Window window = activity.getWindow();
                            ViewGroup viewGroup = (ViewGroup) window.findViewById(16908290);
                            this.f518c.removeView(this.f517b);
                            this.f517b = null;
                            LinearLayout linearLayout = (LinearLayout) this.f518c.findViewWithTag(adMarvelInternalWebView.f611t + "VIDEO_CONTROLS");
                            if (linearLayout != null) {
                                this.f518c.removeView(linearLayout);
                            }
                            this.f518c.setBackgroundColor(0);
                            viewGroup.removeView(this.f518c);
                            viewGroup.requestLayout();
                            this.f518c = null;
                            adMarvelInternalWebView.f586P = false;
                            if (window.getCurrentFocus() instanceof AdMarvelInternalWebView) {
                                ((AdMarvelInternalWebView) window.getCurrentFocus()).setOnKeyListener(null);
                            }
                        }
                    }
                }
                try {
                    if (this.f516a != null) {
                        this.f516a.onCustomViewHidden();
                    }
                } catch (Exception e) {
                }
            }
        }

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f519d.get();
            if (adMarvelInternalWebView == null) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ag) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.BANNER) {
                result.cancel();
                return true;
            } else if (this.f520e.get() != null && (this.f520e.get() instanceof Activity)) {
                return super.onJsAlert(view, url, message, result);
            } else {
                result.cancel();
                return true;
            }
        }

        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            result.cancel();
            return true;
        }

        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f519d.get();
            if (adMarvelInternalWebView == null) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ag) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.BANNER) {
                result.cancel();
                return true;
            } else if (this.f520e.get() != null && (this.f520e.get() instanceof Activity)) {
                return super.onJsConfirm(view, url, message, result);
            } else {
                result.cancel();
                return true;
            }
        }

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f519d.get();
            if (adMarvelInternalWebView == null) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ag) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.BANNER) {
                result.cancel();
                return true;
            } else if (this.f520e.get() != null && (this.f520e.get() instanceof Activity)) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            } else {
                result.cancel();
                return true;
            }
        }

        public void onProgressChanged(WebView view, int progress) {
            try {
                AdMarvelInternalWebView adMarvelInternalWebView = this.f519d != null ? (AdMarvelInternalWebView) this.f519d.get() : null;
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                    FullScreenControls fullScreenControls = (adMarvelInternalWebView.getParent() == null || !(adMarvelInternalWebView.getParent() instanceof RelativeLayout)) ? null : (FullScreenControls) ((RelativeLayout) adMarvelInternalWebView.getParent()).findViewWithTag(adMarvelInternalWebView.f611t + "CONTROLS");
                    if (fullScreenControls != null) {
                        ((ProgressBar) fullScreenControls.findViewWithTag(adMarvelInternalWebView.f611t + "PROGRESS_BAR")).setProgress(progress);
                    }
                    Context context = (Context) this.f520e.get();
                    if (context != null && (context instanceof Activity)) {
                        Activity activity = (Activity) context;
                        if (activity != null && (activity instanceof AdMarvelActivity)) {
                            AdMarvelActivity adMarvelActivity = (AdMarvelActivity) activity;
                            if (adMarvelActivity != null && Version.getAndroidSDKVersion() >= 14 && progress == 100 && !adMarvelInternalWebView.ak && adMarvelActivity.f98i && !adMarvelActivity.f99j) {
                                adMarvelInternalWebView.ak = true;
                                adMarvelInternalWebView.loadUrl("javascript:AdApp.adView().play()");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Logging.log("Exception in onProgressChanged " + e.getMessage());
            }
        }

        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f519d.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                this.f516a = callback;
                Context context = adMarvelInternalWebView.f612u != null ? (Context) adMarvelInternalWebView.f612u.get() : null;
                if (context != null && (context instanceof Activity)) {
                    Activity activity = (Activity) context;
                    if (activity == null) {
                        return;
                    }
                    if (this.f517b != null) {
                        callback.onCustomViewHidden();
                        return;
                    }
                    adMarvelInternalWebView.f586P = true;
                    this.f517b = view;
                    Window window = activity.getWindow();
                    ViewGroup viewGroup = (ViewGroup) window.findViewById(16908290);
                    this.f518c = new FrameLayout(adMarvelInternalWebView.getContext());
                    this.f518c.setBackgroundColor(Color.parseColor("#000000"));
                    this.f518c.setLayoutParams(new LayoutParams(-1, -1));
                    View linearLayout = new LinearLayout(adMarvelInternalWebView.getContext());
                    linearLayout.setTag(adMarvelInternalWebView.f611t + "VIDEO_CONTROLS");
                    linearLayout.setBackgroundColor(0);
                    linearLayout.setGravity(53);
                    LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
                    layoutParams.weight = 40.0f;
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.addView(new AdMarvelInternalWebView(adMarvelInternalWebView.getContext(), adMarvelInternalWebView, this, activity));
                    this.f517b.setFocusableInTouchMode(true);
                    this.f517b.requestFocus();
                    this.f517b.setOnKeyListener(new AdMarvelInternalWebView(this));
                    this.f518c.addView(this.f517b);
                    this.f518c.addView(linearLayout);
                    if (view instanceof FrameLayout) {
                        View focusedChild = ((FrameLayout) view).getFocusedChild();
                        if (focusedChild instanceof VideoView) {
                            ((VideoView) focusedChild).setOnErrorListener(new AdMarvelInternalWebView(this));
                            if (window.getCurrentFocus() instanceof AdMarvelInternalWebView) {
                                ((AdMarvelInternalWebView) window.getCurrentFocus()).setOnKeyListener(new AdMarvelInternalWebView(this));
                            }
                            focusedChild.setOnKeyListener(new AdMarvelInternalWebView(this));
                        }
                    }
                    viewGroup.addView(this.f518c);
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.f.b */
    private static class AdMarvelInternalWebView extends WebChromeClient {
        private final WeakReference<AdMarvelInternalWebView> f521a;
        private final WeakReference<Context> f522b;

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, Context context) {
            this.f521a = new WeakReference(adMarvelInternalWebView);
            this.f522b = new WeakReference(context);
        }

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f521a.get();
            if (adMarvelInternalWebView == null) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ag) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.BANNER) {
                result.cancel();
                return true;
            } else if (this.f522b.get() != null && (this.f522b.get() instanceof Activity)) {
                return super.onJsAlert(view, url, message, result);
            } else {
                result.cancel();
                return true;
            }
        }

        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            result.cancel();
            return true;
        }

        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f521a.get();
            if (adMarvelInternalWebView == null) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ag) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.BANNER) {
                result.cancel();
                return true;
            } else if (this.f522b.get() != null && (this.f522b.get() instanceof Activity)) {
                return super.onJsConfirm(view, url, message, result);
            } else {
                result.cancel();
                return true;
            }
        }

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f521a.get();
            if (adMarvelInternalWebView == null) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ag) {
                result.cancel();
                return true;
            } else if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.BANNER) {
                result.cancel();
                return true;
            } else if (this.f522b.get() != null && (this.f522b.get() instanceof Activity)) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            } else {
                result.cancel();
                return true;
            }
        }

        public void onProgressChanged(WebView view, int progress) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f521a.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                FullScreenControls fullScreenControls = null;
                if (adMarvelInternalWebView.getParent() instanceof RelativeLayout) {
                    fullScreenControls = (FullScreenControls) ((RelativeLayout) adMarvelInternalWebView.getParent()).findViewWithTag(adMarvelInternalWebView.f611t + "CONTROLS");
                }
                if (fullScreenControls != null && adMarvelInternalWebView.f587Q.get()) {
                    ((ProgressBar) fullScreenControls.findViewWithTag(adMarvelInternalWebView.f611t + "PROGRESS_BAR")).setProgress(progress);
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.f.c */
    private static class AdMarvelInternalWebView extends WebViewClient {
        private final WeakReference<AdMarvelInternalWebView> f523a;
        private final WeakReference<Context> f524b;
        private final AdMarvelAd f525c;

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, Context context, AdMarvelAd adMarvelAd) {
            this.f523a = new WeakReference(adMarvelInternalWebView);
            this.f524b = new WeakReference(context);
            this.f525c = adMarvelAd;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f523a.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                Context context = (Context) this.f524b.get();
                if (context != null && !adMarvelInternalWebView.ag) {
                    Logging.log("Load Ad: onPageFinished");
                    if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.INAPPBROWSER) {
                        adMarvelInternalWebView.am = true;
                    } else {
                        adMarvelInternalWebView.al = true;
                    }
                    new Handler(Looper.getMainLooper()).post(new AdMarvelInternalWebView(adMarvelInternalWebView, context));
                }
            }
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f523a.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                Logging.log("Load Ad: onPageStarted");
                if (adMarvelInternalWebView.getParent() != null && (adMarvelInternalWebView.getParent() instanceof RelativeLayout)) {
                    RelativeLayout relativeLayout = (RelativeLayout) adMarvelInternalWebView.getParent();
                    FullScreenControls fullScreenControls = (FullScreenControls) relativeLayout.findViewWithTag(adMarvelInternalWebView.f611t + "CONTROLS");
                    if (fullScreenControls != null) {
                        adMarvelInternalWebView.setVisibility(0);
                        fullScreenControls.setVisibility(0);
                        fullScreenControls.findViewWithTag(adMarvelInternalWebView.f611t + "PROGRESS_BAR").setVisibility(0);
                        ((ProgressBar) fullScreenControls.findViewWithTag(adMarvelInternalWebView.f611t + "PROGRESS_BAR")).setProgress(10);
                        relativeLayout.requestLayout();
                    }
                }
                Context context = (Context) this.f524b.get();
                if (context == null) {
                    return;
                }
                if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.INAPPBROWSER) {
                    if (adMarvelInternalWebView.an) {
                        adMarvelInternalWebView.am = false;
                        new Handler(Looper.getMainLooper()).postDelayed(new AdMarvelInternalWebView(adMarvelInternalWebView, context), Constants.WAIT_FOR_INAPP_BROWSER);
                        adMarvelInternalWebView.an = false;
                    }
                } else if (adMarvelInternalWebView.ao) {
                    adMarvelInternalWebView.al = false;
                    new Handler(Looper.getMainLooper()).postDelayed(new AdMarvelInternalWebView(adMarvelInternalWebView, context), Constants.WAIT_FOR_INTERSTITIAL);
                    adMarvelInternalWebView.ao = false;
                }
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Context context = (Context) this.f524b.get();
            if (context != null && (context instanceof Activity)) {
                Activity activity = (Activity) context;
                if (activity != null && (activity instanceof AdMarvelActivity)) {
                    AdMarvelActivity adMarvelActivity = (AdMarvelActivity) activity;
                    if (adMarvelActivity != null) {
                        Logging.log("onReceivedError - Closing AdMarvel FullScreen due to bad URL : " + failingUrl);
                        adMarvelActivity.m41g();
                    }
                }
            }
        }

        @TargetApi(21)
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String uri;
            AdMarvelInternalWebView adMarvelInternalWebView;
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
                    adMarvelInternalWebView = (AdMarvelInternalWebView) this.f523a.get();
                    if (adMarvelInternalWebView != null || adMarvelInternalWebView.ag || uri == null) {
                        return null;
                    }
                    if (uri.equals("http://baseurl.admarvel.com/mraid.js") && (!adMarvelInternalWebView.f593W || !uri.endsWith("mraid.js"))) {
                        return super.shouldInterceptRequest(view, request);
                    }
                    str = Stomp.EMPTY;
                    dir = adMarvelInternalWebView.getContext().getDir("adm_assets", 0);
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
                                AdMarvelInternalWebView adMarvelInternalWebView2 = new AdMarvelInternalWebView();
                                adMarvelInternalWebView2.f532a = bArr;
                                adMarvelInternalWebView2.f533b = contentLength;
                                responseCode += contentLength;
                                arrayList.add(adMarvelInternalWebView2);
                            }
                        }
                        inputStream.close();
                        if (responseCode <= 0) {
                            obj = new byte[responseCode];
                            for (responseCode = 0; responseCode < arrayList.size(); responseCode++) {
                                AdMarvelInternalWebView adMarvelInternalWebView3 = (AdMarvelInternalWebView) arrayList.get(responseCode);
                                System.arraycopy(adMarvelInternalWebView3.f532a, 0, obj, i, adMarvelInternalWebView3.f533b);
                                i += adMarvelInternalWebView3.f533b;
                            }
                            uri = new String(obj);
                        } else {
                            uri = str;
                        }
                        adMarvelInternalWebView.f593W = false;
                        return new WebResourceResponse(WebRequest.CONTENT_TYPE_CSS, HttpRequest.CHARSET_UTF8, new ByteArrayInputStream(uri.getBytes()));
                    } catch (Throwable e2) {
                        Logging.log(Log.getStackTraceString(e2));
                        return super.shouldInterceptRequest(view, request);
                    }
                }
            }
            uri = null;
            adMarvelInternalWebView = (AdMarvelInternalWebView) this.f523a.get();
            if (adMarvelInternalWebView != null) {
                return null;
            }
            if (uri.equals("http://baseurl.admarvel.com/mraid.js")) {
            }
            str = Stomp.EMPTY;
            dir = adMarvelInternalWebView.getContext().getDir("adm_assets", 0);
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
                    AdMarvelInternalWebView adMarvelInternalWebView22 = new AdMarvelInternalWebView();
                    adMarvelInternalWebView22.f532a = bArr;
                    adMarvelInternalWebView22.f533b = contentLength;
                    responseCode += contentLength;
                    arrayList.add(adMarvelInternalWebView22);
                }
            }
            inputStream.close();
            if (responseCode <= 0) {
                uri = str;
            } else {
                obj = new byte[responseCode];
                for (responseCode = 0; responseCode < arrayList.size(); responseCode++) {
                    AdMarvelInternalWebView adMarvelInternalWebView32 = (AdMarvelInternalWebView) arrayList.get(responseCode);
                    System.arraycopy(adMarvelInternalWebView32.f532a, 0, obj, i, adMarvelInternalWebView32.f533b);
                    i += adMarvelInternalWebView32.f533b;
                }
                uri = new String(obj);
            }
            adMarvelInternalWebView.f593W = false;
            return new WebResourceResponse(WebRequest.CONTENT_TYPE_CSS, HttpRequest.CHARSET_UTF8, new ByteArrayInputStream(uri.getBytes()));
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f523a.get();
            if (adMarvelInternalWebView == null) {
                return false;
            }
            return adMarvelInternalWebView.ag ? false : adMarvelInternalWebView.m296f(url);
        }
    }

    /* renamed from: com.admarvel.android.ads.f.d */
    private static class AdMarvelInternalWebView extends WebViewClient {
        private final WeakReference<AdMarvelInternalWebView> f526a;
        private final WeakReference<Context> f527b;
        private final AdMarvelAd f528c;

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, Context context, AdMarvelAd adMarvelAd) {
            this.f526a = new WeakReference(adMarvelInternalWebView);
            this.f527b = new WeakReference(context);
            this.f528c = adMarvelAd;
        }

        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f526a.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag && adMarvelInternalWebView.f593W && url.contains("mraid.js")) {
                view.loadUrl("javascript: (function() { var script=document.createElement('script');script.type='text/javascript';script.src='http://admarvel.s3.amazonaws.com/js/admarvel_mraid_v2_complete.js';document.getElementsByTagName('head').item(0).appendChild(script);})()");
                adMarvelInternalWebView.f593W = false;
            }
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f526a.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                Context context = (Context) this.f527b.get();
                if (context != null && !adMarvelInternalWebView.ag) {
                    Logging.log("Load Ad: onPageFinished");
                    if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.INAPPBROWSER) {
                        adMarvelInternalWebView.am = true;
                    } else {
                        adMarvelInternalWebView.al = true;
                    }
                    new Handler(Looper.getMainLooper()).post(new AdMarvelInternalWebView(adMarvelInternalWebView, context));
                }
            }
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f526a.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                Logging.log("Load Ad: onPageStarted");
                if (adMarvelInternalWebView.getParent() != null && (adMarvelInternalWebView.getParent() instanceof RelativeLayout)) {
                    RelativeLayout relativeLayout = (RelativeLayout) adMarvelInternalWebView.getParent();
                    FullScreenControls fullScreenControls = (FullScreenControls) relativeLayout.findViewWithTag(adMarvelInternalWebView.f611t + "CONTROLS");
                    if (fullScreenControls != null) {
                        adMarvelInternalWebView.setVisibility(0);
                        fullScreenControls.setVisibility(0);
                        fullScreenControls.findViewWithTag(adMarvelInternalWebView.f611t + "PROGRESS_BAR").setVisibility(0);
                        ((ProgressBar) fullScreenControls.findViewWithTag(adMarvelInternalWebView.f611t + "PROGRESS_BAR")).setProgress(10);
                        relativeLayout.requestLayout();
                    }
                }
                Context context = (Context) this.f527b.get();
                if (context == null) {
                    return;
                }
                if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.INAPPBROWSER) {
                    if (adMarvelInternalWebView.an) {
                        adMarvelInternalWebView.am = false;
                        new Handler(Looper.getMainLooper()).postDelayed(new AdMarvelInternalWebView(adMarvelInternalWebView, context), Constants.WAIT_FOR_INAPP_BROWSER);
                        adMarvelInternalWebView.an = false;
                    }
                } else if (adMarvelInternalWebView.ao) {
                    adMarvelInternalWebView.al = false;
                    new Handler(Looper.getMainLooper()).postDelayed(new AdMarvelInternalWebView(adMarvelInternalWebView, context), Constants.WAIT_FOR_INTERSTITIAL);
                    adMarvelInternalWebView.ao = false;
                }
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Context context = (Context) this.f527b.get();
            if (context != null && (context instanceof Activity)) {
                Activity activity = (Activity) context;
                if (activity != null && (activity instanceof AdMarvelActivity)) {
                    AdMarvelActivity adMarvelActivity = (AdMarvelActivity) activity;
                    if (adMarvelActivity != null) {
                        Logging.log("onReceivedError - Closing AdMarvel FullScreen due to bad URL : " + failingUrl);
                        adMarvelActivity.m41g();
                    }
                }
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f526a.get();
            if (adMarvelInternalWebView == null) {
                return false;
            }
            return adMarvelInternalWebView.ag ? false : adMarvelInternalWebView.m296f(url);
        }
    }

    /* renamed from: com.admarvel.android.ads.f.e */
    private static class AdMarvelInternalWebView extends WebViewClient {
        private final WeakReference<AdMarvelInternalWebView> f529a;
        private final WeakReference<Context> f530b;
        private final AdMarvelAd f531c;

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, Context context, AdMarvelAd adMarvelAd) {
            this.f529a = new WeakReference(adMarvelInternalWebView);
            this.f530b = new WeakReference(context);
            this.f531c = adMarvelAd;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f529a.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                Context context = (Context) this.f530b.get();
                if (context != null && !adMarvelInternalWebView.ag) {
                    Logging.log("Load Ad: onPageFinished");
                    if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.INAPPBROWSER) {
                        adMarvelInternalWebView.am = true;
                    } else {
                        adMarvelInternalWebView.al = true;
                    }
                    new Handler(Looper.getMainLooper()).post(new AdMarvelInternalWebView(adMarvelInternalWebView, context));
                }
            }
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f529a.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                Logging.log("Load Ad: onPageStarted");
                if (adMarvelInternalWebView.getParent() != null && (adMarvelInternalWebView.getParent() instanceof RelativeLayout)) {
                    RelativeLayout relativeLayout = (RelativeLayout) adMarvelInternalWebView.getParent();
                    FullScreenControls fullScreenControls = (FullScreenControls) relativeLayout.findViewWithTag(adMarvelInternalWebView.f611t + "CONTROLS");
                    if (fullScreenControls != null) {
                        adMarvelInternalWebView.setVisibility(0);
                        fullScreenControls.setVisibility(0);
                        fullScreenControls.findViewWithTag(adMarvelInternalWebView.f611t + "PROGRESS_BAR").setVisibility(0);
                        ((ProgressBar) fullScreenControls.findViewWithTag(adMarvelInternalWebView.f611t + "PROGRESS_BAR")).setProgress(10);
                        relativeLayout.requestLayout();
                    }
                }
                Context context = (Context) this.f530b.get();
                if (context == null) {
                    return;
                }
                if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.INAPPBROWSER) {
                    if (adMarvelInternalWebView.an) {
                        adMarvelInternalWebView.am = false;
                        new Handler(Looper.getMainLooper()).postDelayed(new AdMarvelInternalWebView(adMarvelInternalWebView, context), Constants.WAIT_FOR_INAPP_BROWSER);
                        adMarvelInternalWebView.an = false;
                    }
                } else if (adMarvelInternalWebView.ao) {
                    adMarvelInternalWebView.al = false;
                    new Handler(Looper.getMainLooper()).postDelayed(new AdMarvelInternalWebView(adMarvelInternalWebView, context), Constants.WAIT_FOR_INTERSTITIAL);
                    adMarvelInternalWebView.ao = false;
                }
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Context context = (Context) this.f530b.get();
            if (context != null && (context instanceof Activity)) {
                Activity activity = (Activity) context;
                if (activity != null && (activity instanceof AdMarvelActivity)) {
                    AdMarvelActivity adMarvelActivity = (AdMarvelActivity) activity;
                    if (adMarvelActivity != null) {
                        Logging.log("onReceivedError - Closing AdMarvel FullScreen due to bad URL : " + failingUrl);
                        adMarvelActivity.m41g();
                    }
                }
            }
        }

        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            File file = null;
            int i = 0;
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f529a.get();
            if (adMarvelInternalWebView == null) {
                return null;
            }
            if (adMarvelInternalWebView.ag) {
                return null;
            }
            if (url == null) {
                return null;
            }
            if (!url.equals("http://baseurl.admarvel.com/mraid.js") && (!adMarvelInternalWebView.f593W || !url.endsWith("mraid.js"))) {
                return super.shouldInterceptRequest(view, url);
            }
            String str = Stomp.EMPTY;
            File dir = adMarvelInternalWebView.getContext().getDir("adm_assets", 0);
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
                            AdMarvelInternalWebView adMarvelInternalWebView2 = new AdMarvelInternalWebView();
                            adMarvelInternalWebView2.f532a = bArr;
                            adMarvelInternalWebView2.f533b = contentLength;
                            responseCode += contentLength;
                            arrayList.add(adMarvelInternalWebView2);
                        }
                    }
                    inputStream.close();
                    if (responseCode > 0) {
                        Object obj = new byte[responseCode];
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            AdMarvelInternalWebView adMarvelInternalWebView3 = (AdMarvelInternalWebView) arrayList.get(i2);
                            System.arraycopy(adMarvelInternalWebView3.f532a, 0, obj, i, adMarvelInternalWebView3.f533b);
                            i += adMarvelInternalWebView3.f533b;
                        }
                        str2 = new String(obj);
                    } else {
                        str2 = str;
                    }
                    adMarvelInternalWebView.f593W = false;
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
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f529a.get();
            if (adMarvelInternalWebView == null) {
                return false;
            }
            return adMarvelInternalWebView.ag ? false : adMarvelInternalWebView.m296f(url);
        }
    }

    /* renamed from: com.admarvel.android.ads.f.f */
    private static class AdMarvelInternalWebView {
        public byte[] f532a;
        public int f533b;

        private AdMarvelInternalWebView() {
            this.f532a = null;
            this.f533b = 0;
        }
    }

    /* renamed from: com.admarvel.android.ads.f.g */
    public enum AdMarvelInternalWebView {
        VISIBLE_ENABLE,
        VISIBLE_DISABLE,
        INVISIBLE_ENABLE,
        INVISIBLE_DISABLE
    }

    /* renamed from: com.admarvel.android.ads.f.h */
    private static class AdMarvelInternalWebView implements Runnable {
        private final WeakReference<Context> f539a;
        private final WeakReference<AdMarvelInternalWebView> f540b;
        private boolean f541c;

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, Context context, Boolean bool) {
            this.f541c = true;
            this.f539a = new WeakReference(context);
            this.f540b = new WeakReference(adMarvelInternalWebView);
            this.f541c = bool.booleanValue();
        }

        public void run() {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f540b.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                Context context = (Context) this.f539a.get();
                if (context != null && (context instanceof Activity)) {
                    Activity activity = (Activity) context;
                    if (activity != null) {
                        ViewGroup viewGroup = (ViewGroup) activity.getWindow().findViewById(16908290);
                        ImageView imageView;
                        if (!this.f541c) {
                            LinearLayout linearLayout = (LinearLayout) viewGroup.findViewWithTag(adMarvelInternalWebView.f611t + "BTN_CLOSE");
                            if (linearLayout != null) {
                                if (linearLayout.getVisibility() != 0) {
                                    linearLayout.setVisibility(0);
                                }
                                imageView = (ImageView) viewGroup.findViewWithTag(adMarvelInternalWebView.f611t + "BTN_CLOSE_IMAGE");
                                if (imageView != null && imageView.getVisibility() != 0) {
                                    imageView.setVisibility(0);
                                }
                            }
                        } else if (adMarvelInternalWebView.f592V) {
                            imageView = (ImageView) viewGroup.findViewWithTag(adMarvelInternalWebView.f611t + "BTN_CLOSE_IMAGE");
                            if (imageView != null) {
                                imageView.setVisibility(4);
                            }
                        } else {
                            LinearLayout linearLayout2 = (LinearLayout) viewGroup.findViewWithTag(adMarvelInternalWebView.f611t + "BTN_CLOSE");
                            if (linearLayout2 != null && adMarvelInternalWebView.f591U) {
                                linearLayout2.setVisibility(8);
                            }
                        }
                    }
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.f.i */
    private static class AdMarvelInternalWebView implements Runnable {
        private final WeakReference<Context> f542a;
        private final WeakReference<AdMarvelInternalWebView> f543b;

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, Context context) {
            this.f542a = new WeakReference(context);
            this.f543b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            AdMarvelInternalWebView adMarvelInternalWebView = null;
            Context context = this.f542a != null ? (Context) this.f542a.get() : null;
            if (context != null && (context instanceof AdMarvelActivity)) {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) context;
                if (this.f543b.get() != null) {
                    adMarvelInternalWebView = (AdMarvelInternalWebView) this.f543b.get();
                }
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && !adMarvelInternalWebView.am) {
                    Logging.log("Closing In-App Browser as onPageFinished is not called");
                    adMarvelActivity.m41g();
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.f.j */
    private static class AdMarvelInternalWebView implements Runnable {
        private final WeakReference<Context> f544a;
        private final WeakReference<AdMarvelInternalWebView> f545b;

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, Context context) {
            this.f544a = new WeakReference(context);
            this.f545b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            AdMarvelInternalWebView adMarvelInternalWebView = null;
            Context context = this.f544a != null ? (Context) this.f544a.get() : null;
            if (context != null && (context instanceof AdMarvelActivity)) {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) context;
                if (this.f545b.get() != null) {
                    adMarvelInternalWebView = (AdMarvelInternalWebView) this.f545b.get();
                }
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && !adMarvelInternalWebView.al) {
                    Logging.log("Closing Interstitial as onpagefinished is not called");
                    adMarvelActivity.m41g();
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.f.k */
    private static class AdMarvelInternalWebView extends LinearLayout {
        private final AdMarvelInternalWebView f549a;
        private final WeakReference<AdMarvelInternalWebView> f550b;
        private final WeakReference<Activity> f551c;

        /* renamed from: com.admarvel.android.ads.f.k.1 */
        class AdMarvelInternalWebView implements OnClickListener {
            final /* synthetic */ AdMarvelInternalWebView f546a;

            AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView) {
                this.f546a = adMarvelInternalWebView;
            }

            public void onClick(View v) {
                this.f546a.f549a.m271a();
            }
        }

        /* renamed from: com.admarvel.android.ads.f.k.2 */
        class AdMarvelInternalWebView implements OnClickListener {
            final /* synthetic */ AdMarvelInternalWebView f547a;
            final /* synthetic */ AdMarvelInternalWebView f548b;

            AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelInternalWebView adMarvelInternalWebView2) {
                this.f548b = adMarvelInternalWebView;
                this.f547a = adMarvelInternalWebView2;
            }

            public void onClick(View v) {
                Activity activity = (Activity) this.f548b.f551c.get();
                if (activity != null && (activity instanceof AdMarvelActivity)) {
                    AdMarvelActivity adMarvelActivity = (AdMarvelActivity) activity;
                    if (adMarvelActivity.m39e()) {
                        if (AdMarvelInternalWebView.m286c(this.f547a.f611t) != null) {
                            AdMarvelInternalWebView.m286c(this.f547a.f611t).m342a();
                        }
                        activity.finish();
                        return;
                    }
                    if (adMarvelActivity.f107r != null && adMarvelActivity.f107r.length() > 0) {
                        this.f547a.m315e(adMarvelActivity.f107r + "()");
                    }
                    Intent intent = new Intent();
                    intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_STATE_INTENT);
                    intent.putExtra("WEBVIEW_GUID", this.f547a.at);
                    activity.getApplicationContext().sendBroadcast(intent);
                    Intent intent2 = new Intent();
                    intent2.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                    intent2.putExtra("WEBVIEW_GUID", this.f547a.at);
                    intent2.putExtra("callback", "close");
                    adMarvelActivity.sendBroadcast(intent2);
                    adMarvelActivity.m36b();
                    adMarvelActivity.finish();
                    if (adMarvelActivity.m34a() > 2) {
                        adMarvelActivity.finish();
                    }
                }
            }
        }

        public AdMarvelInternalWebView(Context context, AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelInternalWebView adMarvelInternalWebView2, Activity activity) {
            super(context);
            this.f549a = adMarvelInternalWebView2;
            this.f550b = new WeakReference(adMarvelInternalWebView);
            this.f551c = new WeakReference(activity);
            m273a(context);
        }

        private void m273a(Context context) {
            setBackgroundColor(0);
            LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.weight = 1.0f;
            layoutParams.width = 0;
            layoutParams.gravity = 5;
            setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams2.weight = 25.0f;
            layoutParams2.gravity = 5;
            float applyDimension = TypedValue.applyDimension(1, 36.0f, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams((int) applyDimension, (int) applyDimension);
            layoutParams3.gravity = 17;
            m274a(context, layoutParams3, layoutParams2);
        }

        private void m274a(Context context, LinearLayout.LayoutParams layoutParams, LinearLayout.LayoutParams layoutParams2) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f550b.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                View imageView = new ImageView(context);
                View relativeLayout = new RelativeLayout(context);
                relativeLayout.setLayoutParams(layoutParams2);
                View linearLayout = new LinearLayout(context);
                LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(Utils.m176a(50.0f, context), Utils.m176a(50.0f, context));
                layoutParams3.addRule(11);
                linearLayout.setLayoutParams(layoutParams3);
                if (this.f549a != null) {
                    linearLayout.setOnClickListener(new AdMarvelInternalWebView(this));
                } else {
                    linearLayout.setOnClickListener(new AdMarvelInternalWebView(this, adMarvelInternalWebView));
                }
                layoutParams.weight = 1.0f;
                imageView.setLayoutParams(layoutParams);
                imageView.setDuplicateParentStateEnabled(true);
                imageView.setClickable(false);
                AdMarvelBitmapDrawableUtils.getBitMapDrawable("close", context, imageView);
                imageView.setTag(adMarvelInternalWebView.f611t + "BTN_CLOSE_IMAGE");
                linearLayout.addView(imageView);
                relativeLayout.addView(linearLayout);
                addView(relativeLayout);
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.f.l */
    private class AdMarvelInternalWebView implements Runnable {
        final /* synthetic */ AdMarvelInternalWebView f552a;
        private final WeakReference<AdMarvelInternalWebView> f553b;
        private String f554c;

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelInternalWebView adMarvelInternalWebView2, String str) {
            this.f552a = adMarvelInternalWebView;
            this.f554c = null;
            this.f553b = new WeakReference(adMarvelInternalWebView2);
            this.f554c = str;
        }

        public void run() {
            if (this.f553b != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f553b.get();
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                    adMarvelInternalWebView.loadUrl("javascript:" + this.f554c);
                    Logging.log("Inject JS:" + this.f554c);
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.f.m */
    private static class AdMarvelInternalWebView implements Runnable {
        private final WeakReference<Context> f555a;
        private final WeakReference<AdMarvelInternalWebView> f556b;

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, Context context) {
            this.f555a = new WeakReference(context);
            this.f556b = new WeakReference(adMarvelInternalWebView);
        }

        public void run() {
            try {
                AdMarvelInternalWebView adMarvelInternalWebView = this.f556b != null ? (AdMarvelInternalWebView) this.f556b.get() : null;
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.ag) {
                    Context context = this.f555a != null ? (Context) this.f555a.get() : null;
                    if (context != null && adMarvelInternalWebView.ap == AdMarvelInternalWebView.INTERSTITIAL) {
                        String str = adMarvelInternalWebView.aj.getPartnerId() + "|" + adMarvelInternalWebView.aj.getSiteId();
                        if (!(adMarvelInternalWebView.ai == null || adMarvelInternalWebView.ai.get() == null)) {
                            ((AdMarvelInterstitialAds) adMarvelInternalWebView.ai.get()).setInterstitialAdsState(InterstitialAdsState.AVAILABLE);
                            ((AdMarvelInterstitialAds) adMarvelInternalWebView.ai.get()).interstitialPublisherID = str;
                            Intent intent = new Intent();
                            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                            intent.putExtra("WEBVIEW_GUID", adMarvelInternalWebView.at);
                            intent.putExtra("callback", "receive");
                            context.sendBroadcast(intent);
                        }
                    }
                    if (context != null && (context instanceof Activity)) {
                        Activity activity = (Activity) context;
                        if (activity != null) {
                            ViewGroup viewGroup = (ViewGroup) activity.getWindow().findViewById(16908290);
                            LinearLayout linearLayout = (LinearLayout) viewGroup.findViewWithTag(adMarvelInternalWebView.f611t + "BTN_CLOSE");
                            if (linearLayout != null) {
                                if (!adMarvelInternalWebView.f591U) {
                                    linearLayout.setVisibility(0);
                                } else if (adMarvelInternalWebView.f592V) {
                                    linearLayout.setVisibility(0);
                                    ImageView imageView = (ImageView) viewGroup.findViewWithTag(adMarvelInternalWebView.f611t + "BTN_CLOSE_IMAGE");
                                    if (imageView != null) {
                                        imageView.setVisibility(4);
                                    }
                                } else {
                                    linearLayout.setVisibility(8);
                                }
                            }
                        }
                        if (adMarvelInternalWebView.f587Q.compareAndSet(false, true)) {
                            if (activity != null && (activity instanceof AdMarvelActivity)) {
                                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) activity;
                                if (adMarvelActivity.m37c() || adMarvelActivity.m39e()) {
                                    adMarvelInternalWebView.setVisibility(0);
                                    if (AdMarvelUtils.isLogDumpEnabled()) {
                                        adMarvelInternalWebView.loadUrl("javascript:window.ADMARVEL.fetchWebViewHtmlContent(document.getElementsByTagName('html')[0].outerHTML);");
                                        ((AdMarvelActivity) activity).m42h();
                                    }
                                } else {
                                    adMarvelInternalWebView.setVisibility(0);
                                    if (AdMarvelUtils.isLogDumpEnabled()) {
                                        ((AdMarvelActivity) activity).m43i();
                                    }
                                }
                            }
                            adMarvelInternalWebView.clearHistory();
                        }
                        if (adMarvelInternalWebView.f587Q.get()) {
                            FullScreenControls fullScreenControls = (adMarvelInternalWebView.getParent() == null || !(adMarvelInternalWebView.getParent() instanceof RelativeLayout)) ? null : (FullScreenControls) ((RelativeLayout) adMarvelInternalWebView.getParent()).findViewWithTag(adMarvelInternalWebView.f611t + "CONTROLS");
                            if (fullScreenControls != null) {
                                fullScreenControls.m494a();
                                fullScreenControls.findViewWithTag(adMarvelInternalWebView.f611t + "PROGRESS_BAR").setVisibility(8);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Logging.log("Exception" + e.getMessage());
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.f.n */
    private class AdMarvelInternalWebView implements Runnable {
        final /* synthetic */ AdMarvelInternalWebView f559a;
        private final WeakReference<AdMarvelInternalWebView> f560b;

        /* renamed from: com.admarvel.android.ads.f.n.1 */
        class AdMarvelInternalWebView implements Runnable {
            final /* synthetic */ AdMarvelInternalWebView f557a;
            final /* synthetic */ AdMarvelInternalWebView f558b;

            AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelInternalWebView adMarvelInternalWebView2) {
                this.f558b = adMarvelInternalWebView;
                this.f557a = adMarvelInternalWebView2;
            }

            public void run() {
                this.f557a.stopLoading();
                if (Version.getAndroidSDKVersion() >= 11) {
                    ac.m255b(this.f557a);
                } else {
                    ad.m257b(this.f557a);
                }
            }
        }

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, AdMarvelInternalWebView adMarvelInternalWebView2) {
            this.f559a = adMarvelInternalWebView;
            this.f560b = new WeakReference(adMarvelInternalWebView2);
        }

        public void run() {
            if (this.f560b != null) {
                AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f560b.get();
                if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                    if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.INTERSTITIAL || adMarvelInternalWebView.ap == AdMarvelInternalWebView.TWOPARTEXPAND) {
                        if (adMarvelInternalWebView.f601j != null && adMarvelInternalWebView.f602k) {
                            adMarvelInternalWebView.m315e(adMarvelInternalWebView.f601j + "(" + false + ")");
                            adMarvelInternalWebView.f602k = false;
                            if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.INTERSTITIAL) {
                                adMarvelInternalWebView.f604m = true;
                            }
                        }
                    } else if (adMarvelInternalWebView.ap == AdMarvelInternalWebView.BANNER && adMarvelInternalWebView.f601j != null) {
                        int i;
                        boolean z;
                        String str;
                        int[] iArr = new int[]{-1, -1};
                        adMarvelInternalWebView.getLocationInWindow(iArr);
                        int height = this.f559a.getHeight() > 0 ? this.f559a.getHeight() / 2 : 0;
                        ViewParent parent = adMarvelInternalWebView.getParent();
                        if (parent instanceof AdMarvelWebView) {
                            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) parent;
                            if (adMarvelWebView != null) {
                                i = (adMarvelWebView.f891w == ExploreByTouchHelper.INVALID_ID || adMarvelWebView.f891w <= 0) ? 0 : adMarvelWebView.f891w;
                                z = (iArr[1] - i) + height < 0 && iArr[1] + height < Utils.m224n(this.f559a.getContext());
                                if (adMarvelInternalWebView.f601j != null && adMarvelInternalWebView.f602k && z) {
                                    str = adMarvelInternalWebView.f601j + "(" + false + ")";
                                    Logging.log("javascript:" + str);
                                    adMarvelInternalWebView.m315e(str);
                                    adMarvelInternalWebView.f602k = false;
                                }
                            }
                        }
                        i = 0;
                        if ((iArr[1] - i) + height < 0) {
                        }
                        str = adMarvelInternalWebView.f601j + "(" + false + ")";
                        Logging.log("javascript:" + str);
                        adMarvelInternalWebView.m315e(str);
                        adMarvelInternalWebView.f602k = false;
                    }
                    adMarvelInternalWebView.m318h();
                    new Handler(Looper.getMainLooper()).post(new AdMarvelInternalWebView(this, adMarvelInternalWebView));
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.f.o */
    private static class AdMarvelInternalWebView implements Runnable {
        private WeakReference<AdMarvelInternalWebView> f561a;
        private WeakReference<AdMarvelWebView> f562b;
        private long f563c;

        public AdMarvelInternalWebView(AdMarvelInternalWebView adMarvelInternalWebView, Context context, AdMarvelWebView adMarvelWebView) {
            this.f563c = 500;
            this.f561a = new WeakReference(adMarvelInternalWebView);
            this.f562b = new WeakReference(adMarvelWebView);
        }

        public void run() {
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f562b.get();
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f561a.get();
            if (adMarvelInternalWebView != null && adMarvelWebView != null && !adMarvelInternalWebView.ag && !adMarvelInternalWebView.ah && !Thread.currentThread().isInterrupted()) {
                int[] iArr = new int[]{-1, -1};
                adMarvelInternalWebView.getLocationInWindow(iArr);
                int height = adMarvelInternalWebView.getHeight() > 0 ? adMarvelInternalWebView.getHeight() / 2 : 0;
                int height2 = adMarvelInternalWebView.getHeight() > 0 ? adMarvelInternalWebView.getHeight() / 2 : 0;
                int i = (adMarvelWebView.f891w == ExploreByTouchHelper.INVALID_ID || adMarvelWebView.f891w <= 0) ? 0 : adMarvelWebView.f891w;
                if (!Thread.currentThread().isInterrupted()) {
                    if (adMarvelWebView.ab) {
                        iArr = new int[]{-1, -1};
                        adMarvelWebView.getLocationInWindow(iArr);
                        boolean z = height + (iArr[1] - i) >= 0 && iArr[1] + height2 < Utils.m224n(adMarvelWebView.getContext());
                        if (iArr[1] == 0 && z) {
                            if (adMarvelWebView.getVisiblityListener() != null) {
                                adMarvelWebView.getVisiblityListener().m343a(true);
                                return;
                            }
                            return;
                        } else if (z && !adMarvelInternalWebView.f602k) {
                            adMarvelInternalWebView.m315e(adMarvelInternalWebView.f601j + "(" + true + ")");
                            adMarvelInternalWebView.f602k = true;
                            if (adMarvelWebView.getVisiblityListener() != null) {
                                adMarvelWebView.getVisiblityListener().m343a(true);
                                return;
                            }
                            return;
                        } else if (!z && adMarvelInternalWebView.f602k) {
                            adMarvelInternalWebView.m315e(adMarvelInternalWebView.f601j + "(" + false + ")");
                            adMarvelInternalWebView.f602k = false;
                            return;
                        } else {
                            return;
                        }
                    }
                    boolean z2 = height + (iArr[1] - i) >= 0 && iArr[1] + height2 < Utils.m224n(adMarvelWebView.getContext());
                    if (z2 && !adMarvelInternalWebView.f602k) {
                        adMarvelInternalWebView.m315e(adMarvelInternalWebView.f601j + "(" + true + ")");
                        adMarvelInternalWebView.f602k = true;
                    } else if (!z2 && adMarvelInternalWebView.f602k) {
                        adMarvelInternalWebView.m315e(adMarvelInternalWebView.f601j + "(" + false + ")");
                        adMarvelInternalWebView.f602k = false;
                    }
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.f.p */
    public enum AdMarvelInternalWebView {
        BANNER,
        INTERSTITIAL,
        INAPPBROWSER,
        TWOPARTEXPAND
    }

    static {
        f569a = 100002;
        f570b = "admarvel_internal_webview_" + f569a;
        au = new ConcurrentHashMap();
        av = new ConcurrentHashMap();
    }

    AdMarvelInternalWebView(Context context, String str, String str2, AdMarvelView adMarvelView, RelativeLayout relativeLayout, AdMarvelAd adMarvelAd, AdMarvelInternalWebView adMarvelInternalWebView, String str3) {
        super(context);
        this.f594c = false;
        this.f597f = false;
        this.f598g = false;
        this.f588R = null;
        this.f590T = true;
        this.f591U = false;
        this.f592V = false;
        this.f593W = false;
        this.f599h = 0;
        this.f600i = 0;
        this.f601j = null;
        this.f602k = false;
        this.f603l = false;
        this.f604m = false;
        this.f605n = true;
        this.f606o = null;
        this.f607p = null;
        this.f608q = null;
        this.f609r = null;
        this.f610s = null;
        this.ac = -1;
        this.ad = -1;
        this.ak = false;
        this.f613v = null;
        this.al = true;
        this.am = true;
        this.an = true;
        this.ao = true;
        this.f614w = false;
        this.f616y = false;
        this.f617z = -1;
        this.f573C = false;
        this.f582L = false;
        this.f583M = true;
        this.f584N = false;
        this.f585O = null;
        this.ar = false;
        this.f586P = false;
        this.af = str;
        this.f587Q = new AtomicBoolean(false);
        this.at = str3;
        this.ae = new AtomicBoolean(true);
        this.ag = false;
        this.ah = false;
        this.f611t = str2;
        this.f612u = new WeakReference(context);
        this.aj = adMarvelAd;
        this.ap = adMarvelInternalWebView;
        this.aq = AdMarvelInternalWebView.VISIBLE_ENABLE;
        this.f595d = new WeakReference(adMarvelView);
        this.f596e = new WeakReference(relativeLayout);
        if (this.f595d != null) {
            AdMarvelView adMarvelView2 = (AdMarvelView) this.f595d.get();
            if (adMarvelView2 != null) {
                this.f594c = adMarvelView2.isSoftwareLayer();
            }
        }
        setOnTouchListener(this);
        this.f613v = new GestureDetector(context, new AdMarvelInternalWebView());
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        if (this.ap == AdMarvelInternalWebView.INAPPBROWSER) {
            atomicBoolean.set(true);
        }
        if (Version.getAndroidSDKVersion() >= 18) {
            AdMarvelInternalWebView.m518a(this, context, atomicBoolean, this.f594c);
        } else if (Version.getAndroidSDKVersion() >= 17) {
            AdMarvelInternalWebView.m522a(this, context, atomicBoolean, this.f594c);
        } else if (Version.getAndroidSDKVersion() >= 16) {
            AdMarvelInternalWebView.m521a(this, context, atomicBoolean, this.f594c);
        } else if (Version.getAndroidSDKVersion() >= 11) {
            AdMarvelInternalWebView.m520a(this, context, atomicBoolean, this.f594c);
        } else if (Version.getAndroidSDKVersion() >= 8) {
            AdMarvelInternalWebView.m519a(this, context, atomicBoolean);
        } else if (Version.getAndroidSDKVersion() >= 7) {
            ab.m253a(this, context, atomicBoolean);
        } else {
            aa.m252a(this, context, atomicBoolean);
        }
        if (atomicBoolean.get()) {
            setScrollBarStyle(0);
        }
        if (this.ap == AdMarvelInternalWebView.TWOPARTEXPAND) {
            this.f593W = true;
            m309a(context, false);
        }
        if (Version.getAndroidSDKVersion() >= 7) {
            WebChromeClient adMarvelInternalWebView2 = new AdMarvelInternalWebView(this, context);
            this.as = new WeakReference(adMarvelInternalWebView2);
            setWebChromeClient(adMarvelInternalWebView2);
            return;
        }
        setWebChromeClient(new AdMarvelInternalWebView(this, context));
    }

    static synchronized void m276a(String str) {
        synchronized (AdMarvelInternalWebView.class) {
            try {
                au.remove(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void m277a(String str, AdMarvelInAppBrowserPrivateListener adMarvelInAppBrowserPrivateListener) {
        synchronized (AdMarvelInternalWebView.class) {
            av.put(str, adMarvelInAppBrowserPrivateListener);
        }
    }

    public static synchronized void m278a(String str, AdMarvelTwoPartPrivateListener adMarvelTwoPartPrivateListener) {
        synchronized (AdMarvelInternalWebView.class) {
            au.put(str, adMarvelTwoPartPrivateListener);
        }
    }

    static void m283b(String str) {
        try {
            av.remove(str);
        } catch (Exception e) {
        }
    }

    public static AdMarvelTwoPartPrivateListener m286c(String str) {
        return (AdMarvelTwoPartPrivateListener) au.get(str);
    }

    public static AdMarvelInAppBrowserPrivateListener m288d(String str) {
        return (AdMarvelInAppBrowserPrivateListener) av.get(str);
    }

    public static void m290d() {
        av.clear();
        au.clear();
    }

    private boolean m296f(String str) {
        Context context = (Context) this.f612u.get();
        if (context != null && (context instanceof Activity)) {
            Activity activity = (Activity) context;
            if (activity != null && (activity instanceof AdMarvelActivity)) {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) activity;
                if (adMarvelActivity != null) {
                    Intent intent;
                    if (Utils.m193a(getContext(), str, false)) {
                        new Utils(getContext()).m247c(this.af);
                        if (!adMarvelActivity.m37c()) {
                            adMarvelActivity.m41g();
                        }
                        if (adMarvelActivity.m38d() && this.aj != null) {
                            intent = new Intent();
                            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                            intent.putExtra("WEBVIEW_GUID", this.at);
                            intent.putExtra(SettingsJsonConstants.APP_URL_KEY, str);
                            intent.putExtra("callback", Promotion.ACTION_CLICK);
                            adMarvelActivity.sendBroadcast(intent);
                        } else if (!(AdMarvelWebView.m451a(this.f611t) == null || this.aj == null)) {
                            AdMarvelWebView.m451a(this.f611t).m152a(this.aj, str);
                        }
                        return true;
                    } else if (adMarvelActivity.m37c()) {
                        if (adMarvelActivity.m37c()) {
                            if (AdMarvelInterstitialAds.getEnableClickRedirect()) {
                                if (str != null && Utils.m179a(str, "admarvelsdk") != C0250s.NONE) {
                                    if (this.aj != null) {
                                        this.aj.setWebViewRedirectUrlProtocol("admarvelsdk");
                                        this.aj.setWebViewRedirectUrl(str);
                                        intent = new Intent();
                                        intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                                        intent.putExtra("callback", Promotion.ACTION_CLICK);
                                        intent.putExtra("WEBVIEW_GUID", this.at);
                                        adMarvelActivity.sendBroadcast(intent);
                                    }
                                    new Utils(getContext()).m247c(this.af);
                                    return true;
                                } else if (str != null && Utils.m179a(str, "admarvelinternal") != C0250s.NONE) {
                                    if (this.aj != null) {
                                        this.aj.setWebViewRedirectUrlProtocol("admarvelinternal");
                                        this.aj.setWebViewRedirectUrl(str);
                                        intent = new Intent();
                                        intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                                        intent.putExtra("callback", Promotion.ACTION_CLICK);
                                        intent.putExtra("WEBVIEW_GUID", this.at);
                                        adMarvelActivity.sendBroadcast(intent);
                                    }
                                    new Utils(getContext()).m247c(this.af);
                                    return true;
                                } else if (str != null && Utils.m179a(str, "admarvelvideo") != C0250s.NONE) {
                                    if (this.aj != null) {
                                        this.aj.setWebViewRedirectUrlProtocol("admarvelvideo");
                                        this.aj.setWebViewRedirectUrl(str);
                                        intent = new Intent();
                                        intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                                        intent.putExtra("callback", Promotion.ACTION_CLICK);
                                        intent.putExtra("WEBVIEW_GUID", this.at);
                                        adMarvelActivity.sendBroadcast(intent);
                                    }
                                    r2 = Utils.m184a(str, "admarvelvideo", "http://", Utils.m179a(str, "admarvelvideo"), getContext());
                                    r3 = new Intent("android.intent.action.VIEW");
                                    r3.addFlags(268435456);
                                    r3.setDataAndType(Uri.parse(r2), "video/*");
                                    if (Utils.m191a(getContext(), r3)) {
                                        getContext().startActivity(r3);
                                    }
                                    new Utils(getContext()).m247c(this.af);
                                } else if (str != null && Utils.m179a(str, "admarvelcustomvideo") != C0250s.NONE) {
                                    if (adMarvelActivity.m38d() && this.aj != null) {
                                        this.aj.setWebViewRedirectUrlProtocol("admarvelcustomvideo");
                                        this.aj.setWebViewRedirectUrl(str);
                                        r3 = new Intent();
                                        r3.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                                        r3.putExtra("callback", Promotion.ACTION_CLICK);
                                        r3.putExtra("WEBVIEW_GUID", this.at);
                                        adMarvelActivity.sendBroadcast(r3);
                                    } else if (!(AdMarvelWebView.m451a(this.f611t) == null || this.aj == null)) {
                                        AdMarvelWebView.m451a(this.f611t).m152a(this.aj, str);
                                    }
                                    r3 = new Intent(getContext(), AdMarvelVideoActivity.class);
                                    r3.addFlags(268435456);
                                    r3.putExtra(SettingsJsonConstants.APP_URL_KEY, str);
                                    r3.putExtra("isCustomUrl", true);
                                    r3.putExtra("isInterstitial", false);
                                    r3.putExtra("isInterstitialClick", true);
                                    r3.putExtra("xml", this.af);
                                    r3.putExtra("GUID", this.f611t);
                                    r3.putExtra("WEBVIEW_GUID", this.at);
                                    getContext().startActivity(r3);
                                    new Utils(getContext()).m247c(this.af);
                                } else if (str != null && Utils.m179a(str, "admarvelexternal") != C0250s.NONE) {
                                    intent = new Intent("android.intent.action.VIEW", Uri.parse(Utils.m184a(str, "admarvelexternal", Stomp.EMPTY, Utils.m179a(str, "admarvelexternal"), getContext())));
                                    intent.addFlags(268435456);
                                    if (Utils.m191a(getContext(), intent)) {
                                        getContext().startActivity(intent);
                                    }
                                    new Utils(getContext()).m247c(this.af);
                                } else if (this.ae.get() && str != null && str.length() > 0) {
                                    r3 = new Intent(getContext(), AdMarvelActivity.class);
                                    r3.addFlags(268435456);
                                    r3.putExtra(SettingsJsonConstants.APP_URL_KEY, str);
                                    r3.putExtra("isInterstitial", false);
                                    r3.putExtra("isInterstitialClick", true);
                                    r3.putExtra("xml", this.af);
                                    r3.putExtra("GUID", this.f611t);
                                    r3.putExtra("WEBVIEW_GUID", this.at);
                                    try {
                                        if (this.aj != null) {
                                            this.aj.removeNonStringEntriesTargetParam();
                                            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                                            objectOutputStream.writeObject(this.aj);
                                            objectOutputStream.close();
                                            r3.putExtra("serialized_admarvelad", byteArrayOutputStream.toByteArray());
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (adMarvelActivity.m40f() != null) {
                                        r3.putExtra(locationTracking.source, adMarvelActivity.m40f());
                                    }
                                    getContext().startActivity(r3);
                                    new Utils(getContext()).m247c(this.af);
                                }
                            }
                            if (this.aj != null) {
                                intent = new Intent();
                                intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                                intent.putExtra(SettingsJsonConstants.APP_URL_KEY, str);
                                intent.putExtra("WEBVIEW_GUID", this.at);
                                intent.putExtra("callback", Promotion.ACTION_CLICK);
                                adMarvelActivity.sendBroadcast(intent);
                            }
                            return true;
                        }
                    } else if (str != null && Utils.m179a(str, "admarvelsdk") != C0250s.NONE) {
                        if (adMarvelActivity.m38d() && this.aj != null) {
                            intent = new Intent();
                            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                            intent.putExtra("callback", Promotion.ACTION_CLICK);
                            intent.putExtra("WEBVIEW_GUID", this.at);
                            intent.putExtra(SettingsJsonConstants.APP_URL_KEY, Utils.m184a(str, "admarvelsdk", Stomp.EMPTY, Utils.m179a(str, "admarvelsdk"), getContext()));
                            adMarvelActivity.sendBroadcast(intent);
                        } else if (!(AdMarvelWebView.m451a(this.f611t) == null || this.aj == null)) {
                            AdMarvelWebView.m451a(this.f611t).m152a(this.aj, Utils.m184a(str, "admarvelsdk", Stomp.EMPTY, Utils.m179a(str, "admarvelsdk"), getContext()));
                        }
                        new Utils(getContext()).m247c(this.af);
                        adMarvelActivity.m41g();
                        return true;
                    } else if (str != null && Utils.m179a(str, "admarvelinternal") != C0250s.NONE) {
                        if (adMarvelActivity.m38d() && this.aj != null) {
                            intent = new Intent();
                            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                            intent.putExtra("callback", Promotion.ACTION_CLICK);
                            intent.putExtra("WEBVIEW_GUID", this.at);
                            intent.putExtra(SettingsJsonConstants.APP_URL_KEY, Utils.m184a(str, "admarvelinternal", Stomp.EMPTY, Utils.m179a(str, "admarvelinternal"), getContext()));
                            adMarvelActivity.sendBroadcast(intent);
                        } else if (!(AdMarvelWebView.m451a(this.f611t) == null || this.aj == null)) {
                            AdMarvelWebView.m451a(this.f611t).m152a(this.aj, Utils.m184a(str, "admarvelinternal", Stomp.EMPTY, Utils.m179a(str, "admarvelinternal"), getContext()));
                        }
                        new Utils(getContext()).m247c(this.af);
                        adMarvelActivity.m41g();
                        return true;
                    } else if (str != null && Utils.m179a(str, "admarvelvideo") != C0250s.NONE) {
                        if (adMarvelActivity.m38d() && this.aj != null) {
                            intent = new Intent();
                            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                            intent.putExtra("callback", Promotion.ACTION_CLICK);
                            intent.putExtra("WEBVIEW_GUID", this.at);
                            intent.putExtra(SettingsJsonConstants.APP_URL_KEY, Utils.m184a(str, "admarvelvideo", "http://", Utils.m179a(str, "admarvelvideo"), getContext()));
                            adMarvelActivity.sendBroadcast(intent);
                        } else if (!(AdMarvelWebView.m451a(this.f611t) == null || this.aj == null)) {
                            AdMarvelWebView.m451a(this.f611t).m152a(this.aj, str);
                        }
                        r2 = Utils.m184a(str, "admarvelvideo", "http://", Utils.m179a(str, "admarvelvideo"), getContext());
                        r3 = new Intent("android.intent.action.VIEW");
                        r3.addFlags(268435456);
                        r3.setDataAndType(Uri.parse(r2), "video/*");
                        getContext().startActivity(r3);
                        new Utils(getContext()).m247c(this.af);
                        adMarvelActivity.m41g();
                        return true;
                    } else if (str != null && Utils.m179a(str, "admarvelcustomvideo") != C0250s.NONE) {
                        if (adMarvelActivity.m38d() && this.aj != null) {
                            r3 = new Intent();
                            r3.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                            r3.putExtra("callback", Promotion.ACTION_CLICK);
                            r3.putExtra("WEBVIEW_GUID", this.at);
                            r3.putExtra(SettingsJsonConstants.APP_URL_KEY, str);
                            adMarvelActivity.sendBroadcast(r3);
                        } else if (!(AdMarvelWebView.m451a(this.f611t) == null || this.aj == null)) {
                            AdMarvelWebView.m451a(this.f611t).m152a(this.aj, str);
                        }
                        Intent intent2 = new Intent(getContext(), AdMarvelVideoActivity.class);
                        intent2.addFlags(268435456);
                        this.aj.removeNonStringEntriesTargetParam();
                        try {
                            OutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                            ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(byteArrayOutputStream2);
                            objectOutputStream2.writeObject(this.aj);
                            objectOutputStream2.close();
                            intent2.putExtra("serialized_admarvelad", byteArrayOutputStream2.toByteArray());
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        intent2.putExtra(SettingsJsonConstants.APP_URL_KEY, str);
                        intent2.putExtra("isCustomUrl", true);
                        intent2.putExtra("xml", this.af);
                        intent2.putExtra("GUID", this.f611t);
                        getContext().startActivity(intent2);
                        new Utils(getContext()).m247c(this.af);
                        adMarvelActivity.m41g();
                    } else if (!(str == null || Utils.m179a(str, "admarvelexternal") == C0250s.NONE)) {
                        Intent intent3 = new Intent("android.intent.action.VIEW", Uri.parse(Utils.m184a(str, "admarvelexternal", Stomp.EMPTY, Utils.m179a(str, "admarvelexternal"), getContext())));
                        intent3.addFlags(268435456);
                        if (Utils.m191a(getContext(), intent3)) {
                            getContext().startActivity(intent3);
                        }
                        new Utils(getContext()).m247c(this.af);
                        adMarvelActivity.m41g();
                    }
                }
            }
        }
        return false;
    }

    public void m306a() {
        this.ao = true;
        m314e();
        WebSettings settings = getSettings();
        if (settings != null) {
            settings.setJavaScriptEnabled(false);
        }
        this.f612u.clear();
        this.f595d.clear();
        this.f596e.clear();
        m318h();
        this.f613v = null;
        this.f613v = null;
        Logging.log("AdMarveInternalWebView:cleanup");
    }

    void m307a(int i, int i2, int i3, int i4) {
        if (!this.ag) {
            setLayoutParams(new RelativeLayout.LayoutParams(-1, i4));
        }
    }

    public void m308a(Context context) {
        this.f612u.clear();
        this.f612u = new WeakReference(context);
    }

    public void m309a(Context context, boolean z) {
        if (context != null && (context instanceof Activity)) {
            Activity activity = (Activity) context;
            if (activity != null && (activity instanceof AdMarvelActivity) && ((AdMarvelActivity) activity) != null) {
                ViewGroup viewGroup = (ViewGroup) activity.getWindow().findViewById(16908290);
                View linearLayout = new LinearLayout(getContext());
                linearLayout.setBackgroundColor(0);
                linearLayout.setTag(this.f611t + "BTN_CLOSE");
                if (!z) {
                    linearLayout.setVisibility(4);
                }
                linearLayout.setGravity(53);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                View adMarvelInternalWebView = new AdMarvelInternalWebView(context, this, null, activity);
                linearLayout.addView(adMarvelInternalWebView);
                if (this.aq == AdMarvelInternalWebView.INVISIBLE_DISABLE) {
                    linearLayout.setVisibility(8);
                } else if (this.aq == AdMarvelInternalWebView.INVISIBLE_ENABLE) {
                    adMarvelInternalWebView.findViewWithTag(this.f611t + "BTN_CLOSE_IMAGE").setVisibility(4);
                }
                viewGroup.addView(linearLayout);
            }
        }
    }

    public void m310a(AdMarvelWebView adMarvelWebView) {
        if (!this.ag) {
            m318h();
            if (this.f601j != null) {
                Context context = this.f612u != null ? (Context) this.f612u.get() : null;
                if (context != null) {
                    this.f588R = null;
                    this.f589S = null;
                    this.f588R = new AdMarvelInternalWebView(this, context, adMarvelWebView);
                    this.f589S = new ScheduledThreadPoolExecutor(1);
                    this.f589S.scheduleWithFixedDelay(this.f588R, 0, 500, TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    public void m311a(boolean z) {
        if (!this.ag) {
            this.f591U = true;
            this.f592V = z;
            if (z) {
                this.aq = AdMarvelInternalWebView.INVISIBLE_ENABLE;
            } else {
                this.aq = AdMarvelInternalWebView.INVISIBLE_DISABLE;
            }
            Context context = (Context) this.f612u.get();
            if (context != null) {
                new Handler(Looper.getMainLooper()).post(new AdMarvelInternalWebView(this, context, Boolean.valueOf(true)));
            }
        }
    }

    public boolean m312b() {
        return this.ag;
    }

    public boolean m313c() {
        return this.ar;
    }

    public void destroy() {
        m314e();
        this.ao = true;
        super.destroy();
        Logging.log("AdMarveInternalWebView:destroy()");
    }

    public void m314e() {
        this.ag = true;
    }

    public void m315e(String str) {
        new Handler(Looper.getMainLooper()).post(new AdMarvelInternalWebView(this, this, str));
    }

    public void m316f() {
        this.ah = true;
        AdMarvelThreadExecutorService.m597a().m598b().execute(new AdMarvelInternalWebView(this, this));
    }

    public void m317g() {
        boolean z = false;
        this.ah = false;
        if (this.ap == AdMarvelInternalWebView.INTERSTITIAL || this.ap == AdMarvelInternalWebView.TWOPARTEXPAND) {
            if (this.f601j != null && !this.f602k) {
                m315e(this.f601j + "(" + true + ")");
                this.f602k = true;
                if (this.ap == AdMarvelInternalWebView.INTERSTITIAL) {
                    this.f604m = true;
                }
            }
        } else if (this.ap == AdMarvelInternalWebView.BANNER && this.f601j != null) {
            int i;
            int[] iArr = new int[]{-1, -1};
            getLocationInWindow(iArr);
            int height = getHeight() > 0 ? getHeight() / 2 : 0;
            ViewParent parent = getParent();
            if (parent instanceof AdMarvelWebView) {
                AdMarvelWebView adMarvelWebView = (AdMarvelWebView) parent;
                if (adMarvelWebView != null) {
                    i = (adMarvelWebView.f891w == ExploreByTouchHelper.INVALID_ID || adMarvelWebView.f891w <= 0) ? 0 : adMarvelWebView.f891w;
                    if ((iArr[1] - i) + height >= 0 && iArr[1] + height < Utils.m224n(getContext())) {
                        z = true;
                    }
                    if (this.f601j != null && !this.f602k && r2) {
                        m315e(this.f601j + "(" + true + ")");
                        this.f602k = true;
                        return;
                    }
                    return;
                }
            }
            i = 0;
            z = true;
            if (this.f601j != null) {
            }
        }
    }

    AdMarvelInterstitialAds getAdMarvelInterstitialAdsInstance() {
        return this.ai == null ? null : (AdMarvelInterstitialAds) this.ai.get();
    }

    public String getBaseUrl() {
        return null;
    }

    public AtomicBoolean getEnableAutoDetect() {
        return this.ae;
    }

    public void m318h() {
        if (this.f588R != null && this.f601j != null && this.f589S != null) {
            this.f589S.remove(this.f588R);
            this.f589S.shutdown();
            this.f589S.purge();
            this.f588R = null;
        }
    }

    void m319i() {
        if (!this.ag) {
            LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = this.ab;
            layoutParams.height = this.aa;
            setVisibility(0);
            ViewParent parent = getParent();
            if (parent instanceof AdMarvelWebView) {
                ((AdMarvelWebView) parent).m475a(this.ab, this.aa);
            }
            requestLayout();
        }
    }

    void m320j() {
        if (!this.ag) {
            setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        }
    }

    void m321k() {
        if (!this.ag) {
            LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(2, FullScreenControls.f903a);
            setLayoutParams(layoutParams);
        }
    }

    void m322l() {
        Context context = (Context) this.f612u.get();
        if (context == null) {
            return;
        }
        if (this.aj == null && this.ap != AdMarvelInternalWebView.INAPPBROWSER) {
            return;
        }
        if (Version.getAndroidSDKVersion() >= 11 && Version.getAndroidSDKVersion() < 21) {
            setWebViewClient(new AdMarvelInternalWebView(this, context, this.aj));
        } else if (Version.getAndroidSDKVersion() < 11) {
            setWebViewClient(new AdMarvelInternalWebView(this, context, this.aj));
        } else {
            setWebViewClient(new AdMarvelInternalWebView(this, context, this.aj));
        }
    }

    public void m323m() {
        this.aq = AdMarvelInternalWebView.VISIBLE_ENABLE;
        if (this.f591U) {
            Context context = (Context) this.f612u.get();
            if (context != null) {
                new Handler(Looper.getMainLooper()).post(new AdMarvelInternalWebView(this, context, Boolean.valueOf(false)));
            }
            this.f591U = false;
        }
    }

    void m324n() {
        if (this.as != null && this.as.get() != null) {
            ((AdMarvelInternalWebView) this.as.get()).m271a();
        }
    }

    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (IllegalArgumentException e) {
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.ap == AdMarvelInternalWebView.INTERSTITIAL && this.f605n && this.f601j != null && !this.f602k) {
            m315e(this.f601j + "(" + true + ")");
            this.f602k = true;
            this.f605n = false;
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!this.ag) {
            ViewParent parent = getParent();
            int width = getWidth();
            int height = getHeight();
            if ((parent instanceof AdMarvelWebView) && !((AdMarvelWebView) parent).f869a.get()) {
                this.aa = getHeight();
                this.ab = getWidth();
                if (this.f601j != null && this.f590T) {
                    this.f590T = false;
                    m310a((AdMarvelWebView) parent);
                }
            } else if (this.ap == AdMarvelInternalWebView.INTERSTITIAL || this.ap == AdMarvelInternalWebView.TWOPARTEXPAND) {
                if (height == 0) {
                    this.f604m = false;
                } else {
                    this.f604m = true;
                }
            }
            if (!(this.ac == -1 || this.ad == -1 || ((width == this.ad && height == this.ac) || width <= 0 || height <= 0 || this.ad < 0 || this.ac < 0 || this.f606o == null))) {
                m315e(this.f606o + "(" + width + Stomp.COMMA + height + ")");
            }
            this.ad = width;
            this.ac = height;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        this.ar = true;
        switch (event.getAction()) {
            case Tokenizer.EOF /*0*/:
            case Zone.PRIMARY /*1*/:
                if (!v.hasFocus()) {
                    v.requestFocus();
                    break;
                }
                break;
        }
        return (this.ap == AdMarvelInternalWebView.INAPPBROWSER || Version.getAndroidSDKVersion() <= 10 || this.f613v == null) ? false : this.f613v.onTouchEvent(event);
    }

    public void setAdMarvelInterstitialAdsInstance(AdMarvelInterstitialAds adMarvelInterstitialAds) {
        this.ai = new WeakReference(adMarvelInterstitialAds);
    }
}
