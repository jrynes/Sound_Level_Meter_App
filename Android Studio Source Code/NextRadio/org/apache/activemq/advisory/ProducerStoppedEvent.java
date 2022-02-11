package org.apache.activemq.advisory;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ProducerId;

public class ProducerStoppedEvent extends ProducerEvent {
    private static final long serialVersionUID = 5378835541037193206L;

    public ProducerStoppedEvent(ProducerEventSource source, ActiveMQDestination destination, ProducerId consumerId, int count) {
        super(source, destination, consumerId, count);
    }

    public boolean isStarted() {
        return false;
    }
}
