package org.apache.activemq.transport.discovery;

import java.io.IOException;
import org.apache.activemq.Service;
import org.apache.activemq.command.DiscoveryEvent;

public interface DiscoveryAgent extends Service {
    void registerService(String str) throws IOException;

    void serviceFailed(DiscoveryEvent discoveryEvent) throws IOException;

    void setDiscoveryListener(DiscoveryListener discoveryListener);
}
