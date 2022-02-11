package com.google.android.gms.analytics.internal;

import android.app.Application;
import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzmt;
import com.google.android.gms.measurement.zzg;
import java.lang.Thread.UncaughtExceptionHandler;

public class zzf {
    private static zzf zzQn;
    private final Context mContext;
    private final zzu zzQA;
    private final Context zzQo;
    private final zzr zzQp;
    private final zzaf zzQq;
    private final zzg zzQr;
    private final zzb zzQs;
    private final zzv zzQt;
    private final zzan zzQu;
    private final zzai zzQv;
    private final GoogleAnalytics zzQw;
    private final zzn zzQx;
    private final zza zzQy;
    private final zzk zzQz;
    private final zzmq zzqW;

    /* renamed from: com.google.android.gms.analytics.internal.zzf.1 */
    class C06601 implements UncaughtExceptionHandler {
        final /* synthetic */ zzf zzQB;

        C06601(zzf com_google_android_gms_analytics_internal_zzf) {
            this.zzQB = com_google_android_gms_analytics_internal_zzf;
        }

        public void uncaughtException(Thread thread, Throwable error) {
            zzaf zzjy = this.zzQB.zzjy();
            if (zzjy != null) {
                zzjy.zze("Job execution failed", error);
            }
        }
    }

    protected zzf(zzg com_google_android_gms_analytics_internal_zzg) {
        Object applicationContext = com_google_android_gms_analytics_internal_zzg.getApplicationContext();
        zzx.zzb(applicationContext, (Object) "Application context can't be null");
        zzx.zzb(applicationContext instanceof Application, (Object) "getApplicationContext didn't return the application");
        Context zzjx = com_google_android_gms_analytics_internal_zzg.zzjx();
        zzx.zzz(zzjx);
        this.mContext = applicationContext;
        this.zzQo = zzjx;
        this.zzqW = com_google_android_gms_analytics_internal_zzg.zzh(this);
        this.zzQp = com_google_android_gms_analytics_internal_zzg.zzg(this);
        zzaf zzf = com_google_android_gms_analytics_internal_zzg.zzf(this);
        zzf.zza();
        this.zzQq = zzf;
        if (zzjn().zzkr()) {
            zzjm().zzbf("Google Analytics " + zze.VERSION + " is starting up.");
        } else {
            zzjm().zzbf("Google Analytics " + zze.VERSION + " is starting up. " + "To enable debug logging on a device run:\n" + "  adb shell setprop log.tag.GAv4 DEBUG\n" + "  adb logcat -s GAv4");
        }
        zzai zzq = com_google_android_gms_analytics_internal_zzg.zzq(this);
        zzq.zza();
        this.zzQv = zzq;
        zzan zze = com_google_android_gms_analytics_internal_zzg.zze(this);
        zze.zza();
        this.zzQu = zze;
        zzb zzl = com_google_android_gms_analytics_internal_zzg.zzl(this);
        zzn zzd = com_google_android_gms_analytics_internal_zzg.zzd(this);
        zza zzc = com_google_android_gms_analytics_internal_zzg.zzc(this);
        zzk zzb = com_google_android_gms_analytics_internal_zzg.zzb(this);
        zzu zza = com_google_android_gms_analytics_internal_zzg.zza(this);
        zzg zzab = com_google_android_gms_analytics_internal_zzg.zzab(applicationContext);
        zzab.zza(zzjw());
        this.zzQr = zzab;
        GoogleAnalytics zzi = com_google_android_gms_analytics_internal_zzg.zzi(this);
        zzd.zza();
        this.zzQx = zzd;
        zzc.zza();
        this.zzQy = zzc;
        zzb.zza();
        this.zzQz = zzb;
        zza.zza();
        this.zzQA = zza;
        zzv zzp = com_google_android_gms_analytics_internal_zzg.zzp(this);
        zzp.zza();
        this.zzQt = zzp;
        zzl.zza();
        this.zzQs = zzl;
        if (zzjn().zzkr()) {
            zzjm().zzb("Device AnalyticsService version", zze.VERSION);
        }
        zzi.zza();
        this.zzQw = zzi;
        zzl.start();
    }

    private void zza(zzd com_google_android_gms_analytics_internal_zzd) {
        zzx.zzb((Object) com_google_android_gms_analytics_internal_zzd, (Object) "Analytics service not created/initialized");
        zzx.zzb(com_google_android_gms_analytics_internal_zzd.isInitialized(), (Object) "Analytics service not initialized");
    }

    public static zzf zzaa(Context context) {
        zzx.zzz(context);
        if (zzQn == null) {
            synchronized (zzf.class) {
                if (zzQn == null) {
                    zzmq zzsc = zzmt.zzsc();
                    long elapsedRealtime = zzsc.elapsedRealtime();
                    zzf com_google_android_gms_analytics_internal_zzf = new zzf(new zzg(context.getApplicationContext()));
                    zzQn = com_google_android_gms_analytics_internal_zzf;
                    GoogleAnalytics.zziF();
                    elapsedRealtime = zzsc.elapsedRealtime() - elapsedRealtime;
                    long longValue = ((Long) zzy.zzSz.get()).longValue();
                    if (elapsedRealtime > longValue) {
                        com_google_android_gms_analytics_internal_zzf.zzjm().zzc("Slow initialization (ms)", Long.valueOf(elapsedRealtime), Long.valueOf(longValue));
                    }
                }
            }
        }
        return zzQn;
    }

    public Context getContext() {
        return this.mContext;
    }

    public zzb zziH() {
        zza(this.zzQs);
        return this.zzQs;
    }

    public zzan zziI() {
        zza(this.zzQu);
        return this.zzQu;
    }

    public zzai zzjA() {
        return (this.zzQv == null || !this.zzQv.isInitialized()) ? null : this.zzQv;
    }

    public zza zzjB() {
        zza(this.zzQy);
        return this.zzQy;
    }

    public zzn zzjC() {
        zza(this.zzQx);
        return this.zzQx;
    }

    public void zzjk() {
        zzg.zzjk();
    }

    public zzmq zzjl() {
        return this.zzqW;
    }

    public zzaf zzjm() {
        zza(this.zzQq);
        return this.zzQq;
    }

    public zzr zzjn() {
        return this.zzQp;
    }

    public zzg zzjo() {
        zzx.zzz(this.zzQr);
        return this.zzQr;
    }

    public zzv zzjp() {
        zza(this.zzQt);
        return this.zzQt;
    }

    public zzai zzjq() {
        zza(this.zzQv);
        return this.zzQv;
    }

    public zzk zzjt() {
        zza(this.zzQz);
        return this.zzQz;
    }

    public zzu zzju() {
        return this.zzQA;
    }

    protected UncaughtExceptionHandler zzjw() {
        return new C06601(this);
    }

    public Context zzjx() {
        return this.zzQo;
    }

    public zzaf zzjy() {
        return this.zzQq;
    }

    public GoogleAnalytics zzjz() {
        zzx.zzz(this.zzQw);
        zzx.zzb(this.zzQw.isInitialized(), (Object) "Analytics instance not initialized");
        return this.zzQw;
    }
}
