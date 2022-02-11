package org.apache.activemq.transport.tcp;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xbill.DNS.Type;

public class TcpBufferedInputStream extends FilterInputStream {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    protected int count;
    protected byte[] internalBuffer;
    protected int position;

    public TcpBufferedInputStream(InputStream in) {
        this(in, DEFAULT_BUFFER_SIZE);
    }

    public TcpBufferedInputStream(InputStream in, int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.internalBuffer = new byte[size];
    }

    protected void fill() throws IOException {
        byte[] buffer = this.internalBuffer;
        this.count = 0;
        this.position = 0;
        int n = this.in.read(buffer, this.position, buffer.length - this.position);
        if (n > 0) {
            this.count = this.position + n;
        }
    }

    public int read() throws IOException {
        if (this.position >= this.count) {
            fill();
            if (this.position >= this.count) {
                return -1;
            }
        }
        byte[] bArr = this.internalBuffer;
        int i = this.position;
        this.position = i + 1;
        return bArr[i] & Type.ANY;
    }

    private int readStream(byte[] b, int off, int len) throws IOException {
        int cnt;
        int avail = this.count - this.position;
        if (avail <= 0) {
            if (len >= this.internalBuffer.length) {
                return this.in.read(b, off, len);
            }
            fill();
            avail = this.count - this.position;
            if (avail <= 0) {
                return -1;
            }
        }
        if (avail < len) {
            cnt = avail;
        } else {
            cnt = len;
        }
        System.arraycopy(this.internalBuffer, this.position, b, off, cnt);
        this.position += cnt;
        return cnt;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if ((((off | len) | (off + len)) | (b.length - (off + len))) < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        } else {
            int nread;
            int n = 0;
            while (true) {
                nread = readStream(b, off + n, len - n);
                if (nread <= 0) {
                    break;
                }
                n += nread;
                if (n >= len) {
                    return n;
                }
                InputStream input = this.in;
                if (input != null && input.available() <= 0) {
                    return n;
                }
            }
            if (n != 0) {
                nread = n;
            }
            return nread;
        }
    }

    public long skip(long n) throws IOException {
        if (n <= 0) {
            return 0;
        }
        long avail = (long) (this.count - this.position);
        if (avail <= 0) {
            return this.in.skip(n);
        }
        long skipped;
        if (avail < n) {
            skipped = avail;
        } else {
            skipped = n;
        }
        this.position = (int) (((long) this.position) + skipped);
        return skipped;
    }

    public int available() throws IOException {
        return this.in.available() + (this.count - this.position);
    }

    public boolean markSupported() {
        return false;
    }

    public void close() throws IOException {
        if (this.in != null) {
            this.in.close();
        }
    }
}
