package org.apache.activemq.util;

import java.io.IOException;
import java.io.InputStream;
import org.xbill.DNS.Type;

public class ByteArrayInputStream extends InputStream {
    byte[] buffer;
    int limit;
    int mark;
    int pos;

    public ByteArrayInputStream(byte[] data) {
        this(data, 0, data.length);
    }

    public ByteArrayInputStream(ByteSequence sequence) {
        this(sequence.getData(), sequence.getOffset(), sequence.getLength());
    }

    public ByteArrayInputStream(byte[] data, int offset, int size) {
        this.buffer = data;
        this.mark = offset;
        this.pos = offset;
        this.limit = offset + size;
    }

    public int read() throws IOException {
        if (this.pos >= this.limit) {
            return -1;
        }
        byte[] bArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & Type.ANY;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) {
        if (this.pos >= this.limit) {
            return -1;
        }
        len = Math.min(len, this.limit - this.pos);
        if (len > 0) {
            System.arraycopy(this.buffer, this.pos, b, off, len);
            this.pos += len;
        }
        return len;
    }

    public long skip(long len) throws IOException {
        if (this.pos >= this.limit) {
            return -1;
        }
        len = Math.min(len, (long) (this.limit - this.pos));
        if (len > 0) {
            this.pos = (int) (((long) this.pos) + len);
        }
        return len;
    }

    public int available() {
        return this.limit - this.pos;
    }

    public boolean markSupported() {
        return true;
    }

    public void mark(int markpos) {
        this.mark = this.pos;
    }

    public void reset() {
        this.pos = this.mark;
    }
}
