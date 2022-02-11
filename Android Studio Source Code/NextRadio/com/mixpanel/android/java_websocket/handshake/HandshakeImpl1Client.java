package com.mixpanel.android.java_websocket.handshake;

import org.apache.activemq.filter.DestinationFilter;

public class HandshakeImpl1Client extends HandshakedataImpl1 implements ClientHandshakeBuilder {
    private String resourceDescriptor;

    public HandshakeImpl1Client() {
        this.resourceDescriptor = DestinationFilter.ANY_CHILD;
    }

    public void setResourceDescriptor(String resourceDescriptor) throws IllegalArgumentException {
        if (resourceDescriptor == null) {
            throw new IllegalArgumentException("http resource descriptor must not be null");
        }
        this.resourceDescriptor = resourceDescriptor;
    }

    public String getResourceDescriptor() {
        return this.resourceDescriptor;
    }
}
