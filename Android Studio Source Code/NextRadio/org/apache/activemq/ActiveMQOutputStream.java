package org.apache.activemq;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.ProducerInfo;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IntrospectionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.KEYRecord.Flags;

public class ActiveMQOutputStream extends OutputStream implements Disposable {
    public static final String AMQ_STREAM_CHUNK_SIZE = "AMQ_STREAM_CHUNK_SIZE";
    private static final Logger LOG;
    private boolean addPropertiesOnFirstMsgOnly;
    private boolean alwaysSyncSend;
    final byte[] buffer;
    private boolean closed;
    private final ActiveMQConnection connection;
    protected int count;
    private final int deliveryMode;
    private final ProducerInfo info;
    private long messageSequence;
    private final int priority;
    private final Map<String, Object> properties;
    private final long timeToLive;

    static {
        LOG = LoggerFactory.getLogger(ActiveMQOutputStream.class);
    }

    public ActiveMQOutputStream(ActiveMQConnection connection, ProducerId producerId, ActiveMQDestination destination, Map<String, Object> properties, int deliveryMode, int priority, long timeToLive) throws JMSException {
        this.alwaysSyncSend = false;
        this.addPropertiesOnFirstMsgOnly = false;
        this.connection = connection;
        this.deliveryMode = deliveryMode;
        this.priority = priority;
        this.timeToLive = timeToLive;
        this.properties = properties == null ? null : new HashMap(properties);
        Integer chunkSize = this.properties == null ? null : (Integer) this.properties.get(AMQ_STREAM_CHUNK_SIZE);
        if (chunkSize == null) {
            chunkSize = Integer.valueOf(AccessibilityNodeInfoCompat.ACTION_CUT);
        } else if (chunkSize.intValue() < 1) {
            throw new IllegalArgumentException("Chunk size must be greater then 0");
        } else {
            chunkSize = Integer.valueOf(chunkSize.intValue() * Flags.FLAG5);
        }
        this.buffer = new byte[chunkSize.intValue()];
        if (destination == null) {
            throw new InvalidDestinationException("Don't understand null destinations");
        }
        this.info = new ProducerInfo(producerId);
        if (destination.getOptions() != null) {
            Map<String, String> options = new HashMap(destination.getOptions());
            IntrospectionSupport.setProperties(this, options, "producer.");
            IntrospectionSupport.setProperties(this.info, options, "producer.");
            if (options.size() > 0) {
                String msg = "There are " + options.size() + " producer options that couldn't be set on the producer." + " Check the options are spelled correctly." + " Unknown parameters=[" + options + "]." + " This producer cannot be started.";
                LOG.warn(msg);
                throw new ConfigurationException(msg);
            }
        }
        this.info.setDestination(destination);
        this.connection.addOutputStream(this);
        this.connection.asyncSendPacket(this.info);
    }

    public void close() throws IOException {
        if (!this.closed) {
            flushBuffer();
            try {
                send(new ActiveMQMessage(), true);
                dispose();
                this.connection.asyncSendPacket(this.info.createRemoveCommand());
            } catch (Exception e) {
                IOExceptionSupport.create(e);
            }
        }
    }

    public void dispose() {
        if (!this.closed) {
            this.connection.removeOutputStream(this);
            this.closed = true;
        }
    }

    public synchronized void write(int b) throws IOException {
        byte[] bArr = this.buffer;
        int i = this.count;
        this.count = i + 1;
        bArr[i] = (byte) b;
        if (this.count == this.buffer.length) {
            flushBuffer();
        }
    }

    public synchronized void write(byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            int max = Math.min(len, this.buffer.length - this.count);
            System.arraycopy(b, off, this.buffer, this.count, max);
            len -= max;
            this.count += max;
            off += max;
            if (this.count == this.buffer.length) {
                flushBuffer();
            }
        }
    }

    public synchronized void flush() throws IOException {
        flushBuffer();
    }

    private void flushBuffer() throws IOException {
        if (this.count != 0) {
            try {
                ActiveMQBytesMessage msg = new ActiveMQBytesMessage();
                msg.writeBytes(this.buffer, 0, this.count);
                send(msg, false);
                this.count = 0;
            } catch (Exception e) {
                throw IOExceptionSupport.create(e);
            }
        }
    }

    private void send(ActiveMQMessage msg, boolean eosMessage) throws JMSException {
        if (this.properties != null && (this.messageSequence == 0 || !this.addPropertiesOnFirstMsgOnly)) {
            for (String key : this.properties.keySet()) {
                msg.setObjectProperty(key, this.properties.get(key));
            }
        }
        msg.setType("org.apache.activemq.Stream");
        msg.setGroupID(this.info.getProducerId().toString());
        if (eosMessage) {
            msg.setGroupSequence(-1);
        } else {
            msg.setGroupSequence((int) this.messageSequence);
        }
        ProducerId producerId = this.info.getProducerId();
        long j = this.messageSequence;
        this.messageSequence = 1 + j;
        MessageId id = new MessageId(producerId, j);
        ActiveMQConnection activeMQConnection = this.connection;
        ActiveMQDestination destination = this.info.getDestination();
        int i = this.deliveryMode;
        int i2 = this.priority;
        long j2 = this.timeToLive;
        boolean z = (eosMessage || isAlwaysSyncSend()) ? false : true;
        activeMQConnection.send(destination, msg, id, i, i2, j2, z);
    }

    public String toString() {
        return "ActiveMQOutputStream { producerId=" + this.info.getProducerId() + " }";
    }

    public boolean isAlwaysSyncSend() {
        return this.alwaysSyncSend;
    }

    public void setAlwaysSyncSend(boolean alwaysSyncSend) {
        this.alwaysSyncSend = alwaysSyncSend;
    }

    public boolean isAddPropertiesOnFirstMsgOnly() {
        return this.addPropertiesOnFirstMsgOnly;
    }

    public void setAddPropertiesOnFirstMsgOnly(boolean propertiesOnFirstMsgOnly) {
        this.addPropertiesOnFirstMsgOnly = propertiesOnFirstMsgOnly;
    }
}
