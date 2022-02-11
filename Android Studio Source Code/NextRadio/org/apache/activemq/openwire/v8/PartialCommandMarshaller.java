package org.apache.activemq.openwire.v8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.PartialCommand;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class PartialCommandMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return PartialCommand.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new PartialCommand();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        PartialCommand info = (PartialCommand) o;
        info.setCommandId(dataIn.readInt());
        info.setData(tightUnmarshalByteArray(dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        return (super.tightMarshal1(wireFormat, o, bs) + tightMarshalByteArray1(((PartialCommand) o).getData(), bs)) + 4;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        PartialCommand info = (PartialCommand) o;
        dataOut.writeInt(info.getCommandId());
        tightMarshalByteArray2(info.getData(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        PartialCommand info = (PartialCommand) o;
        info.setCommandId(dataIn.readInt());
        info.setData(looseUnmarshalByteArray(dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        PartialCommand info = (PartialCommand) o;
        super.looseMarshal(wireFormat, o, dataOut);
        dataOut.writeInt(info.getCommandId());
        looseMarshalByteArray(wireFormat, info.getData(), dataOut);
    }
}
