package org.apache.activemq.transport.stomp;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;
import org.apache.activemq.transport.stomp.Stomp.Headers.Subscribe;
import org.apache.activemq.transport.stomp.Stomp.Responses;

public class StompSubscription {
    public static final String AUTO_ACK = "auto";
    public static final String CLIENT_ACK = "client";
    public static final String INDIVIDUAL_ACK = "client-individual";
    protected String ackMode;
    protected final ConsumerInfo consumerInfo;
    protected ActiveMQDestination destination;
    protected final LinkedHashMap<MessageId, MessageDispatch> dispatchedMessage;
    protected final ProtocolConverter protocolConverter;
    protected final String subscriptionId;
    protected String transformation;
    protected final LinkedList<MessageDispatch> unconsumedMessage;

    public StompSubscription(ProtocolConverter stompTransport, String subscriptionId, ConsumerInfo consumerInfo, String transformation) {
        this.dispatchedMessage = new LinkedHashMap();
        this.unconsumedMessage = new LinkedList();
        this.ackMode = AUTO_ACK;
        this.protocolConverter = stompTransport;
        this.subscriptionId = subscriptionId;
        this.consumerInfo = consumerInfo;
        this.transformation = transformation;
    }

    void onMessageDispatch(MessageDispatch md, String ackId) throws IOException, JMSException {
        ActiveMQMessage message = (ActiveMQMessage) md.getMessage();
        if (this.ackMode == CLIENT_ACK) {
            synchronized (this) {
                this.dispatchedMessage.put(message.getMessageId(), md);
            }
        } else if (this.ackMode == INDIVIDUAL_ACK) {
            synchronized (this) {
                this.dispatchedMessage.put(message.getMessageId(), md);
            }
        } else if (this.ackMode == AUTO_ACK) {
            this.protocolConverter.getStompTransport().sendToActiveMQ(new MessageAck(md, (byte) 2, 1));
        }
        boolean ignoreTransformation = false;
        if (this.transformation != null && !(message instanceof ActiveMQBytesMessage)) {
            message.setReadOnlyProperties(false);
            message.setStringProperty(Headers.TRANSFORMATION, this.transformation);
        } else if (message.getStringProperty(Headers.TRANSFORMATION) != null) {
            ignoreTransformation = true;
        }
        StompFrame command = this.protocolConverter.convertMessage(message, ignoreTransformation);
        command.setAction(Responses.MESSAGE);
        if (this.subscriptionId != null) {
            command.getHeaders().put(Message.SUBSCRIPTION, this.subscriptionId);
        }
        if (ackId != null) {
            command.getHeaders().put(Subscribe.ACK_MODE, ackId);
        }
        this.protocolConverter.getStompTransport().sendToStomp(command);
    }

    synchronized void onStompAbort(TransactionId transactionId) {
        this.unconsumedMessage.clear();
    }

    void onStompCommit(TransactionId transactionId) {
        Throwable th;
        MessageAck ack = null;
        synchronized (this) {
            try {
                Iterator<?> iter = this.dispatchedMessage.entrySet().iterator();
                while (iter.hasNext()) {
                    if (this.unconsumedMessage.contains((MessageDispatch) ((Entry) iter.next()).getValue())) {
                        iter.remove();
                    }
                }
                if (!this.unconsumedMessage.isEmpty()) {
                    MessageAck ack2 = new MessageAck((MessageDispatch) this.unconsumedMessage.getLast(), (byte) 2, this.unconsumedMessage.size());
                    try {
                        this.unconsumedMessage.clear();
                        ack = ack2;
                    } catch (Throwable th2) {
                        th = th2;
                        ack = ack2;
                        throw th;
                    }
                }
                if (ack != null) {
                    this.protocolConverter.getStompTransport().sendToActiveMQ(ack);
                }
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    synchronized MessageAck onStompMessageAck(String messageId, TransactionId transactionId) {
        MessageAck ack;
        MessageId msgId = new MessageId(messageId);
        if (this.dispatchedMessage.containsKey(msgId)) {
            ack = new MessageAck();
            ack.setDestination(this.consumerInfo.getDestination());
            ack.setConsumerId(this.consumerInfo.getConsumerId());
            if (this.ackMode == CLIENT_ACK) {
                if (transactionId == null) {
                    ack.setAckType((byte) 2);
                } else {
                    ack.setAckType((byte) 0);
                }
                int count = 0;
                Iterator<?> iter = this.dispatchedMessage.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry entry = (Entry) iter.next();
                    MessageId id = (MessageId) entry.getKey();
                    MessageDispatch msg = (MessageDispatch) entry.getValue();
                    if (transactionId == null) {
                        iter.remove();
                        count++;
                    } else if (!this.unconsumedMessage.contains(msg)) {
                        this.unconsumedMessage.add(msg);
                        count++;
                    }
                    if (id.equals(msgId)) {
                        ack.setLastMessageId(id);
                        break;
                    }
                }
                ack.setMessageCount(count);
                if (transactionId != null) {
                    ack.setTransactionId(transactionId);
                }
            } else if (this.ackMode == INDIVIDUAL_ACK) {
                ack.setAckType((byte) 4);
                ack.setMessageID(msgId);
                if (transactionId != null) {
                    this.unconsumedMessage.add(this.dispatchedMessage.get(msgId));
                    ack.setTransactionId(transactionId);
                }
                this.dispatchedMessage.remove(msgId);
            }
        } else {
            ack = null;
        }
        return ack;
    }

    public MessageAck onStompMessageNack(String messageId, TransactionId transactionId) throws ProtocolException {
        MessageId msgId = new MessageId(messageId);
        if (!this.dispatchedMessage.containsKey(msgId)) {
            return null;
        }
        MessageAck ack = new MessageAck();
        ack.setDestination(this.consumerInfo.getDestination());
        ack.setConsumerId(this.consumerInfo.getConsumerId());
        ack.setAckType((byte) 1);
        ack.setMessageID(msgId);
        if (transactionId != null) {
            this.unconsumedMessage.add(this.dispatchedMessage.get(msgId));
            ack.setTransactionId(transactionId);
        }
        this.dispatchedMessage.remove(msgId);
        return ack;
    }

    public String getAckMode() {
        return this.ackMode;
    }

    public void setAckMode(String ackMode) {
        this.ackMode = ackMode;
    }

    public String getSubscriptionId() {
        return this.subscriptionId;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public ConsumerInfo getConsumerInfo() {
        return this.consumerInfo;
    }
}
