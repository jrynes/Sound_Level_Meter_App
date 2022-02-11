package com.rabbitmq.client.impl;

import com.rabbitmq.client.BasicProperties;
import java.io.DataInputStream;
import java.io.IOException;

public abstract class AMQBasicProperties extends AMQContentHeader implements BasicProperties {
    protected AMQBasicProperties() {
    }

    protected AMQBasicProperties(DataInputStream in) throws IOException {
        super(in);
    }

    public Object clone() throws CloneNotSupportedException {
        return (AMQBasicProperties) super.clone();
    }
}
