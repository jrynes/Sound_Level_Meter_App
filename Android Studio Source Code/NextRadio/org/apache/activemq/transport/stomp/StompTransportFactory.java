package org.apache.activemq.transport.stomp;

import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.broker.BrokerContext;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.BrokerServiceAware;
import org.apache.activemq.transport.MutexTransport;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.tcp.TcpTransportFactory;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.wireformat.WireFormat;

public class StompTransportFactory extends TcpTransportFactory implements BrokerServiceAware {
    private BrokerContext brokerContext;

    public StompTransportFactory() {
        this.brokerContext = null;
    }

    protected String getDefaultWireFormatType() {
        return "stomp";
    }

    public Transport compositeConfigure(Transport transport, WireFormat format, Map options) {
        Transport transport2 = new StompTransportFilter(transport, format, this.brokerContext);
        IntrospectionSupport.setProperties(transport2, options);
        return super.compositeConfigure(transport2, format, options);
    }

    public void setBrokerService(BrokerService brokerService) {
        this.brokerContext = brokerService.getBrokerContext();
    }

    public Transport serverConfigure(Transport transport, WireFormat format, HashMap options) throws Exception {
        transport = super.serverConfigure(transport, format, options);
        MutexTransport mutex = (MutexTransport) transport.narrow(MutexTransport.class);
        if (mutex != null) {
            mutex.setSyncOnCommand(true);
        }
        return transport;
    }

    protected Transport createInactivityMonitor(Transport transport, WireFormat format) {
        StompInactivityMonitor monitor = new StompInactivityMonitor(transport, format);
        ((StompTransportFilter) transport.narrow(StompTransportFilter.class)).setInactivityMonitor(monitor);
        return monitor;
    }
}
