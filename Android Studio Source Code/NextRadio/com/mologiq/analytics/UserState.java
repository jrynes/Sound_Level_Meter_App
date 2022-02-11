package com.mologiq.analytics;

import android.content.Context;
import com.mologiq.analytics.MeanListData.MeanListData;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: com.mologiq.analytics.v */
final class UserState extends PersistentObject {
    private static final UserState f2235H;
    private String f2236A;
    private boolean f2237B;
    private boolean f2238C;
    private boolean f2239D;
    private int f2240E;
    private String f2241F;
    private String f2242G;
    private String f2243a;
    private String f2244b;
    private boolean f2245c;
    private String f2246d;
    private String f2247e;
    private String f2248f;
    private String f2249g;
    private String f2250h;
    private String f2251i;
    private String f2252j;
    private String f2253k;
    private String f2254l;
    private String f2255m;
    private String f2256n;
    private String f2257o;
    private UserState f2258p;
    private double f2259q;
    private double f2260r;
    private List<Integer> f2261s;
    private List<Integer> f2262t;
    private List<AppUrlInfo> f2263u;
    private Map<Integer, Integer> f2264v;
    private double f2265w;
    private long f2266x;
    private double f2267y;
    private double f2268z;

    /* renamed from: com.mologiq.analytics.v.a */
    class UserState {
        final /* synthetic */ UserState f2224a;
        private int f2225b;
        private List<Integer> f2226c;
        private int f2227d;
        private int f2228e;
        private String f2229f;
        private List<Integer> f2230g;
        private List<Integer> f2231h;
        private Map<Integer, Integer> f2232i;

        UserState(UserState userState) {
            this.f2224a = userState;
        }

        final int m1834a() {
            return this.f2225b;
        }

        final void m1835a(int i) {
            this.f2225b = i;
        }

        final void m1836a(String str) {
            this.f2229f = str;
        }

        final void m1837a(List<Integer> list) {
            this.f2226c = list;
        }

        final void m1838a(Map<Integer, Integer> map) {
            this.f2232i = map;
        }

        final List<Integer> m1839b() {
            return this.f2226c;
        }

        final void m1840b(int i) {
            this.f2227d = i;
        }

        final void m1841b(List<Integer> list) {
            this.f2230g = list;
        }

        final int m1842c() {
            return this.f2227d;
        }

        public final void m1843c(int i) {
            this.f2228e = i;
        }

        final void m1844c(List<Integer> list) {
            this.f2231h = list;
        }

        final String m1845d() {
            return this.f2229f;
        }

        final List<Integer> m1846e() {
            return this.f2230g;
        }

        final List<Integer> m1847f() {
            return this.f2231h;
        }

        final Map<Integer, Integer> m1848g() {
            return this.f2232i;
        }

        public final int m1849h() {
            return this.f2228e;
        }
    }

    /* renamed from: com.mologiq.analytics.v.b */
    class UserState {
        final /* synthetic */ UserState f2233a;
        private WifiState f2234b;

        UserState(UserState userState) {
            this.f2233a = userState;
        }

        final WifiState m1850a() {
            return this.f2234b;
        }

        final void m1851a(WifiState wifiState) {
            this.f2234b = wifiState;
        }
    }

    static {
        f2235H = new UserState("deviceEventRequest");
    }

    private UserState(String str) {
        super(str);
        this.f2245c = false;
        this.f2237B = true;
        this.f2238C = true;
        this.f2239D = true;
        this.f2240E = 1;
    }

    static UserState m1852b() {
        return f2235H;
    }

    private static int m1853e(Context context) {
        try {
            return DatabaseHandler.m1725a(context).m1743c();
        } catch (Throwable th) {
            Utils.m1924a(Utils.m1922a(th));
            return 0;
        }
    }

    private static int m1854f(Context context) {
        try {
            return DatabaseHandler.m1725a(context).m1742b();
        } catch (Throwable th) {
            Utils.m1924a(Utils.m1922a(th));
            return 0;
        }
    }

    public final boolean m1855A() {
        return this.f2238C;
    }

    public final boolean m1856B() {
        return this.f2239D;
    }

