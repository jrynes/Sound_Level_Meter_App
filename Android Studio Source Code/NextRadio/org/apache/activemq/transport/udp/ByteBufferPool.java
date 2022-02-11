package org.apache.activemq.transport.udp;

import java.nio.ByteBuffer;
import org.apache.activemq.Service;

public interface ByteBufferPool extends Service {
    ByteBuffer borrowBuffer();

    void returnBuffer(ByteBuffer byteBuffer);

    void setDefaultSize(int i);
}
