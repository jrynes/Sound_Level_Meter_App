package org.apache.activemq.command;

import org.apache.activemq.util.IntrospectionSupport;

public class JournalTransaction implements DataStructure {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 54;
    public static final byte LOCAL_COMMIT = (byte) 4;
    public static final byte LOCAL_ROLLBACK = (byte) 5;
    public static final byte XA_COMMIT = (byte) 2;
    public static final byte XA_PREPARE = (byte) 1;
    public static final byte XA_ROLLBACK = (byte) 3;
    public TransactionId transactionId;
    public byte type;
    public boolean wasPrepared;

    public JournalTransaction(byte type, TransactionId transactionId, boolean wasPrepared) {
        this.type = type;
        this.transactionId = transactionId;
        this.wasPrepared = wasPrepared;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public TransactionId getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public boolean getWasPrepared() {
        return this.wasPrepared;
    }

    public void setWasPrepared(boolean wasPrepared) {
        this.wasPrepared = wasPrepared;
    }

    public boolean isMarshallAware() {
        return false;
    }

    public String toString() {
        return IntrospectionSupport.toString(this, JournalTransaction.class);
    }
}
