package org.apache.activemq.transport;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.WireFormatInfo;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.util.IOExceptionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WireFormatNegotiator extends TransportFilter {
    private static final Logger LOG;
    private final AtomicBoolean firstStart;
    private final int minimumVersion;
    private long negotiateTimeout;
    private final CountDownLatch readyCountDownLatch;
    private OpenWireFormat wireFormat;
    private final CountDownLatch wireInfoSentDownLatch;

    static {
        LOG = LoggerFactory.getLogger(WireFormatNegotiator.class);
    }

    public WireFormatNegotiator(Transport next, OpenWireFormat wireFormat, int minimumVersion) {
        super(next);
        this.negotiateTimeout = 15000;
        this.firstStart = new AtomicBoolean(true);
        this.readyCountDownLatch = new CountDownLatch(1);
        this.wireInfoSentDownLatch = new CountDownLatch(1);
        this.wireFormat = wireFormat;
        if (minimumVersion <= 0) {
            minimumVersion = 1;
        }
        this.minimumVersion = minimumVersion;
        try {
            if (wireFormat.getPreferedWireFormatInfo() != null) {
                setNegotiateTimeout(wireFormat.getPreferedWireFormatInfo().getMaxInactivityDurationInitalDelay());
            }
        } catch (IOException e) {
        }
    }

    public void start() throws Exception {
        super.start();
        if (this.firstStart.compareAndSet(true, false)) {
            sendWireFormat();
        }
    }

    public void sendWireFormat() throws IOException {
        try {
            WireFormatInfo info = this.wireFormat.getPreferedWireFormatInfo();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sending: " + info);
            }
            sendWireFormat(info);
        } finally {
            this.wireInfoSentDownLatch.countDown();
        }
    }

    public void stop() throws Exception {
        super.stop();
        this.readyCountDownLatch.countDown();
    }

    public void oneway(Object command) throws IOException {
        try {
            if (this.readyCountDownLatch.await(this.negotiateTimeout, TimeUnit.MILLISECONDS)) {
                super.oneway(command);
                return;
            }
            throw new IOException("Wire format negotiation timeout: peer did not send his wire format.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
        }
    }

    public void onCommand(Object o) {
        Command command = (Command) o;
        if (command.isWireFormatInfo()) {
            negociate((WireFormatInfo) command);
        }
        getTransportListener().onCommand(command);
    }

    public void negociate(WireFormatInfo info) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received WireFormat: " + info);
        }
        try {
            this.wireInfoSentDownLatch.await();
            if (LOG.isDebugEnabled()) {
                LOG.debug(this + " before negotiation: " + this.wireFormat);
            }
            if (!info.isValid()) {
                onException(new IOException("Remote wire format magic is invalid"));
            } else if (info.getVersion() < this.minimumVersion) {
                onException(new IOException("Remote wire format (" + info.getVersion() + ") is lower the minimum version required (" + this.minimumVersion + ")"));
            }
            this.wireFormat.renegotiateWireFormat(info);
            Socket socket = (Socket) this.next.narrow(Socket.class);
            if (socket != null) {
                socket.setTcpNoDelay(this.wireFormat.isTcpNoDelayEnabled());
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(this + " after negotiation: " + this.wireFormat);
            }
        } catch (IOException e) {
            onException(e);
        } catch (InterruptedException e2) {
            onException((IOException) new InterruptedIOException().initCause(e2));
        } catch (Exception e3) {
            onException(IOExceptionSupport.create(e3));
        }
        this.readyCountDownLatch.countDown();
        onWireFormatNegotiated(info);
    }

    public void onException(IOException error) {
        this.readyCountDownLatch.countDown();
        super.onException(error);
    }

    public String toString() {
        return this.next.toString();
    }

    protected void sendWireFormat(WireFormatInfo info) throws IOException {
        this.next.oneway(info);
    }

    protected void onWireFormatNegotiated(WireFormatInfo info) {
    }

    public long getNegotiateTimeout() {
        return this.negotiateTimeout;
    }

    public void setNegotiateTimeout(long negotiateTimeout) {
        this.negotiateTimeout = negotiateTimeout;
    }
}
