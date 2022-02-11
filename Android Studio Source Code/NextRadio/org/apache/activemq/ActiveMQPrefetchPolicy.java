package org.apache.activemq;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQPrefetchPolicy implements Serializable {
    public static final int DEFAULT_DURABLE_TOPIC_PREFETCH = 100;
    public static final int DEFAULT_INPUT_STREAM_PREFETCH = 100;
    public static final int DEFAULT_OPTIMIZE_DURABLE_TOPIC_PREFETCH = 1000;
    public static final int DEFAULT_QUEUE_BROWSER_PREFETCH = 500;
    public static final int DEFAULT_QUEUE_PREFETCH = 1000;
    public static final int DEFAULT_TOPIC_PREFETCH = 32767;
    private static final Logger LOG;
    public static final int MAX_PREFETCH_SIZE = 32767;
    private int durableTopicPrefetch;
    private int inputStreamPrefetch;
    private int maximumPendingMessageLimit;
    private int optimizeDurableTopicPrefetch;
    private int queueBrowserPrefetch;
    private int queuePrefetch;
    private int topicPrefetch;

    static {
        LOG = LoggerFactory.getLogger(ActiveMQPrefetchPolicy.class);
    }

    public ActiveMQPrefetchPolicy() {
        this.queuePrefetch = DEFAULT_QUEUE_PREFETCH;
        this.queueBrowserPrefetch = DEFAULT_QUEUE_BROWSER_PREFETCH;
        this.topicPrefetch = MAX_PREFETCH_SIZE;
        this.durableTopicPrefetch = DEFAULT_INPUT_STREAM_PREFETCH;
        this.optimizeDurableTopicPrefetch = DEFAULT_QUEUE_PREFETCH;
        this.inputStreamPrefetch = DEFAULT_INPUT_STREAM_PREFETCH;
    }

    public int getDurableTopicPrefetch() {
        return this.durableTopicPrefetch;
    }

    public void setDurableTopicPrefetch(int durableTopicPrefetch) {
        this.durableTopicPrefetch = getMaxPrefetchLimit(durableTopicPrefetch);
    }

    public int getQueuePrefetch() {
        return this.queuePrefetch;
    }

    public void setQueuePrefetch(int queuePrefetch) {
        this.queuePrefetch = getMaxPrefetchLimit(queuePrefetch);
    }

    public int getQueueBrowserPrefetch() {
        return this.queueBrowserPrefetch;
    }

    public void setQueueBrowserPrefetch(int queueBrowserPrefetch) {
        this.queueBrowserPrefetch = getMaxPrefetchLimit(queueBrowserPrefetch);
    }

    public int getTopicPrefetch() {
        return this.topicPrefetch;
    }

    public void setTopicPrefetch(int topicPrefetch) {
        this.topicPrefetch = getMaxPrefetchLimit(topicPrefetch);
    }

    public int getOptimizeDurableTopicPrefetch() {
        return this.optimizeDurableTopicPrefetch;
    }

    public void setOptimizeDurableTopicPrefetch(int optimizeAcknowledgePrefetch) {
        this.optimizeDurableTopicPrefetch = optimizeAcknowledgePrefetch;
    }

    public int getMaximumPendingMessageLimit() {
        return this.maximumPendingMessageLimit;
    }

    public void setMaximumPendingMessageLimit(int maximumPendingMessageLimit) {
        this.maximumPendingMessageLimit = maximumPendingMessageLimit;
    }

    private int getMaxPrefetchLimit(int value) {
        int result = Math.min(value, MAX_PREFETCH_SIZE);
        if (result < value) {
            LOG.warn("maximum prefetch limit has been reset from " + value + " to " + MAX_PREFETCH_SIZE);
        }
        return result;
    }

    public void setAll(int i) {
        this.durableTopicPrefetch = i;
        this.queueBrowserPrefetch = i;
        this.queuePrefetch = i;
        this.topicPrefetch = i;
        this.inputStreamPrefetch = 1;
        this.optimizeDurableTopicPrefetch = i;
    }

    public int getInputStreamPrefetch() {
        return this.inputStreamPrefetch;
    }

    public void setInputStreamPrefetch(int inputStreamPrefetch) {
        this.inputStreamPrefetch = getMaxPrefetchLimit(inputStreamPrefetch);
    }

    public boolean equals(Object object) {
        if (!(object instanceof ActiveMQPrefetchPolicy)) {
            return false;
        }
        ActiveMQPrefetchPolicy other = (ActiveMQPrefetchPolicy) object;
        if (this.queuePrefetch == other.queuePrefetch && this.queueBrowserPrefetch == other.queueBrowserPrefetch && this.topicPrefetch == other.topicPrefetch && this.durableTopicPrefetch == other.durableTopicPrefetch && this.optimizeDurableTopicPrefetch == other.optimizeDurableTopicPrefetch && this.inputStreamPrefetch == other.inputStreamPrefetch) {
            return true;
        }
        return false;
    }
}
