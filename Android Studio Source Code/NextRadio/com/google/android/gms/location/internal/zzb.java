package com.google.android.gms.location.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.location.internal.zzi.zza;

public class zzb extends zzj<zzi> {
    private final String zzaOs;
    protected final zzp<zzi> zzaOt;

    /* renamed from: com.google.android.gms.location.internal.zzb.1 */
    class C09331 implements zzp<zzi> {
        final /* synthetic */ zzb zzaOu;

        C09331(zzb com_google_android_gms_location_internal_zzb) {
            this.zzaOu = com_google_android_gms_location_internal_zzb;
        }

        public void zzqI() {
            this.zzaOu.zzqI();
        }

        public /* synthetic */ IInterface zzqJ() throws DeadObjectException {
            return zzyM();
        }

        public zzi zzyM() throws DeadObjectException {
            return (zzi) this.zzaOu.zzqJ();
        }
    }

    public zzb(Context context, Looper looper, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str, zzf com_google_android_gms_common_internal_zzf) {
        super(context, looper, 23, com_google_android_gms_common_internal_zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzaOt = new C09331(this);
        this.zzaOs = str;
    }

    protected /* synthetic */ IInterface zzW(IBinder iBinder) {
        return zzcg(iBinder);
    }

    protected zzi zzcg(IBinder iBinder) {
        return zza.zzcj(iBinder);
    }

    protected String zzgu() {
        return "com.google.android.location.internal.GoogleLocationManagerService.START";
    }

    protected String zzgv() {
        return "com.google.android.gms.location.internal.IGoogleLocationManagerService";
    }

    protected Bundle zzml() {
        Bundle bundle = new Bundle();
        bundle.putString("client_name", this.zzaOs);
        return bundle;
    }
}
