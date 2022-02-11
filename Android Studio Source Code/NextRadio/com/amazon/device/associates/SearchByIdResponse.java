package com.amazon.device.associates;

import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchByIdResponse extends ShoppingServiceResponse {
    private final Status f1133a;
    private final List<Product> f1134b;
    private final Set<String> f1135c;

    public enum Status {
        SUCCESSFUL,
        FAILED,
        INVALID_PARAMETER,
        NOT_SUPPORTED
    }

    protected boolean m673a() {
        return this.f1133a == Status.NOT_SUPPORTED;
    }

    SearchByIdResponse(RequestId requestId, Status status) {
        this(requestId, status, new ArrayList(), new HashSet());
    }

    SearchByIdResponse(RequestId requestId, Status status, List<Product> list, Set<String> set) {
        super(requestId);
        ar.m782a((Object) status, SettingsJsonConstants.APP_STATUS_KEY);
        this.f1133a = status;
        this.f1134b = list;
        this.f1135c = set;
    }

    JSONObject m674b() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONArray jSONArray;
            jSONObject.put("requestId", getRequestId());
            jSONObject.put(SettingsJsonConstants.APP_STATUS_KEY, this.f1133a);
            Object obj = null;
            if (this.f1134b != null) {
                jSONArray = new JSONArray();
                for (Product a : this.f1134b) {
                    jSONArray.put(a.m654a());
                }
                obj = jSONArray;
            }
            jSONObject.put("products", obj);
            jSONArray = new JSONArray();
            for (String put : this.f1135c) {
                jSONArray.put(put);
            }
            jSONObject.put("unavailableProductIds", jSONArray);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m674b().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public Status getStatus() {
        return this.f1133a;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(this.f1134b);
    }

    public Set<String> getUnavailableProductIds() {
        return Collections.unmodifiableSet(this.f1135c);
    }
}
