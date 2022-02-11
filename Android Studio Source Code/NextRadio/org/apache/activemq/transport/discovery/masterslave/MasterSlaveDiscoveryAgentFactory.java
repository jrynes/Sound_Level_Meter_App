package org.apache.activemq.transport.discovery.masterslave;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import org.apache.activemq.transport.discovery.DiscoveryAgent;
import org.apache.activemq.transport.discovery.DiscoveryAgentFactory;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.util.URISupport.CompositeData;

public class MasterSlaveDiscoveryAgentFactory extends DiscoveryAgentFactory {
    protected DiscoveryAgent doCreateDiscoveryAgent(URI uri) throws IOException {
        try {
            CompositeData data = URISupport.parseComposite(uri);
            Map options = data.getParameters();
            MasterSlaveDiscoveryAgent rc = new MasterSlaveDiscoveryAgent();
            IntrospectionSupport.setProperties(rc, options);
            rc.setServices(data.getComponents());
            return rc;
        } catch (Throwable e) {
            IOException create = IOExceptionSupport.create("Could not create discovery agent: " + uri, e);
        }
    }
}
