package org.apache.activemq.filter;

import javax.jms.JMSException;
import org.apache.activemq.util.JMSExceptionSupport;

public class NoLocalExpression implements BooleanExpression {
    private final String connectionId;

    public NoLocalExpression(String connectionId) {
        this.connectionId = connectionId;
    }

    public boolean matches(MessageEvaluationContext message) throws JMSException {
        try {
            if (message.isDropped()) {
                return false;
            }
            if (this.connectionId.equals(message.getMessage().getProducerId().getConnectionId())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw JMSExceptionSupport.create(e);
        }
    }

    public Object evaluate(MessageEvaluationContext message) throws JMSException {
        return matches(message) ? Boolean.TRUE : Boolean.FALSE;
    }
}
