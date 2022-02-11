package org.apache.activemq.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.TransactionId;

public class TransactionState {
    private final List<Command> commands;
    private final TransactionId id;
    private boolean prepared;
    private int preparedResult;
    private final Map<ProducerId, ProducerState> producers;
    private final AtomicBoolean shutdown;

    public TransactionState(TransactionId id) {
        this.commands = new ArrayList();
        this.shutdown = new AtomicBoolean(false);
        this.producers = new ConcurrentHashMap();
        this.id = id;
    }

    public String toString() {
        return this.id.toString();
    }

    public void addCommand(Command operation) {
        checkShutdown();
        this.commands.add(operation);
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    private void checkShutdown() {
        if (this.shutdown.get()) {
            throw new IllegalStateException("Disposed");
        }
    }

    public void shutdown() {
        this.shutdown.set(false);
    }

    public TransactionId getId() {
        return this.id;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    public boolean isPrepared() {
        return this.prepared;
    }

    public void setPreparedResult(int preparedResult) {
        this.preparedResult = preparedResult;
    }

    public int getPreparedResult() {
        return this.preparedResult;
    }

    public void addProducerState(ProducerState producerState) {
        if (producerState != null) {
            this.producers.put(producerState.getInfo().getProducerId(), producerState);
        }
    }

    public Map<ProducerId, ProducerState> getProducerStates() {
        return this.producers;
    }
}
