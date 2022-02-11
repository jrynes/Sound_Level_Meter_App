package com.rabbitmq.client;

import java.io.IOException;

public interface ConfirmListener {
    void handleAck(long j, boolean z) throws IOException;

    void handleNack(long j, boolean z) throws IOException;
}
