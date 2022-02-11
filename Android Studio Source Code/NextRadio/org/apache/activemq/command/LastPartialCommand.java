package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class LastPartialCommand extends PartialCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 61;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        throw new IllegalStateException("The transport layer should filter out LastPartialCommand instances but received: " + this);
    }

    public void configure(Command completeCommand) {
        completeCommand.setFrom(getFrom());
    }
}
