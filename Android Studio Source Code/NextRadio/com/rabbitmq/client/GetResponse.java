package com.rabbitmq.client;

import com.rabbitmq.client.AMQP.BasicProperties;

public class GetResponse {
    private final byte[] body;
    private final Envelope envelope;
    private final int messageCount;
    private final BasicProperties props;

    public GetResponse(Envelope envelope, BasicProperties props, byte[] body, int messageCount) {
        this.envelope = envelope;
        this.props = props;
        this.body = body;
        this.messageCount = messageCount;
    }

    public Envelope getEnvelope() {
        return this.envelope;
    }

    public BasicProperties getProps() {
        return this.props;
    }

    public byte[] getBody() {
        return this.body;
    }

    public int getMessageCount() {
        return this.messageCount;
    }
}
