package org.apache.activemq.transport.udp;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.Endpoint;
import org.apache.activemq.command.LastPartialCommand;
import org.apache.activemq.command.PartialCommand;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.transport.reliable.ReplayBuffer;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandDatagramChannel extends CommandChannelSupport {
    private static final Logger LOG;
    private ByteBufferPool bufferPool;
    private DatagramChannel channel;
    private int defaultMarshalBufferSize;
    private ByteBuffer readBuffer;
    private Object readLock;
    private volatile int receiveCounter;
    private Object writeLock;

    static {
        LOG = LoggerFactory.getLogger(CommandDatagramChannel.class);
    }

    public CommandDatagramChannel(UdpTransport transport, OpenWireFormat wireFormat, int datagramSize, SocketAddress targetAddress, DatagramHeaderMarshaller headerMarshaller, DatagramChannel channel, ByteBufferPool bufferPool) {
        super(transport, wireFormat, datagramSize, targetAddress, headerMarshaller);
        this.readLock = new Object();
        this.writeLock = new Object();
        this.defaultMarshalBufferSize = AccessibilityNodeInfoCompat.ACTION_CUT;
        this.channel = channel;
        this.bufferPool = bufferPool;
    }

    public void start() throws Exception {
        this.bufferPool.setDefaultSize(this.datagramSize);
        this.bufferPool.start();
        this.readBuffer = this.bufferPool.borrowBuffer();
    }

    public void stop() throws Exception {
        this.bufferPool.stop();
    }

    public Command read() throws IOException {
        Command answer;
        synchronized (this.readLock) {
            SocketAddress address;
            do {
                this.readBuffer.clear();
                address = this.channel.receive(this.readBuffer);
                this.readBuffer.flip();
            } while (this.readBuffer.limit() == 0);
            this.receiveCounter++;
            Endpoint from = this.headerMarshaller.createEndpoint(this.readBuffer, address);
            byte[] data = new byte[this.readBuffer.remaining()];
            this.readBuffer.get(data);
            answer = (Command) this.wireFormat.unmarshal(new DataInputStream(new ByteArrayInputStream(data)));
        }
        if (answer != null) {
            answer.setFrom(from);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Channel: " + this.name + " received from: " + from + " about to process: " + answer);
            }
        }
        return answer;
    }

    public void write(Command command, SocketAddress address) throws IOException {
        synchronized (this.writeLock) {
            ByteArrayOutputStream largeBuffer = new ByteArrayOutputStream(this.defaultMarshalBufferSize);
            this.wireFormat.marshal(command, new DataOutputStream(largeBuffer));
            byte[] data = largeBuffer.toByteArray();
            int size = data.length;
            ByteBuffer writeBuffer = this.bufferPool.borrowBuffer();
            writeBuffer.clear();
            this.headerMarshaller.writeHeader(command, writeBuffer);
            if (size > writeBuffer.remaining()) {
                int offset = 0;
                boolean lastFragment = false;
                int length = data.length;
                int fragment = 0;
                while (!lastFragment) {
                    if (fragment > 0) {
                        writeBuffer = this.bufferPool.borrowBuffer();
                        writeBuffer.clear();
                        this.headerMarshaller.writeHeader(command, writeBuffer);
                    }
                    int chunkSize = writeBuffer.remaining();
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
                        writeBuffer.putInt(chunkSize);
                        chunkSize -= 4;
                    }
                    lastFragment = offset + chunkSize >= length;
                    if (chunkSize + offset > length) {
                        chunkSize = length - offset;
                    }
                    if (lastFragment) {
                        writeBuffer.put(LastPartialCommand.DATA_STRUCTURE_TYPE);
                    } else {
                        writeBuffer.put(PartialCommand.DATA_STRUCTURE_TYPE);
                    }
                    if (bs != null) {
                        bs.marshal(writeBuffer);
                    }
                    int commandId = command.getCommandId();
                    if (fragment > 0) {
                        commandId = this.sequenceGenerator.getNextSequenceId();
                    }
                    writeBuffer.putInt(commandId);
                    if (bs == null) {
                        writeBuffer.put((byte) 1);
                    }
                    writeBuffer.putInt(chunkSize);
                    writeBuffer.put(data, offset, chunkSize);
                    offset += chunkSize;
                    sendWriteBuffer(commandId, address, writeBuffer, false);
                    fragment++;
                }
            } else {
                writeBuffer.put(data);
                sendWriteBuffer(command.getCommandId(), address, writeBuffer, false);
            }
        }
    }

    public ByteBufferPool getBufferPool() {
        return this.bufferPool;
    }

    public void setBufferPool(ByteBufferPool bufferPool) {
        this.bufferPool = bufferPool;
    }

    protected void sendWriteBuffer(int commandId, SocketAddress address, ByteBuffer writeBuffer, boolean redelivery) throws IOException {
        ReplayBuffer bufferCache = getReplayBuffer();
        if (!(bufferCache == null || redelivery)) {
            bufferCache.addBuffer(commandId, writeBuffer);
        }
        writeBuffer.flip();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Channel: " + this.name + " " + (redelivery ? "REDELIVERING" : "sending") + " datagram: " + commandId + " to: " + address);
        }
        this.channel.send(writeBuffer, address);
    }

    public void sendBuffer(int commandId, Object buffer) throws IOException {
        if (buffer != null) {
            sendWriteBuffer(commandId, getReplayAddress(), (ByteBuffer) buffer, true);
        } else if (LOG.isWarnEnabled()) {
            LOG.warn("Request for buffer: " + commandId + " is no longer present");
        }
    }

    public int getReceiveCounter() {
        return this.receiveCounter;
    }
}
