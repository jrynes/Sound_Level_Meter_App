package org.apache.activemq.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import javax.jms.JMSException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.broker.region.MessageReference;
import org.apache.activemq.usage.MemoryUsage;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.ByteSequence;
import org.apache.activemq.util.MarshallingSupport;
import org.apache.activemq.wireformat.WireFormat;
import org.fusesource.hawtbuf.UTF8Buffer;

public abstract class Message extends BaseCommand implements MarshallAware, MessageReference {
    public static final int DEFAULT_MINIMUM_MESSAGE_SIZE = 1024;
    public static final String ORIGINAL_EXPIRATION = "originalExpiration";
    protected long arrival;
    protected long brokerInTime;
    protected long brokerOutTime;
    private BrokerId[] brokerPath;
    private BrokerId[] cluster;
    protected boolean compressed;
    private transient ActiveMQConnection connection;
    protected ByteSequence content;
    protected String correlationId;
    protected DataStructure dataStructure;
    protected ActiveMQDestination destination;
    protected boolean droppable;
    protected long expiration;
    protected String groupID;
    protected int groupSequence;
    protected ByteSequence marshalledProperties;
    transient MemoryUsage memoryUsage;
    protected MessageId messageId;
    protected ActiveMQDestination originalDestination;
    protected TransactionId originalTransactionId;
    protected boolean persistent;
    protected byte priority;
    protected ProducerId producerId;
    protected Map<String, Object> properties;
    protected boolean readOnlyBody;
    protected boolean readOnlyProperties;
    protected transient boolean recievedByDFBridge;
    protected int redeliveryCounter;
    private transient short referenceCount;
    transient MessageDestination regionDestination;
    protected ActiveMQDestination replyTo;
    protected int size;
    protected ConsumerId targetConsumerId;
    protected long timestamp;
    protected TransactionId transactionId;
    protected String type;
    protected String userID;

    public interface MessageDestination {
        MemoryUsage getMemoryUsage();

        int getMinimumMessageSize();
    }

    public abstract void clearBody() throws JMSException;

    public abstract Message copy();

    public abstract void storeContent();

    public void clearMarshalledState() throws JMSException {
        this.properties = null;
    }

    protected void copy(Message copy) {
        super.copy(copy);
        copy.producerId = this.producerId;
        copy.transactionId = this.transactionId;
        copy.destination = this.destination;
        copy.messageId = this.messageId != null ? this.messageId.copy() : null;
        copy.originalDestination = this.originalDestination;
        copy.originalTransactionId = this.originalTransactionId;
        copy.expiration = this.expiration;
        copy.timestamp = this.timestamp;
        copy.correlationId = this.correlationId;
        copy.replyTo = this.replyTo;
        copy.persistent = this.persistent;
        copy.redeliveryCounter = this.redeliveryCounter;
        copy.type = this.type;
        copy.priority = this.priority;
        copy.size = this.size;
        copy.groupID = this.groupID;
        copy.userID = this.userID;
        copy.groupSequence = this.groupSequence;
        if (this.properties != null) {
            copy.properties = new HashMap(this.properties);
            copy.properties.remove(ORIGINAL_EXPIRATION);
        } else {
            copy.properties = this.properties;
        }
        copy.content = this.content;
        copy.marshalledProperties = this.marshalledProperties;
        copy.dataStructure = this.dataStructure;
        copy.readOnlyProperties = this.readOnlyProperties;
        copy.readOnlyBody = this.readOnlyBody;
        copy.compressed = this.compressed;
        copy.recievedByDFBridge = this.recievedByDFBridge;
        copy.arrival = this.arrival;
        copy.connection = this.connection;
        copy.regionDestination = this.regionDestination;
        copy.brokerInTime = this.brokerInTime;
        copy.brokerOutTime = this.brokerOutTime;
        copy.memoryUsage = this.memoryUsage;
        copy.brokerPath = this.brokerPath;
    }

    public Object getProperty(String name) throws IOException {
        if (this.properties == null) {
            if (this.marshalledProperties == null) {
                return null;
            }
            this.properties = unmarsallProperties(this.marshalledProperties);
        }
        Object result = this.properties.get(name);
        if (result instanceof UTF8Buffer) {
            return result.toString();
        }
        return result;
    }

    public Map<String, Object> getProperties() throws IOException {
        if (this.properties == null) {
            if (this.marshalledProperties == null) {
                return Collections.EMPTY_MAP;
            }
            this.properties = unmarsallProperties(this.marshalledProperties);
        }
        return Collections.unmodifiableMap(this.properties);
    }

    public void clearProperties() {
        this.marshalledProperties = null;
        this.properties = null;
    }

