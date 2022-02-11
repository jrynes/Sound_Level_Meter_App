package org.apache.activemq.transport.udp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DefaultBufferPool extends SimpleBufferPool implements ByteBufferPool {
    private List<ByteBuffer> buffers;
    private Object lock;

    public DefaultBufferPool() {
        super(true);
        this.buffers = new ArrayList();
        this.lock = new Object();
    }

    public DefaultBufferPool(boolean useDirect) {
        super(useDirect);
        this.buffers = new ArrayList();
        this.lock = new Object();
    }

    public synchronized ByteBuffer borrowBuffer() {
        ByteBuffer byteBuffer;
        synchronized (this.lock) {
            int size = this.buffers.size();
            if (size > 0) {
                byteBuffer = (ByteBuffer) this.buffers.remove(size - 1);
            } else {
                byteBuffer = createBuffer();
            }
        }
        return byteBuffer;
    }

    public void returnBuffer(ByteBuffer buffer) {
        synchronized (this.lock) {
            this.buffers.add(buffer);
        }
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        synchronized (this.lock) {
            this.buffers.clear();
        }
    }
}
