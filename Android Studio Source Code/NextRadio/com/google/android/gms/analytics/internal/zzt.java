package com.google.android.gms.analytics.internal;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.common.internal.zzx;

abstract class zzt {
    private static volatile Handler zzRC;
    private final zzf zzQj;
    private volatile long zzRD;
    private boolean zzRE;
    private final Runnable zzx;

    /* renamed from: com.google.android.gms.analytics.internal.zzt.1 */
    class C06711 implements Runnable {
        final /* synthetic */ zzt zzRF;

        C06711(zzt com_google_android_gms_analytics_internal_zzt) {
            this.zzRF = com_google_android_gms_analytics_internal_zzt;
        }

        public void run() {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                this.zzRF.zzQj.zzjo().zzf(this);
                return;
            }
            boolean zzbw = this.zzRF.zzbw();
            this.zzRF.zzRD = 0;
            if (zzbw && !this.zzRF.zzRE) {
                this.zzRF.run();
            }
        }
    }

    zzt(zzf com_google_android_gms_analytics_internal_zzf) {
        zzx.zzz(com_google_android_gms_analytics_internal_zzf);
        this.zzQj = com_google_android_gms_analytics_internal_zzf;
        this.zzx = new C06711(this);
    }

    private Handler getHandler() {
        if (zzRC != null) {
            return zzRC;
        }
        Handler handler;
        synchronized (zzt.class) {
            if (zzRC == null) {
                zzRC = new Handler(this.zzQj.getContext().getMainLooper());
            }
            handler = zzRC;
        }
        return handler;
    }

    public void cancel() {
        this.zzRD = 0;
        getHandler().removeCallbacks(this.zzx);
    }

    public abstract void run();

    public boolean zzbw() {
        return this.zzRD != 0;
    }

    public long zzkY() {
        return this.zzRD == 0 ? 0 : Math.abs(this.zzQj.zzjl().currentTimeMillis() - this.zzRD);
    }

    public void zzt(long j) {
        cancel();
        if (j >= 0) {
            this.zzRD = this.zzQj.zzjl().currentTimeMillis();
            if (!getHandler().postDelayed(this.zzx, j)) {
                this.zzQj.zzjm().zze("Failed to schedule delayed post. time", Long.valueOf(j));
            }
        }
    }

    public void zzu(long j) {
        long j2 = 0;
        if (!zzbw()) {
            return;
        }
        if (j < 0) {
            cancel();
            return;
        }
        long abs = j - Math.abs(this.zzQj.zzjl().currentTimeMillis() - this.zzRD);
        if (abs >= 0) {
            j2 = abs;
        }
        getHandler().removeCallbacks(this.zzx);
        if (!getHandler().postDelayed(this.zzx, j2)) {
            this.zzQj.zzjm().zze("Failed to adjust delayed post. time", Long.valueOf(j2));
        }
    }
}
