package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class SessionInfo extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 4;
    protected SessionId sessionId;

    public SessionInfo() {
        this.sessionId = new SessionId();
    }

    public SessionInfo(ConnectionInfo connectionInfo, long sessionId) {
        this.sessionId = new SessionId(connectionInfo.getConnectionId(), sessionId);
    }

    public SessionInfo(SessionId sessionId) {
        this.sessionId = sessionId;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public SessionId getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(SessionId sessionId) {
        this.sessionId = sessionId;
    }

    public RemoveInfo createRemoveCommand() {
        RemoveInfo command = new RemoveInfo(getSessionId());
        command.setResponseRequired(isResponseRequired());
        return command;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processAddSession(this);
    }
}
