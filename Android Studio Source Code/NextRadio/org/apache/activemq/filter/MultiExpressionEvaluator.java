package org.apache.activemq.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jms.JMSException;

public class MultiExpressionEvaluator {
    Map<Expression, CacheExpression> cachedExpressions;
    Map<String, ExpressionListenerSet> rootExpressions;
    int view;

    public class CacheExpression extends UnaryExpression {
        int cachedHashCode;
        Object cachedValue;
        int cview;
        short refCount;

        public CacheExpression(Expression realExpression) {
            super(realExpression);
            this.cview = MultiExpressionEvaluator.this.view - 1;
            this.cachedHashCode = realExpression.hashCode();
        }

        public Object evaluate(MessageEvaluationContext message) throws JMSException {
            if (MultiExpressionEvaluator.this.view == this.cview) {
                return this.cachedValue;
            }
            this.cachedValue = this.right.evaluate(message);
            this.cview = MultiExpressionEvaluator.this.view;
            return this.cachedValue;
        }

        public int hashCode() {
            return this.cachedHashCode;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            return ((CacheExpression) o).right.equals(this.right);
        }

        public String getExpressionSymbol() {
            return null;
        }

        public String toString() {
            return this.right.toString();
        }
    }

    interface ExpressionListener {
        void evaluateResultEvent(Expression expression, MessageEvaluationContext messageEvaluationContext, Object obj);
    }

    static class ExpressionListenerSet {
        Expression expression;
        List<ExpressionListener> listeners;

        ExpressionListenerSet() {
            this.listeners = new ArrayList();
        }
    }

    public MultiExpressionEvaluator() {
        this.rootExpressions = new HashMap();
        this.cachedExpressions = new HashMap();
    }

    public void addExpressionListner(Expression selector, ExpressionListener c) {
        ExpressionListenerSet data = (ExpressionListenerSet) this.rootExpressions.get(selector.toString());
        if (data == null) {
            data = new ExpressionListenerSet();
            data.expression = addToCache(selector);
            this.rootExpressions.put(selector.toString(), data);
        }
        data.listeners.add(c);
    }

    public boolean removeEventListner(String selector, ExpressionListener c) {
        String expKey = selector;
        ExpressionListenerSet d = (ExpressionListenerSet) this.rootExpressions.get(expKey);
        if (d == null || !d.listeners.remove(c)) {
            return false;
        }
        if (d.listeners.size() == 0) {
            removeFromCache((CacheExpression) d.expression);
            this.rootExpressions.remove(expKey);
        }
        return true;
    }

    private CacheExpression addToCache(Expression expr) {
        CacheExpression n = (CacheExpression) this.cachedExpressions.get(expr);
        if (n == null) {
            n = new CacheExpression(expr);
            this.cachedExpressions.put(expr, n);
            if (expr instanceof UnaryExpression) {
                UnaryExpression un = (UnaryExpression) expr;
                un.setRight(addToCache(un.getRight()));
            } else if (expr instanceof BinaryExpression) {
                BinaryExpression bn = (BinaryExpression) expr;
                bn.setRight(addToCache(bn.getRight()));
                bn.setLeft(addToCache(bn.getLeft()));
            }
        }
        n.refCount = (short) (n.refCount + 1);
        return n;
    }

    private void removeFromCache(CacheExpression cn) {
        cn.refCount = (short) (cn.refCount - 1);
        Expression realExpr = cn.getRight();
        if (cn.refCount == (short) 0) {
            this.cachedExpressions.remove(realExpr);
        }
        if (realExpr instanceof UnaryExpression) {
            removeFromCache((CacheExpression) ((UnaryExpression) realExpr).getRight());
        }
        if (realExpr instanceof BinaryExpression) {
            removeFromCache((CacheExpression) ((BinaryExpression) realExpr).getRight());
        }
    }

    public void evaluate(MessageEvaluationContext message) {
        for (ExpressionListenerSet els : this.rootExpressions.values()) {
            try {
                Object result = els.expression.evaluate(message);
                for (ExpressionListener l : els.listeners) {
                    l.evaluateResultEvent(els.expression, message, result);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
