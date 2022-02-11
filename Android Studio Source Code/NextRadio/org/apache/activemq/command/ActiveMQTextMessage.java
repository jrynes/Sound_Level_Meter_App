package org.apache.activemq.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import javax.jms.JMSException;
import javax.jms.MessageNotWriteableException;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.ByteSequence;
import org.apache.activemq.util.JMSExceptionSupport;
import org.apache.activemq.util.MarshallingSupport;
import org.apache.activemq.wireformat.WireFormat;

public class ActiveMQTextMessage extends ActiveMQMessage implements TextMessage {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 28;
    protected String text;

    public Message copy() {
        ActiveMQTextMessage copy = new ActiveMQTextMessage();
        copy(copy);
        return copy;
    }

    private void copy(ActiveMQTextMessage copy) {
        super.copy(copy);
        copy.text = this.text;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getJMSXMimeType() {
        return "jms/text-message";
    }

    public void setText(String text) throws MessageNotWriteableException {
        checkReadOnlyBody();
        this.text = text;
        setContent(null);
    }

    public String getText() throws JMSException {
        Exception ioe;
        Throwable th;
        if (this.text == null && getContent() != null) {
            InputStream is = null;
            try {
                ByteSequence bodyAsBytes = getContent();
                if (bodyAsBytes != null) {
                    InputStream is2 = new ByteArrayInputStream(bodyAsBytes);
                    try {
                        if (isCompressed()) {
                            is = new InflaterInputStream(is2);
                        } else {
                            is = is2;
                        }
                        DataInputStream dataIn = new DataInputStream(is);
                        this.text = MarshallingSupport.readUTF8(dataIn);
                        dataIn.close();
                        setContent(null);
                        setCompressed(false);
                    } catch (IOException e) {
                        ioe = e;
                        is = is2;
                        try {
                            throw JMSExceptionSupport.create(ioe);
                        } catch (Throwable th2) {
                            th = th2;
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (IOException e2) {
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        is = is2;
                        if (is != null) {
                            is.close();
                        }
                        throw th;
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (IOException e4) {
                ioe = e4;
                throw JMSExceptionSupport.create(ioe);
            }
        }
        return this.text;
    }

    public void beforeMarshall(WireFormat wireFormat) throws IOException {
        super.beforeMarshall(wireFormat);
        storeContent();
    }

    public void storeContent() {
        try {
            if (getContent() == null && this.text != null) {
                OutputStream bytesOut = new ByteArrayOutputStream();
                OutputStream os = bytesOut;
                ActiveMQConnection connection = getConnection();
                if (connection != null && connection.isUseCompression()) {
                    this.compressed = true;
                    os = new DeflaterOutputStream(os);
                }
                DataOutputStream dataOut = new DataOutputStream(os);
                MarshallingSupport.writeUTF8(dataOut, this.text);
                dataOut.close();
                setContent(bytesOut.toByteSequence());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearMarshalledState() throws JMSException {
        super.clearMarshalledState();
        this.text = null;
    }

    public void clearBody() throws JMSException {
        super.clearBody();
        this.text = null;
    }

    public int getSize() {
        if (this.size == 0 && this.content == null && this.text != null) {
            this.size = getMinimumMessageSize();
            if (this.marshalledProperties != null) {
                this.size += this.marshalledProperties.getLength();
            }
            this.size += this.text.length() * 2;
        }
        return super.getSize();
    }

    public String toString() {
        try {
            String text = getText();
            if (text != null) {
                text = MarshallingSupport.truncate64(text);
                HashMap<String, Object> overrideFields = new HashMap();
                overrideFields.put("text", text);
                return super.toString(overrideFields);
            }
        } catch (JMSException e) {
        }
        return super.toString();
    }
}
