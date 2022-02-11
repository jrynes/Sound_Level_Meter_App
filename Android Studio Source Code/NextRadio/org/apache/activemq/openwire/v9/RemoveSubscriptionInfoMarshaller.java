package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.RemoveSubscriptionInfo;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class RemoveSubscriptionInfoMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return (byte) 9;
    }

    public DataStructure createObject() {
        return new RemoveSubscriptionInfo();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        RemoveSubscriptionInfo info = (RemoveSubscriptionInfo) o;
        info.setConnectionId((ConnectionId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setSubcriptionName(tightUnmarshalString(dataIn, bs));
        info.setClientId(tightUnmarshalString(dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        RemoveSubscriptionInfo info = (RemoveSubscriptionInfo) o;
        return (((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getConnectionId(), bs)) + tightMarshalString1(info.getSubcriptionName(), bs)) + tightMarshalString1(info.getClientId(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        RemoveSubscriptionInfo info = (RemoveSubscriptionInfo) o;
        tightMarshalCachedObject2(wireFormat, info.getConnectionId(), dataOut, bs);
        tightMarshalString2(info.getSubcriptionName(), dataOut, bs);
        tightMarshalString2(info.getClientId(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        RemoveSubscriptionInfo info = (RemoveSubscriptionInfo) o;
        info.setConnectionId((ConnectionId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setSubcriptionName(looseUnmarshalString(dataIn));
        info.setClientId(looseUnmarshalString(dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        RemoveSubscriptionInfo info = (RemoveSubscriptionInfo) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getConnectionId(), dataOut);
        looseMarshalString(info.getSubcriptionName(), dataOut);
        looseMarshalString(info.getClientId(), dataOut);
    }
}
