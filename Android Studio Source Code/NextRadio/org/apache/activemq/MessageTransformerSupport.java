package org.apache.activemq;

import javax.jms.JMSException;
import javax.jms.Message;

public abstract class MessageTransformerSupport implements MessageTransformer {
    protected void copyProperties(Message fromMessage, Message toMesage) throws JMSException {
        ActiveMQMessageTransformation.copyProperties(fromMessage, toMesage);
    }
}
