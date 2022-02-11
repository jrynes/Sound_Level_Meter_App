package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.BrokerId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.ProducerInfo;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ProducerInfoMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return (byte) 6;
    }

    public DataStructure createObject() {
        return new ProducerInfo();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ProducerInfo info = (ProducerInfo) o;
        info.setProducerId((ProducerId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setDestination((ActiveMQDestination) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
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
        info.setDispatchAsync(bs.readBoolean());
        info.setWindowSize(dataIn.readInt());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        ProducerInfo info = (ProducerInfo) o;
        int rc = ((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getProducerId(), bs)) + tightMarshalCachedObject1(wireFormat, info.getDestination(), bs)) + tightMarshalObjectArray1(wireFormat, info.getBrokerPath(), bs);
        bs.writeBoolean(info.isDispatchAsync());
        return rc + 4;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        ProducerInfo info = (ProducerInfo) o;
        tightMarshalCachedObject2(wireFormat, info.getProducerId(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getDestination(), dataOut, bs);
        tightMarshalObjectArray2(wireFormat, info.getBrokerPath(), dataOut, bs);
        bs.readBoolean();
        dataOut.writeInt(info.getWindowSize());
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ProducerInfo info = (ProducerInfo) o;
        info.setProducerId((ProducerId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setDestination((ActiveMQDestination) looseUnmarsalCachedObject(wireFormat, dataIn));
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
        info.setDispatchAsync(dataIn.readBoolean());
        info.setWindowSize(dataIn.readInt());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ProducerInfo info = (ProducerInfo) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getProducerId(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getDestination(), dataOut);
        looseMarshalObjectArray(wireFormat, info.getBrokerPath(), dataOut);
        dataOut.writeBoolean(info.isDispatchAsync());
        dataOut.writeInt(info.getWindowSize());
    }
}
