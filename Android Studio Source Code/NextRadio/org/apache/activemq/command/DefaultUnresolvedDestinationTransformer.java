package org.apache.activemq.command;

import java.lang.reflect.Method;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

public class DefaultUnresolvedDestinationTransformer implements UnresolvedDestinationTransformer {
    public ActiveMQDestination transform(Destination dest) throws JMSException {
        String queueName = ((Queue) dest).getQueueName();
        String topicName = ((Topic) dest).getTopicName();
        if (queueName == null && topicName == null) {
            throw new JMSException("Unresolvable destination: Both queue and topic names are null: " + dest);
        }
        try {
            Method isQueueMethod = dest.getClass().getMethod("isQueue", new Class[0]);
            Boolean isTopic = (Boolean) dest.getClass().getMethod("isTopic", new Class[0]).invoke(dest, new Object[0]);
            if (((Boolean) isQueueMethod.invoke(dest, new Object[0])).booleanValue()) {
                return new ActiveMQQueue(queueName);
            }
            if (isTopic.booleanValue()) {
                return new ActiveMQTopic(topicName);
            }
            throw new JMSException("Unresolvable destination: Neither Queue nor Topic: " + dest);
        } catch (Exception e) {
            throw new JMSException("Unresolvable destination: " + e.getMessage() + ": " + dest);
        }
    }

    public ActiveMQDestination transform(String dest) throws JMSException {
        return new ActiveMQQueue(dest);
    }
}
