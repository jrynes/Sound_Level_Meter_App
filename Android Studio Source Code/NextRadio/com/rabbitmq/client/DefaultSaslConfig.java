package com.rabbitmq.client;

import com.rabbitmq.client.impl.ExternalMechanism;
import com.rabbitmq.client.impl.PlainMechanism;
import java.util.Arrays;
import java.util.HashSet;

public class DefaultSaslConfig implements SaslConfig {
    public static final DefaultSaslConfig EXTERNAL;
    public static final DefaultSaslConfig PLAIN;
    private final String mechanism;

    static {
        PLAIN = new DefaultSaslConfig("PLAIN");
        EXTERNAL = new DefaultSaslConfig("EXTERNAL");
    }

    private DefaultSaslConfig(String mechanism) {
        this.mechanism = mechanism;
    }

    public SaslMechanism getSaslMechanism(String[] serverMechanisms) {
        if (new HashSet(Arrays.asList(serverMechanisms)).contains(this.mechanism)) {
            if (this.mechanism.equals("PLAIN")) {
                return new PlainMechanism();
            }
            if (this.mechanism.equals("EXTERNAL")) {
                return new ExternalMechanism();
            }
        }
        return null;
    }
}
