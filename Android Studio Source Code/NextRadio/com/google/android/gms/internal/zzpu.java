package com.google.android.gms.internal;

import android.text.TextUtils;
import com.admarvel.android.ads.Constants;
import com.google.android.gms.measurement.zze;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import java.util.HashMap;
import java.util.Map;

public final class zzpu extends zze<zzpu> {
    private String mCategory;
    private String zzSU;
    private long zzaDV;
    private String zzaUO;

    public String getAction() {
        return this.zzSU;
    }

    public String getLabel() {
        return this.zzaUO;
    }

    public long getValue() {
        return this.zzaDV;
    }

    public String toString() {
        Map hashMap = new HashMap();
        hashMap.put("category", this.mCategory);
        hashMap.put(locationTracking.action, this.zzSU);
        hashMap.put("label", this.zzaUO);
        hashMap.put(Constants.NATIVE_AD_VALUE_ELEMENT, Long.valueOf(this.zzaDV));
        return zze.zzF(hashMap);
    }

    public String zzAZ() {
        return this.mCategory;
    }

    public void zzN(long j) {
        this.zzaDV = j;
    }

    public void zza(zzpu com_google_android_gms_internal_zzpu) {
        if (!TextUtils.isEmpty(this.mCategory)) {
            com_google_android_gms_internal_zzpu.zzeE(this.mCategory);
        }
        if (!TextUtils.isEmpty(this.zzSU)) {
            com_google_android_gms_internal_zzpu.zzeF(this.zzSU);
        }
        if (!TextUtils.isEmpty(this.zzaUO)) {
            com_google_android_gms_internal_zzpu.zzeG(this.zzaUO);
        }
        if (this.zzaDV != 0) {
            com_google_android_gms_internal_zzpu.zzN(this.zzaDV);
        }
    }

    public void zzeE(String str) {
        this.mCategory = str;
    }

    public void zzeF(String str) {
        this.zzSU = str;
    }

    public void zzeG(String str) {
        this.zzaUO = str;
    }
}
