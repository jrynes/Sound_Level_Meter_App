package com.onelouder.adlib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.admarvel.android.ads.Constants;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.WKSRecord.Service;

@SuppressLint({"SetJavaScriptEnabled", "InlinedApi"})
public class AdActivity extends Activity {
    private static final String TAG = "AdActivity";
    private static HashMap<String, Bitmap> mBitmaps;
    private BitmapDrawable drawableBack;
    private BitmapDrawable drawableBackDis;
    private BitmapDrawable drawableFwrd;
    private BitmapDrawable drawableFwrdDis;
    private BitmapDrawable drawableRefresh1;
    private BitmapDrawable drawableRefresh2;
    private BitmapDrawable drawableRefresh3;
    private BitmapDrawable drawableRefresh4;
    private BitmapDrawable drawableRefresh5;
    private BitmapDrawable drawableRefresh6;
    private BitmapDrawable drawableStop;
    private boolean initialUrlRequest;
    private ProgressBar load_progress;
    private Handler mHandler;
    Runnable mHideNavActionsRunnable;
    private String mOriginalUrl;
    Runnable mShowNavActionsRunnable;
    private String mUrl;
    private int nInitialState;
    private int nUpdateDepth;
    private ImageView navaction_back;
    private ImageView navaction_forward;
    private ImageView navaction_refresh;
    private ImageView navaction_stop;
    private RelativeLayout navactions;
    private ImageView progress;
    private View refresh;
    private AnimationDrawable refreshAnimation;
    private WebView webview;

    /* renamed from: com.onelouder.adlib.AdActivity.1 */
    class C12721 implements OnClickListener {
        C12721() {
        }

        public void onClick(View v) {
            AdActivity.this.webview.goBack();
        }
    }

    /* renamed from: com.onelouder.adlib.AdActivity.2 */
    class C12732 implements OnClickListener {
        C12732() {
        }

        public void onClick(View v) {
            AdActivity.this.webview.goForward();
        }
    }

    /* renamed from: com.onelouder.adlib.AdActivity.3 */
    class C12743 implements OnClickListener {
        C12743() {
        }

        public void onClick(View v) {
            AdActivity.this.webview.reload();
        }
    }

    /* renamed from: com.onelouder.adlib.AdActivity.4 */
    class C12754 implements OnClickListener {
        C12754() {
        }

        public void onClick(View v) {
            AdActivity.this.webview.stopLoading();
        }
    }

    /* renamed from: com.onelouder.adlib.AdActivity.5 */
    class C12765 implements Runnable {
        C12765() {
        }

        public void run() {
            Utils.slideInUp(AdActivity.this.navactions, null, 0);
        }
    }

    /* renamed from: com.onelouder.adlib.AdActivity.6 */
    class C12776 implements Runnable {
        C12776() {
        }

        public void run() {
            if (AdActivity.this.progress == null || AdActivity.this.progress.getVisibility() != 0) {
                Utils.slideOutDown(AdActivity.this.navactions, null, 0);
            } else if (AdActivity.this.mHandler != null) {
                AdActivity.this.mHandler.postDelayed(AdActivity.this.mHideNavActionsRunnable, 3000);
            }
        }
    }

    /* renamed from: com.onelouder.adlib.AdActivity.7 */
    class C12787 implements Runnable {
        C12787() {
        }

        public void run() {
            AdActivity.this.webview.getSettings().setSupportZoom(true);
            AdActivity.this.webview.getSettings().setBuiltInZoomControls(true);
            if (VERSION.SDK_INT >= 11) {
                AdActivity.this.webview.getSettings().setDisplayZoomControls(false);
            }
            AdActivity.this.webview.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
            AdActivity.this.webview.getSettings().setJavaScriptEnabled(true);
            AdActivity.this.webview.getSettings().setPluginState(PluginState.ON_DEMAND);
            AdActivity.this.webview.getSettings().setUseWideViewPort(true);
            AdActivity.this.webview.getSettings().setLoadWithOverviewMode(true);
            AdActivity.this.webview.getSettings().setGeolocationEnabled(true);
            AdActivity.this.webview.getSettings().setDomStorageEnabled(true);
            AdActivity.this.webview.getSettings().setAppCacheEnabled(true);
            AdActivity.this.webview.getSettings().setDatabaseEnabled(true);
            AdActivity.this.webview.setWebViewClient(new MyWebViewClient());
            AdActivity.this.webview.setWebChromeClient(new MyWebChromeClient());
            AdActivity.this.webview.getSettings().setDatabasePath(AdActivity.this.webview.getContext().getDir("database", 0).getPath());
            AdActivity.this.webview.loadUrl(AdActivity.this.mUrl);
            AdActivity.this.showWebView();
        }
    }

