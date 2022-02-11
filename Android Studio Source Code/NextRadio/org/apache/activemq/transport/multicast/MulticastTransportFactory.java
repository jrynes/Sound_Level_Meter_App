package org.apache.activemq.transport.multicast;

import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.udp.UdpTransportFactory;
import org.apache.activemq.wireformat.WireFormat;

public class MulticastTransportFactory extends UdpTransportFactory {
    protected Transport createTransport(URI location, WireFormat wf) throws UnknownHostException, IOException {
        return new MulticastTransport(asOpenWireFormat(wf), location);
    }
}
