package org.apache.activemq.management;

public class JCAConnectionStatsImpl extends StatsImpl {
    private String connectionFactory;
    private String managedConnectionFactory;
    private TimeStatisticImpl useTime;
    private TimeStatisticImpl waitTime;

    public JCAConnectionStatsImpl(String connectionFactory, String managedConnectionFactory, TimeStatisticImpl waitTime, TimeStatisticImpl useTime) {
        this.connectionFactory = connectionFactory;
        this.managedConnectionFactory = managedConnectionFactory;
        this.waitTime = waitTime;
        this.useTime = useTime;
        addStatistic("waitTime", waitTime);
        addStatistic("useTime", useTime);
    }

    public String getConnectionFactory() {
        return this.connectionFactory;
    }

    public String getManagedConnectionFactory() {
        return this.managedConnectionFactory;
    }

    public TimeStatisticImpl getWaitTime() {
        return this.waitTime;
    }

    public TimeStatisticImpl getUseTime() {
        return this.useTime;
    }
}
