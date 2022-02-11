package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class zzt extends zzak {
    private static final String ID;
    private static final String zzbhF;
    private static final String zzbip;
    private final zza zzbiq;

    public interface zza {
        Object zzc(String str, Map<String, Object> map);
    }

    static {
        ID = zzad.FUNCTION_CALL.toString();
        zzbip = zzae.FUNCTION_CALL_NAME.toString();
        zzbhF = zzae.ADDITIONAL_PARAMS.toString();
    }

    public zzt(zza com_google_android_gms_tagmanager_zzt_zza) {
        super(ID, zzbip);
        this.zzbiq = com_google_android_gms_tagmanager_zzt_zza;
    }

    public boolean zzFW() {
        return false;
    }

    public com.google.android.gms.internal.zzag.zza zzP(Map<String, com.google.android.gms.internal.zzag.zza> map) {
        String zzg = zzdf.zzg((com.google.android.gms.internal.zzag.zza) map.get(zzbip));
        Map hashMap = new HashMap();
        com.google.android.gms.internal.zzag.zza com_google_android_gms_internal_zzag_zza = (com.google.android.gms.internal.zzag.zza) map.get(zzbhF);
        if (com_google_android_gms_internal_zzag_zza != null) {
            Object zzl = zzdf.zzl(com_google_android_gms_internal_zzag_zza);
            if (zzl instanceof Map) {
                for (Entry entry : ((Map) zzl).entrySet()) {
                    hashMap.put(entry.getKey().toString(), entry.getValue());
                }
            } else {
                zzbg.zzaK("FunctionCallMacro: expected ADDITIONAL_PARAMS to be a map.");
                return zzdf.zzHF();
            }
        }
        try {
            return zzdf.zzR(this.zzbiq.zzc(zzg, hashMap));
        } catch (Exception e) {
            zzbg.zzaK("Custom macro/tag " + zzg + " threw exception " + e.getMessage());
            return zzdf.zzHF();
        }
    }
}
