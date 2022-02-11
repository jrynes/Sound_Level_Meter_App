package org.apache.activemq.transport;

public interface ResponseCallback {
    void onCompletion(FutureResponse futureResponse);
}
