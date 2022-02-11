package org.apache.activemq.transport.fanout;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.apache.activemq.transport.MutexTransport;
import org.apache.activemq.transport.ResponseCorrelator;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFactory;
import org.apache.activemq.transport.TransportServer;
import org.apache.activemq.transport.discovery.DiscoveryTransportFactory;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.util.URISupport.CompositeData;

public class FanoutTransportFactory extends TransportFactory {
    public Transport doConnect(URI location) throws IOException {
        try {
            return new ResponseCorrelator(new MutexTransport(createTransport(location)));
        } catch (URISyntaxException e) {
            throw new IOException("Invalid location: " + location);
        }
    }

    public Transport doCompositeConnect(URI location) throws IOException {
        try {
            return createTransport(location);
        } catch (URISyntaxException e) {
            throw new IOException("Invalid location: " + location);
        }
    }

    public Transport createTransport(URI location) throws IOException, URISyntaxException {
        CompositeData compositeData = URISupport.parseComposite(location);
        Map parameters = compositeData.getParameters();
        return DiscoveryTransportFactory.createTransport(createTransport(parameters), compositeData, parameters);
    }

    public FanoutTransport createTransport(Map<String, String> parameters) throws IOException {
        FanoutTransport transport = new FanoutTransport();
        IntrospectionSupport.setProperties(transport, parameters);
        return transport;
    }

    public TransportServer doBind(URI location) throws IOException {
        throw new IOException("Invalid server URI: " + location);
    }
}
