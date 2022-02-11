package org.apache.activemq.transport.discovery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.activemq.command.DiscoveryEvent;
import org.apache.activemq.transport.CompositeTransport;
import org.apache.activemq.transport.TransportFilter;
import org.apache.activemq.util.ServiceStopper;
import org.apache.activemq.util.URISupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscoveryTransport extends TransportFilter implements DiscoveryListener {
    private static final Logger LOG;
    private DiscoveryAgent discoveryAgent;
    private final CompositeTransport next;
    private Map<String, String> parameters;
    private final ConcurrentHashMap<String, URI> serviceURIs;

    static {
        LOG = LoggerFactory.getLogger(DiscoveryTransport.class);
    }

    public DiscoveryTransport(CompositeTransport next) {
        super(next);
        this.serviceURIs = new ConcurrentHashMap();
        this.next = next;
    }

    public void start() throws Exception {
        if (this.discoveryAgent == null) {
            throw new IllegalStateException("discoveryAgent not configured");
        }
        this.discoveryAgent.setDiscoveryListener(this);
        this.discoveryAgent.start();
        this.next.start();
    }

    public void stop() throws Exception {
        ServiceStopper ss = new ServiceStopper();
        ss.stop(this.discoveryAgent);
        ss.stop(this.next);
        ss.throwFirstException();
    }

    public void onServiceAdd(DiscoveryEvent event) {
        String url = event.getServiceName();
        if (url != null) {
            try {
                URI uri = new URI(url);
                LOG.info("Adding new broker connection URL: " + uri);
                this.serviceURIs.put(event.getServiceName(), URISupport.applyParameters(uri, this.parameters, DiscoveryListener.DISCOVERED_OPTION_PREFIX));
                this.next.add(false, new URI[]{uri});
            } catch (URISyntaxException e) {
                LOG.warn("Could not connect to remote URI: " + url + " due to bad URI syntax: " + e, e);
            }
        }
    }

    public void onServiceRemove(DiscoveryEvent event) {
        if (((URI) this.serviceURIs.get(event.getServiceName())) != null) {
            this.next.remove(false, new URI[]{uri});
        }
    }

    public DiscoveryAgent getDiscoveryAgent() {
        return this.discoveryAgent;
    }

    public void setDiscoveryAgent(DiscoveryAgent discoveryAgent) {
        this.discoveryAgent = discoveryAgent;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
