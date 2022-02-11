package com.google.android.gms.internal;

import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class zzrt {
    public static final Integer zzbmw;
    public static final Integer zzbmx;
    private final Context mContext;
    private final ExecutorService zzbkn;

    static {
        zzbmw = Integer.valueOf(0);
        zzbmx = Integer.valueOf(1);
    }

    public zzrt(Context context) {
        this(context, Executors.newSingleThreadExecutor());
    }

    zzrt(Context context, ExecutorService executorService) {
        this.mContext = context;
        this.zzbkn = executorService;
    }
}
