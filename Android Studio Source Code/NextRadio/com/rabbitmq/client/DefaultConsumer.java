package com.rabbitmq.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import java.io.IOException;

public class DefaultConsumer implements Consumer {
    private final Channel _channel;
    private volatile String _consumerTag;

    public DefaultConsumer(Channel channel) {
        this._channel = channel;
    }

    public void handleConsumeOk(String consumerTag) {
        this._consumerTag = consumerTag;
    }

    public void handleCancelOk(String consumerTag) {
    }

    public void handleCancel(String consumerTag) throws IOException {
    }

    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
    }

    public void handleRecoverOk(String consumerTag) {
    }

    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
    }

    public Channel getChannel() {
        return this._channel;
    }

    public String getConsumerTag() {
        return this._consumerTag;
    }
}
