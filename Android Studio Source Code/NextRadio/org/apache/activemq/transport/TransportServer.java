package org.apache.activemq.transport;

import java.net.InetSocketAddress;
import java.net.URI;
import org.apache.activemq.Service;
import org.apache.activemq.command.BrokerInfo;

public interface TransportServer extends Service {
    URI getConnectURI();

    InetSocketAddress getSocketAddress();

    boolean isSslServer();

    void setAcceptListener(TransportAcceptListener transportAcceptListener);

    void setBrokerInfo(BrokerInfo brokerInfo);
}
