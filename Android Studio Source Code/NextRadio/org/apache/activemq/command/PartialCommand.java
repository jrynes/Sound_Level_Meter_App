package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class PartialCommand implements Command {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 60;
    private int commandId;
    private byte[] data;
    private transient Endpoint from;
    private transient Endpoint to;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public int getCommandId() {
        return this.commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Endpoint getFrom() {
        return this.from;
    }

    public void setFrom(Endpoint from) {
        this.from = from;
    }

    public Endpoint getTo() {
        return this.to;
    }

    public void setTo(Endpoint to) {
        this.to = to;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        throw new IllegalStateException("The transport layer should filter out PartialCommand instances but received: " + this);
    }

    public boolean isResponseRequired() {
        return false;
    }

    public boolean isResponse() {
        return false;
    }

    public boolean isBrokerInfo() {
        return false;
    }

    public boolean isMessageDispatch() {
        return false;
    }

    public boolean isMessage() {
        return false;
    }

    public boolean isMessageAck() {
        return false;
    }

    public boolean isMessageDispatchNotification() {
        return false;
    }

    public boolean isShutdownInfo() {
        return false;
    }

    public boolean isConnectionControl() {
        return false;
    }

    public void setResponseRequired(boolean responseRequired) {
    }

    public boolean isWireFormatInfo() {
        return false;
    }

    public boolean isMarshallAware() {
        return false;
    }

    public String toString() {
        int size = 0;
        if (this.data != null) {
            size = this.data.length;
        }
        return "PartialCommand[id: " + this.commandId + " data: " + size + " byte(s)]";
    }
}
