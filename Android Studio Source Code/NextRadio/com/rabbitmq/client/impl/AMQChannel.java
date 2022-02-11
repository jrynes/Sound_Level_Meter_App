package com.rabbitmq.client.impl;

import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Command;
import com.rabbitmq.client.Method;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.utility.BlockingValueOrException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.apache.activemq.transport.stomp.Stomp;

public abstract class AMQChannel extends ShutdownNotifierComponent {
    private RpcContinuation _activeRpc;
    public boolean _blockContent;
    protected final Object _channelMutex;
    private final int _channelNumber;
    private AMQCommand _command;
    private final AMQConnection _connection;

    public interface RpcContinuation {
        void handleCommand(AMQCommand aMQCommand);

        void handleShutdownSignal(ShutdownSignalException shutdownSignalException);
    }

    public static abstract class BlockingRpcContinuation<T> implements RpcContinuation {
        public final BlockingValueOrException<T, ShutdownSignalException> _blocker;

        public abstract T transformReply(AMQCommand aMQCommand);

        public BlockingRpcContinuation() {
            this._blocker = new BlockingValueOrException();
        }

        public void handleCommand(AMQCommand command) {
            this._blocker.setValue(transformReply(command));
        }

        public void handleShutdownSignal(ShutdownSignalException signal) {
            this._blocker.setException(signal);
        }

        public T getReply() throws ShutdownSignalException {
            return this._blocker.uninterruptibleGetValue();
        }

        public T getReply(int timeout) throws ShutdownSignalException, TimeoutException {
            return this._blocker.uninterruptibleGetValue(timeout);
        }
    }

    public static class SimpleBlockingRpcContinuation extends BlockingRpcContinuation<AMQCommand> {
        public AMQCommand transformReply(AMQCommand command) {
            return command;
        }
    }

    public abstract boolean processAsync(Command command) throws IOException;

    public AMQChannel(AMQConnection connection, int channelNumber) {
        this._channelMutex = new Object();
        this._command = new AMQCommand();
        this._activeRpc = null;
        this._blockContent = false;
        this._connection = connection;
        this._channelNumber = channelNumber;
    }

    public int getChannelNumber() {
        return this._channelNumber;
    }

    public void handleFrame(Frame frame) throws IOException {
        AMQCommand command = this._command;
        if (command.handleFrame(frame)) {
            this._command = new AMQCommand();
            handleCompleteInboundCommand(command);
        }
    }

    public static IOException wrap(ShutdownSignalException ex) {
        return wrap(ex, null);
    }

    public static IOException wrap(ShutdownSignalException ex, String message) {
        IOException ioe = new IOException(message);
        ioe.initCause(ex);
        return ioe;
    }

    public AMQCommand exnWrappingRpc(Method m) throws IOException {
        try {
            return privateRpc(m);
        } catch (AlreadyClosedException ace) {
            throw ace;
        } catch (ShutdownSignalException ex) {
            throw wrap(ex);
        }
    }

