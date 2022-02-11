package org.apache.activemq;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.IllegalStateException;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import org.apache.activemq.blob.BlobDownloader;
import org.apache.activemq.blob.BlobTransferPolicy;
import org.apache.activemq.blob.BlobUploader;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQStreamMessage;
import org.apache.activemq.command.ActiveMQTempDestination;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.RemoveInfo;
import org.apache.activemq.command.Response;
import org.apache.activemq.command.SessionId;
import org.apache.activemq.command.SessionInfo;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.management.JMSSessionStatsImpl;
import org.apache.activemq.management.StatsCapable;
import org.apache.activemq.management.StatsImpl;
import org.apache.activemq.thread.Scheduler;
import org.apache.activemq.transaction.Synchronization;
import org.apache.activemq.usage.MemoryUsage;
import org.apache.activemq.util.Callback;
import org.apache.activemq.util.JMSExceptionSupport;
import org.apache.activemq.util.LongSequenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQSession implements Session, QueueSession, TopicSession, StatsCapable, ActiveMQDispatcher {
    public static final int INDIVIDUAL_ACKNOWLEDGE = 4;
    private static final Logger LOG;
    public static final int MAX_ACK_CONSTANT = 4;
    protected int acknowledgementMode;
    protected boolean asyncDispatch;
    private BlobTransferPolicy blobTransferPolicy;
    protected boolean closed;
    protected final ActiveMQConnection connection;
    private final ThreadPoolExecutor connectionExecutor;
    protected final LongSequenceGenerator consumerIdGenerator;
    protected final CopyOnWriteArrayList<ActiveMQMessageConsumer> consumers;
    protected final boolean debug;
    protected final LongSequenceGenerator deliveryIdGenerator;
    private DeliveryListener deliveryListener;
    protected final ActiveMQSessionExecutor executor;
    protected final SessionInfo info;
    private long lastDeliveredSequenceId;
    private MessageListener messageListener;
    protected final LongSequenceGenerator producerIdGenerator;
    protected final CopyOnWriteArrayList<ActiveMQMessageProducer> producers;
    protected Object sendMutex;
    protected boolean sessionAsyncDispatch;
    protected final AtomicBoolean started;
    private final JMSSessionStatsImpl stats;
    private volatile boolean synchronizationRegistered;
    private TransactionContext transactionContext;
    private MessageTransformer transformer;

    class 1 extends Synchronization {
        1() {
        }

        public void afterCommit() throws Exception {
            ActiveMQSession.this.doClose();
            ActiveMQSession.this.synchronizationRegistered = false;
        }

        public void afterRollback() throws Exception {
            ActiveMQSession.this.doClose();
            ActiveMQSession.this.synchronizationRegistered = false;
        }
    }

    class 2 implements Runnable {
        final /* synthetic */ ActiveMQMessageConsumer val$consumer;

        2(ActiveMQMessageConsumer activeMQMessageConsumer) {
            this.val$consumer = activeMQMessageConsumer;
        }

        public void run() {
            this.val$consumer.clearMessagesInProgress();
        }
    }

    class 3 implements Callback {
        3() {
        }

        public void execute() throws Exception {
        }
    }

    class 4 extends Synchronization {
        final /* synthetic */ MessageDispatch val$md;

        class 1 implements Runnable {
            1() {
            }

            public void run() {
                ((ActiveMQDispatcher) 4.this.val$md.getConsumer()).dispatch(4.this.val$md);
            }
        }

        4(MessageDispatch messageDispatch) {
            this.val$md = messageDispatch;
        }

        public void afterRollback() throws Exception {
            this.val$md.getMessage().onMessageRolledBack();
            ActiveMQSession.this.connection.rollbackDuplicate(ActiveMQSession.this, this.val$md.getMessage());
            RedeliveryPolicy redeliveryPolicy = ActiveMQSession.this.connection.getRedeliveryPolicy();
            int redeliveryCounter = this.val$md.getMessage().getRedeliveryCounter();
            if (redeliveryPolicy.getMaximumRedeliveries() == -1 || redeliveryCounter <= redeliveryPolicy.getMaximumRedeliveries()) {
                MessageAck ack = new MessageAck(this.val$md, (byte) 3, 1);
                ack.setFirstMessageId(this.val$md.getMessage().getMessageId());
                ActiveMQSession.this.asyncSendPacket(ack);
                long redeliveryDelay = redeliveryPolicy.getInitialRedeliveryDelay();
                for (int i = 0; i < redeliveryCounter; i++) {
                    redeliveryDelay = redeliveryPolicy.getNextRedeliveryDelay(redeliveryDelay);
                }
                ActiveMQSession.this.connection.getScheduler().executeAfterDelay(new 1(), redeliveryDelay);
                return;
            }
            ack = new MessageAck(this.val$md, (byte) 1, 1);
            ack.setFirstMessageId(this.val$md.getMessage().getMessageId());
            ActiveMQSession.this.asyncSendPacket(ack);
        }
    }

    public interface DeliveryListener {
        void afterDelivery(ActiveMQSession activeMQSession, Message message);

        void beforeDelivery(ActiveMQSession activeMQSession, Message message);
    }

    static {
        LOG = LoggerFactory.getLogger(ActiveMQSession.class);
    }

    protected ActiveMQSession(ActiveMQConnection connection, SessionId sessionId, int acknowledgeMode, boolean asyncDispatch, boolean sessionAsyncDispatch) throws JMSException {
        this.consumerIdGenerator = new LongSequenceGenerator();
        this.producerIdGenerator = new LongSequenceGenerator();
        this.deliveryIdGenerator = new LongSequenceGenerator();
        this.started = new AtomicBoolean(false);
        this.consumers = new CopyOnWriteArrayList();
        this.producers = new CopyOnWriteArrayList();
        this.sendMutex = new Object();
        this.debug = LOG.isDebugEnabled();
        this.connection = connection;
        this.acknowledgementMode = acknowledgeMode;
        this.asyncDispatch = asyncDispatch;
        this.sessionAsyncDispatch = sessionAsyncDispatch;
        this.info = new SessionInfo(connection.getConnectionInfo(), sessionId.getValue());
        setTransactionContext(new TransactionContext(connection));
        this.stats = new JMSSessionStatsImpl(this.producers, this.consumers);
        this.connection.asyncSendPacket(this.info);
        setTransformer(connection.getTransformer());
        setBlobTransferPolicy(connection.getBlobTransferPolicy());
        this.connectionExecutor = connection.getExecutor();
        this.executor = new ActiveMQSessionExecutor(this);
        connection.addSession(this);
        if (connection.isStarted()) {
            start();
        }
    }

    protected ActiveMQSession(ActiveMQConnection connection, SessionId sessionId, int acknowledgeMode, boolean asyncDispatch) throws JMSException {
        this(connection, sessionId, acknowledgeMode, asyncDispatch, true);
    }

    public void setTransactionContext(TransactionContext transactionContext) {
        this.transactionContext = transactionContext;
    }

    public TransactionContext getTransactionContext() {
        return this.transactionContext;
    }

    public StatsImpl getStats() {
        return this.stats;
    }

    public JMSSessionStatsImpl getSessionStats() {
        return this.stats;
    }

    public BytesMessage createBytesMessage() throws JMSException {
        ActiveMQBytesMessage message = new ActiveMQBytesMessage();
        configureMessage(message);
        return message;
    }

    public MapMessage createMapMessage() throws JMSException {
        ActiveMQMapMessage message = new ActiveMQMapMessage();
        configureMessage(message);
        return message;
    }

    public Message createMessage() throws JMSException {
        ActiveMQMessage message = new ActiveMQMessage();
        configureMessage(message);
        return message;
    }

    public ObjectMessage createObjectMessage() throws JMSException {
        ActiveMQObjectMessage message = new ActiveMQObjectMessage();
        configureMessage(message);
        return message;
    }

    public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
        ActiveMQObjectMessage message = new ActiveMQObjectMessage();
        configureMessage(message);
        message.setObject(object);
        return message;
    }

    public StreamMessage createStreamMessage() throws JMSException {
        ActiveMQStreamMessage message = new ActiveMQStreamMessage();
        configureMessage(message);
        return message;
    }

    public TextMessage createTextMessage() throws JMSException {
        ActiveMQTextMessage message = new ActiveMQTextMessage();
        configureMessage(message);
        return message;
    }

    public TextMessage createTextMessage(String text) throws JMSException {
        ActiveMQTextMessage message = new ActiveMQTextMessage();
        message.setText(text);
        configureMessage(message);
        return message;
    }

    public BlobMessage createBlobMessage(URL url) throws JMSException {
        return createBlobMessage(url, false);
    }

    public BlobMessage createBlobMessage(URL url, boolean deletedByBroker) throws JMSException {
        ActiveMQBlobMessage message = new ActiveMQBlobMessage();
        configureMessage(message);
        message.setURL(url);
        message.setDeletedByBroker(deletedByBroker);
        message.setBlobDownloader(new BlobDownloader(getBlobTransferPolicy()));
        return message;
    }

    public BlobMessage createBlobMessage(File file) throws JMSException {
        ActiveMQBlobMessage message = new ActiveMQBlobMessage();
        configureMessage(message);
        message.setBlobUploader(new BlobUploader(getBlobTransferPolicy(), file));
        message.setBlobDownloader(new BlobDownloader(getBlobTransferPolicy()));
        message.setDeletedByBroker(true);
        message.setName(file.getName());
        return message;
    }

    public BlobMessage createBlobMessage(InputStream in) throws JMSException {
        ActiveMQBlobMessage message = new ActiveMQBlobMessage();
        configureMessage(message);
        message.setBlobUploader(new BlobUploader(getBlobTransferPolicy(), in));
        message.setBlobDownloader(new BlobDownloader(getBlobTransferPolicy()));
        message.setDeletedByBroker(true);
        return message;
    }

    public boolean getTransacted() throws JMSException {
        checkClosed();
        return isTransacted();
    }

    public int getAcknowledgeMode() throws JMSException {
        checkClosed();
        return this.acknowledgementMode;
    }

    public void commit() throws JMSException {
        checkClosed();
        if (getTransacted()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(getSessionId() + " Transaction Commit :" + this.transactionContext.getTransactionId());
            }
            this.transactionContext.commit();
            return;
        }
        throw new IllegalStateException("Not a transacted session");
    }

    public void rollback() throws JMSException {
        checkClosed();
        if (getTransacted()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(getSessionId() + " Transaction Rollback, txid:" + this.transactionContext.getTransactionId());
            }
            this.transactionContext.rollback();
            return;
        }
        throw new IllegalStateException("Not a transacted session");
    }

    public void close() throws JMSException {
        if (!this.closed) {
            if (!getTransactionContext().isInXATransaction()) {
                doClose();
            } else if (!this.synchronizationRegistered) {
                this.synchronizationRegistered = true;
                getTransactionContext().addSynchronization(new 1());
            }
        }
    }

    private void doClose() throws JMSException {
        boolean interrupted = Thread.interrupted();
        dispose();
        RemoveInfo removeCommand = this.info.createRemoveCommand();
        removeCommand.setLastDeliveredSequenceId(this.lastDeliveredSequenceId);
        this.connection.asyncSendPacket(removeCommand);
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    void clearMessagesInProgress() {
        this.executor.clearMessagesInProgress();
        Iterator i$ = this.consumers.iterator();
        while (i$.hasNext()) {
            ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer) i$.next();
            consumer.inProgressClearRequired();
            try {
                this.connection.getScheduler().executeAfterDelay(new 2(consumer), 0);
            } catch (JMSException e) {
                this.connection.onClientInternalException(e);
            }
        }
    }

    void deliverAcks() {
        Iterator<ActiveMQMessageConsumer> iter = this.consumers.iterator();
        while (iter.hasNext()) {
            ((ActiveMQMessageConsumer) iter.next()).deliverAcks();
        }
    }

    public synchronized void dispose() throws JMSException {
        if (!this.closed) {
            try {
                this.executor.stop();
                Iterator<ActiveMQMessageConsumer> iter = this.consumers.iterator();
                while (iter.hasNext()) {
                    ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer) iter.next();
                    consumer.setFailureError(this.connection.getFirstFailureError());
                    consumer.dispose();
                    this.lastDeliveredSequenceId = Math.max(this.lastDeliveredSequenceId, consumer.getLastDeliveredSequenceId());
                }
                this.consumers.clear();
                Iterator<ActiveMQMessageProducer> iter2 = this.producers.iterator();
                while (iter2.hasNext()) {
                    ((ActiveMQMessageProducer) iter2.next()).dispose();
                }
                this.producers.clear();
                try {
                    if (getTransactionContext().isInLocalTransaction()) {
                        rollback();
                    }
                } catch (JMSException e) {
                }
                this.connection.removeSession(this);
                this.transactionContext = null;
                this.closed = true;
            } catch (Throwable th) {
                this.connection.removeSession(this);
                this.transactionContext = null;
                this.closed = true;
            }
        }
    }

    protected void configureMessage(ActiveMQMessage message) throws IllegalStateException {
        checkClosed();
        message.setConnection(this.connection);
    }

    protected void checkClosed() throws IllegalStateException {
        if (this.closed) {
            throw new IllegalStateException("The Session is closed");
        }
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void recover() throws JMSException {
        checkClosed();
        if (getTransacted()) {
            throw new IllegalStateException("This session is transacted");
        }
        Iterator<ActiveMQMessageConsumer> iter = this.consumers.iterator();
        while (iter.hasNext()) {
            ((ActiveMQMessageConsumer) iter.next()).rollback();
        }
    }

    public MessageListener getMessageListener() throws JMSException {
        checkClosed();
        return this.messageListener;
    }

    public void setMessageListener(MessageListener listener) throws JMSException {
        if (listener != null) {
            checkClosed();
        }
        this.messageListener = listener;
        if (listener != null) {
            this.executor.setDispatchedBySessionPool(true);
        }
    }

    public void run() {
        while (true) {
            MessageDispatch messageDispatch = this.executor.dequeueNoWait();
            if (messageDispatch != null) {
                MessageDispatch md = messageDispatch;
                ActiveMQMessage message = (ActiveMQMessage) md.getMessage();
                if (!(message.isExpired() || this.connection.isDuplicate(this, message))) {
                    if (isClientAcknowledge() || isIndividualAcknowledge()) {
                        message.setAcknowledgeCallback(new 3());
                    }
                    if (this.deliveryListener != null) {
                        this.deliveryListener.beforeDelivery(this, message);
                    }
                    md.setDeliverySequenceId(getNextDeliveryId());
                    try {
                        this.messageListener.onMessage(message);
                    } catch (RuntimeException e) {
                        LOG.error("error dispatching message: ", e);
                        this.connection.onClientInternalException(e);
                    }
                    try {
                        MessageAck ack = new MessageAck(md, (byte) 2, 1);
                        ack.setFirstMessageId(md.getMessage().getMessageId());
                        doStartTransaction();
                        ack.setTransactionId(getTransactionContext().getTransactionId());
                        if (ack.getTransactionId() != null) {
                            getTransactionContext().addSynchronization(new 4(md));
                        }
                        asyncSendPacket(ack);
                    } catch (Throwable e2) {
                        this.connection.onClientInternalException(e2);
                    }
                    if (this.deliveryListener != null) {
                        this.deliveryListener.afterDelivery(this, message);
                    }
                }
            } else {
                return;
            }
        }
    }

    public MessageProducer createProducer(Destination destination) throws JMSException {
        checkClosed();
        if (destination instanceof CustomDestination) {
            return ((CustomDestination) destination).createProducer(this);
        }
        return new ActiveMQMessageProducer(this, getNextProducerId(), ActiveMQMessageTransformation.transformDestination(destination), this.connection.getSendTimeout());
    }

    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        return createConsumer(destination, (String) null);
    }

    public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
        return createConsumer(destination, messageSelector, false);
    }

    public MessageConsumer createConsumer(Destination destination, MessageListener messageListener) throws JMSException {
        return createConsumer(destination, null, messageListener);
    }

    public MessageConsumer createConsumer(Destination destination, String messageSelector, MessageListener messageListener) throws JMSException {
        return createConsumer(destination, messageSelector, false, messageListener);
    }

    public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) throws JMSException {
        return createConsumer(destination, messageSelector, noLocal, null);
    }

    public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal, MessageListener messageListener) throws JMSException {
        checkClosed();
        if (destination instanceof CustomDestination) {
            return ((CustomDestination) destination).createConsumer(this, messageSelector, noLocal);
        }
        int prefetch;
        ActiveMQPrefetchPolicy prefetchPolicy = this.connection.getPrefetchPolicy();
        if (destination instanceof Topic) {
            prefetch = prefetchPolicy.getTopicPrefetch();
        } else {
            prefetch = prefetchPolicy.getQueuePrefetch();
        }
        return new ActiveMQMessageConsumer(this, getNextConsumerId(), ActiveMQMessageTransformation.transformDestination(destination), null, messageSelector, prefetch, prefetchPolicy.getMaximumPendingMessageLimit(), noLocal, false, isAsyncDispatch(), messageListener);
    }

    public Queue createQueue(String queueName) throws JMSException {
        checkClosed();
        if (queueName.startsWith(ActiveMQDestination.TEMP_DESTINATION_NAME_PREFIX)) {
            return new ActiveMQTempQueue(queueName);
        }
        return new ActiveMQQueue(queueName);
    }

    public Topic createTopic(String topicName) throws JMSException {
        checkClosed();
        if (topicName.startsWith(ActiveMQDestination.TEMP_DESTINATION_NAME_PREFIX)) {
            return new ActiveMQTempTopic(topicName);
        }
        return new ActiveMQTopic(topicName);
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
        checkClosed();
        return createDurableSubscriber(topic, name, null, false);
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
        checkClosed();
        if (topic == null) {
            throw new InvalidDestinationException("Topic cannot be null");
        } else if (isIndividualAcknowledge()) {
            throw JMSExceptionSupport.create("Cannot create a durable consumer for a Session in INDIVIDUAL_ACKNOWLEDGE mode.", null);
        } else if (topic instanceof CustomDestination) {
            return ((CustomDestination) topic).createDurableSubscriber(this, name, messageSelector, noLocal);
        } else {
            this.connection.checkClientIDWasManuallySpecified();
            ActiveMQPrefetchPolicy prefetchPolicy = this.connection.getPrefetchPolicy();
            int prefetch = (isAutoAcknowledge() && this.connection.isOptimizedMessageDispatch()) ? prefetchPolicy.getOptimizeDurableTopicPrefetch() : prefetchPolicy.getDurableTopicPrefetch();
            return new ActiveMQTopicSubscriber(this, getNextConsumerId(), ActiveMQMessageTransformation.transformDestination(topic), name, messageSelector, prefetch, prefetchPolicy.getMaximumPendingMessageLimit(), noLocal, false, this.asyncDispatch);
        }
    }

    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        checkClosed();
        return createBrowser(queue, null);
    }

    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
        checkClosed();
        return new ActiveMQQueueBrowser(this, getNextConsumerId(), ActiveMQMessageTransformation.transformDestination(queue), messageSelector, this.asyncDispatch);
    }

    public TemporaryQueue createTemporaryQueue() throws JMSException {
        checkClosed();
        return (TemporaryQueue) this.connection.createTempDestination(false);
    }

    public TemporaryTopic createTemporaryTopic() throws JMSException {
        checkClosed();
        return (TemporaryTopic) this.connection.createTempDestination(true);
    }

    public QueueReceiver createReceiver(Queue queue) throws JMSException {
        checkClosed();
        return createReceiver(queue, null);
    }

    public QueueReceiver createReceiver(Queue queue, String messageSelector) throws JMSException {
        checkClosed();
        if (queue instanceof CustomDestination) {
            return ((CustomDestination) queue).createReceiver(this, messageSelector);
        }
        ActiveMQPrefetchPolicy prefetchPolicy = this.connection.getPrefetchPolicy();
        return new ActiveMQQueueReceiver(this, getNextConsumerId(), ActiveMQMessageTransformation.transformDestination(queue), messageSelector, prefetchPolicy.getQueuePrefetch(), prefetchPolicy.getMaximumPendingMessageLimit(), this.asyncDispatch);
    }

    public QueueSender createSender(Queue queue) throws JMSException {
        checkClosed();
        if (queue instanceof CustomDestination) {
            return ((CustomDestination) queue).createSender(this);
        }
        return new ActiveMQQueueSender(this, ActiveMQMessageTransformation.transformDestination(queue), this.connection.getSendTimeout());
    }

    public TopicSubscriber createSubscriber(Topic topic) throws JMSException {
        checkClosed();
        return createSubscriber(topic, null, false);
    }

    public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException {
        checkClosed();
        if (topic instanceof CustomDestination) {
            return ((CustomDestination) topic).createSubscriber(this, messageSelector, noLocal);
        }
        ActiveMQPrefetchPolicy prefetchPolicy = this.connection.getPrefetchPolicy();
        return new ActiveMQTopicSubscriber(this, getNextConsumerId(), ActiveMQMessageTransformation.transformDestination(topic), null, messageSelector, prefetchPolicy.getTopicPrefetch(), prefetchPolicy.getMaximumPendingMessageLimit(), noLocal, false, this.asyncDispatch);
    }

    public TopicPublisher createPublisher(Topic topic) throws JMSException {
        checkClosed();
        if (topic instanceof CustomDestination) {
            return ((CustomDestination) topic).createPublisher(this);
        }
        return new ActiveMQTopicPublisher(this, ActiveMQMessageTransformation.transformDestination(topic), this.connection.getSendTimeout());
    }

    public void unsubscribe(String name) throws JMSException {
        checkClosed();
        this.connection.unsubscribe(name);
    }

    public void dispatch(MessageDispatch messageDispatch) {
        try {
            this.executor.execute(messageDispatch);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.connection.onClientInternalException(e);
        }
    }

    public void acknowledge() throws JMSException {
        Iterator<ActiveMQMessageConsumer> iter = this.consumers.iterator();
        while (iter.hasNext()) {
            ((ActiveMQMessageConsumer) iter.next()).acknowledge();
        }
    }

    protected void addConsumer(ActiveMQMessageConsumer consumer) throws JMSException {
        this.consumers.add(consumer);
        if (consumer.isDurableSubscriber()) {
            this.stats.onCreateDurableSubscriber();
        }
        this.connection.addDispatcher(consumer.getConsumerId(), this);
    }

    protected void removeConsumer(ActiveMQMessageConsumer consumer) {
        this.connection.removeDispatcher(consumer.getConsumerId());
        if (consumer.isDurableSubscriber()) {
            this.stats.onRemoveDurableSubscriber();
        }
        this.consumers.remove(consumer);
        this.connection.removeDispatcher((ActiveMQDispatcher) consumer);
    }

    protected void addProducer(ActiveMQMessageProducer producer) throws JMSException {
        this.producers.add(producer);
        this.connection.addProducer(producer.getProducerInfo().getProducerId(), producer);
    }

    protected void removeProducer(ActiveMQMessageProducer producer) {
        this.connection.removeProducer(producer.getProducerInfo().getProducerId());
        this.producers.remove(producer);
    }

    protected void start() throws JMSException {
        this.started.set(true);
        Iterator<ActiveMQMessageConsumer> iter = this.consumers.iterator();
        while (iter.hasNext()) {
            ((ActiveMQMessageConsumer) iter.next()).start();
        }
        this.executor.start();
    }

    protected void stop() throws JMSException {
        Iterator<ActiveMQMessageConsumer> iter = this.consumers.iterator();
        while (iter.hasNext()) {
            ((ActiveMQMessageConsumer) iter.next()).stop();
        }
        this.started.set(false);
        this.executor.stop();
    }

    protected SessionId getSessionId() {
        return this.info.getSessionId();
    }

    protected ConsumerId getNextConsumerId() {
        return new ConsumerId(this.info.getSessionId(), this.consumerIdGenerator.getNextSequenceId());
    }

    protected ProducerId getNextProducerId() {
        return new ProducerId(this.info.getSessionId(), this.producerIdGenerator.getNextSequenceId());
    }

    protected void send(ActiveMQMessageProducer producer, ActiveMQDestination destination, Message message, int deliveryMode, int priority, long timeToLive, MemoryUsage producerWindow, int sendTimeout, AsyncCallback onComplete) throws JMSException {
        checkClosed();
        if (destination.isTemporary() && this.connection.isDeleted(destination)) {
            throw new InvalidDestinationException("Cannot publish to a deleted Destination: " + destination);
        }
        synchronized (this.sendMutex) {
            doStartTransaction();
            TransactionId txid = this.transactionContext.getTransactionId();
            long sequenceNumber = producer.getMessageSequence();
            message.setJMSDeliveryMode(deliveryMode);
            long expiration = 0;
            if (!producer.getDisableMessageTimestamp()) {
                long timeStamp = System.currentTimeMillis();
                message.setJMSTimestamp(timeStamp);
                if (timeToLive > 0) {
                    expiration = timeToLive + timeStamp;
                }
            }
            message.setJMSExpiration(expiration);
            message.setJMSPriority(priority);
            message.setJMSRedelivered(false);
            Command msg = ActiveMQMessageTransformation.transformMessage(message, this.connection);
            msg.setDestination(destination);
            msg.setMessageId(new MessageId(producer.getProducerInfo().getProducerId(), sequenceNumber));
            if (msg != message) {
                message.setJMSMessageID(msg.getMessageId().toString());
                message.setJMSDestination(destination);
            }
            msg.setBrokerPath(null);
            msg.setTransactionId(txid);
            if (this.connection.isCopyMessageOnSend()) {
                msg = (ActiveMQMessage) msg.copy();
            }
            msg.setConnection(this.connection);
            msg.onSend();
            msg.setProducerId(msg.getMessageId().getProducerId());
            if (LOG.isTraceEnabled()) {
                LOG.trace(getSessionId() + " sending message: " + msg);
            }
            if (onComplete == null && sendTimeout <= 0 && !msg.isResponseRequired() && !this.connection.isAlwaysSyncSend() && (!msg.isPersistent() || this.connection.isUseAsyncSend() || txid != null)) {
                this.connection.asyncSendPacket(msg);
                if (producerWindow != null) {
                    producerWindow.increaseUsage((long) msg.getSize());
                }
            } else if (sendTimeout <= 0 || onComplete != null) {
                this.connection.syncSendPacket(msg, onComplete);
            } else {
                this.connection.syncSendPacket(msg, sendTimeout);
            }
        }
    }

    protected void doStartTransaction() throws JMSException {
        if (getTransacted() && !this.transactionContext.isInXATransaction()) {
            this.transactionContext.begin();
        }
    }

    public boolean hasUncomsumedMessages() {
        return this.executor.hasUncomsumedMessages();
    }

    public boolean isTransacted() {
        return this.acknowledgementMode == 0 || this.transactionContext.isInXATransaction();
    }

    protected boolean isClientAcknowledge() {
        return this.acknowledgementMode == 2;
    }

    public boolean isAutoAcknowledge() {
        return this.acknowledgementMode == 1;
    }

    public boolean isDupsOkAcknowledge() {
        return this.acknowledgementMode == 3;
    }

    public boolean isIndividualAcknowledge() {
        return this.acknowledgementMode == MAX_ACK_CONSTANT;
    }

    public DeliveryListener getDeliveryListener() {
        return this.deliveryListener;
    }

    public void setDeliveryListener(DeliveryListener deliveryListener) {
        this.deliveryListener = deliveryListener;
    }

    protected SessionInfo getSessionInfo() throws JMSException {
        return new SessionInfo(this.connection.getConnectionInfo(), getSessionId().getValue());
    }

    public void asyncSendPacket(Command command) throws JMSException {
        this.connection.asyncSendPacket(command);
    }

    public Response syncSendPacket(Command command) throws JMSException {
        return this.connection.syncSendPacket(command);
    }

    public long getNextDeliveryId() {
        return this.deliveryIdGenerator.getNextSequenceId();
    }

    public void redispatch(ActiveMQDispatcher dispatcher, MessageDispatchChannel unconsumedMessages) throws JMSException {
        List<MessageDispatch> c = unconsumedMessages.removeAll();
        for (MessageDispatch md : c) {
            this.connection.rollbackDuplicate(dispatcher, md.getMessage());
        }
        Collections.reverse(c);
        for (MessageDispatch md2 : c) {
            this.executor.executeFirst(md2);
        }
    }

    public boolean isRunning() {
        return this.started.get();
    }

    public boolean isAsyncDispatch() {
        return this.asyncDispatch;
    }

    public void setAsyncDispatch(boolean asyncDispatch) {
        this.asyncDispatch = asyncDispatch;
    }

    public boolean isSessionAsyncDispatch() {
        return this.sessionAsyncDispatch;
    }

    public void setSessionAsyncDispatch(boolean sessionAsyncDispatch) {
        this.sessionAsyncDispatch = sessionAsyncDispatch;
    }

    public MessageTransformer getTransformer() {
        return this.transformer;
    }

    public ActiveMQConnection getConnection() {
        return this.connection;
    }

    public void setTransformer(MessageTransformer transformer) {
        this.transformer = transformer;
    }

    public BlobTransferPolicy getBlobTransferPolicy() {
        return this.blobTransferPolicy;
    }

    public void setBlobTransferPolicy(BlobTransferPolicy blobTransferPolicy) {
        this.blobTransferPolicy = blobTransferPolicy;
    }

    public List<MessageDispatch> getUnconsumedMessages() {
        return this.executor.getUnconsumedMessages();
    }

    public String toString() {
        return "ActiveMQSession {id=" + this.info.getSessionId() + ",started=" + this.started.get() + "}";
    }

    public void checkMessageListener() throws JMSException {
        if (this.messageListener != null) {
            throw new IllegalStateException("Cannot synchronously receive a message when a MessageListener is set");
        }
        Iterator<ActiveMQMessageConsumer> i = this.consumers.iterator();
        while (i.hasNext()) {
            if (((ActiveMQMessageConsumer) i.next()).getMessageListener() != null) {
                throw new IllegalStateException("Cannot synchronously receive a message when a MessageListener is set");
            }
        }
    }

    protected void setOptimizeAcknowledge(boolean value) {
        Iterator<ActiveMQMessageConsumer> iter = this.consumers.iterator();
        while (iter.hasNext()) {
            ((ActiveMQMessageConsumer) iter.next()).setOptimizeAcknowledge(value);
        }
    }

    protected void setPrefetchSize(ConsumerId id, int prefetch) {
        Iterator<ActiveMQMessageConsumer> iter = this.consumers.iterator();
        while (iter.hasNext()) {
            ActiveMQMessageConsumer c = (ActiveMQMessageConsumer) iter.next();
            if (c.getConsumerId().equals(id)) {
                c.setPrefetchSize(prefetch);
                return;
            }
        }
    }

    protected void close(ConsumerId id) {
        Iterator<ActiveMQMessageConsumer> iter = this.consumers.iterator();
        while (iter.hasNext()) {
            ActiveMQMessageConsumer c = (ActiveMQMessageConsumer) iter.next();
            if (c.getConsumerId().equals(id)) {
                try {
                    c.close();
                } catch (JMSException e) {
                    LOG.warn("Exception closing consumer", e);
                }
                LOG.warn("Closed consumer on Command");
                return;
            }
        }
    }

    public boolean isInUse(ActiveMQTempDestination destination) {
        Iterator<ActiveMQMessageConsumer> iter = this.consumers.iterator();
        while (iter.hasNext()) {
            if (((ActiveMQMessageConsumer) iter.next()).isInUse(destination)) {
                return true;
            }
        }
        return false;
    }

    public long getLastDeliveredSequenceId() {
        return this.lastDeliveredSequenceId;
    }

    protected void sendAck(MessageAck ack) throws JMSException {
        sendAck(ack, false);
    }

    protected void sendAck(MessageAck ack, boolean lazy) throws JMSException {
        if (lazy || this.connection.isSendAcksAsync() || getTransacted()) {
            asyncSendPacket(ack);
        } else {
            syncSendPacket(ack);
        }
    }

    protected Scheduler getScheduler() throws JMSException {
        return this.connection.getScheduler();
    }

    protected ThreadPoolExecutor getConnectionExecutor() {
        return this.connectionExecutor;
    }
}
