package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;

class zzai extends zzak {
    private static final String ID;
    private final zzcp zzbhO;

    static {
        ID = zzad.EVENT.toString();
    }

    public zzai(zzcp com_google_android_gms_tagmanager_zzcp) {
        super(ID, new String[0]);
        this.zzbhO = com_google_android_gms_tagmanager_zzcp;
    }

    public boolean zzFW() {
        return false;
    }

    public zza zzP(Map<String, zza> map) {
        String zzHe = this.zzbhO.zzHe();
        return zzHe == null ? zzdf.zzHF() : zzdf.zzR(zzHe);
    }
}
