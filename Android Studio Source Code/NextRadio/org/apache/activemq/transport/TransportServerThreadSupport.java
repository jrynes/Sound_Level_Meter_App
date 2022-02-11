package org.apache.activemq.transport;

import java.net.URI;
import org.apache.activemq.util.ServiceStopper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TransportServerThreadSupport extends TransportServerSupport implements Runnable {
    private static final Logger LOG;
    private boolean daemon;
    private boolean joinOnStop;
    private Thread runner;
    private long stackSize;

    static {
        LOG = LoggerFactory.getLogger(TransportServerThreadSupport.class);
    }

    public TransportServerThreadSupport() {
        this.daemon = true;
        this.joinOnStop = true;
    }

    public TransportServerThreadSupport(URI location) {
        super(location);
        this.daemon = true;
        this.joinOnStop = true;
    }

    public boolean isDaemon() {
        return this.daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public boolean isJoinOnStop() {
        return this.joinOnStop;
    }

    public void setJoinOnStop(boolean joinOnStop) {
        this.joinOnStop = joinOnStop;
    }

    protected void doStart() throws Exception {
        LOG.info("Listening for connections at: " + getConnectURI());
        this.runner = new Thread(null, this, "ActiveMQ Transport Server: " + toString(), this.stackSize);
        this.runner.setDaemon(this.daemon);
        this.runner.setPriority(9);
        this.runner.start();
    }

    protected void doStop(ServiceStopper stopper) throws Exception {
        if (this.runner != null && this.joinOnStop) {
            this.runner.join();
            this.runner = null;
        }
    }

    public long getStackSize() {
        return this.stackSize;
    }

    public void setStackSize(long stackSize) {
        this.stackSize = stackSize;
    }
}
