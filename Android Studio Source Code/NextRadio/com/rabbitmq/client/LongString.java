package com.rabbitmq.client;

import java.io.DataInputStream;
import java.io.IOException;

public interface LongString {
    public static final long MAX_LENGTH = 4294967295L;

    byte[] getBytes();

    DataInputStream getStream() throws IOException;

    long length();
}
