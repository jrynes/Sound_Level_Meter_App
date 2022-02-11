package org.apache.activemq.transport.udp;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.Endpoint;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.transport.reliable.ReplayBuffer;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandDatagramSocket extends CommandChannelSupport {
    private static final Logger LOG;
    private DatagramSocket channel;
    private Object readLock;
    private volatile int receiveCounter;
    private Object writeLock;

    static {
        LOG = LoggerFactory.getLogger(CommandDatagramSocket.class);
    }

    public CommandDatagramSocket(UdpTransport transport, OpenWireFormat wireFormat, int datagramSize, SocketAddress targetAddress, DatagramHeaderMarshaller headerMarshaller, DatagramSocket channel) {
        super(transport, wireFormat, datagramSize, targetAddress, headerMarshaller);
        this.readLock = new Object();
        this.writeLock = new Object();
        this.channel = channel;
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
    }

    public Command read() throws IOException {
        Command answer;
        synchronized (this.readLock) {
            DatagramPacket datagram = createDatagramPacket();
            this.channel.receive(datagram);
            this.receiveCounter++;
            DataInput dataIn = new DataInputStream(new ByteArrayInputStream(datagram.getData(), 0, datagram.getLength()));
            Endpoint from = this.headerMarshaller.createEndpoint(datagram, (DataInputStream) dataIn);
            answer = (Command) this.wireFormat.unmarshal(dataIn);
        }
        if (answer != null) {
            answer.setFrom(from);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Channel: " + this.name + " about to process: " + answer);
            }
        }
        return answer;
    }

    public void write(Command command, SocketAddress address) throws IOException {
        synchronized (this.writeLock) {
            ByteArrayOutputStream writeBuffer = createByteArrayOutputStream();
            DataOutput dataOut = new DataOutputStream(writeBuffer);
            this.headerMarshaller.writeHeader(command, (DataOutputStream) dataOut);
            int offset = writeBuffer.size();
            this.wireFormat.marshal(command, dataOut);
            if (remaining(writeBuffer) >= 0) {
                sendWriteBuffer(address, writeBuffer, command.getCommandId());
            } else {
                byte[] data = writeBuffer.toByteArray();
                boolean lastFragment = false;
                int length = data.length;
                int fragment = 0;
                while (!lastFragment) {
                    writeBuffer = createByteArrayOutputStream();
                    this.headerMarshaller.writeHeader(command, (DataOutputStream) dataOut);
                    int chunkSize = remaining(writeBuffer);
                    BooleanStream bs = null;
                    if (this.wireFormat.isTightEncodingEnabled()) {
                        bs = new BooleanStream();
                        bs.writeBoolean(true);
                    }
                    chunkSize -= 9;
                    if (bs != null) {
                        chunkSize -= bs.marshalledSize();
                    } else {
                        chunkSize--;
                    }
                    if (!this.wireFormat.isSizePrefixDisabled()) {
                        dataOut.writeInt(chunkSize);
                        chunkSize -= 4;
                    }
                    lastFragment = offset + chunkSize >= length;
                    if (chunkSize + offset > length) {
                        chunkSize = length - offset;
                    }
                    if (lastFragment) {
                        dataOut.write(61);
                    } else {
                        dataOut.write(60);
                    }
                    if (bs != null) {
                        bs.marshal(dataOut);
                    }
                    int commandId = command.getCommandId();
                    if (fragment > 0) {
                        commandId = this.sequenceGenerator.getNextSequenceId();
                    }
                    dataOut.writeInt(commandId);
                    if (bs == null) {
                        dataOut.write(1);
                    }
                    dataOut.writeInt(chunkSize);
                    dataOut.write(data, offset, chunkSize);
                    offset += chunkSize;
                    sendWriteBuffer(address, writeBuffer, commandId);
                    fragment++;
                }
            }
        }
    }

    public int getDatagramSize() {
        return this.datagramSize;
    }

    public void setDatagramSize(int datagramSize) {
        this.datagramSize = datagramSize;
    }

    protected void sendWriteBuffer(SocketAddress address, ByteArrayOutputStream writeBuffer, int commandId) throws IOException {
        sendWriteBuffer(commandId, address, writeBuffer.toByteArray(), false);
    }

    protected void sendWriteBuffer(int commandId, SocketAddress address, byte[] data, boolean redelivery) throws IOException {
        ReplayBuffer bufferCache = getReplayBuffer();
        if (!(bufferCache == null || redelivery)) {
            bufferCache.addBuffer(commandId, data);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Channel: " + this.name + " " + (redelivery ? "REDELIVERING" : "sending") + " datagram: " + commandId + " to: " + address);
        }
        this.channel.send(new DatagramPacket(data, 0, data.length, address));
    }

    public void sendBuffer(int commandId, Object buffer) throws IOException {
        if (buffer != null) {
            sendWriteBuffer(commandId, this.replayAddress, (byte[]) buffer, true);
        } else if (LOG.isWarnEnabled()) {
            LOG.warn("Request for buffer: " + commandId + " is no longer present");
        }
    }

    protected DatagramPacket createDatagramPacket() {
        return new DatagramPacket(new byte[this.datagramSize], this.datagramSize);
    }

    protected int remaining(ByteArrayOutputStream buffer) {
        return this.datagramSize - buffer.size();
    }

    protected ByteArrayOutputStream createByteArrayOutputStream() {
        return new ByteArrayOutputStream(this.datagramSize);
    }

    public int getReceiveCounter() {
        return this.receiveCounter;
    }
}
