package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Zone;

public class zzh implements Creator<PlacePhotoMetadataResult> {
    static void zza(PlacePhotoMetadataResult placePhotoMetadataResult, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zza(parcel, 1, placePhotoMetadataResult.getStatus(), i, false);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, placePhotoMetadataResult.mVersionCode);
        zzb.zza(parcel, 2, placePhotoMetadataResult.zzaPE, i, false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzff(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhQ(i);
    }

    public PlacePhotoMetadataResult zzff(Parcel parcel) {
        DataHolder dataHolder = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        Status status = null;
        while (parcel.dataPosition() < zzau) {
            int i2;
            DataHolder dataHolder2;
            Status status2;
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i2 = i;
                    Status status3 = (Status) zza.zza(parcel, zzat, Status.CREATOR);
                    dataHolder2 = dataHolder;
                    status2 = status3;
                    break;
                case Zone.SECONDARY /*2*/:
                    dataHolder2 = (DataHolder) zza.zza(parcel, zzat, DataHolder.CREATOR);
                    status2 = status;
                    i2 = i;
                    break;
                case ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH /*1000*/:
                    DataHolder dataHolder3 = dataHolder;
                    status2 = status;
                    i2 = zza.zzg(parcel, zzat);
                    dataHolder2 = dataHolder3;
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    dataHolder2 = dataHolder;
                    status2 = status;
                    i2 = i;
                    break;
            }
            i = i2;
            status = status2;
            dataHolder = dataHolder2;
        }
        if (parcel.dataPosition() == zzau) {
            return new PlacePhotoMetadataResult(i, status, dataHolder);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public PlacePhotoMetadataResult[] zzhQ(int i) {
        return new PlacePhotoMetadataResult[i];
    }
}
