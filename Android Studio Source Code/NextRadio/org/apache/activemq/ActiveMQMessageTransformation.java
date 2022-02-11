package org.apache.activemq;

import java.util.Enumeration;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.ActiveMQTopic;

public final class ActiveMQMessageTransformation {
    private ActiveMQMessageTransformation() {
    }

    public static ActiveMQDestination transformDestination(Destination destination) throws JMSException {
        ActiveMQDestination activeMQDestination = null;
        if (destination != null) {
            if (destination instanceof ActiveMQDestination) {
                return (ActiveMQDestination) destination;
            }
            if (destination instanceof TemporaryQueue) {
                activeMQDestination = new ActiveMQTempQueue(((Queue) destination).getQueueName());
            } else if (destination instanceof TemporaryTopic) {
                activeMQDestination = new ActiveMQTempTopic(((Topic) destination).getTopicName());
            } else if (destination instanceof Queue) {
                activeMQDestination = new ActiveMQQueue(((Queue) destination).getQueueName());
            } else if (destination instanceof Topic) {
                activeMQDestination = new ActiveMQTopic(((Topic) destination).getTopicName());
            }
        }
        return activeMQDestination;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.apache.activemq.command.ActiveMQMessage transformMessage(javax.jms.Message r13, org.apache.activemq.ActiveMQConnection r14) throws javax.jms.JMSException {
        /*
        r11 = r13 instanceof org.apache.activemq.command.ActiveMQMessage;
        if (r11 == 0) goto L_0x0007;
    L_0x0004:
        r13 = (org.apache.activemq.command.ActiveMQMessage) r13;
    L_0x0006:
        return r13;
    L_0x0007:
        r0 = 0;
        r11 = r13 instanceof javax.jms.BytesMessage;
        if (r11 == 0) goto L_0x0029;
    L_0x000c:
        r2 = r13;
        r2 = (javax.jms.BytesMessage) r2;
        r2.reset();
        r5 = new org.apache.activemq.command.ActiveMQBytesMessage;
        r5.<init>();
        r5.setConnection(r14);
    L_0x001a:
        r11 = r2.readByte();	 Catch:{ MessageEOFException -> 0x0022, JMSException -> 0x00db }
        r5.writeByte(r11);	 Catch:{ MessageEOFException -> 0x0022, JMSException -> 0x00db }
        goto L_0x001a;
    L_0x0022:
        r11 = move-exception;
    L_0x0023:
        r0 = r5;
    L_0x0024:
        copyProperties(r13, r0);
        r13 = r0;
        goto L_0x0006;
    L_0x0029:
        r11 = r13 instanceof javax.jms.MapMessage;
        if (r11 == 0) goto L_0x0054;
    L_0x002d:
        r4 = r13;
        r4 = (javax.jms.MapMessage) r4;
        r5 = new org.apache.activemq.command.ActiveMQMapMessage;
        r5.<init>();
        r5.setConnection(r14);
        r3 = r4.getMapNames();
    L_0x003c:
        r11 = r3.hasMoreElements();
        if (r11 == 0) goto L_0x0052;
    L_0x0042:
        r11 = r3.nextElement();
        r6 = r11.toString();
        r11 = r4.getObject(r6);
        r5.setObject(r6, r11);
        goto L_0x003c;
    L_0x0052:
        r0 = r5;
        goto L_0x0024;
    L_0x0054:
        r11 = r13 instanceof javax.jms.ObjectMessage;
        if (r11 == 0) goto L_0x006f;
    L_0x0058:
        r8 = r13;
        r8 = (javax.jms.ObjectMessage) r8;
        r5 = new org.apache.activemq.command.ActiveMQObjectMessage;
        r5.<init>();
        r5.setConnection(r14);
        r11 = r8.getObject();
        r5.setObject(r11);
        r5.storeContent();
        r0 = r5;
        goto L_0x0024;
    L_0x006f:
        r11 = r13 instanceof javax.jms.StreamMessage;
        if (r11 == 0) goto L_0x008f;
    L_0x0073:
        r9 = r13;
        r9 = (javax.jms.StreamMessage) r9;
        r9.reset();
        r5 = new org.apache.activemq.command.ActiveMQStreamMessage;
        r5.<init>();
        r5.setConnection(r14);
        r7 = 0;
    L_0x0082:
        r7 = r9.readObject();	 Catch:{ MessageEOFException -> 0x008c, JMSException -> 0x00d9 }
        if (r7 == 0) goto L_0x008d;
    L_0x0088:
        r5.writeObject(r7);	 Catch:{ MessageEOFException -> 0x008c, JMSException -> 0x00d9 }
        goto L_0x0082;
    L_0x008c:
        r11 = move-exception;
    L_0x008d:
        r0 = r5;
        goto L_0x0024;
    L_0x008f:
        r11 = r13 instanceof javax.jms.TextMessage;
        if (r11 == 0) goto L_0x00a8;
    L_0x0093:
        r10 = r13;
        r10 = (javax.jms.TextMessage) r10;
        r5 = new org.apache.activemq.command.ActiveMQTextMessage;
        r5.<init>();
        r5.setConnection(r14);
        r11 = r10.getText();
        r5.setText(r11);
        r0 = r5;
        goto L_0x0024;
    L_0x00a8:
        r11 = r13 instanceof org.apache.activemq.BlobMessage;
        if (r11 == 0) goto L_0x00cd;
    L_0x00ac:
        r1 = r13;
        r1 = (org.apache.activemq.BlobMessage) r1;
        r5 = new org.apache.activemq.command.ActiveMQBlobMessage;
        r5.<init>();
        r5.setConnection(r14);
        r11 = new org.apache.activemq.blob.BlobDownloader;
        r12 = r14.getBlobTransferPolicy();
        r11.<init>(r12);
        r5.setBlobDownloader(r11);
        r11 = r1.getURL();	 Catch:{ MalformedURLException -> 0x00d7 }
        r5.setURL(r11);	 Catch:{ MalformedURLException -> 0x00d7 }
    L_0x00ca:
        r0 = r5;
        goto L_0x0024;
    L_0x00cd:
        r0 = new org.apache.activemq.command.ActiveMQMessage;
        r0.<init>();
        r0.setConnection(r14);
        goto L_0x0024;
    L_0x00d7:
        r11 = move-exception;
        goto L_0x00ca;
    L_0x00d9:
        r11 = move-exception;
        goto L_0x008d;
    L_0x00db:
        r11 = move-exception;
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.ActiveMQMessageTransformation.transformMessage(javax.jms.Message, org.apache.activemq.ActiveMQConnection):org.apache.activemq.command.ActiveMQMessage");
    }

    public static void copyProperties(Message fromMessage, Message toMessage) throws JMSException {
        toMessage.setJMSMessageID(fromMessage.getJMSMessageID());
        toMessage.setJMSCorrelationID(fromMessage.getJMSCorrelationID());
        toMessage.setJMSReplyTo(transformDestination(fromMessage.getJMSReplyTo()));
        toMessage.setJMSDestination(transformDestination(fromMessage.getJMSDestination()));
        toMessage.setJMSDeliveryMode(fromMessage.getJMSDeliveryMode());
        toMessage.setJMSRedelivered(fromMessage.getJMSRedelivered());
        toMessage.setJMSType(fromMessage.getJMSType());
        toMessage.setJMSExpiration(fromMessage.getJMSExpiration());
        toMessage.setJMSPriority(fromMessage.getJMSPriority());
        toMessage.setJMSTimestamp(fromMessage.getJMSTimestamp());
        Enumeration propertyNames = fromMessage.getPropertyNames();
        while (propertyNames.hasMoreElements()) {
            String name = propertyNames.nextElement().toString();
            toMessage.setObjectProperty(name, fromMessage.getObjectProperty(name));
        }
    }
}
