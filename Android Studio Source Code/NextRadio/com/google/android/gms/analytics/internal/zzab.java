package com.google.android.gms.analytics.internal;

import android.text.TextUtils;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;

public class zzab {
    private final List<Command> zzSJ;
    private final long zzSK;
    private final long zzSL;
    private final int zzSM;
    private final boolean zzSN;
    private final String zzSO;
    private final Map<String, String> zzxA;

    public zzab(zzc com_google_android_gms_analytics_internal_zzc, Map<String, String> map, long j, boolean z) {
        this(com_google_android_gms_analytics_internal_zzc, map, j, z, 0, 0, null);
    }

    public zzab(zzc com_google_android_gms_analytics_internal_zzc, Map<String, String> map, long j, boolean z, long j2, int i) {
        this(com_google_android_gms_analytics_internal_zzc, map, j, z, j2, i, null);
    }

    public zzab(zzc com_google_android_gms_analytics_internal_zzc, Map<String, String> map, long j, boolean z, long j2, int i, List<Command> list) {
        zzx.zzz(com_google_android_gms_analytics_internal_zzc);
        zzx.zzz(map);
        this.zzSL = j;
        this.zzSN = z;
        this.zzSK = j2;
        this.zzSM = i;
        this.zzSJ = list != null ? list : Collections.EMPTY_LIST;
        this.zzSO = zzp(list);
        Map hashMap = new HashMap();
        for (Entry entry : map.entrySet()) {
            String zza;
            if (zzk(entry.getKey())) {
                zza = zza(com_google_android_gms_analytics_internal_zzc, entry.getKey());
                if (zza != null) {
                    hashMap.put(zza, zzb(com_google_android_gms_analytics_internal_zzc, entry.getValue()));
                }
            }
        }
        for (Entry entry2 : map.entrySet()) {
            if (!zzk(entry2.getKey())) {
                zza = zza(com_google_android_gms_analytics_internal_zzc, entry2.getKey());
                if (zza != null) {
                    hashMap.put(zza, zzb(com_google_android_gms_analytics_internal_zzc, entry2.getValue()));
                }
            }
        }
        if (!TextUtils.isEmpty(this.zzSO)) {
            zzam.zzc(hashMap, "_v", this.zzSO);
            if (this.zzSO.equals("ma4.0.0") || this.zzSO.equals("ma4.0.1")) {
                hashMap.remove("adid");
            }
        }
        this.zzxA = Collections.unmodifiableMap(hashMap);
    }

    public static zzab zza(zzc com_google_android_gms_analytics_internal_zzc, zzab com_google_android_gms_analytics_internal_zzab, Map<String, String> map) {
        return new zzab(com_google_android_gms_analytics_internal_zzc, map, com_google_android_gms_analytics_internal_zzab.zzlr(), com_google_android_gms_analytics_internal_zzab.zzlt(), com_google_android_gms_analytics_internal_zzab.zzlq(), com_google_android_gms_analytics_internal_zzab.zzlp(), com_google_android_gms_analytics_internal_zzab.zzls());
    }

    private static String zza(zzc com_google_android_gms_analytics_internal_zzc, Object obj) {
        if (obj == null) {
            return null;
        }
        Object obj2 = obj.toString();
        if (obj2.startsWith("&")) {
            obj2 = obj2.substring(1);
        }
        int length = obj2.length();
        if (length > KEYRecord.OWNER_ZONE) {
            obj2 = obj2.substring(0, KEYRecord.OWNER_ZONE);
            com_google_android_gms_analytics_internal_zzc.zzc("Hit param name is too long and will be trimmed", Integer.valueOf(length), obj2);
        }
        return TextUtils.isEmpty(obj2) ? null : obj2;
    }

    private static String zzb(zzc com_google_android_gms_analytics_internal_zzc, Object obj) {
        String obj2 = obj == null ? Stomp.EMPTY : obj.toString();
        int length = obj2.length();
        if (length <= Flags.FLAG2) {
            return obj2;
        }
        obj2 = obj2.substring(0, Flags.FLAG2);
        com_google_android_gms_analytics_internal_zzc.zzc("Hit param value is too long and will be trimmed", Integer.valueOf(length), obj2);
        return obj2;
    }

    private static boolean zzk(Object obj) {
        return obj == null ? false : obj.toString().startsWith("&");
    }

    private String zzm(String str, String str2) {
        zzx.zzcM(str);
        zzx.zzb(!str.startsWith("&"), (Object) "Short param name required");
        String str3 = (String) this.zzxA.get(str);
        return str3 != null ? str3 : str2;
    }

    private static String zzp(List<Command> list) {
        CharSequence value;
        if (list != null) {
            for (Command command : list) {
                if ("appendVersion".equals(command.getId())) {
                    value = command.getValue();
                    break;
                }
            }
        }
        value = null;
        return TextUtils.isEmpty(value) ? null : value;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ht=").append(this.zzSL);
        if (this.zzSK != 0) {
            stringBuffer.append(", dbId=").append(this.zzSK);
        }
        if (((long) this.zzSM) != 0) {
            stringBuffer.append(", appUID=").append(this.zzSM);
        }
        List<String> arrayList = new ArrayList(this.zzxA.keySet());
        Collections.sort(arrayList);
        for (String str : arrayList) {
            stringBuffer.append(", ");
            stringBuffer.append(str);
            stringBuffer.append("=");
            stringBuffer.append((String) this.zzxA.get(str));
        }
        return stringBuffer.toString();
    }

    public int zzlp() {
        return this.zzSM;
    }

    public long zzlq() {
        return this.zzSK;
    }

    public long zzlr() {
        return this.zzSL;
    }

    public List<Command> zzls() {
        return this.zzSJ;
    }

    public boolean zzlt() {
        return this.zzSN;
    }

    public long zzlu() {
        return zzam.zzbt(zzm("_s", "0"));
    }

    public String zzlv() {
        return zzm("_m", Stomp.EMPTY);
    }

    public Map<String, String> zzn() {
        return this.zzxA;
    }
}
