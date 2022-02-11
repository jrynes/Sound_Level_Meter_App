package org.apache.activemq.transport.stomp;

import java.io.IOException;
import org.apache.activemq.command.WireFormatInfo;
import org.apache.activemq.transport.AbstractInactivityMonitor;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StompInactivityMonitor extends AbstractInactivityMonitor {
    private static final Logger LOG;
    private boolean isConfigured;

    static {
        LOG = LoggerFactory.getLogger(StompInactivityMonitor.class);
    }

    public StompInactivityMonitor(Transport next, WireFormat wireFormat) {
        super(next, wireFormat);
        this.isConfigured = false;
    }

    public void startMonitoring() throws IOException {
        this.isConfigured = true;
        startMonitorThreads();
    }

    protected void processInboundWireFormatInfo(WireFormatInfo info) throws IOException {
    }

    protected void processOutboundWireFormatInfo(WireFormatInfo info) throws IOException {
    }

    protected boolean configuredOk() throws IOException {
        if (!this.isConfigured) {
            return false;
        }
        LOG.debug("Stomp Inactivity Monitor read check: " + getReadCheckTime() + ", write check: " + getWriteCheckTime());
        if (getReadCheckTime() < 0 || getWriteCheckTime() < 0) {
            return false;
        }
        return true;
    }
}
