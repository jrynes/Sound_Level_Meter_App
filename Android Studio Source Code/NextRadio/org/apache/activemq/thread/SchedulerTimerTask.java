package org.apache.activemq.thread;

import java.util.TimerTask;

public class SchedulerTimerTask extends TimerTask {
    private final Runnable task;

    public SchedulerTimerTask(Runnable task) {
        this.task = task;
    }

    public void run() {
        this.task.run();
    }
}
