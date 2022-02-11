package org.apache.activemq.command;

public class ExceptionResponse extends Response {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 31;
    Throwable exception;

    public ExceptionResponse(Throwable e) {
        setException(e);
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public Throwable getException() {
        return this.exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public boolean isException() {
        return true;
    }
}
