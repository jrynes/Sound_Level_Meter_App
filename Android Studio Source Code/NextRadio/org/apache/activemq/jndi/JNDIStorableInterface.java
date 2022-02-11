package org.apache.activemq.jndi;

import java.util.Properties;
import javax.naming.Referenceable;

public interface JNDIStorableInterface extends Referenceable {
    Properties getProperties();

    void setProperties(Properties properties);
}
