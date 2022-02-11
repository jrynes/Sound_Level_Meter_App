package org.apache.activemq.command;

import org.apache.activemq.transport.stomp.Stomp.Headers;

public class SessionId implements DataStructure {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 121;
    protected String connectionId;
    protected transient int hashCode;
    protected transient String key;
    protected transient ConnectionId parentId;
    protected long value;

    public SessionId(ConnectionId connectionId, long sessionId) {
        this.connectionId = connectionId.getValue();
        this.value = sessionId;
    }

    public SessionId(SessionId id) {
        this.connectionId = id.getConnectionId();
        this.value = id.getValue();
    }

    public SessionId(ProducerId id) {
        this.connectionId = id.getConnectionId();
        this.value = id.getSessionId();
    }

    public SessionId(ConsumerId id) {
        this.connectionId = id.getConnectionId();
        this.value = id.getSessionId();
    }

    public ConnectionId getParentId() {
        if (this.parentId == null) {
            this.parentId = new ConnectionId(this);
        }
        return this.parentId;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.connectionId.hashCode() ^ ((int) this.value);
        }
        return this.hashCode;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != SessionId.class) {
            return false;
        }
        SessionId id = (SessionId) o;
        if (this.value == id.value && this.connectionId.equals(id.connectionId)) {
            return true;
        }
        return false;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long sessionId) {
        this.value = sessionId;
    }

    public String toString() {
        if (this.key == null) {
            this.key = this.connectionId + Headers.SEPERATOR + this.value;
        }
        return this.key;
    }

    public boolean isMarshallAware() {
        return false;
    }
}
