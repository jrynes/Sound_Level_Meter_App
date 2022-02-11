package org.apache.activemq.filter;

import org.apache.activemq.command.ActiveMQDestination;

public class PrefixDestinationFilter extends DestinationFilter {
    private byte destinationType;
    private String[] prefixes;

    public PrefixDestinationFilter(String[] prefixes, byte destinationType) {
        this.prefixes = prefixes;
        this.destinationType = destinationType;
    }

    public boolean matches(ActiveMQDestination destination) {
        if (destination.getDestinationType() != this.destinationType) {
            return false;
        }
        String[] path = DestinationPath.getDestinationPaths(destination.getPhysicalName());
        int length = this.prefixes.length;
        if (path.length < length) {
            return false;
        }
        int size = length - 1;
        int i = 0;
        while (i < size) {
            if (!path[i].equals(DestinationFilter.ANY_CHILD) && !this.prefixes[i].equals(DestinationFilter.ANY_CHILD) && !this.prefixes[i].equals(path[i])) {
                return false;
            }
            i++;
        }
        return true;
    }

    public String getText() {
        return DestinationPath.toString(this.prefixes);
    }

    public String toString() {
        return super.toString() + "[destination: " + getText() + "]";
    }

    public boolean isWildcard() {
        return true;
    }
}
