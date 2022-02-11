package org.apache.activemq.advisory;

import java.util.EventObject;
import javax.jms.Destination;
import org.apache.activemq.command.ProducerId;

public abstract class ProducerEvent extends EventObject {
    private static final long serialVersionUID = 2442156576867593780L;
    private final Destination destination;
    private final int producerCount;
    private final ProducerId producerId;

    public abstract boolean isStarted();

    public ProducerEvent(ProducerEventSource source, Destination destination, ProducerId producerId, int producerCount) {
        super(source);
        this.destination = destination;
        this.producerId = producerId;
        this.producerCount = producerCount;
    }

    public ProducerEventSource getAdvisor() {
        return (ProducerEventSource) getSource();
    }

    public Destination getDestination() {
        return this.destination;
    }

    public int getProducerCount() {
        return this.producerCount;
    }

    public ProducerId getProducerId() {
        return this.producerId;
    }
}
