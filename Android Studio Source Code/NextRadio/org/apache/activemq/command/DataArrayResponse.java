package org.apache.activemq.command;

public class DataArrayResponse extends Response {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 33;
    DataStructure[] data;

    public DataArrayResponse(DataStructure[] data) {
        this.data = data;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public DataStructure[] getData() {
        return this.data;
    }

    public void setData(DataStructure[] data) {
        this.data = data;
    }
}
