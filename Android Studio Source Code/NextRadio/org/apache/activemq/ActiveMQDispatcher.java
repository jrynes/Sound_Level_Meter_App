package org.apache.activemq;

import org.apache.activemq.command.MessageDispatch;

public interface ActiveMQDispatcher {
    void dispatch(MessageDispatch messageDispatch);
}
