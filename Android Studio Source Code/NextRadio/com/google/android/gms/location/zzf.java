package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class zzf implements Creator<LocationSettingsRequest> {
    static void zza(LocationSettingsRequest locationSettingsRequest, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, locationSettingsRequest.zzuZ(), false);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, locationSettingsRequest.getVersionCode());
        zzb.zza(parcel, 2, locationSettingsRequest.zzyK());
        zzb.zza(parcel, 3, locationSettingsRequest.zzyL());
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzeS(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhv(i);
    }

    public LocationSettingsRequest zzeS(Parcel parcel) {
        boolean z = false;
        int zzau = zza.zzau(parcel);
        List list = null;
        boolean z2 = false;
        int i = 0;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    list = zza.zzc(parcel, zzat, LocationRequest.CREATOR);
                    break;
                case Zone.SECONDARY /*2*/:
                    z2 = zza.zzc(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    z = zza.zzc(parcel, zzat);
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
            return new LocationSettingsRequest(i, list, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public LocationSettingsRequest[] zzhv(int i) {
        return new LocationSettingsRequest[i];
    }
}
