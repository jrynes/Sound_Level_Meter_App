package org.apache.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import org.apache.activemq.command.ActiveMQDestination;

public class ActiveMQTopicPublisher extends ActiveMQMessageProducer implements TopicPublisher {
    protected ActiveMQTopicPublisher(ActiveMQSession session, ActiveMQDestination destination, int sendTimeout) throws JMSException {
        super(session, session.getNextProducerId(), destination, sendTimeout);
    }

    public Topic getTopic() throws JMSException {
        return (Topic) super.getDestination();
    }

    public void publish(Message message) throws JMSException {
        super.send(message);
    }

    public void publish(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        super.send(message, deliveryMode, priority, timeToLive);
    }

    public void publish(Topic topic, Message message) throws JMSException {
        super.send(topic, message);
    }

    public void publish(Topic topic, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        super.send((Destination) topic, message, deliveryMode, priority, timeToLive);
    }
}
