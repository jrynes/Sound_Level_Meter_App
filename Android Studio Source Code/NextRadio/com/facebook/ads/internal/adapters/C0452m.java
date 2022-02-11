package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.util.Log;
import com.facebook.ads.internal.util.C0531o;
import com.facebook.ads.internal.util.C0536s;
import com.facebook.ads.internal.view.C0541d;
import java.util.Collections;
import java.util.Map;

/* renamed from: com.facebook.ads.internal.adapters.m */
public class C0452m extends C0433b {
    private static final String f1628b;
    private final C0541d f1629c;
    private C0450l f1630d;
    private boolean f1631e;

    /* renamed from: com.facebook.ads.internal.adapters.m.1 */
    class C04511 implements Runnable {
        final /* synthetic */ C0452m f1627a;

        C04511(C0452m c0452m) {
            this.f1627a = c0452m;
        }

        public void run() {
            if (this.f1627a.f1629c.m1576b()) {
                Log.w(C0452m.f1628b, "Webview already destroyed, cannot activate");
            } else {
                this.f1627a.f1629c.loadUrl("javascript:" + this.f1627a.f1630d.m1286e());
            }
        }
    }

    static {
        f1628b = C0452m.class.getSimpleName();
    }

    public C0452m(Context context, C0541d c0541d, C0417c c0417c) {
        super(context, c0417c);
        this.f1629c = c0541d;
    }

    private void m1295a(Map<String, String> map) {
        if (this.f1630d != null) {
            if (!C0536s.m1572a(this.f1630d.m1287f())) {
                new C0531o(map).execute(new String[]{r0});
            }
        }
    }

    public void m1298a(C0450l c0450l) {
        this.f1630d = c0450l;
    }

    protected void m1299b() {
        if (this.f1630d != null) {
            if (!(this.f1629c == null || C0536s.m1572a(this.f1630d.m1288g()))) {
                if (this.f1629c.m1576b()) {
                    Log.w(f1628b, "Webview already destroyed, cannot send impression");
                } else {
                    this.f1629c.loadUrl("javascript:" + this.f1630d.m1288g());
                }
            }
            m1295a(Collections.singletonMap("evt", "native_imp"));
        }
    }

    public synchronized void m1300c() {
        if (!(this.f1631e || this.f1630d == null)) {
            this.f1631e = true;
            if (!(this.f1629c == null || C0536s.m1572a(this.f1630d.m1286e()))) {
                this.f1629c.post(new C04511(this));
            }
        }
    }

    public void m1301d() {
        m1295a(Collections.singletonMap("evt", "interstitial_displayed"));
    }
}
