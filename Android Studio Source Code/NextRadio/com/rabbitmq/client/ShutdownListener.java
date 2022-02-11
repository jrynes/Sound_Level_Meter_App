package com.rabbitmq.client;

import java.util.EventListener;

public interface ShutdownListener extends EventListener {
    void shutdownCompleted(ShutdownSignalException shutdownSignalException);
}
