package com.rabbitmq.client;

public interface SaslConfig {
    SaslMechanism getSaslMechanism(String[] strArr);
}
