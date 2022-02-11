package org.apache.activemq.transport.udp;

import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.TransportLoggerSupport;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.transport.CommandJoiner;
import org.apache.activemq.transport.InactivityMonitor;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFactory;
import org.apache.activemq.transport.TransportServer;
import org.apache.activemq.transport.reliable.DefaultReplayStrategy;
import org.apache.activemq.transport.reliable.ExceptionIfDroppedReplayStrategy;
import org.apache.activemq.transport.reliable.ReliableTransport;
import org.apache.activemq.transport.reliable.ReplayStrategy;
import org.apache.activemq.transport.reliable.Replayer;
import org.apache.activemq.transport.tcp.TcpTransportFactory;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class UdpTransportFactory extends TransportFactory {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger(TcpTransportFactory.class);
    }

    public TransportServer doBind(URI location) throws IOException {
        try {
            Map<String, String> options = new HashMap(URISupport.parseParameters(location));
            if (options.containsKey("port")) {
                throw new IllegalArgumentException("The port property cannot be specified on a UDP server transport - please use the port in the URI syntax");
            }
            WireFormat wf = createWireFormat(options);
            int port = location.getPort();
            OpenWireFormat openWireFormat = asOpenWireFormat(wf);
            UdpTransport transport = (UdpTransport) createTransport(location.getPort(), wf);
            return new UdpTransportServer(location, transport, configure(transport, wf, options, true), createReplayStrategy());
        } catch (Exception e) {
            throw IOExceptionSupport.create(e);
        } catch (Exception e2) {
            throw IOExceptionSupport.create(e2);
        }
    }

    public Transport configure(Transport transport, WireFormat format, Map options) throws Exception {
        return configure(transport, format, options, false);
    }

    public Transport compositeConfigure(Transport transport, WireFormat format, Map options) {
        IntrospectionSupport.setProperties(transport, options);
        UdpTransport udpTransport = (UdpTransport) transport;
        Transport transport2 = new CommandJoiner(transport, asOpenWireFormat(format));
        if (udpTransport.isTrace()) {
            try {
                transport = TransportLoggerSupport.createTransportLogger(transport2);
            } catch (Throwable e) {
                log.error("Could not create TransportLogger, reason: " + e, e);
            }
            transport2 = new InactivityMonitor(transport, format);
            if (format instanceof OpenWireFormat) {
                return transport2;
            }
            return configureClientSideNegotiator(transport2, format, udpTransport);
        }
        transport = transport2;
        transport2 = new InactivityMonitor(transport, format);
        if (format instanceof OpenWireFormat) {
            return transport2;
        }
        return configureClientSideNegotiator(transport2, format, udpTransport);
    }

    protected Transport createTransport(URI location, WireFormat wf) throws UnknownHostException, IOException {
        return new UdpTransport(asOpenWireFormat(wf), location);
    }

    protected Transport createTransport(int port, WireFormat wf) throws UnknownHostException, IOException {
        return new UdpTransport(asOpenWireFormat(wf), port);
    }

    protected Transport configure(Transport transport, WireFormat format, Map options, boolean acceptServer) throws Exception {
        IntrospectionSupport.setProperties(transport, options);
        UdpTransport udpTransport = (UdpTransport) transport;
        OpenWireFormat openWireFormat = asOpenWireFormat(format);
        if (udpTransport.isTrace()) {
            transport = TransportLoggerSupport.createTransportLogger(transport);
        }
        Transport transport2 = new InactivityMonitor(transport, format);
        if (!acceptServer && (format instanceof OpenWireFormat)) {
            transport2 = configureClientSideNegotiator(transport2, format, udpTransport);
        }
        if (acceptServer) {
            udpTransport.setReplayEnabled(false);
            return new CommandJoiner(transport2, openWireFormat);
        }
        ReliableTransport reliableTransport = new ReliableTransport(transport2, udpTransport);
        reliableTransport.setReplayStrategy(createReplayStrategy(reliableTransport.getReplayer()));
        CommandJoiner commandJoiner = new CommandJoiner(reliableTransport, openWireFormat);
        transport = transport2;
        return commandJoiner;
    }

    protected ReplayStrategy createReplayStrategy(Replayer replayer) {
        if (replayer != null) {
            return new DefaultReplayStrategy(5);
        }
        return new ExceptionIfDroppedReplayStrategy(1);
    }

    protected ReplayStrategy createReplayStrategy() {
        return new DefaultReplayStrategy(5);
    }

    protected Transport configureClientSideNegotiator(Transport transport, WireFormat format, UdpTransport udpTransport) {
        return new ResponseRedirectInterceptor(transport, udpTransport);
    }

    protected OpenWireFormat asOpenWireFormat(WireFormat wf) {
        return (OpenWireFormat) wf;
    }
}
