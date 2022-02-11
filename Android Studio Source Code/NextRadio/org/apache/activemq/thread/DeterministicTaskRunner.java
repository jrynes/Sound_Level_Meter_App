package org.apache.activemq.thread;

import java.util.concurrent.Executor;

public class DeterministicTaskRunner implements TaskRunner {
    private final Executor executor;
    private final Runnable runable;
    private boolean shutdown;
    private final Task task;

    class 1 implements Runnable {
        1() {
        }

        public void run() {
            Thread.currentThread();
            DeterministicTaskRunner.this.runTask();
        }
    }

    public DeterministicTaskRunner(Executor executor, Task task) {
        this.executor = executor;
        this.task = task;
        this.runable = new 1();
    }

    public void wakeup() throws InterruptedException {
        synchronized (this.runable) {
            if (this.shutdown) {
                return;
            }
            this.executor.execute(this.runable);
        }
    }

    public void shutdown(long timeout) throws InterruptedException {
        synchronized (this.runable) {
            this.shutdown = true;
        }
    }

    public void shutdown() throws InterruptedException {
        shutdown(0);
    }

    final void runTask() {
        synchronized (this.runable) {
            if (this.shutdown) {
                this.runable.notifyAll();
                return;
            }
            this.task.iterate();
        }
    }
}
