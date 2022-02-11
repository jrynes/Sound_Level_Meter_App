package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzh implements Creator<WakeLockEvent> {
    static void zza(WakeLockEvent wakeLockEvent, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, wakeLockEvent.mVersionCode);
        zzb.zza(parcel, 2, wakeLockEvent.getTimeMillis());
        zzb.zza(parcel, 4, wakeLockEvent.zzrR(), false);
        zzb.zzc(parcel, 5, wakeLockEvent.zzrT());
        zzb.zzb(parcel, 6, wakeLockEvent.zzrU(), false);
        zzb.zza(parcel, 8, wakeLockEvent.zzrN());
        zzb.zza(parcel, 10, wakeLockEvent.zzrS(), false);
        zzb.zzc(parcel, 11, wakeLockEvent.getEventType());
        zzb.zza(parcel, 12, wakeLockEvent.zzrK(), false);
        zzb.zza(parcel, 13, wakeLockEvent.zzrW(), false);
        zzb.zzc(parcel, 14, wakeLockEvent.zzrV());
        zzb.zza(parcel, 15, wakeLockEvent.zzrX());
        zzb.zza(parcel, 16, wakeLockEvent.zzrY());
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzaG(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzcm(i);
    }

    public WakeLockEvent zzaG(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        int i = 0;
        long j = 0;
        int i2 = 0;
        String str = null;
        int i3 = 0;
        List list = null;
        String str2 = null;
        long j2 = 0;
        int i4 = 0;
        String str3 = null;
        String str4 = null;
        float f = 0.0f;
        long j3 = 0;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case Zone.PRIMARY /*1*/:
                    i = zza.zzg(parcel, zzat);
                    break;
                case Zone.SECONDARY /*2*/:
                    j = zza.zzi(parcel, zzat);
                    break;
                case Type.MF /*4*/:
                    str = zza.zzp(parcel, zzat);
                    break;
                case Service.RJE /*5*/:
                    i3 = zza.zzg(parcel, zzat);
                    break;
                case Protocol.TCP /*6*/:
                    list = zza.zzD(parcel, zzat);
                    break;
                case Protocol.EGP /*8*/:
                    j2 = zza.zzi(parcel, zzat);
                    break;
                case Protocol.BBN_RCC_MON /*10*/:
                    str3 = zza.zzp(parcel, zzat);
                    break;
                case Service.USERS /*11*/:
                    i2 = zza.zzg(parcel, zzat);
                    break;
                case Protocol.PUP /*12*/:
                    str2 = zza.zzp(parcel, zzat);
                    break;
                case Service.DAYTIME /*13*/:
                    str4 = zza.zzp(parcel, zzat);
                    break;
                case Protocol.EMCON /*14*/:
                    i4 = zza.zzg(parcel, zzat);
                    break;
                case Protocol.XNET /*15*/:
                    f = zza.zzl(parcel, zzat);
                    break;
                case Protocol.CHAOS /*16*/:
                    j3 = zza.zzi(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new WakeLockEvent(i, j, i2, str, i3, list, str2, j2, i4, str3, str4, f, j3);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public WakeLockEvent[] zzcm(int i) {
        return new WakeLockEvent[i];
    }
}
