package org.apache.activemq;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Message;
import org.apache.activemq.broker.region.MessageReference;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.util.BitArrayBin;
import org.apache.activemq.util.IdGenerator;
import org.apache.activemq.util.LRUCache;

public class ActiveMQMessageAuditNoSync implements Serializable {
    public static final int DEFAULT_WINDOW_SIZE = 2048;
    public static final int MAXIMUM_PRODUCER_COUNT = 64;
    private static final long serialVersionUID = 1;
    private int auditDepth;
    private final LRUCache<Object, BitArrayBin> map;
    private int maximumNumberOfProducersToTrack;
    private transient boolean modified;

    public ActiveMQMessageAuditNoSync() {
        this(DEFAULT_WINDOW_SIZE, MAXIMUM_PRODUCER_COUNT);
    }

    public ActiveMQMessageAuditNoSync(int auditDepth, int maximumNumberOfProducersToTrack) {
        this.modified = true;
        this.auditDepth = auditDepth;
        this.maximumNumberOfProducersToTrack = maximumNumberOfProducersToTrack;
        this.map = new LRUCache(0, maximumNumberOfProducersToTrack, 0.75f, true);
    }

    public int getAuditDepth() {
        return this.auditDepth;
    }

    public void setAuditDepth(int auditDepth) {
        this.auditDepth = auditDepth;
        this.modified = true;
    }

    public int getMaximumNumberOfProducersToTrack() {
        return this.maximumNumberOfProducersToTrack;
    }

    public void setMaximumNumberOfProducersToTrack(int maximumNumberOfProducersToTrack) {
        if (maximumNumberOfProducersToTrack < this.maximumNumberOfProducersToTrack) {
            LRUCache<Object, BitArrayBin> newMap = new LRUCache(0, maximumNumberOfProducersToTrack, 0.75f, true);
            newMap.putAll(this.map);
            this.map.clear();
            this.map.putAll(newMap);
        }
        this.map.setMaxCacheSize(maximumNumberOfProducersToTrack);
        this.maximumNumberOfProducersToTrack = maximumNumberOfProducersToTrack;
        this.modified = true;
    }

    public boolean isDuplicate(Message message) throws JMSException {
        return isDuplicate(message.getJMSMessageID());
    }

    public boolean isDuplicate(String id) {
        String seed = IdGenerator.getSeedFromId(id);
        if (seed == null) {
            return false;
        }
        BitArrayBin bab = (BitArrayBin) this.map.get(seed);
        if (bab == null) {
            bab = new BitArrayBin(this.auditDepth);
            this.map.put(seed, bab);
            this.modified = true;
        }
        long index = IdGenerator.getSequenceFromId(id);
        if (index < 0) {
            return false;
        }
        boolean answer = bab.setBit(index, true);
        this.modified = true;
        return answer;
    }

    public boolean isDuplicate(MessageReference message) {
        return isDuplicate(message.getMessageId());
    }

    public boolean isDuplicate(MessageId id) {
        if (id == null) {
            return false;
        }
        ProducerId pid = id.getProducerId();
        if (pid == null) {
            return false;
        }
        BitArrayBin bab = (BitArrayBin) this.map.get(pid);
        if (bab == null) {
            bab = new BitArrayBin(this.auditDepth);
            this.map.put(pid, bab);
            this.modified = true;
        }
        return bab.setBit(id.getProducerSequenceId(), true);
    }

    public void rollback(MessageReference message) {
        rollback(message.getMessageId());
    }

    public void rollback(MessageId id) {
        if (id != null) {
            ProducerId pid = id.getProducerId();
            if (pid != null) {
                BitArrayBin bab = (BitArrayBin) this.map.get(pid);
                if (bab != null) {
                    bab.setBit(id.getProducerSequenceId(), false);
                    this.modified = true;
                }
            }
        }
    }

    public void rollback(String id) {
        String seed = IdGenerator.getSeedFromId(id);
        if (seed != null) {
            BitArrayBin bab = (BitArrayBin) this.map.get(seed);
            if (bab != null) {
                bab.setBit(IdGenerator.getSequenceFromId(id), false);
                this.modified = true;
            }
        }
    }

    public boolean isInOrder(Message msg) throws JMSException {
        return isInOrder(msg.getJMSMessageID());
    }

    public boolean isInOrder(String id) {
        if (id == null) {
            return true;
        }
        String seed = IdGenerator.getSeedFromId(id);
        if (seed == null) {
            return true;
        }
        BitArrayBin bab = (BitArrayBin) this.map.get(seed);
        if (bab == null) {
            return true;
        }
        boolean answer = bab.isInOrder(IdGenerator.getSequenceFromId(id));
        this.modified = true;
        return answer;
    }

    public boolean isInOrder(MessageReference message) {
        return isInOrder(message.getMessageId());
    }

    public boolean isInOrder(MessageId id) {
        if (id == null) {
            return false;
        }
        ProducerId pid = id.getProducerId();
        if (pid == null) {
            return false;
        }
        BitArrayBin bab = (BitArrayBin) this.map.get(pid);
        if (bab == null) {
            bab = new BitArrayBin(this.auditDepth);
            this.map.put(pid, bab);
            this.modified = true;
        }
        return bab.isInOrder(id.getProducerSequenceId());
    }

    public long getLastSeqId(ProducerId id) {
        BitArrayBin bab = (BitArrayBin) this.map.get(id.toString());
        if (bab != null) {
            return bab.getLastSetIndex();
        }
        return -1;
    }

    public void clear() {
        this.map.clear();
    }

    public boolean isModified() {
        return this.modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean modified() {
        if (!this.modified) {
            return false;
        }
        this.modified = false;
        return true;
    }
}
