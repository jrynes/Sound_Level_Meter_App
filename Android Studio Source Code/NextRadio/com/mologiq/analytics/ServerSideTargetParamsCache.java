package com.mologiq.analytics;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: com.mologiq.analytics.r */
final class ServerSideTargetParamsCache extends PersistentObject {
    private static final ServerSideTargetParamsCache f2179c;
    private List<ServerSideTargetParamsCache> f2180a;
    private long f2181b;

    /* renamed from: com.mologiq.analytics.r.a */
    public class ServerSideTargetParamsCache {
        final /* synthetic */ ServerSideTargetParamsCache f2176a;
        private String f2177b;
        private String f2178c;

        public ServerSideTargetParamsCache(ServerSideTargetParamsCache serverSideTargetParamsCache) {
            this.f2176a = serverSideTargetParamsCache;
        }

        public final String m1773a() {
            return this.f2177b;
        }

        public final void m1774a(String str) {
            this.f2177b = str;
        }

        public final String m1775b() {
            return this.f2178c;
        }

        public final void m1776b(String str) {
            this.f2178c = str;
        }
    }

    static {
        f2179c = new ServerSideTargetParamsCache("enhanceParamRequest");
    }

    private ServerSideTargetParamsCache(String str) {
        super(str);
        this.f2180a = new ArrayList();
        this.f2181b = 0;
    }

    static ServerSideTargetParamsCache m1777b() {
        return f2179c;
    }

    protected final String m1778a() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(Message.TIMESTAMP, this.f2181b);
        if (this.f2180a != null && this.f2180a.size() > 0) {
            JSONArray jSONArray = new JSONArray();
            for (ServerSideTargetParamsCache serverSideTargetParamsCache : this.f2180a) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("n", serverSideTargetParamsCache.m1773a());
                jSONObject2.put("v", serverSideTargetParamsCache.m1775b());
                jSONArray.put(jSONObject2);
            }
            jSONObject.put("tp", jSONArray);
        }
        return jSONObject.toString();
    }

    final void m1779a(long j) {
        this.f2181b = j;
    }

    protected final void m1780a(String str) {
        if (str != null && str.length() > 0) {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.isNull("core")) {
                jSONObject = jSONObject.getJSONObject("core");
                this.f2181b = jSONObject.getLong(Message.TIMESTAMP);
                if (!jSONObject.isNull("tp")) {
                    JSONArray jSONArray = jSONObject.getJSONArray("tp");
                    if (jSONArray != null && jSONArray.length() > 0) {
                        this.f2180a = new ArrayList();
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            ServerSideTargetParamsCache serverSideTargetParamsCache = new ServerSideTargetParamsCache(this);
                            serverSideTargetParamsCache.m1774a(jSONObject2.getString("n"));
                            serverSideTargetParamsCache.m1776b(jSONObject2.getString("v"));
                            this.f2180a.add(serverSideTargetParamsCache);
                        }
                    }
                }
            }
        }
    }

    protected final void m1781b(String str) {
        if (str != null && str.length() > 0) {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.isNull("tp")) {
                JSONArray jSONArray = jSONObject.getJSONArray("tp");
                if (jSONArray != null && jSONArray.length() > 0) {
                    this.f2180a = new ArrayList();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                        ServerSideTargetParamsCache serverSideTargetParamsCache = new ServerSideTargetParamsCache(this);
                        serverSideTargetParamsCache.m1774a(jSONObject2.getString("n"));
                        serverSideTargetParamsCache.m1776b(jSONObject2.getString("v"));
                        this.f2180a.add(serverSideTargetParamsCache);
                    }
                }
            }
        }
    }

    public final Map<String, Object> m1782c() {
        if (this.f2180a == null || this.f2180a.size() <= 0) {
            return null;
        }
        Map<String, Object> hashMap = new HashMap();
        for (ServerSideTargetParamsCache serverSideTargetParamsCache : this.f2180a) {
            hashMap.put(serverSideTargetParamsCache.m1773a(), serverSideTargetParamsCache.m1775b());
        }
        return hashMap;
    }

    public final void m1783c(Context context) {
        JSONObject jSONObject = new JSONObject();
        String a = m1778a();
        if (a != null && a.length() > 0) {
            jSONObject.put("core", new JSONObject(a));
        }
        a = Stomp.EMPTY;
        if (a != null && a.length() > 0) {
            jSONObject.put("extra", new JSONObject(a));
        }
        try {
            DatabaseHandler.m1725a(context).m1741a("enhanceParamRequest", jSONObject.toString());
        } catch (Throwable th) {
            Utils.m1924a(Utils.m1922a(th));
        }
    }

    final long m1784d() {
        return this.f2181b;
    }
}
