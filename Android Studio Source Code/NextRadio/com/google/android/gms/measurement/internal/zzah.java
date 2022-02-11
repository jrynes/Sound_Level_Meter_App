package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzah implements Creator<UserAttributeParcel> {
    static void zza(UserAttributeParcel userAttributeParcel, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, userAttributeParcel.versionCode);
        zzb.zza(parcel, 2, userAttributeParcel.name, false);
        zzb.zza(parcel, 3, userAttributeParcel.zzaZm);
        zzb.zza(parcel, 4, userAttributeParcel.zzaZn, false);
        zzb.zza(parcel, 5, userAttributeParcel.zzaZo, false);
        zzb.zza(parcel, 6, userAttributeParcel.zzamJ, false);
        zzb.zza(parcel, 7, userAttributeParcel.zzaVW, false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzfO(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zziK(i);
    }

    public UserAttributeParcel zzfO(Parcel parcel) {
        String str = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        long j = 0;
        String str2 = null;
        Float f = null;
        Long l = null;
        String str3 = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    str3 = zza.zzp(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    j = zza.zzi(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    l = zza.zzj(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    f = zza.zzm(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    str2 = zza.zzp(parcel, zzat);
                    break;
                case Service.ECHO /*7*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new UserAttributeParcel(i, str3, j, l, f, str2, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public UserAttributeParcel[] zziK(int i) {
        return new UserAttributeParcel[i];
    }
}
