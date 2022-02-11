package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class Response extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 30;
    int correlationId;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public int getCorrelationId() {
        return this.correlationId;
    }

    public void setCorrelationId(int responseId) {
        this.correlationId = responseId;
    }

    public boolean isResponse() {
        return true;
    }

    public boolean isException() {
        return false;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return null;
    }
}
