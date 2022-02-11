package com.google.android.gms.location.places.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzo implements Creator<PlaceLocalization> {
    static void zza(PlaceLocalization placeLocalization, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zza(parcel, 1, placeLocalization.name, false);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, placeLocalization.versionCode);
        zzb.zza(parcel, 2, placeLocalization.address, false);
        zzb.zza(parcel, 3, placeLocalization.zzaQO, false);
        zzb.zza(parcel, 4, placeLocalization.zzaQP, false);
        zzb.zzb(parcel, 5, placeLocalization.zzaQQ, false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfn(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzia(i);
    }

    public PlaceLocalization zzfn(Parcel parcel) {
        List list = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    str4 = zza.zzp(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    str3 = zza.zzp(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    str2 = zza.zzp(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    list = zza.zzD(parcel, zzat);
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
            return new PlaceLocalization(i, str4, str3, str2, str, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public PlaceLocalization[] zzia(int i) {
        return new PlaceLocalization[i];
    }
}
