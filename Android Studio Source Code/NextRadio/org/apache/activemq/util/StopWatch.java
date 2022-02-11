package org.apache.activemq.util;

public final class StopWatch {
    private long start;
    private long stop;

    public StopWatch() {
        this(true);
    }

    public StopWatch(boolean started) {
        if (started) {
            restart();
        }
    }

    public void restart() {
        this.start = System.currentTimeMillis();
        this.stop = 0;
    }

    public long stop() {
        this.stop = System.currentTimeMillis();
        return taken();
    }

    public long taken() {
        if (this.start > 0 && this.stop > 0) {
            return this.stop - this.start;
        }
        if (this.start > 0) {
            return System.currentTimeMillis() - this.start;
        }
        return 0;
    }
}
