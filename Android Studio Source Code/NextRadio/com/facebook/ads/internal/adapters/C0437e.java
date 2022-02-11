package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.facebook.ads.internal.util.C0435u;
import com.facebook.ads.internal.util.C0522g;
import org.apache.activemq.ActiveMQPrefetchPolicy;

/* renamed from: com.facebook.ads.internal.adapters.e */
public class C0437e {
    private int f1567a;
    private int f1568b;
    private final Context f1569c;
    private final View f1570d;
    private final int f1571e;
    private final C0415a f1572f;
    private final Handler f1573g;
    private final Runnable f1574h;
    private final boolean f1575i;
    private volatile boolean f1576j;

    /* renamed from: com.facebook.ads.internal.adapters.e.a */
    public static abstract class C0415a {
        public abstract void m1102a();

        public void m1103b() {
        }
    }

    /* renamed from: com.facebook.ads.internal.adapters.e.b */
    private static final class C0436b extends C0435u<C0437e> {
        public C0436b(C0437e c0437e) {
            super(c0437e);
        }

        public void run() {
            C0437e c0437e = (C0437e) m1184a();
            if (c0437e != null) {
                if (c0437e.f1575i || !c0437e.f1576j) {
                    View c = c0437e.f1570d;
                    C0415a d = c0437e.f1572f;
                    if (c != null && d != null) {
                        if (C0522g.m1540a(c0437e.f1569c, c, c0437e.f1571e)) {
                            d.m1102a();
                            c0437e.f1576j = true;
                            return;
                        }
                        d.m1103b();
                        c0437e.f1573g.postDelayed(c0437e.f1574h, (long) c0437e.f1568b);
                    }
                }
            }
        }
    }

    public C0437e(Context context, View view, int i, C0415a c0415a) {
        this(context, view, i, false, c0415a);
    }

    public C0437e(Context context, View view, int i, boolean z, C0415a c0415a) {
        this.f1567a = 0;
        this.f1568b = ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
        this.f1573g = new Handler();
        this.f1574h = new C0436b(this);
        this.f1569c = context;
        this.f1570d = view;
        this.f1571e = i;
        this.f1572f = c0415a;
        this.f1575i = z;
    }

    public void m1195a() {
        if (!this.f1575i && !this.f1576j) {
            this.f1573g.postDelayed(this.f1574h, (long) this.f1567a);
        }
    }

    public void m1196a(int i) {
        this.f1567a = i;
    }

    public void m1197b() {
        this.f1573g.removeCallbacks(this.f1574h);
    }

    public void m1198b(int i) {
        this.f1568b = i;
    }
}
