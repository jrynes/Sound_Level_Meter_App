package org.androidannotations.api;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.transport.stomp.Stomp;

public class BackgroundExecutor {
    public static Executor DEFAULT_EXECUTOR = null;
    private static final String TAG = "BackgroundExecutor";
    private static Executor executor;
    private static final List<Task> tasks;

    public static abstract class Task implements Runnable {
        private boolean executionAsked;
        private Future<?> future;
        private String id;
        private AtomicBoolean managed;
        private int remainingDelay;
        private String serial;
        private long targetTimeMillis;

        public abstract void execute();

        public Task(String id, int delay, String serial) {
            this.managed = new AtomicBoolean();
            if (!Stomp.EMPTY.equals(id)) {
                this.id = id;
            }
            if (delay > 0) {
                this.remainingDelay = delay;
                this.targetTimeMillis = System.currentTimeMillis() + ((long) delay);
            }
            if (!Stomp.EMPTY.equals(serial)) {
                this.serial = serial;
            }
        }

        public void run() {
            if (!this.managed.getAndSet(true)) {
                try {
                    execute();
                } finally {
                    postExecute();
                }
            }
        }

        private void postExecute() {
            if (this.id != null || this.serial != null) {
                synchronized (BackgroundExecutor.class) {
                    BackgroundExecutor.tasks.remove(this);
                    if (this.serial != null) {
                        Task next = BackgroundExecutor.take(this.serial);
                        if (next != null) {
                            if (next.remainingDelay != 0) {
                                next.remainingDelay = Math.max(0, (int) (this.targetTimeMillis - System.currentTimeMillis()));
                            }
                            BackgroundExecutor.execute(next);
                        }
                    }
                }
            }
        }
    }

    static {
        DEFAULT_EXECUTOR = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        executor = DEFAULT_EXECUTOR;
        tasks = new ArrayList();
    }

    private static Future<?> directExecute(Runnable runnable, int delay) {
        if (delay > 0) {
            if (executor instanceof ScheduledExecutorService) {
                return executor.schedule(runnable, (long) delay, TimeUnit.MILLISECONDS);
            }
            throw new IllegalArgumentException("The executor set does not support scheduling");
        } else if (executor instanceof ExecutorService) {
            return executor.submit(runnable);
        } else {
            executor.execute(runnable);
            return null;
        }
    }

    public static synchronized void execute(Task task) {
        synchronized (BackgroundExecutor.class) {
            Future<?> future = null;
            if (task.serial == null || !hasSerialRunning(task.serial)) {
                task.executionAsked = true;
                future = directExecute(task, task.remainingDelay);
            }
            if (!(task.id == null && task.serial == null)) {
                task.future = future;
                tasks.add(task);
            }
        }
    }

    public static void execute(Runnable runnable, String id, int delay, String serial) {
        execute(new 1(id, delay, serial, runnable));
    }

    public static void execute(Runnable runnable, int delay) {
        directExecute(runnable, delay);
    }

    public static void execute(Runnable runnable) {
        directExecute(runnable, 0);
    }

    public static void execute(Runnable runnable, String id, String serial) {
        execute(runnable, id, 0, serial);
    }

    public static void setExecutor(Executor executor) {
        executor = executor;
    }

    public static synchronized void cancelAll(String id, boolean mayInterruptIfRunning) {
        synchronized (BackgroundExecutor.class) {
            for (int i = tasks.size() - 1; i >= 0; i--) {
                Task task = (Task) tasks.get(i);
                if (id.equals(task.id)) {
                    if (task.future != null) {
                        task.future.cancel(mayInterruptIfRunning);
                        if (!task.managed.getAndSet(true)) {
                            task.postExecute();
                        }
                    } else if (task.executionAsked) {
                        Log.w(TAG, "A task with id " + task.id + " cannot be cancelled (the executor set does not support it)");
                    } else {
                        tasks.remove(i);
                    }
                }
            }
        }
    }

    private static boolean hasSerialRunning(String serial) {
        for (Task task : tasks) {
            if (task.executionAsked && serial.equals(task.serial)) {
                return true;
            }
        }
        return false;
    }

    private static Task take(String serial) {
        int len = tasks.size();
        for (int i = 0; i < len; i++) {
            if (serial.equals(((Task) tasks.get(i)).serial)) {
                return (Task) tasks.remove(i);
            }
        }
        return null;
    }
}
