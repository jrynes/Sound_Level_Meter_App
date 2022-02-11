package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.BrokerId;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ConnectionInfoMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return (byte) 3;
    }

    public DataStructure createObject() {
        return new ConnectionInfo();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ConnectionInfo info = (ConnectionInfo) o;
        info.setConnectionId((ConnectionId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setClientId(tightUnmarshalString(dataIn, bs));
        info.setPassword(tightUnmarshalString(dataIn, bs));
        info.setUserName(tightUnmarshalString(dataIn, bs));
        if (bs.readBoolean()) {
            short size = dataIn.readShort();
            BrokerId[] value = new BrokerId[size];
            for (short i = (short) 0; i < size; i++) {
                value[i] = (BrokerId) tightUnmarsalNestedObject(wireFormat, dataIn, bs);
            }
            info.setBrokerPath(value);
        } else {
            info.setBrokerPath(null);
        }
        info.setBrokerMasterConnector(bs.readBoolean());
        info.setManageable(bs.readBoolean());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        ConnectionInfo info = (ConnectionInfo) o;
        int rc = ((((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getConnectionId(), bs)) + tightMarshalString1(info.getClientId(), bs)) + tightMarshalString1(info.getPassword(), bs)) + tightMarshalString1(info.getUserName(), bs)) + tightMarshalObjectArray1(wireFormat, info.getBrokerPath(), bs);
        bs.writeBoolean(info.isBrokerMasterConnector());
        bs.writeBoolean(info.isManageable());
        return rc + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        ConnectionInfo info = (ConnectionInfo) o;
        tightMarshalCachedObject2(wireFormat, info.getConnectionId(), dataOut, bs);
        tightMarshalString2(info.getClientId(), dataOut, bs);
        tightMarshalString2(info.getPassword(), dataOut, bs);
        tightMarshalString2(info.getUserName(), dataOut, bs);
        tightMarshalObjectArray2(wireFormat, info.getBrokerPath(), dataOut, bs);
        bs.readBoolean();
        bs.readBoolean();
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ConnectionInfo info = (ConnectionInfo) o;
        info.setConnectionId((ConnectionId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setClientId(looseUnmarshalString(dataIn));
        info.setPassword(looseUnmarshalString(dataIn));
        info.setUserName(looseUnmarshalString(dataIn));
        if (dataIn.readBoolean()) {
            short size = dataIn.readShort();
            BrokerId[] value = new BrokerId[size];
            for (short i = (short) 0; i < size; i++) {
                value[i] = (BrokerId) looseUnmarsalNestedObject(wireFormat, dataIn);
            }
            info.setBrokerPath(value);
        } else {
            info.setBrokerPath(null);
        }
        info.setBrokerMasterConnector(dataIn.readBoolean());
        info.setManageable(dataIn.readBoolean());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ConnectionInfo info = (ConnectionInfo) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getConnectionId(), dataOut);
        looseMarshalString(info.getClientId(), dataOut);
        looseMarshalString(info.getPassword(), dataOut);
        looseMarshalString(info.getUserName(), dataOut);
        looseMarshalObjectArray(wireFormat, info.getBrokerPath(), dataOut);
        dataOut.writeBoolean(info.isBrokerMasterConnector());
        dataOut.writeBoolean(info.isManageable());
    }
}
