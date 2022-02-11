package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class MessageDispatchNotification extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 90;
    protected ConsumerId consumerId;
    protected long deliverySequenceId;
    protected ActiveMQDestination destination;
    protected MessageId messageId;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isMessageDispatchNotification() {
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

    public long getDeliverySequenceId() {
        return this.deliverySequenceId;
    }

    public void setDeliverySequenceId(long deliverySequenceId) {
        this.deliverySequenceId = deliverySequenceId;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processMessageDispatchNotification(this);
    }

    public MessageId getMessageId() {
        return this.messageId;
    }

    public void setMessageId(MessageId messageId) {
        this.messageId = messageId;
    }
}
