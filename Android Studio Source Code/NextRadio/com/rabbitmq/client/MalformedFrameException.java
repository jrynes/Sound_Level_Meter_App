package com.rabbitmq.client;

import java.io.IOException;

public class MalformedFrameException extends IOException {
    private static final long serialVersionUID = 1;

    public MalformedFrameException(String reason) {
        super(reason);
    }
}
