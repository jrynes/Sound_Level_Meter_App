package org.apache.activemq.transport.nio;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.cert.X509Certificate;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.thread.TaskRunnerFactory;
import org.apache.activemq.transport.nio.SelectorManager.Listener;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.ServiceStopper;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NIOSSLTransport extends NIOTransport {
    private static final Logger LOG;
    protected String[] enabledCipherSuites;
    protected volatile boolean handshakeInProgress;
    protected HandshakeStatus handshakeStatus;
    protected boolean needClientAuth;
    protected SSLContext sslContext;
    protected SSLEngine sslEngine;
    protected SSLSession sslSession;
    protected Status status;
    protected TaskRunnerFactory taskRunnerFactory;
    protected boolean wantClientAuth;

    class 1 implements Listener {
        1() {
        }

        public void onSelect(SelectorSelection selection) {
            NIOSSLTransport.this.serviceRead();
        }

        public void onError(SelectorSelection selection, Throwable error) {
            if (error instanceof IOException) {
                NIOSSLTransport.this.onException((IOException) error);
            } else {
                NIOSSLTransport.this.onException(IOExceptionSupport.create(error));
            }
        }
    }

    static /* synthetic */ class 2 {
        static final /* synthetic */ int[] $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus;

        static {
            $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus = new int[HandshakeStatus.values().length];
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[HandshakeStatus.NEED_UNWRAP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[HandshakeStatus.NEED_TASK.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[HandshakeStatus.NEED_WRAP.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[HandshakeStatus.FINISHED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[HandshakeStatus.NOT_HANDSHAKING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    static {
        LOG = LoggerFactory.getLogger(NIOSSLTransport.class);
    }

    public NIOSSLTransport(WireFormat wireFormat, SocketFactory socketFactory, URI remoteLocation, URI localLocation) throws UnknownHostException, IOException {
        super(wireFormat, socketFactory, remoteLocation, localLocation);
        this.handshakeInProgress = false;
        this.status = null;
        this.handshakeStatus = null;
    }

    public NIOSSLTransport(WireFormat wireFormat, Socket socket) throws IOException {
        super(wireFormat, socket);
        this.handshakeInProgress = false;
        this.status = null;
        this.handshakeStatus = null;
    }

    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    protected void initializeStreams() throws IOException {
        try {
            this.channel = this.socket.getChannel();
            this.channel.configureBlocking(false);
            if (this.sslContext == null) {
                this.sslContext = SSLContext.getDefault();
            }
            String str = null;
            int remotePort = -1;
            try {
                URI remoteAddress = new URI(getRemoteAddress());
                str = remoteAddress.getHost();
                remotePort = remoteAddress.getPort();
            } catch (Exception e) {
            }
            if (str == null || remotePort == -1) {
                this.sslEngine = this.sslContext.createSSLEngine();
            } else {
                this.sslEngine = this.sslContext.createSSLEngine(str, remotePort);
            }
            this.sslEngine.setUseClientMode(false);
            if (this.enabledCipherSuites != null) {
                this.sslEngine.setEnabledCipherSuites(this.enabledCipherSuites);
            }
            if (this.wantClientAuth) {
                this.sslEngine.setWantClientAuth(this.wantClientAuth);
            }
            if (this.needClientAuth) {
                this.sslEngine.setNeedClientAuth(this.needClientAuth);
            }
            this.sslSession = this.sslEngine.getSession();
            this.inputBuffer = ByteBuffer.allocate(this.sslSession.getPacketBufferSize());
            this.inputBuffer.clear();
            NIOOutputStream outputStream = new NIOOutputStream(this.channel);
            outputStream.setEngine(this.sslEngine);
            this.dataOut = new DataOutputStream(outputStream);
            this.buffOut = outputStream;
            this.sslEngine.beginHandshake();
            this.handshakeStatus = this.sslEngine.getHandshakeStatus();
            doHandshake();
        } catch (Exception e2) {
            throw new IOException(e2);
        }
    }

    protected void finishHandshake() throws Exception {
        if (this.handshakeInProgress) {
            this.handshakeInProgress = false;
            this.nextFrameSize = -1;
            this.sslSession = this.sslEngine.getSession();
            this.selection = SelectorManager.getInstance().register(this.channel, new 1());
        }
    }

    protected void serviceRead() {
        try {
            if (this.handshakeInProgress) {
                doHandshake();
            }
            ByteBuffer plain = ByteBuffer.allocate(this.sslSession.getApplicationBufferSize());
            plain.position(plain.limit());
            while (true) {
                if (!plain.hasRemaining()) {
                    int readCount = secureRead(plain);
                    if (readCount != 0) {
                        if (readCount == -1) {
                            onException(new EOFException());
                            this.selection.close();
                            return;
                        }
                        this.receiveCounter += readCount;
                    } else {
                        return;
                    }
                }
                if (this.status == Status.OK && this.handshakeStatus != HandshakeStatus.NEED_UNWRAP) {
                    processCommand(plain);
                }
            }
        } catch (IOException e) {
            onException(e);
        } catch (Throwable e2) {
            onException(IOExceptionSupport.create(e2));
        }
    }

    protected void processCommand(ByteBuffer plain) throws Exception {
        if (this.nextFrameSize == -1) {
            if (plain.remaining() < 32) {
                if (this.currentBuffer == null) {
                    this.currentBuffer = ByteBuffer.allocate(4);
                }
                while (this.currentBuffer.hasRemaining() && plain.hasRemaining()) {
                    this.currentBuffer.put(plain.get());
                }
                if (!this.currentBuffer.hasRemaining()) {
                    this.currentBuffer.flip();
                    this.nextFrameSize = this.currentBuffer.getInt();
                } else {
                    return;
                }
            } else if (this.currentBuffer != null) {
                while (this.currentBuffer.hasRemaining()) {
                    this.currentBuffer.put(plain.get());
                }
                this.currentBuffer.flip();
                this.nextFrameSize = this.currentBuffer.getInt();
            } else {
                this.nextFrameSize = plain.getInt();
            }
            if (this.wireFormat instanceof OpenWireFormat) {
                long maxFrameSize = ((OpenWireFormat) this.wireFormat).getMaxFrameSize();
                if (((long) this.nextFrameSize) > maxFrameSize) {
                    throw new IOException("Frame size of " + (this.nextFrameSize / AccessibilityNodeInfoCompat.ACTION_DISMISS) + " MB larger than max allowed " + (maxFrameSize / 1048576) + " MB");
                }
            }
            this.currentBuffer = ByteBuffer.allocate(this.nextFrameSize + 4);
            this.currentBuffer.putInt(this.nextFrameSize);
            return;
        }
        if (this.currentBuffer.remaining() >= plain.remaining()) {
            this.currentBuffer.put(plain);
        } else {
            byte[] fill = new byte[this.currentBuffer.remaining()];
            plain.get(fill);
            this.currentBuffer.put(fill);
        }
        if (!this.currentBuffer.hasRemaining()) {
            this.currentBuffer.flip();
            doConsume((Command) this.wireFormat.unmarshal(new DataInputStream(new NIOInputStream(this.currentBuffer))));
            this.nextFrameSize = -1;
            this.currentBuffer = null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected int secureRead(java.nio.ByteBuffer r6) throws java.lang.Exception {
        /*
        r5 = this;
        r2 = -1;
        r3 = r5.inputBuffer;
        r3 = r3.position();
        if (r3 == 0) goto L_0x0017;
    L_0x0009:
        r3 = r5.inputBuffer;
        r3 = r3.hasRemaining();
        if (r3 == 0) goto L_0x0017;
    L_0x0011:
        r3 = r5.status;
        r4 = javax.net.ssl.SSLEngineResult.Status.BUFFER_UNDERFLOW;
        if (r3 != r4) goto L_0x0038;
    L_0x0017:
        r3 = r5.channel;
        r4 = r5.inputBuffer;
        r0 = r3.read(r4);
        if (r0 != 0) goto L_0x0023;
    L_0x0021:
        r2 = 0;
    L_0x0022:
        return r2;
    L_0x0023:
        if (r0 != r2) goto L_0x0038;
    L_0x0025:
        r3 = r5.sslEngine;
        r3.closeInbound();
        r3 = r5.inputBuffer;
        r3 = r3.position();
        if (r3 == 0) goto L_0x0022;
    L_0x0032:
        r3 = r5.status;
        r4 = javax.net.ssl.SSLEngineResult.Status.BUFFER_UNDERFLOW;
        if (r3 == r4) goto L_0x0022;
    L_0x0038:
        r6.clear();
        r3 = r5.inputBuffer;
        r3.flip();
    L_0x0040:
        r3 = r5.sslEngine;
        r4 = r5.inputBuffer;
        r1 = r3.unwrap(r4, r6);
        r3 = r1.getStatus();
        r4 = javax.net.ssl.SSLEngineResult.Status.OK;
        if (r3 != r4) goto L_0x005e;
    L_0x0050:
        r3 = r1.getHandshakeStatus();
        r4 = javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
        if (r3 != r4) goto L_0x005e;
    L_0x0058:
        r3 = r1.bytesProduced();
        if (r3 == 0) goto L_0x0040;
    L_0x005e:
        r3 = r1.getHandshakeStatus();
        r4 = javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED;
        if (r3 != r4) goto L_0x0069;
    L_0x0066:
        r5.finishHandshake();
    L_0x0069:
        r3 = r1.getStatus();
        r5.status = r3;
        r3 = r1.getHandshakeStatus();
        r5.handshakeStatus = r3;
        r3 = r5.status;
        r4 = javax.net.ssl.SSLEngineResult.Status.CLOSED;
        if (r3 != r4) goto L_0x0081;
    L_0x007b:
        r3 = r5.sslEngine;
        r3.closeInbound();
        goto L_0x0022;
    L_0x0081:
        r2 = r5.inputBuffer;
        r2.compact();
        r6.flip();
        r2 = r6.remaining();
        goto L_0x0022;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.nio.NIOSSLTransport.secureRead(java.nio.ByteBuffer):int");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void doHandshake() throws java.lang.Exception {
        /*
        r3 = this;
        r1 = 1;
        r3.handshakeInProgress = r1;
    L_0x0003:
        r1 = org.apache.activemq.transport.nio.NIOSSLTransport.2.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus;
        r2 = r3.sslEngine;
        r2 = r2.getHandshakeStatus();
        r2 = r2.ordinal();
        r1 = r1[r2];
        switch(r1) {
            case 1: goto L_0x0015;
            case 2: goto L_0x0023;
            case 3: goto L_0x0031;
            case 4: goto L_0x003e;
            case 5: goto L_0x003e;
            default: goto L_0x0014;
        };
    L_0x0014:
        goto L_0x0003;
    L_0x0015:
        r1 = r3.sslSession;
        r1 = r1.getApplicationBufferSize();
        r1 = java.nio.ByteBuffer.allocate(r1);
        r3.secureRead(r1);
        goto L_0x0003;
    L_0x0023:
        r1 = r3.sslEngine;
        r0 = r1.getDelegatedTask();
        if (r0 == 0) goto L_0x0003;
    L_0x002b:
        r1 = r3.taskRunnerFactory;
        r1.execute(r0);
        goto L_0x0023;
    L_0x0031:
        r1 = r3.buffOut;
        r1 = (org.apache.activemq.transport.nio.NIOOutputStream) r1;
        r2 = 0;
        r2 = java.nio.ByteBuffer.allocate(r2);
        r1.write(r2);
        goto L_0x0003;
    L_0x003e:
        r3.finishHandshake();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.nio.NIOSSLTransport.doHandshake():void");
    }

    protected void doStart() throws Exception {
        this.taskRunnerFactory = new TaskRunnerFactory("ActiveMQ NIOSSLTransport Task");
        super.doStart();
    }

    protected void doStop(ServiceStopper stopper) throws Exception {
        if (this.taskRunnerFactory != null) {
            this.taskRunnerFactory.shutdownNow();
            this.taskRunnerFactory = null;
        }
        if (this.channel != null) {
            this.channel.close();
            this.channel = null;
        }
        super.doStop(stopper);
    }

    public void doConsume(Object command) {
        if (command instanceof ConnectionInfo) {
            ((ConnectionInfo) command).setTransportContext(getPeerCertificates());
        }
        super.doConsume(command);
    }

    public X509Certificate[] getPeerCertificates() {
        try {
            if (this.sslEngine.getSession() == null) {
                return null;
            }
            return (X509Certificate[]) this.sslEngine.getSession().getPeerCertificates();
        } catch (SSLPeerUnverifiedException e) {
            if (!LOG.isTraceEnabled()) {
                return null;
            }
            LOG.trace("Failed to get peer certificates.", e);
            return null;
        }
    }

    public boolean isNeedClientAuth() {
        return this.needClientAuth;
    }

    public void setNeedClientAuth(boolean needClientAuth) {
        this.needClientAuth = needClientAuth;
    }

    public boolean isWantClientAuth() {
        return this.wantClientAuth;
    }

    public void setWantClientAuth(boolean wantClientAuth) {
        this.wantClientAuth = wantClientAuth;
    }

    public String[] getEnabledCipherSuites() {
        return this.enabledCipherSuites;
    }

    public void setEnabledCipherSuites(String[] enabledCipherSuites) {
        this.enabledCipherSuites = enabledCipherSuites;
    }
}
