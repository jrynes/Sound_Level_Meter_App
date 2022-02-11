package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.LocalTransactionId;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class LocalTransactionIdMarshaller extends TransactionIdMarshaller {
    public byte getDataStructureType() {
        return LocalTransactionId.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new LocalTransactionId();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        LocalTransactionId info = (LocalTransactionId) o;
        info.setValue(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.setConnectionId((ConnectionId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        LocalTransactionId info = (LocalTransactionId) o;
        return ((super.tightMarshal1(wireFormat, o, bs) + tightMarshalLong1(wireFormat, info.getValue(), bs)) + tightMarshalCachedObject1(wireFormat, info.getConnectionId(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        LocalTransactionId info = (LocalTransactionId) o;
        tightMarshalLong2(wireFormat, info.getValue(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getConnectionId(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        LocalTransactionId info = (LocalTransactionId) o;
        info.setValue(looseUnmarshalLong(wireFormat, dataIn));
        info.setConnectionId((ConnectionId) looseUnmarsalCachedObject(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        LocalTransactionId info = (LocalTransactionId) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalLong(wireFormat, info.getValue(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getConnectionId(), dataOut);
    }
}
