package com.mologiq.analytics;

import android.util.Base64;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/* renamed from: com.mologiq.analytics.n */
final class RSAEncryption {
    private PublicKey f2152a;
    private Cipher f2153b;

    RSAEncryption() {
        try {
            this.f2152a = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode("MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJHz5rYyjP4OdTotQP4QUKHHk0YP+YM/axTmaG2FO+AS0WG9vtLijYptdMACMeIvPANrey0HmQBqYmsCCFY7HO8CAwEAAQ==", 0)));
            this.f2153b = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final String m1772a(String str) {
        try {
            if (!(this.f2153b == null || this.f2152a == null)) {
                this.f2153b.init(1, this.f2152a);
                return Base64.encodeToString(this.f2153b.doFinal(str.getBytes()), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
