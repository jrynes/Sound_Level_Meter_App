package org.apache.activemq.command;

import org.apache.activemq.util.IntrospectionSupport;

public class JournalTrace implements DataStructure {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 53;
    private String message;

    public JournalTrace(String message) {
        this.message = message;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMarshallAware() {
        return false;
    }

    public String toString() {
        return IntrospectionSupport.toString(this, JournalTrace.class);
    }
}
