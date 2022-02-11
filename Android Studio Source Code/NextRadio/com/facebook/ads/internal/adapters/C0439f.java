package com.facebook.ads.internal.adapters;

import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.util.C0537t;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.adapters.f */
public class C0439f {
    private static final Set<C0441h> f1578a;
    private static final Map<AdPlacementType, String> f1579b;

    /* renamed from: com.facebook.ads.internal.adapters.f.1 */
    static /* synthetic */ class C04381 {
        static final /* synthetic */ int[] f1577a;

        static {
            f1577a = new int[AdPlacementType.values().length];
            try {
                f1577a[AdPlacementType.BANNER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1577a[AdPlacementType.INTERSTITIAL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1577a[AdPlacementType.NATIVE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    static {
        f1578a = new HashSet();
        f1579b = new ConcurrentHashMap();
        for (C0441h c0441h : C0441h.values()) {
            Class cls;
            switch (C04381.f1577a[c0441h.f1590g.ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    cls = BannerAdapter.class;
                    break;
                case Zone.SECONDARY /*2*/:
                    cls = InterstitialAdapter.class;
                    break;
                case Protocol.GGP /*3*/:
                    cls = C0448p.class;
                    break;
                default:
                    cls = null;
                    break;
            }
            if (cls != null) {
                Class cls2 = c0441h.f1587d;
                if (cls2 == null) {
                    try {
                        cls2 = Class.forName(c0441h.f1588e);
                    } catch (ClassNotFoundException e) {
                    }
                }
                if (cls2 != null && cls.isAssignableFrom(cls2)) {
                    f1578a.add(c0441h);
                }
            }
        }
    }

    public static AdAdapter m1199a(C0440g c0440g, AdPlacementType adPlacementType) {
        try {
            C0441h b = C0439f.m1202b(c0440g, adPlacementType);
            if (b != null && f1578a.contains(b)) {
                Class cls = b.f1587d;
                if (cls == null) {
                    cls = Class.forName(b.f1588e);
                }
                return (AdAdapter) cls.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AdAdapter m1200a(String str, AdPlacementType adPlacementType) {
        return C0439f.m1199a(C0440g.m1203a(str), adPlacementType);
    }

    public static String m1201a(AdPlacementType adPlacementType) {
        if (f1579b.containsKey(adPlacementType)) {
            return (String) f1579b.get(adPlacementType);
        }
        Set hashSet = new HashSet();
        for (C0441h c0441h : f1578a) {
            if (c0441h.f1590g == adPlacementType) {
                hashSet.add(c0441h.f1589f.toString());
            }
        }
        String a = C0537t.m1574a(hashSet, Stomp.COMMA);
        f1579b.put(adPlacementType, a);
        return a;
    }

    private static C0441h m1202b(C0440g c0440g, AdPlacementType adPlacementType) {
        for (C0441h c0441h : f1578a) {
            if (c0441h.f1589f == c0440g && c0441h.f1590g == adPlacementType) {
                return c0441h;
            }
        }
        return null;
    }
}
