package org.apache.activemq;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.activemq.command.MessageDispatch;

public class FifoMessageDispatchChannel implements MessageDispatchChannel {
    private boolean closed;
    private final LinkedList<MessageDispatch> list;
    private final Object mutex;
    private boolean running;

    public FifoMessageDispatchChannel() {
        this.mutex = new Object();
        this.list = new LinkedList();
    }

    public void enqueue(MessageDispatch message) {
        synchronized (this.mutex) {
            this.list.addLast(message);
            this.mutex.notify();
        }
    }

    public void enqueueFirst(MessageDispatch message) {
        synchronized (this.mutex) {
            this.list.addFirst(message);
            this.mutex.notify();
        }
    }

    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.mutex) {
            isEmpty = this.list.isEmpty();
        }
        return isEmpty;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.activemq.command.MessageDispatch dequeue(long r6) throws java.lang.InterruptedException {
        /*
        r5 = this;
        r1 = r5.mutex;
        monitor-enter(r1);
    L_0x0003:
        r2 = 0;
        r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
        if (r0 == 0) goto L_0x002d;
    L_0x0009:
        r0 = r5.closed;	 Catch:{ all -> 0x0025 }
        if (r0 != 0) goto L_0x002d;
    L_0x000d:
        r0 = r5.list;	 Catch:{ all -> 0x0025 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0025 }
        if (r0 != 0) goto L_0x0019;
    L_0x0015:
        r0 = r5.running;	 Catch:{ all -> 0x0025 }
        if (r0 != 0) goto L_0x002d;
    L_0x0019:
        r2 = -1;
        r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
        if (r0 != 0) goto L_0x0028;
    L_0x001f:
        r0 = r5.mutex;	 Catch:{ all -> 0x0025 }
        r0.wait();	 Catch:{ all -> 0x0025 }
        goto L_0x0003;
    L_0x0025:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0025 }
        throw r0;
    L_0x0028:
        r0 = r5.mutex;	 Catch:{ all -> 0x0025 }
        r0.wait(r6);	 Catch:{ all -> 0x0025 }
    L_0x002d:
        r0 = r5.closed;	 Catch:{ all -> 0x0025 }
        if (r0 != 0) goto L_0x003d;
    L_0x0031:
        r0 = r5.running;	 Catch:{ all -> 0x0025 }
        if (r0 == 0) goto L_0x003d;
    L_0x0035:
        r0 = r5.list;	 Catch:{ all -> 0x0025 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0025 }
        if (r0 == 0) goto L_0x0040;
    L_0x003d:
        r0 = 0;
        monitor-exit(r1);	 Catch:{ all -> 0x0025 }
    L_0x003f:
        return r0;
    L_0x0040:
        r0 = r5.list;	 Catch:{ all -> 0x0025 }
        r0 = r0.removeFirst();	 Catch:{ all -> 0x0025 }
        r0 = (org.apache.activemq.command.MessageDispatch) r0;	 Catch:{ all -> 0x0025 }
        monitor-exit(r1);	 Catch:{ all -> 0x0025 }
        goto L_0x003f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.FifoMessageDispatchChannel.dequeue(long):org.apache.activemq.command.MessageDispatch");
    }

    public MessageDispatch dequeueNoWait() {
        MessageDispatch messageDispatch;
        synchronized (this.mutex) {
            if (this.closed || !this.running || this.list.isEmpty()) {
                messageDispatch = null;
            } else {
                messageDispatch = (MessageDispatch) this.list.removeFirst();
            }
        }
        return messageDispatch;
    }

    public MessageDispatch peek() {
        MessageDispatch messageDispatch;
        synchronized (this.mutex) {
            if (this.closed || !this.running || this.list.isEmpty()) {
                messageDispatch = null;
            } else {
                messageDispatch = (MessageDispatch) this.list.getFirst();
            }
        }
        return messageDispatch;
    }

    public void start() {
        synchronized (this.mutex) {
            this.running = true;
            this.mutex.notifyAll();
        }
    }

    public void stop() {
        synchronized (this.mutex) {
            this.running = false;
            this.mutex.notifyAll();
        }
    }

    public void close() {
        synchronized (this.mutex) {
            if (!this.closed) {
                this.running = false;
                this.closed = true;
            }
            this.mutex.notifyAll();
        }
    }

    public void clear() {
        synchronized (this.mutex) {
            this.list.clear();
        }
    }

    public boolean isClosed() {
        return this.closed;
    }

    public int size() {
        int size;
        synchronized (this.mutex) {
            size = this.list.size();
        }
        return size;
    }

    public Object getMutex() {
        return this.mutex;
    }

    public boolean isRunning() {
        return this.running;
    }

    public List<MessageDispatch> removeAll() {
        ArrayList<MessageDispatch> rc;
        synchronized (this.mutex) {
            rc = new ArrayList(this.list);
            this.list.clear();
        }
        return rc;
    }

    public String toString() {
        String linkedList;
        synchronized (this.mutex) {
            linkedList = this.list.toString();
        }
        return linkedList;
    }
}
