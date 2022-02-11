package org.apache.activemq.command;

public class ConnectionId implements DataStructure, Comparable<ConnectionId> {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 120;
    protected String value;

    public ConnectionId(String connectionId) {
        this.value = connectionId;
    }

    public ConnectionId(ConnectionId id) {
        this.value = id.getValue();
    }

    public ConnectionId(SessionId id) {
        this.value = id.getConnectionId();
    }

    public ConnectionId(ProducerId id) {
        this.value = id.getConnectionId();
    }

    public ConnectionId(ConsumerId id) {
        this.value = id.getConnectionId();
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != ConnectionId.class) {
            return false;
        }
        return this.value.equals(((ConnectionId) o).value);
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String toString() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String connectionId) {
        this.value = connectionId;
    }

    public boolean isMarshallAware() {
        return false;
    }

    public int compareTo(ConnectionId o) {
        return this.value.compareTo(o.value);
    }
}
