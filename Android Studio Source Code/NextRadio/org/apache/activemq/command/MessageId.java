package org.apache.activemq.command;

import java.util.concurrent.atomic.AtomicReference;
import org.apache.activemq.transport.stomp.Stomp.Headers;

public class MessageId implements DataStructure, Comparable<MessageId> {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 110;
    protected long brokerSequenceId;
    private transient AtomicReference<Object> dataLocator;
    private transient Object entryLocator;
    private transient int hashCode;
    private transient String key;
    private transient Object plistLocator;
    protected ProducerId producerId;
    protected long producerSequenceId;

    public MessageId() {
        this.dataLocator = new AtomicReference();
        this.producerId = new ProducerId();
    }

    public MessageId(ProducerInfo producerInfo, long producerSequenceId) {
        this.dataLocator = new AtomicReference();
        this.producerId = producerInfo.getProducerId();
        this.producerSequenceId = producerSequenceId;
    }

    public MessageId(String messageKey) {
        this.dataLocator = new AtomicReference();
        setValue(messageKey);
    }

    public MessageId(String producerId, long producerSequenceId) {
        this(new ProducerId(producerId), producerSequenceId);
    }

    public MessageId(ProducerId producerId, long producerSequenceId) {
        this.dataLocator = new AtomicReference();
        this.producerId = producerId;
        this.producerSequenceId = producerSequenceId;
    }

    public void setValue(String messageKey) {
        this.key = messageKey;
        int p = messageKey.lastIndexOf(Headers.SEPERATOR);
        if (p >= 0) {
            this.producerSequenceId = Long.parseLong(messageKey.substring(p + 1));
            messageKey = messageKey.substring(0, p);
        }
        this.producerId = new ProducerId(messageKey);
    }

    public void setTextView(String key) {
        this.key = key;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        MessageId id = (MessageId) o;
        if (this.producerSequenceId == id.producerSequenceId && this.producerId.equals(id.producerId)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.producerId.hashCode() ^ ((int) this.producerSequenceId);
        }
        return this.hashCode;
    }

    public String toString() {
        if (this.key == null) {
            this.key = this.producerId.toString() + Headers.SEPERATOR + this.producerSequenceId;
        }
        return this.key;
    }

    public ProducerId getProducerId() {
        return this.producerId;
    }

    public void setProducerId(ProducerId producerId) {
        this.producerId = producerId;
    }

    public long getProducerSequenceId() {
        return this.producerSequenceId;
    }

    public void setProducerSequenceId(long producerSequenceId) {
        this.producerSequenceId = producerSequenceId;
    }

    public long getBrokerSequenceId() {
        return this.brokerSequenceId;
    }

    public void setBrokerSequenceId(long brokerSequenceId) {
        this.brokerSequenceId = brokerSequenceId;
    }

    public boolean isMarshallAware() {
        return false;
    }

    public MessageId copy() {
        MessageId copy = new MessageId(this.producerId, this.producerSequenceId);
        copy.key = this.key;
        copy.brokerSequenceId = this.brokerSequenceId;
        copy.dataLocator = new AtomicReference(this.dataLocator != null ? this.dataLocator.get() : null);
        copy.entryLocator = this.entryLocator;
        copy.plistLocator = this.plistLocator;
        return copy;
    }

    public int compareTo(MessageId other) {
        if (other != null) {
            return toString().compareTo(other.toString());
        }
        return -1;
    }

    public Object getDataLocator() {
        return this.dataLocator.get();
    }

    public void setDataLocator(Object value) {
        this.dataLocator.set(value);
    }

    public Object getEntryLocator() {
        return this.entryLocator;
    }

    public void setEntryLocator(Object entryLocator) {
        this.entryLocator = entryLocator;
    }

    public Object getPlistLocator() {
        return this.plistLocator;
    }

    public void setPlistLocator(Object plistLocator) {
        this.plistLocator = plistLocator;
    }
}
