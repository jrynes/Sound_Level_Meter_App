package org.apache.activemq.openwire.v8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.JournalTransaction;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class JournalTransactionMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return JournalTransaction.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new JournalTransaction();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        JournalTransaction info = (JournalTransaction) o;
        info.setTransactionId((TransactionId) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setType(dataIn.readByte());
        info.setWasPrepared(bs.readBoolean());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        JournalTransaction info = (JournalTransaction) o;
        int rc = super.tightMarshal1(wireFormat, o, bs) + tightMarshalNestedObject1(wireFormat, info.getTransactionId(), bs);
        bs.writeBoolean(info.getWasPrepared());
        return rc + 1;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        JournalTransaction info = (JournalTransaction) o;
        tightMarshalNestedObject2(wireFormat, info.getTransactionId(), dataOut, bs);
        dataOut.writeByte(info.getType());
        bs.readBoolean();
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        JournalTransaction info = (JournalTransaction) o;
        info.setTransactionId((TransactionId) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setType(dataIn.readByte());
        info.setWasPrepared(dataIn.readBoolean());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        JournalTransaction info = (JournalTransaction) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalNestedObject(wireFormat, info.getTransactionId(), dataOut);
        dataOut.writeByte(info.getType());
        dataOut.writeBoolean(info.getWasPrepared());
    }
}
