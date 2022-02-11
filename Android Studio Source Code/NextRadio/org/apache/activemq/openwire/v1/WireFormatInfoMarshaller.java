package org.apache.activemq.openwire.v1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.WireFormatInfo;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class WireFormatInfoMarshaller extends BaseDataStreamMarshaller {
    public byte getDataStructureType() {
        return (byte) 1;
    }

    public DataStructure createObject() {
        return new WireFormatInfo();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        WireFormatInfo info = (WireFormatInfo) o;
        info.beforeUnmarshall(wireFormat);
        info.setMagic(tightUnmarshalConstByteArray(dataIn, bs, 8));
        info.setVersion(dataIn.readInt());
        info.setMarshalledProperties(tightUnmarshalByteSequence(dataIn, bs));
        info.afterUnmarshall(wireFormat);
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        WireFormatInfo info = (WireFormatInfo) o;
        info.beforeMarshall(wireFormat);
        return ((super.tightMarshal1(wireFormat, o, bs) + tightMarshalConstByteArray1(info.getMagic(), bs, 8)) + tightMarshalByteSequence1(info.getMarshalledProperties(), bs)) + 4;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        WireFormatInfo info = (WireFormatInfo) o;
        tightMarshalConstByteArray2(info.getMagic(), dataOut, bs, 8);
        dataOut.writeInt(info.getVersion());
        tightMarshalByteSequence2(info.getMarshalledProperties(), dataOut, bs);
        info.afterMarshall(wireFormat);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        WireFormatInfo info = (WireFormatInfo) o;
        info.beforeUnmarshall(wireFormat);
        info.setMagic(looseUnmarshalConstByteArray(dataIn, 8));
        info.setVersion(dataIn.readInt());
        info.setMarshalledProperties(looseUnmarshalByteSequence(dataIn));
        info.afterUnmarshall(wireFormat);
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        WireFormatInfo info = (WireFormatInfo) o;
        info.beforeMarshall(wireFormat);
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalConstByteArray(wireFormat, info.getMagic(), dataOut, 8);
        dataOut.writeInt(info.getVersion());
        looseMarshalByteSequence(wireFormat, info.getMarshalledProperties(), dataOut);
    }
}
