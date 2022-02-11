package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;
import org.apache.activemq.util.IntrospectionSupport;

public class KeepAliveInfo extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 10;
    private transient Endpoint from;
    private transient Endpoint to;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isResponse() {
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

    public boolean isBrokerInfo() {
        return false;
    }

    public boolean isWireFormatInfo() {
        return false;
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
        return visitor.processKeepAlive(this);
    }

    public boolean isMarshallAware() {
        return false;
    }

    public boolean isMessageDispatchNotification() {
        return false;
    }

    public boolean isShutdownInfo() {
        return false;
    }

    public String toString() {
        return IntrospectionSupport.toString(this, KeepAliveInfo.class);
    }
}
