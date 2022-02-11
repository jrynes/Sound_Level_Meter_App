package com.facebook.ads.internal.util;

import io.fabric.sdk.android.services.common.CommonUtils;
import java.security.MessageDigest;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.Type;

/* renamed from: com.facebook.ads.internal.util.s */
public class C0536s {
    public static boolean m1572a(String str) {
        return str == null || str.length() <= 0;
    }

    public static String m1573b(String str) {
        try {
            byte[] digest = MessageDigest.getInstance(CommonUtils.MD5_INSTANCE).digest(str.getBytes("utf-8"));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : digest) {
                stringBuilder.append(Integer.toString((b & Type.ANY) + KEYRecord.OWNER_ZONE, 16).substring(1));
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
