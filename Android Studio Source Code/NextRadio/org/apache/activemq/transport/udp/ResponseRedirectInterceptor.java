package org.apache.activemq.transport.udp;

import org.apache.activemq.command.Command;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFilter;

public class ResponseRedirectInterceptor extends TransportFilter {
    private final UdpTransport transport;

    public ResponseRedirectInterceptor(Transport next, UdpTransport transport) {
        super(next);
        this.transport = transport;
    }

    public void onCommand(Object o) {
        Command command = (Command) o;
        this.transport.setTargetEndpoint(command.getFrom());
        super.onCommand(command);
    }
}
