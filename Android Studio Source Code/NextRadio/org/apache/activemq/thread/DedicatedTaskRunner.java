package org.apache.activemq.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DedicatedTaskRunner implements TaskRunner {
    private static final Logger LOG;
    private final Object mutex;
    private boolean pending;
    private boolean shutdown;
    private final Task task;
    private final Thread thread;
    private boolean threadTerminated;

    class 1 extends Thread {
        final /* synthetic */ Task val$task;

        1(String x0, Task task) {
            this.val$task = task;
            super(x0);
        }

        public void run() {
            try {
                DedicatedTaskRunner.this.runTask();
            } finally {
                DedicatedTaskRunner.LOG.trace("Run task done: {}", this.val$task);
            }
        }
    }

    static {
        LOG = LoggerFactory.getLogger(DedicatedTaskRunner.class);
    }

    public DedicatedTaskRunner(Task task, String name, int priority, boolean daemon) {
        this.mutex = new Object();
        this.task = task;
        this.thread = new 1(name, task);
        this.thread.setDaemon(daemon);
        this.thread.setName(name);
        this.thread.setPriority(priority);
        this.thread.start();
    }

    public void wakeup() throws InterruptedException {
        synchronized (this.mutex) {
            if (this.shutdown) {
                return;
            }
            this.pending = true;
            this.mutex.notifyAll();
        }
    }

    public void shutdown(long timeout) throws InterruptedException {
        LOG.trace("Shutdown timeout: {} task: {}", this.task);
        synchronized (this.mutex) {
            this.shutdown = true;
            this.pending = true;
            this.mutex.notifyAll();
            if (!(Thread.currentThread() == this.thread || this.threadTerminated)) {
                this.mutex.wait(timeout);
            }
        }
    }

    public void shutdown() throws InterruptedException {
        shutdown(0);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final void runTask() {
        /*
        r4 = this;
    L_0x0000:
        r2 = r4.mutex;	 Catch:{ InterruptedException -> 0x0048 }
        monitor-enter(r2);	 Catch:{ InterruptedException -> 0x0048 }
        r1 = 0;
        r4.pending = r1;	 Catch:{ all -> 0x0045 }
        r1 = r4.shutdown;	 Catch:{ all -> 0x0045 }
        if (r1 == 0) goto L_0x001b;
    L_0x000a:
        monitor-exit(r2);	 Catch:{ all -> 0x0045 }
        r2 = r4.mutex;
        monitor-enter(r2);
        r1 = 1;
        r4.threadTerminated = r1;	 Catch:{ all -> 0x0018 }
        r1 = r4.mutex;	 Catch:{ all -> 0x0018 }
        r1.notifyAll();	 Catch:{ all -> 0x0018 }
        monitor-exit(r2);	 Catch:{ all -> 0x0018 }
    L_0x0017:
        return;
    L_0x0018:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0018 }
        throw r1;
    L_0x001b:
        monitor-exit(r2);	 Catch:{ all -> 0x0045 }
        r1 = LOG;	 Catch:{ InterruptedException -> 0x0048 }
        r2 = "Running task {}";
        r3 = r4.task;	 Catch:{ InterruptedException -> 0x0048 }
        r1.trace(r2, r3);	 Catch:{ InterruptedException -> 0x0048 }
        r1 = r4.task;	 Catch:{ InterruptedException -> 0x0048 }
        r1 = r1.iterate();	 Catch:{ InterruptedException -> 0x0048 }
        if (r1 != 0) goto L_0x0000;
    L_0x002d:
        r2 = r4.mutex;	 Catch:{ InterruptedException -> 0x0048 }
        monitor-enter(r2);	 Catch:{ InterruptedException -> 0x0048 }
        r1 = r4.shutdown;	 Catch:{ all -> 0x006a }
        if (r1 == 0) goto L_0x0060;
    L_0x0034:
        monitor-exit(r2);	 Catch:{ all -> 0x006a }
        r2 = r4.mutex;
        monitor-enter(r2);
        r1 = 1;
        r4.threadTerminated = r1;	 Catch:{ all -> 0x0042 }
        r1 = r4.mutex;	 Catch:{ all -> 0x0042 }
        r1.notifyAll();	 Catch:{ all -> 0x0042 }
        monitor-exit(r2);	 Catch:{ all -> 0x0042 }
        goto L_0x0017;
    L_0x0042:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0042 }
        throw r1;
    L_0x0045:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0045 }
        throw r1;	 Catch:{ InterruptedException -> 0x0048 }
    L_0x0048:
        r0 = move-exception;
        r1 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x006d }
        r1.interrupt();	 Catch:{ all -> 0x006d }
        r2 = r4.mutex;
        monitor-enter(r2);
        r1 = 1;
        r4.threadTerminated = r1;	 Catch:{ all -> 0x005d }
        r1 = r4.mutex;	 Catch:{ all -> 0x005d }
        r1.notifyAll();	 Catch:{ all -> 0x005d }
        monitor-exit(r2);	 Catch:{ all -> 0x005d }
        goto L_0x0017;
    L_0x005d:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x005d }
        throw r1;
    L_0x0060:
        r1 = r4.pending;	 Catch:{ all -> 0x006a }
        if (r1 != 0) goto L_0x007b;
    L_0x0064:
        r1 = r4.mutex;	 Catch:{ all -> 0x006a }
        r1.wait();	 Catch:{ all -> 0x006a }
        goto L_0x0060;
    L_0x006a:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x006a }
        throw r1;	 Catch:{ InterruptedException -> 0x0048 }
    L_0x006d:
        r1 = move-exception;
        r2 = r4.mutex;
        monitor-enter(r2);
        r3 = 1;
        r4.threadTerminated = r3;	 Catch:{ all -> 0x007d }
        r3 = r4.mutex;	 Catch:{ all -> 0x007d }
        r3.notifyAll();	 Catch:{ all -> 0x007d }
        monitor-exit(r2);	 Catch:{ all -> 0x007d }
        throw r1;
    L_0x007b:
        monitor-exit(r2);	 Catch:{ all -> 0x006a }
        goto L_0x0000;
    L_0x007d:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x007d }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.thread.DedicatedTaskRunner.runTask():void");
    }
}
