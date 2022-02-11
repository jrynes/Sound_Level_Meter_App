package org.apache.activemq.management;

public class BoundedRangeStatisticImpl extends RangeStatisticImpl {
    private long lowerBound;
    private long upperBound;

    public BoundedRangeStatisticImpl(String name, String unit, String description, long lowerBound, long upperBound) {
        super(name, unit, description);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public long getLowerBound() {
        return this.lowerBound;
    }

    public long getUpperBound() {
        return this.upperBound;
    }

    protected void appendFieldDescription(StringBuffer buffer) {
        buffer.append(" lowerBound: ");
        buffer.append(Long.toString(this.lowerBound));
        buffer.append(" upperBound: ");
        buffer.append(Long.toString(this.upperBound));
        super.appendFieldDescription(buffer);
    }
}
