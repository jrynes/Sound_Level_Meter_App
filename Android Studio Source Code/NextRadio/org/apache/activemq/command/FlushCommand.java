package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class FlushCommand extends BaseCommand {
    public static final Command COMMAND;
    public static final byte DATA_STRUCTURE_TYPE = (byte) 15;

    static {
        COMMAND = new FlushCommand();
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processFlush(this);
    }
}
