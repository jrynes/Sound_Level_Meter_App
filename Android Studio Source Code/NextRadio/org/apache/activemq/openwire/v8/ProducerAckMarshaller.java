package org.apache.activemq.openwire.v8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.ProducerAck;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ProducerAckMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return ProducerAck.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new ProducerAck();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ProducerAck info = (ProducerAck) o;
        info.setProducerId((ProducerId) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setSize(dataIn.readInt());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        return (super.tightMarshal1(wireFormat, o, bs) + tightMarshalNestedObject1(wireFormat, ((ProducerAck) o).getProducerId(), bs)) + 4;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        ProducerAck info = (ProducerAck) o;
        tightMarshalNestedObject2(wireFormat, info.getProducerId(), dataOut, bs);
        dataOut.writeInt(info.getSize());
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ProducerAck info = (ProducerAck) o;
        info.setProducerId((ProducerId) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setSize(dataIn.readInt());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ProducerAck info = (ProducerAck) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalNestedObject(wireFormat, info.getProducerId(), dataOut);
        dataOut.writeInt(info.getSize());
    }
}
