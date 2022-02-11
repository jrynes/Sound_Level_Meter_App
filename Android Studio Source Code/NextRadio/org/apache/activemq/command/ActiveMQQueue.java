package org.apache.activemq.command;

import javax.jms.JMSException;
import javax.jms.Queue;

public class ActiveMQQueue extends ActiveMQDestination implements Queue {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 100;
    private static final long serialVersionUID = -3885260014960795889L;

    public ActiveMQQueue(String name) {
        super(name);
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
        return (byte) 1;
    }

    protected String getQualifiedPrefix() {
        return ActiveMQDestination.QUEUE_QUALIFIED_PREFIX;
    }
}
