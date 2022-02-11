package com.facebook.ads.internal.adapters;

import android.content.Intent;
import android.os.Bundle;
import com.admarvel.android.ads.Constants;
import com.facebook.ads.internal.util.C0518e;
import com.facebook.ads.internal.util.C0519f;
import com.facebook.ads.internal.util.C0522g;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.adapters.l */
public class C0450l implements C0432a {
    private final String f1617a;
    private final String f1618b;
    private final String f1619c;
    private final C0518e f1620d;
    private final String f1621e;
    private final Collection<String> f1622f;
    private final Map<String, String> f1623g;
    private final String f1624h;
    private final int f1625i;
    private final int f1626j;

    private C0450l(String str, String str2, String str3, C0518e c0518e, String str4, Collection<String> collection, Map<String, String> map, String str5, int i, int i2) {
        this.f1617a = str;
        this.f1618b = str2;
        this.f1619c = str3;
        this.f1620d = c0518e;
        this.f1621e = str4;
        this.f1622f = collection;
        this.f1623g = map;
        this.f1624h = str5;
        this.f1625i = i;
        this.f1626j = i2;
    }

    public static C0450l m1278a(Bundle bundle) {
        return new C0450l(bundle.getString("markup"), null, bundle.getString("native_impression_report_url"), C0518e.NONE, Stomp.EMPTY, null, null, bundle.getString("request_id"), bundle.getInt("viewability_check_initial_delay"), bundle.getInt("viewability_check_interval"));
    }

    public static C0450l m1279a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        JSONArray jSONArray;
        String optString = jSONObject.optString("markup");
        String optString2 = jSONObject.optString("activation_command");
        String optString3 = jSONObject.optString("native_impression_report_url");
        String optString4 = jSONObject.optString("request_id");
        C0518e a = C0518e.m1523a(jSONObject.optString("invalidation_behavior"));
        String optString5 = jSONObject.optString("invalidation_report_url");
        try {
            jSONArray = new JSONArray(jSONObject.optString("detection_strings"));
        } catch (JSONException e) {
            e.printStackTrace();
            jSONArray = null;
        }
        Collection a2 = C0519f.m1524a(jSONArray);
        JSONObject optJSONObject = jSONObject.optJSONObject(Constants.NATIVE_AD_METADATA_ELEMENT);
        Map hashMap = new HashMap();
        if (optJSONObject != null) {
            Iterator keys = optJSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                hashMap.put(str, optJSONObject.optString(str));
            }
        }
        int i = 0;
        int i2 = ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
        if (hashMap.containsKey("viewability_check_initial_delay")) {
            i = Integer.parseInt((String) hashMap.get("viewability_check_initial_delay"));
        }
        if (hashMap.containsKey("viewability_check_interval")) {
            i2 = Integer.parseInt((String) hashMap.get("viewability_check_interval"));
        }
        return new C0450l(optString, optString2, optString3, a, optString5, a2, hashMap, optString4, i, i2);
    }

    public static C0450l m1280b(Intent intent) {
        return new C0450l(C0522g.m1532a(intent.getByteArrayExtra("markup")), intent.getStringExtra("activation_command"), intent.getStringExtra("native_impression_report_url"), C0518e.NONE, Stomp.EMPTY, null, null, intent.getStringExtra("request_id"), intent.getIntExtra("viewability_check_initial_delay", 0), intent.getIntExtra("viewability_check_interval", ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH));
    }

    public C0518e m1281a() {
        return this.f1620d;
    }

    public void m1282a(Intent intent) {
        intent.putExtra("markup", C0522g.m1541a(this.f1617a));
        intent.putExtra("activation_command", this.f1618b);
        intent.putExtra("native_impression_report_url", this.f1619c);
        intent.putExtra("request_id", this.f1624h);
        intent.putExtra("viewability_check_initial_delay", this.f1625i);
        intent.putExtra("viewability_check_interval", this.f1626j);
    }

    public String m1283b() {
        return this.f1621e;
    }

    public Collection<String> m1284c() {
        return this.f1622f;
    }

    public String m1285d() {
        return this.f1617a;
    }

    public String m1286e() {
        return this.f1618b;
    }

    public String m1287f() {
        return this.f1619c;
    }

    public String m1288g() {
        return "facebookAd.sendImpression();";
    }

    public Map<String, String> m1289h() {
        return this.f1623g;
    }

    public String m1290i() {
        return this.f1624h;
    }

    public int m1291j() {
        return this.f1625i;
    }

    public int m1292k() {
        return this.f1626j;
    }

    public Bundle m1293l() {
        Bundle bundle = new Bundle();
        bundle.putString("markup", this.f1617a);
        bundle.putString("native_impression_report_url", this.f1619c);
        bundle.putString("request_id", this.f1624h);
        bundle.putInt("viewability_check_initial_delay", this.f1625i);
        bundle.putInt("viewability_check_interval", this.f1626j);
        return bundle;
    }
}
