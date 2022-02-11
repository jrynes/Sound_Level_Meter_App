package org.apache.activemq.command;

public class BrokerId implements DataStructure {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 124;
    protected String value;

    public BrokerId(String brokerId) {
        this.value = brokerId;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != BrokerId.class) {
            return false;
        }
        return this.value.equals(((BrokerId) o).value);
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String toString() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String brokerId) {
        this.value = brokerId;
    }

    public boolean isMarshallAware() {
        return false;
    }
}
