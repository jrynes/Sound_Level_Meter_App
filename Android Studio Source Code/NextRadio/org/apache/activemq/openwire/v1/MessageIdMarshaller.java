package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class MessageIdMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return MessageId.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new MessageId();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        MessageId info = (MessageId) o;
        info.setProducerId((ProducerId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setProducerSequenceId(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.setBrokerSequenceId(tightUnmarshalLong(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        MessageId info = (MessageId) o;
        return (((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getProducerId(), bs)) + tightMarshalLong1(wireFormat, info.getProducerSequenceId(), bs)) + tightMarshalLong1(wireFormat, info.getBrokerSequenceId(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        MessageId info = (MessageId) o;
        tightMarshalCachedObject2(wireFormat, info.getProducerId(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getProducerSequenceId(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getBrokerSequenceId(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        MessageId info = (MessageId) o;
        info.setProducerId((ProducerId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setProducerSequenceId(looseUnmarshalLong(wireFormat, dataIn));
        info.setBrokerSequenceId(looseUnmarshalLong(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        MessageId info = (MessageId) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getProducerId(), dataOut);
        looseMarshalLong(wireFormat, info.getProducerSequenceId(), dataOut);
        looseMarshalLong(wireFormat, info.getBrokerSequenceId(), dataOut);
    }
}
