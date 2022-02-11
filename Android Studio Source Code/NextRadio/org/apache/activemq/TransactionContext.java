package org.apache.activemq;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.TransactionInProgressException;
import javax.jms.TransactionRolledBackException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.DataArrayResponse;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.LocalTransactionId;
import org.apache.activemq.command.Response;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.command.TransactionInfo;
import org.apache.activemq.command.XATransactionId;
import org.apache.activemq.transaction.Synchronization;
import org.apache.activemq.util.JMSExceptionSupport;
import org.apache.activemq.util.LongSequenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionContext implements XAResource {
    private static final HashMap<TransactionId, List<TransactionContext>> ENDED_XA_TRANSACTION_CONTEXTS;
    private static final Logger LOG;
    private Xid associatedXid;
    private int beforeEndIndex;
    private final ActiveMQConnection connection;
    private final ConnectionId connectionId;
    private LocalTransactionEventListener localTransactionEventListener;
    private final LongSequenceGenerator localTransactionIdGenerator;
    private List<Synchronization> synchronizations;
    private TransactionId transactionId;

    static {
        LOG = LoggerFactory.getLogger(TransactionContext.class);
        ENDED_XA_TRANSACTION_CONTEXTS = new HashMap();
    }

    public TransactionContext(ActiveMQConnection connection) {
        this.connection = connection;
        this.localTransactionIdGenerator = connection.getLocalTransactionIdGenerator();
        this.connectionId = connection.getConnectionInfo().getConnectionId();
    }

    public boolean isInXATransaction() {
        if (this.transactionId != null && this.transactionId.isXATransaction()) {
            return true;
        }
        if (!ENDED_XA_TRANSACTION_CONTEXTS.isEmpty()) {
            synchronized (ENDED_XA_TRANSACTION_CONTEXTS) {
                for (List<TransactionContext> transactions : ENDED_XA_TRANSACTION_CONTEXTS.values()) {
                    if (transactions.contains(this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInLocalTransaction() {
        return this.transactionId != null && this.transactionId.isLocalTransaction();
    }

    public boolean isInTransaction() {
        return this.transactionId != null;
    }

    public LocalTransactionEventListener getLocalTransactionEventListener() {
        return this.localTransactionEventListener;
    }

    public void setLocalTransactionEventListener(LocalTransactionEventListener localTransactionEventListener) {
        this.localTransactionEventListener = localTransactionEventListener;
    }

    public void addSynchronization(Synchronization s) {
        if (this.synchronizations == null) {
            this.synchronizations = new ArrayList(10);
        }
        this.synchronizations.add(s);
    }

    private void afterRollback() throws JMSException {
        if (this.synchronizations != null) {
            Throwable firstException = null;
            int size = this.synchronizations.size();
            for (int i = 0; i < size; i++) {
                try {
                    ((Synchronization) this.synchronizations.get(i)).afterRollback();
                } catch (Throwable t) {
                    LOG.debug("Exception from afterRollback on " + this.synchronizations.get(i), t);
                    if (firstException == null) {
                        firstException = t;
                    }
                }
            }
            this.synchronizations = null;
            if (firstException != null) {
                throw JMSExceptionSupport.create(firstException);
            }
        }
    }

    private void afterCommit() throws JMSException {
        if (this.synchronizations != null) {
            Throwable firstException = null;
            int size = this.synchronizations.size();
            for (int i = 0; i < size; i++) {
                try {
                    ((Synchronization) this.synchronizations.get(i)).afterCommit();
                } catch (Throwable t) {
                    LOG.debug("Exception from afterCommit on " + this.synchronizations.get(i), t);
                    if (firstException == null) {
                        firstException = t;
                    }
                }
            }
            this.synchronizations = null;
            if (firstException != null) {
                throw JMSExceptionSupport.create(firstException);
            }
        }
    }

    private void beforeEnd() throws JMSException {
        if (this.synchronizations != null) {
            int size = this.synchronizations.size();
            while (this.beforeEndIndex < size) {
                try {
                    List list = this.synchronizations;
                    int i = this.beforeEndIndex;
                    this.beforeEndIndex = i + 1;
                    ((Synchronization) list.get(i)).beforeEnd();
                } catch (JMSException e) {
                    throw e;
                } catch (Throwable e2) {
                    JMSException create = JMSExceptionSupport.create(e2);
                }
            }
        }
    }

    public TransactionId getTransactionId() {
        return this.transactionId;
    }

    public void begin() throws JMSException {
        if (isInXATransaction()) {
            throw new TransactionInProgressException("Cannot start local transaction.  XA transaction is already in progress.");
        } else if (this.transactionId == null) {
            this.synchronizations = null;
            this.beforeEndIndex = 0;
            this.transactionId = new LocalTransactionId(this.connectionId, this.localTransactionIdGenerator.getNextSequenceId());
            TransactionInfo info = new TransactionInfo(getConnectionId(), this.transactionId, (byte) 0);
            this.connection.ensureConnectionInfoSent();
            this.connection.asyncSendPacket(info);
            if (this.localTransactionEventListener != null) {
                this.localTransactionEventListener.beginEvent();
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Begin:" + this.transactionId);
            }
        }
    }

    public void rollback() throws JMSException {
        if (isInXATransaction()) {
            throw new TransactionInProgressException("Cannot rollback() if an XA transaction is already in progress ");
        }
        try {
            beforeEnd();
        } catch (TransactionRolledBackException canOcurrOnFailover) {
            LOG.warn("rollback processing error", canOcurrOnFailover);
        }
        if (this.transactionId != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Rollback: " + this.transactionId + " syncCount: " + (this.synchronizations != null ? this.synchronizations.size() : 0));
            }
            TransactionInfo info = new TransactionInfo(getConnectionId(), this.transactionId, (byte) 4);
            this.transactionId = null;
            this.connection.syncSendPacket(info);
            if (this.localTransactionEventListener != null) {
                this.localTransactionEventListener.rollbackEvent();
            }
        }
        afterRollback();
    }

    public void commit() throws JMSException {
        if (isInXATransaction()) {
            throw new TransactionInProgressException("Cannot commit() if an XA transaction is already in progress ");
        }
        try {
            beforeEnd();
            if (this.transactionId != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Commit: " + this.transactionId + " syncCount: " + (this.synchronizations != null ? this.synchronizations.size() : 0));
                }
                TransactionInfo info = new TransactionInfo(getConnectionId(), this.transactionId, (byte) 2);
                this.transactionId = null;
                try {
                    syncSendPacketWithInterruptionHandling(info);
                    if (this.localTransactionEventListener != null) {
                        this.localTransactionEventListener.commitEvent();
                    }
                    afterCommit();
                } catch (JMSException cause) {
                    LOG.info("commit failed for transaction " + info.getTransactionId(), cause);
                    if (this.localTransactionEventListener != null) {
                        this.localTransactionEventListener.rollbackEvent();
                    }
                    afterRollback();
                    throw cause;
                }
            }
        } catch (JMSException e) {
            rollback();
            throw e;
        }
    }

    public void start(Xid xid, int flags) throws XAException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start: " + xid);
        }
        if (isInLocalTransaction()) {
            throw new XAException(-6);
        } else if (this.associatedXid != null) {
            throw new XAException(-6);
        } else {
            this.synchronizations = null;
            this.beforeEndIndex = 0;
            setXid(xid);
        }
    }

    private ConnectionId getConnectionId() {
        return this.connection.getConnectionInfo().getConnectionId();
    }

    public void end(Xid xid, int flags) throws XAException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("End: " + xid);
        }
        if (isInLocalTransaction()) {
            throw new XAException(-6);
        } else if ((570425344 & flags) != 0) {
            if (equals(this.associatedXid, xid)) {
                try {
                    beforeEnd();
                    setXid(null);
                    return;
                } catch (JMSException e) {
                    throw toXAException(e);
                }
            }
            throw new XAException(-6);
        } else if ((flags & 67108864) != 67108864) {
            throw new XAException(-5);
        } else if (equals(this.associatedXid, xid)) {
            try {
                beforeEnd();
                setXid(null);
            } catch (JMSException e2) {
                throw toXAException(e2);
            }
        }
    }

    private boolean equals(Xid xid1, Xid xid2) {
        if (xid1 == xid2) {
            return true;
        }
        int i;
        if (xid1 == null) {
            i = 1;
        } else {
            i = 0;
        }
        if (((xid2 == null ? 1 : 0) ^ i) != 0) {
            return false;
        }
        if (xid1.getFormatId() == xid2.getFormatId() && Arrays.equals(xid1.getBranchQualifier(), xid2.getBranchQualifier()) && Arrays.equals(xid1.getGlobalTransactionId(), xid2.getGlobalTransactionId())) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int prepare(javax.transaction.xa.Xid r13) throws javax.transaction.xa.XAException {
        /*
        r12 = this;
        r8 = LOG;
        r8 = r8.isDebugEnabled();
        if (r8 == 0) goto L_0x0020;
    L_0x0008:
        r8 = LOG;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Prepare: ";
        r9 = r9.append(r10);
        r9 = r9.append(r13);
        r9 = r9.toString();
        r8.debug(r9);
    L_0x0020:
        if (r13 == 0) goto L_0x002a;
    L_0x0022:
        r8 = r12.associatedXid;
        r8 = r12.equals(r8, r13);
        if (r8 == 0) goto L_0x0031;
    L_0x002a:
        r8 = new javax.transaction.xa.XAException;
        r9 = -6;
        r8.<init>(r9);
        throw r8;
    L_0x0031:
        r7 = new org.apache.activemq.command.XATransactionId;
        r7.<init>(r13);
        r4 = new org.apache.activemq.command.TransactionInfo;	 Catch:{ JMSException -> 0x0097 }
        r8 = r12.getConnectionId();	 Catch:{ JMSException -> 0x0097 }
        r9 = 1;
        r4.<init>(r8, r7, r9);	 Catch:{ JMSException -> 0x0097 }
        r6 = r12.syncSendPacketWithInterruptionHandling(r4);	 Catch:{ JMSException -> 0x0097 }
        r6 = (org.apache.activemq.command.IntegerResponse) r6;	 Catch:{ JMSException -> 0x0097 }
        r8 = 3;
        r9 = r6.getResult();	 Catch:{ JMSException -> 0x0097 }
        if (r8 != r9) goto L_0x0111;
    L_0x004d:
        r9 = ENDED_XA_TRANSACTION_CONTEXTS;	 Catch:{ JMSException -> 0x0097 }
        monitor-enter(r9);	 Catch:{ JMSException -> 0x0097 }
        r8 = ENDED_XA_TRANSACTION_CONTEXTS;	 Catch:{ all -> 0x0094 }
        r5 = r8.remove(r7);	 Catch:{ all -> 0x0094 }
        r5 = (java.util.List) r5;	 Catch:{ all -> 0x0094 }
        if (r5 == 0) goto L_0x0110;
    L_0x005a:
        r8 = r5.isEmpty();	 Catch:{ all -> 0x0094 }
        if (r8 != 0) goto L_0x0110;
    L_0x0060:
        r8 = LOG;	 Catch:{ all -> 0x0094 }
        r8 = r8.isDebugEnabled();	 Catch:{ all -> 0x0094 }
        if (r8 == 0) goto L_0x0080;
    L_0x0068:
        r8 = LOG;	 Catch:{ all -> 0x0094 }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0094 }
        r10.<init>();	 Catch:{ all -> 0x0094 }
        r11 = "firing afterCommit callbacks on XA_RDONLY from prepare: ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x0094 }
        r10 = r10.append(r13);	 Catch:{ all -> 0x0094 }
        r10 = r10.toString();	 Catch:{ all -> 0x0094 }
        r8.debug(r10);	 Catch:{ all -> 0x0094 }
    L_0x0080:
        r2 = r5.iterator();	 Catch:{ all -> 0x0094 }
    L_0x0084:
        r8 = r2.hasNext();	 Catch:{ all -> 0x0094 }
        if (r8 == 0) goto L_0x0110;
    L_0x008a:
        r0 = r2.next();	 Catch:{ all -> 0x0094 }
        r0 = (org.apache.activemq.TransactionContext) r0;	 Catch:{ all -> 0x0094 }
        r0.afterCommit();	 Catch:{ all -> 0x0094 }
        goto L_0x0084;
    L_0x0094:
        r8 = move-exception;
        monitor-exit(r9);	 Catch:{ all -> 0x0094 }
        throw r8;	 Catch:{ JMSException -> 0x0097 }
    L_0x0097:
        r1 = move-exception;
        r8 = LOG;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "prepare of: ";
        r9 = r9.append(r10);
        r9 = r9.append(r7);
        r10 = " failed with: ";
        r9 = r9.append(r10);
        r9 = r9.append(r1);
        r9 = r9.toString();
        r8.warn(r9, r1);
        r9 = ENDED_XA_TRANSACTION_CONTEXTS;
        monitor-enter(r9);
        r8 = ENDED_XA_TRANSACTION_CONTEXTS;	 Catch:{ all -> 0x010d }
        r5 = r8.remove(r7);	 Catch:{ all -> 0x010d }
        r5 = (java.util.List) r5;	 Catch:{ all -> 0x010d }
        if (r5 == 0) goto L_0x0116;
    L_0x00c7:
        r8 = r5.isEmpty();	 Catch:{ all -> 0x010d }
        if (r8 != 0) goto L_0x0116;
    L_0x00cd:
        r2 = r5.iterator();	 Catch:{ all -> 0x010d }
    L_0x00d1:
        r8 = r2.hasNext();	 Catch:{ all -> 0x010d }
        if (r8 == 0) goto L_0x0116;
    L_0x00d7:
        r0 = r2.next();	 Catch:{ all -> 0x010d }
        r0 = (org.apache.activemq.TransactionContext) r0;	 Catch:{ all -> 0x010d }
        r0.afterRollback();	 Catch:{ Throwable -> 0x00e1 }
        goto L_0x00d1;
    L_0x00e1:
        r3 = move-exception;
        r8 = LOG;	 Catch:{ all -> 0x010d }
        r8 = r8.isDebugEnabled();	 Catch:{ all -> 0x010d }
        if (r8 == 0) goto L_0x00d1;
    L_0x00ea:
        r8 = LOG;	 Catch:{ all -> 0x010d }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x010d }
        r10.<init>();	 Catch:{ all -> 0x010d }
        r11 = "failed to firing afterRollback callbacks on prepare failure, txid: ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x010d }
        r10 = r10.append(r7);	 Catch:{ all -> 0x010d }
        r11 = ", context: ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x010d }
        r10 = r10.append(r0);	 Catch:{ all -> 0x010d }
        r10 = r10.toString();	 Catch:{ all -> 0x010d }
        r8.debug(r10, r3);	 Catch:{ all -> 0x010d }
        goto L_0x00d1;
    L_0x010d:
        r8 = move-exception;
        monitor-exit(r9);	 Catch:{ all -> 0x010d }
        throw r8;
    L_0x0110:
        monitor-exit(r9);	 Catch:{ all -> 0x0094 }
    L_0x0111:
        r8 = r6.getResult();	 Catch:{ JMSException -> 0x0097 }
        return r8;
    L_0x0116:
        monitor-exit(r9);	 Catch:{ all -> 0x010d }
        r8 = r12.toXAException(r1);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.TransactionContext.prepare(javax.transaction.xa.Xid):int");
    }

    public void rollback(Xid xid) throws XAException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Rollback: " + xid);
        }
        if (xid == null) {
            throw new XAException(-6);
        }
        XATransactionId x;
        if (equals(this.associatedXid, xid)) {
            x = this.transactionId;
        } else {
            x = new XATransactionId(xid);
        }
        try {
            this.connection.checkClosedOrFailed();
            this.connection.ensureConnectionInfoSent();
            syncSendPacketWithInterruptionHandling(new TransactionInfo(getConnectionId(), x, (byte) 4));
            synchronized (ENDED_XA_TRANSACTION_CONTEXTS) {
                List<TransactionContext> l = (List) ENDED_XA_TRANSACTION_CONTEXTS.remove(x);
                if (!(l == null || l.isEmpty())) {
                    for (TransactionContext ctx : l) {
                        ctx.afterRollback();
                    }
                }
            }
        } catch (JMSException e) {
            throw toXAException(e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void commit(javax.transaction.xa.Xid r12, boolean r13) throws javax.transaction.xa.XAException {
        /*
        r11 = this;
        r7 = LOG;
        r7 = r7.isDebugEnabled();
        if (r7 == 0) goto L_0x002a;
    L_0x0008:
        r7 = LOG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "Commit: ";
        r8 = r8.append(r9);
        r8 = r8.append(r12);
        r9 = ", onePhase=";
        r8 = r8.append(r9);
        r8 = r8.append(r13);
        r8 = r8.toString();
        r7.debug(r8);
    L_0x002a:
        if (r12 == 0) goto L_0x0034;
    L_0x002c:
        r7 = r11.associatedXid;
        r7 = r11.equals(r7, r12);
        if (r7 == 0) goto L_0x003b;
    L_0x0034:
        r7 = new javax.transaction.xa.XAException;
        r8 = -6;
        r7.<init>(r8);
        throw r7;
    L_0x003b:
        r6 = new org.apache.activemq.command.XATransactionId;
        r6.<init>(r12);
        r7 = r11.connection;	 Catch:{ JMSException -> 0x009d }
        r7.checkClosedOrFailed();	 Catch:{ JMSException -> 0x009d }
        r7 = r11.connection;	 Catch:{ JMSException -> 0x009d }
        r7.ensureConnectionInfoSent();	 Catch:{ JMSException -> 0x009d }
        r4 = new org.apache.activemq.command.TransactionInfo;	 Catch:{ JMSException -> 0x009d }
        r8 = r11.getConnectionId();	 Catch:{ JMSException -> 0x009d }
        if (r13 == 0) goto L_0x0118;
    L_0x0052:
        r7 = 2;
    L_0x0053:
        r4.<init>(r8, r6, r7);	 Catch:{ JMSException -> 0x009d }
        r11.syncSendPacketWithInterruptionHandling(r4);	 Catch:{ JMSException -> 0x009d }
        r8 = ENDED_XA_TRANSACTION_CONTEXTS;	 Catch:{ JMSException -> 0x009d }
        monitor-enter(r8);	 Catch:{ JMSException -> 0x009d }
        r7 = ENDED_XA_TRANSACTION_CONTEXTS;	 Catch:{ all -> 0x009a }
        r5 = r7.remove(r6);	 Catch:{ all -> 0x009a }
        r5 = (java.util.List) r5;	 Catch:{ all -> 0x009a }
        if (r5 == 0) goto L_0x011b;
    L_0x0066:
        r7 = r5.isEmpty();	 Catch:{ all -> 0x009a }
        if (r7 != 0) goto L_0x011b;
    L_0x006c:
        r2 = r5.iterator();	 Catch:{ all -> 0x009a }
    L_0x0070:
        r7 = r2.hasNext();	 Catch:{ all -> 0x009a }
        if (r7 == 0) goto L_0x011b;
    L_0x0076:
        r0 = r2.next();	 Catch:{ all -> 0x009a }
        r0 = (org.apache.activemq.TransactionContext) r0;	 Catch:{ all -> 0x009a }
        r0.afterCommit();	 Catch:{ Exception -> 0x0080 }
        goto L_0x0070;
    L_0x0080:
        r3 = move-exception;
        r7 = LOG;	 Catch:{ all -> 0x009a }
        r9 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009a }
        r9.<init>();	 Catch:{ all -> 0x009a }
        r10 = "ignoring exception from after completion on ended transaction: ";
        r9 = r9.append(r10);	 Catch:{ all -> 0x009a }
        r9 = r9.append(r3);	 Catch:{ all -> 0x009a }
        r9 = r9.toString();	 Catch:{ all -> 0x009a }
        r7.debug(r9, r3);	 Catch:{ all -> 0x009a }
        goto L_0x0070;
    L_0x009a:
        r7 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x009a }
        throw r7;	 Catch:{ JMSException -> 0x009d }
    L_0x009d:
        r1 = move-exception;
        r7 = LOG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "commit of: ";
        r8 = r8.append(r9);
        r8 = r8.append(r6);
        r9 = " failed with: ";
        r8 = r8.append(r9);
        r8 = r8.append(r1);
        r8 = r8.toString();
        r7.warn(r8, r1);
        if (r13 == 0) goto L_0x011e;
    L_0x00c2:
        r8 = ENDED_XA_TRANSACTION_CONTEXTS;
        monitor-enter(r8);
        r7 = ENDED_XA_TRANSACTION_CONTEXTS;	 Catch:{ all -> 0x0115 }
        r5 = r7.remove(r6);	 Catch:{ all -> 0x0115 }
        r5 = (java.util.List) r5;	 Catch:{ all -> 0x0115 }
        if (r5 == 0) goto L_0x011d;
    L_0x00cf:
        r7 = r5.isEmpty();	 Catch:{ all -> 0x0115 }
        if (r7 != 0) goto L_0x011d;
    L_0x00d5:
        r2 = r5.iterator();	 Catch:{ all -> 0x0115 }
    L_0x00d9:
        r7 = r2.hasNext();	 Catch:{ all -> 0x0115 }
        if (r7 == 0) goto L_0x011d;
    L_0x00df:
        r0 = r2.next();	 Catch:{ all -> 0x0115 }
        r0 = (org.apache.activemq.TransactionContext) r0;	 Catch:{ all -> 0x0115 }
        r0.afterRollback();	 Catch:{ Throwable -> 0x00e9 }
        goto L_0x00d9;
    L_0x00e9:
        r3 = move-exception;
        r7 = LOG;	 Catch:{ all -> 0x0115 }
        r7 = r7.isDebugEnabled();	 Catch:{ all -> 0x0115 }
        if (r7 == 0) goto L_0x00d9;
    L_0x00f2:
        r7 = LOG;	 Catch:{ all -> 0x0115 }
        r9 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0115 }
        r9.<init>();	 Catch:{ all -> 0x0115 }
        r10 = "failed to firing afterRollback callbacks commit failure, txid: ";
        r9 = r9.append(r10);	 Catch:{ all -> 0x0115 }
        r9 = r9.append(r6);	 Catch:{ all -> 0x0115 }
        r10 = ", context: ";
        r9 = r9.append(r10);	 Catch:{ all -> 0x0115 }
        r9 = r9.append(r0);	 Catch:{ all -> 0x0115 }
        r9 = r9.toString();	 Catch:{ all -> 0x0115 }
        r7.debug(r9, r3);	 Catch:{ all -> 0x0115 }
        goto L_0x00d9;
    L_0x0115:
        r7 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x0115 }
        throw r7;
    L_0x0118:
        r7 = 3;
        goto L_0x0053;
    L_0x011b:
        monitor-exit(r8);	 Catch:{ all -> 0x009a }
        return;
    L_0x011d:
        monitor-exit(r8);	 Catch:{ all -> 0x0115 }
    L_0x011e:
        r7 = r11.toXAException(r1);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.TransactionContext.commit(javax.transaction.xa.Xid, boolean):void");
    }

    public void forget(Xid xid) throws XAException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Forget: " + xid);
        }
        if (xid == null) {
            throw new XAException(-6);
        }
        XATransactionId x;
        if (equals(this.associatedXid, xid)) {
            x = this.transactionId;
        } else {
            x = new XATransactionId(xid);
        }
        try {
            syncSendPacketWithInterruptionHandling(new TransactionInfo(getConnectionId(), x, (byte) 6));
            synchronized (ENDED_XA_TRANSACTION_CONTEXTS) {
                ENDED_XA_TRANSACTION_CONTEXTS.remove(x);
            }
        } catch (JMSException e) {
            throw toXAException(e);
        }
    }

    public boolean isSameRM(XAResource xaResource) throws XAException {
        boolean z = false;
        if (xaResource != null && (xaResource instanceof TransactionContext)) {
            try {
                z = getResourceManagerId().equals(((TransactionContext) xaResource).getResourceManagerId());
            } catch (Throwable e) {
                XAException xAException = (XAException) new XAException("Could not get resource manager id.").initCause(e);
            }
        }
        return z;
    }

    public Xid[] recover(int flag) throws XAException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Recover: " + flag);
        }
        TransactionInfo info = new TransactionInfo(getConnectionId(), null, (byte) 5);
        try {
            this.connection.checkClosedOrFailed();
            this.connection.ensureConnectionInfoSent();
            DataStructure[] data = ((DataArrayResponse) this.connection.syncSendPacket(info)).getData();
            if (data instanceof XATransactionId[]) {
                return (XATransactionId[]) data;
            }
            XATransactionId[] answer = new XATransactionId[data.length];
            System.arraycopy(data, 0, answer, 0, data.length);
            return answer;
        } catch (JMSException e) {
            throw toXAException(e);
        }
    }

    public int getTransactionTimeout() throws XAException {
        return 0;
    }

    public boolean setTransactionTimeout(int seconds) throws XAException {
        return false;
    }

    private String getResourceManagerId() throws JMSException {
        return this.connection.getResourceManagerId();
    }

    private void setXid(Xid xid) throws XAException {
        try {
            this.connection.checkClosedOrFailed();
            this.connection.ensureConnectionInfoSent();
            if (xid != null) {
                this.associatedXid = xid;
                this.transactionId = new XATransactionId(xid);
                try {
                    this.connection.asyncSendPacket(new TransactionInfo(this.connectionId, this.transactionId, (byte) 0));
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Started XA transaction: " + this.transactionId);
                        return;
                    }
                    return;
                } catch (JMSException e) {
                    throw toXAException(e);
                }
            }
            if (this.transactionId != null) {
                try {
                    syncSendPacketWithInterruptionHandling(new TransactionInfo(this.connectionId, this.transactionId, (byte) 7));
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Ended XA transaction: " + this.transactionId);
                    }
                    synchronized (ENDED_XA_TRANSACTION_CONTEXTS) {
                        List<TransactionContext> l = (List) ENDED_XA_TRANSACTION_CONTEXTS.get(this.transactionId);
                        if (l == null) {
                            l = new ArrayList(3);
                            ENDED_XA_TRANSACTION_CONTEXTS.put(this.transactionId, l);
                            l.add(this);
                        } else if (!l.contains(this)) {
                            l.add(this);
                        }
                    }
                } catch (JMSException e2) {
                    throw toXAException(e2);
                }
            }
            this.associatedXid = null;
            this.transactionId = null;
        } catch (JMSException e22) {
            throw toXAException(e22);
        }
    }

    private Response syncSendPacketWithInterruptionHandling(Command command) throws JMSException {
        Response syncSendPacket;
        try {
            syncSendPacket = this.connection.syncSendPacket(command);
        } catch (JMSException e) {
            if (e.getLinkedException() instanceof InterruptedIOException) {
                Thread.interrupted();
                syncSendPacket = this.connection.syncSendPacket(command);
            } else {
                throw e;
            }
        } finally {
            Thread.currentThread().interrupt();
        }
        return syncSendPacket;
    }

    private XAException toXAException(JMSException e) {
        if (e.getCause() == null || !(e.getCause() instanceof XAException)) {
            XAException xae = new XAException(e.getMessage());
            xae.errorCode = -7;
            xae.initCause(e);
            return xae;
        }
        XAException original = (XAException) e.getCause();
        xae = new XAException(original.getMessage());
        xae.errorCode = original.errorCode;
        xae.initCause(original);
        return xae;
    }

    public ActiveMQConnection getConnection() {
        return this.connection;
    }

    public void cleanup() {
        this.associatedXid = null;
        this.transactionId = null;
    }

    public String toString() {
        return "TransactionContext{transactionId=" + this.transactionId + '}';
    }
}
