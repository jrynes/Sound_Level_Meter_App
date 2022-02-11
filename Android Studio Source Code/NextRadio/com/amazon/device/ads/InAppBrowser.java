package com.amazon.device.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.amazon.device.ads.AdActivity.IAdActivityAdapter;
import com.amazon.device.ads.ThreadUtils.ThreadRunner;
import java.util.concurrent.atomic.AtomicBoolean;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

class InAppBrowser implements IAdActivityAdapter {
    protected static final int BUTTON_SIZE_DP = 50;
    private static final String CONTENT_DESCRIPTION_BACK_BUTTON = "inAppBrowserBackButton";
    private static final String CONTENT_DESCRIPTION_BUTTON_LAYOUT = "inAppBrowserButtonLayout";
    private static final String CONTENT_DESCRIPTION_CLOSE_BUTTON = "inAppBrowserCloseButton";
    private static final String CONTENT_DESCRIPTION_FORWARD_BUTTON = "inAppBrowserForwardButton";
    private static final String CONTENT_DESCRIPTION_HORZ_RULE = "inAppBrowserHorizontalRule";
    private static final String CONTENT_DESCRIPTION_MAIN_LAYOUT = "inAppBrowserMainLayout";
    private static final String CONTENT_DESCRIPTION_OPEN_EXT_BRWSR_BUTTON = "inAppBrowserOpenExternalBrowserButton";
    private static final String CONTENT_DESCRIPTION_REFRESH_BUTTON = "inAppBrowserRefreshButton";
    private static final String CONTENT_DESCRIPTION_RELATIVE_LAYOUT = "inAppBrowserRelativeLayout";
    private static final String CONTENT_DESCRIPTION_WEB_VIEW = "inAppBrowserWebView";
    protected static final int HORIZONTAL_RULE_SIZE_DP = 3;
    protected static final String LOGTAG;
    protected static final String SHOW_OPEN_EXTERNAL_BROWSER_BTN = "extra_open_btn";
    protected static final String URL_EXTRA = "extra_url";
    private Activity activity;
    private final Assets assets;
    private ImageButton browserBackButton;
    private ImageButton browserForwardButton;
    private final AtomicBoolean buttonsCreated;
    private ImageButton closeButton;
    private final MobileAdsCookieManager cookieManager;
    private final MobileAdsInfoStore infoStore;
    private final LayoutFactory layoutFactory;
    private final MobileAdsLogger logger;
    private ImageButton openExternalBrowserButton;
    private ImageButton refreshButton;
    private final Settings settings;
    private boolean showOpenExternalBrowserButton;
    private final ThreadRunner threadRunner;
    private final WebUtils2 webUtils;
    private WebView webView;
    private final WebViewFactory webViewFactory;

    /* renamed from: com.amazon.device.ads.InAppBrowser.1 */
    class C03041 extends WebViewClient {
        C03041() {
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            InAppBrowser.this.logger.m646w("InApp Browser error: %s", description);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (StringUtils.isNullOrWhiteSpace(url)) {
                return false;
            }
            String scheme = InAppBrowser.this.webUtils.getScheme(url);
            if (scheme.equals("http") || scheme.equals("https")) {
                return false;
            }
            return InAppBrowser.this.webUtils.launchActivityForIntentLink(url, InAppBrowser.this.activity);
        }
    }

    /* renamed from: com.amazon.device.ads.InAppBrowser.2 */
    class C03052 extends WebChromeClient {
        C03052() {
        }

        public void onProgressChanged(WebView view, int progress) {
            Activity activity = (Activity) view.getContext();
            activity.setTitle("Loading...");
            activity.setProgress(progress * 100);
            if (progress == 100) {
                activity.setTitle(view.getUrl());
            }
            InAppBrowser.this.updateNavigationButtons(view);
        }
    }

    /* renamed from: com.amazon.device.ads.InAppBrowser.3 */
    class C03063 implements OnClickListener {
        C03063() {
        }

        public void onClick(View view) {
            if (InAppBrowser.this.webView.canGoBack()) {
                InAppBrowser.this.webView.goBack();
            }
        }
    }

