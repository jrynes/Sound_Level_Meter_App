package com.google.android.gms.internal;

import com.google.android.gms.measurement.zze;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class zzkc extends zze<zzkc> {
    private Map<Integer, Double> zzPM;

    public zzkc() {
        this.zzPM = new HashMap(4);
    }

    public String toString() {
        Map hashMap = new HashMap();
        for (Entry entry : this.zzPM.entrySet()) {
            hashMap.put("metric" + entry.getKey(), entry.getValue());
        }
        return zze.zzF(hashMap);
    }

    public void zza(zzkc com_google_android_gms_internal_zzkc) {
        com_google_android_gms_internal_zzkc.zzPM.putAll(this.zzPM);
    }

    public Map<Integer, Double> zziQ() {
        return Collections.unmodifiableMap(this.zzPM);
    }
}
