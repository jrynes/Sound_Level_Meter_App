package com.rabbitmq.client;

import org.apache.activemq.transport.stomp.Stomp.Headers;

public class Address {
    private final String _host;
    private final int _port;

    public Address(String host, int port) {
        this._host = host;
        this._port = port;
    }

    public Address(String host) {
        this._host = host;
        this._port = -1;
    }

    public String getHost() {
        return this._host;
    }

    public int getPort() {
        return this._port;
    }

    public static Address parseAddress(String addressString) {
        int idx = addressString.indexOf(58);
        return idx == -1 ? new Address(addressString) : new Address(addressString.substring(0, idx), Integer.parseInt(addressString.substring(idx + 1)));
    }

    public static Address[] parseAddresses(String addresses) {
        String[] addrs = addresses.split(" *, *");
        Address[] res = new Address[addrs.length];
        for (int i = 0; i < addrs.length; i++) {
            res[i] = parseAddress(addrs[i]);
        }
        return res;
    }

    public int hashCode() {
        return (this._host.hashCode() * 31) + this._port;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Address addr = (Address) obj;
        if (this._host.equals(addr._host) && this._port == addr._port) {
            return true;
        }
        return false;
    }

    public String toString() {
        return this._port == -1 ? this._host : this._host + Headers.SEPERATOR + this._port;
    }
}
