package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.MessagePull;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class MessagePullMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return MessagePull.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new MessagePull();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        MessagePull info = (MessagePull) o;
        info.setConsumerId((ConsumerId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setDestination((ActiveMQDestination) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setTimeout(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.setCorrelationId(tightUnmarshalString(dataIn, bs));
        info.setMessageId((MessageId) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        MessagePull info = (MessagePull) o;
        return (((((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getConsumerId(), bs)) + tightMarshalCachedObject1(wireFormat, info.getDestination(), bs)) + tightMarshalLong1(wireFormat, info.getTimeout(), bs)) + tightMarshalString1(info.getCorrelationId(), bs)) + tightMarshalNestedObject1(wireFormat, info.getMessageId(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        MessagePull info = (MessagePull) o;
        tightMarshalCachedObject2(wireFormat, info.getConsumerId(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getDestination(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getTimeout(), dataOut, bs);
        tightMarshalString2(info.getCorrelationId(), dataOut, bs);
        tightMarshalNestedObject2(wireFormat, info.getMessageId(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        MessagePull info = (MessagePull) o;
        info.setConsumerId((ConsumerId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setDestination((ActiveMQDestination) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setTimeout(looseUnmarshalLong(wireFormat, dataIn));
        info.setCorrelationId(looseUnmarshalString(dataIn));
        info.setMessageId((MessageId) looseUnmarsalNestedObject(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        MessagePull info = (MessagePull) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getConsumerId(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getDestination(), dataOut);
        looseMarshalLong(wireFormat, info.getTimeout(), dataOut);
        looseMarshalString(info.getCorrelationId(), dataOut);
        looseMarshalNestedObject(wireFormat, info.getMessageId(), dataOut);
    }
}
