package org.apache.activemq.command;

public interface DataStructure {
    byte getDataStructureType();

    boolean isMarshallAware();
}
