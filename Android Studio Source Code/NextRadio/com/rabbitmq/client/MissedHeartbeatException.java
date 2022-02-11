package com.rabbitmq.client;

import java.net.SocketTimeoutException;

public class MissedHeartbeatException extends SocketTimeoutException {
    private static final long serialVersionUID = 1;

    public MissedHeartbeatException(String reason) {
        super(reason);
    }
}
