package com.facebook.ads.internal.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout.LayoutParams;
import com.facebook.ads.InterstitialAdActivity;
import com.facebook.ads.internal.action.C0428a;
import com.facebook.ads.internal.action.C0429b;
import com.facebook.ads.internal.adapters.C0417c;
import com.facebook.ads.internal.adapters.C0450l;
import com.facebook.ads.internal.adapters.C0452m;
import com.facebook.ads.internal.util.C0514b;
import com.facebook.ads.internal.util.C0514b.C0513a;
import com.facebook.ads.internal.util.C0515c;
import com.facebook.ads.internal.util.C0523h;
import com.facebook.ads.internal.view.C0542a.C0442a;
import com.facebook.ads.internal.view.C0557h.C0406a;

/* renamed from: com.facebook.ads.internal.view.f */
public class C0558f implements C0557h {
    private static final String f1994a;
    private C0406a f1995b;
    private C0542a f1996c;
    private C0450l f1997d;
    private C0452m f1998e;
    private long f1999f;
    private long f2000g;
    private C0513a f2001h;

    /* renamed from: com.facebook.ads.internal.view.f.1 */
    class C05551 implements C0442a {
        final /* synthetic */ InterstitialAdActivity f1991a;
        final /* synthetic */ C0558f f1992b;

        C05551(C0558f c0558f, InterstitialAdActivity interstitialAdActivity) {
            this.f1992b = c0558f;
            this.f1991a = interstitialAdActivity;
        }

        public void m1601a() {
            this.f1992b.f1998e.m1300c();
        }

        public void m1602a(int i) {
        }

        public void m1603a(String str) {
            Uri parse = Uri.parse(str);
            if ("fbad".equals(parse.getScheme()) && "close".equals(parse.getAuthority())) {
                this.f1991a.finish();
                return;
            }
            this.f1992b.f1995b.m1081a("com.facebook.ads.interstitial.clicked");
            C0428a a = C0429b.m1166a(this.f1991a, parse);
            if (a != null) {
                try {
                    this.f1992b.f2001h = a.m1163a();
                    this.f1992b.f2000g = System.currentTimeMillis();
                    a.m1165b();
                } catch (Throwable e) {
                    Log.e(C0558f.f1994a, "Error executing action", e);
                }
            }
        }

        public void m1604b() {
            this.f1992b.f1998e.m1180a();
        }
    }

    /* renamed from: com.facebook.ads.internal.view.f.2 */
    class C05562 extends C0417c {
        final /* synthetic */ C0558f f1993a;

        C05562(C0558f c0558f) {
            this.f1993a = c0558f;
        }

        public void m1605d() {
            this.f1993a.f1995b.m1081a("com.facebook.ads.interstitial.impression.logged");
        }
    }

    static {
        f1994a = C0558f.class.getSimpleName();
    }

    public C0558f(InterstitialAdActivity interstitialAdActivity, C0406a c0406a) {
        this.f1995b = c0406a;
        this.f1999f = System.currentTimeMillis();
        this.f1996c = new C0542a(interstitialAdActivity, new C05551(this, interstitialAdActivity), 1);
        this.f1996c.setId(100001);
        this.f1996c.setLayoutParams(new LayoutParams(-1, -1));
        this.f1998e = new C0452m(interstitialAdActivity, this.f1996c, new C05562(this));
        this.f1998e.m1301d();
        c0406a.m1080a(this.f1996c);
    }

    public void m1616a() {
        if (this.f1996c != null) {
            this.f1996c.onPause();
        }
    }

    public void m1617a(Intent intent, Bundle bundle) {
        if (bundle == null || !bundle.containsKey("dataModel")) {
            this.f1997d = C0450l.m1280b(intent);
            if (this.f1997d != null) {
                this.f1998e.m1298a(this.f1997d);
                this.f1996c.loadDataWithBaseURL(C0523h.m1543a(), this.f1997d.m1285d(), WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
                this.f1996c.m1579a(this.f1997d.m1291j(), this.f1997d.m1292k());
                return;
            }
            return;
        }
        this.f1997d = C0450l.m1278a(bundle.getBundle("dataModel"));
        if (this.f1997d != null) {
            this.f1996c.loadDataWithBaseURL(C0523h.m1543a(), this.f1997d.m1285d(), WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
            this.f1996c.m1579a(this.f1997d.m1291j(), this.f1997d.m1292k());
        }
    }

    public void m1618a(Bundle bundle) {
        if (this.f1997d != null) {
            bundle.putBundle("dataModel", this.f1997d.m1293l());
        }
    }

    public void m1619b() {
        if (!(this.f2000g <= 0 || this.f2001h == null || this.f1997d == null)) {
            C0515c.m1515a(C0514b.m1511a(this.f2000g, this.f2001h, this.f1997d.m1290i()));
        }
        if (this.f1996c != null) {
            this.f1996c.onResume();
        }
    }

    public void m1620c() {
        if (this.f1997d != null) {
            C0515c.m1515a(C0514b.m1511a(this.f1999f, C0513a.XOUT, this.f1997d.m1290i()));
        }
        if (this.f1996c != null) {
            C0523h.m1547a(this.f1996c);
            this.f1996c.destroy();
            this.f1996c = null;
        }
    }
}
