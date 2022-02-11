package org.apache.activemq.transport.tcp;

import java.io.IOException;
import java.net.URI;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.wireformat.WireFormat;

public class SslTransport extends TcpTransport {
    public SslTransport(WireFormat wireFormat, SSLSocketFactory socketFactory, URI remoteLocation, URI localLocation, boolean needClientAuth) throws IOException {
        super(wireFormat, socketFactory, remoteLocation, localLocation);
        if (this.socket != null) {
            ((SSLSocket) this.socket).setNeedClientAuth(needClientAuth);
        }
    }

    public SslTransport(WireFormat wireFormat, SSLSocket socket) throws IOException {
        super(wireFormat, socket);
    }

    public void doConsume(Object command) {
        if (command instanceof ConnectionInfo) {
            ((ConnectionInfo) command).setTransportContext(getPeerCertificates());
        }
        super.doConsume(command);
    }

    public X509Certificate[] getPeerCertificates() {
        try {
            return (X509Certificate[]) this.socket.getSession().getPeerCertificates();
        } catch (SSLPeerUnverifiedException e) {
            return null;
        }
    }

    public String toString() {
        return "ssl://" + this.socket.getInetAddress() + Headers.SEPERATOR + this.socket.getPort();
    }
}
