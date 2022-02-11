package com.facebook.ads.internal.util;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.util.i */
public class C0524i {
    private final String f1916a;
    private final String f1917b;
    private final String f1918c;
    private final List<String> f1919d;
    private final String f1920e;
    private final String f1921f;

    private C0524i(String str, String str2, String str3, List<String> list, String str4, String str5) {
        this.f1916a = str;
        this.f1917b = str2;
        this.f1918c = str3;
        this.f1919d = list;
        this.f1920e = str4;
        this.f1921f = str5;
    }

    public static C0524i m1549a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        String optString = jSONObject.optString("package");
        String optString2 = jSONObject.optString("appsite");
        String optString3 = jSONObject.optString("appsite_url");
        JSONArray optJSONArray = jSONObject.optJSONArray("key_hashes");
        List arrayList = new ArrayList();
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i));
            }
        }
        return new C0524i(optString, optString2, optString3, arrayList, jSONObject.optString("market_uri"), jSONObject.optString("fallback_url"));
    }

    public String m1550a() {
        return this.f1916a;
    }

    public String m1551b() {
        return this.f1917b;
    }

    public String m1552c() {
        return this.f1918c;
    }
}
