package org.apache.activemq.transport;

import java.net.URI;

public interface CompositeTransport extends Transport {
    void add(boolean z, URI[] uriArr);

    void remove(boolean z, URI[] uriArr);
}
