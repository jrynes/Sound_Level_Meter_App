package org.apache.activemq.advisory;

import java.util.ArrayList;
import javax.jms.Destination;
import javax.jms.JMSException;
import org.apache.activemq.ActiveMQMessageTransformation;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public final class AdvisorySupport {
    public static final String ADIVSORY_MESSAGE_TYPE = "Advisory";
    public static final String ADVISORY_TOPIC_PREFIX = "ActiveMQ.Advisory.";
    public static final String AGENT_TOPIC = "ActiveMQ.Agent";
    private static final ActiveMQTopic AGENT_TOPIC_DESTINATION;
    public static final ActiveMQTopic ALL_DESTINATIONS_COMPOSITE_ADVISORY_TOPIC;
    public static final ActiveMQTopic CONNECTION_ADVISORY_TOPIC;
    public static final String CONSUMER_ADVISORY_TOPIC_PREFIX = "ActiveMQ.Advisory.Consumer.";
    public static final String EXPIRED_QUEUE_MESSAGES_TOPIC_PREFIX = "ActiveMQ.Advisory.Expired.Queue.";
    public static final String EXPIRED_TOPIC_MESSAGES_TOPIC_PREFIX = "ActiveMQ.Advisory.Expired.Topic.";
    public static final String FAST_PRODUCER_TOPIC_PREFIX = "ActiveMQ.Advisory.FastProducer.";
    public static final String FULL_TOPIC_PREFIX = "ActiveMQ.Advisory.FULL.";
    public static final String MASTER_BROKER_TOPIC_PREFIX = "ActiveMQ.Advisory.MasterBroker";
    public static final String MESSAGE_CONSUMED_TOPIC_PREFIX = "ActiveMQ.Advisory.MessageConsumed.";
    public static final String MESSAGE_DELIVERED_TOPIC_PREFIX = "ActiveMQ.Advisory.MessageDelivered.";
    public static final String MESSAGE_DISCAREDED_TOPIC_PREFIX = "ActiveMQ.Advisory.MessageDiscarded.";
    public static final String MESSAGE_DLQ_TOPIC_PREFIX = "ActiveMQ.Advisory.MessageDLQd.";
    public static final String MSG_PROPERTY_CONSUMER_COUNT = "consumerCount";
    public static final String MSG_PROPERTY_CONSUMER_ID = "consumerId";
    public static final String MSG_PROPERTY_DISCARDED_COUNT = "discardedCount";
    public static final String MSG_PROPERTY_MESSAGE_ID = "orignalMessageId";
    public static final String MSG_PROPERTY_ORIGIN_BROKER_ID = "originBrokerId";
    public static final String MSG_PROPERTY_ORIGIN_BROKER_NAME = "originBrokerName";
    public static final String MSG_PROPERTY_ORIGIN_BROKER_URL = "originBrokerURL";
    public static final String MSG_PROPERTY_PRODUCER_ID = "producerId";
    public static final String MSG_PROPERTY_USAGE_NAME = "usageName";
    public static final String NETWORK_BRIDGE_FORWARD_FAILURE_TOPIC_PREFIX = "ActiveMQ.Advisory.NetworkBridge.ForwardFailure";
    public static final String NETWORK_BRIDGE_TOPIC_PREFIX = "ActiveMQ.Advisory.NetworkBridge";
    public static final String NO_QUEUE_CONSUMERS_TOPIC_PREFIX = "ActiveMQ.Advisory.NoConsumer.Queue.";
    public static final String NO_TOPIC_CONSUMERS_TOPIC_PREFIX = "ActiveMQ.Advisory.NoConsumer.Topic.";
    public static final String PRODUCER_ADVISORY_TOPIC_PREFIX = "ActiveMQ.Advisory.Producer.";
    public static final ActiveMQTopic QUEUE_ADVISORY_TOPIC;
    public static final String QUEUE_CONSUMER_ADVISORY_TOPIC_PREFIX = "ActiveMQ.Advisory.Consumer.Queue.";
    public static final String QUEUE_PRODUCER_ADVISORY_TOPIC_PREFIX = "ActiveMQ.Advisory.Producer.Queue.";
    public static final String SLOW_CONSUMER_TOPIC_PREFIX = "ActiveMQ.Advisory.SlowConsumer.";
    public static final ActiveMQTopic TEMP_DESTINATION_COMPOSITE_ADVISORY_TOPIC;
    public static final ActiveMQTopic TEMP_QUEUE_ADVISORY_TOPIC;
    public static final ActiveMQTopic TEMP_TOPIC_ADVISORY_TOPIC;
    public static final ActiveMQTopic TOPIC_ADVISORY_TOPIC;
    public static final String TOPIC_CONSUMER_ADVISORY_TOPIC_PREFIX = "ActiveMQ.Advisory.Consumer.Topic.";
    public static final String TOPIC_PRODUCER_ADVISORY_TOPIC_PREFIX = "ActiveMQ.Advisory.Producer.Topic.";

    static {
        CONNECTION_ADVISORY_TOPIC = new ActiveMQTopic("ActiveMQ.Advisory.Connection");
        QUEUE_ADVISORY_TOPIC = new ActiveMQTopic("ActiveMQ.Advisory.Queue");
        TOPIC_ADVISORY_TOPIC = new ActiveMQTopic("ActiveMQ.Advisory.Topic");
        TEMP_QUEUE_ADVISORY_TOPIC = new ActiveMQTopic("ActiveMQ.Advisory.TempQueue");
        TEMP_TOPIC_ADVISORY_TOPIC = new ActiveMQTopic("ActiveMQ.Advisory.TempTopic");
        ALL_DESTINATIONS_COMPOSITE_ADVISORY_TOPIC = new ActiveMQTopic(TOPIC_ADVISORY_TOPIC.getPhysicalName() + Stomp.COMMA + QUEUE_ADVISORY_TOPIC.getPhysicalName() + Stomp.COMMA + TEMP_QUEUE_ADVISORY_TOPIC.getPhysicalName() + Stomp.COMMA + TEMP_TOPIC_ADVISORY_TOPIC.getPhysicalName());
        TEMP_DESTINATION_COMPOSITE_ADVISORY_TOPIC = new ActiveMQTopic(TEMP_QUEUE_ADVISORY_TOPIC.getPhysicalName() + Stomp.COMMA + TEMP_TOPIC_ADVISORY_TOPIC.getPhysicalName());
        AGENT_TOPIC_DESTINATION = new ActiveMQTopic(AGENT_TOPIC);
    }

    private AdvisorySupport() {
    }

    public static ActiveMQTopic getConnectionAdvisoryTopic() {
        return CONNECTION_ADVISORY_TOPIC;
    }

    public static ActiveMQTopic[] getAllDestinationAdvisoryTopics(Destination destination) throws JMSException {
        return getAllDestinationAdvisoryTopics(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic[] getAllDestinationAdvisoryTopics(ActiveMQDestination destination) throws JMSException {
        ArrayList<ActiveMQTopic> result = new ArrayList();
        result.add(getConsumerAdvisoryTopic(destination));
        result.add(getProducerAdvisoryTopic(destination));
        result.add(getExpiredMessageTopic(destination));
        result.add(getNoConsumersAdvisoryTopic(destination));
        result.add(getSlowConsumerAdvisoryTopic(destination));
        result.add(getFastProducerAdvisoryTopic(destination));
        result.add(getMessageDiscardedAdvisoryTopic(destination));
        result.add(getMessageDeliveredAdvisoryTopic(destination));
        result.add(getMessageConsumedAdvisoryTopic(destination));
        result.add(getMessageDLQdAdvisoryTopic(destination));
        result.add(getFullAdvisoryTopic(destination));
        return (ActiveMQTopic[]) result.toArray(new ActiveMQTopic[0]);
    }

    public static ActiveMQTopic getConsumerAdvisoryTopic(Destination destination) throws JMSException {
        return getConsumerAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getConsumerAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isQueue()) {
            return new ActiveMQTopic(QUEUE_CONSUMER_ADVISORY_TOPIC_PREFIX + destination.getPhysicalName());
        }
        return new ActiveMQTopic(TOPIC_CONSUMER_ADVISORY_TOPIC_PREFIX + destination.getPhysicalName());
    }

    public static ActiveMQTopic getProducerAdvisoryTopic(Destination destination) throws JMSException {
        return getProducerAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getProducerAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isQueue()) {
            return new ActiveMQTopic(QUEUE_PRODUCER_ADVISORY_TOPIC_PREFIX + destination.getPhysicalName());
        }
        return new ActiveMQTopic(TOPIC_PRODUCER_ADVISORY_TOPIC_PREFIX + destination.getPhysicalName());
    }

    public static ActiveMQTopic getExpiredMessageTopic(Destination destination) throws JMSException {
        return getExpiredMessageTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getExpiredMessageTopic(ActiveMQDestination destination) {
        if (destination.isQueue()) {
            return getExpiredQueueMessageAdvisoryTopic(destination);
        }
        return getExpiredTopicMessageAdvisoryTopic(destination);
    }

    public static ActiveMQTopic getExpiredTopicMessageAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(EXPIRED_TOPIC_MESSAGES_TOPIC_PREFIX + destination.getPhysicalName());
    }

    public static ActiveMQTopic getExpiredQueueMessageAdvisoryTopic(Destination destination) throws JMSException {
        return getExpiredQueueMessageAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getExpiredQueueMessageAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(EXPIRED_QUEUE_MESSAGES_TOPIC_PREFIX + destination.getPhysicalName());
    }

    public static ActiveMQTopic getNoConsumersAdvisoryTopic(Destination destination) throws JMSException {
        return getExpiredMessageTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getNoConsumersAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isQueue()) {
            return getNoQueueConsumersAdvisoryTopic(destination);
        }
        return getNoTopicConsumersAdvisoryTopic(destination);
    }

    public static ActiveMQTopic getNoTopicConsumersAdvisoryTopic(Destination destination) throws JMSException {
        return getNoTopicConsumersAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getNoTopicConsumersAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(NO_TOPIC_CONSUMERS_TOPIC_PREFIX + destination.getPhysicalName());
    }

    public static ActiveMQTopic getNoQueueConsumersAdvisoryTopic(Destination destination) throws JMSException {
        return getNoQueueConsumersAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getNoQueueConsumersAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(NO_QUEUE_CONSUMERS_TOPIC_PREFIX + destination.getPhysicalName());
    }

    public static ActiveMQTopic getSlowConsumerAdvisoryTopic(Destination destination) throws JMSException {
        return getSlowConsumerAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getSlowConsumerAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(SLOW_CONSUMER_TOPIC_PREFIX + destination.getDestinationTypeAsString() + ActiveMQDestination.PATH_SEPERATOR + destination.getPhysicalName());
    }

    public static ActiveMQTopic getFastProducerAdvisoryTopic(Destination destination) throws JMSException {
        return getFastProducerAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getFastProducerAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(FAST_PRODUCER_TOPIC_PREFIX + destination.getDestinationTypeAsString() + ActiveMQDestination.PATH_SEPERATOR + destination.getPhysicalName());
    }

    public static ActiveMQTopic getMessageDiscardedAdvisoryTopic(Destination destination) throws JMSException {
        return getMessageDiscardedAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getMessageDiscardedAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(MESSAGE_DISCAREDED_TOPIC_PREFIX + destination.getDestinationTypeAsString() + ActiveMQDestination.PATH_SEPERATOR + destination.getPhysicalName());
    }

    public static ActiveMQTopic getMessageDeliveredAdvisoryTopic(Destination destination) throws JMSException {
        return getMessageDeliveredAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getMessageDeliveredAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(MESSAGE_DELIVERED_TOPIC_PREFIX + destination.getDestinationTypeAsString() + ActiveMQDestination.PATH_SEPERATOR + destination.getPhysicalName());
    }

    public static ActiveMQTopic getMessageConsumedAdvisoryTopic(Destination destination) throws JMSException {
        return getMessageConsumedAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getMessageConsumedAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(MESSAGE_CONSUMED_TOPIC_PREFIX + destination.getDestinationTypeAsString() + ActiveMQDestination.PATH_SEPERATOR + destination.getPhysicalName());
    }

    public static ActiveMQTopic getMessageDLQdAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(MESSAGE_DLQ_TOPIC_PREFIX + destination.getDestinationTypeAsString() + ActiveMQDestination.PATH_SEPERATOR + destination.getPhysicalName());
    }

    public static ActiveMQTopic getMasterBrokerAdvisoryTopic() {
        return new ActiveMQTopic(MASTER_BROKER_TOPIC_PREFIX);
    }

    public static ActiveMQTopic getNetworkBridgeAdvisoryTopic() {
        return new ActiveMQTopic(NETWORK_BRIDGE_TOPIC_PREFIX);
    }

    public static ActiveMQTopic getFullAdvisoryTopic(Destination destination) throws JMSException {
        return getFullAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getFullAdvisoryTopic(ActiveMQDestination destination) {
        return new ActiveMQTopic(FULL_TOPIC_PREFIX + destination.getDestinationTypeAsString() + ActiveMQDestination.PATH_SEPERATOR + destination.getPhysicalName());
    }

    public static ActiveMQTopic getDestinationAdvisoryTopic(Destination destination) throws JMSException {
        return getDestinationAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static ActiveMQTopic getDestinationAdvisoryTopic(ActiveMQDestination destination) {
        switch (destination.getDestinationType()) {
            case Zone.PRIMARY /*1*/:
                return QUEUE_ADVISORY_TOPIC;
            case Zone.SECONDARY /*2*/:
                return TOPIC_ADVISORY_TOPIC;
            case Service.RJE /*5*/:
                return TEMP_QUEUE_ADVISORY_TOPIC;
            case Protocol.TCP /*6*/:
                return TEMP_TOPIC_ADVISORY_TOPIC;
            default:
                throw new RuntimeException("Unknown destination type: " + destination.getDestinationType());
        }
    }

    public static boolean isDestinationAdvisoryTopic(Destination destination) throws JMSException {
        return isDestinationAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isTempDestinationAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isTempDestinationAdvisoryTopic : compositeDestinations) {
                if (!isTempDestinationAdvisoryTopic(isTempDestinationAdvisoryTopic)) {
                    return false;
                }
            }
            return true;
        } else if (destination.equals(TEMP_QUEUE_ADVISORY_TOPIC) || destination.equals(TEMP_TOPIC_ADVISORY_TOPIC)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDestinationAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isDestinationAdvisoryTopic : compositeDestinations) {
                if (isDestinationAdvisoryTopic(isDestinationAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.equals(TEMP_QUEUE_ADVISORY_TOPIC) || destination.equals(TEMP_TOPIC_ADVISORY_TOPIC) || destination.equals(QUEUE_ADVISORY_TOPIC) || destination.equals(TOPIC_ADVISORY_TOPIC)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAdvisoryTopic(Destination destination) throws JMSException {
        return isAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isAdvisoryTopic(ActiveMQDestination destination) {
        if (destination == null) {
            return false;
        }
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isAdvisoryTopic : compositeDestinations) {
                if (isAdvisoryTopic(isAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(ADVISORY_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isConnectionAdvisoryTopic(Destination destination) throws JMSException {
        return isConnectionAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isConnectionAdvisoryTopic(ActiveMQDestination destination) {
        if (!destination.isComposite()) {
            return destination.equals(CONNECTION_ADVISORY_TOPIC);
        }
        ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
        for (ActiveMQDestination isConnectionAdvisoryTopic : compositeDestinations) {
            if (isConnectionAdvisoryTopic(isConnectionAdvisoryTopic)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isProducerAdvisoryTopic(Destination destination) throws JMSException {
        return isProducerAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isProducerAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isProducerAdvisoryTopic : compositeDestinations) {
                if (isProducerAdvisoryTopic(isProducerAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(PRODUCER_ADVISORY_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isConsumerAdvisoryTopic(Destination destination) throws JMSException {
        return isConsumerAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isConsumerAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isConsumerAdvisoryTopic : compositeDestinations) {
                if (isConsumerAdvisoryTopic(isConsumerAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(CONSUMER_ADVISORY_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSlowConsumerAdvisoryTopic(Destination destination) throws JMSException {
        return isSlowConsumerAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isSlowConsumerAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isSlowConsumerAdvisoryTopic : compositeDestinations) {
                if (isSlowConsumerAdvisoryTopic(isSlowConsumerAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(SLOW_CONSUMER_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFastProducerAdvisoryTopic(Destination destination) throws JMSException {
        return isFastProducerAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isFastProducerAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isFastProducerAdvisoryTopic : compositeDestinations) {
                if (isFastProducerAdvisoryTopic(isFastProducerAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(FAST_PRODUCER_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMessageConsumedAdvisoryTopic(Destination destination) throws JMSException {
        return isMessageConsumedAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isMessageConsumedAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isMessageConsumedAdvisoryTopic : compositeDestinations) {
                if (isMessageConsumedAdvisoryTopic(isMessageConsumedAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(MESSAGE_CONSUMED_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMasterBrokerAdvisoryTopic(Destination destination) throws JMSException {
        return isMasterBrokerAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isMasterBrokerAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isMasterBrokerAdvisoryTopic : compositeDestinations) {
                if (isMasterBrokerAdvisoryTopic(isMasterBrokerAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(MASTER_BROKER_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMessageDeliveredAdvisoryTopic(Destination destination) throws JMSException {
        return isMessageDeliveredAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isMessageDeliveredAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isMessageDeliveredAdvisoryTopic : compositeDestinations) {
                if (isMessageDeliveredAdvisoryTopic(isMessageDeliveredAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(MESSAGE_DELIVERED_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMessageDiscardedAdvisoryTopic(Destination destination) throws JMSException {
        return isMessageDiscardedAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isMessageDiscardedAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isMessageDiscardedAdvisoryTopic : compositeDestinations) {
                if (isMessageDiscardedAdvisoryTopic(isMessageDiscardedAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(MESSAGE_DISCAREDED_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFullAdvisoryTopic(Destination destination) throws JMSException {
        return isFullAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isFullAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isFullAdvisoryTopic : compositeDestinations) {
                if (isFullAdvisoryTopic(isFullAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(FULL_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNetworkBridgeAdvisoryTopic(Destination destination) throws JMSException {
        return isNetworkBridgeAdvisoryTopic(ActiveMQMessageTransformation.transformDestination(destination));
    }

    public static boolean isNetworkBridgeAdvisoryTopic(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            ActiveMQDestination[] compositeDestinations = destination.getCompositeDestinations();
            for (ActiveMQDestination isNetworkBridgeAdvisoryTopic : compositeDestinations) {
                if (isNetworkBridgeAdvisoryTopic(isNetworkBridgeAdvisoryTopic)) {
                    return true;
                }
            }
            return false;
        } else if (destination.isTopic() && destination.getPhysicalName().startsWith(NETWORK_BRIDGE_TOPIC_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static Destination getAgentDestination() {
        return AGENT_TOPIC_DESTINATION;
    }

    public static ActiveMQTopic getNetworkBridgeForwardFailureAdvisoryTopic() {
        return new ActiveMQTopic(NETWORK_BRIDGE_FORWARD_FAILURE_TOPIC_PREFIX);
    }
}
