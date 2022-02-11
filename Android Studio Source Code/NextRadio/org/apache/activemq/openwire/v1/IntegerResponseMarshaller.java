package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.IntegerResponse;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class IntegerResponseMarshaller extends ResponseMarshaller {
    public byte getDataStructureType() {
        return IntegerResponse.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new IntegerResponse();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ((IntegerResponse) o).setResult(dataIn.readInt());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        IntegerResponse info = (IntegerResponse) o;
        return super.tightMarshal1(wireFormat, o, bs) + 4;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        dataOut.writeInt(((IntegerResponse) o).getResult());
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ((IntegerResponse) o).setResult(dataIn.readInt());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        IntegerResponse info = (IntegerResponse) o;
        super.looseMarshal(wireFormat, o, dataOut);
        dataOut.writeInt(info.getResult());
    }
}