    public void setProperty(String name, Object value) throws IOException {
        lazyCreateProperties();
        this.properties.put(name, value);
    }

    public void removeProperty(String name) throws IOException {
        lazyCreateProperties();
        this.properties.remove(name);
    }

    protected void lazyCreateProperties() throws IOException {
        if (this.properties != null) {
            return;
        }
        if (this.marshalledProperties == null) {
            this.properties = new HashMap();
            return;
        }
        this.properties = unmarsallProperties(this.marshalledProperties);
        this.marshalledProperties = null;
    }

    private Map<String, Object> unmarsallProperties(ByteSequence marshalledProperties) throws IOException {
        return MarshallingSupport.unmarshalPrimitiveMap(new DataInputStream(new ByteArrayInputStream(marshalledProperties)));
    }

    public void beforeMarshall(WireFormat wireFormat) throws IOException {
        if (this.marshalledProperties == null && this.properties != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(baos);
            MarshallingSupport.marshalPrimitiveMap(this.properties, os);
            os.close();
            this.marshalledProperties = baos.toByteSequence();
        }
    }

    public void afterMarshall(WireFormat wireFormat) throws IOException {
    }

    public void beforeUnmarshall(WireFormat wireFormat) throws IOException {
    }

    public void afterUnmarshall(WireFormat wireFormat) throws IOException {
    }

    public ProducerId getProducerId() {
        return this.producerId;
    }

    public void setProducerId(ProducerId producerId) {
        this.producerId = producerId;
    }

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public TransactionId getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isInTransaction() {
        return this.transactionId != null;
    }

    public ActiveMQDestination getOriginalDestination() {
        return this.originalDestination;
    }

    public void setOriginalDestination(ActiveMQDestination destination) {
        this.originalDestination = destination;
    }

    public MessageId getMessageId() {
        return this.messageId;
    }

    public void setMessageId(MessageId messageId) {
        this.messageId = messageId;
    }

    public TransactionId getOriginalTransactionId() {
        return this.originalTransactionId;
    }

    public void setOriginalTransactionId(TransactionId transactionId) {
        this.originalTransactionId = transactionId;
    }

