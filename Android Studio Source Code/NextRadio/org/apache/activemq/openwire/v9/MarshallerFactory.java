package org.apache.activemq.openwire.v9;

import org.apache.activemq.openwire.DataStreamMarshaller;
import org.apache.activemq.openwire.OpenWireFormat;
import org.xbill.DNS.KEYRecord;

public class MarshallerFactory {
    private static final DataStreamMarshaller[] marshaller;

    static {
        marshaller = new DataStreamMarshaller[KEYRecord.OWNER_ZONE];
        add(new ActiveMQBlobMessageMarshaller());
        add(new ActiveMQBytesMessageMarshaller());
        add(new ActiveMQMapMessageMarshaller());
        add(new ActiveMQMessageMarshaller());
        add(new ActiveMQObjectMessageMarshaller());
        add(new ActiveMQQueueMarshaller());
        add(new ActiveMQStreamMessageMarshaller());
        add(new ActiveMQTempQueueMarshaller());
        add(new ActiveMQTempTopicMarshaller());
        add(new ActiveMQTextMessageMarshaller());
        add(new ActiveMQTopicMarshaller());
        add(new BrokerIdMarshaller());
        add(new BrokerInfoMarshaller());
        add(new ConnectionControlMarshaller());
        add(new ConnectionErrorMarshaller());
        add(new ConnectionIdMarshaller());
        add(new ConnectionInfoMarshaller());
        add(new ConsumerControlMarshaller());
        add(new ConsumerIdMarshaller());
        add(new ConsumerInfoMarshaller());
        add(new ControlCommandMarshaller());
        add(new DataArrayResponseMarshaller());
        add(new DataResponseMarshaller());
        add(new DestinationInfoMarshaller());
        add(new DiscoveryEventMarshaller());
        add(new ExceptionResponseMarshaller());
        add(new FlushCommandMarshaller());
        add(new IntegerResponseMarshaller());
        add(new JournalQueueAckMarshaller());
        add(new JournalTopicAckMarshaller());
        add(new JournalTraceMarshaller());
        add(new JournalTransactionMarshaller());
        add(new KeepAliveInfoMarshaller());
        add(new LastPartialCommandMarshaller());
        add(new LocalTransactionIdMarshaller());
        add(new MessageAckMarshaller());
        add(new MessageDispatchMarshaller());
        add(new MessageDispatchNotificationMarshaller());
        add(new MessageIdMarshaller());
        add(new MessagePullMarshaller());
        add(new NetworkBridgeFilterMarshaller());
        add(new PartialCommandMarshaller());
        add(new ProducerAckMarshaller());
        add(new ProducerIdMarshaller());
        add(new ProducerInfoMarshaller());
        add(new RemoveInfoMarshaller());
        add(new RemoveSubscriptionInfoMarshaller());
        add(new ReplayCommandMarshaller());
        add(new ResponseMarshaller());
        add(new SessionIdMarshaller());
        add(new SessionInfoMarshaller());
        add(new ShutdownInfoMarshaller());
        add(new SubscriptionInfoMarshaller());
        add(new TransactionInfoMarshaller());
        add(new WireFormatInfoMarshaller());
        add(new XATransactionIdMarshaller());
    }

    private static void add(DataStreamMarshaller dsm) {
        marshaller[dsm.getDataStructureType()] = dsm;
    }

    public static DataStreamMarshaller[] createMarshallerMap(OpenWireFormat wireFormat) {
        return marshaller;
    }
}
