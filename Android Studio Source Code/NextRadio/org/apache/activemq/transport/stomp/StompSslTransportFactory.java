package org.apache.activemq.transport.stomp;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import org.apache.activemq.broker.BrokerContext;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.BrokerServiceAware;
import org.apache.activemq.transport.MutexTransport;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.tcp.SslTransport;
import org.apache.activemq.transport.tcp.SslTransportFactory;
import org.apache.activemq.transport.tcp.SslTransportServer;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.wireformat.WireFormat;

public class StompSslTransportFactory extends SslTransportFactory implements BrokerServiceAware {
    private BrokerContext brokerContext;

    class 1 extends SslTransportServer {

        class 1 extends SslTransport {
            private X509Certificate[] cachedPeerCerts;

            1(WireFormat x0, SSLSocket x1) {
                super(x0, x1);
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

        1(SslTransportFactory x0, URI x1, SSLServerSocketFactory x2) {
            super(x0, x1, x2);
        }

        protected Transport createTransport(Socket socket, WireFormat format) throws IOException {
            return new 1(format, (SSLSocket) socket);
        }
    }

    public StompSslTransportFactory() {
        this.brokerContext = null;
    }

    protected String getDefaultWireFormatType() {
        return "stomp";
    }

    protected SslTransportServer createSslTransportServer(URI location, SSLServerSocketFactory serverSocketFactory) throws IOException, URISyntaxException {
        return new 1(this, location, serverSocketFactory);
    }

    public Transport compositeConfigure(Transport transport, WireFormat format, Map options) {
        Transport transport2 = new StompTransportFilter(transport, format, this.brokerContext);
        IntrospectionSupport.setProperties(transport2, options);
        return super.compositeConfigure(transport2, format, options);
    }

    public Transport serverConfigure(Transport transport, WireFormat format, HashMap options) throws Exception {
        transport = super.serverConfigure(transport, format, options);
        MutexTransport mutex = (MutexTransport) transport.narrow(MutexTransport.class);
        if (mutex != null) {
            mutex.setSyncOnCommand(true);
        }
        return transport;
    }

    public void setBrokerService(BrokerService brokerService) {
        this.brokerContext = brokerService.getBrokerContext();
    }

    protected Transport createInactivityMonitor(Transport transport, WireFormat format) {
        StompInactivityMonitor monitor = new StompInactivityMonitor(transport, format);
        ((StompTransportFilter) transport.narrow(StompTransportFilter.class)).setInactivityMonitor(monitor);
        return monitor;
    }
}
