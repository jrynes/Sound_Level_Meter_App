package org.apache.activemq.broker.region;

import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.Message.MessageDestination;
import org.apache.activemq.command.MessageId;

public interface MessageReference {
    int decrementReferenceCount();

    long getExpiration();

    String getGroupID();

    int getGroupSequence();

    Message getMessage();

    Message getMessageHardRef();

    MessageId getMessageId();

    int getRedeliveryCounter();

    int getReferenceCount();

    MessageDestination getRegionDestination();

    int getSize();

    ConsumerId getTargetConsumerId();

    void incrementRedeliveryCounter();

    int incrementReferenceCount();

    boolean isAdvisory();

    boolean isDropped();

    boolean isExpired();

    boolean isPersistent();
}
