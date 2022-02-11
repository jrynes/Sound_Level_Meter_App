package com.onelouder.adlib.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class DocumentCryptCommon {
    protected static final String AES_ALGO = "AES";
    protected static final int AES_ALGO_BITS = 256;
    protected static final int AES_IV_BYTES = 16;
    protected static final int AES_KEY_BYTES = 32;
    protected static final String AES_TRANSFORM = "AES/CBC/PKCS5Padding";
    protected static final int INTEGER_BYTES = 4;
    protected static final String RSA_ALGO = "RSA";
    protected static final String RSA_TRANSFORM = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";

    protected static Cipher getAESCipher(int mode, SecretKey aesKey, byte[] aesIV) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher c = Cipher.getInstance(AES_TRANSFORM);
        c.init(mode, aesKey, new IvParameterSpec(aesIV));
        return c;
    }
}
