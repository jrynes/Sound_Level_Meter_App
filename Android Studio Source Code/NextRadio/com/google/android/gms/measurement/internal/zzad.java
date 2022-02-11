package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import com.google.android.gms.internal.zzmq;
import org.apache.activemq.transport.stomp.StompSubscription;

public class zzad extends zzz {
    private Handler mHandler;
    private long zzaZa;
    private final Runnable zzaZb;
    private final zzf zzaZc;
    private final zzf zzaZd;

    /* renamed from: com.google.android.gms.measurement.internal.zzad.1 */
    class C10001 implements Runnable {
        final /* synthetic */ zzad zzaZe;

        /* renamed from: com.google.android.gms.measurement.internal.zzad.1.1 */
        class C09991 implements Runnable {
            final /* synthetic */ C10001 zzaZf;

            C09991(C10001 c10001) {
                this.zzaZf = c10001;
            }

            public void run() {
                this.zzaZf.zzaZe.zzDx();
            }
        }

        C10001(zzad com_google_android_gms_measurement_internal_zzad) {
            this.zzaZe = com_google_android_gms_measurement_internal_zzad;
        }

        @MainThread
        public void run() {
            this.zzaZe.zzCn().zzg(new C09991(this));
        }
    }

    /* renamed from: com.google.android.gms.measurement.internal.zzad.2 */
    class C10012 extends zzf {
        final /* synthetic */ zzad zzaZe;

        C10012(zzad com_google_android_gms_measurement_internal_zzad, zzw com_google_android_gms_measurement_internal_zzw) {
            this.zzaZe = com_google_android_gms_measurement_internal_zzad;
            super(com_google_android_gms_measurement_internal_zzw);
        }

        @WorkerThread
        public void run() {
            this.zzaZe.zzDy();
        }
    }

    /* renamed from: com.google.android.gms.measurement.internal.zzad.3 */
    class C10023 extends zzf {
        final /* synthetic */ zzad zzaZe;

        C10023(zzad com_google_android_gms_measurement_internal_zzad, zzw com_google_android_gms_measurement_internal_zzw) {
            this.zzaZe = com_google_android_gms_measurement_internal_zzad;
            super(com_google_android_gms_measurement_internal_zzw);
        }

        @WorkerThread
        public void run() {
            this.zzaZe.zzDz();
        }
    }

    /* renamed from: com.google.android.gms.measurement.internal.zzad.4 */
    class C10034 implements Runnable {
        final /* synthetic */ zzad zzaZe;
        final /* synthetic */ long zzaZg;

        C10034(zzad com_google_android_gms_measurement_internal_zzad, long j) {
            this.zzaZe = com_google_android_gms_measurement_internal_zzad;
            this.zzaZg = j;
        }

        public void run() {
            this.zzaZe.zzae(this.zzaZg);
        }
    }

    /* renamed from: com.google.android.gms.measurement.internal.zzad.5 */
    class C10045 implements Runnable {
        final /* synthetic */ zzad zzaZe;
        final /* synthetic */ long zzaZg;

        C10045(zzad com_google_android_gms_measurement_internal_zzad, long j) {
            this.zzaZe = com_google_android_gms_measurement_internal_zzad;
            this.zzaZg = j;
        }

        public void run() {
            this.zzaZe.zzaf(this.zzaZg);
        }
    }

    zzad(zzw com_google_android_gms_measurement_internal_zzw) {
        super(com_google_android_gms_measurement_internal_zzw);
        this.zzaZb = new C10001(this);
        this.zzaZc = new C10012(this, this.zzaTV);
        this.zzaZd = new C10023(this, this.zzaTV);
    }

    private void zzDv() {
        synchronized (this) {
            if (this.mHandler == null) {
                this.mHandler = new Handler(Looper.getMainLooper());
            }
        }
    }

    @WorkerThread
    private void zzDy() {
        zzjk();
        zzAo().zzCK().zzj("Session started, time", Long.valueOf(zzjl().elapsedRealtime()));
        zzCo().zzaXu.set(false);
        zzCf().zze(StompSubscription.AUTO_ACK, "_s", new Bundle());
    }

