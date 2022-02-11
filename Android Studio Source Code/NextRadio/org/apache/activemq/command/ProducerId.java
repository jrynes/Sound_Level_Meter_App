package org.apache.activemq.command;

import org.apache.activemq.transport.stomp.Stomp.Headers;

public class ProducerId implements DataStructure {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 123;
    protected String connectionId;
    protected transient int hashCode;
    protected transient String key;
    protected transient SessionId parentId;
    protected long sessionId;
    protected long value;

    public ProducerId(SessionId sessionId, long producerId) {
        this.connectionId = sessionId.getConnectionId();
        this.sessionId = sessionId.getValue();
        this.value = producerId;
    }

    public ProducerId(ProducerId id) {
        this.connectionId = id.getConnectionId();
        this.sessionId = id.getSessionId();
        this.value = id.getValue();
    }

    public ProducerId(String producerKey) {
        int p = producerKey.lastIndexOf(Headers.SEPERATOR);
        if (p >= 0) {
            this.value = Long.parseLong(producerKey.substring(p + 1));
            producerKey = producerKey.substring(0, p);
        }
        setProducerSessionKey(producerKey);
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
        if (o == null || o.getClass() != ProducerId.class) {
            return false;
        }
        ProducerId id = (ProducerId) o;
        if (this.sessionId == id.sessionId && this.value == id.value && this.connectionId.equals(id.connectionId)) {
            return true;
        }
        return false;
    }

    private void setProducerSessionKey(String sessionKey) {
        int p = sessionKey.lastIndexOf(Headers.SEPERATOR);
        if (p >= 0) {
            this.sessionId = Long.parseLong(sessionKey.substring(p + 1));
            sessionKey = sessionKey.substring(0, p);
        }
        this.connectionId = sessionKey;
    }

    public String toString() {
        if (this.key == null) {
            this.key = this.connectionId + Headers.SEPERATOR + this.sessionId + Headers.SEPERATOR + this.value;
        }
        return this.key;
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

    public void setValue(long producerId) {
        this.value = producerId;
    }

    public long getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isMarshallAware() {
        return false;
    }
}
