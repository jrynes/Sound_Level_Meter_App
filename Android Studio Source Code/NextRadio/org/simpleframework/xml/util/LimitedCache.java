package org.simpleframework.xml.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LimitedCache<T> extends LinkedHashMap<Object, T> implements Cache<T> {
    private final int capacity;

    public LimitedCache() {
        this(50000);
    }

    public LimitedCache(int capacity) {
        this.capacity = capacity;
    }

    public void cache(Object key, T value) {
        put(key, value);
    }

    public T take(Object key) {
        return remove(key);
    }

    public T fetch(Object key) {
        return get(key);
    }

    public boolean contains(Object key) {
        return containsKey(key);
    }

    protected boolean removeEldestEntry(Entry<Object, T> entry) {
        return size() > this.capacity;
    }
}
