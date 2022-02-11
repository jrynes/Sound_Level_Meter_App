package org.apache.activemq.transport.reliable;

import java.io.IOException;

public interface ReplayBuffer {
    void addBuffer(int i, Object obj);

    void replayMessages(int i, int i2, Replayer replayer) throws IOException;

    void setReplayBufferListener(ReplayBufferListener replayBufferListener);
}
