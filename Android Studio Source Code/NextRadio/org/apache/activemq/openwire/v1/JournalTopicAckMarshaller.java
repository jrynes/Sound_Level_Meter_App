package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.JournalTopicAck;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class JournalTopicAckMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return JournalTopicAck.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new JournalTopicAck();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        JournalTopicAck info = (JournalTopicAck) o;
        info.setDestination((ActiveMQDestination) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setMessageId((MessageId) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setMessageSequenceId(tightUnmarshalLong(wireFormat, dataIn, bs));
        info.setSubscritionName(tightUnmarshalString(dataIn, bs));
        info.setClientId(tightUnmarshalString(dataIn, bs));
        info.setTransactionId((TransactionId) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        JournalTopicAck info = (JournalTopicAck) o;
        return ((((((super.tightMarshal1(wireFormat, o, bs) + tightMarshalNestedObject1(wireFormat, info.getDestination(), bs)) + tightMarshalNestedObject1(wireFormat, info.getMessageId(), bs)) + tightMarshalLong1(wireFormat, info.getMessageSequenceId(), bs)) + tightMarshalString1(info.getSubscritionName(), bs)) + tightMarshalString1(info.getClientId(), bs)) + tightMarshalNestedObject1(wireFormat, info.getTransactionId(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        JournalTopicAck info = (JournalTopicAck) o;
        tightMarshalNestedObject2(wireFormat, info.getDestination(), dataOut, bs);
        tightMarshalNestedObject2(wireFormat, info.getMessageId(), dataOut, bs);
        tightMarshalLong2(wireFormat, info.getMessageSequenceId(), dataOut, bs);
        tightMarshalString2(info.getSubscritionName(), dataOut, bs);
        tightMarshalString2(info.getClientId(), dataOut, bs);
        tightMarshalNestedObject2(wireFormat, info.getTransactionId(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        JournalTopicAck info = (JournalTopicAck) o;
        info.setDestination((ActiveMQDestination) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setMessageId((MessageId) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setMessageSequenceId(looseUnmarshalLong(wireFormat, dataIn));
        info.setSubscritionName(looseUnmarshalString(dataIn));
        info.setClientId(looseUnmarshalString(dataIn));
        info.setTransactionId((TransactionId) looseUnmarsalNestedObject(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        JournalTopicAck info = (JournalTopicAck) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalNestedObject(wireFormat, info.getDestination(), dataOut);
        looseMarshalNestedObject(wireFormat, info.getMessageId(), dataOut);
        looseMarshalLong(wireFormat, info.getMessageSequenceId(), dataOut);
        looseMarshalString(info.getSubscritionName(), dataOut);
        looseMarshalString(info.getClientId(), dataOut);
        looseMarshalNestedObject(wireFormat, info.getTransactionId(), dataOut);
    }
}
