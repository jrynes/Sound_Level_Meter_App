package org.apache.activemq;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.RejectedExecutionHandler;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import org.apache.activemq.blob.BlobTransferPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.jndi.JNDIBaseStorable;
import org.apache.activemq.management.JMSStatsImpl;
import org.apache.activemq.management.StatsCapable;
import org.apache.activemq.management.StatsImpl;
import org.apache.activemq.thread.TaskRunnerFactory;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFactory;
import org.apache.activemq.transport.TransportListener;
import org.apache.activemq.util.IdGenerator;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.JMSExceptionSupport;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.util.URISupport.CompositeData;
import org.xbill.DNS.KEYRecord.Flags;

public class ActiveMQConnectionFactory extends JNDIBaseStorable implements ConnectionFactory, QueueConnectionFactory, TopicConnectionFactory, StatsCapable, Cloneable {
    public static final String DEFAULT_BROKER_BIND_URL = "tcp://localhost:61616";
    public static final String DEFAULT_BROKER_URL = "failover://tcp://localhost:61616";
    public static final String DEFAULT_PASSWORD;
    public static final int DEFAULT_PRODUCER_WINDOW_SIZE = 0;
    public static final String DEFAULT_USER;
    protected boolean alwaysSessionAsync;
    private boolean alwaysSyncSend;
    private int auditDepth;
    private int auditMaximumProducerNumber;
    private BlobTransferPolicy blobTransferPolicy;
    protected URI brokerURL;
    private boolean checkForDuplicates;
    protected String clientID;
    private String clientIDPrefix;
    private IdGenerator clientIdGenerator;
    private ClientInternalExceptionListener clientInternalExceptionListener;
    private int closeTimeout;
    private String connectionIDPrefix;
    private IdGenerator connectionIdGenerator;
    private long consumerFailoverRedeliveryWaitPeriod;
    private boolean copyMessageOnSend;
    private boolean disableTimeStampsByDefault;
    protected boolean dispatchAsync;
    private ExceptionListener exceptionListener;
    private boolean exclusiveConsumer;
    JMSStatsImpl factoryStats;
    private int maxThreadPoolSize;
    private boolean messagePrioritySupported;
    private boolean nestedMapAndListEnabled;
    private boolean nonBlockingRedelivery;
    private boolean objectMessageSerializationDefered;
    private boolean optimizeAcknowledge;
    private long optimizeAcknowledgeTimeOut;
    private long optimizedAckScheduledAckInterval;
    private boolean optimizedMessageDispatch;
    protected String password;
    private ActiveMQPrefetchPolicy prefetchPolicy;
    private int producerWindowSize;
    private RedeliveryPolicyMap redeliveryPolicyMap;
    private RejectedExecutionHandler rejectedTaskHandler;
    private boolean sendAcksAsync;
    private int sendTimeout;
    private TaskRunnerFactory sessionTaskRunner;
    private boolean transactedIndividualAck;
    private MessageTransformer transformer;
    private TransportListener transportListener;
    private boolean useAsyncSend;
    private boolean useCompression;
    private boolean useDedicatedTaskRunner;
    private boolean useRetroactiveConsumer;
    protected String userName;
    private long warnAboutUnstartedConnectionTimeout;
    private boolean watchTopicAdvisories;

    static {
        DEFAULT_USER = null;
        DEFAULT_PASSWORD = null;
    }

    public ActiveMQConnectionFactory() {
        this(DEFAULT_BROKER_URL);
    }

    public ActiveMQConnectionFactory(String brokerURL) {
        this(createURI(brokerURL));
    }