    public final int m1857C() {
        return this.f2240E;
    }

    public final String m1858D() {
        return this.f2241F;
    }

    public final String m1859E() {
        return this.f2242G;
    }

    protected final String m1860a() {
        JSONArray jSONArray;
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("product", this.f2243a);
        jSONObject2.put("androidadvertisingid", this.f2244b);
        jSONObject2.put("androidadvertisingoptout", this.f2245c);
        jSONObject2.put("androidid", this.f2246d);
        if (this.f2261s != null && this.f2261s.size() > 0) {
            jSONObject2.put("installedapps", new JSONArray(this.f2261s));
        }
        if (this.f2263u != null && this.f2263u.size() > 0) {
            jSONArray = new JSONArray();
            for (AppUrlInfo appUrlInfo : this.f2263u) {
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put(Name.MARK, appUrlInfo.f2126a);
                jSONObject3.put(SettingsJsonConstants.APP_URL_KEY, appUrlInfo.f2127b);
                jSONArray.put(jSONObject3);
            }
            jSONObject2.put("appurls", jSONArray);
        }
        if (this.f2264v != null && this.f2264v.size() > 0) {
            jSONArray = new JSONArray();
            for (Integer num : this.f2264v.keySet()) {
                jSONObject3 = new JSONObject();
                jSONObject3.put("classificationid", num);
                jSONObject3.put("count", this.f2264v.get(num));
                jSONArray.put(jSONObject3);
            }
            jSONObject2.put("classifications", jSONArray);
        }
        JSONObject jSONObject4 = new JSONObject();
        jSONObject4.put("os", this.f2247e);
        jSONObject4.put("model", this.f2248f);
        jSONObject4.put("device", this.f2249g);
        jSONObject4.put("manufacturer", this.f2250h);
        jSONObject4.put("brand", this.f2251i);
        jSONObject4.put("timezone", this.f2252j);
        jSONObject4.put("timezoneId", this.f2253k);
        jSONObject4.put("country_code", this.f2254l);
        jSONObject4.put("device_resolution", this.f2255m);
        jSONObject4.put("language", this.f2256n);
        jSONObject4.put("carrier", this.f2257o);
        jSONObject4.put("classificationid", this.f2240E);
        jSONObject4.put("isAppInstallCheckEnableInUserSetting", this.f2238C);
        jSONObject4.put("isLocationCheckEnableInUserSetting", this.f2239D);
        jSONObject4.put("isNetworkCheckEnableInUserSetting", this.f2237B);
        jSONObject2.put("DeviceInfo", jSONObject4);
        jSONObject.put("DeviceEvent", jSONObject2);
        jSONObject4 = new JSONObject();
        jSONObject4.put(locationTracking.latitude, this.f2259q);
        jSONObject4.put(locationTracking.longitude, this.f2260r);
        jSONObject2.put("LocationInfo", jSONObject4);
        return jSONObject.toString();
    }

    final void m1861a(double d) {
        this.f2259q = d;
    }

    public final void m1862a(int i) {
        this.f2240E = i;
    }

    final void m1863a(long j) {
        this.f2266x = j;
    }

    final void m1864a(UserState userState) {
        this.f2258p = userState;
    }

