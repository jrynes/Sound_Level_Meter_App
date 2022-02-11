package org.apache.activemq.jndi;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;
import org.apache.activemq.transport.stomp.Stomp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JNDIReferenceFactory implements ObjectFactory {
    static Logger log;

    static {
        log = LoggerFactory.getLogger(JNDIReferenceFactory.class);
    }

    public Object getObjectInstance(Object object, Name name, Context nameCtx, Hashtable environment) throws Exception {
        if (object instanceof Reference) {
            Reference reference = (Reference) object;
            if (log.isTraceEnabled()) {
                log.trace("Getting instance of " + reference.getClassName());
            }
            Class theClass = loadClass(this, reference.getClassName());
            if (!JNDIStorableInterface.class.isAssignableFrom(theClass)) {
                return null;
            }
            JNDIStorableInterface store = (JNDIStorableInterface) theClass.newInstance();
            Properties properties = new Properties();
            Enumeration iter = reference.getAll();
            while (iter.hasMoreElements()) {
                StringRefAddr addr = (StringRefAddr) iter.nextElement();
                properties.put(addr.getType(), addr.getContent() == null ? Stomp.EMPTY : addr.getContent());
            }
            store.setProperties(properties);
            return store;
        }
        log.error("Object " + object + " is not a reference - cannot load");
        throw new RuntimeException("Object " + object + " is not a reference");
    }

    public static Reference createReference(String instanceClassName, JNDIStorableInterface po) throws NamingException {
        if (log.isTraceEnabled()) {
            log.trace("Creating reference: " + instanceClassName + Stomp.COMMA + po);
        }
        Reference result = new Reference(instanceClassName, JNDIReferenceFactory.class.getName(), null);
        try {
            Properties props = po.getProperties();
            Enumeration iter = props.propertyNames();
            while (iter.hasMoreElements()) {
                String key = (String) iter.nextElement();
                result.add(new StringRefAddr(key, props.getProperty(key)));
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NamingException(e.getMessage());
        }
    }

    public static Class loadClass(Object thisObj, String className) throws ClassNotFoundException {
        ClassLoader loader = thisObj.getClass().getClassLoader();
        if (loader != null) {
            return loader.loadClass(className);
        }
        return Class.forName(className);
    }
}
