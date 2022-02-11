package com.rabbitmq.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import java.io.IOException;

public interface Consumer {
    void handleCancel(String str) throws IOException;

    void handleCancelOk(String str);

    void handleConsumeOk(String str);

    void handleDelivery(String str, Envelope envelope, BasicProperties basicProperties, byte[] bArr) throws IOException;

    void handleRecoverOk(String str);

    void handleShutdownSignal(String str, ShutdownSignalException shutdownSignalException);
}
