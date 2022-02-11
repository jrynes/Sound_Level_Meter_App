package com.amazon.device.associates;

import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptsResponse extends ShoppingServiceResponse {
    private final Status f1125a;
    private final List<Receipt> f1126b;
    private final Offset f1127c;
    private final boolean f1128d;

    public enum Status {
        SUCCESSFUL,
        FAILED,
        NO_MORE_DATA,
        NOT_SUPPORTED
    }

    protected boolean m669a() {
        return this.f1125a == Status.NOT_SUPPORTED;
    }

    ReceiptsResponse(RequestId requestId, Status status) {
        this(requestId, status, new ArrayList(), null, false);
    }

    ReceiptsResponse(RequestId requestId, Status status, List<Receipt> list, Offset offset, boolean z) {
        super(requestId);
        ar.m782a((Object) list, "receipts");
        ar.m782a((Object) status, SettingsJsonConstants.APP_STATUS_KEY);
        this.f1125a = status;
        this.f1126b = list;
        this.f1127c = offset;
        this.f1128d = z;
    }

    JSONObject m670b() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("requestId", getRequestId());
            Object obj = null;
            if (this.f1126b != null) {
                JSONArray jSONArray = new JSONArray();
                for (Receipt a : this.f1126b) {
                    jSONArray.put(a.m667a());
                }
                obj = jSONArray;
            }
            jSONObject.put("receipts", obj);
            jSONObject.put("nextOffset", this.f1127c);
            jSONObject.put("hasMore", this.f1128d);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m670b().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public Status getStatus() {
        return this.f1125a;
    }

    public List<Receipt> getReceipts() {
        return Collections.unmodifiableList(this.f1126b);
    }

    public Offset getNextOffset() {
        return this.f1127c;
    }

    public boolean hasMore() {
        return this.f1128d;
    }
}
