package org.apache.activemq.transport.udp;

import java.io.IOException;
import java.net.SocketAddress;
import org.apache.activemq.command.Command;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.transport.reliable.ReplayBuffer;
import org.apache.activemq.util.IntSequenceGenerator;
import org.xbill.DNS.KEYRecord.Flags;

public abstract class CommandChannelSupport implements CommandChannel {
    protected int datagramSize;
    protected DatagramHeaderMarshaller headerMarshaller;
    protected final String name;
    protected SocketAddress replayAddress;
    private ReplayBuffer replayBuffer;
    protected final IntSequenceGenerator sequenceGenerator;
    protected SocketAddress targetAddress;
    protected OpenWireFormat wireFormat;

    public CommandChannelSupport(UdpTransport transport, OpenWireFormat wireFormat, int datagramSize, SocketAddress targetAddress, DatagramHeaderMarshaller headerMarshaller) {
        this.datagramSize = Flags.EXTEND;
        this.wireFormat = wireFormat;
        this.datagramSize = datagramSize;
        this.targetAddress = targetAddress;
        this.headerMarshaller = headerMarshaller;
        this.name = transport.toString();
        this.sequenceGenerator = transport.getSequenceGenerator();
        this.replayAddress = targetAddress;
        if (this.sequenceGenerator == null) {
            throw new IllegalArgumentException("No sequenceGenerator on the given transport: " + transport);
        }
    }

    public void write(Command command) throws IOException {
        write(command, this.targetAddress);
    }

    public int getDatagramSize() {
        return this.datagramSize;
    }

    public void setDatagramSize(int datagramSize) {
        this.datagramSize = datagramSize;
    }

    public SocketAddress getTargetAddress() {
        return this.targetAddress;
    }

    public void setTargetAddress(SocketAddress targetAddress) {
        this.targetAddress = targetAddress;
    }

    public SocketAddress getReplayAddress() {
        return this.replayAddress;
    }

    public void setReplayAddress(SocketAddress replayAddress) {
        this.replayAddress = replayAddress;
    }

    public String toString() {
        return "CommandChannel#" + this.name;
    }

    public DatagramHeaderMarshaller getHeaderMarshaller() {
        return this.headerMarshaller;
    }

    public void setHeaderMarshaller(DatagramHeaderMarshaller headerMarshaller) {
        this.headerMarshaller = headerMarshaller;
    }

    public ReplayBuffer getReplayBuffer() {
        return this.replayBuffer;
    }

    public void setReplayBuffer(ReplayBuffer replayBuffer) {
        this.replayBuffer = replayBuffer;
    }
}
