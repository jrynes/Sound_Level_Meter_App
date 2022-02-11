package com.google.android.gms.analytics.internal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.analytics.AnalyticsReceiver;
import com.google.android.gms.common.internal.zzx;

public class zzv extends zzd {
    private boolean zzRG;
    private boolean zzRH;
    private AlarmManager zzRI;

    protected zzv(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf);
        this.zzRI = (AlarmManager) getContext().getSystemService(NotificationCompat.CATEGORY_ALARM);
    }

    private PendingIntent zzld() {
        Intent intent = new Intent(getContext(), AnalyticsReceiver.class);
        intent.setAction("com.google.android.gms.analytics.ANALYTICS_DISPATCH");
        return PendingIntent.getBroadcast(getContext(), 0, intent, 0);
    }

    public void cancel() {
        zzjv();
        this.zzRH = false;
        this.zzRI.cancel(zzld());
    }

    public boolean zzbw() {
        return this.zzRH;
    }

    protected void zziJ() {
        try {
            this.zzRI.cancel(zzld());
            if (zzjn().zzkA() > 0) {
                ActivityInfo receiverInfo = getContext().getPackageManager().getReceiverInfo(new ComponentName(getContext(), AnalyticsReceiver.class), 2);
                if (receiverInfo != null && receiverInfo.enabled) {
                    zzbd("Receiver registered. Using alarm for local dispatch.");
                    this.zzRG = true;
                }
            }
        } catch (NameNotFoundException e) {
        }
    }

    public boolean zzlb() {
        return this.zzRG;
    }

    public void zzlc() {
        zzjv();
        zzx.zza(zzlb(), (Object) "Receiver not registered");
        long zzkA = zzjn().zzkA();
        if (zzkA > 0) {
            cancel();
            long elapsedRealtime = zzjl().elapsedRealtime() + zzkA;
            this.zzRH = true;
            this.zzRI.setInexactRepeating(2, elapsedRealtime, 0, zzld());
        }
    }
}
