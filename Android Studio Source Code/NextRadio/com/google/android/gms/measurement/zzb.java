package com.google.android.gms.measurement;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.LogPrinter;
import com.admarvel.android.ads.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class zzb implements zzi {
    private static final Uri zzaUf;
    private final LogPrinter zzaUg;

    /* renamed from: com.google.android.gms.measurement.zzb.1 */
    class C10201 implements Comparator<zze> {
        final /* synthetic */ zzb zzaUh;

        C10201(zzb com_google_android_gms_measurement_zzb) {
            this.zzaUh = com_google_android_gms_measurement_zzb;
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return zza((zze) obj, (zze) obj2);
        }

        public int zza(zze com_google_android_gms_measurement_zze, zze com_google_android_gms_measurement_zze2) {
            return com_google_android_gms_measurement_zze.getClass().getCanonicalName().compareTo(com_google_android_gms_measurement_zze2.getClass().getCanonicalName());
        }
    }

    static {
        Builder builder = new Builder();
        builder.scheme("uri");
        builder.authority(Constants.LOCAL);
        zzaUf = builder.build();
    }

    public zzb() {
        this.zzaUg = new LogPrinter(4, "GA/LogCatTransport");
    }

    public void zzb(zzc com_google_android_gms_measurement_zzc) {
        List<zze> arrayList = new ArrayList(com_google_android_gms_measurement_zzc.zzAv());
        Collections.sort(arrayList, new C10201(this));
        StringBuilder stringBuilder = new StringBuilder();
        for (zze obj : arrayList) {
            Object obj2 = obj.toString();
            if (!TextUtils.isEmpty(obj2)) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(obj2);
            }
        }
        this.zzaUg.println(stringBuilder.toString());
    }

    public Uri zziA() {
        return zzaUf;
    }
}