    static class ImageCallback extends AbsLoadImageCallback {
        String imagename;
        Context mContext;
        String url;

        public ImageCallback(Context context, String image, Object v) {
            super(null, null, v);
            this.url = "https://advrts.s3.amazonaws.com/sdk2/IMAGE.png";
            this.url = this.url.replace("IMAGE", image);
            this.imagename = image;
            this.mContext = context;
        }

        public String getUrl() {
            return this.url;
        }

        protected void onExisting(Bitmap bitmap) {
            onReady(bitmap);
        }

        protected void onReady(Bitmap bitmap) {
            try {
                bitmap.setDensity(240);
                if (AdActivity.mBitmaps == null) {
                    AdActivity.mBitmaps = new HashMap();
                }
                AdActivity.mBitmaps.put(this.imagename, bitmap);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        MyWebChromeClient() {
        }

        public void onProgressChanged(WebView view, int progress) {
            if (AdActivity.this.initialUrlRequest) {
                AdActivity.this.load_progress.setProgress(progress);
            }
        }

        public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
            callback.invoke(origin, true, false);
        }

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return true;
        }

        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return true;
        }

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return true;
        }

        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, QuotaUpdater quotaUpdater) {
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(AdActivity.TAG, "onExceededDatabaseQuota");
            }
            if (estimatedSize < 10485760) {
                if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.m1951d(AdActivity.TAG, "MyWebChromeClient - update quota to estimatedSize=" + estimatedSize);
                }
                quotaUpdater.updateQuota(estimatedSize);
                return;
            }
            Diagnostics.m1957w(AdActivity.TAG, "MyWebChromeClient - quota exceeded");
        }
    }

    class MyWebViewClient extends WebViewClient {
        MyWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.m1951d(AdActivity.TAG, "shouldOverrideUrlLoading, url=" + url);
                }
                url = url.replace("admarvelsdkexternal", Stomp.EMPTY).replace("admarvelexternal", Stomp.EMPTY);
                Intent intent;
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    boolean isVideo = false;
                    try {
                        int idxQuestionmark = url.indexOf("?");
                        if (idxQuestionmark != -1) {
                            String temp = url.substring(0, idxQuestionmark);
                            if (temp.endsWith(".mp4") || temp.endsWith(".m3u8")) {
                                isVideo = true;
                            }
                        }
                        if (isVideo) {
                            intent = new Intent("android.intent.action.VIEW");
                            intent.setFlags(268435456);
                            if (Build.MANUFACTURER.equals("HTC")) {
                                intent.setDataAndType(Uri.parse(url), WebRequest.CONTENT_TYPE_HTML);
                            } else {
                                intent.setDataAndType(Uri.parse(url), "video/mp4");
                            }
                            if (intent.resolveActivity(AdActivity.this.getPackageManager()) != null) {
                                AdActivity.this.startActivity(intent);
                            }
                            view.stopLoading();
                            AdActivity.this.finish();
                            return true;
                        }
                    } catch (Throwable e) {
                        Diagnostics.m1953e(AdActivity.TAG, e);
                    }
                    return false;
                }
                intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(AdActivity.this.getPackageManager()) != null) {
                    AdActivity.this.startActivity(intent);
                }
                AdActivity.this.finish();
                return true;
            } catch (Throwable e2) {
                Diagnostics.m1953e(AdActivity.TAG, e2);
            }
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(AdActivity.TAG, "onPageStarted, url=" + url);
            }
            AdActivity.this.navaction_refresh.setVisibility(8);
            AdActivity.this.navaction_stop.setVisibility(0);
            AdActivity.this.showUpdateProgress(true);
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(AdActivity.TAG, "onPageFinished, url=" + url);
            }
            if (!AdActivity.this.isFinishing()) {
                super.onPageFinished(view, url);
                AdActivity.this.mUrl = url;
                if (view.canGoBack()) {
                    AdActivity.this.navaction_back.setImageDrawable(AdActivity.this.drawableBack);
                    AdActivity.this.navaction_back.setEnabled(true);
                } else {
                    AdActivity.this.navaction_back.setImageDrawable(AdActivity.this.drawableBackDis);
                    AdActivity.this.navaction_back.setEnabled(false);
                }
                if (view.canGoForward()) {
                    AdActivity.this.navaction_forward.setImageDrawable(AdActivity.this.drawableFwrd);
                    AdActivity.this.navaction_forward.setEnabled(true);
                } else {
                    AdActivity.this.navaction_forward.setImageDrawable(AdActivity.this.drawableFwrdDis);
                    AdActivity.this.navaction_forward.setEnabled(false);
                }
                AdActivity.this.navaction_refresh.setVisibility(0);
                AdActivity.this.navaction_stop.setVisibility(8);
                if (AdActivity.this.initialUrlRequest) {
                    Utils.fadeOut(AdActivity.this.load_progress, null, 0, 150);
                    AdActivity.this.initialUrlRequest = false;
                }
                AdActivity.this.nUpdateDepth = 0;
                AdActivity.this.showUpdateProgress(false);
            }
        }
    }

    class localWebView extends WebView {
        public localWebView(Context context) {
            super(context);
        }

        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case Tokenizer.EOF /*0*/:
                    if (AdActivity.this.navactions.getVisibility() != 8) {
                        AdActivity.this.mHandler.removeCallbacks(AdActivity.this.mHideNavActionsRunnable);
                        AdActivity.this.mHandler.postDelayed(AdActivity.this.mHideNavActionsRunnable, 3000);
                        break;
                    }
                    AdActivity.this.mHandler.post(AdActivity.this.mShowNavActionsRunnable);
                    AdActivity.this.mHandler.postDelayed(AdActivity.this.mHideNavActionsRunnable, 3000);
                    break;
            }
            return super.onTouchEvent(event);
        }
    }

    public AdActivity() {
        this.initialUrlRequest = true;
        this.mHandler = new Handler();
        this.mShowNavActionsRunnable = new C12765();
        this.mHideNavActionsRunnable = new C12776();
        this.nUpdateDepth = 0;
        this.nInitialState = 8;
    }

    public static void updateGraphics(Context context) {
        try {
            Object o;
            if (mBitmaps == null) {
                mBitmaps = new HashMap();
            }
            if (!mBitmaps.containsKey("nav_backward")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_backward", o), o);
            }
            if (!mBitmaps.containsKey("nav_backward_disabled")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_backward_disabled", o), o);
            }
            if (!mBitmaps.containsKey("nav_cancel")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_cancel", o), o);
            }
            if (!mBitmaps.containsKey("nav_forward")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_forward", o), o);
            }
            if (!mBitmaps.containsKey("nav_forward_disabled")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_forward_disabled", o), o);
            }
            if (!mBitmaps.containsKey("nav_refresh_1")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_refresh_1", o), o);
            }
            if (!mBitmaps.containsKey("nav_refresh_2")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_refresh_2", o), o);
            }
            if (!mBitmaps.containsKey("nav_refresh_3")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_refresh_3", o), o);
            }
            if (!mBitmaps.containsKey("nav_refresh_4")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_refresh_4", o), o);
            }
            if (!mBitmaps.containsKey("nav_refresh_5")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_refresh_5", o), o);
            }
            if (!mBitmaps.containsKey("nav_refresh_6")) {
                o = new Object();
                ImageLoader.displayCachedImage(context, new ImageCallback(context, "nav_refresh_6", o), o);
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG, e);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mOriginalUrl = savedInstanceState.getString("orig-url");
            this.mUrl = savedInstanceState.getString(SettingsJsonConstants.APP_URL_KEY);
        } else {
            this.mUrl = getIntent().getStringExtra(SettingsJsonConstants.APP_URL_KEY);
        }
        updateGraphics(this);
        try {
            if (mBitmaps.containsKey("nav_backward")) {
                this.drawableBack = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_backward"));
            }
            if (mBitmaps.containsKey("nav_backward_disabled")) {
                this.drawableBackDis = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_backward_disabled"));
            }
            if (mBitmaps.containsKey("nav_forward")) {
                this.drawableFwrd = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_forward"));
            }
            if (mBitmaps.containsKey("nav_forward_disabled")) {
                this.drawableFwrdDis = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_forward_disabled"));
            }
            if (mBitmaps.containsKey("nav_cancel")) {
                this.drawableStop = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_cancel"));
            }
            if (mBitmaps.containsKey("nav_refresh_1")) {
                this.drawableRefresh1 = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_refresh_1"));
            }
            if (mBitmaps.containsKey("nav_refresh_2")) {
                this.drawableRefresh2 = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_refresh_2"));
            }
            if (mBitmaps.containsKey("nav_refresh_3")) {
                this.drawableRefresh3 = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_refresh_3"));
            }
            if (mBitmaps.containsKey("nav_refresh_4")) {
                this.drawableRefresh4 = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_refresh_4"));
            }
            if (mBitmaps.containsKey("nav_refresh_5")) {
                this.drawableRefresh5 = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_refresh_5"));
            }
            if (mBitmaps.containsKey("nav_refresh_6")) {
                this.drawableRefresh6 = new BitmapDrawable(getResources(), (Bitmap) mBitmaps.get("nav_refresh_6"));
            }
            updateRefreshAnimation();
            RelativeLayout screen = new RelativeLayout(this);
            int padding8 = Utils.getDIP(8.0d);
            int size48 = Utils.getDIP(48.0d);
            this.webview = new localWebView(this);
            screen.addView(this.webview, new LayoutParams(-1, -1));
            this.navaction_back = new ImageView(this);
            this.navaction_back.setImageDrawable(this.drawableBackDis);
            this.navaction_back.setEnabled(false);
            this.navaction_back.setPadding(padding8, padding8, padding8, padding8);
            this.navaction_forward = new ImageView(this);
            this.navaction_forward.setImageDrawable(this.drawableFwrdDis);
            this.navaction_forward.setEnabled(false);
            this.navaction_forward.setPadding(padding8, padding8, padding8, padding8);
            this.navaction_refresh = new ImageView(this);
            this.navaction_refresh.setImageDrawable(this.drawableRefresh1);
            this.navaction_refresh.setPadding(padding8, padding8, padding8, padding8);
            this.navaction_stop = new ImageView(this);
            this.navaction_stop.setImageDrawable(this.drawableStop);
            this.navaction_stop.setPadding(padding8, padding8, padding8, padding8);
            this.progress = new ImageView(this);
            this.progress.setImageDrawable(this.refreshAnimation);
            this.progress.setPadding(padding8, padding8, padding8, padding8);
            this.progress.setVisibility(8);
            this.navactions = new RelativeLayout(this);
            this.navactions.setId(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH);
            LayoutParams rParams = new LayoutParams(-1, size48);
            rParams.addRule(12);
            screen.addView(this.navactions, rParams);
            LinearLayout llayout = new LinearLayout(this);
            llayout.setGravity(Flags.FLAG8);
            llayout.setBackgroundColor(Color.rgb(24, 24, 24));
            this.navactions.addView(llayout, new LayoutParams(-1, -1));
            llayout.addView(this.navaction_back, new LinearLayout.LayoutParams(size48, size48, 1.0f));
            llayout.addView(this.navaction_forward, new LinearLayout.LayoutParams(size48, size48, 1.0f));
            llayout.addView(this.navaction_refresh, new LinearLayout.LayoutParams(size48, size48, 1.0f));
            llayout.addView(this.navaction_stop, new LinearLayout.LayoutParams(size48, size48, 1.0f));
            llayout.addView(this.progress, new LinearLayout.LayoutParams(size48, size48, 1.0f));
            ImageView stroke = new ImageView(this);
            stroke.setBackgroundColor(Color.argb(230, 0, 0, 0));
            rParams = new LayoutParams(-1, Utils.getDIP(1.0d));
            rParams.addRule(10);
            this.navactions.addView(stroke, rParams);
            this.load_progress = new ProgressBar(this, null, 16842872);
            ShapeDrawable pgDrawable = new ShapeDrawable(new RoundRectShape(new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f}, null, null));
            pgDrawable.getPaint().setColor(Color.parseColor("#50bfe9"));
            this.load_progress.setProgressDrawable(new ClipDrawable(pgDrawable, 3, 1));
            this.load_progress.setBackgroundDrawable(getResources().getDrawable(17301612));
            rParams = new LayoutParams(-1, padding8);
            rParams.addRule(2, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH);
            rParams.leftMargin = padding8;
            rParams.rightMargin = padding8;
            rParams.bottomMargin = padding8;
            screen.addView(this.load_progress, rParams);
            this.navaction_back.setOnClickListener(new C12721());
            this.navaction_forward.setOnClickListener(new C12732());
            this.navaction_refresh.setOnClickListener(new C12743());
            this.navaction_stop.setOnClickListener(new C12754());
            setContentView(screen, new ViewGroup.LayoutParams(-1, -1));
            this.mHandler.postDelayed(this.mHideNavActionsRunnable, 3000);
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG, e);
        }
    }

    public void onSaveInstanceState(Bundle b) {
        b.putString("orig-url", this.mOriginalUrl);
        b.putString(SettingsJsonConstants.APP_URL_KEY, this.mUrl);
        super.onSaveInstanceState(b);
    }

    public void onResume() {
        super.onResume();
        if (this.mUrl != null) {
            setUrl(this.mUrl);
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mHideNavActionsRunnable);
            this.mHandler.removeCallbacks(this.mShowNavActionsRunnable);
        }
        onPauseWebview(this.webview);
    }

    public void onStop() {
        super.onStop();
    }

    public void onStart() {
        super.onStart();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && onBackKeyPressed()) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean onBackKeyPressed() {
        try {
            this.webview.stopLoading();
            this.webview.getSettings().setSupportZoom(false);
            this.webview.setWebViewClient(null);
            this.webview.setWebChromeClient(null);
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG, e);
        }
        return false;
    }

    public static void onPauseWebview(WebView webinstance) {
        try {
            Class.forName("android.webkit.WebView").getMethod("onPause", (Class[]) null).invoke(webinstance, (Object[]) null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
        }
    }

    void setUrl(String url) {
        try {
            if (this.mOriginalUrl == null) {
                this.mOriginalUrl = url;
            }
            if (url.startsWith("http://") || url.startsWith("https://")) {
                this.mUrl = url;
            } else {
                this.mUrl = "http://" + url;
            }
            this.navaction_refresh.setVisibility(8);
            this.navaction_stop.setVisibility(0);
            this.webview.setVisibility(8);
            int delayMilliseconds = Constants.ANIMATION_DURATION;
            if (VERSION.SDK_INT >= 16) {
                delayMilliseconds = 300;
            }
            this.mHandler.postDelayed(new C12787(), (long) delayMilliseconds);
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG, e);
        }
    }

    protected void showWebView() {
        if (this.webview != null && this.webview.getVisibility() != 0) {
            this.webview.setVisibility(0);
            this.webview.requestFocus(Service.CISCO_FNA);
        }
    }

    void updateRefreshAnimation() {
        this.refreshAnimation = new AnimationDrawable();
        if (this.drawableRefresh1 != null) {
            this.refreshAnimation.addFrame(this.drawableRefresh1, 100);
        }
        if (this.drawableRefresh2 != null) {
            this.refreshAnimation.addFrame(this.drawableRefresh2, 100);
        }
        if (this.drawableRefresh3 != null) {
            this.refreshAnimation.addFrame(this.drawableRefresh3, 100);
        }
        if (this.drawableRefresh4 != null) {
            this.refreshAnimation.addFrame(this.drawableRefresh4, 100);
        }
        if (this.drawableRefresh5 != null) {
            this.refreshAnimation.addFrame(this.drawableRefresh5, 100);
        }
        if (this.drawableRefresh6 != null) {
            this.refreshAnimation.addFrame(this.drawableRefresh6, 100);
        }
        this.refreshAnimation.setOneShot(false);
    }

    void hideUpdateProgress() {
        this.nUpdateDepth = 0;
        showUpdateProgress(false);
    }

    void showUpdateProgress(boolean show) {
        synchronized (this.mHandler) {
            if (show) {
                if (this.nUpdateDepth == 0) {
                    if (this.progress != null) {
                        this.progress.setVisibility(0);
                    }
                    if (this.refresh != null) {
                        this.nInitialState = this.refresh.getVisibility();
                        this.refresh.setVisibility(8);
                    }
                }
                this.nUpdateDepth++;
            } else {
                this.nUpdateDepth--;
                if (this.nUpdateDepth < 0) {
                    this.nUpdateDepth = 0;
                }
                if (this.nUpdateDepth == 0) {
                    if (this.progress != null) {
                        this.progress.setVisibility(8);
                    }
                    if (this.refresh != null) {
                        this.refresh.setVisibility(this.nInitialState);
                    }
                }
            }
        }
    }
}
