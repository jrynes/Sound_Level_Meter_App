package org.apache.activemq.transport.reliable;

import java.io.IOException;

public interface Replayer {
    void sendBuffer(int i, Object obj) throws IOException;
}
