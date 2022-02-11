package com.rabbitmq.client;

public class AlreadyClosedException extends ShutdownSignalException {
    private static final long serialVersionUID = 1;

    public AlreadyClosedException(String s, Object ref) {
        super(true, true, s, ref);
    }
}
