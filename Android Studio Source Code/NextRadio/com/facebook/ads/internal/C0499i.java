package com.facebook.ads.internal;

import android.content.Context;
import android.os.Handler;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.NativeAd.MediaCacheFlag;
import com.facebook.ads.internal.adapters.AdAdapter;
import com.facebook.ads.internal.adapters.C0439f;
import com.facebook.ads.internal.adapters.C0448p;
import com.facebook.ads.internal.adapters.C0457q;
import com.facebook.ads.internal.dto.C0461a;
import com.facebook.ads.internal.dto.C0464c;
import com.facebook.ads.internal.dto.C0467e;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.server.C0505a;
import com.facebook.ads.internal.server.C0505a.C0485a;
import com.facebook.ads.internal.server.C0509d;
import com.facebook.ads.internal.util.C0435u;
import com.facebook.ads.internal.util.C0522g;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;

/* renamed from: com.facebook.ads.internal.i */
public class C0499i implements C0485a {
    private final Context f1841a;
    private final String f1842b;
    private final C0505a f1843c;
    private final C0469e f1844d;
    private final C0459c f1845e;
    private final AdSize f1846f;
    private final int f1847g;
    private boolean f1848h;
    private final Handler f1849i;
    private final Runnable f1850j;
    private C0426a f1851k;
    private C0464c f1852l;

    /* renamed from: com.facebook.ads.internal.i.a */
    public interface C0426a {
        void m1153a(C0458b c0458b);

        void m1154a(List<C0448p> list);
    }

    /* renamed from: com.facebook.ads.internal.i.1 */
    class C04971 implements C0457q {
        final /* synthetic */ List f1839a;
        final /* synthetic */ C0499i f1840b;

        C04971(C0499i c0499i, List list) {
            this.f1840b = c0499i;
            this.f1839a = list;
        }

        public void m1469a(C0448p c0448p) {
            this.f1839a.add(c0448p);
        }

        public void m1470a(C0448p c0448p, AdError adError) {
        }
    }

    /* renamed from: com.facebook.ads.internal.i.b */
    private static final class C0498b extends C0435u<C0499i> {
        public C0498b(C0499i c0499i) {
            super(c0499i);
        }

        public void run() {
            C0499i c0499i = (C0499i) m1184a();
            if (c0499i != null) {
                if (C0522g.m1539a(c0499i.f1841a)) {
                    c0499i.m1475a();
                } else {
                    c0499i.f1849i.postDelayed(c0499i.f1850j, 5000);
                }
            }
        }
    }

    public C0499i(Context context, String str, C0469e c0469e, AdSize adSize, C0459c c0459c, int i, EnumSet<MediaCacheFlag> enumSet) {
        this.f1841a = context;
        this.f1842b = str;
        this.f1844d = c0469e;
        this.f1846f = adSize;
        this.f1845e = c0459c;
        this.f1847g = i;
        this.f1843c = new C0505a();
        this.f1843c.m1500a((C0485a) this);
        this.f1848h = true;
        this.f1849i = new Handler();
        this.f1850j = new C0498b(this);
    }

    private List<C0448p> m1474d() {
        C0464c c0464c = this.f1852l;
        C0461a c = c0464c.m1364c();
        List<C0448p> arrayList = new ArrayList(c0464c.m1363b());
        for (C0461a c0461a = c; c0461a != null; c0461a = c0464c.m1364c()) {
            AdAdapter a = C0439f.m1200a(c0461a.f1705b, AdPlacementType.NATIVE);
            if (a != null && a.getPlacementType() == AdPlacementType.NATIVE) {
                Map hashMap = new HashMap();
                hashMap.put(MPDbAdapter.KEY_DATA, c0461a.f1706c);
                hashMap.put("definition", c0464c.m1361a());
                ((C0448p) a).m1225a(this.f1841a, new C04971(this, arrayList), hashMap);
            }
        }
        return arrayList;
    }

    public void m1475a() {
        this.f1843c.m1499a(this.f1841a, new C0467e(this.f1841a, this.f1842b, this.f1846f, this.f1844d, this.f1845e, this.f1847g, AdSettings.isTestMode(this.f1841a)));
    }

    public void m1476a(C0458b c0458b) {
        if (this.f1848h) {
            this.f1849i.postDelayed(this.f1850j, 1800000);
        }
        if (this.f1851k != null) {
            this.f1851k.m1153a(c0458b);
        }
    }

    public void m1477a(C0426a c0426a) {
        this.f1851k = c0426a;
    }

    public void m1478a(C0509d c0509d) {
        C0464c b = c0509d.m1507b();
        if (b == null) {
            throw new IllegalStateException("no placement in response");
        }
        if (this.f1848h) {
            long b2 = b.m1361a().m1367b();
            if (b2 == 0) {
                b2 = 1800000;
            }
            this.f1849i.postDelayed(this.f1850j, b2);
        }
        this.f1852l = b;
        List d = m1474d();
        if (this.f1851k == null) {
            return;
        }
        if (d.isEmpty()) {
            this.f1851k.m1153a(AdErrorType.NO_FILL.getAdErrorWrapper(Stomp.EMPTY));
        } else {
            this.f1851k.m1154a(d);
        }
    }

    public void m1479b() {
    }

    public void m1480c() {
        this.f1848h = false;
        this.f1849i.removeCallbacks(this.f1850j);
    }
}
