package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzc implements Creator<ScopeDetail> {
    static void zza(ScopeDetail scopeDetail, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, scopeDetail.version);
        zzb.zza(parcel, 2, scopeDetail.description, false);
        zzb.zza(parcel, 3, scopeDetail.zzYw, false);
        zzb.zza(parcel, 4, scopeDetail.zzYx, false);
        zzb.zza(parcel, 5, scopeDetail.zzYy, false);
        zzb.zza(parcel, 6, scopeDetail.zzYz, false);
        zzb.zzb(parcel, 7, scopeDetail.zzYA, false);
        zzb.zza(parcel, 8, scopeDetail.zzYB, i, false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzY(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaV(i);
    }

    public ScopeDetail zzY(Parcel parcel) {
        FACLData fACLData = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        List arrayList = new ArrayList();
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    str5 = zza.zzp(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    str4 = zza.zzp(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    str3 = zza.zzp(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    str2 = zza.zzp(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                case Service.ECHO /*7*/:
                    arrayList = zza.zzD(parcel, zzat);
                    break;
                case Protocol.EGP /*8*/:
                    fACLData = (FACLData) zza.zza(parcel, zzat, FACLData.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new ScopeDetail(i, str5, str4, str3, str2, str, arrayList, fACLData);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public ScopeDetail[] zzaV(int i) {
        return new ScopeDetail[i];
    }
}
