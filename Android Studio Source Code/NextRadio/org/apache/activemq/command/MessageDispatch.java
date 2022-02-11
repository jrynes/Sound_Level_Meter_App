package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;
import org.apache.activemq.transport.TransmitCallback;

public class MessageDispatch extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 21;
    protected transient Object consumer;
    protected ConsumerId consumerId;
    protected transient long deliverySequenceId;
    protected ActiveMQDestination destination;
    protected Message message;
    protected int redeliveryCounter;
    protected transient Throwable rollbackCause;
    protected transient TransmitCallback transmitCallback;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isMessageDispatch() {
        return true;
    }

    public ConsumerId getConsumerId() {
        return this.consumerId;
    }

    public void setConsumerId(ConsumerId consumerId) {
        this.consumerId = consumerId;
    }

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public long getDeliverySequenceId() {
        return this.deliverySequenceId;
    }

    public void setDeliverySequenceId(long deliverySequenceId) {
        this.deliverySequenceId = deliverySequenceId;
    }

    public int getRedeliveryCounter() {
        return this.redeliveryCounter;
    }

    public void setRedeliveryCounter(int deliveryCounter) {
        this.redeliveryCounter = deliveryCounter;
    }

    public Object getConsumer() {
        return this.consumer;
    }

    public void setConsumer(Object consumer) {
        this.consumer = consumer;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processMessageDispatch(this);
    }

    public TransmitCallback getTransmitCallback() {
        return this.transmitCallback;
    }

    public void setTransmitCallback(TransmitCallback transmitCallback) {
        this.transmitCallback = transmitCallback;
    }

    public Throwable getRollbackCause() {
        return this.rollbackCause;
    }

    public void setRollbackCause(Throwable rollbackCause) {
        this.rollbackCause = rollbackCause;
    }
}
