package org.apache.activemq.jndi;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.Reference;

public abstract class JNDIBaseStorable implements JNDIStorableInterface, Externalizable {
    private Properties properties;

    protected abstract void buildFromProperties(Properties properties);

    protected abstract void populateProperties(Properties properties);

    public synchronized void setProperties(Properties props) {
        this.properties = props;
        buildFromProperties(props);
    }

    public synchronized Properties getProperties() {
        if (this.properties == null) {
            this.properties = new Properties();
        }
        populateProperties(this.properties);
        return this.properties;
    }

    public Reference getReference() throws NamingException {
        return JNDIReferenceFactory.createReference(getClass().getName(), this);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Properties props = (Properties) in.readObject();
        if (props != null) {
            setProperties(props);
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getProperties());
    }
}
