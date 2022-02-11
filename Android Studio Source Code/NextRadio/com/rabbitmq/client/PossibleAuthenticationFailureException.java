package com.rabbitmq.client;

import java.io.IOException;

public class PossibleAuthenticationFailureException extends IOException {
    private static final long serialVersionUID = 1;

    public PossibleAuthenticationFailureException(Throwable cause) {
        super("Possibly caused by authentication failure");
        super.initCause(cause);
    }
}
