package org.apache.activemq.transport.stomp;

import org.apache.activemq.wireformat.WireFormat;
import org.apache.activemq.wireformat.WireFormatFactory;

public class StompWireFormatFactory implements WireFormatFactory {
    public WireFormat createWireFormat() {
        return new StompWireFormat();
    }
}
