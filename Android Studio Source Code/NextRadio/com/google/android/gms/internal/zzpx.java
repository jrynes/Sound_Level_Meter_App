package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.measurement.zze;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import java.util.HashMap;
import java.util.Map;

public final class zzpx extends zze<zzpx> {
    public String zzSU;
    public String zzaUY;
    public String zzaUZ;

    public String getAction() {
        return this.zzSU;
    }

    public String getTarget() {
        return this.zzaUZ;
    }

    public String toString() {
        Map hashMap = new HashMap();
        hashMap.put("network", this.zzaUY);
        hashMap.put(locationTracking.action, this.zzSU);
        hashMap.put("target", this.zzaUZ);
        return zze.zzF(hashMap);
    }

    public String zzBg() {
        return this.zzaUY;
    }

    public void zza(zzpx com_google_android_gms_internal_zzpx) {
        if (!TextUtils.isEmpty(this.zzaUY)) {
            com_google_android_gms_internal_zzpx.zzeJ(this.zzaUY);
        }
        if (!TextUtils.isEmpty(this.zzSU)) {
            com_google_android_gms_internal_zzpx.zzeF(this.zzSU);
        }
        if (!TextUtils.isEmpty(this.zzaUZ)) {
            com_google_android_gms_internal_zzpx.zzeK(this.zzaUZ);
        }
    }

    public void zzeF(String str) {
        this.zzSU = str;
    }

    public void zzeJ(String str) {
        this.zzaUY = str;
    }

    public void zzeK(String str) {
        this.zzaUZ = str;
    }
}
