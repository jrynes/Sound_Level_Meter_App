package org.apache.activemq.openwire.v8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ProducerIdMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return ProducerId.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new ProducerId();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ProducerId info = (ProducerId) o;
        info.setConnectionId(tightUnmarshalString(dataIn, bs));
        info.setValue(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.setSessionId(tightUnmarshalLong(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        ProducerId info = (ProducerId) o;
        return (((super.tightMarshal1(wireFormat, o, bs) + tightMarshalString1(info.getConnectionId(), bs)) + tightMarshalLong1(wireFormat, info.getValue(), bs)) + tightMarshalLong1(wireFormat, info.getSessionId(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        ProducerId info = (ProducerId) o;
        tightMarshalString2(info.getConnectionId(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getValue(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getSessionId(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ProducerId info = (ProducerId) o;
        info.setConnectionId(looseUnmarshalString(dataIn));
        info.setValue(looseUnmarshalLong(wireFormat, dataIn));
        info.setSessionId(looseUnmarshalLong(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ProducerId info = (ProducerId) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalString(info.getConnectionId(), dataOut);
        looseMarshalLong(wireFormat, info.getValue(), dataOut);
        looseMarshalLong(wireFormat, info.getSessionId(), dataOut);
    }
}
