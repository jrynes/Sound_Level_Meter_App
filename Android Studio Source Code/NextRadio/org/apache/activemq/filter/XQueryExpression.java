package org.apache.activemq.filter;

import javax.jms.JMSException;

public final class XQueryExpression implements BooleanExpression {
    private final String xpath;

    XQueryExpression(String xpath) {
        this.xpath = xpath;
    }

    public Object evaluate(MessageEvaluationContext message) throws JMSException {
        return Boolean.FALSE;
    }

    public String toString() {
        return "XQUERY " + ConstantExpression.encodeString(this.xpath);
    }

    public boolean matches(MessageEvaluationContext message) throws JMSException {
        Boolean object = evaluate(message);
        return object != null && object == Boolean.TRUE;
    }
}
