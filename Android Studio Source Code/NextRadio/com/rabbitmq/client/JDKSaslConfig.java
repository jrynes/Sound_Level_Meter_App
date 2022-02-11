package com.rabbitmq.client;

import com.rabbitmq.client.impl.LongStringHelper;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

public class JDKSaslConfig implements SaslConfig {
    private static final String[] DEFAULT_PREFERRED_MECHANISMS;
    private final CallbackHandler callbackHandler;
    private final ConnectionFactory factory;
    private final List<String> mechanisms;

    private class JDKSaslMechanism implements SaslMechanism {
        private SaslClient client;

        public JDKSaslMechanism(SaslClient client) {
            this.client = client;
        }

        public String getName() {
            return this.client.getMechanismName();
        }

        public LongString handleChallenge(LongString challenge, String username, String password) {
            try {
                return LongStringHelper.asLongString(this.client.evaluateChallenge(challenge.getBytes()));
            } catch (SaslException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class UsernamePasswordCallbackHandler implements CallbackHandler {
        private ConnectionFactory factory;

        public UsernamePasswordCallbackHandler(ConnectionFactory factory) {
            this.factory = factory;
        }

        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    ((NameCallback) callback).setName(this.factory.getUsername());
                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(this.factory.getPassword().toCharArray());
                } else {
                    throw new UnsupportedCallbackException(callback, "Unrecognized Callback");
                }
            }
        }
    }

    static {
        DEFAULT_PREFERRED_MECHANISMS = new String[]{"PLAIN"};
    }

    public JDKSaslConfig(ConnectionFactory factory) {
        this(factory, DEFAULT_PREFERRED_MECHANISMS);
    }

    public JDKSaslConfig(ConnectionFactory factory, String[] mechanisms) {
        this.factory = factory;
        this.callbackHandler = new UsernamePasswordCallbackHandler(factory);
        this.mechanisms = Arrays.asList(mechanisms);
    }

    public SaslMechanism getSaslMechanism(String[] serverMechanisms) {
        Set<String> server = new HashSet(Arrays.asList(serverMechanisms));
        for (String mechanism : this.mechanisms) {
            if (server.contains(mechanism)) {
                try {
                    SaslClient saslClient = Sasl.createSaslClient(new String[]{(String) i$.next()}, null, "AMQP", this.factory.getHost(), null, this.callbackHandler);
                    if (saslClient != null) {
                        return new JDKSaslMechanism(saslClient);
                    }
                } catch (SaslException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}
