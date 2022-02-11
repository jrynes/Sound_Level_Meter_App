package org.apache.activemq.transport.udp;

import java.io.IOException;
import java.net.SocketAddress;
import org.apache.activemq.Service;
import org.apache.activemq.command.Command;
import org.apache.activemq.transport.reliable.ReplayBuffer;
import org.apache.activemq.transport.reliable.Replayer;

public interface CommandChannel extends Replayer, Service {
    int getDatagramSize();

    DatagramHeaderMarshaller getHeaderMarshaller();

    int getReceiveCounter();

    Command read() throws IOException;

    void setDatagramSize(int i);

    void setHeaderMarshaller(DatagramHeaderMarshaller datagramHeaderMarshaller);

    void setReplayAddress(SocketAddress socketAddress);

    void setReplayBuffer(ReplayBuffer replayBuffer);

    void setTargetAddress(SocketAddress socketAddress);

    void write(Command command, SocketAddress socketAddress) throws IOException;
}
