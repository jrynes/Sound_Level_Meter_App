package org.apache.activemq.transport.discovery;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.activemq.util.FactoryFinder;
import org.apache.activemq.util.IOExceptionSupport;

public abstract class DiscoveryAgentFactory {
    private static final ConcurrentHashMap<String, DiscoveryAgentFactory> DISCOVERY_AGENT_FACTORYS;
    private static final FactoryFinder DISCOVERY_AGENT_FINDER;

    protected abstract DiscoveryAgent doCreateDiscoveryAgent(URI uri) throws IOException;

    static {
        DISCOVERY_AGENT_FINDER = new FactoryFinder("META-INF/services/org/apache/activemq/transport/discoveryagent/");
        DISCOVERY_AGENT_FACTORYS = new ConcurrentHashMap();
    }

    private static DiscoveryAgentFactory findDiscoveryAgentFactory(URI uri) throws IOException {
        String scheme = uri.getScheme();
        if (scheme == null) {
            throw new IOException("DiscoveryAgent scheme not specified: [" + uri + "]");
        }
        DiscoveryAgentFactory daf = (DiscoveryAgentFactory) DISCOVERY_AGENT_FACTORYS.get(scheme);
        if (daf != null) {
            return daf;
        }
        try {
            daf = (DiscoveryAgentFactory) DISCOVERY_AGENT_FINDER.newInstance(scheme);
            DISCOVERY_AGENT_FACTORYS.put(scheme, daf);
            return daf;
        } catch (Throwable e) {
            IOException create = IOExceptionSupport.create("DiscoveryAgent scheme NOT recognized: [" + scheme + "]", e);
        }
    }

    public static DiscoveryAgent createDiscoveryAgent(URI uri) throws IOException {
        return findDiscoveryAgentFactory(uri).doCreateDiscoveryAgent(uri);
    }
}
