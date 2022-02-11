package com.rabbitmq.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.transport.stomp.Stomp;

public class MessageProperties {
    public static final BasicProperties BASIC;
    public static final BasicProperties MINIMAL_BASIC;
    public static final BasicProperties MINIMAL_PERSISTENT_BASIC;
    public static final BasicProperties PERSISTENT_BASIC;
    public static final BasicProperties PERSISTENT_TEXT_PLAIN;
    public static final BasicProperties TEXT_PLAIN;

    static {
        MINIMAL_BASIC = new BasicProperties(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        MINIMAL_PERSISTENT_BASIC = new BasicProperties(null, null, null, Integer.valueOf(2), null, null, null, null, null, null, null, null, null, null);
        BASIC = new BasicProperties(ActiveMQBlobMessage.BINARY_MIME_TYPE, null, null, Integer.valueOf(1), Integer.valueOf(0), null, null, null, null, null, null, null, null, null);
        PERSISTENT_BASIC = new BasicProperties(ActiveMQBlobMessage.BINARY_MIME_TYPE, null, null, Integer.valueOf(2), Integer.valueOf(0), null, null, null, null, null, null, null, null, null);
        TEXT_PLAIN = new BasicProperties(Stomp.TEXT_PLAIN, null, null, Integer.valueOf(1), Integer.valueOf(0), null, null, null, null, null, null, null, null, null);
        PERSISTENT_TEXT_PLAIN = new BasicProperties(Stomp.TEXT_PLAIN, null, null, Integer.valueOf(2), Integer.valueOf(0), null, null, null, null, null, null, null, null, null);
    }
}
