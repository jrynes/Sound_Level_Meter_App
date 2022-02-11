package org.apache.activemq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.jms.IllegalStateException;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TransactionRolledBackException;
import org.apache.activemq.blob.BlobDownloader;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTempDestination;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.MessagePull;
import org.apache.activemq.command.RemoveInfo;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.management.JMSConsumerStatsImpl;
import org.apache.activemq.management.StatsCapable;
import org.apache.activemq.management.StatsImpl;
import org.apache.activemq.selector.SelectorParser;
import org.apache.activemq.transaction.Synchronization;
import org.apache.activemq.util.Callback;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQMessageConsumer implements MessageAvailableConsumer, StatsCapable, ActiveMQDispatcher {
    private static final Logger LOG;
    private int ackCounter;
    private int additionalWindowSize;
    private MessageAvailableListener availableListener;
    private boolean clearDispatchList;
    private int deliveredCounter;
    private final LinkedList<MessageDispatch> deliveredMessages;
    private final AtomicBoolean deliveryingAcknowledgements;
    private int dispatchedCount;
    private ExecutorService executorService;
    private long failoverRedeliveryWaitPeriod;
    private IOException failureError;
    AtomicInteger inProgressClearRequiredFlag;
    protected final ConsumerInfo info;
    private long lastDeliveredSequenceId;
    private final AtomicReference<MessageListener> messageListener;
    private boolean nonBlockingRedelivery;
    private long optimizeAckTimestamp;
    private boolean optimizeAcknowledge;
    private long optimizeAcknowledgeTimeOut;
    private long optimizedAckScheduledAckInterval;
    private Runnable optimizedAckTask;
    private MessageAck pendingAck;
    private PreviouslyDeliveredMap<MessageId, Boolean> previouslyDeliveredMessages;
    private long redeliveryDelay;
    private RedeliveryPolicy redeliveryPolicy;
    private final String selector;
    protected final ActiveMQSession session;
    private final AtomicBoolean started;
    private final JMSConsumerStatsImpl stats;
    private boolean synchronizationRegistered;
    private boolean transactedIndividualAck;
    private MessageTransformer transformer;
    protected final MessageDispatchChannel unconsumedMessages;

    class 1 implements Callback {
        1() {
        }

        public void execute() throws Exception {
            ActiveMQMessageConsumer.this.session.checkClosed();
            ActiveMQMessageConsumer.this.session.acknowledge();
        }
    }

    class 2 implements Callback {
        final /* synthetic */ MessageDispatch val$md;

        2(MessageDispatch messageDispatch) {
            this.val$md = messageDispatch;
        }

        public void execute() throws Exception {
            ActiveMQMessageConsumer.this.session.checkClosed();
            ActiveMQMessageConsumer.this.acknowledge(this.val$md);
        }
    }

    class 3 extends Synchronization {
        3() {
        }

        public void afterCommit() throws Exception {
            ActiveMQMessageConsumer.this.doClose();
        }

        public void afterRollback() throws Exception {
            ActiveMQMessageConsumer.this.doClose();
        }
    }

    class 4 implements Runnable {
        final /* synthetic */ MessageAck val$ackToSend;

        4(MessageAck messageAck) {
            this.val$ackToSend = messageAck;
        }

        public void run() {
            try {
                ActiveMQMessageConsumer.this.session.sendAck(this.val$ackToSend, true);
            } catch (JMSException e) {
                ActiveMQMessageConsumer.LOG.error(ActiveMQMessageConsumer.this.getConsumerId() + " failed to delivered acknowledgements", e);
            } finally {
                ActiveMQMessageConsumer.this.deliveryingAcknowledgements.set(false);
            }
        }
    }

    class 5 extends Synchronization {
        5() {
        }

        public void beforeEnd() throws Exception {
            if (ActiveMQMessageConsumer.this.transactedIndividualAck) {
                ActiveMQMessageConsumer.this.clearDispatchList();
                ActiveMQMessageConsumer.this.waitForRedeliveries();
                synchronized (ActiveMQMessageConsumer.this.deliveredMessages) {
                    ActiveMQMessageConsumer.this.rollbackOnFailedRecoveryRedelivery();
                }
            } else {
                ActiveMQMessageConsumer.this.acknowledge();
            }
            ActiveMQMessageConsumer.this.synchronizationRegistered = false;
        }

        public void afterCommit() throws Exception {
            ActiveMQMessageConsumer.this.commit();
            ActiveMQMessageConsumer.this.synchronizationRegistered = false;
        }

        public void afterRollback() throws Exception {
            ActiveMQMessageConsumer.this.rollback();
            ActiveMQMessageConsumer.this.synchronizationRegistered = false;
        }
    }

    class 6 implements Runnable {
        final /* synthetic */ LinkedList val$pendingRedeliveries;

        6(LinkedList linkedList) {
            this.val$pendingRedeliveries = linkedList;
        }

        public void run() {
            try {
                if (!ActiveMQMessageConsumer.this.unconsumedMessages.isClosed()) {
                    Iterator i$ = this.val$pendingRedeliveries.iterator();
                    while (i$.hasNext()) {
                        ActiveMQMessageConsumer.this.session.dispatch((MessageDispatch) i$.next());
                    }
                }
            } catch (Exception e) {
                ActiveMQMessageConsumer.this.session.connection.onAsyncException(e);
            }
        }
    }

    class 7 implements Runnable {
        7() {
        }

        public void run() {
            try {
                if (ActiveMQMessageConsumer.this.started.get()) {
                    ActiveMQMessageConsumer.this.start();
                }
            } catch (JMSException e) {
                ActiveMQMessageConsumer.this.session.connection.onAsyncException(e);
            }
        }
    }

    class 8 implements Runnable {
        8() {
        }

        public void run() {
            try {
                if (ActiveMQMessageConsumer.this.optimizeAcknowledge && !ActiveMQMessageConsumer.this.unconsumedMessages.isClosed()) {
                    if (ActiveMQMessageConsumer.LOG.isInfoEnabled()) {
                        ActiveMQMessageConsumer.LOG.info("Consumer:{} is performing scheduled delivery of outstanding optimized Acks", ActiveMQMessageConsumer.this.info.getConsumerId());
                    }
                    ActiveMQMessageConsumer.this.deliverAcks();
                }
            } catch (Exception e) {
                ActiveMQMessageConsumer.LOG.debug("Optimized Ack Task caught exception during ack", e);
            }
        }
    }

    class PreviouslyDeliveredMap<K, V> extends HashMap<K, V> {
        final TransactionId transactionId;

        public PreviouslyDeliveredMap(TransactionId transactionId) {
            this.transactionId = transactionId;
        }
    }

    static {
        LOG = LoggerFactory.getLogger(ActiveMQMessageConsumer.class);
    }

    public ActiveMQMessageConsumer(ActiveMQSession session, ConsumerId consumerId, ActiveMQDestination dest, String name, String selector, int prefetch, int maximumPendingMessageCount, boolean noLocal, boolean browser, boolean dispatchAsync, MessageListener messageListener) throws JMSException {
        this.deliveredMessages = new LinkedList();
        this.messageListener = new AtomicReference();
        this.started = new AtomicBoolean(false);
        this.deliveryingAcknowledgements = new AtomicBoolean();
        this.inProgressClearRequiredFlag = new AtomicInteger(0);
        this.optimizeAckTimestamp = System.currentTimeMillis();
        this.optimizeAcknowledgeTimeOut = 0;
        this.optimizedAckScheduledAckInterval = 0;
        this.failoverRedeliveryWaitPeriod = 0;
        this.transactedIndividualAck = false;
        this.nonBlockingRedelivery = false;
        if (dest == null) {
            throw new InvalidDestinationException("Don't understand null destinations");
        } else if (dest.getPhysicalName() == null) {
            throw new InvalidDestinationException("The destination object was not given a physical name.");
        } else {
            if (dest.isTemporary()) {
                String physicalName = dest.getPhysicalName();
                if (physicalName == null) {
                    throw new IllegalArgumentException("Physical name of Destination should be valid: " + dest);
                } else if (physicalName.indexOf(session.connection.getConnectionInfo().getConnectionId().getValue()) < 0) {
                    throw new InvalidDestinationException("Cannot use a Temporary destination from another Connection");
                } else if (session.connection.isDeleted(dest)) {
                    throw new InvalidDestinationException("Cannot use a Temporary destination that has been deleted");
                } else if (prefetch < 0) {
                    throw new JMSException("Cannot have a prefetch size less than zero");
                }
            }
            if (session.connection.isMessagePrioritySupported()) {
                this.unconsumedMessages = new SimplePriorityMessageDispatchChannel();
            } else {
                this.unconsumedMessages = new FifoMessageDispatchChannel();
            }
            this.session = session;
            this.redeliveryPolicy = session.connection.getRedeliveryPolicyMap().getEntryFor(dest);
            setTransformer(session.getTransformer());
            this.info = new ConsumerInfo(consumerId);
            this.info.setExclusive(this.session.connection.isExclusiveConsumer());
            this.info.setSubscriptionName(name);
            this.info.setPrefetchSize(prefetch);
            this.info.setCurrentPrefetchSize(prefetch);
            this.info.setMaximumPendingMessageLimit(maximumPendingMessageCount);
            this.info.setNoLocal(noLocal);
            this.info.setDispatchAsync(dispatchAsync);
            this.info.setRetroactive(this.session.connection.isUseRetroactiveConsumer());
            this.info.setSelector(null);
            if (dest.getOptions() != null) {
                Map<String, Object> options = IntrospectionSupport.extractProperties(new HashMap(dest.getOptions()), "consumer.");
                IntrospectionSupport.setProperties(this.info, options);
                if (options.size() > 0) {
                    String msg = "There are " + options.size() + " consumer options that couldn't be set on the consumer." + " Check the options are spelled correctly." + " Unknown parameters=[" + options + "]." + " This consumer cannot be started.";
                    LOG.warn(msg);
                    throw new ConfigurationException(msg);
                }
            }
            this.info.setDestination(dest);
            this.info.setBrowser(browser);
            if (selector != null && selector.trim().length() != 0) {
                SelectorParser.parse(selector);
                this.info.setSelector(selector);
                this.selector = selector;
            } else if (this.info.getSelector() != null) {
                SelectorParser.parse(this.info.getSelector());
                this.selector = this.info.getSelector();
            } else {
                this.selector = null;
            }
            this.stats = new JMSConsumerStatsImpl(session.getSessionStats(), dest);
            boolean z = session.connection.isOptimizeAcknowledge() && session.isAutoAcknowledge() && !this.info.isBrowser();
            this.optimizeAcknowledge = z;
            if (this.optimizeAcknowledge) {
                this.optimizeAcknowledgeTimeOut = session.connection.getOptimizeAcknowledgeTimeOut();
                setOptimizedAckScheduledAckInterval(session.connection.getOptimizedAckScheduledAckInterval());
            }
            this.info.setOptimizedAcknowledge(this.optimizeAcknowledge);
            this.failoverRedeliveryWaitPeriod = session.connection.getConsumerFailoverRedeliveryWaitPeriod();
            this.nonBlockingRedelivery = session.connection.isNonBlockingRedelivery();
            z = session.connection.isTransactedIndividualAck() || this.nonBlockingRedelivery;
            this.transactedIndividualAck = z;
            if (messageListener != null) {
                setMessageListener(messageListener);
            }
            try {
                this.session.addConsumer(this);
                this.session.syncSendPacket(this.info);
                if (session.connection.isStarted()) {
                    start();
                }
            } catch (JMSException e) {
                this.session.removeConsumer(this);
                throw e;
            }
        }
    }

    private boolean isAutoAcknowledgeEach() {
        return this.session.isAutoAcknowledge() || (this.session.isDupsOkAcknowledge() && getDestination().isQueue());
    }

    private boolean isAutoAcknowledgeBatch() {
        return this.session.isDupsOkAcknowledge() && !getDestination().isQueue();
    }

    public StatsImpl getStats() {
        return this.stats;
    }

    public JMSConsumerStatsImpl getConsumerStats() {
        return this.stats;
    }

    public RedeliveryPolicy getRedeliveryPolicy() {
        return this.redeliveryPolicy;
    }

    public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
        this.redeliveryPolicy = redeliveryPolicy;
    }

    public MessageTransformer getTransformer() {
        return this.transformer;
    }

    public void setTransformer(MessageTransformer transformer) {
        this.transformer = transformer;
    }

    public ConsumerId getConsumerId() {
        return this.info.getConsumerId();
    }

    public String getConsumerName() {
        return this.info.getSubscriptionName();
    }

    protected boolean isNoLocal() {
        return this.info.isNoLocal();
    }

    protected boolean isBrowser() {
        return this.info.isBrowser();
    }

    protected ActiveMQDestination getDestination() {
        return this.info.getDestination();
    }

    public int getPrefetchNumber() {
        return this.info.getPrefetchSize();
    }

    public boolean isDurableSubscriber() {
        return this.info.getSubscriptionName() != null && this.info.getDestination().isTopic();
    }

    public String getMessageSelector() throws JMSException {
        checkClosed();
        return this.selector;
    }

    public MessageListener getMessageListener() throws JMSException {
        checkClosed();
        return (MessageListener) this.messageListener.get();
    }

    public void setMessageListener(MessageListener listener) throws JMSException {
        checkClosed();
        if (this.info.getPrefetchSize() == 0) {
            throw new JMSException("Illegal prefetch size of zero. This setting is not supported for asynchronous consumers please set a value of at least 1");
        } else if (listener != null) {
            boolean wasRunning = this.session.isRunning();
            if (wasRunning) {
                this.session.stop();
            }
            this.messageListener.set(listener);
            this.session.redispatch(this, this.unconsumedMessages);
            if (wasRunning) {
                this.session.start();
            }
        } else {
            this.messageListener.set(null);
        }
    }

    public MessageAvailableListener getAvailableListener() {
        return this.availableListener;
    }

    public void setAvailableListener(MessageAvailableListener availableListener) {
        this.availableListener = availableListener;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.apache.activemq.command.MessageDispatch dequeue(long r14) throws javax.jms.JMSException {
        /*
        r13 = this;
        r4 = 0;
        r10 = 0;
        r0 = 0;
        r5 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1));
        if (r5 <= 0) goto L_0x000f;
    L_0x0009:
        r6 = java.lang.System.currentTimeMillis();	 Catch:{ InterruptedException -> 0x003b }
        r0 = r6 + r14;
    L_0x000f:
        r5 = r13.unconsumedMessages;	 Catch:{ InterruptedException -> 0x003b }
        r3 = r5.dequeue(r14);	 Catch:{ InterruptedException -> 0x003b }
        if (r3 != 0) goto L_0x004a;
    L_0x0017:
        r5 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1));
        if (r5 <= 0) goto L_0x0030;
    L_0x001b:
        r5 = r13.unconsumedMessages;	 Catch:{ InterruptedException -> 0x003b }
        r5 = r5.isClosed();	 Catch:{ InterruptedException -> 0x003b }
        if (r5 != 0) goto L_0x0030;
    L_0x0023:
        r6 = java.lang.System.currentTimeMillis();	 Catch:{ InterruptedException -> 0x003b }
        r6 = r0 - r6;
        r8 = 0;
        r14 = java.lang.Math.max(r6, r8);	 Catch:{ InterruptedException -> 0x003b }
        goto L_0x000f;
    L_0x0030:
        r5 = r13.failureError;	 Catch:{ InterruptedException -> 0x003b }
        if (r5 == 0) goto L_0x0048;
    L_0x0034:
        r4 = r13.failureError;	 Catch:{ InterruptedException -> 0x003b }
        r4 = org.apache.activemq.util.JMSExceptionSupport.create(r4);	 Catch:{ InterruptedException -> 0x003b }
        throw r4;	 Catch:{ InterruptedException -> 0x003b }
    L_0x003b:
        r2 = move-exception;
        r4 = java.lang.Thread.currentThread();
        r4.interrupt();
        r4 = org.apache.activemq.util.JMSExceptionSupport.create(r2);
        throw r4;
    L_0x0048:
        r3 = r4;
    L_0x0049:
        return r3;
    L_0x004a:
        r5 = r3.getMessage();	 Catch:{ InterruptedException -> 0x003b }
        if (r5 != 0) goto L_0x0052;
    L_0x0050:
        r3 = r4;
        goto L_0x0049;
    L_0x0052:
        r5 = r3.getMessage();	 Catch:{ InterruptedException -> 0x003b }
        r5 = r5.isExpired();	 Catch:{ InterruptedException -> 0x003b }
        if (r5 == 0) goto L_0x009d;
    L_0x005c:
        r5 = LOG;	 Catch:{ InterruptedException -> 0x003b }
        r5 = r5.isDebugEnabled();	 Catch:{ InterruptedException -> 0x003b }
        if (r5 == 0) goto L_0x0084;
    L_0x0064:
        r5 = LOG;	 Catch:{ InterruptedException -> 0x003b }
        r6 = new java.lang.StringBuilder;	 Catch:{ InterruptedException -> 0x003b }
        r6.<init>();	 Catch:{ InterruptedException -> 0x003b }
        r7 = r13.getConsumerId();	 Catch:{ InterruptedException -> 0x003b }
        r6 = r6.append(r7);	 Catch:{ InterruptedException -> 0x003b }
        r7 = " received expired message: ";
        r6 = r6.append(r7);	 Catch:{ InterruptedException -> 0x003b }
        r6 = r6.append(r3);	 Catch:{ InterruptedException -> 0x003b }
        r6 = r6.toString();	 Catch:{ InterruptedException -> 0x003b }
        r5.debug(r6);	 Catch:{ InterruptedException -> 0x003b }
    L_0x0084:
        r13.beforeMessageIsConsumed(r3);	 Catch:{ InterruptedException -> 0x003b }
        r5 = 1;
        r13.afterMessageIsConsumed(r3, r5);	 Catch:{ InterruptedException -> 0x003b }
        r5 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1));
        if (r5 <= 0) goto L_0x000f;
    L_0x008f:
        r6 = java.lang.System.currentTimeMillis();	 Catch:{ InterruptedException -> 0x003b }
        r6 = r0 - r6;
        r8 = 0;
        r14 = java.lang.Math.max(r6, r8);	 Catch:{ InterruptedException -> 0x003b }
        goto L_0x000f;
    L_0x009d:
        r4 = LOG;	 Catch:{ InterruptedException -> 0x003b }
        r4 = r4.isTraceEnabled();	 Catch:{ InterruptedException -> 0x003b }
        if (r4 == 0) goto L_0x0049;
    L_0x00a5:
        r4 = LOG;	 Catch:{ InterruptedException -> 0x003b }
        r5 = new java.lang.StringBuilder;	 Catch:{ InterruptedException -> 0x003b }
        r5.<init>();	 Catch:{ InterruptedException -> 0x003b }
        r6 = r13.getConsumerId();	 Catch:{ InterruptedException -> 0x003b }
        r5 = r5.append(r6);	 Catch:{ InterruptedException -> 0x003b }
        r6 = " received message: ";
        r5 = r5.append(r6);	 Catch:{ InterruptedException -> 0x003b }
        r5 = r5.append(r3);	 Catch:{ InterruptedException -> 0x003b }
        r5 = r5.toString();	 Catch:{ InterruptedException -> 0x003b }
        r4.trace(r5);	 Catch:{ InterruptedException -> 0x003b }
        goto L_0x0049;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.ActiveMQMessageConsumer.dequeue(long):org.apache.activemq.command.MessageDispatch");
    }

    public Message receive() throws JMSException {
        checkClosed();
        checkMessageListener();
        sendPullCommand(0);
        MessageDispatch md = dequeue(-1);
        if (md == null) {
            return null;
        }
        beforeMessageIsConsumed(md);
        afterMessageIsConsumed(md, false);
        return createActiveMQMessage(md);
    }

    private ActiveMQMessage createActiveMQMessage(MessageDispatch md) throws JMSException {
        ActiveMQMessage m = (ActiveMQMessage) md.getMessage().copy();
        if (m.getDataStructureType() == 29) {
            ((ActiveMQBlobMessage) m).setBlobDownloader(new BlobDownloader(this.session.getBlobTransferPolicy()));
        }
        if (this.transformer != null) {
            Message transformedMessage = this.transformer.consumerTransform(this.session, this, m);
            if (transformedMessage != null) {
                m = ActiveMQMessageTransformation.transformMessage(transformedMessage, this.session.connection);
            }
        }
        if (this.session.isClientAcknowledge()) {
            m.setAcknowledgeCallback(new 1());
        } else if (this.session.isIndividualAcknowledge()) {
            m.setAcknowledgeCallback(new 2(md));
        }
        return m;
    }

    public Message receive(long timeout) throws JMSException {
        checkClosed();
        checkMessageListener();
        if (timeout == 0) {
            return receive();
        }
        sendPullCommand(timeout);
        if (timeout <= 0) {
            return null;
        }
        MessageDispatch md;
        if (this.info.getPrefetchSize() == 0) {
            md = dequeue(-1);
        } else {
            md = dequeue(timeout);
        }
        if (md == null) {
            return null;
        }
        beforeMessageIsConsumed(md);
        afterMessageIsConsumed(md, false);
        return createActiveMQMessage(md);
    }

    public Message receiveNoWait() throws JMSException {
        MessageDispatch md;
        checkClosed();
        checkMessageListener();
        sendPullCommand(-1);
        if (this.info.getPrefetchSize() == 0) {
            md = dequeue(-1);
        } else {
            md = dequeue(0);
        }
        if (md == null) {
            return null;
        }
        beforeMessageIsConsumed(md);
        afterMessageIsConsumed(md, false);
        return createActiveMQMessage(md);
    }

    public void close() throws JMSException {
        if (!this.unconsumedMessages.isClosed()) {
            if (this.session.getTransactionContext().isInTransaction()) {
                this.session.getTransactionContext().addSynchronization(new 3());
            } else {
                doClose();
            }
        }
    }

    void doClose() throws JMSException {
        boolean interrupted = Thread.interrupted();
        dispose();
        RemoveInfo removeCommand = this.info.createRemoveCommand();
        if (LOG.isDebugEnabled()) {
            LOG.debug("remove: " + getConsumerId() + ", lastDeliveredSequenceId:" + this.lastDeliveredSequenceId);
        }
        removeCommand.setLastDeliveredSequenceId(this.lastDeliveredSequenceId);
        this.session.asyncSendPacket(removeCommand);
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    void inProgressClearRequired() {
        this.inProgressClearRequiredFlag.incrementAndGet();
        this.clearDispatchList = true;
    }

    void clearMessagesInProgress() {
        if (this.inProgressClearRequiredFlag.get() > 0) {
            synchronized (this.unconsumedMessages.getMutex()) {
                if (this.inProgressClearRequiredFlag.get() > 0) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(getConsumerId() + " clearing unconsumed list (" + this.unconsumedMessages.size() + ") on transport interrupt");
                    }
                    List<MessageDispatch> list = this.unconsumedMessages.removeAll();
                    if (!this.info.isBrowser()) {
                        for (MessageDispatch old : list) {
                            this.session.connection.rollbackDuplicate(this, old.getMessage());
                        }
                    }
                    this.session.connection.transportInterruptionProcessingComplete();
                    this.inProgressClearRequiredFlag.decrementAndGet();
                    this.unconsumedMessages.getMutex().notifyAll();
                }
            }
        }
    }

    void deliverAcks() {
        MessageAck ack = null;
        if (this.deliveryingAcknowledgements.compareAndSet(false, true)) {
            if (isAutoAcknowledgeEach()) {
                synchronized (this.deliveredMessages) {
                    ack = makeAckForAllDeliveredMessages((byte) 2);
                    if (ack != null) {
                        this.deliveredMessages.clear();
                        this.ackCounter = 0;
                    } else {
                        ack = this.pendingAck;
                        this.pendingAck = null;
                    }
                }
            } else if (this.pendingAck != null && this.pendingAck.isStandardAck()) {
                ack = this.pendingAck;
                this.pendingAck = null;
            }
            if (ack != null) {
                MessageAck ackToSend = ack;
                if (this.executorService == null) {
                    this.executorService = Executors.newSingleThreadExecutor();
                }
                this.executorService.submit(new 4(ackToSend));
                return;
            }
            this.deliveryingAcknowledgements.set(false);
        }
    }

    public void dispose() throws JMSException {
        Throwable th;
        if (!this.unconsumedMessages.isClosed()) {
            if (!this.session.getTransacted()) {
                deliverAcks();
                if (isAutoAcknowledgeBatch()) {
                    acknowledge();
                }
            }
            if (this.executorService != null) {
                ThreadPoolUtils.shutdownGraceful(this.executorService, 60000);
                this.executorService = null;
            }
            if (this.optimizedAckTask != null) {
                this.session.connection.getScheduler().cancel(this.optimizedAckTask);
                this.optimizedAckTask = null;
            }
            if (this.session.isClientAcknowledge() && !this.info.isBrowser()) {
                synchronized (this.deliveredMessages) {
                    try {
                        List<MessageDispatch> tmp = new ArrayList(this.deliveredMessages);
                        try {
                            for (MessageDispatch old : tmp) {
                                this.session.connection.rollbackDuplicate(this, old.getMessage());
                            }
                            tmp.clear();
                        } catch (Throwable th2) {
                            th = th2;
                            List<MessageDispatch> list = tmp;
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th;
                    }
                }
            }
            if (!this.session.isTransacted()) {
                synchronized (this.deliveredMessages) {
                    this.deliveredMessages.clear();
                }
            }
            this.unconsumedMessages.close();
            this.session.removeConsumer(this);
            List<MessageDispatch> list2 = this.unconsumedMessages.removeAll();
            if (!this.info.isBrowser()) {
                for (MessageDispatch old2 : list2) {
                    this.session.connection.rollbackDuplicate(this, old2.getMessage());
                }
            }
        }
    }

    protected void checkClosed() throws IllegalStateException {
        if (this.unconsumedMessages.isClosed()) {
            throw new IllegalStateException("The Consumer is closed");
        }
    }

    protected void sendPullCommand(long timeout) throws JMSException {
        clearDispatchList();
        if (this.info.getCurrentPrefetchSize() == 0 && this.unconsumedMessages.isEmpty()) {
            MessagePull messagePull = new MessagePull();
            messagePull.configure(this.info);
            messagePull.setTimeout(timeout);
            this.session.asyncSendPacket(messagePull);
        }
    }

    protected void checkMessageListener() throws JMSException {
        this.session.checkMessageListener();
    }

    protected void setOptimizeAcknowledge(boolean value) {
        if (this.optimizeAcknowledge && !value) {
            deliverAcks();
        }
        this.optimizeAcknowledge = value;
    }

    protected void setPrefetchSize(int prefetch) {
        deliverAcks();
        this.info.setCurrentPrefetchSize(prefetch);
    }

    private void beforeMessageIsConsumed(MessageDispatch md) throws JMSException {
        md.setDeliverySequenceId(this.session.getNextDeliveryId());
        this.lastDeliveredSequenceId = md.getMessage().getMessageId().getBrokerSequenceId();
        if (!isAutoAcknowledgeBatch()) {
            synchronized (this.deliveredMessages) {
                this.deliveredMessages.addFirst(md);
            }
            if (!this.session.getTransacted()) {
                return;
            }
            if (this.transactedIndividualAck) {
                immediateIndividualTransactedAck(md);
            } else {
                ackLater(md, (byte) 0);
            }
        }
    }

    private void immediateIndividualTransactedAck(MessageDispatch md) throws JMSException {
        registerSync();
        MessageAck ack = new MessageAck(md, (byte) 4, 1);
        ack.setTransactionId(this.session.getTransactionContext().getTransactionId());
        this.session.syncSendPacket(ack);
    }

    private void afterMessageIsConsumed(MessageDispatch md, boolean messageExpired) throws JMSException {
        if (!this.unconsumedMessages.isClosed()) {
            if (messageExpired) {
                acknowledge(md, (byte) 0);
                this.stats.getExpiredMessageCount().increment();
                return;
            }
            this.stats.onMessage();
            if (!this.session.getTransacted()) {
                if (isAutoAcknowledgeEach()) {
                    if (this.deliveryingAcknowledgements.compareAndSet(false, true)) {
                        synchronized (this.deliveredMessages) {
                            if (!this.deliveredMessages.isEmpty()) {
                                MessageAck ack;
                                if (this.optimizeAcknowledge) {
                                    this.ackCounter++;
                                    if (((double) (this.ackCounter + this.deliveredCounter)) >= ((double) this.info.getPrefetchSize()) * 0.65d || (this.optimizeAcknowledgeTimeOut > 0 && System.currentTimeMillis() >= this.optimizeAckTimestamp + this.optimizeAcknowledgeTimeOut)) {
                                        ack = makeAckForAllDeliveredMessages((byte) 2);
                                        if (ack != null) {
                                            this.deliveredMessages.clear();
                                            this.ackCounter = 0;
                                            this.session.sendAck(ack);
                                            this.optimizeAckTimestamp = System.currentTimeMillis();
                                        }
                                        if (this.pendingAck != null && this.deliveredCounter > 0) {
                                            this.session.sendAck(this.pendingAck);
                                            this.pendingAck = null;
                                            this.deliveredCounter = 0;
                                        }
                                    }
                                } else {
                                    ack = makeAckForAllDeliveredMessages((byte) 2);
                                    if (ack != null) {
                                        this.deliveredMessages.clear();
                                        this.session.sendAck(ack);
                                    }
                                }
                            }
                        }
                        this.deliveryingAcknowledgements.set(false);
                    }
                } else if (isAutoAcknowledgeBatch()) {
                    ackLater(md, (byte) 2);
                } else if (this.session.isClientAcknowledge() || this.session.isIndividualAcknowledge()) {
                    boolean messageUnackedByConsumer;
                    synchronized (this.deliveredMessages) {
                        messageUnackedByConsumer = this.deliveredMessages.contains(md);
                    }
                    if (messageUnackedByConsumer) {
                        ackLater(md, (byte) 0);
                    }
                } else {
                    throw new IllegalStateException("Invalid session state.");
                }
            }
        }
    }

    private MessageAck makeAckForAllDeliveredMessages(byte type) {
        MessageAck messageAck;
        synchronized (this.deliveredMessages) {
            if (this.deliveredMessages.isEmpty()) {
                messageAck = null;
            } else {
                messageAck = new MessageAck((MessageDispatch) this.deliveredMessages.getFirst(), type, this.deliveredMessages.size());
                messageAck.setFirstMessageId(((MessageDispatch) this.deliveredMessages.getLast()).getMessage().getMessageId());
            }
        }
        return messageAck;
    }

    private void ackLater(MessageDispatch md, byte ackType) throws JMSException {
        if (this.session.getTransacted()) {
            registerSync();
        }
        this.deliveredCounter++;
        MessageAck oldPendingAck = this.pendingAck;
        this.pendingAck = new MessageAck(md, ackType, this.deliveredCounter);
        this.pendingAck.setTransactionId(this.session.getTransactionContext().getTransactionId());
        if (oldPendingAck == null) {
            this.pendingAck.setFirstMessageId(this.pendingAck.getLastMessageId());
        } else if (oldPendingAck.getAckType() == this.pendingAck.getAckType()) {
            this.pendingAck.setFirstMessageId(oldPendingAck.getFirstMessageId());
        } else if (!oldPendingAck.isDeliveredAck()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sending old pending ack " + oldPendingAck + ", new pending: " + this.pendingAck);
            }
            this.session.sendAck(oldPendingAck);
        } else if (LOG.isDebugEnabled()) {
            LOG.debug("dropping old pending ack " + oldPendingAck + ", new pending: " + this.pendingAck);
        }
        if (0.5d * ((double) this.info.getPrefetchSize()) <= ((double) ((this.deliveredCounter + this.ackCounter) - this.additionalWindowSize))) {
            this.session.sendAck(this.pendingAck);
            this.pendingAck = null;
            this.deliveredCounter = 0;
            this.additionalWindowSize = 0;
        }
    }

    private void registerSync() throws JMSException {
        this.session.doStartTransaction();
        if (!this.synchronizationRegistered) {
            this.synchronizationRegistered = true;
            this.session.getTransactionContext().addSynchronization(new 5());
        }
    }

    public void acknowledge() throws JMSException {
        clearDispatchList();
        waitForRedeliveries();
        synchronized (this.deliveredMessages) {
            MessageAck ack = makeAckForAllDeliveredMessages((byte) 2);
            if (ack == null) {
                return;
            }
            if (this.session.getTransacted()) {
                rollbackOnFailedRecoveryRedelivery();
                this.session.doStartTransaction();
                ack.setTransactionId(this.session.getTransactionContext().getTransactionId());
            }
            this.pendingAck = null;
            this.session.sendAck(ack);
            this.deliveredCounter = Math.max(0, this.deliveredCounter - this.deliveredMessages.size());
            this.additionalWindowSize = Math.max(0, this.additionalWindowSize - this.deliveredMessages.size());
            if (!this.session.getTransacted()) {
                this.deliveredMessages.clear();
            }
        }
    }

    private void waitForRedeliveries() {
        if (this.failoverRedeliveryWaitPeriod > 0 && this.previouslyDeliveredMessages != null) {
            long expiry = System.currentTimeMillis() + this.failoverRedeliveryWaitPeriod;
            do {
                int numberNotReplayed = 0;
                synchronized (this.deliveredMessages) {
                    if (this.previouslyDeliveredMessages != null) {
                        for (Entry<MessageId, Boolean> entry : this.previouslyDeliveredMessages.entrySet()) {
                            if (!((Boolean) entry.getValue()).booleanValue()) {
                                numberNotReplayed++;
                            }
                        }
                    }
                }
                if (numberNotReplayed > 0) {
                    LOG.info("waiting for redelivery of " + numberNotReplayed + " in transaction: " + this.previouslyDeliveredMessages.transactionId + ", to consumer :" + getConsumerId());
                    try {
                        Thread.sleep(Math.max(500, this.failoverRedeliveryWaitPeriod / 4));
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                if (numberNotReplayed <= 0) {
                    return;
                }
            } while (expiry < System.currentTimeMillis());
        }
    }

    private void rollbackOnFailedRecoveryRedelivery() throws JMSException {
        if (this.previouslyDeliveredMessages != null) {
            int numberNotReplayed = 0;
            for (Entry<MessageId, Boolean> entry : this.previouslyDeliveredMessages.entrySet()) {
                if (!((Boolean) entry.getValue()).booleanValue()) {
                    numberNotReplayed++;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("previously delivered message has not been replayed in transaction: " + this.previouslyDeliveredMessages.transactionId + " , messageId: " + entry.getKey());
                    }
                }
            }
            if (numberNotReplayed > 0) {
                String message = "rolling back transaction (" + this.previouslyDeliveredMessages.transactionId + ") post failover recovery. " + numberNotReplayed + " previously delivered message(s) not replayed to consumer: " + getConsumerId();
                LOG.warn(message);
                throw new TransactionRolledBackException(message);
            }
        }
    }

    void acknowledge(MessageDispatch md) throws JMSException {
        acknowledge(md, (byte) 4);
    }

    void acknowledge(MessageDispatch md, byte ackType) throws JMSException {
        this.session.sendAck(new MessageAck(md, ackType, 1));
        synchronized (this.deliveredMessages) {
            this.deliveredMessages.remove(md);
        }
    }

    public void commit() throws JMSException {
        synchronized (this.deliveredMessages) {
            this.deliveredMessages.clear();
            clearPreviouslyDelivered();
        }
        this.redeliveryDelay = 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void rollback() throws javax.jms.JMSException {
        /*
        r18 = this;
        r0 = r18;
        r11 = r0.unconsumedMessages;
        r12 = r11.getMutex();
        monitor-enter(r12);
        r0 = r18;
        r11 = r0.optimizeAcknowledge;	 Catch:{ all -> 0x0064 }
        if (r11 == 0) goto L_0x004c;
    L_0x000f:
        r0 = r18;
        r11 = r0.info;	 Catch:{ all -> 0x0064 }
        r11 = r11.isBrowser();	 Catch:{ all -> 0x0064 }
        if (r11 != 0) goto L_0x004c;
    L_0x0019:
        r0 = r18;
        r13 = r0.deliveredMessages;	 Catch:{ all -> 0x0064 }
        monitor-enter(r13);	 Catch:{ all -> 0x0064 }
        r5 = 0;
    L_0x001f:
        r0 = r18;
        r11 = r0.deliveredMessages;	 Catch:{ all -> 0x0061 }
        r11 = r11.size();	 Catch:{ all -> 0x0061 }
        if (r5 >= r11) goto L_0x004b;
    L_0x0029:
        r0 = r18;
        r11 = r0.ackCounter;	 Catch:{ all -> 0x0061 }
        if (r5 >= r11) goto L_0x004b;
    L_0x002f:
        r0 = r18;
        r11 = r0.deliveredMessages;	 Catch:{ all -> 0x0061 }
        r9 = r11.removeLast();	 Catch:{ all -> 0x0061 }
        r9 = (org.apache.activemq.command.MessageDispatch) r9;	 Catch:{ all -> 0x0061 }
        r0 = r18;
        r11 = r0.session;	 Catch:{ all -> 0x0061 }
        r11 = r11.connection;	 Catch:{ all -> 0x0061 }
        r14 = r9.getMessage();	 Catch:{ all -> 0x0061 }
        r0 = r18;
        r11.rollbackDuplicate(r0, r14);	 Catch:{ all -> 0x0061 }
        r5 = r5 + 1;
        goto L_0x001f;
    L_0x004b:
        monitor-exit(r13);	 Catch:{ all -> 0x0061 }
    L_0x004c:
        r0 = r18;
        r13 = r0.deliveredMessages;	 Catch:{ all -> 0x0064 }
        monitor-enter(r13);	 Catch:{ all -> 0x0064 }
        r18.rollbackPreviouslyDeliveredAndNotRedelivered();	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r11 = r11.isEmpty();	 Catch:{ all -> 0x00c8 }
        if (r11 == 0) goto L_0x0067;
    L_0x005e:
        monitor-exit(r13);	 Catch:{ all -> 0x00c8 }
        monitor-exit(r12);	 Catch:{ all -> 0x0064 }
    L_0x0060:
        return;
    L_0x0061:
        r11 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x0061 }
        throw r11;	 Catch:{ all -> 0x0064 }
    L_0x0064:
        r11 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x0064 }
        throw r11;
    L_0x0067:
        r0 = r18;
        r11 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r8 = r11.getFirst();	 Catch:{ all -> 0x00c8 }
        r8 = (org.apache.activemq.command.MessageDispatch) r8;	 Catch:{ all -> 0x00c8 }
        r11 = r8.getMessage();	 Catch:{ all -> 0x00c8 }
        r3 = r11.getRedeliveryCounter();	 Catch:{ all -> 0x00c8 }
        if (r3 <= 0) goto L_0x00cb;
    L_0x007b:
        r0 = r18;
        r11 = r0.redeliveryPolicy;	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r14 = r0.redeliveryDelay;	 Catch:{ all -> 0x00c8 }
        r14 = r11.getNextRedeliveryDelay(r14);	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r0.redeliveryDelay = r14;	 Catch:{ all -> 0x00c8 }
    L_0x008b:
        r0 = r18;
        r11 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r11 = r11.getLast();	 Catch:{ all -> 0x00c8 }
        r11 = (org.apache.activemq.command.MessageDispatch) r11;	 Catch:{ all -> 0x00c8 }
        r11 = r11.getMessage();	 Catch:{ all -> 0x00c8 }
        r4 = r11.getMessageId();	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r7 = r11.iterator();	 Catch:{ all -> 0x00c8 }
    L_0x00a5:
        r11 = r7.hasNext();	 Catch:{ all -> 0x00c8 }
        if (r11 == 0) goto L_0x00d8;
    L_0x00ab:
        r9 = r7.next();	 Catch:{ all -> 0x00c8 }
        r9 = (org.apache.activemq.command.MessageDispatch) r9;	 Catch:{ all -> 0x00c8 }
        r11 = r9.getMessage();	 Catch:{ all -> 0x00c8 }
        r11.onMessageRolledBack();	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11 = r0.session;	 Catch:{ all -> 0x00c8 }
        r11 = r11.connection;	 Catch:{ all -> 0x00c8 }
        r14 = r9.getMessage();	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11.rollbackDuplicate(r0, r14);	 Catch:{ all -> 0x00c8 }
        goto L_0x00a5;
    L_0x00c8:
        r11 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x00c8 }
        throw r11;	 Catch:{ all -> 0x0064 }
    L_0x00cb:
        r0 = r18;
        r11 = r0.redeliveryPolicy;	 Catch:{ all -> 0x00c8 }
        r14 = r11.getInitialRedeliveryDelay();	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r0.redeliveryDelay = r14;	 Catch:{ all -> 0x00c8 }
        goto L_0x008b;
    L_0x00d8:
        r0 = r18;
        r11 = r0.redeliveryPolicy;	 Catch:{ all -> 0x00c8 }
        r11 = r11.getMaximumRedeliveries();	 Catch:{ all -> 0x00c8 }
        r14 = -1;
        if (r11 == r14) goto L_0x0164;
    L_0x00e3:
        r11 = r8.getMessage();	 Catch:{ all -> 0x00c8 }
        r11 = r11.getRedeliveryCounter();	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r14 = r0.redeliveryPolicy;	 Catch:{ all -> 0x00c8 }
        r14 = r14.getMaximumRedeliveries();	 Catch:{ all -> 0x00c8 }
        if (r11 <= r14) goto L_0x0164;
    L_0x00f5:
        r2 = new org.apache.activemq.command.MessageAck;	 Catch:{ all -> 0x00c8 }
        r11 = 1;
        r0 = r18;
        r14 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r14 = r14.size();	 Catch:{ all -> 0x00c8 }
        r2.<init>(r8, r11, r14);	 Catch:{ all -> 0x00c8 }
        r11 = r8.getRollbackCause();	 Catch:{ all -> 0x00c8 }
        r2.setPoisonCause(r11);	 Catch:{ all -> 0x00c8 }
        r2.setFirstMessageId(r4);	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11 = r0.session;	 Catch:{ all -> 0x00c8 }
        r14 = 1;
        r11.sendAck(r2, r14);	 Catch:{ all -> 0x00c8 }
        r11 = 0;
        r0 = r18;
        r14 = r0.additionalWindowSize;	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r15 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r15 = r15.size();	 Catch:{ all -> 0x00c8 }
        r14 = r14 - r15;
        r11 = java.lang.Math.max(r11, r14);	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r0.additionalWindowSize = r11;	 Catch:{ all -> 0x00c8 }
        r14 = 0;
        r0 = r18;
        r0.redeliveryDelay = r14;	 Catch:{ all -> 0x00c8 }
    L_0x0131:
        r0 = r18;
        r11 = r0.deliveredCounter;	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r14 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r14 = r14.size();	 Catch:{ all -> 0x00c8 }
        r11 = r11 - r14;
        r0 = r18;
        r0.deliveredCounter = r11;	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r11.clear();	 Catch:{ all -> 0x00c8 }
        monitor-exit(r13);	 Catch:{ all -> 0x00c8 }
        monitor-exit(r12);	 Catch:{ all -> 0x0064 }
        r0 = r18;
        r11 = r0.messageListener;
        r11 = r11.get();
        if (r11 == 0) goto L_0x0060;
    L_0x0155:
        r0 = r18;
        r11 = r0.session;
        r0 = r18;
        r12 = r0.unconsumedMessages;
        r0 = r18;
        r11.redispatch(r0, r12);
        goto L_0x0060;
    L_0x0164:
        if (r3 <= 0) goto L_0x017f;
    L_0x0166:
        r2 = new org.apache.activemq.command.MessageAck;	 Catch:{ all -> 0x00c8 }
        r11 = 3;
        r0 = r18;
        r14 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r14 = r14.size();	 Catch:{ all -> 0x00c8 }
        r2.<init>(r8, r11, r14);	 Catch:{ all -> 0x00c8 }
        r2.setFirstMessageId(r4);	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11 = r0.session;	 Catch:{ all -> 0x00c8 }
        r14 = 1;
        r11.sendAck(r2, r14);	 Catch:{ all -> 0x00c8 }
    L_0x017f:
        r0 = r18;
        r11 = r0.nonBlockingRedelivery;	 Catch:{ all -> 0x00c8 }
        if (r11 == 0) goto L_0x01b4;
    L_0x0185:
        r0 = r18;
        r11 = r0.unconsumedMessages;	 Catch:{ all -> 0x00c8 }
        r11 = r11.isClosed();	 Catch:{ all -> 0x00c8 }
        if (r11 != 0) goto L_0x0131;
    L_0x018f:
        r10 = new java.util.LinkedList;	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r10.<init>(r11);	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11 = r0.session;	 Catch:{ all -> 0x00c8 }
        r11 = r11.getScheduler();	 Catch:{ all -> 0x00c8 }
        r14 = new org.apache.activemq.ActiveMQMessageConsumer$6;	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r14.<init>(r10);	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r0 = r0.redeliveryDelay;	 Catch:{ all -> 0x00c8 }
        r16 = r0;
        r0 = r16;
        r11.executeAfterDelay(r14, r0);	 Catch:{ all -> 0x00c8 }
        goto L_0x0131;
    L_0x01b4:
        r0 = r18;
        r11 = r0.unconsumedMessages;	 Catch:{ all -> 0x00c8 }
        r11.stop();	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11 = r0.deliveredMessages;	 Catch:{ all -> 0x00c8 }
        r6 = r11.iterator();	 Catch:{ all -> 0x00c8 }
    L_0x01c3:
        r11 = r6.hasNext();	 Catch:{ all -> 0x00c8 }
        if (r11 == 0) goto L_0x01d7;
    L_0x01c9:
        r9 = r6.next();	 Catch:{ all -> 0x00c8 }
        r9 = (org.apache.activemq.command.MessageDispatch) r9;	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r11 = r0.unconsumedMessages;	 Catch:{ all -> 0x00c8 }
        r11.enqueueFirst(r9);	 Catch:{ all -> 0x00c8 }
        goto L_0x01c3;
    L_0x01d7:
        r0 = r18;
        r14 = r0.redeliveryDelay;	 Catch:{ all -> 0x00c8 }
        r16 = 0;
        r11 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1));
        if (r11 <= 0) goto L_0x0207;
    L_0x01e1:
        r0 = r18;
        r11 = r0.unconsumedMessages;	 Catch:{ all -> 0x00c8 }
        r11 = r11.isClosed();	 Catch:{ all -> 0x00c8 }
        if (r11 != 0) goto L_0x0207;
    L_0x01eb:
        r0 = r18;
        r11 = r0.session;	 Catch:{ all -> 0x00c8 }
        r11 = r11.getScheduler();	 Catch:{ all -> 0x00c8 }
        r14 = new org.apache.activemq.ActiveMQMessageConsumer$7;	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r14.<init>();	 Catch:{ all -> 0x00c8 }
        r0 = r18;
        r0 = r0.redeliveryDelay;	 Catch:{ all -> 0x00c8 }
        r16 = r0;
        r0 = r16;
        r11.executeAfterDelay(r14, r0);	 Catch:{ all -> 0x00c8 }
        goto L_0x0131;
    L_0x0207:
        r18.start();	 Catch:{ all -> 0x00c8 }
        goto L_0x0131;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.ActiveMQMessageConsumer.rollback():void");
    }

    private void rollbackPreviouslyDeliveredAndNotRedelivered() {
        if (this.previouslyDeliveredMessages != null) {
            for (Entry<MessageId, Boolean> entry : this.previouslyDeliveredMessages.entrySet()) {
                if (!((Boolean) entry.getValue()).booleanValue()) {
                    removeFromDeliveredMessages((MessageId) entry.getKey());
                }
            }
            clearPreviouslyDelivered();
        }
    }

    private void removeFromDeliveredMessages(MessageId key) {
        Iterator<MessageDispatch> iterator = this.deliveredMessages.iterator();
        while (iterator.hasNext()) {
            MessageDispatch candidate = (MessageDispatch) iterator.next();
            if (key.equals(candidate.getMessage().getMessageId())) {
                this.session.connection.rollbackDuplicate(this, candidate.getMessage());
                iterator.remove();
                return;
            }
        }
    }

    private void clearPreviouslyDelivered() {
        if (this.previouslyDeliveredMessages != null) {
            this.previouslyDeliveredMessages.clear();
            this.previouslyDeliveredMessages = null;
        }
    }

    public void dispatch(MessageDispatch md) {
        MessageListener listener = (MessageListener) this.messageListener.get();
        try {
            clearMessagesInProgress();
            clearDispatchList();
            synchronized (this.unconsumedMessages.getMutex()) {
                if (!this.unconsumedMessages.isClosed()) {
                    if (this.info.isBrowser() || !this.session.connection.isDuplicate(this, md.getMessage())) {
                        if (listener == null || !this.unconsumedMessages.isRunning()) {
                            if (!this.unconsumedMessages.isRunning()) {
                                this.session.connection.rollbackDuplicate(this, md.getMessage());
                            }
                            this.unconsumedMessages.enqueue(md);
                            if (this.availableListener != null) {
                                this.availableListener.onMessageAvailable(this);
                            }
                        } else {
                            ActiveMQMessage message = createActiveMQMessage(md);
                            beforeMessageIsConsumed(md);
                            try {
                                boolean expired = message.isExpired();
                                if (!expired) {
                                    listener.onMessage(message);
                                }
                                afterMessageIsConsumed(md, expired);
                            } catch (RuntimeException e) {
                                LOG.error(getConsumerId() + " Exception while processing message: " + md.getMessage().getMessageId(), e);
                                if (isAutoAcknowledgeBatch() || isAutoAcknowledgeEach() || this.session.isIndividualAcknowledge()) {
                                    md.setRollbackCause(e);
                                    rollback();
                                } else {
                                    afterMessageIsConsumed(md, false);
                                }
                            }
                        }
                    } else if (this.session.isTransacted()) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(getConsumerId() + " tracking transacted redelivery of duplicate: " + md.getMessage());
                        }
                        boolean needsPoisonAck = false;
                        synchronized (this.deliveredMessages) {
                            if (this.previouslyDeliveredMessages != null) {
                                this.previouslyDeliveredMessages.put(md.getMessage().getMessageId(), Boolean.valueOf(true));
                            } else {
                                needsPoisonAck = true;
                            }
                        }
                        if (needsPoisonAck) {
                            MessageAck poisonAck = new MessageAck(md, (byte) 1, 1);
                            poisonAck.setFirstMessageId(md.getMessage().getMessageId());
                            poisonAck.setPoisonCause(new JMSException("Duplicate dispatch with transacted redeliver pending on another consumer, connection: " + this.session.getConnection().getConnectionInfo().getConnectionId()));
                            LOG.warn("acking duplicate delivery as poison, redelivery must be pending to another consumer on this connection, failoverRedeliveryWaitPeriod=" + this.failoverRedeliveryWaitPeriod + ". Message: " + md + ", poisonAck: " + poisonAck);
                            this.session.sendAck(poisonAck);
                        } else if (this.transactedIndividualAck) {
                            immediateIndividualTransactedAck(md);
                        } else {
                            this.session.sendAck(new MessageAck(md, (byte) 0, 1));
                        }
                    } else {
                        LOG.warn("Duplicate dispatch on connection: " + this.session.getConnection().getConnectionInfo().getConnectionId() + " to consumer: " + getConsumerId() + ", ignoring (auto acking) duplicate: " + md);
                        this.session.sendAck(new MessageAck(md, (byte) 4, 1));
                    }
                }
            }
            int i = this.dispatchedCount + 1;
            this.dispatchedCount = i;
            if (i % ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH == 0) {
                this.dispatchedCount = 0;
                Thread.yield();
            }
        } catch (RuntimeException e2) {
            this.session.connection.onClientInternalException(e2);
        }
    }

    private void clearDispatchList() {
        if (this.clearDispatchList) {
            synchronized (this.deliveredMessages) {
                if (this.clearDispatchList) {
                    if (!this.deliveredMessages.isEmpty()) {
                        if (this.session.isTransacted()) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(getConsumerId() + " tracking existing transacted delivered list (" + this.deliveredMessages.size() + ") on transport interrupt");
                            }
                            if (this.previouslyDeliveredMessages == null) {
                                this.previouslyDeliveredMessages = new PreviouslyDeliveredMap(this.session.getTransactionContext().getTransactionId());
                            }
                            Iterator i$ = this.deliveredMessages.iterator();
                            while (i$.hasNext()) {
                                this.previouslyDeliveredMessages.put(((MessageDispatch) i$.next()).getMessage().getMessageId(), Boolean.valueOf(false));
                            }
                        } else {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(getConsumerId() + " clearing delivered list (" + this.deliveredMessages.size() + ") on transport interrupt");
                            }
                            this.deliveredMessages.clear();
                            this.pendingAck = null;
                        }
                    }
                    this.clearDispatchList = false;
                }
            }
        }
    }

    public int getMessageSize() {
        return this.unconsumedMessages.size();
    }

    public void start() throws JMSException {
        if (!this.unconsumedMessages.isClosed()) {
            this.started.set(true);
            this.unconsumedMessages.start();
            this.session.executor.wakeup();
        }
    }

    public void stop() {
        this.started.set(false);
        this.unconsumedMessages.stop();
    }

    public String toString() {
        return "ActiveMQMessageConsumer { value=" + this.info.getConsumerId() + ", started=" + this.started.get() + " }";
    }

    public boolean iterate() {
        if (((MessageListener) this.messageListener.get()) != null) {
            MessageDispatch md = this.unconsumedMessages.dequeueNoWait();
            if (md != null) {
                dispatch(md);
                return true;
            }
        }
        return false;
    }

    public boolean isInUse(ActiveMQTempDestination destination) {
        return this.info.getDestination().equals(destination);
    }

    public long getLastDeliveredSequenceId() {
        return this.lastDeliveredSequenceId;
    }

    public IOException getFailureError() {
        return this.failureError;
    }

    public void setFailureError(IOException failureError) {
        this.failureError = failureError;
    }

    public long getOptimizedAckScheduledAckInterval() {
        return this.optimizedAckScheduledAckInterval;
    }

    public void setOptimizedAckScheduledAckInterval(long optimizedAckScheduledAckInterval) throws JMSException {
        this.optimizedAckScheduledAckInterval = optimizedAckScheduledAckInterval;
        if (this.optimizedAckTask != null) {
            try {
                this.session.connection.getScheduler().cancel(this.optimizedAckTask);
                this.optimizedAckTask = null;
            } catch (JMSException e) {
                LOG.debug("Caught exception while cancelling old optimized ack task", e);
                throw e;
            }
        }
        if (this.optimizeAcknowledge && this.optimizedAckScheduledAckInterval > 0) {
            this.optimizedAckTask = new 8();
            try {
                this.session.connection.getScheduler().executePeriodically(this.optimizedAckTask, optimizedAckScheduledAckInterval);
            } catch (JMSException e2) {
                LOG.debug("Caught exception while scheduling new optimized ack task", e2);
                throw e2;
            }
        }
    }
}
