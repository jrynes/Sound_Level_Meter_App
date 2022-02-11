package com.facebook.ads.internal.util;

import com.facebook.ads.internal.dto.C0463b;
import com.facebook.ads.internal.dto.C0467e;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.util.d */
public class C0517d {
    private static Map<String, Long> f1902a;
    private static Map<String, Long> f1903b;
    private static Map<String, String> f1904c;

    /* renamed from: com.facebook.ads.internal.util.d.1 */
    static /* synthetic */ class C05161 {
        static final /* synthetic */ int[] f1901a;

        static {
            f1901a = new int[C0463b.values().length];
            try {
                f1901a[C0463b.BANNER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1901a[C0463b.INTERSTITIAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1901a[C0463b.NATIVE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1901a[C0463b.UNKNOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    static {
        f1902a = new ConcurrentHashMap();
        f1903b = new ConcurrentHashMap();
        f1904c = new ConcurrentHashMap();
    }

    private static long m1516a(String str, C0463b c0463b) {
        if (f1902a.containsKey(str)) {
            return ((Long) f1902a.get(str)).longValue();
        }
        switch (C05161.f1901a[c0463b.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                return 15000;
            case Zone.SECONDARY /*2*/:
            case Protocol.GGP /*3*/:
                return -1000;
            default:
                return -1000;
        }
    }

    public static void m1517a(long j, C0467e c0467e) {
        f1902a.put(C0517d.m1522d(c0467e), Long.valueOf(j));
    }

    public static void m1518a(String str, C0467e c0467e) {
        f1904c.put(C0517d.m1522d(c0467e), str);
    }

    public static boolean m1519a(C0467e c0467e) {
        String d = C0517d.m1522d(c0467e);
        if (!f1903b.containsKey(d)) {
            return false;
        }
        return System.currentTimeMillis() - ((Long) f1903b.get(d)).longValue() < C0517d.m1516a(d, c0467e.m1378b());
    }

    public static void m1520b(C0467e c0467e) {
        f1903b.put(C0517d.m1522d(c0467e), Long.valueOf(System.currentTimeMillis()));
    }

    public static String m1521c(C0467e c0467e) {
        return (String) f1904c.get(C0517d.m1522d(c0467e));
    }

    private static String m1522d(C0467e c0467e) {
        int i = 0;
        String str = "%s:%s:%s:%d:%d:%d";
        Object[] objArr = new Object[6];
        objArr[0] = c0467e.m1377a();
        objArr[1] = c0467e.m1378b();
        objArr[2] = c0467e.f1732e;
        objArr[3] = Integer.valueOf(c0467e.m1379c() == null ? 0 : c0467e.m1379c().getHeight());
        if (c0467e.m1379c() != null) {
            i = c0467e.m1379c().getWidth();
        }
        objArr[4] = Integer.valueOf(i);
        objArr[5] = Integer.valueOf(c0467e.m1380d());
        return String.format(str, objArr);
    }
}
