package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class MessageDispatchMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return MessageDispatch.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new MessageDispatch();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        MessageDispatch info = (MessageDispatch) o;
        info.setConsumerId((ConsumerId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setDestination((ActiveMQDestination) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setMessage((Message) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setRedeliveryCounter(dataIn.readInt());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        MessageDispatch info = (MessageDispatch) o;
        return (((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getConsumerId(), bs)) + tightMarshalCachedObject1(wireFormat, info.getDestination(), bs)) + tightMarshalNestedObject1(wireFormat, info.getMessage(), bs)) + 4;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        MessageDispatch info = (MessageDispatch) o;
        tightMarshalCachedObject2(wireFormat, info.getConsumerId(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getDestination(), dataOut, bs);
        tightMarshalNestedObject2(wireFormat, info.getMessage(), dataOut, bs);
        dataOut.writeInt(info.getRedeliveryCounter());
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        MessageDispatch info = (MessageDispatch) o;
        info.setConsumerId((ConsumerId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setDestination((ActiveMQDestination) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setMessage((Message) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setRedeliveryCounter(dataIn.readInt());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        MessageDispatch info = (MessageDispatch) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getConsumerId(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getDestination(), dataOut);
        looseMarshalNestedObject(wireFormat, info.getMessage(), dataOut);
        dataOut.writeInt(info.getRedeliveryCounter());
    }
}
