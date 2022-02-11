package org.apache.activemq.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQDestination;
import org.xbill.DNS.Message;

public abstract class ComparisonExpression extends BinaryExpression implements BooleanExpression {
    public static final ThreadLocal<Boolean> CONVERT_STRING_EXPRESSIONS;
    private static final Set<Character> REGEXP_CONTROL_CHARS;
    boolean convertStringExpressions;

    static class 1 extends ComparisonExpression {
        1(Expression x0, Expression x1) {
            super(x0, x1);
        }

        public Object evaluate(MessageEvaluationContext message) throws JMSException {
            int i;
            int i2 = 1;
            Object lv = this.left.evaluate(message);
            Object rv = this.right.evaluate(message);
            if (lv == null) {
                i = 1;
            } else {
                i = 0;
            }
            if (rv != null) {
                i2 = 0;
            }
            if ((i2 ^ i) != 0) {
                return Boolean.FALSE;
            }
            if (lv == rv || lv.equals(rv)) {
                return Boolean.TRUE;
            }
            if ((lv instanceof Comparable) && (rv instanceof Comparable)) {
                return compare((Comparable) lv, (Comparable) rv);
            }
            return Boolean.FALSE;
        }

        protected boolean asBoolean(int answer) {
            return answer == 0;
        }

        public String getExpressionSymbol() {
            return "=";
        }
    }

    static class 2 extends ComparisonExpression {
        2(Expression x0, Expression x1) {
            super(x0, x1);
        }

        protected boolean asBoolean(int answer) {
            return answer > 0;
        }

        public String getExpressionSymbol() {
            return DestinationFilter.ANY_DESCENDENT;
        }
    }

    static class 3 extends ComparisonExpression {
        3(Expression x0, Expression x1) {
            super(x0, x1);
        }

        protected boolean asBoolean(int answer) {
            return answer >= 0;
        }

        public String getExpressionSymbol() {
            return ">=";
        }
    }

    static class 4 extends ComparisonExpression {
        4(Expression x0, Expression x1) {
            super(x0, x1);
        }

        protected boolean asBoolean(int answer) {
            return answer < 0;
        }

        public String getExpressionSymbol() {
            return "<";
        }
    }

    static class 5 extends ComparisonExpression {
        5(Expression x0, Expression x1) {
            super(x0, x1);
        }

        protected boolean asBoolean(int answer) {
            return answer <= 0;
        }

        public String getExpressionSymbol() {
            return "<=";
        }
    }

    static class LikeExpression extends UnaryExpression implements BooleanExpression {
        Pattern likePattern;

        public LikeExpression(Expression right, String like, int escape) {
            super(right);
            StringBuffer regexp = new StringBuffer(like.length() * 2);
            regexp.append("\\A");
            int i = 0;
            while (i < like.length()) {
                char c = like.charAt(i);
                if (escape == (Message.MAXLENGTH & c)) {
                    i++;
                    if (i >= like.length()) {
                        break;
                    }
                    char t = like.charAt(i);
                    regexp.append("\\x");
                    regexp.append(Integer.toHexString(Message.MAXLENGTH & t));
                } else if (c == '%') {
                    regexp.append(".*?");
                } else if (c == '_') {
                    regexp.append(ActiveMQDestination.PATH_SEPERATOR);
                } else if (ComparisonExpression.REGEXP_CONTROL_CHARS.contains(new Character(c))) {
                    regexp.append("\\x");
                    regexp.append(Integer.toHexString(Message.MAXLENGTH & c));
                } else {
                    regexp.append(c);
                }
                i++;
            }
            regexp.append("\\z");
            this.likePattern = Pattern.compile(regexp.toString(), 32);
        }

        public String getExpressionSymbol() {
            return "LIKE";
        }

        public Object evaluate(MessageEvaluationContext message) throws JMSException {
            Object rv = getRight().evaluate(message);
            if (rv == null) {
                return null;
            }
            if (rv instanceof String) {
                return this.likePattern.matcher((String) rv).matches() ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return Boolean.FALSE;
            }
        }

        public boolean matches(MessageEvaluationContext message) throws JMSException {
            Boolean object = evaluate(message);
            return object != null && object == Boolean.TRUE;
        }
    }

    protected abstract boolean asBoolean(int i);