    @WorkerThread
    private void zzDz() {
        zzjk();
        long elapsedRealtime = zzjl().elapsedRealtime();
        if (this.zzaZa == 0) {
            this.zzaZa = elapsedRealtime - 3600000;
        }
        long j = zzCo().zzaXw.get() + (elapsedRealtime - this.zzaZa);
        zzCo().zzaXw.set(j);
        zzAo().zzCK().zzj("Recording user engagement, ms", Long.valueOf(j));
        Bundle bundle = new Bundle();
        bundle.putLong("_et", j);
        zzCf().zze(StompSubscription.AUTO_ACK, "_e", bundle);
        zzCo().zzaXw.set(0);
        this.zzaZa = elapsedRealtime;
        this.zzaZd.zzt(Math.max(0, 3600000 - zzCo().zzaXw.get()));
    }

    @WorkerThread
    private void zzae(long j) {
        zzjk();
        zzDv();
        this.zzaZc.cancel();
        this.zzaZd.cancel();
        zzAo().zzCK().zzj("Activity resumed, time", Long.valueOf(j));
        this.zzaZa = j;
        if (zzjl().currentTimeMillis() - zzCo().zzaXt.get() > zzCo().zzaXv.get()) {
            zzCo().zzaXu.set(true);
            zzCo().zzaXw.set(0);
        }
        if (zzCo().zzaXu.get()) {
            this.zzaZc.zzt(Math.max(0, zzCo().zzaXs.get() - zzCo().zzaXw.get()));
        } else {
            this.zzaZd.zzt(Math.max(0, 3600000 - zzCo().zzaXw.get()));
        }
    }

    @WorkerThread
    private void zzaf(long j) {
        zzjk();
        zzDv();
        this.zzaZc.cancel();
        this.zzaZd.cancel();
        zzAo().zzCK().zzj("Activity paused, time", Long.valueOf(j));
        if (this.zzaZa != 0) {
            zzCo().zzaXw.set(zzCo().zzaXw.get() + (j - this.zzaZa));
        }
        zzCo().zzaXv.set(zzjl().currentTimeMillis());
        synchronized (this) {
            if (!zzCo().zzaXu.get()) {
                this.mHandler.postDelayed(this.zzaZb, 1000);
            }
        }
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public /* bridge */ /* synthetic */ zzp zzAo() {
        return super.zzAo();
    }

    public /* bridge */ /* synthetic */ void zzCd() {
        super.zzCd();
    }

    public /* bridge */ /* synthetic */ zzc zzCe() {
        return super.zzCe();
    }

    public /* bridge */ /* synthetic */ zzab zzCf() {
        return super.zzCf();
    }

    public /* bridge */ /* synthetic */ zzn zzCg() {
        return super.zzCg();
    }

    public /* bridge */ /* synthetic */ zzg zzCh() {
        return super.zzCh();
    }

    public /* bridge */ /* synthetic */ zzac zzCi() {
        return super.zzCi();
    }

    public /* bridge */ /* synthetic */ zze zzCj() {
        return super.zzCj();
    }

    public /* bridge */ /* synthetic */ zzaj zzCk() {
        return super.zzCk();
    }

    public /* bridge */ /* synthetic */ zzu zzCl() {
        return super.zzCl();
    }

    public /* bridge */ /* synthetic */ zzad zzCm() {
        return super.zzCm();
    }

    public /* bridge */ /* synthetic */ zzv zzCn() {
        return super.zzCn();
    }

    public /* bridge */ /* synthetic */ zzt zzCo() {
        return super.zzCo();
    }

    public /* bridge */ /* synthetic */ zzd zzCp() {
        return super.zzCp();
    }

    @MainThread
    protected void zzDu() {
        synchronized (this) {
            zzDv();
            this.mHandler.removeCallbacks(this.zzaZb);
        }
        zzCn().zzg(new C10034(this, zzjl().elapsedRealtime()));
    }

    @MainThread
    protected void zzDw() {
        zzCn().zzg(new C10045(this, zzjl().elapsedRealtime()));
    }

    @WorkerThread
    public void zzDx() {
        zzjk();
        zzAo().zzCJ().zzfg("Application backgrounded. Logging engagement");
        long j = zzCo().zzaXw.get();
        if (j > 0) {
            Bundle bundle = new Bundle();
            bundle.putLong("_et", j);
            zzCf().zze(StompSubscription.AUTO_ACK, "_e", bundle);
            zzCo().zzaXw.set(0);
            return;
        }
        zzAo().zzCF().zzj("Not logging non-positive engagement time", Long.valueOf(j));
    }

    protected void zziJ() {
    }

    public /* bridge */ /* synthetic */ void zzjj() {
        super.zzjj();
    }

    public /* bridge */ /* synthetic */ void zzjk() {
        super.zzjk();
    }

    public /* bridge */ /* synthetic */ zzmq zzjl() {
        return super.zzjl();
    }
}
