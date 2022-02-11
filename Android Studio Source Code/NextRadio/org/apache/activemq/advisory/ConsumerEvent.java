package org.apache.activemq.advisory;

import java.util.EventObject;
import javax.jms.Destination;
import org.apache.activemq.command.ConsumerId;

public abstract class ConsumerEvent extends EventObject {
    private static final long serialVersionUID = 2442156576867593780L;
    private final int consumerCount;
    private final ConsumerId consumerId;
    private final Destination destination;

    public abstract boolean isStarted();

    public ConsumerEvent(ConsumerEventSource source, Destination destination, ConsumerId consumerId, int consumerCount) {
        super(source);
        this.destination = destination;
        this.consumerId = consumerId;
        this.consumerCount = consumerCount;
    }

    public ConsumerEventSource getAdvisor() {
        return (ConsumerEventSource) getSource();
    }

    public Destination getDestination() {
        return this.destination;
    }

    public int getConsumerCount() {
        return this.consumerCount;
    }

    public ConsumerId getConsumerId() {
        return this.consumerId;
    }
}
