package com.facebook.ads.internal.view;

import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.facebook.ads.internal.action.C0428a;
import com.facebook.ads.internal.action.C0429b;
import com.facebook.ads.internal.adapters.C0417c;
import com.facebook.ads.internal.adapters.C0437e;
import com.facebook.ads.internal.adapters.C0437e.C0415a;
import com.facebook.ads.internal.adapters.C0450l;
import com.facebook.ads.internal.adapters.C0452m;
import com.facebook.ads.internal.ssp.ANAdRenderer.Listener;
import com.facebook.ads.internal.util.C0512a;
import com.facebook.ads.internal.util.C0514b;
import com.facebook.ads.internal.util.C0514b.C0513a;
import com.facebook.ads.internal.util.C0515c;
import com.facebook.ads.internal.util.C0522g;
import com.facebook.ads.internal.util.C0523h;

/* renamed from: com.facebook.ads.internal.view.c */
public class C0549c extends C0541d {
    private static final String f1969a;
    private final C0450l f1970b;
    private final int f1971c;
    private final Listener f1972d;
    private C0452m f1973e;
    private C0437e f1974f;
    private long f1975g;
    private C0513a f1976h;

    /* renamed from: com.facebook.ads.internal.view.c.1 */
    class C05451 extends C0417c {
        final /* synthetic */ C0549c f1964a;

        C05451(C0549c c0549c) {
            this.f1964a = c0549c;
        }

        public void m1585d() {
            this.f1964a.f1972d.onAdImpression();
        }
    }

    /* renamed from: com.facebook.ads.internal.view.c.2 */
    class C05462 extends C0415a {
        final /* synthetic */ C0549c f1965a;

        C05462(C0549c c0549c) {
            this.f1965a = c0549c;
        }

        public void m1586a() {
            this.f1965a.f1973e.m1180a();
        }
    }

    /* renamed from: com.facebook.ads.internal.view.c.a */
    private class C0547a extends WebViewClient {
        final /* synthetic */ C0549c f1966a;

        private C0547a(C0549c c0549c) {
            this.f1966a = c0549c;
        }

        public void onReceivedSslError(WebView webView, @NonNull SslErrorHandler sslErrorHandler, SslError sslError) {
            if (C0522g.m1538a()) {
                sslErrorHandler.proceed();
            } else {
                sslErrorHandler.cancel();
            }
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            Uri parse = Uri.parse(str);
            if ("fbad".equals(parse.getScheme()) && "close".equals(parse.getAuthority())) {
                this.f1966a.f1972d.onAdClose();
            } else {
                this.f1966a.f1972d.onAdClick();
                C0428a a = C0429b.m1166a(this.f1966a.getContext(), parse);
                if (a != null) {
                    try {
                        this.f1966a.f1976h = a.m1163a();
                        this.f1966a.f1975g = System.currentTimeMillis();
                        a.m1165b();
                    } catch (Throwable e) {
                        Log.e(C0549c.f1969a, "Error executing action", e);
                    }
                }
            }
            return true;
        }
    }

    /* renamed from: com.facebook.ads.internal.view.c.b */
    private class C0548b {
        final /* synthetic */ C0549c f1967a;
        private final String f1968b;

        private C0548b(C0549c c0549c) {
            this.f1967a = c0549c;
            this.f1968b = C0548b.class.getSimpleName();
        }

        @JavascriptInterface
        public void alert(String str) {
            Log.e(this.f1968b, str);
        }

        @JavascriptInterface
        public String getAnalogInfo() {
            return C0522g.m1531a(C0512a.m1509a());
        }

        @JavascriptInterface
        public void onPageInitialized() {
            if (!this.f1967a.m1576b()) {
                this.f1967a.m1595d();
                if (this.f1967a.f1974f != null) {
                    this.f1967a.f1974f.m1195a();
                }
            }
        }
    }

    static {
        f1969a = C0549c.class.getSimpleName();
    }

    public C0549c(Context context, C0450l c0450l, int i, Listener listener) {
        super(context);
        if (c0450l == null || listener == null) {
            throw new IllegalArgumentException();
        }
        this.f1970b = c0450l;
        this.f1971c = i;
        this.f1972d = listener;
        m1592c();
    }

    private void m1592c() {
        setWebViewClient(new C0547a());
        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(false);
        C0523h.m1548b(this);
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(false);
        setVerticalScrollBarEnabled(false);
        setVerticalScrollbarOverlay(false);
        addJavascriptInterface(new C0548b(), "AdControl");
        this.f1973e = new C0452m(getContext(), this, new C05451(this));
        this.f1973e.m1298a(this.f1970b);
        this.f1974f = new C0437e(getContext(), this, this.f1971c, new C05462(this));
        this.f1974f.m1196a(this.f1970b.m1291j());
        this.f1974f.m1198b(this.f1970b.m1292k());
        this.f1974f.m1195a();
        loadDataWithBaseURL(C0523h.m1543a(), this.f1970b.m1285d(), WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
    }

    private void m1595d() {
        this.f1973e.m1300c();
    }

    public void destroy() {
        if (this.f1974f != null) {
            this.f1974f.m1197b();
            this.f1974f = null;
        }
        C0523h.m1547a((WebView) this);
        super.destroy();
    }

    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i == 0) {
            if (this.f1975g > 0 && this.f1976h != null) {
                C0515c.m1515a(C0514b.m1511a(this.f1975g, this.f1976h, this.f1970b.m1290i()));
                this.f1975g = 0;
                this.f1976h = null;
            }
            if (this.f1974f != null) {
                this.f1974f.m1195a();
            }
        } else if (i == 8 && this.f1974f != null) {
            this.f1974f.m1197b();
        }
    }
}
