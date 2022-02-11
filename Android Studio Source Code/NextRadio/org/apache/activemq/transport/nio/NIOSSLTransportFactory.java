package org.apache.activemq.transport.nio;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Map;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.activemq.broker.SslContext;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportServer;
import org.apache.activemq.transport.tcp.SslTransport;
import org.apache.activemq.transport.tcp.TcpTransportServer;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NIOSSLTransportFactory extends NIOTransportFactory {
    private static final Logger LOG;
    protected SSLContext context;

    static {
        LOG = LoggerFactory.getLogger(NIOSSLTransportFactory.class);
    }

    protected TcpTransportServer createTcpTransportServer(URI location, ServerSocketFactory serverSocketFactory) throws IOException, URISyntaxException {
        return new NIOSSLTransportServer(this.context, this, location, serverSocketFactory);
    }

    public TransportServer doBind(URI location) throws IOException {
        if (SslContext.getCurrentSslContext() != null) {
            try {
                this.context = SslContext.getCurrentSslContext().getSSLContext();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        return super.doBind(location);
    }

    public Transport compositeConfigure(Transport transport, WireFormat format, Map options) {
        if (transport instanceof SslTransport) {
            IntrospectionSupport.setProperties((SslTransport) transport.narrow(SslTransport.class), options);
        } else if (transport instanceof NIOSSLTransport) {
            IntrospectionSupport.setProperties((NIOSSLTransport) transport.narrow(NIOSSLTransport.class), options);
        }
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