    protected final void m1865a(String str) {
        int i = 0;
        if (str != null && str.length() > 0) {
            JSONObject jSONObject;
            JSONObject jSONObject2 = new JSONObject(str);
            if (!jSONObject2.isNull("core")) {
                jSONObject = jSONObject2.getJSONObject("core");
                if (!jSONObject.isNull("DeviceEvent")) {
                    JSONArray jSONArray;
                    List arrayList;
                    int i2;
                    JSONObject jSONObject3 = jSONObject.getJSONObject("DeviceEvent");
                    if (!jSONObject3.isNull("product")) {
                        this.f2243a = jSONObject3.getString("product");
                    }
                    if (!jSONObject3.isNull("androidadvertisingid")) {
                        this.f2244b = jSONObject3.getString("androidadvertisingid");
                    }
                    if (!jSONObject3.isNull("androidadvertisingidout")) {
                        this.f2245c = jSONObject3.getBoolean("androidadvertisingidoptout");
                    }
                    if (!jSONObject3.isNull("androidid")) {
                        this.f2246d = jSONObject3.getString("androidid");
                    }
                    if (!jSONObject3.isNull("installedapps")) {
                        jSONArray = jSONObject3.getJSONArray("installedapps");
                        arrayList = new ArrayList();
                        for (i2 = 0; i2 < jSONArray.length(); i2++) {
                            arrayList.add(Integer.valueOf(jSONArray.getInt(i2)));
                        }
                        this.f2261s = arrayList;
                    }
                    if (!jSONObject3.isNull("appurls")) {
                        jSONArray = jSONObject3.getJSONArray("appurls");
                        arrayList = new ArrayList();
                        for (i2 = 0; i2 < jSONArray.length(); i2++) {
                            JSONObject jSONObject4 = jSONArray.getJSONObject(i2);
                            AppUrlInfo appUrlInfo = new AppUrlInfo();
                            appUrlInfo.f2126a = jSONObject4.getInt(Name.MARK);
                            appUrlInfo.f2127b = jSONObject4.getString(SettingsJsonConstants.APP_URL_KEY);
                            arrayList.add(appUrlInfo);
                        }
                        this.f2263u = arrayList;
                    }
                    if (!jSONObject3.isNull("classifications")) {
                        JSONArray jSONArray2 = jSONObject3.getJSONArray("classifications");
                        Map hashMap = new HashMap();
                        while (i < jSONArray2.length()) {
                            JSONObject jSONObject5 = jSONArray2.getJSONObject(i);
                            hashMap.put(Integer.valueOf(jSONObject5.getInt("classificationid")), Integer.valueOf(jSONObject5.getInt("count")));
                            i++;
                        }
                        this.f2264v = hashMap;
                    }
                    if (!jSONObject3.isNull("DeviceInfo")) {
                        jSONObject = jSONObject3.getJSONObject("DeviceInfo");
                        if (!jSONObject.isNull("os")) {
                            this.f2247e = jSONObject.getString("os");
                        }
                        if (!jSONObject.isNull("model")) {
                            this.f2248f = jSONObject.getString("model");
                        }
                        if (!jSONObject.isNull("device")) {
                            this.f2249g = jSONObject.getString("device");
                        }
                        if (!jSONObject.isNull("manufacturer")) {
                            this.f2250h = jSONObject.getString("manufacturer");
                        }
                        if (!jSONObject.isNull("brand")) {
                            this.f2251i = jSONObject.getString("brand");
                        }
                        if (!jSONObject.isNull("timezone")) {
                            this.f2252j = jSONObject.getString("timezone");
                        }
                        if (!jSONObject.isNull("timezoneId")) {
                            this.f2253k = jSONObject.getString("timezoneId");
                        }
                        if (!jSONObject.isNull("country_code")) {
                            this.f2254l = jSONObject.getString("country_code");
                        }
                        if (!jSONObject.isNull("device_resolution")) {
                            this.f2255m = jSONObject.getString("device_resolution");
                        }
                        if (!jSONObject.isNull("language")) {
                            this.f2256n = jSONObject.getString("language");
                        }
                        if (!jSONObject.isNull("carrier")) {
                            this.f2257o = jSONObject.getString("carrier");
                        }
                        if (!jSONObject.isNull("classificationid")) {
                            this.f2240E = jSONObject.getInt("classificationid");
                        }
                        if (!jSONObject.isNull("isNetworkCheckEnableInUserSetting")) {
                            this.f2237B = jSONObject.getBoolean("isNetworkCheckEnableInUserSetting");
                        }
                        if (!jSONObject.isNull("isLocationCheckEnableInUserSetting")) {
                            this.f2239D = jSONObject.getBoolean("isLocationCheckEnableInUserSetting");
                        }
                        if (!jSONObject.isNull("isAppInstallCheckEnableInUserSetting")) {
                            this.f2238C = jSONObject.getBoolean("isAppInstallCheckEnableInUserSetting");
                        }
                    }
                    if (!jSONObject3.isNull("LocationInfo")) {
                        jSONObject = jSONObject3.getJSONObject("LocationInfo");
                        if (!jSONObject.isNull(locationTracking.latitude)) {
                            this.f2259q = jSONObject.getDouble(locationTracking.latitude);
                        }
                        if (!jSONObject.isNull(locationTracking.longitude)) {
                            this.f2260r = jSONObject.getDouble(locationTracking.longitude);
                        }
                    }
                }
            }
            if (!jSONObject2.isNull("extra")) {
                jSONObject = jSONObject2.getJSONObject("extra");
                if (!jSONObject.isNull("DeviceEvent")) {
                    jSONObject = jSONObject.getJSONObject("DeviceEvent");
                    if (!jSONObject.isNull("NetworkInfo")) {
                        JSONObject jSONObject6 = jSONObject.getJSONObject("NetworkInfo");
                        UserState userState = new UserState(this);
                        if (!jSONObject6.isNull("wificurrent")) {
                            jSONObject6 = jSONObject6.getJSONObject("wificurrent");
                            WifiState wifiState = new WifiState();
                            wifiState.m1929a(jSONObject6.getString("ssid"));
                            wifiState.m1930b(jSONObject6.getString("bssid"));
                            userState.m1851a(wifiState);
                        }
                        this.f2258p = userState;
                    }
                    if (!jSONObject.isNull("LocationInfoExtra")) {
                        jSONObject = jSONObject.getJSONObject("LocationInfoExtra");
                        if (!jSONObject.isNull("locationAltitude")) {
                            this.f2265w = jSONObject.getDouble("locationAltitude");
                        }
                        if (!jSONObject.isNull("locationTimestamp")) {
                            this.f2266x = jSONObject.getLong("locationTimestamp");
                        }
                        if (!jSONObject.isNull("locationAccuracy")) {
                            this.f2267y = jSONObject.getDouble("locationAccuracy");
                        }
                        if (!jSONObject.isNull("locationSpeed")) {
                            this.f2268z = jSONObject.getDouble("locationSpeed");
                        }
                    }
                }
            }
        }
    }

