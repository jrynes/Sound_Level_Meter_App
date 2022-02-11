package org.apache.activemq;

public interface LocalTransactionEventListener {
    void beginEvent();

    void commitEvent();

    void rollbackEvent();
}
