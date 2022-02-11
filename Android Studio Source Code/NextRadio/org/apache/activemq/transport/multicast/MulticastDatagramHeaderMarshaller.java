package org.apache.activemq.transport.multicast;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.Endpoint;
import org.apache.activemq.transport.udp.DatagramEndpoint;
import org.apache.activemq.transport.udp.DatagramHeaderMarshaller;

public class MulticastDatagramHeaderMarshaller extends DatagramHeaderMarshaller {
    private final byte[] localUriAsBytes;

    public MulticastDatagramHeaderMarshaller(String localUri) {
        this.localUriAsBytes = localUri.getBytes();
    }

    public Endpoint createEndpoint(ByteBuffer readBuffer, SocketAddress address) {
        byte[] data = new byte[readBuffer.getInt()];
        readBuffer.get(data);
        return new DatagramEndpoint(new String(data), address);
    }

    public void writeHeader(Command command, ByteBuffer writeBuffer) {
        writeBuffer.putInt(this.localUriAsBytes.length);
        writeBuffer.put(this.localUriAsBytes);
        super.writeHeader(command, writeBuffer);
    }
}
