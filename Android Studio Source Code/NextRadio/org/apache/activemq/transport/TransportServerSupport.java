package org.apache.activemq.transport;

import java.net.URI;
import java.util.Map;
import org.apache.activemq.util.ServiceSupport;

public abstract class TransportServerSupport extends ServiceSupport implements TransportServer {
    private TransportAcceptListener acceptListener;
    private URI bindLocation;
    private URI connectURI;
    protected Map<String, Object> transportOptions;

    public TransportServerSupport(URI location) {
        this.connectURI = location;
        this.bindLocation = location;
    }

    public TransportAcceptListener getAcceptListener() {
        return this.acceptListener;
    }

    public void setAcceptListener(TransportAcceptListener acceptListener) {
        this.acceptListener = acceptListener;
    }

    public URI getConnectURI() {
        return this.connectURI;
    }

    public void setConnectURI(URI location) {
        this.connectURI = location;
    }

    protected void onAcceptError(Exception e) {
        if (this.acceptListener != null) {
            this.acceptListener.onAcceptError(e);
        }
    }

    public URI getBindLocation() {
        return this.bindLocation;
    }

    public void setBindLocation(URI bindLocation) {
        this.bindLocation = bindLocation;
    }

    public void setTransportOption(Map<String, Object> transportOptions) {
        this.transportOptions = transportOptions;
    }
}
