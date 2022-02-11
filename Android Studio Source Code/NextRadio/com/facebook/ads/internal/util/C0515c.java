package com.facebook.ads.internal.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;

/* renamed from: com.facebook.ads.internal.util.c */
public class C0515c {
    private static final List<C0514b> f1900a;

    static {
        f1900a = new ArrayList();
    }

    public static String m1514a() {
        synchronized (f1900a) {
            if (f1900a.isEmpty()) {
                String str = Stomp.EMPTY;
                return str;
            }
            List<C0514b> arrayList = new ArrayList(f1900a);
            f1900a.clear();
            JSONArray jSONArray = new JSONArray();
            for (C0514b a : arrayList) {
                jSONArray.put(a.m1513a());
            }
            return jSONArray.toString();
        }
    }

    public static void m1515a(C0514b c0514b) {
        synchronized (f1900a) {
            f1900a.add(c0514b);
        }
    }
}
