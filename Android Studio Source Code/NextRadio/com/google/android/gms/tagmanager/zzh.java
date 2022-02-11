package com.google.android.gms.tagmanager;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;

class zzh extends zzak {
    private static final String ID;
    private final Context mContext;

    static {
        ID = zzad.APP_VERSION.toString();
    }

    public zzh(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    public boolean zzFW() {
        return true;
    }

    public zza zzP(Map<String, zza> map) {
        try {
            return zzdf.zzR(Integer.valueOf(this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionCode));
        } catch (NameNotFoundException e) {
            zzbg.m1675e("Package name " + this.mContext.getPackageName() + " not found. " + e.getMessage());
            return zzdf.zzHF();
        }
    }
}