    public void handleCompleteInboundCommand(AMQCommand command) throws IOException {
        if (!processAsync(command)) {
            nextOutstandingRpc().handleCommand(command);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void enqueueRpc(com.rabbitmq.client.impl.AMQChannel.RpcContinuation r5) {
        /*
        r4 = this;
        r3 = r4._channelMutex;
        monitor-enter(r3);
        r1 = 0;
    L_0x0004:
        r2 = r4._activeRpc;	 Catch:{ all -> 0x001e }
        if (r2 == 0) goto L_0x0011;
    L_0x0008:
        r2 = r4._channelMutex;	 Catch:{ InterruptedException -> 0x000e }
        r2.wait();	 Catch:{ InterruptedException -> 0x000e }
        goto L_0x0004;
    L_0x000e:
        r0 = move-exception;
        r1 = 1;
        goto L_0x0004;
    L_0x0011:
        if (r1 == 0) goto L_0x001a;
    L_0x0013:
        r2 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x001e }
        r2.interrupt();	 Catch:{ all -> 0x001e }
    L_0x001a:
        r4._activeRpc = r5;	 Catch:{ all -> 0x001e }
        monitor-exit(r3);	 Catch:{ all -> 0x001e }
        return;
    L_0x001e:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x001e }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.rabbitmq.client.impl.AMQChannel.enqueueRpc(com.rabbitmq.client.impl.AMQChannel$RpcContinuation):void");
    }

    public boolean isOutstandingRpc() {
        boolean z;
        synchronized (this._channelMutex) {
            z = this._activeRpc != null;
        }
        return z;
    }

    public RpcContinuation nextOutstandingRpc() {
        RpcContinuation result;
        synchronized (this._channelMutex) {
            result = this._activeRpc;
            this._activeRpc = null;
            this._channelMutex.notifyAll();
        }
        return result;
    }

    public void ensureIsOpen() throws AlreadyClosedException {
        if (!isOpen()) {
            throw new AlreadyClosedException("Attempt to use closed channel", this);
        }
    }

    public AMQCommand rpc(Method m) throws IOException, ShutdownSignalException {
        return privateRpc(m);
    }

    private AMQCommand privateRpc(Method m) throws IOException, ShutdownSignalException {
        SimpleBlockingRpcContinuation k = new SimpleBlockingRpcContinuation();
        rpc(m, k);
        return (AMQCommand) k.getReply();
    }

    public void rpc(Method m, RpcContinuation k) throws IOException {
        synchronized (this._channelMutex) {
            ensureIsOpen();
            quiescingRpc(m, k);
        }
    }

    public void quiescingRpc(Method m, RpcContinuation k) throws IOException {
        synchronized (this._channelMutex) {
            enqueueRpc(k);
            quiescingTransmit(m);
        }
    }

    public String toString() {
        return "AMQChannel(" + this._connection + Stomp.COMMA + this._channelNumber + ")";
    }

    public void processShutdownSignal(ShutdownSignalException signal, boolean ignoreClosed, boolean notifyRpc) {
        try {
            synchronized (this._channelMutex) {
                if (setShutdownCauseIfOpen(signal) || ignoreClosed) {
                    this._channelMutex.notifyAll();
                } else {
                    throw new AlreadyClosedException("Attempt to use closed channel", this);
                }
            }
            if (notifyRpc) {
                notifyOutstandingRpc(signal);
            }
        } catch (Throwable th) {
            if (notifyRpc) {
                notifyOutstandingRpc(signal);
            }
        }
    }

    public void notifyOutstandingRpc(ShutdownSignalException signal) {
        RpcContinuation k = nextOutstandingRpc();
        if (k != null) {
            k.handleShutdownSignal(signal);
        }
    }

    public void transmit(Method m) throws IOException {
        synchronized (this._channelMutex) {
            transmit(new AMQCommand(m));
        }
    }

    public void transmit(AMQCommand c) throws IOException {
        synchronized (this._channelMutex) {
            ensureIsOpen();
            quiescingTransmit(c);
        }
    }

    public void quiescingTransmit(Method m) throws IOException {
        synchronized (this._channelMutex) {
            quiescingTransmit(new AMQCommand(m));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void quiescingTransmit(com.rabbitmq.client.impl.AMQCommand r3) throws java.io.IOException {
        /*
        r2 = this;
        r1 = r2._channelMutex;
        monitor-enter(r1);
        r0 = r3.getMethod();	 Catch:{ all -> 0x001a }
        r0 = r0.hasContent();	 Catch:{ all -> 0x001a }
        if (r0 == 0) goto L_0x001d;
    L_0x000d:
        r0 = r2._blockContent;	 Catch:{ all -> 0x001a }
        if (r0 == 0) goto L_0x001d;
    L_0x0011:
        r0 = r2._channelMutex;	 Catch:{ InterruptedException -> 0x0022 }
        r0.wait();	 Catch:{ InterruptedException -> 0x0022 }
    L_0x0016:
        r2.ensureIsOpen();	 Catch:{ all -> 0x001a }
        goto L_0x000d;
    L_0x001a:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
        throw r0;
    L_0x001d:
        r3.transmit(r2);	 Catch:{ all -> 0x001a }
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
        return;
    L_0x0022:
        r0 = move-exception;
        goto L_0x0016;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.rabbitmq.client.impl.AMQChannel.quiescingTransmit(com.rabbitmq.client.impl.AMQCommand):void");
    }

    public AMQConnection getConnection() {
        return this._connection;
    }
}
