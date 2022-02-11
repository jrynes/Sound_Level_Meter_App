package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zze implements Creator<TokenData> {
    static void zza(TokenData tokenData, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, tokenData.mVersionCode);
        zzb.zza(parcel, 2, tokenData.getToken(), false);
        zzb.zza(parcel, 3, tokenData.zzmn(), false);
        zzb.zza(parcel, 4, tokenData.zzmo());
        zzb.zza(parcel, 5, tokenData.zzmp());
        zzb.zzb(parcel, 6, tokenData.zzmq(), false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzC(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzax(i);
    }

    public TokenData zzC(Parcel parcel) {
        List list = null;
        boolean z = false;
        int zzau = zza.zzau(parcel);
        boolean z2 = false;
        Long l = null;
        String str = null;
        int i = 0;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    l = zza.zzj(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    z2 = zza.zzc(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    z = zza.zzc(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    list = zza.zzD(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new TokenData(i, str, l, z2, z, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public TokenData[] zzax(int i) {
        return new TokenData[i];
    }
}
