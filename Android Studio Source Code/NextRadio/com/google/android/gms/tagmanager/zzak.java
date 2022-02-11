package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzag.zza;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract class zzak {
    private final Set<String> zzbiU;
    private final String zzbiV;

    public zzak(String str, String... strArr) {
        this.zzbiV = str;
        this.zzbiU = new HashSet(strArr.length);
        for (Object add : strArr) {
            this.zzbiU.add(add);
        }
    }

    public abstract boolean zzFW();

    public String zzGB() {
        return this.zzbiV;
    }

    public Set<String> zzGC() {
        return this.zzbiU;
    }

    public abstract zza zzP(Map<String, zza> map);

    boolean zze(Set<String> set) {
        return set.containsAll(this.zzbiU);
    }
}
