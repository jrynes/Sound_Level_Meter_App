package org.apache.activemq.advisory;

public interface ConsumerListener {
    void onConsumerEvent(ConsumerEvent consumerEvent);
}
