package org.apache.activemq.transport.stomp;

import java.io.IOException;
import javax.jms.JMSException;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;
import org.apache.activemq.transport.stomp.Stomp.Headers.Subscribe;
import org.apache.activemq.transport.stomp.Stomp.Headers.Unsubscribe;
import org.apache.activemq.transport.stomp.Stomp.Responses;

public class StompQueueBrowserSubscription extends StompSubscription {
    public StompQueueBrowserSubscription(ProtocolConverter stompTransport, String subscriptionId, ConsumerInfo consumerInfo, String transformation) {
        super(stompTransport, subscriptionId, consumerInfo, transformation);
    }

    void onMessageDispatch(MessageDispatch md, String ackId) throws IOException, JMSException {
        if (md.getMessage() != null) {
            super.onMessageDispatch(md, ackId);
            return;
        }
        StompFrame browseDone = new StompFrame(Responses.MESSAGE);
        browseDone.getHeaders().put(Message.SUBSCRIPTION, getSubscriptionId());
        browseDone.getHeaders().put(Subscribe.BROWSER, Stomp.END);
        browseDone.getHeaders().put(Unsubscribe.DESTINATION, this.protocolConverter.findTranslator(null).convertDestination(this.protocolConverter, this.destination));
        browseDone.getHeaders().put(Message.MESSAGE_ID, "0");
        this.protocolConverter.sendToStomp(browseDone);
    }

    public MessageAck onStompMessageNack(String messageId, TransactionId transactionId) throws ProtocolException {
        throw new ProtocolException("Cannot Nack a message on a Queue Browser Subscription.");
    }
}
