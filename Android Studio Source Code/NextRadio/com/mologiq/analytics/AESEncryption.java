package com.mologiq.analytics;

import android.util.Base64;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.mologiq.analytics.a */
final class AESEncryption {
    private SecretKey f2088a;

    AESEncryption() {
    }

    static String m1683a(String str, String str2) {
        try {
            Key secretKeySpec = new SecretKeySpec(Base64.decode(str, 0), "AES");
            Cipher instance = Cipher.getInstance("AES");
            instance.init(2, secretKeySpec);
            return new String(instance.doFinal(Base64.decode(str2, 0)));
        } catch (Exception e) {
            Utils.m1924a("Error");
            return null;
        }
    }

    final String m1684a() {
        return new String(Base64.encode(this.f2088a.getEncoded(), 0));
    }

    final String m1685a(String str) {
        SecureRandom secureRandom = new SecureRandom();
        KeyGenerator instance = KeyGenerator.getInstance("AES");
        instance.init(Flags.FLAG8, secureRandom);
        this.f2088a = instance.generateKey();
        Cipher instance2 = Cipher.getInstance("AES");
        instance2.init(1, this.f2088a);
        return Base64.encodeToString(instance2.doFinal(str.getBytes()), 0);
    }
}
