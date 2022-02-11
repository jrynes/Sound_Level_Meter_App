package org.apache.activemq.management;

public class TimeStatisticImpl extends StatisticImpl {
    private long count;
    private long maxTime;
    private long minTime;
    private TimeStatisticImpl parent;
    private long totalTime;

    public TimeStatisticImpl(String name, String description) {
        this(name, "millis", description);
    }

    public TimeStatisticImpl(TimeStatisticImpl parent, String name, String description) {
        this(name, description);
        this.parent = parent;
    }

    public TimeStatisticImpl(String name, String unit, String description) {
        super(name, unit, description);
    }

    public synchronized void reset() {
        if (isDoReset()) {
            super.reset();
            this.count = 0;
            this.maxTime = 0;
            this.minTime = 0;
            this.totalTime = 0;
        }
    }

    public synchronized long getCount() {
        return this.count;
    }

    public synchronized void addTime(long time) {
        this.count++;
        this.totalTime += time;
        if (time > this.maxTime) {
            this.maxTime = time;
        }
        if (time < this.minTime || this.minTime == 0) {
            this.minTime = time;
        }
        updateSampleTime();
        if (this.parent != null) {
            this.parent.addTime(time);
        }
    }

    public long getMaxTime() {
        return this.maxTime;
    }

    public synchronized long getMinTime() {
        return this.minTime;
    }

    public synchronized long getTotalTime() {
        return this.totalTime;
    }

    public synchronized double getAverageTime() {
        double d;
        if (this.count == 0) {
            d = 0.0d;
        } else {
            d = ((double) this.totalTime) / ((double) this.count);
        }
        return d;
    }

    public synchronized double getAverageTimeExcludingMinMax() {
        double d;
        if (this.count <= 2) {
            d = 0.0d;
        } else {
            d = ((double) ((this.totalTime - this.minTime) - this.maxTime)) / ((double) (this.count - 2));
        }
        return d;
    }

    public double getAveragePerSecond() {
        double averageTime = getAverageTime();
        if (averageTime == 0.0d) {
            return 0.0d;
        }
        return 1000.0d / averageTime;
    }

    public double getAveragePerSecondExcludingMinMax() {
        double average = getAverageTimeExcludingMinMax();
        if (average == 0.0d) {
            return 0.0d;
        }
        return 1000.0d / average;
    }

    public TimeStatisticImpl getParent() {
        return this.parent;
    }

    public void setParent(TimeStatisticImpl parent) {
        this.parent = parent;
    }

    protected synchronized void appendFieldDescription(StringBuffer buffer) {
        buffer.append(" count: ");
        buffer.append(Long.toString(this.count));
        buffer.append(" maxTime: ");
        buffer.append(Long.toString(this.maxTime));
        buffer.append(" minTime: ");
        buffer.append(Long.toString(this.minTime));
        buffer.append(" totalTime: ");
        buffer.append(Long.toString(this.totalTime));
        buffer.append(" averageTime: ");
        buffer.append(Double.toString(getAverageTime()));
        buffer.append(" averageTimeExMinMax: ");
        buffer.append(Double.toString(getAverageTimeExcludingMinMax()));
        buffer.append(" averagePerSecond: ");
        buffer.append(Double.toString(getAveragePerSecond()));
        buffer.append(" averagePerSecondExMinMax: ");
        buffer.append(Double.toString(getAveragePerSecondExcludingMinMax()));
        super.appendFieldDescription(buffer);
    }
}
