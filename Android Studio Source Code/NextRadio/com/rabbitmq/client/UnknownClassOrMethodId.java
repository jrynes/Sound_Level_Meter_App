package com.rabbitmq.client;

import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.filter.DestinationFilter;

public class UnknownClassOrMethodId extends IOException {
    private static final int NO_METHOD_ID = -1;
    private static final long serialVersionUID = 1;
    public final int classId;
    public final int methodId;

    public UnknownClassOrMethodId(int classId) {
        this(classId, NO_METHOD_ID);
    }

    public UnknownClassOrMethodId(int classId, int methodId) {
        this.classId = classId;
        this.methodId = methodId;
    }

    public String toString() {
        if (this.methodId == NO_METHOD_ID) {
            return super.toString() + "<" + this.classId + DestinationFilter.ANY_DESCENDENT;
        }
        return super.toString() + "<" + this.classId + ActiveMQDestination.PATH_SEPERATOR + this.methodId + DestinationFilter.ANY_DESCENDENT;
    }
}