    /* renamed from: com.amazon.device.ads.InAppBrowser.4 */
    class C03074 implements OnClickListener {
        C03074() {
        }

        public void onClick(View view) {
            if (InAppBrowser.this.webView.canGoForward()) {
                InAppBrowser.this.webView.goForward();
            }
        }
    }

    /* renamed from: com.amazon.device.ads.InAppBrowser.5 */
    class C03085 implements OnClickListener {
        C03085() {
        }

        public void onClick(View view) {
            InAppBrowser.this.webView.reload();
        }
    }

    /* renamed from: com.amazon.device.ads.InAppBrowser.6 */
    class C03096 implements OnClickListener {
        C03096() {
        }

        public void onClick(View view) {
            InAppBrowser.this.activity.finish();
        }
    }

    /* renamed from: com.amazon.device.ads.InAppBrowser.7 */
    class C03107 implements OnClickListener {
        final /* synthetic */ String val$originalUrl;

        C03107(String str) {
            this.val$originalUrl = str;
        }

        public void onClick(View view) {
            String currentUrl = InAppBrowser.this.webView.getUrl();
            if (currentUrl == null) {
                String msg = "The current URL is null. Reverting to the original URL for external browser.";
                InAppBrowser.this.logger.m645w("The current URL is null. Reverting to the original URL for external browser.");
                currentUrl = this.val$originalUrl;
            }
            InAppBrowser.this.webUtils.launchActivityForIntentLink(currentUrl, InAppBrowser.this.webView.getContext());
        }
    }

    public static class InAppBrowserBuilder {
        private static final String LOGTAG;
        private final Assets assets;
        private Context context;
        private final MobileAdsLogger logger;
        private boolean showOpenExternalBrowserButton;
        private String url;

        static {
            LOGTAG = InAppBrowserBuilder.class.getSimpleName();
        }

        public InAppBrowserBuilder() {
            this(Assets.getInstance(), new MobileAdsLoggerFactory());
        }

        InAppBrowserBuilder(Assets assets, MobileAdsLoggerFactory loggerFactory) {
            this.assets = assets;
            this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        }

        public InAppBrowserBuilder withContext(Context context) {
            this.context = context;
            return this;
        }

        public InAppBrowserBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public InAppBrowserBuilder withExternalBrowserButton() {
            this.showOpenExternalBrowserButton = true;
            return this;
        }

        public void show() {
            if (this.context == null) {
                throw new IllegalArgumentException("Context must not be null");
            } else if (StringUtils.isNullOrWhiteSpace(this.url)) {
                throw new IllegalArgumentException("Url must not be null or white space");
            } else if (this.assets.ensureAssetsCreated()) {
                Intent intent = new Intent(this.context, AdActivity.class);
                intent.putExtra("adapter", InAppBrowser.class.getName());
                intent.putExtra(InAppBrowser.URL_EXTRA, this.url);
                intent.putExtra(InAppBrowser.SHOW_OPEN_EXTERNAL_BROWSER_BTN, this.showOpenExternalBrowserButton);
                intent.addFlags(268435456);
                this.context.startActivity(intent);
            } else {
                this.logger.m640e("Could not load application assets, failed to open URI: %s", this.url);
            }
        }
    }

    class LoadButtonsTask extends MobileAdsAsyncTask<Void, Void, Void> {
        private final int buttonHeight;
        private final int buttonWidth;
        private final Intent intent;
        private final ViewGroup layout;

        public LoadButtonsTask(Intent intent, ViewGroup layout, int buttonWidth, int buttonHeight) {
            this.intent = intent;
            this.layout = layout;
            this.buttonWidth = buttonWidth;
            this.buttonHeight = buttonHeight;
        }