    public ActiveMQConnectionFactory(URI brokerURL) {
        this.dispatchAsync = true;
        this.alwaysSessionAsync = true;
        this.factoryStats = new JMSStatsImpl();
        this.prefetchPolicy = new ActiveMQPrefetchPolicy();
        this.redeliveryPolicyMap = new RedeliveryPolicyMap();
        this.redeliveryPolicyMap.setDefaultEntry(new RedeliveryPolicy());
        this.blobTransferPolicy = new BlobTransferPolicy();
        this.optimizedMessageDispatch = true;
        this.optimizeAcknowledgeTimeOut = 300;
        this.optimizedAckScheduledAckInterval = 0;
        this.copyMessageOnSend = true;
        this.closeTimeout = 15000;
        this.nestedMapAndListEnabled = true;
        this.watchTopicAdvisories = true;
        this.producerWindowSize = 0;
        this.warnAboutUnstartedConnectionTimeout = 500;
        this.sendTimeout = 0;
        this.sendAcksAsync = true;
        this.auditDepth = Flags.FLAG4;
        this.auditMaximumProducerNumber = 64;
        this.consumerFailoverRedeliveryWaitPeriod = 0;
        this.checkForDuplicates = true;
        this.messagePrioritySupported = true;
        this.transactedIndividualAck = false;
        this.nonBlockingRedelivery = false;
        this.maxThreadPoolSize = ActiveMQConnection.DEFAULT_THREAD_POOL_SIZE;
        this.rejectedTaskHandler = null;
        setBrokerURL(brokerURL.toString());
    }

    public ActiveMQConnectionFactory(String userName, String password, URI brokerURL) {
        this.dispatchAsync = true;
        this.alwaysSessionAsync = true;
        this.factoryStats = new JMSStatsImpl();
        this.prefetchPolicy = new ActiveMQPrefetchPolicy();
        this.redeliveryPolicyMap = new RedeliveryPolicyMap();
        this.redeliveryPolicyMap.setDefaultEntry(new RedeliveryPolicy());
        this.blobTransferPolicy = new BlobTransferPolicy();
        this.optimizedMessageDispatch = true;
        this.optimizeAcknowledgeTimeOut = 300;
        this.optimizedAckScheduledAckInterval = 0;
        this.copyMessageOnSend = true;
        this.closeTimeout = 15000;
        this.nestedMapAndListEnabled = true;
        this.watchTopicAdvisories = true;
        this.producerWindowSize = 0;
        this.warnAboutUnstartedConnectionTimeout = 500;
        this.sendTimeout = 0;
        this.sendAcksAsync = true;
        this.auditDepth = Flags.FLAG4;
        this.auditMaximumProducerNumber = 64;
        this.consumerFailoverRedeliveryWaitPeriod = 0;
        this.checkForDuplicates = true;
        this.messagePrioritySupported = true;
        this.transactedIndividualAck = false;
        this.nonBlockingRedelivery = false;
        this.maxThreadPoolSize = ActiveMQConnection.DEFAULT_THREAD_POOL_SIZE;
        this.rejectedTaskHandler = null;
        setUserName(userName);
        setPassword(password);
        setBrokerURL(brokerURL.toString());
    }

    public ActiveMQConnectionFactory(String userName, String password, String brokerURL) {
        this.dispatchAsync = true;
        this.alwaysSessionAsync = true;
        this.factoryStats = new JMSStatsImpl();
        this.prefetchPolicy = new ActiveMQPrefetchPolicy();
        this.redeliveryPolicyMap = new RedeliveryPolicyMap();
        this.redeliveryPolicyMap.setDefaultEntry(new RedeliveryPolicy());
        this.blobTransferPolicy = new BlobTransferPolicy();
        this.optimizedMessageDispatch = true;
        this.optimizeAcknowledgeTimeOut = 300;
        this.optimizedAckScheduledAckInterval = 0;
        this.copyMessageOnSend = true;
        this.closeTimeout = 15000;
        this.nestedMapAndListEnabled = true;
        this.watchTopicAdvisories = true;
        this.producerWindowSize = 0;
        this.warnAboutUnstartedConnectionTimeout = 500;
        this.sendTimeout = 0;
        this.sendAcksAsync = true;
        this.auditDepth = Flags.FLAG4;
        this.auditMaximumProducerNumber = 64;
        this.consumerFailoverRedeliveryWaitPeriod = 0;
        this.checkForDuplicates = true;
        this.messagePrioritySupported = true;
        this.transactedIndividualAck = false;
        this.nonBlockingRedelivery = false;
        this.maxThreadPoolSize = ActiveMQConnection.DEFAULT_THREAD_POOL_SIZE;
        this.rejectedTaskHandler = null;
        setUserName(userName);
        setPassword(password);
        setBrokerURL(brokerURL);
    }

