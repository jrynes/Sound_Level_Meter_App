package com.google.android.gms.analytics;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.admarvel.android.ads.Constants;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.google.android.gms.analytics.internal.zzab;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.analytics.internal.zzc;
import com.google.android.gms.analytics.internal.zze;
import com.google.android.gms.analytics.internal.zzf;
import com.google.android.gms.analytics.internal.zzh;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzkb;
import com.google.android.gms.internal.zzkc;
import com.google.android.gms.internal.zzkd;
import com.google.android.gms.internal.zzke;
import com.google.android.gms.internal.zzpq;
import com.google.android.gms.internal.zzpr;
import com.google.android.gms.internal.zzps;
import com.google.android.gms.internal.zzpt;
import com.google.android.gms.internal.zzpu;
import com.google.android.gms.internal.zzpv;
import com.google.android.gms.internal.zzpw;
import com.google.android.gms.internal.zzpx;
import com.google.android.gms.internal.zzpy;
import com.google.android.gms.measurement.zzi;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class zzb extends zzc implements zzi {
    private static DecimalFormat zzOU;
    private final zzf zzOK;
    private final String zzOV;
    private final Uri zzOW;
    private final boolean zzOX;
    private final boolean zzOY;

    public zzb(zzf com_google_android_gms_analytics_internal_zzf, String str) {
        this(com_google_android_gms_analytics_internal_zzf, str, true, false);
    }

    public zzb(zzf com_google_android_gms_analytics_internal_zzf, String str, boolean z, boolean z2) {
        super(com_google_android_gms_analytics_internal_zzf);
        zzx.zzcM(str);
        this.zzOK = com_google_android_gms_analytics_internal_zzf;
        this.zzOV = str;
        this.zzOX = z;
        this.zzOY = z2;
        this.zzOW = zzaU(this.zzOV);
    }

    private static String zzH(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : map.entrySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append((String) entry.getValue());
        }
        return stringBuilder.toString();
    }

    private static void zza(Map<String, String> map, String str, double d) {
        if (d != 0.0d) {
            map.put(str, zzb(d));
        }
    }

    private static void zza(Map<String, String> map, String str, int i, int i2) {
        if (i > 0 && i2 > 0) {
            map.put(str, i + "x" + i2);
        }
    }

    private static void zza(Map<String, String> map, String str, boolean z) {
        if (z) {
            map.put(str, "1");
        }
    }

    static Uri zzaU(String str) {
        zzx.zzcM(str);
        Builder builder = new Builder();
        builder.scheme("uri");
        builder.authority("google-analytics.com");
        builder.path(str);
        return builder.build();
    }

    static String zzb(double d) {
        if (zzOU == null) {
            zzOU = new DecimalFormat("0.######");
        }
        return zzOU.format(d);
    }

    private static void zzb(Map<String, String> map, String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            map.put(str, str2);
        }
    }

    public static Map<String, String> zzc(com.google.android.gms.measurement.zzc com_google_android_gms_measurement_zzc) {
        CharSequence zzU;
        Map hashMap = new HashMap();
        zzkd com_google_android_gms_internal_zzkd = (zzkd) com_google_android_gms_measurement_zzc.zze(zzkd.class);
        if (com_google_android_gms_internal_zzkd != null) {
            for (Entry entry : com_google_android_gms_internal_zzkd.zziR().entrySet()) {
                String zzi = zzi(entry.getValue());
                if (zzi != null) {
                    hashMap.put(entry.getKey(), zzi);
                }
            }
        }
        zzke com_google_android_gms_internal_zzke = (zzke) com_google_android_gms_measurement_zzc.zze(zzke.class);
        if (com_google_android_gms_internal_zzke != null) {
            zzb(hashMap, "t", com_google_android_gms_internal_zzke.zziS());
            zzb(hashMap, "cid", com_google_android_gms_internal_zzke.getClientId());
            zzb(hashMap, "uid", com_google_android_gms_internal_zzke.getUserId());
            zzb(hashMap, "sc", com_google_android_gms_internal_zzke.zziV());
            zza(hashMap, "sf", com_google_android_gms_internal_zzke.zziX());
            zza(hashMap, "ni", com_google_android_gms_internal_zzke.zziW());
            zzb(hashMap, "adid", com_google_android_gms_internal_zzke.zziT());
            zza(hashMap, "ate", com_google_android_gms_internal_zzke.zziU());
        }
        zzpw com_google_android_gms_internal_zzpw = (zzpw) com_google_android_gms_measurement_zzc.zze(zzpw.class);
        if (com_google_android_gms_internal_zzpw != null) {
            zzb(hashMap, "cd", com_google_android_gms_internal_zzpw.zzBc());
            zza(hashMap, "a", (double) com_google_android_gms_internal_zzpw.zzBd());
            zzb(hashMap, "dr", com_google_android_gms_internal_zzpw.zzBe());
        }
        zzpu com_google_android_gms_internal_zzpu = (zzpu) com_google_android_gms_measurement_zzc.zze(zzpu.class);
        if (com_google_android_gms_internal_zzpu != null) {
            zzb(hashMap, "ec", com_google_android_gms_internal_zzpu.zzAZ());
            zzb(hashMap, "ea", com_google_android_gms_internal_zzpu.getAction());
            zzb(hashMap, "el", com_google_android_gms_internal_zzpu.getLabel());
            zza(hashMap, "ev", (double) com_google_android_gms_internal_zzpu.getValue());
        }
        zzpr com_google_android_gms_internal_zzpr = (zzpr) com_google_android_gms_measurement_zzc.zze(zzpr.class);
        if (com_google_android_gms_internal_zzpr != null) {
            zzb(hashMap, "cn", com_google_android_gms_internal_zzpr.getName());
            zzb(hashMap, "cs", com_google_android_gms_internal_zzpr.getSource());
            zzb(hashMap, "cm", com_google_android_gms_internal_zzpr.zzAK());
            zzb(hashMap, "ck", com_google_android_gms_internal_zzpr.zzAL());
            zzb(hashMap, "cc", com_google_android_gms_internal_zzpr.getContent());
            zzb(hashMap, "ci", com_google_android_gms_internal_zzpr.getId());
            zzb(hashMap, "anid", com_google_android_gms_internal_zzpr.zzAM());
            zzb(hashMap, "gclid", com_google_android_gms_internal_zzpr.zzAN());
            zzb(hashMap, "dclid", com_google_android_gms_internal_zzpr.zzAO());
            zzb(hashMap, "aclid", com_google_android_gms_internal_zzpr.zzAP());
        }
        zzpv com_google_android_gms_internal_zzpv = (zzpv) com_google_android_gms_measurement_zzc.zze(zzpv.class);
        if (com_google_android_gms_internal_zzpv != null) {
            zzb(hashMap, "exd", com_google_android_gms_internal_zzpv.getDescription());
            zza(hashMap, "exf", com_google_android_gms_internal_zzpv.zzBa());
        }
        zzpx com_google_android_gms_internal_zzpx = (zzpx) com_google_android_gms_measurement_zzc.zze(zzpx.class);
        if (com_google_android_gms_internal_zzpx != null) {
            zzb(hashMap, "sn", com_google_android_gms_internal_zzpx.zzBg());
            zzb(hashMap, "sa", com_google_android_gms_internal_zzpx.getAction());
            zzb(hashMap, "st", com_google_android_gms_internal_zzpx.getTarget());
        }
        zzpy com_google_android_gms_internal_zzpy = (zzpy) com_google_android_gms_measurement_zzc.zze(zzpy.class);
        if (com_google_android_gms_internal_zzpy != null) {
            zzb(hashMap, "utv", com_google_android_gms_internal_zzpy.zzBh());
            zza(hashMap, "utt", (double) com_google_android_gms_internal_zzpy.getTimeInMillis());
            zzb(hashMap, Constants.UTC, com_google_android_gms_internal_zzpy.zzAZ());
            zzb(hashMap, "utl", com_google_android_gms_internal_zzpy.getLabel());
        }
        zzkb com_google_android_gms_internal_zzkb = (zzkb) com_google_android_gms_measurement_zzc.zze(zzkb.class);
        if (com_google_android_gms_internal_zzkb != null) {
            for (Entry entry2 : com_google_android_gms_internal_zzkb.zziP().entrySet()) {
                zzU = zzc.zzU(((Integer) entry2.getKey()).intValue());
                if (!TextUtils.isEmpty(zzU)) {
                    hashMap.put(zzU, entry2.getValue());
                }
            }
        }
        zzkc com_google_android_gms_internal_zzkc = (zzkc) com_google_android_gms_measurement_zzc.zze(zzkc.class);
        if (com_google_android_gms_internal_zzkc != null) {
            for (Entry entry22 : com_google_android_gms_internal_zzkc.zziQ().entrySet()) {
                zzU = zzc.zzW(((Integer) entry22.getKey()).intValue());
                if (!TextUtils.isEmpty(zzU)) {
                    hashMap.put(zzU, zzb(((Double) entry22.getValue()).doubleValue()));
                }
            }
        }
        zzpt com_google_android_gms_internal_zzpt = (zzpt) com_google_android_gms_measurement_zzc.zze(zzpt.class);
        if (com_google_android_gms_internal_zzpt != null) {
            ProductAction zzAV = com_google_android_gms_internal_zzpt.zzAV();
            if (zzAV != null) {
                for (Entry entry3 : zzAV.build().entrySet()) {
                    if (((String) entry3.getKey()).startsWith("&")) {
                        hashMap.put(((String) entry3.getKey()).substring(1), entry3.getValue());
                    } else {
                        hashMap.put(entry3.getKey(), entry3.getValue());
                    }
                }
            }
            int i = 1;
            for (Promotion zzba : com_google_android_gms_internal_zzpt.zzAY()) {
                hashMap.putAll(zzba.zzba(zzc.zzaa(i)));
                i++;
            }
            i = 1;
            for (Product zzba2 : com_google_android_gms_internal_zzpt.zzAW()) {
                hashMap.putAll(zzba2.zzba(zzc.zzY(i)));
                i++;
            }
            i = 1;
            for (Entry entry222 : com_google_android_gms_internal_zzpt.zzAX().entrySet()) {
                List<Product> list = (List) entry222.getValue();
                String zzad = zzc.zzad(i);
                int i2 = 1;
                for (Product zzba22 : list) {
                    hashMap.putAll(zzba22.zzba(zzad + zzc.zzab(i2)));
                    i2++;
                }
                if (!TextUtils.isEmpty((CharSequence) entry222.getKey())) {
                    hashMap.put(zzad + "nm", entry222.getKey());
                }
                i++;
            }
        }
        zzps com_google_android_gms_internal_zzps = (zzps) com_google_android_gms_measurement_zzc.zze(zzps.class);
        if (com_google_android_gms_internal_zzps != null) {
            zzb(hashMap, "ul", com_google_android_gms_internal_zzps.getLanguage());
            zza(hashMap, "sd", (double) com_google_android_gms_internal_zzps.zzAQ());
            zza(hashMap, "sr", com_google_android_gms_internal_zzps.zzAR(), com_google_android_gms_internal_zzps.zzAS());
            zza(hashMap, "vp", com_google_android_gms_internal_zzps.zzAT(), com_google_android_gms_internal_zzps.zzAU());
        }
        zzpq com_google_android_gms_internal_zzpq = (zzpq) com_google_android_gms_measurement_zzc.zze(zzpq.class);
        if (com_google_android_gms_internal_zzpq != null) {
            zzb(hashMap, "an", com_google_android_gms_internal_zzpq.zzlg());
            zzb(hashMap, "aid", com_google_android_gms_internal_zzpq.zzwK());
            zzb(hashMap, "aiid", com_google_android_gms_internal_zzpq.zzAJ());
            zzb(hashMap, "av", com_google_android_gms_internal_zzpq.zzli());
        }
        return hashMap;
    }

    private static String zzi(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            return TextUtils.isEmpty(str) ? null : str;
        } else if (!(obj instanceof Double)) {
            return obj instanceof Boolean ? obj != Boolean.FALSE ? "1" : null : String.valueOf(obj);
        } else {
            Double d = (Double) obj;
            return d.doubleValue() != 0.0d ? zzb(d.doubleValue()) : null;
        }
    }

    public void zzb(com.google.android.gms.measurement.zzc com_google_android_gms_measurement_zzc) {
        zzx.zzz(com_google_android_gms_measurement_zzc);
        zzx.zzb(com_google_android_gms_measurement_zzc.zzAz(), (Object) "Can't deliver not submitted measurement");
        zzx.zzcE("deliver should be called on worker thread");
        com.google.android.gms.measurement.zzc zzAu = com_google_android_gms_measurement_zzc.zzAu();
        zzke com_google_android_gms_internal_zzke = (zzke) zzAu.zzf(zzke.class);
        if (TextUtils.isEmpty(com_google_android_gms_internal_zzke.zziS())) {
            zzjm().zzh(zzc(zzAu), "Ignoring measurement without type");
        } else if (TextUtils.isEmpty(com_google_android_gms_internal_zzke.getClientId())) {
            zzjm().zzh(zzc(zzAu), "Ignoring measurement without client id");
        } else if (!this.zzOK.zzjz().getAppOptOut()) {
            double zziX = com_google_android_gms_internal_zzke.zziX();
            if (zzam.zza(zziX, com_google_android_gms_internal_zzke.getClientId())) {
                zzb("Sampling enabled. Hit sampled out. sampling rate", Double.valueOf(zziX));
                return;
            }
            Map zzc = zzc(zzAu);
            zzc.put("v", "1");
            zzc.put("_v", zze.zzQm);
            zzc.put("tid", this.zzOV);
            if (this.zzOK.zzjz().isDryRunEnabled()) {
                zzc("Dry run is enabled. GoogleAnalytics would have sent", zzH(zzc));
                return;
            }
            Map hashMap = new HashMap();
            zzam.zzc(hashMap, "uid", com_google_android_gms_internal_zzke.getUserId());
            zzpq com_google_android_gms_internal_zzpq = (zzpq) com_google_android_gms_measurement_zzc.zze(zzpq.class);
            if (com_google_android_gms_internal_zzpq != null) {
                zzam.zzc(hashMap, "an", com_google_android_gms_internal_zzpq.zzlg());
                zzam.zzc(hashMap, "aid", com_google_android_gms_internal_zzpq.zzwK());
                zzam.zzc(hashMap, "av", com_google_android_gms_internal_zzpq.zzli());
                zzam.zzc(hashMap, "aiid", com_google_android_gms_internal_zzpq.zzAJ());
            }
            zzc.put("_s", String.valueOf(zziH().zza(new zzh(0, com_google_android_gms_internal_zzke.getClientId(), this.zzOV, !TextUtils.isEmpty(com_google_android_gms_internal_zzke.zziT()), 0, hashMap))));
            zziH().zza(new zzab(zzjm(), zzc, com_google_android_gms_measurement_zzc.zzAx(), true));
        }
    }

    public Uri zziA() {
        return this.zzOW;
    }
}
