package com.rabbitmq.client.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.Connection.Close;
import com.rabbitmq.client.AMQP.Connection.CloseOk;
import com.rabbitmq.client.AMQP.Connection.Open;
import com.rabbitmq.client.AMQP.Connection.Secure;
import com.rabbitmq.client.AMQP.Connection.SecureOk;
import com.rabbitmq.client.AMQP.Connection.Start;
import com.rabbitmq.client.AMQP.Connection.StartOk.Builder;
import com.rabbitmq.client.AMQP.Connection.Tune;
import com.rabbitmq.client.AMQP.Connection.TuneOk;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Command;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.LongString;
import com.rabbitmq.client.Method;
import com.rabbitmq.client.MissedHeartbeatException;
import com.rabbitmq.client.PossibleAuthenticationFailureException;
import com.rabbitmq.client.ProtocolVersionMismatchException;
import com.rabbitmq.client.SaslConfig;
import com.rabbitmq.client.SaslMechanism;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.impl.AMQChannel.SimpleBlockingRpcContinuation;
import com.rabbitmq.utility.BlockingCell;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.transport.stomp.Stomp.Headers.Connected;

public class AMQConnection extends ShutdownNotifierComponent implements Connection {
    public static final int HANDSHAKE_TIMEOUT = 10000;
    private static final Version clientVersion;
    private final BlockingCell<Object> _appContinuation;
    private volatile boolean _brokerInitiatedShutdown;
    private final AMQChannel _channel0;
    private volatile ChannelManager _channelManager;
    private final Map<String, Object> _clientProperties;
    private final ExceptionHandler _exceptionHandler;
    private final FrameHandler _frameHandler;
    private volatile int _frameMax;
    private volatile int _heartbeat;
    private final HeartbeatSender _heartbeatSender;
    private volatile boolean _inConnectionNegotiation;
    private volatile int _missedHeartbeats;
    private volatile boolean _running;
    private volatile Map<String, Object> _serverProperties;
    private final String _virtualHost;
    private final ConsumerWorkService _workService;
    private final String password;
    private final int requestedChannelMax;
    private final int requestedFrameMax;
    private final int requestedHeartbeat;
    private final SaslConfig saslConfig;
    private final String username;

    /* renamed from: com.rabbitmq.client.impl.AMQConnection.1 */
    class C13151 extends AMQChannel {
        C13151(AMQConnection x0, int x1) {
            super(x0, x1);
        }

        public boolean processAsync(Command c) throws IOException {
            return getConnection().processControlCommand(c);
        }
    }

    private class MainLoop extends Thread {
        MainLoop(String name) {
            super(name);
        }

        public void run() {
            while (AMQConnection.this._running) {
                try {
                    Frame frame = AMQConnection.this._frameHandler.readFrame();
                    if (frame != null) {
                        AMQConnection.this._missedHeartbeats = 0;
                        if (frame.type == 8) {
                            continue;
                        } else if (frame.channel == 0) {
                            AMQConnection.this._channel0.handleFrame(frame);
                        } else if (AMQConnection.this.isOpen()) {
                            ChannelManager cm = AMQConnection.this._channelManager;
                            if (cm != null) {
                                cm.getChannel(frame.channel).handleFrame(frame);
                            }
                        }
                    } else {
                        AMQConnection.this.handleSocketTimeout();
                    }
                } catch (EOFException ex) {
                    if (!AMQConnection.this._brokerInitiatedShutdown) {
                        AMQConnection.this.shutdown(ex, false, ex, true);
                    }
                    AMQConnection.this._frameHandler.close();
                    AMQConnection.this._appContinuation.set(null);
                    AMQConnection.this.notifyListeners();
                    return;
                } catch (Throwable th) {
                    AMQConnection.this._frameHandler.close();
                    AMQConnection.this._appContinuation.set(null);
                    AMQConnection.this.notifyListeners();
                }
            }
            AMQConnection.this._frameHandler.close();
            AMQConnection.this._appContinuation.set(null);
            AMQConnection.this.notifyListeners();
        }
    }

    private class SocketCloseWait extends Thread {
        private final ShutdownSignalException cause;

        public SocketCloseWait(ShutdownSignalException sse) {
            this.cause = sse;
        }

        public void run() {
            try {
                AMQConnection.this._appContinuation.uninterruptibleGet();
            } finally {
                AMQConnection.this._running = false;
                AMQConnection.this._channel0.notifyOutstandingRpc(this.cause);
            }
        }
    }

