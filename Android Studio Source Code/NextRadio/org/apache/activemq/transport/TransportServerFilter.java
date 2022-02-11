package org.apache.activemq.transport;

import java.net.InetSocketAddress;
import java.net.URI;
import org.apache.activemq.command.BrokerInfo;

public class TransportServerFilter implements TransportServer {
    protected final TransportServer next;

    public TransportServerFilter(TransportServer next) {
        this.next = next;
    }

    public URI getConnectURI() {
        return this.next.getConnectURI();
    }

    public void setAcceptListener(TransportAcceptListener acceptListener) {
        this.next.setAcceptListener(acceptListener);
    }

    public void setBrokerInfo(BrokerInfo brokerInfo) {
        this.next.setBrokerInfo(brokerInfo);
    }

    public void start() throws Exception {
        this.next.start();
    }

    public void stop() throws Exception {
        this.next.stop();
    }

    public InetSocketAddress getSocketAddress() {
        return this.next.getSocketAddress();
    }

    public boolean isSslServer() {
        return this.next.isSslServer();
    }
}
