package org.apache.activemq.command;

import javax.jms.JMSException;
import javax.jms.TemporaryQueue;

public class ActiveMQTempQueue extends ActiveMQTempDestination implements TemporaryQueue {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 102;
    private static final long serialVersionUID = 6683049467527633867L;

    public ActiveMQTempQueue(String name) {
        super(name);
    }

    public ActiveMQTempQueue(ConnectionId connectionId, long sequenceId) {
        super(connectionId.getValue(), sequenceId);
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isQueue() {
        return true;
    }

    public String getQueueName() throws JMSException {
        return getPhysicalName();
    }

    public byte getDestinationType() {
        return (byte) 5;
    }

    protected String getQualifiedPrefix() {
        return ActiveMQDestination.TEMP_QUEUE_QUALIFED_PREFIX;
    }
}
