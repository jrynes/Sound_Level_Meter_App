package org.apache.activemq.util;

import java.io.IOException;

public final class IOExceptionSupport {
    private IOExceptionSupport() {
    }

    public static IOException create(String msg, Throwable cause) {
        IOException exception = new IOException(msg);
        exception.initCause(cause);
        return exception;
    }

    public static IOException create(String msg, Exception cause) {
        IOException exception = new IOException(msg);
        exception.initCause(cause);
        return exception;
    }

    public static IOException create(Throwable cause) {
        IOException exception = new IOException(cause.getMessage());
        exception.initCause(cause);
        return exception;
    }

    public static IOException create(Exception cause) {
        IOException exception = new IOException(cause.getMessage());
        exception.initCause(cause);
        return exception;
    }
}
