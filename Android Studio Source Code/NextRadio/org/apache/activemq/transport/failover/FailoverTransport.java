package org.apache.activemq.transport.failover;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.activemq.broker.SslContext;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.ConnectionControl;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.Response;
import org.apache.activemq.state.ConnectionStateTracker;
import org.apache.activemq.state.Tracked;
import org.apache.activemq.thread.Task;
import org.apache.activemq.thread.TaskRunner;
import org.apache.activemq.thread.TaskRunnerFactory;
import org.apache.activemq.transport.CompositeTransport;
import org.apache.activemq.transport.DefaultTransportListener;
import org.apache.activemq.transport.FutureResponse;
import org.apache.activemq.transport.ResponseCallback;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFactory;
import org.apache.activemq.transport.TransportListener;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailoverTransport implements CompositeTransport {
    private static final int DEFAULT_INITIAL_RECONNECT_DELAY = 10;
    private static final int INFINITE = -1;
    private static final Logger LOG;
    private double backOffMultiplier;
    private boolean backup;
    private final Object backupMutex;
    private int backupPoolSize;
    private final List<BackupTransport> backups;
    private SslContext brokerSslContext;
    private int connectFailures;
    private boolean connected;
    private boolean connectedToPriority;
    private final AtomicReference<Transport> connectedTransport;
    private URI connectedTransportURI;
    private Exception connectionFailure;
    private boolean disposed;
    private final TransportListener disposedListener;
    private boolean doRebalance;
    private URI failedConnectTransportURI;
    private boolean firstConnection;
    private long initialReconnectDelay;
    private boolean initialized;
    private final Object listenerMutex;
    private int maxCacheSize;
    private int maxReconnectAttempts;
    private long maxReconnectDelay;
    private final TransportListener myTransportListener;
    private boolean priorityBackup;
    private boolean priorityBackupAvailable;
    private ArrayList<URI> priorityList;
    private boolean randomize;
    private boolean rebalanceUpdateURIs;
    private long reconnectDelay;
    private final Object reconnectMutex;
    private boolean reconnectSupported;
    private final TaskRunner reconnectTask;
    private final TaskRunnerFactory reconnectTaskFactory;
    private final Map<Integer, Command> requestMap;
    private final Object sleepMutex;
    private boolean started;
    private int startupMaxReconnectAttempts;
    private final ConnectionStateTracker stateTracker;
    private long timeout;
    private boolean trackMessages;
    private boolean trackTransactionProducers;
    private TransportListener transportListener;
    private boolean updateURIsSupported;
    private String updateURIsURL;
    private final CopyOnWriteArrayList<URI> updated;
    private final CopyOnWriteArrayList<URI> uris;
    private boolean useExponentialBackOff;

    class 1 extends DefaultTransportListener {
        1() {
        }
    }

    class 2 implements Task {
        2() {
        }

        public boolean iterate() {
            boolean result = false;
            if (!FailoverTransport.this.started) {
                return false;
            }
            boolean buildBackup = true;
            synchronized (FailoverTransport.this.backupMutex) {
                if ((FailoverTransport.this.connectedTransport.get() == null || FailoverTransport.this.doRebalance || FailoverTransport.this.priorityBackupAvailable) && !FailoverTransport.this.disposed) {
                    result = FailoverTransport.this.doReconnect();
                    buildBackup = false;
                    FailoverTransport.this.connectedToPriority = FailoverTransport.this.isPriority(FailoverTransport.this.connectedTransportURI);
                }
            }
            if (buildBackup) {
                FailoverTransport.this.buildBackups();
                if (FailoverTransport.this.priorityBackup && !FailoverTransport.this.connectedToPriority) {
                    try {
                        FailoverTransport.this.doDelay();
                        if (FailoverTransport.this.reconnectTask == null) {
                            return true;
                        }
                        FailoverTransport.this.reconnectTask.wakeup();
                    } catch (InterruptedException e) {
                        FailoverTransport.LOG.debug("Reconnect task has been interrupted.", e);
                    }
                }
            } else {
                try {
                    if (FailoverTransport.this.reconnectTask == null) {
                        return true;
                    }
                    FailoverTransport.this.reconnectTask.wakeup();
                } catch (InterruptedException e2) {
                    FailoverTransport.LOG.debug("Reconnect task has been interrupted.", e2);
                }
            }
            return result;
        }
    }

    class 3 implements TransportListener {
        3() {
        }

        public void onCommand(Object o) {
            Command command = (Command) o;
            if (command != null) {
                if (command.isResponse()) {
                    Object object;
                    synchronized (FailoverTransport.this.requestMap) {
                        object = FailoverTransport.this.requestMap.remove(Integer.valueOf(((Response) command).getCorrelationId()));
                    }
                    if (object != null && object.getClass() == Tracked.class) {
                        ((Tracked) object).onResponses(command);
                    }
                }
                if (!FailoverTransport.this.initialized) {
                    FailoverTransport.this.initialized = true;
                }
                if (command.isConnectionControl()) {
                    FailoverTransport.this.handleConnectionControl((ConnectionControl) command);
                }
                if (FailoverTransport.this.transportListener != null) {
                    FailoverTransport.this.transportListener.onCommand(command);
                }
            }
        }

        public void onException(IOException error) {
            try {
                FailoverTransport.this.handleTransportFailure(error);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                FailoverTransport.this.transportListener.onException(new InterruptedIOException());
            }
        }

        public void transportInterupted() {
            if (FailoverTransport.this.transportListener != null) {
                FailoverTransport.this.transportListener.transportInterupted();
            }
        }

        public void transportResumed() {
            if (FailoverTransport.this.transportListener != null) {
                FailoverTransport.this.transportListener.transportResumed();
            }
        }
    }

    static {
        LOG = LoggerFactory.getLogger(FailoverTransport.class);
    }

    public FailoverTransport() throws InterruptedIOException {
        this.uris = new CopyOnWriteArrayList();
        this.updated = new CopyOnWriteArrayList();
        this.reconnectMutex = new Object();
        this.backupMutex = new Object();
        this.sleepMutex = new Object();
        this.listenerMutex = new Object();
        this.stateTracker = new ConnectionStateTracker();
        this.requestMap = new LinkedHashMap();
        this.connectedTransport = new AtomicReference();
        this.initialReconnectDelay = 10;
        this.maxReconnectDelay = 30000;
        this.backOffMultiplier = 2.0d;
        this.timeout = -1;
        this.useExponentialBackOff = true;
        this.randomize = true;
        this.maxReconnectAttempts = INFINITE;
        this.startupMaxReconnectAttempts = INFINITE;
        this.reconnectDelay = 10;
        this.firstConnection = true;
        this.backup = false;
        this.backups = new CopyOnWriteArrayList();
        this.backupPoolSize = 1;
        this.trackMessages = false;
        this.trackTransactionProducers = true;
        this.maxCacheSize = AccessibilityNodeInfoCompat.ACTION_SET_SELECTION;
        this.disposedListener = new 1();
        this.myTransportListener = createTransportListener();
        this.updateURIsSupported = true;
        this.reconnectSupported = true;
        this.updateURIsURL = null;
        this.rebalanceUpdateURIs = true;
        this.doRebalance = false;
        this.connectedToPriority = false;
        this.priorityBackup = false;
        this.priorityList = new ArrayList();
        this.priorityBackupAvailable = false;
        this.brokerSslContext = SslContext.getCurrentSslContext();
        this.stateTracker.setTrackTransactions(true);
        this.reconnectTaskFactory = new TaskRunnerFactory();
        this.reconnectTaskFactory.init();
        this.reconnectTask = this.reconnectTaskFactory.createTaskRunner(new 2(), "ActiveMQ Failover Worker: " + System.identityHashCode(this));
    }

    TransportListener createTransportListener() {
        return new 3();
    }

    public final void disposeTransport(Transport transport) {
        transport.setTransportListener(this.disposedListener);
        ServiceSupport.dispose(transport);
    }

    public final void handleTransportFailure(IOException e) throws InterruptedException {
        if (LOG.isTraceEnabled()) {
            LOG.trace(this + " handleTransportFailure: " + e);
        }
        Transport transport = (Transport) this.connectedTransport.getAndSet(null);
        if (transport == null) {
            synchronized (this.reconnectMutex) {
                transport = (Transport) this.connectedTransport.getAndSet(null);
            }
        }
        if (transport != null) {
            disposeTransport(transport);
            boolean reconnectOk = false;
            synchronized (this.reconnectMutex) {
                if (canReconnect()) {
                    reconnectOk = true;
                }
                LOG.warn("Transport (" + transport.getRemoteAddress() + ") failed, reason:  " + e + (reconnectOk ? Stomp.COMMA : ", not") + " attempting to automatically reconnect");
                this.initialized = false;
                this.failedConnectTransportURI = this.connectedTransportURI;
                this.connectedTransportURI = null;
                this.connected = false;
                if (this.transportListener != null) {
                    this.transportListener.transportInterupted();
                }
                if (reconnectOk) {
                    this.updated.remove(this.failedConnectTransportURI);
                    this.reconnectTask.wakeup();
                } else if (!isDisposed()) {
                    propagateFailureToExceptionListener(e);
                }
            }
        }
    }

    private boolean canReconnect() {
        return this.started && calculateReconnectAttemptLimit() != 0;
    }

    public final void handleConnectionControl(ConnectionControl control) {
        String reconnectStr = control.getReconnectTo();
        if (reconnectStr != null) {
            reconnectStr = reconnectStr.trim();
            if (reconnectStr.length() > 0) {
                try {
                    URI uri = new URI(reconnectStr);
                    if (isReconnectSupported()) {
                        reconnect(uri);
                        LOG.info("Reconnected to: " + uri);
                    }
                } catch (Exception e) {
                    LOG.error("Failed to handle ConnectionControl reconnect to " + reconnectStr, e);
                }
            }
        }
        processNewTransports(control.isRebalanceConnection(), control.getConnectedBrokers());
    }

    private final void processNewTransports(boolean rebalance, String newTransports) {
        if (newTransports != null) {
            newTransports = newTransports.trim();
            if (newTransports.length() > 0 && isUpdateURIsSupported()) {
                List<URI> list = new ArrayList();
                StringTokenizer tokenizer = new StringTokenizer(newTransports, Stomp.COMMA);
                while (tokenizer.hasMoreTokens()) {
                    String str = tokenizer.nextToken();
                    try {
                        list.add(new URI(str));
                    } catch (Exception e) {
                        LOG.error("Failed to parse broker address: " + str, e);
                    }
                }
                if (!list.isEmpty()) {
                    try {
                        updateURIs(rebalance, (URI[]) list.toArray(new URI[list.size()]));
                    } catch (IOException e2) {
                        LOG.error("Failed to update transport URI's from: " + newTransports, e2);
                    }
                }
            }
        }
    }

    public void start() throws Exception {
        synchronized (this.reconnectMutex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Started " + this);
            }
            if (this.started) {
                return;
            }
            this.started = true;
            this.stateTracker.setMaxCacheSize(getMaxCacheSize());
            this.stateTracker.setTrackMessages(isTrackMessages());
            this.stateTracker.setTrackTransactionProducers(isTrackTransactionProducers());
            if (this.connectedTransport.get() != null) {
                this.stateTracker.restore((Transport) this.connectedTransport.get());
            } else {
                reconnect(false);
            }
        }
    }

    public void stop() throws Exception {
        Transport transportToStop = null;
        List<Transport> backupsToStop = new ArrayList(this.backups.size());
        try {
            synchronized (this.reconnectMutex) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Stopped " + this);
                }
                if (this.started) {
                    Transport transport;
                    this.started = false;
                    this.disposed = true;
                    this.connected = false;
                    if (this.connectedTransport.get() != null) {
                        transportToStop = (Transport) this.connectedTransport.getAndSet(null);
                    }
                    this.reconnectMutex.notifyAll();
                    synchronized (this.sleepMutex) {
                        this.sleepMutex.notifyAll();
                    }
                    this.reconnectTask.shutdown();
                    this.reconnectTaskFactory.shutdownNow();
                    synchronized (this.backupMutex) {
                        for (BackupTransport backup : this.backups) {
                            backup.setDisposed(true);
                            transport = backup.getTransport();
                            if (transport != null) {
                                transport.setTransportListener(this.disposedListener);
                                backupsToStop.add(transport);
                            }
                        }
                        this.backups.clear();
                    }
                    for (Transport transport2 : backupsToStop) {
                        try {
                            if (LOG.isTraceEnabled()) {
                                LOG.trace("Stopped backup: " + transport2);
                            }
                            disposeTransport(transport2);
                        } catch (Exception e) {
                        }
                    }
                    if (transportToStop != null) {
                        transportToStop.stop();
                        return;
                    }
                    return;
                }
                this.reconnectTask.shutdown();
                this.reconnectTaskFactory.shutdownNow();
            }
        } catch (Throwable th) {
            this.reconnectTask.shutdown();
            this.reconnectTaskFactory.shutdownNow();
        }
    }

    public long getInitialReconnectDelay() {
        return this.initialReconnectDelay;
    }

    public void setInitialReconnectDelay(long initialReconnectDelay) {
        this.initialReconnectDelay = initialReconnectDelay;
    }

    public long getMaxReconnectDelay() {
        return this.maxReconnectDelay;
    }

    public void setMaxReconnectDelay(long maxReconnectDelay) {
        this.maxReconnectDelay = maxReconnectDelay;
    }

    public long getReconnectDelay() {
        return this.reconnectDelay;
    }

    public void setReconnectDelay(long reconnectDelay) {
        this.reconnectDelay = reconnectDelay;
    }

    public double getReconnectDelayExponent() {
        return this.backOffMultiplier;
    }

    public void setReconnectDelayExponent(double reconnectDelayExponent) {
        this.backOffMultiplier = reconnectDelayExponent;
    }

    public Transport getConnectedTransport() {
        return (Transport) this.connectedTransport.get();
    }

    public URI getConnectedTransportURI() {
        return this.connectedTransportURI;
    }

    public int getMaxReconnectAttempts() {
        return this.maxReconnectAttempts;
    }

    public void setMaxReconnectAttempts(int maxReconnectAttempts) {
        this.maxReconnectAttempts = maxReconnectAttempts;
    }

    public int getStartupMaxReconnectAttempts() {
        return this.startupMaxReconnectAttempts;
    }

    public void setStartupMaxReconnectAttempts(int startupMaxReconnectAttempts) {
        this.startupMaxReconnectAttempts = startupMaxReconnectAttempts;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isRandomize() {
        return this.randomize;
    }

    public void setRandomize(boolean randomize) {
        this.randomize = randomize;
    }

    public boolean isBackup() {
        return this.backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    public int getBackupPoolSize() {
        return this.backupPoolSize;
    }

    public void setBackupPoolSize(int backupPoolSize) {
        this.backupPoolSize = backupPoolSize;
    }

    public int getCurrentBackups() {
        return this.backups.size();
    }

    public boolean isTrackMessages() {
        return this.trackMessages;
    }

    public void setTrackMessages(boolean trackMessages) {
        this.trackMessages = trackMessages;
    }

    public boolean isTrackTransactionProducers() {
        return this.trackTransactionProducers;
    }

    public void setTrackTransactionProducers(boolean trackTransactionProducers) {
        this.trackTransactionProducers = trackTransactionProducers;
    }

    public int getMaxCacheSize() {
        return this.maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public boolean isPriorityBackup() {
        return this.priorityBackup;
    }

    public void setPriorityBackup(boolean priorityBackup) {
        this.priorityBackup = priorityBackup;
    }

    public void setPriorityURIs(String priorityURIs) {
        StringTokenizer tokenizer = new StringTokenizer(priorityURIs, Stomp.COMMA);
        while (tokenizer.hasMoreTokens()) {
            String str = tokenizer.nextToken();
            try {
                this.priorityList.add(new URI(str));
            } catch (Exception e) {
                LOG.error("Failed to parse broker address: " + str, e);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void oneway(java.lang.Object r27) throws java.io.IOException {
        /*
        r26 = this;
        r4 = r27;
        r4 = (org.apache.activemq.command.Command) r4;
        r7 = 0;
        r0 = r26;
        r0 = r0.reconnectMutex;	 Catch:{ InterruptedException -> 0x005d }
        r20 = r0;
        monitor-enter(r20);	 Catch:{ InterruptedException -> 0x005d }
        if (r4 == 0) goto L_0x00a4;
    L_0x000e:
        r0 = r26;
        r0 = r0.connectedTransport;	 Catch:{ all -> 0x005a }
        r19 = r0;
        r19 = r19.get();	 Catch:{ all -> 0x005a }
        if (r19 != 0) goto L_0x00a4;
    L_0x001a:
        r19 = r4.isShutdownInfo();	 Catch:{ all -> 0x005a }
        if (r19 == 0) goto L_0x0022;
    L_0x0020:
        monitor-exit(r20);	 Catch:{ all -> 0x005a }
    L_0x0021:
        return;
    L_0x0022:
        r0 = r4 instanceof org.apache.activemq.command.RemoveInfo;	 Catch:{ all -> 0x005a }
        r19 = r0;
        if (r19 != 0) goto L_0x002e;
    L_0x0028:
        r19 = r4.isMessageAck();	 Catch:{ all -> 0x005a }
        if (r19 == 0) goto L_0x006b;
    L_0x002e:
        r0 = r26;
        r0 = r0.stateTracker;	 Catch:{ all -> 0x005a }
        r19 = r0;
        r0 = r19;
        r0.track(r4);	 Catch:{ all -> 0x005a }
        r19 = r4.isResponseRequired();	 Catch:{ all -> 0x005a }
        if (r19 == 0) goto L_0x0058;
    L_0x003f:
        r13 = new org.apache.activemq.command.Response;	 Catch:{ all -> 0x005a }
        r13.<init>();	 Catch:{ all -> 0x005a }
        r19 = r4.getCommandId();	 Catch:{ all -> 0x005a }
        r0 = r19;
        r13.setCorrelationId(r0);	 Catch:{ all -> 0x005a }
        r0 = r26;
        r0 = r0.myTransportListener;	 Catch:{ all -> 0x005a }
        r19 = r0;
        r0 = r19;
        r0.onCommand(r13);	 Catch:{ all -> 0x005a }
    L_0x0058:
        monitor-exit(r20);	 Catch:{ all -> 0x005a }
        goto L_0x0021;
    L_0x005a:
        r19 = move-exception;
        monitor-exit(r20);	 Catch:{ all -> 0x005a }
        throw r19;	 Catch:{ InterruptedException -> 0x005d }
    L_0x005d:
        r6 = move-exception;
        r19 = java.lang.Thread.currentThread();
        r19.interrupt();
        r19 = new java.io.InterruptedIOException;
        r19.<init>();
        throw r19;
    L_0x006b:
        r0 = r4 instanceof org.apache.activemq.command.MessagePull;	 Catch:{ all -> 0x005a }
        r19 = r0;
        if (r19 == 0) goto L_0x00a4;
    L_0x0071:
        r0 = r4;
        r0 = (org.apache.activemq.command.MessagePull) r0;	 Catch:{ all -> 0x005a }
        r12 = r0;
        r22 = r12.getTimeout();	 Catch:{ all -> 0x005a }
        r24 = 0;
        r19 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
        if (r19 == 0) goto L_0x00a1;
    L_0x007f:
        r5 = new org.apache.activemq.command.MessageDispatch;	 Catch:{ all -> 0x005a }
        r5.<init>();	 Catch:{ all -> 0x005a }
        r19 = r12.getConsumerId();	 Catch:{ all -> 0x005a }
        r0 = r19;
        r5.setConsumerId(r0);	 Catch:{ all -> 0x005a }
        r19 = r12.getDestination();	 Catch:{ all -> 0x005a }
        r0 = r19;
        r5.setDestination(r0);	 Catch:{ all -> 0x005a }
        r0 = r26;
        r0 = r0.myTransportListener;	 Catch:{ all -> 0x005a }
        r19 = r0;
        r0 = r19;
        r0.onCommand(r5);	 Catch:{ all -> 0x005a }
    L_0x00a1:
        monitor-exit(r20);	 Catch:{ all -> 0x005a }
        goto L_0x0021;
    L_0x00a4:
        r11 = 0;
    L_0x00a5:
        r0 = r26;
        r0 = r0.disposed;	 Catch:{ all -> 0x005a }
        r19 = r0;
        if (r19 != 0) goto L_0x015f;
    L_0x00ad:
        r0 = r26;
        r0 = r0.connectedTransport;	 Catch:{ IOException -> 0x01bf }
        r19 = r0;
        r18 = r19.get();	 Catch:{ IOException -> 0x01bf }
        r18 = (org.apache.activemq.transport.Transport) r18;	 Catch:{ IOException -> 0x01bf }
        r14 = java.lang.System.currentTimeMillis();	 Catch:{ IOException -> 0x01bf }
        r16 = 0;
    L_0x00bf:
        if (r18 != 0) goto L_0x014b;
    L_0x00c1:
        r0 = r26;
        r0 = r0.disposed;	 Catch:{ IOException -> 0x01bf }
        r19 = r0;
        if (r19 != 0) goto L_0x014b;
    L_0x00c9:
        r0 = r26;
        r0 = r0.connectionFailure;	 Catch:{ IOException -> 0x01bf }
        r19 = r0;
        if (r19 != 0) goto L_0x014b;
    L_0x00d1:
        r19 = java.lang.Thread.currentThread();	 Catch:{ IOException -> 0x01bf }
        r19 = r19.isInterrupted();	 Catch:{ IOException -> 0x01bf }
        if (r19 != 0) goto L_0x014b;
    L_0x00db:
        r19 = LOG;	 Catch:{ IOException -> 0x01bf }
        r19 = r19.isTraceEnabled();	 Catch:{ IOException -> 0x01bf }
        if (r19 == 0) goto L_0x0101;
    L_0x00e3:
        r19 = LOG;	 Catch:{ IOException -> 0x01bf }
        r21 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01bf }
        r21.<init>();	 Catch:{ IOException -> 0x01bf }
        r22 = "Waiting for transport to reconnect..: ";
        r21 = r21.append(r22);	 Catch:{ IOException -> 0x01bf }
        r0 = r21;
        r21 = r0.append(r4);	 Catch:{ IOException -> 0x01bf }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01bf }
        r0 = r19;
        r1 = r21;
        r0.trace(r1);	 Catch:{ IOException -> 0x01bf }
    L_0x0101:
        r8 = java.lang.System.currentTimeMillis();	 Catch:{ IOException -> 0x01bf }
        r0 = r26;
        r0 = r0.timeout;	 Catch:{ IOException -> 0x01bf }
        r22 = r0;
        r24 = 0;
        r19 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
        if (r19 <= 0) goto L_0x0173;
    L_0x0111:
        r22 = r8 - r14;
        r0 = r26;
        r0 = r0.timeout;	 Catch:{ IOException -> 0x01bf }
        r24 = r0;
        r19 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
        if (r19 <= 0) goto L_0x0173;
    L_0x011d:
        r16 = 1;
        r19 = LOG;	 Catch:{ IOException -> 0x01bf }
        r19 = r19.isInfoEnabled();	 Catch:{ IOException -> 0x01bf }
        if (r19 == 0) goto L_0x014b;
    L_0x0127:
        r19 = LOG;	 Catch:{ IOException -> 0x01bf }
        r21 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01bf }
        r21.<init>();	 Catch:{ IOException -> 0x01bf }
        r22 = "Failover timed out after ";
        r21 = r21.append(r22);	 Catch:{ IOException -> 0x01bf }
        r22 = r8 - r14;
        r21 = r21.append(r22);	 Catch:{ IOException -> 0x01bf }
        r22 = "ms";
        r21 = r21.append(r22);	 Catch:{ IOException -> 0x01bf }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01bf }
        r0 = r19;
        r1 = r21;
        r0.info(r1);	 Catch:{ IOException -> 0x01bf }
    L_0x014b:
        if (r18 != 0) goto L_0x0252;
    L_0x014d:
        r0 = r26;
        r0 = r0.disposed;	 Catch:{ IOException -> 0x01bf }
        r19 = r0;
        if (r19 == 0) goto L_0x01fb;
    L_0x0155:
        r10 = new java.io.IOException;	 Catch:{ IOException -> 0x01bf }
        r19 = "Transport disposed.";
        r0 = r19;
        r10.<init>(r0);	 Catch:{ IOException -> 0x01bf }
        r7 = r10;
    L_0x015f:
        monitor-exit(r20);	 Catch:{ all -> 0x005a }
        r0 = r26;
        r0 = r0.disposed;
        r19 = r0;
        if (r19 != 0) goto L_0x0021;
    L_0x0168:
        if (r7 == 0) goto L_0x0021;
    L_0x016a:
        r0 = r7 instanceof java.io.IOException;
        r19 = r0;
        if (r19 == 0) goto L_0x0310;
    L_0x0170:
        r7 = (java.io.IOException) r7;
        throw r7;
    L_0x0173:
        r0 = r26;
        r0 = r0.reconnectMutex;	 Catch:{ InterruptedException -> 0x0190 }
        r19 = r0;
        r22 = 100;
        r0 = r19;
        r1 = r22;
        r0.wait(r1);	 Catch:{ InterruptedException -> 0x0190 }
    L_0x0182:
        r0 = r26;
        r0 = r0.connectedTransport;	 Catch:{ IOException -> 0x01bf }
        r19 = r0;
        r18 = r19.get();	 Catch:{ IOException -> 0x01bf }
        r18 = (org.apache.activemq.transport.Transport) r18;	 Catch:{ IOException -> 0x01bf }
        goto L_0x00bf;
    L_0x0190:
        r6 = move-exception;
        r19 = java.lang.Thread.currentThread();	 Catch:{ IOException -> 0x01bf }
        r19.interrupt();	 Catch:{ IOException -> 0x01bf }
        r19 = LOG;	 Catch:{ IOException -> 0x01bf }
        r19 = r19.isDebugEnabled();	 Catch:{ IOException -> 0x01bf }
        if (r19 == 0) goto L_0x0182;
    L_0x01a0:
        r19 = LOG;	 Catch:{ IOException -> 0x01bf }
        r21 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01bf }
        r21.<init>();	 Catch:{ IOException -> 0x01bf }
        r22 = "Interupted: ";
        r21 = r21.append(r22);	 Catch:{ IOException -> 0x01bf }
        r0 = r21;
        r21 = r0.append(r6);	 Catch:{ IOException -> 0x01bf }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01bf }
        r0 = r19;
        r1 = r21;
        r0.debug(r1, r6);	 Catch:{ IOException -> 0x01bf }
        goto L_0x0182;
    L_0x01bf:
        r6 = move-exception;
        r19 = LOG;	 Catch:{ all -> 0x005a }
        r19 = r19.isDebugEnabled();	 Catch:{ all -> 0x005a }
        if (r19 == 0) goto L_0x01f2;
    L_0x01c8:
        r19 = LOG;	 Catch:{ all -> 0x005a }
        r21 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005a }
        r21.<init>();	 Catch:{ all -> 0x005a }
        r22 = "Send oneway attempt: ";
        r21 = r21.append(r22);	 Catch:{ all -> 0x005a }
        r0 = r21;
        r21 = r0.append(r11);	 Catch:{ all -> 0x005a }
        r22 = " failed for command:";
        r21 = r21.append(r22);	 Catch:{ all -> 0x005a }
        r0 = r21;
        r21 = r0.append(r4);	 Catch:{ all -> 0x005a }
        r21 = r21.toString();	 Catch:{ all -> 0x005a }
        r0 = r19;
        r1 = r21;
        r0.debug(r1);	 Catch:{ all -> 0x005a }
    L_0x01f2:
        r0 = r26;
        r0.handleTransportFailure(r6);	 Catch:{ all -> 0x005a }
        r11 = r11 + 1;
        goto L_0x00a5;
    L_0x01fb:
        r0 = r26;
        r0 = r0.connectionFailure;	 Catch:{ IOException -> 0x01bf }
        r19 = r0;
        if (r19 == 0) goto L_0x0209;
    L_0x0203:
        r0 = r26;
        r7 = r0.connectionFailure;	 Catch:{ IOException -> 0x01bf }
        goto L_0x015f;
    L_0x0209:
        r19 = 1;
        r0 = r16;
        r1 = r19;
        if (r0 != r1) goto L_0x0246;
    L_0x0211:
        r10 = new java.io.IOException;	 Catch:{ IOException -> 0x01bf }
        r19 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01bf }
        r19.<init>();	 Catch:{ IOException -> 0x01bf }
        r21 = "Failover timeout of ";
        r0 = r19;
        r1 = r21;
        r19 = r0.append(r1);	 Catch:{ IOException -> 0x01bf }
        r0 = r26;
        r0 = r0.timeout;	 Catch:{ IOException -> 0x01bf }
        r22 = r0;
        r0 = r19;
        r1 = r22;
        r19 = r0.append(r1);	 Catch:{ IOException -> 0x01bf }
        r21 = " ms reached.";
        r0 = r19;
        r1 = r21;
        r19 = r0.append(r1);	 Catch:{ IOException -> 0x01bf }
        r19 = r19.toString();	 Catch:{ IOException -> 0x01bf }
        r0 = r19;
        r10.<init>(r0);	 Catch:{ IOException -> 0x01bf }
        r7 = r10;
        goto L_0x015f;
    L_0x0246:
        r10 = new java.io.IOException;	 Catch:{ IOException -> 0x01bf }
        r19 = "Unexpected failure.";
        r0 = r19;
        r10.<init>(r0);	 Catch:{ IOException -> 0x01bf }
        r7 = r10;
        goto L_0x015f;
    L_0x0252:
        r0 = r26;
        r0 = r0.stateTracker;	 Catch:{ IOException -> 0x01bf }
        r19 = r0;
        r0 = r19;
        r17 = r0.track(r4);	 Catch:{ IOException -> 0x01bf }
        r0 = r26;
        r0 = r0.requestMap;	 Catch:{ IOException -> 0x01bf }
        r21 = r0;
        monitor-enter(r21);	 Catch:{ IOException -> 0x01bf }
        if (r17 == 0) goto L_0x0298;
    L_0x0267:
        r19 = r17.isWaitingForResponse();	 Catch:{ all -> 0x02b6 }
        if (r19 == 0) goto L_0x0298;
    L_0x026d:
        r0 = r26;
        r0 = r0.requestMap;	 Catch:{ all -> 0x02b6 }
        r19 = r0;
        r22 = r4.getCommandId();	 Catch:{ all -> 0x02b6 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ all -> 0x02b6 }
        r0 = r19;
        r1 = r22;
        r2 = r17;
        r0.put(r1, r2);	 Catch:{ all -> 0x02b6 }
    L_0x0284:
        monitor-exit(r21);	 Catch:{ all -> 0x02b6 }
        r0 = r18;
        r0.oneway(r4);	 Catch:{ IOException -> 0x02b9 }
        r0 = r26;
        r0 = r0.stateTracker;	 Catch:{ IOException -> 0x02b9 }
        r19 = r0;
        r0 = r19;
        r0.trackBack(r4);	 Catch:{ IOException -> 0x02b9 }
    L_0x0295:
        monitor-exit(r20);	 Catch:{ all -> 0x005a }
        goto L_0x0021;
    L_0x0298:
        if (r17 != 0) goto L_0x0284;
    L_0x029a:
        r19 = r4.isResponseRequired();	 Catch:{ all -> 0x02b6 }
        if (r19 == 0) goto L_0x0284;
    L_0x02a0:
        r0 = r26;
        r0 = r0.requestMap;	 Catch:{ all -> 0x02b6 }
        r19 = r0;
        r22 = r4.getCommandId();	 Catch:{ all -> 0x02b6 }
        r22 = java.lang.Integer.valueOf(r22);	 Catch:{ all -> 0x02b6 }
        r0 = r19;
        r1 = r22;
        r0.put(r1, r4);	 Catch:{ all -> 0x02b6 }
        goto L_0x0284;
    L_0x02b6:
        r19 = move-exception;
        monitor-exit(r21);	 Catch:{ all -> 0x02b6 }
        throw r19;	 Catch:{ IOException -> 0x01bf }
    L_0x02b9:
        r6 = move-exception;
        if (r17 != 0) goto L_0x02d8;
    L_0x02bc:
        r19 = r4.isResponseRequired();	 Catch:{ IOException -> 0x01bf }
        if (r19 == 0) goto L_0x02d7;
    L_0x02c2:
        r0 = r26;
        r0 = r0.requestMap;	 Catch:{ IOException -> 0x01bf }
        r19 = r0;
        r21 = r4.getCommandId();	 Catch:{ IOException -> 0x01bf }
        r21 = java.lang.Integer.valueOf(r21);	 Catch:{ IOException -> 0x01bf }
        r0 = r19;
        r1 = r21;
        r0.remove(r1);	 Catch:{ IOException -> 0x01bf }
    L_0x02d7:
        throw r6;	 Catch:{ IOException -> 0x01bf }
    L_0x02d8:
        r19 = LOG;	 Catch:{ IOException -> 0x01bf }
        r19 = r19.isDebugEnabled();	 Catch:{ IOException -> 0x01bf }
        if (r19 == 0) goto L_0x030a;
    L_0x02e0:
        r19 = LOG;	 Catch:{ IOException -> 0x01bf }
        r21 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01bf }
        r21.<init>();	 Catch:{ IOException -> 0x01bf }
        r22 = "Send oneway attempt: ";
        r21 = r21.append(r22);	 Catch:{ IOException -> 0x01bf }
        r0 = r21;
        r21 = r0.append(r11);	 Catch:{ IOException -> 0x01bf }
        r22 = " failed for command:";
        r21 = r21.append(r22);	 Catch:{ IOException -> 0x01bf }
        r0 = r21;
        r21 = r0.append(r4);	 Catch:{ IOException -> 0x01bf }
        r21 = r21.toString();	 Catch:{ IOException -> 0x01bf }
        r0 = r19;
        r1 = r21;
        r0.debug(r1);	 Catch:{ IOException -> 0x01bf }
    L_0x030a:
        r0 = r26;
        r0.handleTransportFailure(r6);	 Catch:{ IOException -> 0x01bf }
        goto L_0x0295;
    L_0x0310:
        r19 = org.apache.activemq.util.IOExceptionSupport.create(r7);
        throw r19;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.failover.FailoverTransport.oneway(java.lang.Object):void");
    }

    public FutureResponse asyncRequest(Object command, ResponseCallback responseCallback) throws IOException {
        throw new AssertionError("Unsupported Method");
    }

    public Object request(Object command) throws IOException {
        throw new AssertionError("Unsupported Method");
    }

    public Object request(Object command, int timeout) throws IOException {
        throw new AssertionError("Unsupported Method");
    }

    public void add(boolean rebalance, URI[] u) {
        boolean newURI = false;
        for (URI uri : u) {
            if (!contains(uri)) {
                this.uris.add(uri);
                newURI = true;
            }
        }
        if (newURI) {
            reconnect(rebalance);
        }
    }

    public void remove(boolean rebalance, URI[] u) {
        for (URI uri : u) {
            this.uris.remove(uri);
        }
    }

    public void add(boolean rebalance, String u) {
        try {
            URI newURI = new URI(u);
            if (!contains(newURI)) {
                this.uris.add(newURI);
                reconnect(rebalance);
            }
        } catch (Exception e) {
            LOG.error("Failed to parse URI: " + u);
        }
    }

    public void reconnect(boolean rebalance) {
        synchronized (this.reconnectMutex) {
            if (this.started) {
                if (rebalance) {
                    this.doRebalance = true;
                }
                LOG.debug("Waking up reconnect task");
                try {
                    this.reconnectTask.wakeup();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                LOG.debug("Reconnect was triggered but transport is not started yet. Wait for start to connect the transport.");
            }
        }
    }

    private List<URI> getConnectList() {
        if (this.updated.isEmpty()) {
            List<URI> l = new ArrayList(this.uris);
            boolean removed = false;
            if (this.failedConnectTransportURI != null) {
                removed = l.remove(this.failedConnectTransportURI);
            }
            if (this.randomize) {
                for (int i = 0; i < l.size(); i++) {
                    int p = (int) ((Math.random() * 100.0d) % ((double) l.size()));
                    URI t = (URI) l.get(p);
                    l.set(p, l.get(i));
                    l.set(i, t);
                }
            }
            if (removed) {
                l.add(this.failedConnectTransportURI);
            }
            if (!LOG.isDebugEnabled()) {
                return l;
            }
            LOG.debug("urlList connectionList:" + l + ", from: " + this.uris);
            return l;
        }
        if (this.failedConnectTransportURI != null && this.updated.remove(this.failedConnectTransportURI)) {
            this.updated.add(this.failedConnectTransportURI);
        }
        return this.updated;
    }

    public TransportListener getTransportListener() {
        return this.transportListener;
    }

    public void setTransportListener(TransportListener commandListener) {
        synchronized (this.listenerMutex) {
            this.transportListener = commandListener;
            this.listenerMutex.notifyAll();
        }
    }

    public <T> T narrow(Class<T> target) {
        if (target.isAssignableFrom(getClass())) {
            return target.cast(this);
        }
        Transport transport = (Transport) this.connectedTransport.get();
        if (transport != null) {
            return transport.narrow(target);
        }
        return null;
    }

    protected void restoreTransport(Transport t) throws Exception, IOException {
        Throwable th;
        t.start();
        ConnectionControl cc = new ConnectionControl();
        cc.setFaultTolerant(true);
        t.oneway(cc);
        this.stateTracker.restore(t);
        synchronized (this.requestMap) {
            try {
                Map<Integer, Command> tmpMap = new LinkedHashMap(this.requestMap);
                try {
                    for (Command command : tmpMap.values()) {
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("restore requestMap, replay: " + command);
                        }
                        t.oneway(command);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    Map<Integer, Command> map = tmpMap;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    public boolean isUseExponentialBackOff() {
        return this.useExponentialBackOff;
    }

    public void setUseExponentialBackOff(boolean useExponentialBackOff) {
        this.useExponentialBackOff = useExponentialBackOff;
    }

    public String toString() {
        return this.connectedTransportURI == null ? "unconnected" : this.connectedTransportURI.toString();
    }

    public String getRemoteAddress() {
        Transport transport = (Transport) this.connectedTransport.get();
        if (transport != null) {
            return transport.getRemoteAddress();
        }
        return null;
    }

    public boolean isFaultTolerant() {
        return true;
    }

    private void doUpdateURIsFromDisk() {
        IOException ioe;
        Throwable th;
        String fileURL = getUpdateURIsURL();
        if (fileURL != null) {
            BufferedReader bufferedReader = null;
            String newUris = null;
            StringBuffer buffer = new StringBuffer();
            try {
                BufferedReader in = new BufferedReader(getURLStream(fileURL));
                while (true) {
                    try {
                        String line = in.readLine();
                        if (line == null) {
                            break;
                        }
                        buffer.append(line);
                    } catch (IOException e) {
                        ioe = e;
                        bufferedReader = in;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedReader = in;
                    }
                }
                newUris = buffer.toString();
                if (in != null) {
                    try {
                        in.close();
                        bufferedReader = in;
                    } catch (IOException e2) {
                        bufferedReader = in;
                    }
                }
            } catch (IOException e3) {
                ioe = e3;
                try {
                    LOG.error("Failed to read updateURIsURL: " + fileURL, ioe);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e4) {
                        }
                    }
                    processNewTransports(isRebalanceUpdateURIs(), newUris);
                } catch (Throwable th3) {
                    th = th3;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw th;
                }
            }
            processNewTransports(isRebalanceUpdateURIs(), newUris);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final boolean doReconnect() {
        /*
        r22 = this;
        r8 = 0;
        r0 = r22;
        r0 = r0.reconnectMutex;
        r17 = r0;
        monitor-enter(r17);
        r22.doUpdateURIsFromDisk();	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0 = r0.disposed;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        if (r16 != 0) goto L_0x001b;
    L_0x0013:
        r0 = r22;
        r0 = r0.connectionFailure;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        if (r16 == 0) goto L_0x0024;
    L_0x001b:
        r0 = r22;
        r0 = r0.reconnectMutex;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        r16.notifyAll();	 Catch:{ all -> 0x00f7 }
    L_0x0024:
        r0 = r22;
        r0 = r0.connectedTransport;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        r16 = r16.get();	 Catch:{ all -> 0x00f7 }
        if (r16 == 0) goto L_0x0040;
    L_0x0030:
        r0 = r22;
        r0 = r0.doRebalance;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        if (r16 != 0) goto L_0x0040;
    L_0x0038:
        r0 = r22;
        r0 = r0.priorityBackupAvailable;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        if (r16 == 0) goto L_0x0050;
    L_0x0040:
        r0 = r22;
        r0 = r0.disposed;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        if (r16 != 0) goto L_0x0050;
    L_0x0048:
        r0 = r22;
        r0 = r0.connectionFailure;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        if (r16 == 0) goto L_0x0054;
    L_0x0050:
        r16 = 0;
        monitor-exit(r17);	 Catch:{ all -> 0x00f7 }
    L_0x0053:
        return r16;
    L_0x0054:
        r5 = r22.getConnectList();	 Catch:{ all -> 0x00f7 }
        r16 = r5.isEmpty();	 Catch:{ all -> 0x00f7 }
        if (r16 == 0) goto L_0x00fa;
    L_0x005e:
        r9 = new java.io.IOException;	 Catch:{ all -> 0x00f7 }
        r16 = "No uris available to connect to.";
        r0 = r16;
        r9.<init>(r0);	 Catch:{ all -> 0x00f7 }
        r8 = r9;
    L_0x0068:
        r13 = r22.calculateReconnectAttemptLimit();	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0 = r0.connectFailures;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        r16 = r16 + 1;
        r0 = r16;
        r1 = r22;
        r1.connectFailures = r0;	 Catch:{ all -> 0x00f7 }
        r16 = -1;
        r0 = r16;
        if (r13 == r0) goto L_0x0495;
    L_0x0080:
        r0 = r22;
        r0 = r0.connectFailures;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        r0 = r16;
        if (r0 < r13) goto L_0x0495;
    L_0x008a:
        r16 = LOG;	 Catch:{ all -> 0x00f7 }
        r18 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f7 }
        r18.<init>();	 Catch:{ all -> 0x00f7 }
        r19 = "Failed to connect to ";
        r18 = r18.append(r19);	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0 = r0.uris;	 Catch:{ all -> 0x00f7 }
        r19 = r0;
        r18 = r18.append(r19);	 Catch:{ all -> 0x00f7 }
        r19 = " after: ";
        r18 = r18.append(r19);	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0 = r0.connectFailures;	 Catch:{ all -> 0x00f7 }
        r19 = r0;
        r18 = r18.append(r19);	 Catch:{ all -> 0x00f7 }
        r19 = " attempt(s)";
        r18 = r18.append(r19);	 Catch:{ all -> 0x00f7 }
        r18 = r18.toString();	 Catch:{ all -> 0x00f7 }
        r0 = r16;
        r1 = r18;
        r0.error(r1);	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0.connectionFailure = r8;	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0 = r0.listenerMutex;	 Catch:{ all -> 0x00f7 }
        r18 = r0;
        monitor-enter(r18);	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0 = r0.transportListener;	 Catch:{ all -> 0x0492 }
        r16 = r0;
        if (r16 != 0) goto L_0x00e4;
    L_0x00d5:
        r0 = r22;
        r0 = r0.listenerMutex;	 Catch:{ InterruptedException -> 0x04b1 }
        r16 = r0;
        r20 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r0 = r16;
        r1 = r20;
        r0.wait(r1);	 Catch:{ InterruptedException -> 0x04b1 }
    L_0x00e4:
        monitor-exit(r18);	 Catch:{ all -> 0x0492 }
        r0 = r22;
        r0 = r0.connectionFailure;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.propagateFailureToExceptionListener(r1);	 Catch:{ all -> 0x00f7 }
        r16 = 0;
        monitor-exit(r17);	 Catch:{ all -> 0x00f7 }
        goto L_0x0053;
    L_0x00f7:
        r16 = move-exception;
        monitor-exit(r17);	 Catch:{ all -> 0x00f7 }
        throw r16;
    L_0x00fa:
        r0 = r22;
        r0 = r0.doRebalance;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        if (r16 == 0) goto L_0x0180;
    L_0x0102:
        r16 = 0;
        r0 = r16;
        r16 = r5.get(r0);	 Catch:{ all -> 0x00f7 }
        r16 = (java.net.URI) r16;	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0 = r0.connectedTransportURI;	 Catch:{ all -> 0x00f7 }
        r18 = r0;
        r0 = r16;
        r1 = r18;
        r16 = r0.equals(r1);	 Catch:{ all -> 0x00f7 }
        if (r16 == 0) goto L_0x0129;
    L_0x011c:
        r16 = 0;
        r0 = r16;
        r1 = r22;
        r1.doRebalance = r0;	 Catch:{ all -> 0x00f7 }
        r16 = 0;
        monitor-exit(r17);	 Catch:{ all -> 0x00f7 }
        goto L_0x0053;
    L_0x0129:
        r16 = LOG;	 Catch:{ all -> 0x00f7 }
        r16 = r16.isDebugEnabled();	 Catch:{ all -> 0x00f7 }
        if (r16 == 0) goto L_0x015f;
    L_0x0131:
        r16 = LOG;	 Catch:{ all -> 0x00f7 }
        r18 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f7 }
        r18.<init>();	 Catch:{ all -> 0x00f7 }
        r19 = "Doing rebalance from: ";
        r18 = r18.append(r19);	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0 = r0.connectedTransportURI;	 Catch:{ all -> 0x00f7 }
        r19 = r0;
        r18 = r18.append(r19);	 Catch:{ all -> 0x00f7 }
        r19 = " to ";
        r18 = r18.append(r19);	 Catch:{ all -> 0x00f7 }
        r0 = r18;
        r18 = r0.append(r5);	 Catch:{ all -> 0x00f7 }
        r18 = r18.toString();	 Catch:{ all -> 0x00f7 }
        r0 = r16;
        r1 = r18;
        r0.debug(r1);	 Catch:{ all -> 0x00f7 }
    L_0x015f:
        r0 = r22;
        r0 = r0.connectedTransport;	 Catch:{ Exception -> 0x03b8 }
        r16 = r0;
        r18 = 0;
        r0 = r16;
        r1 = r18;
        r14 = r0.getAndSet(r1);	 Catch:{ Exception -> 0x03b8 }
        r14 = (org.apache.activemq.transport.Transport) r14;	 Catch:{ Exception -> 0x03b8 }
        if (r14 == 0) goto L_0x0178;
    L_0x0173:
        r0 = r22;
        r0.disposeTransport(r14);	 Catch:{ Exception -> 0x03b8 }
    L_0x0178:
        r16 = 0;
        r0 = r16;
        r1 = r22;
        r1.doRebalance = r0;	 Catch:{ all -> 0x00f7 }
    L_0x0180:
        r22.resetReconnectDelay();	 Catch:{ all -> 0x00f7 }
        r14 = 0;
        r15 = 0;
        r0 = r22;
        r0 = r0.backupMutex;	 Catch:{ all -> 0x00f7 }
        r18 = r0;
        monitor-enter(r18);	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0 = r0.priorityBackup;	 Catch:{ all -> 0x03ce }
        r16 = r0;
        if (r16 != 0) goto L_0x019c;
    L_0x0194:
        r0 = r22;
        r0 = r0.backup;	 Catch:{ all -> 0x03ce }
        r16 = r0;
        if (r16 == 0) goto L_0x020e;
    L_0x019c:
        r0 = r22;
        r0 = r0.backups;	 Catch:{ all -> 0x03ce }
        r16 = r0;
        r16 = r16.isEmpty();	 Catch:{ all -> 0x03ce }
        if (r16 != 0) goto L_0x020e;
    L_0x01a8:
        r11 = new java.util.ArrayList;	 Catch:{ all -> 0x03ce }
        r0 = r22;
        r0 = r0.backups;	 Catch:{ all -> 0x03ce }
        r16 = r0;
        r0 = r16;
        r11.<init>(r0);	 Catch:{ all -> 0x03ce }
        r0 = r22;
        r0 = r0.randomize;	 Catch:{ all -> 0x03ce }
        r16 = r0;
        if (r16 == 0) goto L_0x01c0;
    L_0x01bd:
        java.util.Collections.shuffle(r11);	 Catch:{ all -> 0x03ce }
    L_0x01c0:
        r16 = 0;
        r0 = r16;
        r4 = r11.remove(r0);	 Catch:{ all -> 0x03ce }
        r4 = (org.apache.activemq.transport.failover.BackupTransport) r4;	 Catch:{ all -> 0x03ce }
        r0 = r22;
        r0 = r0.backups;	 Catch:{ all -> 0x03ce }
        r16 = r0;
        r0 = r16;
        r0.remove(r4);	 Catch:{ all -> 0x03ce }
        r14 = r4.getTransport();	 Catch:{ all -> 0x03ce }
        r15 = r4.getUri();	 Catch:{ all -> 0x03ce }
        r0 = r22;
        r0 = r0.priorityBackup;	 Catch:{ all -> 0x03ce }
        r16 = r0;
        if (r16 == 0) goto L_0x020e;
    L_0x01e5:
        r0 = r22;
        r0 = r0.priorityBackupAvailable;	 Catch:{ all -> 0x03ce }
        r16 = r0;
        if (r16 == 0) goto L_0x020e;
    L_0x01ed:
        r0 = r22;
        r0 = r0.connectedTransport;	 Catch:{ all -> 0x03ce }
        r16 = r0;
        r19 = 0;
        r0 = r16;
        r1 = r19;
        r12 = r0.getAndSet(r1);	 Catch:{ all -> 0x03ce }
        r12 = (org.apache.activemq.transport.Transport) r12;	 Catch:{ all -> 0x03ce }
        if (r14 == 0) goto L_0x0206;
    L_0x0201:
        r0 = r22;
        r0.disposeTransport(r12);	 Catch:{ all -> 0x03ce }
    L_0x0206:
        r16 = 0;
        r0 = r16;
        r1 = r22;
        r1.priorityBackupAvailable = r0;	 Catch:{ all -> 0x03ce }
    L_0x020e:
        monitor-exit(r18);	 Catch:{ all -> 0x03ce }
        if (r14 != 0) goto L_0x0278;
    L_0x0211:
        r0 = r22;
        r0 = r0.firstConnection;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        if (r16 != 0) goto L_0x0278;
    L_0x0219:
        r0 = r22;
        r0 = r0.reconnectDelay;	 Catch:{ all -> 0x00f7 }
        r18 = r0;
        r20 = 0;
        r16 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
        if (r16 <= 0) goto L_0x0278;
    L_0x0225:
        r0 = r22;
        r0 = r0.disposed;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        if (r16 != 0) goto L_0x0278;
    L_0x022d:
        r0 = r22;
        r0 = r0.sleepMutex;	 Catch:{ all -> 0x00f7 }
        r18 = r0;
        monitor-enter(r18);	 Catch:{ all -> 0x00f7 }
        r16 = LOG;	 Catch:{ all -> 0x03db }
        r16 = r16.isDebugEnabled();	 Catch:{ all -> 0x03db }
        if (r16 == 0) goto L_0x0264;
    L_0x023c:
        r16 = LOG;	 Catch:{ all -> 0x03db }
        r19 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03db }
        r19.<init>();	 Catch:{ all -> 0x03db }
        r20 = "Waiting ";
        r19 = r19.append(r20);	 Catch:{ all -> 0x03db }
        r0 = r22;
        r0 = r0.reconnectDelay;	 Catch:{ all -> 0x03db }
        r20 = r0;
        r19 = r19.append(r20);	 Catch:{ all -> 0x03db }
        r20 = " ms before attempting connection. ";
        r19 = r19.append(r20);	 Catch:{ all -> 0x03db }
        r19 = r19.toString();	 Catch:{ all -> 0x03db }
        r0 = r16;
        r1 = r19;
        r0.debug(r1);	 Catch:{ all -> 0x03db }
    L_0x0264:
        r0 = r22;
        r0 = r0.sleepMutex;	 Catch:{ InterruptedException -> 0x03d1 }
        r16 = r0;
        r0 = r22;
        r0 = r0.reconnectDelay;	 Catch:{ InterruptedException -> 0x03d1 }
        r20 = r0;
        r0 = r16;
        r1 = r20;
        r0.wait(r1);	 Catch:{ InterruptedException -> 0x03d1 }
    L_0x0277:
        monitor-exit(r18);	 Catch:{ all -> 0x03db }
    L_0x0278:
        r10 = r5.iterator();	 Catch:{ all -> 0x00f7 }
    L_0x027c:
        if (r14 != 0) goto L_0x0284;
    L_0x027e:
        r16 = r10.hasNext();	 Catch:{ all -> 0x00f7 }
        if (r16 == 0) goto L_0x0068;
    L_0x0284:
        r0 = r22;
        r0 = r0.connectedTransport;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        r16 = r16.get();	 Catch:{ all -> 0x00f7 }
        if (r16 != 0) goto L_0x0068;
    L_0x0290:
        r0 = r22;
        r0 = r0.disposed;	 Catch:{ all -> 0x00f7 }
        r16 = r0;
        if (r16 != 0) goto L_0x0068;
    L_0x0298:
        r0 = r22;
        r0 = r0.brokerSslContext;	 Catch:{ Exception -> 0x03e1 }
        r16 = r0;
        org.apache.activemq.broker.SslContext.setCurrentSslContext(r16);	 Catch:{ Exception -> 0x03e1 }
        if (r14 != 0) goto L_0x02b0;
    L_0x02a3:
        r16 = r10.next();	 Catch:{ Exception -> 0x03e1 }
        r0 = r16;
        r0 = (java.net.URI) r0;	 Catch:{ Exception -> 0x03e1 }
        r15 = r0;
        r14 = org.apache.activemq.transport.TransportFactory.compositeConnect(r15);	 Catch:{ Exception -> 0x03e1 }
    L_0x02b0:
        r16 = LOG;	 Catch:{ Exception -> 0x03e1 }
        r16 = r16.isDebugEnabled();	 Catch:{ Exception -> 0x03e1 }
        if (r16 == 0) goto L_0x02e6;
    L_0x02b8:
        r16 = LOG;	 Catch:{ Exception -> 0x03e1 }
        r18 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x03e1 }
        r18.<init>();	 Catch:{ Exception -> 0x03e1 }
        r19 = "Attempting  ";
        r18 = r18.append(r19);	 Catch:{ Exception -> 0x03e1 }
        r0 = r22;
        r0 = r0.connectFailures;	 Catch:{ Exception -> 0x03e1 }
        r19 = r0;
        r18 = r18.append(r19);	 Catch:{ Exception -> 0x03e1 }
        r19 = "th  connect to: ";
        r18 = r18.append(r19);	 Catch:{ Exception -> 0x03e1 }
        r0 = r18;
        r18 = r0.append(r15);	 Catch:{ Exception -> 0x03e1 }
        r18 = r18.toString();	 Catch:{ Exception -> 0x03e1 }
        r0 = r16;
        r1 = r18;
        r0.debug(r1);	 Catch:{ Exception -> 0x03e1 }
    L_0x02e6:
        r0 = r22;
        r0 = r0.myTransportListener;	 Catch:{ Exception -> 0x03e1 }
        r16 = r0;
        r0 = r16;
        r14.setTransportListener(r0);	 Catch:{ Exception -> 0x03e1 }
        r14.start();	 Catch:{ Exception -> 0x03e1 }
        r0 = r22;
        r0 = r0.started;	 Catch:{ Exception -> 0x03e1 }
        r16 = r0;
        if (r16 == 0) goto L_0x0309;
    L_0x02fc:
        r0 = r22;
        r0 = r0.firstConnection;	 Catch:{ Exception -> 0x03e1 }
        r16 = r0;
        if (r16 != 0) goto L_0x0309;
    L_0x0304:
        r0 = r22;
        r0.restoreTransport(r14);	 Catch:{ Exception -> 0x03e1 }
    L_0x0309:
        r16 = LOG;	 Catch:{ Exception -> 0x03e1 }
        r16 = r16.isDebugEnabled();	 Catch:{ Exception -> 0x03e1 }
        if (r16 == 0) goto L_0x031c;
    L_0x0311:
        r16 = LOG;	 Catch:{ Exception -> 0x03e1 }
        r18 = "Connection established";
        r0 = r16;
        r1 = r18;
        r0.debug(r1);	 Catch:{ Exception -> 0x03e1 }
    L_0x031c:
        r0 = r22;
        r0 = r0.initialReconnectDelay;	 Catch:{ Exception -> 0x03e1 }
        r18 = r0;
        r0 = r18;
        r2 = r22;
        r2.reconnectDelay = r0;	 Catch:{ Exception -> 0x03e1 }
        r0 = r22;
        r0.connectedTransportURI = r15;	 Catch:{ Exception -> 0x03e1 }
        r0 = r22;
        r0 = r0.connectedTransport;	 Catch:{ Exception -> 0x03e1 }
        r16 = r0;
        r0 = r16;
        r0.set(r14);	 Catch:{ Exception -> 0x03e1 }
        r0 = r22;
        r0 = r0.reconnectMutex;	 Catch:{ Exception -> 0x03e1 }
        r16 = r0;
        r16.notifyAll();	 Catch:{ Exception -> 0x03e1 }
        r16 = 0;
        r0 = r16;
        r1 = r22;
        r1.connectFailures = r0;	 Catch:{ Exception -> 0x03e1 }
        r0 = r22;
        r0 = r0.listenerMutex;	 Catch:{ Exception -> 0x03e1 }
        r18 = r0;
        monitor-enter(r18);	 Catch:{ Exception -> 0x03e1 }
        r0 = r22;
        r0 = r0.transportListener;	 Catch:{ all -> 0x03de }
        r16 = r0;
        if (r16 != 0) goto L_0x0366;
    L_0x0357:
        r0 = r22;
        r0 = r0.listenerMutex;	 Catch:{ InterruptedException -> 0x04b4 }
        r16 = r0;
        r20 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r0 = r16;
        r1 = r20;
        r0.wait(r1);	 Catch:{ InterruptedException -> 0x04b4 }
    L_0x0366:
        monitor-exit(r18);	 Catch:{ all -> 0x03de }
        r0 = r22;
        r0 = r0.transportListener;	 Catch:{ Exception -> 0x03e1 }
        r16 = r0;
        if (r16 == 0) goto L_0x0422;
    L_0x036f:
        r0 = r22;
        r0 = r0.transportListener;	 Catch:{ Exception -> 0x03e1 }
        r16 = r0;
        r16.transportResumed();	 Catch:{ Exception -> 0x03e1 }
    L_0x0378:
        r0 = r22;
        r0 = r0.firstConnection;	 Catch:{ Exception -> 0x03e1 }
        r16 = r0;
        if (r16 == 0) goto L_0x043e;
    L_0x0380:
        r16 = 0;
        r0 = r16;
        r1 = r22;
        r1.firstConnection = r0;	 Catch:{ Exception -> 0x03e1 }
        r16 = LOG;	 Catch:{ Exception -> 0x03e1 }
        r18 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x03e1 }
        r18.<init>();	 Catch:{ Exception -> 0x03e1 }
        r19 = "Successfully connected to ";
        r18 = r18.append(r19);	 Catch:{ Exception -> 0x03e1 }
        r0 = r18;
        r18 = r0.append(r15);	 Catch:{ Exception -> 0x03e1 }
        r18 = r18.toString();	 Catch:{ Exception -> 0x03e1 }
        r0 = r16;
        r1 = r18;
        r0.info(r1);	 Catch:{ Exception -> 0x03e1 }
    L_0x03a6:
        r16 = 1;
        r0 = r16;
        r1 = r22;
        r1.connected = r0;	 Catch:{ Exception -> 0x03e1 }
        r16 = 0;
        r18 = 0;
        org.apache.activemq.broker.SslContext.setCurrentSslContext(r18);	 Catch:{ all -> 0x00f7 }
        monitor-exit(r17);	 Catch:{ all -> 0x00f7 }
        goto L_0x0053;
    L_0x03b8:
        r6 = move-exception;
        r16 = LOG;	 Catch:{ all -> 0x00f7 }
        r16 = r16.isDebugEnabled();	 Catch:{ all -> 0x00f7 }
        if (r16 == 0) goto L_0x0178;
    L_0x03c1:
        r16 = LOG;	 Catch:{ all -> 0x00f7 }
        r18 = "Caught an exception stopping existing transport for rebalance";
        r0 = r16;
        r1 = r18;
        r0.debug(r1, r6);	 Catch:{ all -> 0x00f7 }
        goto L_0x0178;
    L_0x03ce:
        r16 = move-exception;
        monitor-exit(r18);	 Catch:{ all -> 0x03ce }
        throw r16;	 Catch:{ all -> 0x00f7 }
    L_0x03d1:
        r6 = move-exception;
        r16 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x03db }
        r16.interrupt();	 Catch:{ all -> 0x03db }
        goto L_0x0277;
    L_0x03db:
        r16 = move-exception;
        monitor-exit(r18);	 Catch:{ all -> 0x03db }
        throw r16;	 Catch:{ all -> 0x00f7 }
    L_0x03de:
        r16 = move-exception;
        monitor-exit(r18);	 Catch:{ all -> 0x03de }
        throw r16;	 Catch:{ Exception -> 0x03e1 }
    L_0x03e1:
        r6 = move-exception;
        r8 = r6;
        r16 = LOG;	 Catch:{ all -> 0x0437 }
        r16 = r16.isDebugEnabled();	 Catch:{ all -> 0x0437 }
        if (r16 == 0) goto L_0x0415;
    L_0x03eb:
        r16 = LOG;	 Catch:{ all -> 0x0437 }
        r18 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0437 }
        r18.<init>();	 Catch:{ all -> 0x0437 }
        r19 = "Connect fail to: ";
        r18 = r18.append(r19);	 Catch:{ all -> 0x0437 }
        r0 = r18;
        r18 = r0.append(r15);	 Catch:{ all -> 0x0437 }
        r19 = ", reason: ";
        r18 = r18.append(r19);	 Catch:{ all -> 0x0437 }
        r0 = r18;
        r18 = r0.append(r6);	 Catch:{ all -> 0x0437 }
        r18 = r18.toString();	 Catch:{ all -> 0x0437 }
        r0 = r16;
        r1 = r18;
        r0.debug(r1);	 Catch:{ all -> 0x0437 }
    L_0x0415:
        if (r14 == 0) goto L_0x041b;
    L_0x0417:
        r14.stop();	 Catch:{ Exception -> 0x045e }
        r14 = 0;
    L_0x041b:
        r16 = 0;
        org.apache.activemq.broker.SslContext.setCurrentSslContext(r16);	 Catch:{ all -> 0x00f7 }
        goto L_0x027c;
    L_0x0422:
        r16 = LOG;	 Catch:{ Exception -> 0x03e1 }
        r16 = r16.isDebugEnabled();	 Catch:{ Exception -> 0x03e1 }
        if (r16 == 0) goto L_0x0378;
    L_0x042a:
        r16 = LOG;	 Catch:{ Exception -> 0x03e1 }
        r18 = "transport resumed by transport listener not set";
        r0 = r16;
        r1 = r18;
        r0.debug(r1);	 Catch:{ Exception -> 0x03e1 }
        goto L_0x0378;
    L_0x0437:
        r16 = move-exception;
        r18 = 0;
        org.apache.activemq.broker.SslContext.setCurrentSslContext(r18);	 Catch:{ all -> 0x00f7 }
        throw r16;	 Catch:{ all -> 0x00f7 }
    L_0x043e:
        r16 = LOG;	 Catch:{ Exception -> 0x03e1 }
        r18 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x03e1 }
        r18.<init>();	 Catch:{ Exception -> 0x03e1 }
        r19 = "Successfully reconnected to ";
        r18 = r18.append(r19);	 Catch:{ Exception -> 0x03e1 }
        r0 = r18;
        r18 = r0.append(r15);	 Catch:{ Exception -> 0x03e1 }
        r18 = r18.toString();	 Catch:{ Exception -> 0x03e1 }
        r0 = r16;
        r1 = r18;
        r0.info(r1);	 Catch:{ Exception -> 0x03e1 }
        goto L_0x03a6;
    L_0x045e:
        r7 = move-exception;
        r16 = LOG;	 Catch:{ all -> 0x0437 }
        r16 = r16.isDebugEnabled();	 Catch:{ all -> 0x0437 }
        if (r16 == 0) goto L_0x041b;
    L_0x0467:
        r16 = LOG;	 Catch:{ all -> 0x0437 }
        r18 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0437 }
        r18.<init>();	 Catch:{ all -> 0x0437 }
        r19 = "Stop of failed transport: ";
        r18 = r18.append(r19);	 Catch:{ all -> 0x0437 }
        r0 = r18;
        r18 = r0.append(r14);	 Catch:{ all -> 0x0437 }
        r19 = " failed with reason: ";
        r18 = r18.append(r19);	 Catch:{ all -> 0x0437 }
        r0 = r18;
        r18 = r0.append(r7);	 Catch:{ all -> 0x0437 }
        r18 = r18.toString();	 Catch:{ all -> 0x0437 }
        r0 = r16;
        r1 = r18;
        r0.debug(r1);	 Catch:{ all -> 0x0437 }
        goto L_0x041b;
    L_0x0492:
        r16 = move-exception;
        monitor-exit(r18);	 Catch:{ all -> 0x0492 }
        throw r16;	 Catch:{ all -> 0x00f7 }
    L_0x0495:
        monitor-exit(r17);	 Catch:{ all -> 0x00f7 }
        r0 = r22;
        r0 = r0.disposed;
        r16 = r0;
        if (r16 != 0) goto L_0x04a1;
    L_0x049e:
        r22.doDelay();
    L_0x04a1:
        r0 = r22;
        r0 = r0.disposed;
        r16 = r0;
        if (r16 != 0) goto L_0x04ad;
    L_0x04a9:
        r16 = 1;
        goto L_0x0053;
    L_0x04ad:
        r16 = 0;
        goto L_0x0053;
    L_0x04b1:
        r16 = move-exception;
        goto L_0x00e4;
    L_0x04b4:
        r16 = move-exception;
        goto L_0x0366;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.failover.FailoverTransport.doReconnect():boolean");
    }

    private void doDelay() {
        if (this.reconnectDelay > 0) {
            synchronized (this.sleepMutex) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Waiting " + this.reconnectDelay + " ms before attempting connection");
                }
                try {
                    this.sleepMutex.wait(this.reconnectDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (this.useExponentialBackOff) {
            this.reconnectDelay = (long) (((double) this.reconnectDelay) * this.backOffMultiplier);
            if (this.reconnectDelay > this.maxReconnectDelay) {
                this.reconnectDelay = this.maxReconnectDelay;
            }
        }
    }

    private void resetReconnectDelay() {
        if (!this.useExponentialBackOff || this.reconnectDelay == 10) {
            this.reconnectDelay = this.initialReconnectDelay;
        }
    }

    private void propagateFailureToExceptionListener(Exception exception) {
        if (this.transportListener != null) {
            if (exception instanceof IOException) {
                this.transportListener.onException((IOException) exception);
            } else {
                this.transportListener.onException(IOExceptionSupport.create(exception));
            }
        }
        this.reconnectMutex.notifyAll();
    }

    private int calculateReconnectAttemptLimit() {
        int maxReconnectValue = this.maxReconnectAttempts;
        if (!this.firstConnection || this.startupMaxReconnectAttempts == INFINITE) {
            return maxReconnectValue;
        }
        return this.startupMaxReconnectAttempts;
    }

    final boolean buildBackups() {
        synchronized (this.backupMutex) {
            if (!this.disposed && ((this.backup || this.priorityBackup) && this.backups.size() < this.backupPoolSize)) {
                URI uri;
                BackupTransport bt;
                ArrayList<URI> backupList = new ArrayList(this.priorityList);
                for (URI uri2 : getConnectList()) {
                    if (!backupList.contains(uri2)) {
                        backupList.add(uri2);
                    }
                }
                List<BackupTransport> disposedList = new ArrayList();
                for (BackupTransport bt2 : this.backups) {
                    if (bt2.isDisposed()) {
                        disposedList.add(bt2);
                    }
                }
                this.backups.removeAll(disposedList);
                disposedList.clear();
                Iterator<URI> iter = backupList.iterator();
                while (!this.disposed && iter.hasNext() && this.backups.size() < this.backupPoolSize) {
                    uri2 = (URI) iter.next();
                    if (!(this.connectedTransportURI == null || this.connectedTransportURI.equals(uri2))) {
                        try {
                            SslContext.setCurrentSslContext(this.brokerSslContext);
                            bt2 = new BackupTransport(this);
                            bt2.setUri(uri2);
                            if (!this.backups.contains(bt2)) {
                                Transport t = TransportFactory.compositeConnect(uri2);
                                t.setTransportListener(bt2);
                                t.start();
                                bt2.setTransport(t);
                                this.backups.add(bt2);
                                if (this.priorityBackup && isPriority(uri2)) {
                                    this.priorityBackupAvailable = true;
                                }
                            }
                            SslContext.setCurrentSslContext(null);
                        } catch (Exception e) {
                            LOG.debug("Failed to build backup ", e);
                            SslContext.setCurrentSslContext(null);
                        } catch (Throwable th) {
                            SslContext.setCurrentSslContext(null);
                        }
                    }
                }
            }
        }
        return false;
    }

    protected boolean isPriority(URI uri) {
        if (this.priorityList.isEmpty()) {
            return this.uris.indexOf(uri) == 0;
        } else {
            return this.priorityList.contains(uri);
        }
    }

    public boolean isDisposed() {
        return this.disposed;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void reconnect(URI uri) throws IOException {
        add(true, new URI[]{uri});
    }

    public boolean isReconnectSupported() {
        return this.reconnectSupported;
    }

    public void setReconnectSupported(boolean value) {
        this.reconnectSupported = value;
    }

    public boolean isUpdateURIsSupported() {
        return this.updateURIsSupported;
    }

    public void setUpdateURIsSupported(boolean value) {
        this.updateURIsSupported = value;
    }

    public void updateURIs(boolean rebalance, URI[] updatedURIs) throws IOException {
        if (isUpdateURIsSupported()) {
            HashSet<URI> copy = new HashSet(this.updated);
            this.updated.clear();
            if (updatedURIs != null && updatedURIs.length > 0) {
                for (URI uri : updatedURIs) {
                    if (!(uri == null || this.updated.contains(uri))) {
                        this.updated.add(uri);
                    }
                }
                if ((!copy.isEmpty() || !this.updated.isEmpty()) && !copy.equals(new HashSet(this.updated))) {
                    buildBackups();
                    synchronized (this.reconnectMutex) {
                        reconnect(rebalance);
                    }
                }
            }
        }
    }

    public String getUpdateURIsURL() {
        return this.updateURIsURL;
    }

    public void setUpdateURIsURL(String updateURIsURL) {
        this.updateURIsURL = updateURIsURL;
    }

    public boolean isRebalanceUpdateURIs() {
        return this.rebalanceUpdateURIs;
    }

    public void setRebalanceUpdateURIs(boolean rebalanceUpdateURIs) {
        this.rebalanceUpdateURIs = rebalanceUpdateURIs;
    }

    public int getReceiveCounter() {
        Transport transport = (Transport) this.connectedTransport.get();
        if (transport == null) {
            return 0;
        }
        return transport.getReceiveCounter();
    }

    public int getConnectFailures() {
        return this.connectFailures;
    }

    public void connectionInterruptProcessingComplete(ConnectionId connectionId) {
        synchronized (this.reconnectMutex) {
            this.stateTracker.connectionInterruptProcessingComplete(this, connectionId);
        }
    }

    public ConnectionStateTracker getStateTracker() {
        return this.stateTracker;
    }

    private boolean contains(URI newURI) {
        Iterator i$ = this.uris.iterator();
        while (i$.hasNext()) {
            URI uri = (URI) i$.next();
            if (newURI.getPort() == uri.getPort()) {
                InetAddress newAddr = null;
                try {
                    newAddr = InetAddress.getByName(newURI.getHost());
                    if (InetAddress.getByName(uri.getHost()).equals(newAddr)) {
                        return true;
                    }
                } catch (IOException e) {
                    if (newAddr == null) {
                        LOG.error("Failed to Lookup INetAddress for URI[ " + newURI + " ] : " + e);
                    } else {
                        LOG.error("Failed to Lookup INetAddress for URI[ " + uri + " ] : " + e);
                    }
                    if (newURI.getHost().equalsIgnoreCase(uri.getHost())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private InputStreamReader getURLStream(String path) throws IOException {
        InputStreamReader result = null;
        try {
            URL url = new URL(path);
            URL url2;
            try {
                url2 = url;
                result = new InputStreamReader(url.openStream());
            } catch (MalformedURLException e) {
                url2 = url;
            }
        } catch (MalformedURLException e2) {
        }
        if (result == null) {
            return new FileReader(path);
        }
        return result;
    }
}
