package org.apache.activemq.filter;

public class DefaultDestinationMapEntry extends DestinationMapEntry {
    private DestinationMapEntry value;

    public DestinationMapEntry getValue() {
        return this.value;
    }

    public void setValue(DestinationMapEntry value) {
        this.value = value;
    }
}
