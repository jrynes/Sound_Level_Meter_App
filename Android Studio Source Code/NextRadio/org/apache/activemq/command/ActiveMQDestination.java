package org.apache.activemq.command;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import org.apache.activemq.jndi.JNDIBaseStorable;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public abstract class ActiveMQDestination extends JNDIBaseStorable implements DataStructure, Destination, Externalizable, Comparable<Object> {
    public static final char COMPOSITE_SEPERATOR = ',';
    public static final String PATH_SEPERATOR = ".";
    public static final String QUEUE_QUALIFIED_PREFIX = "queue://";
    public static final byte QUEUE_TYPE = (byte) 1;
    public static final String TEMP_DESTINATION_NAME_PREFIX = "ID:";
    public static final byte TEMP_MASK = (byte) 4;
    public static final String TEMP_QUEUE_QUALIFED_PREFIX = "temp-queue://";
    public static final byte TEMP_QUEUE_TYPE = (byte) 5;
    public static final String TEMP_TOPIC_QUALIFED_PREFIX = "temp-topic://";
    public static final byte TEMP_TOPIC_TYPE = (byte) 6;
    public static final String TOPIC_QUALIFIED_PREFIX = "topic://";
    public static final byte TOPIC_TYPE = (byte) 2;
    private static final long serialVersionUID = -3885260014960795889L;
    protected static UnresolvedDestinationTransformer unresolvableDestinationTransformer;
    protected transient ActiveMQDestination[] compositeDestinations;
    protected transient String[] destinationPaths;
    protected transient int hashValue;
    protected transient boolean isPattern;
    protected Map<String, String> options;
    protected String physicalName;

    public abstract byte getDestinationType();

    protected abstract String getQualifiedPrefix();

    static {
        unresolvableDestinationTransformer = new DefaultUnresolvedDestinationTransformer();
    }

    protected ActiveMQDestination(String name) {
        setPhysicalName(name);
    }

    public ActiveMQDestination(ActiveMQDestination[] composites) {
        setCompositeDestinations(composites);
    }

    public static ActiveMQDestination createDestination(String name, byte defaultType) {
        if (name.startsWith(QUEUE_QUALIFIED_PREFIX)) {
            return new ActiveMQQueue(name.substring(QUEUE_QUALIFIED_PREFIX.length()));
        }
        if (name.startsWith(TOPIC_QUALIFIED_PREFIX)) {
            return new ActiveMQTopic(name.substring(TOPIC_QUALIFIED_PREFIX.length()));
        }
        if (name.startsWith(TEMP_QUEUE_QUALIFED_PREFIX)) {
            return new ActiveMQTempQueue(name.substring(TEMP_QUEUE_QUALIFED_PREFIX.length()));
        }
        if (name.startsWith(TEMP_TOPIC_QUALIFED_PREFIX)) {
            return new ActiveMQTempTopic(name.substring(TEMP_TOPIC_QUALIFED_PREFIX.length()));
        }
        switch (defaultType) {
            case Zone.PRIMARY /*1*/:
                return new ActiveMQQueue(name);
            case Zone.SECONDARY /*2*/:
                return new ActiveMQTopic(name);
            case Service.RJE /*5*/:
                return new ActiveMQTempQueue(name);
            case Protocol.TCP /*6*/:
                return new ActiveMQTempTopic(name);
            default:
                throw new IllegalArgumentException("Invalid default destination type: " + defaultType);
        }
    }

    public static ActiveMQDestination transform(Destination dest) throws JMSException {
        if (dest == null) {
            return null;
        }
        if (dest instanceof ActiveMQDestination) {
            return (ActiveMQDestination) dest;
        }
        if ((dest instanceof Queue) && (dest instanceof Topic)) {
            String queueName = ((Queue) dest).getQueueName();
            String topicName = ((Topic) dest).getTopicName();
            if (queueName != null && topicName == null) {
                return new ActiveMQQueue(queueName);
            }
            if (queueName != null || topicName == null) {
                return unresolvableDestinationTransformer.transform(dest);
            }
            return new ActiveMQTopic(topicName);
        } else if (dest instanceof TemporaryQueue) {
            return new ActiveMQTempQueue(((TemporaryQueue) dest).getQueueName());
        } else {
            if (dest instanceof TemporaryTopic) {
                return new ActiveMQTempTopic(((TemporaryTopic) dest).getTopicName());
            }
            if (dest instanceof Queue) {
                return new ActiveMQQueue(((Queue) dest).getQueueName());
            }
            if (dest instanceof Topic) {
                return new ActiveMQTopic(((Topic) dest).getTopicName());
            }
            throw new JMSException("Could not transform the destination into a ActiveMQ destination: " + dest);
        }
    }

    public static int compare(ActiveMQDestination destination, ActiveMQDestination destination2) {
        if (destination == destination2) {
            return 0;
        }
        if (destination == null) {
            return -1;
        }
        if (destination2 == null) {
            return 1;
        }
        if (destination.isQueue() == destination2.isQueue()) {
            return destination.getPhysicalName().compareTo(destination2.getPhysicalName());
        }
        if (destination.isQueue()) {
            return -1;
        }
        return 1;
    }

    public int compareTo(Object that) {
        if (that instanceof ActiveMQDestination) {
            return compare(this, (ActiveMQDestination) that);
        }
        if (that == null) {
            return 1;
        }
        return getClass().getName().compareTo(that.getClass().getName());
    }

    public boolean isComposite() {
        return this.compositeDestinations != null;
    }

    public ActiveMQDestination[] getCompositeDestinations() {
        return this.compositeDestinations;
    }

    public void setCompositeDestinations(ActiveMQDestination[] destinations) {
        this.compositeDestinations = destinations;
        this.destinationPaths = null;
        this.hashValue = 0;
        this.isPattern = false;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < destinations.length; i++) {
            if (i != 0) {
                sb.append(COMPOSITE_SEPERATOR);
            }
            if (getDestinationType() == destinations[i].getDestinationType()) {
                sb.append(destinations[i].getPhysicalName());
            } else {
                sb.append(destinations[i].getQualifiedName());
            }
        }
        this.physicalName = sb.toString();
    }

    public String getQualifiedName() {
        if (isComposite()) {
            return this.physicalName;
        }
        return getQualifiedPrefix() + this.physicalName;
    }

    public String getPhysicalName() {
        return this.physicalName;
    }

    public void setPhysicalName(String physicalName) {
        physicalName = physicalName.trim();
        int len = physicalName.length();
        int p = -1;
        boolean composite = false;
        for (int i = 0; i < len; i++) {
            char c = physicalName.charAt(i);
            if (c == '?') {
                p = i;
                break;
            }
            if (c == ',') {
                this.isPattern = false;
                composite = true;
            } else if (!composite && (c == '*' || c == '>')) {
                this.isPattern = true;
            }
        }
        if (p >= 0) {
            String optstring = physicalName.substring(p + 1);
            physicalName = physicalName.substring(0, p);
            try {
                this.options = URISupport.parseQuery(optstring);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid destination name: " + physicalName + ", it's options are not encoded properly: " + e);
            }
        }
        this.physicalName = physicalName;
        this.destinationPaths = null;
        this.hashValue = 0;
        if (composite) {
            Set<String> l = new HashSet();
            StringTokenizer iter = new StringTokenizer(physicalName, Stomp.COMMA);
            while (iter.hasMoreTokens()) {
                String name = iter.nextToken().trim();
                if (name.length() != 0) {
                    l.add(name);
                }
            }
            this.compositeDestinations = new ActiveMQDestination[l.size()];
            int counter = 0;
            for (String dest : l) {
                int counter2 = counter + 1;
                this.compositeDestinations[counter] = createDestination(dest);
                counter = counter2;
            }
        }
    }

    public ActiveMQDestination createDestination(String name) {
        return createDestination(name, getDestinationType());
    }

    public String[] getDestinationPaths() {
        if (this.destinationPaths != null) {
            return this.destinationPaths;
        }
        List<String> l = new ArrayList();
        StringTokenizer iter = new StringTokenizer(this.physicalName, PATH_SEPERATOR);
        while (iter.hasMoreTokens()) {
            String name = iter.nextToken().trim();
            if (name.length() != 0) {
                l.add(name);
            }
        }
        this.destinationPaths = new String[l.size()];
        l.toArray(this.destinationPaths);
        return this.destinationPaths;
    }

    public boolean isQueue() {
        return false;
    }

    public boolean isTopic() {
        return false;
    }

    public boolean isTemporary() {
        return false;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.physicalName.equals(((ActiveMQDestination) o).physicalName);
    }

    public int hashCode() {
        if (this.hashValue == 0) {
            this.hashValue = this.physicalName.hashCode();
        }
        return this.hashValue;
    }

    public String toString() {
        return getQualifiedName();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(getPhysicalName());
        out.writeObject(this.options);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setPhysicalName(in.readUTF());
        this.options = (Map) in.readObject();
    }

    public String getDestinationTypeAsString() {
        switch (getDestinationType()) {
            case Zone.PRIMARY /*1*/:
                return "Queue";
            case Zone.SECONDARY /*2*/:
                return "Topic";
            case Service.RJE /*5*/:
                return "TempQueue";
            case Protocol.TCP /*6*/:
                return "TempTopic";
            default:
                throw new IllegalArgumentException("Invalid destination type: " + getDestinationType());
        }
    }

    public Map<String, String> getOptions() {
        return this.options;
    }

    public boolean isMarshallAware() {
        return false;
    }

    public void buildFromProperties(Properties properties) {
        if (properties == null) {
            properties = new Properties();
        }
        IntrospectionSupport.setProperties(this, properties);
    }

    public void populateProperties(Properties props) {
        props.setProperty("physicalName", getPhysicalName());
    }

    public boolean isPattern() {
        return this.isPattern;
    }

    public static UnresolvedDestinationTransformer getUnresolvableDestinationTransformer() {
        return unresolvableDestinationTransformer;
    }

    public static void setUnresolvableDestinationTransformer(UnresolvedDestinationTransformer unresolvableDestinationTransformer) {
        unresolvableDestinationTransformer = unresolvableDestinationTransformer;
    }
}
