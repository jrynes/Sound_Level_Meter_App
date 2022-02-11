package org.apache.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

public interface MessageTransformer {
    Message consumerTransform(Session session, MessageConsumer messageConsumer, Message message) throws JMSException;

    Message producerTransform(Session session, MessageProducer messageProducer, Message message) throws JMSException;
}
