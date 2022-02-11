package org.apache.activemq.usage;

import org.apache.activemq.transport.discovery.multicast.MulticastDiscoveryAgent;

public class MemoryUsage extends Usage<MemoryUsage> {
    private long usage;

    public MemoryUsage() {
        this(null, null);
    }

    public MemoryUsage(MemoryUsage parent) {
        this(parent, MulticastDiscoveryAgent.DEFAULT_HOST_STR);
    }

    public MemoryUsage(String name) {
        this(null, name);
    }

    public MemoryUsage(MemoryUsage parent, String name) {
        this(parent, name, 1.0f);
    }

    public MemoryUsage(MemoryUsage parent, String name, float portion) {
        super(parent, name, portion);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void waitForSpace() throws java.lang.InterruptedException {
        /*
        r4 = this;
        r1 = r4.parent;
        if (r1 == 0) goto L_0x000b;
    L_0x0004:
        r1 = r4.parent;
        r1 = (org.apache.activemq.usage.MemoryUsage) r1;
        r1.waitForSpace();
    L_0x000b:
        r2 = r4.usageMutex;
        monitor-enter(r2);
        r0 = 0;
    L_0x000f:
        r1 = r4.percentUsage;	 Catch:{ all -> 0x001f }
        r3 = 100;
        if (r1 < r3) goto L_0x001d;
    L_0x0015:
        r1 = r4.usageMutex;	 Catch:{ all -> 0x001f }
        r1.wait();	 Catch:{ all -> 0x001f }
        r0 = r0 + 1;
        goto L_0x000f;
    L_0x001d:
        monitor-exit(r2);	 Catch:{ all -> 0x001f }
        return;
    L_0x001f:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001f }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.usage.MemoryUsage.waitForSpace():void");
    }

    public boolean waitForSpace(long timeout) throws InterruptedException {
        if (this.parent != null && !((MemoryUsage) this.parent).waitForSpace(timeout)) {
            return false;
        }
        boolean z;
        synchronized (this.usageMutex) {
            if (this.percentUsage >= 100) {
                this.usageMutex.wait(timeout);
            }
            if (this.percentUsage < 100) {
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public boolean isFull() {
        if (this.parent != null && ((MemoryUsage) this.parent).isFull()) {
            return true;
        }
        boolean z;
        synchronized (this.usageMutex) {
            z = this.percentUsage >= 100;
        }
        return z;
    }

    public void enqueueUsage(long value) throws InterruptedException {
        waitForSpace();
        increaseUsage(value);
    }

    public void increaseUsage(long value) {
        if (value != 0) {
            int percentUsage;
            synchronized (this.usageMutex) {
                this.usage += value;
                percentUsage = caclPercentUsage();
            }
            setPercentUsage(percentUsage);
            if (this.parent != null) {
                ((MemoryUsage) this.parent).increaseUsage(value);
            }
        }
    }

    public void decreaseUsage(long value) {
        if (value != 0) {
            int percentUsage;
            synchronized (this.usageMutex) {
                this.usage -= value;
                percentUsage = caclPercentUsage();
            }
            setPercentUsage(percentUsage);
            if (this.parent != null) {
                ((MemoryUsage) this.parent).decreaseUsage(value);
            }
        }
    }

    protected long retrieveUsage() {
        return this.usage;
    }

    public long getUsage() {
        return this.usage;
    }

    public void setUsage(long usage) {
        this.usage = usage;
    }
}
