package org.apache.activemq.advisory;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ProducerInfo;

public class ProducerStartedEvent extends ProducerEvent {
    private static final long serialVersionUID = 5088138839609391074L;
    private final transient ProducerInfo consumerInfo;

    public ProducerStartedEvent(ProducerEventSource source, ActiveMQDestination destination, ProducerInfo consumerInfo, int count) {
        super(source, destination, consumerInfo.getProducerId(), count);
        this.consumerInfo = consumerInfo;
    }

    public boolean isStarted() {
        return true;
    }

    public ProducerInfo getProducerInfo() {
        return this.consumerInfo;
    }
}
