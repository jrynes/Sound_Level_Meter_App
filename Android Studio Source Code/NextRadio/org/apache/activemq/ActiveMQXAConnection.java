package org.apache.activemq;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.XAConnection;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueSession;
import javax.jms.XASession;
import javax.jms.XATopicConnection;
import javax.jms.XATopicSession;
import org.apache.activemq.management.JMSStatsImpl;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.util.IdGenerator;

public class ActiveMQXAConnection extends ActiveMQConnection implements XATopicConnection, XAQueueConnection, XAConnection {
    protected ActiveMQXAConnection(Transport transport, IdGenerator clientIdGenerator, IdGenerator connectionIdGenerator, JMSStatsImpl factoryStats) throws Exception {
        super(transport, clientIdGenerator, connectionIdGenerator, factoryStats);
    }

    public XASession createXASession() throws JMSException {
        return (XASession) createSession(true, 0);
    }

    public XATopicSession createXATopicSession() throws JMSException {
        return (XATopicSession) createSession(true, 0);
    }

    public XAQueueSession createXAQueueSession() throws JMSException {
        return (XAQueueSession) createSession(true, 0);
    }

    public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
        checkClosedOrFailed();
        ensureConnectionInfoSent();
        return new ActiveMQXASession(this, getNextSessionId(), 0, isDispatchAsync());
    }
}
