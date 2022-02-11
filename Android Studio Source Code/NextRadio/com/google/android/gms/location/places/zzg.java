package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class zzg implements Creator<PlaceFilter> {
    static void zza(PlaceFilter placeFilter, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zza(parcel, 1, placeFilter.zzaPk, false);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, placeFilter.mVersionCode);
        zzb.zza(parcel, 3, placeFilter.zzaPA);
        zzb.zzc(parcel, 4, placeFilter.zzaPl, false);
        zzb.zzb(parcel, 6, placeFilter.zzaPj, false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfe(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhO(i);
    }

    public PlaceFilter zzfe(Parcel parcel) {
        boolean z = false;
        List list = null;
        int zzau = zza.zzau(parcel);
        List list2 = null;
        List list3 = null;
        int i = 0;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    list3 = zza.zzC(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    z = zza.zzc(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    list = zza.zzc(parcel, zzat, UserDataType.CREATOR);
                    break;
                case Protocol.TCP /*6*/:
                    list2 = zza.zzD(parcel, zzat);
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
            return new PlaceFilter(i, list3, z, list2, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public PlaceFilter[] zzhO(int i) {
        return new PlaceFilter[i];
    }
}
