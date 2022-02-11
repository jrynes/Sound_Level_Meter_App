package org.apache.activemq.management;

import javax.management.j2ee.statistics.Statistic;

public class StatisticImpl implements Statistic, Resettable {
    private String description;
    private boolean doReset;
    protected boolean enabled;
    private long lastSampleTime;
    private String name;
    private long startTime;
    private String unit;

    public StatisticImpl(String name, String unit, String description) {
        this.doReset = true;
        this.name = name;
        this.unit = unit;
        this.description = description;
        this.startTime = System.currentTimeMillis();
        this.lastSampleTime = this.startTime;
    }

    public synchronized void reset() {
        if (isDoReset()) {
            this.startTime = System.currentTimeMillis();
            this.lastSampleTime = this.startTime;
        }
    }

    protected synchronized void updateSampleTime() {
        this.lastSampleTime = System.currentTimeMillis();
    }

    public synchronized String toString() {
        StringBuffer buffer;
        buffer = new StringBuffer();
        buffer.append(this.name);
        buffer.append("{");
        appendFieldDescription(buffer);
        buffer.append(" }");
        return buffer.toString();
    }

    public String getName() {
        return this.name;
    }

    public String getUnit() {
        return this.unit;
    }

    public String getDescription() {
        return this.description;
    }

    public synchronized long getStartTime() {
        return this.startTime;
    }

    public synchronized long getLastSampleTime() {
        return this.lastSampleTime;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDoReset() {
        return this.doReset;
    }

    public void setDoReset(boolean doReset) {
        this.doReset = doReset;
    }

    protected synchronized void appendFieldDescription(StringBuffer buffer) {
        buffer.append(" unit: ");
        buffer.append(this.unit);
        buffer.append(" startTime: ");
        buffer.append(this.startTime);
        buffer.append(" lastSampleTime: ");
        buffer.append(this.lastSampleTime);
        buffer.append(" description: ");
        buffer.append(this.description);
    }
}
