package org.apache.activemq.jndi;

import java.util.Hashtable;
import java.util.Map.Entry;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.activemq.command.ActiveMQDestination;

public class ActiveMQWASInitialContextFactory extends ActiveMQInitialContextFactory {
    public Context getInitialContext(Hashtable environment) throws NamingException {
        return super.getInitialContext(transformEnvironment(environment));
    }

    protected Hashtable transformEnvironment(Hashtable environment) {
        Hashtable environment1 = new Hashtable();
        for (Entry entry : environment.entrySet()) {
            if ((entry.getKey() instanceof String) && (entry.getValue() instanceof String)) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (key.startsWith("java.naming.queue.")) {
                    environment1.put("queue." + key.substring("java.naming.queue.".length()).replace('.', '/'), value);
                } else if (key.startsWith("java.naming.topic.")) {
                    environment1.put("topic." + key.substring("java.naming.topic.".length()).replace('.', '/'), value);
                } else if (key.startsWith("java.naming.connectionFactoryNames")) {
                    environment1.put(key.substring("java.naming.".length()), value);
                } else if (key.startsWith("java.naming.connection")) {
                    environment1.put(key.substring("java.naming.".length()), value);
                } else if (key.startsWith("java.naming.provider.url")) {
                    environment1.put("java.naming.provider.url", value.replace(';', ActiveMQDestination.COMPOSITE_SEPERATOR));
                } else {
                    environment1.put(key, value);
                }
            }
        }
        return environment1;
    }
}
