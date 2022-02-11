package org.apache.activemq.filter;

import javax.jms.JMSException;

public interface Expression {
    Object evaluate(MessageEvaluationContext messageEvaluationContext) throws JMSException;
}
