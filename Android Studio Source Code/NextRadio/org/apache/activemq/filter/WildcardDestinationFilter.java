package org.apache.activemq.filter;

import org.apache.activemq.command.ActiveMQDestination;

public class WildcardDestinationFilter extends DestinationFilter {
    private byte destinationType;
    private String[] prefixes;

    public WildcardDestinationFilter(String[] prefixes, byte destinationType) {
        this.prefixes = new String[prefixes.length];
        for (int i = 0; i < prefixes.length; i++) {
            String prefix = prefixes[i];
            if (!prefix.equals(DestinationFilter.ANY_CHILD)) {
                this.prefixes[i] = prefix;
            }
        }
        this.destinationType = destinationType;
    }

    public boolean matches(ActiveMQDestination destination) {
        if (destination.getDestinationType() != this.destinationType) {
            return false;
        }
        String[] path = DestinationPath.getDestinationPaths(destination);
        int length = this.prefixes.length;
        if (path.length != length) {
            return false;
        }
        int i = 0;
        while (i < length) {
            String prefix = this.prefixes[i];
            if (prefix != null && !prefix.equals(path[i])) {
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