    public String getGroupID() {
        return this.groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public int getGroupSequence() {
        return this.groupSequence;
    }

    public void setGroupSequence(int groupSequence) {
        this.groupSequence = groupSequence;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public void setPersistent(boolean deliveryMode) {
        this.persistent = deliveryMode;
    }

    public long getExpiration() {
        return this.expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public byte getPriority() {
        return this.priority;
    }

    public void setPriority(byte priority) {
        if (priority < null) {
            this.priority = (byte) 0;
        } else if (priority > (byte) 9) {
            this.priority = (byte) 9;
        } else {
            this.priority = priority;
        }
    }

    public ActiveMQDestination getReplyTo() {
        return this.replyTo;
    }

    public void setReplyTo(ActiveMQDestination replyTo) {
        this.replyTo = replyTo;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ByteSequence getContent() {
        return this.content;
    }

    public void setContent(ByteSequence content) {
        this.content = content;
    }

    public ByteSequence getMarshalledProperties() {
        return this.marshalledProperties;
    }

    public void setMarshalledProperties(ByteSequence marshalledProperties) {
        this.marshalledProperties = marshalledProperties;
    }

    public DataStructure getDataStructure() {
        return this.dataStructure;
    }

    public void setDataStructure(DataStructure data) {
        this.dataStructure = data;
    }

    public ConsumerId getTargetConsumerId() {
        return this.targetConsumerId;
    }

    public void setTargetConsumerId(ConsumerId targetConsumerId) {
        this.targetConsumerId = targetConsumerId;
    }

    public boolean isExpired() {
        long expireTime = getExpiration();
        return expireTime > 0 && System.currentTimeMillis() > expireTime;
    }

    public boolean isAdvisory() {
        return this.type != null && this.type.equals(AdvisorySupport.ADIVSORY_MESSAGE_TYPE);
    }

    public boolean isCompressed() {
        return this.compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public boolean isRedelivered() {
        return this.redeliveryCounter > 0;
    }

    public void setRedelivered(boolean redelivered) {
        if (redelivered) {
            if (!isRedelivered()) {
                setRedeliveryCounter(1);
            }
        } else if (isRedelivered()) {
            setRedeliveryCounter(0);
        }
    }

    public void incrementRedeliveryCounter() {
        this.redeliveryCounter++;
    }

    public int getRedeliveryCounter() {
        return this.redeliveryCounter;
    }

    public void setRedeliveryCounter(int deliveryCounter) {
        this.redeliveryCounter = deliveryCounter;
    }

    public BrokerId[] getBrokerPath() {
        return this.brokerPath;
    }

    public void setBrokerPath(BrokerId[] brokerPath) {
        this.brokerPath = brokerPath;
    }

    public boolean isReadOnlyProperties() {
        return this.readOnlyProperties;
    }

    public void setReadOnlyProperties(boolean readOnlyProperties) {
        this.readOnlyProperties = readOnlyProperties;
    }

    public boolean isReadOnlyBody() {
        return this.readOnlyBody;
    }

    public void setReadOnlyBody(boolean readOnlyBody) {
        this.readOnlyBody = readOnlyBody;
    }

    public ActiveMQConnection getConnection() {
        return this.connection;
    }

    public void setConnection(ActiveMQConnection connection) {
        this.connection = connection;
    }

    public long getArrival() {
        return this.arrival;
    }

    public void setArrival(long arrival) {
        this.arrival = arrival;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String jmsxUserID) {
        this.userID = jmsxUserID;
    }

    public int getReferenceCount() {
        return this.referenceCount;
    }

    public Message getMessageHardRef() {
        return this;
    }

    public Message getMessage() {
        return this;
    }

    public void setRegionDestination(MessageDestination destination) {
        this.regionDestination = destination;
        if (this.memoryUsage == null) {
            this.memoryUsage = destination.getMemoryUsage();
        }
    }

    public MessageDestination getRegionDestination() {
        return this.regionDestination;
    }

    public MemoryUsage getMemoryUsage() {
        return this.memoryUsage;
    }

    public void setMemoryUsage(MemoryUsage usage) {
        this.memoryUsage = usage;
    }

    public boolean isMarshallAware() {
        return true;
    }

    public int incrementReferenceCount() {
        int rc;
        synchronized (this) {
            rc = (short) (this.referenceCount + 1);
            this.referenceCount = rc;
            int size = getSize();
        }
        if (rc == 1 && getMemoryUsage() != null) {
            getMemoryUsage().increaseUsage((long) size);
        }
        return rc;
    }

    public int decrementReferenceCount() {
        int rc;
        synchronized (this) {
            rc = (short) (this.referenceCount - 1);
            this.referenceCount = rc;
            int size = getSize();
        }
        if (rc == 0 && getMemoryUsage() != null) {
            getMemoryUsage().decreaseUsage((long) size);
        }
        return rc;
    }

    public int getSize() {
        int minimumMessageSize = getMinimumMessageSize();
        if (this.size < minimumMessageSize || this.size == 0) {
            this.size = minimumMessageSize;
            if (this.marshalledProperties != null) {
                this.size += this.marshalledProperties.getLength();
            }
            if (this.content != null) {
                this.size += this.content.getLength();
            }
        }
        return this.size;
    }

    protected int getMinimumMessageSize() {
        MessageDestination dest = this.regionDestination;
        if (dest != null) {
            return dest.getMinimumMessageSize();
        }
        return DEFAULT_MINIMUM_MESSAGE_SIZE;
    }

    public boolean isRecievedByDFBridge() {
        return this.recievedByDFBridge;
    }

    public void setRecievedByDFBridge(boolean recievedByDFBridge) {
        this.recievedByDFBridge = recievedByDFBridge;
    }

    public void onMessageRolledBack() {
        incrementRedeliveryCounter();
    }

    public boolean isDroppable() {
        return this.droppable;
    }

    public void setDroppable(boolean droppable) {
        this.droppable = droppable;
    }

    public BrokerId[] getCluster() {
        return this.cluster;
    }

    public void setCluster(BrokerId[] cluster) {
        this.cluster = cluster;
    }

    public boolean isMessage() {
        return true;
    }

    public long getBrokerInTime() {
        return this.brokerInTime;
    }

    public void setBrokerInTime(long brokerInTime) {
        this.brokerInTime = brokerInTime;
    }

    public long getBrokerOutTime() {
        return this.brokerOutTime;
    }

    public void setBrokerOutTime(long brokerOutTime) {
        this.brokerOutTime = brokerOutTime;
    }

    public boolean isDropped() {
        return false;
    }

    public void compress() throws IOException {
        if (!isCompressed()) {
            storeContent();
            if (!isCompressed() && getContent() != null) {
                doCompress();
            }
        }
    }

    protected void doCompress() throws IOException {
        this.compressed = true;
        ByteSequence bytes = getContent();
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        OutputStream os = new DeflaterOutputStream(bytesOut);
        os.write(bytes.data, bytes.offset, bytes.length);
        os.close();
        setContent(bytesOut.toByteSequence());
    }

    public String toString() {
        return toString(null);
    }

    public String toString(Map<String, Object> overrideFields) {
        try {
            getProperties();
        } catch (IOException e) {
        }
        return super.toString(overrideFields);
    }
}
