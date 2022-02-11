package org.apache.activemq.transport.discovery.zeroconf;

import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.DiscoveryEvent;
import org.apache.activemq.transport.discovery.DiscoveryAgent;
import org.apache.activemq.transport.discovery.DiscoveryListener;
import org.apache.activemq.transport.discovery.multicast.MulticastDiscoveryAgent;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.JMSExceptionSupport;
import org.apache.activemq.util.MapHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZeroconfDiscoveryAgent implements DiscoveryAgent, ServiceListener {
    private static final Logger LOG;
    private static final String TYPE_SUFFIX = "ActiveMQ-5.";
    private String group;
    private JmDNS jmdns;
    private DiscoveryListener listener;
    private InetAddress localAddress;
    private String localhost;
    private int priority;
    private final CopyOnWriteArrayList<ServiceInfo> serviceInfos;
    private int weight;

    class 1 extends Thread {
        final /* synthetic */ JmDNS val$closeTarget;

        1(JmDNS jmDNS) {
            this.val$closeTarget = jmDNS;
        }

        public void run() {
            try {
                if (JmDNSFactory.onClose(ZeroconfDiscoveryAgent.this.getLocalAddress())) {
                    this.val$closeTarget.close();
                }
            } catch (IOException e) {
                ZeroconfDiscoveryAgent.LOG.debug("Error closing JmDNS " + ZeroconfDiscoveryAgent.this.getLocalhost() + ". This exception will be ignored.", e);
            }
        }
    }

    public ZeroconfDiscoveryAgent() {
        this.group = MulticastDiscoveryAgent.DEFAULT_HOST_STR;
        this.serviceInfos = new CopyOnWriteArrayList();
    }

    static {
        LOG = LoggerFactory.getLogger(ZeroconfDiscoveryAgent.class);
    }

    public void start() throws Exception {
        if (this.group == null) {
            throw new IOException("You must specify a group to discover");
        }
        String type = getType();
        if (!type.endsWith(ActiveMQDestination.PATH_SEPERATOR)) {
            LOG.warn("The type '" + type + "' should end with '.' to be a valid Rendezvous type");
            type = type + ActiveMQDestination.PATH_SEPERATOR;
        }
        try {
            getJmdns();
            if (this.listener != null) {
                LOG.info("Discovering service of type: " + type);
                this.jmdns.addServiceListener(type, this);
            }
        } catch (Exception e) {
            JMSExceptionSupport.create("Failed to start JmDNS service: " + e, e);
        }
    }

    public void stop() {
        if (this.jmdns != null) {
            Iterator<ServiceInfo> iter = this.serviceInfos.iterator();
            while (iter.hasNext()) {
                this.jmdns.unregisterService((ServiceInfo) iter.next());
            }
            Thread thread = new 1(this.jmdns);
            thread.setDaemon(true);
            thread.start();
            this.jmdns = null;
        }
    }

    public void registerService(String name) throws IOException {
        ServiceInfo si = createServiceInfo(name, new HashMap());
        this.serviceInfos.add(si);
        getJmdns().registerService(si);
    }

    public void addService(JmDNS jmDNS, String type, String name) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("addService with type: " + type + " name: " + name);
        }
        if (this.listener != null) {
            this.listener.onServiceAdd(new DiscoveryEvent(name));
        }
        jmDNS.requestServiceInfo(type, name);
    }

    public void removeService(JmDNS jmDNS, String type, String name) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removeService with type: " + type + " name: " + name);
        }
        if (this.listener != null) {
            this.listener.onServiceRemove(new DiscoveryEvent(name));
        }
    }

    public void serviceAdded(ServiceEvent event) {
        addService(event.getDNS(), event.getType(), event.getName());
    }

    public void serviceRemoved(ServiceEvent event) {
        removeService(event.getDNS(), event.getType(), event.getName());
    }

    public void serviceResolved(ServiceEvent event) {
    }

    public void resolveService(JmDNS jmDNS, String type, String name, ServiceInfo serviceInfo) {
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public JmDNS getJmdns() throws IOException {
        if (this.jmdns == null) {
            this.jmdns = createJmDNS();
        }
        return this.jmdns;
    }

    public void setJmdns(JmDNS jmdns) {
        this.jmdns = jmdns;
    }

    public InetAddress getLocalAddress() throws UnknownHostException {
        if (this.localAddress == null) {
            this.localAddress = createLocalAddress();
        }
        return this.localAddress;
    }

    public void setLocalAddress(InetAddress localAddress) {
        this.localAddress = localAddress;
    }

    public String getLocalhost() {
        return this.localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    protected ServiceInfo createServiceInfo(String name, Map map) {
        int port = MapHelper.getInt(map, "port", 0);
        String type = getType();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Registering service type: " + type + " name: " + name + " details: " + map);
        }
        return ServiceInfo.create(type, name + ActiveMQDestination.PATH_SEPERATOR + type, port, this.weight, this.priority, Stomp.EMPTY);
    }

    protected JmDNS createJmDNS() throws IOException {
        return JmDNSFactory.create(getLocalAddress());
    }

    protected InetAddress createLocalAddress() throws UnknownHostException {
        if (this.localhost != null) {
            return InetAddress.getByName(this.localhost);
        }
        return InetAddress.getLocalHost();
    }

    public void setDiscoveryListener(DiscoveryListener listener) {
        this.listener = listener;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + this.group + ActiveMQDestination.PATH_SEPERATOR + TYPE_SUFFIX;
    }

    public void serviceFailed(DiscoveryEvent event) throws IOException {
    }
}
