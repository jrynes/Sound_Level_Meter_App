package org.apache.activemq.filter;

import javax.jms.JMSException;

public abstract class LogicExpression extends BinaryExpression implements BooleanExpression {

    static class 1 extends LogicExpression {
        1(BooleanExpression x0, BooleanExpression x1) {
            super(x0, x1);
        }

        public Object evaluate(MessageEvaluationContext message) throws JMSException {
            Boolean lv = (Boolean) this.left.evaluate(message);
            if (lv != null && lv.booleanValue()) {
                return Boolean.TRUE;
            }
            Boolean rv = (Boolean) this.right.evaluate(message);
            return rv == null ? null : rv;
        }

        public String getExpressionSymbol() {
            return "OR";
        }
    }

    static class 2 extends LogicExpression {
        2(BooleanExpression x0, BooleanExpression x1) {
            super(x0, x1);
        }

        public Object evaluate(MessageEvaluationContext message) throws JMSException {
            Boolean lv = (Boolean) this.left.evaluate(message);
            if (lv == null) {
                return null;
            }
            if (!lv.booleanValue()) {
                return Boolean.FALSE;
            }
            Boolean rv = (Boolean) this.right.evaluate(message);
            if (rv == null) {
                rv = null;
            }
            return rv;
        }

        public String getExpressionSymbol() {
            return "AND";
        }
    }

    public abstract Object evaluate(MessageEvaluationContext messageEvaluationContext) throws JMSException;

    public LogicExpression(BooleanExpression left, BooleanExpression right) {
        super(left, right);
    }

    public static BooleanExpression createOR(BooleanExpression lvalue, BooleanExpression rvalue) {
        return new 1(lvalue, rvalue);
    }

    public static BooleanExpression createAND(BooleanExpression lvalue, BooleanExpression rvalue) {
        return new 2(lvalue, rvalue);
    }

    public boolean matches(MessageEvaluationContext message) throws JMSException {
        Boolean object = evaluate(message);
        return object != null && object == Boolean.TRUE;
    }
}
