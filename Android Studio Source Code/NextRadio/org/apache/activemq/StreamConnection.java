package org.apache.activemq;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Topic;

public interface StreamConnection extends Connection {
    InputStream createDurableInputStream(Topic topic, String str) throws JMSException;

    InputStream createDurableInputStream(Topic topic, String str, String str2) throws JMSException;

    InputStream createDurableInputStream(Topic topic, String str, String str2, boolean z) throws JMSException;

    InputStream createDurableInputStream(Topic topic, String str, String str2, boolean z, long j) throws JMSException;

    InputStream createInputStream(Destination destination) throws JMSException;

    InputStream createInputStream(Destination destination, String str) throws JMSException;

    InputStream createInputStream(Destination destination, String str, boolean z) throws JMSException;

    InputStream createInputStream(Destination destination, String str, boolean z, long j) throws JMSException;

    OutputStream createOutputStream(Destination destination) throws JMSException;

    OutputStream createOutputStream(Destination destination, Map<String, Object> map, int i, int i2, long j) throws JMSException;

    void unsubscribe(String str) throws JMSException;
}
