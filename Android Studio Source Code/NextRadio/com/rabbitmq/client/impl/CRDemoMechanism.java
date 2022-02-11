package com.rabbitmq.client.impl;

import com.rabbitmq.client.LongString;
import com.rabbitmq.client.SaslConfig;
import com.rabbitmq.client.SaslMechanism;
import java.util.Arrays;

public class CRDemoMechanism implements SaslMechanism {
    private static final String NAME = "RABBIT-CR-DEMO";
    private int round;

    public static class CRDemoSaslConfig implements SaslConfig {
        public SaslMechanism getSaslMechanism(String[] mechanisms) {
            if (Arrays.asList(mechanisms).contains(CRDemoMechanism.NAME)) {
                return new CRDemoMechanism();
            }
            return null;
        }
    }

    public CRDemoMechanism() {
        this.round = 0;
    }

    public String getName() {
        return NAME;
    }

    public LongString handleChallenge(LongString challenge, String username, String password) {
        this.round++;
        if (this.round == 1) {
            return LongStringHelper.asLongString(username);
        }
        return LongStringHelper.asLongString("My password is " + password);
    }
}
