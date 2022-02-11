package org.apache.activemq.transport;

import java.io.IOException;
import java.net.URI;

public class TransportFilter implements TransportListener, Transport {
    protected final Transport next;
    protected TransportListener transportListener;

    public TransportFilter(Transport next) {
        this.next = next;
    }

    public TransportListener getTransportListener() {
        return this.transportListener;
    }

    public void setTransportListener(TransportListener channelListener) {
        this.transportListener = channelListener;
        if (channelListener == null) {
            this.next.setTransportListener(null);
        } else {
            this.next.setTransportListener(this);
        }
    }

    public void start() throws Exception {
        if (this.next == null) {
            throw new IOException("The next channel has not been set.");
        } else if (this.transportListener == null) {
            throw new IOException("The command listener has not been set.");
        } else {
            this.next.start();
        }
    }

    public void stop() throws Exception {
        this.next.stop();
    }

    public void onCommand(Object command) {
        this.transportListener.onCommand(command);
    }

    public Transport getNext() {
        return this.next;
    }

    public String toString() {
        return this.next.toString();
    }

    public void oneway(Object command) throws IOException {
        this.next.oneway(command);
    }

    public FutureResponse asyncRequest(Object command, ResponseCallback responseCallback) throws IOException {
        return this.next.asyncRequest(command, null);
    }

    public Object request(Object command) throws IOException {
        return this.next.request(command);
    }

    public Object request(Object command, int timeout) throws IOException {
        return this.next.request(command, timeout);
    }

    public void onException(IOException error) {
        this.transportListener.onException(error);
    }

    public void transportInterupted() {
        this.transportListener.transportInterupted();
    }

    public void transportResumed() {
        this.transportListener.transportResumed();
    }

    public <T> T narrow(Class<T> target) {
        if (target.isAssignableFrom(getClass())) {
            return target.cast(this);
        }
        return this.next.narrow(target);
    }

    public String getRemoteAddress() {
        return this.next.getRemoteAddress();
    }

    public boolean isFaultTolerant() {
        return this.next.isFaultTolerant();
    }

    public boolean isDisposed() {
        return this.next.isDisposed();
    }

    public boolean isConnected() {
        return this.next.isConnected();
    }

    public void reconnect(URI uri) throws IOException {
        this.next.reconnect(uri);
    }

    public int getReceiveCounter() {
        return this.next.getReceiveCounter();
    }

    public boolean isReconnectSupported() {
        return this.next.isReconnectSupported();
    }

    public boolean isUpdateURIsSupported() {
        return this.next.isUpdateURIsSupported();
    }

    public void updateURIs(boolean rebalance, URI[] uris) throws IOException {
        this.next.updateURIs(rebalance, uris);
    }
}
