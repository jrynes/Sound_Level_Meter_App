package com.facebook.ads.internal.dto;

import android.content.Context;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.AdSdkVersion;
import com.facebook.ads.internal.C0459c;
import com.facebook.ads.internal.C0460d;
import com.facebook.ads.internal.C0469e;
import com.facebook.ads.internal.adapters.C0439f;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.util.AdInternalSettings;
import com.facebook.ads.internal.util.C0515c;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.dto.e */
public class C0467e {
    protected String f1728a;
    protected AdPlacementType f1729b;
    protected C0463b f1730c;
    public Context f1731d;
    public C0469e f1732e;
    public boolean f1733f;
    private C0459c f1734g;
    private int f1735h;
    private AdSize f1736i;

    /* renamed from: com.facebook.ads.internal.dto.e.1 */
    static /* synthetic */ class C04661 {
        static final /* synthetic */ int[] f1727a;

        static {
            f1727a = new int[C0463b.values().length];
            try {
                f1727a[C0463b.INTERSTITIAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1727a[C0463b.BANNER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1727a[C0463b.NATIVE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public C0467e(Context context, String str, AdSize adSize, C0469e c0469e, C0459c c0459c, int i, boolean z) {
        this.f1728a = str;
        this.f1736i = adSize;
        this.f1732e = c0469e;
        this.f1730c = C0463b.m1360a(c0469e);
        this.f1734g = c0459c;
        this.f1735h = i;
        this.f1733f = z;
        m1373a(context);
    }

    private void m1373a(Context context) {
        this.f1731d = context;
        C0468f.m1382a(context);
        m1376f();
    }

    private void m1374a(Map<String, String> map, String str, String str2) {
        if (AdInternalSettings.shouldUseLiveRailEndpoint()) {
            map.put("LR_" + str, str2);
        } else {
            map.put(str, str2);
        }
    }

    private static Map<String, String> m1375b(Context context) {
        Map<String, String> hashMap = new HashMap();
        hashMap.put("VIEWABLE", "1");
        hashMap.put("SCHEMA", "json");
        hashMap.put("SDK", AbstractSpiCall.ANDROID_CLIENT_TYPE);
        hashMap.put("SDK_VERSION", AdSdkVersion.BUILD);
        hashMap.put("LOCALE", Locale.getDefault().toString());
        float f = context.getResources().getDisplayMetrics().density;
        int i = context.getResources().getDisplayMetrics().widthPixels;
        int i2 = context.getResources().getDisplayMetrics().heightPixels;
        hashMap.put("DENSITY", String.valueOf(f));
        hashMap.put("SCREEN_WIDTH", String.valueOf((int) (((float) i) / f)));
        hashMap.put("SCREEN_HEIGHT", String.valueOf((int) (((float) i2) / f)));
        hashMap.put("IDFA", C0468f.f1750n);
        hashMap.put("IDFA_FLAG", C0468f.f1751o ? "0" : "1");
        hashMap.put("ATTRIBUTION_ID", C0468f.f1749m);
        hashMap.put("OS", "Android");
        hashMap.put("OSVERS", C0468f.f1737a);
        hashMap.put("BUNDLE", C0468f.f1740d);
        hashMap.put("APPNAME", C0468f.f1741e);
        hashMap.put("APPVERS", C0468f.f1742f);
        hashMap.put("APPBUILD", String.valueOf(C0468f.f1743g));
        hashMap.put("CARRIER", C0468f.f1744h);
        hashMap.put("MAKE", C0468f.f1738b);
        hashMap.put("MODEL", C0468f.f1739c);
        hashMap.put("COPPA", String.valueOf(AdSettings.isChildDirected()));
        hashMap.put("SDK_CAPABILITY", C0460d.m1355b());
        return hashMap;
    }

    private void m1376f() {
        if (this.f1730c == null) {
            this.f1730c = C0463b.UNKNOWN;
        }
        switch (C04661.f1727a[this.f1730c.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                this.f1729b = AdPlacementType.INTERSTITIAL;
            case Zone.SECONDARY /*2*/:
                this.f1729b = AdPlacementType.BANNER;
            case Protocol.GGP /*3*/:
                this.f1729b = AdPlacementType.NATIVE;
            default:
                this.f1729b = AdPlacementType.UNKNOWN;
        }
    }

    public String m1377a() {
        return this.f1728a;
    }

    public C0463b m1378b() {
        return this.f1730c;
    }

    public AdSize m1379c() {
        return this.f1736i;
    }

    public int m1380d() {
        return this.f1735h;
    }

    public Map<String, String> m1381e() {
        Map<String, String> hashMap = new HashMap();
        m1374a(hashMap, "PLACEMENT_ID", this.f1728a);
        if (this.f1729b != AdPlacementType.UNKNOWN) {
            m1374a(hashMap, "PLACEMENT_TYPE", this.f1729b.toString().toLowerCase());
        }
        for (Entry entry : C0467e.m1375b(this.f1731d).entrySet()) {
            m1374a(hashMap, (String) entry.getKey(), (String) entry.getValue());
        }
        if (this.f1736i != null) {
            m1374a(hashMap, "WIDTH", String.valueOf(this.f1736i.getWidth()));
            m1374a(hashMap, "HEIGHT", String.valueOf(this.f1736i.getHeight()));
        }
        m1374a(hashMap, "ADAPTERS", C0439f.m1201a(this.f1729b));
        if (this.f1732e != null) {
            m1374a(hashMap, "TEMPLATE_ID", String.valueOf(this.f1732e.m1386a()));
        }
        if (this.f1734g != null) {
            m1374a(hashMap, "REQUEST_TYPE", String.valueOf(this.f1734g.m1354a()));
        }
        if (this.f1733f) {
            m1374a(hashMap, "TEST_MODE", "1");
        }
        if (this.f1735h != 0) {
            m1374a(hashMap, "NUM_ADS_REQUESTED", String.valueOf(this.f1735h));
        }
        m1374a(hashMap, "CLIENT_EVENTS", C0515c.m1514a());
        return hashMap;
    }
}
