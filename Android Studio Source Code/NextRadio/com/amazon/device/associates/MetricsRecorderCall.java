package com.amazon.device.associates;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.amazon.device.associates.j */
class MetricsRecorderCall extends ag {
    private static final String f1335b;
    private static String f1336c;
    private static final String f1337d;
    private String f1338e;
    private String f1339f;
    private HashMap<String, String> f1340g;
    private HashMap<String, String> f1341h;

    static {
        f1335b = MetricsRecorderCall.class.getName();
        f1336c = "putMetrics";
        f1337d = MetricsRecorderCall.class.getName();
    }

    public MetricsRecorderCall(String str) {
        this.f1340g = new HashMap();
        this.f1341h = new HashMap();
        this.f1338e = bp.m901b();
        this.f1339f = "ATVPDKIKX0DER";
        m970a(str);
    }

    public MetricsRecorderCall(String str, String str2) {
        this.f1340g = new HashMap();
        this.f1341h = new HashMap();
        this.f1338e = bp.m901b();
        this.f1339f = "ATVPDKIKX0DER";
        m970a(str + ActiveMQDestination.PATH_SEPERATOR + str2);
    }

    protected void m969a() {
        this.a = new au(new StringBuilder("http://assoc-msdk-us.amazon-adsystem.com/" + f1336c).toString());
        this.a.m797a("appId", this.f1338e);
        this.a.m797a("marketplaceId", this.f1339f);
        this.a.m797a("device", m967f());
        this.a.m797a("counters", m968a(this.f1340g));
        this.a.m797a("timers", m968a(this.f1341h));
    }

    private String m967f() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("deviceDetail", bq.m902a(bp.m899a()).m903a());
            jSONObject.put("packageDetail", ad.m710a(bp.m899a()).m711a());
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("ver", Stomp.V1_0);
            jSONObject.put("SDKVersion", jSONObject2);
            return jSONObject.toString();
        } catch (JSONException e) {
            return Stomp.EMPTY;
        }
    }

    protected synchronized void m971b() {
        try {
            this.a.m796a(RequestMethod.GET);
        } catch (UnsupportedEncodingException e) {
            Log.m1019c(f1335b, "Call to MetricsRecorderCall URL failed. Ex=", e);
        } catch (Exception e2) {
            Log.m1018c(f1335b, "Call to get MetricsRecorderCall URL failed. Ex=");
        }
    }

    protected String m972c() {
        return f1337d;
    }

    public void m970a(String str) {
        this.f1340g.put(str, "1");
    }

    public String m968a(HashMap<String, String> hashMap) {
        if (hashMap != null) {
            return new JSONObject(hashMap).toString();
        }
        return Stomp.EMPTY;
    }

    public void m973d() {
        new av().execute(new ag[]{this});
    }
}
