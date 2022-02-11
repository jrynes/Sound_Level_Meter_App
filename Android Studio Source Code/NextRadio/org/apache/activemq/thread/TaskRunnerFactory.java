package org.apache.activemq.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskRunnerFactory implements Executor {
    private static final Logger LOG;
    private boolean daemon;
    private boolean dedicatedTaskRunner;
    private ExecutorService executor;
    private AtomicLong id;
    private AtomicBoolean initDone;
    private int maxIterationsPerRun;
    private int maxThreadPoolSize;
    private String name;
    private int priority;
    private RejectedExecutionHandler rejectedTaskHandler;
    private long shutdownAwaitTermination;

    class 1 implements ThreadFactory {
        1() {
        }

        public Thread newThread(Runnable runnable) {
            String threadName = TaskRunnerFactory.this.name + "-" + TaskRunnerFactory.this.id.incrementAndGet();
            Thread thread = new Thread(runnable, threadName);
            thread.setDaemon(TaskRunnerFactory.this.daemon);
            thread.setPriority(TaskRunnerFactory.this.priority);
            TaskRunnerFactory.LOG.trace("Created thread[{}]: {}", threadName, thread);
            return thread;
        }
    }

    static {
        LOG = LoggerFactory.getLogger(TaskRunnerFactory.class);
    }

    public TaskRunnerFactory() {
        this("ActiveMQ Task");
    }

    public TaskRunnerFactory(String name) {
        this(name, 5, true, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH);
    }

    private TaskRunnerFactory(String name, int priority, boolean daemon, int maxIterationsPerRun) {
        this(name, priority, daemon, maxIterationsPerRun, false);
    }

    public TaskRunnerFactory(String name, int priority, boolean daemon, int maxIterationsPerRun, boolean dedicatedTaskRunner) {
        this(name, priority, daemon, maxIterationsPerRun, dedicatedTaskRunner, Integer.MAX_VALUE);
    }

    public TaskRunnerFactory(String name, int priority, boolean daemon, int maxIterationsPerRun, boolean dedicatedTaskRunner, int maxThreadPoolSize) {
        this.id = new AtomicLong(0);
        this.shutdownAwaitTermination = 30000;
        this.initDone = new AtomicBoolean(false);
        this.maxThreadPoolSize = Integer.MAX_VALUE;
        this.rejectedTaskHandler = null;
        this.name = name;
        this.priority = priority;
        this.daemon = daemon;
        this.maxIterationsPerRun = maxIterationsPerRun;
        this.dedicatedTaskRunner = dedicatedTaskRunner;
        this.maxThreadPoolSize = maxThreadPoolSize;
    }

    public void init() {
        if (this.initDone.compareAndSet(false, true)) {
            if (this.dedicatedTaskRunner || Stomp.TRUE.equalsIgnoreCase(System.getProperty("org.apache.activemq.UseDedicatedTaskRunner"))) {
                this.executor = null;
            } else if (this.executor == null) {
                this.executor = createDefaultExecutor();
            }
            LOG.debug("Initialized TaskRunnerFactory[{}] using ExecutorService: {}", this.name, this.executor);
        }
    }

    public void shutdown() {
        if (this.executor != null) {
            ThreadPoolUtils.shutdown(this.executor);
            this.executor = null;
        }
        this.initDone.set(false);
    }

    public void shutdownNow() {
        if (this.executor != null) {
            ThreadPoolUtils.shutdownNow(this.executor);
            this.executor = null;
        }
        this.initDone.set(false);
    }

    public void shutdownGraceful() {
        if (this.executor != null) {
            ThreadPoolUtils.shutdownGraceful(this.executor, this.shutdownAwaitTermination);
            this.executor = null;
        }
        this.initDone.set(false);
    }

    public TaskRunner createTaskRunner(Task task, String name) {
        init();
        if (this.executor != null) {
            return new PooledTaskRunner(this.executor, task, this.maxIterationsPerRun);
        }
        return new DedicatedTaskRunner(task, name, this.priority, this.daemon);
    }

    public void execute(Runnable runnable) {
        execute(runnable, this.name);
    }

    public void execute(Runnable runnable, String name) {
        init();
        LOG.trace("Execute[{}] runnable: {}", name, runnable);
        if (this.executor != null) {
            this.executor.execute(runnable);
        } else {
            doExecuteNewThread(runnable, name);
        }
    }

    private void doExecuteNewThread(Runnable runnable, String name) {
        String threadName = name + "-" + this.id.incrementAndGet();
        Thread thread = new Thread(runnable, threadName);
        thread.setDaemon(this.daemon);
        LOG.trace("Created and running thread[{}]: {}", threadName, thread);
        thread.start();
    }

    protected ExecutorService createDefaultExecutor() {
        ThreadPoolExecutor rc = new ThreadPoolExecutor(0, getMaxThreadPoolSize(), 30, TimeUnit.SECONDS, new SynchronousQueue(), new 1());
        if (this.rejectedTaskHandler != null) {
            rc.setRejectedExecutionHandler(this.rejectedTaskHandler);
        }
        return rc;
    }

    public ExecutorService getExecutor() {
        return this.executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public int getMaxIterationsPerRun() {
        return this.maxIterationsPerRun;
    }

    public void setMaxIterationsPerRun(int maxIterationsPerRun) {
        this.maxIterationsPerRun = maxIterationsPerRun;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isDaemon() {
        return this.daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public boolean isDedicatedTaskRunner() {
        return this.dedicatedTaskRunner;
    }

    public void setDedicatedTaskRunner(boolean dedicatedTaskRunner) {
        this.dedicatedTaskRunner = dedicatedTaskRunner;
    }

    public int getMaxThreadPoolSize() {
        return this.maxThreadPoolSize;
    }

    public void setMaxThreadPoolSize(int maxThreadPoolSize) {
        this.maxThreadPoolSize = maxThreadPoolSize;
    }

    public RejectedExecutionHandler getRejectedTaskHandler() {
        return this.rejectedTaskHandler;
    }

    public void setRejectedTaskHandler(RejectedExecutionHandler rejectedTaskHandler) {
        this.rejectedTaskHandler = rejectedTaskHandler;
    }

    public long getShutdownAwaitTermination() {
        return this.shutdownAwaitTermination;
    }

    public void setShutdownAwaitTermination(long shutdownAwaitTermination) {
        this.shutdownAwaitTermination = shutdownAwaitTermination;
    }
}