    final void m1866a(List<Integer> list) {
        this.f2261s = list;
    }

    final void m1867a(boolean z) {
        this.f2245c = z;
    }

    final void m1868b(double d) {
        this.f2260r = d;
    }

    final void m1869b(String str) {
        this.f2243a = str;
    }

    final void m1870b(List<Integer> list) {
        this.f2262t = list;
    }

    public final void m1871b(boolean z) {
        this.f2237B = z;
    }

    final String m1872c() {
        return this.f2259q + Stomp.COMMA + this.f2260r;
    }

    final void m1873c(double d) {
        this.f2265w = d;
    }

    final void m1874c(Context context) {
        JSONObject jSONObject = new JSONObject();
        String a = m1860a();
        if (a != null && a.length() > 0) {
            jSONObject.put("core", new JSONObject(a));
        }
        JSONObject jSONObject2 = new JSONObject();
        if (this.f2258p != null) {
            UserState userState = this.f2258p;
            JSONObject jSONObject3 = new JSONObject();
            if (userState.m1850a() != null) {
                JSONObject jSONObject4 = new JSONObject();
                jSONObject4.put("ssid", userState.m1850a().m1928a());
                jSONObject3.put("wificurrent", jSONObject4);
            }
            jSONObject2.put("NetworkInfo", jSONObject3);
        }
        JSONObject jSONObject5 = new JSONObject();
        jSONObject5.put("locationAltitude", this.f2265w);
        jSONObject5.put("locationTimestamp", this.f2266x);
        jSONObject5.put("locationAccuracy", this.f2267y);
        jSONObject5.put("locationSpeed", this.f2268z);
        jSONObject2.put("LocationInfoExtra", jSONObject5);
        a = jSONObject2.toString();
        if (a != null && a.length() > 0) {
            jSONObject.put("extra", new JSONObject(a));
        }
        try {
            DatabaseHandler.m1725a(context).m1741a("deviceEventRequest", jSONObject.toString());
        } catch (Throwable th) {
            Utils.m1924a(Utils.m1922a(th));
        }
    }

