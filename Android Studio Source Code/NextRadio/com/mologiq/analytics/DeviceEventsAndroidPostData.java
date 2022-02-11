package com.mologiq.analytics;

import android.content.Context;
import com.mologiq.analytics.UserState.UserState;
import com.nextradioapp.androidSDK.data.schema.Tables.stations;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: com.mologiq.analytics.h */
final class DeviceEventsAndroidPostData {
    private String f2134a;
    private String f2135b;
    private String f2136c;
    private UserState f2137d;

    DeviceEventsAndroidPostData() {
    }

    final String m1748a(Context context) {
        UserSettings d = UserSettings.m1806d(context);
        JSONObject jSONObject = new JSONObject();
        if (d.m1832t() == 0) {
            return Stomp.EMPTY;
        }
        if (this.f2137d == null) {
            return Stomp.EMPTY;
        }
        if (this.f2137d.m1878d() == null || this.f2137d.m1878d().length() <= 0) {
            return Stomp.EMPTY;
        }
        if (this.f2136c == null || this.f2136c.length() <= 0) {
            return Stomp.EMPTY;
        }
        if ((this.f2137d.m1903o() == null || this.f2137d.m1903o().length() < 0) && (this.f2137d.m1899m() == null || this.f2137d.m1899m().length() < 0)) {
            return Stomp.EMPTY;
        }
        jSONObject.put(Message.TIMESTAMP, System.currentTimeMillis());
        jSONObject.put("product", this.f2137d.m1878d());
        jSONObject.put("p", this.f2136c);
        jSONObject.put("v", this.f2134a);
        jSONObject.put("d", this.f2135b);
        jSONObject.put("androidadvertisingid", this.f2137d.m1899m());
        jSONObject.put("androidadvertisingidoptout", this.f2137d.m1902n());
        jSONObject.put("androidid", this.f2137d.m1903o());
        jSONObject.put("os", this.f2137d.m1882e());
        jSONObject.put("model", this.f2137d.m1885f());
        if (this.f2137d.m1856B()) {
            jSONObject.put("location", this.f2137d.m1872c());
            jSONObject.put("locationAltitude", this.f2137d.m1911s());
            jSONObject.put("locationTimestamp", this.f2137d.m1912t());
            jSONObject.put("locationAccuracy", this.f2137d.m1913u());
            jSONObject.put("locationSpeed", this.f2137d.m1914v());
        }
        jSONObject.put("classificationid", this.f2137d.m1857C());
        jSONObject.put("isNetworkCheckEnableInUserSetting", this.f2137d.m1918z());
        jSONObject.put("isLocationCheckEnableInUserSetting", this.f2137d.m1856B());
        jSONObject.put("isAppInstallCheckEnableInUserSetting", this.f2137d.m1855A());
        jSONObject.put("device", this.f2137d.m1887g());
        if (this.f2137d.m1918z()) {
            if (!(this.f2137d.m1909r() == null || this.f2137d.m1909r().m1850a() == null)) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("ssid", this.f2137d.m1909r().m1850a().m1928a());
                jSONObject.put("wificurrent", jSONObject2);
            }
            jSONObject.put("carrier", this.f2137d.m1917y());
        }
        jSONObject.put("api", this.f2137d.m1889h());
        jSONObject.put("brand", this.f2137d.m1891i());
        jSONObject.put("language", this.f2137d.m1893j());
        jSONObject.put(stations.countryCode, this.f2137d.m1907q());
        jSONObject.put("timezone", this.f2137d.m1905p());
        if (d.m1820h()) {
            UserState d2 = this.f2137d.m1877d(context);
            if (d2 != null) {
                jSONObject.put("policy", d2.m1834a());
                if (d2.m1839b() != null && d2.m1839b().size() > 0) {
                    jSONObject.put("audience", new JSONArray(d2.m1839b()));
                }
                jSONObject.put("appcount", d2.m1842c());
                jSONObject.put("lastappid", d2.m1849h());
                jSONObject.put("meanversion", d2.m1845d());
                if (d2.m1846e() != null && d2.m1846e().size() > 0) {
                    jSONObject.put("campaigns", new JSONArray(d2.m1846e()));
                }
                if (d2.m1848g() != null && d2.m1848g().size() > 0) {
                    JSONArray jSONArray = new JSONArray();
                    for (Integer num : d2.m1848g().keySet()) {
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("classificationid", num);
                        jSONObject3.put("count", d2.m1848g().get(num));
                        jSONArray.put(jSONObject3);
                    }
                    jSONObject.put("classifications", jSONArray);
                }
                if (d2.m1847f() != null && d2.m1847f().size() > 0) {
                    jSONObject.put("appids", new JSONArray(d2.m1847f()));
                }
            }
        }
        if (this.f2137d.m1916x() != null && this.f2137d.m1916x().size() > 0) {
            jSONObject.put("urlids", new JSONArray(this.f2137d.m1916x()));
        }
        jSONObject.put("mcc", this.f2137d.m1858D());
        jSONObject.put("mnc", this.f2137d.m1859E());
        return jSONObject.toString();
    }

    final void m1749a(UserState userState) {
        this.f2137d = userState;
    }

    final void m1750a(String str) {
        this.f2134a = str;
    }

    final void m1751b(String str) {
        this.f2135b = str;
    }

    final void m1752c(String str) {
        this.f2136c = str;
    }
}
