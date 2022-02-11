package org.apache.activemq.transport;

public abstract class TransportThreadSupport extends TransportSupport implements Runnable {
    private boolean daemon;
    private Thread runner;
    private long stackSize;

    public boolean isDaemon() {
        return this.daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    protected void doStart() throws Exception {
        this.runner = new Thread(null, this, "ActiveMQ Transport: " + toString(), this.stackSize);
        this.runner.setDaemon(this.daemon);
        this.runner.start();
    }

    public long getStackSize() {
        return this.stackSize;
    }

    public void setStackSize(long stackSize) {
        this.stackSize = stackSize;
    }
}
