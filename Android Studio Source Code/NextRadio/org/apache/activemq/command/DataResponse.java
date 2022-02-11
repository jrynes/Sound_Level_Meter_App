package org.apache.activemq.command;

public class DataResponse extends Response {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 32;
    DataStructure data;

    public DataResponse(DataStructure data) {
        this.data = data;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public DataStructure getData() {
        return this.data;
    }

    public void setData(DataStructure data) {
        this.data = data;
    }
}
