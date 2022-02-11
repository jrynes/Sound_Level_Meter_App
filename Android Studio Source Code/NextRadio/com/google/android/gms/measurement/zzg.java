package com.google.android.gms.measurement;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzpq;
import com.google.android.gms.internal.zzps;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class zzg {
    private static volatile zzg zzaUv;
    private final Context mContext;
    private volatile zzpq zzQX;
    private final List<zzh> zzaUw;
    private final zzb zzaUx;
    private final zza zzaUy;
    private UncaughtExceptionHandler zzaUz;

    /* renamed from: com.google.android.gms.measurement.zzg.1 */
    class C10211 implements Runnable {
        final /* synthetic */ zzc zzaUA;
        final /* synthetic */ zzg zzaUB;

        C10211(zzg com_google_android_gms_measurement_zzg, zzc com_google_android_gms_measurement_zzc) {
            this.zzaUB = com_google_android_gms_measurement_zzg;
            this.zzaUA = com_google_android_gms_measurement_zzc;
        }

        public void run() {
            this.zzaUA.zzAB().zza(this.zzaUA);
            for (zzh zza : this.zzaUB.zzaUw) {
                zza.zza(this.zzaUA);
            }
            this.zzaUB.zzb(this.zzaUA);
        }
    }

    private class zza extends ThreadPoolExecutor {
        final /* synthetic */ zzg zzaUB;

        /* renamed from: com.google.android.gms.measurement.zzg.zza.1 */
        class C10221 extends FutureTask<T> {
            final /* synthetic */ zza zzaUC;

            C10221(zza com_google_android_gms_measurement_zzg_zza, Runnable runnable, Object obj) {
                this.zzaUC = com_google_android_gms_measurement_zzg_zza;
                super(runnable, obj);
            }

            protected void setException(Throwable error) {
                UncaughtExceptionHandler zzb = this.zzaUC.zzaUB.zzaUz;
                if (zzb != null) {
                    zzb.uncaughtException(Thread.currentThread(), error);
                } else if (Log.isLoggable("GAv4", 6)) {
                    Log.e("GAv4", "MeasurementExecutor: job failed with " + error);
                }
                super.setException(error);
            }
        }

        public zza(zzg com_google_android_gms_measurement_zzg) {
            this.zzaUB = com_google_android_gms_measurement_zzg;
            super(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
            setThreadFactory(new zzb());
        }

        protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
            return new C10221(this, runnable, value);
        }
    }

    private static class zzb implements ThreadFactory {
        private static final AtomicInteger zzaUD;

        static {
            zzaUD = new AtomicInteger();
        }

        private zzb() {
        }

        public Thread newThread(Runnable target) {
            return new zzc(target, "measurement-" + zzaUD.incrementAndGet());
        }
    }

    private static class zzc extends Thread {
        zzc(Runnable runnable, String str) {
            super(runnable, str);
        }

        public void run() {
            Process.setThreadPriority(10);
            super.run();
        }
    }

    zzg(Context context) {
        Context applicationContext = context.getApplicationContext();
        zzx.zzz(applicationContext);
        this.mContext = applicationContext;
        this.zzaUy = new zza(this);
        this.zzaUw = new CopyOnWriteArrayList();
        this.zzaUx = new zzb();
    }

    public static zzg zzaS(Context context) {
        zzx.zzz(context);
        if (zzaUv == null) {
            synchronized (zzg.class) {
                if (zzaUv == null) {
                    zzaUv = new zzg(context);
                }
            }
        }
        return zzaUv;
    }

    private void zzb(zzc com_google_android_gms_measurement_zzc) {
        zzx.zzcE("deliver should be called from worker thread");
        zzx.zzb(com_google_android_gms_measurement_zzc.zzAz(), (Object) "Measurement must be submitted");
        List<zzi> zzAw = com_google_android_gms_measurement_zzc.zzAw();
        if (!zzAw.isEmpty()) {
            Set hashSet = new HashSet();
            for (zzi com_google_android_gms_measurement_zzi : zzAw) {
                Uri zziA = com_google_android_gms_measurement_zzi.zziA();
                if (!hashSet.contains(zziA)) {
                    hashSet.add(zziA);
                    com_google_android_gms_measurement_zzi.zzb(com_google_android_gms_measurement_zzc);
                }
            }
        }
    }

    public static void zzjk() {
        if (!(Thread.currentThread() instanceof zzc)) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public zzpq zzAH() {
        if (this.zzQX == null) {
            synchronized (this) {
                if (this.zzQX == null) {
                    zzpq com_google_android_gms_internal_zzpq = new zzpq();
                    PackageManager packageManager = this.mContext.getPackageManager();
                    String packageName = this.mContext.getPackageName();
                    com_google_android_gms_internal_zzpq.setAppId(packageName);
                    com_google_android_gms_internal_zzpq.setAppInstallerId(packageManager.getInstallerPackageName(packageName));
                    String str = null;
                    try {
                        PackageInfo packageInfo = packageManager.getPackageInfo(this.mContext.getPackageName(), 0);
                        if (packageInfo != null) {
                            CharSequence applicationLabel = packageManager.getApplicationLabel(packageInfo.applicationInfo);
                            if (!TextUtils.isEmpty(applicationLabel)) {
                                packageName = applicationLabel.toString();
                            }
                            str = packageInfo.versionName;
                        }
                    } catch (NameNotFoundException e) {
                        Log.e("GAv4", "Error retrieving package info: appName set to " + packageName);
                    }
                    com_google_android_gms_internal_zzpq.setAppName(packageName);
                    com_google_android_gms_internal_zzpq.setAppVersion(str);
                    this.zzQX = com_google_android_gms_internal_zzpq;
                }
            }
        }
        return this.zzQX;
    }

    public zzps zzAI() {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        zzps com_google_android_gms_internal_zzps = new zzps();
        com_google_android_gms_internal_zzps.setLanguage(zzam.zza(Locale.getDefault()));
        com_google_android_gms_internal_zzps.zziB(displayMetrics.widthPixels);
        com_google_android_gms_internal_zzps.zziC(displayMetrics.heightPixels);
        return com_google_android_gms_internal_zzps;
    }

    public void zza(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.zzaUz = uncaughtExceptionHandler;
    }

    public <V> Future<V> zzc(Callable<V> callable) {
        zzx.zzz(callable);
        if (!(Thread.currentThread() instanceof zzc)) {
            return this.zzaUy.submit(callable);
        }
        Future futureTask = new FutureTask(callable);
        futureTask.run();
        return futureTask;
    }

    void zze(zzc com_google_android_gms_measurement_zzc) {
        if (com_google_android_gms_measurement_zzc.zzAD()) {
            throw new IllegalStateException("Measurement prototype can't be submitted");
        } else if (com_google_android_gms_measurement_zzc.zzAz()) {
            throw new IllegalStateException("Measurement can only be submitted once");
        } else {
            zzc zzAu = com_google_android_gms_measurement_zzc.zzAu();
            zzAu.zzAA();
            this.zzaUy.execute(new C10211(this, zzAu));
        }
    }

    public void zzf(Runnable runnable) {
        zzx.zzz(runnable);
        this.zzaUy.submit(runnable);
    }
}
