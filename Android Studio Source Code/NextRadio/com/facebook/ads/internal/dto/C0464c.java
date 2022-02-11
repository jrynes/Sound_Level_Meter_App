package com.facebook.ads.internal.dto;

import java.util.ArrayList;
import java.util.List;

/* renamed from: com.facebook.ads.internal.dto.c */
public class C0464c {
    private List<C0461a> f1713a;
    private int f1714b;
    private C0465d f1715c;

    public C0464c(C0465d c0465d) {
        this.f1714b = 0;
        this.f1713a = new ArrayList();
        this.f1715c = c0465d;
    }

    public C0465d m1361a() {
        return this.f1715c;
    }

    public void m1362a(C0461a c0461a) {
        this.f1713a.add(c0461a);
    }

    public int m1363b() {
        return this.f1713a.size();
    }

    public C0461a m1364c() {
        if (this.f1714b >= this.f1713a.size()) {
            return null;
        }
        this.f1714b++;
        return (C0461a) this.f1713a.get(this.f1714b - 1);
    }
}
