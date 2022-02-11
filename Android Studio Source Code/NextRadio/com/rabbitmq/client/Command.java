package com.rabbitmq.client;

public interface Command {
    byte[] getContentBody();

    ContentHeader getContentHeader();

    Method getMethod();
}
