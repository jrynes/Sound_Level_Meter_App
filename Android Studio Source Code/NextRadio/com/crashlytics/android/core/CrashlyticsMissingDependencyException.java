package com.crashlytics.android.core;

import org.apache.activemq.transport.stomp.Stomp;

public class CrashlyticsMissingDependencyException extends RuntimeException {
    private static final long serialVersionUID = -1151536370019872859L;

    CrashlyticsMissingDependencyException(String message) {
        super(buildExceptionMessage(message));
    }

    private static String buildExceptionMessage(String message) {
        return Stomp.NEWLINE + message + Stomp.NEWLINE;
    }
}
