package org.apache.activemq.command;

import java.io.IOException;
import org.apache.activemq.state.CommandVisitor;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class TransactionInfo extends BaseCommand {
    public static final byte BEGIN = (byte) 0;
    public static final byte COMMIT_ONE_PHASE = (byte) 2;
    public static final byte COMMIT_TWO_PHASE = (byte) 3;
    public static final byte DATA_STRUCTURE_TYPE = (byte) 7;
    public static final byte END = (byte) 7;
    public static final byte FORGET = (byte) 6;
    public static final byte PREPARE = (byte) 1;
    public static final byte RECOVER = (byte) 5;
    public static final byte ROLLBACK = (byte) 4;
    protected ConnectionId connectionId;
    protected TransactionId transactionId;
    protected byte type;

    public TransactionInfo(ConnectionId connectionId, TransactionId transactionId, byte type) {
        this.connectionId = connectionId;
        this.transactionId = transactionId;
        this.type = type;
    }

    public byte getDataStructureType() {
        return END;
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId(ConnectionId connectionId) {
        this.connectionId = connectionId;
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

    public Response visit(CommandVisitor visitor) throws Exception {
        switch (this.type) {
            case Tokenizer.EOF /*0*/:
                return visitor.processBeginTransaction(this);
            case Zone.PRIMARY /*1*/:
                return visitor.processPrepareTransaction(this);
            case Zone.SECONDARY /*2*/:
                return visitor.processCommitTransactionOnePhase(this);
            case Protocol.GGP /*3*/:
                return visitor.processCommitTransactionTwoPhase(this);
            case Type.MF /*4*/:
                return visitor.processRollbackTransaction(this);
            case Service.RJE /*5*/:
                return visitor.processRecoverTransactions(this);
            case Protocol.TCP /*6*/:
                return visitor.processForgetTransaction(this);
            case Service.ECHO /*7*/:
                return visitor.processEndTransaction(this);
            default:
                throw new IOException("Transaction info type unknown: " + this.type);
        }
    }
}
