package org.apache.activemq.transport.discovery.multicast;

import java.io.IOException;
import java.net.URI;
import org.apache.activemq.transport.discovery.DiscoveryAgent;
import org.apache.activemq.transport.discovery.DiscoveryAgentFactory;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MulticastDiscoveryAgentFactory extends DiscoveryAgentFactory {
    private static final Logger LOG;

    static {
        LOG = LoggerFactory.getLogger(MulticastDiscoveryAgentFactory.class);
    }

    protected DiscoveryAgent doCreateDiscoveryAgent(URI uri) throws IOException {
        try {
            if (LOG.isTraceEnabled()) {
                LOG.trace("doCreateDiscoveryAgent: uri = " + uri.toString());
            }
            MulticastDiscoveryAgent mda = new MulticastDiscoveryAgent();
            mda.setDiscoveryURI(uri);
            IntrospectionSupport.setProperties(mda, URISupport.parseParameters(uri));
            return mda;
        } catch (Throwable e) {
            IOException create = IOExceptionSupport.create("Could not create discovery agent: " + uri, e);
        }
    }
}
