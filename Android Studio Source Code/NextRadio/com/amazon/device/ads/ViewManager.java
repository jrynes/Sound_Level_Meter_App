package com.amazon.device.ads;

import android.content.Context;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.FrameLayout.LayoutParams;
import com.amazon.device.ads.AndroidTargetUtils.AndroidClassAdapter;
import java.util.HashSet;
import java.util.Set;

class ViewManager {
    private static final String CONTENT_DESCRIPTION_NEW_WEBVIEW = "newWebView";
    private static final String CONTENT_DESCRIPTION_ORIGINAL_WEBVIEW = "originalWebView";
    private static final String CONTENT_DESCRIPTION_PRELOADED_WEBVIEW = "preloadedWebView";
    private static final String LOGTAG;
    private final AndroidClassAdapter androidClassAdapter;
    private WebView currentWebView;
    private boolean disableHardwareAcceleration;
    private int gravity;
    private final Set<String> javascriptInterfaceNames;
    private OnKeyListener keyListener;
    private final MobileAdsLogger logger;
    private final ViewGroup parent;
    private WebView preloadedWebView;
    private WebView stashedWebView;
    private WebViewClient webViewClient;
    private final WebViewFactory webViewFactory;
    private int webViewHeight;
    private int webViewWidth;

    /* renamed from: com.amazon.device.ads.ViewManager.1 */
    class C03461 implements Runnable {
        final /* synthetic */ WebView[] val$webViews;

        C03461(WebView[] webViewArr) {
            this.val$webViews = webViewArr;
        }

        public void run() {
            for (WebView webView : this.val$webViews) {
                if (webView != null) {
                    if (webView.getParent() != null) {
                        ((ViewGroup) webView.getParent()).removeView(webView);
                    }
                    try {
                        webView.destroy();
                    } catch (IllegalArgumentException e) {
                        ViewManager.this.logger.m646w("Caught an IllegalArgumentException while destroying a WebView: %s", e.getMessage());
                    }
                }
            }
        }
    }

