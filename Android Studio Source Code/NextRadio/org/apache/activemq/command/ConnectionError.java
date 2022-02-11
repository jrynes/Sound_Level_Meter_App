package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class ConnectionError extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 16;
    private ConnectionId connectionId;
    private Throwable exception;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processConnectionError(this);
    }

    public Throwable getException() {
        return this.exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId(ConnectionId connectionId) {
        this.connectionId = connectionId;
    }
}
