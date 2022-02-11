package com.google.android.gms.measurement;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class zzc {
    private final zzf zzaUi;
    private boolean zzaUj;
    private long zzaUk;
    private long zzaUl;
    private long zzaUm;
    private long zzaUn;
    private long zzaUo;
    private boolean zzaUp;
    private final Map<Class<? extends zze>, zze> zzaUq;
    private final List<zzi> zzaUr;
    private final zzmq zzqW;

    zzc(zzc com_google_android_gms_measurement_zzc) {
        this.zzaUi = com_google_android_gms_measurement_zzc.zzaUi;
        this.zzqW = com_google_android_gms_measurement_zzc.zzqW;
        this.zzaUk = com_google_android_gms_measurement_zzc.zzaUk;
        this.zzaUl = com_google_android_gms_measurement_zzc.zzaUl;
        this.zzaUm = com_google_android_gms_measurement_zzc.zzaUm;
        this.zzaUn = com_google_android_gms_measurement_zzc.zzaUn;
        this.zzaUo = com_google_android_gms_measurement_zzc.zzaUo;
        this.zzaUr = new ArrayList(com_google_android_gms_measurement_zzc.zzaUr);
        this.zzaUq = new HashMap(com_google_android_gms_measurement_zzc.zzaUq.size());
        for (Entry entry : com_google_android_gms_measurement_zzc.zzaUq.entrySet()) {
            zze zzg = zzg((Class) entry.getKey());
            ((zze) entry.getValue()).zza(zzg);
            this.zzaUq.put(entry.getKey(), zzg);
        }
    }

    zzc(zzf com_google_android_gms_measurement_zzf, zzmq com_google_android_gms_internal_zzmq) {
        zzx.zzz(com_google_android_gms_measurement_zzf);
        zzx.zzz(com_google_android_gms_internal_zzmq);
        this.zzaUi = com_google_android_gms_measurement_zzf;
        this.zzqW = com_google_android_gms_internal_zzmq;
        this.zzaUn = 1800000;
        this.zzaUo = 3024000000L;
        this.zzaUq = new HashMap();
        this.zzaUr = new ArrayList();
    }

    private static <T extends zze> T zzg(Class<T> cls) {
        try {
            return (zze) cls.newInstance();
        } catch (Throwable e) {
            throw new IllegalArgumentException("dataType doesn't have default constructor", e);
        } catch (Throwable e2) {
            throw new IllegalArgumentException("dataType default constructor is not accessible", e2);
        }
    }

    void zzAA() {
        this.zzaUm = this.zzqW.elapsedRealtime();
        if (this.zzaUl != 0) {
            this.zzaUk = this.zzaUl;
        } else {
            this.zzaUk = this.zzqW.currentTimeMillis();
        }
        this.zzaUj = true;
    }

    zzf zzAB() {
        return this.zzaUi;
    }

    zzg zzAC() {
        return this.zzaUi.zzAC();
    }

    boolean zzAD() {
        return this.zzaUp;
    }

    void zzAE() {
        this.zzaUp = true;
    }

    public zzc zzAu() {
        return new zzc(this);
    }

    public Collection<zze> zzAv() {
        return this.zzaUq.values();
    }

    public List<zzi> zzAw() {
        return this.zzaUr;
    }

    public long zzAx() {
        return this.zzaUk;
    }

    public void zzAy() {
        zzAC().zze(this);
    }

    public boolean zzAz() {
        return this.zzaUj;
    }

    public void zzM(long j) {
        this.zzaUl = j;
    }

    public void zzb(zze com_google_android_gms_measurement_zze) {
        zzx.zzz(com_google_android_gms_measurement_zze);
        Class cls = com_google_android_gms_measurement_zze.getClass();
        if (cls.getSuperclass() != zze.class) {
            throw new IllegalArgumentException();
        }
        com_google_android_gms_measurement_zze.zza(zzf(cls));
    }

    public <T extends zze> T zze(Class<T> cls) {
        return (zze) this.zzaUq.get(cls);
    }

    public <T extends zze> T zzf(Class<T> cls) {
        zze com_google_android_gms_measurement_zze = (zze) this.zzaUq.get(cls);
        if (com_google_android_gms_measurement_zze != null) {
            return com_google_android_gms_measurement_zze;
        }
        T zzg = zzg(cls);
        this.zzaUq.put(cls, zzg);
        return zzg;
    }
}
