package org.apache.activemq.transport.stomp;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.jms.JMSException;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.broker.BrokerContext;
import org.apache.activemq.broker.BrokerContextAware;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.ConnectionError;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.ConsumerControl;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.DestinationInfo;
import org.apache.activemq.command.ExceptionResponse;
import org.apache.activemq.command.LocalTransactionId;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.ProducerInfo;
import org.apache.activemq.command.RemoveSubscriptionInfo;
import org.apache.activemq.command.Response;
import org.apache.activemq.command.SessionId;
import org.apache.activemq.command.SessionInfo;
import org.apache.activemq.command.ShutdownInfo;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.command.TransactionInfo;
import org.apache.activemq.transport.stomp.Stomp.Commands;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.transport.stomp.Stomp.Headers.Connect;
import org.apache.activemq.transport.stomp.Stomp.Headers.Connected;
import org.apache.activemq.transport.stomp.Stomp.Headers.Error;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;
import org.apache.activemq.transport.stomp.Stomp.Headers.Subscribe;
import org.apache.activemq.transport.stomp.Stomp.Headers.Unsubscribe;
import org.apache.activemq.transport.stomp.Stomp.Responses;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.FactoryFinder;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IdGenerator;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.LongSequenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolConverter {
    private static final String BROKER_VERSION;
    private static final IdGenerator CONNECTION_ID_GENERATOR;
    private static final Logger LOG;
    private static final StompFrame ping;
    private final IdGenerator ACK_ID_GENERATOR;
    private final FactoryFinder FRAME_TRANSLATOR_FINDER;
    private final BrokerContext brokerContext;
    private final Object commnadIdMutex;
    private final AtomicBoolean connected;
    private final ConnectionId connectionId;
    ConnectionInfo connectionInfo;
    private final LongSequenceGenerator consumerIdGenerator;
    private String defaultHeartBeat;
    private final FrameTranslator frameTranslator;
    private long hbReadInterval;
    private long hbWriteInterval;
    private int lastCommandId;
    private final LongSequenceGenerator messageIdGenerator;
    private final ConcurrentHashMap<String, AckEntry> pedingAcks;
    private final ProducerId producerId;
    private final ConcurrentHashMap<Integer, ResponseHandler> resposeHandlers;
    private final SessionId sessionId;
    private final StompTransport stompTransport;
    private final ConcurrentHashMap<String, StompSubscription> subscriptions;
    private final ConcurrentHashMap<ConsumerId, StompSubscription> subscriptionsByConsumerId;
    private final ConcurrentHashMap<String, String> tempDestinationAmqToStompMap;
    private final LongSequenceGenerator tempDestinationGenerator;
    private final ConcurrentHashMap<String, ActiveMQDestination> tempDestinations;
    private final LongSequenceGenerator transactionIdGenerator;
    private final Map<String, LocalTransactionId> transactions;
    private String version;

    class 1 implements ResponseHandler {
        final /* synthetic */ StompFrame val$command;
        final /* synthetic */ String val$receiptId;

        1(StompFrame stompFrame, String str) {
            this.val$command = stompFrame;
            this.val$receiptId = str;
        }

        public void onResponse(ProtocolConverter converter, Response response) throws IOException {
            if (response.isException()) {
                ProtocolConverter.this.handleException(((ExceptionResponse) response).getException(), this.val$command);
                return;
            }
            StompFrame sc = new StompFrame();
            sc.setAction(Responses.RECEIPT);
            sc.setHeaders(new HashMap(1));
            sc.getHeaders().put(Headers.Response.RECEIPT_ID, this.val$receiptId);
            ProtocolConverter.this.stompTransport.sendToStomp(sc);
        }
    }

    class 2 implements ResponseHandler {
        final /* synthetic */ ActiveMQDestination val$actualDest;
        final /* synthetic */ StompFrame val$cmd;
        final /* synthetic */ ConsumerId val$id;
        final /* synthetic */ int val$prefetch;
        final /* synthetic */ String val$receiptId;

        2(StompFrame stompFrame, String str, int i, ActiveMQDestination activeMQDestination, ConsumerId consumerId) {
            this.val$cmd = stompFrame;
            this.val$receiptId = str;
            this.val$prefetch = i;
            this.val$actualDest = activeMQDestination;
            this.val$id = consumerId;
        }

        public void onResponse(ProtocolConverter converter, Response response) throws IOException {
            if (response.isException()) {
                ProtocolConverter.this.handleException(((ExceptionResponse) response).getException(), this.val$cmd);
                return;
            }
            StompFrame sc = new StompFrame();
            sc.setAction(Responses.RECEIPT);
            sc.setHeaders(new HashMap(1));
            sc.getHeaders().put(Headers.Response.RECEIPT_ID, this.val$receiptId);
            ProtocolConverter.this.stompTransport.sendToStomp(sc);
            ConsumerControl control = new ConsumerControl();
            control.setPrefetch(this.val$prefetch);
            control.setDestination(this.val$actualDest);
            control.setConsumerId(this.val$id);
            ProtocolConverter.this.sendToActiveMQ(control, null);
        }
    }

    class 3 implements ResponseHandler {
        final /* synthetic */ StompFrame val$command;
        final /* synthetic */ Map val$headers;

        class 1 implements ResponseHandler {
            1() {
            }

            public void onResponse(ProtocolConverter converter, Response response) throws IOException {
                if (response.isException()) {
                    Throwable exception = ((ExceptionResponse) response).getException();
                    ProtocolConverter.this.handleException(exception, 3.this.val$command);
                    ProtocolConverter.this.getStompTransport().onException(IOExceptionSupport.create(exception));
                }
                ProtocolConverter.this.connected.set(true);
                HashMap<String, String> responseHeaders = new HashMap();
                responseHeaders.put(Connected.SESSION, ProtocolConverter.this.connectionInfo.getClientId());
                String requestId = (String) 3.this.val$headers.get(Connect.REQUEST_ID);
                if (requestId == null) {
                    requestId = (String) 3.this.val$headers.get(Headers.RECEIPT_REQUESTED);
                }
                if (requestId != null) {
                    responseHeaders.put(Connected.RESPONSE_ID, requestId);
                    responseHeaders.put(Headers.Response.RECEIPT_ID, requestId);
                }
                responseHeaders.put(Connected.VERSION, ProtocolConverter.this.version);
                responseHeaders.put(Connected.HEART_BEAT, String.format("%d,%d", new Object[]{Long.valueOf(ProtocolConverter.this.hbWriteInterval), Long.valueOf(ProtocolConverter.this.hbReadInterval)}));
                responseHeaders.put(Connected.SERVER, "ActiveMQ/" + ProtocolConverter.BROKER_VERSION);
                StompFrame sc = new StompFrame();
                sc.setAction(Responses.CONNECTED);
                sc.setHeaders(responseHeaders);
                ProtocolConverter.this.sendToStomp(sc);
                StompWireFormat format = ProtocolConverter.this.stompTransport.getWireFormat();
                if (format != null) {
                    format.setStompVersion(ProtocolConverter.this.version);
                }
            }
        }

        3(StompFrame stompFrame, Map map) {
            this.val$command = stompFrame;
            this.val$headers = map;
        }

        public void onResponse(ProtocolConverter converter, Response response) throws IOException {
            if (response.isException()) {
                Throwable exception = ((ExceptionResponse) response).getException();
                ProtocolConverter.this.handleException(exception, this.val$command);
                ProtocolConverter.this.getStompTransport().onException(IOExceptionSupport.create(exception));
                return;
            }
            ProtocolConverter.this.sendToActiveMQ(new SessionInfo(ProtocolConverter.this.sessionId), null);
            ProtocolConverter.this.sendToActiveMQ(new ProducerInfo(ProtocolConverter.this.producerId), new 1());
        }
    }

    private static class AckEntry {
        private String messageId;
        private StompSubscription subscription;

        public AckEntry(String messageId, StompSubscription subscription) {
            this.messageId = messageId;
            this.subscription = subscription;
        }

        public MessageAck onMessageAck(TransactionId transactionId) {
            return this.subscription.onStompMessageAck(this.messageId, transactionId);
        }

        public MessageAck onMessageNack(TransactionId transactionId) throws ProtocolException {
            return this.subscription.onStompMessageNack(this.messageId, transactionId);
        }

        public String getMessageId() {
            return this.messageId;
        }

        public StompSubscription getSubscription() {
            return this.subscription;
        }
    }

    static {
        LOG = LoggerFactory.getLogger(ProtocolConverter.class);
        CONNECTION_ID_GENERATOR = new IdGenerator();
        ping = new StompFrame(Commands.KEEPALIVE);
        String version = "5.6.0";
        InputStream in = ProtocolConverter.class.getResourceAsStream("/org/apache/activemq/version.txt");
        if (in != null) {
            try {
                version = new BufferedReader(new InputStreamReader(in)).readLine();
            } catch (Exception e) {
            }
        }
        BROKER_VERSION = version;
    }

    public ProtocolConverter(StompTransport stompTransport, BrokerContext brokerContext) {
        this.connectionId = new ConnectionId(CONNECTION_ID_GENERATOR.generateId());
        this.sessionId = new SessionId(this.connectionId, -1);
        this.producerId = new ProducerId(this.sessionId, 1);
        this.consumerIdGenerator = new LongSequenceGenerator();
        this.messageIdGenerator = new LongSequenceGenerator();
        this.transactionIdGenerator = new LongSequenceGenerator();
        this.tempDestinationGenerator = new LongSequenceGenerator();
        this.resposeHandlers = new ConcurrentHashMap();
        this.subscriptionsByConsumerId = new ConcurrentHashMap();
        this.subscriptions = new ConcurrentHashMap();
        this.tempDestinations = new ConcurrentHashMap();
        this.tempDestinationAmqToStompMap = new ConcurrentHashMap();
        this.transactions = new ConcurrentHashMap();
        this.pedingAcks = new ConcurrentHashMap();
        this.ACK_ID_GENERATOR = new IdGenerator();
        this.commnadIdMutex = new Object();
        this.connected = new AtomicBoolean(false);
        this.frameTranslator = new LegacyFrameTranslator();
        this.FRAME_TRANSLATOR_FINDER = new FactoryFinder("META-INF/services/org/apache/activemq/transport/frametranslator/");
        this.version = Stomp.V1_0;
        this.defaultHeartBeat = Stomp.DEFAULT_HEART_BEAT;
        this.connectionInfo = new ConnectionInfo();
        this.stompTransport = stompTransport;
        this.brokerContext = brokerContext;
    }

    protected int generateCommandId() {
        int i;
        synchronized (this.commnadIdMutex) {
            i = this.lastCommandId;
            this.lastCommandId = i + 1;
        }
        return i;
    }

    protected ResponseHandler createResponseHandler(StompFrame command) {
        String receiptId = (String) command.getHeaders().get(Headers.RECEIPT_REQUESTED);
        return receiptId != null ? new 1(command, receiptId) : null;
    }

    protected void sendToActiveMQ(Command command, ResponseHandler handler) {
        command.setCommandId(generateCommandId());
        if (handler != null) {
            command.setResponseRequired(true);
            this.resposeHandlers.put(Integer.valueOf(command.getCommandId()), handler);
        }
        this.stompTransport.sendToActiveMQ(command);
    }

    protected void sendToStomp(StompFrame command) throws IOException {
        this.stompTransport.sendToStomp(command);
    }

    protected FrameTranslator findTranslator(String header) {
        FrameTranslator translator = this.frameTranslator;
        if (header != null) {
            try {
                translator = (FrameTranslator) this.FRAME_TRANSLATOR_FINDER.newInstance(header);
                if (translator instanceof BrokerContextAware) {
                    ((BrokerContextAware) translator).setBrokerContext(this.brokerContext);
                }
            } catch (Exception e) {
            }
        }
        return translator;
    }

    public void onStompCommand(StompFrame command) throws IOException, JMSException {
        try {
            if (command.getClass() == StompFrameError.class) {
                throw ((StompFrameError) command).getException();
            }
            String action = command.getAction();
            if (action.startsWith(Commands.SEND)) {
                onStompSend(command);
            } else if (action.startsWith(Commands.ACK)) {
                onStompAck(command);
            } else if (action.startsWith(Commands.NACK)) {
                onStompNack(command);
            } else if (action.startsWith(Commands.BEGIN_TRANSACTION)) {
                onStompBegin(command);
            } else if (action.startsWith(Commands.COMMIT_TRANSACTION)) {
                onStompCommit(command);
            } else if (action.startsWith(Commands.ABORT_TRANSACTION)) {
                onStompAbort(command);
            } else if (action.startsWith(Commands.SUBSCRIBE)) {
                onStompSubscribe(command);
            } else if (action.startsWith(Commands.UNSUBSCRIBE)) {
                onStompUnsubscribe(command);
            } else if (action.startsWith(Commands.CONNECT) || action.startsWith(Commands.STOMP)) {
                onStompConnect(command);
            } else if (action.startsWith(Commands.DISCONNECT)) {
                onStompDisconnect(command);
            } else {
                throw new ProtocolException("Unknown STOMP action: " + action);
            }
        } catch (ProtocolException e) {
            handleException(e, command);
            if (e.isFatal()) {
                getStompTransport().onException(e);
            }
        }
    }

    protected void handleException(Throwable exception, StompFrame command) throws IOException {
        LOG.warn("Exception occurred processing: \n" + command + ": " + exception.toString());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Exception detail", exception);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter stream = new PrintWriter(new OutputStreamWriter(baos, HttpRequest.CHARSET_UTF8));
        exception.printStackTrace(stream);
        stream.close();
        HashMap<String, String> headers = new HashMap();
        headers.put(Error.MESSAGE, exception.getMessage());
        headers.put(Headers.CONTENT_TYPE, Stomp.TEXT_PLAIN);
        if (command != null) {
            String receiptId = (String) command.getHeaders().get(Headers.RECEIPT_REQUESTED);
            if (receiptId != null) {
                headers.put(Headers.Response.RECEIPT_ID, receiptId);
            }
        }
        sendToStomp(new StompFrame(Responses.ERROR, headers, baos.toByteArray()));
    }

    protected void onStompSend(StompFrame command) throws IOException, JMSException {
        checkConnected();
        Map<String, String> headers = command.getHeaders();
        if (((String) headers.get(Unsubscribe.DESTINATION)) == null) {
            throw new ProtocolException("SEND received without a Destination specified!");
        }
        String stompTx = (String) headers.get(Headers.TRANSACTION);
        headers.remove(Headers.TRANSACTION);
        ActiveMQMessage message = convertMessage(command);
        message.setProducerId(this.producerId);
        message.setMessageId(new MessageId(this.producerId, this.messageIdGenerator.getNextSequenceId()));
        message.setJMSTimestamp(System.currentTimeMillis());
        if (stompTx != null) {
            TransactionId activemqTx = (TransactionId) this.transactions.get(stompTx);
            if (activemqTx == null) {
                throw new ProtocolException("Invalid transaction id: " + stompTx);
            }
            message.setTransactionId(activemqTx);
        }
        message.onSend();
        sendToActiveMQ(message, createResponseHandler(command));
    }

    protected void onStompNack(StompFrame command) throws ProtocolException {
        checkConnected();
        if (this.version.equals(Stomp.V1_0)) {
            throw new ProtocolException("NACK received but connection is in v1.0 mode.");
        }
        Map<String, String> headers = command.getHeaders();
        String subscriptionId = (String) headers.get(Message.SUBSCRIPTION);
        if (subscriptionId != null || this.version.equals(Stomp.V1_2)) {
            String messageId = (String) headers.get(Message.MESSAGE_ID);
            if (messageId != null || this.version.equals(Stomp.V1_2)) {
                String ackId = (String) headers.get(Name.MARK);
                if (ackId == null && this.version.equals(Stomp.V1_2)) {
                    throw new ProtocolException("NACK received without an ack header to acknowledge!");
                }
                TransactionId activemqTx = null;
                String stompTx = (String) headers.get(Headers.TRANSACTION);
                if (stompTx != null) {
                    activemqTx = (TransactionId) this.transactions.get(stompTx);
                    if (activemqTx == null) {
                        throw new ProtocolException("Invalid transaction id: " + stompTx);
                    }
                }
                boolean nacked = false;
                MessageAck ack;
                if (ackId != null) {
                    AckEntry pendingAck = (AckEntry) this.pedingAcks.get(ackId);
                    if (pendingAck != null) {
                        messageId = pendingAck.getMessageId();
                        ack = pendingAck.onMessageNack(activemqTx);
                        if (ack != null) {
                            sendToActiveMQ(ack, createResponseHandler(command));
                            nacked = true;
                        }
                    }
                } else if (subscriptionId != null) {
                    StompSubscription sub = (StompSubscription) this.subscriptions.get(subscriptionId);
                    if (sub != null) {
                        ack = sub.onStompMessageNack(messageId, activemqTx);
                        if (ack != null) {
                            sendToActiveMQ(ack, createResponseHandler(command));
                            nacked = true;
                        }
                    }
                }
                if (!nacked) {
                    throw new ProtocolException("Unexpected NACK received for message-id [" + messageId + "]");
                }
                return;
            }
            throw new ProtocolException("NACK received without a message-id to acknowledge!");
        }
        throw new ProtocolException("NACK received without a subscription id for acknowledge!");
    }

    protected void onStompAck(StompFrame command) throws ProtocolException {
        checkConnected();
        Map<String, String> headers = command.getHeaders();
        String messageId = (String) headers.get(Message.MESSAGE_ID);
        if (messageId != null || this.version.equals(Stomp.V1_2)) {
            String subscriptionId = (String) headers.get(Message.SUBSCRIPTION);
            if (subscriptionId == null && this.version.equals(Stomp.V1_1)) {
                throw new ProtocolException("ACK received without a subscription id for acknowledge!");
            }
            String ackId = (String) headers.get(Name.MARK);
            if (ackId == null && this.version.equals(Stomp.V1_2)) {
                throw new ProtocolException("ACK received without a ack id for acknowledge!");
            }
            TransactionId activemqTx = null;
            String stompTx = (String) headers.get(Headers.TRANSACTION);
            if (stompTx != null) {
                activemqTx = (TransactionId) this.transactions.get(stompTx);
                if (activemqTx == null) {
                    throw new ProtocolException("Invalid transaction id: " + stompTx);
                }
            }
            boolean acked = false;
            MessageAck ack;
            if (ackId == null) {
                StompSubscription sub;
                if (subscriptionId == null) {
                    for (StompSubscription sub2 : this.subscriptionsByConsumerId.values()) {
                        ack = sub2.onStompMessageAck(messageId, activemqTx);
                        if (ack != null) {
                            sendToActiveMQ(ack, createResponseHandler(command));
                            acked = true;
                            break;
                        }
                    }
                }
                sub2 = (StompSubscription) this.subscriptions.get(subscriptionId);
                if (sub2 != null) {
                    ack = sub2.onStompMessageAck(messageId, activemqTx);
                    if (ack != null) {
                        sendToActiveMQ(ack, createResponseHandler(command));
                        acked = true;
                    }
                }
            } else {
                AckEntry pendingAck = (AckEntry) this.pedingAcks.get(ackId);
                if (pendingAck != null) {
                    messageId = pendingAck.getMessageId();
                    ack = pendingAck.onMessageAck(activemqTx);
                    if (ack != null) {
                        sendToActiveMQ(ack, createResponseHandler(command));
                        acked = true;
                    }
                }
            }
            if (!acked) {
                throw new ProtocolException("Unexpected ACK received for message-id [" + messageId + "]");
            }
            return;
        }
        throw new ProtocolException("ACK received without a message-id to acknowledge!");
    }

    protected void onStompBegin(StompFrame command) throws ProtocolException {
        checkConnected();
        Map<String, String> headers = command.getHeaders();
        String stompTx = (String) headers.get(Headers.TRANSACTION);
        if (!headers.containsKey(Headers.TRANSACTION)) {
            throw new ProtocolException("Must specify the transaction you are beginning");
        } else if (this.transactions.get(stompTx) != null) {
            throw new ProtocolException("The transaction was allready started: " + stompTx);
        } else {
            LocalTransactionId activemqTx = new LocalTransactionId(this.connectionId, this.transactionIdGenerator.getNextSequenceId());
            this.transactions.put(stompTx, activemqTx);
            TransactionInfo tx = new TransactionInfo();
            tx.setConnectionId(this.connectionId);
            tx.setTransactionId(activemqTx);
            tx.setType((byte) 0);
            sendToActiveMQ(tx, createResponseHandler(command));
        }
    }

    protected void onStompCommit(StompFrame command) throws ProtocolException {
        checkConnected();
        String stompTx = (String) command.getHeaders().get(Headers.TRANSACTION);
        if (stompTx == null) {
            throw new ProtocolException("Must specify the transaction you are committing");
        }
        TransactionId activemqTx = (TransactionId) this.transactions.remove(stompTx);
        if (activemqTx == null) {
            throw new ProtocolException("Invalid transaction id: " + stompTx);
        }
        for (StompSubscription sub : this.subscriptionsByConsumerId.values()) {
            sub.onStompCommit(activemqTx);
        }
        TransactionInfo tx = new TransactionInfo();
        tx.setConnectionId(this.connectionId);
        tx.setTransactionId(activemqTx);
        tx.setType((byte) 2);
        sendToActiveMQ(tx, createResponseHandler(command));
    }

    protected void onStompAbort(StompFrame command) throws ProtocolException {
        checkConnected();
        String stompTx = (String) command.getHeaders().get(Headers.TRANSACTION);
        if (stompTx == null) {
            throw new ProtocolException("Must specify the transaction you are committing");
        }
        TransactionId activemqTx = (TransactionId) this.transactions.remove(stompTx);
        if (activemqTx == null) {
            throw new ProtocolException("Invalid transaction id: " + stompTx);
        }
        for (StompSubscription sub : this.subscriptionsByConsumerId.values()) {
            try {
                sub.onStompAbort(activemqTx);
            } catch (Exception e) {
                throw new ProtocolException("Transaction abort failed", false, e);
            }
        }
        TransactionInfo tx = new TransactionInfo();
        tx.setConnectionId(this.connectionId);
        tx.setTransactionId(activemqTx);
        tx.setType((byte) 4);
        sendToActiveMQ(tx, createResponseHandler(command));
    }

    protected void onStompSubscribe(StompFrame command) throws ProtocolException {
        checkConnected();
        FrameTranslator translator = findTranslator((String) command.getHeaders().get(Headers.TRANSFORMATION));
        Map<String, String> headers = command.getHeaders();
        String subscriptionId = (String) headers.get(Name.MARK);
        String destination = (String) headers.get(Unsubscribe.DESTINATION);
        if (this.version.equals(Stomp.V1_1) && subscriptionId == null) {
            throw new ProtocolException("SUBSCRIBE received without a subscription id!");
        }
        ActiveMQDestination actualDest = translator.convertDestination(this, destination, true);
        if (actualDest == null) {
            throw new ProtocolException("Invalid 'null' Destination.");
        }
        ConsumerId id = new ConsumerId(this.sessionId, this.consumerIdGenerator.getNextSequenceId());
        ConsumerInfo consumerInfo = new ConsumerInfo(id);
        int i = actualDest.isQueue() ? ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH : headers.containsKey("activemq.subscriptionName") ? 100 : ActiveMQPrefetchPolicy.MAX_PREFETCH_SIZE;
        consumerInfo.setPrefetchSize(i);
        consumerInfo.setDispatchAsync(true);
        String browser = (String) headers.get(Subscribe.BROWSER);
        if (browser != null && browser.equals(Stomp.TRUE)) {
            if (this.version.equals(Stomp.V1_1)) {
                consumerInfo.setBrowser(true);
                consumerInfo.setPrefetchSize(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH);
            } else {
                throw new ProtocolException("Queue Browser feature only valid for Stomp v1.1 clients!");
            }
        }
        String selector = (String) headers.remove(Subscribe.SELECTOR);
        if (selector != null) {
            consumerInfo.setSelector("convert_string_expressions:" + selector);
        }
        IntrospectionSupport.setProperties(consumerInfo, headers, "activemq.");
        if (!actualDest.isQueue() || consumerInfo.getSubscriptionName() == null) {
            StompSubscription stompSubscription;
            consumerInfo.setDestination(translator.convertDestination(this, destination, true));
            if (consumerInfo.isBrowser()) {
                stompSubscription = new StompQueueBrowserSubscription(this, subscriptionId, consumerInfo, (String) headers.get(Headers.TRANSFORMATION));
            } else {
                stompSubscription = new StompSubscription(this, subscriptionId, consumerInfo, (String) headers.get(Headers.TRANSFORMATION));
            }
            stompSubscription.setDestination(actualDest);
            String ackMode = (String) headers.get(Subscribe.ACK_MODE);
            if (StompSubscription.CLIENT_ACK.equals(ackMode)) {
                stompSubscription.setAckMode(StompSubscription.CLIENT_ACK);
            } else if (StompSubscription.INDIVIDUAL_ACK.equals(ackMode)) {
                stompSubscription.setAckMode(StompSubscription.INDIVIDUAL_ACK);
            } else {
                stompSubscription.setAckMode(StompSubscription.AUTO_ACK);
            }
            this.subscriptionsByConsumerId.put(id, stompSubscription);
            if (subscriptionId != null) {
                this.subscriptions.put(subscriptionId, stompSubscription);
            }
            String receiptId = (String) command.getHeaders().get(Headers.RECEIPT_REQUESTED);
            if (receiptId == null || consumerInfo.getPrefetchSize() <= 0) {
                sendToActiveMQ(consumerInfo, createResponseHandler(command));
                return;
            }
            StompFrame cmd = command;
            int prefetch = consumerInfo.getPrefetchSize();
            consumerInfo.setPrefetchSize(0);
            sendToActiveMQ(consumerInfo, new 2(cmd, receiptId, prefetch, actualDest, id));
            return;
        }
        throw new ProtocolException("Invalid Subscription: cannot durably subscribe to a Queue destination!");
    }

    protected void onStompUnsubscribe(StompFrame command) throws ProtocolException {
        checkConnected();
        Map<String, String> headers = command.getHeaders();
        ActiveMQDestination destination = null;
        Object o = headers.get(Unsubscribe.DESTINATION);
        if (o != null) {
            destination = findTranslator((String) command.getHeaders().get(Headers.TRANSFORMATION)).convertDestination(this, (String) o, true);
        }
        String subscriptionId = (String) headers.get(Name.MARK);
        if (this.version.equals(Stomp.V1_1) && subscriptionId == null) {
            throw new ProtocolException("UNSUBSCRIBE received without a subscription id!");
        } else if (subscriptionId == null && destination == null) {
            throw new ProtocolException("Must specify the subscriptionId or the destination you are unsubscribing from");
        } else {
            String durable = (String) command.getHeaders().get("activemq.subscriptionName");
            String clientId = durable;
            if (this.version.equals(Stomp.V1_1)) {
                clientId = this.connectionInfo.getClientId();
            }
            if (durable != null) {
                RemoveSubscriptionInfo info = new RemoveSubscriptionInfo();
                info.setClientId(clientId);
                info.setSubscriptionName(durable);
                info.setConnectionId(this.connectionId);
                sendToActiveMQ(info, createResponseHandler(command));
                return;
            }
            StompSubscription sub;
            if (subscriptionId != null) {
                sub = (StompSubscription) this.subscriptions.remove(subscriptionId);
                if (sub != null) {
                    sendToActiveMQ(sub.getConsumerInfo().createRemoveCommand(), createResponseHandler(command));
                    return;
                }
            }
            Iterator<StompSubscription> iter = this.subscriptionsByConsumerId.values().iterator();
            while (iter.hasNext()) {
                sub = (StompSubscription) iter.next();
                if (destination != null && destination.equals(sub.getDestination())) {
                    sendToActiveMQ(sub.getConsumerInfo().createRemoveCommand(), createResponseHandler(command));
                    iter.remove();
                    return;
                }
            }
            throw new ProtocolException("No subscription matched.");
        }
    }

    protected void onStompConnect(StompFrame command) throws ProtocolException {
        if (this.connected.get()) {
            throw new ProtocolException("Allready connected.");
        }
        Map<String, String> headers = command.getHeaders();
        String login = (String) headers.get(Connect.LOGIN);
        String passcode = (String) headers.get(Connect.PASSCODE);
        String clientId = (String) headers.get(Connect.CLIENT_ID);
        String heartBeat = (String) headers.get(Connected.HEART_BEAT);
        if (heartBeat == null) {
            heartBeat = this.defaultHeartBeat;
        }
        this.version = StompCodec.detectVersion(headers);
        configureInactivityMonitor(heartBeat.trim());
        IntrospectionSupport.setProperties(this.connectionInfo, headers, "activemq.");
        this.connectionInfo.setConnectionId(this.connectionId);
        if (clientId != null) {
            this.connectionInfo.setClientId(clientId);
        } else {
            this.connectionInfo.setClientId(Stomp.EMPTY + this.connectionInfo.getConnectionId().toString());
        }
        this.connectionInfo.setResponseRequired(true);
        this.connectionInfo.setUserName(login);
        this.connectionInfo.setPassword(passcode);
        this.connectionInfo.setTransportContext(command.getTransportContext());
        sendToActiveMQ(this.connectionInfo, new 3(command, headers));
    }

    protected void onStompDisconnect(StompFrame command) throws ProtocolException {
        if (this.connected.get()) {
            sendToActiveMQ(this.connectionInfo.createRemoveCommand(), createResponseHandler(command));
            sendToActiveMQ(new ShutdownInfo(), createResponseHandler(command));
            this.connected.set(false);
        }
    }

    protected void checkConnected() throws ProtocolException {
        if (!this.connected.get()) {
            throw new ProtocolException("Not connected.");
        }
    }

    public void onActiveMQCommand(Command command) throws IOException, JMSException {
        if (command.isResponse()) {
            Response response = (Response) command;
            ResponseHandler rh = (ResponseHandler) this.resposeHandlers.remove(Integer.valueOf(response.getCorrelationId()));
            if (rh != null) {
                rh.onResponse(this, response);
            } else if (response.isException()) {
                handleException(((ExceptionResponse) response).getException(), null);
            }
        } else if (command.isMessageDispatch()) {
            MessageDispatch md = (MessageDispatch) command;
            StompSubscription sub = (StompSubscription) this.subscriptionsByConsumerId.get(md.getConsumerId());
            if (sub != null) {
                String ackId = null;
                if (this.version.equals(Stomp.V1_2) && sub.getAckMode() != StompSubscription.AUTO_ACK) {
                    AckEntry pendingAck = new AckEntry(md.getMessage().getMessageId().toString(), sub);
                    ackId = this.ACK_ID_GENERATOR.generateId();
                    this.pedingAcks.put(ackId, pendingAck);
                }
                try {
                    sub.onMessageDispatch(md, ackId);
                } catch (Exception e) {
                    if (ackId != null) {
                        this.pedingAcks.remove(ackId);
                    }
                }
            }
        } else if (command.getDataStructureType() == 10) {
            this.stompTransport.sendToStomp(ping);
        } else if (command.getDataStructureType() == 16) {
            handleException(((ConnectionError) command).getException(), null);
        }
    }

    public ActiveMQMessage convertMessage(StompFrame command) throws IOException, JMSException {
        return findTranslator((String) command.getHeaders().get(Headers.TRANSFORMATION)).convertFrame(this, command);
    }

    public StompFrame convertMessage(ActiveMQMessage message, boolean ignoreTransformation) throws IOException, JMSException {
        if (ignoreTransformation) {
            return this.frameTranslator.convertMessage(this, message);
        }
        return findTranslator(message.getStringProperty(Headers.TRANSFORMATION)).convertMessage(this, message);
    }

    public StompTransport getStompTransport() {
        return this.stompTransport;
    }

    public ActiveMQDestination createTempDestination(String name, boolean topic) {
        ActiveMQDestination rc = (ActiveMQDestination) this.tempDestinations.get(name);
        if (rc == null) {
            if (topic) {
                rc = new ActiveMQTempTopic(this.connectionId, this.tempDestinationGenerator.getNextSequenceId());
            } else {
                rc = new ActiveMQTempQueue(this.connectionId, this.tempDestinationGenerator.getNextSequenceId());
            }
            sendToActiveMQ(new DestinationInfo(this.connectionId, (byte) 0, rc), null);
            this.tempDestinations.put(name, rc);
            this.tempDestinationAmqToStompMap.put(rc.getQualifiedName(), name);
        }
        return rc;
    }

    public String getCreatedTempDestinationName(ActiveMQDestination destination) {
        return (String) this.tempDestinationAmqToStompMap.get(destination.getQualifiedName());
    }

    public String getDefaultHeartBeat() {
        return this.defaultHeartBeat;
    }

    public void setDefaultHeartBeat(String defaultHeartBeat) {
        this.defaultHeartBeat = defaultHeartBeat;
    }

    protected void configureInactivityMonitor(String heartBeatConfig) throws ProtocolException {
        String[] keepAliveOpts = heartBeatConfig.split(Stomp.COMMA);
        if (keepAliveOpts == null || keepAliveOpts.length != 2) {
            throw new ProtocolException("Invalid heart-beat header:" + heartBeatConfig, true);
        }
        try {
            this.hbReadInterval = Long.parseLong(keepAliveOpts[0]);
            this.hbWriteInterval = Long.parseLong(keepAliveOpts[1]);
            try {
                StompInactivityMonitor monitor = this.stompTransport.getInactivityMonitor();
                monitor.setReadCheckTime(this.hbReadInterval);
                monitor.setInitialDelayTime(Math.min(this.hbReadInterval, this.hbWriteInterval));
                monitor.setWriteCheckTime(this.hbWriteInterval);
                monitor.startMonitoring();
            } catch (Exception e) {
                this.hbReadInterval = 0;
                this.hbWriteInterval = 0;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Stomp Connect heartbeat conf RW[" + this.hbReadInterval + Stomp.COMMA + this.hbWriteInterval + "]");
            }
        } catch (NumberFormatException e2) {
            throw new ProtocolException("Invalid heart-beat header:" + heartBeatConfig, true);
        }
    }

    protected void sendReceipt(StompFrame command) {
        String receiptId = (String) command.getHeaders().get(Headers.RECEIPT_REQUESTED);
        if (receiptId != null) {
            StompFrame sc = new StompFrame();
            sc.setAction(Responses.RECEIPT);
            sc.setHeaders(new HashMap(1));
            sc.getHeaders().put(Headers.Response.RECEIPT_ID, receiptId);
            try {
                sendToStomp(sc);
            } catch (IOException e) {
                LOG.warn("Could not send a receipt for " + command, e);
            }
        }
    }
}
