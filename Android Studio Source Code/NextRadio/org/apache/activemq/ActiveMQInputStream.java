package org.apache.activemq;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.jms.IllegalStateException;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.selector.SelectorParser;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.JMSExceptionSupport;
import org.xbill.DNS.Type;

public class ActiveMQInputStream extends InputStream implements ActiveMQDispatcher {
    private byte[] buffer;
    private final ActiveMQConnection connection;
    private int deliveredCounter;
    private boolean eosReached;
    private boolean firstReceived;
    private final ConsumerInfo info;
    private Map<String, Object> jmsProperties;
    private MessageDispatch lastDelivered;
    private long nextSequenceId;
    private int pos;
    private ProducerId producerId;
    private final long timeout;
    private final MessageDispatchChannel unconsumedMessages;

    public class ReadTimeoutException extends IOException {
        private static final long serialVersionUID = -3217758894326719909L;
    }

    public ActiveMQInputStream(ActiveMQConnection connection, ConsumerId consumerId, ActiveMQDestination dest, String selector, boolean noLocal, String name, int prefetch, long timeout) throws JMSException {
        this.unconsumedMessages = new FifoMessageDispatchChannel();
        this.connection = connection;
        if (dest == null) {
            throw new InvalidDestinationException("Don't understand null destinations");
        }
        if (dest.isTemporary()) {
            String physicalName = dest.getPhysicalName();
            if (physicalName == null) {
                throw new IllegalArgumentException("Physical name of Destination should be valid: " + dest);
            } else if (physicalName.indexOf(connection.getConnectionInfo().getConnectionId().getValue()) < 0) {
                throw new InvalidDestinationException("Cannot use a Temporary destination from another Connection");
            } else if (connection.isDeleted(dest)) {
                throw new InvalidDestinationException("Cannot use a Temporary destination that has been deleted");
            }
        }
        if (timeout < -1) {
            throw new IllegalArgumentException("Timeout must be >= -1");
        }
        this.timeout = timeout;
        this.info = new ConsumerInfo(consumerId);
        this.info.setSubscriptionName(name);
        if (selector == null || selector.trim().length() == 0) {
            selector = "JMSType='org.apache.activemq.Stream'";
        } else {
            selector = "JMSType='org.apache.activemq.Stream' AND ( " + selector + " ) ";
        }
        SelectorParser.parse(selector);
        this.info.setSelector(selector);
        this.info.setPrefetchSize(prefetch);
        this.info.setNoLocal(noLocal);
        this.info.setBrowser(false);
        this.info.setDispatchAsync(false);
        if (dest.getOptions() != null) {
            IntrospectionSupport.setProperties(this.info, new HashMap(dest.getOptions()), "consumer.");
        }
        this.info.setDestination(dest);
        this.connection.addInputStream(this);
        this.connection.addDispatcher(this.info.getConsumerId(), this);
        this.connection.syncSendPacket(this.info);
        this.unconsumedMessages.start();
    }

    public void close() throws IOException {
        if (!this.unconsumedMessages.isClosed()) {
            try {
                if (this.lastDelivered != null) {
                    this.connection.asyncSendPacket(new MessageAck(this.lastDelivered, (byte) 2, this.deliveredCounter));
                }
                dispose();
                this.connection.syncSendPacket(this.info.createRemoveCommand());
            } catch (Exception e) {
                throw IOExceptionSupport.create(e);
            }
        }
    }

    public void dispose() {
        if (!this.unconsumedMessages.isClosed()) {
            this.unconsumedMessages.close();
            this.connection.removeDispatcher(this.info.getConsumerId());
            this.connection.removeInputStream(this);
        }
    }

    public Map<String, Object> getJMSProperties() throws IOException {
        if (this.jmsProperties == null) {
            fillBuffer();
        }
        return this.jmsProperties;
    }

