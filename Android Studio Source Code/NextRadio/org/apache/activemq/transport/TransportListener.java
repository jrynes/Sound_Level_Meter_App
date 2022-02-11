package org.apache.activemq.transport;

import java.io.IOException;

public interface TransportListener {
    void onCommand(Object obj);

    void onException(IOException iOException);

    void transportInterupted();

    void transportResumed();
}
