package org.apache.activemq.transport.nio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import javax.net.ssl.SSLEngine;
import org.apache.activemq.transport.tcp.TimeStampStream;

public class NIOOutputStream extends OutputStream implements TimeStampStream {
    private static final int BUFFER_SIZE = 8192;
    private final byte[] buffer;
    private final ByteBuffer byteBuffer;
    private boolean closed;
    private int count;
    private SSLEngine engine;
    private final WritableByteChannel out;
    private volatile long writeTimestamp;

    public NIOOutputStream(WritableByteChannel out) {
        this(out, BUFFER_SIZE);
    }

    public NIOOutputStream(WritableByteChannel out, int size) {
        this.writeTimestamp = -1;
        this.out = out;
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.buffer = new byte[size];
        this.byteBuffer = ByteBuffer.wrap(this.buffer);
    }

    public void write(int b) throws IOException {
        checkClosed();
        if (availableBufferToWrite() < 1) {
            flush();
        }
        byte[] bArr = this.buffer;
        int i = this.count;
        this.count = i + 1;
        bArr[i] = (byte) b;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        checkClosed();
        if (availableBufferToWrite() < len) {
            flush();
        }
        if (this.buffer.length >= len) {
            System.arraycopy(b, off, this.buffer, this.count, len);
            this.count += len;
            return;
        }
        write(ByteBuffer.wrap(b, off, len));
    }

    public void flush() throws IOException {
        if (this.count > 0 && this.out != null) {
            this.byteBuffer.position(0);
            this.byteBuffer.limit(this.count);
            write(this.byteBuffer);
            this.count = 0;
        }
    }

    public void close() throws IOException {
        super.close();
        if (this.engine != null) {
            this.engine.closeOutbound();
        }
        this.closed = true;
    }

    protected void checkClosed() throws IOException {
        if (this.closed) {
            throw new EOFException("Cannot write to the stream any more it has already been closed");
        }
    }

    private int availableBufferToWrite() {
        return this.buffer.length - this.count;
    }

    protected void write(ByteBuffer data) throws IOException {
        ByteBuffer plain;
        if (this.engine != null) {
            plain = ByteBuffer.allocate(this.engine.getSession().getPacketBufferSize());
            plain.clear();
            this.engine.wrap(data, plain);
            plain.flip();
        } else {
            plain = data;
        }
        int remaining = plain.remaining();
        int lastRemaining = remaining - 1;
        long delay = 1;
        try {
            this.writeTimestamp = System.currentTimeMillis();
            while (remaining > 0) {
                if (remaining == lastRemaining) {
                    Thread.sleep(delay);
                    delay *= 2;
                    if (delay > 1000) {
                        delay = 1000;
                    }
                } else {
                    delay = 1;
                }
                lastRemaining = remaining;
                this.out.write(plain);
                remaining = data.remaining();
                if (!(this.engine == null || !data.hasRemaining() || plain.hasRemaining())) {
                    plain.clear();
                    this.engine.wrap(data, plain);
                    plain.flip();
                }
            }
            this.writeTimestamp = -1;
        } catch (InterruptedException e) {
            throw new InterruptedIOException();
        } catch (Throwable th) {
            this.writeTimestamp = -1;
        }
    }

    public boolean isWriting() {
        return this.writeTimestamp > 0;
    }

    public long getWriteTimestamp() {
        return this.writeTimestamp;
    }

    public void setEngine(SSLEngine engine) {
        this.engine = engine;
    }
}
