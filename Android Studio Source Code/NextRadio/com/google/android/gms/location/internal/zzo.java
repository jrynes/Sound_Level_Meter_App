package com.google.android.gms.location.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzo implements Creator<ParcelableGeofence> {
    static void zza(ParcelableGeofence parcelableGeofence, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zza(parcel, 1, parcelableGeofence.getRequestId(), false);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, parcelableGeofence.getVersionCode());
        zzb.zza(parcel, 2, parcelableGeofence.getExpirationTime());
        zzb.zza(parcel, 3, parcelableGeofence.zzyT());
        zzb.zza(parcel, 4, parcelableGeofence.getLatitude());
        zzb.zza(parcel, 5, parcelableGeofence.getLongitude());
        zzb.zza(parcel, 6, parcelableGeofence.zzyU());
        zzb.zzc(parcel, 7, parcelableGeofence.zzyV());
        zzb.zzc(parcel, 8, parcelableGeofence.getNotificationResponsiveness());
        zzb.zzc(parcel, 9, parcelableGeofence.zzyW());
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzeZ(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhH(i);
    }

    public ParcelableGeofence zzeZ(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        int i = 0;
        String str = null;
        int i2 = 0;
        short s = (short) 0;
        double d = 0.0d;
        double d2 = 0.0d;
        float f = 0.0f;
        long j = 0;
        int i3 = 0;
        int i4 = -1;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    j = zza.zzi(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    s = zza.zzf(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    d = zza.zzn(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    d2 = zza.zzn(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    f = zza.zzl(parcel, zzat);
                    break;
                case Service.ECHO /*7*/:
                    i2 = zza.zzg(parcel, zzat);
                    break;
                case Protocol.EGP /*8*/:
                    i3 = zza.zzg(parcel, zzat);
                    break;
                case Service.DISCARD /*9*/:
                    i4 = zza.zzg(parcel, zzat);
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
            return new ParcelableGeofence(i, str, i2, s, d, d2, f, j, i3, i4);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public ParcelableGeofence[] zzhH(int i) {
        return new ParcelableGeofence[i];
    }
}
