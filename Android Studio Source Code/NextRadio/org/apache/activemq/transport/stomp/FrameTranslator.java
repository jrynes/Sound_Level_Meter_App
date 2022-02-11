package org.apache.activemq.transport.stomp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.jms.Destination;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;
import org.apache.activemq.transport.stomp.Stomp.Headers.Unsubscribe;

public interface FrameTranslator {

    public static final class Helper {
        private Helper() {
        }

        public static void copyStandardHeadersFromMessageToFrame(ProtocolConverter converter, ActiveMQMessage message, StompFrame command, FrameTranslator ft) throws IOException {
            Map<String, String> headers = command.getHeaders();
            headers.put(Unsubscribe.DESTINATION, ft.convertDestination(converter, message.getDestination()));
            headers.put(Message.MESSAGE_ID, message.getJMSMessageID());
            if (message.getJMSCorrelationID() != null) {
                headers.put(Send.CORRELATION_ID, message.getJMSCorrelationID());
            }
            headers.put(Send.EXPIRATION_TIME, Stomp.EMPTY + message.getJMSExpiration());
            if (message.getJMSRedelivered()) {
                headers.put(Message.REDELIVERED, Stomp.TRUE);
            }
            headers.put(Send.PRIORITY, Stomp.EMPTY + message.getJMSPriority());
            if (message.getJMSReplyTo() != null) {
                headers.put(Send.REPLY_TO, ft.convertDestination(converter, message.getJMSReplyTo()));
            }
            headers.put(Message.TIMESTAMP, Stomp.EMPTY + message.getJMSTimestamp());
            if (message.getJMSType() != null) {
                headers.put(Send.TYPE, message.getJMSType());
            }
            if (message.getUserID() != null) {
                headers.put(Message.USERID, message.getUserID());
            }
            if (message.getOriginalDestination() != null) {
                headers.put(Message.ORIGINAL_DESTINATION, ft.convertDestination(converter, message.getOriginalDestination()));
            }
            if (message.isPersistent()) {
                headers.put(Send.PERSISTENT, Stomp.TRUE);
            }
            Map<String, Object> properties = message.getProperties();
            if (properties != null) {
                for (Entry<String, Object> prop : properties.entrySet()) {
                    headers.put(prop.getKey(), Stomp.EMPTY + prop.getValue());
                }
            }
        }

        public static void copyStandardHeadersFromFrameToMessage(ProtocolConverter converter, StompFrame command, ActiveMQMessage msg, FrameTranslator ft) throws ProtocolException, JMSException {
            Map<String, String> headers = new HashMap(command.getHeaders());
            msg.setDestination(ft.convertDestination(converter, (String) headers.remove(Unsubscribe.DESTINATION), true));
            msg.setJMSCorrelationID((String) headers.remove(Send.CORRELATION_ID));
            Object o = headers.remove(Send.EXPIRATION_TIME);
            if (o != null) {
                msg.setJMSExpiration(Long.parseLong((String) o));
            }
            o = headers.remove(Send.PRIORITY);
            if (o != null) {
                msg.setJMSPriority(Integer.parseInt((String) o));
            } else {
                msg.setJMSPriority(4);
            }
            o = headers.remove(Send.TYPE);
            if (o != null) {
                msg.setJMSType((String) o);
            }
            o = headers.remove(Send.REPLY_TO);
            if (o != null) {
                try {
                    msg.setJMSReplyTo(ft.convertDestination(converter, (String) o, false));
                } catch (ProtocolException e) {
                    msg.setStringProperty(Send.REPLY_TO, (String) o);
                }
            }
            o = headers.remove(Send.PERSISTENT);
            if (o != null) {
                msg.setPersistent(Stomp.TRUE.equals(o));
            }
            headers.remove(Headers.RECEIPT_REQUESTED);
            headers.remove(Message.MESSAGE_ID);
            headers.remove(Message.TIMESTAMP);
            headers.remove(Message.REDELIVERED);
            headers.remove(Message.SUBSCRIPTION);
            headers.remove(Message.USERID);
            msg.setProperties(headers);
        }
    }

    String convertDestination(ProtocolConverter protocolConverter, Destination destination);

    ActiveMQDestination convertDestination(ProtocolConverter protocolConverter, String str, boolean z) throws ProtocolException;

    ActiveMQMessage convertFrame(ProtocolConverter protocolConverter, StompFrame stompFrame) throws JMSException, ProtocolException;

    StompFrame convertMessage(ProtocolConverter protocolConverter, ActiveMQMessage activeMQMessage) throws IOException, JMSException;
}
