package org.apache.activemq.command;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotWriteableException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.Message;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.broker.scheduler.CronParser;
import org.apache.activemq.filter.PropertyExpression;
import org.apache.activemq.state.CommandVisitor;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.util.Callback;
import org.apache.activemq.util.JMSExceptionSupport;
import org.apache.activemq.util.TypeConversionSupport;

public class ActiveMQMessage extends Message implements Message, ScheduledMessage {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 23;
    public static final String DLQ_DELIVERY_FAILURE_CAUSE_PROPERTY = "dlqDeliveryFailureCause";
    private static final Map<String, PropertySetter> JMS_PROPERTY_SETERS;
    protected transient Callback acknowledgeCallback;

    interface PropertySetter {
        void set(Message message, Object obj) throws MessageFormatException;
    }

    static class 10 implements PropertySetter {
        10() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            Long rc = (Long) TypeConversionSupport.convert(value, Long.class);
            if (rc == null) {
                throw new MessageFormatException("Property JMSTimestamp cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
            }
            ((ActiveMQMessage) message).setJMSTimestamp(rc.longValue());
        }
    }

    static class 11 implements PropertySetter {
        11() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            String rc = (String) TypeConversionSupport.convert(value, String.class);
            if (rc == null) {
                throw new MessageFormatException("Property JMSType cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
            }
            ((ActiveMQMessage) message).setJMSType(rc);
        }
    }

    static class 1 implements PropertySetter {
        1() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            Integer rc = (Integer) TypeConversionSupport.convert(value, Integer.class);
            if (rc == null) {
                throw new MessageFormatException("Property JMSXDeliveryCount cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
            }
            message.setRedeliveryCounter(rc.intValue() - 1);
        }
    }

    static class 2 implements PropertySetter {
        2() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            String rc = (String) TypeConversionSupport.convert(value, String.class);
            if (rc == null) {
                throw new MessageFormatException("Property JMSXGroupID cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
            }
            message.setGroupID(rc);
        }
    }

    static class 3 implements PropertySetter {
        3() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            Integer rc = (Integer) TypeConversionSupport.convert(value, Integer.class);
            if (rc == null) {
                throw new MessageFormatException("Property JMSXGroupSeq cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
            }
            message.setGroupSequence(rc.intValue());
        }
    }

    static class 4 implements PropertySetter {
        4() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            String rc = (String) TypeConversionSupport.convert(value, String.class);
            if (rc == null) {
                throw new MessageFormatException("Property JMSCorrelationID cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
            }
            ((ActiveMQMessage) message).setJMSCorrelationID(rc);
        }
    }

    static class 5 implements PropertySetter {
        5() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            Integer rc = (Integer) TypeConversionSupport.convert(value, Integer.class);
            if (rc == null) {
                Boolean bool = (Boolean) TypeConversionSupport.convert(value, Boolean.class);
                if (bool == null) {
                    throw new MessageFormatException("Property JMSDeliveryMode cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
                }
                rc = Integer.valueOf(bool.booleanValue() ? 2 : 1);
            }
            ((ActiveMQMessage) message).setJMSDeliveryMode(rc.intValue());
        }
    }

    static class 6 implements PropertySetter {
        6() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            Long rc = (Long) TypeConversionSupport.convert(value, Long.class);
            if (rc == null) {
                throw new MessageFormatException("Property JMSExpiration cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
            }
            ((ActiveMQMessage) message).setJMSExpiration(rc.longValue());
        }
    }

    static class 7 implements PropertySetter {
        7() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            Integer rc = (Integer) TypeConversionSupport.convert(value, Integer.class);
            if (rc == null) {
                throw new MessageFormatException("Property JMSPriority cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
            }
            ((ActiveMQMessage) message).setJMSPriority(rc.intValue());
        }
    }

    static class 8 implements PropertySetter {
        8() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            Boolean rc = (Boolean) TypeConversionSupport.convert(value, Boolean.class);
            if (rc == null) {
                throw new MessageFormatException("Property JMSRedelivered cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
            }
            ((ActiveMQMessage) message).setJMSRedelivered(rc.booleanValue());
        }
    }

