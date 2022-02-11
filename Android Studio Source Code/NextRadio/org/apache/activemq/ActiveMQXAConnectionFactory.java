package org.apache.activemq;

import java.net.URI;
import javax.jms.JMSException;
import javax.jms.XAConnection;
import javax.jms.XAConnectionFactory;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueConnectionFactory;
import javax.jms.XATopicConnection;
import javax.jms.XATopicConnectionFactory;
import org.apache.activemq.management.JMSStatsImpl;
import org.apache.activemq.transport.Transport;

public class ActiveMQXAConnectionFactory extends ActiveMQConnectionFactory implements XAConnectionFactory, XAQueueConnectionFactory, XATopicConnectionFactory {
    public ActiveMQXAConnectionFactory(String userName, String password, String brokerURL) {
        super(userName, password, brokerURL);
    }

    public ActiveMQXAConnectionFactory(String userName, String password, URI brokerURL) {
        super(userName, password, brokerURL);
    }

    public ActiveMQXAConnectionFactory(String brokerURL) {
        super(brokerURL);
    }

    public ActiveMQXAConnectionFactory(URI brokerURL) {
        super(brokerURL);
    }

    public XAConnection createXAConnection() throws JMSException {
        return (XAConnection) createActiveMQConnection();
    }

    public XAConnection createXAConnection(String userName, String password) throws JMSException {
        return (XAConnection) createActiveMQConnection(userName, password);
    }

    public XAQueueConnection createXAQueueConnection() throws JMSException {
        return (XAQueueConnection) createActiveMQConnection();
    }

    public XAQueueConnection createXAQueueConnection(String userName, String password) throws JMSException {
        return (XAQueueConnection) createActiveMQConnection(userName, password);
    }

    public XATopicConnection createXATopicConnection() throws JMSException {
        return (XATopicConnection) createActiveMQConnection();
    }

    public XATopicConnection createXATopicConnection(String userName, String password) throws JMSException {
        return (XATopicConnection) createActiveMQConnection(userName, password);
    }

    protected ActiveMQConnection createActiveMQConnection(Transport transport, JMSStatsImpl stats) throws Exception {
        return new ActiveMQXAConnection(transport, getClientIdGenerator(), getConnectionIdGenerator(), stats);
    }
}
