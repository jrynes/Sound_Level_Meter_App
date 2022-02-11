package org.apache.activemq;

import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.jms.IllegalStateException;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.selector.SelectorParser;

public class ActiveMQQueueBrowser implements QueueBrowser, Enumeration {
    private final AtomicBoolean browseDone;
    private boolean closed;
    private ActiveMQMessageConsumer consumer;
    private final ConsumerId consumerId;
    private final ActiveMQDestination destination;
    private final boolean dispatchAsync;
    private final String selector;
    private Object semaphore;
    private final ActiveMQSession session;

    class 1 extends ActiveMQMessageConsumer {
        1(ActiveMQSession x0, ConsumerId x1, ActiveMQDestination x2, String x3, String x4, int x5, int x6, boolean x7, boolean x8, boolean x9, MessageListener x10) {
            super(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10);
        }

        public void dispatch(MessageDispatch md) {
            if (md.getMessage() == null) {
                ActiveMQQueueBrowser.this.browseDone.set(true);
            } else {
                super.dispatch(md);
            }
            ActiveMQQueueBrowser.this.notifyMessageAvailable();
        }
    }

    protected ActiveMQQueueBrowser(ActiveMQSession session, ConsumerId consumerId, ActiveMQDestination destination, String selector, boolean dispatchAsync) throws JMSException {
        this.browseDone = new AtomicBoolean(true);
        this.semaphore = new Object();
        if (destination == null) {
            throw new InvalidDestinationException("Don't understand null destinations");
        } else if (destination.getPhysicalName() == null) {
            throw new InvalidDestinationException("The destination object was not given a physical name.");
        } else {
            if (!(selector == null || selector.trim().length() == 0)) {
                SelectorParser.parse(selector);
            }
            this.session = session;
            this.consumerId = consumerId;
            this.destination = destination;
            this.selector = selector;
            this.dispatchAsync = dispatchAsync;
        }
    }

    private ActiveMQMessageConsumer createConsumer() throws JMSException {
        this.browseDone.set(false);
        ActiveMQPrefetchPolicy prefetchPolicy = this.session.connection.getPrefetchPolicy();
        return new 1(this.session, this.consumerId, this.destination, null, this.selector, prefetchPolicy.getQueueBrowserPrefetch(), prefetchPolicy.getMaximumPendingMessageLimit(), false, true, this.dispatchAsync, null);
    }

    private void destroyConsumer() {
        if (this.consumer != null) {
            try {
                if (this.session.getTransacted() && this.session.getTransactionContext().isInLocalTransaction()) {
                    this.session.commit();
                }
                this.consumer.close();
                this.consumer = null;
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public Enumeration getEnumeration() throws JMSException {
        checkClosed();
        if (this.consumer == null) {
            this.consumer = createConsumer();
        }
        return this;
    }

    private void checkClosed() throws IllegalStateException {
        if (this.closed) {
            throw new IllegalStateException("The Consumer is closed");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasMoreElements() {
        /*
        r2 = this;
        r0 = 0;
    L_0x0001:
        monitor-enter(r2);
        r1 = r2.consumer;	 Catch:{ all -> 0x0013 }
        if (r1 != 0) goto L_0x0008;
    L_0x0006:
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
    L_0x0007:
        return r0;
    L_0x0008:
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
        r1 = r2.consumer;
        r1 = r1.getMessageSize();
        if (r1 <= 0) goto L_0x0016;
    L_0x0011:
        r0 = 1;
        goto L_0x0007;
    L_0x0013:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
        throw r0;
    L_0x0016:
        r1 = r2.browseDone;
        r1 = r1.get();
        if (r1 != 0) goto L_0x0026;
    L_0x001e:
        r1 = r2.session;
        r1 = r1.isRunning();
        if (r1 != 0) goto L_0x002a;
    L_0x0026:
        r2.destroyConsumer();
        goto L_0x0007;
    L_0x002a:
        r2.waitForMessage();
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.ActiveMQQueueBrowser.hasMoreElements():boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object nextElement() {
        /*
        r4 = this;
        r2 = 0;
    L_0x0001:
        monitor-enter(r4);
        r3 = r4.consumer;	 Catch:{ all -> 0x0027 }
        if (r3 != 0) goto L_0x0009;
    L_0x0006:
        monitor-exit(r4);	 Catch:{ all -> 0x0027 }
        r0 = r2;
    L_0x0008:
        return r0;
    L_0x0009:
        monitor-exit(r4);	 Catch:{ all -> 0x0027 }
        r3 = r4.consumer;	 Catch:{ JMSException -> 0x002a }
        r0 = r3.receiveNoWait();	 Catch:{ JMSException -> 0x002a }
        if (r0 != 0) goto L_0x0008;
    L_0x0012:
        r3 = r4.browseDone;
        r3 = r3.get();
        if (r3 != 0) goto L_0x0022;
    L_0x001a:
        r3 = r4.session;
        r3 = r3.isRunning();
        if (r3 != 0) goto L_0x0034;
    L_0x0022:
        r4.destroyConsumer();
        r0 = r2;
        goto L_0x0008;
    L_0x0027:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0027 }
        throw r2;
    L_0x002a:
        r1 = move-exception;
        r3 = r4.session;
        r3 = r3.connection;
        r3.onClientInternalException(r1);
        r0 = r2;
        goto L_0x0008;
    L_0x0034:
        r4.waitForMessage();
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.ActiveMQQueueBrowser.nextElement():java.lang.Object");
    }

    public synchronized void close() throws JMSException {
        this.browseDone.set(true);
        destroyConsumer();
        this.closed = true;
    }

    public Queue getQueue() throws JMSException {
        return (Queue) this.destination;
    }

    public String getMessageSelector() throws JMSException {
        return this.selector;
    }

    protected void waitForMessage() {
        try {
            this.consumer.sendPullCommand(-1);
            synchronized (this.semaphore) {
                this.semaphore.wait(2000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (JMSException e2) {
        }
    }

    protected void notifyMessageAvailable() {
        synchronized (this.semaphore) {
            this.semaphore.notifyAll();
        }
    }

    public String toString() {
        return "ActiveMQQueueBrowser { value=" + this.consumerId + " }";
    }
}