    static class 9 implements PropertySetter {
        9() {
        }

        public void set(Message message, Object value) throws MessageFormatException {
            ActiveMQDestination rc = (ActiveMQDestination) TypeConversionSupport.convert(value, ActiveMQDestination.class);
            if (rc == null) {
                throw new MessageFormatException("Property JMSReplyTo cannot be set from a " + value.getClass().getName() + ActiveMQDestination.PATH_SEPERATOR);
            }
            ((ActiveMQMessage) message).setReplyTo(rc);
        }
    }

    static {
        JMS_PROPERTY_SETERS = new HashMap();
        JMS_PROPERTY_SETERS.put("JMSXDeliveryCount", new 1());
        JMS_PROPERTY_SETERS.put("JMSXGroupID", new 2());
        JMS_PROPERTY_SETERS.put("JMSXGroupSeq", new 3());
        JMS_PROPERTY_SETERS.put("JMSCorrelationID", new 4());
        JMS_PROPERTY_SETERS.put("JMSDeliveryMode", new 5());
        JMS_PROPERTY_SETERS.put("JMSExpiration", new 6());
        JMS_PROPERTY_SETERS.put("JMSPriority", new 7());
        JMS_PROPERTY_SETERS.put("JMSRedelivered", new 8());
        JMS_PROPERTY_SETERS.put("JMSReplyTo", new 9());
        JMS_PROPERTY_SETERS.put("JMSTimestamp", new 10());
        JMS_PROPERTY_SETERS.put("JMSType", new 11());
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public Message copy() {
        ActiveMQMessage copy = new ActiveMQMessage();
        copy(copy);
        return copy;
    }

    protected void copy(ActiveMQMessage copy) {
        super.copy(copy);
        copy.acknowledgeCallback = this.acknowledgeCallback;
    }

    public int hashCode() {
        MessageId id = getMessageId();
        if (id != null) {
            return id.hashCode();
        }
        return super.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        MessageId oMsg = ((ActiveMQMessage) o).getMessageId();
        MessageId thisMsg = getMessageId();
        if (thisMsg == null || oMsg == null || !oMsg.equals(thisMsg)) {
            return false;
        }
        return true;
    }

    public void acknowledge() throws JMSException {
        if (this.acknowledgeCallback != null) {
            try {
                this.acknowledgeCallback.execute();
            } catch (JMSException e) {
                throw e;
            } catch (Throwable e2) {
                JMSException create = JMSExceptionSupport.create(e2);
            }
        }
    }

    public void clearBody() throws JMSException {
        setContent(null);
        this.readOnlyBody = false;
    }

    public String getJMSMessageID() {
        MessageId messageId = getMessageId();
        if (messageId == null) {
            return null;
        }
        return messageId.toString();
    }

    public void setJMSMessageID(String value) throws JMSException {
        if (value != null) {
            try {
                setMessageId(new MessageId(value));
                return;
            } catch (NumberFormatException e) {
                new MessageId().setTextView(value);
                setMessageId(this.messageId);
                return;
            }
        }
        setMessageId(null);
    }

    public void setJMSMessageID(ProducerId producerId, long producerSequenceId) throws JMSException {
        Throwable e;
        MessageId id = null;
        try {
            MessageId id2 = new MessageId(producerId, producerSequenceId);
            try {
                setMessageId(id2);
            } catch (Throwable th) {
                e = th;
                id = id2;
                throw JMSExceptionSupport.create("Invalid message id '" + id + "', reason: " + e.getMessage(), e);
            }
        } catch (Throwable th2) {
            e = th2;
            throw JMSExceptionSupport.create("Invalid message id '" + id + "', reason: " + e.getMessage(), e);
        }
    }

    public long getJMSTimestamp() {
        return getTimestamp();
    }

    public void setJMSTimestamp(long timestamp) {
        setTimestamp(timestamp);
    }

    public String getJMSCorrelationID() {
        return getCorrelationId();
    }

    public void setJMSCorrelationID(String correlationId) {
        setCorrelationId(correlationId);
    }

    public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
        return encodeString(getCorrelationId());
    }