        protected Void doInBackground(Void... params) {
            InAppBrowser.this.browserBackButton = InAppBrowser.this.createButton(InAppBrowser.this.assets.getFilePath(Assets.LEFT_ARROW), 9, -1, this.buttonWidth, this.buttonHeight);
            InAppBrowser.this.browserBackButton.setContentDescription(InAppBrowser.CONTENT_DESCRIPTION_BACK_BUTTON);
            InAppBrowser.this.browserBackButton.setId(10537);
            InAppBrowser.this.browserForwardButton = InAppBrowser.this.createButton(InAppBrowser.this.assets.getFilePath(Assets.RIGHT_ARROW), 1, InAppBrowser.this.browserBackButton.getId(), this.buttonWidth, this.buttonHeight);
            InAppBrowser.this.browserForwardButton.setContentDescription(InAppBrowser.CONTENT_DESCRIPTION_FORWARD_BUTTON);
            InAppBrowser.this.browserForwardButton.setId(10794);
            InAppBrowser.this.closeButton = InAppBrowser.this.createButton(InAppBrowser.this.assets.getFilePath(Assets.CLOSE), 11, -1, this.buttonWidth, this.buttonHeight);
            InAppBrowser.this.closeButton.setContentDescription(InAppBrowser.CONTENT_DESCRIPTION_CLOSE_BUTTON);
            if (InAppBrowser.this.showOpenExternalBrowserButton) {
                InAppBrowser.this.openExternalBrowserButton = InAppBrowser.this.createButton(InAppBrowser.this.assets.getFilePath(Assets.OPEN_EXTERNAL_BROWSER), 1, InAppBrowser.this.browserForwardButton.getId(), this.buttonWidth, this.buttonHeight);
                InAppBrowser.this.openExternalBrowserButton.setContentDescription(InAppBrowser.CONTENT_DESCRIPTION_OPEN_EXT_BRWSR_BUTTON);
                InAppBrowser.this.openExternalBrowserButton.setId(10795);
                InAppBrowser.this.refreshButton = InAppBrowser.this.createButton(InAppBrowser.this.assets.getFilePath(Assets.REFRESH), 1, InAppBrowser.this.openExternalBrowserButton.getId(), this.buttonWidth, this.buttonHeight);
            } else {
                InAppBrowser.this.refreshButton = InAppBrowser.this.createButton(InAppBrowser.this.assets.getFilePath(Assets.REFRESH), 1, InAppBrowser.this.browserForwardButton.getId(), this.buttonWidth, this.buttonHeight);
            }
            InAppBrowser.this.refreshButton.setContentDescription(InAppBrowser.CONTENT_DESCRIPTION_REFRESH_BUTTON);
            return null;
        }

        protected void onPostExecute(Void param) {
            this.layout.addView(InAppBrowser.this.browserBackButton);
            this.layout.addView(InAppBrowser.this.browserForwardButton);
            this.layout.addView(InAppBrowser.this.refreshButton);
            this.layout.addView(InAppBrowser.this.closeButton);
            if (InAppBrowser.this.showOpenExternalBrowserButton) {
                this.layout.addView(InAppBrowser.this.openExternalBrowserButton);
            }
            InAppBrowser.this.initializeButtons(this.intent);
            InAppBrowser.this.buttonsCreated.set(true);
        }
    }

    static {
        LOGTAG = InAppBrowser.class.getSimpleName();
    }

    InAppBrowser() {
        this(new WebUtils2(), WebViewFactory.getInstance(), new MobileAdsLoggerFactory(), MobileAdsInfoStore.getInstance(), Settings.getInstance(), Assets.getInstance(), new LayoutFactory(), new MobileAdsCookieManager(), ThreadUtils.getThreadRunner());
    }