    public static final Map<String, Object> defaultClientProperties() {
        Map<String, Object> capabilities = new HashMap();
        capabilities.put("publisher_confirms", Boolean.valueOf(true));
        capabilities.put("exchange_exchange_bindings", Boolean.valueOf(true));
        capabilities.put("basic.nack", Boolean.valueOf(true));
        capabilities.put("consumer_cancel_notify", Boolean.valueOf(true));
        return buildTable(new Object[]{"product", LongStringHelper.asLongString("RabbitMQ"), Connected.VERSION, LongStringHelper.asLongString("2.8.6"), "platform", LongStringHelper.asLongString("Java"), "copyright", LongStringHelper.asLongString("Copyright (C) 2007-2012 VMware, Inc."), "information", LongStringHelper.asLongString("Licensed under the MPL. See http://www.rabbitmq.com/"), "capabilities", capabilities});
    }

    static {
        clientVersion = new Version(0, 9);
    }

    public final void disconnectChannel(ChannelN channel) {
        ChannelManager cm = this._channelManager;
        if (cm != null) {
            cm.releaseChannelNumber(channel);
        }
    }

    private final void ensureIsOpen() throws AlreadyClosedException {
        if (!isOpen()) {
            throw new AlreadyClosedException("Attempt to use closed connection", this);
        }
    }

    public InetAddress getAddress() {
        return this._frameHandler.getAddress();
    }

    public int getPort() {
        return this._frameHandler.getPort();
    }

    public FrameHandler getFrameHandler() {
        return this._frameHandler;
    }

    public Map<String, Object> getServerProperties() {
        return this._serverProperties;
    }

    public AMQConnection(String username, String password, FrameHandler frameHandler, ExecutorService executor, String virtualHost, Map<String, Object> clientProperties, int requestedFrameMax, int requestedChannelMax, int requestedHeartbeat, SaslConfig saslConfig) {
        this(username, password, frameHandler, executor, virtualHost, clientProperties, requestedFrameMax, requestedChannelMax, requestedHeartbeat, saslConfig, new DefaultExceptionHandler());
    }

    public AMQConnection(String username, String password, FrameHandler frameHandler, ExecutorService executor, String virtualHost, Map<String, Object> clientProperties, int requestedFrameMax, int requestedChannelMax, int requestedHeartbeat, SaslConfig saslConfig, ExceptionHandler execeptionHandler) {
        this._channel0 = new C13151(this, 0);
        this._running = false;
        this._appContinuation = new BlockingCell();
        this._frameMax = 0;
        this._missedHeartbeats = 0;
        this._heartbeat = 0;
        checkPreconditions();
        this.username = username;
        this.password = password;
        this._frameHandler = frameHandler;
        this._virtualHost = virtualHost;
        this._exceptionHandler = execeptionHandler;
        this._clientProperties = new HashMap(clientProperties);
        this.requestedFrameMax = requestedFrameMax;
        this.requestedChannelMax = requestedChannelMax;
        this.requestedHeartbeat = requestedHeartbeat;
        this.saslConfig = saslConfig;
        this._workService = new ConsumerWorkService(executor);
        this._channelManager = null;
        this._heartbeatSender = new HeartbeatSender(frameHandler);
        this._brokerInitiatedShutdown = false;
        this._inConnectionNegotiation = true;
    }

