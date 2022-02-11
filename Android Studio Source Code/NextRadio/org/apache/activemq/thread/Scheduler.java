package org.apache.activemq.thread;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.activemq.util.ServiceStopper;
import org.apache.activemq.util.ServiceSupport;

public final class Scheduler extends ServiceSupport {
    private final String name;
    private Timer timer;
    private final HashMap<Runnable, TimerTask> timerTasks;

    public Scheduler(String name) {
        this.timerTasks = new HashMap();
        this.name = name;
    }

    public void executePeriodically(Runnable task, long period) {
        TimerTask timerTask = new SchedulerTimerTask(task);
        this.timer.schedule(timerTask, period, period);
        this.timerTasks.put(task, timerTask);
    }

    public synchronized void schedualPeriodically(Runnable task, long period) {
        TimerTask timerTask = new SchedulerTimerTask(task);
        this.timer.schedule(timerTask, period, period);
        this.timerTasks.put(task, timerTask);
    }

    public synchronized void cancel(Runnable task) {
        TimerTask ticket = (TimerTask) this.timerTasks.remove(task);
        if (ticket != null) {
            ticket.cancel();
            this.timer.purge();
        }
    }

    public synchronized void executeAfterDelay(Runnable task, long redeliveryDelay) {
        this.timer.schedule(new SchedulerTimerTask(task), redeliveryDelay);
    }

    public void shutdown() {
        this.timer.cancel();
    }

    protected synchronized void doStart() throws Exception {
        this.timer = new Timer(this.name, true);
    }

    protected synchronized void doStop(ServiceStopper stopper) throws Exception {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    public String getName() {
        return this.name;
    }
}
