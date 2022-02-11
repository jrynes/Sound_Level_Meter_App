package org.apache.activemq.openwire.v8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.BrokerId;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public abstract class MessageMarshaller extends BaseCommandMarshaller {
    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        BrokerId[] value;
        short i;
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        Message info = (Message) o;
        info.beforeUnmarshall(wireFormat);
        info.setProducerId((ProducerId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setDestination((ActiveMQDestination) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setTransactionId((TransactionId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setOriginalDestination((ActiveMQDestination) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setMessageId((MessageId) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setOriginalTransactionId((TransactionId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setGroupID(tightUnmarshalString(dataIn, bs));
        info.setGroupSequence(dataIn.readInt());
        info.setCorrelationId(tightUnmarshalString(dataIn, bs));
        info.setPersistent(bs.readBoolean());
        info.setExpiration(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.setPriority(dataIn.readByte());
        info.setReplyTo((ActiveMQDestination) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setTimestamp(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.setType(tightUnmarshalString(dataIn, bs));
        info.setContent(tightUnmarshalByteSequence(dataIn, bs));
        info.setMarshalledProperties(tightUnmarshalByteSequence(dataIn, bs));
        info.setDataStructure(tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setTargetConsumerId((ConsumerId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setCompressed(bs.readBoolean());
        info.setRedeliveryCounter(dataIn.readInt());
        if (bs.readBoolean()) {
            short size;
            size = dataIn.readShort();
            value = new BrokerId[size];
            for (i = (short) 0; i < size; i++) {
                value[i] = (BrokerId) tightUnmarsalNestedObject(wireFormat, dataIn, bs);
            }
            info.setBrokerPath(value);
        } else {
            info.setBrokerPath(null);
        }
        info.setArrival(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.setUserID(tightUnmarshalString(dataIn, bs));
        info.setRecievedByDFBridge(bs.readBoolean());
        info.setDroppable(bs.readBoolean());
        if (bs.readBoolean()) {
            size = dataIn.readShort();
            value = new BrokerId[size];
            for (i = (short) 0; i < size; i++) {
                value[i] = (BrokerId) tightUnmarsalNestedObject(wireFormat, dataIn, bs);
            }
            info.setCluster(value);
        } else {
            info.setCluster(null);
        }
        info.setBrokerInTime(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.setBrokerOutTime(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.afterUnmarshall(wireFormat);
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        Message info = (Message) o;
        info.beforeMarshall(wireFormat);
        int rc = (((((((super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getProducerId(), bs)) + tightMarshalCachedObject1(wireFormat, info.getDestination(), bs)) + tightMarshalCachedObject1(wireFormat, info.getTransactionId(), bs)) + tightMarshalCachedObject1(wireFormat, info.getOriginalDestination(), bs)) + tightMarshalNestedObject1(wireFormat, info.getMessageId(), bs)) + tightMarshalCachedObject1(wireFormat, info.getOriginalTransactionId(), bs)) + tightMarshalString1(info.getGroupID(), bs)) + tightMarshalString1(info.getCorrelationId(), bs);
        bs.writeBoolean(info.isPersistent());
        rc = (((((((rc + tightMarshalLong1(wireFormat, info.getExpiration(), bs)) + tightMarshalNestedObject1(wireFormat, info.getReplyTo(), bs)) + tightMarshalLong1(wireFormat, info.getTimestamp(), bs)) + tightMarshalString1(info.getType(), bs)) + tightMarshalByteSequence1(info.getContent(), bs)) + tightMarshalByteSequence1(info.getMarshalledProperties(), bs)) + tightMarshalNestedObject1(wireFormat, info.getDataStructure(), bs)) + tightMarshalCachedObject1(wireFormat, info.getTargetConsumerId(), bs);
        bs.writeBoolean(info.isCompressed());
        rc = ((rc + tightMarshalObjectArray1(wireFormat, info.getBrokerPath(), bs)) + tightMarshalLong1(wireFormat, info.getArrival(), bs)) + tightMarshalString1(info.getUserID(), bs);
        bs.writeBoolean(info.isRecievedByDFBridge());
        bs.writeBoolean(info.isDroppable());
        return (((rc + tightMarshalObjectArray1(wireFormat, info.getCluster(), bs)) + tightMarshalLong1(wireFormat, info.getBrokerInTime(), bs)) + tightMarshalLong1(wireFormat, info.getBrokerOutTime(), bs)) + 9;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        Message info = (Message) o;
        tightMarshalCachedObject2(wireFormat, info.getProducerId(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getDestination(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getTransactionId(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getOriginalDestination(), dataOut, bs);
        tightMarshalNestedObject2(wireFormat, info.getMessageId(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getOriginalTransactionId(), dataOut, bs);
        tightMarshalString2(info.getGroupID(), dataOut, bs);
        dataOut.writeInt(info.getGroupSequence());
        tightMarshalString2(info.getCorrelationId(), dataOut, bs);
        bs.readBoolean();
        tightMarshalLong2(wireFormat, info.getExpiration(), dataOut, bs);
        dataOut.writeByte(info.getPriority());
        tightMarshalNestedObject2(wireFormat, info.getReplyTo(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getTimestamp(), dataOut, bs);
        tightMarshalString2(info.getType(), dataOut, bs);
        tightMarshalByteSequence2(info.getContent(), dataOut, bs);
        tightMarshalByteSequence2(info.getMarshalledProperties(), dataOut, bs);
        tightMarshalNestedObject2(wireFormat, info.getDataStructure(), dataOut, bs);
        tightMarshalCachedObject2(wireFormat, info.getTargetConsumerId(), dataOut, bs);
        bs.readBoolean();
        dataOut.writeInt(info.getRedeliveryCounter());
        tightMarshalObjectArray2(wireFormat, info.getBrokerPath(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getArrival(), dataOut, bs);
        tightMarshalString2(info.getUserID(), dataOut, bs);
        bs.readBoolean();
        bs.readBoolean();
        tightMarshalObjectArray2(wireFormat, info.getCluster(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getBrokerInTime(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getBrokerOutTime(), dataOut, bs);
        info.afterMarshall(wireFormat);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        short size;
        BrokerId[] value;
        short i;
        super.looseUnmarshal(wireFormat, o, dataIn);
        Message info = (Message) o;
        info.beforeUnmarshall(wireFormat);
        info.setProducerId((ProducerId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setDestination((ActiveMQDestination) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setTransactionId((TransactionId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setOriginalDestination((ActiveMQDestination) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setMessageId((MessageId) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setOriginalTransactionId((TransactionId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setGroupID(looseUnmarshalString(dataIn));
        info.setGroupSequence(dataIn.readInt());
        info.setCorrelationId(looseUnmarshalString(dataIn));
        info.setPersistent(dataIn.readBoolean());
        info.setExpiration(looseUnmarshalLong(wireFormat, dataIn));
        info.setPriority(dataIn.readByte());
        info.setReplyTo((ActiveMQDestination) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setTimestamp(looseUnmarshalLong(wireFormat, dataIn));
        info.setType(looseUnmarshalString(dataIn));
        info.setContent(looseUnmarshalByteSequence(dataIn));
        info.setMarshalledProperties(looseUnmarshalByteSequence(dataIn));
        info.setDataStructure(looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setTargetConsumerId((ConsumerId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setCompressed(dataIn.readBoolean());
        info.setRedeliveryCounter(dataIn.readInt());
        if (dataIn.readBoolean()) {
            size = dataIn.readShort();
            value = new BrokerId[size];
            for (i = (short) 0; i < size; i++) {
                value[i] = (BrokerId) looseUnmarsalNestedObject(wireFormat, dataIn);
            }
            info.setBrokerPath(value);
        } else {
            info.setBrokerPath(null);
        }
        info.setArrival(looseUnmarshalLong(wireFormat, dataIn));
        info.setUserID(looseUnmarshalString(dataIn));
        info.setRecievedByDFBridge(dataIn.readBoolean());
        info.setDroppable(dataIn.readBoolean());
        if (dataIn.readBoolean()) {
            size = dataIn.readShort();
            value = new BrokerId[size];
            for (i = (short) 0; i < size; i++) {
                value[i] = (BrokerId) looseUnmarsalNestedObject(wireFormat, dataIn);
            }
            info.setCluster(value);
        } else {
            info.setCluster(null);
        }
        info.setBrokerInTime(looseUnmarshalLong(wireFormat, dataIn));
        info.setBrokerOutTime(looseUnmarshalLong(wireFormat, dataIn));
        info.afterUnmarshall(wireFormat);
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        Message info = (Message) o;
        info.beforeMarshall(wireFormat);
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getProducerId(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getDestination(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getTransactionId(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getOriginalDestination(), dataOut);
        looseMarshalNestedObject(wireFormat, info.getMessageId(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getOriginalTransactionId(), dataOut);
        looseMarshalString(info.getGroupID(), dataOut);
        dataOut.writeInt(info.getGroupSequence());
        looseMarshalString(info.getCorrelationId(), dataOut);
        dataOut.writeBoolean(info.isPersistent());
        looseMarshalLong(wireFormat, info.getExpiration(), dataOut);
        dataOut.writeByte(info.getPriority());
        looseMarshalNestedObject(wireFormat, info.getReplyTo(), dataOut);
        looseMarshalLong(wireFormat, info.getTimestamp(), dataOut);
        looseMarshalString(info.getType(), dataOut);
        looseMarshalByteSequence(wireFormat, info.getContent(), dataOut);
        looseMarshalByteSequence(wireFormat, info.getMarshalledProperties(), dataOut);
        looseMarshalNestedObject(wireFormat, info.getDataStructure(), dataOut);
        looseMarshalCachedObject(wireFormat, info.getTargetConsumerId(), dataOut);
        dataOut.writeBoolean(info.isCompressed());
        dataOut.writeInt(info.getRedeliveryCounter());
        looseMarshalObjectArray(wireFormat, info.getBrokerPath(), dataOut);
        looseMarshalLong(wireFormat, info.getArrival(), dataOut);
        looseMarshalString(info.getUserID(), dataOut);
        dataOut.writeBoolean(info.isRecievedByDFBridge());
        dataOut.writeBoolean(info.isDroppable());
        looseMarshalObjectArray(wireFormat, info.getCluster(), dataOut);
        looseMarshalLong(wireFormat, info.getBrokerInTime(), dataOut);
        looseMarshalLong(wireFormat, info.getBrokerOutTime(), dataOut);
    }
}
