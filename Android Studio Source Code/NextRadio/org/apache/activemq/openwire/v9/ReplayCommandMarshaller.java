package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.ReplayCommand;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ReplayCommandMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return ReplayCommand.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new ReplayCommand();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ReplayCommand info = (ReplayCommand) o;
        info.setFirstNakNumber(dataIn.readInt());
        info.setLastNakNumber(dataIn.readInt());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        ReplayCommand info = (ReplayCommand) o;
        return super.tightMarshal1(wireFormat, o, bs) + 8;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        ReplayCommand info = (ReplayCommand) o;
        dataOut.writeInt(info.getFirstNakNumber());
        dataOut.writeInt(info.getLastNakNumber());
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ReplayCommand info = (ReplayCommand) o;
        info.setFirstNakNumber(dataIn.readInt());
        info.setLastNakNumber(dataIn.readInt());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ReplayCommand info = (ReplayCommand) o;
        super.looseMarshal(wireFormat, o, dataOut);
        dataOut.writeInt(info.getFirstNakNumber());
        dataOut.writeInt(info.getLastNakNumber());
    }
}
