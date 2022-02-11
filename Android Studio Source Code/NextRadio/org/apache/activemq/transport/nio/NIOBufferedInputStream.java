package org.apache.activemq.transport.nio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import org.xbill.DNS.Type;

public class NIOBufferedInputStream extends InputStream {
    private static final int BUFFER_SIZE = 8192;
    private ByteBuffer bb;
    private Selector rs;
    private SocketChannel sc;

    public NIOBufferedInputStream(ReadableByteChannel channel, int size) throws ClosedChannelException, IOException {
        this.sc = null;
        this.bb = null;
        this.rs = null;
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.bb = ByteBuffer.allocateDirect(size);
        this.sc = (SocketChannel) channel;
        this.sc.configureBlocking(false);
        this.rs = Selector.open();
        this.sc.register(this.rs, 1);
        this.bb.position(0);
        this.bb.limit(0);
    }

    public NIOBufferedInputStream(ReadableByteChannel channel) throws ClosedChannelException, IOException {
        this(channel, BUFFER_SIZE);
    }

    public int available() throws IOException {
        if (this.rs.isOpen()) {
            return this.bb.remaining();
        }
        throw new IOException("Input Stream Closed");
    }

    public void close() throws IOException {
        if (this.rs.isOpen()) {
            this.rs.close();
            if (this.sc.isOpen()) {
                this.sc.socket().shutdownInput();
                this.sc.socket().close();
            }
            this.bb = null;
            this.sc = null;
        }
    }

    public int read() throws IOException {
        if (this.rs.isOpen()) {
            if (!this.bb.hasRemaining()) {
                try {
                    fill(1);
                } catch (ClosedChannelException e) {
                    close();
                    return -1;
                }
            }
            return this.bb.get() & Type.ANY;
        }
        throw new IOException("Input Stream Closed");
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int bytesCopied = -1;
        if (this.rs.isOpen()) {
            while (bytesCopied == -1) {
                if (this.bb.hasRemaining()) {
                    bytesCopied = len < this.bb.remaining() ? len : this.bb.remaining();
                    this.bb.get(b, off, bytesCopied);
                } else {
                    try {
                        fill(1);
                    } catch (ClosedChannelException e) {
                        close();
                        return -1;
                    }
                }
            }
            return bytesCopied;
        }
        throw new IOException("Input Stream Closed");
    }

    public long skip(long n) throws IOException {
        long skiped = 0;
        if (this.rs.isOpen()) {
            while (n > 0) {
                if (n <= ((long) this.bb.remaining())) {
                    skiped += n;
                    this.bb.position(this.bb.position() + ((int) n));
                    n = 0;
                } else {
                    skiped += (long) this.bb.remaining();
                    n -= (long) this.bb.remaining();
                    this.bb.position(this.bb.limit());
                    try {
                        fill((int) n);
                    } catch (ClosedChannelException e) {
                        close();
                        return skiped;
                    }
                }
            }
            return skiped;
        }
        throw new IOException("Input Stream Closed");
    }

    private void fill(int n) throws IOException, ClosedChannelException {
        if (n > 0 && n > this.bb.remaining()) {
            this.bb.compact();
            if (this.bb.remaining() < n) {
                n = this.bb.remaining();
            }
            while (true) {
                int bytesRead = this.sc.read(this.bb);
                if (bytesRead == -1) {
                    break;
                }
                n -= bytesRead;
                if (n <= 0) {
                    this.bb.flip();
                    return;
                } else {
                    this.rs.select(0);
                    this.rs.selectedKeys().clear();
                }
            }
            throw new ClosedChannelException();
        }
    }
}
