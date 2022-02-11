package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ControlCommand;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ControlCommandMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return ControlCommand.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new ControlCommand();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ((ControlCommand) o).setCommand(tightUnmarshalString(dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        return (super.tightMarshal1(wireFormat, o, bs) + tightMarshalString1(((ControlCommand) o).getCommand(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        tightMarshalString2(((ControlCommand) o).getCommand(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ((ControlCommand) o).setCommand(looseUnmarshalString(dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ControlCommand info = (ControlCommand) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalString(info.getCommand(), dataOut);
    }
}
