package org.apache.activemq.transport.tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.wireformat.WireFormat;

public class SslTransportServer extends TcpTransportServer {
    private boolean needClientAuth;
    private boolean wantClientAuth;

    public SslTransportServer(SslTransportFactory transportFactory, URI location, SSLServerSocketFactory serverSocketFactory) throws IOException, URISyntaxException {
        super(transportFactory, location, serverSocketFactory);
    }

    public void setNeedClientAuth(boolean needAuth) {
        this.needClientAuth = needAuth;
    }

    public boolean getNeedClientAuth() {
        return this.needClientAuth;
    }

    public boolean getWantClientAuth() {
        return this.wantClientAuth;
    }

    public void setWantClientAuth(boolean wantAuth) {
        this.wantClientAuth = wantAuth;
    }

    public void bind() throws IOException {
        super.bind();
        if (this.needClientAuth) {
            ((SSLServerSocket) this.serverSocket).setNeedClientAuth(true);
        } else if (this.wantClientAuth) {
            ((SSLServerSocket) this.serverSocket).setWantClientAuth(true);
        }
    }

    protected Transport createTransport(Socket socket, WireFormat format) throws IOException {
        return new SslTransport(format, (SSLSocket) socket);
    }

    public boolean isSslServer() {
        return true;
    }
}
