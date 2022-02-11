package com.mologiq.analytics;

import android.content.Context;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: com.mologiq.analytics.b */
final class AdEventsAndroidPostData {
    private String f2099a;
    private String f2100b;
    private String f2101c;
    private boolean f2102d;
    private String f2103e;
    private int f2104f;
    private String f2105g;
    private String f2106h;
    private AdEventsAndroidPostData f2107i;
    private AdEventsAndroidPostData f2108j;
    private AdEventsAndroidPostData f2109k;

    /* renamed from: com.mologiq.analytics.b.a */
    class AdEventsAndroidPostData {
        final /* synthetic */ AdEventsAndroidPostData f2092a;
        private Long f2093b;
        private Long f2094c;
        private String f2095d;
        private String f2096e;
        private String f2097f;
        private List<AdEventsAndroidPostData> f2098g;

        /* renamed from: com.mologiq.analytics.b.a.a */
        class AdEventsAndroidPostData {
            final /* synthetic */ AdEventsAndroidPostData f2089a;
            private String f2090b;
            private String f2091c;

            AdEventsAndroidPostData(AdEventsAndroidPostData adEventsAndroidPostData) {
                this.f2089a = adEventsAndroidPostData;
            }

            final String m1686a() {
                return this.f2090b;
            }

            final void m1687a(String str) {
                this.f2090b = str;
            }

            final String m1688b() {
                return this.f2091c;
            }

            final void m1689b(String str) {
                this.f2091c = str;
            }
        }

        AdEventsAndroidPostData(AdEventsAndroidPostData adEventsAndroidPostData) {
            this.f2092a = adEventsAndroidPostData;
        }

        final JSONObject m1690a() {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("ts", this.f2093b);
            jSONObject.put("bid", this.f2094c);
            jSONObject.put("sid", this.f2095d);
            jSONObject.put("pid", this.f2096e);
            jSONObject.put("p", this.f2097f);
            if (this.f2098g != null && this.f2098g.size() > 0) {
                JSONArray jSONArray = new JSONArray();
                for (AdEventsAndroidPostData adEventsAndroidPostData : this.f2098g) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("n", adEventsAndroidPostData.m1686a());
                    jSONObject2.put("v", adEventsAndroidPostData.m1688b());
                    jSONArray.put(jSONObject2);
                }
                jSONObject.put("tp", jSONArray);
            }
            return jSONObject;
        }

        final void m1691a(Long l) {
            this.f2093b = l;
        }

        final void m1692a(String str) {
            this.f2095d = str;
        }

        final void m1693a(List<AdEventsAndroidPostData> list) {
            this.f2098g = list;
        }

        final void m1694b(Long l) {
            this.f2094c = l;
        }

        final void m1695b(String str) {
            this.f2097f = str;
        }
    }

    AdEventsAndroidPostData() {
    }

    final String m1696a(Context context) {
        if (UserSettings.m1806d(context).m1832t() == 0) {
            return Stomp.EMPTY;
        }
        if (this.f2105g == null || this.f2105g.length() <= 0) {
            return Stomp.EMPTY;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("androidadvertisingid", this.f2101c);
        jSONObject.put("androidadvertisingidoptout", this.f2102d);
        jSONObject.put("androidid", this.f2103e);
        jSONObject.put("v", this.f2099a);
        jSONObject.put("d", this.f2100b);
        jSONObject.put("p", this.f2105g);
        jSONObject.put("wifi", this.f2104f);
        jSONObject.put("ip", this.f2106h);
        if (this.f2109k != null) {
            jSONObject.put("clicks", this.f2109k.m1690a());
        }
        if (this.f2107i != null) {
            jSONObject.put("impressions", this.f2107i.m1690a());
        }
        if (this.f2108j != null) {
            jSONObject.put("requests", this.f2108j.m1690a());
        }
        return jSONObject.toString();
    }

    final void m1697a(int i) {
        this.f2104f = i;
    }

    final void m1698a(AdEventsAndroidPostData adEventsAndroidPostData) {
        this.f2107i = adEventsAndroidPostData;
    }

    final void m1699a(String str) {
        this.f2099a = str;
    }

    final void m1700a(boolean z) {
        this.f2102d = z;
    }

    final void m1701b(AdEventsAndroidPostData adEventsAndroidPostData) {
        this.f2108j = adEventsAndroidPostData;
    }

    final void m1702b(String str) {
        this.f2100b = str;
    }

    final void m1703c(AdEventsAndroidPostData adEventsAndroidPostData) {
        this.f2109k = adEventsAndroidPostData;
    }

    final void m1704c(String str) {
        this.f2105g = str;
    }

    final void m1705d(String str) {
        this.f2106h = str;
    }

    final void m1706e(String str) {
        this.f2101c = str;
    }

    final void m1707f(String str) {
        this.f2103e = str;
    }
}
