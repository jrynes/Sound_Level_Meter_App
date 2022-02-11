package com.amazon.device.associates;

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceStatusResponse extends ShoppingServiceResponse {
    private final UserData f1146a;
    private final boolean f1147b;
    private final boolean f1148c;
    private Set<PurchaseExperience> f1149d;

    ServiceStatusResponse(RequestId requestId, UserData userData, Set<PurchaseExperience> set, boolean z, boolean z2) {
        super(requestId);
        this.f1146a = userData;
        this.f1147b = z;
        this.f1148c = z2;
        this.f1149d = set;
    }

    protected boolean m678a() {
        return false;
    }

    JSONObject m679b() {
        JSONObject jSONObject = new JSONObject();
        try {
            Object a;
            jSONObject.put("requestId", getRequestId());
            String str = "userData";
            if (this.f1146a != null) {
                a = this.f1146a.m680a();
            } else {
                JSONObject jSONObject2 = (JSONObject) null;
            }
            jSONObject.put(str, a);
            jSONObject.put("canSearch", this.f1147b);
            jSONObject.put("canGetReceipts", this.f1148c);
            JSONArray jSONArray = new JSONArray();
            for (PurchaseExperience purchaseExperience : this.f1149d) {
                jSONArray.put(purchaseExperience.toString());
            }
            jSONObject.put("supportedPurchaseExperiences", jSONArray);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m679b().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public UserData getUserData() {
        return this.f1146a;
    }

    public boolean canSearch() {
        return this.f1147b;
    }

    public boolean canGetReceipts() {
        return this.f1148c;
    }

    public Set<PurchaseExperience> getSupportedPurchaseExperiences() {
        return this.f1149d;
    }
}
