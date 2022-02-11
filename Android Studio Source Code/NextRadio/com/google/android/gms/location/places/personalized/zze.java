package com.google.android.gms.location.places.personalized;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class zze implements Creator<PlaceUserData> {
    static void zza(PlaceUserData placeUserData, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zza(parcel, 1, placeUserData.zzzD(), false);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, placeUserData.mVersionCode);
        zzb.zza(parcel, 2, placeUserData.getPlaceId(), false);
        zzb.zzc(parcel, 6, placeUserData.zzzE(), false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfs(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzif(i);
    }

    public PlaceUserData zzfs(Parcel parcel) {
        List list = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    str2 = zza.zzp(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    list = zza.zzc(parcel, zzat, PlaceAlias.CREATOR);
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
            return new PlaceUserData(i, str2, str, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public PlaceUserData[] zzif(int i) {
        return new PlaceUserData[i];
    }
}
