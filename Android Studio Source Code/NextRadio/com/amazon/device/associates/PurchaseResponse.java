package com.amazon.device.associates;

import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseResponse extends ShoppingServiceResponse {
    private final Status f1114a;
    private final List<Receipt> f1115b;

    public enum Status {
        SUCCESSFUL,
        FAILED,
        PENDING,
        INVALID_ID,
        NOT_SUPPORTED
    }

    protected boolean m665a() {
        return this.f1114a == Status.NOT_SUPPORTED;
    }

    PurchaseResponse(RequestId requestId, Status status) {
        this(requestId, status, new ArrayList());
    }

    PurchaseResponse(RequestId requestId, Status status, List<Receipt> list) {
        super(requestId);
        ar.m782a((Object) status, SettingsJsonConstants.APP_STATUS_KEY);
        this.f1114a = status;
        this.f1115b = list;
    }

    JSONObject m666b() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("requestId", getRequestId());
            jSONObject.put(SettingsJsonConstants.APP_STATUS_KEY, this.f1114a.toString());
            JSONArray jSONArray = new JSONArray();
            for (Receipt a : this.f1115b) {
                jSONArray.put(a.m667a());
            }
            jSONObject.put("receipts", jSONArray);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m666b().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public Status getStatus() {
        return this.f1114a;
    }

    public List<Receipt> getReceipts() {
        return Collections.unmodifiableList(this.f1115b);
    }
}
