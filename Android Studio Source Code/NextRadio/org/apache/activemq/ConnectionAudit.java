package org.apache.activemq;

import java.util.LinkedHashMap;
import org.apache.activemq.broker.region.MessageReference;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.Message;
import org.apache.activemq.util.LRUCache;
import org.xbill.DNS.KEYRecord.Flags;

class ConnectionAudit {
    private int auditDepth;
    private int auditMaximumProducerNumber;
    private boolean checkForDuplicates;
    private LinkedHashMap<ActiveMQDestination, ActiveMQMessageAudit> destinations;
    private LinkedHashMap<ActiveMQDispatcher, ActiveMQMessageAudit> dispatchers;

    ConnectionAudit() {
        this.destinations = new LRUCache(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH);
        this.dispatchers = new LRUCache(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH);
        this.auditDepth = Flags.FLAG4;
        this.auditMaximumProducerNumber = 64;
    }

    synchronized void removeDispatcher(ActiveMQDispatcher dispatcher) {
        this.dispatchers.remove(dispatcher);
    }

    synchronized boolean isDuplicate(ActiveMQDispatcher dispatcher, Message message) {
        boolean isDuplicate;
        if (this.checkForDuplicates && message != null) {
            ActiveMQDestination destination = message.getDestination();
            if (destination != null) {
                ActiveMQMessageAudit audit;
                if (destination.isQueue()) {
                    audit = (ActiveMQMessageAudit) this.destinations.get(destination);
                    if (audit == null) {
                        audit = new ActiveMQMessageAudit(this.auditDepth, this.auditMaximumProducerNumber);
                        this.destinations.put(destination, audit);
                    }
                    isDuplicate = audit.isDuplicate((MessageReference) message);
                } else {
                    audit = (ActiveMQMessageAudit) this.dispatchers.get(dispatcher);
                    if (audit == null) {
                        audit = new ActiveMQMessageAudit(this.auditDepth, this.auditMaximumProducerNumber);
                        this.dispatchers.put(dispatcher, audit);
                    }
                    isDuplicate = audit.isDuplicate((MessageReference) message);
                }
            }
        }
        isDuplicate = false;
        return isDuplicate;
    }

    protected synchronized void rollbackDuplicate(ActiveMQDispatcher dispatcher, Message message) {
        if (this.checkForDuplicates && message != null) {
            ActiveMQDestination destination = message.getDestination();
            if (destination != null) {
                ActiveMQMessageAudit audit;
                if (destination.isQueue()) {
                    audit = (ActiveMQMessageAudit) this.destinations.get(destination);
                    if (audit != null) {
                        audit.rollback((MessageReference) message);
                    }
                } else {
                    audit = (ActiveMQMessageAudit) this.dispatchers.get(dispatcher);
                    if (audit != null) {
                        audit.rollback((MessageReference) message);
                    }
                }
            }
        }
    }

    boolean isCheckForDuplicates() {
        return this.checkForDuplicates;
    }

    void setCheckForDuplicates(boolean checkForDuplicates) {
        this.checkForDuplicates = checkForDuplicates;
    }

    public int getAuditDepth() {
        return this.auditDepth;
    }

    public void setAuditDepth(int auditDepth) {
        this.auditDepth = auditDepth;
    }

    public int getAuditMaximumProducerNumber() {
        return this.auditMaximumProducerNumber;
    }

    public void setAuditMaximumProducerNumber(int auditMaximumProducerNumber) {
        this.auditMaximumProducerNumber = auditMaximumProducerNumber;
    }
}
