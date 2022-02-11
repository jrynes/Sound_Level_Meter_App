package org.apache.activemq.transport.discovery.simple;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.command.DiscoveryEvent;
import org.apache.activemq.thread.TaskRunnerFactory;
import org.apache.activemq.transport.discovery.DiscoveryAgent;
import org.apache.activemq.transport.discovery.DiscoveryListener;
import org.apache.activemq.transport.stomp.Stomp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDiscoveryAgent implements DiscoveryAgent {
    private static final Logger LOG;
    private long backOffMultiplier;
    private long initialReconnectDelay;
    private DiscoveryListener listener;
    private int maxReconnectAttempts;
    private long maxReconnectDelay;
    private long minConnectTime;
    private final AtomicBoolean running;
    private String[] services;
    private final Object sleepMutex;
    private TaskRunnerFactory taskRunner;
    private boolean useExponentialBackOff;

    class 1 implements Runnable {
        final /* synthetic */ SimpleDiscoveryEvent val$sevent;

        1(SimpleDiscoveryEvent simpleDiscoveryEvent) {
            this.val$sevent = simpleDiscoveryEvent;
        }

        public void run() {
            SimpleDiscoveryEvent event = new SimpleDiscoveryEvent(this.val$sevent);
            if (event.connectTime + SimpleDiscoveryAgent.this.minConnectTime > System.currentTimeMillis()) {
                SimpleDiscoveryAgent.LOG.debug("Failure occurred soon after the discovery event was generated.  It will be classified as a connection failure: " + event);
                event.connectFailures = event.connectFailures + 1;
                if (SimpleDiscoveryAgent.this.maxReconnectAttempts <= 0 || event.connectFailures < SimpleDiscoveryAgent.this.maxReconnectAttempts) {
                    synchronized (SimpleDiscoveryAgent.this.sleepMutex) {
                        try {
                            if (SimpleDiscoveryAgent.this.running.get()) {
                                SimpleDiscoveryAgent.LOG.debug("Waiting " + event.reconnectDelay + " ms before attempting to reconnect.");
                                SimpleDiscoveryAgent.this.sleepMutex.wait(event.reconnectDelay);
                                if (SimpleDiscoveryAgent.this.useExponentialBackOff) {
                                    SimpleDiscoveryEvent.access$930(event, SimpleDiscoveryAgent.this.backOffMultiplier);
                                    if (event.reconnectDelay > SimpleDiscoveryAgent.this.maxReconnectDelay) {
                                        event.reconnectDelay = SimpleDiscoveryAgent.this.maxReconnectDelay;
                                    }
                                } else {
                                    event.reconnectDelay = SimpleDiscoveryAgent.this.initialReconnectDelay;
                                }
                            } else {
                                SimpleDiscoveryAgent.LOG.debug("Reconnecting disabled: stopped");
                                return;
                            }
                        } catch (InterruptedException ie) {
                            SimpleDiscoveryAgent.LOG.debug("Reconnecting disabled: " + ie);
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                } else {
                    SimpleDiscoveryAgent.LOG.warn("Reconnect attempts exceeded " + SimpleDiscoveryAgent.this.maxReconnectAttempts + " tries.  Reconnecting has been disabled for: " + event);
                    return;
                }
            }
            event.connectFailures = 0;
            event.reconnectDelay = SimpleDiscoveryAgent.this.initialReconnectDelay;
            if (SimpleDiscoveryAgent.this.running.get()) {
                event.connectTime = System.currentTimeMillis();
                event.failed.set(false);
                SimpleDiscoveryAgent.this.listener.onServiceAdd(event);
                return;
            }
            SimpleDiscoveryAgent.LOG.debug("Reconnecting disabled: stopped");
        }
    }

    class SimpleDiscoveryEvent extends DiscoveryEvent {
        private int connectFailures;
        private long connectTime;
        private AtomicBoolean failed;
        private long reconnectDelay;

        static /* synthetic */ long access$930(SimpleDiscoveryEvent x0, long x1) {
            long j = x0.reconnectDelay * x1;
            x0.reconnectDelay = j;
            return j;
        }

        public SimpleDiscoveryEvent(String service) {
            super(service);
            this.reconnectDelay = SimpleDiscoveryAgent.this.initialReconnectDelay;
            this.connectTime = System.currentTimeMillis();
            this.failed = new AtomicBoolean(false);
        }

        public SimpleDiscoveryEvent(SimpleDiscoveryEvent copy) {
            super((DiscoveryEvent) copy);
            this.reconnectDelay = SimpleDiscoveryAgent.this.initialReconnectDelay;
            this.connectTime = System.currentTimeMillis();
            this.failed = new AtomicBoolean(false);
            this.connectFailures = copy.connectFailures;
            this.reconnectDelay = copy.reconnectDelay;
            this.connectTime = copy.connectTime;
            this.failed.set(copy.failed.get());
        }

        public String toString() {
            return "[" + this.serviceName + ", failed:" + this.failed + ", connectionFailures:" + this.connectFailures + "]";
        }
    }

    public SimpleDiscoveryAgent() {
        this.initialReconnectDelay = 1000;
        this.maxReconnectDelay = 30000;
        this.backOffMultiplier = 2;
        this.useExponentialBackOff = true;
        this.sleepMutex = new Object();
        this.minConnectTime = 5000;
        this.services = new String[0];
        this.running = new AtomicBoolean(false);
    }

    static {
        LOG = LoggerFactory.getLogger(SimpleDiscoveryAgent.class);
    }

    public void setDiscoveryListener(DiscoveryListener listener) {
        this.listener = listener;
    }

    public void registerService(String name) throws IOException {
    }

    public void start() throws Exception {
        this.taskRunner = new TaskRunnerFactory();
        this.taskRunner.init();
        this.running.set(true);
        for (String simpleDiscoveryEvent : this.services) {
            this.listener.onServiceAdd(new SimpleDiscoveryEvent(simpleDiscoveryEvent));
        }
    }

    public void stop() throws Exception {
        this.running.set(false);
        this.taskRunner.shutdown();
        synchronized (this.sleepMutex) {
            this.sleepMutex.notifyAll();
        }
    }

    public String[] getServices() {
        return this.services;
    }

    public void setServices(String services) {
        this.services = services.split(Stomp.COMMA);
    }

    public void setServices(String[] services) {
        this.services = services;
    }

    public void setServices(URI[] services) {
        this.services = new String[services.length];
        for (int i = 0; i < services.length; i++) {
            this.services[i] = services[i].toString();
        }
    }

    public void serviceFailed(DiscoveryEvent devent) throws IOException {
        SimpleDiscoveryEvent sevent = (SimpleDiscoveryEvent) devent;
        if (sevent.failed.compareAndSet(false, true)) {
            this.listener.onServiceRemove(sevent);
            this.taskRunner.execute(new 1(sevent), "Simple Discovery Agent");
        }
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

    public long getMinConnectTime() {
        return this.minConnectTime;
    }

    public void setMinConnectTime(long minConnectTime) {
        this.minConnectTime = minConnectTime;
    }

    public boolean isUseExponentialBackOff() {
        return this.useExponentialBackOff;
    }

    public void setUseExponentialBackOff(boolean useExponentialBackOff) {
        this.useExponentialBackOff = useExponentialBackOff;
    }
}
