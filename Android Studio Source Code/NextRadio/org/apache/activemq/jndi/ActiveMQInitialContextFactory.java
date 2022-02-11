package org.apache.activemq.jndi;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.transport.stomp.Stomp;

public class ActiveMQInitialContextFactory implements InitialContextFactory {
    private static final String[] DEFAULT_CONNECTION_FACTORY_NAMES;
    private String connectionPrefix;
    private String queuePrefix;
    private String topicPrefix;

    class 1 extends LazyCreateContext {
        private static final long serialVersionUID = 6503881346214855588L;

        1() {
        }

        protected Object createEntry(String name) {
            return new ActiveMQQueue(name);
        }
    }

    class 2 extends LazyCreateContext {
        private static final long serialVersionUID = 2019166796234979615L;

        2() {
        }

        protected Object createEntry(String name) {
            return new ActiveMQTopic(name);
        }
    }

    public ActiveMQInitialContextFactory() {
        this.connectionPrefix = "connection.";
        this.queuePrefix = "queue.";
        this.topicPrefix = "topic.";
    }

    static {
        DEFAULT_CONNECTION_FACTORY_NAMES = new String[]{"ConnectionFactory", "XAConnectionFactory", "QueueConnectionFactory", "TopicConnectionFactory"};
    }

    public Context getInitialContext(Hashtable environment) throws NamingException {
        Map<String, Object> data = new ConcurrentHashMap();
        String[] names = getConnectionFactoryNames(environment);
        int i = 0;
        while (i < names.length) {
            String name = names[i];
            try {
                data.put(name, createConnectionFactory(name, environment));
                i++;
            } catch (Exception e) {
                throw new NamingException("Invalid broker URL");
            }
        }
        createQueues(data, environment);
        createTopics(data, environment);
        data.put("dynamicQueues", new 1());
        data.put("dynamicTopics", new 2());
        return createContext(environment, data);
    }

    public String getTopicPrefix() {
        return this.topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public String getQueuePrefix() {
        return this.queuePrefix;
    }

    public void setQueuePrefix(String queuePrefix) {
        this.queuePrefix = queuePrefix;
    }

    protected ReadOnlyContext createContext(Hashtable environment, Map<String, Object> data) {
        return new ReadOnlyContext(environment, (Map) data);
    }

    protected ActiveMQConnectionFactory createConnectionFactory(String name, Hashtable environment) throws URISyntaxException {
        Hashtable temp = new Hashtable(environment);
        if (DEFAULT_CONNECTION_FACTORY_NAMES[1].equals(name)) {
            temp.put("xa", String.valueOf(true));
        }
        String prefix = this.connectionPrefix + name + ActiveMQDestination.PATH_SEPERATOR;
        for (Entry entry : environment.entrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(prefix)) {
                temp.remove(key);
                temp.put(key.substring(prefix.length()), entry.getValue());
            }
        }
        return createConnectionFactory(temp);
    }

    protected String[] getConnectionFactoryNames(Map environment) {
        String factoryNames = (String) environment.get("connectionFactoryNames");
        if (factoryNames != null) {
            List<String> list = new ArrayList();
            StringTokenizer enumeration = new StringTokenizer(factoryNames, Stomp.COMMA);
            while (enumeration.hasMoreTokens()) {
                list.add(enumeration.nextToken().trim());
            }
            int size = list.size();
            if (size > 0) {
                String[] answer = new String[size];
                list.toArray(answer);
                return answer;
            }
        }
        return DEFAULT_CONNECTION_FACTORY_NAMES;
    }

    protected void createQueues(Map<String, Object> data, Hashtable environment) {
        for (Entry entry : environment.entrySet()) {
            String key = entry.getKey().toString();
            if (key.startsWith(this.queuePrefix)) {
                data.put(key.substring(this.queuePrefix.length()), createQueue(entry.getValue().toString()));
            }
        }
    }

    protected void createTopics(Map<String, Object> data, Hashtable environment) {
        for (Entry entry : environment.entrySet()) {
            String key = entry.getKey().toString();
            if (key.startsWith(this.topicPrefix)) {
                data.put(key.substring(this.topicPrefix.length()), createTopic(entry.getValue().toString()));
            }
        }
    }

    protected Queue createQueue(String name) {
        return new ActiveMQQueue(name);
    }

    protected Topic createTopic(String name) {
        return new ActiveMQTopic(name);
    }

    protected ActiveMQConnectionFactory createConnectionFactory(Hashtable environment) throws URISyntaxException {
        ActiveMQConnectionFactory answer = needsXA(environment) ? new ActiveMQXAConnectionFactory() : new ActiveMQConnectionFactory();
        Properties properties = new Properties();
        properties.putAll(environment);
        answer.setProperties(properties);
        return answer;
    }

    private boolean needsXA(Hashtable environment) {
        boolean isXA = Boolean.parseBoolean((String) environment.get("xa"));
        environment.remove("xa");
        return isXA;
    }

    public String getConnectionPrefix() {
        return this.connectionPrefix;
    }

    public void setConnectionPrefix(String connectionPrefix) {
        this.connectionPrefix = connectionPrefix;
    }
}
