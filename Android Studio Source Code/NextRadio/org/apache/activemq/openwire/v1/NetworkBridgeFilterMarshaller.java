package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.BrokerId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.NetworkBridgeFilter;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class NetworkBridgeFilterMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return NetworkBridgeFilter.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new NetworkBridgeFilter();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        NetworkBridgeFilter info = (NetworkBridgeFilter) o;
        info.setNetworkTTL(dataIn.readInt());
        info.setNetworkBrokerId((BrokerId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        return (super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, ((NetworkBridgeFilter) o).getNetworkBrokerId(), bs)) + 4;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        NetworkBridgeFilter info = (NetworkBridgeFilter) o;
        dataOut.writeInt(info.getNetworkTTL());
        tightMarshalCachedObject2(wireFormat, info.getNetworkBrokerId(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        NetworkBridgeFilter info = (NetworkBridgeFilter) o;
        info.setNetworkTTL(dataIn.readInt());
        info.setNetworkBrokerId((BrokerId) looseUnmarsalCachedObject(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        NetworkBridgeFilter info = (NetworkBridgeFilter) o;
        super.looseMarshal(wireFormat, o, dataOut);
        dataOut.writeInt(info.getNetworkTTL());
        looseMarshalCachedObject(wireFormat, info.getNetworkBrokerId(), dataOut);
    }
}
