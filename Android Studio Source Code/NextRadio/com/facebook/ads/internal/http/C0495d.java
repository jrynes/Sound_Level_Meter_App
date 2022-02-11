package com.facebook.ads.internal.http;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

/* renamed from: com.facebook.ads.internal.http.d */
public class C0495d {
    private static String f1830d;
    protected ConcurrentHashMap<String, String> f1831a;
    protected ConcurrentHashMap<String, C0494a> f1832b;
    protected ConcurrentHashMap<String, ArrayList<String>> f1833c;

    /* renamed from: com.facebook.ads.internal.http.d.a */
    private static class C0494a {
        public InputStream f1827a;
        public String f1828b;
        public String f1829c;

        public String m1459a() {
            return this.f1828b != null ? this.f1828b : "nofilename";
        }
    }

    static {
        f1830d = HttpRequest.CHARSET_UTF8;
    }

    public C0495d() {
        m1460c();
    }

    public C0495d(Map<String, String> map) {
        m1460c();
        for (Entry entry : map.entrySet()) {
            m1462a((String) entry.getKey(), (String) entry.getValue());
        }
    }

    private void m1460c() {
        this.f1831a = new ConcurrentHashMap();
        this.f1832b = new ConcurrentHashMap();
        this.f1833c = new ConcurrentHashMap();
    }

    public HttpEntity m1461a() {
        if (this.f1832b.isEmpty()) {
            try {
                return new UrlEncodedFormEntity(m1463b(), f1830d);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        HttpEntity c0496e = new C0496e();
        for (Entry entry : this.f1831a.entrySet()) {
            c0496e.m1465a((String) entry.getKey(), (String) entry.getValue());
        }
        int size = this.f1832b.entrySet().size() - 1;
        int i = 0;
        for (Entry entry2 : this.f1832b.entrySet()) {
            C0494a c0494a = (C0494a) entry2.getValue();
            if (c0494a.f1827a != null) {
                boolean z = i == size;
                if (c0494a.f1829c != null) {
                    c0496e.m1466a((String) entry2.getKey(), c0494a.m1459a(), c0494a.f1827a, c0494a.f1829c, z);
                } else {
                    c0496e.m1467a((String) entry2.getKey(), c0494a.m1459a(), c0494a.f1827a, z);
                }
            }
            i++;
        }
        for (Entry entry22 : this.f1833c.entrySet()) {
            Iterator it = ((ArrayList) entry22.getValue()).iterator();
            while (it.hasNext()) {
                c0496e.m1465a((String) entry22.getKey(), (String) it.next());
            }
        }
        return c0496e;
    }

    public void m1462a(String str, String str2) {
        if (str != null && str2 != null) {
            this.f1831a.put(str, str2);
        }
    }

    protected List<BasicNameValuePair> m1463b() {
        List<BasicNameValuePair> linkedList = new LinkedList();
        for (Entry entry : this.f1831a.entrySet()) {
            linkedList.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
        }
        for (Entry entry2 : this.f1833c.entrySet()) {
            Iterator it = ((ArrayList) entry2.getValue()).iterator();
            while (it.hasNext()) {
                linkedList.add(new BasicNameValuePair((String) entry2.getKey(), (String) it.next()));
            }
        }
        return linkedList;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : this.f1831a.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append((String) entry.getValue());
        }
        for (Entry entry2 : this.f1832b.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append((String) entry2.getKey());
            stringBuilder.append("=");
            stringBuilder.append("FILE");
        }
        for (Entry entry22 : this.f1833c.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            ArrayList arrayList = (ArrayList) entry22.getValue();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (arrayList.indexOf(str) != 0) {
                    stringBuilder.append("&");
                }
                stringBuilder.append((String) entry22.getKey());
                stringBuilder.append("=");
                stringBuilder.append(str);
            }
        }
        return stringBuilder.toString();
    }
}
