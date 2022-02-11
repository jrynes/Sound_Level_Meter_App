package org.apache.activemq.state;

import org.apache.activemq.command.ProducerInfo;

public class ProducerState {
    final ProducerInfo info;
    private TransactionState transactionState;

    public ProducerState(ProducerInfo info) {
        this.info = info;
    }

    public String toString() {
        return this.info.toString();
    }

    public ProducerInfo getInfo() {
        return this.info;
    }

    public void setTransactionState(TransactionState transactionState) {
        this.transactionState = transactionState;
    }

    public TransactionState getTransactionState() {
        return this.transactionState;
    }
}
