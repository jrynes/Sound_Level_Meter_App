package org.apache.activemq.transport;

import java.io.IOException;
import java.net.URI;
import org.apache.activemq.Service;

public interface Transport extends Service {
    FutureResponse asyncRequest(Object obj, ResponseCallback responseCallback) throws IOException;

    int getReceiveCounter();

    String getRemoteAddress();

    TransportListener getTransportListener();

    boolean isConnected();

    boolean isDisposed();

    boolean isFaultTolerant();

    boolean isReconnectSupported();

    boolean isUpdateURIsSupported();

    <T> T narrow(Class<T> cls);

    void oneway(Object obj) throws IOException;

    void reconnect(URI uri) throws IOException;

    Object request(Object obj) throws IOException;

    Object request(Object obj, int i) throws IOException;

    void setTransportListener(TransportListener transportListener);

    void updateURIs(boolean z, URI[] uriArr) throws IOException;
}
