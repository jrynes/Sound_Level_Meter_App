package com.facebook.ads.internal.view;

import android.content.Context;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.facebook.ads.internal.adapters.C0437e;
import com.facebook.ads.internal.adapters.C0437e.C0415a;
import com.facebook.ads.internal.util.C0512a;
import com.facebook.ads.internal.util.C0522g;
import com.facebook.ads.internal.util.C0523h;

/* renamed from: com.facebook.ads.internal.view.a */
public class C0542a extends C0541d {
    private final C0442a f1958a;
    private C0437e f1959b;

    /* renamed from: com.facebook.ads.internal.view.a.a */
    public interface C0442a {
        void m1204a();

        void m1205a(int i);

        void m1206a(String str);

        void m1207b();
    }

    /* renamed from: com.facebook.ads.internal.view.a.1 */
    class C05381 extends C0415a {
        final /* synthetic */ C0442a f1952a;
        final /* synthetic */ C0542a f1953b;

        C05381(C0542a c0542a, C0442a c0442a) {
            this.f1953b = c0542a;
            this.f1952a = c0442a;
        }

        public void m1575a() {
            this.f1952a.m1207b();
        }
    }

    /* renamed from: com.facebook.ads.internal.view.a.b */
    private class C0539b extends WebViewClient {
        final /* synthetic */ C0542a f1954a;

        private C0539b(C0542a c0542a) {
            this.f1954a = c0542a;
        }

        public void onReceivedSslError(WebView webView, @NonNull SslErrorHandler sslErrorHandler, SslError sslError) {
            if (C0522g.m1538a()) {
                sslErrorHandler.proceed();
            } else {
                sslErrorHandler.cancel();
            }
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            this.f1954a.f1958a.m1206a(str);
            return true;
        }
    }

    /* renamed from: com.facebook.ads.internal.view.a.c */
    public class C0540c {
        final /* synthetic */ C0542a f1955a;
        private final String f1956b;

        public C0540c(C0542a c0542a) {
            this.f1955a = c0542a;
            this.f1956b = C0540c.class.getSimpleName();
        }

        @JavascriptInterface
        public void alert(String str) {
            Log.e(this.f1956b, str);
        }

        @JavascriptInterface
        public String getAnalogInfo() {
            return C0522g.m1531a(C0512a.m1509a());
        }

        @JavascriptInterface
        public void onPageInitialized() {
            if (!this.f1955a.m1576b()) {
                this.f1955a.f1958a.m1204a();
                if (this.f1955a.f1959b != null) {
                    this.f1955a.f1959b.m1195a();
                }
            }
        }
    }

    public C0542a(Context context, C0442a c0442a, int i) {
        super(context);
        this.f1958a = c0442a;
        setWebViewClient(new C0539b());
        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(false);
        C0523h.m1548b(this);
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(false);
        setVerticalScrollBarEnabled(false);
        setVerticalScrollbarOverlay(false);
        addJavascriptInterface(new C0540c(this), "AdControl");
        this.f1959b = new C0437e(getContext(), this, i, new C05381(this, c0442a));
    }

    public void m1579a(int i, int i2) {
        this.f1959b.m1196a(i);
        this.f1959b.m1198b(i2);
    }

    public void destroy() {
        if (this.f1959b != null) {
            this.f1959b.m1197b();
            this.f1959b = null;
        }
        C0523h.m1547a((WebView) this);
        super.destroy();
    }

    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (this.f1958a != null) {
            this.f1958a.m1205a(i);
        }
        if (i == 0) {
            if (this.f1959b != null) {
                this.f1959b.m1195a();
            }
        } else if (i == 8 && this.f1959b != null) {
            this.f1959b.m1197b();
        }
    }
}
