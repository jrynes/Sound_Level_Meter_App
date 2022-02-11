package com.google.android.gms.location.places.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.location.places.internal.AutocompletePredictionEntity.SubstringEntity;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Zone;

public class zzu implements Creator<SubstringEntity> {
    static void zza(SubstringEntity substringEntity, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, substringEntity.mOffset);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, substringEntity.mVersionCode);
        zzb.zzc(parcel, 2, substringEntity.mLength);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfp(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzic(i);
    }

    public SubstringEntity zzfp(Parcel parcel) {
        int i = 0;
        int zzau = zza.zzau(parcel);
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i2 = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                case ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH /*1000*/:
                    i3 = zza.zzg(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new SubstringEntity(i3, i2, i);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public SubstringEntity[] zzic(int i) {
        return new SubstringEntity[i];
    }
}
