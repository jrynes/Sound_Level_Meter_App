package org.apache.activemq;

import javax.jms.ConnectionConsumer;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.ServerSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.MessageDispatch;

public class ActiveMQConnectionConsumer implements ConnectionConsumer, ActiveMQDispatcher {
    private boolean closed;
    private ActiveMQConnection connection;
    private ConsumerInfo consumerInfo;
    private ServerSessionPool sessionPool;

    protected ActiveMQConnectionConsumer(ActiveMQConnection theConnection, ServerSessionPool theSessionPool, ConsumerInfo theConsumerInfo) throws JMSException {
        this.connection = theConnection;
        this.sessionPool = theSessionPool;
        this.consumerInfo = theConsumerInfo;
        this.connection.addConnectionConsumer(this);
        this.connection.addDispatcher(this.consumerInfo.getConsumerId(), this);
        this.connection.syncSendPacket(this.consumerInfo);
    }

    public ServerSessionPool getServerSessionPool() throws JMSException {
        if (!this.closed) {
            return this.sessionPool;
        }
        throw new IllegalStateException("The Connection Consumer is closed");
    }

    public void close() throws JMSException {
        if (!this.closed) {
            dispose();
            this.connection.asyncSendPacket(this.consumerInfo.createRemoveCommand());
        }
    }

    public void dispose() {
        if (!this.closed) {
            this.connection.removeDispatcher(this.consumerInfo.getConsumerId());
            this.connection.removeConnectionConsumer(this);
            this.closed = true;
        }
    }

    public void dispatch(MessageDispatch messageDispatch) {
        try {
            ActiveMQSession session;
            messageDispatch.setConsumer(this);
            ServerSession serverSession = this.sessionPool.getServerSession();
            Session s = serverSession.getSession();
            if (s instanceof ActiveMQSession) {
                session = (ActiveMQSession) s;
            } else if (s instanceof ActiveMQTopicSession) {
                session = (ActiveMQSession) ((ActiveMQTopicSession) s).getNext();
            } else if (s instanceof ActiveMQQueueSession) {
                session = (ActiveMQSession) ((ActiveMQQueueSession) s).getNext();
            } else {
                this.connection.onClientInternalException(new JMSException("Session pool provided an invalid session type: " + s.getClass()));
                return;
            }
            session.dispatch(messageDispatch);
            serverSession.start();
        } catch (JMSException e) {
            this.connection.onAsyncException(e);
        }
    }

    public String toString() {
        return "ActiveMQConnectionConsumer { value=" + this.consumerInfo.getConsumerId() + " }";
    }

    public void clearMessagesInProgress() {
        this.connection.transportInterruptionProcessingComplete();
    }

    public ConsumerInfo getConsumerInfo() {
        return this.consumerInfo;
    }
}
