package org.apache.activemq.transport;

public interface TransmitCallback {
    void onFailure();

    void onSuccess();
}
