package org.apache.activemq.thread;

import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PooledTaskRunner implements TaskRunner {
    private static final Logger LOG;
    private final Executor executor;
    private boolean iterating;
    private final int maxIterationsPerRun;
    private boolean queued;
    private final Runnable runable;
    private volatile Thread runningThread;
    private boolean shutdown;
    private final Task task;

    class 1 implements Runnable {
        final /* synthetic */ Task val$task;

        1(Task task) {
            this.val$task = task;
        }

        public void run() {
            PooledTaskRunner.this.runningThread = Thread.currentThread();
            try {
                PooledTaskRunner.this.runTask();
            } finally {
                PooledTaskRunner.LOG.trace("Run task done: {}", this.val$task);
                PooledTaskRunner.this.runningThread = null;
            }
        }
    }

    static {
        LOG = LoggerFactory.getLogger(PooledTaskRunner.class);
    }

    public PooledTaskRunner(Executor executor, Task task, int maxIterationsPerRun) {
        this.executor = executor;
        this.maxIterationsPerRun = maxIterationsPerRun;
        this.task = task;
        this.runable = new 1(task);
    }

    public void wakeup() throws InterruptedException {
        synchronized (this.runable) {
            if (this.queued || this.shutdown) {
                return;
            }
            this.queued = true;
            if (!this.iterating) {
                this.executor.execute(this.runable);
            }
        }
    }

    public void shutdown(long timeout) throws InterruptedException {
        LOG.trace("Shutdown timeout: {} task: {}", this.task);
        synchronized (this.runable) {
            this.shutdown = true;
            if (this.runningThread != Thread.currentThread() && this.iterating) {
                this.runable.wait(timeout);
            }
        }
    }

    public void shutdown() throws InterruptedException {
        shutdown(0);
    }

    final void runTask() {
        synchronized (this.runable) {
            this.queued = false;
            if (this.shutdown) {
                this.iterating = false;
                this.runable.notifyAll();
                return;
            }
            this.iterating = true;
            boolean done = false;
            int i = 0;
            while (i < this.maxIterationsPerRun) {
                try {
                    LOG.trace("Running task iteration {} - {}", Integer.valueOf(i), this.task);
                    if (!this.task.iterate()) {
                        done = true;
                        break;
                    }
                    i++;
                } catch (Throwable th) {
                    synchronized (this.runable) {
                    }
                    this.iterating = false;
                    this.runable.notifyAll();
                    if (this.shutdown) {
                        this.queued = false;
                        this.runable.notifyAll();
                        return;
                    }
                    if (null == null) {
                        this.queued = true;
                    }
                    if (this.queued) {
                        this.executor.execute(this.runable);
                    }
                }
            }
            synchronized (this.runable) {
                this.iterating = false;
                this.runable.notifyAll();
                if (this.shutdown) {
                    this.queued = false;
                    this.runable.notifyAll();
                    return;
                }
                if (!done) {
                    this.queued = true;
                }
                if (this.queued) {
                    this.executor.execute(this.runable);
                }
            }
        }
    }
}
