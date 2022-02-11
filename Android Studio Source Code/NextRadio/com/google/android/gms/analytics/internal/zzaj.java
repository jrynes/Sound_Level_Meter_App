package com.google.android.gms.analytics.internal;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;

class zzaj {
    private long zzCv;
    private final zzmq zzqW;

    public zzaj(zzmq com_google_android_gms_internal_zzmq) {
        zzx.zzz(com_google_android_gms_internal_zzmq);
        this.zzqW = com_google_android_gms_internal_zzmq;
    }

    public zzaj(zzmq com_google_android_gms_internal_zzmq, long j) {
        zzx.zzz(com_google_android_gms_internal_zzmq);
        this.zzqW = com_google_android_gms_internal_zzmq;
        this.zzCv = j;
    }

    public void clear() {
        this.zzCv = 0;
    }

    public void start() {
        this.zzCv = this.zzqW.elapsedRealtime();
    }

    public boolean zzv(long j) {
        return this.zzCv == 0 || this.zzqW.elapsedRealtime() - this.zzCv > j;
    }
}
