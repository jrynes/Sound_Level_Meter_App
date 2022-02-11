package org.apache.activemq.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotWriteableException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.JMSExceptionSupport;
import org.apache.activemq.util.MarshallingSupport;
import org.apache.activemq.wireformat.WireFormat;
import org.fusesource.hawtbuf.UTF8Buffer;

public class ActiveMQMapMessage extends ActiveMQMessage implements MapMessage {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 25;
    protected transient Map<String, Object> map;

    public ActiveMQMapMessage() {
        this.map = new HashMap();
    }

    private Object readResolve() throws ObjectStreamException {
        if (this.map == null) {
            this.map = new HashMap();
        }
        return this;
    }

    public Message copy() {
        ActiveMQMapMessage copy = new ActiveMQMapMessage();
        copy(copy);
        return copy;
    }

    private void copy(ActiveMQMapMessage copy) {
        storeContent();
        super.copy(copy);
    }

    public void beforeMarshall(WireFormat wireFormat) throws IOException {
        super.beforeMarshall(wireFormat);
        storeContent();
    }

    public void clearMarshalledState() throws JMSException {
        super.clearMarshalledState();
        this.map.clear();
    }

    public void storeContent() {
        try {
            if (getContent() == null && !this.map.isEmpty()) {
                OutputStream bytesOut = new ByteArrayOutputStream();
                OutputStream os = bytesOut;
                ActiveMQConnection connection = getConnection();
                if (connection != null && connection.isUseCompression()) {
                    this.compressed = true;
                    os = new DeflaterOutputStream(os);
                }
                DataOutputStream dataOut = new DataOutputStream(os);
                MarshallingSupport.marshalPrimitiveMap(this.map, dataOut);
                dataOut.close();
                setContent(bytesOut.toByteSequence());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadContent() throws JMSException {
        try {
            if (getContent() != null && this.map.isEmpty()) {
                InputStream is = new ByteArrayInputStream(getContent());
                if (isCompressed()) {
                    is = new InflaterInputStream(is);
                }
                DataInputStream dataIn = new DataInputStream(is);
                this.map = MarshallingSupport.unmarshalPrimitiveMap(dataIn);
                dataIn.close();
            }
        } catch (Exception e) {
            throw JMSExceptionSupport.create(e);
        }
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getJMSXMimeType() {
        return "jms/map-message";
    }

    public void clearBody() throws JMSException {
        super.clearBody();
        this.map.clear();
    }

    public boolean getBoolean(String name) throws JMSException {
        initializeReading();
        Object value = this.map.get(name);
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        if (value instanceof UTF8Buffer) {
            return Boolean.valueOf(value.toString()).booleanValue();
        }
        if (value instanceof String) {
            return Boolean.valueOf(value.toString()).booleanValue();
        }
        throw new MessageFormatException(" cannot read a boolean from " + value.getClass().getName());
    }

    public byte getByte(String name) throws JMSException {
        initializeReading();
        Object value = this.map.get(name);
        if (value == null) {
            return (byte) 0;
        }
        if (value instanceof Byte) {
            return ((Byte) value).byteValue();
        }
        if (value instanceof UTF8Buffer) {
            return Byte.valueOf(value.toString()).byteValue();
        }
        if (value instanceof String) {
            return Byte.valueOf(value.toString()).byteValue();
        }
        throw new MessageFormatException(" cannot read a byte from " + value.getClass().getName());
    }

    public short getShort(String name) throws JMSException {
        initializeReading();
        Object value = this.map.get(name);
        if (value == null) {
            return (short) 0;
        }
        if (value instanceof Short) {
            return ((Short) value).shortValue();
        }
        if (value instanceof Byte) {
            return ((Byte) value).shortValue();
        }
        if (value instanceof UTF8Buffer) {
            return Short.valueOf(value.toString()).shortValue();
        }
        if (value instanceof String) {
            return Short.valueOf(value.toString()).shortValue();
        }
        throw new MessageFormatException(" cannot read a short from " + value.getClass().getName());
    }

    public char getChar(String name) throws JMSException {
        initializeReading();
        Object value = this.map.get(name);
        if (value == null) {
            throw new NullPointerException();
        } else if (value instanceof Character) {
            return ((Character) value).charValue();
        } else {
            throw new MessageFormatException(" cannot read a short from " + value.getClass().getName());
        }
    }

    public int getInt(String name) throws JMSException {
        initializeReading();
        Object value = this.map.get(name);
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        if (value instanceof Short) {
            return ((Short) value).intValue();
        }
        if (value instanceof Byte) {
            return ((Byte) value).intValue();
        }
        if (value instanceof UTF8Buffer) {
            return Integer.valueOf(value.toString()).intValue();
        }
        if (value instanceof String) {
            return Integer.valueOf(value.toString()).intValue();
        }
        throw new MessageFormatException(" cannot read an int from " + value.getClass().getName());
    }

    public long getLong(String name) throws JMSException {
        initializeReading();
        Object value = this.map.get(name);
        if (value == null) {
            return 0;
        }
        if (value instanceof Long) {
            return ((Long) value).longValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        if (value instanceof Short) {
            return ((Short) value).longValue();
        }
        if (value instanceof Byte) {
            return ((Byte) value).longValue();
        }
        if (value instanceof UTF8Buffer) {
            return Long.valueOf(value.toString()).longValue();
        }
        if (value instanceof String) {
            return Long.valueOf(value.toString()).longValue();
        }
        throw new MessageFormatException(" cannot read a long from " + value.getClass().getName());
    }

    public float getFloat(String name) throws JMSException {
        initializeReading();
        Object value = this.map.get(name);
        if (value == null) {
            return 0.0f;
        }
        if (value instanceof Float) {
            return ((Float) value).floatValue();
        }
        if (value instanceof UTF8Buffer) {
            return Float.valueOf(value.toString()).floatValue();
        }
        if (value instanceof String) {
            return Float.valueOf(value.toString()).floatValue();
        }
        throw new MessageFormatException(" cannot read a float from " + value.getClass().getName());
    }

    public double getDouble(String name) throws JMSException {
        initializeReading();
        Object value = this.map.get(name);
        if (value == null) {
            return 0.0d;
        }
        if (value instanceof Double) {
            return ((Double) value).doubleValue();
        }
        if (value instanceof Float) {
            return (double) ((Float) value).floatValue();
        }
        if (value instanceof UTF8Buffer) {
            return (double) Float.valueOf(value.toString()).floatValue();
        }
        if (value instanceof String) {
            return (double) Float.valueOf(value.toString()).floatValue();
        }
        throw new MessageFormatException(" cannot read a double from " + value.getClass().getName());
    }

    public String getString(String name) throws JMSException {
        initializeReading();
        Object value = this.map.get(name);
        if (value == null) {
            return null;
        }
        if (!(value instanceof byte[])) {
            return value.toString();
        }
        throw new MessageFormatException("Use getBytes to read a byte array");
    }

    public byte[] getBytes(String name) throws JMSException {
        initializeReading();
        Object value = this.map.get(name);
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        throw new MessageFormatException(" cannot read a byte[] from " + value.getClass().getName());
    }

    public Object getObject(String name) throws JMSException {
        initializeReading();
        Object result = this.map.get(name);
        if (result instanceof UTF8Buffer) {
            return result.toString();
        }
        return result;
    }

    public Enumeration<String> getMapNames() throws JMSException {
        initializeReading();
        return Collections.enumeration(this.map.keySet());
    }

    protected void put(String name, Object value) throws JMSException {
        if (name == null) {
            throw new IllegalArgumentException("The name of the property cannot be null.");
        } else if (name.length() == 0) {
            throw new IllegalArgumentException("The name of the property cannot be an emprty string.");
        } else {
            this.map.put(name, value);
        }
    }

    public void setBoolean(String name, boolean value) throws JMSException {
        initializeWriting();
        put(name, value ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setByte(String name, byte value) throws JMSException {
        initializeWriting();
        put(name, Byte.valueOf(value));
    }

    public void setShort(String name, short value) throws JMSException {
        initializeWriting();
        put(name, Short.valueOf(value));
    }

    public void setChar(String name, char value) throws JMSException {
        initializeWriting();
        put(name, Character.valueOf(value));
    }

    public void setInt(String name, int value) throws JMSException {
        initializeWriting();
        put(name, Integer.valueOf(value));
    }

    public void setLong(String name, long value) throws JMSException {
        initializeWriting();
        put(name, Long.valueOf(value));
    }

    public void setFloat(String name, float value) throws JMSException {
        initializeWriting();
        put(name, new Float(value));
    }

    public void setDouble(String name, double value) throws JMSException {
        initializeWriting();
        put(name, new Double(value));
    }

    public void setString(String name, String value) throws JMSException {
        initializeWriting();
        put(name, value);
    }

    public void setBytes(String name, byte[] value) throws JMSException {
        initializeWriting();
        if (value != null) {
            put(name, value);
        } else {
            this.map.remove(name);
        }
    }

    public void setBytes(String name, byte[] value, int offset, int length) throws JMSException {
        initializeWriting();
        byte[] data = new byte[length];
        System.arraycopy(value, offset, data, 0, length);
        put(name, data);
    }

    public void setObject(String name, Object value) throws JMSException {
        initializeWriting();
        if (value != null) {
            if (!(value instanceof byte[])) {
                checkValidObject(value);
            }
            put(name, value);
            return;
        }
        put(name, null);
    }

    public boolean itemExists(String name) throws JMSException {
        initializeReading();
        return this.map.containsKey(name);
    }

    private void initializeReading() throws JMSException {
        loadContent();
    }

    private void initializeWriting() throws MessageNotWriteableException {
        checkReadOnlyBody();
        setContent(null);
    }

    public void compress() throws IOException {
        storeContent();
        super.compress();
    }

    public String toString() {
        return super.toString() + " ActiveMQMapMessage{ " + "theTable = " + this.map + " }";
    }

    public Map<String, Object> getContentMap() throws JMSException {
        initializeReading();
        return this.map;
    }
}
