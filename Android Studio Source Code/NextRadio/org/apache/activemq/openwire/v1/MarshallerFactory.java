package org.apache.activemq.openwire.v1;

import org.apache.activemq.openwire.DataStreamMarshaller;
import org.apache.activemq.openwire.OpenWireFormat;
import org.xbill.DNS.KEYRecord;

public final class MarshallerFactory {
    private static final DataStreamMarshaller[] MARSHALLER;

    static {
        MARSHALLER = new DataStreamMarshaller[KEYRecord.OWNER_ZONE];
        add(new LocalTransactionIdMarshaller());
        add(new PartialCommandMarshaller());
        add(new IntegerResponseMarshaller());
        add(new ActiveMQQueueMarshaller());
        add(new ActiveMQObjectMessageMarshaller());
        add(new ConnectionIdMarshaller());
        add(new ConnectionInfoMarshaller());
        add(new ProducerInfoMarshaller());
        add(new MessageDispatchNotificationMarshaller());
        add(new SessionInfoMarshaller());
        add(new TransactionInfoMarshaller());
        add(new ActiveMQStreamMessageMarshaller());
        add(new MessageAckMarshaller());
        add(new ProducerIdMarshaller());
        add(new MessageIdMarshaller());
        add(new ActiveMQTempQueueMarshaller());
        add(new RemoveSubscriptionInfoMarshaller());
        add(new SessionIdMarshaller());
        add(new DataArrayResponseMarshaller());
        add(new JournalQueueAckMarshaller());
        add(new ResponseMarshaller());
        add(new ConnectionErrorMarshaller());
        add(new ConsumerInfoMarshaller());
        add(new XATransactionIdMarshaller());
        add(new JournalTraceMarshaller());
        add(new ConsumerIdMarshaller());
        add(new ActiveMQTextMessageMarshaller());
        add(new SubscriptionInfoMarshaller());
        add(new JournalTransactionMarshaller());
        add(new ControlCommandMarshaller());
        add(new LastPartialCommandMarshaller());
        add(new NetworkBridgeFilterMarshaller());
        add(new ActiveMQBytesMessageMarshaller());
        add(new WireFormatInfoMarshaller());
        add(new ActiveMQTempTopicMarshaller());
        add(new DiscoveryEventMarshaller());
        add(new ReplayCommandMarshaller());
        add(new ActiveMQTopicMarshaller());
        add(new BrokerInfoMarshaller());
        add(new DestinationInfoMarshaller());
        add(new ShutdownInfoMarshaller());
        add(new DataResponseMarshaller());
        add(new ConnectionControlMarshaller());
        add(new KeepAliveInfoMarshaller());
        add(new FlushCommandMarshaller());
        add(new ConsumerControlMarshaller());
        add(new JournalTopicAckMarshaller());
        add(new BrokerIdMarshaller());
        add(new MessageDispatchMarshaller());
        add(new ActiveMQMapMessageMarshaller());
        add(new ActiveMQMessageMarshaller());
        add(new RemoveInfoMarshaller());
        add(new ExceptionResponseMarshaller());
    }

    private MarshallerFactory() {
    }

    private static void add(DataStreamMarshaller dsm) {
        MARSHALLER[dsm.getDataStructureType()] = dsm;
    }

    public static DataStreamMarshaller[] createMarshallerMap(OpenWireFormat wireFormat) {
        return MARSHALLER;
    }
}