    private class AdWebChromeClient extends WebChromeClient {
        private AdWebChromeClient() {
        }

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            ViewManager.this.logger.m637d(message);
            return false;
        }
    }

    private class PreloadWebViewClient extends WebViewClient {
        private final PreloadCallback callback;

        public PreloadWebViewClient(PreloadCallback callback) {
            this.callback = callback;
        }

        public void onPageFinished(WebView webView, String url) {
            if (this.callback != null) {
                this.callback.onPreloadComplete(url);
            }
        }
    }

    static {
        LOGTAG = ViewManager.class.getSimpleName();
    }

    public ViewManager(ViewGroup parent) {
        this(parent, WebViewFactory.getInstance(), AndroidTargetUtils.getDefaultAndroidClassAdapter());
    }

    ViewManager(ViewGroup parent, WebViewFactory webViewFactory, AndroidClassAdapter androidClassAdapter) {
        this.webViewHeight = -1;
        this.webViewWidth = -1;
        this.gravity = 17;
        this.disableHardwareAcceleration = false;
        this.javascriptInterfaceNames = new HashSet();
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.parent = parent;
        this.webViewFactory = webViewFactory;
        this.androidClassAdapter = androidClassAdapter;
    }

    public boolean canShowViews() {
        Context context = getContext(this.parent);
        return this.webViewFactory.isWebViewOk(context) && WebViewDatabase.getInstance(context) != null;
    }

    public void initialize() throws IllegalStateException {
        getCurrentWebView();
    }

    private boolean isInitialized() {
        return this.currentWebView != null;
    }

    private WebView getCurrentWebView() {
        if (this.currentWebView == null) {
            WebView webView = createWebView(getContext(this.parent));
            if (validateWebView(webView)) {
                webView.setContentDescription(CONTENT_DESCRIPTION_ORIGINAL_WEBVIEW);
                setWebView(webView, false);
            } else {
                throw new IllegalStateException("Could not create WebView");
            }
        }
        return this.currentWebView;
    }

    void addViewToParent(WebView webView) {
        this.parent.addView(webView);
    }

    public void disableHardwareAcceleration(boolean shouldDisable) {
        this.disableHardwareAcceleration = shouldDisable;
    }

    boolean validateWebView(WebView webView) {
        return webView != null;
    }

    Context getContext(View view) {
        return view.getContext();
    }

    public void listenForKey(OnKeyListener keyListener) {
        this.keyListener = keyListener;
        getCurrentWebView().requestFocus();
        getCurrentWebView().setOnKeyListener(this.keyListener);
    }

    protected void setWebViewLayoutParams(WebView webView, int width, int height, int gravity) {
        if (webView.getLayoutParams() == null) {
            LayoutParams layoutParams = new LayoutParams(width, height);
            layoutParams.gravity = gravity;
            webView.setLayoutParams(layoutParams);
            return;
        }
        webView.getLayoutParams().width = width;
        webView.getLayoutParams().height = height;
        if (webView.getLayoutParams() instanceof LayoutParams) {
            ((LayoutParams) webView.getLayoutParams()).gravity = gravity;
        }
    }

    public void destroy() {
        destroyWebViews(this.currentWebView, this.stashedWebView, this.preloadedWebView);
        this.currentWebView = null;
        this.stashedWebView = null;
        this.preloadedWebView = null;
    }

    public void setWebViewClient(WebViewClient client) {
        this.webViewClient = client;
        if (isInitialized()) {
            getCurrentWebView().setWebViewClient(this.webViewClient);
        }
    }

    public void setHeight(int height) {
        this.webViewHeight = height;
        updateLayoutParamsIfNeeded();
    }

    public void setLayoutParams(int width, int height, int gravity) {
        this.webViewWidth = width;
        this.webViewHeight = height;
        this.gravity = gravity;
        updateLayoutParamsIfNeeded();
    }

    private void updateLayoutParamsIfNeeded() {
        if (isInitialized()) {
            setWebViewLayoutParams(getCurrentWebView(), this.webViewWidth, this.webViewHeight, this.gravity);
        }
    }

    WebView createWebView(Context context) {
        WebView webView = this.webViewFactory.createWebView(context);
        if (!this.webViewFactory.setJavaScriptEnabledForWebView(true, webView, LOGTAG)) {
            return null;
        }
        WebSettings webSettings = webView.getSettings();
        this.androidClassAdapter.withWebSettings(webSettings).setMediaPlaybackRequiresUserGesture(false);
        webView.setScrollContainer(false);
        webView.setBackgroundColor(0);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new AdWebChromeClient());
        webSettings.setDomStorageEnabled(true);
        if (!this.disableHardwareAcceleration) {
            return webView;
        }
        AndroidTargetUtils.disableHardwareAcceleration(webView);
        return webView;
    }

    void setWebView(WebView webView, boolean shouldDestroyPrevious) {
        WebView oldView = this.currentWebView;
        if (oldView != null) {
            oldView.setOnKeyListener(null);
            oldView.setWebViewClient(new WebViewClient());
            this.parent.removeView(oldView);
            if (shouldDestroyPrevious) {
                destroyWebViews(oldView);
            }
        }
        webView.setWebViewClient(this.webViewClient);
        this.currentWebView = webView;
        updateLayoutParamsIfNeeded();
        addViewToParent(this.currentWebView);
        if (this.keyListener != null) {
            listenForKey(this.keyListener);
        }
    }

    public void stashView() {
        WebView webView;
        if (this.stashedWebView != null) {
            destroyWebViews(this.stashedWebView);
        }
        this.stashedWebView = this.currentWebView;
        if (this.preloadedWebView == null) {
            webView = createWebView(this.parent.getContext());
            webView.setContentDescription(CONTENT_DESCRIPTION_NEW_WEBVIEW);
        } else {
            webView = this.preloadedWebView;
            this.preloadedWebView = createWebView(this.parent.getContext());
        }
        setWebView(webView, false);
    }

    public boolean popView() {
        if (this.stashedWebView == null) {
            return false;
        }
        WebView webView = this.stashedWebView;
        this.stashedWebView = null;
        setWebView(webView, true);
        return true;
    }

    public void loadUrl(String url) {
        loadUrl(url, false, null);
    }

    public void loadUrl(String url, boolean shouldPreload, PreloadCallback callback) {
        if (shouldPreload) {
            if (callback != null) {
                getPreloadedWebView().setWebViewClient(new PreloadWebViewClient(callback));
            }
            getPreloadedWebView().loadUrl(url);
            return;
        }
        this.logger.m637d("Loading URL: " + url);
        getCurrentWebView().loadUrl(url);
    }

    public void loadData(String data, String mimeType, String encoding) {
        loadData(data, mimeType, encoding, false, null);
    }

    public void loadData(String data, String mimeType, String encoding, boolean shouldPreload, PreloadCallback callback) {
        if (shouldPreload) {
            if (callback != null) {
                getPreloadedWebView().setWebViewClient(new PreloadWebViewClient(callback));
            }
            getPreloadedWebView().loadData(data, mimeType, encoding);
            return;
        }
        getCurrentWebView().loadData(data, mimeType, encoding);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl, false, null);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl, boolean shouldPreload, PreloadCallback callback) {
        if (shouldPreload) {
            if (callback != null) {
                getPreloadedWebView().setWebViewClient(new PreloadWebViewClient(callback));
            }
            getPreloadedWebView().loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
            return;
        }
        getCurrentWebView().loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    public boolean isCurrentView(View view) {
        return view.equals(this.currentWebView);
    }

    private WebView getPreloadedWebView() {
        if (this.preloadedWebView == null) {
            this.preloadedWebView = createWebView(this.parent.getContext());
            this.preloadedWebView.setContentDescription(CONTENT_DESCRIPTION_PRELOADED_WEBVIEW);
        }
        return this.preloadedWebView;
    }

    public void removePreviousInterfaces() {
        if (this.currentWebView != null) {
            if (AndroidTargetUtils.isAtLeastAndroidAPI(11)) {
                for (String interfaceName : this.javascriptInterfaceNames) {
                    AndroidTargetUtils.removeJavascriptInterface(this.currentWebView, interfaceName);
                }
            } else {
                setWebView(createWebView(this.parent.getContext()), true);
                this.currentWebView.setContentDescription(CONTENT_DESCRIPTION_ORIGINAL_WEBVIEW);
            }
        }
        this.javascriptInterfaceNames.clear();
    }

    public void addJavascriptInterface(Object javascriptInterface, boolean preload, String interfaceName) {
        this.logger.m638d("Add JavaScript Interface %s", interfaceName);
        this.javascriptInterfaceNames.add(interfaceName);
        if (preload) {
            getPreloadedWebView().addJavascriptInterface(javascriptInterface, interfaceName);
        } else {
            getCurrentWebView().addJavascriptInterface(javascriptInterface, interfaceName);
        }
    }

    private void destroyWebViews(WebView... webViews) {
        ThreadUtils.executeOnMainThread(new C03461(webViews));
    }
}
