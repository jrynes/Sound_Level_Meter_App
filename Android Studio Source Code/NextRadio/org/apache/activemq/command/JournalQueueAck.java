package org.apache.activemq.command;

import org.apache.activemq.util.IntrospectionSupport;

public class JournalQueueAck implements DataStructure {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 52;
    ActiveMQDestination destination;
    MessageAck messageAck;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public MessageAck getMessageAck() {
        return this.messageAck;
    }

    public void setMessageAck(MessageAck messageAck) {
        this.messageAck = messageAck;
    }

    public boolean isMarshallAware() {
        return false;
    }

    public String toString() {
        return IntrospectionSupport.toString(this, JournalQueueAck.class);
    }
}
