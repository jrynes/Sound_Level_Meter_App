package org.apache.activemq.filter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.activemq.command.ActiveMQDestination;

public class DestinationMap {
    protected static final String ANY_CHILD = "*";
    protected static final String ANY_DESCENDENT = ">";
    private DestinationMapNode queueRootNode;
    private DestinationMapNode tempQueueRootNode;
    private DestinationMapNode tempTopicRootNode;
    private DestinationMapNode topicRootNode;

    public DestinationMap() {
        this.queueRootNode = new DestinationMapNode(null);
        this.tempQueueRootNode = new DestinationMapNode(null);
        this.topicRootNode = new DestinationMapNode(null);
        this.tempTopicRootNode = new DestinationMapNode(null);
    }

    public synchronized Set get(ActiveMQDestination key) {
        Set hashSet;
        if (key.isComposite()) {
            ActiveMQDestination[] destinations = key.getCompositeDestinations();
            hashSet = new HashSet(destinations.length);
            for (ActiveMQDestination childDestination : destinations) {
                Set value = get(childDestination);
                if (value instanceof Set) {
                    hashSet.addAll(value);
                } else if (value != null) {
                    hashSet.add(value);
                }
            }
        } else {
            hashSet = findWildcardMatches(key);
        }
        return hashSet;
    }

    public synchronized void put(ActiveMQDestination key, Object value) {
        if (key.isComposite()) {
            ActiveMQDestination[] destinations = key.getCompositeDestinations();
            for (ActiveMQDestination childDestination : destinations) {
                put(childDestination, value);
            }
        } else {
            getRootNode(key).add(key.getDestinationPaths(), 0, value);
        }
    }

    public synchronized void remove(ActiveMQDestination key, Object value) {
        if (key.isComposite()) {
            ActiveMQDestination[] destinations = key.getCompositeDestinations();
            for (ActiveMQDestination childDestination : destinations) {
                remove(childDestination, value);
            }
        } else {
            getRootNode(key).remove(key.getDestinationPaths(), 0, value);
        }
    }

    public int getTopicRootChildCount() {
        return this.topicRootNode.getChildCount();
    }

    public int getQueueRootChildCount() {
        return this.queueRootNode.getChildCount();
    }

    public DestinationMapNode getQueueRootNode() {
        return this.queueRootNode;
    }

    public DestinationMapNode getTopicRootNode() {
        return this.topicRootNode;
    }

    public DestinationMapNode getTempQueueRootNode() {
        return this.tempQueueRootNode;
    }

    public DestinationMapNode getTempTopicRootNode() {
        return this.tempTopicRootNode;
    }

    protected void setEntries(List<DestinationMapEntry> entries) {
        for (DestinationMapEntry element : entries) {
            Class<? extends DestinationMapEntry> type = getEntryClass();
            if (type.isInstance(element)) {
                DestinationMapEntry entry = element;
                put(entry.getDestination(), entry.getValue());
            } else {
                throw new IllegalArgumentException("Each entry must be an instance of type: " + type.getName() + " but was: " + element);
            }
        }
    }

    protected Class<? extends DestinationMapEntry> getEntryClass() {
        return DestinationMapEntry.class;
    }

    protected Set findWildcardMatches(ActiveMQDestination key) {
        String[] paths = key.getDestinationPaths();
        Set answer = new HashSet();
        getRootNode(key).appendMatchingValues(answer, paths, 0);
        return answer;
    }

    public Set removeAll(ActiveMQDestination key) {
        Set rc = new HashSet();
        if (key.isComposite()) {
            ActiveMQDestination[] destinations = key.getCompositeDestinations();
            for (ActiveMQDestination removeAll : destinations) {
                rc.add(removeAll(removeAll));
            }
        } else {
            getRootNode(key).removeAll(rc, key.getDestinationPaths(), 0);
        }
        return rc;
    }

    public Object chooseValue(ActiveMQDestination destination) {
        Set set = get(destination);
        if (set == null || set.isEmpty()) {
            return null;
        }
        return new TreeSet(set).last();
    }

    protected DestinationMapNode getRootNode(ActiveMQDestination key) {
        if (key.isTemporary()) {
            if (key.isQueue()) {
                return this.tempQueueRootNode;
            }
            return this.tempTopicRootNode;
        } else if (key.isQueue()) {
            return this.queueRootNode;
        } else {
            return this.topicRootNode;
        }
    }

    public void reset() {
        this.queueRootNode = new DestinationMapNode(null);
        this.tempQueueRootNode = new DestinationMapNode(null);
        this.topicRootNode = new DestinationMapNode(null);
        this.tempTopicRootNode = new DestinationMapNode(null);
    }

    public static Set union(Set existing, Set candidates) {
        if (candidates != null) {
            if (existing == null) {
                return candidates;
            }
            Iterator<Object> iterator = existing.iterator();
            while (iterator.hasNext()) {
                if (!candidates.contains(iterator.next())) {
                    iterator.remove();
                }
            }
            return existing;
        } else if (existing == null) {
            return existing;
        } else {
            existing.clear();
            return existing;
        }
    }
}
