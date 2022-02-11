package org.apache.activemq.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotReadableException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.ByteSequence;
import org.apache.activemq.util.ByteSequenceData;
import org.apache.activemq.util.JMSExceptionSupport;

public class ActiveMQBytesMessage extends ActiveMQMessage implements BytesMessage {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 24;
    protected transient ByteArrayOutputStream bytesOut;
    protected transient DataInputStream dataIn;
    protected transient DataOutputStream dataOut;
    protected transient int length;

    class 1 extends FilterOutputStream {
        final /* synthetic */ Deflater val$deflater;

        1(OutputStream x0, Deflater deflater) {
            this.val$deflater = deflater;
            super(x0);
        }

        public void write(byte[] arg0) throws IOException {
            ActiveMQBytesMessage activeMQBytesMessage = ActiveMQBytesMessage.this;
            activeMQBytesMessage.length += arg0.length;
            this.out.write(arg0);
        }

        public void write(byte[] arg0, int arg1, int arg2) throws IOException {
            ActiveMQBytesMessage activeMQBytesMessage = ActiveMQBytesMessage.this;
            activeMQBytesMessage.length += arg2;
            this.out.write(arg0, arg1, arg2);
        }

        public void write(int arg0) throws IOException {
            ActiveMQBytesMessage activeMQBytesMessage = ActiveMQBytesMessage.this;
            activeMQBytesMessage.length++;
            this.out.write(arg0);
        }

        public void close() throws IOException {
            super.close();
            this.val$deflater.end();
        }
    }

    public Message copy() {
        ActiveMQBytesMessage copy = new ActiveMQBytesMessage();
        copy(copy);
        return copy;
    }

