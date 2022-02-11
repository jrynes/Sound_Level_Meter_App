package org.apache.activemq.transport;

import java.io.IOException;

public class TransportDisposedIOException extends IOException {
    private static final long serialVersionUID = -7107323414439622596L;

    public TransportDisposedIOException(String message) {
        super(message);
    }
}
