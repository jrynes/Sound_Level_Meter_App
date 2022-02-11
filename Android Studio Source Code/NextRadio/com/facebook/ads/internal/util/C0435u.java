package com.facebook.ads.internal.util;

import java.lang.ref.WeakReference;

/* renamed from: com.facebook.ads.internal.util.u */
public abstract class C0435u<T> implements Runnable {
    private final WeakReference<T> f1566a;

    public C0435u(T t) {
        this.f1566a = new WeakReference(t);
    }

    public T m1184a() {
        return this.f1566a.get();
    }
}
