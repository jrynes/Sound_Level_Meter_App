package org.simpleframework.xml.util;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;

public class Dictionary<T extends Entry> extends AbstractSet<T> {
    protected final Table<T> map;

    private static class Table<T> extends HashMap<String, T> {
    }

    public Dictionary() {
        this.map = new Table();
    }

    public boolean add(T item) {
        return this.map.put(item.getName(), item) != null;
    }

    public int size() {
        return this.map.size();
    }

    public Iterator<T> iterator() {
        return this.map.values().iterator();
    }

    public T get(String name) {
        return (Entry) this.map.get(name);
    }

    public T remove(String name) {
        return (Entry) this.map.remove(name);
    }
}
