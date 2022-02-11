package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.measurement.zze;
import java.util.HashMap;
import java.util.Map;

public final class zzke extends zze<zzke> {
    private String zzPN;
    private String zzPO;
    private String zzPP;
    private boolean zzPQ;
    private String zzPR;
    private boolean zzPS;
    private double zzPT;
    private String zzrG;

    public String getClientId() {
        return this.zzPO;
    }

    public String getUserId() {
        return this.zzrG;
    }

    public void setClientId(String clientId) {
        this.zzPO = clientId;
    }

    public void setSampleRate(double percentage) {
        boolean z = percentage >= 0.0d && percentage <= 100.0d;
        zzx.zzb(z, (Object) "Sample rate must be between 0% and 100%");
        this.zzPT = percentage;
    }

    public void setUserId(String userId) {
        this.zzrG = userId;
    }

    public String toString() {
        Map hashMap = new HashMap();
        hashMap.put("hitType", this.zzPN);
        hashMap.put("clientId", this.zzPO);
        hashMap.put("userId", this.zzrG);
        hashMap.put("androidAdId", this.zzPP);
        hashMap.put("AdTargetingEnabled", Boolean.valueOf(this.zzPQ));
        hashMap.put("sessionControl", this.zzPR);
        hashMap.put("nonInteraction", Boolean.valueOf(this.zzPS));
        hashMap.put("sampleRate", Double.valueOf(this.zzPT));
        return zze.zzF(hashMap);
    }

    public void zzH(boolean z) {
        this.zzPQ = z;
    }

    public void zzI(boolean z) {
        this.zzPS = z;
    }

    public void zza(zzke com_google_android_gms_internal_zzke) {
        if (!TextUtils.isEmpty(this.zzPN)) {
            com_google_android_gms_internal_zzke.zzaX(this.zzPN);
        }
        if (!TextUtils.isEmpty(this.zzPO)) {
            com_google_android_gms_internal_zzke.setClientId(this.zzPO);
        }
        if (!TextUtils.isEmpty(this.zzrG)) {
            com_google_android_gms_internal_zzke.setUserId(this.zzrG);
        }
        if (!TextUtils.isEmpty(this.zzPP)) {
            com_google_android_gms_internal_zzke.zzaY(this.zzPP);
        }
        if (this.zzPQ) {
            com_google_android_gms_internal_zzke.zzH(true);
        }
        if (!TextUtils.isEmpty(this.zzPR)) {
            com_google_android_gms_internal_zzke.zzaZ(this.zzPR);
        }
        if (this.zzPS) {
            com_google_android_gms_internal_zzke.zzI(this.zzPS);
        }
        if (this.zzPT != 0.0d) {
            com_google_android_gms_internal_zzke.setSampleRate(this.zzPT);
        }
    }

    public void zzaX(String str) {
        this.zzPN = str;
    }

    public void zzaY(String str) {
        this.zzPP = str;
    }

    public void zzaZ(String str) {
        this.zzPR = str;
    }

    public String zziS() {
        return this.zzPN;
    }

    public String zziT() {
        return this.zzPP;
    }

    public boolean zziU() {
        return this.zzPQ;
    }

    public String zziV() {
        return this.zzPR;
    }

    public boolean zziW() {
        return this.zzPS;
    }

    public double zziX() {
        return this.zzPT;
    }
}
