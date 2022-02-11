package com.amazon.device.associates;

import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchResponse extends ShoppingServiceResponse {
    private final Status f1142a;
    private final List<Product> f1143b;
    private final int f1144c;
    private final int f1145d;

    public enum Status {
        SUCCESSFUL,
        INVALID_PARAMETER,
        FAILED,
        NOT_SUPPORTED
    }

    protected boolean m676a() {
        return this.f1142a == Status.NOT_SUPPORTED;
    }

    SearchResponse(RequestId requestId, Status status) {
        this(requestId, status, new ArrayList(), 0, 0);
    }

    SearchResponse(RequestId requestId, Status status, List<Product> list, int i, int i2) {
        super(requestId);
        ar.m782a((Object) status, SettingsJsonConstants.APP_STATUS_KEY);
        ar.m782a((Object) list, "products");
        this.f1142a = status;
        this.f1143b = list;
        this.f1144c = i;
        this.f1145d = i2;
    }

    JSONObject m677b() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("requestId", getRequestId());
            jSONObject.put(SettingsJsonConstants.APP_STATUS_KEY, this.f1142a);
            Object obj = null;
            if (this.f1143b != null) {
                JSONArray jSONArray = new JSONArray();
                for (Product a : this.f1143b) {
                    jSONArray.put(a.m654a());
                }
                obj = jSONArray;
            }
            jSONObject.put("products", obj);
            jSONObject.put("page", this.f1144c);
            jSONObject.put("totalPages", this.f1145d);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m677b().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public Status getStatus() {
        return this.f1142a;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(this.f1143b);
    }

    public int getPage() {
        return this.f1144c;
    }

    public int getTotalPages() {
        return this.f1145d;
    }
}
