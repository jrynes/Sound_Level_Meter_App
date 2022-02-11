package org.apache.activemq;

import java.util.List;
import org.apache.activemq.command.MessageDispatch;

public interface MessageDispatchChannel {
    void clear();

    void close();

    MessageDispatch dequeue(long j) throws InterruptedException;

    MessageDispatch dequeueNoWait();

    void enqueue(MessageDispatch messageDispatch);

    void enqueueFirst(MessageDispatch messageDispatch);

    Object getMutex();

    boolean isClosed();

    boolean isEmpty();

    boolean isRunning();

    MessageDispatch peek();

    List<MessageDispatch> removeAll();

    int size();

    void start();

    void stop();
}
