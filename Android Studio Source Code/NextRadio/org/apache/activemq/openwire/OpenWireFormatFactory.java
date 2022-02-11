package org.apache.activemq.openwire;

import org.apache.activemq.command.WireFormatInfo;
import org.apache.activemq.util.ThreadPoolUtils;
import org.apache.activemq.wireformat.WireFormat;
import org.apache.activemq.wireformat.WireFormatFactory;
import org.xbill.DNS.KEYRecord.Flags;

public class OpenWireFormatFactory implements WireFormatFactory {
    private boolean cacheEnabled;
    private int cacheSize;
    private long maxFrameSize;
    private long maxInactivityDuration;
    private long maxInactivityDurationInitalDelay;
    private boolean sizePrefixDisabled;
    private boolean stackTraceEnabled;
    private boolean tcpNoDelayEnabled;
    private boolean tightEncodingEnabled;
    private int version;

    public OpenWireFormatFactory() {
        this.version = 9;
        this.stackTraceEnabled = true;
        this.tcpNoDelayEnabled = true;
        this.cacheEnabled = true;
        this.tightEncodingEnabled = true;
        this.maxInactivityDuration = 30000;
        this.maxInactivityDurationInitalDelay = ThreadPoolUtils.DEFAULT_SHUTDOWN_AWAIT_TERMINATION;
        this.cacheSize = Flags.FLAG5;
        this.maxFrameSize = OpenWireFormat.DEFAULT_MAX_FRAME_SIZE;
    }

    public WireFormat createWireFormat() {
        WireFormatInfo info = new WireFormatInfo();
        info.setVersion(this.version);
        try {
            info.setStackTraceEnabled(this.stackTraceEnabled);
            info.setCacheEnabled(this.cacheEnabled);
            info.setTcpNoDelayEnabled(this.tcpNoDelayEnabled);
            info.setTightEncodingEnabled(this.tightEncodingEnabled);
            info.setSizePrefixDisabled(this.sizePrefixDisabled);
            info.setMaxInactivityDuration(this.maxInactivityDuration);
            info.setMaxInactivityDurationInitalDelay(this.maxInactivityDurationInitalDelay);
            info.setCacheSize(this.cacheSize);
            info.setMaxFrameSize(this.maxFrameSize);
            OpenWireFormat f = new OpenWireFormat(this.version);
            f.setMaxFrameSize(this.maxFrameSize);
            f.setPreferedWireFormatInfo(info);
            return f;
        } catch (Exception e) {
            IllegalStateException ise = new IllegalStateException("Could not configure WireFormatInfo");
            ise.initCause(e);
            throw ise;
        }
    }

    public boolean isStackTraceEnabled() {
        return this.stackTraceEnabled;
    }

    public void setStackTraceEnabled(boolean stackTraceEnabled) {
        this.stackTraceEnabled = stackTraceEnabled;
    }

    public boolean isTcpNoDelayEnabled() {
        return this.tcpNoDelayEnabled;
    }

    public void setTcpNoDelayEnabled(boolean tcpNoDelayEnabled) {
        this.tcpNoDelayEnabled = tcpNoDelayEnabled;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isCacheEnabled() {
        return this.cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public boolean isTightEncodingEnabled() {
        return this.tightEncodingEnabled;
    }

    public void setTightEncodingEnabled(boolean tightEncodingEnabled) {
        this.tightEncodingEnabled = tightEncodingEnabled;
    }

    public boolean isSizePrefixDisabled() {
        return this.sizePrefixDisabled;
    }

    public void setSizePrefixDisabled(boolean sizePrefixDisabled) {
        this.sizePrefixDisabled = sizePrefixDisabled;
    }

    public long getMaxInactivityDuration() {
        return this.maxInactivityDuration;
    }

    public void setMaxInactivityDuration(long maxInactivityDuration) {
        this.maxInactivityDuration = maxInactivityDuration;
    }

    public int getCacheSize() {
        return this.cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public long getMaxInactivityDurationInitalDelay() {
        return this.maxInactivityDurationInitalDelay;
    }

    public void setMaxInactivityDurationInitalDelay(long maxInactivityDurationInitalDelay) {
        this.maxInactivityDurationInitalDelay = maxInactivityDurationInitalDelay;
    }

    public long getMaxFrameSize() {
        return this.maxFrameSize;
    }

    public void setMaxFrameSize(long maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }
}
