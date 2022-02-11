package org.apache.activemq.openwire.v8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerControl;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ConsumerControlMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return ConsumerControl.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new ConsumerControl();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ConsumerControl info = (ConsumerControl) o;
        info.setDestination((ActiveMQDestination) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setClose(bs.readBoolean());
        info.setConsumerId((ConsumerId) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setPrefetch(dataIn.readInt());
        info.setFlush(bs.readBoolean());
        info.setStart(bs.readBoolean());
        info.setStop(bs.readBoolean());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        ConsumerControl info = (ConsumerControl) o;
        int rc = super.tightMarshal1(wireFormat, o, bs) + tightMarshalNestedObject1(wireFormat, info.getDestination(), bs);
        bs.writeBoolean(info.isClose());
        rc += tightMarshalNestedObject1(wireFormat, info.getConsumerId(), bs);
        bs.writeBoolean(info.isFlush());
        bs.writeBoolean(info.isStart());
        bs.writeBoolean(info.isStop());
        return rc + 4;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        ConsumerControl info = (ConsumerControl) o;
        tightMarshalNestedObject2(wireFormat, info.getDestination(), dataOut, bs);
        bs.readBoolean();
        tightMarshalNestedObject2(wireFormat, info.getConsumerId(), dataOut, bs);
        dataOut.writeInt(info.getPrefetch());
        bs.readBoolean();
        bs.readBoolean();
        bs.readBoolean();
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ConsumerControl info = (ConsumerControl) o;
        info.setDestination((ActiveMQDestination) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setClose(dataIn.readBoolean());
        info.setConsumerId((ConsumerId) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setPrefetch(dataIn.readInt());
        info.setFlush(dataIn.readBoolean());
        info.setStart(dataIn.readBoolean());
        info.setStop(dataIn.readBoolean());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ConsumerControl info = (ConsumerControl) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalNestedObject(wireFormat, info.getDestination(), dataOut);
        dataOut.writeBoolean(info.isClose());
        looseMarshalNestedObject(wireFormat, info.getConsumerId(), dataOut);
        dataOut.writeInt(info.getPrefetch());
        dataOut.writeBoolean(info.isFlush());
        dataOut.writeBoolean(info.isStart());
        dataOut.writeBoolean(info.isStop());
    }
}
