package org.apache.activemq.advisory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.Service;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.RemoveInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerEventSource implements Service, MessageListener {
    private static final Logger LOG;
    private final Connection connection;
    private ActiveMQMessageConsumer consumer;
    private AtomicInteger consumerCount;
    private final ActiveMQDestination destination;
    private ConsumerListener listener;
    private Session session;
    private AtomicBoolean started;

    static {
        LOG = LoggerFactory.getLogger(ConsumerEventSource.class);
    }

    public ConsumerEventSource(Connection connection, Destination destination) throws JMSException {
        this.started = new AtomicBoolean(false);
        this.consumerCount = new AtomicInteger();
        this.connection = connection;
        this.destination = ActiveMQDestination.transform(destination);
    }

    public void setConsumerListener(ConsumerListener listener) {
        this.listener = listener;
    }

    public String getConsumerId() {
        return this.consumer != null ? this.consumer.getConsumerId().toString() : "NOT_SET";
    }

    public void start() throws Exception {
        if (this.started.compareAndSet(false, true)) {
            this.session = this.connection.createSession(false, 1);
            this.consumer = (ActiveMQMessageConsumer) this.session.createConsumer(AdvisorySupport.getConsumerAdvisoryTopic(this.destination));
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
            if (command instanceof ConsumerInfo) {
                fireConsumerEvent(new ConsumerStartedEvent(this, this.destination, (ConsumerInfo) command, extractConsumerCountFromMessage(message, this.consumerCount.incrementAndGet())));
                return;
            } else if (command instanceof RemoveInfo) {
                RemoveInfo removeInfo = (RemoveInfo) command;
                if (removeInfo.isConsumerRemove()) {
                    fireConsumerEvent(new ConsumerStoppedEvent(this, this.destination, (ConsumerId) removeInfo.getObjectId(), extractConsumerCountFromMessage(message, this.consumerCount.decrementAndGet())));
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

    protected int extractConsumerCountFromMessage(Message message, int count) {
        try {
            Object value = message.getObjectProperty(AdvisorySupport.MSG_PROPERTY_CONSUMER_COUNT);
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            LOG.warn("No consumerCount header available on the message: " + message);
            return count;
        } catch (Exception e) {
            LOG.warn("Failed to extract consumerCount from message: " + message + ".Reason: " + e, e);
            return count;
        }
    }

    protected void fireConsumerEvent(ConsumerEvent event) {
        if (this.listener != null) {
            this.listener.onConsumerEvent(event);
        }
    }
}
