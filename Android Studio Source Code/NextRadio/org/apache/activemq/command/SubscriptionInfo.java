package org.apache.activemq.command;

import org.apache.activemq.util.IntrospectionSupport;

public class SubscriptionInfo implements DataStructure {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 55;
    protected String clientId;
    protected ActiveMQDestination destination;
    protected String selector;
    protected ActiveMQDestination subscribedDestination;
    protected String subscriptionName;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public String getSelector() {
        return this.selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getSubcriptionName() {
        return this.subscriptionName;
    }

    public void setSubcriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public String getSubscriptionName() {
        return this.subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public boolean isMarshallAware() {
        return false;
    }

    public String toString() {
        return IntrospectionSupport.toString(this);
    }

    public int hashCode() {
        int h1;
        int h2;
        if (this.clientId != null) {
            h1 = this.clientId.hashCode();
        } else {
            h1 = -1;
        }
        if (this.subscriptionName != null) {
            h2 = this.subscriptionName.hashCode();
        } else {
            h2 = -1;
        }
        return h1 ^ h2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SubscriptionInfo)) {
            return false;
        }
        SubscriptionInfo other = (SubscriptionInfo) obj;
        return ((this.clientId == null && other.clientId == null) || !(this.clientId == null || other.clientId == null || !this.clientId.equals(other.clientId))) && ((this.subscriptionName == null && other.subscriptionName == null) || !(this.subscriptionName == null || other.subscriptionName == null || !this.subscriptionName.equals(other.subscriptionName)));
    }

    public ActiveMQDestination getSubscribedDestination() {
        if (this.subscribedDestination == null) {
            return getDestination();
        }
        return this.subscribedDestination;
    }

    public void setSubscribedDestination(ActiveMQDestination subscribedDestination) {
        this.subscribedDestination = subscribedDestination;
    }
}
