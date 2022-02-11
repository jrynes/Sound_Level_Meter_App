package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class MessageAckMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return MessageAck.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new MessageAck();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        MessageAck info = (MessageAck) o;
        info.setDestination((ActiveMQDestination) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setTransactionId((TransactionId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setConsumerId((ConsumerId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setAckType(dataIn.readByte());
        info.setFirstMessageId((MessageId) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setLastMessageId((MessageId) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setMessageCount(dataIn.readInt());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        MessageAck info = (MessageAck) o;
        return (((((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getDestination(), bs)) + tightMarshalCachedObject1(wireFormat, info.getTransactionId(), bs)) + tightMarshalCachedObject1(wireFormat, info.getConsumerId(), bs)) + tightMarshalNestedObject1(wireFormat, info.getFirstMessageId(), bs)) + tightMarshalNestedObject1(wireFormat, info.getLastMessageId(), bs)) + 5;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        MessageAck info = (MessageAck) o;
        tightMarshalCachedObject2(wireFormat, info.getDestination(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getTransactionId(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getConsumerId(), dataOut, bs);
        dataOut.writeByte(info.getAckType());
        tightMarshalNestedObject2(wireFormat, info.getFirstMessageId(), dataOut, bs);
        tightMarshalNestedObject2(wireFormat, info.getLastMessageId(), dataOut, bs);
        dataOut.writeInt(info.getMessageCount());
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        MessageAck info = (MessageAck) o;
        info.setDestination((ActiveMQDestination) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setTransactionId((TransactionId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setConsumerId((ConsumerId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setAckType(dataIn.readByte());
        info.setFirstMessageId((MessageId) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setLastMessageId((MessageId) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setMessageCount(dataIn.readInt());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        MessageAck info = (MessageAck) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getDestination(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getTransactionId(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getConsumerId(), dataOut);
        dataOut.writeByte(info.getAckType());
        looseMarshalNestedObject(wireFormat, info.getFirstMessageId(), dataOut);
        looseMarshalNestedObject(wireFormat, info.getLastMessageId(), dataOut);
        dataOut.writeInt(info.getMessageCount());
    }
}
