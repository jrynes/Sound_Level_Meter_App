package org.apache.activemq.util;

import com.rabbitmq.client.ConnectionFactory;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdGenerator {
    private static final Logger LOG;
    private static final String UNIQUE_STUB;
    private static String hostName;
    private static int instanceCount;
    private int length;
    private String seed;
    private AtomicLong sequence;

    static {
        LOG = LoggerFactory.getLogger(IdGenerator.class);
        String stub = Stomp.EMPTY;
        boolean canAccessSystemProps = true;
        try {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPropertiesAccess();
            }
        } catch (SecurityException e) {
            canAccessSystemProps = false;
        }
        if (canAccessSystemProps) {
            try {
                hostName = InetAddressUtil.getLocalHostName();
                ServerSocket ss = new ServerSocket(0);
                stub = "-" + ss.getLocalPort() + "-" + System.currentTimeMillis() + "-";
                Thread.sleep(100);
                ss.close();
            } catch (Exception ioe) {
                LOG.warn("could not generate unique stub by using DNS and binding to local port", ioe);
            }
        }
        if (hostName == null) {
            hostName = ConnectionFactory.DEFAULT_HOST;
        }
        if (stub.length() == 0) {
            stub = "-1-" + System.currentTimeMillis() + "-";
        }
        UNIQUE_STUB = stub;
    }

    public IdGenerator(String prefix) {
        this.sequence = new AtomicLong(1);
        synchronized (UNIQUE_STUB) {
            StringBuilder append = new StringBuilder().append(prefix).append(UNIQUE_STUB);
            int i = instanceCount;
            instanceCount = i + 1;
            this.seed = append.append(i).append(Headers.SEPERATOR).toString();
            this.length = this.seed.length() + "9223372036854775807".length();
        }
    }

    public IdGenerator() {
        this(ActiveMQDestination.TEMP_DESTINATION_NAME_PREFIX + hostName);
    }

    public static String getHostName() {
        return hostName;
    }

    public synchronized String generateId() {
        StringBuilder sb;
        sb = new StringBuilder(this.length);
        sb.append(this.seed);
        sb.append(this.sequence.getAndIncrement());
        return sb.toString();
    }

    public String generateSanitizedId() {
        return generateId().replace(':', '-').replace('_', '-').replace('.', '-');
    }

    public static String getSeedFromId(String id) {
        String result = id;
        if (id == null) {
            return result;
        }
        int index = id.lastIndexOf(58);
        if (index <= 0 || index + 1 >= id.length()) {
            return result;
        }
        return id.substring(0, index);
    }

    public static long getSequenceFromId(String id) {
        if (id == null) {
            return -1;
        }
        int index = id.lastIndexOf(58);
        if (index <= 0 || index + 1 >= id.length()) {
            return -1;
        }
        return Long.parseLong(id.substring(index + 1, id.length()));
    }

    public static int compare(String id1, String id2) {
        String seed1 = getSeedFromId(id1);
        String seed2 = getSeedFromId(id2);
        if (seed1 == null || seed2 == null) {
            return -1;
        }
        int result = seed1.compareTo(seed2);
        if (result == 0) {
            return (int) (getSequenceFromId(id1) - getSequenceFromId(id2));
        }
        return result;
    }
}
