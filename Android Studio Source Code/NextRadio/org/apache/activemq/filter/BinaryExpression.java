package org.apache.activemq.filter;

public abstract class BinaryExpression implements Expression {
    protected Expression left;
    protected Expression right;

    public abstract String getExpressionSymbol();

    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return this.left;
    }

    public Expression getRight() {
        return this.right;
    }

    public String toString() {
        return "(" + this.left.toString() + " " + getExpressionSymbol() + " " + this.right.toString() + ")";
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

    public void setRight(Expression expression) {
        this.right = expression;
    }

    public void setLeft(Expression expression) {
        this.left = expression;
    }
}
