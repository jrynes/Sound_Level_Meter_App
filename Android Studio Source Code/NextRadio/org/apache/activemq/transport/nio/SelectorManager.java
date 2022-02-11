package org.apache.activemq.transport.nio;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.xbill.DNS.KEYRecord.Flags;

public final class SelectorManager {
    public static final SelectorManager SINGLETON;
    private Executor channelExecutor;
    private LinkedList<SelectorWorker> freeWorkers;
    private int maxChannelsPerWorker;
    private Executor selectorExecutor;

    public interface Listener {
        void onError(SelectorSelection selectorSelection, Throwable th);

        void onSelect(SelectorSelection selectorSelection);
    }

    class 1 implements ThreadFactory {
        private long i;

        1() {
            this.i = 0;
        }

        public Thread newThread(Runnable runnable) {
            this.i++;
            return new Thread(runnable, "ActiveMQ NIO Worker " + this.i);
        }
    }

    public SelectorManager() {
        this.selectorExecutor = createDefaultExecutor();
        this.channelExecutor = this.selectorExecutor;
        this.freeWorkers = new LinkedList();
        this.maxChannelsPerWorker = Flags.FLAG5;
    }

    static {
        SINGLETON = new SelectorManager();
    }

    protected ExecutorService createDefaultExecutor() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10, TimeUnit.SECONDS, new SynchronousQueue(), new 1());
    }

    public static SelectorManager getInstance() {
        return SINGLETON;
    }

    public synchronized SelectorSelection register(SocketChannel socketChannel, Listener listener) throws IOException {
        SelectorSelection selection;
        selection = null;
        while (selection == null) {
            SelectorWorker worker;
            if (this.freeWorkers.size() > 0) {
                worker = (SelectorWorker) this.freeWorkers.getFirst();
                if (worker.isReleased()) {
                    this.freeWorkers.remove(worker);
                } else {
                    worker.retain();
                    selection = new SelectorSelection(worker, socketChannel, listener);
                }
            } else {
                worker = new SelectorWorker(this);
                this.freeWorkers.addFirst(worker);
                selection = new SelectorSelection(worker, socketChannel, listener);
            }
        }
        return selection;
    }

    synchronized void onWorkerFullEvent(SelectorWorker worker) {
        this.freeWorkers.remove(worker);
    }

    public synchronized void onWorkerEmptyEvent(SelectorWorker worker) {
        this.freeWorkers.remove(worker);
    }

    public synchronized void onWorkerNotFullEvent(SelectorWorker worker) {
        this.freeWorkers.addFirst(worker);
    }

    public Executor getChannelExecutor() {
        return this.channelExecutor;
    }

    public void setChannelExecutor(Executor channelExecutor) {
        this.channelExecutor = channelExecutor;
    }

    public int getMaxChannelsPerWorker() {
        return this.maxChannelsPerWorker;
    }

    public void setMaxChannelsPerWorker(int maxChannelsPerWorker) {
        this.maxChannelsPerWorker = maxChannelsPerWorker;
    }

    public Executor getSelectorExecutor() {
        return this.selectorExecutor;
    }

    public void setSelectorExecutor(Executor selectorExecutor) {
        this.selectorExecutor = selectorExecutor;
    }
}
