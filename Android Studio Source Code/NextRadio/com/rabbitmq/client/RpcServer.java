package com.rabbitmq.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import java.io.IOException;
import org.apache.activemq.transport.stomp.Stomp;

public class RpcServer {
    private final Channel _channel;
    private QueueingConsumer _consumer;
    private boolean _mainloopRunning;
    private final String _queueName;

    public RpcServer(Channel channel) throws IOException {
        this(channel, null);
    }

    public RpcServer(Channel channel, String queueName) throws IOException {
        this._mainloopRunning = true;
        this._channel = channel;
        if (queueName == null || queueName.equals(Stomp.EMPTY)) {
            this._queueName = this._channel.queueDeclare().getQueue();
        } else {
            this._queueName = queueName;
        }
        this._consumer = setupConsumer();
    }

    public void close() throws IOException {
        if (this._consumer != null) {
            this._channel.basicCancel(this._consumer.getConsumerTag());
            this._consumer = null;
        }
        terminateMainloop();
    }

    protected QueueingConsumer setupConsumer() throws IOException {
        QueueingConsumer consumer = new QueueingConsumer(this._channel);
        this._channel.basicConsume(this._queueName, consumer);
        return consumer;
    }

    public ShutdownSignalException mainloop() throws IOException {
        while (this._mainloopRunning) {
            try {
                try {
                    Delivery request = this._consumer.nextDelivery();
                    processRequest(request);
                    this._channel.basicAck(request.getEnvelope().getDeliveryTag(), false);
                } catch (InterruptedException e) {
                }
            } catch (ShutdownSignalException e2) {
                return e2;
            }
        }
        return null;
    }

    public void terminateMainloop() {
        this._mainloopRunning = false;
    }

    public void processRequest(Delivery request) throws IOException {
        BasicProperties requestProperties = request.getProperties();
        String correlationId = requestProperties.getCorrelationId();
        String replyTo = requestProperties.getReplyTo();
        if (correlationId == null || replyTo == null) {
            handleCast(request);
            return;
        }
        BasicProperties replyProperties = new Builder().correlationId(correlationId).build();
        this._channel.basicPublish(Stomp.EMPTY, replyTo, replyProperties, handleCall(request, replyProperties));
    }

    public byte[] handleCall(Delivery request, BasicProperties replyProperties) {
        return handleCall(request.getProperties(), request.getBody(), replyProperties);
    }

    public byte[] handleCall(BasicProperties requestProperties, byte[] requestBody, BasicProperties replyProperties) {
        return handleCall(requestBody, replyProperties);
    }

    public byte[] handleCall(byte[] requestBody, BasicProperties replyProperties) {
        return new byte[0];
    }

    public void handleCast(Delivery request) {
        handleCast(request.getProperties(), request.getBody());
    }

    public void handleCast(BasicProperties requestProperties, byte[] requestBody) {
        handleCast(requestBody);
    }

    public void handleCast(byte[] requestBody) {
    }

    public Channel getChannel() {
        return this._channel;
    }

    public String getQueueName() {
        return this._queueName;
    }
}
