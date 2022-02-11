package org.apache.activemq.wireformat;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.ByteSequence;
import org.apache.activemq.util.ClassLoadingAwareObjectInputStream;

public class ObjectStreamWireFormat implements WireFormat {
    public ByteSequence marshal(Object command) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(baos);
        marshal(command, ds);
        ds.close();
        return baos.toByteSequence();
    }

    public Object unmarshal(ByteSequence packet) throws IOException {
        return unmarshal(new DataInputStream(new ByteArrayInputStream(packet)));
    }

    public void marshal(Object command, DataOutput ds) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream((OutputStream) ds);
        out.writeObject(command);
        out.flush();
        out.reset();
    }

    public Object unmarshal(DataInput ds) throws IOException {
        try {
            ClassLoadingAwareObjectInputStream in = new ClassLoadingAwareObjectInputStream((InputStream) ds);
            Object command = in.readObject();
            in.close();
            return command;
        } catch (ClassNotFoundException e) {
            throw ((IOException) new IOException("unmarshal failed: " + e).initCause(e));
        }
    }

    public void setVersion(int version) {
    }

    public int getVersion() {
        return 0;
    }
}
