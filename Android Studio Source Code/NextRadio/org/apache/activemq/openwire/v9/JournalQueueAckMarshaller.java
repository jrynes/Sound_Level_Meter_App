package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.JournalQueueAck;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class JournalQueueAckMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return JournalQueueAck.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new JournalQueueAck();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        JournalQueueAck info = (JournalQueueAck) o;
        info.setDestination((ActiveMQDestination) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setMessageAck((MessageAck) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        JournalQueueAck info = (JournalQueueAck) o;
        return ((super.tightMarshal1(wireFormat, o, bs) + tightMarshalNestedObject1(wireFormat, info.getDestination(), bs)) + tightMarshalNestedObject1(wireFormat, info.getMessageAck(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        JournalQueueAck info = (JournalQueueAck) o;
        tightMarshalNestedObject2(wireFormat, info.getDestination(), dataOut, bs);
        tightMarshalNestedObject2(wireFormat, info.getMessageAck(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        JournalQueueAck info = (JournalQueueAck) o;
        info.setDestination((ActiveMQDestination) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setMessageAck((MessageAck) looseUnmarsalNestedObject(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        JournalQueueAck info = (JournalQueueAck) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalNestedObject(wireFormat, info.getDestination(), dataOut);
        looseMarshalNestedObject(wireFormat, info.getMessageAck(), dataOut);
    }
}
