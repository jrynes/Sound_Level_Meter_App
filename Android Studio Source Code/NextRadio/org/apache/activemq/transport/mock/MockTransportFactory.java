package org.apache.activemq.transport.mock;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.activemq.transport.MutexTransport;
import org.apache.activemq.transport.ResponseCorrelator;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFactory;
import org.apache.activemq.transport.TransportServer;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.util.URISupport.CompositeData;

public class MockTransportFactory extends TransportFactory {
    public Transport doConnect(URI location) throws URISyntaxException, Exception {
        return new ResponseCorrelator(new MutexTransport(createTransport(URISupport.parseComposite(location))));
    }

    public Transport doCompositeConnect(URI location) throws URISyntaxException, Exception {
        return createTransport(URISupport.parseComposite(location));
    }

    public Transport createTransport(CompositeData compositData) throws Exception {
        MockTransport transport = new MockTransport(TransportFactory.compositeConnect(compositData.getComponents()[0]));
        IntrospectionSupport.setProperties(transport, compositData.getParameters());
        return transport;
    }

    public TransportServer doBind(URI location) throws IOException {
        throw new IOException("This protocol does not support being bound.");
    }
}
