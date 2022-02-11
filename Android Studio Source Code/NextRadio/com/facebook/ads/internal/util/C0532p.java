package com.facebook.ads.internal.util;

import android.support.annotation.NonNull;
import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/* renamed from: com.facebook.ads.internal.util.p */
public class C0532p implements ThreadFactory {
    protected final AtomicLong f1942a;
    private int f1943b;

    public C0532p() {
        this.f1942a = new AtomicLong();
        this.f1943b = Thread.currentThread().getPriority();
    }

    protected String m1569a() {
        return String.format(Locale.ENGLISH, "com.facebook.ads thread-%d %tF %<tT", new Object[]{Long.valueOf(this.f1942a.incrementAndGet()), Long.valueOf(System.currentTimeMillis())});
    }

    public Thread newThread(@NonNull Runnable runnable) {
        Thread thread = new Thread(null, runnable, m1569a(), 0);
        thread.setPriority(this.f1943b);
        return thread;
    }
}
