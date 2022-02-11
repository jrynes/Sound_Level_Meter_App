package com.google.android.gms.location.places;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzb implements Creator<AddPlaceRequest> {
    static void zza(AddPlaceRequest addPlaceRequest, Parcel parcel, int i) {
        int zzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, addPlaceRequest.getName(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, addPlaceRequest.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, addPlaceRequest.getLatLng(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, addPlaceRequest.getAddress(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, addPlaceRequest.getPlaceTypes(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, addPlaceRequest.getPhoneNumber(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, addPlaceRequest.getWebsiteUri(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfa(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzhI(i);
    }

    public AddPlaceRequest zzfa(Parcel parcel) {
        Uri uri = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        String str = null;
        List list = null;
        String str2 = null;
        LatLng latLng = null;
        String str3 = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    str3 = zza.zzp(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    latLng = (LatLng) zza.zza(parcel, zzat, LatLng.CREATOR);
                    break;
                case Protocol.GGP /*3*/:
                    str2 = zza.zzp(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    list = zza.zzC(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    uri = (Uri) zza.zza(parcel, zzat, Uri.CREATOR);
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
            return new AddPlaceRequest(i, str3, latLng, str2, list, str, uri);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public AddPlaceRequest[] zzhI(int i) {
        return new AddPlaceRequest[i];
    }
}
