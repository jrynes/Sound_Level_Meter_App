package org.apache.activemq;

public interface ClientInternalExceptionListener {
    void onException(Throwable th);
}
