package org.apache.activemq.transport.stomp;

public class StompFrameError extends StompFrame {
    private final ProtocolException exception;

    public StompFrameError(ProtocolException exception) {
        this.exception = exception;
    }

    public ProtocolException getException() {
        return this.exception;
    }
}
