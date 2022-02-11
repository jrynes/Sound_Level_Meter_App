package org.simpleframework.xml.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

public class WeakCache<T> implements Cache<T> {
    private SegmentList list;

    private class Segment extends WeakHashMap<Object, T> {
        private Segment() {
        }

        public synchronized void cache(Object key, T value) {
            put(key, value);
        }

        public synchronized T fetch(Object key) {
            return get(key);
        }

        public synchronized T take(Object key) {
            return remove(key);
        }

        public synchronized boolean contains(Object key) {
            return containsKey(key);
        }
    }

    private class SegmentList implements Iterable<Segment> {
        private List<Segment> list;
        private int size;

        public SegmentList(int size) {
            this.list = new ArrayList();
            this.size = size;
            create(size);
        }

        public Iterator<Segment> iterator() {
            return this.list.iterator();
        }

        public Segment get(Object key) {
            int segment = segment(key);
            if (segment < this.size) {
                return (Segment) this.list.get(segment);
            }
            return null;
        }

        private void create(int size) {
            int count = size;
            while (true) {
                int count2 = count - 1;
                if (count > 0) {
                    this.list.add(new Segment(null));
                    count = count2;
                } else {
                    return;
                }
            }
        }

        private int segment(Object key) {
            return Math.abs(key.hashCode() % this.size);
        }
    }

    public WeakCache() {
        this(10);
    }

    public WeakCache(int size) {
        this.list = new SegmentList(size);
    }

    public boolean isEmpty() {
        Iterator i$ = this.list.iterator();
        while (i$.hasNext()) {
            if (!((Segment) i$.next()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void cache(Object key, T value) {
        map(key).cache(key, value);
    }

    public T take(Object key) {
        return map(key).take(key);
    }

    public T fetch(Object key) {
        return map(key).fetch(key);
    }

    public boolean contains(Object key) {
        return map(key).contains(key);
    }

    private Segment map(Object key) {
        return this.list.get(key);
    }
}
