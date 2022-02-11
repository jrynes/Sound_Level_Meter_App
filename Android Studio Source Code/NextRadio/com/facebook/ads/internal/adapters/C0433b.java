package com.facebook.ads.internal.adapters;

import android.content.Context;
import com.facebook.ads.internal.util.C0522g;

/* renamed from: com.facebook.ads.internal.adapters.b */
public abstract class C0433b {
    protected final C0417c f1559a;
    private final Context f1560b;
    private boolean f1561c;

    public C0433b(Context context, C0417c c0417c) {
        this.f1560b = context;
        this.f1559a = c0417c;
    }

    public final void m1180a() {
        if (!this.f1561c) {
            if (this.f1559a != null) {
                this.f1559a.m1108d();
            }
            m1181b();
            this.f1561c = true;
            C0522g.m1535a(this.f1560b, "Impression logged");
            if (this.f1559a != null) {
                this.f1559a.m1109e();
            }
        }
    }

    protected abstract void m1181b();
}
