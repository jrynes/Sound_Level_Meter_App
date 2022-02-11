package org.apache.activemq;

import java.io.Serializable;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.IllegalStateException;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

public class ActiveMQTopicSession implements TopicSession {
    private final TopicSession next;

    public ActiveMQTopicSession(TopicSession next) {
        this.next = next;
    }

    public void close() throws JMSException {
        this.next.close();
    }

    public void commit() throws JMSException {
        this.next.commit();
    }

    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        throw new IllegalStateException("Operation not supported by a TopicSession");
    }

    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
        throw new IllegalStateException("Operation not supported by a TopicSession");
    }

    public BytesMessage createBytesMessage() throws JMSException {
        return this.next.createBytesMessage();
    }

    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        if (!(destination instanceof Queue)) {
            return this.next.createConsumer(destination);
        }
        throw new InvalidDestinationException("Queues are not supported by a TopicSession");
    }

    public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
        if (!(destination instanceof Queue)) {
            return this.next.createConsumer(destination, messageSelector);
        }
        throw new InvalidDestinationException("Queues are not supported by a TopicSession");
    }

    public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) throws JMSException {
        if (!(destination instanceof Queue)) {
            return this.next.createConsumer(destination, messageSelector, noLocal);
        }
        throw new InvalidDestinationException("Queues are not supported by a TopicSession");
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
        return this.next.createDurableSubscriber(topic, name);
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
        return this.next.createDurableSubscriber(topic, name, messageSelector, noLocal);
    }

    public MapMessage createMapMessage() throws JMSException {
        return this.next.createMapMessage();
    }

    public Message createMessage() throws JMSException {
        return this.next.createMessage();
    }

    public ObjectMessage createObjectMessage() throws JMSException {
        return this.next.createObjectMessage();
    }

    public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
        return this.next.createObjectMessage(object);
    }

    public MessageProducer createProducer(Destination destination) throws JMSException {
        if (!(destination instanceof Queue)) {
            return this.next.createProducer(destination);
        }
        throw new InvalidDestinationException("Queues are not supported by a TopicSession");
    }

    public TopicPublisher createPublisher(Topic topic) throws JMSException {
        return this.next.createPublisher(topic);
    }

    public Queue createQueue(String queueName) throws JMSException {
        throw new IllegalStateException("Operation not supported by a TopicSession");
    }

    public StreamMessage createStreamMessage() throws JMSException {
        return this.next.createStreamMessage();
    }

    public TopicSubscriber createSubscriber(Topic topic) throws JMSException {
        return this.next.createSubscriber(topic);
    }

    public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException {
        return this.next.createSubscriber(topic, messageSelector, noLocal);
    }

    public TemporaryQueue createTemporaryQueue() throws JMSException {
        throw new IllegalStateException("Operation not supported by a TopicSession");
    }

    public TemporaryTopic createTemporaryTopic() throws JMSException {
        return this.next.createTemporaryTopic();
    }

    public TextMessage createTextMessage() throws JMSException {
        return this.next.createTextMessage();
    }

    public TextMessage createTextMessage(String text) throws JMSException {
        return this.next.createTextMessage(text);
    }

    public Topic createTopic(String topicName) throws JMSException {
        return this.next.createTopic(topicName);
    }

    public boolean equals(Object arg0) {
        if (this != arg0) {
            return this.next.equals(arg0);
        }
        return true;
    }

    public int getAcknowledgeMode() throws JMSException {
        return this.next.getAcknowledgeMode();
    }

    public MessageListener getMessageListener() throws JMSException {
        return this.next.getMessageListener();
    }

    public boolean getTransacted() throws JMSException {
        return this.next.getTransacted();
    }

    public int hashCode() {
        return this.next.hashCode();
    }

    public void recover() throws JMSException {
        this.next.recover();
    }

    public void rollback() throws JMSException {
        this.next.rollback();
    }

    public void run() {
        this.next.run();
    }

    public void setMessageListener(MessageListener listener) throws JMSException {
        this.next.setMessageListener(listener);
    }

    public String toString() {
        return this.next.toString();
    }

    public void unsubscribe(String name) throws JMSException {
        this.next.unsubscribe(name);
    }

    public TopicSession getNext() {
        return this.next;
    }
}
