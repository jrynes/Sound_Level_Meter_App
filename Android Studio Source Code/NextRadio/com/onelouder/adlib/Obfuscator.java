package com.onelouder.adlib;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;

class Obfuscator {
    private static final String src = "We're no strangers to love You know the rules and so do I A full commitment's what I'm thinking of You wouldn't get this from any other guy I just wanna tell you how I'm feeling Gotta make you understand";
    private static byte[] srcBytes;

    Obfuscator() {
    }

    static {
        srcBytes = null;
    }

    private static byte[] getSrcBytes() {
        if (srcBytes != null) {
            return srcBytes;
        }
        try {
            srcBytes = src.getBytes(HttpRequest.CHARSET_UTF8);
            return srcBytes;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Even though it should not be possible, UTF-8 is apparently unavailable.", e);
        }
    }

    public static byte[] process(byte[] mb) {
        byte[] sb = getSrcBytes();
        byte[] ob = new byte[mb.length];
        int s = 0;
        for (int i = 0; i < mb.length; i++) {
            int s2 = s + 1;
            ob[i] = (byte) (mb[i] ^ sb[s]);
            if (s2 == sb.length) {
                s = 0;
            } else {
                s = s2;
            }
        }
        return ob;
    }
}
