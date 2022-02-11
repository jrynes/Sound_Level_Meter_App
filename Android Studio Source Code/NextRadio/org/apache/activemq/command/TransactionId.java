package org.apache.activemq.command;

public abstract class TransactionId implements DataStructure {
    public abstract String getTransactionKey();

    public abstract boolean isLocalTransaction();

    public abstract boolean isXATransaction();

    public boolean isMarshallAware() {
        return false;
    }
}
