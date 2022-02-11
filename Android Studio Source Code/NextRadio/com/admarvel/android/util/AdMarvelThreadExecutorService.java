package com.admarvel.android.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/* renamed from: com.admarvel.android.util.g */
public class AdMarvelThreadExecutorService {
    private static AdMarvelThreadExecutorService f1020c;
    private ExecutorService f1021a;
    private ScheduledExecutorService f1022b;

    static {
        f1020c = null;
    }

    private AdMarvelThreadExecutorService() {
        this.f1021a = null;
        this.f1022b = null;
    }

    public static AdMarvelThreadExecutorService m597a() {
        if (f1020c == null) {
            f1020c = new AdMarvelThreadExecutorService();
        }
        return f1020c;
    }

    public ExecutorService m598b() {
        if (this.f1021a == null) {
            this.f1021a = Executors.newCachedThreadPool();
        }
        return this.f1021a;
    }
}
