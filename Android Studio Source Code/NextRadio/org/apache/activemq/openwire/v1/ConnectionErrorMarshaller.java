package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ConnectionError;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ConnectionErrorMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return ConnectionError.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new ConnectionError();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ConnectionError info = (ConnectionError) o;
        info.setException(tightUnmarsalThrowable(wireFormat, dataIn, bs));
        info.setConnectionId((ConnectionId) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        ConnectionError info = (ConnectionError) o;
        return ((super.tightMarshal1(wireFormat, o, bs) + tightMarshalThrowable1(wireFormat, info.getException(), bs)) + tightMarshalNestedObject1(wireFormat, info.getConnectionId(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        ConnectionError info = (ConnectionError) o;
        tightMarshalThrowable2(wireFormat, info.getException(), dataOut, bs);
        tightMarshalNestedObject2(wireFormat, info.getConnectionId(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ConnectionError info = (ConnectionError) o;
        info.setException(looseUnmarsalThrowable(wireFormat, dataIn));
        info.setConnectionId((ConnectionId) looseUnmarsalNestedObject(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ConnectionError info = (ConnectionError) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalThrowable(wireFormat, info.getException(), dataOut);
        looseMarshalNestedObject(wireFormat, info.getConnectionId(), dataOut);
    }
}
