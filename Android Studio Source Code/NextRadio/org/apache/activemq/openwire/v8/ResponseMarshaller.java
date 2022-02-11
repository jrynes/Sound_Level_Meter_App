package org.apache.activemq.openwire.v8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.Response;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ResponseMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return Response.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new Response();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ((Response) o).setCorrelationId(dataIn.readInt());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        Response info = (Response) o;
        return super.tightMarshal1(wireFormat, o, bs) + 4;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        dataOut.writeInt(((Response) o).getCorrelationId());
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ((Response) o).setCorrelationId(dataIn.readInt());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        Response info = (Response) o;
        super.looseMarshal(wireFormat, o, dataOut);
        dataOut.writeInt(info.getCorrelationId());
    }
}
