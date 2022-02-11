package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebView;
import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: AdMarvelInternalWebView */
class aa {
    @SuppressLint({"SetJavaScriptEnabled"})
    static void m252a(WebView webView, Context context, AtomicBoolean atomicBoolean) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(false);
        webView.setInitialScale(100);
        if (atomicBoolean.get()) {
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
        }
    }
}
