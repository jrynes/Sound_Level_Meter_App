package org.apache.activemq.filter;

import java.util.Collection;
import java.util.Set;

public interface DestinationNode {
    void appendDescendantValues(Set<DestinationNode> set);

    void appendMatchingValues(Set<DestinationNode> set, String[] strArr, int i);

    void appendMatchingWildcards(Set<DestinationNode> set, String[] strArr, int i);

    DestinationNode getChild(String str);

    Collection<DestinationNode> getChildren();

    Collection<DestinationNode> getDesendentValues();

    Collection<DestinationNode> getValues();

    Collection<DestinationNode> removeDesendentValues();

    Collection<DestinationNode> removeValues();
}
