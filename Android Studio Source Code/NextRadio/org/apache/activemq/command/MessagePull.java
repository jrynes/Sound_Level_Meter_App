package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class MessagePull extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 20;
    protected ConsumerId consumerId;
    private String correlationId;
    protected ActiveMQDestination destination;
    private MessageId messageId;
    protected long timeout;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processMessagePull(this);
    }

    public void configure(ConsumerInfo info) {
        setConsumerId(info.getConsumerId());
        setDestination(info.getDestination());
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

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public MessageId getMessageId() {
        return this.messageId;
    }

    public void setMessageId(MessageId messageId) {
        this.messageId = messageId;
    }
}
