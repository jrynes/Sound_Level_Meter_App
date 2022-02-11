package org.apache.activemq.transport;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.LastPartialCommand;
import org.apache.activemq.command.PartialCommand;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.util.ByteArrayInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandJoiner extends TransportFilter {
    private static final Logger LOG;
    private ByteArrayOutputStream out;
    private OpenWireFormat wireFormat;

    static {
        LOG = LoggerFactory.getLogger(CommandJoiner.class);
    }

    public CommandJoiner(Transport next, OpenWireFormat wireFormat) {
        super(next);
        this.out = new ByteArrayOutputStream();
        this.wireFormat = wireFormat;
    }

    public void onCommand(Object o) {
        Command command = (Command) o;
        byte type = command.getDataStructureType();
        if (type == 60 || type == LastPartialCommand.DATA_STRUCTURE_TYPE) {
            try {
                this.out.write(((PartialCommand) command).getData());
            } catch (IOException e) {
                getTransportListener().onException(e);
            }
            if (type == LastPartialCommand.DATA_STRUCTURE_TYPE) {
                try {
                    byte[] fullData = this.out.toByteArray();
                    this.out.reset();
                    Command completeCommand = (Command) this.wireFormat.unmarshal(new DataInputStream(new ByteArrayInputStream(fullData)));
                    ((LastPartialCommand) command).configure(completeCommand);
                    getTransportListener().onCommand(completeCommand);
                    return;
                } catch (IOException e2) {
                    LOG.warn("Failed to unmarshal partial command: " + command);
                    getTransportListener().onException(e2);
                    return;
                }
            }
            return;
        }
        getTransportListener().onCommand(command);
    }

    public void stop() throws Exception {
        super.stop();
        this.out = null;
    }

    public String toString() {
        return this.next.toString();
    }
}
