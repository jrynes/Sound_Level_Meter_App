package org.apache.activemq.transport.reliable;

import java.io.IOException;

public interface ReplayStrategy {
    boolean onDroppedPackets(ReliableTransport reliableTransport, int i, int i2, int i3) throws IOException;

    void onReceivedPacket(ReliableTransport reliableTransport, long j);
}
