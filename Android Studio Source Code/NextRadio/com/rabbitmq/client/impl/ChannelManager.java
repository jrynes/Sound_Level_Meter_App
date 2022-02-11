package com.rabbitmq.client.impl;

import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.utility.IntAllocator;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.xbill.DNS.Message;

public final class ChannelManager {
    private static final int SHUTDOWN_TIMEOUT_SECONDS = 10;
    private final Map<Integer, ChannelN> _channelMap;
    private final int _channelMax;
    private final IntAllocator channelNumberAllocator;
    private final Object monitor;
    private final Set<CountDownLatch> shutdownSet;
    private final ConsumerWorkService workService;

    /* renamed from: com.rabbitmq.client.impl.ChannelManager.1 */
    class C13161 implements Runnable {
        final /* synthetic */ Set val$sdSet;
        final /* synthetic */ ConsumerWorkService val$ssWorkService;

        C13161(Set set, ConsumerWorkService consumerWorkService) {
            this.val$sdSet = set;
            this.val$ssWorkService = consumerWorkService;
        }

        public void run() {
            for (CountDownLatch latch : this.val$sdSet) {
                try {
                    latch.await(10, TimeUnit.SECONDS);
                } catch (Throwable th) {
                }
            }
            this.val$ssWorkService.shutdown();
        }
    }

    public int getChannelMax() {
        return this._channelMax;
    }

    public ChannelManager(ConsumerWorkService workService, int channelMax) {
        this.monitor = new Object();
        this._channelMap = new HashMap();
        this.shutdownSet = new HashSet();
        if (channelMax == 0) {
            channelMax = Message.MAXLENGTH;
        }
        this._channelMax = channelMax;
        this.channelNumberAllocator = new IntAllocator(1, channelMax);
        this.workService = workService;
    }

    public ChannelN getChannel(int channelNumber) {
        ChannelN ch;
        synchronized (this.monitor) {
            ch = (ChannelN) this._channelMap.get(Integer.valueOf(channelNumber));
            if (ch == null) {
                throw new UnknownChannelException(channelNumber);
            }
        }
        return ch;
    }

    public void handleSignal(ShutdownSignalException signal) {
        synchronized (this.monitor) {
            Set<ChannelN> channels = new HashSet(this._channelMap.values());
        }
        for (ChannelN channel : channels) {
            releaseChannelNumber(channel);
            channel.processShutdownSignal(signal, true, true);
            this.shutdownSet.add(channel.getShutdownLatch());
            channel.notifyListeners();
        }
        scheduleShutdownProcessing();
    }

    private void scheduleShutdownProcessing() {
        Thread shutdownThread = new Thread(new C13161(new HashSet(this.shutdownSet), this.workService), "ConsumerWorkServiceShutdown");
        shutdownThread.setDaemon(true);
        shutdownThread.start();
    }

    public ChannelN createChannel(AMQConnection connection) throws IOException {
        ChannelN channelN;
        synchronized (this.monitor) {
            int channelNumber = this.channelNumberAllocator.allocate();
            if (channelNumber == -1) {
                channelN = null;
            } else {
                channelN = addNewChannel(connection, channelNumber);
                channelN.open();
            }
        }
        return channelN;
    }

    public ChannelN createChannel(AMQConnection connection, int channelNumber) throws IOException {
        ChannelN ch;
        synchronized (this.monitor) {
            if (this.channelNumberAllocator.reserve(channelNumber)) {
                ch = addNewChannel(connection, channelNumber);
                ch.open();
            } else {
                ch = null;
            }
        }
        return ch;
    }

    private ChannelN addNewChannel(AMQConnection connection, int channelNumber) throws IOException {
        if (this._channelMap.containsKey(Integer.valueOf(channelNumber))) {
            throw new IllegalStateException("We have attempted to create a channel with a number that is already in use. This should never happen. Please report this as a bug.");
        }
        ChannelN ch = new ChannelN(connection, channelNumber, this.workService);
        this._channelMap.put(Integer.valueOf(ch.getChannelNumber()), ch);
        return ch;
    }

    public void releaseChannelNumber(ChannelN channel) {
        synchronized (this.monitor) {
            int channelNumber = channel.getChannelNumber();
            ChannelN existing = (ChannelN) this._channelMap.remove(Integer.valueOf(channelNumber));
            if (existing == null) {
            } else if (existing != channel) {
                this._channelMap.put(Integer.valueOf(channelNumber), existing);
            } else {
                this.channelNumberAllocator.free(channelNumber);
            }
        }
    }
}
