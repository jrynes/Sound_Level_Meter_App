package org.apache.activemq.command;

import javax.jms.JMSException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActiveMQTempDestination extends ActiveMQDestination {
    private static final Logger LOG;
    protected transient ActiveMQConnection connection;
    protected transient String connectionId;
    protected transient int sequenceId;

    static {
        LOG = LoggerFactory.getLogger(ActiveMQTempDestination.class);
    }

    public ActiveMQTempDestination(String name) {
        super(name);
    }

    public ActiveMQTempDestination(String connectionId, long sequenceId) {
        super(connectionId + Headers.SEPERATOR + sequenceId);
    }

    public boolean isTemporary() {
        return true;
    }

    public void delete() throws JMSException {
        if (this.connection != null) {
            this.connection.deleteTempDestination(this);
        }
    }

    public ActiveMQConnection getConnection() {
        return this.connection;
    }

    public void setConnection(ActiveMQConnection connection) {
        this.connection = connection;
    }

    public void setPhysicalName(String physicalName) {
        super.setPhysicalName(physicalName);
        if (!isComposite()) {
            int p = this.physicalName.lastIndexOf(Headers.SEPERATOR);
            if (p >= 0) {
                String seqStr = this.physicalName.substring(p + 1).trim();
                if (seqStr != null && seqStr.length() > 0) {
                    try {
                        this.sequenceId = Integer.parseInt(seqStr);
                    } catch (NumberFormatException e) {
                        LOG.debug("Did not parse sequence Id from " + physicalName);
                    }
                    this.connectionId = this.physicalName.substring(0, p);
                }
            }
        }
    }

    public String getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public int getSequenceId() {
        return this.sequenceId;
    }
}
