package com.amazon.device.ads;

class Base64 {
    private static final String ENCODE_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    Base64() {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] decode(java.lang.String r8) {
        /*
        r6 = com.amazon.device.ads.StringUtils.isNullOrWhiteSpace(r8);
        if (r6 == 0) goto L_0x000e;
    L_0x0006:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "Encoded String must not be null or white space";
        r6.<init>(r7);
        throw r6;
    L_0x000e:
        r4 = getDecodedLength(r8);
        if (r4 > 0) goto L_0x001c;
    L_0x0014:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "Encoded String decodes to zero bytes";
        r6.<init>(r7);
        throw r6;
    L_0x001c:
        r3 = new byte[r4];
        r1 = 0;
        r5 = 0;
    L_0x0020:
        r6 = r8.length();
        if (r5 >= r6) goto L_0x0028;
    L_0x0026:
        if (r1 < r4) goto L_0x0029;
    L_0x0028:
        return r3;
    L_0x0029:
        r6 = r5 % 4;
        if (r6 != 0) goto L_0x0035;
    L_0x002d:
        r6 = r8.length();
        r7 = r5 + 4;
        if (r6 < r7) goto L_0x0028;
    L_0x0035:
        r6 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        r7 = r8.charAt(r5);
        r0 = r6.indexOf(r7);
        r6 = -1;
        if (r0 == r6) goto L_0x0028;
    L_0x0042:
        r6 = r5 % 4;
        switch(r6) {
            case 0: goto L_0x004a;
            case 1: goto L_0x0050;
            case 2: goto L_0x0066;
            case 3: goto L_0x007c;
            default: goto L_0x0047;
        };
    L_0x0047:
        r5 = r5 + 1;
        goto L_0x0020;
    L_0x004a:
        r6 = r0 << 2;
        r6 = (byte) r6;
        r3[r1] = r6;
        goto L_0x0047;
    L_0x0050:
        r2 = r1 + 1;
        r6 = r3[r1];
        r7 = r0 >> 4;
        r7 = r7 & 3;
        r7 = (byte) r7;
        r6 = r6 | r7;
        r6 = (byte) r6;
        r3[r1] = r6;
        if (r2 >= r4) goto L_0x0087;
    L_0x005f:
        r6 = r0 << 4;
        r6 = (byte) r6;
        r3[r2] = r6;
        r1 = r2;
        goto L_0x0047;
    L_0x0066:
        r2 = r1 + 1;
        r6 = r3[r1];
        r7 = r0 >> 2;
        r7 = r7 & 15;
        r7 = (byte) r7;
        r6 = r6 | r7;
        r6 = (byte) r6;
        r3[r1] = r6;
        if (r2 >= r4) goto L_0x0087;
    L_0x0075:
        r6 = r0 << 6;
        r6 = (byte) r6;
        r3[r2] = r6;
        r1 = r2;
        goto L_0x0047;
    L_0x007c:
        r2 = r1 + 1;
        r6 = r3[r1];
        r7 = r0 & 63;
        r7 = (byte) r7;
        r6 = r6 | r7;
        r6 = (byte) r6;
        r3[r1] = r6;
    L_0x0087:
        r1 = r2;
        goto L_0x0047;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amazon.device.ads.Base64.decode(java.lang.String):byte[]");
    }

    private static int getDecodedLength(String s) {
        int index = s.indexOf("=");
        int end = 0;
        if (index > -1) {
            end = s.length() - index;
        }
        return (((s.length() + 3) / 4) * 3) - end;
    }
}
