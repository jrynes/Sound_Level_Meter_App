package org.apache.activemq.transport.discovery;

import org.apache.activemq.command.DiscoveryEvent;

public interface DiscoveryListener {
    public static final String DISCOVERED_OPTION_PREFIX = "discovered.";

    void onServiceAdd(DiscoveryEvent discoveryEvent);

    void onServiceRemove(DiscoveryEvent discoveryEvent);
}
