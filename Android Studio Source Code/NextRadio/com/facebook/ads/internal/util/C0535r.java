package com.facebook.ads.internal.util;

import android.graphics.Bitmap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* renamed from: com.facebook.ads.internal.util.r */
public class C0535r {
    static final int f1946a;
    static final ExecutorService f1947b;
    private static volatile boolean f1948c;
    private final Bitmap f1949d;
    private Bitmap f1950e;
    private final C0525j f1951f;

    static {
        f1946a = Runtime.getRuntime().availableProcessors();
        f1947b = Executors.newFixedThreadPool(f1946a);
        f1948c = true;
    }

    public C0535r(Bitmap bitmap) {
        this.f1949d = bitmap;
        this.f1951f = new C0530n();
    }

    public Bitmap m1570a() {
        return this.f1950e;
    }

    public Bitmap m1571a(int i) {
        this.f1950e = this.f1951f.m1553a(this.f1949d, (float) i);
        return this.f1950e;
    }
}
