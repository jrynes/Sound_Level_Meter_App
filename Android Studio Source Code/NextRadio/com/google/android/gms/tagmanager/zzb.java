package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;

class zzb extends zzak {
    private static final String ID;
    private final zza zzbhC;

    static {
        ID = zzad.ADVERTISER_ID.toString();
    }

    public zzb(Context context) {
        this(zza.zzaW(context));
    }

    zzb(zza com_google_android_gms_tagmanager_zza) {
        super(ID, new String[0]);
        this.zzbhC = com_google_android_gms_tagmanager_zza;
    }

    public boolean zzFW() {
        return false;
    }

    public zza zzP(Map<String, zza> map) {
        String zzFQ = this.zzbhC.zzFQ();
        return zzFQ == null ? zzdf.zzHF() : zzdf.zzR(zzFQ);
    }
}
