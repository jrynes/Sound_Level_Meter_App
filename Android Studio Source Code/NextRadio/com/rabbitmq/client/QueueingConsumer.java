package com.rabbitmq.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.utility.Utility;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueingConsumer extends DefaultConsumer {
    private static final Delivery POISON;
    private volatile ConsumerCancelledException _cancelled;
    private final BlockingQueue<Delivery> _queue;
    private volatile ShutdownSignalException _shutdown;

    public static class Delivery {
        private final byte[] _body;
        private final Envelope _envelope;
        private final BasicProperties _properties;

        public Delivery(Envelope envelope, BasicProperties properties, byte[] body) {
            this._envelope = envelope;
            this._properties = properties;
            this._body = body;
        }

        public Envelope getEnvelope() {
            return this._envelope;
        }

        public BasicProperties getProperties() {
            return this._properties;
        }

        public byte[] getBody() {
            return this._body;
        }
    }

    static {
        POISON = new Delivery(null, null, null);
    }

    public QueueingConsumer(Channel ch) {
        this(ch, new LinkedBlockingQueue());
    }

    public QueueingConsumer(Channel ch, BlockingQueue<Delivery> q) {
        super(ch);
        this._queue = q;
    }

    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        this._shutdown = sig;
        this._queue.add(POISON);
    }

    public void handleCancel(String consumerTag) throws IOException {
        this._cancelled = new ConsumerCancelledException();
        this._queue.add(POISON);
    }

    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
        checkShutdown();
        this._queue.add(new Delivery(envelope, properties, body));
    }

    private void checkShutdown() {
        if (this._shutdown != null) {
            throw ((ShutdownSignalException) Utility.fixStackTrace(this._shutdown));
        }
    }

    private Delivery handle(Delivery delivery) {
        if (delivery == POISON || (delivery == null && !(this._shutdown == null && this._cancelled == null))) {
            if (delivery == POISON) {
                this._queue.add(POISON);
                if (this._shutdown == null && this._cancelled == null) {
                    throw new IllegalStateException("POISON in queue, but null _shutdown and null _cancelled. This should never happen, please report as a BUG");
                }
            }
            if (this._shutdown != null) {
                throw ((ShutdownSignalException) Utility.fixStackTrace(this._shutdown));
            } else if (this._cancelled != null) {
                throw ((ConsumerCancelledException) Utility.fixStackTrace(this._cancelled));
            }
        }
        return delivery;
    }

    public Delivery nextDelivery() throws InterruptedException, ShutdownSignalException, ConsumerCancelledException {
        return handle((Delivery) this._queue.take());
    }

    public Delivery nextDelivery(long timeout) throws InterruptedException, ShutdownSignalException, ConsumerCancelledException {
        return handle((Delivery) this._queue.poll(timeout, TimeUnit.MILLISECONDS));
    }
}
