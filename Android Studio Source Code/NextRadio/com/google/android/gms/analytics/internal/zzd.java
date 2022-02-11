package com.google.android.gms.analytics.internal;

public abstract class zzd extends zzc {
    private boolean zzQk;
    private boolean zzQl;

    protected zzd(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf);
    }

    public boolean isInitialized() {
        return this.zzQk && !this.zzQl;
    }

    public void zza() {
        zziJ();
        this.zzQk = true;
    }

    protected abstract void zziJ();

    protected void zzjv() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }
}
