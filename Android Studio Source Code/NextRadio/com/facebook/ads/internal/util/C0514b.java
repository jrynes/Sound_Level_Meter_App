package com.facebook.ads.internal.util;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.util.b */
public class C0514b {
    public static String f1895a;
    private String f1896b;
    private Map<String, Object> f1897c;
    private int f1898d;
    private String f1899e;

    /* renamed from: com.facebook.ads.internal.util.b.a */
    public enum C0513a {
        OPEN_STORE(0),
        OPEN_LINK(1),
        XOUT(2),
        OPEN_URL(3),
        SHOW_INTERSTITIAL(4);
        
        int f1894f;

        private C0513a(int i) {
            this.f1894f = i;
        }
    }

    static {
        f1895a = null;
    }

    public C0514b(String str, Map<String, Object> map, int i, String str2) {
        this.f1896b = str;
        this.f1897c = map;
        this.f1898d = i;
        this.f1899e = str2;
    }

    public static C0514b m1511a(long j, C0513a c0513a, String str) {
        long currentTimeMillis = System.currentTimeMillis();
        Map hashMap = new HashMap();
        hashMap.put("Time", String.valueOf(currentTimeMillis - j));
        hashMap.put("AdAction", String.valueOf(c0513a.f1894f));
        return new C0514b("bounceback", hashMap, (int) (currentTimeMillis / 1000), str);
    }

    public static C0514b m1512a(Throwable th, String str) {
        Map hashMap = new HashMap();
        hashMap.put("ex", th.getClass().getSimpleName());
        hashMap.put("ex_msg", th.getMessage());
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        String str2 = Diagnostics.error;
        if (str == null) {
            str = f1895a;
        }
        return new C0514b(str2, hashMap, currentTimeMillis, str);
    }

    public JSONObject m1513a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("name", this.f1896b);
            jSONObject.put(MPDbAdapter.KEY_DATA, new JSONObject(this.f1897c));
            jSONObject.put("time", this.f1898d);
            jSONObject.put("request_id", this.f1899e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}
