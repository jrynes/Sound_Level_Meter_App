package org.apache.activemq.util;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public final class DataByteArrayInputStream extends InputStream implements DataInput {
    private byte[] buf;
    private int offset;
    private int pos;

    public DataByteArrayInputStream(byte[] buf) {
        this.buf = buf;
        this.pos = 0;
        this.offset = 0;
    }

    public DataByteArrayInputStream(ByteSequence sequence) {
        this.buf = sequence.getData();
        this.offset = sequence.getOffset();
        this.pos = this.offset;
    }

    public DataByteArrayInputStream() {
        this(new byte[0]);
    }

    public int size() {
        return this.pos - this.offset;
    }

    public byte[] getRawData() {
        return this.buf;
    }

    public void restart(byte[] newBuff) {
        this.buf = newBuff;
        this.pos = 0;
    }

    public void restart(ByteSequence sequence) {
        this.buf = sequence.getData();
        this.pos = sequence.getOffset();
    }

    public void restart(int size) {
        if (this.buf == null || this.buf.length < size) {
            this.buf = new byte[size];
        }
        restart(this.buf);
    }

    public int read() {
        if (this.pos >= this.buf.length) {
            return -1;
        }
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & Type.ANY;
    }

    public int read(byte[] b, int off, int len) {
        if (b == null) {
            throw new NullPointerException();
        } else if (this.pos >= this.buf.length) {
            return -1;
        } else {
            if (this.pos + len > this.buf.length) {
                len = this.buf.length - this.pos;
            }
            if (len <= 0) {
                return 0;
            }
            System.arraycopy(this.buf, this.pos, b, off, len);
            this.pos += len;
            return len;
        }
    }

    public int available() {
        return this.buf.length - this.pos;
    }

    public void readFully(byte[] b) {
        read(b, 0, b.length);
    }

    public void readFully(byte[] b, int off, int len) {
        read(b, off, len);
    }

    public int skipBytes(int n) {
        if (this.pos + n > this.buf.length) {
            n = this.buf.length - this.pos;
        }
        if (n < 0) {
            return 0;
        }
        this.pos += n;
        return n;
    }

    public boolean readBoolean() {
        return read() != 0;
    }

    public byte readByte() {
        return (byte) read();
    }

    public int readUnsignedByte() {
        return read();
    }

    public short readShort() {
        return (short) ((read() << 8) + (read() << 0));
    }

    public int readUnsignedShort() {
        return (read() << 8) + (read() << 0);
    }

    public char readChar() {
        return (char) ((read() << 8) + (read() << 0));
    }

    public int readInt() {
        return (((read() << 24) + (read() << 16)) + (read() << 8)) + (read() << 0);
    }

    public long readLong() {
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        long j = ((long) bArr[i]) << 56;
        byte[] bArr2 = this.buf;
        int i2 = this.pos;
        this.pos = i2 + 1;
        j += ((long) (bArr2[i2] & Type.ANY)) << 48;
        bArr2 = this.buf;
        i2 = this.pos;
        this.pos = i2 + 1;
        j += ((long) (bArr2[i2] & Type.ANY)) << 40;
        bArr2 = this.buf;
        i2 = this.pos;
        this.pos = i2 + 1;
        long rc = j + (((long) (bArr2[i2] & Type.ANY)) << 32);
        bArr = this.buf;
        i = this.pos;
        this.pos = i + 1;
        j = (((long) (bArr[i] & Type.ANY)) << 24) + rc;
        bArr2 = this.buf;
        i2 = this.pos;
        this.pos = i2 + 1;
        j += (long) ((bArr2[i2] & Type.ANY) << 16);
        bArr2 = this.buf;
        i2 = this.pos;
        this.pos = i2 + 1;
        j += (long) ((bArr2[i2] & Type.ANY) << 8);
        bArr2 = this.buf;
        i2 = this.pos;
        this.pos = i2 + 1;
        return j + ((long) ((bArr2[i2] & Type.ANY) << 0));
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public String readLine() {
        int start = this.pos;
        while (this.pos < this.buf.length) {
            int c = read();
            if (c == 10) {
                break;
            } else if (c == 13) {
                c = read();
                if (!(c == 10 || c == -1)) {
                    this.pos--;
                }
            }
        }
        return new String(this.buf, start, this.pos);
    }

    public String readUTF() throws IOException {
        int length = readUnsignedShort();
        char[] characters = new char[length];
        int count = 0;
        int total = this.pos + length;
        while (this.pos < total) {
            int c = this.buf[this.pos] & Type.ANY;
            if (c > Service.LOCUS_CON) {
                break;
            }
            this.pos++;
            int count2 = count + 1;
            characters[count] = (char) c;
            count = count2;
        }
        while (this.pos < total) {
            c = this.buf[this.pos] & Type.ANY;
            int c2;
            switch (c >> 4) {
                case Tokenizer.EOF /*0*/:
                case Zone.PRIMARY /*1*/:
                case Zone.SECONDARY /*2*/:
                case Protocol.GGP /*3*/:
                case Type.MF /*4*/:
                case Service.RJE /*5*/:
                case Protocol.TCP /*6*/:
                case Service.ECHO /*7*/:
                    this.pos++;
                    count2 = count + 1;
                    characters[count] = (char) c;
                    count = count2;
                    break;
                case Protocol.PUP /*12*/:
                case Service.DAYTIME /*13*/:
                    this.pos += 2;
                    if (this.pos <= total) {
                        c2 = this.buf[this.pos - 1];
                        if ((c2 & 192) == Flags.FLAG8) {
                            count2 = count + 1;
                            characters[count] = (char) (((c & 31) << 6) | (c2 & 63));
                            count = count2;
                            break;
                        }
                        throw new UTFDataFormatException("bad string");
                    }
                    throw new UTFDataFormatException("bad string");
                case Protocol.EMCON /*14*/:
                    this.pos += 3;
                    if (this.pos <= total) {
                        c2 = this.buf[this.pos - 2];
                        int c3 = this.buf[this.pos - 1];
                        if ((c2 & 192) == Flags.FLAG8 && (c3 & 192) == Flags.FLAG8) {
                            count2 = count + 1;
                            characters[count] = (char) ((((c & 15) << 12) | ((c2 & 63) << 6)) | ((c3 & 63) << 0));
                            count = count2;
                            break;
                        }
                        throw new UTFDataFormatException("bad string");
                    }
                    throw new UTFDataFormatException("bad string");
                    break;
                default:
                    throw new UTFDataFormatException("bad string");
            }
        }
        return new String(characters, 0, count);
    }
}
