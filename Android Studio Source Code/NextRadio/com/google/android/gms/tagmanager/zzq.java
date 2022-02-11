package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;

class zzq extends zzak {
    private static final String ID;
    private final String zzadc;

    static {
        ID = zzad.CONTAINER_VERSION.toString();
    }

    public zzq(String str) {
        super(ID, new String[0]);
        this.zzadc = str;
    }

    public boolean zzFW() {
        return true;
    }

    public zza zzP(Map<String, zza> map) {
        return this.zzadc == null ? zzdf.zzHF() : zzdf.zzR(this.zzadc);
    }
}
