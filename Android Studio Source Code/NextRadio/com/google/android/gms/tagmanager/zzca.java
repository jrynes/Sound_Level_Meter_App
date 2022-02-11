package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;
import java.util.Set;

public abstract class zzca extends zzak {
    private static final String zzbiQ;
    private static final String zzbjO;

    static {
        zzbiQ = zzae.ARG0.toString();
        zzbjO = zzae.ARG1.toString();
    }

    public zzca(String str) {
        super(str, zzbiQ, zzbjO);
    }

    public boolean zzFW() {
        return true;
    }

    public /* bridge */ /* synthetic */ String zzGB() {
        return super.zzGB();
    }

    public /* bridge */ /* synthetic */ Set zzGC() {
        return super.zzGC();
    }

    public zza zzP(Map<String, zza> map) {
        for (zza com_google_android_gms_internal_zzag_zza : map.values()) {
            if (com_google_android_gms_internal_zzag_zza == zzdf.zzHF()) {
                return zzdf.zzR(Boolean.valueOf(false));
            }
        }
        zza com_google_android_gms_internal_zzag_zza2 = (zza) map.get(zzbiQ);
        zza com_google_android_gms_internal_zzag_zza3 = (zza) map.get(zzbjO);
        boolean zza = (com_google_android_gms_internal_zzag_zza2 == null || com_google_android_gms_internal_zzag_zza3 == null) ? false : zza(com_google_android_gms_internal_zzag_zza2, com_google_android_gms_internal_zzag_zza3, map);
        return zzdf.zzR(Boolean.valueOf(zza));
    }

    protected abstract boolean zza(zza com_google_android_gms_internal_zzag_zza, zza com_google_android_gms_internal_zzag_zza2, Map<String, zza> map);
}
