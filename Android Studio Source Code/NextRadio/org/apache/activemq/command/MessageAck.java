package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class MessageAck extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 22;
    public static final byte DELIVERED_ACK_TYPE = (byte) 0;
    public static final byte INDIVIDUAL_ACK_TYPE = (byte) 4;
    public static final byte POSION_ACK_TYPE = (byte) 1;
    public static final byte REDELIVERED_ACK_TYPE = (byte) 3;
    public static final byte STANDARD_ACK_TYPE = (byte) 2;
    public static final byte UNMATCHED_ACK_TYPE = (byte) 5;
    protected byte ackType;
    protected ConsumerId consumerId;
    protected transient String consumerKey;
    protected ActiveMQDestination destination;
    protected MessageId firstMessageId;
    protected MessageId lastMessageId;
    protected int messageCount;
    protected Throwable poisonCause;
    protected TransactionId transactionId;

    public MessageAck(MessageDispatch md, byte ackType, int messageCount) {
        this.ackType = ackType;
        this.consumerId = md.getConsumerId();
        this.destination = md.getDestination();
        this.lastMessageId = md.getMessage().getMessageId();
        this.messageCount = messageCount;
    }

    public MessageAck(Message message, byte ackType, int messageCount) {
        this.ackType = ackType;
        this.destination = message.getDestination();
        this.lastMessageId = message.getMessageId();
        this.messageCount = messageCount;
    }

    public void copy(MessageAck copy) {
        super.copy(copy);
        copy.firstMessageId = this.firstMessageId;
        copy.lastMessageId = this.lastMessageId;
        copy.destination = this.destination;
        copy.transactionId = this.transactionId;
        copy.ackType = this.ackType;
        copy.consumerId = this.consumerId;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isMessageAck() {
        return true;
    }

    public boolean isPoisonAck() {
        return this.ackType == POSION_ACK_TYPE;
    }

    public boolean isStandardAck() {
        return this.ackType == 2;
    }

    public boolean isDeliveredAck() {
        return this.ackType == null;
    }

    public boolean isRedeliveredAck() {
        return this.ackType == 3;
    }

    public boolean isIndividualAck() {
        return this.ackType == 4;
    }

    public boolean isUnmatchedAck() {
        return this.ackType == 5;
    }

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public TransactionId getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isInTransaction() {
        return this.transactionId != null;
    }

    public ConsumerId getConsumerId() {
        return this.consumerId;
    }

    public void setConsumerId(ConsumerId consumerId) {
        this.consumerId = consumerId;
    }

    public byte getAckType() {
        return this.ackType;
    }

    public void setAckType(byte ackType) {
        this.ackType = ackType;
    }

    public MessageId getFirstMessageId() {
        return this.firstMessageId;
    }

    public void setFirstMessageId(MessageId firstMessageId) {
        this.firstMessageId = firstMessageId;
    }

    public MessageId getLastMessageId() {
        return this.lastMessageId;
    }

    public void setLastMessageId(MessageId lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public int getMessageCount() {
        return this.messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public Throwable getPoisonCause() {
        return this.poisonCause;
    }

    public void setPoisonCause(Throwable poisonCause) {
        this.poisonCause = poisonCause;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processMessageAck(this);
    }

    public void setMessageID(MessageId messageID) {
        setFirstMessageId(messageID);
        setLastMessageId(messageID);
        setMessageCount(1);
    }
}
