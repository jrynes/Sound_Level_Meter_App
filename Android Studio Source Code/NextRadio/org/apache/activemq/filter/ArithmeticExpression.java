package org.apache.activemq.filter;

import javax.jms.JMSException;
import org.apache.activemq.jndi.ReadOnlyContext;

public abstract class ArithmeticExpression extends BinaryExpression {
    protected static final int DOUBLE = 3;
    protected static final int INTEGER = 1;
    protected static final int LONG = 2;

    static class 1 extends ArithmeticExpression {
        1(Expression x0, Expression x1) {
            super(x0, x1);
        }

        protected Object evaluate(Object lvalue, Object rvalue) {
            if (lvalue instanceof String) {
                return ((String) lvalue) + rvalue;
            } else if (lvalue instanceof Number) {
                return plus((Number) lvalue, asNumber(rvalue));
            } else {
                throw new RuntimeException("Cannot call plus operation on: " + lvalue + " and: " + rvalue);
            }
        }

        public String getExpressionSymbol() {
            return "+";
        }
    }

    static class 2 extends ArithmeticExpression {
        2(Expression x0, Expression x1) {
            super(x0, x1);
        }

        protected Object evaluate(Object lvalue, Object rvalue) {
            if (lvalue instanceof Number) {
                return minus((Number) lvalue, asNumber(rvalue));
            }
            throw new RuntimeException("Cannot call minus operation on: " + lvalue + " and: " + rvalue);
        }

        public String getExpressionSymbol() {
            return "-";
        }
    }

    static class 3 extends ArithmeticExpression {
        3(Expression x0, Expression x1) {
            super(x0, x1);
        }

        protected Object evaluate(Object lvalue, Object rvalue) {
            if (lvalue instanceof Number) {
                return multiply((Number) lvalue, asNumber(rvalue));
            }
            throw new RuntimeException("Cannot call multiply operation on: " + lvalue + " and: " + rvalue);
        }

        public String getExpressionSymbol() {
            return DestinationFilter.ANY_CHILD;
        }
    }

    static class 4 extends ArithmeticExpression {
        4(Expression x0, Expression x1) {
            super(x0, x1);
        }

        protected Object evaluate(Object lvalue, Object rvalue) {
            if (lvalue instanceof Number) {
                return divide((Number) lvalue, asNumber(rvalue));
            }
            throw new RuntimeException("Cannot call divide operation on: " + lvalue + " and: " + rvalue);
        }

        public String getExpressionSymbol() {
            return ReadOnlyContext.SEPARATOR;
        }
    }

    static class 5 extends ArithmeticExpression {
        5(Expression x0, Expression x1) {
            super(x0, x1);
        }

        protected Object evaluate(Object lvalue, Object rvalue) {
            if (lvalue instanceof Number) {
                return mod((Number) lvalue, asNumber(rvalue));
            }
            throw new RuntimeException("Cannot call mod operation on: " + lvalue + " and: " + rvalue);
        }

        public String getExpressionSymbol() {
            return "%";
        }
    }

    protected abstract Object evaluate(Object obj, Object obj2);

    public ArithmeticExpression(Expression left, Expression right) {
        super(left, right);
    }

    public static Expression createPlus(Expression left, Expression right) {
        return new 1(left, right);
    }

    public static Expression createMinus(Expression left, Expression right) {
        return new 2(left, right);
    }

    public static Expression createMultiply(Expression left, Expression right) {
        return new 3(left, right);
    }

    public static Expression createDivide(Expression left, Expression right) {
        return new 4(left, right);
    }

    public static Expression createMod(Expression left, Expression right) {
        return new 5(left, right);
    }

    protected Number plus(Number left, Number right) {
        switch (numberType(left, right)) {
            case INTEGER /*1*/:
                return new Integer(left.intValue() + right.intValue());
            case LONG /*2*/:
                return new Long(left.longValue() + right.longValue());
            default:
                return new Double(left.doubleValue() + right.doubleValue());
        }
    }

    protected Number minus(Number left, Number right) {
        switch (numberType(left, right)) {
            case INTEGER /*1*/:
                return new Integer(left.intValue() - right.intValue());
            case LONG /*2*/:
                return new Long(left.longValue() - right.longValue());
            default:
                return new Double(left.doubleValue() - right.doubleValue());
        }
    }

    protected Number multiply(Number left, Number right) {
        switch (numberType(left, right)) {
            case INTEGER /*1*/:
                return new Integer(left.intValue() * right.intValue());
            case LONG /*2*/:
                return new Long(left.longValue() * right.longValue());
            default:
                return new Double(left.doubleValue() * right.doubleValue());
        }
    }

    protected Number divide(Number left, Number right) {
        return new Double(left.doubleValue() / right.doubleValue());
    }

    protected Number mod(Number left, Number right) {
        return new Double(left.doubleValue() % right.doubleValue());
    }

    private int numberType(Number left, Number right) {
        if (isDouble(left) || isDouble(right)) {
            return DOUBLE;
        }
        if ((left instanceof Long) || (right instanceof Long)) {
            return LONG;
        }
        return INTEGER;
    }

    private boolean isDouble(Number n) {
        return (n instanceof Float) || (n instanceof Double);
    }

    protected Number asNumber(Object value) {
        if (value instanceof Number) {
            return (Number) value;
        }
        throw new RuntimeException("Cannot convert value: " + value + " into a number");
    }

    public Object evaluate(MessageEvaluationContext message) throws JMSException {
        Object lvalue = this.left.evaluate(message);
        if (lvalue == null) {
            return null;
        }
        Object rvalue = this.right.evaluate(message);
        if (rvalue != null) {
            return evaluate(lvalue, rvalue);
        }
        return null;
    }
}
