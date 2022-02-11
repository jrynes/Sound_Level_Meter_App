package com.facebook.ads.internal.util;

import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp;

/* renamed from: com.facebook.ads.internal.util.t */
public class C0537t {
    public static String m1574a(Set<String> set, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String append : set) {
            stringBuilder.append(append);
            stringBuilder.append(str);
        }
        return stringBuilder.length() > 0 ? stringBuilder.substring(0, stringBuilder.length() - 1) : Stomp.EMPTY;
    }
}
