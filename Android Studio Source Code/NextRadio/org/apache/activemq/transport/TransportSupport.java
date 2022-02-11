package org.apache.activemq.transport;

import java.io.IOException;
import java.net.URI;
import org.apache.activemq.util.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TransportSupport extends ServiceSupport implements Transport {
    private static final Logger LOG;
    TransportListener transportListener;

    static {
        LOG = LoggerFactory.getLogger(TransportSupport.class);
    }

    public TransportListener getTransportListener() {
        return this.transportListener;
    }

    public void setTransportListener(TransportListener commandListener) {
        this.transportListener = commandListener;
    }

    public <T> T narrow(Class<T> target) {
        if (target.isAssignableFrom(getClass())) {
            return target.cast(this);
        }
        return null;
    }

    public FutureResponse asyncRequest(Object command, ResponseCallback responseCallback) throws IOException {
        throw new AssertionError("Unsupported Method");
    }

    public Object request(Object command) throws IOException {
        throw new AssertionError("Unsupported Method");
    }

    public Object request(Object command, int timeout) throws IOException {
        throw new AssertionError("Unsupported Method");
    }

    public void doConsume(Object command) {
        if (command == null) {
            return;
        }
        if (this.transportListener != null) {
            this.transportListener.onCommand(command);
        } else {
            LOG.error("No transportListener available to process inbound command: " + command);
        }
    }

    public void onException(IOException e) {
        if (this.transportListener != null) {
            try {
                this.transportListener.onException(e);
            } catch (RuntimeException e2) {
                LOG.debug("Unexpected runtime exception: " + e2, e2);
            }
        }
    }

    protected void checkStarted() throws IOException {
        if (!isStarted()) {
            throw new IOException("The transport is not running.");
        }
    }

    public boolean isFaultTolerant() {
        return false;
    }

    public void reconnect(URI uri) throws IOException {
        throw new IOException("Not supported");
    }

    public boolean isReconnectSupported() {
        return false;
    }

    public boolean isUpdateURIsSupported() {
        return false;
    }

    public void updateURIs(boolean reblance, URI[] uris) throws IOException {
        throw new IOException("Not supported");
    }

    public boolean isDisposed() {
        return isStopped();
    }

    public boolean isConnected() {
        return isStarted();
    }
}
