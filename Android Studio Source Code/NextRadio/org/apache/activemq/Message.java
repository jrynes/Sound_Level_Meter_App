package org.apache.activemq;

public interface Message extends javax.jms.Message {
    String getJMSXMimeType();
}
