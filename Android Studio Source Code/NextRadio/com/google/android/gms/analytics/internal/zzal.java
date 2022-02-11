package com.google.android.gms.analytics.internal;

import android.app.Activity;
import java.util.HashMap;
import java.util.Map;

public class zzal implements zzp {
    public String zzOV;
    public double zzTo;
    public int zzTp;
    public int zzTq;
    public int zzTr;
    public int zzTs;
    public Map<String, String> zzTt;

    public zzal() {
        this.zzTo = -1.0d;
        this.zzTp = -1;
        this.zzTq = -1;
        this.zzTr = -1;
        this.zzTs = -1;
        this.zzTt = new HashMap();
    }

    public int getSessionTimeout() {
        return this.zzTp;
    }

    public String getTrackingId() {
        return this.zzOV;
    }

    public String zzbr(String str) {
        String str2 = (String) this.zzTt.get(str);
        return str2 != null ? str2 : str;
    }

    public boolean zzlT() {
        return this.zzOV != null;
    }

    public boolean zzlU() {
        return this.zzTo >= 0.0d;
    }

    public double zzlV() {
        return this.zzTo;
    }

    public boolean zzlW() {
        return this.zzTp >= 0;
    }

    public boolean zzlX() {
        return this.zzTq != -1;
    }

    public boolean zzlY() {
        return this.zzTq == 1;
    }

    public boolean zzlZ() {
        return this.zzTr != -1;
    }

    public boolean zzma() {
        return this.zzTr == 1;
    }

    public boolean zzmb() {
        return this.zzTs == 1;
    }

    public String zzo(Activity activity) {
        return zzbr(activity.getClass().getCanonicalName());
    }
}
