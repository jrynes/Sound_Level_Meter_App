package org.apache.activemq.transport.stomp;

import com.mixpanel.android.java_websocket.framing.CloseFrame;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.jms.JMSException;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.broker.BrokerContext;
import org.apache.activemq.broker.BrokerContextAware;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.CommandTypes;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.transport.stomp.FrameTranslator.Helper;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.transport.stomp.Stomp.Responses;
import org.apache.activemq.transport.stomp.Stomp.Transformations;
import org.codehaus.jettison.mapped.Configuration;
import org.fusesource.hawtbuf.UTF8Buffer;

public class JmsFrameTranslator extends LegacyFrameTranslator implements BrokerContextAware {
    BrokerContext brokerContext;
    XStream xStream;

    class 1 extends AbstractSingleValueConverter {
        1() {
        }

        public Object fromString(String str) {
            return str;
        }

        public boolean canConvert(Class type) {
            return type.equals(UTF8Buffer.class);
        }
    }

    static /* synthetic */ class 2 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$activemq$transport$stomp$Stomp$Transformations;

        static {
            $SwitchMap$org$apache$activemq$transport$stomp$Stomp$Transformations = new int[Transformations.values().length];
            try {
                $SwitchMap$org$apache$activemq$transport$stomp$Stomp$Transformations[Transformations.JMS_OBJECT_XML.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$activemq$transport$stomp$Stomp$Transformations[Transformations.JMS_OBJECT_JSON.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$activemq$transport$stomp$Stomp$Transformations[Transformations.JMS_MAP_XML.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$activemq$transport$stomp$Stomp$Transformations[Transformations.JMS_MAP_JSON.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public JmsFrameTranslator() {
        this.xStream = null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.activemq.command.ActiveMQMessage convertFrame(org.apache.activemq.transport.stomp.ProtocolConverter r10, org.apache.activemq.transport.stomp.StompFrame r11) throws javax.jms.JMSException, org.apache.activemq.transport.stomp.ProtocolException {
        /*
        r9 = this;
        r1 = r11.getHeaders();
        r6 = "transformation";
        r5 = r1.get(r6);
        r5 = (java.lang.String) r5;
        r6 = "content-length";
        r6 = r1.containsKey(r6);
        if (r6 != 0) goto L_0x0020;
    L_0x0014:
        r6 = org.apache.activemq.transport.stomp.Stomp.Transformations.JMS_BYTE;
        r6 = r6.toString();
        r6 = r5.equals(r6);
        if (r6 == 0) goto L_0x0028;
    L_0x0020:
        r3 = super.convertFrame(r10, r11);
    L_0x0024:
        org.apache.activemq.transport.stomp.FrameTranslator.Helper.copyStandardHeadersFromFrameToMessage(r10, r11, r3, r9);
        return r3;
    L_0x0028:
        r4 = new java.lang.String;	 Catch:{ Throwable -> 0x005b }
        r6 = r11.getContent();	 Catch:{ Throwable -> 0x005b }
        r7 = "UTF-8";
        r4.<init>(r6, r7);	 Catch:{ Throwable -> 0x005b }
        r6 = org.apache.activemq.transport.stomp.JmsFrameTranslator.2.$SwitchMap$org$apache$activemq$transport$stomp$Stomp$Transformations;	 Catch:{ Throwable -> 0x005b }
        r7 = org.apache.activemq.transport.stomp.Stomp.Transformations.getValue(r5);	 Catch:{ Throwable -> 0x005b }
        r7 = r7.ordinal();	 Catch:{ Throwable -> 0x005b }
        r6 = r6[r7];	 Catch:{ Throwable -> 0x005b }
        switch(r6) {
            case 1: goto L_0x006e;
            case 2: goto L_0x0081;
            case 3: goto L_0x0094;
            case 4: goto L_0x00a8;
            default: goto L_0x0042;
        };	 Catch:{ Throwable -> 0x005b }
    L_0x0042:
        r6 = new java.lang.Exception;	 Catch:{ Throwable -> 0x005b }
        r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x005b }
        r7.<init>();	 Catch:{ Throwable -> 0x005b }
        r8 = "Unkown transformation: ";
        r7 = r7.append(r8);	 Catch:{ Throwable -> 0x005b }
        r7 = r7.append(r5);	 Catch:{ Throwable -> 0x005b }
        r7 = r7.toString();	 Catch:{ Throwable -> 0x005b }
        r6.<init>(r7);	 Catch:{ Throwable -> 0x005b }
        throw r6;	 Catch:{ Throwable -> 0x005b }
    L_0x005b:
        r0 = move-exception;
        r6 = r11.getHeaders();
        r7 = "transformation-error";
        r8 = r0.getMessage();
        r6.put(r7, r8);
        r3 = super.convertFrame(r10, r11);
        goto L_0x0024;
    L_0x006e:
        r2 = new com.thoughtworks.xstream.io.xml.XppReader;	 Catch:{ Throwable -> 0x005b }
        r6 = new java.io.StringReader;	 Catch:{ Throwable -> 0x005b }
        r6.<init>(r4);	 Catch:{ Throwable -> 0x005b }
        r7 = com.thoughtworks.xstream.io.xml.xppdom.XppFactory.createDefaultParser();	 Catch:{ Throwable -> 0x005b }
        r2.<init>(r6, r7);	 Catch:{ Throwable -> 0x005b }
        r3 = r9.createObjectMessage(r2);	 Catch:{ Throwable -> 0x005b }
        goto L_0x0024;
    L_0x0081:
        r6 = new com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;	 Catch:{ Throwable -> 0x005b }
        r6.<init>();	 Catch:{ Throwable -> 0x005b }
        r7 = new java.io.StringReader;	 Catch:{ Throwable -> 0x005b }
        r7.<init>(r4);	 Catch:{ Throwable -> 0x005b }
        r2 = r6.createReader(r7);	 Catch:{ Throwable -> 0x005b }
        r3 = r9.createObjectMessage(r2);	 Catch:{ Throwable -> 0x005b }
        goto L_0x0024;
    L_0x0094:
        r2 = new com.thoughtworks.xstream.io.xml.XppReader;	 Catch:{ Throwable -> 0x005b }
        r6 = new java.io.StringReader;	 Catch:{ Throwable -> 0x005b }
        r6.<init>(r4);	 Catch:{ Throwable -> 0x005b }
        r7 = com.thoughtworks.xstream.io.xml.xppdom.XppFactory.createDefaultParser();	 Catch:{ Throwable -> 0x005b }
        r2.<init>(r6, r7);	 Catch:{ Throwable -> 0x005b }
        r3 = r9.createMapMessage(r2);	 Catch:{ Throwable -> 0x005b }
        goto L_0x0024;
    L_0x00a8:
        r6 = new com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;	 Catch:{ Throwable -> 0x005b }
        r6.<init>();	 Catch:{ Throwable -> 0x005b }
        r7 = new java.io.StringReader;	 Catch:{ Throwable -> 0x005b }
        r7.<init>(r4);	 Catch:{ Throwable -> 0x005b }
        r2 = r6.createReader(r7);	 Catch:{ Throwable -> 0x005b }
        r3 = r9.createMapMessage(r2);	 Catch:{ Throwable -> 0x005b }
        goto L_0x0024;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.stomp.JmsFrameTranslator.convertFrame(org.apache.activemq.transport.stomp.ProtocolConverter, org.apache.activemq.transport.stomp.StompFrame):org.apache.activemq.command.ActiveMQMessage");
    }

    public StompFrame convertMessage(ProtocolConverter converter, ActiveMQMessage message) throws IOException, JMSException {
        StompFrame command;
        Map<String, String> headers;
        if (message.getDataStructureType() == 26) {
            command = new StompFrame();
            command.setAction(Responses.MESSAGE);
            headers = new HashMap(25);
            command.setHeaders(headers);
            Helper.copyStandardHeadersFromMessageToFrame(converter, message, command, this);
            if (((String) headers.get(Headers.TRANSFORMATION)).equals(Transformations.JMS_XML.toString())) {
                headers.put(Headers.TRANSFORMATION, Transformations.JMS_OBJECT_XML.toString());
            } else if (((String) headers.get(Headers.TRANSFORMATION)).equals(Transformations.JMS_JSON.toString())) {
                headers.put(Headers.TRANSFORMATION, Transformations.JMS_OBJECT_JSON.toString());
            }
            command.setContent(marshall(((ActiveMQObjectMessage) message.copy()).getObject(), (String) headers.get(Headers.TRANSFORMATION)).getBytes(HttpRequest.CHARSET_UTF8));
            return command;
        } else if (message.getDataStructureType() == CommandTypes.ACTIVEMQ_MAP_MESSAGE) {
            command = new StompFrame();
            command.setAction(Responses.MESSAGE);
            headers = new HashMap(25);
            command.setHeaders(headers);
            Helper.copyStandardHeadersFromMessageToFrame(converter, message, command, this);
            if (((String) headers.get(Headers.TRANSFORMATION)).equals(Transformations.JMS_XML.toString())) {
                headers.put(Headers.TRANSFORMATION, Transformations.JMS_MAP_XML.toString());
            } else if (((String) headers.get(Headers.TRANSFORMATION)).equals(Transformations.JMS_JSON.toString())) {
                headers.put(Headers.TRANSFORMATION, Transformations.JMS_MAP_JSON.toString());
            }
            command.setContent(marshall((Serializable) ((ActiveMQMapMessage) message.copy()).getContentMap(), (String) headers.get(Headers.TRANSFORMATION)).getBytes(HttpRequest.CHARSET_UTF8));
            return command;
        } else if (message.getDataStructureType() != 23 || !AdvisorySupport.ADIVSORY_MESSAGE_TYPE.equals(message.getType())) {
            return super.convertMessage(converter, message);
        } else {
            command = new StompFrame();
            command.setAction(Responses.MESSAGE);
            headers = new HashMap(25);
            command.setHeaders(headers);
            Helper.copyStandardHeadersFromMessageToFrame(converter, message, command, this);
            if (((String) headers.get(Headers.TRANSFORMATION)).equals(Transformations.JMS_XML.toString())) {
                headers.put(Headers.TRANSFORMATION, Transformations.JMS_ADVISORY_XML.toString());
            } else if (((String) headers.get(Headers.TRANSFORMATION)).equals(Transformations.JMS_JSON.toString())) {
                headers.put(Headers.TRANSFORMATION, Transformations.JMS_ADVISORY_JSON.toString());
            }
            command.setContent(marshallAdvisory(message.getDataStructure(), (String) headers.get(Headers.TRANSFORMATION)).getBytes(HttpRequest.CHARSET_UTF8));
            return command;
        }
    }

    protected String marshall(Serializable object, String transformation) throws JMSException {
        HierarchicalStreamWriter out;
        StringWriter buffer = new StringWriter();
        if (transformation.toLowerCase(Locale.ENGLISH).endsWith("json")) {
            out = new JettisonMappedXmlDriver(new Configuration(), false).createWriter(buffer);
        } else {
            out = new PrettyPrintWriter(buffer);
        }
        getXStream().marshal(object, out);
        return buffer.toString();
    }

    protected ActiveMQObjectMessage createObjectMessage(HierarchicalStreamReader in) throws JMSException {
        ActiveMQObjectMessage objMsg = new ActiveMQObjectMessage();
        objMsg.setObject((Serializable) getXStream().unmarshal(in));
        return objMsg;
    }

    protected ActiveMQMapMessage createMapMessage(HierarchicalStreamReader in) throws JMSException {
        ActiveMQMapMessage mapMsg = new ActiveMQMapMessage();
        Map<String, Object> map = (Map) getXStream().unmarshal(in);
        for (String key : map.keySet()) {
            mapMsg.setObject(key, map.get(key));
        }
        return mapMsg;
    }

    protected String marshallAdvisory(DataStructure ds, String transformation) {
        HierarchicalStreamWriter out;
        StringWriter buffer = new StringWriter();
        if (transformation.toLowerCase(Locale.ENGLISH).endsWith("json")) {
            out = new JettisonMappedXmlDriver().createWriter(buffer);
        } else {
            out = new PrettyPrintWriter(buffer);
        }
        XStream xstream = getXStream();
        xstream.setMode(CloseFrame.GOING_AWAY);
        xstream.aliasPackage(Stomp.EMPTY, "org.apache.activemq.command");
        xstream.marshal(ds, out);
        return buffer.toString();
    }

    public XStream getXStream() {
        if (this.xStream == null) {
            this.xStream = createXStream();
        }
        return this.xStream;
    }

    public void setXStream(XStream xStream) {
        this.xStream = xStream;
    }

    protected XStream createXStream() {
        XStream xstream = null;
        if (this.brokerContext != null) {
            for (XStream bean : this.brokerContext.getBeansOfType(XStream.class).values()) {
                if (bean != null) {
                    xstream = bean;
                    break;
                }
            }
        }
        if (xstream == null) {
            xstream = new XStream();
        }
        xstream.registerConverter(new 1());
        xstream.alias("string", UTF8Buffer.class);
        return xstream;
    }

    public void setBrokerContext(BrokerContext brokerContext) {
        this.brokerContext = brokerContext;
    }
}
