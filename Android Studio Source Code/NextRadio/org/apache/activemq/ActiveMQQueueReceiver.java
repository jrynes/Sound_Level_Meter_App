package org.apache.activemq;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerId;

public class ActiveMQQueueReceiver extends ActiveMQMessageConsumer implements QueueReceiver {
    protected ActiveMQQueueReceiver(ActiveMQSession theSession, ConsumerId consumerId, ActiveMQDestination destination, String selector, int prefetch, int maximumPendingMessageCount, boolean asyncDispatch) throws JMSException {
        super(theSession, consumerId, destination, null, selector, prefetch, maximumPendingMessageCount, false, false, asyncDispatch, null);
    }

    public Queue getQueue() throws JMSException {
        checkClosed();
        return (Queue) super.getDestination();
    }
}