    public ActiveMQMessage receive() throws JMSException, ReadTimeoutException {
        checkClosed();
        try {
            MessageDispatch md;
            if (this.firstReceived || this.timeout == -1) {
                md = this.unconsumedMessages.dequeue(-1);
                this.firstReceived = true;
            } else {
                md = this.unconsumedMessages.dequeue(this.timeout);
                if (md == null) {
                    throw new ReadTimeoutException();
                }
            }
            if (md == null || this.unconsumedMessages.isClosed() || md.getMessage().isExpired()) {
                return null;
            }
            this.deliveredCounter++;
            if (0.75d * ((double) this.info.getPrefetchSize()) <= ((double) this.deliveredCounter)) {
                this.connection.asyncSendPacket(new MessageAck(md, (byte) 2, this.deliveredCounter));
                this.deliveredCounter = 0;
                this.lastDelivered = null;
            } else {
                this.lastDelivered = md;
            }
            return (ActiveMQMessage) md.getMessage();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw JMSExceptionSupport.create(e);
        }
    }

    protected void checkClosed() throws IllegalStateException {
        if (this.unconsumedMessages.isClosed()) {
            throw new IllegalStateException("The Consumer is closed");
        }
    }

    public int read() throws IOException {
        fillBuffer();
        if (this.eosReached || this.buffer.length == 0) {
            return -1;
        }
        byte[] bArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & Type.ANY;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        fillBuffer();
        if (this.eosReached || this.buffer.length == 0) {
            return -1;
        }
        int max = Math.min(len, this.buffer.length - this.pos);
        System.arraycopy(this.buffer, this.pos, b, off, max);
        this.pos += max;
        return max;
    }

    private void fillBuffer() throws IOException {
        if (!this.eosReached) {
            if (this.buffer == null || this.buffer.length <= this.pos) {
                ActiveMQMessage m;
                ActiveMQBytesMessage bm;
                long producerSequenceId;
                do {
                    try {
                        m = receive();
                        if (m == null || m.getDataStructureType() != 24) {
                            this.eosReached = true;
                            if (this.jmsProperties == null) {
                                this.jmsProperties = Collections.emptyMap();
                                return;
                            }
                            return;
                        }
                        producerSequenceId = m.getMessageId().getProducerSequenceId();
                        if (this.producerId != null) {
                            if (m.getMessageId().getProducerId().equals(this.producerId)) {
                                long j = this.nextSequenceId;
                                this.nextSequenceId = j + 1;
                                if (producerSequenceId != j) {
                                    throw new IOException("Received an unexpected message: expected ID: " + (this.nextSequenceId - 1) + " but was: " + producerSequenceId + " for message: " + m);
                                }
                            }
                            throw new IOException("Received an unexpected message: invalid producer: " + m);
                        }
                        bm = (ActiveMQBytesMessage) m;
                        this.buffer = new byte[((int) bm.getBodyLength())];
                        bm.readBytes(this.buffer);
                        this.pos = 0;
                        if (this.jmsProperties == null) {
                            this.jmsProperties = Collections.unmodifiableMap(new HashMap(bm.getProperties()));
                        }
                    } catch (Exception e) {
                        this.eosReached = true;
                        if (this.jmsProperties == null) {
                            this.jmsProperties = Collections.emptyMap();
                        }
                        throw IOExceptionSupport.create(e);
                    }
                } while (producerSequenceId != 0);
                this.nextSequenceId++;
                this.producerId = m.getMessageId().getProducerId();
                bm = (ActiveMQBytesMessage) m;
                this.buffer = new byte[((int) bm.getBodyLength())];
                bm.readBytes(this.buffer);
                this.pos = 0;
                if (this.jmsProperties == null) {
                    this.jmsProperties = Collections.unmodifiableMap(new HashMap(bm.getProperties()));
                }
            }
        }
    }

    public void dispatch(MessageDispatch md) {
        this.unconsumedMessages.enqueue(md);
    }

    public String toString() {
        return "ActiveMQInputStream { value=" + this.info.getConsumerId() + ", producerId=" + this.producerId + " }";
    }
}
