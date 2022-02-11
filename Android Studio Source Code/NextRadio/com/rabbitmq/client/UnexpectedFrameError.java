package com.rabbitmq.client;

import com.rabbitmq.client.impl.Frame;

public class UnexpectedFrameError extends Error {
    private static final long serialVersionUID = 1;
    private final int _expectedFrameType;
    private final Frame _frame;

    public UnexpectedFrameError(Frame frame, int expectedFrameType) {
        super("Received frame: " + frame + ", expected type " + expectedFrameType);
        this._frame = frame;
        this._expectedFrameType = expectedFrameType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Frame getReceivedFrame() {
        return this._frame;
    }

    public int getExpectedFrameType() {
        return this._expectedFrameType;
    }
}
