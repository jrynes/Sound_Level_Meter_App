package com.google.android.gms.internal;

import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.Type;

public class zzsl {
    private final byte[] zzbtW;
    private int zzbtX;
    private int zzbtY;

    public zzsl(byte[] bArr) {
        int i;
        this.zzbtW = new byte[KEYRecord.OWNER_ZONE];
        for (i = 0; i < KEYRecord.OWNER_ZONE; i++) {
            this.zzbtW[i] = (byte) i;
        }
        i = 0;
        for (int i2 = 0; i2 < KEYRecord.OWNER_ZONE; i2++) {
            i = ((i + this.zzbtW[i2]) + bArr[i2 % bArr.length]) & Type.ANY;
            byte b = this.zzbtW[i2];
            this.zzbtW[i2] = this.zzbtW[i];
            this.zzbtW[i] = b;
        }
        this.zzbtX = 0;
        this.zzbtY = 0;
    }

    public void zzC(byte[] bArr) {
        int i = this.zzbtX;
        int i2 = this.zzbtY;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            i = (i + 1) & Type.ANY;
            i2 = (i2 + this.zzbtW[i]) & Type.ANY;
            byte b = this.zzbtW[i];
            this.zzbtW[i] = this.zzbtW[i2];
            this.zzbtW[i2] = b;
            bArr[i3] = (byte) (bArr[i3] ^ this.zzbtW[(this.zzbtW[i] + this.zzbtW[i2]) & Type.ANY]);
        }
        this.zzbtX = i;
        this.zzbtY = i2;
    }
}
