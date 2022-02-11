package org.xbill.DNS;

import org.apache.activemq.transport.stomp.Stomp.Headers.Connect;

public class AFSDBRecord extends U16NameBase {
    private static final long serialVersionUID = 3034379930729102437L;

    AFSDBRecord() {
    }

    Record getObject() {
        return new AFSDBRecord();
    }

    public AFSDBRecord(Name name, int dclass, long ttl, int subtype, Name host) {
        super(name, 18, dclass, ttl, subtype, "subtype", host, Connect.HOST);
    }

    public int getSubtype() {
        return getU16Field();
    }

    public Name getHost() {
        return getNameField();
    }
}
