package org.apache.activemq;

import javax.jms.JMSException;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTempDestination;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.DestinationInfo;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdvisoryConsumer implements ActiveMQDispatcher {
    private static final transient Logger LOG;
    private boolean closed;
    private final ActiveMQConnection connection;
    int deliveredCounter;
    private ConsumerInfo info;

    static {
        LOG = LoggerFactory.getLogger(AdvisoryConsumer.class);
    }

    public AdvisoryConsumer(ActiveMQConnection connection, ConsumerId consumerId) throws JMSException {
        this.connection = connection;
        this.info = new ConsumerInfo(consumerId);
        this.info.setDestination(AdvisorySupport.TEMP_DESTINATION_COMPOSITE_ADVISORY_TOPIC);
        this.info.setPrefetchSize(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH);
        this.info.setNoLocal(true);
        this.info.setDispatchAsync(true);
        this.connection.addDispatcher(this.info.getConsumerId(), this);
        this.connection.syncSendPacket(this.info);
    }

    public synchronized void dispose() {
        if (!this.closed) {
            try {
                this.connection.asyncSendPacket(this.info.createRemoveCommand());
            } catch (JMSException e) {
                LOG.debug("Failed to send remove command: " + e, e);
            }
            this.connection.removeDispatcher(this.info.getConsumerId());
            this.closed = true;
        }
    }

    public void dispatch(MessageDispatch md) {
        this.deliveredCounter++;
        if (((double) this.deliveredCounter) > 0.75d * ((double) this.info.getPrefetchSize())) {
            try {
                this.connection.asyncSendPacket(new MessageAck(md, (byte) 2, this.deliveredCounter));
                this.deliveredCounter = 0;
            } catch (JMSException e) {
                this.connection.onClientInternalException(e);
            }
        }
        DataStructure o = md.getMessage().getDataStructure();
        if (o != null && o.getClass() == DestinationInfo.class) {
            processDestinationInfo((DestinationInfo) o);
        } else if (LOG.isDebugEnabled()) {
            LOG.debug("Unexpected message was dispatched to the AdvisoryConsumer: " + md);
        }
    }

    private void processDestinationInfo(DestinationInfo dinfo) {
        ActiveMQDestination dest = dinfo.getDestination();
        if (dest.isTemporary()) {
            ActiveMQTempDestination tempDest = (ActiveMQTempDestination) dest;
            if (dinfo.getOperationType() == null) {
                if (tempDest.getConnection() != null) {
                    tempDest = (ActiveMQTempDestination) tempDest.createDestination(tempDest.getPhysicalName());
                }
                this.connection.activeTempDestinations.put(tempDest, tempDest);
            } else if (dinfo.getOperationType() == 1) {
                this.connection.activeTempDestinations.remove(tempDest);
            }
        }
    }
}
