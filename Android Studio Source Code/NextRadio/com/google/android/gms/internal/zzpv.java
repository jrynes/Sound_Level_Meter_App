package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.measurement.zze;
import com.nextradioapp.androidSDK.data.schema.Tables.activityEvents;
import java.util.HashMap;
import java.util.Map;

public final class zzpv extends zze<zzpv> {
    public boolean zzaUP;
    public String zzaxl;

    public String getDescription() {
        return this.zzaxl;
    }

    public void setDescription(String description) {
        this.zzaxl = description;
    }

    public String toString() {
        Map hashMap = new HashMap();
        hashMap.put(activityEvents.description, this.zzaxl);
        hashMap.put("fatal", Boolean.valueOf(this.zzaUP));
        return zze.zzF(hashMap);
    }

    public boolean zzBa() {
        return this.zzaUP;
    }

    public void zza(zzpv com_google_android_gms_internal_zzpv) {
        if (!TextUtils.isEmpty(this.zzaxl)) {
            com_google_android_gms_internal_zzpv.setDescription(this.zzaxl);
        }
        if (this.zzaUP) {
            com_google_android_gms_internal_zzpv.zzao(this.zzaUP);
        }
    }

    public void zzao(boolean z) {
        this.zzaUP = z;
    }
}
