package org.apache.activemq;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.IllegalStateException;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.XAConnection;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.blob.BlobTransferPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTempDestination;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.BrokerInfo;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.ConnectionControl;
import org.apache.activemq.command.ConnectionError;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.ConsumerControl;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.ControlCommand;
import org.apache.activemq.command.DestinationInfo;
import org.apache.activemq.command.ExceptionResponse;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.ProducerAck;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.RemoveSubscriptionInfo;
import org.apache.activemq.command.Response;
import org.apache.activemq.command.SessionId;
import org.apache.activemq.command.ShutdownInfo;
import org.apache.activemq.command.WireFormatInfo;
import org.apache.activemq.management.JMSConnectionStatsImpl;
import org.apache.activemq.management.JMSStatsImpl;
import org.apache.activemq.management.StatsCapable;
import org.apache.activemq.management.StatsImpl;
import org.apache.activemq.state.CommandVisitorAdapter;
import org.apache.activemq.thread.Scheduler;
import org.apache.activemq.thread.TaskRunnerFactory;
import org.apache.activemq.transport.FutureResponse;
import org.apache.activemq.transport.ResponseCallback;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportListener;
import org.apache.activemq.transport.failover.FailoverTransport;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.IdGenerator;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.JMSExceptionSupport;
import org.apache.activemq.util.LongSequenceGenerator;
import org.apache.activemq.util.ServiceSupport;
import org.apache.activemq.util.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQConnection implements Connection, TopicConnection, QueueConnection, StatsCapable, Closeable, StreamConnection, TransportListener, EnhancedConnection {
    public static final String DEFAULT_BROKER_URL = "failover://tcp://localhost:61616";
    public static final String DEFAULT_PASSWORD;
    public static int DEFAULT_THREAD_POOL_SIZE;
    public static final String DEFAULT_USER;
    private static final Logger LOG;
    public final ConcurrentHashMap<ActiveMQTempDestination, ActiveMQTempDestination> activeTempDestinations;
    private AdvisoryConsumer advisoryConsumer;
    protected boolean alwaysSessionAsync;
    private boolean alwaysSyncSend;
    private BlobTransferPolicy blobTransferPolicy;
    private BrokerInfo brokerInfo;
    private final CountDownLatch brokerInfoReceived;
    private boolean checkForDuplicates;
    private boolean clientIDSet;
    private final IdGenerator clientIdGenerator;
    private ClientInternalExceptionListener clientInternalExceptionListener;
    private int closeTimeout;
    private final AtomicBoolean closed;
    private final AtomicBoolean closing;
    private final ConnectionAudit connectionAudit;
    private final CopyOnWriteArrayList<ActiveMQConnectionConsumer> connectionConsumers;
    private final SessionId connectionSessionId;
    private long consumerFailoverRedeliveryWaitPeriod;
    private final LongSequenceGenerator consumerIdGenerator;
    private boolean copyMessageOnSend;
    private DestinationSource destinationSource;
    private boolean disableTimeStampsByDefault;
    protected boolean dispatchAsync;
    private final ConcurrentHashMap<ConsumerId, ActiveMQDispatcher> dispatchers;
    private final Object ensureConnectionInfoSentMutex;
    private ExceptionListener exceptionListener;
    private boolean exclusiveConsumer;
    private final ThreadPoolExecutor executor;
    private final JMSStatsImpl factoryStats;
    private IOException firstFailureError;
    private final ConnectionInfo info;
    private final CopyOnWriteArrayList<ActiveMQInputStream> inputStreams;
    private boolean isConnectionInfoSentToBroker;
    private final LongSequenceGenerator localTransactionIdGenerator;
    private int maxThreadPoolSize;
    private boolean messagePrioritySupported;
    private boolean nestedMapAndListEnabled;
    private boolean nonBlockingRedelivery;
    private boolean objectMessageSerializationDefered;
    private boolean optimizeAcknowledge;
    private long optimizeAcknowledgeTimeOut;
    private long optimizedAckScheduledAckInterval;
    private boolean optimizedMessageDispatch;
    private final CopyOnWriteArrayList<ActiveMQOutputStream> outputStreams;
    private ActiveMQPrefetchPolicy prefetchPolicy;
    private final LongSequenceGenerator producerIdGenerator;
    private int producerWindowSize;
    private final ConcurrentHashMap<ProducerId, ActiveMQMessageProducer> producers;
    private final AtomicInteger protocolVersion;
    private boolean queueOnlyConnection;
    private RedeliveryPolicyMap redeliveryPolicyMap;
    private RejectedExecutionHandler rejectedTaskHandler;
    private Scheduler scheduler;
    private boolean sendAcksAsync;
    private int sendTimeout;
    private final LongSequenceGenerator sessionIdGenerator;
    private TaskRunnerFactory sessionTaskRunner;
    private final CopyOnWriteArrayList<ActiveMQSession> sessions;
    private final AtomicBoolean started;
    private final JMSConnectionStatsImpl stats;
    private final LongSequenceGenerator tempDestinationIdGenerator;
    private final long timeCreated;
    private boolean transactedIndividualAck;
    private MessageTransformer transformer;
    private final Transport transport;
    private final AtomicBoolean transportFailed;
    protected volatile CountDownLatch transportInterruptionProcessingComplete;
    private final CopyOnWriteArrayList<TransportListener> transportListeners;
    private boolean useAsyncSend;
    private boolean useCompression;
    private boolean useDedicatedTaskRunner;
    private boolean useRetroactiveConsumer;
    private boolean userSpecifiedClientID;
    private long warnAboutUnstartedConnectionTimeout;
    private boolean watchTopicAdvisories;

    class 1 implements ThreadFactory {
        final /* synthetic */ Transport val$transport;

        1(Transport transport) {
            this.val$transport = transport;
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, "ActiveMQ Connection Executor: " + this.val$transport);
        }
    }

    class 2 implements ResponseCallback {
        final /* synthetic */ Command val$command;
        final /* synthetic */ AsyncCallback val$onComplete;

        2(AsyncCallback asyncCallback, Command command) {
            this.val$onComplete = asyncCallback;
            this.val$command = command;
        }

        public void onCompletion(FutureResponse resp) {
            Throwable exception = null;
            try {
                Response response = resp.getResult();
                if (response.isException()) {
                    exception = ((ExceptionResponse) response).getException();
                }
            } catch (Throwable e) {
                exception = e;
            }
            if (exception == null) {
                this.val$onComplete.onSuccess();
            } else if (exception instanceof JMSException) {
                this.val$onComplete.onException((JMSException) exception);
            } else {
                if (ActiveMQConnection.this.isClosed() || ActiveMQConnection.this.closing.get()) {
                    ActiveMQConnection.LOG.debug("Received an exception but connection is closing");
                }
                JMSException jmsEx = null;
                try {
                    jmsEx = JMSExceptionSupport.create(exception);
                } catch (Throwable e2) {
                    ActiveMQConnection.LOG.error("Caught an exception trying to create a JMSException for " + exception, e2);
                }
                if ((exception instanceof SecurityException) && (this.val$command instanceof ConnectionInfo)) {
                    Transport t = ActiveMQConnection.this.transport;
                    if (t != null) {
                        ServiceSupport.dispose(t);
                    }
                }
                if (jmsEx != null) {
                    this.val$onComplete.onException(jmsEx);
                }
            }
        }
    }

    class 3 extends CommandVisitorAdapter {
        final /* synthetic */ Command val$command;

        class 1 implements Runnable {
            final /* synthetic */ ConnectionError val$error;

            1(ConnectionError connectionError) {
                this.val$error = connectionError;
            }

            public void run() {
                ActiveMQConnection.this.onAsyncException(this.val$error.getException());
            }
        }

        3(Command command) {
            this.val$command = command;
        }

        public Response processMessageDispatch(MessageDispatch md) throws Exception {
            ActiveMQConnection.this.waitForTransportInterruptionProcessingToComplete();
            ActiveMQDispatcher dispatcher = (ActiveMQDispatcher) ActiveMQConnection.this.dispatchers.get(md.getConsumerId());
            if (dispatcher != null) {
                Message msg = md.getMessage();
                if (msg != null) {
                    msg = msg.copy();
                    msg.setReadOnlyBody(true);
                    msg.setReadOnlyProperties(true);
                    msg.setRedeliveryCounter(md.getRedeliveryCounter());
                    msg.setConnection(ActiveMQConnection.this);
                    msg.setMemoryUsage(null);
                    md.setMessage(msg);
                }
                dispatcher.dispatch(md);
            }
            return null;
        }

        public Response processProducerAck(ProducerAck pa) throws Exception {
            if (!(pa == null || pa.getProducerId() == null)) {
                ActiveMQMessageProducer producer = (ActiveMQMessageProducer) ActiveMQConnection.this.producers.get(pa.getProducerId());
                if (producer != null) {
                    producer.onProducerAck(pa);
                }
            }
            return null;
        }

        public Response processBrokerInfo(BrokerInfo info) throws Exception {
            ActiveMQConnection.this.brokerInfo = info;
            ActiveMQConnection.this.brokerInfoReceived.countDown();
            ActiveMQConnection.access$772(ActiveMQConnection.this, !ActiveMQConnection.this.brokerInfo.isFaultTolerantConfiguration() ? 1 : 0);
            ActiveMQConnection.this.getBlobTransferPolicy().setBrokerUploadUrl(info.getBrokerUploadUrl());
            return null;
        }

        public Response processConnectionError(ConnectionError error) throws Exception {
            ActiveMQConnection.this.executor.execute(new 1(error));
            return null;
        }

        public Response processControlCommand(ControlCommand command) throws Exception {
            ActiveMQConnection.this.onControlCommand(command);
            return null;
        }

        public Response processConnectionControl(ConnectionControl control) throws Exception {
            ActiveMQConnection.this.onConnectionControl((ConnectionControl) this.val$command);
            return null;
        }

        public Response processConsumerControl(ConsumerControl control) throws Exception {
            ActiveMQConnection.this.onConsumerControl((ConsumerControl) this.val$command);
            return null;
        }

        public Response processWireFormat(WireFormatInfo info) throws Exception {
            ActiveMQConnection.this.onWireFormatInfo((WireFormatInfo) this.val$command);
            return null;
        }
    }

    class 4 implements Runnable {
        final /* synthetic */ Throwable val$error;

        4(Throwable th) {
            this.val$error = th;
        }

        public void run() {
            ActiveMQConnection.this.clientInternalExceptionListener.onException(this.val$error);
        }
    }

    class 5 implements Runnable {
        final /* synthetic */ JMSException val$e;

        5(JMSException jMSException) {
            this.val$e = jMSException;
        }

        public void run() {
            ActiveMQConnection.this.exceptionListener.onException(this.val$e);
        }
    }

    class 6 implements Runnable {
        final /* synthetic */ IOException val$error;

        6(IOException iOException) {
            this.val$error = iOException;
        }

        public void run() {
            ActiveMQConnection.this.transportFailed(this.val$error);
            ServiceSupport.dispose(ActiveMQConnection.this.transport);
            ActiveMQConnection.this.brokerInfoReceived.countDown();
            try {
                ActiveMQConnection.this.cleanup();
            } catch (JMSException e) {
                ActiveMQConnection.LOG.warn("Exception during connection cleanup, " + e, e);
            }
            Iterator<TransportListener> iter = ActiveMQConnection.this.transportListeners.iterator();
            while (iter.hasNext()) {
                ((TransportListener) iter.next()).onException(this.val$error);
            }
        }
    }

    static /* synthetic */ boolean access$772(ActiveMQConnection x0, int x1) {
        boolean z = (byte) (x0.optimizeAcknowledge & x1);
        x0.optimizeAcknowledge = z;
        return z;
    }

    static {
        DEFAULT_USER = ActiveMQConnectionFactory.DEFAULT_USER;
        DEFAULT_PASSWORD = ActiveMQConnectionFactory.DEFAULT_PASSWORD;
        DEFAULT_THREAD_POOL_SIZE = ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
        LOG = LoggerFactory.getLogger(ActiveMQConnection.class);
    }

    protected ActiveMQConnection(Transport transport, IdGenerator clientIdGenerator, IdGenerator connectionIdGenerator, JMSStatsImpl factoryStats) throws Exception {
        this.activeTempDestinations = new ConcurrentHashMap();
        this.dispatchAsync = true;
        this.alwaysSessionAsync = true;
        this.prefetchPolicy = new ActiveMQPrefetchPolicy();
        this.optimizedMessageDispatch = true;
        this.copyMessageOnSend = true;
        this.optimizeAcknowledgeTimeOut = 0;
        this.optimizedAckScheduledAckInterval = 0;
        this.nestedMapAndListEnabled = true;
        this.closeTimeout = 15000;
        this.watchTopicAdvisories = true;
        this.warnAboutUnstartedConnectionTimeout = 500;
        this.sendTimeout = 0;
        this.sendAcksAsync = true;
        this.checkForDuplicates = true;
        this.queueOnlyConnection = false;
        this.started = new AtomicBoolean(false);
        this.closing = new AtomicBoolean(false);
        this.closed = new AtomicBoolean(false);
        this.transportFailed = new AtomicBoolean(false);
        this.sessions = new CopyOnWriteArrayList();
        this.connectionConsumers = new CopyOnWriteArrayList();
        this.inputStreams = new CopyOnWriteArrayList();
        this.outputStreams = new CopyOnWriteArrayList();
        this.transportListeners = new CopyOnWriteArrayList();
        this.dispatchers = new ConcurrentHashMap();
        this.producers = new ConcurrentHashMap();
        this.sessionIdGenerator = new LongSequenceGenerator();
        this.consumerIdGenerator = new LongSequenceGenerator();
        this.producerIdGenerator = new LongSequenceGenerator();
        this.tempDestinationIdGenerator = new LongSequenceGenerator();
        this.localTransactionIdGenerator = new LongSequenceGenerator();
        this.brokerInfoReceived = new CountDownLatch(1);
        this.producerWindowSize = 0;
        this.protocolVersion = new AtomicInteger(9);
        this.connectionAudit = new ConnectionAudit();
        this.ensureConnectionInfoSentMutex = new Object();
        this.messagePrioritySupported = true;
        this.transactedIndividualAck = false;
        this.nonBlockingRedelivery = false;
        this.maxThreadPoolSize = DEFAULT_THREAD_POOL_SIZE;
        this.rejectedTaskHandler = null;
        this.transport = transport;
        this.clientIdGenerator = clientIdGenerator;
        this.factoryStats = factoryStats;
        this.executor = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new LinkedBlockingQueue(), new 1(transport));
        this.info = new ConnectionInfo(new ConnectionId(connectionIdGenerator.generateId()));
        this.info.setManageable(true);
        this.info.setFaultTolerant(transport.isFaultTolerant());
        this.connectionSessionId = new SessionId(this.info.getConnectionId(), -1);
        this.transport.setTransportListener(this);
        this.stats = new JMSConnectionStatsImpl(this.sessions, this instanceof XAConnection);
        this.factoryStats.addConnection(this);
        this.timeCreated = System.currentTimeMillis();
        this.connectionAudit.setCheckForDuplicates(transport.isFaultTolerant());
    }

    protected void setUserName(String userName) {
        this.info.setUserName(userName);
    }

    protected void setPassword(String password) {
        this.info.setPassword(password);
    }

    public static ActiveMQConnection makeConnection() throws JMSException {
        return (ActiveMQConnection) new ActiveMQConnectionFactory().createConnection();
    }

    public static ActiveMQConnection makeConnection(String uri) throws JMSException, URISyntaxException {
        return (ActiveMQConnection) new ActiveMQConnectionFactory(uri).createConnection();
    }

    public static ActiveMQConnection makeConnection(String user, String password, String uri) throws JMSException, URISyntaxException {
        return (ActiveMQConnection) new ActiveMQConnectionFactory(user, password, new URI(uri)).createConnection();
    }

    public JMSConnectionStatsImpl getConnectionStats() {
        return this.stats;
    }

    public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
        checkClosedOrFailed();
        ensureConnectionInfoSent();
        if (!transacted) {
            if (acknowledgeMode == 0) {
                throw new JMSException("acknowledgeMode SESSION_TRANSACTED cannot be used for an non-transacted Session");
            } else if (acknowledgeMode < 0 || acknowledgeMode > 4) {
                throw new JMSException("invalid acknowledgeMode: " + acknowledgeMode + ". Valid values are Session.AUTO_ACKNOWLEDGE (1), " + "Session.CLIENT_ACKNOWLEDGE (2), Session.DUPS_OK_ACKNOWLEDGE (3), ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE (4) or for transacted sessions Session.SESSION_TRANSACTED (0)");
            }
        }
        SessionId nextSessionId = getNextSessionId();
        int i = transacted ? 0 : acknowledgeMode == 0 ? 1 : acknowledgeMode;
        return new ActiveMQSession(this, nextSessionId, i, isDispatchAsync(), isAlwaysSessionAsync());
    }

    protected SessionId getNextSessionId() {
        return new SessionId(this.info.getConnectionId(), this.sessionIdGenerator.getNextSequenceId());
    }

    public String getClientID() throws JMSException {
        checkClosedOrFailed();
        return this.info.getClientId();
    }

    public void setClientID(String newClientID) throws JMSException {
        checkClosedOrFailed();
        if (this.clientIDSet) {
            throw new IllegalStateException("The clientID has already been set");
        } else if (this.isConnectionInfoSentToBroker) {
            throw new IllegalStateException("Setting clientID on a used Connection is not allowed");
        } else {
            this.info.setClientId(newClientID);
            this.userSpecifiedClientID = true;
            ensureConnectionInfoSent();
        }
    }

    public void setDefaultClientID(String clientID) throws JMSException {
        this.info.setClientId(clientID);
        this.userSpecifiedClientID = true;
    }

    public ConnectionMetaData getMetaData() throws JMSException {
        checkClosedOrFailed();
        return ActiveMQConnectionMetaData.INSTANCE;
    }

    public ExceptionListener getExceptionListener() throws JMSException {
        checkClosedOrFailed();
        return this.exceptionListener;
    }

    public void setExceptionListener(ExceptionListener listener) throws JMSException {
        checkClosedOrFailed();
        this.exceptionListener = listener;
    }

    public ClientInternalExceptionListener getClientInternalExceptionListener() {
        return this.clientInternalExceptionListener;
    }

    public void setClientInternalExceptionListener(ClientInternalExceptionListener listener) {
        this.clientInternalExceptionListener = listener;
    }

    public void start() throws JMSException {
        checkClosedOrFailed();
        ensureConnectionInfoSent();
        if (this.started.compareAndSet(false, true)) {
            Iterator<ActiveMQSession> i = this.sessions.iterator();
            while (i.hasNext()) {
                ((ActiveMQSession) i.next()).start();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stop() throws javax.jms.JMSException {
        /*
        r5 = this;
        r5.checkClosedOrFailed();
        r2 = r5.started;
        r3 = 1;
        r4 = 0;
        r2 = r2.compareAndSet(r3, r4);
        if (r2 == 0) goto L_0x002a;
    L_0x000d:
        r3 = r5.sessions;
        monitor-enter(r3);
        r2 = r5.sessions;	 Catch:{ all -> 0x0026 }
        r0 = r2.iterator();	 Catch:{ all -> 0x0026 }
    L_0x0016:
        r2 = r0.hasNext();	 Catch:{ all -> 0x0026 }
        if (r2 == 0) goto L_0x0029;
    L_0x001c:
        r1 = r0.next();	 Catch:{ all -> 0x0026 }
        r1 = (org.apache.activemq.ActiveMQSession) r1;	 Catch:{ all -> 0x0026 }
        r1.stop();	 Catch:{ all -> 0x0026 }
        goto L_0x0016;
    L_0x0026:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0026 }
        throw r2;
    L_0x0029:
        monitor-exit(r3);	 Catch:{ all -> 0x0026 }
    L_0x002a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.ActiveMQConnection.stop():void");
    }

    public void close() throws JMSException {
        boolean interrupted = Thread.interrupted();
        try {
            if (!(this.closed.get() || this.transportFailed.get())) {
                stop();
            }
            synchronized (this) {
                if (!this.closed.get()) {
                    this.closing.set(true);
                    if (this.destinationSource != null) {
                        this.destinationSource.stop();
                        this.destinationSource = null;
                    }
                    if (this.advisoryConsumer != null) {
                        this.advisoryConsumer.dispose();
                        this.advisoryConsumer = null;
                    }
                    Scheduler scheduler = this.scheduler;
                    if (scheduler != null) {
                        try {
                            scheduler.stop();
                        } catch (Exception e) {
                            throw JMSExceptionSupport.create(e);
                        }
                    }
                    long lastDeliveredSequenceId = 0;
                    Iterator<ActiveMQSession> i = this.sessions.iterator();
                    while (i.hasNext()) {
                        ActiveMQSession s = (ActiveMQSession) i.next();
                        s.dispose();
                        lastDeliveredSequenceId = Math.max(lastDeliveredSequenceId, s.getLastDeliveredSequenceId());
                    }
                    Iterator<ActiveMQConnectionConsumer> i2 = this.connectionConsumers.iterator();
                    while (i2.hasNext()) {
                        ((ActiveMQConnectionConsumer) i2.next()).dispose();
                    }
                    Iterator<ActiveMQInputStream> i3 = this.inputStreams.iterator();
                    while (i3.hasNext()) {
                        ((ActiveMQInputStream) i3.next()).dispose();
                    }
                    Iterator<ActiveMQOutputStream> i4 = this.outputStreams.iterator();
                    while (i4.hasNext()) {
                        ((ActiveMQOutputStream) i4.next()).dispose();
                    }
                    this.activeTempDestinations.clear();
                    if (this.isConnectionInfoSentToBroker) {
                        this.info.createRemoveCommand().setLastDeliveredSequenceId(lastDeliveredSequenceId);
                        doSyncSendPacket(this.info.createRemoveCommand(), this.closeTimeout);
                        doAsyncSendPacket(new ShutdownInfo());
                    }
                    this.started.set(false);
                    if (this.sessionTaskRunner != null) {
                        this.sessionTaskRunner.shutdown();
                    }
                    this.closed.set(true);
                    this.closing.set(false);
                }
            }
            try {
                if (this.executor != null) {
                    ThreadPoolUtils.shutdown(this.executor);
                }
            } catch (Throwable e2) {
                LOG.warn("Error shutting down thread pool: " + this.executor + ". This exception will be ignored.", e2);
            }
            ServiceSupport.dispose(this.transport);
            this.factoryStats.removeConnection(this);
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        } catch (Throwable th) {
            try {
                if (this.executor != null) {
                    ThreadPoolUtils.shutdown(this.executor);
                }
            } catch (Throwable e22) {
                LOG.warn("Error shutting down thread pool: " + this.executor + ". This exception will be ignored.", e22);
            }
            ServiceSupport.dispose(this.transport);
            this.factoryStats.removeConnection(this);
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return createDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages, false);
    }

    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages, boolean noLocal) throws JMSException {
        checkClosedOrFailed();
        if (this.queueOnlyConnection) {
            throw new IllegalStateException("QueueConnection cannot be used to create Pub/Sub based resources.");
        }
        ensureConnectionInfoSent();
        ConsumerInfo info = new ConsumerInfo(new ConsumerId(new SessionId(this.info.getConnectionId(), -1), this.consumerIdGenerator.getNextSequenceId()));
        info.setDestination(ActiveMQMessageTransformation.transformDestination(topic));
        info.setSubscriptionName(subscriptionName);
        info.setSelector(messageSelector);
        info.setPrefetchSize(maxMessages);
        info.setDispatchAsync(isDispatchAsync());
        if (info.getDestination().getOptions() != null) {
            IntrospectionSupport.setProperties(this.info, new HashMap(info.getDestination().getOptions()), "consumer.");
        }
        return new ActiveMQConnectionConsumer(this, sessionPool, info);
    }

    public boolean isStarted() {
        return this.started.get();
    }

    public boolean isClosed() {
        return this.closed.get();
    }

    public boolean isClosing() {
        return this.closing.get();
    }

    public boolean isTransportFailed() {
        return this.transportFailed.get();
    }

    public ActiveMQPrefetchPolicy getPrefetchPolicy() {
        return this.prefetchPolicy;
    }

    public void setPrefetchPolicy(ActiveMQPrefetchPolicy prefetchPolicy) {
        this.prefetchPolicy = prefetchPolicy;
    }

    public Transport getTransportChannel() {
        return this.transport;
    }

    public String getInitializedClientID() throws JMSException {
        ensureConnectionInfoSent();
        return this.info.getClientId();
    }

    public boolean isDisableTimeStampsByDefault() {
        return this.disableTimeStampsByDefault;
    }

    public void setDisableTimeStampsByDefault(boolean timeStampsDisableByDefault) {
        this.disableTimeStampsByDefault = timeStampsDisableByDefault;
    }

    public boolean isOptimizedMessageDispatch() {
        return this.optimizedMessageDispatch;
    }

    public void setOptimizedMessageDispatch(boolean dispatchOptimizedMessage) {
        this.optimizedMessageDispatch = dispatchOptimizedMessage;
    }

    public int getCloseTimeout() {
        return this.closeTimeout;
    }

    public void setCloseTimeout(int closeTimeout) {
        this.closeTimeout = closeTimeout;
    }

    public ConnectionInfo getConnectionInfo() {
        return this.info;
    }

    public boolean isUseRetroactiveConsumer() {
        return this.useRetroactiveConsumer;
    }

    public void setUseRetroactiveConsumer(boolean useRetroactiveConsumer) {
        this.useRetroactiveConsumer = useRetroactiveConsumer;
    }

    public boolean isNestedMapAndListEnabled() {
        return this.nestedMapAndListEnabled;
    }

    public void setNestedMapAndListEnabled(boolean structuredMapsEnabled) {
        this.nestedMapAndListEnabled = structuredMapsEnabled;
    }

    public boolean isExclusiveConsumer() {
        return this.exclusiveConsumer;
    }

    public void setExclusiveConsumer(boolean exclusiveConsumer) {
        this.exclusiveConsumer = exclusiveConsumer;
    }

    public void addTransportListener(TransportListener transportListener) {
        this.transportListeners.add(transportListener);
    }

    public void removeTransportListener(TransportListener transportListener) {
        this.transportListeners.remove(transportListener);
    }

    public boolean isUseDedicatedTaskRunner() {
        return this.useDedicatedTaskRunner;
    }

    public void setUseDedicatedTaskRunner(boolean useDedicatedTaskRunner) {
        this.useDedicatedTaskRunner = useDedicatedTaskRunner;
    }

    public TaskRunnerFactory getSessionTaskRunner() {
        synchronized (this) {
            if (this.sessionTaskRunner == null) {
                this.sessionTaskRunner = new TaskRunnerFactory("ActiveMQ Session Task", 7, false, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, isUseDedicatedTaskRunner(), this.maxThreadPoolSize);
                this.sessionTaskRunner.setRejectedTaskHandler(this.rejectedTaskHandler);
            }
        }
        return this.sessionTaskRunner;
    }

    public void setSessionTaskRunner(TaskRunnerFactory sessionTaskRunner) {
        this.sessionTaskRunner = sessionTaskRunner;
    }

    public MessageTransformer getTransformer() {
        return this.transformer;
    }

    public void setTransformer(MessageTransformer transformer) {
        this.transformer = transformer;
    }

    public boolean isStatsEnabled() {
        return this.stats.isEnabled();
    }

    public void setStatsEnabled(boolean statsEnabled) {
        this.stats.setEnabled(statsEnabled);
    }

    public DestinationSource getDestinationSource() throws JMSException {
        if (this.destinationSource == null) {
            this.destinationSource = new DestinationSource(this);
            this.destinationSource.start();
        }
        return this.destinationSource;
    }

    protected void addSession(ActiveMQSession session) throws JMSException {
        this.sessions.add(session);
        if (this.sessions.size() > 1 || session.isTransacted()) {
            this.optimizedMessageDispatch = false;
        }
    }

    protected void removeSession(ActiveMQSession session) {
        this.sessions.remove(session);
        removeDispatcher((ActiveMQDispatcher) session);
    }

    protected void addConnectionConsumer(ActiveMQConnectionConsumer connectionConsumer) throws JMSException {
        this.connectionConsumers.add(connectionConsumer);
    }

    protected void removeConnectionConsumer(ActiveMQConnectionConsumer connectionConsumer) {
        this.connectionConsumers.remove(connectionConsumer);
        removeDispatcher((ActiveMQDispatcher) connectionConsumer);
    }

    public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException {
        return new ActiveMQTopicSession((ActiveMQSession) createSession(transacted, acknowledgeMode));
    }

    public ConnectionConsumer createConnectionConsumer(Topic topic, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return createConnectionConsumer(topic, messageSelector, sessionPool, maxMessages, false);
    }

    public ConnectionConsumer createConnectionConsumer(Queue queue, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return createConnectionConsumer(queue, messageSelector, sessionPool, maxMessages, false);
    }

    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return createConnectionConsumer(destination, messageSelector, sessionPool, maxMessages, false);
    }

    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages, boolean noLocal) throws JMSException {
        checkClosedOrFailed();
        ensureConnectionInfoSent();
        ConsumerInfo consumerInfo = new ConsumerInfo(createConsumerId());
        consumerInfo.setDestination(ActiveMQMessageTransformation.transformDestination(destination));
        consumerInfo.setSelector(messageSelector);
        consumerInfo.setPrefetchSize(maxMessages);
        consumerInfo.setNoLocal(noLocal);
        consumerInfo.setDispatchAsync(isDispatchAsync());
        if (consumerInfo.getDestination().getOptions() != null) {
            IntrospectionSupport.setProperties(consumerInfo, new HashMap(consumerInfo.getDestination().getOptions()), "consumer.");
        }
        return new ActiveMQConnectionConsumer(this, sessionPool, consumerInfo);
    }

    private ConsumerId createConsumerId() {
        return new ConsumerId(this.connectionSessionId, this.consumerIdGenerator.getNextSequenceId());
    }

    private ProducerId createProducerId() {
        return new ProducerId(this.connectionSessionId, this.producerIdGenerator.getNextSequenceId());
    }

    public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException {
        return new ActiveMQQueueSession((ActiveMQSession) createSession(transacted, acknowledgeMode));
    }

    public void checkClientIDWasManuallySpecified() throws JMSException {
        if (!this.userSpecifiedClientID) {
            throw new JMSException("You cannot create a durable subscriber without specifying a unique clientID on a Connection");
        }
    }

    public void asyncSendPacket(Command command) throws JMSException {
        if (isClosed()) {
            throw new ConnectionClosedException();
        }
        doAsyncSendPacket(command);
    }

    private void doAsyncSendPacket(Command command) throws JMSException {
        try {
            this.transport.oneway(command);
        } catch (Exception e) {
            throw JMSExceptionSupport.create(e);
        }
    }

    public void syncSendPacket(Command command, AsyncCallback onComplete) throws JMSException {
        if (onComplete == null) {
            syncSendPacket(command);
        } else if (isClosed()) {
            throw new ConnectionClosedException();
        } else {
            try {
                this.transport.asyncRequest(command, new 2(onComplete, command));
            } catch (Exception e) {
                throw JMSExceptionSupport.create(e);
            }
        }
    }

    public Response syncSendPacket(Command command) throws JMSException {
        if (isClosed()) {
            throw new ConnectionClosedException();
        }
        try {
            Response response = (Response) this.transport.request(command);
            if (response.isException()) {
                ExceptionResponse er = (ExceptionResponse) response;
                if (er.getException() instanceof JMSException) {
                    throw ((JMSException) er.getException());
                }
                if (isClosed() || this.closing.get()) {
                    LOG.debug("Received an exception but connection is closing");
                }
                JMSException jmsEx = null;
                try {
                    jmsEx = JMSExceptionSupport.create(er.getException());
                } catch (Throwable e) {
                    LOG.error("Caught an exception trying to create a JMSException for " + er.getException(), e);
                }
                if ((er.getException() instanceof SecurityException) && (command instanceof ConnectionInfo)) {
                    Transport t = this.transport;
                    if (t != null) {
                        ServiceSupport.dispose(t);
                    }
                }
                if (jmsEx != null) {
                    throw jmsEx;
                }
            }
            return response;
        } catch (Exception e2) {
            throw JMSExceptionSupport.create(e2);
        }
    }

    public Response syncSendPacket(Command command, int timeout) throws JMSException {
        if (!isClosed() && !this.closing.get()) {
            return doSyncSendPacket(command, timeout);
        }
        throw new ConnectionClosedException();
    }

    private Response doSyncSendPacket(Command command, int timeout) throws JMSException {
        Object request;
        if (timeout > 0) {
            try {
                request = this.transport.request(command, timeout);
            } catch (Exception e) {
                throw JMSExceptionSupport.create(e);
            }
        }
        request = this.transport.request(command);
        Response response = (Response) request;
        if (response == null || !response.isException()) {
            return response;
        }
        ExceptionResponse er = (ExceptionResponse) response;
        if (er.getException() instanceof JMSException) {
            throw ((JMSException) er.getException());
        }
        throw JMSExceptionSupport.create(er.getException());
    }

    public StatsImpl getStats() {
        return this.stats;
    }

    protected synchronized void checkClosedOrFailed() throws JMSException {
        checkClosed();
        if (this.transportFailed.get()) {
            throw new ConnectionFailedException(this.firstFailureError);
        }
    }

    protected synchronized void checkClosed() throws JMSException {
        if (this.closed.get()) {
            throw new ConnectionClosedException();
        }
    }

    protected void ensureConnectionInfoSent() throws JMSException {
        synchronized (this.ensureConnectionInfoSentMutex) {
            if (this.isConnectionInfoSentToBroker || this.closed.get()) {
                return;
            }
            if (this.info.getClientId() == null || this.info.getClientId().trim().length() == 0) {
                this.info.setClientId(this.clientIdGenerator.generateId());
            }
            syncSendPacket(this.info.copy());
            this.isConnectionInfoSentToBroker = true;
            ConsumerId consumerId = new ConsumerId(new SessionId(this.info.getConnectionId(), -1), this.consumerIdGenerator.getNextSequenceId());
            if (this.watchTopicAdvisories) {
                this.advisoryConsumer = new AdvisoryConsumer(this, consumerId);
            }
        }
    }

    public synchronized boolean isWatchTopicAdvisories() {
        return this.watchTopicAdvisories;
    }

    public synchronized void setWatchTopicAdvisories(boolean watchTopicAdvisories) {
        this.watchTopicAdvisories = watchTopicAdvisories;
    }

    public boolean isUseAsyncSend() {
        return this.useAsyncSend;
    }

    public void setUseAsyncSend(boolean useAsyncSend) {
        this.useAsyncSend = useAsyncSend;
    }

    public boolean isAlwaysSyncSend() {
        return this.alwaysSyncSend;
    }

    public void setAlwaysSyncSend(boolean alwaysSyncSend) {
        this.alwaysSyncSend = alwaysSyncSend;
    }

    public boolean isMessagePrioritySupported() {
        return this.messagePrioritySupported;
    }

    public void setMessagePrioritySupported(boolean messagePrioritySupported) {
        this.messagePrioritySupported = messagePrioritySupported;
    }

    public void cleanup() throws JMSException {
        if (!(this.advisoryConsumer == null || isTransportFailed())) {
            this.advisoryConsumer.dispose();
            this.advisoryConsumer = null;
        }
        Iterator<ActiveMQSession> i = this.sessions.iterator();
        while (i.hasNext()) {
            ((ActiveMQSession) i.next()).dispose();
        }
        Iterator<ActiveMQConnectionConsumer> i2 = this.connectionConsumers.iterator();
        while (i2.hasNext()) {
            ((ActiveMQConnectionConsumer) i2.next()).dispose();
        }
        Iterator<ActiveMQInputStream> i3 = this.inputStreams.iterator();
        while (i3.hasNext()) {
            ((ActiveMQInputStream) i3.next()).dispose();
        }
        Iterator<ActiveMQOutputStream> i4 = this.outputStreams.iterator();
        while (i4.hasNext()) {
            ((ActiveMQOutputStream) i4.next()).dispose();
        }
        if (this.isConnectionInfoSentToBroker) {
            if (!(this.transportFailed.get() || this.closing.get())) {
                syncSendPacket(this.info.createRemoveCommand());
            }
            this.isConnectionInfoSentToBroker = false;
        }
        if (this.userSpecifiedClientID) {
            this.info.setClientId(null);
            this.userSpecifiedClientID = false;
        }
        this.clientIDSet = false;
        this.started.set(false);
    }

    public void finalize() throws Throwable {
        Scheduler s = this.scheduler;
        if (s != null) {
            s.stop();
        }
    }

    public void changeUserInfo(String userName, String password) throws JMSException {
        if (this.isConnectionInfoSentToBroker) {
            throw new IllegalStateException("changeUserInfo used Connection is not allowed");
        }
        this.info.setUserName(userName);
        this.info.setPassword(password);
    }

    public String getResourceManagerId() throws JMSException {
        waitForBrokerInfo();
        if (this.brokerInfo != null) {
            return this.brokerInfo.getBrokerId().getValue();
        }
        throw new JMSException("Connection failed before Broker info was received.");
    }

    public String getBrokerName() {
        String str = null;
        try {
            this.brokerInfoReceived.await(5, TimeUnit.SECONDS);
            if (this.brokerInfo != null) {
                str = this.brokerInfo.getBrokerName();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return str;
    }

    public BrokerInfo getBrokerInfo() {
        return this.brokerInfo;
    }

    public RedeliveryPolicy getRedeliveryPolicy() throws JMSException {
        return this.redeliveryPolicyMap.getDefaultEntry();
    }

    public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
        this.redeliveryPolicyMap.setDefaultEntry(redeliveryPolicy);
    }

    public BlobTransferPolicy getBlobTransferPolicy() {
        if (this.blobTransferPolicy == null) {
            this.blobTransferPolicy = createBlobTransferPolicy();
        }
        return this.blobTransferPolicy;
    }

    public void setBlobTransferPolicy(BlobTransferPolicy blobTransferPolicy) {
        this.blobTransferPolicy = blobTransferPolicy;
    }

    public boolean isAlwaysSessionAsync() {
        return this.alwaysSessionAsync;
    }

    public void setAlwaysSessionAsync(boolean alwaysSessionAsync) {
        this.alwaysSessionAsync = alwaysSessionAsync;
    }

    public boolean isOptimizeAcknowledge() {
        return this.optimizeAcknowledge;
    }

    public void setOptimizeAcknowledge(boolean optimizeAcknowledge) {
        this.optimizeAcknowledge = optimizeAcknowledge;
    }

    public void setOptimizeAcknowledgeTimeOut(long optimizeAcknowledgeTimeOut) {
        this.optimizeAcknowledgeTimeOut = optimizeAcknowledgeTimeOut;
    }

    public long getOptimizeAcknowledgeTimeOut() {
        return this.optimizeAcknowledgeTimeOut;
    }

    public long getWarnAboutUnstartedConnectionTimeout() {
        return this.warnAboutUnstartedConnectionTimeout;
    }

    public void setWarnAboutUnstartedConnectionTimeout(long warnAboutUnstartedConnectionTimeout) {
        this.warnAboutUnstartedConnectionTimeout = warnAboutUnstartedConnectionTimeout;
    }

    public int getSendTimeout() {
        return this.sendTimeout;
    }

    public void setSendTimeout(int sendTimeout) {
        this.sendTimeout = sendTimeout;
    }

    public boolean isSendAcksAsync() {
        return this.sendAcksAsync;
    }

    public void setSendAcksAsync(boolean sendAcksAsync) {
        this.sendAcksAsync = sendAcksAsync;
    }

    public long getTimeCreated() {
        return this.timeCreated;
    }

    private void waitForBrokerInfo() throws JMSException {
        try {
            this.brokerInfoReceived.await();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw JMSExceptionSupport.create(e);
        }
    }

    public Transport getTransport() {
        return this.transport;
    }

    public void addProducer(ProducerId producerId, ActiveMQMessageProducer producer) {
        this.producers.put(producerId, producer);
    }

    public void removeProducer(ProducerId producerId) {
        this.producers.remove(producerId);
    }

    public void addDispatcher(ConsumerId consumerId, ActiveMQDispatcher dispatcher) {
        this.dispatchers.put(consumerId, dispatcher);
    }

    public void removeDispatcher(ConsumerId consumerId) {
        this.dispatchers.remove(consumerId);
    }

    public void onCommand(Object o) {
        Command command = (Command) o;
        if (!(this.closed.get() || command == null)) {
            try {
                command.visit(new 3(command));
            } catch (Exception e) {
                onClientInternalException(e);
            }
        }
        Iterator<TransportListener> iter = this.transportListeners.iterator();
        while (iter.hasNext()) {
            ((TransportListener) iter.next()).onCommand(command);
        }
    }

    protected void onWireFormatInfo(WireFormatInfo info) {
        this.protocolVersion.set(info.getVersion());
    }

    public void onClientInternalException(Throwable error) {
        if (!this.closed.get() && !this.closing.get()) {
            if (this.clientInternalExceptionListener != null) {
                this.executor.execute(new 4(error));
            } else {
                LOG.debug("Async client internal exception occurred with no exception listener registered: " + error, error);
            }
        }
    }

    public void onAsyncException(Throwable error) {
        if (!this.closed.get() && !this.closing.get()) {
            if (this.exceptionListener != null) {
                if (!(error instanceof JMSException)) {
                    error = JMSExceptionSupport.create(error);
                }
                this.executor.execute(new 5((JMSException) error));
                return;
            }
            LOG.debug("Async exception with no exception listener: " + error, error);
        }
    }

    public void onException(IOException error) {
        onAsyncException(error);
        if (!this.closing.get() && !this.closed.get()) {
            this.executor.execute(new 6(error));
        }
    }

    public void transportInterupted() {
        this.transportInterruptionProcessingComplete = new CountDownLatch(this.dispatchers.size() - (this.advisoryConsumer != null ? 1 : 0));
        if (LOG.isDebugEnabled()) {
            LOG.debug("transport interrupted, dispatchers: " + this.transportInterruptionProcessingComplete.getCount());
        }
        signalInterruptionProcessingNeeded();
        Iterator<ActiveMQSession> i = this.sessions.iterator();
        while (i.hasNext()) {
            ((ActiveMQSession) i.next()).clearMessagesInProgress();
        }
        Iterator i$ = this.connectionConsumers.iterator();
        while (i$.hasNext()) {
            ((ActiveMQConnectionConsumer) i$.next()).clearMessagesInProgress();
        }
        Iterator<TransportListener> iter = this.transportListeners.iterator();
        while (iter.hasNext()) {
            ((TransportListener) iter.next()).transportInterupted();
        }
    }

    public void transportResumed() {
        Iterator<TransportListener> iter = this.transportListeners.iterator();
        while (iter.hasNext()) {
            ((TransportListener) iter.next()).transportResumed();
        }
    }

    protected ActiveMQTempDestination createTempDestination(boolean topic) throws JMSException {
        ActiveMQTempDestination dest;
        if (topic) {
            dest = new ActiveMQTempTopic(this.info.getConnectionId(), this.tempDestinationIdGenerator.getNextSequenceId());
        } else {
            dest = new ActiveMQTempQueue(this.info.getConnectionId(), this.tempDestinationIdGenerator.getNextSequenceId());
        }
        DestinationInfo info = new DestinationInfo();
        info.setConnectionId(this.info.getConnectionId());
        info.setOperationType((byte) 0);
        info.setDestination(dest);
        syncSendPacket(info);
        dest.setConnection(this);
        this.activeTempDestinations.put(dest, dest);
        return dest;
    }

    public void deleteTempDestination(ActiveMQTempDestination destination) throws JMSException {
        checkClosedOrFailed();
        Iterator i$ = this.sessions.iterator();
        while (i$.hasNext()) {
            if (((ActiveMQSession) i$.next()).isInUse(destination)) {
                throw new JMSException("A consumer is consuming from the temporary destination");
            }
        }
        this.activeTempDestinations.remove(destination);
        DestinationInfo destInfo = new DestinationInfo();
        destInfo.setConnectionId(this.info.getConnectionId());
        destInfo.setOperationType((byte) 1);
        destInfo.setDestination(destination);
        destInfo.setTimeout(0);
        syncSendPacket(destInfo);
    }

    public boolean isDeleted(ActiveMQDestination dest) {
        if (this.advisoryConsumer == null || this.activeTempDestinations.contains(dest)) {
            return false;
        }
        return true;
    }

    public boolean isCopyMessageOnSend() {
        return this.copyMessageOnSend;
    }

    public LongSequenceGenerator getLocalTransactionIdGenerator() {
        return this.localTransactionIdGenerator;
    }

    public boolean isUseCompression() {
        return this.useCompression;
    }

    public void setUseCompression(boolean useCompression) {
        this.useCompression = useCompression;
    }

    public void destroyDestination(ActiveMQDestination destination) throws JMSException {
        checkClosedOrFailed();
        ensureConnectionInfoSent();
        DestinationInfo info = new DestinationInfo();
        info.setConnectionId(this.info.getConnectionId());
        info.setOperationType((byte) 1);
        info.setDestination(destination);
        info.setTimeout(0);
        syncSendPacket(info);
    }

    public boolean isDispatchAsync() {
        return this.dispatchAsync;
    }

    public void setDispatchAsync(boolean asyncDispatch) {
        this.dispatchAsync = asyncDispatch;
    }

    public boolean isObjectMessageSerializationDefered() {
        return this.objectMessageSerializationDefered;
    }

    public void setObjectMessageSerializationDefered(boolean objectMessageSerializationDefered) {
        this.objectMessageSerializationDefered = objectMessageSerializationDefered;
    }

    public InputStream createInputStream(Destination dest) throws JMSException {
        return createInputStream(dest, null);
    }

    public InputStream createInputStream(Destination dest, String messageSelector) throws JMSException {
        return createInputStream(dest, messageSelector, false);
    }

    public InputStream createInputStream(Destination dest, String messageSelector, boolean noLocal) throws JMSException {
        return createInputStream(dest, messageSelector, noLocal, -1);
    }

    public InputStream createInputStream(Destination dest, String messageSelector, boolean noLocal, long timeout) throws JMSException {
        return doCreateInputStream(dest, messageSelector, noLocal, null, timeout);
    }

    public InputStream createDurableInputStream(Topic dest, String name) throws JMSException {
        return createInputStream(dest, null, false);
    }

    public InputStream createDurableInputStream(Topic dest, String name, String messageSelector) throws JMSException {
        return createDurableInputStream(dest, name, messageSelector, false);
    }

    public InputStream createDurableInputStream(Topic dest, String name, String messageSelector, boolean noLocal) throws JMSException {
        return createDurableInputStream(dest, name, messageSelector, noLocal, -1);
    }

    public InputStream createDurableInputStream(Topic dest, String name, String messageSelector, boolean noLocal, long timeout) throws JMSException {
        return doCreateInputStream(dest, messageSelector, noLocal, name, timeout);
    }

    private InputStream doCreateInputStream(Destination dest, String messageSelector, boolean noLocal, String subName, long timeout) throws JMSException {
        checkClosedOrFailed();
        ensureConnectionInfoSent();
        return new ActiveMQInputStream(this, createConsumerId(), ActiveMQDestination.transform(dest), messageSelector, noLocal, subName, this.prefetchPolicy.getInputStreamPrefetch(), timeout);
    }

    public OutputStream createOutputStream(Destination dest) throws JMSException {
        return createOutputStream(dest, null, 2, 4, 0);
    }

    public OutputStream createNonPersistentOutputStream(Destination dest) throws JMSException {
        return createOutputStream(dest, null, 1, 4, 0);
    }

    public OutputStream createOutputStream(Destination dest, Map<String, Object> streamProperties, int deliveryMode, int priority, long timeToLive) throws JMSException {
        checkClosedOrFailed();
        ensureConnectionInfoSent();
        return new ActiveMQOutputStream(this, createProducerId(), ActiveMQDestination.transform(dest), streamProperties, deliveryMode, priority, timeToLive);
    }

    public void unsubscribe(String name) throws InvalidDestinationException, JMSException {
        checkClosedOrFailed();
        RemoveSubscriptionInfo rsi = new RemoveSubscriptionInfo();
        rsi.setConnectionId(getConnectionInfo().getConnectionId());
        rsi.setSubscriptionName(name);
        rsi.setClientId(getConnectionInfo().getClientId());
        syncSendPacket(rsi);
    }

    void send(ActiveMQDestination destination, ActiveMQMessage msg, MessageId messageId, int deliveryMode, int priority, long timeToLive, boolean async) throws JMSException {
        checkClosedOrFailed();
        if (destination.isTemporary() && isDeleted(destination)) {
            throw new JMSException("Cannot publish to a deleted Destination: " + destination);
        }
        msg.setJMSDestination(destination);
        msg.setJMSDeliveryMode(deliveryMode);
        long expiration = 0;
        if (!isDisableTimeStampsByDefault()) {
            long timeStamp = System.currentTimeMillis();
            msg.setJMSTimestamp(timeStamp);
            if (timeToLive > 0) {
                expiration = timeToLive + timeStamp;
            }
        }
        msg.setJMSExpiration(expiration);
        msg.setJMSPriority(priority);
        msg.setJMSRedelivered(false);
        msg.setMessageId(messageId);
        msg.onSend();
        msg.setProducerId(msg.getMessageId().getProducerId());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sending message: " + msg);
        }
        if (async) {
            asyncSendPacket(msg);
        } else {
            syncSendPacket(msg);
        }
    }

    public void addOutputStream(ActiveMQOutputStream stream) {
        this.outputStreams.add(stream);
    }

    public void removeOutputStream(ActiveMQOutputStream stream) {
        this.outputStreams.remove(stream);
    }

    public void addInputStream(ActiveMQInputStream stream) {
        this.inputStreams.add(stream);
    }

    public void removeInputStream(ActiveMQInputStream stream) {
        this.inputStreams.remove(stream);
    }

    protected void onControlCommand(ControlCommand command) {
        String text = command.getCommand();
        if (text != null && "shutdown".equals(text)) {
            LOG.info("JVM told to shutdown");
            System.exit(0);
        }
    }

    protected void onConnectionControl(ConnectionControl command) {
        if (command.isFaultTolerant()) {
            this.optimizeAcknowledge = false;
            Iterator<ActiveMQSession> i = this.sessions.iterator();
            while (i.hasNext()) {
                ((ActiveMQSession) i.next()).setOptimizeAcknowledge(false);
            }
        }
    }

    protected void onConsumerControl(ConsumerControl command) {
        Iterator i$;
        if (command.isClose()) {
            i$ = this.sessions.iterator();
            while (i$.hasNext()) {
                ((ActiveMQSession) i$.next()).close(command.getConsumerId());
            }
            return;
        }
        i$ = this.sessions.iterator();
        while (i$.hasNext()) {
            ((ActiveMQSession) i$.next()).setPrefetchSize(command.getConsumerId(), command.getPrefetch());
        }
        i$ = this.connectionConsumers.iterator();
        while (i$.hasNext()) {
            ConsumerInfo consumerInfo = ((ActiveMQConnectionConsumer) i$.next()).getConsumerInfo();
            if (consumerInfo.getConsumerId().equals(command.getConsumerId())) {
                consumerInfo.setPrefetchSize(command.getPrefetch());
            }
        }
    }

    protected void transportFailed(IOException error) {
        this.transportFailed.set(true);
        if (this.firstFailureError == null) {
            this.firstFailureError = error;
        }
    }

    public void setCopyMessageOnSend(boolean copyMessageOnSend) {
        this.copyMessageOnSend = copyMessageOnSend;
    }

    public String toString() {
        return "ActiveMQConnection {id=" + this.info.getConnectionId() + ",clientId=" + this.info.getClientId() + ",started=" + this.started.get() + "}";
    }

    protected BlobTransferPolicy createBlobTransferPolicy() {
        return new BlobTransferPolicy();
    }

    public int getProtocolVersion() {
        return this.protocolVersion.get();
    }

    public int getProducerWindowSize() {
        return this.producerWindowSize;
    }

    public void setProducerWindowSize(int producerWindowSize) {
        this.producerWindowSize = producerWindowSize;
    }

    public void setAuditDepth(int auditDepth) {
        this.connectionAudit.setAuditDepth(auditDepth);
    }

    public void setAuditMaximumProducerNumber(int auditMaximumProducerNumber) {
        this.connectionAudit.setAuditMaximumProducerNumber(auditMaximumProducerNumber);
    }

    protected void removeDispatcher(ActiveMQDispatcher dispatcher) {
        this.connectionAudit.removeDispatcher(dispatcher);
    }

    protected boolean isDuplicate(ActiveMQDispatcher dispatcher, Message message) {
        return this.checkForDuplicates && this.connectionAudit.isDuplicate(dispatcher, message);
    }

    protected void rollbackDuplicate(ActiveMQDispatcher dispatcher, Message message) {
        this.connectionAudit.rollbackDuplicate(dispatcher, message);
    }

    public IOException getFirstFailureError() {
        return this.firstFailureError;
    }

    protected void waitForTransportInterruptionProcessingToComplete() throws InterruptedException {
        CountDownLatch cdl = this.transportInterruptionProcessingComplete;
        if (cdl != null) {
            if (!(this.closed.get() || this.transportFailed.get() || cdl.getCount() <= 0)) {
                LOG.warn("dispatch paused, waiting for outstanding dispatch interruption processing (" + cdl.getCount() + ") to complete..");
                cdl.await(10, TimeUnit.SECONDS);
            }
            signalInterruptionProcessingComplete();
        }
    }

    protected void transportInterruptionProcessingComplete() {
        CountDownLatch cdl = this.transportInterruptionProcessingComplete;
        if (cdl != null) {
            cdl.countDown();
            try {
                signalInterruptionProcessingComplete();
            } catch (InterruptedException e) {
            }
        }
    }

    private void signalInterruptionProcessingComplete() throws InterruptedException {
        if (this.transportInterruptionProcessingComplete.getCount() == 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("transportInterruptionProcessingComplete for: " + getConnectionInfo().getConnectionId());
            }
            this.transportInterruptionProcessingComplete = null;
            FailoverTransport failoverTransport = (FailoverTransport) this.transport.narrow(FailoverTransport.class);
            if (failoverTransport != null) {
                failoverTransport.connectionInterruptProcessingComplete(getConnectionInfo().getConnectionId());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("notified failover transport (" + failoverTransport + ") of interruption completion for: " + getConnectionInfo().getConnectionId());
                }
            }
        }
    }

    private void signalInterruptionProcessingNeeded() {
        FailoverTransport failoverTransport = (FailoverTransport) this.transport.narrow(FailoverTransport.class);
        if (failoverTransport != null) {
            failoverTransport.getStateTracker().transportInterrupted(getConnectionInfo().getConnectionId());
            if (LOG.isDebugEnabled()) {
                LOG.debug("notified failover transport (" + failoverTransport + ") of pending interruption processing for: " + getConnectionInfo().getConnectionId());
            }
        }
    }

    public void setConsumerFailoverRedeliveryWaitPeriod(long consumerFailoverRedeliveryWaitPeriod) {
        this.consumerFailoverRedeliveryWaitPeriod = consumerFailoverRedeliveryWaitPeriod;
    }

    public long getConsumerFailoverRedeliveryWaitPeriod() {
        return this.consumerFailoverRedeliveryWaitPeriod;
    }

    protected Scheduler getScheduler() throws JMSException {
        Scheduler result = this.scheduler;
        if (result == null) {
            synchronized (this) {
                try {
                    result = this.scheduler;
                    if (result == null) {
                        checkClosed();
                        Scheduler result2 = new Scheduler("ActiveMQConnection[" + this.info.getConnectionId().getValue() + "] Scheduler");
                        this.scheduler = result2;
                        try {
                            this.scheduler.start();
                            result = result2;
                        } catch (Exception e) {
                            Exception e2 = e;
                            result = result2;
                            throw JMSExceptionSupport.create(e2);
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            result = result2;
                            throw th2;
                        }
                    }
                } catch (Exception e3) {
                    e2 = e3;
                    throw JMSExceptionSupport.create(e2);
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return result;
    }

    protected ThreadPoolExecutor getExecutor() {
        return this.executor;
    }

    public boolean isCheckForDuplicates() {
        return this.checkForDuplicates;
    }

    public void setCheckForDuplicates(boolean checkForDuplicates) {
        this.checkForDuplicates = checkForDuplicates;
    }

    public boolean isTransactedIndividualAck() {
        return this.transactedIndividualAck;
    }

    public void setTransactedIndividualAck(boolean transactedIndividualAck) {
        this.transactedIndividualAck = transactedIndividualAck;
    }

    public boolean isNonBlockingRedelivery() {
        return this.nonBlockingRedelivery;
    }

    public void setNonBlockingRedelivery(boolean nonBlockingRedelivery) {
        this.nonBlockingRedelivery = nonBlockingRedelivery;
    }

    public void cleanUpTempDestinations() {
        if (this.activeTempDestinations != null && !this.activeTempDestinations.isEmpty()) {
            for (Entry<ActiveMQTempDestination, ActiveMQTempDestination> entry : this.activeTempDestinations.entrySet()) {
                try {
                    ActiveMQTempDestination dest = (ActiveMQTempDestination) entry.getValue();
                    String thisConnectionId = this.info.getConnectionId() == null ? Stomp.EMPTY : this.info.getConnectionId().toString();
                    if (dest.getConnectionId() != null && dest.getConnectionId().equals(thisConnectionId)) {
                        deleteTempDestination((ActiveMQTempDestination) entry.getValue());
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public void setRedeliveryPolicyMap(RedeliveryPolicyMap redeliveryPolicyMap) {
        this.redeliveryPolicyMap = redeliveryPolicyMap;
    }

    public RedeliveryPolicyMap getRedeliveryPolicyMap() {
        return this.redeliveryPolicyMap;
    }

    public int getMaxThreadPoolSize() {
        return this.maxThreadPoolSize;
    }

    public void setMaxThreadPoolSize(int maxThreadPoolSize) {
        this.maxThreadPoolSize = maxThreadPoolSize;
    }

    ActiveMQConnection enforceQueueOnlyConnection() {
        this.queueOnlyConnection = true;
        return this;
    }

    public RejectedExecutionHandler getRejectedTaskHandler() {
        return this.rejectedTaskHandler;
    }

    public void setRejectedTaskHandler(RejectedExecutionHandler rejectedTaskHandler) {
        this.rejectedTaskHandler = rejectedTaskHandler;
    }

    public long getOptimizedAckScheduledAckInterval() {
        return this.optimizedAckScheduledAckInterval;
    }

    public void setOptimizedAckScheduledAckInterval(long optimizedAckScheduledAckInterval) {
        this.optimizedAckScheduledAckInterval = optimizedAckScheduledAckInterval;
    }
}