    public void start() throws IOException {
        this._running = true;
        SimpleBlockingRpcContinuation connStartBlocker = new SimpleBlockingRpcContinuation();
        this._channel0.enqueueRpc(connStartBlocker);
        try {
            this._frameHandler.setTimeout(HANDSHAKE_TIMEOUT);
            this._frameHandler.sendHeader();
            new MainLoop("AMQP Connection " + getHostAddress() + Headers.SEPERATOR + getPort()).start();
            Tune connTune = null;
            try {
                Start connStart = (Start) ((AMQCommand) connStartBlocker.getReply()).getMethod();
                this._serverProperties = Collections.unmodifiableMap(connStart.getServerProperties());
                Version version = new Version(connStart.getVersionMajor(), connStart.getVersionMinor());
                if (Version.checkVersion(clientVersion, version)) {
                    String[] mechanisms = connStart.getMechanisms().toString().split(" ");
                    SaslMechanism sm = this.saslConfig.getSaslMechanism(mechanisms);
                    if (sm == null) {
                        throw new IOException("No compatible authentication mechanism found - server offered [" + connStart.getMechanisms() + "]");
                    }
                    LongString challenge = null;
                    LongString response = sm.handleChallenge(null, this.username, this.password);
                    do {
                        Method method;
                        if (challenge == null) {
                            method = new Builder().clientProperties(this._clientProperties).mechanism(sm.getName()).response(response).build();
                        } else {
                            method = new SecureOk.Builder().response(response).build();
                        }
                        Method serverResponse = this._channel0.rpc(method).getMethod();
                        if (serverResponse instanceof Tune) {
                            connTune = (Tune) serverResponse;
                            continue;
                        } else {
                            challenge = ((Secure) serverResponse).getChallenge();
                            response = sm.handleChallenge(challenge, this.username, this.password);
                            continue;
                        }
                    } while (connTune == null);
                    try {
                        int channelMax = negotiatedMaxValue(this.requestedChannelMax, connTune.getChannelMax());
                        this._channelManager = new ChannelManager(this._workService, channelMax);
                        int frameMax = negotiatedMaxValue(this.requestedFrameMax, connTune.getFrameMax());
                        this._frameMax = frameMax;
                        int heartbeat = negotiatedMaxValue(this.requestedHeartbeat, connTune.getHeartbeat());
                        setHeartbeat(heartbeat);
                        this._channel0.transmit(new TuneOk.Builder().channelMax(channelMax).frameMax(frameMax).heartbeat(heartbeat).build());
                        this._channel0.exnWrappingRpc(new Open.Builder().virtualHost(this._virtualHost).build());
                        this._inConnectionNegotiation = false;
                        return;
                    } catch (IOException ioe) {
                        this._heartbeatSender.shutdown();
                        this._frameHandler.close();
                        throw ioe;
                    } catch (ShutdownSignalException sse) {
                        this._heartbeatSender.shutdown();
                        this._frameHandler.close();
                        throw AMQChannel.wrap(sse);
                    }
                }
                throw new ProtocolVersionMismatchException(clientVersion, version);
            } catch (ShutdownSignalException e) {
                throw new PossibleAuthenticationFailureException(e);
            } catch (IOException ioe2) {
                this._frameHandler.close();
                throw ioe2;
            } catch (ShutdownSignalException sse2) {
                this._frameHandler.close();
                throw AMQChannel.wrap(sse2);
            }
        } catch (IOException ioe22) {
            this._frameHandler.close();
            throw ioe22;
        }
    }

    private static final void checkPreconditions() {
        AMQCommand.checkPreconditions();
    }

    public int getChannelMax() {
        ChannelManager cm = this._channelManager;
        if (cm == null) {
            return 0;
        }
        return cm.getChannelMax();
    }

    public int getFrameMax() {
        return this._frameMax;
    }

    public int getHeartbeat() {
        return this._heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        try {
            this._heartbeatSender.setHeartbeat(heartbeat);
            this._heartbeat = heartbeat;
            this._frameHandler.setTimeout((heartbeat * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) / 4);
        } catch (SocketException e) {
        }
    }

    public Map<String, Object> getClientProperties() {
        return new HashMap(this._clientProperties);
    }

    public ExceptionHandler getExceptionHandler() {
        return this._exceptionHandler;
    }

    public Channel createChannel(int channelNumber) throws IOException {
        ensureIsOpen();
        ChannelManager cm = this._channelManager;
        if (cm == null) {
            return null;
        }
        return cm.createChannel(this, channelNumber);
    }

    public Channel createChannel() throws IOException {
        ensureIsOpen();
        ChannelManager cm = this._channelManager;
        if (cm == null) {
            return null;
        }
        return cm.createChannel(this);
    }

    public void writeFrame(Frame f) throws IOException {
        this._frameHandler.writeFrame(f);
        this._heartbeatSender.signalActivity();
    }

    public void flush() throws IOException {
        this._frameHandler.flush();
    }

    private static final int negotiatedMaxValue(int clientValue, int serverValue) {
        return (clientValue == 0 || serverValue == 0) ? Math.max(clientValue, serverValue) : Math.min(clientValue, serverValue);
    }

    private void handleSocketTimeout() throws SocketTimeoutException {
        if (this._inConnectionNegotiation) {
            throw new SocketTimeoutException("Timeout during Connection negotiation");
        } else if (this._heartbeat != 0) {
            int i = this._missedHeartbeats + 1;
            this._missedHeartbeats = i;
            if (i > 8) {
                throw new MissedHeartbeatException("Heartbeat missing with heartbeat = " + this._heartbeat + " seconds");
            }
        }
    }

