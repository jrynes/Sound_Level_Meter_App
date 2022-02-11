package org.apache.activemq.command;

import org.apache.activemq.transport.stomp.Stomp.Headers;

public class LocalTransactionId extends TransactionId implements Comparable<LocalTransactionId> {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 111;
    protected ConnectionId connectionId;
    private transient int hashCode;
    private transient String transactionKey;
    protected long value;

    public LocalTransactionId(ConnectionId connectionId, long transactionId) {
        this.connectionId = connectionId;
        this.value = transactionId;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isXATransaction() {
        return false;
    }

    public boolean isLocalTransaction() {
        return true;
    }

    public String getTransactionKey() {
        if (this.transactionKey == null) {
            this.transactionKey = "TX:" + this.connectionId + Headers.SEPERATOR + this.value;
        }
        return this.transactionKey;
    }

    public String toString() {
        return getTransactionKey();
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.connectionId.hashCode() ^ ((int) this.value);
        }
        return this.hashCode;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != LocalTransactionId.class) {
            return false;
        }
        LocalTransactionId tx = (LocalTransactionId) o;
        if (this.value == tx.value && this.connectionId.equals(tx.connectionId)) {
            return true;
        }
        return false;
    }

    public int compareTo(LocalTransactionId o) {
        int result = this.connectionId.compareTo(o.connectionId);
        if (result == 0) {
            return (int) (this.value - o.value);
        }
        return result;
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long transactionId) {
        this.value = transactionId;
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId(ConnectionId connectionId) {
        this.connectionId = connectionId;
    }
}
