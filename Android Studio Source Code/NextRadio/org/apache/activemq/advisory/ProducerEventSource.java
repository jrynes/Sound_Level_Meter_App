package org.apache.activemq.advisory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import org.apache.activemq.Service;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.ProducerInfo;
import org.apache.activemq.command.RemoveInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerEventSource implements Service, MessageListener {
    private static final Logger LOG;
    private final Connection connection;
    private MessageConsumer consumer;
    private final ActiveMQDestination destination;
    private ProducerListener listener;
    private AtomicInteger producerCount;
    private Session session;
    private AtomicBoolean started;

    static {
        LOG = LoggerFactory.getLogger(ProducerEventSource.class);
    }

    public ProducerEventSource(Connection connection, Destination destination) throws JMSException {
        this.started = new AtomicBoolean(false);
        this.producerCount = new AtomicInteger();
        this.connection = connection;
        this.destination = ActiveMQDestination.transform(destination);
    }

    public void setProducerListener(ProducerListener listener) {
        this.listener = listener;
    }

    public void start() throws Exception {
        if (this.started.compareAndSet(false, true)) {
            this.session = this.connection.createSession(false, 1);
            this.consumer = this.session.createConsumer(AdvisorySupport.getProducerAdvisoryTopic(this.destination));
            this.consumer.setMessageListener(this);
        }
    }

    public void stop() throws Exception {
        if (this.started.compareAndSet(true, false) && this.session != null) {
            this.session.close();
        }
    }

    public void onMessage(Message message) {
        if (message instanceof ActiveMQMessage) {
            DataStructure command = ((ActiveMQMessage) message).getDataStructure();
            if (command instanceof ProducerInfo) {
                fireProducerEvent(new ProducerStartedEvent(this, this.destination, (ProducerInfo) command, extractProducerCountFromMessage(message, this.producerCount.incrementAndGet())));
                return;
            } else if (command instanceof RemoveInfo) {
                RemoveInfo removeInfo = (RemoveInfo) command;
                if (removeInfo.isProducerRemove()) {
                    fireProducerEvent(new ProducerStoppedEvent(this, this.destination, (ProducerId) removeInfo.getObjectId(), extractProducerCountFromMessage(message, this.producerCount.decrementAndGet())));
                    return;
                }
                return;
            } else {
                LOG.warn("Unknown command: " + command);
                return;
            }
        }
        LOG.warn("Unknown message type: " + message + ". Message ignored");
    }

    protected int extractProducerCountFromMessage(Message message, int count) {
        try {
            Object value = message.getObjectProperty("producerCount");
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            LOG.warn("No producerCount header available on the message: " + message);
            return count;
        } catch (Exception e) {
            LOG.warn("Failed to extract producerCount from message: " + message + ".Reason: " + e, e);
            return count;
        }
    }

    protected void fireProducerEvent(ProducerEvent event) {
        if (this.listener != null) {
            this.listener.onProducerEvent(event);
        }
    }
}
