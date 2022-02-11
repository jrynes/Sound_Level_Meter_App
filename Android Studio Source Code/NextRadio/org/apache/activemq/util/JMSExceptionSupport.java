package org.apache.activemq.util;

import javax.jms.JMSException;
import javax.jms.MessageEOFException;
import javax.jms.MessageFormatException;

public final class JMSExceptionSupport {
    private JMSExceptionSupport() {
    }

    public static JMSException create(String msg, Throwable cause) {
        JMSException exception = new JMSException(msg);
        exception.initCause(cause);
        return exception;
    }

    public static JMSException create(String msg, Exception cause) {
        JMSException exception = new JMSException(msg);
        exception.setLinkedException(cause);
        exception.initCause(cause);
        return exception;
    }

    public static JMSException create(Throwable cause) {
        if (cause instanceof JMSException) {
            return (JMSException) cause;
        }
        String msg = cause.getMessage();
        if (msg == null || msg.length() == 0) {
            msg = cause.toString();
        }
        JMSException exception = new JMSException(msg);
        exception.initCause(cause);
        return exception;
    }

    public static JMSException create(Exception cause) {
        if (cause instanceof JMSException) {
            return (JMSException) cause;
        }
        String msg = cause.getMessage();
        if (msg == null || msg.length() == 0) {
            msg = cause.toString();
        }
        JMSException exception = new JMSException(msg);
        exception.setLinkedException(cause);
        exception.initCause(cause);
        return exception;
    }

    public static MessageEOFException createMessageEOFException(Exception cause) {
        String msg = cause.getMessage();
        if (msg == null || msg.length() == 0) {
            msg = cause.toString();
        }
        MessageEOFException exception = new MessageEOFException(msg);
        exception.setLinkedException(cause);
        exception.initCause(cause);
        return exception;
    }

    public static MessageFormatException createMessageFormatException(Exception cause) {
        String msg = cause.getMessage();
        if (msg == null || msg.length() == 0) {
            msg = cause.toString();
        }
        MessageFormatException exception = new MessageFormatException(msg);
        exception.setLinkedException(cause);
        exception.initCause(cause);
        return exception;
    }
}
