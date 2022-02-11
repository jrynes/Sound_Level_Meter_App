package org.apache.activemq.transport;

public interface TransportAcceptListener {
    void onAccept(Transport transport);

    void onAcceptError(Exception exception);
}
