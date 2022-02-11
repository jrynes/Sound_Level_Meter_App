package org.apache.activemq;

import javax.jms.JMSException;

public class ConfigurationException extends JMSException {
    private static final long serialVersionUID = 5639082552451065258L;

    public ConfigurationException(String description) {
        super(description, "AMQ-1002");
    }
}
