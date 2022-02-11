package org.apache.activemq;

import javax.jms.JMSException;

public class DestinationDoesNotExistException extends JMSException {
    public DestinationDoesNotExistException(String destination) {
        super(destination);
    }

    public boolean isTemporary() {
        return getMessage().startsWith("temp-");
    }

    public String getLocalizedMessage() {
        return "The destination " + getMessage() + " does not exist.";
    }
}
