package org.apache.activemq;

import org.apache.activemq.command.MessageId;

public class ActiveMQMessageAudit extends ActiveMQMessageAuditNoSync {
    private static final long serialVersionUID = 1;

    public ActiveMQMessageAudit(int auditDepth, int maximumNumberOfProducersToTrack) {
        super(auditDepth, maximumNumberOfProducersToTrack);
    }

    public boolean isDuplicate(String id) {
        boolean isDuplicate;
        synchronized (this) {
            isDuplicate = super.isDuplicate(id);
        }
        return isDuplicate;
    }

    public boolean isDuplicate(MessageId id) {
        boolean isDuplicate;
        synchronized (this) {
            isDuplicate = super.isDuplicate(id);
        }
        return isDuplicate;
    }

    public void rollback(MessageId id) {
        synchronized (this) {
            super.rollback(id);
        }
    }

    public boolean isInOrder(String id) {
        boolean isInOrder;
        synchronized (this) {
            isInOrder = super.isInOrder(id);
        }
        return isInOrder;
    }

    public boolean isInOrder(MessageId id) {
        boolean isInOrder;
        synchronized (this) {
            isInOrder = super.isInOrder(id);
        }
        return isInOrder;
    }

    public void setMaximumNumberOfProducersToTrack(int maximumNumberOfProducersToTrack) {
        synchronized (this) {
            super.setMaximumNumberOfProducersToTrack(maximumNumberOfProducersToTrack);
        }
    }
}
