package org.apache.activemq.advisory;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerId;

public class ConsumerStoppedEvent extends ConsumerEvent {
    private static final long serialVersionUID = 5378835541037193206L;

    public ConsumerStoppedEvent(ConsumerEventSource source, ActiveMQDestination destination, ConsumerId consumerId, int count) {
        super(source, destination, consumerId, count);
    }

    public boolean isStarted() {
        return false;
    }
}
