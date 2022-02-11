package com.google.android.gms.ads.internal.purchase;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zza implements Creator<GInAppPurchaseManagerInfoParcel> {
    static void zza(GInAppPurchaseManagerInfoParcel gInAppPurchaseManagerInfoParcel, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, gInAppPurchaseManagerInfoParcel.versionCode);
        zzb.zza(parcel, 3, gInAppPurchaseManagerInfoParcel.zzfT(), false);
        zzb.zza(parcel, 4, gInAppPurchaseManagerInfoParcel.zzfU(), false);
        zzb.zza(parcel, 5, gInAppPurchaseManagerInfoParcel.zzfV(), false);
        zzb.zza(parcel, 6, gInAppPurchaseManagerInfoParcel.zzfS(), false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzh(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzA(i);
    }

    public GInAppPurchaseManagerInfoParcel[] zzA(int i) {
        return new GInAppPurchaseManagerInfoParcel[i];
    }

    public GInAppPurchaseManagerInfoParcel zzh(Parcel parcel) {
        IBinder iBinder = null;
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int i = 0;
        IBinder iBinder2 = null;
        IBinder iBinder3 = null;
        IBinder iBinder4 = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    iBinder4 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    iBinder3 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    iBinder2 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    iBinder = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, zzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new GInAppPurchaseManagerInfoParcel(i, iBinder4, iBinder3, iBinder2, iBinder);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, parcel);
    }
}
