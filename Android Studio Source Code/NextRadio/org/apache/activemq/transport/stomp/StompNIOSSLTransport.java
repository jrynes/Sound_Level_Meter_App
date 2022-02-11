package org.apache.activemq.transport.stomp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.cert.X509Certificate;
import javax.net.SocketFactory;
import org.apache.activemq.transport.nio.NIOSSLTransport;
import org.apache.activemq.wireformat.WireFormat;

public class StompNIOSSLTransport extends NIOSSLTransport {
    private X509Certificate[] cachedPeerCerts;
    StompCodec codec;

    public StompNIOSSLTransport(WireFormat wireFormat, SocketFactory socketFactory, URI remoteLocation, URI localLocation) throws UnknownHostException, IOException {
        super(wireFormat, socketFactory, remoteLocation, localLocation);
    }

    public StompNIOSSLTransport(WireFormat wireFormat, Socket socket) throws IOException {
        super(wireFormat, socket);
    }

    protected void initializeStreams() throws IOException {
        this.codec = new StompCodec(this);
        super.initializeStreams();
        if (this.inputBuffer.position() != 0 && this.inputBuffer.hasRemaining()) {
            serviceRead();
        }
    }

    protected void processCommand(ByteBuffer plain) throws Exception {
        byte[] fill = new byte[plain.remaining()];
        plain.get(fill);
        this.codec.parse(new ByteArrayInputStream(fill), fill.length);
    }

    public void doConsume(Object command) {
        StompFrame frame = (StompFrame) command;
        if (this.cachedPeerCerts == null) {
            this.cachedPeerCerts = getPeerCertificates();
        }
        frame.setTransportContext(this.cachedPeerCerts);
        super.doConsume(command);
    }
}
