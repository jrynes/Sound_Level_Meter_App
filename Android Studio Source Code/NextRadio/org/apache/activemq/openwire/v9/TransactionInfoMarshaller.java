package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.command.TransactionInfo;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class TransactionInfoMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return (byte) 7;
    }

    public DataStructure createObject() {
        return new TransactionInfo();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        TransactionInfo info = (TransactionInfo) o;
        info.setConnectionId((ConnectionId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setTransactionId((TransactionId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setType(dataIn.readByte());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        TransactionInfo info = (TransactionInfo) o;
        return ((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getConnectionId(), bs)) + tightMarshalCachedObject1(wireFormat, info.getTransactionId(), bs)) + 1;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        TransactionInfo info = (TransactionInfo) o;
        tightMarshalCachedObject2(wireFormat, info.getConnectionId(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getTransactionId(), dataOut, bs);
        dataOut.writeByte(info.getType());
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        TransactionInfo info = (TransactionInfo) o;
        info.setConnectionId((ConnectionId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setTransactionId((TransactionId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setType(dataIn.readByte());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        TransactionInfo info = (TransactionInfo) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getConnectionId(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getTransactionId(), dataOut);
        dataOut.writeByte(info.getType());
    }
}
