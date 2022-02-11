package org.apache.activemq.advisory;

public interface ProducerListener {
    void onProducerEvent(ProducerEvent producerEvent);
}
