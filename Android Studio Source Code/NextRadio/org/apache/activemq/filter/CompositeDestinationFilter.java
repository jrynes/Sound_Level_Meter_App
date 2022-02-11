package org.apache.activemq.filter;

import org.apache.activemq.command.ActiveMQDestination;

public class CompositeDestinationFilter extends DestinationFilter {
    private DestinationFilter[] filters;

    public CompositeDestinationFilter(ActiveMQDestination destination) {
        ActiveMQDestination[] destinations = destination.getCompositeDestinations();
        this.filters = new DestinationFilter[destinations.length];
        for (int i = 0; i < destinations.length; i++) {
            this.filters[i] = DestinationFilter.parseFilter(destinations[i]);
        }
    }

    public boolean matches(ActiveMQDestination destination) {
        for (DestinationFilter matches : this.filters) {
            if (matches.matches(destination)) {
                return true;
            }
        }
        return false;
    }

    public boolean isWildcard() {
        return true;
    }
}
