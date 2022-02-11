package org.apache.activemq.command;

import javax.jms.JMSException;
import javax.jms.TemporaryTopic;

public class ActiveMQTempTopic extends ActiveMQTempDestination implements TemporaryTopic {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 103;
    private static final long serialVersionUID = -4325596784597300253L;

    public ActiveMQTempTopic(String name) {
        super(name);
    }

    public ActiveMQTempTopic(ConnectionId connectionId, long sequenceId) {
        super(connectionId.getValue(), sequenceId);
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isTopic() {
        return true;
    }

    public String getTopicName() throws JMSException {
        return getPhysicalName();
    }

    public byte getDestinationType() {
        return (byte) 6;
    }

    protected String getQualifiedPrefix() {
        return ActiveMQDestination.TEMP_TOPIC_QUALIFED_PREFIX;
    }
}
