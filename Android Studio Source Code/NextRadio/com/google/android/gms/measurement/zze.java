package com.google.android.gms.measurement;

import android.text.TextUtils;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.activemq.transport.stomp.Stomp;

public abstract class zze<T extends zze> {
    public static String zzF(Object obj) {
        return zza(obj, 0);
    }

    public static String zzO(Map map) {
        return zza(map, 1);
    }

    private static String zza(Object obj, int i) {
        if (i > 10) {
            return "ERROR: Recursive toString calls";
        }
        if (obj == null) {
            return Stomp.EMPTY;
        }
        if (obj instanceof String) {
            return TextUtils.isEmpty((String) obj) ? Stomp.EMPTY : obj.toString();
        } else {
            if (obj instanceof Integer) {
                return ((Integer) obj).intValue() == 0 ? Stomp.EMPTY : obj.toString();
            } else {
                if (obj instanceof Long) {
                    return ((Long) obj).longValue() == 0 ? Stomp.EMPTY : obj.toString();
                } else {
                    if (obj instanceof Double) {
                        return ((Double) obj).doubleValue() == 0.0d ? Stomp.EMPTY : obj.toString();
                    } else {
                        if (obj instanceof Boolean) {
                            return !((Boolean) obj).booleanValue() ? Stomp.EMPTY : obj.toString();
                        } else {
                            int length;
                            if (obj instanceof List) {
                                StringBuffer stringBuffer = new StringBuffer();
                                if (i > 0) {
                                    stringBuffer.append("[");
                                }
                                List list = (List) obj;
                                length = stringBuffer.length();
                                for (Object next : list) {
                                    if (stringBuffer.length() > length) {
                                        stringBuffer.append(", ");
                                    }
                                    stringBuffer.append(zza(next, i + 1));
                                }
                                if (i > 0) {
                                    stringBuffer.append("]");
                                }
                                return stringBuffer.toString();
                            } else if (!(obj instanceof Map)) {
                                return obj.toString();
                            } else {
                                StringBuffer stringBuffer2 = new StringBuffer();
                                length = 0;
                                Object obj2 = null;
                                for (Entry entry : new TreeMap((Map) obj).entrySet()) {
                                    Object zza = zza(entry.getValue(), i + 1);
                                    if (!TextUtils.isEmpty(zza)) {
                                        if (i > 0 && obj2 == null) {
                                            stringBuffer2.append("{");
                                            obj2 = 1;
                                            length = stringBuffer2.length();
                                        }
                                        if (stringBuffer2.length() > length) {
                                            stringBuffer2.append(", ");
                                        }
                                        stringBuffer2.append((String) entry.getKey());
                                        stringBuffer2.append('=');
                                        stringBuffer2.append(zza);
                                    }
                                }
                                if (obj2 != null) {
                                    stringBuffer2.append("}");
                                }
                                return stringBuffer2.toString();
                            }
                        }
                    }
                }
            }
        }
    }

    public abstract void zza(T t);
}
