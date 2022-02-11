package com.mologiq.analytics;

import android.content.Context;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: com.mologiq.analytics.t */
final class TargetParamAndroidPostData {
    private String f2184a;
    private boolean f2185b;
    private String f2186c;
    private String f2187d;
    private String f2188e;
    private List<TargetParam> f2189f;
    private String f2190g;
    private String f2191h;
    private double f2192i;
    private double f2193j;
    private double f2194k;
    private long f2195l;
    private double f2196m;
    private double f2197n;
    private boolean f2198o;

    TargetParamAndroidPostData() {
    }

    final String m1789a(Context context) {
        if (UserSettings.m1806d(context).m1832t() == 0) {
            return Stomp.EMPTY;
        }
        if (this.f2191h == null || this.f2191h.length() <= 0) {
            return Stomp.EMPTY;
        }
        if ((this.f2186c == null || this.f2186c.length() < 0) && (this.f2184a == null || this.f2184a.length() < 0)) {
            return Stomp.EMPTY;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("androidadvertisingid", this.f2184a);
        jSONObject.put("androidadvertisingidoptout", this.f2185b);
        jSONObject.put("androidid", this.f2186c);
        jSONObject.put("v", this.f2187d);
        jSONObject.put("d", this.f2188e);
        jSONObject.put("siteid", this.f2190g);
        jSONObject.put("p", this.f2191h);
        if (this.f2198o) {
            jSONObject.put("location", this.f2192i + Stomp.COMMA + this.f2193j);
            jSONObject.put("locationAltitude", this.f2194k);
            jSONObject.put("locationTimestamp", this.f2195l);
            jSONObject.put("locationAccuracy", this.f2196m);
            jSONObject.put("locationSpeed", this.f2197n);
        }
        if (this.f2189f != null && this.f2189f.size() > 0) {
            JSONArray jSONArray = new JSONArray();
            for (TargetParam targetParam : this.f2189f) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("n", targetParam.m1785a());
                jSONObject2.put("v", targetParam.m1787b());
                jSONArray.put(jSONObject2);
            }
            jSONObject.put("tp", jSONArray);
        }
        return jSONObject.toString();
    }

    final void m1790a(double d) {
        this.f2192i = d;
    }

    final void m1791a(long j) {
        this.f2195l = j;
    }

    final void m1792a(String str) {
        this.f2191h = str;
    }

    final void m1793a(List<TargetParam> list) {
        this.f2189f = list;
    }

    final void m1794a(boolean z) {
        this.f2185b = z;
    }

    final void m1795b(double d) {
        this.f2193j = d;
    }

    final void m1796b(String str) {
        this.f2187d = str;
    }

    final void m1797b(boolean z) {
        this.f2198o = true;
    }

    final void m1798c(double d) {
        this.f2194k = d;
    }

    final void m1799c(String str) {
        this.f2188e = str;
    }

    final void m1800d(double d) {
        this.f2196m = d;
    }

    final void m1801d(String str) {
        this.f2190g = str;
    }

    final void m1802e(double d) {
        this.f2197n = d;
    }

    final void m1803e(String str) {
        this.f2184a = str;
    }

    final void m1804f(String str) {
        this.f2186c = str;
    }
}
