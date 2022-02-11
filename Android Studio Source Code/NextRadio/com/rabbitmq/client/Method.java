package com.rabbitmq.client;

public interface Method {
    int protocolClassId();

    int protocolMethodId();

    String protocolMethodName();
}
