package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.openwire.OpenWireFormat;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzk implements Creator<PlaceRequest> {
    static void zza(PlaceRequest placeRequest, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, placeRequest.mVersionCode);
        zzb.zza(parcel, 2, placeRequest.zzyZ(), i, false);
        zzb.zza(parcel, 3, placeRequest.getInterval());
        zzb.zzc(parcel, 4, placeRequest.getPriority());
        zzb.zza(parcel, 5, placeRequest.getExpirationTime());
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfi(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhT(i);
    }

    public PlaceRequest zzfi(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        int i = 0;
        PlaceFilter placeFilter = null;
        long j = PlaceRequest.zzaPJ;
        int i2 = Service.ISO_TSAP;
        long j2 = OpenWireFormat.DEFAULT_MAX_FRAME_SIZE;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.SECONDARY /*2*/:
                    placeFilter = (PlaceFilter) zza.zza(parcel, zzat, PlaceFilter.CREATOR);
                    break;
                case Protocol.GGP /*3*/:
                    j = zza.zzi(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    i2 = zza.zzg(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    j2 = zza.zzi(parcel, zzat);
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
            return new PlaceRequest(i, placeFilter, j, i2, j2);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public PlaceRequest[] zzhT(int i) {
        return new PlaceRequest[i];
    }
}
