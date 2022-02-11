package org.apache.activemq;

import javax.jms.JMSException;

public interface Closeable {
    void close() throws JMSException;
}
