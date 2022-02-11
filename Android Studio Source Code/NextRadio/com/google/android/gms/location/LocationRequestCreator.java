package com.google.android.gms.location;

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

public class LocationRequestCreator implements Creator<LocationRequest> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void zza(LocationRequest locationRequest, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, locationRequest.mPriority);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, locationRequest.getVersionCode());
        zzb.zza(parcel, 2, locationRequest.zzaNY);
        zzb.zza(parcel, 3, locationRequest.zzaNZ);
        zzb.zza(parcel, 4, locationRequest.zzaBr);
        zzb.zza(parcel, 5, locationRequest.zzaND);
        zzb.zzc(parcel, 6, locationRequest.zzaOa);
        zzb.zza(parcel, 7, locationRequest.zzaOb);
        zzb.zza(parcel, 8, locationRequest.zzaOc);
        zzb.zzI(parcel, zzav);
    }

    public LocationRequest createFromParcel(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        int i = 0;
        int i2 = Service.ISO_TSAP;
        long j = 3600000;
        long j2 = 600000;
        boolean z = false;
        long j3 = OpenWireFormat.DEFAULT_MAX_FRAME_SIZE;
        int i3 = Integer.MAX_VALUE;
        float f = 0.0f;
        long j4 = 0;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i2 = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    j = zza.zzi(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    j2 = zza.zzi(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    z = zza.zzc(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    j3 = zza.zzi(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    i3 = zza.zzg(parcel, zzat);
                    break;
                case Service.ECHO /*7*/:
                    f = zza.zzl(parcel, zzat);
                    break;
                case Protocol.EGP /*8*/:
                    j4 = zza.zzi(parcel, zzat);
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
            return new LocationRequest(i, i2, j, j2, z, j3, i3, f, j4);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public LocationRequest[] newArray(int size) {
        return new LocationRequest[size];
    }
}
