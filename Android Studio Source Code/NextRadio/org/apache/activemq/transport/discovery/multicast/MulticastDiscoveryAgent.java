package org.apache.activemq.transport.discovery.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.DiscoveryEvent;
import org.apache.activemq.transport.discovery.DiscoveryAgent;
import org.apache.activemq.transport.discovery.DiscoveryListener;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.util.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MulticastDiscoveryAgent implements DiscoveryAgent, Runnable {
    private static final String ALIVE = "alive.";
    private static final int BUFF_SIZE = 8192;
    private static final String DEAD = "dead.";
    public static final String DEFAULT_DISCOVERY_URI_STRING = "multicast://239.255.2.3:6155";
    public static final String DEFAULT_HOST_IP;
    public static final String DEFAULT_HOST_STR = "default";
    private static final int DEFAULT_IDLE_TIME = 500;
    public static final int DEFAULT_PORT = 6155;
    private static final String DELIMITER = "%";
    private static final int HEARTBEAT_MISS_BEFORE_DEATH = 10;
    private static final Logger LOG;
    private static final String TYPE_SUFFIX = "ActiveMQ-4.";
    private long backOffMultiplier;
    private Map<String, RemoteBrokerData> brokersByService;
    private DiscoveryListener discoveryListener;
    private URI discoveryURI;
    private ExecutorService executor;
    private String group;
    private InetAddress inetAddress;
    private long initialReconnectDelay;
    private long keepAliveInterval;
    private long lastAdvertizeTime;
    private boolean loopBackMode;
    private int maxReconnectAttempts;
    private long maxReconnectDelay;
    private String mcInterface;
    private String mcJoinNetworkInterface;
    private String mcNetworkInterface;
    private MulticastSocket mcast;
    private boolean reportAdvertizeFailed;
    private Thread runner;
    private String selfService;
    private SocketAddress sockAddress;
    private AtomicBoolean started;
    private int timeToLive;
    private boolean useExponentialBackOff;

    class 1 implements Runnable {
        final /* synthetic */ RemoteBrokerData val$data;

        1(RemoteBrokerData remoteBrokerData) {
            this.val$data = remoteBrokerData;
        }

        public void run() {
            DiscoveryListener discoveryListener = MulticastDiscoveryAgent.this.discoveryListener;
            if (discoveryListener != null) {
                discoveryListener.onServiceRemove(this.val$data);
            }
        }
    }

    class 2 implements Runnable {
        final /* synthetic */ RemoteBrokerData val$data;

        2(RemoteBrokerData remoteBrokerData) {
            this.val$data = remoteBrokerData;
        }

        public void run() {
            DiscoveryListener discoveryListener = MulticastDiscoveryAgent.this.discoveryListener;
            if (discoveryListener != null) {
                discoveryListener.onServiceAdd(this.val$data);
            }
        }
    }

    class 3 implements ThreadFactory {
        final /* synthetic */ String val$threadName;

        3(String str) {
            this.val$threadName = str;
        }

        public Thread newThread(Runnable runable) {
            Thread t = new Thread(runable, this.val$threadName);
            t.setDaemon(true);
            return t;
        }
    }

    class RemoteBrokerData extends DiscoveryEvent {
        boolean failed;
        int failureCount;
        long lastHeartBeat;
        long recoveryTime;

        public RemoteBrokerData(String brokerName, String service) {
            super(service);
            setBrokerName(brokerName);
            this.lastHeartBeat = System.currentTimeMillis();
        }

        public synchronized void updateHeartBeat() {
            this.lastHeartBeat = System.currentTimeMillis();
            if (!this.failed && this.failureCount > 0 && this.lastHeartBeat - this.recoveryTime > 60000) {
                if (MulticastDiscoveryAgent.LOG.isDebugEnabled()) {
                    MulticastDiscoveryAgent.LOG.debug("I now think that the " + this.serviceName + " service has recovered.");
                }
                this.failureCount = 0;
                this.recoveryTime = 0;
            }
        }

        public synchronized long getLastHeartBeat() {
            return this.lastHeartBeat;
        }

        public synchronized boolean markFailed() {
            boolean z = true;
            synchronized (this) {
                if (this.failed) {
                    z = false;
                } else {
                    long reconnectDelay;
                    this.failed = true;
                    this.failureCount++;
                    if (MulticastDiscoveryAgent.this.useExponentialBackOff) {
                        reconnectDelay = (long) Math.pow((double) MulticastDiscoveryAgent.this.backOffMultiplier, (double) this.failureCount);
                        if (reconnectDelay > MulticastDiscoveryAgent.this.maxReconnectDelay) {
                            reconnectDelay = MulticastDiscoveryAgent.this.maxReconnectDelay;
                        }
                    } else {
                        reconnectDelay = MulticastDiscoveryAgent.this.initialReconnectDelay;
                    }
                    if (MulticastDiscoveryAgent.LOG.isDebugEnabled()) {
                        MulticastDiscoveryAgent.LOG.debug("Remote failure of " + this.serviceName + " while still receiving multicast advertisements.  Advertising events will be suppressed for " + reconnectDelay + " ms, the current failure count is: " + this.failureCount);
                    }
                    this.recoveryTime = System.currentTimeMillis() + reconnectDelay;
                }
            }
            return z;
        }

        public synchronized boolean doRecovery() {
            boolean z = false;
            synchronized (this) {
                if (this.failed) {
                    if (MulticastDiscoveryAgent.this.maxReconnectAttempts <= 0 || this.failureCount <= MulticastDiscoveryAgent.this.maxReconnectAttempts) {
                        if (System.currentTimeMillis() >= this.recoveryTime) {
                            if (MulticastDiscoveryAgent.LOG.isDebugEnabled()) {
                                MulticastDiscoveryAgent.LOG.debug("Resuming event advertisement of the " + this.serviceName + " service.");
                            }
                            this.failed = false;
                            z = true;
                        }
                    } else if (MulticastDiscoveryAgent.LOG.isDebugEnabled()) {
                        MulticastDiscoveryAgent.LOG.debug("Max reconnect attempts of the " + this.serviceName + " service has been reached.");
                    }
                }
            }
            return z;
        }

        public boolean isFailed() {
            return this.failed;
        }
    }

    public MulticastDiscoveryAgent() {
        this.initialReconnectDelay = 5000;
        this.maxReconnectDelay = 30000;
        this.backOffMultiplier = 2;
        this.timeToLive = 1;
        this.brokersByService = new ConcurrentHashMap();
        this.group = DEFAULT_HOST_STR;
        this.keepAliveInterval = 500;
        this.started = new AtomicBoolean(false);
        this.reportAdvertizeFailed = true;
        this.executor = null;
    }

    static {
        DEFAULT_HOST_IP = System.getProperty("activemq.partition.discovery", "239.255.2.3");
        LOG = LoggerFactory.getLogger(MulticastDiscoveryAgent.class);
    }

    public void setDiscoveryListener(DiscoveryListener listener) {
        this.discoveryListener = listener;
    }

    public void registerService(String name) throws IOException {
        this.selfService = name;
        if (this.started.get()) {
            doAdvertizeSelf();
        }
    }

    public boolean isLoopBackMode() {
        return this.loopBackMode;
    }

    public void setLoopBackMode(boolean loopBackMode) {
        this.loopBackMode = loopBackMode;
    }

    public int getTimeToLive() {
        return this.timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public URI getDiscoveryURI() {
        return this.discoveryURI;
    }

    public void setDiscoveryURI(URI discoveryURI) {
        this.discoveryURI = discoveryURI;
    }

    public long getKeepAliveInterval() {
        return this.keepAliveInterval;
    }

    public void setKeepAliveInterval(long keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public void setInterface(String mcInterface) {
        this.mcInterface = mcInterface;
    }

    public void setNetworkInterface(String mcNetworkInterface) {
        this.mcNetworkInterface = mcNetworkInterface;
    }

    public void setJoinNetworkInterface(String mcJoinNetwrokInterface) {
        this.mcJoinNetworkInterface = mcJoinNetwrokInterface;
    }

    public void start() throws Exception {
        if (!this.started.compareAndSet(false, true)) {
            return;
        }
        if (this.group == null || this.group.length() == 0) {
            throw new IOException("You must specify a group to discover");
        }
        String type = getType();
        if (!type.endsWith(ActiveMQDestination.PATH_SEPERATOR)) {
            LOG.warn("The type '" + type + "' should end with '.' to be a valid Discovery type");
            type = type + ActiveMQDestination.PATH_SEPERATOR;
        }
        if (this.discoveryURI == null) {
            this.discoveryURI = new URI(DEFAULT_DISCOVERY_URI_STRING);
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("start - discoveryURI = " + this.discoveryURI);
        }
        String myHost = this.discoveryURI.getHost();
        int myPort = this.discoveryURI.getPort();
        if (DEFAULT_HOST_STR.equals(myHost)) {
            myHost = DEFAULT_HOST_IP;
        }
        if (myPort < 0) {
            myPort = DEFAULT_PORT;
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("start - myHost = " + myHost);
            LOG.trace("start - myPort = " + myPort);
            LOG.trace("start - group  = " + this.group);
            LOG.trace("start - interface  = " + this.mcInterface);
            LOG.trace("start - network interface  = " + this.mcNetworkInterface);
            LOG.trace("start - join network interface  = " + this.mcJoinNetworkInterface);
        }
        this.inetAddress = InetAddress.getByName(myHost);
        this.sockAddress = new InetSocketAddress(this.inetAddress, myPort);
        this.mcast = new MulticastSocket(myPort);
        this.mcast.setLoopbackMode(this.loopBackMode);
        this.mcast.setTimeToLive(getTimeToLive());
        if (this.mcJoinNetworkInterface != null) {
            this.mcast.joinGroup(this.sockAddress, NetworkInterface.getByName(this.mcJoinNetworkInterface));
        } else {
            this.mcast.joinGroup(this.inetAddress);
        }
        this.mcast.setSoTimeout((int) this.keepAliveInterval);
        if (this.mcInterface != null) {
            this.mcast.setInterface(InetAddress.getByName(this.mcInterface));
        }
        if (this.mcNetworkInterface != null) {
            this.mcast.setNetworkInterface(NetworkInterface.getByName(this.mcNetworkInterface));
        }
        this.runner = new Thread(this);
        this.runner.setName(toString() + Headers.SEPERATOR + this.runner.getName());
        this.runner.setDaemon(true);
        this.runner.start();
        doAdvertizeSelf();
    }

    public void stop() throws Exception {
        if (this.started.compareAndSet(true, false)) {
            doAdvertizeSelf();
            if (this.mcast != null) {
                this.mcast.close();
            }
            if (this.runner != null) {
                this.runner.interrupt();
            }
            if (this.executor != null) {
                ThreadPoolUtils.shutdownNow(this.executor);
                this.executor = null;
            }
        }
    }

    public String getType() {
        return this.group + ActiveMQDestination.PATH_SEPERATOR + TYPE_SUFFIX;
    }

    public void run() {
        byte[] buf = new byte[BUFF_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
        while (this.started.get()) {
            doTimeKeepingServices();
            try {
                this.mcast.receive(packet);
                if (packet.getLength() > 0) {
                    processData(new String(packet.getData(), packet.getOffset(), packet.getLength()));
                }
            } catch (SocketTimeoutException e) {
            } catch (IOException e2) {
                if (this.started.get()) {
                    LOG.error("failed to process packet: " + e2);
                }
            }
        }
    }

    private void processData(String str) {
        if (this.discoveryListener != null && str.startsWith(getType())) {
            String payload = str.substring(getType().length());
            if (payload.startsWith(ALIVE)) {
                String brokerName = getBrokerName(payload.substring(ALIVE.length()));
                processAlive(brokerName, payload.substring((ALIVE.length() + brokerName.length()) + 2));
                return;
            }
            processDead(payload.substring((DEAD.length() + getBrokerName(payload.substring(DEAD.length())).length()) + 2));
        }
    }

    private void doTimeKeepingServices() {
        if (this.started.get()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime < this.lastAdvertizeTime || currentTime - this.keepAliveInterval > this.lastAdvertizeTime) {
                doAdvertizeSelf();
                this.lastAdvertizeTime = currentTime;
            }
            doExpireOldServices();
        }
    }

    private void doAdvertizeSelf() {
        if (this.selfService != null) {
            String payload = ((getType() + (this.started.get() ? ALIVE : DEAD)) + "%localhost%") + this.selfService;
            try {
                byte[] data = payload.getBytes();
                this.mcast.send(new DatagramPacket(data, 0, data.length, this.sockAddress));
            } catch (IOException e) {
                if (this.reportAdvertizeFailed) {
                    this.reportAdvertizeFailed = false;
                    LOG.error("Failed to advertise our service: " + payload, e);
                    if ("Operation not permitted".equals(e.getMessage())) {
                        LOG.error("The 'Operation not permitted' error has been know to be caused by improper firewall/network setup.  Please make sure that the OS is properly configured to allow multicast traffic over: " + this.mcast.getLocalAddress());
                    }
                }
            }
        }
    }

    private void processAlive(String brokerName, String service) {
        if (this.selfService == null || !service.equals(this.selfService)) {
            RemoteBrokerData data = (RemoteBrokerData) this.brokersByService.get(service);
            if (data == null) {
                data = new RemoteBrokerData(brokerName, service);
                this.brokersByService.put(service, data);
                fireServiceAddEvent(data);
                doAdvertizeSelf();
                return;
            }
            data.updateHeartBeat();
            if (data.doRecovery()) {
                fireServiceAddEvent(data);
            }
        }
    }

    private void processDead(String service) {
        if (!service.equals(this.selfService)) {
            RemoteBrokerData data = (RemoteBrokerData) this.brokersByService.remove(service);
            if (data != null && !data.isFailed()) {
                fireServiceRemovedEvent(data);
            }
        }
    }

    private void doExpireOldServices() {
        long expireTime = System.currentTimeMillis() - (this.keepAliveInterval * 10);
        for (RemoteBrokerData data : this.brokersByService.values()) {
            if (data.getLastHeartBeat() < expireTime) {
                processDead(data.getServiceName());
            }
        }
    }

    private String getBrokerName(String str) {
        int start = str.indexOf(DELIMITER);
        if (start < 0) {
            return null;
        }
        return str.substring(start + 1, str.indexOf(DELIMITER, start + 1));
    }

    public void serviceFailed(DiscoveryEvent event) throws IOException {
        RemoteBrokerData data = (RemoteBrokerData) this.brokersByService.get(event.getServiceName());
        if (data != null && data.markFailed()) {
            fireServiceRemovedEvent(data);
        }
    }

    private void fireServiceRemovedEvent(RemoteBrokerData data) {
        if (this.discoveryListener != null && this.started.get()) {
            getExecutor().execute(new 1(data));
        }
    }

    private void fireServiceAddEvent(RemoteBrokerData data) {
        if (this.discoveryListener != null && this.started.get()) {
            getExecutor().execute(new 2(data));
        }
    }

    private ExecutorService getExecutor() {
        if (this.executor == null) {
            int i = 1;
            this.executor = new ThreadPoolExecutor(1, i, 30, TimeUnit.SECONDS, new LinkedBlockingQueue(), new 3("Notifier-" + toString()));
        }
        return this.executor;
    }

    public long getBackOffMultiplier() {
        return this.backOffMultiplier;
    }

    public void setBackOffMultiplier(long backOffMultiplier) {
        this.backOffMultiplier = backOffMultiplier;
    }

    public long getInitialReconnectDelay() {
        return this.initialReconnectDelay;
    }

    public void setInitialReconnectDelay(long initialReconnectDelay) {
        this.initialReconnectDelay = initialReconnectDelay;
    }

    public int getMaxReconnectAttempts() {
        return this.maxReconnectAttempts;
    }

    public void setMaxReconnectAttempts(int maxReconnectAttempts) {
        this.maxReconnectAttempts = maxReconnectAttempts;
    }

    public long getMaxReconnectDelay() {
        return this.maxReconnectDelay;
    }

    public void setMaxReconnectDelay(long maxReconnectDelay) {
        this.maxReconnectDelay = maxReconnectDelay;
    }

    public boolean isUseExponentialBackOff() {
        return this.useExponentialBackOff;
    }

    public void setUseExponentialBackOff(boolean useExponentialBackOff) {
        this.useExponentialBackOff = useExponentialBackOff;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String toString() {
        return "MulticastDiscoveryAgent-" + (this.selfService != null ? "advertise:" + this.selfService : "listener:" + this.discoveryListener);
    }
}
