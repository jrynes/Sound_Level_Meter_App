package org.apache.activemq.transport.stomp;

import java.io.IOException;
import org.apache.activemq.broker.BrokerContext;
import org.apache.activemq.command.Command;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFilter;
import org.apache.activemq.transport.TransportListener;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StompTransportFilter extends TransportFilter implements StompTransport {
    private static final Logger TRACE;
    private StompInactivityMonitor monitor;
    private final ProtocolConverter protocolConverter;
    private boolean trace;
    private StompWireFormat wireFormat;

    static {
        TRACE = LoggerFactory.getLogger(StompTransportFilter.class.getPackage().getName() + ".StompIO");
    }

    public StompTransportFilter(Transport next, WireFormat wireFormat, BrokerContext brokerContext) {
        super(next);
        this.protocolConverter = new ProtocolConverter(this, brokerContext);
        if (wireFormat instanceof StompWireFormat) {
            this.wireFormat = (StompWireFormat) wireFormat;
        }
    }

    public void oneway(Object o) throws IOException {
        try {
            this.protocolConverter.onActiveMQCommand((Command) o);
        } catch (Exception e) {
            throw IOExceptionSupport.create(e);
        }
    }

    public void onCommand(Object command) {
        try {
            if (this.trace) {
                TRACE.trace("Received: \n" + command);
            }
            this.protocolConverter.onStompCommand((StompFrame) command);
        } catch (IOException e) {
            onException(e);
        } catch (Exception e2) {
            onException(IOExceptionSupport.create(e2));
        }
    }

    public void sendToActiveMQ(Command command) {
        TransportListener l = this.transportListener;
        if (l != null) {
            l.onCommand(command);
        }
    }

    public void sendToStomp(StompFrame command) throws IOException {
        if (this.trace) {
            TRACE.trace("Sending: \n" + command);
        }
        Transport n = this.next;
        if (n != null) {
            n.oneway(command);
        }
    }

    public boolean isTrace() {
        return this.trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public StompInactivityMonitor getInactivityMonitor() {
        return this.monitor;
    }

    public void setInactivityMonitor(StompInactivityMonitor monitor) {
        this.monitor = monitor;
    }

    public StompWireFormat getWireFormat() {
        return this.wireFormat;
    }

    public String getDefaultHeartBeat() {
        return this.protocolConverter != null ? this.protocolConverter.getDefaultHeartBeat() : null;
    }

    public void setDefaultHeartBeat(String defaultHeartBeat) {
        this.protocolConverter.setDefaultHeartBeat(defaultHeartBeat);
    }
}
