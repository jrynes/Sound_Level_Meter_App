package org.apache.activemq.transport.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.command.BrokerInfo;
import org.apache.activemq.command.Command;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.transport.CommandJoiner;
import org.apache.activemq.transport.InactivityMonitor;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportListener;
import org.apache.activemq.transport.TransportServerSupport;
import org.apache.activemq.transport.reliable.ReliableTransport;
import org.apache.activemq.transport.reliable.ReplayStrategy;
import org.apache.activemq.util.ServiceStopper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class UdpTransportServer extends TransportServerSupport {
    private static final Logger LOG;
    private final Transport configuredTransport;
    private final ReplayStrategy replayStrategy;
    private final UdpTransport serverTransport;
    private final Map<DatagramEndpoint, Transport> transports;
    private boolean usingWireFormatNegotiation;

    class 1 implements TransportListener {
        1() {
        }

        public void onCommand(Object o) {
            UdpTransportServer.this.processInboundConnection((Command) o);
        }

        public void onException(IOException error) {
            UdpTransportServer.LOG.error("Caught: " + error, error);
        }

        public void transportInterupted() {
        }

        public void transportResumed() {
        }
    }

    class 2 extends CommandJoiner {
        final /* synthetic */ Command val$command;
        final /* synthetic */ ReliableTransport val$reliableTransport;

        2(Transport x0, OpenWireFormat x1, ReliableTransport reliableTransport, Command command) {
            this.val$reliableTransport = reliableTransport;
            this.val$command = command;
            super(x0, x1);
        }

        public void start() throws Exception {
            super.start();
            this.val$reliableTransport.onCommand(this.val$command);
        }
    }

    static {
        LOG = LoggerFactory.getLogger(UdpTransportServer.class);
    }

    public UdpTransportServer(URI connectURI, UdpTransport serverTransport, Transport configuredTransport, ReplayStrategy replayStrategy) {
        super(connectURI);
        this.transports = new HashMap();
        this.serverTransport = serverTransport;
        this.configuredTransport = configuredTransport;
        this.replayStrategy = replayStrategy;
    }

    public String toString() {
        return "UdpTransportServer@" + this.serverTransport;
    }

    public void run() {
    }

    public UdpTransport getServerTransport() {
        return this.serverTransport;
    }

    public void setBrokerInfo(BrokerInfo brokerInfo) {
    }

    protected void doStart() throws Exception {
        LOG.info("Starting " + this);
        this.configuredTransport.setTransportListener(new 1());
        this.configuredTransport.start();
    }

    protected void doStop(ServiceStopper stopper) throws Exception {
        this.configuredTransport.stop();
    }

    protected void processInboundConnection(Command command) {
        DatagramEndpoint endpoint = (DatagramEndpoint) command.getFrom();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received command on: " + this + " from address: " + endpoint + " command: " + command);
        }
        synchronized (this.transports) {
            if (((Transport) this.transports.get(endpoint)) != null) {
                LOG.warn("Discarding duplicate command to server from: " + endpoint + " command: " + command);
            } else if (!this.usingWireFormatNegotiation || command.isWireFormatInfo()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Creating a new UDP server connection");
                }
                try {
                    this.transports.put(endpoint, configureTransport(createTransport(command, endpoint)));
                } catch (IOException e) {
                    LOG.error("Caught: " + e, e);
                    getAcceptListener().onAcceptError(e);
                }
            } else {
                LOG.error("Received inbound server communication from: " + command.getFrom() + " expecting WireFormatInfo but was command: " + command);
            }
        }
    }

    protected Transport configureTransport(Transport transport) {
        Transport transport2 = new InactivityMonitor(transport, this.serverTransport.getWireFormat());
        getAcceptListener().onAccept(transport2);
        return transport2;
    }

    protected Transport createTransport(Command command, DatagramEndpoint endpoint) throws IOException {
        if (endpoint == null) {
            throw new IOException("No endpoint available for command: " + command);
        }
        SocketAddress address = endpoint.getAddress();
        OpenWireFormat connectionWireFormat = this.serverTransport.getWireFormat().copy();
        Transport transport = new UdpTransport(connectionWireFormat, address);
        ReliableTransport reliableTransport = new ReliableTransport(transport, (UdpTransport) transport);
        reliableTransport.getReplayer();
        reliableTransport.setReplayStrategy(this.replayStrategy);
        return new 2(reliableTransport, connectionWireFormat, reliableTransport, command);
    }

    public InetSocketAddress getSocketAddress() {
        return this.serverTransport.getLocalSocketAddress();
    }

    public boolean isSslServer() {
        return false;
    }
}
