package com.rabbitmq.client;

import com.rabbitmq.client.impl.Version;
import java.net.ProtocolException;

public class ProtocolVersionMismatchException extends ProtocolException {
    private static final long serialVersionUID = 1;
    private Version clientVersion;
    private Version serverVersion;

    public ProtocolVersionMismatchException(Version clientVersion, Version serverVersion) {
        super("Protocol version mismatch: expected " + clientVersion + ", got " + serverVersion);
        this.clientVersion = clientVersion;
        this.serverVersion = serverVersion;
    }

    public Version getClientVersion() {
        return this.clientVersion;
    }

    public Version getServerVersion() {
        return this.serverVersion;
    }

    public int getClientMajor() {
        return this.clientVersion.getMajor();
    }

    public int getClientMinor() {
        return this.clientVersion.getMinor();
    }

    public int getServerMajor() {
        return this.serverVersion.getMajor();
    }

    public int getServerMinor() {
        return this.serverVersion.getMinor();
    }
}
