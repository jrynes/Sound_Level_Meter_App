package org.apache.activemq.state;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.ProducerInfo;
import org.apache.activemq.command.SessionInfo;

public class SessionState {
    private final Map<ConsumerId, ConsumerState> consumers;
    final SessionInfo info;
    private final Map<ProducerId, ProducerState> producers;
    private final AtomicBoolean shutdown;

    public SessionState(SessionInfo info) {
        this.producers = new ConcurrentHashMap();
        this.consumers = new ConcurrentHashMap();
        this.shutdown = new AtomicBoolean(false);
        this.info = info;
    }

    public String toString() {
        return this.info.toString();
    }

    public void addProducer(ProducerInfo info) {
        checkShutdown();
        this.producers.put(info.getProducerId(), new ProducerState(info));
    }

    public ProducerState removeProducer(ProducerId id) {
        ProducerState producerState = (ProducerState) this.producers.remove(id);
        if (!(producerState == null || producerState.getTransactionState() == null)) {
            producerState.getTransactionState().addProducerState(producerState);
        }
        return producerState;
    }

    public void addConsumer(ConsumerInfo info) {
        checkShutdown();
        this.consumers.put(info.getConsumerId(), new ConsumerState(info));
    }

    public ConsumerState removeConsumer(ConsumerId id) {
        return (ConsumerState) this.consumers.remove(id);
    }

    public SessionInfo getInfo() {
        return this.info;
    }

    public Set<ConsumerId> getConsumerIds() {
        return this.consumers.keySet();
    }

    public Set<ProducerId> getProducerIds() {
        return this.producers.keySet();
    }

    public Collection<ProducerState> getProducerStates() {
        return this.producers.values();
    }

    public ProducerState getProducerState(ProducerId producerId) {
        return (ProducerState) this.producers.get(producerId);
    }

    public Collection<ConsumerState> getConsumerStates() {
        return this.consumers.values();
    }

    public ConsumerState getConsumerState(ConsumerId consumerId) {
        return (ConsumerState) this.consumers.get(consumerId);
    }

    private void checkShutdown() {
        if (this.shutdown.get()) {
            throw new IllegalStateException("Disposed");
        }
    }

    public void shutdown() {
        this.shutdown.set(false);
    }
}
