package org.apache.activemq.management;

public class JCAStatsImpl extends StatsImpl {
    private JCAConnectionPoolStatsImpl[] connectionPoolStats;
    private JCAConnectionStatsImpl[] connectionStats;

    public JCAStatsImpl(JCAConnectionStatsImpl[] connectionStats, JCAConnectionPoolStatsImpl[] connectionPoolStats) {
        this.connectionStats = connectionStats;
        this.connectionPoolStats = connectionPoolStats;
    }

    public JCAConnectionStatsImpl[] getConnections() {
        return this.connectionStats;
    }

    public JCAConnectionPoolStatsImpl[] getConnectionPools() {
        return this.connectionPoolStats;
    }
}
