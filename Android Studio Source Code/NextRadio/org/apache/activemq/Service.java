package org.apache.activemq;

public interface Service {
    void start() throws Exception;

    void stop() throws Exception;
}
