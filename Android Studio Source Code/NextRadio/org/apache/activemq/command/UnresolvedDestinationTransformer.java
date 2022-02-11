package org.apache.activemq.command;

import javax.jms.Destination;
import javax.jms.JMSException;

public interface UnresolvedDestinationTransformer {
    ActiveMQDestination transform(String str) throws JMSException;

    ActiveMQDestination transform(Destination destination) throws JMSException;
}
