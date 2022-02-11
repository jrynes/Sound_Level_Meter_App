package com.amazon.device.ads;

import io.fabric.sdk.android.services.common.CommonUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.Type;

class StringUtils {
    private static final String LOGTAG;
    private static final MobileAdsLogger logger;

    static {
        LOGTAG = StringUtils.class.getSimpleName();
        logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
    }

    private StringUtils() {
    }

    public static boolean containsRegEx(String regex, String input) {
        return Pattern.compile(regex).matcher(input).find();
    }

    public static String getFirstMatch(String regex, String input) {
        Matcher m = Pattern.compile(regex).matcher(input);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

    public static final boolean isNullOrEmpty(String s) {
        return s == null || s.equals(Stomp.EMPTY);
    }

    public static final boolean isNullOrWhiteSpace(String s) {
        return isNullOrEmpty(s) || s.trim().equals(Stomp.EMPTY);
    }

    protected static boolean doesExceptionContainLockedDatabaseMessage(Exception e) {
        String lockedDatabaseMessage = "database is locked";
        return (e == null || e.getMessage() == null) ? false : e.getMessage().contains("database is locked");
    }

    public static String sha1(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance(CommonUtils.SHA1_INSTANCE);
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(Integer.toHexString((b & Type.ANY) | KEYRecord.OWNER_ZONE).substring(1));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return Stomp.EMPTY;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readStringFromInputStream(java.io.InputStream r7) {
        /*
        if (r7 != 0) goto L_0x0004;
    L_0x0002:
        r4 = 0;
    L_0x0003:
        return r4;
    L_0x0004:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = new byte[r4];
    L_0x000d:
        r2 = r7.read(r0);	 Catch:{ IOException -> 0x001e }
        r4 = -1;
        if (r2 == r4) goto L_0x002e;
    L_0x0014:
        r4 = new java.lang.String;	 Catch:{ IOException -> 0x001e }
        r5 = 0;
        r4.<init>(r0, r5, r2);	 Catch:{ IOException -> 0x001e }
        r3.append(r4);	 Catch:{ IOException -> 0x001e }
        goto L_0x000d;
    L_0x001e:
        r1 = move-exception;
        r4 = logger;	 Catch:{ all -> 0x0044 }
        r5 = "Unable to read the stream.";
        r4.m639e(r5);	 Catch:{ all -> 0x0044 }
        r7.close();	 Catch:{ IOException -> 0x003b }
    L_0x0029:
        r4 = r3.toString();
        goto L_0x0003;
    L_0x002e:
        r7.close();	 Catch:{ IOException -> 0x0032 }
        goto L_0x0029;
    L_0x0032:
        r1 = move-exception;
        r4 = logger;
        r5 = "IOException while trying to close the stream.";
        r4.m639e(r5);
        goto L_0x0029;
    L_0x003b:
        r1 = move-exception;
        r4 = logger;
        r5 = "IOException while trying to close the stream.";
        r4.m639e(r5);
        goto L_0x0029;
    L_0x0044:
        r4 = move-exception;
        r7.close();	 Catch:{ IOException -> 0x0049 }
    L_0x0048:
        throw r4;
    L_0x0049:
        r1 = move-exception;
        r5 = logger;
        r6 = "IOException while trying to close the stream.";
        r5.m639e(r6);
        goto L_0x0048;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amazon.device.ads.StringUtils.readStringFromInputStream(java.io.InputStream):java.lang.String");
    }
}
