package org.apache.activemq.transport.reliable;

public interface ReplayBufferListener {
    void onBufferDiscarded(int i, Object obj);
}
