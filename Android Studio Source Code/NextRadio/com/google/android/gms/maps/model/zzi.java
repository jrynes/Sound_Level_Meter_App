package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzi implements Creator<PolylineOptions> {
    static void zza(PolylineOptions polylineOptions, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, polylineOptions.getVersionCode());
        zzb.zzc(parcel, 2, polylineOptions.getPoints(), false);
        zzb.zza(parcel, 3, polylineOptions.getWidth());
        zzb.zzc(parcel, 4, polylineOptions.getColor());
        zzb.zza(parcel, 5, polylineOptions.getZIndex());
        zzb.zza(parcel, 6, polylineOptions.isVisible());
        zzb.zza(parcel, 7, polylineOptions.isGeodesic());
        zzb.zza(parcel, 8, polylineOptions.isClickable());
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfD(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzir(i);
    }

    public PolylineOptions zzfD(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int zzau = zza.zzau(parcel);
        List list = null;
        boolean z2 = false;
        boolean z3 = false;
        int i = 0;
        float f2 = 0.0f;
        int i2 = 0;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i2 = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    list = zza.zzc(parcel, zzat, LatLng.CREATOR);
                    break;
                case Protocol.GGP /*3*/:
                    f2 = zza.zzl(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    f = zza.zzl(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    z3 = zza.zzc(parcel, zzat);
                    break;
                case Service.ECHO /*7*/:
                    z2 = zza.zzc(parcel, zzat);
                    break;
                case Protocol.EGP /*8*/:
                    z = zza.zzc(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new PolylineOptions(i2, list, f2, i, f, z3, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public PolylineOptions[] zzir(int i) {
        return new PolylineOptions[i];
    }
}
