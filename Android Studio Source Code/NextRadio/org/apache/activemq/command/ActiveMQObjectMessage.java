package org.apache.activemq.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.ClassLoadingAwareObjectInputStream;
import org.apache.activemq.util.JMSExceptionSupport;
import org.apache.activemq.wireformat.WireFormat;

public class ActiveMQObjectMessage extends ActiveMQMessage implements ObjectMessage {
    static final ClassLoader ACTIVEMQ_CLASSLOADER;
    public static final byte DATA_STRUCTURE_TYPE = (byte) 26;
    protected transient Serializable object;

    static {
        ACTIVEMQ_CLASSLOADER = ActiveMQObjectMessage.class.getClassLoader();
    }

    public Message copy() {
        ActiveMQObjectMessage copy = new ActiveMQObjectMessage();
        copy(copy);
        return copy;
    }

    private void copy(ActiveMQObjectMessage copy) {
        ActiveMQConnection connection = getConnection();
        if (connection == null || !connection.isObjectMessageSerializationDefered()) {
            storeContent();
            copy.object = null;
        } else {
            copy.object = this.object;
        }
        super.copy(copy);
    }

    public void storeContent() {
        if (getContent() == null && this.object != null) {
            try {
                OutputStream bytesOut = new ByteArrayOutputStream();
                OutputStream os = bytesOut;
                ActiveMQConnection connection = getConnection();
                if (connection != null && connection.isUseCompression()) {
                    this.compressed = true;
                    os = new DeflaterOutputStream(os);
                }
                ObjectOutputStream objOut = new ObjectOutputStream(new DataOutputStream(os));
                objOut.writeObject(this.object);
                objOut.flush();
                objOut.reset();
                objOut.close();
                setContent(bytesOut.toByteSequence());
            } catch (IOException ioe) {
                throw new RuntimeException(ioe.getMessage(), ioe);
            }
        }
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getJMSXMimeType() {
        return "jms/object-message";
    }

    public void clearBody() throws JMSException {
        super.clearBody();
        this.object = null;
    }

    public void setObject(Serializable newObject) throws JMSException {
        checkReadOnlyBody();
        this.object = newObject;
        setContent(null);
        ActiveMQConnection connection = getConnection();
        if (connection == null || !connection.isObjectMessageSerializationDefered()) {
            storeContent();
        }
    }

    public Serializable getObject() throws JMSException {
        if (this.object == null && getContent() != null) {
            try {
                InputStream is = new ByteArrayInputStream(getContent());
                if (isCompressed()) {
                    is = new InflaterInputStream(is);
                }
                DataInputStream dataIn = new DataInputStream(is);
                try {
                    this.object = (Serializable) new ClassLoadingAwareObjectInputStream(dataIn).readObject();
                    dataIn.close();
                } catch (Exception ce) {
                    throw JMSExceptionSupport.create("Failed to build body from content. Serializable class not available to broker. Reason: " + ce, ce);
                } catch (Throwable th) {
                    dataIn.close();
                }
            } catch (Exception e) {
                throw JMSExceptionSupport.create("Failed to build body from bytes. Reason: " + e, e);
            }
        }
        return this.object;
    }

    public void beforeMarshall(WireFormat wireFormat) throws IOException {
        super.beforeMarshall(wireFormat);
        storeContent();
    }

    public void clearMarshalledState() throws JMSException {
        super.clearMarshalledState();
        this.object = null;
    }

    public void onMessageRolledBack() {
        super.onMessageRolledBack();
        this.object = null;
    }

    public void compress() throws IOException {
        storeContent();
        super.compress();
    }

    public String toString() {
        try {
            getObject();
        } catch (JMSException e) {
        }
        return super.toString();
    }
}
