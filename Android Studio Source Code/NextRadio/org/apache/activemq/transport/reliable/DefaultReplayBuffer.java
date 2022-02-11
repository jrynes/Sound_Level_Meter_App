package org.apache.activemq.transport.reliable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultReplayBuffer implements ReplayBuffer {
    private static final Logger LOG;
    private ReplayBufferListener listener;
    private Object lock;
    private int lowestCommandId;
    private Map<Integer, Object> map;
    private final int size;

    static {
        LOG = LoggerFactory.getLogger(DefaultReplayBuffer.class);
    }

    public DefaultReplayBuffer(int size) {
        this.lowestCommandId = 1;
        this.lock = new Object();
        this.size = size;
        this.map = createMap(size);
    }

    public void addBuffer(int commandId, Object buffer) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding command ID: " + commandId + " to replay buffer: " + this + " object: " + buffer);
        }
        synchronized (this.lock) {
            int max = this.size - 1;
            while (this.map.size() >= max) {
                Map map = this.map;
                int i = this.lowestCommandId + 1;
                this.lowestCommandId = i;
                onEvictedBuffer(this.lowestCommandId, map.remove(Integer.valueOf(i)));
            }
            this.map.put(Integer.valueOf(commandId), buffer);
        }
    }

    public void setReplayBufferListener(ReplayBufferListener bufferPoolAdapter) {
        this.listener = bufferPoolAdapter;
    }

    public void replayMessages(int fromCommandId, int toCommandId, Replayer replayer) throws IOException {
        if (replayer == null) {
            throw new IllegalArgumentException("No Replayer parameter specified");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Buffer: " + this + " replaying messages from: " + fromCommandId + " to: " + toCommandId);
        }
        for (int i = fromCommandId; i <= toCommandId; i++) {
            Object buffer;
            synchronized (this.lock) {
                buffer = this.map.get(Integer.valueOf(i));
            }
            replayer.sendBuffer(i, buffer);
        }
    }

    protected Map<Integer, Object> createMap(int maximumSize) {
        return new HashMap(maximumSize);
    }

    protected void onEvictedBuffer(int commandId, Object buffer) {
        if (this.listener != null) {
            this.listener.onBufferDiscarded(commandId, buffer);
        }
    }
}
