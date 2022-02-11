package org.apache.activemq.management;

import java.util.List;
import org.apache.activemq.util.IndentPrinter;

public class JMSSessionStatsImpl extends StatsImpl {
    private List consumers;
    private CountStatisticImpl durableSubscriptionCount;
    private CountStatisticImpl expiredMessageCount;
    private CountStatisticImpl messageCount;
    private TimeStatisticImpl messageRateTime;
    private TimeStatisticImpl messageWaitTime;
    private CountStatisticImpl pendingMessageCount;
    private List producers;

    public JMSSessionStatsImpl(List producers, List consumers) {
        this.producers = producers;
        this.consumers = consumers;
        this.messageCount = new CountStatisticImpl("messageCount", "Number of messages exchanged");
        this.pendingMessageCount = new CountStatisticImpl("pendingMessageCount", "Number of pending messages");
        this.expiredMessageCount = new CountStatisticImpl("expiredMessageCount", "Number of expired messages");
        this.messageWaitTime = new TimeStatisticImpl("messageWaitTime", "Time spent by a message before being delivered");
        this.durableSubscriptionCount = new CountStatisticImpl("durableSubscriptionCount", "The number of durable subscriptions");
        this.messageWaitTime = new TimeStatisticImpl("messageWaitTime", "Time spent by a message before being delivered");
        this.messageRateTime = new TimeStatisticImpl("messageRateTime", "Time taken to process a message (thoughtput rate)");
        addStatistic("messageCount", this.messageCount);
        addStatistic("pendingMessageCount", this.pendingMessageCount);
        addStatistic("expiredMessageCount", this.expiredMessageCount);
        addStatistic("messageWaitTime", this.messageWaitTime);
        addStatistic("durableSubscriptionCount", this.durableSubscriptionCount);
        addStatistic("messageRateTime", this.messageRateTime);
    }

    public JMSProducerStatsImpl[] getProducers() {
        Object[] producerArray = this.producers.toArray();
        int size = producerArray.length;
        JMSProducerStatsImpl[] answer = new JMSProducerStatsImpl[size];
        for (int i = 0; i < size; i++) {
            answer[i] = producerArray[i].getProducerStats();
        }
        return answer;
    }

    public JMSConsumerStatsImpl[] getConsumers() {
        Object[] consumerArray = this.consumers.toArray();
        int size = consumerArray.length;
        JMSConsumerStatsImpl[] answer = new JMSConsumerStatsImpl[size];
        for (int i = 0; i < size; i++) {
            answer[i] = consumerArray[i].getConsumerStats();
        }
        return answer;
    }

    public void reset() {
        super.reset();
        for (JMSConsumerStatsImpl reset : getConsumers()) {
            reset.reset();
        }
        for (JMSProducerStatsImpl reset2 : getProducers()) {
            reset2.reset();
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (JMSConsumerStatsImpl enabled2 : getConsumers()) {
            enabled2.setEnabled(enabled);
        }
        for (JMSProducerStatsImpl enabled3 : getProducers()) {
            enabled3.setEnabled(enabled);
        }
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

    public TimeStatisticImpl getMessageWaitTime() {
        return this.messageWaitTime;
    }

    public CountStatisticImpl getDurableSubscriptionCount() {
        return this.durableSubscriptionCount;
    }

    public TimeStatisticImpl getMessageRateTime() {
        return this.messageRateTime;
    }

    public String toString() {
        int i;
        StringBuffer buffer = new StringBuffer(" ");
        buffer.append(this.messageCount);
        buffer.append(" ");
        buffer.append(this.messageRateTime);
        buffer.append(" ");
        buffer.append(this.pendingMessageCount);
        buffer.append(" ");
        buffer.append(this.expiredMessageCount);
        buffer.append(" ");
        buffer.append(this.messageWaitTime);
        buffer.append(" ");
        buffer.append(this.durableSubscriptionCount);
        buffer.append(" producers{ ");
        JMSProducerStatsImpl[] producerArray = getProducers();
        for (i = 0; i < producerArray.length; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(Integer.toString(i));
            buffer.append(" = ");
            buffer.append(producerArray[i]);
        }
        buffer.append(" } consumers{ ");
        JMSConsumerStatsImpl[] consumerArray = getConsumers();
        for (i = 0; i < consumerArray.length; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(Integer.toString(i));
            buffer.append(" = ");
            buffer.append(consumerArray[i]);
        }
        buffer.append(" }");
        return buffer.toString();
    }

    public void dump(IndentPrinter out) {
        out.printIndent();
        out.println(this.messageCount);
        out.printIndent();
        out.println(this.messageRateTime);
        out.printIndent();
        out.println(this.pendingMessageCount);
        out.printIndent();
        out.println(this.expiredMessageCount);
        out.printIndent();
        out.println(this.messageWaitTime);
        out.printIndent();
        out.println(this.durableSubscriptionCount);
        out.println();
        out.printIndent();
        out.println("producers {");
        out.incrementIndent();
        JMSProducerStatsImpl[] producerArray = getProducers();
        for (JMSProducerStatsImpl producer : producerArray) {
            producer.dump(out);
        }
        out.decrementIndent();
        out.printIndent();
        out.println("}");
        out.printIndent();
        out.println("consumers {");
        out.incrementIndent();
        JMSConsumerStatsImpl[] consumerArray = getConsumers();
        for (JMSConsumerStatsImpl consumer : consumerArray) {
            consumer.dump(out);
        }
        out.decrementIndent();
        out.printIndent();
        out.println("}");
    }

    public void onCreateDurableSubscriber() {
        this.durableSubscriptionCount.increment();
    }

    public void onRemoveDurableSubscriber() {
        this.durableSubscriptionCount.decrement();
    }
}