    public boolean processControlCommand(Command c) throws IOException {
        Method method = c.getMethod();
        if (isOpen()) {
            if (!(method instanceof Close)) {
                return false;
            }
            handleConnectionClose(c);
            return true;
        } else if (method instanceof Close) {
            try {
                this._channel0.quiescingTransmit(new CloseOk.Builder().build());
                return true;
            } catch (IOException e) {
                return true;
            }
        } else if (!(method instanceof CloseOk)) {
            return true;
        } else {
            this._running = false;
            if (this._channel0.isOutstandingRpc()) {
                return false;
            }
            return true;
        }
    }

    public void handleConnectionClose(Command closeCommand) {
        ShutdownSignalException sse = shutdown(closeCommand, false, null, false);
        try {
            this._channel0.quiescingTransmit(new CloseOk.Builder().build());
        } catch (IOException e) {
        }
        this._brokerInitiatedShutdown = true;
        Thread scw = new SocketCloseWait(sse);
        scw.setName("AMQP Connection Closing Monitor " + getHostAddress() + Headers.SEPERATOR + getPort());
        scw.start();
    }

    public ShutdownSignalException shutdown(Object reason, boolean initiatedByApplication, Throwable cause, boolean notifyRpc) {
        boolean z = true;
        ShutdownSignalException sse = new ShutdownSignalException(true, initiatedByApplication, reason, this);
        sse.initCause(cause);
        if (setShutdownCauseIfOpen(sse) || !initiatedByApplication) {
            this._heartbeatSender.shutdown();
            AMQChannel aMQChannel = this._channel0;
            if (initiatedByApplication) {
                z = false;
            }
            aMQChannel.processShutdownSignal(sse, z, notifyRpc);
            ChannelManager cm = this._channelManager;
            if (cm != null) {
                cm.handleSignal(sse);
            }
            return sse;
        }
        throw new AlreadyClosedException("Attempt to use closed connection", this);
    }

    public void close() throws IOException {
        close(-1);
    }

    public void close(int timeout) throws IOException {
        close(AMQP.REPLY_SUCCESS, "OK", timeout);
    }

    public void close(int closeCode, String closeMessage) throws IOException {
        close(closeCode, closeMessage, -1);
    }

    public void close(int closeCode, String closeMessage, int timeout) throws IOException {
        close(closeCode, closeMessage, true, null, timeout, false);
    }

    public void abort() {
        abort(-1);
    }

    public void abort(int closeCode, String closeMessage) {
        abort(closeCode, closeMessage, -1);
    }

    public void abort(int timeout) {
        abort(AMQP.REPLY_SUCCESS, "OK", timeout);
    }

    public void abort(int closeCode, String closeMessage, int timeout) {
        try {
            close(closeCode, closeMessage, true, null, timeout, true);
        } catch (IOException e) {
        }
    }

    public void close(int closeCode, String closeMessage, boolean initiatedByApplication, Throwable cause) throws IOException {
        close(closeCode, closeMessage, initiatedByApplication, cause, -1, false);
    }

    public void close(int closeCode, String closeMessage, boolean initiatedByApplication, Throwable cause, int timeout, boolean abort) throws IOException {
        boolean sync = true;
        if (Thread.currentThread() instanceof MainLoop) {
            sync = false;
        }
        try {
            Method reason = new Close.Builder().replyCode(closeCode).replyText(closeMessage).build();
            shutdown(reason, initiatedByApplication, cause, true);
            if (sync) {
                SimpleBlockingRpcContinuation k = new SimpleBlockingRpcContinuation();
                this._channel0.quiescingRpc(reason, k);
                k.getReply(timeout);
            } else {
                this._channel0.quiescingTransmit(reason);
            }
            if (sync) {
                this._frameHandler.close();
            }
        } catch (TimeoutException tte) {
            if (!abort) {
                throw new ShutdownSignalException(true, true, tte, this);
            } else if (sync) {
                this._frameHandler.close();
            }
        } catch (ShutdownSignalException sse) {
            if (!abort) {
                throw sse;
            } else if (sync) {
                this._frameHandler.close();
            }
        } catch (IOException ioe) {
            if (!abort) {
                throw ioe;
            } else if (sync) {
                this._frameHandler.close();
            }
        } catch (Throwable th) {
            if (sync) {
                this._frameHandler.close();
            }
        }
    }

    public String toString() {
        return "amqp://" + this.username + "@" + getHostAddress() + Headers.SEPERATOR + getPort() + this._virtualHost;
    }

    private String getHostAddress() {
        return getAddress() == null ? null : getAddress().getHostAddress();
    }

    private static final Map<String, Object> buildTable(Object[] keysValues) {
        Map<String, Object> result = new HashMap();
        for (int index = 0; index < keysValues.length; index += 2) {
            result.put(keysValues[index], keysValues[index + 1]);
        }
        return result;
    }
}
