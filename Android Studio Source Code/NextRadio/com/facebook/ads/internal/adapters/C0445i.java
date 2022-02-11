package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.action.C0428a;
import com.facebook.ads.internal.action.C0429b;
import com.facebook.ads.internal.dto.C0465d;
import com.facebook.ads.internal.util.C0514b;
import com.facebook.ads.internal.util.C0514b.C0513a;
import com.facebook.ads.internal.util.C0515c;
import com.facebook.ads.internal.util.C0519f;
import com.facebook.ads.internal.util.C0523h;
import com.facebook.ads.internal.view.C0542a;
import com.facebook.ads.internal.view.C0542a.C0442a;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.adapters.i */
public class C0445i extends BannerAdapter {
    private static final String f1594a;
    private C0542a f1595b;
    private C0452m f1596c;
    private BannerAdapterListener f1597d;
    private Map<String, Object> f1598e;
    private C0449k f1599f;
    private Context f1600g;
    private long f1601h;
    private C0513a f1602i;

    /* renamed from: com.facebook.ads.internal.adapters.i.1 */
    class C04431 implements C0442a {
        final /* synthetic */ C0450l f1591a;
        final /* synthetic */ C0445i f1592b;

        C04431(C0445i c0445i, C0450l c0450l) {
            this.f1592b = c0445i;
            this.f1591a = c0450l;
        }

        public void m1208a() {
            this.f1592b.f1596c.m1300c();
        }

        public void m1209a(int i) {
            if (i == 0 && this.f1592b.f1601h > 0 && this.f1592b.f1602i != null) {
                C0515c.m1515a(C0514b.m1511a(this.f1592b.f1601h, this.f1592b.f1602i, this.f1591a.m1290i()));
                this.f1592b.f1601h = 0;
                this.f1592b.f1602i = null;
            }
        }

        public void m1210a(String str) {
            if (this.f1592b.f1597d != null) {
                this.f1592b.f1597d.onBannerAdClicked(this.f1592b);
            }
            C0428a a = C0429b.m1166a(this.f1592b.f1600g, Uri.parse(str));
            if (a != null) {
                try {
                    this.f1592b.f1602i = a.m1163a();
                    this.f1592b.f1601h = System.currentTimeMillis();
                    a.m1165b();
                } catch (Throwable e) {
                    Log.e(C0445i.f1594a, "Error executing action", e);
                }
            }
        }

        public void m1211b() {
            this.f1592b.onViewableImpression();
        }
    }

    /* renamed from: com.facebook.ads.internal.adapters.i.2 */
    class C04442 extends C0417c {
        final /* synthetic */ C0445i f1593a;

        C04442(C0445i c0445i) {
            this.f1593a = c0445i;
        }

        public void m1212d() {
            if (this.f1593a.f1597d != null) {
                this.f1593a.f1597d.onBannerLoggingImpression(this.f1593a);
            }
        }
    }

    static {
        f1594a = C0445i.class.getSimpleName();
    }

    private void m1217a(C0465d c0465d) {
        this.f1601h = 0;
        this.f1602i = null;
        C0450l a = C0450l.m1279a((JSONObject) this.f1598e.get(MPDbAdapter.KEY_DATA));
        if (C0519f.m1525a(this.f1600g, (C0432a) a)) {
            this.f1597d.onBannerError(this, AdError.NO_FILL);
            return;
        }
        this.f1595b = new C0542a(this.f1600g, new C04431(this, a), c0465d.m1370e());
        this.f1595b.m1579a(c0465d.m1371f(), c0465d.m1372g());
        this.f1596c = new C0452m(this.f1600g, this.f1595b, new C04442(this));
        this.f1596c.m1298a(a);
        this.f1595b.loadDataWithBaseURL(C0523h.m1543a(), a.m1285d(), WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
        if (this.f1597d != null) {
            this.f1597d.onBannerAdLoaded(this, this.f1595b);
        }
    }

    public void loadBannerAd(Context context, AdSize adSize, BannerAdapterListener bannerAdapterListener, Map<String, Object> map) {
        this.f1600g = context;
        this.f1597d = bannerAdapterListener;
        this.f1598e = map;
        m1217a((C0465d) map.get("definition"));
    }

    public void onDestroy() {
        if (this.f1595b != null) {
            C0523h.m1547a(this.f1595b);
            this.f1595b.destroy();
            this.f1595b = null;
        }
        if (this.f1599f != null) {
            this.f1599f.onDestroy();
            this.f1599f = null;
        }
    }

    public void onViewableImpression() {
        if (this.f1596c != null) {
            this.f1596c.m1180a();
        } else if (this.f1599f != null) {
            Map hashMap = new HashMap();
            hashMap.put("mil", Boolean.valueOf(false));
            this.f1599f.m1253a(hashMap);
        }
    }
}
