package org.apache.activemq.util;

public class LongSequenceGenerator {
    private long lastSequenceId;

    public synchronized long getNextSequenceId() {
        long j;
        j = this.lastSequenceId + 1;
        this.lastSequenceId = j;
        return j;
    }

    public synchronized long getLastSequenceId() {
        return this.lastSequenceId;
    }

    public synchronized void setLastSequenceId(long l) {
        this.lastSequenceId = l;
    }
}
