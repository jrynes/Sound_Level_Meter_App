package org.apache.activemq.filter;

import java.math.BigDecimal;
import javax.jms.JMSException;
import org.xbill.DNS.TTL;

public class ConstantExpression implements Expression {
    public static final BooleanConstantExpression FALSE;
    public static final BooleanConstantExpression NULL;
    public static final BooleanConstantExpression TRUE;
    private Object value;

    static class BooleanConstantExpression extends ConstantExpression implements BooleanExpression {
        public BooleanConstantExpression(Object value) {
            super(value);
        }

        public boolean matches(MessageEvaluationContext message) throws JMSException {
            Boolean object = evaluate(message);
            return object != null && object == Boolean.TRUE;
        }
    }

    static {
        NULL = new BooleanConstantExpression(null);
        TRUE = new BooleanConstantExpression(Boolean.TRUE);
        FALSE = new BooleanConstantExpression(Boolean.FALSE);
    }

    public ConstantExpression(Object value) {
        this.value = value;
    }

    public static ConstantExpression createFromDecimal(String text) {
        Number value;
        if (text.endsWith("l") || text.endsWith("L")) {
            text = text.substring(0, text.length() - 1);
        }
        try {
            value = new Long(text);
        } catch (NumberFormatException e) {
            value = new BigDecimal(text);
        }
        long l = value.longValue();
        if (-2147483648L <= l && l <= TTL.MAX_VALUE) {
            value = Integer.valueOf(value.intValue());
        }
        return new ConstantExpression(value);
    }

    public static ConstantExpression createFromHex(String text) {
        Number value = Long.valueOf(Long.parseLong(text.substring(2), 16));
        long l = value.longValue();
        if (-2147483648L <= l && l <= TTL.MAX_VALUE) {
            value = Integer.valueOf(value.intValue());
        }
        return new ConstantExpression(value);
    }

    public static ConstantExpression createFromOctal(String text) {
        Number value = Long.valueOf(Long.parseLong(text, 8));
        long l = value.longValue();
        if (-2147483648L <= l && l <= TTL.MAX_VALUE) {
            value = Integer.valueOf(value.intValue());
        }
        return new ConstantExpression(value);
    }

    public static ConstantExpression createFloat(String text) {
        return new ConstantExpression(new Double(text));
    }

    public Object evaluate(MessageEvaluationContext message) throws JMSException {
        return this.value;
    }

    public Object getValue() {
        return this.value;
    }

    public String toString() {
        if (this.value == null) {
            return "NULL";
        }
        if (this.value instanceof Boolean) {
            return ((Boolean) this.value).booleanValue() ? "TRUE" : "FALSE";
        } else {
            if (this.value instanceof String) {
                return encodeString((String) this.value);
            }
            return this.value.toString();
        }
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

    public static String encodeString(String s) {
        StringBuffer b = new StringBuffer();
        b.append('\'');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\'') {
                b.append(c);
            }
            b.append(c);
        }
        b.append('\'');
        return b.toString();
    }
}
