package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.ExceptionResponse;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ExceptionResponseMarshaller extends ResponseMarshaller {
    public byte getDataStructureType() {
        return ExceptionResponse.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new ExceptionResponse();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ((ExceptionResponse) o).setException(tightUnmarsalThrowable(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        return (super.tightMarshal1(wireFormat, o, bs) + tightMarshalThrowable1(wireFormat, ((ExceptionResponse) o).getException(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        tightMarshalThrowable2(wireFormat, ((ExceptionResponse) o).getException(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ((ExceptionResponse) o).setException(looseUnmarsalThrowable(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ExceptionResponse info = (ExceptionResponse) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalThrowable(wireFormat, info.getException(), dataOut);
    }
}
