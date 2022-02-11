package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.BrokerId;
import org.apache.activemq.command.BrokerInfo;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class BrokerInfoMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return (byte) 2;
    }

    public DataStructure createObject() {
        return new BrokerInfo();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        BrokerInfo info = (BrokerInfo) o;
        info.setBrokerId((BrokerId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setBrokerURL(tightUnmarshalString(dataIn, bs));
        if (bs.readBoolean()) {
            short size = dataIn.readShort();
            BrokerInfo[] value = new BrokerInfo[size];
            for (short i = (short) 0; i < size; i++) {
                value[i] = (BrokerInfo) tightUnmarsalNestedObject(wireFormat, dataIn, bs);
            }
            info.setPeerBrokerInfos(value);
        } else {
            info.setPeerBrokerInfos(null);
        }
        info.setBrokerName(tightUnmarshalString(dataIn, bs));
        info.setSlaveBroker(bs.readBoolean());
        info.setMasterBroker(bs.readBoolean());
        info.setFaultTolerantConfiguration(bs.readBoolean());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        BrokerInfo info = (BrokerInfo) o;
        int rc = (((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getBrokerId(), bs)) + tightMarshalString1(info.getBrokerURL(), bs)) + tightMarshalObjectArray1(wireFormat, info.getPeerBrokerInfos(), bs)) + tightMarshalString1(info.getBrokerName(), bs);
        bs.writeBoolean(info.isSlaveBroker());
        bs.writeBoolean(info.isMasterBroker());
        bs.writeBoolean(info.isFaultTolerantConfiguration());
        return rc + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        BrokerInfo info = (BrokerInfo) o;
        tightMarshalCachedObject2(wireFormat, info.getBrokerId(), dataOut, bs);
        tightMarshalString2(info.getBrokerURL(), dataOut, bs);
        tightMarshalObjectArray2(wireFormat, info.getPeerBrokerInfos(), dataOut, bs);
        tightMarshalString2(info.getBrokerName(), dataOut, bs);
        bs.readBoolean();
        bs.readBoolean();
        bs.readBoolean();
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        BrokerInfo info = (BrokerInfo) o;
        info.setBrokerId((BrokerId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setBrokerURL(looseUnmarshalString(dataIn));
        if (dataIn.readBoolean()) {
            short size = dataIn.readShort();
            BrokerInfo[] value = new BrokerInfo[size];
            for (short i = (short) 0; i < size; i++) {
                value[i] = (BrokerInfo) looseUnmarsalNestedObject(wireFormat, dataIn);
            }
            info.setPeerBrokerInfos(value);
        } else {
            info.setPeerBrokerInfos(null);
        }
        info.setBrokerName(looseUnmarshalString(dataIn));
        info.setSlaveBroker(dataIn.readBoolean());
        info.setMasterBroker(dataIn.readBoolean());
        info.setFaultTolerantConfiguration(dataIn.readBoolean());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        BrokerInfo info = (BrokerInfo) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getBrokerId(), dataOut);
        looseMarshalString(info.getBrokerURL(), dataOut);
        looseMarshalObjectArray(wireFormat, info.getPeerBrokerInfos(), dataOut);
        looseMarshalString(info.getBrokerName(), dataOut);
        dataOut.writeBoolean(info.isSlaveBroker());
        dataOut.writeBoolean(info.isMasterBroker());
        dataOut.writeBoolean(info.isFaultTolerantConfiguration());
    }
}
