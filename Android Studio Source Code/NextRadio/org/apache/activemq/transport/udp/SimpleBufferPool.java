package org.apache.activemq.transport.udp;

import java.nio.ByteBuffer;

public class SimpleBufferPool implements ByteBufferPool {
    private int defaultSize;
    private boolean useDirect;

    public SimpleBufferPool() {
        this(false);
    }

    public SimpleBufferPool(boolean useDirect) {
        this.useDirect = useDirect;
    }

    public synchronized ByteBuffer borrowBuffer() {
        return createBuffer();
    }

    public void returnBuffer(ByteBuffer buffer) {
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public boolean isUseDirect() {
        return this.useDirect;
    }

    public void setUseDirect(boolean useDirect) {
        this.useDirect = useDirect;
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
    }

    protected ByteBuffer createBuffer() {
        if (this.useDirect) {
            return ByteBuffer.allocateDirect(this.defaultSize);
        }
        return ByteBuffer.allocate(this.defaultSize);
    }
}
