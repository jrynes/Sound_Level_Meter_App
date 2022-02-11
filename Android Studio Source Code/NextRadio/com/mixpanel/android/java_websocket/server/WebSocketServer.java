package com.mixpanel.android.java_websocket.server;

import android.annotation.SuppressLint;
import com.mixpanel.android.java_websocket.SocketChannelIOHelper;
import com.mixpanel.android.java_websocket.WebSocket;
import com.mixpanel.android.java_websocket.WebSocketAdapter;
import com.mixpanel.android.java_websocket.WebSocketFactory;
import com.mixpanel.android.java_websocket.WebSocketImpl;
import com.mixpanel.android.java_websocket.WrappedByteChannel;
import com.mixpanel.android.java_websocket.drafts.Draft;
import com.mixpanel.android.java_websocket.exceptions.InvalidDataException;
import com.mixpanel.android.java_websocket.framing.CloseFrame;
import com.mixpanel.android.java_websocket.framing.Framedata;
import com.mixpanel.android.java_websocket.handshake.ClientHandshake;
import com.mixpanel.android.java_websocket.handshake.Handshakedata;
import com.mixpanel.android.java_websocket.handshake.ServerHandshakeBuilder;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressLint({"Assert"})
public abstract class WebSocketServer extends WebSocketAdapter implements Runnable {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static int DECODERS;
    private final InetSocketAddress address;
    private BlockingQueue<ByteBuffer> buffers;
    private final Collection<WebSocket> connections;
    private List<WebSocketWorker> decoders;
    private List<Draft> drafts;
    private List<WebSocketImpl> iqueue;
    private volatile AtomicBoolean isclosed;
    private int queueinvokes;
    private AtomicInteger queuesize;
    private Selector selector;
    private Thread selectorthread;
    private ServerSocketChannel server;
    private WebSocketServerFactory wsf;

    public interface WebSocketServerFactory extends WebSocketFactory {
        WebSocketImpl createWebSocket(WebSocketAdapter webSocketAdapter, Draft draft, Socket socket);

        WebSocketImpl createWebSocket(WebSocketAdapter webSocketAdapter, List<Draft> list, Socket socket);

        ByteChannel wrapChannel(SocketChannel socketChannel, SelectionKey selectionKey) throws IOException;
    }

    public class WebSocketWorker extends Thread {
        static final /* synthetic */ boolean $assertionsDisabled;
        private BlockingQueue<WebSocketImpl> iqueue;

        /* renamed from: com.mixpanel.android.java_websocket.server.WebSocketServer.WebSocketWorker.1 */
        class C10761 implements UncaughtExceptionHandler {
            final /* synthetic */ WebSocketServer val$this$0;

            C10761(WebSocketServer webSocketServer) {
                this.val$this$0 = webSocketServer;
            }

