package org.apache.activemq.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.transaction.xa.Xid;
import org.apache.activemq.util.DataByteArrayInputStream;
import org.apache.activemq.util.DataByteArrayOutputStream;

public class XATransactionId extends TransactionId implements Xid, Comparable {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 112;
    final int XID_PREFIX_SIZE;
    private byte[] branchQualifier;
    private transient byte[] encodedXidBytes;
    private int formatId;
    private byte[] globalTransactionId;
    private transient int hash;
    private transient DataByteArrayOutputStream outputStream;
    private transient ArrayList<MessageAck> preparedAcks;
    private transient String transactionKey;

    public XATransactionId() {
        this.XID_PREFIX_SIZE = 16;
    }

    public XATransactionId(Xid xid) {
        this.XID_PREFIX_SIZE = 16;
        this.formatId = xid.getFormatId();
        this.globalTransactionId = xid.getGlobalTransactionId();
        this.branchQualifier = xid.getBranchQualifier();
    }

    public XATransactionId(byte[] encodedBytes) {
        this.XID_PREFIX_SIZE = 16;
        this.encodedXidBytes = encodedBytes;
        initFromEncodedBytes();
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    private void initFromEncodedBytes() {
        DataByteArrayInputStream inputStream = new DataByteArrayInputStream(this.encodedXidBytes);
        inputStream.skipBytes(10);
        this.formatId = inputStream.readInt();
        this.globalTransactionId = new byte[inputStream.readShort()];
        try {
            inputStream.read(this.globalTransactionId);
            this.branchQualifier = new byte[inputStream.available()];
            inputStream.read(this.branchQualifier);
        } catch (IOException fatal) {
            throw new RuntimeException(this + ", failed to decode:", fatal);
        }
    }

    public synchronized byte[] getEncodedXidBytes() {
        if (this.encodedXidBytes == null) {
            this.outputStream = new DataByteArrayOutputStream((this.globalTransactionId.length + 16) + this.branchQualifier.length);
            this.outputStream.position(10);
            this.outputStream.writeInt(this.formatId);
            this.outputStream.writeShort(this.globalTransactionId.length);
            try {
                this.outputStream.write(this.globalTransactionId);
                this.outputStream.write(this.branchQualifier);
                this.encodedXidBytes = this.outputStream.getData();
            } catch (IOException fatal) {
                throw new RuntimeException(this + ", failed to encode:", fatal);
            }
        }
        return this.encodedXidBytes;
    }

    public DataByteArrayOutputStream internalOutputStream() {
        return this.outputStream;
    }

    public synchronized String getTransactionKey() {
        if (this.transactionKey == null) {
            StringBuffer s = new StringBuffer();
            s.append("XID:[" + this.formatId + ",globalId=");
            for (byte toHexString : this.globalTransactionId) {
                s.append(Integer.toHexString(toHexString));
            }
            s.append(",branchId=");
            for (byte toHexString2 : this.branchQualifier) {
                s.append(Integer.toHexString(toHexString2));
            }
            s.append("]");
            this.transactionKey = s.toString();
        }
        return this.transactionKey;
    }

    public String toString() {
        return getTransactionKey();
    }

    public boolean isXATransaction() {
        return true;
    }

    public boolean isLocalTransaction() {
        return false;
    }

    public int getFormatId() {
        return this.formatId;
    }

    public byte[] getGlobalTransactionId() {
        return this.globalTransactionId;
    }

    public byte[] getBranchQualifier() {
        return this.branchQualifier;
    }

    public void setBranchQualifier(byte[] branchQualifier) {
        this.branchQualifier = branchQualifier;
        this.hash = 0;
    }

    public void setFormatId(int formatId) {
        this.formatId = formatId;
        this.hash = 0;
    }

    public void setGlobalTransactionId(byte[] globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
        this.hash = 0;
    }

    public int hashCode() {
        if (this.hash == 0) {
            this.hash = this.formatId;
            this.hash = hash(this.globalTransactionId, this.hash);
            this.hash = hash(this.branchQualifier, this.hash);
            if (this.hash == 0) {
                this.hash = 11332302;
            }
        }
        return this.hash;
    }

    private static int hash(byte[] bytes, int hash) {
        for (int i = 0; i < bytes.length; i++) {
            hash ^= bytes[i] << ((i % 4) * 8);
        }
        return hash;
    }

    public boolean equals(Object o) {
        if (o == null || o.getClass() != XATransactionId.class) {
            return false;
        }
        XATransactionId xid = (XATransactionId) o;
        if (xid.formatId == this.formatId && Arrays.equals(xid.globalTransactionId, this.globalTransactionId) && Arrays.equals(xid.branchQualifier, this.branchQualifier)) {
            return true;
        }
        return false;
    }

    public int compareTo(Object o) {
        if (o == null || o.getClass() != XATransactionId.class) {
            return -1;
        }
        return getTransactionKey().compareTo(((XATransactionId) o).getTransactionKey());
    }

    public void setPreparedAcks(ArrayList<MessageAck> preparedAcks) {
        this.preparedAcks = preparedAcks;
    }

    public ArrayList<MessageAck> getPreparedAcks() {
        return this.preparedAcks;
    }
}
