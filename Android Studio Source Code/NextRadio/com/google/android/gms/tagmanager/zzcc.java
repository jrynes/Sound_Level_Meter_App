package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;

class zzcc extends zzak {
    private static final String ID;
    private static final String zzbjY;
    private static final String zzbjZ;

    static {
        ID = zzad.RANDOM.toString();
        zzbjY = zzae.MIN.toString();
        zzbjZ = zzae.MAX.toString();
    }

    public zzcc() {
        super(ID, new String[0]);
    }

    public boolean zzFW() {
        return false;
    }

    public zza zzP(Map<String, zza> map) {
        double doubleValue;
        double d;
        zza com_google_android_gms_internal_zzag_zza = (zza) map.get(zzbjY);
        zza com_google_android_gms_internal_zzag_zza2 = (zza) map.get(zzbjZ);
        if (!(com_google_android_gms_internal_zzag_zza == null || com_google_android_gms_internal_zzag_zza == zzdf.zzHF() || com_google_android_gms_internal_zzag_zza2 == null || com_google_android_gms_internal_zzag_zza2 == zzdf.zzHF())) {
            zzde zzh = zzdf.zzh(com_google_android_gms_internal_zzag_zza);
            zzde zzh2 = zzdf.zzh(com_google_android_gms_internal_zzag_zza2);
            if (!(zzh == zzdf.zzHD() || zzh2 == zzdf.zzHD())) {
                double doubleValue2 = zzh.doubleValue();
                doubleValue = zzh2.doubleValue();
                if (doubleValue2 <= doubleValue) {
                    d = doubleValue2;
                    return zzdf.zzR(Long.valueOf(Math.round(((doubleValue - d) * Math.random()) + d)));
                }
            }
        }
        doubleValue = 2.147483647E9d;
        d = 0.0d;
        return zzdf.zzR(Long.valueOf(Math.round(((doubleValue - d) * Math.random()) + d)));
    }
}
