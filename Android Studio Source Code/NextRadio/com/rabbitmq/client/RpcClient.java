package com.rabbitmq.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.impl.MethodArgumentReader;
import com.rabbitmq.client.impl.MethodArgumentWriter;
import com.rabbitmq.client.impl.ValueReader;
import com.rabbitmq.client.impl.ValueWriter;
import com.rabbitmq.utility.BlockingCell;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;
import org.apache.activemq.transport.stomp.Stomp;

public class RpcClient {
    protected static final int NO_TIMEOUT = -1;
    private final Channel _channel;
    private DefaultConsumer _consumer;
    private final Map<String, BlockingCell<Object>> _continuationMap;
    private int _correlationId;
    private final String _exchange;
    private String _replyQueue;
    private final String _routingKey;
    private final int _timeout;

    /* renamed from: com.rabbitmq.client.RpcClient.1 */
    class C13141 extends DefaultConsumer {
        C13141(Channel x0) {
            super(x0);
        }

        public void handleShutdownSignal(String consumerTag, ShutdownSignalException signal) {
            synchronized (RpcClient.this._continuationMap) {
                for (Entry<String, BlockingCell<Object>> entry : RpcClient.this._continuationMap.entrySet()) {
                    ((BlockingCell) entry.getValue()).set(signal);
                }
                RpcClient.this._consumer = null;
            }
        }

        public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
            synchronized (RpcClient.this._continuationMap) {
                String replyId = properties.getCorrelationId();
                BlockingCell<Object> blocker = (BlockingCell) RpcClient.this._continuationMap.get(replyId);
                RpcClient.this._continuationMap.remove(replyId);
                blocker.set(body);
            }
        }
    }

    public RpcClient(Channel channel, String exchange, String routingKey, int timeout) throws IOException {
        this._continuationMap = new HashMap();
        this._channel = channel;
        this._exchange = exchange;
        this._routingKey = routingKey;
        if (timeout < NO_TIMEOUT) {
            throw new IllegalArgumentException("Timeout arguument must be NO_TIMEOUT(-1) or non-negative.");
        }
        this._timeout = timeout;
        this._correlationId = 0;
        this._replyQueue = setupReplyQueue();
        this._consumer = setupConsumer();
    }

    public RpcClient(Channel channel, String exchange, String routingKey) throws IOException {
        this(channel, exchange, routingKey, NO_TIMEOUT);
    }

    public void checkConsumer() throws IOException {
        if (this._consumer == null) {
            throw new EOFException("RpcClient is closed");
        }
    }

    public void close() throws IOException {
        if (this._consumer != null) {
            this._channel.basicCancel(this._consumer.getConsumerTag());
            this._consumer = null;
        }
    }

    protected String setupReplyQueue() throws IOException {
        return this._channel.queueDeclare(Stomp.EMPTY, false, true, true, null).getQueue();
    }

    protected DefaultConsumer setupConsumer() throws IOException {
        DefaultConsumer consumer = new C13141(this._channel);
        this._channel.basicConsume(this._replyQueue, true, consumer);
        return consumer;
    }

    public void publish(BasicProperties props, byte[] message) throws IOException {
        this._channel.basicPublish(this._exchange, this._routingKey, props, message);
    }

    public byte[] primitiveCall(BasicProperties props, byte[] message) throws IOException, ShutdownSignalException, TimeoutException {
        checkConsumer();
        BlockingCell<Object> k = new BlockingCell();
        synchronized (this._continuationMap) {
            this._correlationId++;
            String replyId = Stomp.EMPTY + this._correlationId;
            props = (props == null ? new Builder() : props.builder()).correlationId(replyId).replyTo(this._replyQueue).build();
            this._continuationMap.put(replyId, k);
        }
        publish(props, message);
        ShutdownSignalException reply = k.uninterruptibleGet(this._timeout);
        if (!(reply instanceof ShutdownSignalException)) {
            return (byte[]) reply;
        }
        ShutdownSignalException sig = reply;
        ShutdownSignalException wrapper = new ShutdownSignalException(sig.isHardError(), sig.isInitiatedByApplication(), sig.getReason(), sig.getReference());
        wrapper.initCause(sig);
        throw wrapper;
    }

    public byte[] primitiveCall(byte[] message) throws IOException, ShutdownSignalException, TimeoutException {
        return primitiveCall(null, message);
    }

    public String stringCall(String message) throws IOException, ShutdownSignalException, TimeoutException {
        byte[] request;
        try {
            request = message.getBytes(StringRpcServer.STRING_ENCODING);
        } catch (IOException e) {
            request = message.getBytes();
        }
        byte[] reply = primitiveCall(request);
        try {
            return new String(reply, StringRpcServer.STRING_ENCODING);
        } catch (IOException e2) {
            return new String(reply);
        }
    }

    public Map<String, Object> mapCall(Map<String, Object> message) throws IOException, ShutdownSignalException, TimeoutException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        MethodArgumentWriter writer = new MethodArgumentWriter(new ValueWriter(new DataOutputStream(buffer)));
        writer.writeTable(message);
        writer.flush();
        return new MethodArgumentReader(new ValueReader(new DataInputStream(new ByteArrayInputStream(primitiveCall(buffer.toByteArray()))))).readTable();
    }

    public Map<String, Object> mapCall(Object[] keyValuePairs) throws IOException, ShutdownSignalException, TimeoutException {
        Map message = new HashMap();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            message.put((String) keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return mapCall(message);
    }

    public Channel getChannel() {
        return this._channel;
    }

    public String getExchange() {
        return this._exchange;
    }

    public String getRoutingKey() {
        return this._routingKey;
    }

    public Map<String, BlockingCell<Object>> getContinuationMap() {
        return this._continuationMap;
    }

    public int getCorrelationId() {
        return this._correlationId;
    }

    public String getReplyQueue() {
        return this._replyQueue;
    }

    public Consumer getConsumer() {
        return this._consumer;
    }
}
