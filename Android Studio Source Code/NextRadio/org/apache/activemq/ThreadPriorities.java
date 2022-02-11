package org.apache.activemq;

public interface ThreadPriorities {
    public static final int BROKER_MANAGEMENT = 9;
    public static final int INBOUND_BROKER_CONNECTION = 6;
    public static final int INBOUND_CLIENT_CONNECTION = 7;
    public static final int INBOUND_CLIENT_SESSION = 7;
    public static final int OUT_BOUND_BROKER_DISPATCH = 6;
}
