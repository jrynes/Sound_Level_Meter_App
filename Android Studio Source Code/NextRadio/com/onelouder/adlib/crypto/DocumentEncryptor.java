package com.onelouder.adlib.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.xbill.DNS.KEYRecord;

public class DocumentEncryptor extends DocumentCryptCommon {
    private final SecureRandom SR;
    private final KeyGenerator aesKeyGenerator;
    private final PublicKey rsaPublicKey;

    public DocumentEncryptor(BigInteger modulus, BigInteger publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.SR = new SecureRandom();
        this.aesKeyGenerator = KeyGenerator.getInstance("AES");
        this.rsaPublicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
    }

    private synchronized SecretKey generateAESKey() {
        this.aesKeyGenerator.init(KEYRecord.OWNER_ZONE);
        return this.aesKeyGenerator.generateKey();
    }

    private synchronized byte[] generateAESIV() {
        byte[] iv;
        iv = new byte[16];
        this.SR.nextBytes(iv);
        return iv;
    }

    private synchronized Cipher getRSACipher() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher c;
        c = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        c.init(1, this.rsaPublicKey);
        return c;
    }

    public byte[] encrypt(byte[] input) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeySpecException, IOException, IllegalBlockSizeException, BadPaddingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SecretKey aesKey = generateAESKey();
        byte[] aesIV = generateAESIV();
        Cipher aesCipher = DocumentCryptCommon.getAESCipher(1, aesKey, aesIV);
        Cipher rsaCipher = getRSACipher();
        ByteArrayOutputStream encryptedKeyIV = new ByteArrayOutputStream();
        byte[] chunk = rsaCipher.update(aesKey.getEncoded());
        if (chunk != null) {
            encryptedKeyIV.write(chunk);
        }
        encryptedKeyIV.write(rsaCipher.doFinal(aesIV));
        out.write(ByteBuffer.allocate(4).putInt(encryptedKeyIV.size()).array());
        out.write(encryptedKeyIV.toByteArray());
        out.write(aesCipher.doFinal(input));
        return out.toByteArray();
    }
}
