package org.apache.activemq.usage;

public class DefaultUsageCapacity implements UsageCapacity {
    private long limit;

    public boolean isLimit(long size) {
        return size >= this.limit;
    }

    public final long getLimit() {
        return this.limit;
    }

    public final void setLimit(long limit) {
        this.limit = limit;
    }
}