    final void m1875c(String str) {
        this.f2247e = str;
    }

    public final void m1876c(boolean z) {
        this.f2238C = z;
    }

    final UserState m1877d(Context context) {
        try {
            UserSettings d = UserSettings.m1806d(context);
            UserState userState = new UserState(this);
            userState.m1835a(d.m1832t());
            if (d.m1832t() > 1) {
                List<AppInstallCampaign> s = UserSettings.m1806d(context).m1831s();
                List arrayList = new ArrayList();
                if (s != null) {
                    for (AppInstallCampaign appInstallCampaign : s) {
                        arrayList.add(appInstallCampaign.f2121a);
                    }
                }
                userState.m1841b(arrayList);
                userState.m1840b(UserState.m1853e(context));
                userState.m1843c(UserState.m1854f(context));
                DatabaseHandler a = DatabaseHandler.m1725a(context);
                MeanListData e = a.m1745e();
                Utils.m1924a("===== Total Number of Category Id:= " + d.m1833u());
                if (e != null) {
                    userState.m1836a(e.m1759a());
                    if (d.m1822j() && d.m1833u() <= a.m1744d()) {
                        userState.m1837a(a.m1747g());
                    }
                }
            }
            if (d.m1832t() == 3) {
                userState.m1838a(this.f2264v);
            }
            if (d.m1832t() >= 4) {
                userState.m1844c(this.f2261s);
            }
            return userState;
        } catch (Throwable th) {
            Utils.m1924a(Utils.m1922a(th));
            return null;
        }
    }

    final String m1878d() {
        return this.f2243a;
    }

    final void m1879d(double d) {
        this.f2267y = d;
    }

    final void m1880d(String str) {
        this.f2248f = str;
    }

    public final void m1881d(boolean z) {
        this.f2239D = z;
    }

    final String m1882e() {
        return this.f2247e;
    }

    final void m1883e(double d) {
        this.f2268z = d;
    }

    final void m1884e(String str) {
        this.f2249g = str;
    }

    final String m1885f() {
        return this.f2248f;
    }

    final void m1886f(String str) {
        this.f2236A = str;
    }

    final String m1887g() {
        return this.f2249g;
    }

    final void m1888g(String str) {
        this.f2251i = str;
    }

    final String m1889h() {
        return this.f2236A;
    }

    final void m1890h(String str) {
        this.f2256n = str;
    }

    final String m1891i() {
        return this.f2251i;
    }

    final void m1892i(String str) {
        this.f2244b = str;
    }

    final String m1893j() {
        return this.f2256n;
    }

    final void m1894j(String str) {
        this.f2246d = str;
    }

    final double m1895k() {
        return this.f2259q;
    }

    final void m1896k(String str) {
        this.f2250h = str;
    }

    final double m1897l() {
        return this.f2260r;
    }

    final void m1898l(String str) {
        this.f2252j = str;
    }

    final String m1899m() {
        return this.f2244b;
    }

    final void m1900m(String str) {
        this.f2253k = str;
    }

    final void m1901n(String str) {
        this.f2254l = str;
    }

    final boolean m1902n() {
        return this.f2245c;
    }

    final String m1903o() {
        return this.f2246d;
    }

    final void m1904o(String str) {
        this.f2255m = str;
    }

    final String m1905p() {
        return this.f2252j;
    }

    final void m1906p(String str) {
        this.f2257o = str;
    }

    final String m1907q() {
        return this.f2254l;
    }

    public final void m1908q(String str) {
        this.f2241F = str;
    }

    final UserState m1909r() {
        return this.f2258p;
    }

    public final void m1910r(String str) {
        this.f2242G = str;
    }

    final double m1911s() {
        return this.f2265w;
    }

    final long m1912t() {
        return this.f2266x;
    }

    final double m1913u() {
        return this.f2267y;
    }

    final double m1914v() {
        return this.f2268z;
    }

    final List<Integer> m1915w() {
        return this.f2261s;
    }

    final List<Integer> m1916x() {
        return this.f2262t;
    }

    final String m1917y() {
        return this.f2257o;
    }

    public final boolean m1918z() {
        return this.f2237B;
    }
}
