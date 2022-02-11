package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzag.zza;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class zzdf {
    private static final Object zzblE;
    private static Long zzblF;
    private static Double zzblG;
    private static zzde zzblH;
    private static String zzblI;
    private static Boolean zzblJ;
    private static List<Object> zzblK;
    private static Map<Object, Object> zzblL;
    private static zza zzblM;

    static {
        zzblE = null;
        zzblF = new Long(0);
        zzblG = new Double(0.0d);
        zzblH = zzde.zzam(0);
        zzblI = new String(Stomp.EMPTY);
        zzblJ = new Boolean(false);
        zzblK = new ArrayList(0);
        zzblL = new HashMap();
        zzblM = zzR(zzblI);
    }

    private static double getDouble(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        zzbg.m1675e("getDouble received non-Number");
        return 0.0d;
    }

    public static Long zzHA() {
        return zzblF;
    }

    public static Double zzHB() {
        return zzblG;
    }

    public static Boolean zzHC() {
        return zzblJ;
    }

    public static zzde zzHD() {
        return zzblH;
    }

    public static String zzHE() {
        return zzblI;
    }

    public static zza zzHF() {
        return zzblM;
    }

    public static Object zzHz() {
        return zzblE;
    }

    public static String zzM(Object obj) {
        return obj == null ? zzblI : obj.toString();
    }

    public static zzde zzN(Object obj) {
        return obj instanceof zzde ? (zzde) obj : zzT(obj) ? zzde.zzam(zzU(obj)) : zzS(obj) ? zzde.zza(Double.valueOf(getDouble(obj))) : zzgu(zzM(obj));
    }

    public static Long zzO(Object obj) {
        return zzT(obj) ? Long.valueOf(zzU(obj)) : zzgv(zzM(obj));
    }

    public static Double zzP(Object obj) {
        return zzS(obj) ? Double.valueOf(getDouble(obj)) : zzgw(zzM(obj));
    }

    public static Boolean zzQ(Object obj) {
        return obj instanceof Boolean ? (Boolean) obj : zzgx(zzM(obj));
    }

    public static zza zzR(Object obj) {
        boolean z = false;
        zza com_google_android_gms_internal_zzag_zza = new zza();
        if (obj instanceof zza) {
            return (zza) obj;
        }
        if (obj instanceof String) {
            com_google_android_gms_internal_zzag_zza.type = 1;
            com_google_android_gms_internal_zzag_zza.zzjx = (String) obj;
        } else if (obj instanceof List) {
            com_google_android_gms_internal_zzag_zza.type = 2;
            List<Object> list = (List) obj;
            r5 = new ArrayList(list.size());
            r1 = false;
            for (Object zzR : list) {
                zza zzR2 = zzR(zzR);
                if (zzR2 == zzblM) {
                    return zzblM;
                }
                r0 = r1 || zzR2.zzjH;
                r5.add(zzR2);
                r1 = r0;
            }
            com_google_android_gms_internal_zzag_zza.zzjy = (zza[]) r5.toArray(new zza[0]);
            z = r1;
        } else if (obj instanceof Map) {
            com_google_android_gms_internal_zzag_zza.type = 3;
            Set<Entry> entrySet = ((Map) obj).entrySet();
            r5 = new ArrayList(entrySet.size());
            List arrayList = new ArrayList(entrySet.size());
            r1 = false;
            for (Entry entry : entrySet) {
                zza zzR3 = zzR(entry.getKey());
                zza zzR4 = zzR(entry.getValue());
                if (zzR3 == zzblM || zzR4 == zzblM) {
                    return zzblM;
                }
                r0 = r1 || zzR3.zzjH || zzR4.zzjH;
                r5.add(zzR3);
                arrayList.add(zzR4);
                r1 = r0;
            }
            com_google_android_gms_internal_zzag_zza.zzjz = (zza[]) r5.toArray(new zza[0]);
            com_google_android_gms_internal_zzag_zza.zzjA = (zza[]) arrayList.toArray(new zza[0]);
            z = r1;
        } else if (zzS(obj)) {
            com_google_android_gms_internal_zzag_zza.type = 1;
            com_google_android_gms_internal_zzag_zza.zzjx = obj.toString();
        } else if (zzT(obj)) {
            com_google_android_gms_internal_zzag_zza.type = 6;
            com_google_android_gms_internal_zzag_zza.zzjD = zzU(obj);
        } else if (obj instanceof Boolean) {
            com_google_android_gms_internal_zzag_zza.type = 8;
            com_google_android_gms_internal_zzag_zza.zzjE = ((Boolean) obj).booleanValue();
        } else {
            zzbg.m1675e("Converting to Value from unknown object type: " + (obj == null ? "null" : obj.getClass().toString()));
            return zzblM;
        }
        com_google_android_gms_internal_zzag_zza.zzjH = z;
        return com_google_android_gms_internal_zzag_zza;
    }

    private static boolean zzS(Object obj) {
        return (obj instanceof Double) || (obj instanceof Float) || ((obj instanceof zzde) && ((zzde) obj).zzHu());
    }

    private static boolean zzT(Object obj) {
        return (obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Integer) || (obj instanceof Long) || ((obj instanceof zzde) && ((zzde) obj).zzHv());
    }

    private static long zzU(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        zzbg.m1675e("getInt64 received non-Number");
        return 0;
    }

    public static String zzg(zza com_google_android_gms_internal_zzag_zza) {
        return zzM(zzl(com_google_android_gms_internal_zzag_zza));
    }

    public static zza zzgt(String str) {
        zza com_google_android_gms_internal_zzag_zza = new zza();
        com_google_android_gms_internal_zzag_zza.type = 5;
        com_google_android_gms_internal_zzag_zza.zzjC = str;
        return com_google_android_gms_internal_zzag_zza;
    }

    private static zzde zzgu(String str) {
        try {
            return zzde.zzgs(str);
        } catch (NumberFormatException e) {
            zzbg.m1675e("Failed to convert '" + str + "' to a number.");
            return zzblH;
        }
    }

    private static Long zzgv(String str) {
        zzde zzgu = zzgu(str);
        return zzgu == zzblH ? zzblF : Long.valueOf(zzgu.longValue());
    }

    private static Double zzgw(String str) {
        zzde zzgu = zzgu(str);
        return zzgu == zzblH ? zzblG : Double.valueOf(zzgu.doubleValue());
    }

    private static Boolean zzgx(String str) {
        return Stomp.TRUE.equalsIgnoreCase(str) ? Boolean.TRUE : Stomp.FALSE.equalsIgnoreCase(str) ? Boolean.FALSE : zzblJ;
    }

    public static zzde zzh(zza com_google_android_gms_internal_zzag_zza) {
        return zzN(zzl(com_google_android_gms_internal_zzag_zza));
    }

    public static Long zzi(zza com_google_android_gms_internal_zzag_zza) {
        return zzO(zzl(com_google_android_gms_internal_zzag_zza));
    }

    public static Double zzj(zza com_google_android_gms_internal_zzag_zza) {
        return zzP(zzl(com_google_android_gms_internal_zzag_zza));
    }

    public static Boolean zzk(zza com_google_android_gms_internal_zzag_zza) {
        return zzQ(zzl(com_google_android_gms_internal_zzag_zza));
    }

    public static Object zzl(zza com_google_android_gms_internal_zzag_zza) {
        int i = 0;
        if (com_google_android_gms_internal_zzag_zza == null) {
            return zzblE;
        }
        zza[] com_google_android_gms_internal_zzag_zzaArr;
        int length;
        switch (com_google_android_gms_internal_zzag_zza.type) {
            case Zone.PRIMARY /*1*/:
                return com_google_android_gms_internal_zzag_zza.zzjx;
            case Zone.SECONDARY /*2*/:
                ArrayList arrayList = new ArrayList(com_google_android_gms_internal_zzag_zza.zzjy.length);
                com_google_android_gms_internal_zzag_zzaArr = com_google_android_gms_internal_zzag_zza.zzjy;
                length = com_google_android_gms_internal_zzag_zzaArr.length;
                while (i < length) {
                    Object zzl = zzl(com_google_android_gms_internal_zzag_zzaArr[i]);
                    if (zzl == zzblE) {
                        return zzblE;
                    }
                    arrayList.add(zzl);
                    i++;
                }
                return arrayList;
            case Protocol.GGP /*3*/:
                if (com_google_android_gms_internal_zzag_zza.zzjz.length != com_google_android_gms_internal_zzag_zza.zzjA.length) {
                    zzbg.m1675e("Converting an invalid value to object: " + com_google_android_gms_internal_zzag_zza.toString());
                    return zzblE;
                }
                Map hashMap = new HashMap(com_google_android_gms_internal_zzag_zza.zzjA.length);
                while (i < com_google_android_gms_internal_zzag_zza.zzjz.length) {
                    Object zzl2 = zzl(com_google_android_gms_internal_zzag_zza.zzjz[i]);
                    Object zzl3 = zzl(com_google_android_gms_internal_zzag_zza.zzjA[i]);
                    if (zzl2 == zzblE || zzl3 == zzblE) {
                        return zzblE;
                    }
                    hashMap.put(zzl2, zzl3);
                    i++;
                }
                return hashMap;
            case Type.MF /*4*/:
                zzbg.m1675e("Trying to convert a macro reference to object");
                return zzblE;
            case Service.RJE /*5*/:
                zzbg.m1675e("Trying to convert a function id to object");
                return zzblE;
            case Protocol.TCP /*6*/:
                return Long.valueOf(com_google_android_gms_internal_zzag_zza.zzjD);
            case Service.ECHO /*7*/:
                StringBuffer stringBuffer = new StringBuffer();
                com_google_android_gms_internal_zzag_zzaArr = com_google_android_gms_internal_zzag_zza.zzjF;
                length = com_google_android_gms_internal_zzag_zzaArr.length;
                while (i < length) {
                    String zzg = zzg(com_google_android_gms_internal_zzag_zzaArr[i]);
                    if (zzg == zzblI) {
                        return zzblE;
                    }
                    stringBuffer.append(zzg);
                    i++;
                }
                return stringBuffer.toString();
            case Protocol.EGP /*8*/:
                return Boolean.valueOf(com_google_android_gms_internal_zzag_zza.zzjE);
            default:
                zzbg.m1675e("Failed to convert a value of type: " + com_google_android_gms_internal_zzag_zza.type);
                return zzblE;
        }
    }
}
