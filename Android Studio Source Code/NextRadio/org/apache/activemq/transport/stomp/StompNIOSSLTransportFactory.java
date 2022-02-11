package org.apache.activemq.transport.stomp;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.activemq.broker.SslContext;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportServer;
import org.apache.activemq.transport.nio.NIOSSLTransportServer;
import org.apache.activemq.transport.tcp.TcpTransport;
import org.apache.activemq.transport.tcp.TcpTransportFactory;
import org.apache.activemq.transport.tcp.TcpTransportServer;
import org.apache.activemq.wireformat.WireFormat;

public class StompNIOSSLTransportFactory extends StompNIOTransportFactory {
    protected SSLContext context;

    class 1 extends NIOSSLTransportServer {
        1(SSLContext x0, TcpTransportFactory x1, URI x2, ServerSocketFactory x3) {
            super(x0, x1, x2, x3);
        }

        protected Transport createTransport(Socket socket, WireFormat format) throws IOException {
            StompNIOSSLTransport transport = new StompNIOSSLTransport(format, socket);
            if (StompNIOSSLTransportFactory.this.context != null) {
                transport.setSslContext(StompNIOSSLTransportFactory.this.context);
            }
            transport.setNeedClientAuth(isNeedClientAuth());
            transport.setWantClientAuth(isWantClientAuth());
            return transport;
        }
    }

    protected TcpTransportServer createTcpTransportServer(URI location, ServerSocketFactory serverSocketFactory) throws IOException, URISyntaxException {
        return new 1(this.context, this, location, serverSocketFactory);
    }

    protected TcpTransport createTcpTransport(WireFormat wf, SocketFactory socketFactory, URI location, URI localLocation) throws UnknownHostException, IOException {
        return new StompNIOSSLTransport(wf, socketFactory, location, localLocation);
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
}
