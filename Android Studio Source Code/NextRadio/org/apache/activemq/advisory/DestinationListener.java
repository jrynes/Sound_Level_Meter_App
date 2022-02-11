package org.apache.activemq.advisory;

public interface DestinationListener {
    void onDestinationEvent(DestinationEvent destinationEvent);
}
