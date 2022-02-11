package org.apache.activemq.transport.fanout;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.Response;
import org.apache.activemq.state.ConnectionStateTracker;
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
import org.apache.activemq.util.ServiceStopper;
import org.apache.activemq.util.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FanoutTransport implements CompositeTransport {
    private static final Logger LOG;
    private long backOffMultiplier;
    private boolean connected;
    private int connectedCount;
    private Exception connectionFailure;
    private boolean disposed;
    private boolean fanOutQueues;
    private long initialReconnectDelay;
    private int maxReconnectAttempts;
    private long maxReconnectDelay;
    private int minAckCount;
    private FanoutTransportHandler primary;
    private final Object reconnectMutex;
    private final TaskRunner reconnectTask;
    private final TaskRunnerFactory reconnectTaskFactory;
    private final ConcurrentHashMap<Integer, RequestCounter> requestMap;
    private boolean started;
    private final ConnectionStateTracker stateTracker;
    private TransportListener transportListener;
    private final ArrayList<FanoutTransportHandler> transports;
    private final boolean useExponentialBackOff;

    class 1 implements Task {
        1() {
        }

        public boolean iterate() {
            return FanoutTransport.this.doConnect();
        }
    }

    class FanoutTransportHandler extends DefaultTransportListener {
        private int connectFailures;
        private long reconnectDate;
        private long reconnectDelay;
        private Transport transport;
        private final URI uri;

        static /* synthetic */ long access$1230(FanoutTransportHandler x0, long x1) {
            long j = x0.reconnectDelay * x1;
            x0.reconnectDelay = j;
            return j;
        }

        static /* synthetic */ int access$1304(FanoutTransportHandler x0) {
            int i = x0.connectFailures + 1;
            x0.connectFailures = i;
            return i;
        }

        public FanoutTransportHandler(URI uri) {
            this.reconnectDelay = FanoutTransport.this.initialReconnectDelay;
            this.uri = uri;
        }

        public void onCommand(Object o) {
            Command command = (Command) o;
            if (command.isResponse()) {
                Integer id = new Integer(((Response) command).getCorrelationId());
                RequestCounter rc = (RequestCounter) FanoutTransport.this.requestMap.get(id);
                if (rc == null) {
                    FanoutTransport.this.transportListenerOnCommand(command);
                    return;
                } else if (rc.ackCount.decrementAndGet() <= 0) {
                    FanoutTransport.this.requestMap.remove(id);
                    FanoutTransport.this.transportListenerOnCommand(command);
                    return;
                } else {
                    return;
                }
            }
            FanoutTransport.this.transportListenerOnCommand(command);
        }

        public void onException(IOException error) {
            try {
                synchronized (FanoutTransport.this.reconnectMutex) {
                    if (this.transport == null || !this.transport.isConnected()) {
                        return;
                    }
                    FanoutTransport.LOG.debug("Transport failed, starting up reconnect task", error);
                    ServiceSupport.dispose(this.transport);
                    this.transport = null;
                    FanoutTransport.this.connectedCount = FanoutTransport.this.connectedCount - 1;
                    if (FanoutTransport.this.primary == this) {
                        FanoutTransport.this.primary = null;
                    }
                    FanoutTransport.this.reconnectTask.wakeup();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                if (FanoutTransport.this.transportListener != null) {
                    FanoutTransport.this.transportListener.onException(new InterruptedIOException());
                }
            }
        }
    }

    static class RequestCounter {
        final AtomicInteger ackCount;
        final Command command;

        RequestCounter(Command command, int count) {
            this.command = command;
            this.ackCount = new AtomicInteger(count);
        }

        public String toString() {
            return this.command.getCommandId() + "=" + this.ackCount.get();
        }
    }

    static {
        LOG = LoggerFactory.getLogger(FanoutTransport.class);
    }

    public FanoutTransport() throws InterruptedIOException {
        this.reconnectMutex = new Object();
        this.stateTracker = new ConnectionStateTracker();
        this.requestMap = new ConcurrentHashMap();
        this.transports = new ArrayList();
        this.minAckCount = 2;
        this.initialReconnectDelay = 10;
        this.maxReconnectDelay = 30000;
        this.backOffMultiplier = 2;
        this.useExponentialBackOff = true;
        this.fanOutQueues = false;
        this.reconnectTaskFactory = new TaskRunnerFactory();
        this.reconnectTaskFactory.init();
        this.reconnectTask = this.reconnectTaskFactory.createTaskRunner(new 1(), "ActiveMQ Fanout Worker: " + System.identityHashCode(this));
    }

    private boolean doConnect() {
        long closestReconnectDate = 0;
        synchronized (this.reconnectMutex) {
            if (this.disposed || this.connectionFailure != null) {
                this.reconnectMutex.notifyAll();
            }
            int size = this.transports.size();
            int i = this.connectedCount;
            if (size == r0 || this.disposed || this.connectionFailure != null) {
                return false;
            }
            if (!this.transports.isEmpty()) {
                Iterator<FanoutTransportHandler> iter = this.transports.iterator();
                int i2 = 0;
                while (iter.hasNext() && !this.disposed) {
                    long now = System.currentTimeMillis();
                    FanoutTransportHandler fanoutHandler = (FanoutTransportHandler) iter.next();
                    if (fanoutHandler.transport == null) {
                        if (fanoutHandler.reconnectDate == 0 || fanoutHandler.reconnectDate <= now) {
                            URI uri = fanoutHandler.uri;
                            try {
                                LOG.debug("Stopped: " + this);
                                LOG.debug("Attempting connect to: " + uri);
                                Transport t = TransportFactory.compositeConnect(uri);
                                fanoutHandler.transport = t;
                                t.setTransportListener(fanoutHandler);
                                if (this.started) {
                                    restoreTransport(fanoutHandler);
                                }
                                LOG.debug("Connection established");
                                fanoutHandler.reconnectDelay = this.initialReconnectDelay;
                                fanoutHandler.connectFailures = 0;
                                if (this.primary == null) {
                                    this.primary = fanoutHandler;
                                }
                                this.connectedCount++;
                            } catch (Exception e) {
                                LOG.debug("Connect fail to: " + uri + ", reason: " + e);
                                if (fanoutHandler.transport != null) {
                                    ServiceSupport.dispose(fanoutHandler.transport);
                                    fanoutHandler.transport = null;
                                }
                                if (this.maxReconnectAttempts > 0) {
                                    size = FanoutTransportHandler.access$1304(fanoutHandler);
                                    i = this.maxReconnectAttempts;
                                    if (size >= r0) {
                                        LOG.error("Failed to connect to transport after: " + fanoutHandler.connectFailures + " attempt(s)");
                                        this.connectionFailure = e;
                                        this.reconnectMutex.notifyAll();
                                        return false;
                                    }
                                }
                                FanoutTransportHandler.access$1230(fanoutHandler, this.backOffMultiplier);
                                if (fanoutHandler.reconnectDelay > this.maxReconnectDelay) {
                                    fanoutHandler.reconnectDelay = this.maxReconnectDelay;
                                }
                                fanoutHandler.reconnectDate = fanoutHandler.reconnectDelay + now;
                                if (closestReconnectDate == 0 || fanoutHandler.reconnectDate < closestReconnectDate) {
                                    closestReconnectDate = fanoutHandler.reconnectDate;
                                }
                            }
                        } else if (closestReconnectDate == 0 || fanoutHandler.reconnectDate < closestReconnectDate) {
                            closestReconnectDate = fanoutHandler.reconnectDate;
                        }
                    }
                    i2++;
                }
                size = this.transports.size();
                i = this.connectedCount;
                if (size == r0 || this.disposed) {
                    this.reconnectMutex.notifyAll();
                    return false;
                }
            }
            try {
                long reconnectDelay = closestReconnectDate - System.currentTimeMillis();
                if (reconnectDelay > 0) {
                    LOG.debug("Waiting " + reconnectDelay + " ms before attempting connection. ");
                    Thread.sleep(reconnectDelay);
                }
            } catch (InterruptedException e2) {
                Thread.currentThread().interrupt();
            }
            return true;
        }
    }

    public void start() throws Exception {
        synchronized (this.reconnectMutex) {
            LOG.debug("Started.");
            if (this.started) {
                return;
            }
            this.started = true;
            Iterator<FanoutTransportHandler> iter = this.transports.iterator();
            while (iter.hasNext()) {
                FanoutTransportHandler th = (FanoutTransportHandler) iter.next();
                if (th.transport != null) {
                    restoreTransport(th);
                }
            }
            this.connected = true;
        }
    }

    public void stop() throws Exception {
        try {
            synchronized (this.reconnectMutex) {
                ServiceStopper ss = new ServiceStopper();
                if (this.started) {
                    this.started = false;
                    this.disposed = true;
                    this.connected = false;
                    Iterator<FanoutTransportHandler> iter = this.transports.iterator();
                    while (iter.hasNext()) {
                        FanoutTransportHandler th = (FanoutTransportHandler) iter.next();
                        if (th.transport != null) {
                            ss.stop(th.transport);
                        }
                    }
                    LOG.debug("Stopped: " + this);
                    ss.throwFirstException();
                    this.reconnectTask.shutdown();
                    this.reconnectTaskFactory.shutdownNow();
                    return;
                }
            }
        } finally {
            this.reconnectTask.shutdown();
            this.reconnectTaskFactory.shutdownNow();
        }
    }

    public int getMinAckCount() {
        return this.minAckCount;
    }

    public void setMinAckCount(int minAckCount) {
        this.minAckCount = minAckCount;
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

    public long getReconnectDelayExponent() {
        return this.backOffMultiplier;
    }

    public void setReconnectDelayExponent(long reconnectDelayExponent) {
        this.backOffMultiplier = reconnectDelayExponent;
    }

    public int getMaxReconnectAttempts() {
        return this.maxReconnectAttempts;
    }

    public void setMaxReconnectAttempts(int maxReconnectAttempts) {
        this.maxReconnectAttempts = maxReconnectAttempts;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void oneway(java.lang.Object r13) throws java.io.IOException {
        /*
        r12 = this;
        r0 = r13;
        r0 = (org.apache.activemq.command.Command) r0;
        r8 = r12.reconnectMutex;	 Catch:{ InterruptedException -> 0x003f }
        monitor-enter(r8);	 Catch:{ InterruptedException -> 0x003f }
    L_0x0006:
        r7 = r12.connectedCount;	 Catch:{ all -> 0x003c }
        r9 = r12.minAckCount;	 Catch:{ all -> 0x003c }
        if (r7 >= r9) goto L_0x004d;
    L_0x000c:
        r7 = r12.disposed;	 Catch:{ all -> 0x003c }
        if (r7 != 0) goto L_0x004d;
    L_0x0010:
        r7 = r12.connectionFailure;	 Catch:{ all -> 0x003c }
        if (r7 != 0) goto L_0x004d;
    L_0x0014:
        r7 = LOG;	 Catch:{ all -> 0x003c }
        r9 = new java.lang.StringBuilder;	 Catch:{ all -> 0x003c }
        r9.<init>();	 Catch:{ all -> 0x003c }
        r10 = "Waiting for at least ";
        r9 = r9.append(r10);	 Catch:{ all -> 0x003c }
        r10 = r12.minAckCount;	 Catch:{ all -> 0x003c }
        r9 = r9.append(r10);	 Catch:{ all -> 0x003c }
        r10 = " transports to be connected.";
        r9 = r9.append(r10);	 Catch:{ all -> 0x003c }
        r9 = r9.toString();	 Catch:{ all -> 0x003c }
        r7.debug(r9);	 Catch:{ all -> 0x003c }
        r7 = r12.reconnectMutex;	 Catch:{ all -> 0x003c }
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7.wait(r10);	 Catch:{ all -> 0x003c }
        goto L_0x0006;
    L_0x003c:
        r7 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x003c }
        throw r7;	 Catch:{ InterruptedException -> 0x003f }
    L_0x003f:
        r1 = move-exception;
        r7 = java.lang.Thread.currentThread();
        r7.interrupt();
        r7 = new java.io.InterruptedIOException;
        r7.<init>();
        throw r7;
    L_0x004d:
        r7 = r12.connectedCount;	 Catch:{ all -> 0x003c }
        r9 = r12.minAckCount;	 Catch:{ all -> 0x003c }
        if (r7 >= r9) goto L_0x0079;
    L_0x0053:
        r7 = r12.disposed;	 Catch:{ all -> 0x003c }
        if (r7 == 0) goto L_0x0065;
    L_0x0057:
        r2 = new java.io.IOException;	 Catch:{ all -> 0x003c }
        r7 = "Transport disposed.";
        r2.<init>(r7);	 Catch:{ all -> 0x003c }
    L_0x005e:
        r7 = r2 instanceof java.io.IOException;	 Catch:{ all -> 0x003c }
        if (r7 == 0) goto L_0x0074;
    L_0x0062:
        r2 = (java.io.IOException) r2;	 Catch:{ all -> 0x003c }
        throw r2;	 Catch:{ all -> 0x003c }
    L_0x0065:
        r7 = r12.connectionFailure;	 Catch:{ all -> 0x003c }
        if (r7 == 0) goto L_0x006c;
    L_0x0069:
        r2 = r12.connectionFailure;	 Catch:{ all -> 0x003c }
        goto L_0x005e;
    L_0x006c:
        r2 = new java.io.IOException;	 Catch:{ all -> 0x003c }
        r7 = "Unexpected failure.";
        r2.<init>(r7);	 Catch:{ all -> 0x003c }
        goto L_0x005e;
    L_0x0074:
        r7 = org.apache.activemq.util.IOExceptionSupport.create(r2);	 Catch:{ all -> 0x003c }
        throw r7;	 Catch:{ all -> 0x003c }
    L_0x0079:
        r3 = r12.isFanoutCommand(r0);	 Catch:{ all -> 0x003c }
        r7 = r12.stateTracker;	 Catch:{ all -> 0x003c }
        r7 = r7.track(r0);	 Catch:{ all -> 0x003c }
        if (r7 != 0) goto L_0x00a2;
    L_0x0085:
        r7 = r0.isResponseRequired();	 Catch:{ all -> 0x003c }
        if (r7 == 0) goto L_0x00a2;
    L_0x008b:
        if (r3 == 0) goto L_0x00d0;
    L_0x008d:
        r5 = r12.minAckCount;	 Catch:{ all -> 0x003c }
    L_0x008f:
        r7 = r12.requestMap;	 Catch:{ all -> 0x003c }
        r9 = new java.lang.Integer;	 Catch:{ all -> 0x003c }
        r10 = r0.getCommandId();	 Catch:{ all -> 0x003c }
        r9.<init>(r10);	 Catch:{ all -> 0x003c }
        r10 = new org.apache.activemq.transport.fanout.FanoutTransport$RequestCounter;	 Catch:{ all -> 0x003c }
        r10.<init>(r0, r5);	 Catch:{ all -> 0x003c }
        r7.put(r9, r10);	 Catch:{ all -> 0x003c }
    L_0x00a2:
        if (r3 == 0) goto L_0x00d2;
    L_0x00a4:
        r7 = r12.transports;	 Catch:{ all -> 0x003c }
        r4 = r7.iterator();	 Catch:{ all -> 0x003c }
    L_0x00aa:
        r7 = r4.hasNext();	 Catch:{ all -> 0x003c }
        if (r7 == 0) goto L_0x00db;
    L_0x00b0:
        r6 = r4.next();	 Catch:{ all -> 0x003c }
        r6 = (org.apache.activemq.transport.fanout.FanoutTransport.FanoutTransportHandler) r6;	 Catch:{ all -> 0x003c }
        r7 = r6.transport;	 Catch:{ all -> 0x003c }
        if (r7 == 0) goto L_0x00aa;
    L_0x00bc:
        r7 = r6.transport;	 Catch:{ IOException -> 0x00c4 }
        r7.oneway(r0);	 Catch:{ IOException -> 0x00c4 }
        goto L_0x00aa;
    L_0x00c4:
        r1 = move-exception;
        r7 = LOG;	 Catch:{ all -> 0x003c }
        r9 = "Send attempt: failed.";
        r7.debug(r9);	 Catch:{ all -> 0x003c }
        r6.onException(r1);	 Catch:{ all -> 0x003c }
        goto L_0x00aa;
    L_0x00d0:
        r5 = 1;
        goto L_0x008f;
    L_0x00d2:
        r7 = r12.primary;	 Catch:{ IOException -> 0x00dd }
        r7 = r7.transport;	 Catch:{ IOException -> 0x00dd }
        r7.oneway(r0);	 Catch:{ IOException -> 0x00dd }
    L_0x00db:
        monitor-exit(r8);	 Catch:{ all -> 0x003c }
        return;
    L_0x00dd:
        r1 = move-exception;
        r7 = LOG;	 Catch:{ all -> 0x003c }
        r9 = "Send attempt: failed.";
        r7.debug(r9);	 Catch:{ all -> 0x003c }
        r7 = r12.primary;	 Catch:{ all -> 0x003c }
        r7.onException(r1);	 Catch:{ all -> 0x003c }
        goto L_0x00db;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.fanout.FanoutTransport.oneway(java.lang.Object):void");
    }

    private boolean isFanoutCommand(Command command) {
        if (command.isMessage()) {
            if (this.fanOutQueues) {
                return true;
            }
            return ((Message) command).getDestination().isTopic();
        } else if (command.getDataStructureType() == 5 || command.getDataStructureType() == 12) {
            return false;
        } else {
            return true;
        }
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

    public void reconnect() {
        LOG.debug("Waking up reconnect task");
        try {
            this.reconnectTask.wakeup();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public TransportListener getTransportListener() {
        return this.transportListener;
    }

    public void setTransportListener(TransportListener commandListener) {
        this.transportListener = commandListener;
    }

    public <T> T narrow(Class<T> target) {
        if (target.isAssignableFrom(getClass())) {
            return target.cast(this);
        }
        synchronized (this.reconnectMutex) {
            Iterator<FanoutTransportHandler> iter = this.transports.iterator();
            while (iter.hasNext()) {
                FanoutTransportHandler th = (FanoutTransportHandler) iter.next();
                if (th.transport != null) {
                    T rc = th.transport.narrow(target);
                    if (rc != null) {
                        return rc;
                    }
                }
            }
            return null;
        }
    }

    protected void restoreTransport(FanoutTransportHandler th) throws Exception, IOException {
        th.transport.start();
        this.stateTracker.setRestoreConsumers(th.transport == this.primary);
        this.stateTracker.restore(th.transport);
        for (RequestCounter rc : this.requestMap.values()) {
            th.transport.oneway(rc.command);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void add(boolean r8, java.net.URI[] r9) {
        /*
        r7 = this;
        r6 = r7.reconnectMutex;
        monitor-enter(r6);
        r0 = 0;
    L_0x0004:
        r5 = r9.length;	 Catch:{ all -> 0x003b }
        if (r0 >= r5) goto L_0x0039;
    L_0x0007:
        r4 = r9[r0];	 Catch:{ all -> 0x003b }
        r2 = 0;
        r5 = r7.transports;	 Catch:{ all -> 0x003b }
        r1 = r5.iterator();	 Catch:{ all -> 0x003b }
    L_0x0010:
        r5 = r1.hasNext();	 Catch:{ all -> 0x003b }
        if (r5 == 0) goto L_0x0027;
    L_0x0016:
        r3 = r1.next();	 Catch:{ all -> 0x003b }
        r3 = (org.apache.activemq.transport.fanout.FanoutTransport.FanoutTransportHandler) r3;	 Catch:{ all -> 0x003b }
        r5 = r3.uri;	 Catch:{ all -> 0x003b }
        r5 = r5.equals(r4);	 Catch:{ all -> 0x003b }
        if (r5 == 0) goto L_0x0010;
    L_0x0026:
        r2 = 1;
    L_0x0027:
        if (r2 != 0) goto L_0x0036;
    L_0x0029:
        r3 = new org.apache.activemq.transport.fanout.FanoutTransport$FanoutTransportHandler;	 Catch:{ all -> 0x003b }
        r3.<init>(r4);	 Catch:{ all -> 0x003b }
        r5 = r7.transports;	 Catch:{ all -> 0x003b }
        r5.add(r3);	 Catch:{ all -> 0x003b }
        r7.reconnect();	 Catch:{ all -> 0x003b }
    L_0x0036:
        r0 = r0 + 1;
        goto L_0x0004;
    L_0x0039:
        monitor-exit(r6);	 Catch:{ all -> 0x003b }
        return;
    L_0x003b:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x003b }
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.fanout.FanoutTransport.add(boolean, java.net.URI[]):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(boolean r7, java.net.URI[] r8) {
        /*
        r6 = this;
        r5 = r6.reconnectMutex;
        monitor-enter(r5);
        r0 = 0;
    L_0x0004:
        r4 = r8.length;	 Catch:{ all -> 0x0040 }
        if (r0 >= r4) goto L_0x003e;
    L_0x0007:
        r3 = r8[r0];	 Catch:{ all -> 0x0040 }
        r4 = r6.transports;	 Catch:{ all -> 0x0040 }
        r1 = r4.iterator();	 Catch:{ all -> 0x0040 }
    L_0x000f:
        r4 = r1.hasNext();	 Catch:{ all -> 0x0040 }
        if (r4 == 0) goto L_0x003b;
    L_0x0015:
        r2 = r1.next();	 Catch:{ all -> 0x0040 }
        r2 = (org.apache.activemq.transport.fanout.FanoutTransport.FanoutTransportHandler) r2;	 Catch:{ all -> 0x0040 }
        r4 = r2.uri;	 Catch:{ all -> 0x0040 }
        r4 = r4.equals(r3);	 Catch:{ all -> 0x0040 }
        if (r4 == 0) goto L_0x000f;
    L_0x0025:
        r4 = r2.transport;	 Catch:{ all -> 0x0040 }
        if (r4 == 0) goto L_0x0038;
    L_0x002b:
        r4 = r2.transport;	 Catch:{ all -> 0x0040 }
        org.apache.activemq.util.ServiceSupport.dispose(r4);	 Catch:{ all -> 0x0040 }
        r4 = r6.connectedCount;	 Catch:{ all -> 0x0040 }
        r4 = r4 + -1;
        r6.connectedCount = r4;	 Catch:{ all -> 0x0040 }
    L_0x0038:
        r1.remove();	 Catch:{ all -> 0x0040 }
    L_0x003b:
        r0 = r0 + 1;
        goto L_0x0004;
    L_0x003e:
        monitor-exit(r5);	 Catch:{ all -> 0x0040 }
        return;
    L_0x0040:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0040 }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.fanout.FanoutTransport.remove(boolean, java.net.URI[]):void");
    }

    public void reconnect(URI uri) throws IOException {
        add(true, new URI[]{uri});
    }

    public boolean isReconnectSupported() {
        return true;
    }

    public boolean isUpdateURIsSupported() {
        return true;
    }

    public void updateURIs(boolean reblance, URI[] uris) throws IOException {
        add(reblance, uris);
    }

    public String getRemoteAddress() {
        if (this.primary == null || this.primary.transport == null) {
            return null;
        }
        return this.primary.transport.getRemoteAddress();
    }

    protected void transportListenerOnCommand(Command command) {
        if (this.transportListener != null) {
            this.transportListener.onCommand(command);
        }
    }

    public boolean isFaultTolerant() {
        return true;
    }

    public boolean isFanOutQueues() {
        return this.fanOutQueues;
    }

    public void setFanOutQueues(boolean fanOutQueues) {
        this.fanOutQueues = fanOutQueues;
    }

    public boolean isDisposed() {
        return this.disposed;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public int getReceiveCounter() {
        int rc = 0;
        synchronized (this.reconnectMutex) {
            Iterator i$ = this.transports.iterator();
            while (i$.hasNext()) {
                FanoutTransportHandler th = (FanoutTransportHandler) i$.next();
                if (th.transport != null) {
                    rc += th.transport.getReceiveCounter();
                }
            }
        }
        return rc;
    }
}
