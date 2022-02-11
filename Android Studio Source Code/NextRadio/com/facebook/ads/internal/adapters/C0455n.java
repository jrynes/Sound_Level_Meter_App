package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.admarvel.android.ads.Constants;
import com.facebook.ads.NativeAd.Image;
import com.facebook.ads.NativeAd.Rating;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.action.C0428a;
import com.facebook.ads.internal.action.C0429b;
import com.facebook.ads.internal.util.C0514b;
import com.facebook.ads.internal.util.C0514b.C0513a;
import com.facebook.ads.internal.util.C0515c;
import com.facebook.ads.internal.util.C0518e;
import com.facebook.ads.internal.util.C0519f;
import com.facebook.ads.internal.util.C0522g;
import com.facebook.ads.internal.util.C0531o;
import com.facebook.ads.internal.util.C0536s;
import com.google.android.gms.analytics.ecommerce.Promotion;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.adapters.n */
public class C0455n implements C0432a {
    private static final String f1636a;
    private final String f1637A;
    private final Image f1638B;
    private final String f1639C;
    private final String f1640D;
    private final C0454a f1641E;
    private final NativeAdViewAttributes f1642F;
    private final String f1643G;
    private boolean f1644H;
    private boolean f1645I;
    private boolean f1646J;
    private long f1647K;
    private C0513a f1648L;
    private final Uri f1649b;
    private final String f1650c;
    private final String f1651d;
    private final String f1652e;
    private final String f1653f;
    private final String f1654g;
    private final Image f1655h;
    private final Image f1656i;
    private final Rating f1657j;
    private final String f1658k;
    private final String f1659l;
    private final String f1660m;
    private final String f1661n;
    private final C0518e f1662o;
    private final String f1663p;
    private final Collection<String> f1664q;
    private final boolean f1665r;
    private final boolean f1666s;
    private final boolean f1667t;
    private final int f1668u;
    private final int f1669v;
    private final int f1670w;
    private final int f1671x;
    private final String f1672y;
    private final String f1673z;

    /* renamed from: com.facebook.ads.internal.adapters.n.1 */
    class C04531 implements Runnable {
        final /* synthetic */ Map f1632a;
        final /* synthetic */ Map f1633b;
        final /* synthetic */ C0455n f1634c;

        C04531(C0455n c0455n, Map map, Map map2) {
            this.f1634c = c0455n;
            this.f1632a = map;
            this.f1633b = map2;
        }

        public void run() {
            new C0531o(this.f1632a, this.f1633b).execute(new String[]{this.f1634c.f1659l});
        }
    }

    /* renamed from: com.facebook.ads.internal.adapters.n.a */
    private static class C0454a {
        Map<String, List<String>> f1635a;

