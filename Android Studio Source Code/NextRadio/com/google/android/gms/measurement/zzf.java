package com.google.android.gms.measurement;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import java.util.ArrayList;
import java.util.List;

public abstract class zzf<T extends zzf> {
    private final zzg zzaUs;
    protected final zzc zzaUt;
    private final List<zzd> zzaUu;

    protected zzf(zzg com_google_android_gms_measurement_zzg, zzmq com_google_android_gms_internal_zzmq) {
        zzx.zzz(com_google_android_gms_measurement_zzg);
        this.zzaUs = com_google_android_gms_measurement_zzg;
        this.zzaUu = new ArrayList();
        zzc com_google_android_gms_measurement_zzc = new zzc(this, com_google_android_gms_internal_zzmq);
        com_google_android_gms_measurement_zzc.zzAE();
        this.zzaUt = com_google_android_gms_measurement_zzc;
    }

    protected zzg zzAC() {
        return this.zzaUs;
    }

    public zzc zzAF() {
        return this.zzaUt;
    }

    public List<zzi> zzAG() {
        return this.zzaUt.zzAw();
    }

    protected void zza(zzc com_google_android_gms_measurement_zzc) {
    }

    protected void zzd(zzc com_google_android_gms_measurement_zzc) {
        for (zzd zza : this.zzaUu) {
            zza.zza(this, com_google_android_gms_measurement_zzc);
        }
    }

    public zzc zziy() {
        zzc zzAu = this.zzaUt.zzAu();
        zzd(zzAu);
        return zzAu;
    }
}
