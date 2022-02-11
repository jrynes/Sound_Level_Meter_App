package org.apache.activemq.command;

import java.util.Map;
import org.apache.activemq.util.IntrospectionSupport;

public abstract class BaseCommand implements Command {
    protected int commandId;
    private transient Endpoint from;
    protected boolean responseRequired;
    private transient Endpoint to;

    public void copy(BaseCommand copy) {
        copy.commandId = this.commandId;
        copy.responseRequired = this.responseRequired;
    }

    public int getCommandId() {
        return this.commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public boolean isResponseRequired() {
        return this.responseRequired;
    }

    public void setResponseRequired(boolean responseRequired) {
        this.responseRequired = responseRequired;
    }

    public String toString() {
        return toString(null);
    }

    public String toString(Map<String, Object> overrideFields) {
        return IntrospectionSupport.toString(this, BaseCommand.class, overrideFields);
    }

    public boolean isWireFormatInfo() {
        return false;
    }

    public boolean isBrokerInfo() {
        return false;
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

    public boolean isMarshallAware() {
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
}
