package com.amazon.device.associates;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptsRequest extends ShoppingServiceRequest {
    private final Offset f1123a;

    public ReceiptsRequest(Offset offset) {
        this.f1123a = offset;
    }

    JSONObject m668a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("offset", this.f1123a);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m668a().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public Offset getOffset() {
        return this.f1123a;
    }
}
