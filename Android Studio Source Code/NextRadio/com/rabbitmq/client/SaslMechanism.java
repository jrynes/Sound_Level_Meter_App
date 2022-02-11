package com.rabbitmq.client;

public interface SaslMechanism {
    String getName();

    LongString handleChallenge(LongString longString, String str, String str2);
}
