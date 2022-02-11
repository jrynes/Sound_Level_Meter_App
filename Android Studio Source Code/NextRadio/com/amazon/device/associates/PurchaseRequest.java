package com.amazon.device.associates;

import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseRequest extends ShoppingServiceRequest {
    private final String f1108a;
    private boolean f1109b;
    private View f1110c;
    private PurchaseExperience f1111d;

    public PurchaseRequest(String str, View view) {
        ar.m782a((Object) str, "productId");
        ar.m782a((Object) view, "originView");
        this.f1108a = str;
        this.f1110c = view;
        this.f1109b = false;
    }

    public PurchaseRequest(String str, View view, boolean z) {
        ar.m782a((Object) str, "productId");
        ar.m782a((Object) view, "originView");
        this.f1108a = str;
        this.f1110c = view;
        this.f1109b = z;
    }

    JSONObject m663a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("productId", this.f1108a);
            jSONObject.put("receiveReceipt", this.f1109b);
            jSONObject.put("purchaseExperience", this.f1111d);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m663a().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public String getProductId() {
        return this.f1108a;
    }

    public boolean getReceiveReceipt() {
        return this.f1109b;
    }

    public View getOriginView() {
        return this.f1110c;
    }

    public PurchaseExperience getPurchaseExperience() {
        return this.f1111d;
    }

    public void setPurchaseExperience(PurchaseExperience purchaseExperience) {
        this.f1111d = purchaseExperience;
    }
}
