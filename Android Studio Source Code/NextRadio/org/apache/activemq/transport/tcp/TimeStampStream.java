package org.apache.activemq.transport.tcp;

public interface TimeStampStream {
    long getWriteTimestamp();

    boolean isWriting();
}
