package org.apache.activemq.filter;

import java.util.HashMap;
import java.util.Map;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.util.JMSExceptionSupport;

public class PropertyExpression implements Expression {
    private static final Map<String, SubExpression> JMS_PROPERTY_EXPRESSIONS;
    private final SubExpression jmsPropertyExpression;
    private final String name;

    interface SubExpression {
        Object evaluate(Message message);
    }

    static class 10 implements SubExpression {
        10() {
        }

        public Object evaluate(Message message) {
            return Boolean.valueOf(message.isRedelivered());
        }
    }

    static class 11 implements SubExpression {
        11() {
        }

        public Object evaluate(Message message) {
            return Integer.valueOf(message.getRedeliveryCounter() + 1);
        }
    }

    static class 12 implements SubExpression {
        12() {
        }

        public Object evaluate(Message message) {
            return message.getGroupID();
        }
    }

    static class 13 implements SubExpression {
        13() {
        }

        public Object evaluate(Message message) {
            return new Integer(message.getGroupSequence());
        }
    }

    static class 14 implements SubExpression {
        14() {
        }

        public Object evaluate(Message message) {
            TransactionId txId = message.getOriginalTransactionId();
            if (txId == null) {
                txId = message.getTransactionId();
            }
            if (txId == null) {
                return null;
            }
            return new Integer(txId.toString());
        }
    }

    static class 15 implements SubExpression {
        15() {
        }

        public Object evaluate(Message message) {
            return Long.valueOf(message.getBrokerInTime());
        }
    }

    static class 16 implements SubExpression {
        16() {
        }

        public Object evaluate(Message message) {
            return Long.valueOf(message.getBrokerOutTime());
        }
    }

    static class 1 implements SubExpression {
        1() {
        }

        public Object evaluate(Message message) {
            ActiveMQDestination dest = message.getOriginalDestination();
            if (dest == null) {
                dest = message.getDestination();
            }
            if (dest == null) {
                return null;
            }
            return dest.toString();
        }
    }

    static class 2 implements SubExpression {
        2() {
        }

        public Object evaluate(Message message) {
            if (message.getReplyTo() == null) {
                return null;
            }
            return message.getReplyTo().toString();
        }
    }

    static class 3 implements SubExpression {
        3() {
        }

        public Object evaluate(Message message) {
            return message.getType();
        }
    }

    static class 4 implements SubExpression {
        4() {
        }

        public Object evaluate(Message message) {
            return message.isPersistent() ? "PERSISTENT" : "NON_PERSISTENT";
        }
    }

    static class 5 implements SubExpression {
        5() {
        }

        public Object evaluate(Message message) {
            return Integer.valueOf(message.getPriority());
        }
    }

    static class 6 implements SubExpression {
        6() {
        }

        public Object evaluate(Message message) {
            if (message.getMessageId() == null) {
                return null;
            }
            return message.getMessageId().toString();
        }
    }

    static class 7 implements SubExpression {
        7() {
        }

        public Object evaluate(Message message) {
            return Long.valueOf(message.getTimestamp());
        }
    }

    static class 8 implements SubExpression {
        8() {
        }

        public Object evaluate(Message message) {
            return message.getCorrelationId();
        }
    }

    static class 9 implements SubExpression {
        9() {
        }

        public Object evaluate(Message message) {
            return Long.valueOf(message.getExpiration());
        }
    }

    static {
        JMS_PROPERTY_EXPRESSIONS = new HashMap();
        JMS_PROPERTY_EXPRESSIONS.put("JMSDestination", new 1());
        JMS_PROPERTY_EXPRESSIONS.put("JMSReplyTo", new 2());
        JMS_PROPERTY_EXPRESSIONS.put("JMSType", new 3());
        JMS_PROPERTY_EXPRESSIONS.put("JMSDeliveryMode", new 4());
        JMS_PROPERTY_EXPRESSIONS.put("JMSPriority", new 5());
        JMS_PROPERTY_EXPRESSIONS.put("JMSMessageID", new 6());
        JMS_PROPERTY_EXPRESSIONS.put("JMSTimestamp", new 7());
        JMS_PROPERTY_EXPRESSIONS.put("JMSCorrelationID", new 8());
        JMS_PROPERTY_EXPRESSIONS.put("JMSExpiration", new 9());
        JMS_PROPERTY_EXPRESSIONS.put("JMSRedelivered", new 10());
        JMS_PROPERTY_EXPRESSIONS.put("JMSXDeliveryCount", new 11());
        JMS_PROPERTY_EXPRESSIONS.put("JMSXGroupID", new 12());
        JMS_PROPERTY_EXPRESSIONS.put("JMSXGroupSeq", new 13());
        JMS_PROPERTY_EXPRESSIONS.put("JMSXProducerTXID", new 14());
        JMS_PROPERTY_EXPRESSIONS.put("JMSActiveMQBrokerInTime", new 15());
        JMS_PROPERTY_EXPRESSIONS.put("JMSActiveMQBrokerOutTime", new 16());
    }

    public PropertyExpression(String name) {
        this.name = name;
        this.jmsPropertyExpression = (SubExpression) JMS_PROPERTY_EXPRESSIONS.get(name);
    }

    public Object evaluate(MessageEvaluationContext message) throws JMSException {
        try {
            if (message.isDropped()) {
                return null;
            }
            if (this.jmsPropertyExpression != null) {
                return this.jmsPropertyExpression.evaluate(message.getMessage());
            }
            return message.getMessage().getProperty(this.name);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create("Could not get property: " + this.name + " reason: " + ioe.getMessage(), ioe);
        } catch (Exception e) {
            throw JMSExceptionSupport.create(e);
        }
    }

    public Object evaluate(Message message) throws JMSException {
        if (this.jmsPropertyExpression != null) {
            return this.jmsPropertyExpression.evaluate(message);
        }
        try {
            return message.getProperty(this.name);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean equals(Object o) {
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        return this.name.equals(((PropertyExpression) o).name);
    }
}
