package org.apache.activemq.command;

import java.io.IOException;
import org.apache.activemq.state.CommandVisitor;

public class DestinationInfo extends BaseCommand {
    public static final byte ADD_OPERATION_TYPE = (byte) 0;
    public static final byte DATA_STRUCTURE_TYPE = (byte) 8;
    public static final byte REMOVE_OPERATION_TYPE = (byte) 1;
    protected BrokerId[] brokerPath;
    protected ConnectionId connectionId;
    protected ActiveMQDestination destination;
    protected byte operationType;
    protected long timeout;

    public DestinationInfo(ConnectionId connectionId, byte operationType, ActiveMQDestination destination) {
        this.connectionId = connectionId;
        this.operationType = operationType;
        this.destination = destination;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isAddOperation() {
        return this.operationType == null;
    }

    public boolean isRemoveOperation() {
        return this.operationType == REMOVE_OPERATION_TYPE;
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId(ConnectionId connectionId) {
        this.connectionId = connectionId;
    }

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public byte getOperationType() {
        return this.operationType;
    }

    public void setOperationType(byte operationType) {
        this.operationType = operationType;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public BrokerId[] getBrokerPath() {
        return this.brokerPath;
    }

    public void setBrokerPath(BrokerId[] brokerPath) {
        this.brokerPath = brokerPath;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        if (isAddOperation()) {
            return visitor.processAddDestination(this);
        }
        if (isRemoveOperation()) {
            return visitor.processRemoveDestination(this);
        }
        throw new IOException("Unknown operation type: " + getOperationType());
    }

    public DestinationInfo copy() {
        DestinationInfo result = new DestinationInfo();
        super.copy(result);
        result.connectionId = this.connectionId;
        result.destination = this.destination;
        result.operationType = this.operationType;
        result.brokerPath = this.brokerPath;
        return result;
    }
}
