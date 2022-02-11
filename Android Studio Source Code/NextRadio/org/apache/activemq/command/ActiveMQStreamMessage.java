package org.apache.activemq.command;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import javax.jms.JMSException;
import javax.jms.MessageEOFException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotReadableException;
import javax.jms.MessageNotWriteableException;
import javax.jms.StreamMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.ByteSequence;
import org.apache.activemq.util.JMSExceptionSupport;
import org.apache.activemq.util.MarshallingSupport;

public class ActiveMQStreamMessage extends ActiveMQMessage implements StreamMessage {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 27;
    protected transient ByteArrayOutputStream bytesOut;
    protected transient DataInputStream dataIn;
    protected transient DataOutputStream dataOut;
    protected transient int remainingBytes;

    public ActiveMQStreamMessage() {
        this.remainingBytes = -1;
    }

    public Message copy() {
        ActiveMQStreamMessage copy = new ActiveMQStreamMessage();
        copy(copy);
        return copy;
    }

    private void copy(ActiveMQStreamMessage copy) {
        storeContent();
        super.copy(copy);
        copy.dataOut = null;
        copy.bytesOut = null;
        copy.dataIn = null;
    }

    public void onSend() throws JMSException {
        super.onSend();
        storeContent();
    }

    public void storeContent() {
        if (this.dataOut != null) {
            try {
                this.dataOut.close();
                setContent(this.bytesOut.toByteSequence());
                this.bytesOut = null;
                this.dataOut = null;
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getJMSXMimeType() {
        return "jms/stream-message";
    }

    public void clearBody() throws JMSException {
        super.clearBody();
        this.dataOut = null;
        this.dataIn = null;
        this.bytesOut = null;
        this.remainingBytes = -1;
    }

    public boolean readBoolean() throws JMSException {
        initializeReading();
        try {
            this.dataIn.mark(10);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type == 1) {
                return this.dataIn.readBoolean();
            } else {
                if (type == 9) {
                    return Boolean.valueOf(this.dataIn.readUTF()).booleanValue();
                }
                if (type == 0) {
                    this.dataIn.reset();
                    throw new NullPointerException("Cannot convert NULL value to boolean.");
                }
                this.dataIn.reset();
                throw new MessageFormatException(" not a boolean type");
            }
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public byte readByte() throws JMSException {
        initializeReading();
        try {
            this.dataIn.mark(10);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type == 2) {
                return this.dataIn.readByte();
            } else {
                if (type == 9) {
                    return Byte.valueOf(this.dataIn.readUTF()).byteValue();
                }
                if (type == 0) {
                    this.dataIn.reset();
                    throw new NullPointerException("Cannot convert NULL value to byte.");
                }
                this.dataIn.reset();
                throw new MessageFormatException(" not a byte type");
            }
        } catch (NumberFormatException mfe) {
            try {
                this.dataIn.reset();
                throw mfe;
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public short readShort() throws JMSException {
        initializeReading();
        try {
            this.dataIn.mark(17);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type == 4) {
                return this.dataIn.readShort();
            } else {
                if (type == 2) {
                    return (short) this.dataIn.readByte();
                }
                if (type == 9) {
                    return Short.valueOf(this.dataIn.readUTF()).shortValue();
                }
                if (type == 0) {
                    this.dataIn.reset();
                    throw new NullPointerException("Cannot convert NULL value to short.");
                }
                this.dataIn.reset();
                throw new MessageFormatException(" not a short type");
            }
        } catch (NumberFormatException mfe) {
            try {
                this.dataIn.reset();
                throw mfe;
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public char readChar() throws JMSException {
        initializeReading();
        try {
            this.dataIn.mark(17);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type == 3) {
                return this.dataIn.readChar();
            } else {
                if (type == 0) {
                    this.dataIn.reset();
                    throw new NullPointerException("Cannot convert NULL value to char.");
                }
                this.dataIn.reset();
                throw new MessageFormatException(" not a char type");
            }
        } catch (NumberFormatException mfe) {
            try {
                this.dataIn.reset();
                throw mfe;
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public int readInt() throws JMSException {
        initializeReading();
        try {
            this.dataIn.mark(33);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type == 5) {
                return this.dataIn.readInt();
            } else {
                if (type == 4) {
                    return this.dataIn.readShort();
                }
                if (type == 2) {
                    return this.dataIn.readByte();
                }
                if (type == 9) {
                    return Integer.valueOf(this.dataIn.readUTF()).intValue();
                }
                if (type == 0) {
                    this.dataIn.reset();
                    throw new NullPointerException("Cannot convert NULL value to int.");
                }
                this.dataIn.reset();
                throw new MessageFormatException(" not an int type");
            }
        } catch (NumberFormatException mfe) {
            try {
                this.dataIn.reset();
                throw mfe;
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public long readLong() throws JMSException {
        initializeReading();
        try {
            this.dataIn.mark(65);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type == 6) {
                return this.dataIn.readLong();
            } else {
                if (type == 5) {
                    return (long) this.dataIn.readInt();
                }
                if (type == 4) {
                    return (long) this.dataIn.readShort();
                }
                if (type == 2) {
                    return (long) this.dataIn.readByte();
                }
                if (type == 9) {
                    return Long.valueOf(this.dataIn.readUTF()).longValue();
                }
                if (type == 0) {
                    this.dataIn.reset();
                    throw new NullPointerException("Cannot convert NULL value to long.");
                }
                this.dataIn.reset();
                throw new MessageFormatException(" not a long type");
            }
        } catch (NumberFormatException mfe) {
            try {
                this.dataIn.reset();
                throw mfe;
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public float readFloat() throws JMSException {
        initializeReading();
        try {
            this.dataIn.mark(33);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type == 8) {
                return this.dataIn.readFloat();
            } else {
                if (type == 9) {
                    return Float.valueOf(this.dataIn.readUTF()).floatValue();
                }
                if (type == 0) {
                    this.dataIn.reset();
                    throw new NullPointerException("Cannot convert NULL value to float.");
                }
                this.dataIn.reset();
                throw new MessageFormatException(" not a float type");
            }
        } catch (NumberFormatException mfe) {
            try {
                this.dataIn.reset();
                throw mfe;
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public double readDouble() throws JMSException {
        initializeReading();
        try {
            this.dataIn.mark(65);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type == 7) {
                return this.dataIn.readDouble();
            } else {
                if (type == 8) {
                    return (double) this.dataIn.readFloat();
                }
                if (type == 9) {
                    return Double.valueOf(this.dataIn.readUTF()).doubleValue();
                }
                if (type == 0) {
                    this.dataIn.reset();
                    throw new NullPointerException("Cannot convert NULL value to double.");
                }
                this.dataIn.reset();
                throw new MessageFormatException(" not a double type");
            }
        } catch (NumberFormatException mfe) {
            try {
                this.dataIn.reset();
                throw mfe;
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public String readString() throws JMSException {
        initializeReading();
        try {
            this.dataIn.mark(65);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type == 0) {
                return null;
            } else {
                if (type == 13) {
                    return MarshallingSupport.readUTF8(this.dataIn);
                }
                if (type == 9) {
                    return this.dataIn.readUTF();
                }
                if (type == 6) {
                    return new Long(this.dataIn.readLong()).toString();
                }
                if (type == 5) {
                    return new Integer(this.dataIn.readInt()).toString();
                }
                if (type == 4) {
                    return new Short(this.dataIn.readShort()).toString();
                }
                if (type == 2) {
                    return new Byte(this.dataIn.readByte()).toString();
                }
                if (type == 8) {
                    return new Float(this.dataIn.readFloat()).toString();
                }
                if (type == 7) {
                    return new Double(this.dataIn.readDouble()).toString();
                }
                if (type == 1) {
                    return (this.dataIn.readBoolean() ? Boolean.TRUE : Boolean.FALSE).toString();
                } else if (type == 3) {
                    return new Character(this.dataIn.readChar()).toString();
                } else {
                    this.dataIn.reset();
                    throw new MessageFormatException(" not a String type");
                }
            }
        } catch (NumberFormatException mfe) {
            try {
                this.dataIn.reset();
                throw mfe;
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public int readBytes(byte[] value) throws JMSException {
        initializeReading();
        if (value == null) {
            try {
                throw new NullPointerException();
            } catch (EOFException e) {
                JMSException jmsEx = new MessageEOFException(e.getMessage());
                jmsEx.setLinkedException(e);
                throw jmsEx;
            } catch (IOException e2) {
                jmsEx = new MessageFormatException(e2.getMessage());
                jmsEx.setLinkedException(e2);
                throw jmsEx;
            }
        }
        if (this.remainingBytes == -1) {
            this.dataIn.mark(value.length + 1);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type != 10) {
                throw new MessageFormatException("Not a byte array");
            } else {
                this.remainingBytes = this.dataIn.readInt();
            }
        } else if (this.remainingBytes == 0) {
            this.remainingBytes = -1;
            return -1;
        }
        if (value.length <= this.remainingBytes) {
            this.remainingBytes -= value.length;
            this.dataIn.readFully(value);
            return value.length;
        }
        int rc = this.dataIn.read(value, 0, this.remainingBytes);
        this.remainingBytes = 0;
        return rc;
    }

    public Object readObject() throws JMSException {
        JMSException jmsEx;
        initializeReading();
        try {
            this.dataIn.mark(65);
            int type = this.dataIn.read();
            if (type == -1) {
                throw new MessageEOFException("reached end of data");
            } else if (type == 0) {
                return null;
            } else {
                if (type == 13) {
                    return MarshallingSupport.readUTF8(this.dataIn);
                }
                if (type == 9) {
                    return this.dataIn.readUTF();
                }
                if (type == 6) {
                    return Long.valueOf(this.dataIn.readLong());
                }
                if (type == 5) {
                    return Integer.valueOf(this.dataIn.readInt());
                }
                if (type == 4) {
                    return Short.valueOf(this.dataIn.readShort());
                }
                if (type == 2) {
                    return Byte.valueOf(this.dataIn.readByte());
                }
                if (type == 8) {
                    return new Float(this.dataIn.readFloat());
                }
                if (type == 7) {
                    return new Double(this.dataIn.readDouble());
                }
                if (type == 1) {
                    return this.dataIn.readBoolean() ? Boolean.TRUE : Boolean.FALSE;
                } else {
                    if (type == 3) {
                        return Character.valueOf(this.dataIn.readChar());
                    }
                    if (type == 10) {
                        Object value = new byte[this.dataIn.readInt()];
                        this.dataIn.readFully(value);
                        return value;
                    }
                    this.dataIn.reset();
                    throw new MessageFormatException("unknown type");
                }
            }
        } catch (NumberFormatException mfe) {
            try {
                this.dataIn.reset();
                throw mfe;
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        } catch (EOFException e) {
            jmsEx = new MessageEOFException(e.getMessage());
            jmsEx.setLinkedException(e);
            throw jmsEx;
        } catch (IOException e2) {
            jmsEx = new MessageFormatException(e2.getMessage());
            jmsEx.setLinkedException(e2);
            throw jmsEx;
        }
    }

    public void writeBoolean(boolean value) throws JMSException {
        initializeWriting();
        try {
            MarshallingSupport.marshalBoolean(this.dataOut, value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeByte(byte value) throws JMSException {
        initializeWriting();
        try {
            MarshallingSupport.marshalByte(this.dataOut, value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeShort(short value) throws JMSException {
        initializeWriting();
        try {
            MarshallingSupport.marshalShort(this.dataOut, value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeChar(char value) throws JMSException {
        initializeWriting();
        try {
            MarshallingSupport.marshalChar(this.dataOut, value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeInt(int value) throws JMSException {
        initializeWriting();
        try {
            MarshallingSupport.marshalInt(this.dataOut, value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeLong(long value) throws JMSException {
        initializeWriting();
        try {
            MarshallingSupport.marshalLong(this.dataOut, value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeFloat(float value) throws JMSException {
        initializeWriting();
        try {
            MarshallingSupport.marshalFloat(this.dataOut, value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeDouble(double value) throws JMSException {
        initializeWriting();
        try {
            MarshallingSupport.marshalDouble(this.dataOut, value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeString(String value) throws JMSException {
        initializeWriting();
        if (value == null) {
            try {
                MarshallingSupport.marshalNull(this.dataOut);
                return;
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        }
        MarshallingSupport.marshalString(this.dataOut, value);
    }

    public void writeBytes(byte[] value) throws JMSException {
        writeBytes(value, 0, value.length);
    }

    public void writeBytes(byte[] value, int offset, int length) throws JMSException {
        initializeWriting();
        try {
            MarshallingSupport.marshalByteArray(this.dataOut, value, offset, length);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeObject(Object value) throws JMSException {
        initializeWriting();
        if (value == null) {
            try {
                MarshallingSupport.marshalNull(this.dataOut);
            } catch (Exception ioe) {
                throw JMSExceptionSupport.create(ioe);
            }
        } else if (value instanceof String) {
            writeString(value.toString());
        } else if (value instanceof Character) {
            writeChar(((Character) value).charValue());
        } else if (value instanceof Boolean) {
            writeBoolean(((Boolean) value).booleanValue());
        } else if (value instanceof Byte) {
            writeByte(((Byte) value).byteValue());
        } else if (value instanceof Short) {
            writeShort(((Short) value).shortValue());
        } else if (value instanceof Integer) {
            writeInt(((Integer) value).intValue());
        } else if (value instanceof Float) {
            writeFloat(((Float) value).floatValue());
        } else if (value instanceof Double) {
            writeDouble(((Double) value).doubleValue());
        } else if (value instanceof byte[]) {
            writeBytes((byte[]) value);
        } else if (value instanceof Long) {
            writeLong(((Long) value).longValue());
        } else {
            throw new MessageFormatException("Unsupported Object type: " + value.getClass());
        }
    }

    public void reset() throws JMSException {
        storeContent();
        this.bytesOut = null;
        this.dataIn = null;
        this.dataOut = null;
        this.remainingBytes = -1;
        setReadOnlyBody(true);
    }

    private void initializeWriting() throws MessageNotWriteableException {
        checkReadOnlyBody();
        if (this.dataOut == null) {
            this.bytesOut = new ByteArrayOutputStream();
            OutputStream os = this.bytesOut;
            ActiveMQConnection connection = getConnection();
            if (connection != null && connection.isUseCompression()) {
                this.compressed = true;
                os = new DeflaterOutputStream(os);
            }
            this.dataOut = new DataOutputStream(os);
        }
    }

    protected void checkWriteOnlyBody() throws MessageNotReadableException {
        if (!this.readOnlyBody) {
            throw new MessageNotReadableException("Message body is write-only");
        }
    }

    private void initializeReading() throws MessageNotReadableException {
        checkWriteOnlyBody();
        if (this.dataIn == null) {
            ByteSequence data = getContent();
            if (data == null) {
                data = new ByteSequence(new byte[0], 0, 0);
            }
            InputStream is = new ByteArrayInputStream(data);
            if (isCompressed()) {
                is = new BufferedInputStream(new InflaterInputStream(is));
            }
            this.dataIn = new DataInputStream(is);
        }
    }

    public void compress() throws IOException {
        storeContent();
        super.compress();
    }

    public String toString() {
        return super.toString() + " ActiveMQStreamMessage{ " + "bytesOut = " + this.bytesOut + ", dataOut = " + this.dataOut + ", dataIn = " + this.dataIn + " }";
    }
}
