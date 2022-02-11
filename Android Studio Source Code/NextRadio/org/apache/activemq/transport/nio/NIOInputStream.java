package org.apache.activemq.transport.nio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import org.xbill.DNS.Type;

public class NIOInputStream extends InputStream {
    protected int count;
    private final ByteBuffer in;
    protected int position;

    public NIOInputStream(ByteBuffer in) {
        this.in = in;
    }

    public int read() throws IOException {
        try {
            return this.in.get() & Type.ANY;
        } catch (BufferUnderflowException e) {
            return -1;
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.in.hasRemaining()) {
            int rc = Math.min(len, this.in.remaining());
            this.in.get(b, off, rc);
            return rc;
        }
        return len == 0 ? 0 : -1;
    }

    public long skip(long n) throws IOException {
        int rc = Math.min((int) n, this.in.remaining());
        this.in.position(this.in.position() + rc);
        return (long) rc;
    }

    public int available() throws IOException {
        return this.in.remaining();
    }

    public boolean markSupported() {
        return false;
    }

    public void close() throws IOException {
    }
}
