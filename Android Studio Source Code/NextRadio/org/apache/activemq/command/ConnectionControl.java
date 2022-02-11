package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;
import org.apache.activemq.transport.stomp.Stomp;

public class ConnectionControl extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 18;
    protected boolean close;
    protected String connectedBrokers;
    protected boolean exit;
    protected boolean faultTolerant;
    protected boolean rebalanceConnection;
    protected String reconnectTo;
    protected boolean resume;
    protected boolean suspend;
    protected byte[] token;

    public ConnectionControl() {
        this.connectedBrokers = Stomp.EMPTY;
        this.reconnectTo = Stomp.EMPTY;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processConnectionControl(this);
    }

    public boolean isConnectionControl() {
        return true;
    }

    public boolean isClose() {
        return this.close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public boolean isExit() {
        return this.exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean isFaultTolerant() {
        return this.faultTolerant;
    }

    public void setFaultTolerant(boolean faultTolerant) {
        this.faultTolerant = faultTolerant;
    }

    public boolean isResume() {
        return this.resume;
    }

    public void setResume(boolean resume) {
        this.resume = resume;
    }

    public boolean isSuspend() {
        return this.suspend;
    }

    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }

    public String getConnectedBrokers() {
        return this.connectedBrokers;
    }

    public void setConnectedBrokers(String connectedBrokers) {
        this.connectedBrokers = connectedBrokers;
    }

    public String getReconnectTo() {
        return this.reconnectTo;
    }

    public void setReconnectTo(String reconnectTo) {
        this.reconnectTo = reconnectTo;
    }

    public boolean isRebalanceConnection() {
        return this.rebalanceConnection;
    }

    public void setRebalanceConnection(boolean rebalanceConnection) {
        this.rebalanceConnection = rebalanceConnection;
    }

    public byte[] getToken() {
        return this.token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }
}
