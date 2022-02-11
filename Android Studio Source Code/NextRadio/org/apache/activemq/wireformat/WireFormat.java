package org.apache.activemq.wireformat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.util.ByteSequence;

public interface WireFormat {
    int getVersion();

    ByteSequence marshal(Object obj) throws IOException;

    void marshal(Object obj, DataOutput dataOutput) throws IOException;

    void setVersion(int i);

    Object unmarshal(DataInput dataInput) throws IOException;

    Object unmarshal(ByteSequence byteSequence) throws IOException;
}
