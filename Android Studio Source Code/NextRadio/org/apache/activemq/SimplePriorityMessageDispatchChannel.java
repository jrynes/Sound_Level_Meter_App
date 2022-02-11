package org.apache.activemq;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.transport.stomp.Stomp;

public class SimplePriorityMessageDispatchChannel implements MessageDispatchChannel {
    private static final Integer MAX_PRIORITY;
    private boolean closed;
    private final LinkedList<MessageDispatch>[] lists;
    private final Object mutex;
    private boolean running;
    private int size;

    static {
        MAX_PRIORITY = Integer.valueOf(10);
    }

    public SimplePriorityMessageDispatchChannel() {
        this.mutex = new Object();
        this.size = 0;
        this.lists = new LinkedList[MAX_PRIORITY.intValue()];
        for (int i = 0; i < MAX_PRIORITY.intValue(); i++) {
            this.lists[i] = new LinkedList();
        }
    }

    public void enqueue(MessageDispatch message) {
        synchronized (this.mutex) {
            getList(message).addLast(message);
            this.size++;
            this.mutex.notify();
        }
    }

    public void enqueueFirst(MessageDispatch message) {
        synchronized (this.mutex) {
            getList(message).addFirst(message);
            this.size++;
            this.mutex.notify();
        }
    }

    public boolean isEmpty() {
        return this.size == 0;
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
        if (r0 == 0) goto L_0x002b;
    L_0x0009:
        r0 = r5.closed;	 Catch:{ all -> 0x0023 }
        if (r0 != 0) goto L_0x002b;
    L_0x000d:
        r0 = r5.isEmpty();	 Catch:{ all -> 0x0023 }
        if (r0 != 0) goto L_0x0017;
    L_0x0013:
        r0 = r5.running;	 Catch:{ all -> 0x0023 }
        if (r0 != 0) goto L_0x002b;
    L_0x0017:
        r2 = -1;
        r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
        if (r0 != 0) goto L_0x0026;
    L_0x001d:
        r0 = r5.mutex;	 Catch:{ all -> 0x0023 }
        r0.wait();	 Catch:{ all -> 0x0023 }
        goto L_0x0003;
    L_0x0023:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        throw r0;
    L_0x0026:
        r0 = r5.mutex;	 Catch:{ all -> 0x0023 }
        r0.wait(r6);	 Catch:{ all -> 0x0023 }
    L_0x002b:
        r0 = r5.closed;	 Catch:{ all -> 0x0023 }
        if (r0 != 0) goto L_0x0039;
    L_0x002f:
        r0 = r5.running;	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x0039;
    L_0x0033:
        r0 = r5.isEmpty();	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x003c;
    L_0x0039:
        r0 = 0;
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
    L_0x003b:
        return r0;
    L_0x003c:
        r0 = r5.removeFirst();	 Catch:{ all -> 0x0023 }
        monitor-exit(r1);	 Catch:{ all -> 0x0023 }
        goto L_0x003b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.SimplePriorityMessageDispatchChannel.dequeue(long):org.apache.activemq.command.MessageDispatch");
    }

    public MessageDispatch dequeueNoWait() {
        MessageDispatch messageDispatch;
        synchronized (this.mutex) {
            if (this.closed || !this.running || isEmpty()) {
                messageDispatch = null;
            } else {
                messageDispatch = removeFirst();
            }
        }
        return messageDispatch;
    }

    public MessageDispatch peek() {
        MessageDispatch messageDispatch;
        synchronized (this.mutex) {
            if (this.closed || !this.running || isEmpty()) {
                messageDispatch = null;
            } else {
                messageDispatch = getFirst();
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clear() {
        /*
        r3 = this;
        r2 = r3.mutex;
        monitor-enter(r2);
        r0 = 0;
    L_0x0004:
        r1 = MAX_PRIORITY;	 Catch:{ all -> 0x0018 }
        r1 = r1.intValue();	 Catch:{ all -> 0x0018 }
        if (r0 >= r1) goto L_0x0016;
    L_0x000c:
        r1 = r3.lists;	 Catch:{ all -> 0x0018 }
        r1 = r1[r0];	 Catch:{ all -> 0x0018 }
        r1.clear();	 Catch:{ all -> 0x0018 }
        r0 = r0 + 1;
        goto L_0x0004;
    L_0x0016:
        monitor-exit(r2);	 Catch:{ all -> 0x0018 }
        return;
    L_0x0018:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0018 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.SimplePriorityMessageDispatchChannel.clear():void");
    }

    public boolean isClosed() {
        return this.closed;
    }

    public int size() {
        int i;
        synchronized (this.mutex) {
            i = this.size;
        }
        return i;
    }

    public Object getMutex() {
        return this.mutex;
    }

    public boolean isRunning() {
        return this.running;
    }

    public List<MessageDispatch> removeAll() {
        ArrayList<MessageDispatch> result;
        synchronized (this.mutex) {
            result = new ArrayList(size());
            for (int i = MAX_PRIORITY.intValue() - 1; i >= 0; i--) {
                List<MessageDispatch> list = this.lists[i];
                result.addAll(list);
                this.size -= list.size();
                list.clear();
            }
        }
        return result;
    }

    public String toString() {
        String result = Stomp.EMPTY;
        for (int i = MAX_PRIORITY.intValue() - 1; i >= 0; i--) {
            result = result + i + ":{" + this.lists[i].toString() + "}";
        }
        return result;
    }

    protected int getPriority(MessageDispatch message) {
        if (message.getMessage() != null) {
            return Math.min(Math.max(message.getMessage().getPriority(), 0), 9);
        }
        return 4;
    }

    protected LinkedList<MessageDispatch> getList(MessageDispatch md) {
        return this.lists[getPriority(md)];
    }

    private final MessageDispatch removeFirst() {
        if (this.size > 0) {
            int i = MAX_PRIORITY.intValue() - 1;
            while (i >= 0) {
                LinkedList<MessageDispatch> list = this.lists[i];
                if (list.isEmpty()) {
                    i--;
                } else {
                    this.size--;
                    return (MessageDispatch) list.removeFirst();
                }
            }
        }
        return null;
    }

    private final MessageDispatch getFirst() {
        if (this.size > 0) {
            for (int i = MAX_PRIORITY.intValue() - 1; i >= 0; i--) {
                LinkedList<MessageDispatch> list = this.lists[i];
                if (!list.isEmpty()) {
                    return (MessageDispatch) list.getFirst();
                }
            }
        }
        return null;
    }
}
