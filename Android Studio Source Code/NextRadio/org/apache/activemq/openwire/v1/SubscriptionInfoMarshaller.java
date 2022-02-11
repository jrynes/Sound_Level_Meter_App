package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.SubscriptionInfo;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class SubscriptionInfoMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return SubscriptionInfo.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new SubscriptionInfo();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        SubscriptionInfo info = (SubscriptionInfo) o;
        info.setClientId(tightUnmarshalString(dataIn, bs));
        info.setDestination((ActiveMQDestination) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setSelector(tightUnmarshalString(dataIn, bs));
        info.setSubscriptionName(tightUnmarshalString(dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        SubscriptionInfo info = (SubscriptionInfo) o;
        return ((((super.tightMarshal1(wireFormat, o, bs) + tightMarshalString1(info.getClientId(), bs)) + tightMarshalCachedObject1(wireFormat, info.getDestination(), bs)) + tightMarshalString1(info.getSelector(), bs)) + tightMarshalString1(info.getSubscriptionName(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        SubscriptionInfo info = (SubscriptionInfo) o;
        tightMarshalString2(info.getClientId(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getDestination(), dataOut, bs);
        tightMarshalString2(info.getSelector(), dataOut, bs);
        tightMarshalString2(info.getSubscriptionName(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        SubscriptionInfo info = (SubscriptionInfo) o;
        info.setClientId(looseUnmarshalString(dataIn));
        info.setDestination((ActiveMQDestination) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setSelector(looseUnmarshalString(dataIn));
        info.setSubscriptionName(looseUnmarshalString(dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        SubscriptionInfo info = (SubscriptionInfo) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalString(info.getClientId(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getDestination(), dataOut);
        looseMarshalString(info.getSelector(), dataOut);
        looseMarshalString(info.getSubscriptionName(), dataOut);
    }
}
