package org.apache.activemq.transport.stomp;

import java.io.IOException;
import org.apache.activemq.command.Command;

public interface StompTransport {
    StompInactivityMonitor getInactivityMonitor();

    StompWireFormat getWireFormat();

    void onException(IOException iOException);

    void sendToActiveMQ(Command command);

    void sendToStomp(StompFrame stompFrame) throws IOException;
}
