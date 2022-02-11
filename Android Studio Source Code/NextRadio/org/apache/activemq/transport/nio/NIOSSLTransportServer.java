package org.apache.activemq.transport.nio;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.tcp.TcpTransportFactory;
import org.apache.activemq.transport.tcp.TcpTransportServer;
import org.apache.activemq.wireformat.WireFormat;

public class NIOSSLTransportServer extends TcpTransportServer {
    private SSLContext context;
    private boolean needClientAuth;
    private boolean wantClientAuth;

    public NIOSSLTransportServer(SSLContext context, TcpTransportFactory transportFactory, URI location, ServerSocketFactory serverSocketFactory) throws IOException, URISyntaxException {
        super(transportFactory, location, serverSocketFactory);
        this.context = context;
    }

    protected Transport createTransport(Socket socket, WireFormat format) throws IOException {
        NIOSSLTransport transport = new NIOSSLTransport(format, socket);
        if (this.context != null) {
            transport.setSslContext(this.context);
        }
        transport.setNeedClientAuth(this.needClientAuth);
        transport.setWantClientAuth(this.wantClientAuth);
        return transport;
    }

    public boolean isSslServer() {
        return true;
    }

    public boolean isNeedClientAuth() {
        return this.needClientAuth;
    }

    public void setNeedClientAuth(boolean value) {
        this.needClientAuth = value;
    }

    public boolean isWantClientAuth() {
        return this.wantClientAuth;
    }

    public void setWantClientAuth(boolean value) {
        this.wantClientAuth = value;
    }
}
