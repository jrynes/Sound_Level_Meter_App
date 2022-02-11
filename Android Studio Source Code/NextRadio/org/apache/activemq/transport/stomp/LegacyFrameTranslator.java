package org.apache.activemq.transport.stomp;

import com.mixpanel.android.java_websocket.framing.CloseFrame;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.jms.Destination;
import javax.jms.JMSException;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.transport.stomp.FrameTranslator.Helper;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.transport.stomp.Stomp.Responses;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.ByteSequence;

public class LegacyFrameTranslator implements FrameTranslator {
    public ActiveMQMessage convertFrame(ProtocolConverter converter, StompFrame command) throws JMSException, ProtocolException {
        ActiveMQMessage msg;
        ProtocolException protocolException;
        Map<?, ?> headers = command.getHeaders();
        ActiveMQMessage text;
        ByteArrayOutputStream bytes;
        DataOutputStream data;
        if (headers.containsKey(Headers.AMQ_MESSAGE_TYPE)) {
            String intendedType = (String) headers.get(Headers.AMQ_MESSAGE_TYPE);
            if (intendedType.equalsIgnoreCase("text")) {
                text = new ActiveMQTextMessage();
                try {
                    bytes = new ByteArrayOutputStream(command.getContent().length + 4);
                    data = new DataOutputStream(bytes);
                    data.writeInt(command.getContent().length);
                    data.write(command.getContent());
                    text.setContent(bytes.toByteSequence());
                    data.close();
                    msg = text;
                } catch (Throwable e) {
                    protocolException = new ProtocolException("Text could not bet set: " + e, false, e);
                }
            } else if (intendedType.equalsIgnoreCase("bytes")) {
                ActiveMQMessage byteMessage = new ActiveMQBytesMessage();
                byteMessage.writeBytes(command.getContent());
                msg = byteMessage;
            } else {
                throw new ProtocolException("Unsupported message type '" + intendedType + "'", false);
            }
        } else if (headers.containsKey(Headers.CONTENT_LENGTH)) {
            headers.remove(Headers.CONTENT_LENGTH);
            ActiveMQMessage bm = new ActiveMQBytesMessage();
            bm.writeBytes(command.getContent());
            msg = bm;
        } else {
            text = new ActiveMQTextMessage();
            try {
                bytes = new ByteArrayOutputStream(command.getContent().length + 4);
                data = new DataOutputStream(bytes);
                data.writeInt(command.getContent().length);
                data.write(command.getContent());
                text.setContent(bytes.toByteSequence());
                data.close();
                msg = text;
            } catch (Throwable e2) {
                protocolException = new ProtocolException("Text could not bet set: " + e2, false, e2);
            }
        }
        Helper.copyStandardHeadersFromFrameToMessage(converter, command, msg, this);
        return msg;
    }

    public StompFrame convertMessage(ProtocolConverter converter, ActiveMQMessage message) throws IOException, JMSException {
        StompFrame command = new StompFrame();
        command.setAction(Responses.MESSAGE);
        Map<String, String> headers = new HashMap(25);
        command.setHeaders(headers);
        Helper.copyStandardHeadersFromMessageToFrame(converter, message, command, this);
        if (message.getDataStructureType() == 28) {
            if (message.isCompressed() || message.getContent() == null) {
                ActiveMQTextMessage msg = (ActiveMQTextMessage) message.copy();
                if (msg.getText() != null) {
                    command.setContent(msg.getText().getBytes(HttpRequest.CHARSET_UTF8));
                }
            } else {
                ByteSequence msgContent = message.getContent();
                if (msgContent.getLength() > 4) {
                    byte[] content = new byte[(msgContent.getLength() - 4)];
                    System.arraycopy(msgContent.data, 4, content, 0, content.length);
                    command.setContent(content);
                }
            }
        } else if (message.getDataStructureType() == 24) {
            ActiveMQBytesMessage msg2 = (ActiveMQBytesMessage) message.copy();
            msg2.setReadOnlyBody(true);
            byte[] data = new byte[((int) msg2.getBodyLength())];
            msg2.readBytes(data);
            headers.put(Headers.CONTENT_LENGTH, Integer.toString(data.length));
            command.setContent(data);
        } else if (message.getDataStructureType() == 23 && AdvisorySupport.ADIVSORY_MESSAGE_TYPE.equals(message.getType())) {
            Helper.copyStandardHeadersFromMessageToFrame(converter, message, command, this);
            command.setContent(marshallAdvisory(message.getDataStructure()).getBytes(HttpRequest.CHARSET_UTF8));
        }
        return command;
    }

    public String convertDestination(ProtocolConverter converter, Destination d) {
        if (d == null) {
            return null;
        }
        ActiveMQDestination activeMQDestination = (ActiveMQDestination) d;
        String physicalName = activeMQDestination.getPhysicalName();
        String rc = converter.getCreatedTempDestinationName(activeMQDestination);
        if (rc != null) {
            return rc;
        }
        StringBuilder buffer = new StringBuilder();
        if (activeMQDestination.isQueue()) {
            if (activeMQDestination.isTemporary()) {
                buffer.append("/remote-temp-queue/");
            } else {
                buffer.append("/queue/");
            }
        } else if (activeMQDestination.isTemporary()) {
            buffer.append("/remote-temp-topic/");
        } else {
            buffer.append("/topic/");
        }
        buffer.append(physicalName);
        return buffer.toString();
    }

    public ActiveMQDestination convertDestination(ProtocolConverter converter, String name, boolean forceFallback) throws ProtocolException {
        if (name == null) {
            return null;
        }
        String originalName = name;
        name = name.trim();
        if (name.startsWith("/queue/")) {
            return ActiveMQDestination.createDestination(name.substring("/queue/".length(), name.length()), (byte) 1);
        }
        if (name.startsWith("/topic/")) {
            return ActiveMQDestination.createDestination(name.substring("/topic/".length(), name.length()), (byte) 2);
        }
        if (name.startsWith("/remote-temp-queue/")) {
            return ActiveMQDestination.createDestination(name.substring("/remote-temp-queue/".length(), name.length()), (byte) 5);
        }
        if (name.startsWith("/remote-temp-topic/")) {
            return ActiveMQDestination.createDestination(name.substring("/remote-temp-topic/".length(), name.length()), (byte) 6);
        }
        if (name.startsWith("/temp-queue/")) {
            return converter.createTempDestination(name, false);
        }
        if (name.startsWith("/temp-topic/")) {
            return converter.createTempDestination(name, true);
        }
        if (forceFallback) {
            try {
                ActiveMQDestination fallback = ActiveMQDestination.getUnresolvableDestinationTransformer().transform(originalName);
                if (fallback != null) {
                    return fallback;
                }
            } catch (JMSException e) {
                throw new ProtocolException("Illegal destination name: [" + originalName + "] -- ActiveMQ STOMP destinations " + "must begin with one of: /queue/ /topic/ /temp-queue/ /temp-topic/", false, e);
            }
        }
        throw new ProtocolException("Illegal destination name: [" + originalName + "] -- ActiveMQ STOMP destinations " + "must begin with one of: /queue/ /topic/ /temp-queue/ /temp-topic/");
    }

    protected String marshallAdvisory(DataStructure ds) {
        XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
        xstream.setMode(CloseFrame.GOING_AWAY);
        xstream.aliasPackage(Stomp.EMPTY, "org.apache.activemq.command");
        return xstream.toXML(ds);
    }
}