    InAppBrowser(WebUtils2 webUtils, WebViewFactory webViewFactory, MobileAdsLoggerFactory loggerFactory, MobileAdsInfoStore infoStore, Settings settings, Assets assets, LayoutFactory layoutFactory, MobileAdsCookieManager cookieManager, ThreadRunner threadRunner) {
        this.buttonsCreated = new AtomicBoolean(false);
        this.webUtils = webUtils;
        this.webViewFactory = webViewFactory;
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        this.infoStore = infoStore;
        this.settings = settings;
        this.assets = assets;
        this.layoutFactory = layoutFactory;
        this.cookieManager = cookieManager;
        this.threadRunner = threadRunner;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void onCreate() {
        this.activity.getWindow().requestFeature(2);
        this.activity.getWindow().setFeatureInt(2, -1);
        Intent intent = this.activity.getIntent();
        this.showOpenExternalBrowserButton = intent.getBooleanExtra(SHOW_OPEN_EXTERNAL_BROWSER_BTN, false);
        initialize(intent);
        initializeWebView(intent);
        enableCookies();
    }

    @SuppressLint({"InlinedApi"})
    private void initialize(Intent intent) {
        DisplayMetrics metrics = new DisplayMetrics();
        getMetrics(metrics);
        float mDensity = metrics.density;
        int buttonHeight = (int) ((50.0f * mDensity) + 0.5f);
        int ruleSize = (int) ((PDF417.PREFERRED_RATIO * mDensity) + 0.5f);
        int buttonWidth = Math.min(metrics.widthPixels / (this.showOpenExternalBrowserButton ? 5 : 4), buttonHeight * 2);
        ViewGroup layout = this.layoutFactory.createLayout(this.activity, LayoutType.RELATIVE_LAYOUT, CONTENT_DESCRIPTION_BUTTON_LAYOUT);
        layout.setId(10280);
        LayoutParams rp = new LayoutParams(-1, buttonHeight + ruleSize);
        rp.addRule(12);
        layout.setLayoutParams(rp);
        layout.setBackgroundColor(-986896);
        this.threadRunner.executeAsyncTask(new LoadButtonsTask(intent, layout, buttonWidth, buttonHeight), new Void[0]);
        View rule = new View(this.activity);
        rule.setContentDescription(CONTENT_DESCRIPTION_HORZ_RULE);
        rule.setBackgroundColor(-3355444);
        LayoutParams params = new LayoutParams(-1, ruleSize);
        params.addRule(10);
        rule.setLayoutParams(params);
        layout.addView(rule);
        this.webView = this.webViewFactory.createWebView(this.activity);
        this.webView.getSettings().setUserAgentString(this.infoStore.getDeviceInfo().getUserAgentString() + "-inAppBrowser");
        this.webView.setContentDescription(CONTENT_DESCRIPTION_WEB_VIEW);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(2, layout.getId());
        this.webView.setLayoutParams(layoutParams);
        ViewGroup rl = this.layoutFactory.createLayout(this.activity, LayoutType.RELATIVE_LAYOUT, CONTENT_DESCRIPTION_RELATIVE_LAYOUT);
        rl.setLayoutParams(new LayoutParams(-1, -2));
        rl.addView(this.webView);
        rl.addView(layout);
        LinearLayout mainll = (LinearLayout) this.layoutFactory.createLayout(this.activity, LayoutType.LINEAR_LAYOUT, CONTENT_DESCRIPTION_MAIN_LAYOUT);
        mainll.setOrientation(1);
        mainll.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        mainll.addView(rl);
        this.activity.setContentView(mainll);
    }

    private ImageButton createButton(String src, int verb, int anchor, int buttonWidth, int buttonHeight) {
        ImageButton button = new ImageButton(this.activity);
        button.setImageBitmap(BitmapFactory.decodeFile(src));
        LayoutParams params = new LayoutParams(buttonWidth, buttonHeight);
        params.addRule(verb, anchor);
        params.addRule(12);
        button.setLayoutParams(params);
        button.setBackgroundColor(0);
        button.setScaleType(ScaleType.FIT_CENTER);
        return button;
    }

    private void initializeWebView(Intent intent) {
        this.webViewFactory.setJavaScriptEnabledForWebView(true, this.webView, LOGTAG);
        this.webView.loadUrl(intent.getStringExtra(URL_EXTRA));
        this.webView.setWebViewClient(new C03041());
        this.webView.setWebChromeClient(new C03052());
    }

    private void initializeButtons(Intent intent) {
        this.browserBackButton.setOnClickListener(new C03063());
        this.browserForwardButton.setOnClickListener(new C03074());
        this.refreshButton.setOnClickListener(new C03085());
        this.closeButton.setOnClickListener(new C03096());
        if (this.showOpenExternalBrowserButton) {
            this.openExternalBrowserButton.setOnClickListener(new C03107(intent.getStringExtra(URL_EXTRA)));
        }
    }

    private void updateNavigationButtons(WebView view) {
        if (this.browserBackButton != null && this.browserForwardButton != null) {
            if (view.canGoBack()) {
                AndroidTargetUtils.setImageButtonAlpha(this.browserBackButton, Type.ANY);
            } else {
                AndroidTargetUtils.setImageButtonAlpha(this.browserBackButton, Service.ISO_TSAP);
            }
            if (view.canGoForward()) {
                AndroidTargetUtils.setImageButtonAlpha(this.browserForwardButton, Type.ANY);
            } else {
                AndroidTargetUtils.setImageButtonAlpha(this.browserForwardButton, Service.ISO_TSAP);
            }
        }
    }

    private void enableCookies() {
        this.cookieManager.createCookieSyncManager(this.activity);
        this.cookieManager.startSync();
    }

    protected boolean getShouldPauseWebViewTimers() {
        return this.settings.getBoolean(Settings.SETTING_ENABLE_WEBVIEW_PAUSE_LOGIC, false);
    }

    protected boolean canPauseWebViewTimers() {
        return this.webView != null && getShouldPauseWebViewTimers();
    }

    public void onPause() {
        this.logger.m637d("onPause");
        pauseWebView();
        if (canPauseWebViewTimers()) {
            this.webView.pauseTimers();
        }
        this.cookieManager.stopSync();
    }

    void pauseWebView() {
        this.webView.onPause();
    }

    protected boolean canResumeWebViewTimers() {
        return this.webView != null && getShouldPauseWebViewTimers();
    }

    public void onResume() {
        this.logger.m637d("onResume");
        resumeWebView();
        if (canResumeWebViewTimers()) {
            this.webView.resumeTimers();
        }
        this.cookieManager.startSync();
    }

    void resumeWebView() {
        this.webView.onResume();
    }

    public void onStop() {
    }

    public void preOnCreate() {
    }

    void getMetrics(DisplayMetrics metrics) {
        ((WindowManager) this.activity.getSystemService("window")).getDefaultDisplay().getMetrics(metrics);
    }

    public void onConfigurationChanged(Configuration configuration) {
        DisplayMetrics metrics = new DisplayMetrics();
        getMetrics(metrics);
        int buttonHeight = (int) ((50.0f * metrics.density) + 0.5f);
        int buttonWidth = Math.min(metrics.widthPixels / (this.showOpenExternalBrowserButton ? 5 : 4), buttonHeight * 2);
        this.logger.m637d("Width: " + metrics.widthPixels + " ButtonWidth: " + buttonWidth);
        LayoutParams params = new LayoutParams(buttonWidth, buttonHeight);
        if (this.browserBackButton != null) {
            params.addRule(9);
            params.addRule(12);
            this.browserBackButton.setLayoutParams(params);
        }
        if (this.browserForwardButton != null) {
            params = new LayoutParams(buttonWidth, buttonHeight);
            params.addRule(1, this.browserBackButton.getId());
            params.addRule(12);
            this.browserForwardButton.setLayoutParams(params);
        }
        if (this.closeButton != null) {
            params = new LayoutParams(buttonWidth, buttonHeight);
            params.addRule(11);
            params.addRule(12);
            this.closeButton.setLayoutParams(params);
        }
        if (this.openExternalBrowserButton != null) {
            params = new LayoutParams(buttonWidth, buttonHeight);
            params.addRule(1, this.browserForwardButton.getId());
            params.addRule(12);
            this.openExternalBrowserButton.setLayoutParams(params);
            if (this.refreshButton != null) {
                params = new LayoutParams(buttonWidth, buttonHeight);
                params.addRule(1, this.openExternalBrowserButton.getId());
                params.addRule(12);
                this.refreshButton.setLayoutParams(params);
            }
        } else if (this.refreshButton != null) {
            params = new LayoutParams(buttonWidth, buttonHeight);
            params.addRule(1, this.browserForwardButton.getId());
            params.addRule(12);
            this.refreshButton.setLayoutParams(params);
        }
    }

    public boolean onBackPressed() {
        return false;
    }
}
