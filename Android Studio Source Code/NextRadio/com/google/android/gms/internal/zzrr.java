package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.internal.zzrs.zzc;
import java.util.HashMap;
import java.util.Map;

public class zzrr {
    private final Context mContext;
    private String zzbio;
    private final zzrt zzbmf;
    Map<String, Object<zzc>> zzbmg;
    private final Map<String, Object> zzbmh;
    private final zzmq zzqW;

    public zzrr(Context context) {
        this(context, new HashMap(), new zzrt(context), zzmt.zzsc());
    }

    zzrr(Context context, Map<String, Object> map, zzrt com_google_android_gms_internal_zzrt, zzmq com_google_android_gms_internal_zzmq) {
        this.zzbio = null;
        this.zzbmg = new HashMap();
        this.mContext = context;
        this.zzqW = com_google_android_gms_internal_zzmq;
        this.zzbmf = com_google_android_gms_internal_zzrt;
        this.zzbmh = map;
    }

    public void zzgB(String str) {
        this.zzbio = str;
    }
}
