package org.apache.activemq.management;

public class RangeStatisticImpl extends StatisticImpl {
    private long current;
    private long highWaterMark;
    private long lowWaterMark;

    public RangeStatisticImpl(String name, String unit, String description) {
        super(name, unit, description);
    }

    public void reset() {
        if (isDoReset()) {
            super.reset();
            this.current = 0;
            this.lowWaterMark = 0;
            this.highWaterMark = 0;
        }
    }

    public long getHighWaterMark() {
        return this.highWaterMark;
    }

    public long getLowWaterMark() {
        return this.lowWaterMark;
    }

    public long getCurrent() {
        return this.current;
    }

    public void setCurrent(long current) {
        this.current = current;
        if (current > this.highWaterMark) {
            this.highWaterMark = current;
        }
        if (current < this.lowWaterMark || this.lowWaterMark == 0) {
            this.lowWaterMark = current;
        }
        updateSampleTime();
    }

    protected void appendFieldDescription(StringBuffer buffer) {
        buffer.append(" current: ");
        buffer.append(Long.toString(this.current));
        buffer.append(" lowWaterMark: ");
        buffer.append(Long.toString(this.lowWaterMark));
        buffer.append(" highWaterMark: ");
        buffer.append(Long.toString(this.highWaterMark));
        super.appendFieldDescription(buffer);
    }
}
