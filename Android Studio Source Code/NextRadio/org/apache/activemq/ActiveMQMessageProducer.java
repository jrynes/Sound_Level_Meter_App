package org.apache.activemq;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.jms.Destination;
import javax.jms.IllegalStateException;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ProducerAck;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.ProducerInfo;
import org.apache.activemq.management.JMSProducerStatsImpl;
import org.apache.activemq.management.StatsCapable;
import org.apache.activemq.management.StatsImpl;
import org.apache.activemq.usage.MemoryUsage;
import org.apache.activemq.util.IntrospectionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQMessageProducer extends ActiveMQMessageProducerSupport implements StatsCapable, Disposable {
    private static final Logger LOG;
    protected boolean closed;
    protected ProducerInfo info;
    private AtomicLong messageSequence;
    private MemoryUsage producerWindow;
    private final long startTime;
    private final JMSProducerStatsImpl stats;
    private MessageTransformer transformer;

    static {
        LOG = LoggerFactory.getLogger(ActiveMQMessageProducer.class);
    }

    protected ActiveMQMessageProducer(ActiveMQSession session, ProducerId producerId, ActiveMQDestination destination, int sendTimeout) throws JMSException {
        super(session);
        this.info = new ProducerInfo(producerId);
        this.info.setWindowSize(session.connection.getProducerWindowSize());
        if (!(destination == null || destination.getOptions() == null)) {
            Map<String, Object> options = IntrospectionSupport.extractProperties(new HashMap(destination.getOptions()), "producer.");
            IntrospectionSupport.setProperties(this.info, options);
            if (options.size() > 0) {
                String msg = "There are " + options.size() + " producer options that couldn't be set on the producer." + " Check the options are spelled correctly." + " Unknown parameters=[" + options + "]." + " This producer cannot be started.";
                LOG.warn(msg);
                throw new ConfigurationException(msg);
            }
        }
        this.info.setDestination(destination);
        if (session.connection.getProtocolVersion() >= 3 && this.info.getWindowSize() > 0) {
            this.producerWindow = new MemoryUsage("Producer Window: " + producerId);
            this.producerWindow.setExecutor(session.getConnectionExecutor());
            this.producerWindow.setLimit((long) this.info.getWindowSize());
            this.producerWindow.start();
        }
        this.defaultDeliveryMode = 2;
        this.defaultPriority = 4;
        this.defaultTimeToLive = 0;
        this.startTime = System.currentTimeMillis();
        this.messageSequence = new AtomicLong(0);
        this.stats = new JMSProducerStatsImpl(session.getSessionStats(), destination);
        try {
            this.session.addProducer(this);
            this.session.syncSendPacket(this.info);
            setSendTimeout(sendTimeout);
            setTransformer(session.getTransformer());
        } catch (JMSException e) {
            this.session.removeProducer(this);
            throw e;
        }
    }

    public StatsImpl getStats() {
        return this.stats;
    }

    public JMSProducerStatsImpl getProducerStats() {
        return this.stats;
    }

    public Destination getDestination() throws JMSException {
        checkClosed();
        return this.info.getDestination();
    }

    public void close() throws JMSException {
        if (!this.closed) {
            dispose();
            this.session.asyncSendPacket(this.info.createRemoveCommand());
        }
    }

    public void dispose() {
        if (!this.closed) {
            this.session.removeProducer(this);
            if (this.producerWindow != null) {
                this.producerWindow.stop();
            }
            this.closed = true;
        }
    }

    protected void checkClosed() throws IllegalStateException {
        if (this.closed) {
            throw new IllegalStateException("The producer is closed");
        }
    }

    public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        send(destination, message, deliveryMode, priority, timeToLive, null);
    }

    public void send(Message message, AsyncCallback onComplete) throws JMSException {
        send(getDestination(), message, this.defaultDeliveryMode, this.defaultPriority, this.defaultTimeToLive, onComplete);
    }

    public void send(Destination destination, Message message, AsyncCallback onComplete) throws JMSException {
        send(destination, message, this.defaultDeliveryMode, this.defaultPriority, this.defaultTimeToLive, onComplete);
    }

    public void send(Message message, int deliveryMode, int priority, long timeToLive, AsyncCallback onComplete) throws JMSException {
        send(getDestination(), message, deliveryMode, priority, timeToLive, onComplete);
    }

    public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive, AsyncCallback onComplete) throws JMSException {
        checkClosed();
        if (destination != null) {
            ActiveMQDestination dest;
            if (destination.equals(this.info.getDestination())) {
                dest = (ActiveMQDestination) destination;
            } else if (this.info.getDestination() == null) {
                dest = ActiveMQDestination.transform(destination);
            } else {
                throw new UnsupportedOperationException("This producer can only send messages to: " + this.info.getDestination().getPhysicalName());
            }
            if (dest == null) {
                throw new JMSException("No destination specified");
            }
            if (this.transformer != null) {
                Message transformedMessage = this.transformer.producerTransform(this.session, this, message);
                if (transformedMessage != null) {
                    message = transformedMessage;
                }
            }
            if (this.producerWindow != null) {
                try {
                    this.producerWindow.waitForSpace();
                } catch (InterruptedException e) {
                    throw new JMSException("Send aborted due to thread interrupt.");
                }
            }
            this.session.send(this, dest, message, deliveryMode, priority, timeToLive, this.producerWindow, this.sendTimeout, onComplete);
            this.stats.onMessage();
        } else if (this.info.getDestination() == null) {
            throw new UnsupportedOperationException("A destination must be specified.");
        } else {
            throw new InvalidDestinationException("Don't understand null destinations");
        }
    }

    public MessageTransformer getTransformer() {
        return this.transformer;
    }

    public void setTransformer(MessageTransformer transformer) {
        this.transformer = transformer;
    }

    protected long getStartTime() {
        return this.startTime;
    }

    protected long getMessageSequence() {
        return this.messageSequence.incrementAndGet();
    }

    protected void setMessageSequence(AtomicLong messageSequence) {
        this.messageSequence = messageSequence;
    }

    protected ProducerInfo getProducerInfo() {
        return this.info != null ? this.info : null;
    }

    protected void setProducerInfo(ProducerInfo info) {
        this.info = info;
    }

    public String toString() {
        return "ActiveMQMessageProducer { value=" + this.info.getProducerId() + " }";
    }

    public void onProducerAck(ProducerAck pa) {
        if (this.producerWindow != null) {
            this.producerWindow.decreaseUsage((long) pa.getSize());
        }
    }
}
