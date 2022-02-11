package org.apache.activemq;

import javax.jms.Destination;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

public abstract class ActiveMQMessageProducerSupport implements MessageProducer, Closeable {
    protected int defaultDeliveryMode;
    protected int defaultPriority;
    protected long defaultTimeToLive;
    protected boolean disableMessageID;
    protected boolean disableMessageTimestamp;
    protected int sendTimeout;
    protected ActiveMQSession session;

    protected abstract void checkClosed() throws IllegalStateException;

    public ActiveMQMessageProducerSupport(ActiveMQSession session) {
        this.sendTimeout = 0;
        this.session = session;
        this.disableMessageTimestamp = session.connection.isDisableTimeStampsByDefault();
    }

    public void setDisableMessageID(boolean value) throws JMSException {
        checkClosed();
        this.disableMessageID = value;
    }

    public boolean getDisableMessageID() throws JMSException {
        checkClosed();
        return this.disableMessageID;
    }

    public void setDisableMessageTimestamp(boolean value) throws JMSException {
        checkClosed();
        this.disableMessageTimestamp = value;
    }

    public boolean getDisableMessageTimestamp() throws JMSException {
        checkClosed();
        return this.disableMessageTimestamp;
    }

    public void setDeliveryMode(int newDeliveryMode) throws JMSException {
        if (newDeliveryMode == 2 || newDeliveryMode == 1) {
            checkClosed();
            this.defaultDeliveryMode = newDeliveryMode;
            return;
        }
        throw new IllegalStateException("unknown delivery mode: " + newDeliveryMode);
    }

    public int getDeliveryMode() throws JMSException {
        checkClosed();
        return this.defaultDeliveryMode;
    }

    public void setPriority(int newDefaultPriority) throws JMSException {
        if (newDefaultPriority < 0 || newDefaultPriority > 9) {
            throw new IllegalStateException("default priority must be a value between 0 and 9");
        }
        checkClosed();
        this.defaultPriority = newDefaultPriority;
    }

    public int getPriority() throws JMSException {
        checkClosed();
        return this.defaultPriority;
    }

    public void setTimeToLive(long timeToLive) throws JMSException {
        if (timeToLive < 0) {
            throw new IllegalStateException("cannot set a negative timeToLive");
        }
        checkClosed();
        this.defaultTimeToLive = timeToLive;
    }

    public long getTimeToLive() throws JMSException {
        checkClosed();
        return this.defaultTimeToLive;
    }

    public void send(Message message) throws JMSException {
        send(getDestination(), message, this.defaultDeliveryMode, this.defaultPriority, this.defaultTimeToLive);
    }

    public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        send(getDestination(), message, deliveryMode, priority, timeToLive);
    }

    public void send(Destination destination, Message message) throws JMSException {
        send(destination, message, this.defaultDeliveryMode, this.defaultPriority, this.defaultTimeToLive);
    }

    public int getSendTimeout() {
        return this.sendTimeout;
    }

    public void setSendTimeout(int sendTimeout) {
        this.sendTimeout = sendTimeout;
    }
}
