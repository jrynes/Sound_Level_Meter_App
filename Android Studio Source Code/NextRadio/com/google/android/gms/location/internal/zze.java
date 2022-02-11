package com.google.android.gms.location.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Zone;

public class zze implements Creator<FusedLocationProviderResult> {
    static void zza(FusedLocationProviderResult fusedLocationProviderResult, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zza(parcel, 1, fusedLocationProviderResult.getStatus(), i, false);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, fusedLocationProviderResult.getVersionCode());
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzeW(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhB(i);
    }

    public FusedLocationProviderResult zzeW(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        int i = 0;
        Status status = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    status = (Status) zza.zza(parcel, zzat, Status.CREATOR);
                    break;
                case ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH /*1000*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new FusedLocationProviderResult(i, status);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public FusedLocationProviderResult[] zzhB(int i) {
        return new FusedLocationProviderResult[i];
    }
}
