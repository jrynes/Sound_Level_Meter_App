package com.amazon.device.associates;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchByIdRequest extends ShoppingServiceRequest {
    private final Set<String> f1131a;

    public SearchByIdRequest() {
        this.f1131a = new HashSet();
    }

    public SearchByIdRequest(Set<String> set) {
        ar.m783a((Collection) set, "productIds");
        this.f1131a = new HashSet(set);
    }

    JSONObject m672a() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONArray jSONArray = new JSONArray();
            for (String put : this.f1131a) {
                jSONArray.put(put);
            }
            jSONObject.put("productIds", jSONArray);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m672a().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public void addProductId(String str) {
        ar.m782a((Object) str, "productId");
        this.f1131a.add(str);
    }

    public void removeProductId(String str) {
        ar.m782a((Object) str, "productId");
        this.f1131a.remove(str);
    }

    public Set<String> getProductIds() {
        return Collections.unmodifiableSet(this.f1131a);
    }
}
