package com.facebook.ads.internal.util;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* renamed from: com.facebook.ads.internal.util.a */
public class C0512a {
    private static SensorManager f1881a;
    private static Sensor f1882b;
    private static Sensor f1883c;
    private static volatile float[] f1884d;
    private static volatile float[] f1885e;
    private static Map<String, Object> f1886f;
    private static String[] f1887g;

    static {
        f1881a = null;
        f1882b = null;
        f1883c = null;
        f1886f = new ConcurrentHashMap();
        f1887g = new String[]{"x", "y", "z"};
    }

    public static Map<String, Object> m1509a() {
        Map<String, Object> hashMap = new HashMap();
        hashMap.putAll(f1886f);
        C0512a.m1510a(hashMap);
        return hashMap;
    }

    private static void m1510a(Map<String, Object> map) {
        int i;
        int i2 = 0;
        float[] fArr = f1884d;
        float[] fArr2 = f1885e;
        if (fArr != null) {
            int min = Math.min(f1887g.length, fArr.length);
            for (i = 0; i < min; i++) {
                map.put("accelerometer_" + f1887g[i], Float.valueOf(fArr[i]));
            }
        }
        if (fArr2 != null) {
            i = Math.min(f1887g.length, fArr2.length);
            while (i2 < i) {
                map.put("rotation_" + f1887g[i2], Float.valueOf(fArr2[i2]));
                i2++;
            }
        }
    }
}
