package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Zone;

public class zzi implements Creator<PlacePhotoResult> {
    static void zza(PlacePhotoResult placePhotoResult, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zza(parcel, 1, placePhotoResult.getStatus(), i, false);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, placePhotoResult.mVersionCode);
        zzb.zza(parcel, 2, placePhotoResult.zzaPG, i, false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfg(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhR(i);
    }

    public PlacePhotoResult zzfg(Parcel parcel) {
        BitmapTeleporter bitmapTeleporter = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        Status status = null;
        while (parcel.dataPosition() < zzau) {
            int i2;
            BitmapTeleporter bitmapTeleporter2;
            Status status2;
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i2 = i;
                    Status status3 = (Status) zza.zza(parcel, zzat, Status.CREATOR);
                    bitmapTeleporter2 = bitmapTeleporter;
                    status2 = status3;
                    break;
                case Zone.SECONDARY /*2*/:
                    bitmapTeleporter2 = (BitmapTeleporter) zza.zza(parcel, zzat, BitmapTeleporter.CREATOR);
                    status2 = status;
                    i2 = i;
                    break;
                case ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH /*1000*/:
                    BitmapTeleporter bitmapTeleporter3 = bitmapTeleporter;
                    status2 = status;
                    i2 = zza.zzg(parcel, zzat);
                    bitmapTeleporter2 = bitmapTeleporter3;
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    bitmapTeleporter2 = bitmapTeleporter;
                    status2 = status;
                    i2 = i;
                    break;
            }
            i = i2;
            status = status2;
            bitmapTeleporter = bitmapTeleporter2;
        }
        if (parcel.dataPosition() == zzau) {
            return new PlacePhotoResult(i, status, bitmapTeleporter);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public PlacePhotoResult[] zzhR(int i) {
        return new PlacePhotoResult[i];
    }
}
