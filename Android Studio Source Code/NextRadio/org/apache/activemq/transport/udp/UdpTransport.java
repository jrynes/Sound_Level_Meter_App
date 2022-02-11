package org.apache.activemq.transport.udp;

import com.rabbitmq.client.ConnectionFactory;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.DatagramChannel;
import org.apache.activemq.Service;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.Endpoint;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportThreadSupport;
import org.apache.activemq.transport.reliable.ExceptionIfDroppedReplayStrategy;
import org.apache.activemq.transport.reliable.ReplayBuffer;
import org.apache.activemq.transport.reliable.ReplayStrategy;
import org.apache.activemq.transport.reliable.Replayer;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.InetAddressUtil;
import org.apache.activemq.util.IntSequenceGenerator;
import org.apache.activemq.util.ServiceStopper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.KEYRecord.Flags;

public class UdpTransport extends TransportThreadSupport implements Transport, Service, Runnable {
    private static final long BIND_ATTEMPT_DELAY = 100;
    private static final Logger LOG;
    private static final int MAX_BIND_ATTEMPTS = 50;
    private ByteBufferPool bufferPool;
    private DatagramChannel channel;
    private CommandChannel commandChannel;
    private int datagramSize;
    private String description;
    private int minmumWireFormatVersion;
    private SocketAddress originalTargetAddress;
    private int port;
    private ReplayBuffer replayBuffer;
    private boolean replayEnabled;
    private ReplayStrategy replayStrategy;
    private IntSequenceGenerator sequenceGenerator;
    private SocketAddress targetAddress;
    private boolean trace;
    private boolean useLocalHost;
    private OpenWireFormat wireFormat;

    static {
        LOG = LoggerFactory.getLogger(UdpTransport.class);
    }

    protected UdpTransport(OpenWireFormat wireFormat) throws IOException {
        this.replayStrategy = new ExceptionIfDroppedReplayStrategy();
        this.datagramSize = Flags.EXTEND;
        this.useLocalHost = false;
        this.replayEnabled = true;
        this.wireFormat = wireFormat;
    }

    public UdpTransport(OpenWireFormat wireFormat, URI remoteLocation) throws UnknownHostException, IOException {
        this(wireFormat);
        this.targetAddress = createAddress(remoteLocation);
        this.description = remoteLocation.toString() + "@";
    }

    public UdpTransport(OpenWireFormat wireFormat, SocketAddress socketAddress) throws IOException {
        this(wireFormat);
        this.targetAddress = socketAddress;
        this.description = getProtocolName() + "ServerConnection@";
    }

    public UdpTransport(OpenWireFormat wireFormat, int port) throws UnknownHostException, IOException {
        this(wireFormat);
        this.port = port;
        this.targetAddress = null;
        this.description = getProtocolName() + "Server@";
    }

    public Replayer createReplayer() throws IOException {
        if (this.replayEnabled) {
            return getCommandChannel();
        }
        return null;
    }

    public void oneway(Object command) throws IOException {
        oneway(command, this.targetAddress);
    }

