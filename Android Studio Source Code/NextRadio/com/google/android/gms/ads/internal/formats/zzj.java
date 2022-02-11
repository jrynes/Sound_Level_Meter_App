package com.google.android.gms.ads.internal.formats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class zzj implements Creator<NativeAdOptionsParcel> {
    static void zza(NativeAdOptionsParcel nativeAdOptionsParcel, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, nativeAdOptionsParcel.versionCode);
        zzb.zza(parcel, 2, nativeAdOptionsParcel.zzyA);
        zzb.zzc(parcel, 3, nativeAdOptionsParcel.zzyB);
        zzb.zza(parcel, 4, nativeAdOptionsParcel.zzyC);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zze(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzp(i);
    }

    public NativeAdOptionsParcel zze(Parcel parcel) {
        boolean z = false;
        int zzau = zza.zzau(parcel);
        int i = 0;
        boolean z2 = false;
        int i2 = 0;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i2 = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    z2 = zza.zzc(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    z = zza.zzc(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new NativeAdOptionsParcel(i2, z2, i, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public NativeAdOptionsParcel[] zzp(int i) {
        return new NativeAdOptionsParcel[i];
    }
}
