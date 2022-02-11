package org.apache.activemq.command;

import java.util.ArrayList;
import java.util.List;
import org.apache.activemq.filter.BooleanExpression;
import org.apache.activemq.state.CommandVisitor;

public class ConsumerInfo extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 5;
    public static final byte HIGH_PRIORITY = (byte) 10;
    public static final byte LOW_PRIORITY = (byte) -10;
    public static final byte NETWORK_CONSUMER_PRIORITY = (byte) -5;
    public static final byte NORMAL_PRIORITY = (byte) 0;
    protected BooleanExpression additionalPredicate;
    protected BrokerId[] brokerPath;
    protected boolean browser;
    protected ConsumerId consumerId;
    protected transient int currentPrefetchSize;
    protected ActiveMQDestination destination;
    protected boolean dispatchAsync;
    protected boolean exclusive;
    private transient long lastDeliveredSequenceId;
    protected int maximumPendingMessageLimit;
    protected transient List<ConsumerId> networkConsumerIds;
    protected transient boolean networkSubscription;
    protected boolean noLocal;
    protected boolean noRangeAcks;
    protected boolean optimizedAcknowledge;
    protected int prefetchSize;
    protected byte priority;
    protected boolean retroactive;
    protected String selector;
    protected String subscriptionName;

    public ConsumerInfo(ConsumerId consumerId) {
        this.consumerId = consumerId;
    }

    public ConsumerInfo(SessionInfo sessionInfo, long consumerId) {
        this.consumerId = new ConsumerId(sessionInfo.getSessionId(), consumerId);
    }

    public ConsumerInfo copy() {
        ConsumerInfo info = new ConsumerInfo();
        copy(info);
        return info;
    }

    public void copy(ConsumerInfo info) {
        super.copy(info);
        info.consumerId = this.consumerId;
        info.destination = this.destination;
        info.prefetchSize = this.prefetchSize;
        info.maximumPendingMessageLimit = this.maximumPendingMessageLimit;
        info.browser = this.browser;
        info.dispatchAsync = this.dispatchAsync;
        info.selector = this.selector;
        info.subscriptionName = this.subscriptionName;
        info.noLocal = this.noLocal;
        info.exclusive = this.exclusive;
        info.retroactive = this.retroactive;
        info.priority = this.priority;
        info.brokerPath = this.brokerPath;
        info.networkSubscription = this.networkSubscription;
        if (this.networkConsumerIds != null) {
            if (info.networkConsumerIds == null) {
                info.networkConsumerIds = new ArrayList();
            }
            info.networkConsumerIds.addAll(this.networkConsumerIds);
        }
    }

    public boolean isDurable() {
        return this.subscriptionName != null;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public ConsumerId getConsumerId() {
        return this.consumerId;
    }

    public void setConsumerId(ConsumerId consumerId) {
        this.consumerId = consumerId;
    }

    public boolean isBrowser() {
        return this.browser;
    }

    public void setBrowser(boolean browser) {
        this.browser = browser;
    }

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public int getPrefetchSize() {
        return this.prefetchSize;
    }

    public void setPrefetchSize(int prefetchSize) {
        this.prefetchSize = prefetchSize;
        this.currentPrefetchSize = prefetchSize;
    }

    public int getMaximumPendingMessageLimit() {
        return this.maximumPendingMessageLimit;
    }

    public void setMaximumPendingMessageLimit(int maximumPendingMessageLimit) {
        this.maximumPendingMessageLimit = maximumPendingMessageLimit;
    }

    public boolean isDispatchAsync() {
        return this.dispatchAsync;
    }

    public void setDispatchAsync(boolean dispatchAsync) {
        this.dispatchAsync = dispatchAsync;
    }

    public String getSelector() {
        return this.selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getSubscriptionName() {
        return this.subscriptionName;
    }

    public void setSubscriptionName(String durableSubscriptionId) {
        this.subscriptionName = durableSubscriptionId;
    }

    public boolean isNoLocal() {
        return this.noLocal;
    }

    public void setNoLocal(boolean noLocal) {
        this.noLocal = noLocal;
    }

    public boolean isExclusive() {
        return this.exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isRetroactive() {
        return this.retroactive;
    }

    public void setRetroactive(boolean retroactive) {
        this.retroactive = retroactive;
    }

    public RemoveInfo createRemoveCommand() {
        RemoveInfo command = new RemoveInfo(getConsumerId());
        command.setResponseRequired(isResponseRequired());
        return command;
    }

    public byte getPriority() {
        return this.priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public BrokerId[] getBrokerPath() {
        return this.brokerPath;
    }

    public void setBrokerPath(BrokerId[] brokerPath) {
        this.brokerPath = brokerPath;
    }

    public BooleanExpression getAdditionalPredicate() {
        return this.additionalPredicate;
    }

    public void setAdditionalPredicate(BooleanExpression additionalPredicate) {
        this.additionalPredicate = additionalPredicate;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processAddConsumer(this);
    }

    public boolean isNetworkSubscription() {
        return this.networkSubscription;
    }

    public void setNetworkSubscription(boolean networkSubscription) {
        this.networkSubscription = networkSubscription;
    }

    public boolean isOptimizedAcknowledge() {
        return this.optimizedAcknowledge;
    }

    public void setOptimizedAcknowledge(boolean optimizedAcknowledge) {
        this.optimizedAcknowledge = optimizedAcknowledge;
    }

    public int getCurrentPrefetchSize() {
        return this.currentPrefetchSize;
    }

    public void setCurrentPrefetchSize(int currentPrefetchSize) {
        this.currentPrefetchSize = currentPrefetchSize;
    }

    public boolean isNoRangeAcks() {
        return this.noRangeAcks;
    }

    public void setNoRangeAcks(boolean noRangeAcks) {
        this.noRangeAcks = noRangeAcks;
    }

    public synchronized void addNetworkConsumerId(ConsumerId networkConsumerId) {
        if (this.networkConsumerIds == null) {
            this.networkConsumerIds = new ArrayList();
        }
        this.networkConsumerIds.add(networkConsumerId);
    }

    public synchronized void removeNetworkConsumerId(ConsumerId networkConsumerId) {
        if (this.networkConsumerIds != null) {
            this.networkConsumerIds.remove(networkConsumerId);
            if (this.networkConsumerIds.isEmpty()) {
                this.networkConsumerIds = null;
            }
        }
    }

    public synchronized boolean isNetworkConsumersEmpty() {
        boolean z;
        z = this.networkConsumerIds == null || this.networkConsumerIds.isEmpty();
        return z;
    }

    public synchronized List<ConsumerId> getNetworkConsumerIds() {
        List<ConsumerId> result;
        result = new ArrayList();
        if (this.networkConsumerIds != null) {
            result.addAll(this.networkConsumerIds);
        }
        return result;
    }

    public ConsumerId[] getNetworkConsumerPath() {
        if (this.networkConsumerIds != null) {
            return (ConsumerId[]) this.networkConsumerIds.toArray(new ConsumerId[0]);
        }
        return null;
    }

    public void setNetworkConsumerPath(ConsumerId[] consumerPath) {
        if (consumerPath != null) {
            for (ConsumerId addNetworkConsumerId : consumerPath) {
                addNetworkConsumerId(addNetworkConsumerId);
            }
        }
    }

    public void setLastDeliveredSequenceId(long lastDeliveredSequenceId) {
        this.lastDeliveredSequenceId = lastDeliveredSequenceId;
    }

    public long getLastDeliveredSequenceId() {
        return this.lastDeliveredSequenceId;
    }
}
