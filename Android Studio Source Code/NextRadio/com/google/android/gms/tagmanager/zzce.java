package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag.zza;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class zzce extends zzak {
    private static final String ID;
    private static final String zzbka;
    private static final String zzbkb;
    private static final String zzbkc;
    private static final String zzbkd;

    static {
        ID = zzad.REGEX_GROUP.toString();
        zzbka = zzae.ARG0.toString();
        zzbkb = zzae.ARG1.toString();
        zzbkc = zzae.IGNORE_CASE.toString();
        zzbkd = zzae.GROUP.toString();
    }

    public zzce() {
        super(ID, zzbka, zzbkb);
    }

    public boolean zzFW() {
        return true;
    }

    public zza zzP(Map<String, zza> map) {
        zza com_google_android_gms_internal_zzag_zza = (zza) map.get(zzbka);
        zza com_google_android_gms_internal_zzag_zza2 = (zza) map.get(zzbkb);
        if (com_google_android_gms_internal_zzag_zza == null || com_google_android_gms_internal_zzag_zza == zzdf.zzHF() || com_google_android_gms_internal_zzag_zza2 == null || com_google_android_gms_internal_zzag_zza2 == zzdf.zzHF()) {
            return zzdf.zzHF();
        }
        int i = 64;
        if (zzdf.zzk((zza) map.get(zzbkc)).booleanValue()) {
            i = 66;
        }
        zza com_google_android_gms_internal_zzag_zza3 = (zza) map.get(zzbkd);
        int intValue;
        if (com_google_android_gms_internal_zzag_zza3 != null) {
            Long zzi = zzdf.zzi(com_google_android_gms_internal_zzag_zza3);
            if (zzi == zzdf.zzHA()) {
                return zzdf.zzHF();
            }
            intValue = zzi.intValue();
            if (intValue < 0) {
                return zzdf.zzHF();
            }
        }
        intValue = 1;
        try {
            CharSequence zzg = zzdf.zzg(com_google_android_gms_internal_zzag_zza);
            Object obj = null;
            Matcher matcher = Pattern.compile(zzdf.zzg(com_google_android_gms_internal_zzag_zza2), i).matcher(zzg);
            if (matcher.find() && matcher.groupCount() >= intValue) {
                obj = matcher.group(intValue);
            }
            return obj == null ? zzdf.zzHF() : zzdf.zzR(obj);
        } catch (PatternSyntaxException e) {
            return zzdf.zzHF();
        }
    }
}
