package org.apache.activemq.state;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import javax.jms.TransactionRolledBackException;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.ConsumerControl;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.DestinationInfo;
import org.apache.activemq.command.ExceptionResponse;
import org.apache.activemq.command.IntegerResponse;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.MessagePull;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.ProducerInfo;
import org.apache.activemq.command.Response;
import org.apache.activemq.command.SessionId;
import org.apache.activemq.command.SessionInfo;
import org.apache.activemq.command.TransactionInfo;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.util.IOExceptionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionStateTracker extends CommandVisitorAdapter {
    private static final Logger LOG;
    private static final int MESSAGE_PULL_SIZE = 400;
    private static final Tracked TRACKED_RESPONSE_MARKER;
    protected final ConcurrentHashMap<ConnectionId, ConnectionState> connectionStates;
    private int currentCacheSize;
    private int maxCacheSize;
    private Map<Object, Command> messageCache;
    private boolean restoreConsumers;
    private boolean restoreProducers;
    private boolean restoreSessions;
    private boolean restoreTransaction;
    private boolean trackMessages;
    private boolean trackTransactionProducers;
    private boolean trackTransactions;

    class 1 extends LinkedHashMap<Object, Command> {
        1() {
        }

        protected boolean removeEldestEntry(Entry<Object, Command> eldest) {
            boolean result = ConnectionStateTracker.this.currentCacheSize > ConnectionStateTracker.this.maxCacheSize;
            if (result) {
                if (eldest.getValue() instanceof Message) {
                    ConnectionStateTracker.access$020(ConnectionStateTracker.this, ((Message) eldest.getValue()).getSize());
                } else if (eldest.getValue() instanceof MessagePull) {
                    ConnectionStateTracker.access$020(ConnectionStateTracker.this, ConnectionStateTracker.MESSAGE_PULL_SIZE);
                }
                if (ConnectionStateTracker.LOG.isTraceEnabled()) {
                    ConnectionStateTracker.LOG.trace("removing tracked message: " + eldest.getKey());
                }
            }
            return result;
        }
    }

    private class RemoveTransactionAction implements ResponseHandler {
        private final TransactionInfo info;

        public RemoveTransactionAction(TransactionInfo info) {
            this.info = info;
        }

        public void onResponse(Command response) {
            ConnectionState cs = (ConnectionState) ConnectionStateTracker.this.connectionStates.get(this.info.getConnectionId());
            if (cs != null) {
                cs.removeTransactionState(this.info.getTransactionId());
            }
        }
    }

    private class PrepareReadonlyTransactionAction extends RemoveTransactionAction {
        public PrepareReadonlyTransactionAction(TransactionInfo info) {
            super(info);
        }

        public void onResponse(Command command) {
            if (3 == ((IntegerResponse) command).getResult()) {
                super.onResponse(command);
            }
        }
    }

    public ConnectionStateTracker() {
        this.connectionStates = new ConcurrentHashMap();
        this.restoreSessions = true;
        this.restoreConsumers = true;
        this.restoreProducers = true;
        this.restoreTransaction = true;
        this.trackMessages = true;
        this.trackTransactionProducers = true;
        this.maxCacheSize = AccessibilityNodeInfoCompat.ACTION_SET_SELECTION;
        this.messageCache = new 1();
    }

    static /* synthetic */ int access$020(ConnectionStateTracker x0, int x1) {
        int i = x0.currentCacheSize - x1;
        x0.currentCacheSize = i;
        return i;
    }

    static {
        LOG = LoggerFactory.getLogger(ConnectionStateTracker.class);
        TRACKED_RESPONSE_MARKER = new Tracked(null);
    }

    public Tracked track(Command command) throws IOException {
        try {
            return (Tracked) command.visit(this);
        } catch (IOException e) {
            throw e;
        } catch (Throwable e2) {
            IOException create = IOExceptionSupport.create(e2);
        }
    }

    public void trackBack(Command command) {
        if (command == null) {
            return;
        }
        if (this.trackMessages && command.isMessage()) {
            Message message = (Message) command;
            if (message.getTransactionId() == null) {
                this.currentCacheSize += message.getSize();
            }
        } else if (command instanceof MessagePull) {
            this.currentCacheSize += MESSAGE_PULL_SIZE;
        }
    }

    public void restore(Transport transport) throws IOException {
        for (ConnectionState connectionState : this.connectionStates.values()) {
            connectionState.getInfo().setFailoverReconnect(true);
            if (LOG.isDebugEnabled()) {
                LOG.debug("conn: " + connectionState.getInfo().getConnectionId());
            }
            transport.oneway(connectionState.getInfo());
            restoreTempDestinations(transport, connectionState);
            if (this.restoreSessions) {
                restoreSessions(transport, connectionState);
            }
            if (this.restoreTransaction) {
                restoreTransactions(transport, connectionState);
            }
        }
        for (Command msg : this.messageCache.values()) {
            if (LOG.isDebugEnabled()) {
                Object messageId;
                Logger logger = LOG;
                StringBuilder append = new StringBuilder().append("command: ");
                if (msg.isMessage()) {
                    messageId = ((Message) msg).getMessageId();
                } else {
                    Command command = msg;
                }
                logger.debug(append.append(messageId).toString());
            }
            transport.oneway(msg);
        }
    }

    private void restoreTransactions(Transport transport, ConnectionState connectionState) throws IOException {
        Vector<TransactionInfo> toRollback = new Vector();
        for (TransactionState transactionState : connectionState.getTransactionStates()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("tx: " + transactionState.getId());
            }
            if (!transactionState.getCommands().isEmpty()) {
                Command lastCommand = (Command) transactionState.getCommands().get(transactionState.getCommands().size() - 1);
                if (lastCommand instanceof TransactionInfo) {
                    TransactionInfo transactionInfo = (TransactionInfo) lastCommand;
                    if (transactionInfo.getType() == 2) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("rolling back potentially completed tx: " + transactionState.getId());
                        }
                        toRollback.add(transactionInfo);
                    }
                }
            }
            for (ProducerState producerState : transactionState.getProducerStates().values()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("tx replay producer :" + producerState.getInfo());
                }
                transport.oneway(producerState.getInfo());
            }
            for (Command command : transactionState.getCommands()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("tx replay: " + command);
                }
                transport.oneway(command);
            }
            for (ProducerState producerState2 : transactionState.getProducerStates().values()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("tx remove replayed producer :" + producerState2.getInfo());
                }
                transport.oneway(producerState2.getInfo().createRemoveCommand());
            }
        }
        Iterator it = toRollback.iterator();
        while (it.hasNext()) {
            TransactionInfo command2 = (TransactionInfo) it.next();
            ExceptionResponse response = new ExceptionResponse();
            response.setException(new TransactionRolledBackException("Transaction completion in doubt due to failover. Forcing rollback of " + command2.getTransactionId()));
            response.setCorrelationId(command2.getCommandId());
            transport.getTransportListener().onCommand(response);
        }
    }

    protected void restoreSessions(Transport transport, ConnectionState connectionState) throws IOException {
        for (SessionState sessionState : connectionState.getSessionStates()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("session: " + sessionState.getInfo().getSessionId());
            }
            transport.oneway(sessionState.getInfo());
            if (this.restoreProducers) {
                restoreProducers(transport, sessionState);
            }
            if (this.restoreConsumers) {
                restoreConsumers(transport, sessionState);
            }
        }
    }

    protected void restoreConsumers(Transport transport, SessionState sessionState) throws IOException {
        ConnectionState connectionState = (ConnectionState) this.connectionStates.get(sessionState.getInfo().getSessionId().getParentId());
        boolean connectionInterruptionProcessingComplete = connectionState.isConnectionInterruptProcessingComplete();
        for (ConsumerState consumerState : sessionState.getConsumerStates()) {
            ConsumerInfo infoToSend = consumerState.getInfo();
            if (!connectionInterruptionProcessingComplete && infoToSend.getPrefetchSize() > 0) {
                infoToSend = consumerState.getInfo().copy();
                connectionState.getRecoveringPullConsumers().put(infoToSend.getConsumerId(), consumerState.getInfo());
                infoToSend.setPrefetchSize(0);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("restore consumer: " + infoToSend.getConsumerId() + " in pull mode pending recovery, overriding prefetch: " + consumerState.getInfo().getPrefetchSize());
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("restore consumer: " + infoToSend.getConsumerId());
            }
            transport.oneway(infoToSend);
        }
    }

    protected void restoreProducers(Transport transport, SessionState sessionState) throws IOException {
        for (ProducerState producerState : sessionState.getProducerStates()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("producer: " + producerState.getInfo().getProducerId());
            }
            transport.oneway(producerState.getInfo());
        }
    }

    protected void restoreTempDestinations(Transport transport, ConnectionState connectionState) throws IOException {
        for (DestinationInfo info : connectionState.getTempDestinations()) {
            transport.oneway(info);
            if (LOG.isDebugEnabled()) {
                LOG.debug("tempDest: " + info.getDestination());
            }
        }
    }

    public Response processAddDestination(DestinationInfo info) {
        if (info != null) {
            ConnectionState cs = (ConnectionState) this.connectionStates.get(info.getConnectionId());
            if (cs != null && info.getDestination().isTemporary()) {
                cs.addTempDestination(info);
            }
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processRemoveDestination(DestinationInfo info) {
        if (info != null) {
            ConnectionState cs = (ConnectionState) this.connectionStates.get(info.getConnectionId());
            if (cs != null && info.getDestination().isTemporary()) {
                cs.removeTempDestination(info.getDestination());
            }
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processAddProducer(ProducerInfo info) {
        if (!(info == null || info.getProducerId() == null)) {
            SessionId sessionId = info.getProducerId().getParentId();
            if (sessionId != null) {
                ConnectionId connectionId = sessionId.getParentId();
                if (connectionId != null) {
                    ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                    if (cs != null) {
                        SessionState ss = cs.getSessionState(sessionId);
                        if (ss != null) {
                            ss.addProducer(info);
                        }
                    }
                }
            }
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processRemoveProducer(ProducerId id) {
        if (id != null) {
            SessionId sessionId = id.getParentId();
            if (sessionId != null) {
                ConnectionId connectionId = sessionId.getParentId();
                if (connectionId != null) {
                    ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                    if (cs != null) {
                        SessionState ss = cs.getSessionState(sessionId);
                        if (ss != null) {
                            ss.removeProducer(id);
                        }
                    }
                }
            }
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processAddConsumer(ConsumerInfo info) {
        if (info != null) {
            SessionId sessionId = info.getConsumerId().getParentId();
            if (sessionId != null) {
                ConnectionId connectionId = sessionId.getParentId();
                if (connectionId != null) {
                    ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                    if (cs != null) {
                        SessionState ss = cs.getSessionState(sessionId);
                        if (ss != null) {
                            ss.addConsumer(info);
                        }
                    }
                }
            }
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processRemoveConsumer(ConsumerId id, long lastDeliveredSequenceId) {
        if (id != null) {
            SessionId sessionId = id.getParentId();
            if (sessionId != null) {
                ConnectionId connectionId = sessionId.getParentId();
                if (connectionId != null) {
                    ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                    if (cs != null) {
                        SessionState ss = cs.getSessionState(sessionId);
                        if (ss != null) {
                            ss.removeConsumer(id);
                        }
                    }
                }
            }
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processAddSession(SessionInfo info) {
        if (info != null) {
            ConnectionId connectionId = info.getSessionId().getParentId();
            if (connectionId != null) {
                ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                if (cs != null) {
                    cs.addSession(info);
                }
            }
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processRemoveSession(SessionId id, long lastDeliveredSequenceId) {
        if (id != null) {
            ConnectionId connectionId = id.getParentId();
            if (connectionId != null) {
                ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                if (cs != null) {
                    cs.removeSession(id);
                }
            }
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processAddConnection(ConnectionInfo info) {
        if (info != null) {
            this.connectionStates.put(info.getConnectionId(), new ConnectionState(info));
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processRemoveConnection(ConnectionId id, long lastDeliveredSequenceId) throws Exception {
        if (id != null) {
            this.connectionStates.remove(id);
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processMessage(Message send) throws Exception {
        if (send != null) {
            if (this.trackTransactions && send.getTransactionId() != null) {
                ProducerId producerId = send.getProducerId();
                ConnectionId connectionId = producerId.getParentId().getParentId();
                if (connectionId != null) {
                    ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                    if (cs != null) {
                        TransactionState transactionState = cs.getTransactionState(send.getTransactionId());
                        if (transactionState != null) {
                            transactionState.addCommand(send);
                            if (this.trackTransactionProducers) {
                                cs.getSessionState(producerId.getParentId()).getProducerState(producerId).setTransactionState(transactionState);
                            }
                        }
                    }
                }
                return TRACKED_RESPONSE_MARKER;
            } else if (this.trackMessages) {
                this.messageCache.put(send.getMessageId(), send);
            }
        }
        return null;
    }

    public Response processBeginTransaction(TransactionInfo info) {
        if (!this.trackTransactions || info == null || info.getTransactionId() == null) {
            return null;
        }
        ConnectionId connectionId = info.getConnectionId();
        if (connectionId != null) {
            ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
            if (cs != null) {
                cs.addTransactionState(info.getTransactionId());
                cs.getTransactionState(info.getTransactionId()).addCommand(info);
            }
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processPrepareTransaction(TransactionInfo info) throws Exception {
        if (this.trackTransactions && info != null) {
            ConnectionId connectionId = info.getConnectionId();
            if (connectionId != null) {
                ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                if (cs != null) {
                    TransactionState transactionState = cs.getTransactionState(info.getTransactionId());
                    if (transactionState != null) {
                        transactionState.addCommand(info);
                        return new Tracked(new PrepareReadonlyTransactionAction(info));
                    }
                }
            }
        }
        return null;
    }

    public Response processCommitTransactionOnePhase(TransactionInfo info) throws Exception {
        if (this.trackTransactions && info != null) {
            ConnectionId connectionId = info.getConnectionId();
            if (connectionId != null) {
                ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                if (cs != null) {
                    TransactionState transactionState = cs.getTransactionState(info.getTransactionId());
                    if (transactionState != null) {
                        transactionState.addCommand(info);
                        return new Tracked(new RemoveTransactionAction(info));
                    }
                }
            }
        }
        return null;
    }

    public Response processCommitTransactionTwoPhase(TransactionInfo info) throws Exception {
        if (this.trackTransactions && info != null) {
            ConnectionId connectionId = info.getConnectionId();
            if (connectionId != null) {
                ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                if (cs != null) {
                    TransactionState transactionState = cs.getTransactionState(info.getTransactionId());
                    if (transactionState != null) {
                        transactionState.addCommand(info);
                        return new Tracked(new RemoveTransactionAction(info));
                    }
                }
            }
        }
        return null;
    }

    public Response processRollbackTransaction(TransactionInfo info) throws Exception {
        if (this.trackTransactions && info != null) {
            ConnectionId connectionId = info.getConnectionId();
            if (connectionId != null) {
                ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
                if (cs != null) {
                    TransactionState transactionState = cs.getTransactionState(info.getTransactionId());
                    if (transactionState != null) {
                        transactionState.addCommand(info);
                        return new Tracked(new RemoveTransactionAction(info));
                    }
                }
            }
        }
        return null;
    }

    public Response processEndTransaction(TransactionInfo info) throws Exception {
        if (!this.trackTransactions || info == null) {
            return null;
        }
        ConnectionId connectionId = info.getConnectionId();
        if (connectionId != null) {
            ConnectionState cs = (ConnectionState) this.connectionStates.get(connectionId);
            if (cs != null) {
                TransactionState transactionState = cs.getTransactionState(info.getTransactionId());
                if (transactionState != null) {
                    transactionState.addCommand(info);
                }
            }
        }
        return TRACKED_RESPONSE_MARKER;
    }

    public Response processMessagePull(MessagePull pull) throws Exception {
        if (pull != null) {
            this.messageCache.put((pull.getDestination() + "::" + pull.getConsumerId()).intern(), pull);
        }
        return null;
    }

    public boolean isRestoreConsumers() {
        return this.restoreConsumers;
    }

    public void setRestoreConsumers(boolean restoreConsumers) {
        this.restoreConsumers = restoreConsumers;
    }

    public boolean isRestoreProducers() {
        return this.restoreProducers;
    }

    public void setRestoreProducers(boolean restoreProducers) {
        this.restoreProducers = restoreProducers;
    }

    public boolean isRestoreSessions() {
        return this.restoreSessions;
    }

    public void setRestoreSessions(boolean restoreSessions) {
        this.restoreSessions = restoreSessions;
    }

    public boolean isTrackTransactions() {
        return this.trackTransactions;
    }

    public void setTrackTransactions(boolean trackTransactions) {
        this.trackTransactions = trackTransactions;
    }

    public boolean isTrackTransactionProducers() {
        return this.trackTransactionProducers;
    }

    public void setTrackTransactionProducers(boolean trackTransactionProducers) {
        this.trackTransactionProducers = trackTransactionProducers;
    }

    public boolean isRestoreTransaction() {
        return this.restoreTransaction;
    }

    public void setRestoreTransaction(boolean restoreTransaction) {
        this.restoreTransaction = restoreTransaction;
    }

    public boolean isTrackMessages() {
        return this.trackMessages;
    }

    public void setTrackMessages(boolean trackMessages) {
        this.trackMessages = trackMessages;
    }

    public int getMaxCacheSize() {
        return this.maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public void connectionInterruptProcessingComplete(Transport transport, ConnectionId connectionId) {
        ConnectionState connectionState = (ConnectionState) this.connectionStates.get(connectionId);
        if (connectionState != null) {
            connectionState.setConnectionInterruptProcessingComplete(true);
            Map<ConsumerId, ConsumerInfo> stalledConsumers = connectionState.getRecoveringPullConsumers();
            for (Entry<ConsumerId, ConsumerInfo> entry : stalledConsumers.entrySet()) {
                ConsumerControl control = new ConsumerControl();
                control.setConsumerId((ConsumerId) entry.getKey());
                control.setPrefetch(((ConsumerInfo) entry.getValue()).getPrefetchSize());
                control.setDestination(((ConsumerInfo) entry.getValue()).getDestination());
                try {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("restored recovering consumer: " + control.getConsumerId() + " with: " + control.getPrefetch());
                    }
                    transport.oneway(control);
                } catch (Exception ex) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Failed to submit control for consumer: " + control.getConsumerId() + " with: " + control.getPrefetch(), ex);
                    }
                }
            }
            stalledConsumers.clear();
        }
    }

    public void transportInterrupted(ConnectionId connectionId) {
        ConnectionState connectionState = (ConnectionState) this.connectionStates.get(connectionId);
        if (connectionState != null) {
            connectionState.setConnectionInterruptProcessingComplete(false);
        }
    }
}
