package org.apache.activemq.util;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import org.xbill.DNS.Type;

public final class DataByteArrayOutputStream extends OutputStream implements DataOutput {
    private static final int DEFAULT_SIZE = 2048;
    private byte[] buf;
    private int pos;

    public DataByteArrayOutputStream(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Invalid size: " + size);
        }
        this.buf = new byte[size];
    }

    public DataByteArrayOutputStream() {
        this(DEFAULT_SIZE);
    }

    public void restart(int size) {
        this.buf = new byte[size];
        this.pos = 0;
    }

    public void restart() {
        restart(DEFAULT_SIZE);
    }

    public ByteSequence toByteSequence() {
        return new ByteSequence(this.buf, 0, this.pos);
    }

    public void write(int b) {
        int newcount = this.pos + 1;
        ensureEnoughBuffer(newcount);
        this.buf[this.pos] = (byte) b;
        this.pos = newcount;
    }

    public void write(byte[] b, int off, int len) {
        if (len != 0) {
            int newcount = this.pos + len;
            ensureEnoughBuffer(newcount);
            System.arraycopy(b, off, this.buf, this.pos, len);
            this.pos = newcount;
        }
    }

    public byte[] getData() {
        return this.buf;
    }

    public void reset() {
        this.pos = 0;
    }

    public void position(int offset) {
        ensureEnoughBuffer(offset);
        this.pos = offset;
    }

    public int size() {
        return this.pos;
    }

    public void writeBoolean(boolean v) {
        ensureEnoughBuffer(this.pos + 1);
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (v ? 1 : 0);
    }

    public void writeByte(int v) {
        ensureEnoughBuffer(this.pos + 1);
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (v >>> 0);
    }

    public void writeShort(int v) {
        ensureEnoughBuffer(this.pos + 2);
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (v >>> 8);
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (v >>> 0);
    }

    public void writeChar(int v) {
        ensureEnoughBuffer(this.pos + 2);
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (v >>> 8);
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (v >>> 0);
    }

    public void writeInt(int v) {
        ensureEnoughBuffer(this.pos + 4);
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (v >>> 24);
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (v >>> 16);
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (v >>> 8);
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (v >>> 0);
    }

    public void writeLong(long v) {
        ensureEnoughBuffer(this.pos + 8);
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (v >>> 56));
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (v >>> 48));
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (v >>> 40));
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (v >>> 32));
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (v >>> 24));
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (v >>> 16));
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (v >>> 8));
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (v >>> null));
    }

    public void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    public void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    public void writeBytes(String s) {
        int length = s.length();
        for (int i = 0; i < length; i++) {
            write((byte) s.charAt(i));
        }
    }

    public void writeChars(String s) {
        int length = s.length();
        for (int i = 0; i < length; i++) {
            int c = s.charAt(i);
            write((c >>> 8) & Type.ANY);
            write((c >>> 0) & Type.ANY);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeUTF(java.lang.String r11) throws java.io.IOException {
        /*
        r10 = this;
        r9 = 2047; // 0x7ff float:2.868E-42 double:1.0114E-320;
        r8 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r7 = 1;
        r3 = r11.length();
        r1 = 0;
        r2 = 0;
    L_0x000b:
        if (r2 >= r3) goto L_0x0022;
    L_0x000d:
        r0 = r11.charAt(r2);
        if (r0 < r7) goto L_0x001a;
    L_0x0013:
        if (r0 > r8) goto L_0x001a;
    L_0x0015:
        r1 = r1 + 1;
    L_0x0017:
        r2 = r2 + 1;
        goto L_0x000b;
    L_0x001a:
        if (r0 <= r9) goto L_0x001f;
    L_0x001c:
        r1 = r1 + 3;
        goto L_0x0017;
    L_0x001f:
        r1 = r1 + 2;
        goto L_0x0017;
    L_0x0022:
        r4 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r1 <= r4) goto L_0x0046;
    L_0x0027:
        r4 = new java.io.UTFDataFormatException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "encoded string too long: ";
        r5 = r5.append(r6);
        r5 = r5.append(r1);
        r6 = " bytes";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.<init>(r5);
        throw r4;
    L_0x0046:
        r4 = r10.pos;
        r4 = r4 + r1;
        r4 = r4 + 2;
        r10.ensureEnoughBuffer(r4);
        r10.writeShort(r1);
        r2 = 0;
        r2 = 0;
    L_0x0053:
        if (r2 >= r3) goto L_0x005d;
    L_0x0055:
        r0 = r11.charAt(r2);
        if (r0 < r7) goto L_0x005d;
    L_0x005b:
        if (r0 <= r8) goto L_0x0075;
    L_0x005d:
        if (r2 >= r3) goto L_0x00dc;
    L_0x005f:
        r0 = r11.charAt(r2);
        if (r0 < r7) goto L_0x0083;
    L_0x0065:
        if (r0 > r8) goto L_0x0083;
    L_0x0067:
        r4 = r10.buf;
        r5 = r10.pos;
        r6 = r5 + 1;
        r10.pos = r6;
        r6 = (byte) r0;
        r4[r5] = r6;
    L_0x0072:
        r2 = r2 + 1;
        goto L_0x005d;
    L_0x0075:
        r4 = r10.buf;
        r5 = r10.pos;
        r6 = r5 + 1;
        r10.pos = r6;
        r6 = (byte) r0;
        r4[r5] = r6;
        r2 = r2 + 1;
        goto L_0x0053;
    L_0x0083:
        if (r0 <= r9) goto L_0x00b9;
    L_0x0085:
        r4 = r10.buf;
        r5 = r10.pos;
        r6 = r5 + 1;
        r10.pos = r6;
        r6 = r0 >> 12;
        r6 = r6 & 15;
        r6 = r6 | 224;
        r6 = (byte) r6;
        r4[r5] = r6;
        r4 = r10.buf;
        r5 = r10.pos;
        r6 = r5 + 1;
        r10.pos = r6;
        r6 = r0 >> 6;
        r6 = r6 & 63;
        r6 = r6 | 128;
        r6 = (byte) r6;
        r4[r5] = r6;
        r4 = r10.buf;
        r5 = r10.pos;
        r6 = r5 + 1;
        r10.pos = r6;
        r6 = r0 >> 0;
        r6 = r6 & 63;
        r6 = r6 | 128;
        r6 = (byte) r6;
        r4[r5] = r6;
        goto L_0x0072;
    L_0x00b9:
        r4 = r10.buf;
        r5 = r10.pos;
        r6 = r5 + 1;
        r10.pos = r6;
        r6 = r0 >> 6;
        r6 = r6 & 31;
        r6 = r6 | 192;
        r6 = (byte) r6;
        r4[r5] = r6;
        r4 = r10.buf;
        r5 = r10.pos;
        r6 = r5 + 1;
        r10.pos = r6;
        r6 = r0 >> 0;
        r6 = r6 & 63;
        r6 = r6 | 128;
        r6 = (byte) r6;
        r4[r5] = r6;
        goto L_0x0072;
    L_0x00dc:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.util.DataByteArrayOutputStream.writeUTF(java.lang.String):void");
    }

    private void ensureEnoughBuffer(int newcount) {
        if (newcount > this.buf.length) {
            byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
            System.arraycopy(this.buf, 0, newbuf, 0, this.pos);
            this.buf = newbuf;
        }
    }
}
