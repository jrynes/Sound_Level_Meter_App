package org.apache.activemq.advisory;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerInfo;

public class ConsumerStartedEvent extends ConsumerEvent {
    private static final long serialVersionUID = 5088138839609391074L;
    private final transient ConsumerInfo consumerInfo;

    public ConsumerStartedEvent(ConsumerEventSource source, ActiveMQDestination destination, ConsumerInfo consumerInfo, int count) {
        super(source, destination, consumerInfo.getConsumerId(), count);
        this.consumerInfo = consumerInfo;
    }

    public boolean isStarted() {
        return true;
    }

    public ConsumerInfo getConsumerInfo() {
        return this.consumerInfo;
    }
}
