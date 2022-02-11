package org.apache.activemq.management;

import java.util.concurrent.atomic.AtomicLong;
import javax.management.j2ee.statistics.CountStatistic;

public class CountStatisticImpl extends StatisticImpl implements CountStatistic {
    private final AtomicLong counter;
    private CountStatisticImpl parent;

    public CountStatisticImpl(CountStatisticImpl parent, String name, String description) {
        this(name, description);
        this.parent = parent;
    }

    public CountStatisticImpl(String name, String description) {
        this(name, "count", description);
    }

    public CountStatisticImpl(String name, String unit, String description) {
        super(name, unit, description);
        this.counter = new AtomicLong(0);
    }

    public void reset() {
        if (isDoReset()) {
            super.reset();
            this.counter.set(0);
        }
    }

    public long getCount() {
        return this.counter.get();
    }

    public void setCount(long count) {
        if (isEnabled()) {
            this.counter.set(count);
        }
    }

    public void add(long amount) {
        if (isEnabled()) {
            this.counter.addAndGet(amount);
            updateSampleTime();
            if (this.parent != null) {
                this.parent.add(amount);
            }
        }
    }

    public void increment() {
        if (isEnabled()) {
            this.counter.incrementAndGet();
            updateSampleTime();
            if (this.parent != null) {
                this.parent.increment();
            }
        }
    }

    public void subtract(long amount) {
        if (isEnabled()) {
            this.counter.addAndGet(-amount);
            updateSampleTime();
            if (this.parent != null) {
                this.parent.subtract(amount);
            }
        }
    }

    public void decrement() {
        if (isEnabled()) {
            this.counter.decrementAndGet();
            updateSampleTime();
            if (this.parent != null) {
                this.parent.decrement();
            }
        }
    }

    public CountStatisticImpl getParent() {
        return this.parent;
    }

    public void setParent(CountStatisticImpl parent) {
        this.parent = parent;
    }

    protected void appendFieldDescription(StringBuffer buffer) {
        buffer.append(" count: ");
        buffer.append(Long.toString(this.counter.get()));
        super.appendFieldDescription(buffer);
    }

    public double getPeriod() {
        double count = (double) this.counter.get();
        if (count == 0.0d) {
            return 0.0d;
        }
        return ((double) (System.currentTimeMillis() - getStartTime())) / (1000.0d * count);
    }

    public double getFrequency() {
        return (1000.0d * ((double) this.counter.get())) / ((double) (System.currentTimeMillis() - getStartTime()));
    }
}
