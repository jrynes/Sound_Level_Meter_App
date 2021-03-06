package com.google.android.gms.internal;

import io.fabric.sdk.android.services.common.CommonUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class zzbg {
    private static MessageDigest zzto;
    protected Object zzpV;

    static {
        zzto = null;
    }

    public zzbg() {
        this.zzpV = new Object();
    }

    protected MessageDigest zzcL() {
        MessageDigest messageDigest;
        synchronized (this.zzpV) {
            if (zzto != null) {
                messageDigest = zzto;
            } else {
                for (int i = 0; i < 2; i++) {
                    try {
                        zzto = MessageDigest.getInstance(CommonUtils.MD5_INSTANCE);
                    } catch (NoSuchAlgorithmException e) {
                    }
                }
                messageDigest = zzto;
            }
        }
        return messageDigest;
    }

    abstract byte[] zzu(String str);
}
