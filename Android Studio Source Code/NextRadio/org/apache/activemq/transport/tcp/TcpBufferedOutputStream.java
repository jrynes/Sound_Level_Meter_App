package org.apache.activemq.transport.tcp;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TcpBufferedOutputStream extends FilterOutputStream implements TimeStampStream {
    private static final int BUFFER_SIZE = 8192;
    private byte[] buffer;
    private int bufferlen;
    private int count;
    private volatile long writeTimestamp;

    public TcpBufferedOutputStream(OutputStream out) {
        this(out, BUFFER_SIZE);
    }

    public TcpBufferedOutputStream(OutputStream out, int size) {
        super(out);
        this.writeTimestamp = -1;
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.buffer = new byte[size];
        this.bufferlen = size;
    }

    public void write(int b) throws IOException {
        if (this.bufferlen - this.count < 1) {
            flush();
        }
        byte[] bArr = this.buffer;
        int i = this.count;
        this.count = i + 1;
        bArr[i] = (byte) b;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (b != null) {
            if (this.bufferlen - this.count < len) {
                flush();
            }
            if (this.buffer.length >= len) {
                System.arraycopy(b, off, this.buffer, this.count, len);
                this.count += len;
                return;
            }
            try {
                this.writeTimestamp = System.currentTimeMillis();
                this.out.write(b, off, len);
            } finally {
                this.writeTimestamp = -1;
            }
        }
    }

    public void flush() throws IOException {
        if (this.count > 0 && this.out != null) {
            try {
                this.writeTimestamp = System.currentTimeMillis();
                this.out.write(this.buffer, 0, this.count);
                this.count = 0;
            } finally {
                this.writeTimestamp = -1;
            }
        }
    }

    public void close() throws IOException {
        super.close();
    }

    public boolean isWriting() {
        return this.writeTimestamp > 0;
    }

    public long getWriteTimestamp() {
        return this.writeTimestamp;
    }
}
