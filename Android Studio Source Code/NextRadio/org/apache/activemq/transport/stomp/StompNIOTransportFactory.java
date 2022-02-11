package org.apache.activemq.transport.stomp;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.apache.activemq.broker.BrokerContext;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.BrokerServiceAware;
import org.apache.activemq.transport.MutexTransport;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.nio.NIOTransportFactory;
import org.apache.activemq.transport.tcp.TcpTransport;
import org.apache.activemq.transport.tcp.TcpTransportFactory;
import org.apache.activemq.transport.tcp.TcpTransportServer;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.wireformat.WireFormat;

public class StompNIOTransportFactory extends NIOTransportFactory implements BrokerServiceAware {
    private BrokerContext brokerContext;

    class 1 extends TcpTransportServer {
        1(TcpTransportFactory x0, URI x1, ServerSocketFactory x2) {
            super(x0, x1, x2);
        }

        protected Transport createTransport(Socket socket, WireFormat format) throws IOException {
            return new StompNIOTransport(format, socket);
        }
    }

    public StompNIOTransportFactory() {
        this.brokerContext = null;
    }

    protected String getDefaultWireFormatType() {
        return "stomp";
    }

    protected TcpTransportServer createTcpTransportServer(URI location, ServerSocketFactory serverSocketFactory) throws IOException, URISyntaxException {
        return new 1(this, location, serverSocketFactory);
    }

    protected TcpTransport createTcpTransport(WireFormat wf, SocketFactory socketFactory, URI location, URI localLocation) throws UnknownHostException, IOException {
        return new StompNIOTransport(wf, socketFactory, location, localLocation);
    }

    public Transport serverConfigure(Transport transport, WireFormat format, HashMap options) throws Exception {
        transport = super.serverConfigure(transport, format, options);
        MutexTransport mutex = (MutexTransport) transport.narrow(MutexTransport.class);
        if (mutex != null) {
            mutex.setSyncOnCommand(true);
        }
        return transport;
    }

    public Transport compositeConfigure(Transport transport, WireFormat format, Map options) {
        Transport transport2 = new StompTransportFilter(transport, format, this.brokerContext);
        IntrospectionSupport.setProperties(transport2, options);
        return super.compositeConfigure(transport2, format, options);
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
