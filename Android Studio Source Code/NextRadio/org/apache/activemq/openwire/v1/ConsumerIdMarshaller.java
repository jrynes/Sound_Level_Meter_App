package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ConsumerIdMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return ConsumerId.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new ConsumerId();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ConsumerId info = (ConsumerId) o;
        info.setConnectionId(tightUnmarshalString(dataIn, bs));
        info.setSessionId(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.setValue(tightUnmarshalLong(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        ConsumerId info = (ConsumerId) o;
        return (((super.tightMarshal1(wireFormat, o, bs) + tightMarshalString1(info.getConnectionId(), bs)) + tightMarshalLong1(wireFormat, info.getSessionId(), bs)) + tightMarshalLong1(wireFormat, info.getValue(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        ConsumerId info = (ConsumerId) o;
        tightMarshalString2(info.getConnectionId(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getSessionId(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getValue(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ConsumerId info = (ConsumerId) o;
        info.setConnectionId(looseUnmarshalString(dataIn));
        info.setSessionId(looseUnmarshalLong(wireFormat, dataIn));
        info.setValue(looseUnmarshalLong(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ConsumerId info = (ConsumerId) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalString(info.getConnectionId(), dataOut);
        looseMarshalLong(wireFormat, info.getSessionId(), dataOut);
        looseMarshalLong(wireFormat, info.getValue(), dataOut);
    }
}
