package org.apache.activemq;

import javax.jms.MessageConsumer;

public interface MessageAvailableConsumer extends MessageConsumer {
    MessageAvailableListener getAvailableListener();

    void setAvailableListener(MessageAvailableListener messageAvailableListener);
}