    static {
        CONVERT_STRING_EXPRESSIONS = new ThreadLocal();
        REGEXP_CONTROL_CHARS = new HashSet();
        REGEXP_CONTROL_CHARS.add(Character.valueOf('.'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('\\'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('['));
        REGEXP_CONTROL_CHARS.add(Character.valueOf(']'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('^'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('$'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('?'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('*'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('+'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('{'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('}'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('|'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('('));
        REGEXP_CONTROL_CHARS.add(Character.valueOf(')'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf(':'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('&'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('<'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('>'));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('='));
        REGEXP_CONTROL_CHARS.add(Character.valueOf('!'));
    }

    public ComparisonExpression(Expression left, Expression right) {
        boolean z = false;
        super(left, right);
        this.convertStringExpressions = false;
        if (CONVERT_STRING_EXPRESSIONS.get() != null) {
            z = true;
        }
        this.convertStringExpressions = z;
    }

    public static BooleanExpression createBetween(Expression value, Expression left, Expression right) {
        return LogicExpression.createAND(createGreaterThanEqual(value, left), createLessThanEqual(value, right));
    }

    public static BooleanExpression createNotBetween(Expression value, Expression left, Expression right) {
        return LogicExpression.createOR(createLessThan(value, left), createGreaterThan(value, right));
    }

    public static BooleanExpression createLike(Expression left, String right, String escape) {
        if (escape == null || escape.length() == 1) {
            int c = -1;
            if (escape != null) {
                c = Message.MAXLENGTH & escape.charAt(0);
            }
            return new LikeExpression(left, right, c);
        }
        throw new RuntimeException("The ESCAPE string litteral is invalid.  It can only be one character.  Litteral used: " + escape);
    }

    public static BooleanExpression createNotLike(Expression left, String right, String escape) {
        return UnaryExpression.createNOT(createLike(left, right, escape));
    }

    public static BooleanExpression createInFilter(Expression left, List elements) {
        if (left instanceof PropertyExpression) {
            return UnaryExpression.createInExpression((PropertyExpression) left, elements, false);
        }
        throw new RuntimeException("Expected a property for In expression, got: " + left);
    }

    public static BooleanExpression createNotInFilter(Expression left, List elements) {
        if (left instanceof PropertyExpression) {
            return UnaryExpression.createInExpression((PropertyExpression) left, elements, true);
        }
        throw new RuntimeException("Expected a property for In expression, got: " + left);
    }

    public static BooleanExpression createIsNull(Expression left) {
        return doCreateEqual(left, ConstantExpression.NULL);
    }

    public static BooleanExpression createIsNotNull(Expression left) {
        return UnaryExpression.createNOT(doCreateEqual(left, ConstantExpression.NULL));
    }

    public static BooleanExpression createNotEqual(Expression left, Expression right) {
        return UnaryExpression.createNOT(createEqual(left, right));
    }

    public static BooleanExpression createEqual(Expression left, Expression right) {
        checkEqualOperand(left);
        checkEqualOperand(right);
        checkEqualOperandCompatability(left, right);
        return doCreateEqual(left, right);
    }

    private static BooleanExpression doCreateEqual(Expression left, Expression right) {
        return new 1(left, right);
    }

    public static BooleanExpression createGreaterThan(Expression left, Expression right) {
        checkLessThanOperand(left);
        checkLessThanOperand(right);
        return new 2(left, right);
    }

    public static BooleanExpression createGreaterThanEqual(Expression left, Expression right) {
        checkLessThanOperand(left);
        checkLessThanOperand(right);
        return new 3(left, right);
    }

    public static BooleanExpression createLessThan(Expression left, Expression right) {
        checkLessThanOperand(left);
        checkLessThanOperand(right);
        return new 4(left, right);
    }

    public static BooleanExpression createLessThanEqual(Expression left, Expression right) {
        checkLessThanOperand(left);
        checkLessThanOperand(right);
        return new 5(left, right);
    }

    public static void checkLessThanOperand(Expression expr) {
        if (expr instanceof ConstantExpression) {
            if (!(((ConstantExpression) expr).getValue() instanceof Number)) {
                throw new RuntimeException("Value '" + expr + "' cannot be compared.");
            }
        } else if (expr instanceof BooleanExpression) {
            throw new RuntimeException("Value '" + expr + "' cannot be compared.");
        }
    }

    public static void checkEqualOperand(Expression expr) {
        if ((expr instanceof ConstantExpression) && ((ConstantExpression) expr).getValue() == null) {
            throw new RuntimeException("'" + expr + "' cannot be compared.");
        }
    }

    private static void checkEqualOperandCompatability(Expression left, Expression right) {
        if ((left instanceof ConstantExpression) && (right instanceof ConstantExpression) && (left instanceof BooleanExpression) && !(right instanceof BooleanExpression)) {
            throw new RuntimeException("'" + left + "' cannot be compared with '" + right + "'");
        }
    }

    public Object evaluate(MessageEvaluationContext message) throws JMSException {
        Comparable<Comparable> lv = (Comparable) this.left.evaluate(message);
        if (lv == null) {
            return null;
        }
        Comparable rv = (Comparable) this.right.evaluate(message);
        if (rv != null) {
            return compare(lv, rv);
        }
        return null;
    }

    protected Boolean compare(Comparable lv, Comparable rv) {
        Class<? extends Comparable> lc = lv.getClass();
        Class<? extends Comparable> rc = rv.getClass();
        if (lc != rc) {
            try {
                if (lc == Boolean.class) {
                    if (!this.convertStringExpressions || rc != String.class) {
                        return Boolean.FALSE;
                    }
                    lv = Boolean.valueOf(Boolean.valueOf((String) lv).booleanValue());
                } else if (lc == Byte.class) {
                    if (rc == Short.class) {
                        lv = Short.valueOf(((Number) lv).shortValue());
                    } else if (rc == Integer.class) {
                        lv = Integer.valueOf(((Number) lv).intValue());
                    } else if (rc == Long.class) {
                        lv = Long.valueOf(((Number) lv).longValue());
                    } else if (rc == Float.class) {
                        lv = new Float(((Number) lv).floatValue());
                    } else if (rc == Double.class) {
                        lv = new Double(((Number) lv).doubleValue());
                    } else if (!this.convertStringExpressions || rc != String.class) {
                        return Boolean.FALSE;
                    } else {
                        rv = Byte.valueOf((String) rv);
                    }
                } else if (lc == Short.class) {
                    if (rc == Integer.class) {
                        lv = Integer.valueOf(((Number) lv).intValue());
                    } else if (rc == Long.class) {
                        lv = Long.valueOf(((Number) lv).longValue());
                    } else if (rc == Float.class) {
                        lv = new Float(((Number) lv).floatValue());
                    } else if (rc == Double.class) {
                        lv = new Double(((Number) lv).doubleValue());
                    } else if (!this.convertStringExpressions || rc != String.class) {
                        return Boolean.FALSE;
                    } else {
                        rv = Short.valueOf((String) rv);
                    }
                } else if (lc == Integer.class) {
                    if (rc == Long.class) {
                        lv = Long.valueOf(((Number) lv).longValue());
                    } else if (rc == Float.class) {
                        lv = new Float(((Number) lv).floatValue());
                    } else if (rc == Double.class) {
                        lv = new Double(((Number) lv).doubleValue());
                    } else if (!this.convertStringExpressions || rc != String.class) {
                        return Boolean.FALSE;
                    } else {
                        rv = Integer.valueOf((String) rv);
                    }
                } else if (lc == Long.class) {
                    if (rc == Integer.class) {
                        rv = Long.valueOf(((Number) rv).longValue());
                    } else if (rc == Float.class) {
                        lv = new Float(((Number) lv).floatValue());
                    } else if (rc == Double.class) {
                        lv = new Double(((Number) lv).doubleValue());
                    } else if (!this.convertStringExpressions || rc != String.class) {
                        return Boolean.FALSE;
                    } else {
                        rv = Long.valueOf((String) rv);
                    }
                } else if (lc == Float.class) {
                    if (rc == Integer.class) {
                        rv = new Float(((Number) rv).floatValue());
                    } else if (rc == Long.class) {
                        rv = new Float(((Number) rv).floatValue());
                    } else if (rc == Double.class) {
                        lv = new Double(((Number) lv).doubleValue());
                    } else if (!this.convertStringExpressions || rc != String.class) {
                        return Boolean.FALSE;
                    } else {
                        rv = Float.valueOf((String) rv);
                    }
                } else if (lc == Double.class) {
                    if (rc == Integer.class) {
                        rv = new Double(((Number) rv).doubleValue());
                    } else if (rc == Long.class) {
                        rv = new Double(((Number) rv).doubleValue());
                    } else if (rc == Float.class) {
                        rv = new Float(((Number) rv).doubleValue());
                    } else if (!this.convertStringExpressions || rc != String.class) {
                        return Boolean.FALSE;
                    } else {
                        rv = Double.valueOf((String) rv);
                    }
                } else if (!this.convertStringExpressions || lc != String.class) {
                    return Boolean.FALSE;
                } else {
                    if (rc == Boolean.class) {
                        lv = Boolean.valueOf((String) lv);
                    } else if (rc == Byte.class) {
                        lv = Byte.valueOf((String) lv);
                    } else if (rc == Short.class) {
                        lv = Short.valueOf((String) lv);
                    } else if (rc == Integer.class) {
                        lv = Integer.valueOf((String) lv);
                    } else if (rc == Long.class) {
                        lv = Long.valueOf((String) lv);
                    } else if (rc == Float.class) {
                        lv = Float.valueOf((String) lv);
                    } else if (rc != Double.class) {
                        return Boolean.FALSE;
                    } else {
                        lv = Double.valueOf((String) lv);
                    }
                }
            } catch (NumberFormatException e) {
                return Boolean.FALSE;
            }
        }
        if (asBoolean(lv.compareTo(rv))) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public boolean matches(MessageEvaluationContext message) throws JMSException {
        Boolean object = evaluate(message);
        return object != null && object == Boolean.TRUE;
    }
}
