package com.google.android.gms.analytics.internal;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.analytics.AnalyticsReceiver;
import com.google.android.gms.analytics.AnalyticsService;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.measurement.zzg;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class zzb extends zzd {
    private final zzl zzQb;

    /* renamed from: com.google.android.gms.analytics.internal.zzb.1 */
    class C06531 implements Runnable {
        final /* synthetic */ int zzQc;
        final /* synthetic */ zzb zzQd;

        C06531(zzb com_google_android_gms_analytics_internal_zzb, int i) {
            this.zzQd = com_google_android_gms_analytics_internal_zzb;
            this.zzQc = i;
        }

        public void run() {
            this.zzQd.zzQb.zzs(((long) this.zzQc) * 1000);
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzb.2 */
    class C06542 implements Runnable {
        final /* synthetic */ zzb zzQd;
        final /* synthetic */ boolean zzQe;

        C06542(zzb com_google_android_gms_analytics_internal_zzb, boolean z) {
            this.zzQd = com_google_android_gms_analytics_internal_zzb;
            this.zzQe = z;
        }

        public void run() {
            this.zzQd.zzQb.zzJ(this.zzQe);
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzb.3 */
    class C06553 implements Runnable {
        final /* synthetic */ zzb zzQd;
        final /* synthetic */ String zzQf;
        final /* synthetic */ Runnable zzQg;

        C06553(zzb com_google_android_gms_analytics_internal_zzb, String str, Runnable runnable) {
            this.zzQd = com_google_android_gms_analytics_internal_zzb;
            this.zzQf = str;
            this.zzQg = runnable;
        }

        public void run() {
            this.zzQd.zzQb.zzbl(this.zzQf);
            if (this.zzQg != null) {
                this.zzQg.run();
            }
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzb.4 */
    class C06564 implements Runnable {
        final /* synthetic */ zzb zzQd;
        final /* synthetic */ zzab zzQh;

        C06564(zzb com_google_android_gms_analytics_internal_zzb, zzab com_google_android_gms_analytics_internal_zzab) {
            this.zzQd = com_google_android_gms_analytics_internal_zzb;
            this.zzQh = com_google_android_gms_analytics_internal_zzab;
        }

        public void run() {
            this.zzQd.zzQb.zza(this.zzQh);
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzb.5 */
    class C06575 implements Runnable {
        final /* synthetic */ zzb zzQd;

        C06575(zzb com_google_android_gms_analytics_internal_zzb) {
            this.zzQd = com_google_android_gms_analytics_internal_zzb;
        }

        public void run() {
            this.zzQd.zzQb.zzjc();
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzb.6 */
    class C06586 implements Runnable {
        final /* synthetic */ zzb zzQd;
        final /* synthetic */ zzw zzQi;

        C06586(zzb com_google_android_gms_analytics_internal_zzb, zzw com_google_android_gms_analytics_internal_zzw) {
            this.zzQd = com_google_android_gms_analytics_internal_zzb;
            this.zzQi = com_google_android_gms_analytics_internal_zzw;
        }

        public void run() {
            this.zzQd.zzQb.zzb(this.zzQi);
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzb.7 */
    class C06597 implements Callable<Void> {
        final /* synthetic */ zzb zzQd;

        C06597(zzb com_google_android_gms_analytics_internal_zzb) {
            this.zzQd = com_google_android_gms_analytics_internal_zzb;
        }

        public /* synthetic */ Object call() throws Exception {
            return zzdt();
        }

        public Void zzdt() throws Exception {
            this.zzQd.zzQb.zzka();
            return null;
        }
    }

    public zzb(zzf com_google_android_gms_analytics_internal_zzf, zzg com_google_android_gms_analytics_internal_zzg) {
        super(com_google_android_gms_analytics_internal_zzf);
        zzx.zzz(com_google_android_gms_analytics_internal_zzg);
        this.zzQb = com_google_android_gms_analytics_internal_zzg.zzj(com_google_android_gms_analytics_internal_zzf);
    }

    void onServiceConnected() {
        zzjk();
        this.zzQb.onServiceConnected();
    }

    public void setLocalDispatchPeriod(int dispatchPeriodInSeconds) {
        zzjv();
        zzb("setLocalDispatchPeriod (sec)", Integer.valueOf(dispatchPeriodInSeconds));
        zzjo().zzf(new C06531(this, dispatchPeriodInSeconds));
    }

    public void start() {
        this.zzQb.start();
    }

    public void zzJ(boolean z) {
        zza("Network connectivity status changed", Boolean.valueOf(z));
        zzjo().zzf(new C06542(this, z));
    }

    public long zza(zzh com_google_android_gms_analytics_internal_zzh) {
        zzjv();
        zzx.zzz(com_google_android_gms_analytics_internal_zzh);
        zzjk();
        long zza = this.zzQb.zza(com_google_android_gms_analytics_internal_zzh, true);
        if (zza == 0) {
            this.zzQb.zzc(com_google_android_gms_analytics_internal_zzh);
        }
        return zza;
    }

    public void zza(zzab com_google_android_gms_analytics_internal_zzab) {
        zzx.zzz(com_google_android_gms_analytics_internal_zzab);
        zzjv();
        zzb("Hit delivery requested", com_google_android_gms_analytics_internal_zzab);
        zzjo().zzf(new C06564(this, com_google_android_gms_analytics_internal_zzab));
    }

    public void zza(zzw com_google_android_gms_analytics_internal_zzw) {
        zzjv();
        zzjo().zzf(new C06586(this, com_google_android_gms_analytics_internal_zzw));
    }

    public void zza(String str, Runnable runnable) {
        zzx.zzh(str, "campaign param can't be empty");
        zzjo().zzf(new C06553(this, str, runnable));
    }

    protected void zziJ() {
        this.zzQb.zza();
    }

    public void zzjc() {
        zzjv();
        zzjj();
        zzjo().zzf(new C06575(this));
    }

    public void zzjd() {
        zzjv();
        Context context = getContext();
        if (AnalyticsReceiver.zzY(context) && AnalyticsService.zzZ(context)) {
            Intent intent = new Intent(context, AnalyticsService.class);
            intent.setAction("com.google.android.gms.analytics.ANALYTICS_DISPATCH");
            context.startService(intent);
            return;
        }
        zza(null);
    }

    public boolean zzje() {
        zzjv();
        try {
            zzjo().zzc(new C06597(this)).get(4, TimeUnit.SECONDS);
            return true;
        } catch (InterruptedException e) {
            zzd("syncDispatchLocalHits interrupted", e);
            return false;
        } catch (ExecutionException e2) {
            zze("syncDispatchLocalHits failed", e2);
            return false;
        } catch (TimeoutException e3) {
            zzd("syncDispatchLocalHits timed out", e3);
            return false;
        }
    }

    public void zzjf() {
        zzjv();
        zzg.zzjk();
        this.zzQb.zzjf();
    }

    public void zzjg() {
        zzbd("Radio powered up");
        zzjd();
    }

    void zzjh() {
        zzjk();
        this.zzQb.zzjh();
    }
}
