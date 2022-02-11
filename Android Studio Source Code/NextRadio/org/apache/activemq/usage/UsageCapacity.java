package org.apache.activemq.usage;

public interface UsageCapacity {
    long getLimit();

    boolean isLimit(long j);

    void setLimit(long j);
}
