package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzb implements Creator<FACLData> {
    static void zza(FACLData fACLData, Parcel parcel, int i) {
        int zzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, fACLData.version);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, fACLData.zzYs, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, fACLData.zzYt, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, fACLData.zzYu);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, fACLData.zzYv, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzX(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaU(i);
    }

    public FACLData zzX(Parcel parcel) {
        boolean z = false;
        String str = null;
        int zzau = zza.zzau(parcel);
        String str2 = null;
        FACLConfig fACLConfig = null;
        int i = 0;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    fACLConfig = (FACLConfig) zza.zza(parcel, zzat, FACLConfig.CREATOR);
                    break;
                case Protocol.GGP /*3*/:
                    str2 = zza.zzp(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    z = zza.zzc(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new FACLData(i, fACLConfig, str2, z, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public FACLData[] zzaU(int i) {
        return new FACLData[i];
    }
}
