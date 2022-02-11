package org.apache.activemq.command;

import org.apache.activemq.transport.stomp.Stomp.Headers;

public class ConsumerId implements DataStructure {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 122;
    protected String connectionId;
    protected transient int hashCode;
    protected transient String key;
    protected transient SessionId parentId;
    protected long sessionId;
    protected long value;

    public ConsumerId(SessionId sessionId, long consumerId) {
        this.connectionId = sessionId.getConnectionId();
        this.sessionId = sessionId.getValue();
        this.value = consumerId;
    }

    public ConsumerId(ConsumerId id) {
        this.connectionId = id.getConnectionId();
        this.sessionId = id.getSessionId();
        this.value = id.getValue();
    }

    public SessionId getParentId() {
        if (this.parentId == null) {
            this.parentId = new SessionId(this);
        }
        return this.parentId;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = (this.connectionId.hashCode() ^ ((int) this.sessionId)) ^ ((int) this.value);
        }
        return this.hashCode;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != ConsumerId.class) {
            return false;
        }
        ConsumerId id = (ConsumerId) o;
        if (this.sessionId == id.sessionId && this.value == id.value && this.connectionId.equals(id.connectionId)) {
            return true;
        }
        return false;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String toString() {
        if (this.key == null) {
            this.key = this.connectionId + Headers.SEPERATOR + this.sessionId + Headers.SEPERATOR + this.value;
        }
        return this.key;
    }

    public String getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public long getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long consumerId) {
        this.value = consumerId;
    }

    public boolean isMarshallAware() {
        return false;
    }
}
