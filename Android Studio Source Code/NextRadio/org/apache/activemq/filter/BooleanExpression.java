package org.apache.activemq.filter;

import javax.jms.JMSException;

public interface BooleanExpression extends Expression {
    boolean matches(MessageEvaluationContext messageEvaluationContext) throws JMSException;
}
