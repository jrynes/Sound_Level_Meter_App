package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.BrokerId;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.DestinationInfo;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class DestinationInfoMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return (byte) 8;
    }

    public DataStructure createObject() {
        return new DestinationInfo();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        DestinationInfo info = (DestinationInfo) o;
        info.setConnectionId((ConnectionId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setDestination((ActiveMQDestination) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setOperationType(dataIn.readByte());
        info.setTimeout(tightUnmarshalLong(wireFormat, dataIn, bs));
        if (bs.readBoolean()) {
            short size = dataIn.readShort();
            BrokerId[] value = new BrokerId[size];
            for (short i = (short) 0; i < size; i++) {
                value[i] = (BrokerId) tightUnmarsalNestedObject(wireFormat, dataIn, bs);
            }
            info.setBrokerPath(value);
            return;
        }
        info.setBrokerPath(null);
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        DestinationInfo info = (DestinationInfo) o;
        return ((((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getConnectionId(), bs)) + tightMarshalCachedObject1(wireFormat, info.getDestination(), bs)) + tightMarshalLong1(wireFormat, info.getTimeout(), bs)) + tightMarshalObjectArray1(wireFormat, info.getBrokerPath(), bs)) + 1;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        DestinationInfo info = (DestinationInfo) o;
        tightMarshalCachedObject2(wireFormat, info.getConnectionId(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getDestination(), dataOut, bs);
        dataOut.writeByte(info.getOperationType());
        tightMarshalLong2(wireFormat, info.getTimeout(), dataOut, bs);
        tightMarshalObjectArray2(wireFormat, info.getBrokerPath(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        DestinationInfo info = (DestinationInfo) o;
        info.setConnectionId((ConnectionId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setDestination((ActiveMQDestination) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setOperationType(dataIn.readByte());
        info.setTimeout(looseUnmarshalLong(wireFormat, dataIn));
        if (dataIn.readBoolean()) {
            short size = dataIn.readShort();
            BrokerId[] value = new BrokerId[size];
            for (short i = (short) 0; i < size; i++) {
                value[i] = (BrokerId) looseUnmarsalNestedObject(wireFormat, dataIn);
            }
            info.setBrokerPath(value);
            return;
        }
        info.setBrokerPath(null);
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        DestinationInfo info = (DestinationInfo) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getConnectionId(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getDestination(), dataOut);
        dataOut.writeByte(info.getOperationType());
        looseMarshalLong(wireFormat, info.getTimeout(), dataOut);
        looseMarshalObjectArray(wireFormat, info.getBrokerPath(), dataOut);
    }
}
