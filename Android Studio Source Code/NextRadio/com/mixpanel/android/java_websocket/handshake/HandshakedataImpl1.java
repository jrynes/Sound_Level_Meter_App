package com.mixpanel.android.java_websocket.handshake;

import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import org.apache.activemq.transport.stomp.Stomp;

public class HandshakedataImpl1 implements HandshakeBuilder {
    private byte[] content;
    private TreeMap<String, String> map;

    public HandshakedataImpl1() {
        this.map = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    }

    public Iterator<String> iterateHttpFields() {
        return Collections.unmodifiableSet(this.map.keySet()).iterator();
    }

    public String getFieldValue(String name) {
        String s = (String) this.map.get(name);
        if (s == null) {
            return Stomp.EMPTY;
        }
        return s;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void put(String name, String value) {
        this.map.put(name, value);
    }

    public boolean hasFieldValue(String name) {
        return this.map.containsKey(name);
    }
}
