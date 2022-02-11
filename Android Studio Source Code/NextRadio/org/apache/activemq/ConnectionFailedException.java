package org.apache.activemq;

import java.io.IOException;
import javax.jms.JMSException;

public class ConnectionFailedException extends JMSException {
    private static final long serialVersionUID = 2288453203492073973L;

    public ConnectionFailedException(IOException cause) {
        super("The JMS connection has failed: " + extractMessage(cause));
        initCause(cause);
        setLinkedException(cause);
    }

    public ConnectionFailedException() {
        super("The JMS connection has failed due to a Transport problem");
    }

    private static String extractMessage(IOException cause) {
        String m = cause.getMessage();
        if (m == null || m.length() == 0) {
            return cause.toString();
        }
        return m;
    }
}
