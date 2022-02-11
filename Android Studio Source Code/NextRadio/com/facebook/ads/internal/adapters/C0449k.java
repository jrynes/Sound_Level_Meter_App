package com.facebook.ads.internal.adapters;

import android.content.Context;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd.Image;
import com.facebook.ads.NativeAd.Rating;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.util.C0514b;
import com.facebook.ads.internal.util.C0519f;
import java.util.Map;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.adapters.k */
public class C0449k extends C0448p {
    private Context f1615a;
    private C0455n f1616b;

    public void m1251a(int i) {
        if (this.f1616b != null) {
            this.f1616b.m1312a(i);
        }
    }

    public void m1252a(Context context, C0457q c0457q, Map<String, Object> map) {
        this.f1616b = C0455n.m1305a((JSONObject) map.get(MPDbAdapter.KEY_DATA));
        this.f1615a = context;
        if (this.f1616b == null || C0519f.m1525a(context, this.f1616b)) {
            c0457q.m1351a(this, AdError.NO_FILL);
            return;
        }
        if (c0457q != null) {
            c0457q.m1350a(this);
        }
        C0514b.f1895a = this.f1616b.m1335v();
    }

    public void m1253a(Map<String, Object> map) {
        if (this.f1616b != null) {
            this.f1616b.m1314a((Map) map);
        }
    }

    public boolean m1254a() {
        return m1277w() && this.f1616b.m1328o();
    }

    public void m1255b(Map<String, Object> map) {
        if (this.f1616b != null) {
            this.f1616b.m1313a(this.f1615a, (Map) map);
        }
    }

    public boolean m1256b() {
        return this.f1616b.m1329p();
    }

    public boolean m1257c() {
        return this.f1616b.m1337x();
    }

    public boolean m1258d() {
        return this.f1616b.m1330q();
    }

    public int m1259e() {
        return this.f1616b.m1331r();
    }

    public int m1260f() {
        return this.f1616b.m1338y();
    }

    public int m1261g() {
        return this.f1616b.m1339z();
    }

    public Image m1262h() {
        return !m1277w() ? null : this.f1616b.m1317d();
    }

    public Image m1263i() {
        return !m1277w() ? null : this.f1616b.m1318e();
    }

    public NativeAdViewAttributes m1264j() {
        return !m1277w() ? null : this.f1616b.m1336w();
    }

    public String m1265k() {
        return !m1277w() ? null : this.f1616b.m1319f();
    }

    public String m1266l() {
        return !m1277w() ? null : this.f1616b.m1320g();
    }

    public String m1267m() {
        return !m1277w() ? null : this.f1616b.m1321h();
    }

    public String m1268n() {
        return !m1277w() ? null : this.f1616b.m1322i();
    }

    public String m1269o() {
        return !m1277w() ? null : this.f1616b.m1323j();
    }

    public void onDestroy() {
    }

    public Rating m1270p() {
        return !m1277w() ? null : this.f1616b.m1324k();
    }

    public String m1271q() {
        return !m1277w() ? null : this.f1616b.m1332s();
    }

    public Image m1272r() {
        return !m1277w() ? null : this.f1616b.m1333t();
    }

    public String m1273s() {
        return !m1277w() ? null : this.f1616b.m1334u();
    }

    public String m1274t() {
        return !m1277w() ? null : this.f1616b.m1325l();
    }

    public String m1275u() {
        return !m1277w() ? null : this.f1616b.m1326m();
    }

    public String m1276v() {
        return !m1277w() ? null : this.f1616b.m1327n();
    }

    public boolean m1277w() {
        return this.f1616b != null;
    }
}
