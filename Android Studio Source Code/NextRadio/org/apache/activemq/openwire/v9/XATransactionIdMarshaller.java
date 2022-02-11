package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.XATransactionId;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class XATransactionIdMarshaller extends TransactionIdMarshaller {
    public byte getDataStructureType() {
        return XATransactionId.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new XATransactionId();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        XATransactionId info = (XATransactionId) o;
        info.setFormatId(dataIn.readInt());
        info.setGlobalTransactionId(tightUnmarshalByteArray(dataIn, bs));
        info.setBranchQualifier(tightUnmarshalByteArray(dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        XATransactionId info = (XATransactionId) o;
        return ((super.tightMarshal1(wireFormat, o, bs) + tightMarshalByteArray1(info.getGlobalTransactionId(), bs)) + tightMarshalByteArray1(info.getBranchQualifier(), bs)) + 4;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        XATransactionId info = (XATransactionId) o;
        dataOut.writeInt(info.getFormatId());
        tightMarshalByteArray2(info.getGlobalTransactionId(), dataOut, bs);
        tightMarshalByteArray2(info.getBranchQualifier(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        XATransactionId info = (XATransactionId) o;
        info.setFormatId(dataIn.readInt());
        info.setGlobalTransactionId(looseUnmarshalByteArray(dataIn));
        info.setBranchQualifier(looseUnmarshalByteArray(dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        XATransactionId info = (XATransactionId) o;
        super.looseMarshal(wireFormat, o, dataOut);
        dataOut.writeInt(info.getFormatId());
        looseMarshalByteArray(wireFormat, info.getGlobalTransactionId(), dataOut);
        looseMarshalByteArray(wireFormat, info.getBranchQualifier(), dataOut);
    }
}