    public ActiveMQConnectionFactory copy() {
        try {
            return (ActiveMQConnectionFactory) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("This should never happen: " + e, e);
        }
    }

    private static URI createURI(String brokerURL) {
        try {
            return new URI(brokerURL);
        } catch (URISyntaxException e) {
            throw ((IllegalArgumentException) new IllegalArgumentException("Invalid broker URI: " + brokerURL).initCause(e));
        }
    }

    public Connection createConnection() throws JMSException {
        return createActiveMQConnection();
    }

    public Connection createConnection(String userName, String password) throws JMSException {
        return createActiveMQConnection(userName, password);
    }

    public QueueConnection createQueueConnection() throws JMSException {
        return createActiveMQConnection().enforceQueueOnlyConnection();
    }

    public QueueConnection createQueueConnection(String userName, String password) throws JMSException {
        return createActiveMQConnection(userName, password).enforceQueueOnlyConnection();
    }

    public TopicConnection createTopicConnection() throws JMSException {
        return createActiveMQConnection();
    }

    public TopicConnection createTopicConnection(String userName, String password) throws JMSException {
        return createActiveMQConnection(userName, password);
    }

    public StatsImpl getStats() {
        return this.factoryStats;
    }

    protected ActiveMQConnection createActiveMQConnection() throws JMSException {
        return createActiveMQConnection(this.userName, this.password);
    }

