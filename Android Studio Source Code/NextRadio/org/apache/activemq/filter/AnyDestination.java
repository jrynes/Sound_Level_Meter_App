package org.apache.activemq.filter;

import org.apache.activemq.command.ActiveMQDestination;

public class AnyDestination extends ActiveMQDestination {
    public AnyDestination(ActiveMQDestination[] destinations) {
        super(destinations);
        this.physicalName = "0";
    }

    protected String getQualifiedPrefix() {
        return "Any://";
    }

    public byte getDestinationType() {
        return (byte) 0;
    }

    public byte getDataStructureType() {
        throw new IllegalStateException("not for marshalling");
    }

    public boolean isQueue() {
        return true;
    }

    public boolean isTopic() {
        return true;
    }
}
