package org.apache.activemq;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerId;

public class ActiveMQTopicSubscriber extends ActiveMQMessageConsumer implements TopicSubscriber {
    protected ActiveMQTopicSubscriber(ActiveMQSession theSession, ConsumerId consumerId, ActiveMQDestination dest, String name, String selector, int prefetch, int maximumPendingMessageCount, boolean noLocalValue, boolean browserValue, boolean asyncDispatch) throws JMSException {
        super(theSession, consumerId, dest, name, selector, prefetch, maximumPendingMessageCount, noLocalValue, browserValue, asyncDispatch, null);
    }

    public Topic getTopic() throws JMSException {
        checkClosed();
        return (Topic) super.getDestination();
    }

    public boolean getNoLocal() throws JMSException {
        checkClosed();
        return super.isNoLocal();
    }
}
