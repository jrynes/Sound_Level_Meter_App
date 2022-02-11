package org.apache.activemq.transport.tcp;

public class ExceededMaximumConnectionsException extends Exception {
    private static final long serialVersionUID = -1166885550766355524L;

    public ExceededMaximumConnectionsException(String message) {
        super(message);
    }
}
