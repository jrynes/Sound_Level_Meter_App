package com.rabbitmq.client;

public class UnexpectedMethodError extends Error {
    private static final long serialVersionUID = 1;
    private final Method _method;

    public UnexpectedMethodError(Method method) {
        this._method = method;
    }

    public String toString() {
        return super.toString() + ": " + this._method;
    }

    public Method getMethod() {
        return this._method;
    }
}
