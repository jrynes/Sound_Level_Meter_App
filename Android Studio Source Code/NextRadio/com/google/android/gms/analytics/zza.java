package com.google.android.gms.analytics;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzke;
import com.google.android.gms.measurement.zzc;
import com.google.android.gms.measurement.zzf;
import com.google.android.gms.measurement.zzi;
import java.util.ListIterator;

public class zza extends zzf<zza> {
    private final com.google.android.gms.analytics.internal.zzf zzOK;
    private boolean zzOL;

    public zza(com.google.android.gms.analytics.internal.zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf.zzjo(), com_google_android_gms_analytics_internal_zzf.zzjl());
        this.zzOK = com_google_android_gms_analytics_internal_zzf;
    }

    public void enableAdvertisingIdCollection(boolean enable) {
        this.zzOL = enable;
    }

    protected void zza(zzc com_google_android_gms_measurement_zzc) {
        zzke com_google_android_gms_internal_zzke = (zzke) com_google_android_gms_measurement_zzc.zzf(zzke.class);
        if (TextUtils.isEmpty(com_google_android_gms_internal_zzke.getClientId())) {
            com_google_android_gms_internal_zzke.setClientId(this.zzOK.zzjC().zzkk());
        }
        if (this.zzOL && TextUtils.isEmpty(com_google_android_gms_internal_zzke.zziT())) {
            com.google.android.gms.analytics.internal.zza zzjB = this.zzOK.zzjB();
            com_google_android_gms_internal_zzke.zzaY(zzjB.zziY());
            com_google_android_gms_internal_zzke.zzH(zzjB.zziU());
        }
    }

    public void zzaS(String str) {
        zzx.zzcM(str);
        zzaT(str);
        zzAG().add(new zzb(this.zzOK, str));
    }

    public void zzaT(String str) {
        Uri zzaU = zzb.zzaU(str);
        ListIterator listIterator = zzAG().listIterator();
        while (listIterator.hasNext()) {
            if (zzaU.equals(((zzi) listIterator.next()).zziA())) {
                listIterator.remove();
            }
        }
    }

    com.google.android.gms.analytics.internal.zzf zzix() {
        return this.zzOK;
    }

    public zzc zziy() {
        zzc zzAu = zzAF().zzAu();
        zzAu.zzb(this.zzOK.zzjt().zzjS());
        zzAu.zzb(this.zzOK.zzju().zzkZ());
        zzd(zzAu);
        return zzAu;
    }
}
