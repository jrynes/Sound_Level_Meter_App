package org.apache.activemq.transport.discovery.zeroconf;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jmdns.JmDNS;

public final class JmDNSFactory {
    static Map<InetAddress, UsageTracker> registry;

    static class UsageTracker {
        AtomicInteger count;
        JmDNS jmDNS;

        UsageTracker() {
            this.count = new AtomicInteger(0);
        }
    }

    static {
        registry = new HashMap();
    }

    private JmDNSFactory() {
    }

    static synchronized JmDNS create(InetAddress address) throws IOException {
        JmDNS jmDNS;
        synchronized (JmDNSFactory.class) {
            UsageTracker tracker = (UsageTracker) registry.get(address);
            if (tracker == null) {
                tracker = new UsageTracker();
                tracker.jmDNS = JmDNS.create(address);
                registry.put(address, tracker);
            }
            tracker.count.incrementAndGet();
            jmDNS = tracker.jmDNS;
        }
        return jmDNS;
    }

    static synchronized boolean onClose(InetAddress address) {
        boolean z;
        synchronized (JmDNSFactory.class) {
            UsageTracker tracker = (UsageTracker) registry.get(address);
            if (tracker == null || tracker.count.decrementAndGet() != 0) {
                z = false;
            } else {
                registry.remove(address);
                z = true;
            }
        }
        return z;
    }
}
