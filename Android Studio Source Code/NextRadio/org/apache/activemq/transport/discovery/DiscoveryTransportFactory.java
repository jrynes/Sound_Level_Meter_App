package org.apache.activemq.transport.discovery;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.transport.CompositeTransport;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportServer;
import org.apache.activemq.transport.failover.FailoverTransportFactory;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport.CompositeData;

public class DiscoveryTransportFactory extends FailoverTransportFactory {
    public Transport createTransport(CompositeData compositeData) throws IOException {
        Map<String, String> parameters = new HashMap(compositeData.getParameters());
        return createTransport(createTransport((Map) parameters), compositeData, parameters);
    }

    public static DiscoveryTransport createTransport(CompositeTransport compositeTransport, CompositeData compositeData, Map<String, String> parameters) throws IOException {
        DiscoveryTransport transport = new DiscoveryTransport(compositeTransport);
        IntrospectionSupport.setProperties(transport, parameters);
        transport.setParameters(parameters);
        transport.setDiscoveryAgent(DiscoveryAgentFactory.createDiscoveryAgent(compositeData.getComponents()[0]));
        return transport;
    }

    public TransportServer doBind(URI location) throws IOException {
        throw new IOException("Invalid server URI: " + location);
    }
}
