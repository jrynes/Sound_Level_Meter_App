package org.apache.activemq.transport.tcp;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.apache.activemq.TransportLoggerSupport;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.transport.InactivityMonitor;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFactory;
import org.apache.activemq.transport.TransportServer;
import org.apache.activemq.transport.WireFormatNegotiator;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpTransportFactory extends TransportFactory {
    private static final Logger LOG;

    static {
        LOG = LoggerFactory.getLogger(TcpTransportFactory.class);
    }

    public TransportServer doBind(URI location) throws IOException {
        try {
            Map<String, String> options = new HashMap(URISupport.parseParameters(location));
            TcpTransportServer server = createTcpTransportServer(location, createServerSocketFactory());
            server.setWireFormatFactory(createWireFormatFactory(options));
            IntrospectionSupport.setProperties(server, options);
            server.setTransportOption(IntrospectionSupport.extractProperties(options, "transport."));
            server.bind();
            return server;
        } catch (Exception e) {
            throw IOExceptionSupport.create(e);
        }
    }

    protected TcpTransportServer createTcpTransportServer(URI location, ServerSocketFactory serverSocketFactory) throws IOException, URISyntaxException {
        return new TcpTransportServer(this, location, serverSocketFactory);
    }

    public Transport compositeConfigure(Transport transport, WireFormat format, Map options) {
        TcpTransport tcpTransport = (TcpTransport) transport.narrow(TcpTransport.class);
        IntrospectionSupport.setProperties(tcpTransport, options);
        tcpTransport.setSocketOptions(IntrospectionSupport.extractProperties(options, "socket."));
        if (tcpTransport.isTrace()) {
            try {
                transport = TransportLoggerSupport.createTransportLogger(transport, tcpTransport.getLogWriterName(), tcpTransport.isDynamicManagement(), tcpTransport.isStartLogging(), tcpTransport.getJmxPort());
            } catch (Throwable e) {
                LOG.error("Could not create TransportLogger object for: " + tcpTransport.getLogWriterName() + ", reason: " + e, e);
            }
        }
        if (Stomp.TRUE.equals(getOption(options, "useInactivityMonitor", Stomp.TRUE)) && isUseInactivityMonitor(transport)) {
            transport = createInactivityMonitor(transport, format);
            IntrospectionSupport.setProperties(transport, options);
        }
        if (format instanceof OpenWireFormat) {
            transport = new WireFormatNegotiator(transport, (OpenWireFormat) format, tcpTransport.getMinmumWireFormatVersion());
        }
        return super.compositeConfigure(transport, format, options);
    }

    protected boolean isUseInactivityMonitor(Transport transport) {
        return true;
    }

    protected Transport createTransport(URI location, WireFormat wf) throws UnknownHostException, IOException {
        URI localLocation = null;
        String path = location.getPath();
        if (path != null && path.length() > 0) {
            try {
                Integer.parseInt(path.substring(path.indexOf(58) + 1, path.length()));
                localLocation = new URI(location.getScheme() + ":/" + path);
            } catch (Exception e) {
                LOG.warn("path isn't a valid local location for TcpTransport to use", e.getMessage());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Failure detail", e);
                }
            }
        }
        return createTcpTransport(wf, createSocketFactory(), location, localLocation);
    }

    protected TcpTransport createTcpTransport(WireFormat wf, SocketFactory socketFactory, URI location, URI localLocation) throws UnknownHostException, IOException {
        return new TcpTransport(wf, socketFactory, location, localLocation);
    }

    protected ServerSocketFactory createServerSocketFactory() throws IOException {
        return ServerSocketFactory.getDefault();
    }

    protected SocketFactory createSocketFactory() throws IOException {
        return SocketFactory.getDefault();
    }

    protected Transport createInactivityMonitor(Transport transport, WireFormat format) {
        return new InactivityMonitor(transport, format);
    }
}
