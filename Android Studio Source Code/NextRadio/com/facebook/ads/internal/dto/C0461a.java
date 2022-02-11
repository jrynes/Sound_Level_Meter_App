package com.facebook.ads.internal.dto;

import com.facebook.ads.internal.server.AdPlacementType;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.dto.a */
public class C0461a {
    public final AdPlacementType f1704a;
    public String f1705b;
    public JSONObject f1706c;

    public C0461a(AdPlacementType adPlacementType) {
        this.f1704a = adPlacementType;
    }

    public void m1358a(String str) {
        this.f1705b = str;
    }

    public void m1359a(JSONObject jSONObject) {
        this.f1706c = jSONObject;
    }
}
