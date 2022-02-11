package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class zzg implements Creator<PointOfInterest> {
    static void zza(PointOfInterest pointOfInterest, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, pointOfInterest.getVersionCode());
        zzb.zza(parcel, 2, pointOfInterest.zzaTG, i, false);
        zzb.zza(parcel, 3, pointOfInterest.zzaTH, false);
        zzb.zza(parcel, 4, pointOfInterest.name, false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfB(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzip(i);
    }

    public PointOfInterest zzfB(Parcel parcel) {
        String str = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        String str2 = null;
        LatLng latLng = null;
        while (parcel.dataPosition() < zzau) {
            LatLng latLng2;
            int zzg;
            String str3;
            int zzat = zza.zzat(parcel);
            String str4;
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    str4 = str;
                    str = str2;
                    latLng2 = latLng;
                    zzg = zza.zzg(parcel, zzat);
                    str3 = str4;
                    break;
                case Zone.SECONDARY /*2*/:
                    zzg = i;
                    str4 = str2;
                    latLng2 = (LatLng) zza.zza(parcel, zzat, LatLng.CREATOR);
                    str3 = str;
                    str = str4;
                    break;
                case Protocol.GGP /*3*/:
                    latLng2 = latLng;
                    zzg = i;
                    str4 = str;
                    str = zza.zzp(parcel, zzat);
                    str3 = str4;
                    break;
                case Type.MF /*4*/:
                    str3 = zza.zzp(parcel, zzat);
                    str = str2;
                    latLng2 = latLng;
                    zzg = i;
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    str3 = str;
                    str = str2;
                    latLng2 = latLng;
                    zzg = i;
                    break;
            }
            i = zzg;
            latLng = latLng2;
            str2 = str;
            str = str3;
        }
        if (parcel.dataPosition() == zzau) {
            return new PointOfInterest(i, latLng, str2, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public PointOfInterest[] zzip(int i) {
        return new PointOfInterest[i];
    }
}
