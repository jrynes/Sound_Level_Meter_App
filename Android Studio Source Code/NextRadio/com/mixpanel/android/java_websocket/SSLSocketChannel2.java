package com.mixpanel.android.java_websocket;

import android.annotation.SuppressLint;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

@SuppressLint({"Assert"})
public class SSLSocketChannel2 implements ByteChannel, WrappedByteChannel {
    static final /* synthetic */ boolean $assertionsDisabled;
    protected static ByteBuffer emptybuffer;
    protected int bufferallocations;
    protected ExecutorService exec;
    protected ByteBuffer inCrypt;
    protected ByteBuffer inData;
    protected ByteBuffer outCrypt;
    protected SSLEngineResult readEngineResult;
    protected SelectionKey selectionKey;
    protected SocketChannel socketChannel;
    protected SSLEngine sslEngine;
    protected List<Future<?>> tasks;
    protected SSLEngineResult writeEngineResult;

    static {
        $assertionsDisabled = !SSLSocketChannel2.class.desiredAssertionStatus();
        emptybuffer = ByteBuffer.allocate(0);
    }

    public SSLSocketChannel2(SocketChannel channel, SSLEngine sslEngine, ExecutorService exec, SelectionKey key) throws IOException {
        this.bufferallocations = 0;
        if (channel == null || sslEngine == null || exec == null) {
            throw new IllegalArgumentException("parameter must not be null");
        }
        this.socketChannel = channel;
        this.sslEngine = sslEngine;
        this.exec = exec;
        SSLEngineResult sSLEngineResult = new SSLEngineResult(Status.BUFFER_UNDERFLOW, sslEngine.getHandshakeStatus(), 0, 0);
        this.writeEngineResult = sSLEngineResult;
        this.readEngineResult = sSLEngineResult;
        this.tasks = new ArrayList(3);
        if (key != null) {
            key.interestOps(key.interestOps() | 4);
            this.selectionKey = key;
        }
        createBuffers(sslEngine.getSession());
        this.socketChannel.write(wrap(emptybuffer));
        processHandshake();
    }