    protected Transport createTransport() throws JMSException {
        try {
            return TransportFactory.connect(this.brokerURL);
        } catch (Exception e) {
            throw JMSExceptionSupport.create("Could not create Transport. Reason: " + e, e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected org.apache.activemq.ActiveMQConnection createActiveMQConnection(java.lang.String r6, java.lang.String r7) throws javax.jms.JMSException {
        /*
        r5 = this;
        r3 = r5.brokerURL;
        if (r3 != 0) goto L_0x000c;
    L_0x0004:
        r3 = new org.apache.activemq.ConfigurationException;
        r4 = "brokerURL not set.";
        r3.<init>(r4);
        throw r3;
    L_0x000c:
        r0 = 0;
        r2 = r5.createTransport();	 Catch:{ JMSException -> 0x002d, Exception -> 0x0032 }
        r3 = r5.factoryStats;	 Catch:{ JMSException -> 0x002d, Exception -> 0x0032 }
        r0 = r5.createActiveMQConnection(r2, r3);	 Catch:{ JMSException -> 0x002d, Exception -> 0x0032 }
        r0.setUserName(r6);	 Catch:{ JMSException -> 0x002d, Exception -> 0x0032 }
        r0.setPassword(r7);	 Catch:{ JMSException -> 0x002d, Exception -> 0x0032 }
        r5.configureConnection(r0);	 Catch:{ JMSException -> 0x002d, Exception -> 0x0032 }
        r2.start();	 Catch:{ JMSException -> 0x002d, Exception -> 0x0032 }
        r3 = r5.clientID;	 Catch:{ JMSException -> 0x002d, Exception -> 0x0032 }
        if (r3 == 0) goto L_0x002c;
    L_0x0027:
        r3 = r5.clientID;	 Catch:{ JMSException -> 0x002d, Exception -> 0x0032 }
        r0.setDefaultClientID(r3);	 Catch:{ JMSException -> 0x002d, Exception -> 0x0032 }
    L_0x002c:
        return r0;
    L_0x002d:
        r1 = move-exception;
        r0.close();	 Catch:{ Throwable -> 0x005a }
    L_0x0031:
        throw r1;
    L_0x0032:
        r1 = move-exception;
        r0.close();	 Catch:{ Throwable -> 0x005c }
    L_0x0036:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Could not connect to broker URL: ";
        r3 = r3.append(r4);
        r4 = r5.brokerURL;
        r3 = r3.append(r4);
        r4 = ". Reason: ";
        r3 = r3.append(r4);
        r3 = r3.append(r1);
        r3 = r3.toString();
        r3 = org.apache.activemq.util.JMSExceptionSupport.create(r3, r1);
        throw r3;
    L_0x005a:
        r3 = move-exception;
        goto L_0x0031;
    L_0x005c:
        r3 = move-exception;
        goto L_0x0036;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.ActiveMQConnectionFactory.createActiveMQConnection(java.lang.String, java.lang.String):org.apache.activemq.ActiveMQConnection");
    }

    protected ActiveMQConnection createActiveMQConnection(Transport transport, JMSStatsImpl stats) throws Exception {
        return new ActiveMQConnection(transport, getClientIdGenerator(), getConnectionIdGenerator(), stats);
    }

    protected void configureConnection(ActiveMQConnection connection) throws JMSException {
        connection.setPrefetchPolicy(getPrefetchPolicy());
        connection.setDisableTimeStampsByDefault(isDisableTimeStampsByDefault());
        connection.setOptimizedMessageDispatch(isOptimizedMessageDispatch());
        connection.setCopyMessageOnSend(isCopyMessageOnSend());
        connection.setUseCompression(isUseCompression());
        connection.setObjectMessageSerializationDefered(isObjectMessageSerializationDefered());
        connection.setDispatchAsync(isDispatchAsync());
        connection.setUseAsyncSend(isUseAsyncSend());
        connection.setAlwaysSyncSend(isAlwaysSyncSend());
        connection.setAlwaysSessionAsync(isAlwaysSessionAsync());
        connection.setOptimizeAcknowledge(isOptimizeAcknowledge());
        connection.setOptimizeAcknowledgeTimeOut(getOptimizeAcknowledgeTimeOut());
        connection.setOptimizedAckScheduledAckInterval(getOptimizedAckScheduledAckInterval());
        connection.setUseRetroactiveConsumer(isUseRetroactiveConsumer());
        connection.setExclusiveConsumer(isExclusiveConsumer());
        connection.setRedeliveryPolicyMap(getRedeliveryPolicyMap());
        connection.setTransformer(getTransformer());
        connection.setBlobTransferPolicy(getBlobTransferPolicy().copy());
        connection.setWatchTopicAdvisories(isWatchTopicAdvisories());
        connection.setProducerWindowSize(getProducerWindowSize());
        connection.setWarnAboutUnstartedConnectionTimeout(getWarnAboutUnstartedConnectionTimeout());
        connection.setSendTimeout(getSendTimeout());
        connection.setCloseTimeout(getCloseTimeout());
        connection.setSendAcksAsync(isSendAcksAsync());
        connection.setAuditDepth(getAuditDepth());
        connection.setAuditMaximumProducerNumber(getAuditMaximumProducerNumber());
        connection.setUseDedicatedTaskRunner(isUseDedicatedTaskRunner());
        connection.setConsumerFailoverRedeliveryWaitPeriod(getConsumerFailoverRedeliveryWaitPeriod());
        connection.setCheckForDuplicates(isCheckForDuplicates());
        connection.setMessagePrioritySupported(isMessagePrioritySupported());
        connection.setTransactedIndividualAck(isTransactedIndividualAck());
        connection.setNonBlockingRedelivery(isNonBlockingRedelivery());
        connection.setMaxThreadPoolSize(getMaxThreadPoolSize());
        connection.setSessionTaskRunner(getSessionTaskRunner());
        connection.setRejectedTaskHandler(getRejectedTaskHandler());
        connection.setNestedMapAndListEnabled(isNestedMapAndListEnabled());
        if (this.transportListener != null) {
            connection.addTransportListener(this.transportListener);
        }
        if (this.exceptionListener != null) {
            connection.setExceptionListener(this.exceptionListener);
        }
        if (this.clientInternalExceptionListener != null) {
            connection.setClientInternalExceptionListener(this.clientInternalExceptionListener);
        }
    }

    public String getBrokerURL() {
        return this.brokerURL == null ? null : this.brokerURL.toString();
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = createURI(brokerURL);
        Map<String, Object> jmsOptionsMap;
        if (this.brokerURL.getQuery() != null) {
            try {
                Map<String, String> map = URISupport.parseQuery(this.brokerURL.getQuery());
                jmsOptionsMap = IntrospectionSupport.extractProperties(map, "jms.");
                if (!buildFromMap(jmsOptionsMap)) {
                    return;
                }
                if (jmsOptionsMap.isEmpty()) {
                    this.brokerURL = URISupport.createRemainingURI(this.brokerURL, map);
                    return;
                }
                throw new IllegalArgumentException("There are " + jmsOptionsMap.size() + " jms options that couldn't be set on the ConnectionFactory." + " Check the options are spelled correctly." + " Unknown parameters=[" + jmsOptionsMap + "]." + " This connection factory cannot be started.");
            } catch (URISyntaxException e) {
                return;
            }
        }
        try {
            CompositeData data = URISupport.parseComposite(this.brokerURL);
            jmsOptionsMap = IntrospectionSupport.extractProperties(data.getParameters(), "jms.");
            if (!buildFromMap(jmsOptionsMap)) {
                return;
            }
            if (jmsOptionsMap.isEmpty()) {
                this.brokerURL = data.toURI();
                return;
            }
            throw new IllegalArgumentException("There are " + jmsOptionsMap.size() + " jms options that couldn't be set on the ConnectionFactory." + " Check the options are spelled correctly." + " Unknown parameters=[" + jmsOptionsMap + "]." + " This connection factory cannot be started.");
        } catch (URISyntaxException e2) {
        }
    }

    public String getClientID() {
        return this.clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public boolean isCopyMessageOnSend() {
        return this.copyMessageOnSend;
    }

    public void setCopyMessageOnSend(boolean copyMessageOnSend) {
        this.copyMessageOnSend = copyMessageOnSend;
    }

    public boolean isDisableTimeStampsByDefault() {
        return this.disableTimeStampsByDefault;
    }

    public void setDisableTimeStampsByDefault(boolean disableTimeStampsByDefault) {
        this.disableTimeStampsByDefault = disableTimeStampsByDefault;
    }

    public boolean isOptimizedMessageDispatch() {
        return this.optimizedMessageDispatch;
    }

    public void setOptimizedMessageDispatch(boolean optimizedMessageDispatch) {
        this.optimizedMessageDispatch = optimizedMessageDispatch;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ActiveMQPrefetchPolicy getPrefetchPolicy() {
        return this.prefetchPolicy;
    }

    public void setPrefetchPolicy(ActiveMQPrefetchPolicy prefetchPolicy) {
        this.prefetchPolicy = prefetchPolicy;
    }

    public boolean isUseAsyncSend() {
        return this.useAsyncSend;
    }

    public BlobTransferPolicy getBlobTransferPolicy() {
        return this.blobTransferPolicy;
    }

    public void setBlobTransferPolicy(BlobTransferPolicy blobTransferPolicy) {
        this.blobTransferPolicy = blobTransferPolicy;
    }

    public void setUseAsyncSend(boolean useAsyncSend) {
        this.useAsyncSend = useAsyncSend;
    }

    public synchronized boolean isWatchTopicAdvisories() {
        return this.watchTopicAdvisories;
    }

    public synchronized void setWatchTopicAdvisories(boolean watchTopicAdvisories) {
        this.watchTopicAdvisories = watchTopicAdvisories;
    }

    public boolean isAlwaysSyncSend() {
        return this.alwaysSyncSend;
    }

    public void setAlwaysSyncSend(boolean alwaysSyncSend) {
        this.alwaysSyncSend = alwaysSyncSend;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isUseRetroactiveConsumer() {
        return this.useRetroactiveConsumer;
    }

    public void setUseRetroactiveConsumer(boolean useRetroactiveConsumer) {
        this.useRetroactiveConsumer = useRetroactiveConsumer;
    }

    public boolean isExclusiveConsumer() {
        return this.exclusiveConsumer;
    }

    public void setExclusiveConsumer(boolean exclusiveConsumer) {
        this.exclusiveConsumer = exclusiveConsumer;
    }

    public RedeliveryPolicy getRedeliveryPolicy() {
        return this.redeliveryPolicyMap.getDefaultEntry();
    }

    public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
        this.redeliveryPolicyMap.setDefaultEntry(redeliveryPolicy);
    }

    public RedeliveryPolicyMap getRedeliveryPolicyMap() {
        return this.redeliveryPolicyMap;
    }

    public void setRedeliveryPolicyMap(RedeliveryPolicyMap redeliveryPolicyMap) {
        this.redeliveryPolicyMap = redeliveryPolicyMap;
    }

    public MessageTransformer getTransformer() {
        return this.transformer;
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

    public boolean isMessagePrioritySupported() {
        return this.messagePrioritySupported;
    }

    public void setMessagePrioritySupported(boolean messagePrioritySupported) {
        this.messagePrioritySupported = messagePrioritySupported;
    }

    public void setTransformer(MessageTransformer transformer) {
        this.transformer = transformer;
    }

    public void buildFromProperties(Properties properties) {
        if (properties == null) {
            properties = new Properties();
        }
        String temp = properties.getProperty("java.naming.provider.url");
        if (temp == null || temp.length() == 0) {
            temp = properties.getProperty("brokerURL");
        }
        if (temp != null && temp.length() > 0) {
            setBrokerURL(temp);
        }
        buildFromMap(new HashMap(properties));
    }

    public boolean buildFromMap(Map<String, Object> properties) {
        boolean rc = false;
        ActiveMQPrefetchPolicy p = new ActiveMQPrefetchPolicy();
        if (IntrospectionSupport.setProperties(p, properties, "prefetchPolicy.")) {
            setPrefetchPolicy(p);
            rc = true;
        }
        RedeliveryPolicy rp = new RedeliveryPolicy();
        if (IntrospectionSupport.setProperties(rp, properties, "redeliveryPolicy.")) {
            setRedeliveryPolicy(rp);
            rc = true;
        }
        BlobTransferPolicy blobTransferPolicy = new BlobTransferPolicy();
        if (IntrospectionSupport.setProperties(blobTransferPolicy, properties, "blobTransferPolicy.")) {
            setBlobTransferPolicy(blobTransferPolicy);
            rc = true;
        }
        return rc | IntrospectionSupport.setProperties(this, properties);
    }

    public void populateProperties(Properties props) {
        props.setProperty("dispatchAsync", Boolean.toString(isDispatchAsync()));
        if (getBrokerURL() != null) {
            props.setProperty("java.naming.provider.url", getBrokerURL());
            props.setProperty("brokerURL", getBrokerURL());
        }
        if (getClientID() != null) {
            props.setProperty("clientID", getClientID());
        }
        IntrospectionSupport.getProperties(getPrefetchPolicy(), props, "prefetchPolicy.");
        IntrospectionSupport.getProperties(getRedeliveryPolicy(), props, "redeliveryPolicy.");
        IntrospectionSupport.getProperties(getBlobTransferPolicy(), props, "blobTransferPolicy.");
        props.setProperty("copyMessageOnSend", Boolean.toString(isCopyMessageOnSend()));
        props.setProperty("disableTimeStampsByDefault", Boolean.toString(isDisableTimeStampsByDefault()));
        props.setProperty("objectMessageSerializationDefered", Boolean.toString(isObjectMessageSerializationDefered()));
        props.setProperty("optimizedMessageDispatch", Boolean.toString(isOptimizedMessageDispatch()));
        if (getPassword() != null) {
            props.setProperty("password", getPassword());
        }
        props.setProperty("useAsyncSend", Boolean.toString(isUseAsyncSend()));
        props.setProperty("useCompression", Boolean.toString(isUseCompression()));
        props.setProperty("useRetroactiveConsumer", Boolean.toString(isUseRetroactiveConsumer()));
        props.setProperty("watchTopicAdvisories", Boolean.toString(isWatchTopicAdvisories()));
        if (getUserName() != null) {
            props.setProperty("userName", getUserName());
        }
        props.setProperty("closeTimeout", Integer.toString(getCloseTimeout()));
        props.setProperty("alwaysSessionAsync", Boolean.toString(isAlwaysSessionAsync()));
        props.setProperty("optimizeAcknowledge", Boolean.toString(isOptimizeAcknowledge()));
        props.setProperty("statsEnabled", Boolean.toString(isStatsEnabled()));
        props.setProperty("alwaysSyncSend", Boolean.toString(isAlwaysSyncSend()));
        props.setProperty("producerWindowSize", Integer.toString(getProducerWindowSize()));
        props.setProperty("sendTimeout", Integer.toString(getSendTimeout()));
        props.setProperty("sendAcksAsync", Boolean.toString(isSendAcksAsync()));
        props.setProperty("auditDepth", Integer.toString(getAuditDepth()));
        props.setProperty("auditMaximumProducerNumber", Integer.toString(getAuditMaximumProducerNumber()));
        props.setProperty("checkForDuplicates", Boolean.toString(isCheckForDuplicates()));
        props.setProperty("messagePrioritySupported", Boolean.toString(isMessagePrioritySupported()));
        props.setProperty("transactedIndividualAck", Boolean.toString(isTransactedIndividualAck()));
        props.setProperty("nonBlockingRedelivery", Boolean.toString(isNonBlockingRedelivery()));
        props.setProperty("maxThreadPoolSize", Integer.toString(getMaxThreadPoolSize()));
        props.setProperty("nestedMapAndListEnabled", Boolean.toString(isNestedMapAndListEnabled()));
    }

    public boolean isUseCompression() {
        return this.useCompression;
    }

    public void setUseCompression(boolean useCompression) {
        this.useCompression = useCompression;
    }

    public boolean isObjectMessageSerializationDefered() {
        return this.objectMessageSerializationDefered;
    }

    public void setObjectMessageSerializationDefered(boolean objectMessageSerializationDefered) {
        this.objectMessageSerializationDefered = objectMessageSerializationDefered;
    }

    public boolean isDispatchAsync() {
        return this.dispatchAsync;
    }

    public void setDispatchAsync(boolean asyncDispatch) {
        this.dispatchAsync = asyncDispatch;
    }

    public int getCloseTimeout() {
        return this.closeTimeout;
    }

    public void setCloseTimeout(int closeTimeout) {
        this.closeTimeout = closeTimeout;
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

    public boolean isNestedMapAndListEnabled() {
        return this.nestedMapAndListEnabled;
    }

    public void setNestedMapAndListEnabled(boolean structuredMapsEnabled) {
        this.nestedMapAndListEnabled = structuredMapsEnabled;
    }

    public String getClientIDPrefix() {
        return this.clientIDPrefix;
    }

    public void setClientIDPrefix(String clientIDPrefix) {
        this.clientIDPrefix = clientIDPrefix;
    }

    protected synchronized IdGenerator getClientIdGenerator() {
        if (this.clientIdGenerator == null) {
            if (this.clientIDPrefix != null) {
                this.clientIdGenerator = new IdGenerator(this.clientIDPrefix);
            } else {
                this.clientIdGenerator = new IdGenerator();
            }
        }
        return this.clientIdGenerator;
    }

    protected void setClientIdGenerator(IdGenerator clientIdGenerator) {
        this.clientIdGenerator = clientIdGenerator;
    }

    public void setConnectionIDPrefix(String connectionIDPrefix) {
        this.connectionIDPrefix = connectionIDPrefix;
    }

    protected synchronized IdGenerator getConnectionIdGenerator() {
        if (this.connectionIdGenerator == null) {
            if (this.connectionIDPrefix != null) {
                this.connectionIdGenerator = new IdGenerator(this.connectionIDPrefix);
            } else {
                this.connectionIdGenerator = new IdGenerator();
            }
        }
        return this.connectionIdGenerator;
    }

    protected void setConnectionIdGenerator(IdGenerator connectionIdGenerator) {
        this.connectionIdGenerator = connectionIdGenerator;
    }

    public boolean isStatsEnabled() {
        return this.factoryStats.isEnabled();
    }

    public void setStatsEnabled(boolean statsEnabled) {
        this.factoryStats.setEnabled(statsEnabled);
    }

    public synchronized int getProducerWindowSize() {
        return this.producerWindowSize;
    }

    public synchronized void setProducerWindowSize(int producerWindowSize) {
        this.producerWindowSize = producerWindowSize;
    }

    public long getWarnAboutUnstartedConnectionTimeout() {
        return this.warnAboutUnstartedConnectionTimeout;
    }

    public void setWarnAboutUnstartedConnectionTimeout(long warnAboutUnstartedConnectionTimeout) {
        this.warnAboutUnstartedConnectionTimeout = warnAboutUnstartedConnectionTimeout;
    }

    public TransportListener getTransportListener() {
        return this.transportListener;
    }

    public void setTransportListener(TransportListener transportListener) {
        this.transportListener = transportListener;
    }

    public ExceptionListener getExceptionListener() {
        return this.exceptionListener;
    }

    public void setExceptionListener(ExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
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

    public void setUseDedicatedTaskRunner(boolean useDedicatedTaskRunner) {
        this.useDedicatedTaskRunner = useDedicatedTaskRunner;
    }

    public boolean isUseDedicatedTaskRunner() {
        return this.useDedicatedTaskRunner;
    }

    public void setConsumerFailoverRedeliveryWaitPeriod(long consumerFailoverRedeliveryWaitPeriod) {
        this.consumerFailoverRedeliveryWaitPeriod = consumerFailoverRedeliveryWaitPeriod;
    }

    public long getConsumerFailoverRedeliveryWaitPeriod() {
        return this.consumerFailoverRedeliveryWaitPeriod;
    }

    public ClientInternalExceptionListener getClientInternalExceptionListener() {
        return this.clientInternalExceptionListener;
    }

    public void setClientInternalExceptionListener(ClientInternalExceptionListener clientInternalExceptionListener) {
        this.clientInternalExceptionListener = clientInternalExceptionListener;
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

    public int getMaxThreadPoolSize() {
        return this.maxThreadPoolSize;
    }

    public void setMaxThreadPoolSize(int maxThreadPoolSize) {
        this.maxThreadPoolSize = maxThreadPoolSize;
    }

    public TaskRunnerFactory getSessionTaskRunner() {
        return this.sessionTaskRunner;
    }

    public void setSessionTaskRunner(TaskRunnerFactory sessionTaskRunner) {
        this.sessionTaskRunner = sessionTaskRunner;
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
