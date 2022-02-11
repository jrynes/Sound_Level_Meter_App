package org.apache.activemq.filter;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.ActiveMQTopic;

public abstract class DestinationMapEntry<T> implements Comparable<T> {
    protected ActiveMQDestination destination;

    public int compareTo(Object that) {
        if (that instanceof DestinationMapEntry) {
            return ActiveMQDestination.compare(this.destination, ((DestinationMapEntry) that).destination);
        } else if (that == null) {
            return 1;
        } else {
            return getClass().getName().compareTo(that.getClass().getName());
        }
    }

    public void setQueue(String name) {
        setDestination(new ActiveMQQueue(name));
    }

    public void setTopic(String name) {
        setDestination(new ActiveMQTopic(name));
    }

    public void setTempTopic(boolean flag) {
        setDestination(new ActiveMQTempTopic(DestinationFilter.ANY_DESCENDENT));
    }

    public void setTempQueue(boolean flag) {
        setDestination(new ActiveMQTempQueue(DestinationFilter.ANY_DESCENDENT));
    }

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public Comparable<T> getValue() {
        return this;
    }
}
