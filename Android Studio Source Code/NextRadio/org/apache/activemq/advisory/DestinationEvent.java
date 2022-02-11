package org.apache.activemq.advisory;

import java.util.EventObject;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.DestinationInfo;

public class DestinationEvent extends EventObject {
    private static final long serialVersionUID = 2442156576867593780L;
    private DestinationInfo destinationInfo;

    public DestinationEvent(DestinationSource source, DestinationInfo destinationInfo) {
        super(source);
        this.destinationInfo = destinationInfo;
    }

    public ActiveMQDestination getDestination() {
        return getDestinationInfo().getDestination();
    }

    public boolean isAddOperation() {
        return getDestinationInfo().isAddOperation();
    }

    public long getTimeout() {
        return getDestinationInfo().getTimeout();
    }

    public boolean isRemoveOperation() {
        return getDestinationInfo().isRemoveOperation();
    }

    public DestinationInfo getDestinationInfo() {
        return this.destinationInfo;
    }
}
