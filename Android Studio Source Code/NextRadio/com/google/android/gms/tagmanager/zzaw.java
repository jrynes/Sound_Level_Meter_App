package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;

class zzaw extends zzak {
    private static final String ID;
    private static final String zzbhD;
    private final Context context;

    static {
        ID = zzad.INSTALL_REFERRER.toString();
        zzbhD = zzae.COMPONENT.toString();
    }

    public zzaw(Context context) {
        super(ID, new String[0]);
        this.context = context;
    }

    public boolean zzFW() {
        return true;
    }

    public zza zzP(Map<String, zza> map) {
        String zzm = zzax.zzm(this.context, ((zza) map.get(zzbhD)) != null ? zzdf.zzg((zza) map.get(zzbhD)) : null);
        return zzm != null ? zzdf.zzR(zzm) : zzdf.zzHF();
    }
}
