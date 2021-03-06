package com.google.android.gms.location.internal;

import android.app.PendingIntent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzn implements Creator<LocationRequestUpdateData> {
    static void zza(LocationRequestUpdateData locationRequestUpdateData, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, locationRequestUpdateData.zzaOU);
        zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, locationRequestUpdateData.getVersionCode());
        zzb.zza(parcel, 2, locationRequestUpdateData.zzaOV, i, false);
        zzb.zza(parcel, 3, locationRequestUpdateData.zzyQ(), false);
        zzb.zza(parcel, 4, locationRequestUpdateData.mPendingIntent, i, false);
        zzb.zza(parcel, 5, locationRequestUpdateData.zzyR(), false);
        zzb.zza(parcel, 6, locationRequestUpdateData.zzyS(), false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzeY(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhE(i);
    }

    public LocationRequestUpdateData zzeY(Parcel parcel) {
        IBinder iBinder = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        int i2 = 1;
        IBinder iBinder2 = null;
        PendingIntent pendingIntent = null;
        IBinder iBinder3 = null;
        LocationRequestInternal locationRequestInternal = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i2 = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    locationRequestInternal = (LocationRequestInternal) zza.zza(parcel, zzat, LocationRequestInternal.CREATOR);
                    break;
                case Protocol.GGP /*3*/:
                    iBinder3 = zza.zzq(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    pendingIntent = (PendingIntent) zza.zza(parcel, zzat, PendingIntent.CREATOR);
                    break;
                case Service.RJE /*5*/:
                    iBinder2 = zza.zzq(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    iBinder = zza.zzq(parcel, zzat);
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
            return new LocationRequestUpdateData(i, i2, locationRequestInternal, iBinder3, pendingIntent, iBinder2, iBinder);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public LocationRequestUpdateData[] zzhE(int i) {
        return new LocationRequestUpdateData[i];
    }
}
