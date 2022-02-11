package org.apache.activemq;

import javax.jms.MessageConsumer;

public interface MessageAvailableListener {
    void onMessageAvailable(MessageConsumer messageConsumer);
}
