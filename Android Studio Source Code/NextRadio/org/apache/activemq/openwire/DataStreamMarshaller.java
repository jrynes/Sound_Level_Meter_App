package org.apache.activemq.openwire;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;

public interface DataStreamMarshaller {
    DataStructure createObject();

    byte getDataStructureType();

    void looseMarshal(OpenWireFormat openWireFormat, Object obj, DataOutput dataOutput) throws IOException;

    void looseUnmarshal(OpenWireFormat openWireFormat, Object obj, DataInput dataInput) throws IOException;

    int tightMarshal1(OpenWireFormat openWireFormat, Object obj, BooleanStream booleanStream) throws IOException;

    void tightMarshal2(OpenWireFormat openWireFormat, Object obj, DataOutput dataOutput, BooleanStream booleanStream) throws IOException;

    void tightUnmarshal(OpenWireFormat openWireFormat, Object obj, DataInput dataInput, BooleanStream booleanStream) throws IOException;
}
