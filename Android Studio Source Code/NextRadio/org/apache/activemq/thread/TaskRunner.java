package org.apache.activemq.thread;

public interface TaskRunner {
    void shutdown() throws InterruptedException;

    void shutdown(long j) throws InterruptedException;

    void wakeup() throws InterruptedException;
}
