package org.apache.activemq.transport.mock;

import java.io.IOException;
import java.net.URI;
import org.apache.activemq.transport.DefaultTransportListener;
import org.apache.activemq.transport.FutureResponse;
import org.apache.activemq.transport.ResponseCallback;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFilter;
import org.apache.activemq.transport.TransportListener;

public class MockTransport extends DefaultTransportListener implements Transport {
    protected Transport next;
    protected TransportListener transportListener;

    public MockTransport(Transport next) {
        this.next = next;
    }

    public synchronized void setTransportListener(TransportListener channelListener) {
        this.transportListener = channelListener;
        if (channelListener == null) {
            getNext().setTransportListener(null);
        } else {
            getNext().setTransportListener(this);
        }
    }

    public void start() throws Exception {
        if (getNext() == null) {
            throw new IOException("The next channel has not been set.");
        } else if (this.transportListener == null) {
            throw new IOException("The command listener has not been set.");
        } else {
            getNext().start();
        }
    }

    public void stop() throws Exception {
        getNext().stop();
    }

    public void onCommand(Object command) {
        getTransportListener().onCommand(command);
    }

    public synchronized Transport getNext() {
        return this.next;
    }

    public synchronized TransportListener getTransportListener() {
        return this.transportListener;
    }

    public String toString() {
        return getNext().toString();
    }

    public void oneway(Object command) throws IOException {
        getNext().oneway(command);
    }

    public FutureResponse asyncRequest(Object command, ResponseCallback responseCallback) throws IOException {
        return getNext().asyncRequest(command, null);
    }

    public Object request(Object command) throws IOException {
        return getNext().request(command);
    }

    public Object request(Object command, int timeout) throws IOException {
        return getNext().request(command, timeout);
    }

    public void onException(IOException error) {
        getTransportListener().onException(error);
    }

    public <T> T narrow(Class<T> target) {
        if (target.isAssignableFrom(getClass())) {
            return target.cast(this);
        }
        return getNext().narrow(target);
    }

    public synchronized void setNext(Transport next) {
        this.next = next;
    }

    public void install(TransportFilter filter) {
        filter.setTransportListener(this);
        getNext().setTransportListener(filter);
        setNext(filter);
    }

    public String getRemoteAddress() {
        return getNext().getRemoteAddress();
    }

    public boolean isFaultTolerant() {
        return getNext().isFaultTolerant();
    }

    public boolean isDisposed() {
        return getNext().isDisposed();
    }

    public boolean isConnected() {
        return getNext().isConnected();
    }

    public void reconnect(URI uri) throws IOException {
        getNext().reconnect(uri);
    }

    public int getReceiveCounter() {
        return getNext().getReceiveCounter();
    }

    public boolean isReconnectSupported() {
        return getNext().isReconnectSupported();
    }

    public boolean isUpdateURIsSupported() {
        return getNext().isUpdateURIsSupported();
    }

    public void updateURIs(boolean reblance, URI[] uris) throws IOException {
        getNext().updateURIs(reblance, uris);
    }
}
