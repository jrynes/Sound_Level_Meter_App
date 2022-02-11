package org.apache.activemq.util;

public class IntSequenceGenerator {
    private int lastSequenceId;

    public synchronized int getNextSequenceId() {
        int i;
        i = this.lastSequenceId + 1;
        this.lastSequenceId = i;
        return i;
    }

    public synchronized int getLastSequenceId() {
        return this.lastSequenceId;
    }

    public synchronized void setLastSequenceId(int l) {
        this.lastSequenceId = l;
    }
}
