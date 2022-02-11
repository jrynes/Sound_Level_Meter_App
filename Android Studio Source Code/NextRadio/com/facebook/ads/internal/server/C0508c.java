package com.facebook.ads.internal.server;

import com.facebook.ads.internal.dto.C0464c;

/* renamed from: com.facebook.ads.internal.server.c */
public class C0508c {
    private C0464c f1877a;
    private C0507a f1878b;

    /* renamed from: com.facebook.ads.internal.server.c.a */
    public enum C0507a {
        UNKNOWN,
        ERROR,
        ADS
    }

    public C0508c(C0507a c0507a, C0464c c0464c) {
        this.f1878b = c0507a;
        this.f1877a = c0464c;
    }

    public C0507a m1506a() {
        return this.f1878b;
    }

    public C0464c m1507b() {
        return this.f1877a;
    }
}
