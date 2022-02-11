package org.apache.activemq.openwire.v8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataArrayResponse;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class DataArrayResponseMarshaller extends ResponseMarshaller {
    public byte getDataStructureType() {
        return DataArrayResponse.DATA_STRUCTURE_TYPE;
    }

    public DataStructure createObject() {
        return new DataArrayResponse();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        DataArrayResponse info = (DataArrayResponse) o;
        if (bs.readBoolean()) {
            short size = dataIn.readShort();
            DataStructure[] value = new DataStructure[size];
            for (short i = (short) 0; i < size; i++) {
                value[i] = tightUnmarsalNestedObject(wireFormat, dataIn, bs);
            }
            info.setData(value);
            return;
        }
        info.setData(null);
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        return (super.tightMarshal1(wireFormat, o, bs) + tightMarshalObjectArray1(wireFormat, ((DataArrayResponse) o).getData(), bs)) + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        tightMarshalObjectArray2(wireFormat, ((DataArrayResponse) o).getData(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        DataArrayResponse info = (DataArrayResponse) o;
        if (dataIn.readBoolean()) {
            short size = dataIn.readShort();
            DataStructure[] value = new DataStructure[size];
            for (short i = (short) 0; i < size; i++) {
                value[i] = looseUnmarsalNestedObject(wireFormat, dataIn);
            }
            info.setData(value);
            return;
        }
        info.setData(null);
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        DataArrayResponse info = (DataArrayResponse) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalObjectArray(wireFormat, info.getData(), dataOut);
    }
}
