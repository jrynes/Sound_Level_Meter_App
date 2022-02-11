package com.facebook.ads.internal.server;

import com.admarvel.android.ads.Constants;
import com.facebook.ads.internal.dto.C0461a;
import com.facebook.ads.internal.dto.C0464c;
import com.facebook.ads.internal.dto.C0465d;
import com.facebook.ads.internal.server.C0508c.C0507a;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Error;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.server.b */
public class C0506b {
    private static C0506b f1872a;

    static {
        f1872a = new C0506b();
    }

    public static synchronized C0506b m1501a() {
        C0506b c0506b;
        synchronized (C0506b.class) {
            c0506b = f1872a;
        }
        return c0506b;
    }

    private C0509d m1502a(JSONObject jSONObject) {
        int i = 0;
        JSONObject jSONObject2 = jSONObject.getJSONArray("placements").getJSONObject(0);
        C0465d a = C0465d.m1365a(jSONObject2.getJSONObject("definition"));
        C0464c c0464c = new C0464c(a);
        AdPlacementType a2 = a.m1366a();
        if (jSONObject2.has("ads")) {
            JSONArray jSONArray = jSONObject2.getJSONArray("ads");
            while (i < jSONArray.length()) {
                C0461a c0461a = new C0461a(a2);
                JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                c0461a.m1358a(jSONObject3.getString("adapter"));
                JSONObject jSONObject4 = jSONObject3.getJSONObject(MPDbAdapter.KEY_DATA);
                JSONArray optJSONArray = jSONObject3.optJSONArray(Constants.NATIVE_AD_TRACKERS_ELEMENT);
                if (optJSONArray != null) {
                    jSONObject4.put(Constants.NATIVE_AD_TRACKERS_ELEMENT, optJSONArray);
                }
                c0461a.m1359a(jSONObject4);
                c0464c.m1362a(c0461a);
                i++;
            }
        }
        return new C0509d(c0464c);
    }

    private C0510e m1503b(JSONObject jSONObject) {
        try {
            return new C0510e(jSONObject.optString(Error.MESSAGE, Stomp.EMPTY), new C0464c(C0465d.m1365a(jSONObject.getJSONArray("placements").getJSONObject(0).getJSONObject("definition"))));
        } catch (JSONException e) {
            return m1504c(jSONObject);
        }
    }

    private C0510e m1504c(JSONObject jSONObject) {
        return new C0510e(jSONObject.optString(Error.MESSAGE, Stomp.EMPTY), null);
    }

    public C0508c m1505a(String str) {
        JSONObject jSONObject = new JSONObject(str);
        String optString = jSONObject.optString(Send.TYPE);
        Object obj = -1;
        switch (optString.hashCode()) {
            case 96432:
                if (optString.equals("ads")) {
                    obj = null;
                    break;
                }
                break;
            case 96784904:
                if (optString.equals(Diagnostics.error)) {
                    obj = 1;
                    break;
                }
                break;
        }
        switch (obj) {
            case Tokenizer.EOF /*0*/:
                return m1502a(jSONObject);
            case Zone.PRIMARY /*1*/:
                return m1503b(jSONObject);
            default:
                JSONObject optJSONObject = jSONObject.optJSONObject(Diagnostics.error);
                return optJSONObject != null ? m1504c(optJSONObject) : new C0508c(C0507a.UNKNOWN, null);
        }
    }
}
