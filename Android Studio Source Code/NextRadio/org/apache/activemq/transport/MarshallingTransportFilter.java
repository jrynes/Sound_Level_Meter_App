package org.apache.activemq.transport;

import java.io.IOException;
import org.apache.activemq.command.Command;
import org.apache.activemq.wireformat.WireFormat;

public class MarshallingTransportFilter extends TransportFilter {
    private final WireFormat localWireFormat;
    private final WireFormat remoteWireFormat;

    public MarshallingTransportFilter(Transport next, WireFormat localWireFormat, WireFormat remoteWireFormat) {
        super(next);
        this.localWireFormat = localWireFormat;
        this.remoteWireFormat = remoteWireFormat;
    }

    public void oneway(Object command) throws IOException {
        this.next.oneway((Command) this.remoteWireFormat.unmarshal(this.localWireFormat.marshal(command)));
    }

    public void onCommand(Object command) {
        try {
            getTransportListener().onCommand((Command) this.localWireFormat.unmarshal(this.remoteWireFormat.marshal(command)));
        } catch (IOException e) {
            getTransportListener().onException(e);
        }
    }
}
