package org.apache.activemq.usage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.Service;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Usage<T extends Usage> implements Service {
    private static final Logger LOG;
    private final List<Runnable> callbacks;
    private final List<T> children;
    private final boolean debug;
    private ThreadPoolExecutor executor;
    private UsageCapacity limiter;
    private final List<UsageListener> listeners;
    protected String name;
    protected T parent;
    protected int percentUsage;
    private int percentUsageMinDelta;
    private int pollingTime;
    private final AtomicBoolean started;
    protected final Object usageMutex;
    private float usagePortion;

    class 1 implements Runnable {
        final /* synthetic */ int val$newPercentUsage;
        final /* synthetic */ int val$oldPercentUsage;

        1(int i, int i2) {
            this.val$oldPercentUsage = i;
            this.val$newPercentUsage = i2;
        }

        public void run() {
            for (UsageListener l : Usage.this.listeners) {
                l.onUsageChanged(Usage.this, this.val$oldPercentUsage, this.val$newPercentUsage);
            }
        }
    }

    class 2 implements Runnable {
        final /* synthetic */ Runnable val$callback;

        2(Runnable runnable) {
            this.val$callback = runnable;
        }

        public void run() {
            synchronized (Usage.this.usageMutex) {
                if (Usage.this.percentUsage >= 100) {
                    Usage.this.callbacks.add(this.val$callback);
                } else {
                    this.val$callback.run();
                }
            }
        }
    }

    protected abstract long retrieveUsage();

    static {
        LOG = LoggerFactory.getLogger(Usage.class);
    }

    public Usage(T parent, String name, float portion) {
        this.usageMutex = new Object();
        this.limiter = new DefaultUsageCapacity();
        this.percentUsageMinDelta = 1;
        this.listeners = new CopyOnWriteArrayList();
        this.debug = LOG.isDebugEnabled();
        this.usagePortion = 1.0f;
        this.children = new CopyOnWriteArrayList();
        this.callbacks = new LinkedList();
        this.pollingTime = 100;
        this.started = new AtomicBoolean();
        this.parent = parent;
        this.usagePortion = portion;
        if (parent != null) {
            this.limiter.setLimit((long) (((float) parent.getLimit()) * portion));
            name = parent.name + Headers.SEPERATOR + name;
        }
        this.name = name;
    }

    public void waitForSpace() throws InterruptedException {
        waitForSpace(0);
    }

    public boolean waitForSpace(long timeout) throws InterruptedException {
        return waitForSpace(timeout, 100);
    }

    public boolean waitForSpace(long timeout, int highWaterMark) throws InterruptedException {
        boolean z = false;
        if (this.parent == null || this.parent.waitForSpace(timeout, highWaterMark)) {
            synchronized (this.usageMutex) {
                this.percentUsage = caclPercentUsage();
                if (this.percentUsage >= highWaterMark) {
                    long deadline = timeout > 0 ? System.currentTimeMillis() + timeout : OpenWireFormat.DEFAULT_MAX_FRAME_SIZE;
                    for (long timeleft = deadline; timeleft > 0; timeleft = deadline - System.currentTimeMillis()) {
                        this.percentUsage = caclPercentUsage();
                        if (this.percentUsage < highWaterMark) {
                            break;
                        }
                        this.usageMutex.wait((long) this.pollingTime);
                    }
                }
                if (this.percentUsage < highWaterMark) {
                    z = true;
                }
            }
        }
        return z;
    }

    public boolean isFull() {
        return isFull(100);
    }

    public boolean isFull(int highWaterMark) {
        boolean z = true;
        if (this.parent == null || !this.parent.isFull(highWaterMark)) {
            synchronized (this.usageMutex) {
                this.percentUsage = caclPercentUsage();
                if (this.percentUsage < highWaterMark) {
                    z = false;
                }
            }
        }
        return z;
    }

    public void addUsageListener(UsageListener listener) {
        this.listeners.add(listener);
    }

    public void removeUsageListener(UsageListener listener) {
        this.listeners.remove(listener);
    }

    public long getLimit() {
        long limit;
        synchronized (this.usageMutex) {
            limit = this.limiter.getLimit();
        }
        return limit;
    }

    public void setLimit(long limit) {
        if (this.percentUsageMinDelta < 0) {
            throw new IllegalArgumentException("percentUsageMinDelta must be greater or equal to 0");
        }
        synchronized (this.usageMutex) {
            this.limiter.setLimit(limit);
            this.usagePortion = 0.0f;
        }
        onLimitChange();
    }

    protected void onLimitChange() {
        int percentUsage;
        if (this.usagePortion > 0.0f && this.parent != null) {
            synchronized (this.usageMutex) {
                this.limiter.setLimit((long) (((float) this.parent.getLimit()) * this.usagePortion));
            }
        }
        synchronized (this.usageMutex) {
            percentUsage = caclPercentUsage();
        }
        setPercentUsage(percentUsage);
        for (Usage child : this.children) {
            child.onLimitChange();
        }
    }

    public float getUsagePortion() {
        float f;
        synchronized (this.usageMutex) {
            f = this.usagePortion;
        }
        return f;
    }

    public void setUsagePortion(float usagePortion) {
        synchronized (this.usageMutex) {
            this.usagePortion = usagePortion;
        }
        onLimitChange();
    }

    public int getPercentUsage() {
        int i;
        synchronized (this.usageMutex) {
            i = this.percentUsage;
        }
        return i;
    }

    public int getPercentUsageMinDelta() {
        int i;
        synchronized (this.usageMutex) {
            i = this.percentUsageMinDelta;
        }
        return i;
    }

    public void setPercentUsageMinDelta(int percentUsageMinDelta) {
        if (percentUsageMinDelta < 1) {
            throw new IllegalArgumentException("percentUsageMinDelta must be greater than 0");
        }
        int percentUsage;
        synchronized (this.usageMutex) {
            this.percentUsageMinDelta = percentUsageMinDelta;
            percentUsage = caclPercentUsage();
        }
        setPercentUsage(percentUsage);
    }

    public long getUsage() {
        long retrieveUsage;
        synchronized (this.usageMutex) {
            retrieveUsage = retrieveUsage();
        }
        return retrieveUsage;
    }

    protected void setPercentUsage(int value) {
        synchronized (this.usageMutex) {
            int oldValue = this.percentUsage;
            this.percentUsage = value;
            if (oldValue != value) {
                fireEvent(oldValue, value);
            }
        }
    }

    protected int caclPercentUsage() {
        if (this.limiter.getLimit() == 0) {
            return 0;
        }
        return (int) ((((retrieveUsage() * 100) / this.limiter.getLimit()) / ((long) this.percentUsageMinDelta)) * ((long) this.percentUsageMinDelta));
    }

    private void fireEvent(int oldPercentUsage, int newPercentUsage) {
        if (this.debug) {
            LOG.debug(getName() + ": usage change from: " + oldPercentUsage + "% of available memory, to: " + newPercentUsage + "% of available memory");
        }
        if (this.started.get()) {
            if (oldPercentUsage >= 100 && newPercentUsage < 100) {
                synchronized (this.usageMutex) {
                    this.usageMutex.notifyAll();
                    if (!this.callbacks.isEmpty()) {
                        Iterator<Runnable> iter = new ArrayList(this.callbacks).iterator();
                        while (iter.hasNext()) {
                            getExecutor().execute((Runnable) iter.next());
                        }
                        this.callbacks.clear();
                    }
                }
            }
            if (!this.listeners.isEmpty()) {
                Runnable listenerNotifier = new 1(oldPercentUsage, newPercentUsage);
                if (this.started.get()) {
                    getExecutor().execute(listenerNotifier);
                } else {
                    LOG.warn("Not notifying memory usage change to listeners on shutdown");
                }
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return "Usage(" + getName() + ") percentUsage=" + this.percentUsage + "%, usage=" + retrieveUsage() + ", limit=" + this.limiter.getLimit() + ", percentUsageMinDelta=" + this.percentUsageMinDelta + "%" + (this.parent != null ? ";Parent:" + this.parent.toString() : Stomp.EMPTY);
    }

    public void start() {
        if (this.started.compareAndSet(false, true)) {
            if (this.parent != null) {
                this.parent.addChild(this);
            }
            for (Usage t : this.children) {
                t.start();
            }
        }
    }

    public void stop() {
        if (this.started.compareAndSet(true, false)) {
            if (this.parent != null) {
                this.parent.removeChild(this);
            }
            synchronized (this.usageMutex) {
                this.usageMutex.notifyAll();
                Iterator<Runnable> iter = new ArrayList(this.callbacks).iterator();
                while (iter.hasNext()) {
                    ((Runnable) iter.next()).run();
                }
                this.callbacks.clear();
            }
            for (Usage t : this.children) {
                t.stop();
            }
        }
    }

    protected void addChild(T child) {
        this.children.add(child);
        if (this.started.get()) {
            child.start();
        }
    }

    protected void removeChild(T child) {
        this.children.remove(child);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean notifyCallbackWhenNotFull(java.lang.Runnable r6) {
        /*
        r5 = this;
        r1 = 1;
        r2 = r5.parent;
        if (r2 == 0) goto L_0x0013;
    L_0x0005:
        r0 = new org.apache.activemq.usage.Usage$2;
        r0.<init>(r6);
        r2 = r5.parent;
        r2 = r2.notifyCallbackWhenNotFull(r0);
        if (r2 == 0) goto L_0x0013;
    L_0x0012:
        return r1;
    L_0x0013:
        r2 = r5.usageMutex;
        monitor-enter(r2);
        r3 = r5.percentUsage;	 Catch:{ all -> 0x0023 }
        r4 = 100;
        if (r3 < r4) goto L_0x0026;
    L_0x001c:
        r3 = r5.callbacks;	 Catch:{ all -> 0x0023 }
        r3.add(r6);	 Catch:{ all -> 0x0023 }
        monitor-exit(r2);	 Catch:{ all -> 0x0023 }
        goto L_0x0012;
    L_0x0023:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0023 }
        throw r1;
    L_0x0026:
        r1 = 0;
        monitor-exit(r2);	 Catch:{ all -> 0x0023 }
        goto L_0x0012;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.usage.Usage.notifyCallbackWhenNotFull(java.lang.Runnable):boolean");
    }

    public UsageCapacity getLimiter() {
        return this.limiter;
    }

    public void setLimiter(UsageCapacity limiter) {
        this.limiter = limiter;
    }

    public int getPollingTime() {
        return this.pollingTime;
    }

    public void setPollingTime(int pollingTime) {
        this.pollingTime = pollingTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getParent() {
        return this.parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public ThreadPoolExecutor getExecutor() {
        return this.executor;
    }
}
