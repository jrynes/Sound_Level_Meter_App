package org.apache.activemq.filter;

import org.apache.activemq.command.ActiveMQDestination;

public class SimpleDestinationFilter extends DestinationFilter {
    private ActiveMQDestination destination;

    public SimpleDestinationFilter(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public boolean matches(ActiveMQDestination destination) {
        return this.destination.equals(destination);
    }

    public boolean isWildcard() {
        return false;
    }
}
