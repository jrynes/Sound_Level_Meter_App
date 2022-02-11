package org.apache.activemq.filter;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.jms.JMSException;
import org.apache.activemq.transport.stomp.Stomp;

public abstract class UnaryExpression implements Expression {
    private static final BigDecimal BD_LONG_MIN_VALUE;
    protected Expression right;

    static class 1 extends UnaryExpression {
        1(Expression x0) {
            super(x0);
        }

        public Object evaluate(MessageEvaluationContext message) throws JMSException {
            Object rvalue = this.right.evaluate(message);
            if (rvalue != null && (rvalue instanceof Number)) {
                return UnaryExpression.negate((Number) rvalue);
            }
            return null;
        }

        public String getExpressionSymbol() {
            return "-";
        }
    }

    static abstract class BooleanUnaryExpression extends UnaryExpression implements BooleanExpression {
        public BooleanUnaryExpression(Expression left) {
            super(left);
        }

        public boolean matches(MessageEvaluationContext message) throws JMSException {
            Boolean object = evaluate(message);
            return object != null && object == Boolean.TRUE;
        }
    }

    static class 2 extends BooleanUnaryExpression {
        final /* synthetic */ Collection val$inList;
        final /* synthetic */ boolean val$not;

        2(Expression x0, Collection collection, boolean z) {
            this.val$inList = collection;
            this.val$not = z;
            super(x0);
        }

        public Object evaluate(MessageEvaluationContext message) throws JMSException {
            Object rvalue = this.right.evaluate(message);
            if (rvalue == null || rvalue.getClass() != String.class) {
                return null;
            }
            int i = (this.val$inList == null || !this.val$inList.contains(rvalue)) ? 0 : 1;
            if ((i ^ this.val$not) != 0) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        public String toString() {
            StringBuffer answer = new StringBuffer();
            answer.append(this.right);
            answer.append(" ");
            answer.append(getExpressionSymbol());
            answer.append(" ( ");
            int count = 0;
            for (Object o : this.val$inList) {
                if (count != 0) {
                    answer.append(", ");
                }
                answer.append(o);
                count++;
            }
            answer.append(" )");
            return answer.toString();
        }

        public String getExpressionSymbol() {
            if (this.val$not) {
                return "NOT IN";
            }
            return "IN";
        }
    }

    static class 3 extends BooleanUnaryExpression {
        3(Expression x0) {
            super(x0);
        }

        public Object evaluate(MessageEvaluationContext message) throws JMSException {
            Boolean lvalue = (Boolean) this.right.evaluate(message);
            if (lvalue == null) {
                return null;
            }
            return lvalue.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
        }

        public String getExpressionSymbol() {
            return "NOT";
        }
    }

    static class 4 extends BooleanUnaryExpression {
        4(Expression x0) {
            super(x0);
        }

        public Object evaluate(MessageEvaluationContext message) throws JMSException {
            Object rvalue = this.right.evaluate(message);
            if (rvalue == null) {
                return null;
            }
            if (rvalue.getClass().equals(Boolean.class)) {
                return ((Boolean) rvalue).booleanValue() ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return Boolean.FALSE;
            }
        }

        public String toString() {
            return this.right.toString();
        }

        public String getExpressionSymbol() {
            return Stomp.EMPTY;
        }
    }

    public abstract String getExpressionSymbol();

    static {
        BD_LONG_MIN_VALUE = BigDecimal.valueOf(Long.MIN_VALUE);
    }

    public UnaryExpression(Expression left) {
        this.right = left;
    }

    public static Expression createNegate(Expression left) {
        return new 1(left);
    }

    public static BooleanExpression createInExpression(PropertyExpression right, List<Object> elements, boolean not) {
        Collection<Object> t;
        if (elements.size() == 0) {
            t = null;
        } else if (elements.size() < 5) {
            Object t2 = elements;
        } else {
            t = new HashSet(elements);
        }
        return new 2(right, t, not);
    }

    public static BooleanExpression createNOT(BooleanExpression left) {
        return new 3(left);
    }

    public static BooleanExpression createXPath(String xpath) {
        return new XPathExpression(xpath);
    }

    public static BooleanExpression createXQuery(String xpath) {
        return new XQueryExpression(xpath);
    }

    public static BooleanExpression createBooleanCast(Expression left) {
        return new 4(left);
    }

    private static Number negate(Number left) {
        Class clazz = left.getClass();
        if (clazz == Integer.class) {
            return new Integer(-left.intValue());
        }
        if (clazz == Long.class) {
            return new Long(-left.longValue());
        }
        if (clazz == Float.class) {
            return new Float(-left.floatValue());
        }
        if (clazz == Double.class) {
            return new Double(-left.doubleValue());
        }
        if (clazz == BigDecimal.class) {
            Number bd = ((BigDecimal) left).negate();
            if (BD_LONG_MIN_VALUE.compareTo(bd) == 0) {
                return Long.valueOf(Long.MIN_VALUE);
            }
            return bd;
        }
        throw new RuntimeException("Don't know how to negate: " + left);
    }

    public Expression getRight() {
        return this.right;
    }

    public void setRight(Expression expression) {
        this.right = expression;
    }

    public String toString() {
        return "(" + getExpressionSymbol() + " " + this.right.toString() + ")";
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(Object o) {
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        return toString().equals(o.toString());
    }
}
