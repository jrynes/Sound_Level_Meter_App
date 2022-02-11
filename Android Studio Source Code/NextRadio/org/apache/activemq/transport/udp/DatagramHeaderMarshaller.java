package org.apache.activemq.transport.udp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.Endpoint;

public class DatagramHeaderMarshaller {
    private Map<SocketAddress, Endpoint> endpoints;

    public DatagramHeaderMarshaller() {
        this.endpoints = new HashMap();
    }

    public Endpoint createEndpoint(ByteBuffer readBuffer, SocketAddress address) {
        return getEndpoint(address);
    }

    public Endpoint createEndpoint(DatagramPacket datagram, DataInputStream dataIn) {
        return getEndpoint(datagram.getSocketAddress());
    }

    public void writeHeader(Command command, ByteBuffer writeBuffer) {
    }

    public void writeHeader(Command command, DataOutputStream dataOut) {
    }

    protected Endpoint getEndpoint(SocketAddress address) {
        Endpoint endpoint = (Endpoint) this.endpoints.get(address);
        if (endpoint != null) {
            return endpoint;
        }
        endpoint = createEndpoint(address);
        this.endpoints.put(address, endpoint);
        return endpoint;
    }

    protected Endpoint createEndpoint(SocketAddress address) {
        return new DatagramEndpoint(address.toString(), address);
    }
}
