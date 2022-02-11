package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.SessionId;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class SessionIdMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return SessionId.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new SessionId();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        SessionId info = (SessionId) o;
        info.setConnectionId(tightUnmarshalString(dataIn, bs));
        info.setValue(tightUnmarshalLong(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        SessionId info = (SessionId) o;
        return ((super.tightMarshal1(wireFormat, o, bs) + tightMarshalString1(info.getConnectionId(), bs)) + tightMarshalLong1(wireFormat, info.getValue(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        SessionId info = (SessionId) o;
        tightMarshalString2(info.getConnectionId(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getValue(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        SessionId info = (SessionId) o;
        info.setConnectionId(looseUnmarshalString(dataIn));
        info.setValue(looseUnmarshalLong(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        SessionId info = (SessionId) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalString(info.getConnectionId(), dataOut);
        looseMarshalLong(wireFormat, info.getValue(), dataOut);
    }
}
