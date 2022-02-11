package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.measurement.zze;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import java.util.HashMap;
import java.util.Map;

public final class zzpr extends zze<zzpr> {
    private String mName;
    private String zzaPI;
    private String zzaUF;
    private String zzaUG;
    private String zzaUH;
    private String zzaUI;
    private String zzaUJ;
    private String zzaUK;
    private String zzxG;
    private String zzyv;

    public String getContent() {
        return this.zzxG;
    }

    public String getId() {
        return this.zzyv;
    }

    public String getName() {
        return this.mName;
    }

    public String getSource() {
        return this.zzaPI;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String toString() {
        Map hashMap = new HashMap();
        hashMap.put("name", this.mName);
        hashMap.put(locationTracking.source, this.zzaPI);
        hashMap.put("medium", this.zzaUF);
        hashMap.put("keyword", this.zzaUG);
        hashMap.put("content", this.zzxG);
        hashMap.put(Name.MARK, this.zzyv);
        hashMap.put("adNetworkId", this.zzaUH);
        hashMap.put("gclid", this.zzaUI);
        hashMap.put("dclid", this.zzaUJ);
        hashMap.put("aclid", this.zzaUK);
        return zze.zzF(hashMap);
    }

    public String zzAK() {
        return this.zzaUF;
    }

    public String zzAL() {
        return this.zzaUG;
    }

    public String zzAM() {
        return this.zzaUH;
    }

    public String zzAN() {
        return this.zzaUI;
    }

    public String zzAO() {
        return this.zzaUJ;
    }

    public String zzAP() {
        return this.zzaUK;
    }

    public void zza(zzpr com_google_android_gms_internal_zzpr) {
        if (!TextUtils.isEmpty(this.mName)) {
            com_google_android_gms_internal_zzpr.setName(this.mName);
        }
        if (!TextUtils.isEmpty(this.zzaPI)) {
            com_google_android_gms_internal_zzpr.zzev(this.zzaPI);
        }
        if (!TextUtils.isEmpty(this.zzaUF)) {
            com_google_android_gms_internal_zzpr.zzew(this.zzaUF);
        }
        if (!TextUtils.isEmpty(this.zzaUG)) {
            com_google_android_gms_internal_zzpr.zzex(this.zzaUG);
        }
        if (!TextUtils.isEmpty(this.zzxG)) {
            com_google_android_gms_internal_zzpr.zzey(this.zzxG);
        }
        if (!TextUtils.isEmpty(this.zzyv)) {
            com_google_android_gms_internal_zzpr.zzez(this.zzyv);
        }
        if (!TextUtils.isEmpty(this.zzaUH)) {
            com_google_android_gms_internal_zzpr.zzeA(this.zzaUH);
        }
        if (!TextUtils.isEmpty(this.zzaUI)) {
            com_google_android_gms_internal_zzpr.zzeB(this.zzaUI);
        }
        if (!TextUtils.isEmpty(this.zzaUJ)) {
            com_google_android_gms_internal_zzpr.zzeC(this.zzaUJ);
        }
        if (!TextUtils.isEmpty(this.zzaUK)) {
            com_google_android_gms_internal_zzpr.zzeD(this.zzaUK);
        }
    }

    public void zzeA(String str) {
        this.zzaUH = str;
    }

    public void zzeB(String str) {
        this.zzaUI = str;
    }

    public void zzeC(String str) {
        this.zzaUJ = str;
    }

    public void zzeD(String str) {
        this.zzaUK = str;
    }

    public void zzev(String str) {
        this.zzaPI = str;
    }

    public void zzew(String str) {
        this.zzaUF = str;
    }

    public void zzex(String str) {
        this.zzaUG = str;
    }

    public void zzey(String str) {
        this.zzxG = str;
    }

    public void zzez(String str) {
        this.zzyv = str;
    }
}