    public void oneway(Object command, SocketAddress address) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sending oneway from: " + this + " to target: " + this.targetAddress + " command: " + command);
        }
        checkStarted();
        this.commandChannel.write((Command) command, address);
    }

    public String toString() {
        if (this.description != null) {
            return this.description + this.port;
        }
        return getProtocolUriScheme() + this.targetAddress + "@" + this.port;
    }

    public void run() {
        LOG.trace("Consumer thread starting for: " + toString());
        while (!isStopped()) {
            try {
                doConsume(this.commandChannel.read());
            } catch (AsynchronousCloseException e) {
                try {
                    stop();
                } catch (Exception e2) {
                    LOG.warn("Caught in: " + this + " while closing: " + e2 + ". Now Closed", e2);
                }
            } catch (SocketException e3) {
                LOG.debug("Socket closed: " + e3, e3);
                try {
                    stop();
                } catch (Exception e22) {
                    LOG.warn("Caught in: " + this + " while closing: " + e22 + ". Now Closed", e22);
                }
            } catch (EOFException e4) {
                LOG.debug("Socket closed: " + e4, e4);
                try {
                    stop();
                } catch (Exception e222) {
                    LOG.warn("Caught in: " + this + " while closing: " + e222 + ". Now Closed", e222);
                }
            } catch (Exception e5) {
                try {
                    stop();
                } catch (Exception e2222) {
                    LOG.warn("Caught in: " + this + " while closing: " + e2222 + ". Now Closed", e2222);
                }
                if (e5 instanceof IOException) {
                    onException((IOException) e5);
                } else {
                    LOG.error("Caught: " + e5, e5);
                    e5.printStackTrace();
                }
            }
        }
    }

    public void setTargetEndpoint(Endpoint newTarget) {
        if (newTarget instanceof DatagramEndpoint) {
            SocketAddress address = ((DatagramEndpoint) newTarget).getAddress();
            if (address != null) {
                if (this.originalTargetAddress == null) {
                    this.originalTargetAddress = this.targetAddress;
                }
                this.targetAddress = address;
                this.commandChannel.setTargetAddress(address);
            }
        }
    }

    public boolean isTrace() {
        return this.trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public int getDatagramSize() {
        return this.datagramSize;
    }

    public void setDatagramSize(int datagramSize) {
        this.datagramSize = datagramSize;
    }

    public boolean isUseLocalHost() {
        return this.useLocalHost;
    }

    public void setUseLocalHost(boolean useLocalHost) {
        this.useLocalHost = useLocalHost;
    }

    public CommandChannel getCommandChannel() throws IOException {
        if (this.commandChannel == null) {
            this.commandChannel = createCommandChannel();
        }
        return this.commandChannel;
    }

    public void setCommandChannel(CommandDatagramChannel commandChannel) {
        this.commandChannel = commandChannel;
    }

    public ReplayStrategy getReplayStrategy() {
        return this.replayStrategy;
    }

    public void setReplayStrategy(ReplayStrategy replayStrategy) {
        this.replayStrategy = replayStrategy;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMinmumWireFormatVersion() {
        return this.minmumWireFormatVersion;
    }

    public void setMinmumWireFormatVersion(int minmumWireFormatVersion) {
        this.minmumWireFormatVersion = minmumWireFormatVersion;
    }

    public OpenWireFormat getWireFormat() {
        return this.wireFormat;
    }

    public IntSequenceGenerator getSequenceGenerator() {
        if (this.sequenceGenerator == null) {
            this.sequenceGenerator = new IntSequenceGenerator();
        }
        return this.sequenceGenerator;
    }

    public void setSequenceGenerator(IntSequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    public boolean isReplayEnabled() {
        return this.replayEnabled;
    }

    public void setReplayEnabled(boolean replayEnabled) {
        this.replayEnabled = replayEnabled;
    }

    public ByteBufferPool getBufferPool() {
        if (this.bufferPool == null) {
            this.bufferPool = new DefaultBufferPool();
        }
        return this.bufferPool;
    }

    public void setBufferPool(ByteBufferPool bufferPool) {
        this.bufferPool = bufferPool;
    }

    public ReplayBuffer getReplayBuffer() {
        return this.replayBuffer;
    }

    public void setReplayBuffer(ReplayBuffer replayBuffer) throws IOException {
        this.replayBuffer = replayBuffer;
        getCommandChannel().setReplayBuffer(replayBuffer);
    }

    protected InetSocketAddress createAddress(URI remoteLocation) throws UnknownHostException, IOException {
        return new InetSocketAddress(resolveHostName(remoteLocation.getHost()), remoteLocation.getPort());
    }

    protected String resolveHostName(String host) throws UnknownHostException {
        String localName = InetAddressUtil.getLocalHostName();
        if (localName != null && isUseLocalHost() && localName.equals(host)) {
            return ConnectionFactory.DEFAULT_HOST;
        }
        return host;
    }

    protected void doStart() throws Exception {
        getCommandChannel().start();
        super.doStart();
    }

    protected CommandChannel createCommandChannel() throws IOException {
        SocketAddress localAddress = createLocalAddress();
        this.channel = DatagramChannel.open();
        this.channel = connect(this.channel, this.targetAddress);
        DatagramSocket socket = this.channel.socket();
        bind(socket, localAddress);
        if (this.port == 0) {
            this.port = socket.getLocalPort();
        }
        return createCommandDatagramChannel();
    }

    protected CommandChannel createCommandDatagramChannel() {
        return new CommandDatagramChannel(this, getWireFormat(), getDatagramSize(), getTargetAddress(), createDatagramHeaderMarshaller(), getChannel(), getBufferPool());
    }

    protected void bind(DatagramSocket socket, SocketAddress localAddress) throws IOException {
        this.channel.configureBlocking(true);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Binding to address: " + localAddress);
        }
        int i = 0;
        while (i < MAX_BIND_ATTEMPTS) {
            try {
                socket.bind(localAddress);
                return;
            } catch (BindException e) {
                if (i + 1 == MAX_BIND_ATTEMPTS) {
                    throw e;
                }
                try {
                    Thread.sleep(BIND_ATTEMPT_DELAY);
                    i++;
                } catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                    throw e;
                }
            }
        }
    }

    protected DatagramChannel connect(DatagramChannel channel, SocketAddress targetAddress2) throws IOException {
        return channel;
    }

    protected SocketAddress createLocalAddress() {
        return new InetSocketAddress(this.port);
    }

    protected void doStop(ServiceStopper stopper) throws Exception {
        if (this.channel != null) {
            this.channel.close();
        }
    }

    protected DatagramHeaderMarshaller createDatagramHeaderMarshaller() {
        return new DatagramHeaderMarshaller();
    }

    protected String getProtocolName() {
        return "Udp";
    }

    protected String getProtocolUriScheme() {
        return "udp://";
    }

    protected SocketAddress getTargetAddress() {
        return this.targetAddress;
    }

    protected DatagramChannel getChannel() {
        return this.channel;
    }

    protected void setChannel(DatagramChannel channel) {
        this.channel = channel;
    }

    public InetSocketAddress getLocalSocketAddress() {
        if (this.channel == null) {
            return null;
        }
        return (InetSocketAddress) this.channel.socket().getLocalSocketAddress();
    }

    public String getRemoteAddress() {
        if (this.targetAddress != null) {
            return Stomp.EMPTY + this.targetAddress;
        }
        return null;
    }

    public int getReceiveCounter() {
        if (this.commandChannel == null) {
            return 0;
        }
        return this.commandChannel.getReceiveCounter();
    }
}
