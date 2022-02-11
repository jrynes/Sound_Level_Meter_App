package org.apache.activemq;

import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.jms.ConnectionMetaData;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;

public final class ActiveMQConnectionMetaData implements ConnectionMetaData {
    public static final ActiveMQConnectionMetaData INSTANCE;
    public static final int PROVIDER_MAJOR_VERSION;
    public static final int PROVIDER_MINOR_VERSION;
    public static final String PROVIDER_VERSION;

    static {
        INSTANCE = new ActiveMQConnectionMetaData();
        String version = null;
        int major = 0;
        int minor = 0;
        try {
            Package p = Package.getPackage("org.apache.activemq");
            if (p != null) {
                version = p.getImplementationVersion();
                Matcher m = Pattern.compile("(\\d+)\\.(\\d+).*").matcher(version);
                if (m.matches()) {
                    major = Integer.parseInt(m.group(1));
                    minor = Integer.parseInt(m.group(2));
                }
            }
        } catch (Throwable th) {
        }
        PROVIDER_VERSION = version;
        PROVIDER_MAJOR_VERSION = major;
        PROVIDER_MINOR_VERSION = minor;
    }

    private ActiveMQConnectionMetaData() {
    }

    public String getJMSVersion() {
        return Stomp.V1_1;
    }

    public int getJMSMajorVersion() {
        return 1;
    }

    public int getJMSMinorVersion() {
        return 1;
    }

    public String getJMSProviderName() {
        return "ActiveMQ";
    }

    public String getProviderVersion() {
        return PROVIDER_VERSION;
    }

    public int getProviderMajorVersion() {
        return PROVIDER_MAJOR_VERSION;
    }

    public int getProviderMinorVersion() {
        return PROVIDER_MINOR_VERSION;
    }

    public Enumeration<String> getJMSXPropertyNames() {
        Vector<String> jmxProperties = new Vector();
        jmxProperties.add(Message.USERID);
        jmxProperties.add("JMSXGroupID");
        jmxProperties.add("JMSXGroupSeq");
        jmxProperties.add("JMSXDeliveryCount");
        jmxProperties.add("JMSXProducerTXID");
        return jmxProperties.elements();
    }
}
