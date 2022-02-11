package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class ControlCommand extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 14;
    private String command;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processControlCommand(this);
    }
}
