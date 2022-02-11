package org.apache.activemq.command;

import javax.jms.JMSException;
import javax.jms.Topic;

public class ActiveMQTopic extends ActiveMQDestination implements Topic {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 101;
    private static final long serialVersionUID = 7300307405896488588L;

    public ActiveMQTopic(String name) {
        super(name);
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
        return (byte) 2;
    }

    protected String getQualifiedPrefix() {
        return ActiveMQDestination.TOPIC_QUALIFIED_PREFIX;
    }
}
