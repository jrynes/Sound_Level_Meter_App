package com.onelouder.adlib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.admarvel.android.ads.Constants;
import java.util.HashMap;

@SuppressLint({"SetJavaScriptEnabled"})
public class ProxyVirool extends BaseAdProxy {
    private static int instanceId;
    private String _tag;
    private WebView mAdView;

    class MyWebChromeClient extends WebChromeClient {
        MyWebChromeClient() {
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
                Diagnostics.m1951d(ProxyVirool.this.TAG(), "onExceededDatabaseQuota");
            }
            if (estimatedSize < 10485760) {
                if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.m1951d(ProxyVirool.this.TAG(), "MyWebChromeClient - update quota to estimatedSize=" + estimatedSize);
                }
                quotaUpdater.updateQuota(estimatedSize);
                return;
            }
            Diagnostics.m1957w(ProxyVirool.this.TAG(), "MyWebChromeClient - quota exceeded");
        }
    }

    class MyWebViewClient extends WebViewClient {
        MyWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(ProxyVirool.this.TAG(), "shouldOverrideUrlLoading, url=" + url);
            }
            return false;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(ProxyVirool.this.TAG(), "onPageStarted, url=" + url);
            }
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(ProxyVirool.this.TAG(), "onPageFinished, url=" + url);
            }
            try {
                if (!ProxyVirool.this.mAdPlacement.ispaused()) {
                    ProxyVirool.this.mAdPlacement.setTimestamp(System.currentTimeMillis());
                    ProxyVirool.this.mAdViewParent.resumeAdView(true);
                    super.onPageFinished(view, url);
                } else if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.m1952e(ProxyVirool.this.TAG(), "isPaused=true, ignoring ad");
                }
            } catch (Throwable e) {
                Diagnostics.m1953e(ProxyVirool.this.TAG(), e);
            }
        }
    }

    class localWebView extends WebView {
        public localWebView(Context context) {
            super(context);
        }

        protected void onDetachedFromWindow() {
            AdActivity.onPauseWebview(this);
            super.onDetachedFromWindow();
        }
    }

    static {
        instanceId = 0;
    }

    protected String TAG() {
        if (this._tag == null) {
            this._tag = "ProxyVirool-" + instanceId;
        }
        return this._tag;
    }

    @SuppressLint({"InlinedApi"})
    ProxyVirool(Context context, AdPlacement placement, AdView parent) {
        super(context, placement, parent);
        instanceId++;
        this.mAdView = new localWebView(context);
        this.mAdView.setFocusable(false);
        this.mAdView.setFocusableInTouchMode(false);
        this.mAdView.getSettings().setJavaScriptEnabled(true);
        this.mAdView.getSettings().setPluginState(PluginState.ON_DEMAND);
        this.mAdView.getSettings().setDomStorageEnabled(true);
        this.mAdView.getSettings().setAppCacheEnabled(true);
        this.mAdView.getSettings().setDatabaseEnabled(true);
        this.mAdView.setWebViewClient(new MyWebViewClient());
        this.mAdView.setWebChromeClient(new MyWebChromeClient());
    }

    ProxyVirool(Activity activity, AdPlacement placement) {
        super(activity, placement);
    }

    public View getProxiedView() {
        return this.mAdView;
    }

    public void destroy() {
        Diagnostics.m1951d(TAG(), "destroy");
        if (this.mAdView != null) {
            this.mAdView = null;
        }
    }

    public void invalidate() {
        if (this.mAdView != null) {
            this.mAdView.invalidate();
        }
    }

    public void requestAd(HashMap<String, Object> targetParams) {
        Diagnostics.m1951d(TAG(), "requestAd");
        try {
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(TAG(), "siteid=" + this.mAdPlacement.getSiteid());
            }
            logTargetParams(targetParams);
            this.mAdPlacement.sendBroadcast(getContext(), AdPlacement.ACTION_1L_ADVIEW_REQUESTED, null);
            SendAdUsage.trackEvent(getContext(), this.mAdPlacement, Constants.AD_REQUEST, targetParams, null);
            this.mAdView.loadUrl("http://api.virool.com/widgets/mobile_offer/SITEKEY?height=190&suid=123&width=290".replace("SITEKEY", this.mAdPlacement.getSiteid()));
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public void requestInterstitial(Activity activity, HashMap<String, Object> hashMap) {
        Diagnostics.m1951d(TAG(), "requestInterstitial");
        try {
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1951d(TAG(), "siteid=" + this.mAdPlacement.getSiteid());
            }
            this.mAdView.loadUrl("http://api.virool.com/widgets/mobile_offer/SITEKEY?height=190&suid=123&width=290".replace("SITEKEY", this.mAdPlacement.getSiteid()));
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public boolean isInterstitialReady() {
        return false;
    }

    public void displayInterstitial(Activity activity) {
    }
}
