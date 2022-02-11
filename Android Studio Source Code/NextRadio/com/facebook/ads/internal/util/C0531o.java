package com.facebook.ads.internal.util;

import android.os.AsyncTask;
import android.util.Log;
import com.rabbitmq.client.AMQP;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

/* renamed from: com.facebook.ads.internal.util.o */
public class C0531o extends AsyncTask<String, Void, Void> {
    private static final String f1938a;
    private static final Set<String> f1939b;
    private Map<String, String> f1940c;
    private Map<String, String> f1941d;

    static {
        f1938a = C0531o.class.getSimpleName();
        f1939b = new HashSet();
        f1939b.add("#");
        f1939b.add("null");
    }

    public C0531o() {
        this(null, null);
    }

    public C0531o(Map<String, String> map) {
        this(map, null);
    }

    public C0531o(Map<String, String> map, Map<String, String> map2) {
        this.f1940c = map;
        this.f1941d = map2;
    }

    private String m1565a(String str, String str2, String str3) {
        if (C0536s.m1572a(str) || C0536s.m1572a(str2) || C0536s.m1572a(str3)) {
            return str;
        }
        return str + (str.contains("?") ? "&" : "?") + str2 + "=" + URLEncoder.encode(str3);
    }

    private boolean m1566a(String str) {
        HttpClient b = C0522g.m1542b();
        try {
            if (this.f1941d == null || this.f1941d.size() == 0) {
                return b.execute(new HttpGet(str)).getStatusLine().getStatusCode() == AMQP.REPLY_SUCCESS;
            }
            HttpUriRequest httpPost = new HttpPost(str);
            List arrayList = new ArrayList(1);
            for (Entry entry : this.f1941d.entrySet()) {
                arrayList.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
            return b.execute(httpPost).getStatusLine().getStatusCode() == AMQP.REPLY_SUCCESS;
        } catch (Throwable e) {
            Log.e(f1938a, "Error opening url: " + str, e);
            return false;
        }
    }

    private String m1567b(String str) {
        try {
            str = m1565a(str, "analog", C0522g.m1531a(C0512a.m1509a()));
        } catch (Exception e) {
        }
        return str;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.lang.Void m1568a(java.lang.String... r6) {
        /*
        r5 = this;
        r4 = 0;
        r0 = 0;
        r0 = r6[r0];
        r1 = com.facebook.ads.internal.util.C0536s.m1572a(r0);
        if (r1 != 0) goto L_0x0012;
    L_0x000a:
        r1 = f1939b;
        r1 = r1.contains(r0);
        if (r1 == 0) goto L_0x0013;
    L_0x0012:
        return r4;
    L_0x0013:
        r0 = r5.m1567b(r0);
        r1 = r5.f1940c;
        if (r1 == 0) goto L_0x004d;
    L_0x001b:
        r1 = r5.f1940c;
        r1 = r1.isEmpty();
        if (r1 != 0) goto L_0x004d;
    L_0x0023:
        r1 = r5.f1940c;
        r1 = r1.entrySet();
        r3 = r1.iterator();
        r2 = r0;
    L_0x002e:
        r0 = r3.hasNext();
        if (r0 == 0) goto L_0x004c;
    L_0x0034:
        r0 = r3.next();
        r0 = (java.util.Map.Entry) r0;
        r1 = r0.getKey();
        r1 = (java.lang.String) r1;
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r0 = r5.m1565a(r2, r1, r0);
        r2 = r0;
        goto L_0x002e;
    L_0x004c:
        r0 = r2;
    L_0x004d:
        r1 = 1;
    L_0x004e:
        r2 = r1 + 1;
        r3 = 2;
        if (r1 > r3) goto L_0x0012;
    L_0x0053:
        r1 = r5.m1566a(r0);
        if (r1 != 0) goto L_0x0012;
    L_0x0059:
        r1 = r2;
        goto L_0x004e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.ads.internal.util.o.a(java.lang.String[]):java.lang.Void");
    }

    protected /* synthetic */ Object doInBackground(Object[] objArr) {
        return m1568a((String[]) objArr);
    }
}