            public void uncaughtException(Thread t, Throwable e) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, e);
            }
        }

        static {
            $assertionsDisabled = !WebSocketServer.class.desiredAssertionStatus();
        }

        public WebSocketWorker() {
            this.iqueue = new LinkedBlockingQueue();
            setName("WebSocketWorker-" + getId());
            setUncaughtExceptionHandler(new C10761(WebSocketServer.this));
        }

        public void put(WebSocketImpl ws) throws InterruptedException {
            this.iqueue.put(ws);
        }

        public void run() {
            WebSocketImpl ws = null;
            while (true) {
                ByteBuffer buf;
                try {
                    ws = (WebSocketImpl) this.iqueue.take();
                    buf = (ByteBuffer) ws.inQueue.poll();
                    if (!$assertionsDisabled && buf == null) {
                        break;
                    }
                    ws.decode(buf);
                    WebSocketServer.this.pushBuffer(buf);
                } catch (InterruptedException e) {
                    return;
                } catch (RuntimeException e2) {
                    WebSocketServer.this.handleFatal(ws, e2);
                    return;
                } catch (Throwable th) {
                    WebSocketServer.this.pushBuffer(buf);
                }
            }
            throw new AssertionError();
        }
    }

    public abstract void onClose(WebSocket webSocket, int i, String str, boolean z);

    public abstract void onError(WebSocket webSocket, Exception exception);

    public abstract void onMessage(WebSocket webSocket, String str);

    public abstract void onOpen(WebSocket webSocket, ClientHandshake clientHandshake);

    static {
        $assertionsDisabled = !WebSocketServer.class.desiredAssertionStatus();
        DECODERS = Runtime.getRuntime().availableProcessors();
    }

    public WebSocketServer() throws UnknownHostException {
        this(new InetSocketAddress(80), DECODERS, null);
    }

    public WebSocketServer(InetSocketAddress address) {
        this(address, DECODERS, null);
    }

    public WebSocketServer(InetSocketAddress address, int decoders) {
        this(address, decoders, null);
    }

    public WebSocketServer(InetSocketAddress address, List<Draft> drafts) {
        this(address, DECODERS, drafts);
    }

    public WebSocketServer(InetSocketAddress address, int decodercount, List<Draft> drafts) {
        this(address, decodercount, drafts, new HashSet());
    }

    public WebSocketServer(InetSocketAddress address, int decodercount, List<Draft> drafts, Collection<WebSocket> connectionscontainer) {
        this.isclosed = new AtomicBoolean(false);
        this.queueinvokes = 0;
        this.queuesize = new AtomicInteger(0);
        this.wsf = new DefaultWebSocketServerFactory();
        if (address == null || decodercount < 1 || connectionscontainer == null) {
            throw new IllegalArgumentException("address and connectionscontainer must not be null and you need at least 1 decoder");
        }
        if (drafts == null) {
            this.drafts = Collections.emptyList();
        } else {
            this.drafts = drafts;
        }
        this.address = address;
        this.connections = connectionscontainer;
        this.iqueue = new LinkedList();
        this.decoders = new ArrayList(decodercount);
        this.buffers = new LinkedBlockingQueue();
        for (int i = 0; i < decodercount; i++) {
            WebSocketWorker ex = new WebSocketWorker();
            this.decoders.add(ex);
            ex.start();
        }
    }

    public void start() {
        if (this.selectorthread != null) {
            throw new IllegalStateException(getClass().getName() + " can only be started once.");
        }
        new Thread(this).start();
    }

    public void stop(int timeout) throws InterruptedException {
        Throwable th;
        if (this.isclosed.compareAndSet(false, true)) {
            synchronized (this.connections) {
                try {
                    List<WebSocket> socketsToClose = new ArrayList(this.connections);
                    try {
                        for (WebSocket ws : socketsToClose) {
                            ws.close(CloseFrame.GOING_AWAY);
                        }
                        synchronized (this) {
                            if (this.selectorthread != null) {
                                if (Thread.currentThread() != this.selectorthread) {
                                }
                                if (this.selectorthread != Thread.currentThread()) {
                                    if (socketsToClose.size() > 0) {
                                        this.selectorthread.join((long) timeout);
                                    }
                                    this.selectorthread.interrupt();
                                    this.selectorthread.join();
                                }
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        List<WebSocket> list = socketsToClose;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
    }

    public void stop() throws IOException, InterruptedException {
        stop(0);
    }

    public Collection<WebSocket> connections() {
        return this.connections;
    }

    public InetSocketAddress getAddress() {
        return this.address;
    }

    public int getPort() {
        int port = getAddress().getPort();
        if (port != 0 || this.server == null) {
            return port;
        }
        return this.server.socket().getLocalPort();
    }

    public List<Draft> getDraft() {
        return Collections.unmodifiableList(this.drafts);
    }

    public void run() {
        synchronized (this) {
            if (this.selectorthread != null) {
                throw new IllegalStateException(getClass().getName() + " can only be started once.");
            }
            this.selectorthread = Thread.currentThread();
            if (this.isclosed.get()) {
                return;
            }
            this.selectorthread.setName("WebsocketSelector" + this.selectorthread.getId());
            try {
                this.server = ServerSocketChannel.open();
                this.server.configureBlocking(false);
                ServerSocket socket = this.server.socket();
                socket.setReceiveBufferSize(WebSocketImpl.RCVBUF);
                socket.bind(this.address);
                this.selector = Selector.open();
                this.server.register(this.selector, this.server.validOps());
                while (!this.selectorthread.isInterrupted()) {
                    try {
                        SelectionKey key = null;
                        WebSocketImpl conn = null;
                        ByteBuffer buf;
                        try {
                            this.selector.select();
                            Iterator<SelectionKey> i = this.selector.selectedKeys().iterator();
                            while (i.hasNext()) {
                                key = (SelectionKey) i.next();
                                if (key.isValid()) {
                                    if (!key.isAcceptable()) {
                                        if (key.isReadable()) {
                                            conn = (WebSocketImpl) key.attachment();
                                            buf = takeBuffer();
                                            if (!SocketChannelIOHelper.read(buf, conn, conn.channel)) {
                                                pushBuffer(buf);
                                            } else if (buf.hasRemaining()) {
                                                conn.inQueue.put(buf);
                                                queue(conn);
                                                i.remove();
                                                if ((conn.channel instanceof WrappedByteChannel) && ((WrappedByteChannel) conn.channel).isNeedRead()) {
                                                    this.iqueue.add(conn);
                                                }
                                            } else {
                                                pushBuffer(buf);
                                            }
                                        }
                                        if (key.isWritable()) {
                                            conn = (WebSocketImpl) key.attachment();
                                            if (SocketChannelIOHelper.batch(conn, conn.channel) && key.isValid()) {
                                                key.interestOps(1);
                                            }
                                        }
                                    } else if (onConnect(key)) {
                                        SocketChannel channel = this.server.accept();
                                        channel.configureBlocking(false);
                                        WebSocketImpl w = this.wsf.createWebSocket((WebSocketAdapter) this, this.drafts, channel.socket());
                                        w.key = channel.register(this.selector, 1, w);
                                        w.channel = this.wsf.wrapChannel(channel, w.key);
                                        i.remove();
                                        allocateBuffers(w);
                                    } else {
                                        key.cancel();
                                    }
                                }
                            }
                            while (!this.iqueue.isEmpty()) {
                                conn = (WebSocketImpl) this.iqueue.remove(0);
                                WrappedByteChannel c = conn.channel;
                                buf = takeBuffer();
                                if (SocketChannelIOHelper.readMore(buf, conn, c)) {
                                    this.iqueue.add(conn);
                                }
                                if (buf.hasRemaining()) {
                                    conn.inQueue.put(buf);
                                    queue(conn);
                                } else {
                                    pushBuffer(buf);
                                }
                            }
                            continue;
                        } catch (ClosedByInterruptException e) {
                            pushBuffer(buf);
                            throw e;
                        } catch (CancelledKeyException e2) {
                        } catch (ClosedByInterruptException e3) {
                            if (this.decoders != null) {
                                for (WebSocketWorker w2 : this.decoders) {
                                    w2.interrupt();
                                }
                            }
                            if (this.server != null) {
                                try {
                                    this.server.close();
                                    return;
                                } catch (InterruptedException e4) {
                                    onError(null, e4);
                                    return;
                                }
                            }
                            return;
                        } catch (InterruptedException e5) {
                            if (this.decoders != null) {
                                for (WebSocketWorker w22 : this.decoders) {
                                    w22.interrupt();
                                }
                            }
                            if (this.server != null) {
                                try {
                                    this.server.close();
                                    return;
                                } catch (IOException e6) {
                                    onError(null, e6);
                                    return;
                                }
                            }
                            return;
                        } catch (IOException e62) {
                            pushBuffer(buf);
                            throw e62;
                        } catch (IOException ex) {
                            if (key != null) {
                                key.cancel();
                            }
                            handleIOException(key, conn, ex);
                        }
                    } catch (RuntimeException e7) {
                        handleFatal(null, e7);
                        if (this.decoders != null) {
                            for (WebSocketWorker w222 : this.decoders) {
                                w222.interrupt();
                            }
                        }
                        if (this.server != null) {
                            try {
                                this.server.close();
                                return;
                            } catch (IOException e622) {
                                onError(null, e622);
                                return;
                            }
                        }
                        return;
                    } catch (Throwable th) {
                        if (this.decoders != null) {
                            for (WebSocketWorker w2222 : this.decoders) {
                                w2222.interrupt();
                            }
                        }
                        if (this.server != null) {
                            try {
                                this.server.close();
                            } catch (IOException e6222) {
                                onError(null, e6222);
                            }
                        }
                    }
                }
                if (this.decoders != null) {
                    for (WebSocketWorker w22222 : this.decoders) {
                        w22222.interrupt();
                    }
                }
                if (this.server != null) {
                    try {
                        this.server.close();
                    } catch (RuntimeException e72) {
                        onError(null, e72);
                    }
                }
            } catch (IOException ex2) {
                handleFatal(null, ex2);
            }
        }
    }

    protected void allocateBuffers(WebSocket c) throws InterruptedException {
        if (this.queuesize.get() < (this.decoders.size() * 2) + 1) {
            this.queuesize.incrementAndGet();
            this.buffers.put(createBuffer());
        }
    }

    protected void releaseBuffers(WebSocket c) throws InterruptedException {
    }

    public ByteBuffer createBuffer() {
        return ByteBuffer.allocate(WebSocketImpl.RCVBUF);
    }

    private void queue(WebSocketImpl ws) throws InterruptedException {
        if (ws.workerThread == null) {
            ws.workerThread = (WebSocketWorker) this.decoders.get(this.queueinvokes % this.decoders.size());
            this.queueinvokes++;
        }
        ws.workerThread.put(ws);
    }

    private ByteBuffer takeBuffer() throws InterruptedException {
        return (ByteBuffer) this.buffers.take();
    }

    private void pushBuffer(ByteBuffer buf) throws InterruptedException {
        if (this.buffers.size() <= this.queuesize.intValue()) {
            this.buffers.put(buf);
        }
    }

    private void handleIOException(SelectionKey key, WebSocket conn, IOException ex) {
        if (conn != null) {
            conn.closeConnection(CloseFrame.ABNORMAL_CLOSE, ex.getMessage());
        } else if (key != null) {
            SelectableChannel channel = key.channel();
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException e) {
                }
                if (WebSocketImpl.DEBUG) {
                    System.out.println("Connection closed because of" + ex);
                }
            }
        }
    }

    private void handleFatal(WebSocket conn, Exception e) {
        onError(conn, e);
        try {
            stop();
        } catch (IOException e1) {
            onError(null, e1);
        } catch (InterruptedException e12) {
            Thread.currentThread().interrupt();
            onError(null, e12);
        }
    }

    protected String getFlashSecurityPolicy() {
        return "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + getPort() + "\" /></cross-domain-policy>";
    }

    public final void onWebsocketMessage(WebSocket conn, String message) {
        onMessage(conn, message);
    }

    @Deprecated
    public void onWebsocketMessageFragment(WebSocket conn, Framedata frame) {
        onFragment(conn, frame);
    }

    public final void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {
        onMessage(conn, blob);
    }

    public final void onWebsocketOpen(WebSocket conn, Handshakedata handshake) {
        if (addConnection(conn)) {
            onOpen(conn, (ClientHandshake) handshake);
        }
    }

    public final void onWebsocketClose(WebSocket conn, int code, String reason, boolean remote) {
        this.selector.wakeup();
        try {
            if (removeConnection(conn)) {
                onClose(conn, code, reason, remote);
            }
        } finally {
            try {
                releaseBuffers(conn);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    protected boolean removeConnection(WebSocket ws) {
        boolean removed;
        synchronized (this.connections) {
            removed = this.connections.remove(ws);
            if ($assertionsDisabled || removed) {
            } else {
                throw new AssertionError();
            }
        }
        if (this.isclosed.get() && this.connections.size() == 0) {
            this.selectorthread.interrupt();
        }
        return removed;
    }

    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
        return super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected boolean addConnection(com.mixpanel.android.java_websocket.WebSocket r4) {
        /*
        r3 = this;
        r1 = r3.isclosed;
        r1 = r1.get();
        if (r1 != 0) goto L_0x0022;
    L_0x0008:
        r2 = r3.connections;
        monitor-enter(r2);
        r1 = r3.connections;	 Catch:{ all -> 0x001d }
        r0 = r1.add(r4);	 Catch:{ all -> 0x001d }
        r1 = $assertionsDisabled;	 Catch:{ all -> 0x001d }
        if (r1 != 0) goto L_0x0020;
    L_0x0015:
        if (r0 != 0) goto L_0x0020;
    L_0x0017:
        r1 = new java.lang.AssertionError;	 Catch:{ all -> 0x001d }
        r1.<init>();	 Catch:{ all -> 0x001d }
        throw r1;	 Catch:{ all -> 0x001d }
    L_0x001d:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        throw r1;
    L_0x0020:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
    L_0x0021:
        return r0;
    L_0x0022:
        r1 = 1001; // 0x3e9 float:1.403E-42 double:4.946E-321;
        r4.close(r1);
        r0 = 1;
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.java_websocket.server.WebSocketServer.addConnection(com.mixpanel.android.java_websocket.WebSocket):boolean");
    }

    public final void onWebsocketError(WebSocket conn, Exception ex) {
        onError(conn, ex);
    }

    public final void onWriteDemand(WebSocket w) {
        WebSocketImpl conn = (WebSocketImpl) w;
        try {
            conn.key.interestOps(5);
        } catch (CancelledKeyException e) {
            conn.outQueue.clear();
        }
        this.selector.wakeup();
    }

    public void onWebsocketCloseInitiated(WebSocket conn, int code, String reason) {
        onCloseInitiated(conn, code, reason);
    }

    public void onWebsocketClosing(WebSocket conn, int code, String reason, boolean remote) {
        onClosing(conn, code, reason, remote);
    }

    public void onCloseInitiated(WebSocket conn, int code, String reason) {
    }

    public void onClosing(WebSocket conn, int code, String reason, boolean remote) {
    }

    public final void setWebSocketFactory(WebSocketServerFactory wsf) {
        this.wsf = wsf;
    }

    public final WebSocketFactory getWebSocketFactory() {
        return this.wsf;
    }

    protected boolean onConnect(SelectionKey key) {
        return true;
    }

    private Socket getSocket(WebSocket conn) {
        return ((SocketChannel) ((WebSocketImpl) conn).key.channel()).socket();
    }

    public InetSocketAddress getLocalSocketAddress(WebSocket conn) {
        return (InetSocketAddress) getSocket(conn).getLocalSocketAddress();
    }

    public InetSocketAddress getRemoteSocketAddress(WebSocket conn) {
        return (InetSocketAddress) getSocket(conn).getRemoteSocketAddress();
    }

    public void onMessage(WebSocket conn, ByteBuffer message) {
    }

    public void onFragment(WebSocket conn, Framedata fragment) {
    }
}
