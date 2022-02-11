package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.RemoveInfo;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.util.MarshallingSupport;

public class RemoveInfoMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return MarshallingSupport.LIST_TYPE;
    }

    public DataStructure createObject() {
        return new RemoveInfo();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        RemoveInfo info = (RemoveInfo) o;
        info.setObjectId(tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setLastDeliveredSequenceId(tightUnmarshalLong(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        RemoveInfo info = (RemoveInfo) o;
        return ((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getObjectId(), bs)) + tightMarshalLong1(wireFormat, info.getLastDeliveredSequenceId(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        RemoveInfo info = (RemoveInfo) o;
        tightMarshalCachedObject2(wireFormat, info.getObjectId(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getLastDeliveredSequenceId(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        RemoveInfo info = (RemoveInfo) o;
        info.setObjectId(looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setLastDeliveredSequenceId(looseUnmarshalLong(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        RemoveInfo info = (RemoveInfo) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getObjectId(), dataOut);
        looseMarshalLong(wireFormat, info.getLastDeliveredSequenceId(), dataOut);
    }
}
