package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;

class zzbz extends zzak {
    private static final String ID;
    private static final zza zzbjN;

    static {
        ID = zzad.PLATFORM.toString();
        zzbjN = zzdf.zzR("Android");
    }

    public zzbz() {
        super(ID, new String[0]);
    }

    public boolean zzFW() {
        return true;
    }

    public zza zzP(Map<String, zza> map) {
        return zzbjN;
    }
}
