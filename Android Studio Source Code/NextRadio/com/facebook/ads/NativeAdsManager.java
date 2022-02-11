package com.facebook.ads;

import android.content.Context;
import com.facebook.ads.NativeAd.MediaCacheFlag;
import com.facebook.ads.internal.C0458b;
import com.facebook.ads.internal.C0459c;
import com.facebook.ads.internal.C0469e;
import com.facebook.ads.internal.C0499i;
import com.facebook.ads.internal.C0499i.C0426a;
import com.facebook.ads.internal.adapters.C0448p;
import com.facebook.ads.internal.util.C0410l;
import com.facebook.ads.internal.util.C0528m;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class NativeAdsManager {
    private static final C0459c f1539a;
    private final Context f1540b;
    private final String f1541c;
    private final int f1542d;
    private final List<NativeAd> f1543e;
    private int f1544f;
    private Listener f1545g;
    private C0499i f1546h;
    private boolean f1547i;
    private boolean f1548j;

    /* renamed from: com.facebook.ads.NativeAdsManager.1 */
    class C04271 implements C0426a {
        final /* synthetic */ EnumSet f1537a;
        final /* synthetic */ NativeAdsManager f1538b;

        /* renamed from: com.facebook.ads.NativeAdsManager.1.1 */
        class C04251 implements C0410l {
            final /* synthetic */ NativeAd[] f1532a;
            final /* synthetic */ int f1533b;
            final /* synthetic */ List f1534c;
            final /* synthetic */ int[] f1535d;
            final /* synthetic */ C04271 f1536e;

            C04251(C04271 c04271, NativeAd[] nativeAdArr, int i, List list, int[] iArr) {
                this.f1536e = c04271;
                this.f1532a = nativeAdArr;
                this.f1533b = i;
                this.f1534c = list;
                this.f1535d = iArr;
            }

            public void m1152a() {
                this.f1532a[this.f1533b] = new NativeAd(this.f1536e.f1538b.f1540b, (C0448p) this.f1534c.get(this.f1533b), null);
                int[] iArr = this.f1535d;
                iArr[0] = iArr[0] + 1;
                if (this.f1535d[0] == this.f1534c.size()) {
                    this.f1536e.f1538b.f1548j = true;
                    this.f1536e.f1538b.f1543e.clear();
                    this.f1536e.f1538b.f1544f = 0;
                    for (Object obj : this.f1532a) {
                        if (obj != null) {
                            this.f1536e.f1538b.f1543e.add(obj);
                        }
                    }
                    if (this.f1536e.f1538b.f1545g != null) {
                        this.f1536e.f1538b.f1545g.onAdsLoaded();
                    }
                }
            }
        }

        C04271(NativeAdsManager nativeAdsManager, EnumSet enumSet) {
            this.f1538b = nativeAdsManager;
            this.f1537a = enumSet;
        }

        public void m1155a(C0458b c0458b) {
            if (this.f1538b.f1545g != null) {
                this.f1538b.f1545g.onAdError(c0458b.m1353b());
            }
        }

        public void m1156a(List<C0448p> list) {
            int i = 0;
            NativeAd[] nativeAdArr = new NativeAd[list.size()];
            int[] iArr = new int[]{0};
            while (i < list.size()) {
                C0448p c0448p = (C0448p) list.get(i);
                List arrayList = new ArrayList(2);
                if (this.f1537a.contains(MediaCacheFlag.ICON) && c0448p.m1235h() != null) {
                    arrayList.add(c0448p.m1235h().getUrl());
                }
                if (this.f1537a.contains(MediaCacheFlag.IMAGE) && c0448p.m1236i() != null) {
                    arrayList.add(c0448p.m1236i().getUrl());
                }
                C0528m.m1560a(this.f1538b.f1540b, arrayList, new C04251(this, nativeAdArr, i, list, iArr));
                i++;
            }
        }
    }

    public interface Listener {
        void onAdError(AdError adError);

        void onAdsLoaded();
    }

    static {
        f1539a = C0459c.ADS;
    }

    public NativeAdsManager(Context context, String str, int i) {
        this.f1540b = context;
        this.f1541c = str;
        this.f1542d = Math.max(i, 0);
        this.f1543e = new ArrayList(i);
        this.f1544f = -1;
        this.f1548j = false;
        this.f1547i = false;
    }

    public void disableAutoRefresh() {
        this.f1547i = true;
        if (this.f1546h != null) {
            this.f1546h.m1480c();
        }
    }

    public int getUniqueNativeAdCount() {
        return this.f1543e.size();
    }

    public boolean isLoaded() {
        return this.f1548j;
    }

    public void loadAds() {
        loadAds(EnumSet.of(MediaCacheFlag.NONE));
    }

    public void loadAds(EnumSet<MediaCacheFlag> enumSet) {
        C0469e c0469e = C0469e.NATIVE_UNKNOWN;
        int i = this.f1542d;
        if (this.f1546h != null) {
            this.f1546h.m1479b();
        }
        this.f1546h = new C0499i(this.f1540b, this.f1541c, c0469e, null, f1539a, i, enumSet);
        if (this.f1547i) {
            this.f1546h.m1480c();
        }
        this.f1546h.m1477a(new C04271(this, enumSet));
        this.f1546h.m1475a();
    }

    public NativeAd nextNativeAd() {
        if (this.f1543e.size() == 0) {
            return null;
        }
        int i = this.f1544f;
        this.f1544f = i + 1;
        NativeAd nativeAd = (NativeAd) this.f1543e.get(i % this.f1543e.size());
        return i >= this.f1543e.size() ? new NativeAd(nativeAd) : nativeAd;
    }

    public void setListener(Listener listener) {
        this.f1545g = listener;
    }
}
