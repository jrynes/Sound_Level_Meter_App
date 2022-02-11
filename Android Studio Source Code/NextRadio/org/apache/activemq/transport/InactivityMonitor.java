package org.apache.activemq.transport;

import java.io.IOException;
import org.apache.activemq.command.WireFormatInfo;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InactivityMonitor extends AbstractInactivityMonitor {
    private static final Logger LOG;
    private boolean ignoreAllWireFormatInfo;
    private boolean ignoreRemoteWireFormat;
    private WireFormatInfo localWireFormatInfo;
    private WireFormatInfo remoteWireFormatInfo;

    static {
        LOG = LoggerFactory.getLogger(InactivityMonitor.class);
    }

    public InactivityMonitor(Transport next, WireFormat wireFormat) {
        super(next, wireFormat);
        this.ignoreRemoteWireFormat = false;
        this.ignoreAllWireFormatInfo = false;
        if (this.wireFormat == null) {
            this.ignoreAllWireFormatInfo = true;
        }
    }

    protected void processInboundWireFormatInfo(WireFormatInfo info) throws IOException {
        IOException error = null;
        this.remoteWireFormatInfo = info;
        try {
            startMonitorThreads();
        } catch (IOException e) {
            error = e;
        }
        if (error != null) {
            onException(error);
        }
    }

    protected void processOutboundWireFormatInfo(WireFormatInfo info) throws IOException {
        this.localWireFormatInfo = info;
        startMonitorThreads();
    }

    protected synchronized void startMonitorThreads() throws IOException {
        if (!isMonitorStarted()) {
            long readCheckTime = getReadCheckTime();
            if (readCheckTime > 0) {
                setWriteCheckTime(writeCheckValueFromReadCheck(readCheckTime));
            }
            super.startMonitorThreads();
        }
    }

    private long writeCheckValueFromReadCheck(long readCheckTime) {
        return readCheckTime > 3 ? readCheckTime / 3 : readCheckTime;
    }

    protected boolean configuredOk() throws IOException {
        if (this.ignoreAllWireFormatInfo) {
            return true;
        }
        if (this.localWireFormatInfo == null || this.remoteWireFormatInfo == null) {
            return false;
        }
        long readCheckTime;
        long writeCheckTime;
        if (this.ignoreRemoteWireFormat) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Using local: " + this.localWireFormatInfo);
            }
            readCheckTime = this.localWireFormatInfo.getMaxInactivityDuration();
            writeCheckTime = writeCheckValueFromReadCheck(readCheckTime);
            setReadCheckTime(readCheckTime);
            setInitialDelayTime(this.localWireFormatInfo.getMaxInactivityDurationInitalDelay());
            setWriteCheckTime(writeCheckTime);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Using min of local: " + this.localWireFormatInfo + " and remote: " + this.remoteWireFormatInfo);
            }
            readCheckTime = Math.min(this.localWireFormatInfo.getMaxInactivityDuration(), this.remoteWireFormatInfo.getMaxInactivityDuration());
            writeCheckTime = writeCheckValueFromReadCheck(readCheckTime);
            setReadCheckTime(readCheckTime);
            setInitialDelayTime(Math.min(this.localWireFormatInfo.getMaxInactivityDurationInitalDelay(), this.remoteWireFormatInfo.getMaxInactivityDurationInitalDelay()));
            setWriteCheckTime(writeCheckTime);
        }
        return true;
    }

    public boolean isIgnoreAllWireFormatInfo() {
        return this.ignoreAllWireFormatInfo;
    }

    public void setIgnoreAllWireFormatInfo(boolean ignoreAllWireFormatInfo) {
        this.ignoreAllWireFormatInfo = ignoreAllWireFormatInfo;
    }

    public boolean isIgnoreRemoteWireFormat() {
        return this.ignoreRemoteWireFormat;
    }

    public void setIgnoreRemoteWireFormat(boolean ignoreRemoteWireFormat) {
        this.ignoreRemoteWireFormat = ignoreRemoteWireFormat;
    }
}
