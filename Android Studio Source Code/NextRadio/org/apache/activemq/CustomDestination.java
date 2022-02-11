package org.apache.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.TopicPublisher;
import javax.jms.TopicSubscriber;

public interface CustomDestination extends Destination {
    MessageConsumer createConsumer(ActiveMQSession activeMQSession, String str);

    MessageConsumer createConsumer(ActiveMQSession activeMQSession, String str, boolean z);

    TopicSubscriber createDurableSubscriber(ActiveMQSession activeMQSession, String str, String str2, boolean z);

    MessageProducer createProducer(ActiveMQSession activeMQSession) throws JMSException;

    TopicPublisher createPublisher(ActiveMQSession activeMQSession) throws JMSException;

    QueueReceiver createReceiver(ActiveMQSession activeMQSession, String str);

    QueueSender createSender(ActiveMQSession activeMQSession) throws JMSException;

    TopicSubscriber createSubscriber(ActiveMQSession activeMQSession, String str, boolean z);
}
