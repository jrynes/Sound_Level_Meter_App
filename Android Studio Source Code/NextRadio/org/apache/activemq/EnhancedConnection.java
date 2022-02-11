package org.apache.activemq;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.TopicConnection;
import org.apache.activemq.advisory.DestinationSource;

public interface EnhancedConnection extends TopicConnection, QueueConnection, Closeable {
    DestinationSource getDestinationSource() throws JMSException;
}
