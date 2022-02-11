package org.apache.activemq.transport;

import java.io.IOException;

public class DefaultTransportListener implements TransportListener {
    public void onCommand(Object command) {
    }

    public void onException(IOException error) {
    }

    public void transportInterupted() {
    }

    public void transportResumed() {
    }
}
