package org.apache.activemq.management;

public class JCAConnectionPoolStatsImpl extends JCAConnectionStatsImpl {
    private CountStatisticImpl closeCount;
    private CountStatisticImpl createCount;
    private BoundedRangeStatisticImpl freePoolSize;
    private BoundedRangeStatisticImpl poolSize;
    private RangeStatisticImpl waitingThreadCount;

    public JCAConnectionPoolStatsImpl(String connectionFactory, String managedConnectionFactory, TimeStatisticImpl waitTime, TimeStatisticImpl useTime, CountStatisticImpl closeCount, CountStatisticImpl createCount, BoundedRangeStatisticImpl freePoolSize, BoundedRangeStatisticImpl poolSize, RangeStatisticImpl waitingThreadCount) {
        super(connectionFactory, managedConnectionFactory, waitTime, useTime);
        this.closeCount = closeCount;
        this.createCount = createCount;
        this.freePoolSize = freePoolSize;
        this.poolSize = poolSize;
        this.waitingThreadCount = waitingThreadCount;
        addStatistic("freePoolSize", freePoolSize);
        addStatistic("poolSize", poolSize);
        addStatistic("waitingThreadCount", waitingThreadCount);
    }

    public CountStatisticImpl getCloseCount() {
        return this.closeCount;
    }

    public CountStatisticImpl getCreateCount() {
        return this.createCount;
    }

    public BoundedRangeStatisticImpl getFreePoolSize() {
        return this.freePoolSize;
    }

    public BoundedRangeStatisticImpl getPoolSize() {
        return this.poolSize;
    }

    public RangeStatisticImpl getWaitingThreadCount() {
        return this.waitingThreadCount;
    }
}
