package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.List;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzb implements Creator<GoogleSignInAccount> {
    static void zza(GoogleSignInAccount googleSignInAccount, Parcel parcel, int i) {
        int zzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, googleSignInAccount.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, googleSignInAccount.getId(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, googleSignInAccount.getIdToken(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, googleSignInAccount.getEmail(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, googleSignInAccount.getDisplayName(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, googleSignInAccount.getPhotoUrl(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, googleSignInAccount.getServerAuthCode(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, googleSignInAccount.zzmK());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, googleSignInAccount.zzmL(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 10, googleSignInAccount.zzVs, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzR(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaM(i);
    }

    public GoogleSignInAccount zzR(Parcel parcel) {
        List list = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        long j = 0;
        String str = null;
        String str2 = null;
        Uri uri = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    str6 = zza.zzp(parcel, zzat);
                    break;
                case Protocol.GGP /*3*/:
                    str5 = zza.zzp(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    str4 = zza.zzp(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    str3 = zza.zzp(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    uri = (Uri) zza.zza(parcel, zzat, Uri.CREATOR);
                    break;
                case Service.ECHO /*7*/:
                    str2 = zza.zzp(parcel, zzat);
                    break;
                case Protocol.EGP /*8*/:
                    j = zza.zzi(parcel, zzat);
                    break;
                case Service.DISCARD /*9*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                case Protocol.BBN_RCC_MON /*10*/:
                    list = zza.zzc(parcel, zzat, Scope.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new GoogleSignInAccount(i, str6, str5, str4, str3, uri, str2, j, str, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public GoogleSignInAccount[] zzaM(int i) {
        return new GoogleSignInAccount[i];
    }
}
