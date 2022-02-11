package org.apache.activemq.transport.tcp;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.apache.activemq.broker.SslContext;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportServer;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SslTransportFactory extends TcpTransportFactory {
    private static final Logger LOG;

    static {
        LOG = LoggerFactory.getLogger(SslTransportFactory.class);
    }

    public TransportServer doBind(URI location) throws IOException {
        try {
            Map<String, String> options = new HashMap(URISupport.parseParameters(location));
            SslTransportServer server = createSslTransportServer(location, (SSLServerSocketFactory) createServerSocketFactory());
            server.setWireFormatFactory(createWireFormatFactory(options));
            IntrospectionSupport.setProperties(server, options);
            server.setTransportOption(IntrospectionSupport.extractProperties(options, "transport."));
            server.bind();
            return server;
        } catch (Exception e) {
            throw IOExceptionSupport.create(e);
        }
    }

    protected SslTransportServer createSslTransportServer(URI location, SSLServerSocketFactory serverSocketFactory) throws IOException, URISyntaxException {
        return new SslTransportServer(this, location, serverSocketFactory);
    }

    public Transport compositeConfigure(Transport transport, WireFormat format, Map options) {
        IntrospectionSupport.setProperties((SslTransport) transport.narrow(SslTransport.class), options);
        return super.compositeConfigure(transport, format, options);
    }

    protected Transport createTransport(URI location, WireFormat wf) throws UnknownHostException, IOException {
        URI localLocation = null;
        String path = location.getPath();
        if (path != null && path.length() > 0) {
            try {
                Integer.parseInt(path.substring(path.indexOf(58) + 1, path.length()));
                localLocation = new URI(location.getScheme() + ":/" + path);
            } catch (Exception e) {
                LOG.warn("path isn't a valid local location for SslTransport to use", e);
            }
        }
        return new SslTransport(wf, (SSLSocketFactory) createSocketFactory(), location, localLocation, false);
    }

    protected ServerSocketFactory createServerSocketFactory() throws IOException {
        if (SslContext.getCurrentSslContext() == null) {
            return SSLServerSocketFactory.getDefault();
        }
        try {
            return SslContext.getCurrentSslContext().getSSLContext().getServerSocketFactory();
        } catch (Exception e) {
            throw IOExceptionSupport.create(e);
        }
    }

    protected SocketFactory createSocketFactory() throws IOException {
        if (SslContext.getCurrentSslContext() == null) {
            return SSLSocketFactory.getDefault();
        }
        try {
            return SslContext.getCurrentSslContext().getSSLContext().getSocketFactory();
        } catch (Exception e) {
            throw IOExceptionSupport.create(e);
        }
    }
}
