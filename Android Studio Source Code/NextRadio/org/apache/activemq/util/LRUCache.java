package org.apache.activemq.util;

import com.rabbitmq.client.impl.AMQConnection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = -342098639681884413L;
    protected int maxCacheSize;

    public LRUCache() {
        this(0, AMQConnection.HANDSHAKE_TIMEOUT, 0.75f, true);
    }

    public LRUCache(int maximumCacheSize) {
        this(0, maximumCacheSize, 0.75f, true);
    }

    public LRUCache(int initialCapacity, int maximumCacheSize, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxCacheSize = AMQConnection.HANDSHAKE_TIMEOUT;
        this.maxCacheSize = maximumCacheSize;
    }

    public int getMaxCacheSize() {
        return this.maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        if (size() <= this.maxCacheSize) {
            return false;
        }
        onCacheEviction(eldest);
        return true;
    }

    protected void onCacheEviction(Entry<K, V> entry) {
    }
}
