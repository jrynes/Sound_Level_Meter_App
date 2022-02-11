package com.google.android.gms.analytics.internal;

import com.google.android.gms.internal.zzpq;

public class zzk extends zzd {
    private final zzpq zzQX;

    zzk(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf);
        this.zzQX = new zzpq();
    }

    public void zziE() {
        zzan zziI = zziI();
        String zzlg = zziI.zzlg();
        if (zzlg != null) {
            this.zzQX.setAppName(zzlg);
        }
        String zzli = zziI.zzli();
        if (zzli != null) {
            this.zzQX.setAppVersion(zzli);
        }
    }

    protected void zziJ() {
        zzjo().zzAH().zza(this.zzQX);
        zziE();
    }

    public zzpq zzjS() {
        zzjv();
        return this.zzQX;
    }
}
