package com.google.android.gms.location.places.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Zone;

public class zzm implements Creator<PlaceLikelihoodEntity> {
    static void zza(PlaceLikelihoodEntity placeLikelihoodEntity, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zza(parcel, 1, placeLikelihoodEntity.zzaQM, i, false);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, placeLikelihoodEntity.mVersionCode);
        zzb.zza(parcel, 2, placeLikelihoodEntity.zzaQN);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfm(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhZ(i);
    }

    public PlaceLikelihoodEntity zzfm(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        int i = 0;
        PlaceImpl placeImpl = null;
        float f = 0.0f;
        while (parcel.dataPosition() < zzau) {
            int i2;
            float f2;
            PlaceImpl placeImpl2;
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i2 = i;
                    PlaceImpl placeImpl3 = (PlaceImpl) zza.zza(parcel, zzat, PlaceImpl.CREATOR);
                    f2 = f;
                    placeImpl2 = placeImpl3;
                    break;
                case Zone.SECONDARY /*2*/:
                    f2 = zza.zzl(parcel, zzat);
                    placeImpl2 = placeImpl;
                    i2 = i;
                    break;
                case ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH /*1000*/:
                    float f3 = f;
                    placeImpl2 = placeImpl;
                    i2 = zza.zzg(parcel, zzat);
                    f2 = f3;
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    f2 = f;
                    placeImpl2 = placeImpl;
                    i2 = i;
                    break;
            }
            i = i2;
            placeImpl = placeImpl2;
            f = f2;
        }
        if (parcel.dataPosition() == zzau) {
            return new PlaceLikelihoodEntity(i, placeImpl, f);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public PlaceLikelihoodEntity[] zzhZ(int i) {
        return new PlaceLikelihoodEntity[i];
    }
}
