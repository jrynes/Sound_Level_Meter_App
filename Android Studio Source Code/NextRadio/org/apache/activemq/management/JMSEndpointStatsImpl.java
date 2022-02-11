package org.apache.activemq.management;

import org.apache.activemq.util.IndentPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSEndpointStatsImpl extends StatsImpl {
    private static final Logger LOG;
    protected CountStatisticImpl expiredMessageCount;
    protected CountStatisticImpl messageCount;
    protected TimeStatisticImpl messageRateTime;
    protected TimeStatisticImpl messageWaitTime;
    protected CountStatisticImpl pendingMessageCount;

    static {
        LOG = LoggerFactory.getLogger(JMSEndpointStatsImpl.class);
    }

    public JMSEndpointStatsImpl(JMSSessionStatsImpl sessionStats) {
        this();
        setParent(this.messageCount, sessionStats.getMessageCount());
        setParent(this.pendingMessageCount, sessionStats.getPendingMessageCount());
        setParent(this.expiredMessageCount, sessionStats.getExpiredMessageCount());
        setParent(this.messageWaitTime, sessionStats.getMessageWaitTime());
        setParent(this.messageRateTime, sessionStats.getMessageRateTime());
    }

    public JMSEndpointStatsImpl() {
        this(new CountStatisticImpl("messageCount", "Number of messages processed"), new CountStatisticImpl("pendingMessageCount", "Number of pending messages"), new CountStatisticImpl("expiredMessageCount", "Number of expired messages"), new TimeStatisticImpl("messageWaitTime", "Time spent by a message before being delivered"), new TimeStatisticImpl("messageRateTime", "Time taken to process a message (thoughtput rate)"));
    }

    public JMSEndpointStatsImpl(CountStatisticImpl messageCount, CountStatisticImpl pendingMessageCount, CountStatisticImpl expiredMessageCount, TimeStatisticImpl messageWaitTime, TimeStatisticImpl messageRateTime) {
        this.messageCount = messageCount;
        this.pendingMessageCount = pendingMessageCount;
        this.expiredMessageCount = expiredMessageCount;
        this.messageWaitTime = messageWaitTime;
        this.messageRateTime = messageRateTime;
        addStatistic("messageCount", messageCount);
        addStatistic("pendingMessageCount", pendingMessageCount);
        addStatistic("expiredMessageCount", expiredMessageCount);
        addStatistic("messageWaitTime", messageWaitTime);
        addStatistic("messageRateTime", messageRateTime);
    }

    public synchronized void reset() {
        super.reset();
        this.messageCount.reset();
        this.messageRateTime.reset();
        this.pendingMessageCount.reset();
        this.expiredMessageCount.reset();
        this.messageWaitTime.reset();
    }

    public CountStatisticImpl getMessageCount() {
        return this.messageCount;
    }

    public CountStatisticImpl getPendingMessageCount() {
        return this.pendingMessageCount;
    }

    public CountStatisticImpl getExpiredMessageCount() {
        return this.expiredMessageCount;
    }

    public TimeStatisticImpl getMessageRateTime() {
        return this.messageRateTime;
    }

    public TimeStatisticImpl getMessageWaitTime() {
        return this.messageWaitTime;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.messageCount);
        buffer.append(" ");
        buffer.append(this.messageRateTime);
        buffer.append(" ");
        buffer.append(this.pendingMessageCount);
        buffer.append(" ");
        buffer.append(this.expiredMessageCount);
        buffer.append(" ");
        buffer.append(this.messageWaitTime);
        return buffer.toString();
    }

    public void onMessage() {
        if (this.enabled) {
            long start = this.messageCount.getLastSampleTime();
            this.messageCount.increment();
            this.messageRateTime.addTime(this.messageCount.getLastSampleTime() - start);
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.messageCount.setEnabled(enabled);
        this.messageRateTime.setEnabled(enabled);
        this.pendingMessageCount.setEnabled(enabled);
        this.expiredMessageCount.setEnabled(enabled);
        this.messageWaitTime.setEnabled(enabled);
    }

    public void dump(IndentPrinter out) {
        out.printIndent();
        out.println(this.messageCount);
        out.printIndent();
        out.println(this.messageRateTime);
        out.printIndent();
        out.println(this.pendingMessageCount);
        out.printIndent();
        out.println(this.messageRateTime);
        out.printIndent();
        out.println(this.expiredMessageCount);
        out.printIndent();
        out.println(this.messageWaitTime);
    }

    protected void setParent(CountStatisticImpl child, CountStatisticImpl parent) {
        if ((child instanceof CountStatisticImpl) && (parent instanceof CountStatisticImpl)) {
            child.setParent(parent);
        } else {
            LOG.warn("Cannot associate endpoint counters with session level counters as they are not both CountStatisticImpl clases. Endpoint: " + child + " session: " + parent);
        }
    }

    protected void setParent(TimeStatisticImpl child, TimeStatisticImpl parent) {
        if ((child instanceof TimeStatisticImpl) && (parent instanceof TimeStatisticImpl)) {
            child.setParent(parent);
        } else {
            LOG.warn("Cannot associate endpoint counters with session level counters as they are not both TimeStatisticImpl clases. Endpoint: " + child + " session: " + parent);
        }
    }
}
