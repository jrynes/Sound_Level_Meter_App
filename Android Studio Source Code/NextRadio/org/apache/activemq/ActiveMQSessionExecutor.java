package org.apache.activemq;

import java.util.Iterator;
import java.util.List;
import javax.jms.JMSException;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.thread.Task;
import org.apache.activemq.thread.TaskRunner;
import org.apache.activemq.util.JMSExceptionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQSessionExecutor implements Task {
    private static final Logger LOG;
    private boolean dispatchedBySessionPool;
    private final MessageDispatchChannel messageQueue;
    private final ActiveMQSession session;
    private boolean startedOrWarnedThatNotStarted;
    private volatile TaskRunner taskRunner;

    static {
        LOG = LoggerFactory.getLogger(ActiveMQSessionExecutor.class);
    }

    ActiveMQSessionExecutor(ActiveMQSession session) {
        this.session = session;
        if (this.session.connection == null || !this.session.connection.isMessagePrioritySupported()) {
            this.messageQueue = new FifoMessageDispatchChannel();
        } else {
            this.messageQueue = new SimplePriorityMessageDispatchChannel();
        }
    }

    void setDispatchedBySessionPool(boolean value) {
        this.dispatchedBySessionPool = value;
        wakeup();
    }

    void execute(MessageDispatch message) throws InterruptedException {
        if (!this.startedOrWarnedThatNotStarted) {
            ActiveMQConnection connection = this.session.connection;
            long aboutUnstartedConnectionTimeout = connection.getWarnAboutUnstartedConnectionTimeout();
            if (connection.isStarted() || aboutUnstartedConnectionTimeout < 0) {
                this.startedOrWarnedThatNotStarted = true;
            } else if (System.currentTimeMillis() - connection.getTimeCreated() > aboutUnstartedConnectionTimeout) {
                LOG.warn("Received a message on a connection which is not yet started. Have you forgotten to call Connection.start()? Connection: " + connection + " Received: " + message);
                this.startedOrWarnedThatNotStarted = true;
            }
        }
        if (this.session.isSessionAsyncDispatch() || this.dispatchedBySessionPool) {
            this.messageQueue.enqueue(message);
            wakeup();
            return;
        }
        dispatch(message);
    }

    public void wakeup() {
        if (!this.dispatchedBySessionPool) {
            if (this.session.isSessionAsyncDispatch()) {
                try {
                    TaskRunner taskRunner = this.taskRunner;
                    if (taskRunner == null) {
                        synchronized (this) {
                            if (this.taskRunner == null) {
                                if (isRunning()) {
                                    this.taskRunner = this.session.connection.getSessionTaskRunner().createTaskRunner(this, "ActiveMQ Session: " + this.session.getSessionId());
                                } else {
                                    return;
                                }
                            }
                            taskRunner = this.taskRunner;
                        }
                    }
                    taskRunner.wakeup();
                    return;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            while (iterate()) {
            }
        }
    }

    void executeFirst(MessageDispatch message) {
        this.messageQueue.enqueueFirst(message);
        wakeup();
    }

    public boolean hasUncomsumedMessages() {
        return (this.messageQueue.isClosed() || !this.messageQueue.isRunning() || this.messageQueue.isEmpty()) ? false : true;
    }

    void dispatch(MessageDispatch message) {
        Iterator i$ = this.session.consumers.iterator();
        while (i$.hasNext()) {
            ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer) i$.next();
            if (message.getConsumerId().equals(consumer.getConsumerId())) {
                consumer.dispatch(message);
                return;
            }
        }
    }

    synchronized void start() {
        if (!this.messageQueue.isRunning()) {
            this.messageQueue.start();
            if (hasUncomsumedMessages()) {
                wakeup();
            }
        }
    }

    void stop() throws JMSException {
        try {
            if (this.messageQueue.isRunning()) {
                synchronized (this) {
                    this.messageQueue.stop();
                    if (this.taskRunner != null) {
                        this.taskRunner.shutdown();
                        this.taskRunner = null;
                    }
                }
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw JMSExceptionSupport.create(e);
        }
    }

    boolean isRunning() {
        return this.messageQueue.isRunning();
    }

    void close() {
        this.messageQueue.close();
    }

    void clear() {
        this.messageQueue.clear();
    }

    MessageDispatch dequeueNoWait() {
        return this.messageQueue.dequeueNoWait();
    }

    protected void clearMessagesInProgress() {
        this.messageQueue.clear();
    }

    public boolean isEmpty() {
        return this.messageQueue.isEmpty();
    }

    public boolean iterate() {
        Iterator i$ = this.session.consumers.iterator();
        while (i$.hasNext()) {
            if (((ActiveMQMessageConsumer) i$.next()).iterate()) {
                return true;
            }
        }
        MessageDispatch message = this.messageQueue.dequeueNoWait();
        if (message == null) {
            return false;
        }
        dispatch(message);
        if (this.messageQueue.isEmpty()) {
            return false;
        }
        return true;
    }

    List<MessageDispatch> getUnconsumedMessages() {
        return this.messageQueue.removeAll();
    }
}
