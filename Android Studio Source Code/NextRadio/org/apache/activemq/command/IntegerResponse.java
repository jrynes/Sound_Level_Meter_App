package org.apache.activemq.command;

public class IntegerResponse extends Response {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 34;
    int result;

    public IntegerResponse(int result) {
        this.result = result;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