    private void copy(ActiveMQBytesMessage copy) {
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
        try {
            if (this.dataOut != null) {
                this.dataOut.close();
                ByteSequence bs = this.bytesOut.toByteSequence();
                if (this.compressed) {
                    int pos = bs.offset;
                    ByteSequenceData.writeIntBig(bs, this.length);
                    bs.offset = pos;
                }
                setContent(bs);
                this.bytesOut = null;
                this.dataOut = null;
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage(), ioe);
        }
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getJMSXMimeType() {
        return "jms/bytes-message";
    }

    public void clearBody() throws JMSException {
        super.clearBody();
        this.dataOut = null;
        this.dataIn = null;
        this.bytesOut = null;
    }

    public long getBodyLength() throws JMSException {
        initializeReading();
        return (long) this.length;
    }

    public boolean readBoolean() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readBoolean();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public byte readByte() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readByte();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public int readUnsignedByte() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readUnsignedByte();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public short readShort() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readShort();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public int readUnsignedShort() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readUnsignedShort();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public char readChar() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readChar();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public int readInt() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readInt();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public long readLong() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readLong();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public float readFloat() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readFloat();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public double readDouble() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readDouble();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public String readUTF() throws JMSException {
        initializeReading();
        try {
            return this.dataIn.readUTF();
        } catch (EOFException e) {
            throw JMSExceptionSupport.createMessageEOFException(e);
        } catch (IOException e2) {
            throw JMSExceptionSupport.createMessageFormatException(e2);
        }
    }

    public int readBytes(byte[] value) throws JMSException {
        return readBytes(value, value.length);
    }

    public int readBytes(byte[] value, int length) throws JMSException {
        initializeReading();
        int n = 0;
        while (n < length) {
            try {
                int count = this.dataIn.read(value, n, length - n);
                if (count < 0) {
                    break;
                }
                n += count;
            } catch (EOFException e) {
                throw JMSExceptionSupport.createMessageEOFException(e);
            } catch (IOException e2) {
                throw JMSExceptionSupport.createMessageFormatException(e2);
            }
        }
        if (n != 0 || length <= 0) {
            return n;
        }
        return -1;
    }

    public void writeBoolean(boolean value) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.writeBoolean(value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeByte(byte value) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.writeByte(value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeShort(short value) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.writeShort(value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeChar(char value) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.writeChar(value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeInt(int value) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.writeInt(value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeLong(long value) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.writeLong(value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeFloat(float value) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.writeFloat(value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeDouble(double value) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.writeDouble(value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeUTF(String value) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.writeUTF(value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeBytes(byte[] value) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.write(value);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeBytes(byte[] value, int offset, int length) throws JMSException {
        initializeWriting();
        try {
            this.dataOut.write(value, offset, length);
        } catch (Exception ioe) {
            throw JMSExceptionSupport.create(ioe);
        }
    }

    public void writeObject(Object value) throws JMSException {
        if (value == null) {
            throw new NullPointerException();
        }
        initializeWriting();
        if (value instanceof Boolean) {
            writeBoolean(((Boolean) value).booleanValue());
        } else if (value instanceof Character) {
            writeChar(((Character) value).charValue());
        } else if (value instanceof Byte) {
            writeByte(((Byte) value).byteValue());
        } else if (value instanceof Short) {
            writeShort(((Short) value).shortValue());
        } else if (value instanceof Integer) {
            writeInt(((Integer) value).intValue());
        } else if (value instanceof Long) {
            writeLong(((Long) value).longValue());
        } else if (value instanceof Float) {
            writeFloat(((Float) value).floatValue());
        } else if (value instanceof Double) {
            writeDouble(((Double) value).doubleValue());
        } else if (value instanceof String) {
            writeUTF(value.toString());
        } else if (value instanceof byte[]) {
            writeBytes((byte[]) value);
        } else {
            throw new MessageFormatException("Cannot write non-primitive type:" + value.getClass());
        }
    }

    public void reset() throws JMSException {
        storeContent();
        this.bytesOut = null;
        if (this.dataIn != null) {
            try {
                this.dataIn.close();
            } catch (Exception e) {
            }
        }
        this.dataIn = null;
        this.dataOut = null;
        setReadOnlyBody(true);
    }

    private void initializeWriting() throws JMSException {
        checkReadOnlyBody();
        if (this.dataOut == null) {
            this.bytesOut = new ByteArrayOutputStream();
            OutputStream os = this.bytesOut;
            ActiveMQConnection connection = getConnection();
            if (connection != null && connection.isUseCompression()) {
                try {
                    os.write(new byte[4]);
                    this.length = 0;
                    this.compressed = true;
                    Deflater deflater = new Deflater(1);
                    os = new 1(new DeflaterOutputStream(os, deflater), deflater);
                } catch (Exception e) {
                    throw JMSExceptionSupport.create(e);
                }
            }
            this.dataOut = new DataOutputStream(os);
        }
    }

    protected void checkWriteOnlyBody() throws MessageNotReadableException {
        if (!this.readOnlyBody) {
            throw new MessageNotReadableException("Message body is write-only");
        }
    }

    private void initializeReading() throws JMSException {
        checkWriteOnlyBody();
        if (this.dataIn == null) {
            ByteSequence data = getContent();
            if (data == null) {
                data = new ByteSequence(new byte[0], 0, 0);
            }
            InputStream is = new ByteArrayInputStream(data);
            if (isCompressed()) {
                try {
                    DataInputStream dis = new DataInputStream(is);
                    this.length = dis.readInt();
                    dis.close();
                    is = new InflaterInputStream(is);
                } catch (Exception e) {
                    throw JMSExceptionSupport.create(e);
                }
            }
            this.length = data.getLength();
            this.dataIn = new DataInputStream(is);
        }
    }

    public void setObjectProperty(String name, Object value) throws JMSException {
        initializeWriting();
        super.setObjectProperty(name, value);
    }

    public String toString() {
        return super.toString() + " ActiveMQBytesMessage{ " + "bytesOut = " + this.bytesOut + ", dataOut = " + this.dataOut + ", dataIn = " + this.dataIn + " }";
    }

    protected void doCompress() throws IOException {
        this.compressed = true;
        ByteSequence bytes = getContent();
        int length = bytes.getLength();
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        bytesOut.write(new byte[4]);
        DataOutputStream dataOut = new DataOutputStream(new DeflaterOutputStream(bytesOut));
        dataOut.write(bytes.data, bytes.offset, bytes.length);
        dataOut.flush();
        dataOut.close();
        bytes = bytesOut.toByteSequence();
        ByteSequenceData.writeIntBig(bytes, length);
        bytes.offset = 0;
        setContent(bytes);
    }

    protected void finalize() throws Throwable {
        if (this.dataIn != null) {
            try {
                this.dataIn.close();
            } catch (Exception e) {
            }
        }
    }
}
