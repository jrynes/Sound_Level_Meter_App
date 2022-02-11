package org.apache.activemq.command;

import java.io.IOException;
import org.apache.activemq.wireformat.WireFormat;

public interface MarshallAware {
    void afterMarshall(WireFormat wireFormat) throws IOException;

    void afterUnmarshall(WireFormat wireFormat) throws IOException;

    void beforeMarshall(WireFormat wireFormat) throws IOException;

    void beforeUnmarshall(WireFormat wireFormat) throws IOException;
}
