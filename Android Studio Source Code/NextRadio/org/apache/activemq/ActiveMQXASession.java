package org.apache.activemq;

import javax.jms.JMSException;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TopicSession;
import javax.jms.TransactionInProgressException;
import javax.jms.XAQueueSession;
import javax.jms.XATopicSession;
import javax.transaction.xa.XAResource;
import org.apache.activemq.command.SessionId;

public class ActiveMQXASession extends ActiveMQSession implements QueueSession, TopicSession, XAQueueSession, XATopicSession {
    public ActiveMQXASession(ActiveMQXAConnection connection, SessionId sessionId, int theAcknowlegeMode, boolean dispatchAsync) throws JMSException {
        super(connection, sessionId, theAcknowlegeMode, dispatchAsync);
    }

    public boolean getTransacted() throws JMSException {
        return getTransactionContext().isInXATransaction();
    }

    public void rollback() throws JMSException {
        throw new TransactionInProgressException("Cannot rollback() inside an XASession");
    }

    public void commit() throws JMSException {
        throw new TransactionInProgressException("Cannot commit() inside an XASession");
    }

    public Session getSession() throws JMSException {
        return this;
    }

    public XAResource getXAResource() {
        return getTransactionContext();
    }

    public QueueSession getQueueSession() throws JMSException {
        return new ActiveMQQueueSession(this);
    }

    public TopicSession getTopicSession() throws JMSException {
        return new ActiveMQTopicSession(this);
    }

    public boolean isAutoAcknowledge() {
        return true;
    }

    protected void doStartTransaction() throws JMSException {
    }
}
