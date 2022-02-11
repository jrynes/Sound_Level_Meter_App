package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.DiscoveryEvent;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class DiscoveryEventMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return DiscoveryEvent.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new DiscoveryEvent();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        DiscoveryEvent info = (DiscoveryEvent) o;
        info.setServiceName(tightUnmarshalString(dataIn, bs));
        info.setBrokerName(tightUnmarshalString(dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        DiscoveryEvent info = (DiscoveryEvent) o;
        return ((super.tightMarshal1(wireFormat, o, bs) + tightMarshalString1(info.getServiceName(), bs)) + tightMarshalString1(info.getBrokerName(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        DiscoveryEvent info = (DiscoveryEvent) o;
        tightMarshalString2(info.getServiceName(), dataOut, bs);
        tightMarshalString2(info.getBrokerName(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        DiscoveryEvent info = (DiscoveryEvent) o;
        info.setServiceName(looseUnmarshalString(dataIn));
        info.setBrokerName(looseUnmarshalString(dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        DiscoveryEvent info = (DiscoveryEvent) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalString(info.getServiceName(), dataOut);
        looseMarshalString(info.getBrokerName(), dataOut);
    }
}
