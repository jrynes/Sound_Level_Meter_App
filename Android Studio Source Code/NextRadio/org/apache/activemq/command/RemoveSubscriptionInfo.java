package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class RemoveSubscriptionInfo extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 9;
    protected String clientId;
    protected ConnectionId connectionId;
    protected String subscriptionName;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId(ConnectionId connectionId) {
        this.connectionId = connectionId;
    }

    public String getSubcriptionName() {
        return this.subscriptionName;
    }

    public void setSubcriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public String getSubscriptionName() {
        return this.subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processRemoveSubscription(this);
    }
}
