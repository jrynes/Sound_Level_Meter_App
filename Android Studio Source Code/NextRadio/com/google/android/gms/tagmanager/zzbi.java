package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;

class zzbi extends zzak {
    private static final String ID;
    private static final String zzbiQ;

    static {
        ID = zzad.LOWERCASE_STRING.toString();
        zzbiQ = zzae.ARG0.toString();
    }

    public zzbi() {
        super(ID, zzbiQ);
    }

    public boolean zzFW() {
        return true;
    }

    public zza zzP(Map<String, zza> map) {
        return zzdf.zzR(zzdf.zzg((zza) map.get(zzbiQ)).toLowerCase());
    }
}
