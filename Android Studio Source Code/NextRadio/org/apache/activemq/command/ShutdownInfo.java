package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class ShutdownInfo extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 11;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processShutdown(this);
    }

    public boolean isShutdownInfo() {
        return true;
    }
}
