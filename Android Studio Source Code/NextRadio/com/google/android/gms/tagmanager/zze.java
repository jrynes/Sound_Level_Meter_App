package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;

class zze extends zzak {
    private static final String ID;
    private static final String zzbhD;
    private static final String zzbhE;
    private final Context context;

    static {
        ID = zzad.ADWORDS_CLICK_REFERRER.toString();
        zzbhD = zzae.COMPONENT.toString();
        zzbhE = zzae.CONVERSION_ID.toString();
    }

    public zze(Context context) {
        super(ID, zzbhE);
        this.context = context;
    }

    public boolean zzFW() {
        return true;
    }

    public zza zzP(Map<String, zza> map) {
        zza com_google_android_gms_internal_zzag_zza = (zza) map.get(zzbhE);
        if (com_google_android_gms_internal_zzag_zza == null) {
            return zzdf.zzHF();
        }
        String zzg = zzdf.zzg(com_google_android_gms_internal_zzag_zza);
        com_google_android_gms_internal_zzag_zza = (zza) map.get(zzbhD);
        String zzf = zzax.zzf(this.context, zzg, com_google_android_gms_internal_zzag_zza != null ? zzdf.zzg(com_google_android_gms_internal_zzag_zza) : null);
        return zzf != null ? zzdf.zzR(zzf) : zzdf.zzHF();
    }
}
