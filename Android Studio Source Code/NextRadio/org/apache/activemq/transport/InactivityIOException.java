package org.apache.activemq.transport;

import java.io.IOException;

public class InactivityIOException extends IOException {
    private static final long serialVersionUID = 5816001466763503220L;

    public InactivityIOException(String message) {
        super(message);
    }
}
