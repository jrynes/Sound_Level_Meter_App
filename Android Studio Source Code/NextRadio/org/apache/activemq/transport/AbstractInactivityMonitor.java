package org.apache.activemq.transport;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.activemq.command.KeepAliveInfo;
import org.apache.activemq.command.WireFormatInfo;
import org.apache.activemq.thread.SchedulerTimerTask;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.ThreadPoolUtils;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractInactivityMonitor extends TransportFilter {
    private static ThreadPoolExecutor ASYNC_TASKS;
    private static int CHECKER_COUNTER;
    private static long DEFAULT_CHECK_TIME_MILLS;
    private static final Logger LOG;
    private static Timer READ_CHECK_TIMER;
    private static Timer WRITE_CHECK_TIMER;
    private final AtomicBoolean commandReceived;
    private final AtomicBoolean commandSent;
    private final ThreadFactory factory;
    private final AtomicBoolean failed;
    private final AtomicBoolean inReceive;
    private final AtomicBoolean inSend;
    private long initialDelayTime;
    private boolean keepAliveResponseRequired;
    private final AtomicInteger lastReceiveCounter;
    private final AtomicBoolean monitorStarted;
    private long readCheckTime;
    private final Runnable readChecker;
    private SchedulerTimerTask readCheckerTask;
    private final ReentrantReadWriteLock sendLock;
    private boolean useKeepAlive;
    protected WireFormat wireFormat;
    private long writeCheckTime;
    private final Runnable writeChecker;
    private SchedulerTimerTask writeCheckerTask;

    class 1 implements Runnable {
        long lastRunTime;

        1() {
        }

        public void run() {
            long now = System.currentTimeMillis();
            long elapsed = now - this.lastRunTime;
            if (this.lastRunTime != 0 && AbstractInactivityMonitor.LOG.isDebugEnabled()) {
                AbstractInactivityMonitor.LOG.debug(Stomp.EMPTY + elapsed + " ms elapsed since last read check.");
            }
            if (AbstractInactivityMonitor.this.allowReadCheck(elapsed)) {
                this.lastRunTime = now;
                AbstractInactivityMonitor.this.readCheck();
                return;
            }
            AbstractInactivityMonitor.LOG.debug("Aborting read check.. Not enough time elapsed since last read check.");
        }

        public String toString() {
            return "ReadChecker";
        }
    }

    class 2 implements Runnable {
        long lastRunTime;

        2() {
        }

        public void run() {
            long now = System.currentTimeMillis();
            if (this.lastRunTime != 0 && AbstractInactivityMonitor.LOG.isDebugEnabled()) {
                AbstractInactivityMonitor.LOG.debug(this + " " + (now - this.lastRunTime) + " ms elapsed since last write check.");
            }
            this.lastRunTime = now;
            AbstractInactivityMonitor.this.writeCheck();
        }

        public String toString() {
            return "WriteChecker";
        }
    }

    class 3 implements Runnable {
        3() {
        }

        public void run() {
            if (AbstractInactivityMonitor.LOG.isDebugEnabled()) {
                AbstractInactivityMonitor.LOG.debug("Running {}", this);
            }
            if (AbstractInactivityMonitor.this.monitorStarted.get()) {
                try {
                    if (AbstractInactivityMonitor.this.sendLock.writeLock().tryLock()) {
                        KeepAliveInfo info = new KeepAliveInfo();
                        info.setResponseRequired(AbstractInactivityMonitor.this.keepAliveResponseRequired);
                        AbstractInactivityMonitor.this.doOnewaySend(info);
                    }
                    if (AbstractInactivityMonitor.this.sendLock.writeLock().isHeldByCurrentThread()) {
                        AbstractInactivityMonitor.this.sendLock.writeLock().unlock();
                    }
                } catch (IOException e) {
                    AbstractInactivityMonitor.this.onException(e);
                    if (AbstractInactivityMonitor.this.sendLock.writeLock().isHeldByCurrentThread()) {
                        AbstractInactivityMonitor.this.sendLock.writeLock().unlock();
                    }
                } catch (Throwable th) {
                    if (AbstractInactivityMonitor.this.sendLock.writeLock().isHeldByCurrentThread()) {
                        AbstractInactivityMonitor.this.sendLock.writeLock().unlock();
                    }
                }
            }
        }

        public String toString() {
            return "WriteCheck[" + AbstractInactivityMonitor.this.getRemoteAddress() + "]";
        }
    }

    class 4 implements Runnable {
        4() {
        }

        public void run() {
            if (AbstractInactivityMonitor.LOG.isDebugEnabled()) {
                AbstractInactivityMonitor.LOG.debug("Running {}", this);
            }
            AbstractInactivityMonitor.this.onException(new InactivityIOException("Channel was inactive for too (>" + AbstractInactivityMonitor.this.readCheckTime + ") long: " + AbstractInactivityMonitor.this.next.getRemoteAddress()));
        }

        public String toString() {
            return "ReadCheck[" + AbstractInactivityMonitor.this.getRemoteAddress() + "]";
        }
    }

    class 5 implements ThreadFactory {
        5() {
        }

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "ActiveMQ InactivityMonitor Worker");
            thread.setDaemon(true);
            return thread;
        }
    }

    protected abstract boolean configuredOk() throws IOException;

    protected abstract void processInboundWireFormatInfo(WireFormatInfo wireFormatInfo) throws IOException;

    protected abstract void processOutboundWireFormatInfo(WireFormatInfo wireFormatInfo) throws IOException;

    static {
        LOG = LoggerFactory.getLogger(AbstractInactivityMonitor.class);
        DEFAULT_CHECK_TIME_MILLS = 30000;
    }

    private boolean allowReadCheck(long elapsed) {
        return elapsed > (this.readCheckTime * 9) / 10;
    }

    public AbstractInactivityMonitor(Transport next, WireFormat wireFormat) {
        super(next);
        this.monitorStarted = new AtomicBoolean(false);
        this.commandSent = new AtomicBoolean(false);
        this.inSend = new AtomicBoolean(false);
        this.failed = new AtomicBoolean(false);
        this.commandReceived = new AtomicBoolean(true);
        this.inReceive = new AtomicBoolean(false);
        this.lastReceiveCounter = new AtomicInteger(0);
        this.sendLock = new ReentrantReadWriteLock();
        this.readCheckTime = DEFAULT_CHECK_TIME_MILLS;
        this.writeCheckTime = DEFAULT_CHECK_TIME_MILLS;
        this.initialDelayTime = DEFAULT_CHECK_TIME_MILLS;
        this.useKeepAlive = true;
        this.readChecker = new 1();
        this.writeChecker = new 2();
        this.factory = new 5();
        this.wireFormat = wireFormat;
    }

    public void start() throws Exception {
        this.next.start();
        startMonitorThreads();
    }

    public void stop() throws Exception {
        stopMonitorThreads();
        this.next.stop();
    }

    final void writeCheck() {
        if (!this.inSend.get()) {
            if (!this.commandSent.get() && this.useKeepAlive && this.monitorStarted.get() && !ASYNC_TASKS.isTerminating() && !ASYNC_TASKS.isTerminated()) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace(this + " no message sent since last write check, sending a KeepAliveInfo");
                }
                try {
                    ASYNC_TASKS.execute(new 3());
                } catch (RejectedExecutionException ex) {
                    if (!(ASYNC_TASKS.isTerminating() || ASYNC_TASKS.isTerminated())) {
                        LOG.error("Async write check was rejected from the executor: ", ex);
                        throw ex;
                    }
                }
            } else if (LOG.isTraceEnabled()) {
                LOG.trace(this + " message sent since last write check, resetting flag");
            }
            this.commandSent.set(false);
        } else if (LOG.isTraceEnabled()) {
            LOG.trace("A send is in progress");
        }
    }

    final void readCheck() {
        int currentCounter = this.next.getReceiveCounter();
        int previousCounter = this.lastReceiveCounter.getAndSet(currentCounter);
        if (!this.inReceive.get() && currentCounter == previousCounter) {
            if (!this.commandReceived.get() && this.monitorStarted.get() && !ASYNC_TASKS.isTerminating() && !ASYNC_TASKS.isTerminated()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No message received since last read check for " + toString() + ". Throwing InactivityIOException.");
                }
                try {
                    ASYNC_TASKS.execute(new 4());
                } catch (RejectedExecutionException ex) {
                    if (!(ASYNC_TASKS.isTerminating() || ASYNC_TASKS.isTerminated())) {
                        LOG.error("Async read check was rejected from the executor: ", ex);
                        throw ex;
                    }
                }
            } else if (LOG.isTraceEnabled()) {
                LOG.trace("Message received since last read check, resetting flag: ");
            }
            this.commandReceived.set(false);
        } else if (LOG.isTraceEnabled()) {
            LOG.trace("A receive is in progress");
        }
    }

    public void onCommand(Object command) {
        this.commandReceived.set(true);
        this.inReceive.set(true);
        try {
            if (command.getClass() == KeepAliveInfo.class) {
                KeepAliveInfo info = (KeepAliveInfo) command;
                if (info.isResponseRequired()) {
                    this.sendLock.readLock().lock();
                    info.setResponseRequired(false);
                    oneway(info);
                    this.sendLock.readLock().unlock();
                }
            } else {
                if (command.getClass() == WireFormatInfo.class) {
                    synchronized (this) {
                        try {
                            processInboundWireFormatInfo((WireFormatInfo) command);
                        } catch (IOException e) {
                            onException(e);
                        }
                    }
                }
                this.transportListener.onCommand(command);
            }
        } catch (IOException e2) {
            onException(e2);
            this.sendLock.readLock().unlock();
        } catch (Throwable th) {
            this.inReceive.set(false);
        }
        this.inReceive.set(false);
    }

    public void oneway(Object o) throws IOException {
        this.sendLock.readLock().lock();
        this.inSend.set(true);
        try {
            doOnewaySend(o);
        } finally {
            this.commandSent.set(true);
            this.inSend.set(false);
            this.sendLock.readLock().unlock();
        }
    }

    private void doOnewaySend(Object command) throws IOException {
        if (this.failed.get()) {
            throw new InactivityIOException("Cannot send, channel has already failed: " + this.next.getRemoteAddress());
        }
        if (command.getClass() == WireFormatInfo.class) {
            synchronized (this) {
                processOutboundWireFormatInfo((WireFormatInfo) command);
            }
        }
        this.next.oneway(command);
    }

    public void onException(IOException error) {
        if (this.failed.compareAndSet(false, true)) {
            stopMonitorThreads();
            if (this.sendLock.writeLock().isHeldByCurrentThread()) {
                this.sendLock.writeLock().unlock();
            }
            this.transportListener.onException(error);
        }
    }

    public void setUseKeepAlive(boolean val) {
        this.useKeepAlive = val;
    }

    public long getReadCheckTime() {
        return this.readCheckTime;
    }

    public void setReadCheckTime(long readCheckTime) {
        this.readCheckTime = readCheckTime;
    }

    public long getWriteCheckTime() {
        return this.writeCheckTime;
    }

    public void setWriteCheckTime(long writeCheckTime) {
        this.writeCheckTime = writeCheckTime;
    }

    public long getInitialDelayTime() {
        return this.initialDelayTime;
    }

    public void setInitialDelayTime(long initialDelayTime) {
        this.initialDelayTime = initialDelayTime;
    }

    public boolean isKeepAliveResponseRequired() {
        return this.keepAliveResponseRequired;
    }

    public void setKeepAliveResponseRequired(boolean value) {
        this.keepAliveResponseRequired = value;
    }

    public boolean isMonitorStarted() {
        return this.monitorStarted.get();
    }

    protected synchronized void startMonitorThreads() throws IOException {
        if (!this.monitorStarted.get()) {
            if (configuredOk()) {
                if (this.readCheckTime > 0) {
                    this.readCheckerTask = new SchedulerTimerTask(this.readChecker);
                }
                if (this.writeCheckTime > 0) {
                    this.writeCheckerTask = new SchedulerTimerTask(this.writeChecker);
                }
                if (this.writeCheckTime > 0 || this.readCheckTime > 0) {
                    this.monitorStarted.set(true);
                    synchronized (AbstractInactivityMonitor.class) {
                        if (CHECKER_COUNTER == 0) {
                            ASYNC_TASKS = createExecutor();
                            READ_CHECK_TIMER = new Timer("ActiveMQ InactivityMonitor ReadCheckTimer", true);
                            WRITE_CHECK_TIMER = new Timer("ActiveMQ InactivityMonitor WriteCheckTimer", true);
                        }
                        CHECKER_COUNTER++;
                        if (this.readCheckTime > 0) {
                            READ_CHECK_TIMER.schedule(this.readCheckerTask, this.initialDelayTime, this.readCheckTime);
                        }
                        if (this.writeCheckTime > 0) {
                            WRITE_CHECK_TIMER.schedule(this.writeCheckerTask, this.initialDelayTime, this.writeCheckTime);
                        }
                    }
                }
            }
        }
    }

    protected synchronized void stopMonitorThreads() {
        if (this.monitorStarted.compareAndSet(true, false)) {
            if (this.readCheckerTask != null) {
                this.readCheckerTask.cancel();
            }
            if (this.writeCheckerTask != null) {
                this.writeCheckerTask.cancel();
            }
            synchronized (AbstractInactivityMonitor.class) {
                WRITE_CHECK_TIMER.purge();
                READ_CHECK_TIMER.purge();
                CHECKER_COUNTER--;
                if (CHECKER_COUNTER == 0) {
                    WRITE_CHECK_TIMER.cancel();
                    READ_CHECK_TIMER.cancel();
                    WRITE_CHECK_TIMER = null;
                    READ_CHECK_TIMER = null;
                    ThreadPoolUtils.shutdown(ASYNC_TASKS);
                }
            }
        }
    }

    private ThreadPoolExecutor createExecutor() {
        ThreadPoolExecutor exec = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10, TimeUnit.SECONDS, new SynchronousQueue(), this.factory);
        exec.allowCoreThreadTimeOut(true);
        return exec;
    }
}