    private void consumeFutureUninterruptible(Future<?> f) {
        boolean interrupted = false;
        while (true) {
            try {
                f.get();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
        if (interrupted) {
            try {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    private synchronized void processHandshake() throws IOException {
        if (this.sslEngine.getHandshakeStatus() != HandshakeStatus.NOT_HANDSHAKING) {
            if (!this.tasks.isEmpty()) {
                Iterator<Future<?>> it = this.tasks.iterator();
                while (it.hasNext()) {
                    Future<?> f = (Future) it.next();
                    if (f.isDone()) {
                        it.remove();
                    } else if (isBlocking()) {
                        consumeFutureUninterruptible(f);
                    }
                }
            }
            if (this.sslEngine.getHandshakeStatus() == HandshakeStatus.NEED_UNWRAP) {
                if (!isBlocking() || this.readEngineResult.getStatus() == Status.BUFFER_UNDERFLOW) {
                    this.inCrypt.compact();
                    if (this.socketChannel.read(this.inCrypt) == -1) {
                        throw new IOException("connection closed unexpectedly by peer");
                    }
                    this.inCrypt.flip();
                }
                this.inData.compact();
                unwrap();
                if (this.readEngineResult.getHandshakeStatus() == HandshakeStatus.FINISHED) {
                    createBuffers(this.sslEngine.getSession());
                }
            }
            consumeDelegatedTasks();
            if (this.tasks.isEmpty() || this.sslEngine.getHandshakeStatus() == HandshakeStatus.NEED_WRAP) {
                this.socketChannel.write(wrap(emptybuffer));
                if (this.writeEngineResult.getHandshakeStatus() == HandshakeStatus.FINISHED) {
                    createBuffers(this.sslEngine.getSession());
                }
            }
            if ($assertionsDisabled || this.sslEngine.getHandshakeStatus() != HandshakeStatus.NOT_HANDSHAKING) {
                this.bufferallocations = 1;
            } else {
                throw new AssertionError();
            }
        }
    }

    private synchronized ByteBuffer wrap(ByteBuffer b) throws SSLException {
        this.outCrypt.compact();
        this.writeEngineResult = this.sslEngine.wrap(b, this.outCrypt);
        this.outCrypt.flip();
        return this.outCrypt;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized java.nio.ByteBuffer unwrap() throws javax.net.ssl.SSLException {
        /*
        r4 = this;
        monitor-enter(r4);
    L_0x0001:
        r1 = r4.inData;	 Catch:{ all -> 0x0038 }
        r0 = r1.remaining();	 Catch:{ all -> 0x0038 }
        r1 = r4.sslEngine;	 Catch:{ all -> 0x0038 }
        r2 = r4.inCrypt;	 Catch:{ all -> 0x0038 }
        r3 = r4.inData;	 Catch:{ all -> 0x0038 }
        r1 = r1.unwrap(r2, r3);	 Catch:{ all -> 0x0038 }
        r4.readEngineResult = r1;	 Catch:{ all -> 0x0038 }
        r1 = r4.readEngineResult;	 Catch:{ all -> 0x0038 }
        r1 = r1.getStatus();	 Catch:{ all -> 0x0038 }
        r2 = javax.net.ssl.SSLEngineResult.Status.OK;	 Catch:{ all -> 0x0038 }
        if (r1 != r2) goto L_0x002f;
    L_0x001d:
        r1 = r4.inData;	 Catch:{ all -> 0x0038 }
        r1 = r1.remaining();	 Catch:{ all -> 0x0038 }
        if (r0 != r1) goto L_0x0001;
    L_0x0025:
        r1 = r4.sslEngine;	 Catch:{ all -> 0x0038 }
        r1 = r1.getHandshakeStatus();	 Catch:{ all -> 0x0038 }
        r2 = javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_UNWRAP;	 Catch:{ all -> 0x0038 }
        if (r1 == r2) goto L_0x0001;
    L_0x002f:
        r1 = r4.inData;	 Catch:{ all -> 0x0038 }
        r1.flip();	 Catch:{ all -> 0x0038 }
        r1 = r4.inData;	 Catch:{ all -> 0x0038 }
        monitor-exit(r4);
        return r1;
    L_0x0038:
        r1 = move-exception;
        monitor-exit(r4);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.java_websocket.SSLSocketChannel2.unwrap():java.nio.ByteBuffer");
    }

    protected void consumeDelegatedTasks() {
        while (true) {
            Runnable task = this.sslEngine.getDelegatedTask();
            if (task != null) {
                this.tasks.add(this.exec.submit(task));
            } else {
                return;
            }
        }
    }

    protected void createBuffers(SSLSession session) {
        int appBufferMax = session.getApplicationBufferSize();
        int netBufferMax = session.getPacketBufferSize();
        if (this.inData == null) {
            this.inData = ByteBuffer.allocate(appBufferMax);
            this.outCrypt = ByteBuffer.allocate(netBufferMax);
            this.inCrypt = ByteBuffer.allocate(netBufferMax);
        } else {
            if (this.inData.capacity() != appBufferMax) {
                this.inData = ByteBuffer.allocate(appBufferMax);
            }
            if (this.outCrypt.capacity() != netBufferMax) {
                this.outCrypt = ByteBuffer.allocate(netBufferMax);
            }
            if (this.inCrypt.capacity() != netBufferMax) {
                this.inCrypt = ByteBuffer.allocate(netBufferMax);
            }
        }
        this.inData.rewind();
        this.inData.flip();
        this.inCrypt.rewind();
        this.inCrypt.flip();
        this.outCrypt.rewind();
        this.outCrypt.flip();
        this.bufferallocations++;
    }

    public int write(ByteBuffer src) throws IOException {
        if (isHandShakeComplete()) {
            if (this.bufferallocations <= 1) {
                createBuffers(this.sslEngine.getSession());
            }
            return this.socketChannel.write(wrap(src));
        }
        processHandshake();
        return 0;
    }

    public int read(ByteBuffer dst) throws IOException {
        if (!dst.hasRemaining()) {
            return 0;
        }
        if (!isHandShakeComplete()) {
            if (isBlocking()) {
                while (!isHandShakeComplete()) {
                    processHandshake();
                }
            } else {
                processHandshake();
                if (!isHandShakeComplete()) {
                    return 0;
                }
            }
        }
        if (this.bufferallocations <= 1) {
            createBuffers(this.sslEngine.getSession());
        }
        int purged = readRemaining(dst);
        if (purged != 0) {
            return purged;
        }
        if ($assertionsDisabled || this.inData.position() == 0) {
            this.inData.clear();
            if (this.inCrypt.hasRemaining()) {
                this.inCrypt.compact();
            } else {
                this.inCrypt.clear();
            }
            if ((isBlocking() || this.readEngineResult.getStatus() == Status.BUFFER_UNDERFLOW) && this.socketChannel.read(this.inCrypt) == -1) {
                return -1;
            }
            this.inCrypt.flip();
            unwrap();
            int transfered = transfereTo(this.inData, dst);
            return (transfered == 0 && isBlocking()) ? read(dst) : transfered;
        } else {
            throw new AssertionError();
        }
    }

    private int readRemaining(ByteBuffer dst) throws SSLException {
        if (this.inData.hasRemaining()) {
            return transfereTo(this.inData, dst);
        }
        if (!this.inData.hasRemaining()) {
            this.inData.clear();
        }
        if (this.inCrypt.hasRemaining()) {
            unwrap();
            int amount = transfereTo(this.inData, dst);
            if (amount > 0) {
                return amount;
            }
        }
        return 0;
    }

    public boolean isConnected() {
        return this.socketChannel.isConnected();
    }

    public void close() throws IOException {
        this.sslEngine.closeOutbound();
        this.sslEngine.getSession().invalidate();
        if (this.socketChannel.isOpen()) {
            this.socketChannel.write(wrap(emptybuffer));
        }
        this.socketChannel.close();
        this.exec.shutdownNow();
    }

    private boolean isHandShakeComplete() {
        HandshakeStatus status = this.sslEngine.getHandshakeStatus();
        return status == HandshakeStatus.FINISHED || status == HandshakeStatus.NOT_HANDSHAKING;
    }

    public SelectableChannel configureBlocking(boolean b) throws IOException {
        return this.socketChannel.configureBlocking(b);
    }

    public boolean connect(SocketAddress remote) throws IOException {
        return this.socketChannel.connect(remote);
    }

    public boolean finishConnect() throws IOException {
        return this.socketChannel.finishConnect();
    }

    public Socket socket() {
        return this.socketChannel.socket();
    }

    public boolean isInboundDone() {
        return this.sslEngine.isInboundDone();
    }

    public boolean isOpen() {
        return this.socketChannel.isOpen();
    }

    public boolean isNeedWrite() {
        return this.outCrypt.hasRemaining() || !isHandShakeComplete();
    }

    public void writeMore() throws IOException {
        write(this.outCrypt);
    }

    public boolean isNeedRead() {
        return this.inData.hasRemaining() || !(!this.inCrypt.hasRemaining() || this.readEngineResult.getStatus() == Status.BUFFER_UNDERFLOW || this.readEngineResult.getStatus() == Status.CLOSED);
    }

    public int readMore(ByteBuffer dst) throws SSLException {
        return readRemaining(dst);
    }

    private int transfereTo(ByteBuffer from, ByteBuffer to) {
        int fremain = from.remaining();
        int toremain = to.remaining();
        if (fremain > toremain) {
            int min = Math.min(fremain, toremain);
            for (int i = 0; i < min; i++) {
                to.put(from.get());
            }
            return min;
        }
        to.put(from);
        return fremain;
    }

    public boolean isBlocking() {
        return this.socketChannel.isBlocking();
    }
}
