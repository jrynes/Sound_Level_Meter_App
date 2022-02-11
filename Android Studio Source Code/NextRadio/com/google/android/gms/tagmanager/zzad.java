package com.google.android.gms.tagmanager;

import android.util.Base64;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;

class zzad extends zzak {
    private static final String ID;
    private static final String zzbiQ;
    private static final String zzbiR;
    private static final String zzbiS;
    private static final String zzbiT;

    static {
        ID = com.google.android.gms.internal.zzad.ENCODE.toString();
        zzbiQ = zzae.ARG0.toString();
        zzbiR = zzae.NO_PADDING.toString();
        zzbiS = zzae.INPUT_FORMAT.toString();
        zzbiT = zzae.OUTPUT_FORMAT.toString();
    }

    public zzad() {
        super(ID, zzbiQ);
    }

    public boolean zzFW() {
        return true;
    }

    public zza zzP(Map<String, zza> map) {
        zza com_google_android_gms_internal_zzag_zza = (zza) map.get(zzbiQ);
        if (com_google_android_gms_internal_zzag_zza == null || com_google_android_gms_internal_zzag_zza == zzdf.zzHF()) {
            return zzdf.zzHF();
        }
        String zzg = zzdf.zzg(com_google_android_gms_internal_zzag_zza);
        com_google_android_gms_internal_zzag_zza = (zza) map.get(zzbiS);
        String zzg2 = com_google_android_gms_internal_zzag_zza == null ? "text" : zzdf.zzg(com_google_android_gms_internal_zzag_zza);
        com_google_android_gms_internal_zzag_zza = (zza) map.get(zzbiT);
        String zzg3 = com_google_android_gms_internal_zzag_zza == null ? "base16" : zzdf.zzg(com_google_android_gms_internal_zzag_zza);
        com_google_android_gms_internal_zzag_zza = (zza) map.get(zzbiR);
        int i = (com_google_android_gms_internal_zzag_zza == null || !zzdf.zzk(com_google_android_gms_internal_zzag_zza).booleanValue()) ? 2 : 3;
        try {
            byte[] bytes;
            Object zzj;
            if ("text".equals(zzg2)) {
                bytes = zzg.getBytes();
            } else if ("base16".equals(zzg2)) {
                bytes = zzk.zzfO(zzg);
            } else if ("base64".equals(zzg2)) {
                bytes = Base64.decode(zzg, i);
            } else if ("base64url".equals(zzg2)) {
                bytes = Base64.decode(zzg, i | 8);
            } else {
                zzbg.m1675e("Encode: unknown input format: " + zzg2);
                return zzdf.zzHF();
            }
            if ("base16".equals(zzg3)) {
                zzj = zzk.zzj(bytes);
            } else if ("base64".equals(zzg3)) {
                zzj = Base64.encodeToString(bytes, i);
            } else if ("base64url".equals(zzg3)) {
                zzj = Base64.encodeToString(bytes, i | 8);
            } else {
                zzbg.m1675e("Encode: unknown output format: " + zzg3);
                return zzdf.zzHF();
            }
            return zzdf.zzR(zzj);
        } catch (IllegalArgumentException e) {
            zzbg.m1675e("Encode: invalid input:");
            return zzdf.zzHF();
        }
    }
}
