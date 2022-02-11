package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class LocationAvailabilityCreator implements Creator<LocationAvailability> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void zza(LocationAvailability locationAvailability, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, locationAvailability.zzaNU);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, locationAvailability.getVersionCode());
        zzb.zzc(parcel, 2, locationAvailability.zzaNV);
        zzb.zza(parcel, 3, locationAvailability.zzaNW);
        zzb.zzc(parcel, 4, locationAvailability.zzaNX);
        zzb.zzI(parcel, zzav);
    }

    public LocationAvailability createFromParcel(Parcel parcel) {
        int i = 1;
        int zzau = zza.zzau(parcel);
        int i2 = 0;
        int i3 = ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
        long j = 0;
        int i4 = 1;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i4 = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    j = zza.zzi(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    i3 = zza.zzg(parcel, zzat);
                    break;
                case ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH /*1000*/:
                    i2 = zza.zzg(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new LocationAvailability(i2, i3, i4, i, j);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public LocationAvailability[] newArray(int size) {
        return new LocationAvailability[size];
    }
}
