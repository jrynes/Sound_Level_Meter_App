package com.google.android.gms.analytics;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import com.google.android.gms.analytics.internal.zzae;
import com.google.android.gms.analytics.internal.zzak;
import com.google.android.gms.analytics.internal.zzal;
import com.google.android.gms.analytics.internal.zzan;
import com.google.android.gms.analytics.internal.zzf;
import com.google.android.gms.analytics.internal.zzy;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GoogleAnalytics extends zza {
    private static List<Runnable> zzPe;
    private boolean zzPf;
    private Set<zza> zzPg;
    private boolean zzPh;
    private boolean zzPi;
    private volatile boolean zzPj;
    private boolean zzPk;
    private boolean zzqA;

    interface zza {
        void zzl(Activity activity);

        void zzm(Activity activity);
    }

    @TargetApi(14)
    class zzb implements ActivityLifecycleCallbacks {
        final /* synthetic */ GoogleAnalytics zzPl;

        zzb(GoogleAnalytics googleAnalytics) {
            this.zzPl = googleAnalytics;
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        public void onActivityStarted(Activity activity) {
            this.zzPl.zzj(activity);
        }

        public void onActivityStopped(Activity activity) {
            this.zzPl.zzk(activity);
        }
    }

    static {
        zzPe = new ArrayList();
    }

    public GoogleAnalytics(zzf context) {
        super(context);
        this.zzPg = new HashSet();
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"})
    public static GoogleAnalytics getInstance(Context context) {
        return zzf.zzaa(context).zzjz();
    }

    public static void zziF() {
        synchronized (GoogleAnalytics.class) {
            if (zzPe != null) {
                for (Runnable run : zzPe) {
                    run.run();
                }
                zzPe = null;
            }
        }
    }

    private com.google.android.gms.analytics.internal.zzb zziH() {
        return zzix().zziH();
    }

    private zzan zziI() {
        return zzix().zziI();
    }

    public void dispatchLocalHits() {
        zziH().zzjd();
    }

    @TargetApi(14)
    public void enableAutoActivityReports(Application application) {
        if (VERSION.SDK_INT >= 14 && !this.zzPh) {
            application.registerActivityLifecycleCallbacks(new zzb(this));
            this.zzPh = true;
        }
    }

    public boolean getAppOptOut() {
        return this.zzPj;
    }

    public String getClientId() {
        zzx.zzcE("getClientId can not be called from the main thread");
        return zzix().zzjC().zzkk();
    }

    @Deprecated
    public Logger getLogger() {
        return zzae.getLogger();
    }

    public boolean isDryRunEnabled() {
        return this.zzPi;
    }

    public boolean isInitialized() {
        return this.zzqA && !this.zzPf;
    }

    public Tracker newTracker(int configResId) {
        Tracker tracker;
        synchronized (this) {
            tracker = new Tracker(zzix(), null, null);
            if (configResId > 0) {
                zzal com_google_android_gms_analytics_internal_zzal = (zzal) new zzak(zzix()).zzah(configResId);
                if (com_google_android_gms_analytics_internal_zzal != null) {
                    tracker.zza(com_google_android_gms_analytics_internal_zzal);
                }
            }
            tracker.zza();
        }
        return tracker;
    }

    public Tracker newTracker(String trackingId) {
        Tracker tracker;
        synchronized (this) {
            tracker = new Tracker(zzix(), trackingId, null);
            tracker.zza();
        }
        return tracker;
    }

    public void reportActivityStart(Activity activity) {
        if (!this.zzPh) {
            zzj(activity);
        }
    }

    public void reportActivityStop(Activity activity) {
        if (!this.zzPh) {
            zzk(activity);
        }
    }

    public void setAppOptOut(boolean optOut) {
        this.zzPj = optOut;
        if (this.zzPj) {
            zziH().zzjc();
        }
    }

    public void setDryRun(boolean dryRun) {
        this.zzPi = dryRun;
    }

    public void setLocalDispatchPeriod(int dispatchPeriodInSeconds) {
        zziH().setLocalDispatchPeriod(dispatchPeriodInSeconds);
    }

    @Deprecated
    public void setLogger(Logger logger) {
        zzae.setLogger(logger);
        if (!this.zzPk) {
            Log.i((String) zzy.zzRL.get(), "GoogleAnalytics.setLogger() is deprecated. To enable debug logging, please run:\nadb shell setprop log.tag." + ((String) zzy.zzRL.get()) + " DEBUG");
            this.zzPk = true;
        }
    }

    public void zza() {
        zziE();
        this.zzqA = true;
    }

    void zza(zza com_google_android_gms_analytics_GoogleAnalytics_zza) {
        this.zzPg.add(com_google_android_gms_analytics_GoogleAnalytics_zza);
        Context context = zzix().getContext();
        if (context instanceof Application) {
            enableAutoActivityReports((Application) context);
        }
    }

    void zzb(zza com_google_android_gms_analytics_GoogleAnalytics_zza) {
        this.zzPg.remove(com_google_android_gms_analytics_GoogleAnalytics_zza);
    }

    void zziE() {
        zzan zziI = zziI();
        if (zziI.zzlj()) {
            getLogger().setLogLevel(zziI.getLogLevel());
        }
        if (zziI.zzln()) {
            setDryRun(zziI.zzlo());
        }
        if (zziI.zzlj()) {
            Logger logger = zzae.getLogger();
            if (logger != null) {
                logger.setLogLevel(zziI.getLogLevel());
            }
        }
    }

    void zziG() {
        zziH().zzje();
    }

    void zzj(Activity activity) {
        for (zza zzl : this.zzPg) {
            zzl.zzl(activity);
        }
    }

    void zzk(Activity activity) {
        for (zza zzm : this.zzPg) {
            zzm.zzm(activity);
        }
    }
}
