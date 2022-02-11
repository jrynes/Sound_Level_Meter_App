package org.apache.activemq.management;

import java.util.ArrayList;
import java.util.List;
import javax.management.j2ee.statistics.CountStatistic;

public class PollCountStatisticImpl extends StatisticImpl implements CountStatistic {
    private List<PollCountStatisticImpl> children;
    private PollCountStatisticImpl parent;

    public PollCountStatisticImpl(PollCountStatisticImpl parent, String name, String description) {
        this(name, description);
        setParent(parent);
    }

    public PollCountStatisticImpl(String name, String description) {
        this(name, "count", description);
    }

    public PollCountStatisticImpl(String name, String unit, String description) {
        super(name, unit, description);
    }

    public PollCountStatisticImpl getParent() {
        return this.parent;
    }

    public void setParent(PollCountStatisticImpl parent) {
        if (this.parent != null) {
            this.parent.removeChild(this);
        }
        this.parent = parent;
        if (this.parent != null) {
            this.parent.addChild(this);
        }
    }

    private synchronized void removeChild(PollCountStatisticImpl child) {
        if (this.children != null) {
            this.children.remove(child);
        }
    }

    private synchronized void addChild(PollCountStatisticImpl child) {
        if (this.children == null) {
            this.children = new ArrayList();
        }
        this.children.add(child);
    }

    public synchronized long getCount() {
        long j;
        if (this.children == null) {
            j = 0;
        } else {
            j = 0;
            for (PollCountStatisticImpl child : this.children) {
                j += child.getCount();
            }
        }
        return j;
    }

    protected void appendFieldDescription(StringBuffer buffer) {
        buffer.append(" count: ");
        buffer.append(Long.toString(getCount()));
        super.appendFieldDescription(buffer);
    }

    public double getPeriod() {
        double count = (double) getCount();
        if (count == 0.0d) {
            return 0.0d;
        }
        return ((double) (System.currentTimeMillis() - getStartTime())) / (1000.0d * count);
    }

    public double getFrequency() {
        return (1000.0d * ((double) getCount())) / ((double) (System.currentTimeMillis() - getStartTime()));
    }
}
