package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebView;
import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: AdMarvelInternalWebView */
class ab {
    @SuppressLint({"SetJavaScriptEnabled"})
    static void m253a(WebView webView, Context context, AtomicBoolean atomicBoolean) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(false);
        if (atomicBoolean.get()) {
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
        }
    }
}
