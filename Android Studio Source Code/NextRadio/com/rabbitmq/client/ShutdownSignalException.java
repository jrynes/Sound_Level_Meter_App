package com.rabbitmq.client;

import com.rabbitmq.utility.SensibleClone;

public class ShutdownSignalException extends RuntimeException implements SensibleClone<ShutdownSignalException> {
    private static final long serialVersionUID = 1;
    private final boolean _hardError;
    private final boolean _initiatedByApplication;
    private final Object _reason;
    private final Object _ref;

    public ShutdownSignalException(boolean hardError, boolean initiatedByApplication, Object reason, Object ref) {
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        if (initiatedByApplication) {
            str = "clean " + (hardError ? "connection" : "channel") + " shutdown";
        } else {
            str = (hardError ? "connection" : "channel") + " error";
        }
        super(stringBuilder.append(str).append("; reason: ").append(reason).toString());
        this._hardError = hardError;
        this._initiatedByApplication = initiatedByApplication;
        this._reason = reason;
        this._ref = ref;
    }

    public boolean isHardError() {
        return this._hardError;
    }

    public boolean isInitiatedByApplication() {
        return this._initiatedByApplication;
    }

    public Object getReason() {
        return this._reason;
    }

    public Object getReference() {
        return this._ref;
    }

    public ShutdownSignalException sensibleClone() {
        try {
            return (ShutdownSignalException) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