    public void setJMSCorrelationIDAsBytes(byte[] correlationId) throws JMSException {
        setCorrelationId(decodeString(correlationId));
    }

    public String getJMSXMimeType() {
        return "jms/message";
    }

    protected static String decodeString(byte[] data) throws JMSException {
        if (data == null) {
            return null;
        }
        try {
            return new String(data, HttpRequest.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new JMSException("Invalid UTF-8 encoding: " + e.getMessage());
        }
    }

    protected static byte[] encodeString(String data) throws JMSException {
        if (data == null) {
            return null;
        }
        try {
            return data.getBytes(HttpRequest.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new JMSException("Invalid UTF-8 encoding: " + e.getMessage());
        }
    }

    public Destination getJMSReplyTo() {
        return getReplyTo();
    }

    public void setJMSReplyTo(Destination destination) throws JMSException {
        setReplyTo(ActiveMQDestination.transform(destination));
    }

    public Destination getJMSDestination() {
        return getDestination();
    }

    public void setJMSDestination(Destination destination) throws JMSException {
        setDestination(ActiveMQDestination.transform(destination));
    }

    public int getJMSDeliveryMode() {
        return isPersistent() ? 2 : 1;
    }

    public void setJMSDeliveryMode(int mode) {
        setPersistent(mode == 2);
    }

    public boolean getJMSRedelivered() {
        return isRedelivered();
    }

    public void setJMSRedelivered(boolean redelivered) {
        setRedelivered(redelivered);
    }

    public String getJMSType() {
        return getType();
    }

    public void setJMSType(String type) {
        setType(type);
    }

    public long getJMSExpiration() {
        return getExpiration();
    }

    public void setJMSExpiration(long expiration) {
        setExpiration(expiration);
    }

    public int getJMSPriority() {
        return getPriority();
    }

    public void setJMSPriority(int priority) {
        setPriority((byte) priority);
    }

    public void clearProperties() {
        super.clearProperties();
        this.readOnlyProperties = false;
    }

    public boolean propertyExists(String name) throws JMSException {
        try {
            return getProperties().containsKey(name) || getObjectProperty(name) != null;
        } catch (Exception e) {
            throw JMSExceptionSupport.create(e);
        }
    }

    public Enumeration getPropertyNames() throws JMSException {
        try {
            Vector<String> result = new Vector(getProperties().keySet());
            if (getRedeliveryCounter() != 0) {
                result.add("JMSXDeliveryCount");
            }
            if (getGroupID() != null) {
                result.add("JMSXGroupID");
            }
            if (getGroupID() != null) {
                result.add("JMSXGroupSeq");
            }
            if (getUserID() != null) {
                result.add(Headers.Message.USERID);
            }
            return result.elements();
        } catch (Exception e) {
            throw JMSExceptionSupport.create(e);
        }
    }

    public Enumeration getAllPropertyNames() throws JMSException {
        try {
            Vector<String> result = new Vector(getProperties().keySet());
            result.addAll(JMS_PROPERTY_SETERS.keySet());
            return result.elements();
        } catch (Exception e) {
            throw JMSExceptionSupport.create(e);
        }
    }

    public void setObjectProperty(String name, Object value) throws JMSException {
        setObjectProperty(name, value, true);
    }

    public void setObjectProperty(String name, Object value, boolean checkReadOnly) throws JMSException {
        if (checkReadOnly) {
            checkReadOnlyProperties();
        }
        if (name == null || name.equals(Stomp.EMPTY)) {
            throw new IllegalArgumentException("Property name cannot be empty or null");
        }
        checkValidObject(value);
        value = convertScheduled(name, value);
        PropertySetter setter = (PropertySetter) JMS_PROPERTY_SETERS.get(name);
        if (setter == null || value == null) {
            try {
                setProperty(name, value);
                return;
            } catch (Exception e) {
                throw JMSExceptionSupport.create(e);
            }
        }
        setter.set(this, value);
    }

    public void setProperties(Map<String, ?> properties) throws JMSException {
        for (Entry<String, ?> entry : properties.entrySet()) {
            setObjectProperty((String) entry.getKey(), entry.getValue());
        }
    }

    protected void checkValidObject(Object value) throws MessageFormatException {
        boolean valid;
        if ((value instanceof Boolean) || (value instanceof Byte) || (value instanceof Short) || (value instanceof Integer) || (value instanceof Long)) {
            valid = true;
        } else {
            valid = false;
        }
        if (valid || (value instanceof Float) || (value instanceof Double) || (value instanceof Character) || (value instanceof String) || value == null) {
            valid = true;
        } else {
            valid = false;
        }
        if (!valid) {
            ActiveMQConnection conn = getConnection();
            if (conn != null && !conn.isNestedMapAndListEnabled()) {
                throw new MessageFormatException("Only objectified primitive objects and String types are allowed but was: " + value + " type: " + value.getClass());
            } else if (!(value instanceof Map) && !(value instanceof List)) {
                throw new MessageFormatException("Only objectified primitive objects, String, Map and List types are allowed but was: " + value + " type: " + value.getClass());
            }
        }
    }

    protected void checkValidScheduled(String name, Object value) throws MessageFormatException {
        if ((ScheduledMessage.AMQ_SCHEDULED_DELAY.equals(name) || ScheduledMessage.AMQ_SCHEDULED_PERIOD.equals(name) || ScheduledMessage.AMQ_SCHEDULED_REPEAT.equals(name)) && !(value instanceof Long) && !(value instanceof Integer)) {
            throw new MessageFormatException(name + " should be long or int value");
        } else if (ScheduledMessage.AMQ_SCHEDULED_CRON.equals(name)) {
            CronParser.validate(value.toString());
        }
    }

    protected Object convertScheduled(String name, Object value) throws MessageFormatException {
        Object result = value;
        if (ScheduledMessage.AMQ_SCHEDULED_DELAY.equals(name)) {
            return TypeConversionSupport.convert(value, Long.class);
        }
        if (ScheduledMessage.AMQ_SCHEDULED_PERIOD.equals(name)) {
            return TypeConversionSupport.convert(value, Long.class);
        }
        if (ScheduledMessage.AMQ_SCHEDULED_REPEAT.equals(name)) {
            return TypeConversionSupport.convert(value, Integer.class);
        }
        return result;
    }

    public Object getObjectProperty(String name) throws JMSException {
        if (name != null) {
            return new PropertyExpression(name).evaluate((Message) this);
        }
        throw new NullPointerException("Property name cannot be null");
    }

    public boolean getBooleanProperty(String name) throws JMSException {
        Object value = getObjectProperty(name);
        if (value == null) {
            return false;
        }
        Boolean rc = (Boolean) TypeConversionSupport.convert(value, Boolean.class);
        if (rc != null) {
            return rc.booleanValue();
        }
        throw new MessageFormatException("Property " + name + " was a " + value.getClass().getName() + " and cannot be read as a boolean");
    }

    public byte getByteProperty(String name) throws JMSException {
        Object value = getObjectProperty(name);
        if (value == null) {
            throw new NumberFormatException("property " + name + " was null");
        }
        Byte rc = (Byte) TypeConversionSupport.convert(value, Byte.class);
        if (rc != null) {
            return rc.byteValue();
        }
        throw new MessageFormatException("Property " + name + " was a " + value.getClass().getName() + " and cannot be read as a byte");
    }

    public short getShortProperty(String name) throws JMSException {
        Object value = getObjectProperty(name);
        if (value == null) {
            throw new NumberFormatException("property " + name + " was null");
        }
        Short rc = (Short) TypeConversionSupport.convert(value, Short.class);
        if (rc != null) {
            return rc.shortValue();
        }
        throw new MessageFormatException("Property " + name + " was a " + value.getClass().getName() + " and cannot be read as a short");
    }

    public int getIntProperty(String name) throws JMSException {
        Object value = getObjectProperty(name);
        if (value == null) {
            throw new NumberFormatException("property " + name + " was null");
        }
        Integer rc = (Integer) TypeConversionSupport.convert(value, Integer.class);
        if (rc != null) {
            return rc.intValue();
        }
        throw new MessageFormatException("Property " + name + " was a " + value.getClass().getName() + " and cannot be read as an integer");
    }

    public long getLongProperty(String name) throws JMSException {
        Object value = getObjectProperty(name);
        if (value == null) {
            throw new NumberFormatException("property " + name + " was null");
        }
        Long rc = (Long) TypeConversionSupport.convert(value, Long.class);
        if (rc != null) {
            return rc.longValue();
        }
        throw new MessageFormatException("Property " + name + " was a " + value.getClass().getName() + " and cannot be read as a long");
    }

    public float getFloatProperty(String name) throws JMSException {
        Object value = getObjectProperty(name);
        if (value == null) {
            throw new NullPointerException("property " + name + " was null");
        }
        Float rc = (Float) TypeConversionSupport.convert(value, Float.class);
        if (rc != null) {
            return rc.floatValue();
        }
        throw new MessageFormatException("Property " + name + " was a " + value.getClass().getName() + " and cannot be read as a float");
    }

    public double getDoubleProperty(String name) throws JMSException {
        Object value = getObjectProperty(name);
        if (value == null) {
            throw new NullPointerException("property " + name + " was null");
        }
        Double rc = (Double) TypeConversionSupport.convert(value, Double.class);
        if (rc != null) {
            return rc.doubleValue();
        }
        throw new MessageFormatException("Property " + name + " was a " + value.getClass().getName() + " and cannot be read as a double");
    }

    public String getStringProperty(String name) throws JMSException {
        Object value;
        if (name.equals(Headers.Message.USERID)) {
            value = getUserID();
            if (value == null) {
                value = getObjectProperty(name);
            }
        } else {
            value = getObjectProperty(name);
        }
        if (value == null) {
            return null;
        }
        String rc = (String) TypeConversionSupport.convert(value, String.class);
        if (rc != null) {
            return rc;
        }
        throw new MessageFormatException("Property " + name + " was a " + value.getClass().getName() + " and cannot be read as a String");
    }

    public void setBooleanProperty(String name, boolean value) throws JMSException {
        setBooleanProperty(name, value, true);
    }

    public void setBooleanProperty(String name, boolean value, boolean checkReadOnly) throws JMSException {
        setObjectProperty(name, Boolean.valueOf(value), checkReadOnly);
    }

    public void setByteProperty(String name, byte value) throws JMSException {
        setObjectProperty(name, Byte.valueOf(value));
    }

    public void setShortProperty(String name, short value) throws JMSException {
        setObjectProperty(name, Short.valueOf(value));
    }

    public void setIntProperty(String name, int value) throws JMSException {
        setObjectProperty(name, Integer.valueOf(value));
    }

    public void setLongProperty(String name, long value) throws JMSException {
        setObjectProperty(name, Long.valueOf(value));
    }

    public void setFloatProperty(String name, float value) throws JMSException {
        setObjectProperty(name, new Float(value));
    }

    public void setDoubleProperty(String name, double value) throws JMSException {
        setObjectProperty(name, new Double(value));
    }

    public void setStringProperty(String name, String value) throws JMSException {
        setObjectProperty(name, value);
    }

    private void checkReadOnlyProperties() throws MessageNotWriteableException {
        if (this.readOnlyProperties) {
            throw new MessageNotWriteableException("Message properties are read-only");
        }
    }

    protected void checkReadOnlyBody() throws MessageNotWriteableException {
        if (this.readOnlyBody) {
            throw new MessageNotWriteableException("Message body is read-only");
        }
    }

    public Callback getAcknowledgeCallback() {
        return this.acknowledgeCallback;
    }

    public void setAcknowledgeCallback(Callback acknowledgeCallback) {
        this.acknowledgeCallback = acknowledgeCallback;
    }

    public void onSend() throws JMSException {
        setReadOnlyBody(true);
        setReadOnlyProperties(true);
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processMessage(this);
    }

    public void storeContent() {
    }
}
