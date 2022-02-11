package org.apache.activemq.transport.failover;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.apache.activemq.transport.MutexTransport;
import org.apache.activemq.transport.ResponseCorrelator;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFactory;
import org.apache.activemq.transport.TransportServer;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.util.URISupport.CompositeData;

public class FailoverTransportFactory extends TransportFactory {
    public Transport doConnect(URI location) throws IOException {
        try {
            return new ResponseCorrelator(new MutexTransport(createTransport(URISupport.parseComposite(location))));
        } catch (URISyntaxException e) {
            throw new IOException("Invalid location: " + location);
        }
    }

    public Transport doCompositeConnect(URI location) throws IOException {
        try {
            return createTransport(URISupport.parseComposite(location));
        } catch (URISyntaxException e) {
            throw new IOException("Invalid location: " + location);
        }
    }

    public Transport createTransport(CompositeData compositData) throws IOException {
        Map options = compositData.getParameters();
        FailoverTransport transport = createTransport(options);
        if (options.isEmpty()) {
            transport.add(false, compositData.getComponents());
            return transport;
        }
        throw new IllegalArgumentException("Invalid connect parameters: " + options);
    }

    public FailoverTransport createTransport(Map<String, String> parameters) throws IOException {
        FailoverTransport transport = new FailoverTransport();
        IntrospectionSupport.setProperties(transport, parameters);
        return transport;
    }

    public TransportServer doBind(URI location) throws IOException {
        throw new IOException("Invalid server URI: " + location);
    }
}
