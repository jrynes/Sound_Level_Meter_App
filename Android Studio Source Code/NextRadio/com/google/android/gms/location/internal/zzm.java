package com.google.android.gms.location.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.location.LocationRequest;
import java.util.List;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzm implements Creator<LocationRequestInternal> {
    static void zza(LocationRequestInternal locationRequestInternal, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zza(parcel, 1, locationRequestInternal.zzaBp, i, false);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, locationRequestInternal.getVersionCode());
        zzb.zza(parcel, 2, locationRequestInternal.zzaOP);
        zzb.zza(parcel, 3, locationRequestInternal.zzaOQ);
        zzb.zza(parcel, 4, locationRequestInternal.zzaOR);
        zzb.zzc(parcel, 5, locationRequestInternal.zzaOS, false);
        zzb.zza(parcel, 6, locationRequestInternal.mTag, false);
        zzb.zza(parcel, 7, locationRequestInternal.zzaOT);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzeX(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhD(i);
    }

    public LocationRequestInternal zzeX(Parcel parcel) {
        String str = null;
        boolean z = true;
        boolean z2 = false;
        int zzau = zza.zzau(parcel);
        List list = LocationRequestInternal.zzaOO;
        boolean z3 = true;
        boolean z4 = false;
        LocationRequest locationRequest = null;
        int i = 0;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    locationRequest = (LocationRequest) zza.zza(parcel, zzat, LocationRequest.CREATOR);
                    break;
                case Zone.SECONDARY /*2*/:
                    z4 = zza.zzc(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    z3 = zza.zzc(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    z = zza.zzc(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    list = zza.zzc(parcel, zzat, ClientIdentity.CREATOR);
                    break;
                case Protocol.TCP /*6*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                case Service.ECHO /*7*/:
                    z2 = zza.zzc(parcel, zzat);
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
            return new LocationRequestInternal(i, locationRequest, z4, z3, z, list, str, z2);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public LocationRequestInternal[] zzhD(int i) {
        return new LocationRequestInternal[i];
    }
}
