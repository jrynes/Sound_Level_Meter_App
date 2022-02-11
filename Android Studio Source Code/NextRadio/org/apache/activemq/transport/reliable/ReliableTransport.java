package org.apache.activemq.transport.reliable;

import com.facebook.ads.AdError;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.ReplayCommand;
import org.apache.activemq.command.Response;
import org.apache.activemq.openwire.CommandIdComparator;
import org.apache.activemq.transport.FutureResponse;
import org.apache.activemq.transport.ResponseCorrelator;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.udp.UdpTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class ReliableTransport extends ResponseCorrelator {
    private static final Logger LOG;
    private final SortedSet<Command> commands;
    private int expectedCounter;
    private ReplayBuffer replayBuffer;
    private int replayBufferCommandCount;
    private ReplayStrategy replayStrategy;
    private Replayer replayer;
    private int requestTimeout;
    private UdpTransport udpTransport;

    static {
        LOG = LoggerFactory.getLogger(ReliableTransport.class);
    }

    public ReliableTransport(Transport next, ReplayStrategy replayStrategy) {
        super(next);
        this.commands = new TreeSet(new CommandIdComparator());
        this.expectedCounter = 1;
        this.replayBufferCommandCount = 50;
        this.requestTimeout = AdError.SERVER_ERROR_CODE;
        this.replayStrategy = replayStrategy;
    }

    public ReliableTransport(Transport next, UdpTransport udpTransport) throws IOException {
        super(next, udpTransport.getSequenceGenerator());
        this.commands = new TreeSet(new CommandIdComparator());
        this.expectedCounter = 1;
        this.replayBufferCommandCount = 50;
        this.requestTimeout = AdError.SERVER_ERROR_CODE;
        this.udpTransport = udpTransport;
        this.replayer = udpTransport.createReplayer();
    }

    public void requestReplay(int fromCommandId, int toCommandId) {
        ReplayCommand replay = new ReplayCommand();
        replay.setFirstNakNumber(fromCommandId);
        replay.setLastNakNumber(toCommandId);
        try {
            oneway(replay);
        } catch (IOException e) {
            getTransportListener().onException(e);
        }
    }

    public Object request(Object o) throws IOException {
        Command command = (Command) o;
        FutureResponse response = asyncRequest(command, null);
        while (true) {
            Response result = response.getResult(this.requestTimeout);
            if (result != null) {
                return result;
            }
            onMissingResponse(command, response);
        }
    }

    public Object request(Object o, int timeout) throws IOException {
        Command command = (Command) o;
        FutureResponse response = asyncRequest(command, null);
        while (timeout > 0) {
            int time = timeout;
            if (timeout > this.requestTimeout) {
                time = this.requestTimeout;
            }
            Object result = response.getResult(time);
            if (result != null) {
                return result;
            }
            onMissingResponse(command, response);
            timeout -= time;
        }
        return response.getResult(0);
    }

    public void onCommand(Object o) {
        Command command = (Command) o;
        if (command.isWireFormatInfo()) {
            super.onCommand(command);
        } else if (command.getDataStructureType() == 65) {
            replayCommands((ReplayCommand) command);
        } else {
            boolean valid;
            int actualCounter = command.getCommandId();
            if (this.expectedCounter == actualCounter) {
                valid = true;
            } else {
                valid = false;
            }
            if (!valid) {
                synchronized (this.commands) {
                    int nextCounter = actualCounter;
                    boolean empty = this.commands.isEmpty();
                    if (!empty) {
                        nextCounter = ((Command) this.commands.first()).getCommandId();
                    }
                    try {
                        if (this.replayStrategy.onDroppedPackets(this, this.expectedCounter, actualCounter, nextCounter)) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Received out of order command which is being buffered for later: " + command);
                            }
                            this.commands.add(command);
                        }
                    } catch (IOException e) {
                        onException(e);
                    }
                    if (!empty) {
                        command = (Command) this.commands.first();
                        if (this.expectedCounter == command.getCommandId()) {
                            valid = true;
                        } else {
                            valid = false;
                        }
                        if (valid) {
                            this.commands.remove(command);
                        }
                    }
                }
            }
            while (valid) {
                this.replayStrategy.onReceivedPacket(this, (long) this.expectedCounter);
                this.expectedCounter++;
                super.onCommand(command);
                synchronized (this.commands) {
                    if (this.commands.isEmpty()) {
                        valid = false;
                    } else {
                        valid = true;
                    }
                    if (valid) {
                        command = (Command) this.commands.first();
                        if (this.expectedCounter == command.getCommandId()) {
                            valid = true;
                        } else {
                            valid = false;
                        }
                        if (valid) {
                            this.commands.remove(command);
                        }
                    }
                }
            }
        }
    }

    public int getBufferedCommandCount() {
        int size;
        synchronized (this.commands) {
            size = this.commands.size();
        }
        return size;
    }

    public int getExpectedCounter() {
        return this.expectedCounter;
    }

    public void setExpectedCounter(int expectedCounter) {
        this.expectedCounter = expectedCounter;
    }

    public int getRequestTimeout() {
        return this.requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public ReplayStrategy getReplayStrategy() {
        return this.replayStrategy;
    }

    public ReplayBuffer getReplayBuffer() {
        if (this.replayBuffer == null) {
            this.replayBuffer = createReplayBuffer();
        }
        return this.replayBuffer;
    }

    public void setReplayBuffer(ReplayBuffer replayBuffer) {
        this.replayBuffer = replayBuffer;
    }

    public int getReplayBufferCommandCount() {
        return this.replayBufferCommandCount;
    }

    public void setReplayBufferCommandCount(int replayBufferSize) {
        this.replayBufferCommandCount = replayBufferSize;
    }

    public void setReplayStrategy(ReplayStrategy replayStrategy) {
        this.replayStrategy = replayStrategy;
    }

    public Replayer getReplayer() {
        return this.replayer;
    }

    public void setReplayer(Replayer replayer) {
        this.replayer = replayer;
    }

    public String toString() {
        return this.next.toString();
    }

    public void start() throws Exception {
        if (this.udpTransport != null) {
            this.udpTransport.setReplayBuffer(getReplayBuffer());
        }
        if (this.replayStrategy == null) {
            throw new IllegalArgumentException("Property replayStrategy not specified");
        }
        super.start();
    }

    protected void onMissingResponse(Command command, FutureResponse response) {
        LOG.debug("Still waiting for response on: " + this + " to command: " + command + " sending replay message");
        int commandId = command.getCommandId();
        requestReplay(commandId, commandId);
    }

    protected ReplayBuffer createReplayBuffer() {
        return new DefaultReplayBuffer(getReplayBufferCommandCount());
    }

    protected void replayCommands(ReplayCommand command) {
        try {
            if (this.replayer == null) {
                onException(new IOException("Cannot replay commands. No replayer property configured"));
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Processing replay command: " + command);
            }
            getReplayBuffer().replayMessages(command.getFirstNakNumber(), command.getLastNakNumber(), this.replayer);
        } catch (IOException e) {
            onException(e);
        }
    }
}
