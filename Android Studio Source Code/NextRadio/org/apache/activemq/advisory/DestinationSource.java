package org.apache.activemq.advisory;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.DestinationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DestinationSource implements MessageListener {
    private static final Logger LOG;
    private final Connection connection;
    private DestinationListener listener;
    private MessageConsumer queueConsumer;
    private Set<ActiveMQQueue> queues;
    private Session session;
    private AtomicBoolean started;
    private MessageConsumer tempQueueConsumer;
    private MessageConsumer tempTopicConsumer;
    private Set<ActiveMQTempQueue> temporaryQueues;
    private Set<ActiveMQTempTopic> temporaryTopics;
    private MessageConsumer topicConsumer;
    private Set<ActiveMQTopic> topics;

    static {
        LOG = LoggerFactory.getLogger(ConsumerEventSource.class);
    }

    public DestinationSource(Connection connection) throws JMSException {
        this.started = new AtomicBoolean(false);
        this.queues = new CopyOnWriteArraySet();
        this.topics = new CopyOnWriteArraySet();
        this.temporaryQueues = new CopyOnWriteArraySet();
        this.temporaryTopics = new CopyOnWriteArraySet();
        this.connection = connection;
    }

    public DestinationListener getListener() {
        return this.listener;
    }

    public void setDestinationListener(DestinationListener listener) {
        this.listener = listener;
    }

    public Set<ActiveMQQueue> getQueues() {
        return this.queues;
    }

    public Set<ActiveMQTopic> getTopics() {
        return this.topics;
    }

    public Set<ActiveMQTempQueue> getTemporaryQueues() {
        return this.temporaryQueues;
    }

    public Set<ActiveMQTempTopic> getTemporaryTopics() {
        return this.temporaryTopics;
    }

    public void start() throws JMSException {
        if (this.started.compareAndSet(false, true)) {
            this.session = this.connection.createSession(false, 1);
            this.queueConsumer = this.session.createConsumer(AdvisorySupport.QUEUE_ADVISORY_TOPIC);
            this.queueConsumer.setMessageListener(this);
            this.topicConsumer = this.session.createConsumer(AdvisorySupport.TOPIC_ADVISORY_TOPIC);
            this.topicConsumer.setMessageListener(this);
            this.tempQueueConsumer = this.session.createConsumer(AdvisorySupport.TEMP_QUEUE_ADVISORY_TOPIC);
            this.tempQueueConsumer.setMessageListener(this);
            this.tempTopicConsumer = this.session.createConsumer(AdvisorySupport.TEMP_TOPIC_ADVISORY_TOPIC);
            this.tempTopicConsumer.setMessageListener(this);
        }
    }

    public void stop() throws JMSException {
        if (this.started.compareAndSet(true, false) && this.session != null) {
            this.session.close();
        }
    }

    public void onMessage(Message message) {
        if (message instanceof ActiveMQMessage) {
            DataStructure command = ((ActiveMQMessage) message).getDataStructure();
            if (command instanceof DestinationInfo) {
                fireDestinationEvent(new DestinationEvent(this, (DestinationInfo) command));
                return;
            } else {
                LOG.warn("Unknown dataStructure: " + command);
                return;
            }
        }
        LOG.warn("Unknown message type: " + message + ". Message ignored");
    }

    protected void fireDestinationEvent(DestinationEvent event) {
        ActiveMQDestination destination = event.getDestination();
        boolean add = event.isAddOperation();
        if (destination instanceof ActiveMQQueue) {
            ActiveMQQueue queue = (ActiveMQQueue) destination;
            if (add) {
                this.queues.add(queue);
            } else {
                this.queues.remove(queue);
            }
        } else if (destination instanceof ActiveMQTopic) {
            ActiveMQTopic topic = (ActiveMQTopic) destination;
            if (add) {
                this.topics.add(topic);
            } else {
                this.topics.remove(topic);
            }
        } else if (destination instanceof ActiveMQTempQueue) {
            ActiveMQTempQueue queue2 = (ActiveMQTempQueue) destination;
            if (add) {
                this.temporaryQueues.add(queue2);
            } else {
                this.temporaryQueues.remove(queue2);
            }
        } else if (destination instanceof ActiveMQTempTopic) {
            ActiveMQTempTopic topic2 = (ActiveMQTempTopic) destination;
            if (add) {
                this.temporaryTopics.add(topic2);
            } else {
                this.temporaryTopics.remove(topic2);
            }
        } else {
            LOG.warn("Unknown destination type: " + destination);
        }
        if (this.listener != null) {
            this.listener.onDestinationEvent(event);
        }
    }
}