        C0454a(JSONArray jSONArray) {
            this.f1635a = new HashMap();
            if (jSONArray != null) {
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    String optString = optJSONObject.optString(Send.TYPE);
                    if (!C0536s.m1572a(optString)) {
                        JSONArray optJSONArray = optJSONObject.optJSONArray("urls");
                        if (optJSONArray != null) {
                            List arrayList = new ArrayList(optJSONArray.length());
                            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                                arrayList.add(optJSONArray.optString(i2));
                            }
                            this.f1635a.put(optString, arrayList);
                        }
                    }
                }
            }
        }

        void m1302a(String str) {
            List<String> list = (List) this.f1635a.get(str);
            if (list != null && !list.isEmpty()) {
                for (String str2 : list) {
                    new C0531o().execute(new String[]{str2});
                }
            }
        }
    }

    static {
        f1636a = C0455n.class.getSimpleName();
    }

    private C0455n(Uri uri, String str, String str2, String str3, String str4, String str5, Image image, Image image2, Rating rating, String str6, String str7, String str8, C0518e c0518e, String str9, Collection<String> collection, boolean z, Image image3, String str10, String str11, C0454a c0454a, String str12, boolean z2, boolean z3, int i, int i2, String str13, String str14, String str15, int i3, int i4, NativeAdViewAttributes nativeAdViewAttributes) {
        this.f1647K = 0;
        this.f1648L = null;
        this.f1649b = uri;
        this.f1650c = str;
        this.f1651d = str2;
        this.f1652e = str3;
        this.f1653f = str4;
        this.f1654g = str5;
        this.f1655h = image;
        this.f1656i = image2;
        this.f1657j = rating;
        this.f1658k = str6;
        this.f1660m = str7;
        this.f1661n = str8;
        this.f1662o = c0518e;
        this.f1663p = str9;
        this.f1664q = collection;
        this.f1665r = z;
        this.f1666s = z2;
        this.f1667t = z3;
        this.f1638B = image3;
        this.f1639C = str10;
        this.f1640D = str11;
        this.f1641E = c0454a;
        this.f1659l = str12;
        this.f1668u = i;
        this.f1669v = i2;
        this.f1670w = i3;
        this.f1671x = i4;
        this.f1672y = str13;
        this.f1673z = str14;
        this.f1637A = str15;
        this.f1642F = nativeAdViewAttributes;
        this.f1643G = UUID.randomUUID().toString();
    }

    private void m1303A() {
        if (!this.f1646J) {
            new C0531o().execute(new String[]{this.f1661n});
            this.f1646J = true;
        }
    }

    private boolean m1304B() {
        return (this.f1650c == null || this.f1650c.length() <= 0 || this.f1653f == null || this.f1653f.length() <= 0 || this.f1655h == null || this.f1656i == null) ? false : true;
    }

    public static C0455n m1305a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        NativeAdViewAttributes nativeAdViewAttributes;
        JSONArray jSONArray;
        Uri parse = Uri.parse(jSONObject.optString("fbad_command"));
        String optString = jSONObject.optString(SettingsJsonConstants.PROMPT_TITLE_KEY);
        String optString2 = jSONObject.optString("subtitle");
        String optString3 = jSONObject.optString("body");
        String optString4 = jSONObject.optString("call_to_action");
        String optString5 = jSONObject.optString("social_context");
        Image fromJSONObject = Image.fromJSONObject(jSONObject.optJSONObject(SettingsJsonConstants.APP_ICON_KEY));
        Image fromJSONObject2 = Image.fromJSONObject(jSONObject.optJSONObject(Constants.NATIVE_AD_IMAGE_ELEMENT));
        Rating fromJSONObject3 = Rating.fromJSONObject(jSONObject.optJSONObject("star_rating"));
        String optString6 = jSONObject.optString("impression_report_url");
        String optString7 = jSONObject.optString("native_view_report_url");
        String optString8 = jSONObject.optString("click_report_url");
        String optString9 = jSONObject.optString("used_report_url");
        boolean optBoolean = jSONObject.optBoolean("manual_imp");
        boolean optBoolean2 = jSONObject.optBoolean("enable_view_log");
        boolean optBoolean3 = jSONObject.optBoolean("enable_snapshot_log");
        int optInt = jSONObject.optInt("snapshot_log_delay_second", 4);
        int optInt2 = jSONObject.optInt("snapshot_compress_quality", 0);
        int optInt3 = jSONObject.optInt("viewability_check_initial_delay", 0);
        int optInt4 = jSONObject.optInt("viewability_check_interval", ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH);
        Image image = null;
        JSONObject optJSONObject = jSONObject.optJSONObject("ad_choices_icon");
        JSONObject optJSONObject2 = jSONObject.optJSONObject("native_ui_config");
        if (optJSONObject2 == null) {
            nativeAdViewAttributes = null;
        } else {
            NativeAdViewAttributes nativeAdViewAttributes2 = new NativeAdViewAttributes(optJSONObject2);
        }
        if (optJSONObject != null) {
            image = Image.fromJSONObject(optJSONObject);
        }
        String optString10 = jSONObject.optString("ad_choices_link_url");
        String optString11 = jSONObject.optString("request_id");
        C0518e a = C0518e.m1523a(jSONObject.optString("invalidation_behavior"));
        String optString12 = jSONObject.optString("invalidation_report_url");
        try {
            jSONArray = new JSONArray(jSONObject.optString("detection_strings"));
        } catch (JSONException e) {
            e.printStackTrace();
            jSONArray = null;
        }
        C0455n c0455n = new C0455n(parse, optString, optString2, optString3, optString4, optString5, fromJSONObject, fromJSONObject2, fromJSONObject3, optString6, optString8, optString9, a, optString12, C0519f.m1524a(jSONArray), optBoolean, image, optString10, optString11, new C0454a(jSONObject.optJSONArray(Constants.NATIVE_AD_TRACKERS_ELEMENT)), optString7, optBoolean2, optBoolean3, optInt, optInt2, jSONObject.optString("video_url"), jSONObject.optString("video_play_report_url"), jSONObject.optString("video_time_report_url"), optInt3, optInt4, nativeAdViewAttributes);
        return !c0455n.m1304B() ? null : c0455n;
    }

    private void m1307a(String str, Map<String, String> map, Map<String, Object> map2) {
        if (map2.containsKey(str)) {
            map.put(str, String.valueOf(map2.get(str)));
        }
    }

    private void m1308a(Map<String, String> map, Map<String, Object> map2) {
        if (map2.containsKey("mil")) {
            boolean booleanValue = ((Boolean) map2.get("mil")).booleanValue();
            map2.remove("mil");
            if (!booleanValue) {
                return;
            }
        }
        map.put("mil", String.valueOf(true));
    }

    private Map<String, String> m1309b(Map<String, Object> map) {
        Map<String, String> hashMap = new HashMap();
        if (map.containsKey(Promotion.ACTION_VIEW)) {
            hashMap.put(Promotion.ACTION_VIEW, String.valueOf(map.get(Promotion.ACTION_VIEW)));
        }
        if (map.containsKey("snapshot")) {
            hashMap.put("snapshot", String.valueOf(map.get("snapshot")));
        }
        return hashMap;
    }

    private void m1310b(Map<String, String> map, Map<String, Object> map2) {
        m1307a("nti", map, map2);
        m1307a("nhs", map, map2);
        m1307a("nmv", map, map2);
    }

    public C0518e m1311a() {
        return this.f1662o;
    }

    public void m1312a(int i) {
        if (i == 0 && this.f1647K > 0 && this.f1648L != null) {
            C0515c.m1515a(C0514b.m1511a(this.f1647K, this.f1648L, this.f1640D));
            this.f1647K = 0;
            this.f1648L = null;
        }
    }

    public void m1313a(Context context, Map<String, Object> map) {
        if (!this.f1645I) {
            Map hashMap = new HashMap();
            if (map != null) {
                m1308a(hashMap, (Map) map);
                m1310b(hashMap, map);
                hashMap.put("touch", C0522g.m1531a((Map) map));
            }
            new C0531o(hashMap).execute(new String[]{this.f1660m});
            this.f1641E.m1302a(Promotion.ACTION_CLICK);
            this.f1645I = true;
            C0522g.m1535a(context, "Click logged");
        }
        C0428a a = C0429b.m1166a(context, this.f1649b);
        if (a != null) {
            try {
                this.f1647K = System.currentTimeMillis();
                this.f1648L = a.m1163a();
                a.m1165b();
            } catch (Throwable e) {
                Log.e(f1636a, "Error executing action", e);
            }
        }
    }

    public void m1314a(Map<String, Object> map) {
        if (!this.f1644H) {
            Map hashMap = new HashMap();
            if (map != null) {
                m1308a(hashMap, (Map) map);
                m1310b(hashMap, map);
            }
            new C0531o(hashMap).execute(new String[]{this.f1658k});
            if (m1329p() || m1330q()) {
                try {
                    new Handler().postDelayed(new C04531(this, hashMap, m1309b(map)), (long) (this.f1668u * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH));
                } catch (Exception e) {
                }
            }
            this.f1641E.m1302a("impression");
            this.f1644H = true;
        }
    }

    public String m1315b() {
        return this.f1663p;
    }

    public Collection<String> m1316c() {
        return this.f1664q;
    }

    public Image m1317d() {
        return this.f1655h;
    }

    public Image m1318e() {
        return this.f1656i;
    }

    public String m1319f() {
        m1303A();
        return this.f1650c;
    }

    public String m1320g() {
        m1303A();
        return this.f1651d;
    }

    public String m1321h() {
        m1303A();
        return this.f1652e;
    }

    public String m1322i() {
        m1303A();
        return this.f1653f;
    }

    public String m1323j() {
        m1303A();
        return this.f1654g;
    }

    public Rating m1324k() {
        m1303A();
        return this.f1657j;
    }

    public String m1325l() {
        return this.f1672y;
    }

    public String m1326m() {
        return this.f1673z;
    }

    public String m1327n() {
        return this.f1637A;
    }

    public boolean m1328o() {
        return this.f1665r;
    }

    public boolean m1329p() {
        return this.f1666s;
    }

    public boolean m1330q() {
        return this.f1667t;
    }

    public int m1331r() {
        return (this.f1669v < 0 || this.f1669v > 100) ? 0 : this.f1669v;
    }

    public String m1332s() {
        return this.f1643G;
    }

    public Image m1333t() {
        return this.f1638B;
    }

    public String m1334u() {
        return this.f1639C;
    }

    public String m1335v() {
        return this.f1640D;
    }

    public NativeAdViewAttributes m1336w() {
        return this.f1642F;
    }

    public boolean m1337x() {
        return this.f1642F != null;
    }

    public int m1338y() {
        return this.f1670w;
    }

    public int m1339z() {
        return this.f1671x;
    }
}
